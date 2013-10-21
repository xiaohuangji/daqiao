<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${empty pageSize}" >
	<c:set var="pageSize" value="10" />
</c:if>
<c:if test="${empty navUrlEnd}" >
	<c:set var="navUrlEnd" value="" />
</c:if>
<c:choose>
	<c:when test="${empty requestScope.page}">
		<c:set var="page" value="1"/>
	</c:when>
	<c:otherwise>
		<c:set var="page" value="${requestScope.page}"/> 
	</c:otherwise>
</c:choose>
<c:set var="count" value="${count}"/>
<c:set var="numberMod" value="${count % pageSize}"/>
<c:choose>
	<c:when test="${numberMod==0}">
		<fmt:parseNumber value="${count/pageSize}" integerOnly="true" var="tolNum" parseLocale="zh_CN"/>
	</c:when>
	<c:otherwise>
		<fmt:parseNumber value="${count/pageSize + 1}" integerOnly="true" var="tolNum" parseLocale="zh_CN"/>
	</c:otherwise>
</c:choose>
<c:if test="${page > 0}">
	<c:if test="${page>1}">
	    <a href="<c:out value="${navUrl}"/>&page=1">首页</a>
		<a href="<c:out value="${navUrl}"/>&page=${page-1}">上一页</a>
	</c:if>
	<span><a class="m">${page}/${tolNum}</a></span>
	<c:if test="${page < tolNum}">

		<a href="<c:out value="${navUrl}"/>&page=${page+1}${navUrlEnd}${ssid}${requestScope.sos}${requestScope.sosVersion}${requestScope.stouch}${requestScope.sappId}${requestScope.type}">下一页</a>
		<a href="<c:out value="${navUrl}"/>&page=${tolNum}${navUrlEnd}${ssid}${requestScope.sos}${requestScope.sosVersion}${requestScope.stouch}${requestScope.sappId}${requestScope.type}">末页</a>
	</c:if>
</c:if> 