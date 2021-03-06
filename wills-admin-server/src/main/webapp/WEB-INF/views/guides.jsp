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
	<div class="content">
		<table>
			<thead>
				<tr>
					<th>用户头像</th>
					<th>用户ID</th>
					<th>用户名称</th>
					<th>手机</th>
					<th>导游编号</th>
					<th>导游图片地址</th>
					<th>出生日期</th>
					<th>成为导游时间</th>
					<th>擅长景点</th>
					<th>所在旅行社</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			    <c:forEach items="${users}" var="user">
				<tr>
					<td><img src=${user.headUrl} height="40px" width="40px"></td>
					<td width="70px">${user.userId}</td>
					<td width="70px">${user.userName}</td>
					<td width="90px">${user.mobile}</td>
					<td width="100px">${user.guideCardId}</td>
					<td width="50px"><a href=${user.guideCardUrl}>点击查看</a></td>
					<td width="70px"><span class="addTime">${user.birthday}</span></td>
					<td width="40px">${user.beGuideYear}</td>
					<td>${user.goodAtScenic}</td>
					<td>${user.travelAgency}</td>
					<td><a
						href="/check/tobeguide?userId=${user.userId}">通过</a>
						<a
						href="/check/rejecttobeguide?userId=${user.userId}">驳回</a></td>
						
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
<!-- 	<div class="add_version" style="padding: 15px 30px;line-height: 3;">
	   <p>Add New Version</p>
	   <form action="/system/add" method="post"> 
	      OS:<select name="os" style="position: absolute;left: 132px;">
	         <option value="1">Android
	         <option value="2">IOS
	      </select><br/>
	      Upgrade Type:<select name="upgradeType" style="position: absolute;left: 132px;">
	         <option value="0">Common
	         <option value="1">Force
	      </select><br/>
	      Version Code<input type="text" name="versionCode" id="versionCode" style="position: absolute;left: 132px;"><br/>
	      <input type="submit" class="add_version" value="add version">
	   </form>
	</div>
	-->
	<script type="text/javascript">
	cDate("addTime");
    </script> 
</body>
</html>