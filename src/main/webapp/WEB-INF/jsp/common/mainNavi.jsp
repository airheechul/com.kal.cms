<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"     uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!-- header start-->	
<script type="text/javascript">
function logout(){
	location.href="${pageContext.request.contextPath}/logout.do";
}
</script>
<div id="header">
 
	<div id="gnb">
		<h1><img src="${pageContext.request.contextPath}/images/blue/bi.png" width="457" height="34" alt="Paperless Content Management System"></h1>
		<div class="logInfo">
			<span>${sessionScope.UserAccount.userLocalName} 님 안녕하세요</span> ${sessionScope.UserAccount.roleCode}
			<a title="로그아웃" href="javascript:logout();">
			<button onclick="logout();"><img src="${pageContext.request.contextPath}/images/blue/btn_signout.png" alt="LOGOUT"></button>
			</a>
		</div>
	</div>

	<ul id="nav">
		<c:choose> 
			<c:when test="${sessionScope.UserAccount.roleCode == 50005}">
				<li <c:if test="${param.topNum == '3'}"> class="visited"</c:if> ><a rel="#4C7C0C" href="${pageContext.request.contextPath}/task/taskList.do?topNum=3" class="nav03">Task Monitoring</a></li>
			</c:when>
			<c:otherwise>
				<li <c:if test="${param.topNum == null or param.topNum == '1'}"> class="visited"</c:if> ><a rel="#4C7C0C" href="${pageContext.request.contextPath}/content/searchList.do?topNum=1" class="nav01">Content Search</a></li>
				<li <c:if test="${param.topNum == '2'}"> class="visited"</c:if> ><a rel="#4C7C0C" href="${pageContext.request.contextPath}/content/processList.do?topNum=2" class="nav02">Content Process</a></li>
				<li <c:if test="${param.topNum == '3'}"> class="visited"</c:if> ><a rel="#4C7C0C" href="${pageContext.request.contextPath}/task/taskList.do?topNum=3" class="nav03">Task Monitoring</a></li>
				<c:if test="${sessionScope.UserAccount.roleCode == 50001 || sessionScope.UserAccount.roleCode == 50002}">
				<li <c:if test="${param.topNum == '4'}"> class="visited"</c:if> ><a rel="#4C7C0C" href="${pageContext.request.contextPath}/system/codeList.do?topNum=4" class="nav04">Setting</a></li>
				</c:if>
			</c:otherwise>
		</c:choose>
		
	</ul>
</div>			
<!-- header end -->


