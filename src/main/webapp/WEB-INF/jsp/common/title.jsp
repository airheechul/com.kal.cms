<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
	<!-- Slide Button -->
	<p class="slide"><a href="#" class="btn-slide"><!-- slide open &amp; close --></a></p>
	<!-- /Slide Button -->


    <!-- 현재 선택한 parentMenuSeq 구하기... -->
	<c:forEach var="menuInfo" items="${sessionScope.LOGIN_SESSION.accessMenuList}">
		<c:if test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, fn:substring(menuInfo.url, 0, fn:indexOf(menuInfo.url,'List.do')))}">
			 <c:set var="selectParentSeq" value="${menuInfo.parentMenuSeq}"/>
		</c:if>	
	</c:forEach>			
	
	<!-- Body-title -->
	<div id="body-title">
	<h1><img src="${pageContext.request.contextPath}/images/blue/tit/title${param.topNum}_${param.leftNum}.gif" alt="h1" /></h1>
		<ul id="location">
			<li class="home"><a href="#" class="dfColor3">Home</a></li>
		<!--  메뉴관리, 메뉴 권한 관리  메뉴는 사용하지 않으므로 하드코딩.... Start  -->
		<c:choose>			
		  <c:when test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, '/content/search') }">			
			<li><a href="#" class="dfColor3">Content Search</a></li>
			<li id="currentMenuName" class="look"><a href="#" class="dfColor3">Search</a></li>
		  </c:when>
		  <c:when test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, '/content/process') }">			
			<li><a href="#" class="dfColor3">Content Process</a></li>
			<li id="currentMenuName" class="look"><a href="#" class="dfColor3">Process</a></li>
		  </c:when>
		  <c:when test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, '/content/download') }">			
			<li><a href="#" class="dfColor3">Content Process</a></li>
			<li id="currentMenuName" class="look"><a href="#" class="dfColor3">다운로드</a></li>
		  </c:when>
		  <c:when test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, '/task/monitoring') }">			
			<li><a href="#" class="dfColor3">Task Monitoring</a></li>
			<li id="currentMenuName" class="look"><a href="#" class="dfColor3">모니터링</a></li>
		  </c:when>

		  <c:when test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, '/system/code') }">
			<li><a href="#" class="dfColor3">시스템관리</a></li>
			<li id="currentMenuName" class="look"><a href="#" class="dfColor3">코드 관리</a></li>
		  </c:when>

 		  <c:otherwise>
		  </c:otherwise>
		</c:choose>
		<!--  메뉴관리, 메뉴 권한 관리  메뉴는 사용하지 않으므로 하드코딩.... End  -->					
			<%--
	<!--  메뉴관리, 메뉴 권한 관리  메뉴를 사용하게 되면 주석 풀어줌...  Start  -->					
    <!-- 현재 선택한 대메뉴 구하기... -->
	<c:forEach var="menuInfo" items="${sessionScope.LOGIN_SESSION.accessMenuList}">
		<c:if test="${selectParentSeq == menuInfo.menuSeq}">
			 <li><a href="#" class="dfColor3">${menuInfo.menuNm}</a></li>
		</c:if>	
	</c:forEach>			
			
	<c:set var="selectBoardCd" value="boardCd=${param.boardCd}"/>
	<c:set var="selectMenuChk" value="false"/>
	
    <!-- 현재 선택한 소메뉴 구하기  -->
     <c:forEach var="menuInfo" items="${sessionScope.LOGIN_SESSION.accessMenuList}">
       <c:if test="${fn:startsWith(sessionScope.LOGIN_SESSION.servletPath, fn:substring(menuInfo.url, 0, fn:indexOf(menuInfo.url,'List.do'))) && !selectMenuChk}">
         <c:if test="${menuInfo.menuSeq != menuInfo.parentMenuSeq && selectParentSeq == menuInfo.parentMenuSeq}">
         <c:choose>
            <c:when test="${!fn:startsWith(menuInfo.url, '/board/boardTmpl') && fn:startsWith(menuInfo.url, '/board/board')}">
			  <c:if test="${fn:endsWith(menuInfo.url, selectBoardCd)}">
		           <li id="currentMenuName" class="look"><a href="#" class="dfColor3">${menuInfo.menuNm}</a></li>
		           <c:set var="selectMenuChk" value="true"/>
			  </c:if>		
            </c:when>
		    <c:otherwise>
		           <li id="currentMenuName" class="look"><a href="#" class="dfColor3">${menuInfo.menuNm}</a></li>
		           <c:set var="selectMenuChk" value="true"/>
		    </c:otherwise>
		 </c:choose>
          </c:if>
       </c:if>
     </c:forEach>
	<!--  메뉴관리, 메뉴 권한 관리  메뉴를 사용하게 되면 주석 풀어줌...  End  -->					
			--%>
			
			
		</ul>
	</div>
