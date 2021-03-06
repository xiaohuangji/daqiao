/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2004 Caucho Technology, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Hessian", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.wills.clientproxy;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.*;
import com.caucho.services.server.*;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.WeakHashMap; 
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.entity.ContentProducer;


/**
 * 修改自HessianProxy,用来支持在调用过程中的自动负载均衡
 * @author huangsiping
 *
 */
public class HessianLBProxy implements InvocationHandler, Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4178433512046319789L;

	private static final Logger log = Logger.getLogger(HessianLBProxy.class
			.getName());

	protected HessianLBProxyFactory _factory;

	private WeakHashMap<Method, String> _mangleMap = new WeakHashMap<Method, String>();

	private Class<?> _type; 
	
	private ClusterNodeManager _cm;
	
	//private URL currentURL ;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private ThreadLocal<URL> threadLocal = new ThreadLocal<URL>();

	 
 
	public HessianLBProxy(ClusterNodeManager cm, HessianLBProxyFactory factory, Class<?> type) {
		_factory = factory;
		_cm = cm;
		_type = type;
	}
 

	/**
	 * Handles the object invocation.
	 * 
	 * @param proxy
	 *            the proxy object to invoke
	 * @param method
	 *            the method to call
	 * @param args
	 *            the arguments to the proxy object
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		String mangleName;
		
		HessianClusterNode hcn = _cm.getAvailableNodeByStraitegy();
		if(hcn==null){
			throw new Exception("no available server node found!");
		}
		if (hcn == null||hcn.getNode()==null) {
			throw new Exception("no server available");
		}
		
		threadLocal.set(new URL(hcn.getURL()+this._type.getSimpleName()));
		 
		try{
			lock.readLock().lock();
			mangleName = _mangleMap.get(method);		
		}finally{
			lock.readLock().unlock();
		}
		
		
		if (mangleName == null) {
			String methodName = method.getName();
			Class<?>[] params = method.getParameterTypes(); 
			// equals and hashCode are special cased
			if (methodName.equals("equals") && params.length == 1
					&& params[0].equals(Object.class)) {
				Object value = args[0];
				if (value == null || !Proxy.isProxyClass(value.getClass()))
					return Boolean.FALSE;

				Object proxyHandler = Proxy.getInvocationHandler(value);

				if (!(proxyHandler instanceof HessianLBProxy))
					return Boolean.FALSE;

				HessianLBProxy handler = (HessianLBProxy) proxyHandler;

				return new Boolean(false);
			} else if (methodName.equals("hashCode") && params.length == 0)
				return new Integer(_cm.hashCode());
			else if (methodName.equals("getHessianType"))
				return proxy.getClass().getInterfaces()[0].getName();
			else if (methodName.equals("getHessianURL"))
				return threadLocal.get().toString();
			else if (methodName.equals("toString") && params.length == 0)
				return "HessianProxy[" + threadLocal.get() + "]";

			if (!_factory.isOverloadEnabled())
				mangleName = method.getName();
			else
				mangleName = mangleName(method);

			try{
				lock.writeLock().lock();
				_mangleMap.put(method, mangleName);
			}finally{
				lock.writeLock().unlock();
			}
		} 
		InputStream is = null;
		HessianConnection conn = null;

		try {
			if (log.isLoggable(Level.FINER))
				log.finer("Hessian[" + threadLocal.get() + "] calling " + mangleName); 
			conn = sendRequest(mangleName, args ,threadLocal.get());

			if(conn.getStatusCode()!=200){
				throw new HessianProtocolException("http code is "+conn.getStatusCode());
			}
			 
			is = conn.getInputStream();

			if (log.isLoggable(Level.FINEST)) {
				PrintWriter dbg = new PrintWriter(new LogWriter(log));
				HessianDebugInputStream dIs = new HessianDebugInputStream(is,
						dbg);

				dIs.startTop2();

				is = dIs;
			}
			 
			AbstractHessianInput in;

			int code = is.read();

			if (code == 'H') {
				int major = is.read();
				int minor = is.read();

				in = _factory.getHessian2Input(is);

				Object value = in.readReply(method.getReturnType());

				return value;
			} else if (code == 'r') {
				int major = is.read();
				int minor = is.read();

				in = _factory.getHessianInput(is);

				in.startReplyBody();

				Object value = in.readObject(method.getReturnType());

				if (value instanceof InputStream) {
					value = new ResultInputStream(conn, is, in,
							(InputStream) value);
					is = null;
					conn = null;
				} else
					in.completeReply();

				return value;
			} else
				throw new HessianProtocolException("'" + (char) code
						+ "' is an unknown code");
		} catch (HessianProtocolException e) {
			throw new HessianRuntimeException(e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
				log.log(Level.FINE, e.toString(), e);
			}

			try {
				if (conn != null)
					conn.destroy();
			} catch (Exception e) {
				log.log(Level.FINE, e.toString(), e);
			}
		}
	}

	protected String mangleName(Method method) {
		Class<?>[] param = method.getParameterTypes();

		if (param == null || param.length == 0)
			return method.getName();
		else
			return AbstractSkeleton.mangleName(method, false);
	}

	/**
	 * Sends the HTTP request to the Hessian connection.
	 */
	protected HessianConnection sendRequest(final String methodName,
			final Object[] args , final URL currentUrl) throws IOException {
		HessianConnection conn = null;

		boolean isValid = false;
		try {
			conn = _factory.getConnectionFactory().open(currentUrl);
			addRequestHeaders(conn);

			ContentProducer contentProducer = new ContentProducer() {
				public void writeTo(OutputStream outstream) throws IOException {
					try {
						AbstractHessianOutput out = _factory.getHessianOutput(outstream);

						out.call(methodName, args);
						out.flush();
					} catch (Exception e) {
						throw new HessianRuntimeException(e);
					}
				}
			};

			conn.setContentProducer(contentProducer);
			conn.sendRequest();
			
			isValid = true;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (!isValid && conn != null)
				conn.destroy();
		}
		return conn;
	}

	/**
	 * Method that allows subclasses to add request headers such as cookies.
	 * Default implementation is empty.
	 */
	protected void addRequestHeaders(HessianConnection conn) {
		conn.addHeader("Content-Type", "x-application/hessian");

		String basicAuth = _factory.getBasicAuth();

		if (basicAuth != null)
			conn.addHeader("Authorization", basicAuth);
	}

	/**
	 * Method that allows subclasses to parse response headers such as cookies.
	 * Default implementation is empty.
	 * 
	 * @param conn
	 */
	protected void parseResponseHeaders(URLConnection conn) {
	}

	public Object writeReplace() {
		return new HessianRemote(_type.getName(), threadLocal.get().toString());
	}

	static class ResultInputStream extends InputStream {
		private HessianConnection _conn;
		private InputStream _connIs;
		private AbstractHessianInput _in;
		private InputStream _hessianIs;

		ResultInputStream(HessianConnection conn, InputStream is,
				AbstractHessianInput in, InputStream hessianIs) {
			_conn = conn;
			_connIs = is;
			_in = in;
			_hessianIs = hessianIs;
		}

		public int read() throws IOException {
			if (_hessianIs != null) {
				int value = _hessianIs.read();

				if (value < 0)
					close();

				return value;
			} else
				return -1;
		}

		public int read(byte[] buffer, int offset, int length)
				throws IOException {
			if (_hessianIs != null) {
				int value = _hessianIs.read(buffer, offset, length);

				if (value < 0)
					close();

				return value;
			} else
				return -1;
		}

		public void close() throws IOException {
			HessianConnection conn = _conn;
			_conn = null;

			InputStream connIs = _connIs;
			_connIs = null;

			AbstractHessianInput in = _in;
			_in = null;

			InputStream hessianIs = _hessianIs;
			_hessianIs = null;

			try {
				if (hessianIs != null)
					hessianIs.close();
			} catch (Exception e) {
				log.log(Level.FINE, e.toString(), e);
			}

			try {
				if (in != null) {
					in.completeReply();
					in.close();
				}
			} catch (Exception e) {
				log.log(Level.FINE, e.toString(), e);
			}

			try {
				if (connIs != null) {
					connIs.close();
				}
			} catch (Exception e) {
				log.log(Level.FINE, e.toString(), e);
			}

			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				log.log(Level.FINE, e.toString(), e);
			}
		}
	}

	static class LogWriter extends Writer {
		private Logger _log;
		private Level _level = Level.FINEST;
		private StringBuilder _sb = new StringBuilder();

		LogWriter(Logger log) {
			_log = log;
		}

		public void write(char ch) {
			if (ch == '\n' && _sb.length() > 0) {
				_log.fine(_sb.toString());
				_sb.setLength(0);
			} else
				_sb.append((char) ch);
		}

		public void write(char[] buffer, int offset, int length) {
			for (int i = 0; i < length; i++) {
				char ch = buffer[offset + i];

				if (ch == '\n' && _sb.length() > 0) {
					_log.log(_level, _sb.toString());
					_sb.setLength(0);
				} else
					_sb.append((char) ch);
			}
		}

		public void flush() {
		}

		public void close() {
			if (_sb.length() > 0)
				_log.log(_level, _sb.toString());
		}
	}
}
