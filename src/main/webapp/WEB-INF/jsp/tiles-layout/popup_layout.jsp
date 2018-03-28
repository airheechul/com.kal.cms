<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib uri = "http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<%response.setHeader("P3P", "CP='CAO PSA CONi OTR OUR DEM ONL'" ); %>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="p3p" content='CP="CAO DSP AND SO" policyref="/w3c/p3p.xml"' />

    <!-- header: str -->
	<tiles:insertAttribute name="header" flush="false"/>
    <!-- header : end -->
<script>
$(document).ready(function(){
	setContextPath("${pageContext.request.contextPath }");
});
</script>
</head>

<body class="alertbody">
    <!-- body : str -->
	<tiles:insertAttribute name="body" flush="false"/>
    <!-- body : end -->
</body>
</html>