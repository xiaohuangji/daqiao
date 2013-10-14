package com.caucho.hessian.client;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory; 
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;

public class HessianHttpClient {
	 
	
	private HttpClient httpClient;

	private static HessianHttpClient instance  = new HessianHttpClient();

	private static final Logger log = Logger
			.getLogger(HessianHttpClient.class.getName());

	private HessianHttpClient() {
		SchemeRegistry schemeRegistry = new SchemeRegistry();  
		schemeRegistry.register(new Scheme("http", 8080, PlainSocketFactory.getSocketFactory()));  
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
  
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		cm.getMaxTotal(); 
		// Increase max total connection to 2048*4 
		cm.setMaxTotal(2048*4);  
		// Increase default max connection per route to 2048  
		cm.setDefaultMaxPerRoute(2048);     
		
		httpClient = new DefaultHttpClient(cm);
		 
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
	}

	public static HessianHttpClient getInstance() {

		if (instance == null) {
			instance = new HessianHttpClient();
		}

		return instance;
	}

	public HttpResponse execute(HttpUriRequest request) { 
		HttpResponse response = null ;
		try {
			response =  httpClient.execute(request) ;
		} catch (ClientProtocolException e) {
			request.abort(); 
			log.info(e.getMessage());
		} catch (IOException e) {
			request.abort(); 
			log.info(e.getMessage());
		}
		return response;

	}

	public void setParameter(Map<String, Object> paras) {
		for (String key : paras.keySet()) {
			Object v = paras.get(key);
			if (v instanceof Integer) {
				httpClient.getParams().setIntParameter(key, (Integer) v);
			} else if (v instanceof Boolean) {
				httpClient.getParams().setBooleanParameter(key, (Boolean) v);
			}
		}

	}

	protected void finalize() { 
			httpClient.getConnectionManager().shutdown();
		 
	}
 
}
