package com.tg.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CONFIGUtil {
       
    //------------------
    private  Properties tgProperties = new Properties();

    private static final String PROPERTY_PATH = "/config.properties";

    private Logger logger = Logger.getLogger(CONFIGUtil.class.getName());
    
    private static CONFIGUtil configUtil=null;

    private CONFIGUtil() {
        try {
        	tgProperties.load(this.getClass().getResourceAsStream(PROPERTY_PATH)); 
        
        } catch ( Exception e ) {
            logger.log(Level.SEVERE, "Unable to load configuration: " + e.getMessage(), e);
        }
    }

    public static final CONFIGUtil getInstance () {
    	if(configUtil==null)
    		configUtil=new CONFIGUtil();
        return configUtil;
    }

    
    public  String getConfig (String propertyName) {
    	
        return tgProperties.getProperty(propertyName);
    }
    
    public static void main(String[] args) {
		System.out.println(CONFIGUtil.getInstance().getConfig("zk_host"));
	}
}
