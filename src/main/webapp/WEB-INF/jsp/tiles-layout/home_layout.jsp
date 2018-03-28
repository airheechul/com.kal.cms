<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>
<%
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("P3P", "CP='CAO PSA CONi OTR OUR DEM ONL'" );
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (request.getProtocol().equals("HTTP/1.1")) response.setHeader("Cache-Control", "no-cache");
    
    Log logger = LogFactory.getLog(getClass());
    
    //SessionAdminInfo sessionAdminInfo = (SessionAdminInfo)SessionHandler.getInstance().getLoginInfo(request);
    //logger.info("[Header SessionInfo] : " + sessionAdminInfo);
    
    logger.info(request.getRequestURI() + " : " + request.getRequestURL());

%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>

    <!-- header: str -->
	<tiles:insertAttribute name="header" flush="false"/>
    <!-- header : end -->
<script>
$(document).ready(function(){
	setContextPath("${pageContext.request.contextPath }");
});
</script>
</head>

<body>


	<!-- bodyWrap start -->
	<div id="bodyWrap">

        <!-- header(main navi) : str -->
		<tiles:insertAttribute name="mainNavi" flush="false"/>
        <!-- header(main navi) : end -->


	
		<!-- content start-->
		<div id="content">
	
	    	<!-- contents : str -->
			<tiles:insertAttribute name="body" flush="false"/>
	        <!-- contents : end -->
	            
		</div>
		<!-- content end  -->
		            
	
		<!-- Footer Start -->
			<tiles:insertAttribute name="footer" flush="false"/>
		<!-- Footer end -->
	
	
	</div>	
	<!-- bodyWrap end -->

</body>
</html>
