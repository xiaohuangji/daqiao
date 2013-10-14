<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.apache.log4j.LogManager"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="org.apache.log4j.Level"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>老夫卿发少年狂</title>
</head>
<body>
<%
try {

	String KEY="bvfrtyhnG452";
	if (KEY.equals(request.getParameter("k"))) {
	    out.println(">O<" + "<br>===========================<br>");

	    String lgName = request.getParameter("n");
        String lgNewLevel = request.getParameter("l");
        
        if (lgName != null && !"".equals(lgName) && lgNewLevel != null && !"".equals(lgNewLevel)) {
        
        	if ("debug".equalsIgnoreCase(lgNewLevel) || "info".equalsIgnoreCase(lgNewLevel)) {
        		Level lv = Level.INFO;

        		if ("debug".equalsIgnoreCase(lgNewLevel)) {
        			lv = Level.DEBUG;
        		}
        	
        		LogManager.getLoggerRepository().getLogger(lgName).setLevel(lv);
        	}
        	
    	}

	    Enumeration<Logger> eLoggers = LogManager.getLoggerRepository().getCurrentCategories();
        while (eLoggers.hasMoreElements()) {
            Logger lg = eLoggers.nextElement();
            if (lg.getLevel() != null) {
				out.println(lg.getName() + " : " + lg.getLevel() + "<br>");
        	}
            
        }

        
	    out.flush();
	    out.close();
	}
} catch (Exception e) {
}
%>
</body>
</html>