package com.caucho.hessian.client;

import java.io.InputStream;
import java.io.OutputStream;

import javax.naming.spi.ObjectFactory;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.services.client.ServiceProxyFactory;

public interface HessianProxyFactory  extends ServiceProxyFactory, ObjectFactory  {

	long getConnectTimeout();

	long getReadTimeout();

	String getBasicAuth();

	AbstractHessianOutput getHessianOutput(OutputStream outstream);

	HessianConnectionFactory getConnectionFactory();

	boolean isOverloadEnabled();

	AbstractHessianInput getHessianInput(InputStream is);

	AbstractHessianInput getHessian2Input(InputStream is);

}
