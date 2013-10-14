<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Server Activation</title>
</head>
<body>
<h1>
	Server Activation Page
</h1>
<%
 	response.setHeader("Pragma","No-cache");
 	response.setHeader("Cache-Control","no-cache");
 	response.setDateHeader("Expires", 0);
 	response.flushBuffer();
 	%>
<table>
<tr>
<td>
server
</td>

<td>
status  
</td>
</tr>
<c:forEach var="server" items="${allServers}" >
<tr>
<td>
${server.name}
</td>

<td>
${server.status}
<c:if test="${server.name eq thisServer}">
  			<c:if test="${server.status eq 'disabled'}"><a href="/enable" >Enable</a></c:if> 
		   <c:if test="${server.status eq 'enabled'}"> <a href="/disable" >Disable</a></c:if> 
</c:if>		   
</td>
</tr>
</c:forEach>

</table>
</body>
</html>
