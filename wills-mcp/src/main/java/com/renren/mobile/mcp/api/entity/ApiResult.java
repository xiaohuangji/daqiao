/**
 * $Id: ApiResult.java 2880 2013-01-21 07:23:49Z wei.cheng3 $
 * Copyright 2009-2012 Oak Pacific Interactive. All rights reserved.
 */
package com.renren.mobile.mcp.api.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author sunji
 * 
 */
public class ApiResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private Object data;

    public ApiResult(int resultCode, Object data) {
        super();
        this.code = resultCode;
        if(data==null)
        	this.data=new ArrayList();
        else
        	this.data = data;
    }

    public ApiResult(int resultCode) {
        super();
        this.code = resultCode;
        this.data = "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult [code=" + code + ", data=" + data + "]";
    }
    
    public static void main(String[] args) {
		ApiResult apiResult=new ApiResult(0, null);
		System.out.println(apiResult.toString());
	}
}
