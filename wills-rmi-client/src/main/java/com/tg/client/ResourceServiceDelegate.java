package com.tg.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tg.service.ResourceService;
import com.wills.clientproxy.ClusterServiceRegistry;
import com.wills.clientproxy.HessianDelegateFactory;

public class ResourceServiceDelegate implements ResourceService {

	private ResourceService resourceServiceDelegate;
	
	public ResourceServiceDelegate(){
		resourceServiceDelegate = HessianDelegateFactory.getInstance().retrieveService(new ClusterServiceRegistry(Constants.ClUSTER_PREFIX), ResourceService.class);

	}
	@Override
	public String uploadResource(byte[] b, String suffix) {
		// TODO Auto-generated method stub
		return resourceServiceDelegate.uploadResource(b,suffix);
	}
	
	private static  byte[] InputStreamToByte(InputStream is) throws IOException {   
	    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();   
	   int ch;   
	   while ((ch = is.read()) != -1) {   
	     bytestream.write(ch);   
	    }   
	   byte imgdata[] = bytestream.toByteArray();   
	    bytestream.close();   
	   return imgdata;   
	   } 
	
	public static void main(String[] args) throws IOException {
		ResourceServiceDelegate rdg=new ResourceServiceDelegate();
		File file=new File("/Users/renren/Downloads/20130909142945.jpg");
		  
		rdg.uploadResource(InputStreamToByte(new FileInputStream(file)), "jpg");
	}

}
