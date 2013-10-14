package com.caucho.hessian.client;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.methods.HttpPost;

public class HessianHttpConnectionFactory implements HessianConnectionFactory{
	private static final Logger log
	    = Logger.getLogger(HessianHttpConnectionFactory.class.getName());
	
	private HessianProxyFactory _proxyFactory;
	 
	
	public HessianHttpConnectionFactory( ) {
       
	}
	

	

	@Override
	public void setHessianProxyFactory(HessianProxyFactory factory) {
		_proxyFactory = factory;
		
	}

	@Override
	public HessianConnection open(URL url) throws IOException {
	    if (log.isLoggable(Level.FINER))
	        log.finer(this + " open(" + url + ")");
	    
	    HttpPost  postRequest  = new HttpPost(url.toString()); 
        HessianHttpConnection nc = new HessianHttpConnection(postRequest);
        
		return nc;
	}
	 

}
