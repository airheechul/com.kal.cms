<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<table width="100%" height="100%">
<tr>
	<td align="center" valign="middle" height="100%">
		<img src="/images/common/contentbox/tit_error.gif" alt="페이지 오류가 발생했습니다. 사용 중 불편을 드려 죄송합니다." />
		<br/>
		<br/>
		<br/>
		<c:if test="${not empty exception.errorCode && exception.errorCode != 'MPFRAME_1000003'}">
			<spring:message code="error.code.${exception.errorCode}" />
		</c:if>
		<br/>
		<br/>
		<br/> <br/>
	</td>
</tr>
</table>

<div id="message" style="display:none;">
	exception : ${exception.message}
	<br/>
</div>