<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>                                                                                                                                          
                                                                                                                       

<script type="text/javascript">
$(document).ready(function() {
	$("#btnCancel").click(function(){
		location.href = "${pageContext.request.contextPath}/admin/adminList.do?topNum=4";
		
		return false;
	});
	
	$("#btnModify").click(function(){
		adminModify();
		
		return false;
	});
	
	$("#btnDelete").click(function(){
		adminDelete();
		
		return false;
	});
});	

function adminModify(){
	document.adminForm.action = "${pageContext.request.contextPath}/admin/adminModify.do";
	document.adminForm.submit();
}


function adminDelete(){
	message = "대상을 삭제 하시겠습니까?";
	
	if(confirm(message) == true){
		
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/admin/adminDelete.do",
			cache : false, 
			data : $("#adminForm").serialize(),
			dataType: "json",
			success : function(data){
	
				if(data.success == true) {
					alert("삭제되었습니다.");
					
					location.href = "${pageContext.request.contextPath}/admin/adminList.do?topNum=4";
				} else {
					alert("정보 수정이 실패하였습니다.");
				}
			},
			error: function(data){
				alert("에러가 발생하였습니다.");
			}
		});
	}
}
</script>
		<div id="lnb">
			<ul>
				<li><a href="${pageContext.request.contextPath}/system/codeList.do?topNum=4">코드관리</a> <span>&#8250;</span></li>
				<li><a href="${pageContext.request.contextPath}/system/templateList.do?topNum=4">Template 관리</a> <span>&#8250;</span></li>
				<li class="visited"><a href="${pageContext.request.contextPath}/admin/adminList.do?topNum=4">관리자 계정 관리</a> <span>&#8250;</span></li>
			</ul>
		</div>
		<div id="contentBody">
		
			<h3><img src="${pageContext.request.contextPath}/images/blue/tit_manageAdmin.png" alt="관리자 계정 관리"></h3>
			<form id="adminForm" name="adminForm" method="post"> 
			<input type="hidden" name="userId" value="${adminInfo.userId}" />
			<input type="hidden" name="topNum" value="${param.topNum}"/>
			<table class="tbl_type_b">
				<colgroup>
					<col width="15%">
					<col width="">
				</colgroup>
				<tr>
					<th>이름</th>
					<td>${adminInfo.userLocalName}</td>
				</tr>
				<tr>
					<th>사번</th>
					<td>${adminInfo.userId}</td>
				</tr>
				<tr>
					<th>권한</th>
					<td>
						<c:forEach var="roleInfo" items="${roleList}">
							<c:if test="${adminInfo.roleCode == roleInfo.lookupCode}">${roleInfo.lookupName}</c:if>
						</c:forEach>
					</td>
				</tr>
			</table>
			</form>
		<div class="under_tbl_area">
			<button class="btn_type_a" id="btnModify">수정<span></span></button>
			<button class="btn_type_a" id="btnDelete">삭제<span></span></button>
			<button class="btn_type_a" id="btnCancel">취소<span></span></button>
		</div>
		<div class="space"></div>
	</div>