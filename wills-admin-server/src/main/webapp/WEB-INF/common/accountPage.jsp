<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty pageSize}" >
	<c:set var="pageSize" value="10" />
</c:if>
<c:if test="${empty navUrlEnd}" >
	<c:set var="navUrlEnd" value="" />
</c:if>
<c:choose>
	<c:when test="${empty requestScope.pageNo}">
		<c:set var="pageNo" value="1"/>
	</c:when>
	<c:otherwise>
		<c:set var="pageNo" value="${requestScope.pageNo}"/> 
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
<c:if test="${pageNo > 0}">
	<c:if test="${pageNo>1}">
	    <a href="<c:out value="${navUrl}"/>&pageNo=1">first</a>
		<a href="<c:out value="${navUrl}"/>&pageNo=${pageNo-1}">pre</a>
	</c:if>
	<span><a class="m">${pageNo}/${tolNum}</a></span>
	<c:if test="${pageNo < tolNum}">

		<a href="<c:out value="${navUrl}"/>&pageNo=${pageNo+1}">next</a>
		<a href="<c:out value="${navUrl}"/>&pageNo=${tolNum}">last</a>
	</c:if>
</c:if> 