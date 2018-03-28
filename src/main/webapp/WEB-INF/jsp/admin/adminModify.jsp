<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnCancle").click(function(){
			location.href = "${pageContext.request.contextPath}/admin/adminList.do?topNum=4";
			
			return false;
		});
		
		$("#btnUpdate").click(function(){
			if(formCheck()){
				updateAdmin();
			}
			
			return false;
		});
	});	
	
	function updateAdmin(){
		
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/admin/adminUpdate.do",
			cache : false, 
			data : $("#adminForm").serialize(),
			//dataType: "json",
			success : function(data){
	            var data0 = data[0];
	
				if(data0 != 'undefined' && data0 != '' && data0 >= 1) {
					alert("수정되었습니다.");
					
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
	
	function formCheck(){
		var fo = document.adminForm;
	
		var result = true;
		var falseForm;
		if(result && fo.userLocalName.value == ""){
			result = false;
			falseForm = fo.userLocalName;
		}
		if(result && fo.userId.value == ""){
			result = false;
			falseForm = fo.userId;
		}
		if(result && fo.roleCode.value == ""){
			result = false;
			falseForm = fo.roleCode;
		}
		
		if(!result){
			alert("필수 입력 항목을 확인하세요.");
			falseForm.focus();
			
			return false;
		}
	
		return true;
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
		
			<h3><img src="/images/blue/tit_manageAdmin.png" alt="관리자 계정 관리"></h3>
			<form id="adminForm" name="adminForm" method="post"> 
			<table class="tbl_type_b">
				<colgroup>
					<col width="15%">
					<col width="">
				</colgroup>
				<tr>
					<th>이름</th>
					<td><input name="userLocalName" type="text" class="w80p" id="textfield" value="${adminInfo.userLocalName}"></td>
				</tr>
				<tr>
					<th>아이디</th>
					<td><input name="userName" type="text" class="w80p" id="textfield2" value="${adminInfo.userName}"></td>
				</tr>
				<tr>
					<th>내부아이디</th>
					<td><input name="userId" type="text" class="w80p" id="textfield2" value="${adminInfo.userId}" readonly="readonly"></td>
				</tr>
				<tr>
					<th>권한</th>
					<td>
						<select id="roleCode" name="roleCode">
							<option value="" disabled selected hidden> ----선택---- </option>
							<c:forEach var="roleInfo" items="${roleList}">
								<option value='${roleInfo.lookupCode}' <c:if test="${adminInfo.roleCode == roleInfo.lookupCode}">selected</c:if>>${roleInfo.lookupName}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
			</form>
			<div class="under_tbl_area">
				<button class="btn_type_a" id="btnUpdate">수정<span></span></button>
				<button class="btn_type_a" id="btnCancle">취소<span></span></button>
			</div>
		<div class="space"></div>
	</div>