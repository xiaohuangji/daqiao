<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<title>TG admin</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" href="../../css/index.css">
</head>
<body>
<!-- topbar -->
<%@include file="/WEB-INF/views/top_bar.jsp" %>
<!-- left_menu -->
<div class='l_nav'>
	<ul>
		<li><a href='#' hrefd="/check/getallguide?pageNo=1">全部导游</a></li>
		<li><a href='#' hrefd="/check/getapply">待审批导游</a></li>
		<li><a href='#' hrefd="/mobile/getmobile">管理员手机号</a></li>
	</ul>
</div>
<!-- right_iframe -->
<div class="r_ifra">
	<iframe src="/check/getapply" width="100%" height="100%" scrolling="yes" frameborder="0"></iframe>
</div>
<!-- js -->
<script type='text/javascript' src='../../js/jquery-1.8.3.min.js'></script>
<script type='text/javascript' src='../../js/base.js'></script>
</body>
</html>
