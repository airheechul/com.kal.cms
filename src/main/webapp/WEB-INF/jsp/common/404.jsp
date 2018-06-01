<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1")){
		response.addHeader("Cache-Control","no-cache");
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Korean Air CMS 404</title>
</head>

<body>
	<table width="100%" height="100%">
	<tr>
		<td align="center" valign="middle" height="100%">
			<img src="${pageContext.request.contextPath}/images/common/contentbox/tit_error.gif" alt="페이지 오류가 발생했습니다. 사용 중 불편을 드려 죄송합니다." />
		</td>
	</tr>
	</table>
</body>
</html>
