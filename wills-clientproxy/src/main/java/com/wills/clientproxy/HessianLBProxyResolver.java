package com.wills.clientproxy;

import com.caucho.hessian.io.HessianRemoteResolver;

import java.io.IOException;


/**
 * @author huangsiping
 *
 */
public class HessianLBProxyResolver implements HessianRemoteResolver {
	private HessianLBProxyFactory _factory;

	public HessianLBProxyResolver(HessianLBProxyFactory factory) {
		_factory = factory;
	}

	public Object lookup(String type, String url) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try {
			Class api = Class.forName(type, false, loader);

			return _factory.create(api, url);
		} catch (Exception e) {
			throw new IOException(String.valueOf(e));
		}
	}
}
