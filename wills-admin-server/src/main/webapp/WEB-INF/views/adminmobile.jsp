<%@ page contentType="text/html; charset=utf-8"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>All Accounts</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="Keywords" content="" />

<link rel="stylesheet" type="text/css" media="all"
	href="../../../css/c.css" />
<script src="/js/jquery-1.8.3.min.js"></script>
<script src="/js/j.js"></script>
</head>
<body>
          当前管理员手机号: ${mobile}
	
	<div class="setmobile" style="padding: 15px 30px;line-height: 3;">
	   <!-- <p>设置管理员手机号</p> -->
	   <form action="/mobile/setmobile" method="post"> 
	      设置管理员手机号<input type="text" name="mobile" id="mobile" style="position: absolute;left: 132px;"><br/>
	      <input type="submit" class="setmobile" value="设置">
	   </form>
	</div>
</body>
</html>