package com.caucho.hessian.client;

 
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;

public class HessianHttpConnection implements HessianConnection {
	private static final Logger log = Logger
			.getLogger(HessianHttpConnection.class.getName());

	private HttpPost httpPost; 

	private int _statusCode; 

	private InputStream _inputStream;

 
	
	private  ContentProducer contentProducer ;
	

	public HessianHttpConnection(HttpPost httpPostRequest) {
		this.httpPost = httpPostRequest;
	}

	@Override
	public void addHeader(String key, String value) {
		this.httpPost.addHeader(key, value);

	}
 
	

	public ContentProducer getContentProducer() {
		return contentProducer;
	}

	public void setContentProducer(ContentProducer contentProducer) {
		this.contentProducer = contentProducer;
	}

	@Override
	public void sendRequest() throws IOException {  
			HttpEntity entity = new EntityTemplate(contentProducer);
			httpPost.setEntity(entity);
	//	Future<HttpResponse> response = HessianHttpClient.getInstance().execute(httpPost);
			HttpResponse  response = HessianHttpClient.getInstance().execute(httpPost);
		try {
			
			
			if(response.getStatusLine().getStatusCode()>-1){
				this._statusCode = response.getStatusLine().getStatusCode();
				
				if(_statusCode!=200){
					return;
				}
				HttpEntity responseEntity = response.getEntity();
				_inputStream = responseEntity.getContent();
			} 
			
			 
		}  
		catch (RuntimeException ex) {
            // In case of an unexpected exception you may want to abort
            // the HTTP request in order to shut down the underlying
            // connection immediately.
			httpPost.abort();
            throw ex;
            }
		
		
	}

	public InputStream get_inputStream() {
		return _inputStream;
	}

	public void set_inputStream(InputStream _inputStream) {
		this._inputStream = _inputStream;
	}
	
	@Override
	public int getStatusCode() {
		// TODO Auto-generated method stub
		return _statusCode;
	}

	@Override
	public String getStatusMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return this._inputStream;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws IOException { 
//		if(httpPost!=null)
//		httpPost.releaseConnection();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
