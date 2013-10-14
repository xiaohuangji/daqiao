package com.renren.intl.soundsns.utils;

import java.util.Iterator;
import java.util.Stack;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author huangsiping
 * 
 */
public class MethodTimeAdvice implements MethodInterceptor {
	protected final Log log = LogFactory.getLog(MethodTimeAdvice.class);

	private static ThreadLocal<Stack<Msg>> threadLocal = new ThreadLocal<Stack<Msg>>();

	/**
	 * 拦截要执行的目标方法
	 */
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Stack<Msg> stack = threadLocal.get();
		if (stack == null) {
			stack = new Stack<Msg>();

			threadLocal.set(stack);
		}
 
		Class[] params = invocation.getMethod().getParameterTypes();
		String[] simpleParams = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			simpleParams[i] = params[i].getSimpleName();
		}

		Msg m = new Msg(invocation.getThis().getClass().getSimpleName() + "."
				+ invocation.getMethod().getName() + "("
				+ StringUtils.join(simpleParams, ",") + ") ");
		stack.add(m);
 
		StopWatch clock = new StopWatch();
		clock.start(); 
		Object result = invocation.proceed();
		clock.stop(); 

		m.setExcutionTime(clock.getTime());

		if (stack.firstElement().equals(m)) {
			Iterator<Msg> it = stack.iterator();
			StringBuffer sb = new StringBuffer();
			while (it.hasNext()) {
				Msg log = it.next();
				sb.append(
						log.getMethodName() + "takes:" + log.getExcutionTime()
								+ "ms ").append("\n\t");
			}

			log.debug(sb.toString());
			stack.clear();
		}
		return result;
	}

	class Msg {
		private String methodName;
		private long excutionTime;

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public long getExcutionTime() {
			return excutionTime;
		}

		public void setExcutionTime(long excutionTime) {
			this.excutionTime = excutionTime;
		}

		public Msg(String methodName) {
			super();
			this.methodName = methodName;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((methodName == null) ? 0 : methodName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Msg other = (Msg) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (methodName == null) {
				if (other.methodName != null)
					return false;
			} else if (!methodName.equals(other.methodName))
				return false;
			return true;
		}

		private MethodTimeAdvice getOuterType() {
			return MethodTimeAdvice.this;
		}

	}

}