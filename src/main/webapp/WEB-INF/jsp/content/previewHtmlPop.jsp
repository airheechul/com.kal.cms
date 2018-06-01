<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style type="text/css">
</style>
<script type="text/javascript">
$(document).ready(function() {
	openPreview();
});

function openPreview() {
	var previewUrl = "${previewBaseUrl}${previewUrl}";
	previewUrl = previewUrl+"?model=${param.maintDocModelCode}&manualType=${param.WOCntsDocSubtypCd}&xmlPath=${param.urlAddress}";
	previewUrl = encodeURI(previewUrl);
	
	$("#previewFrame").attr("src", previewUrl);
	
}
</script>
<!-- contents : str -->
<iframe id="previewFrame" width="1024" height="768" scrolling="no" frameborder="0" marginwidth="0" marginheight="0" name="previewFrame"></iframe>
<!-- contents : end -->