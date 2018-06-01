<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.kal.cms.common.constants.AdminConstants"%>

<script type="text/javascript">

	$(document).ready(function() {
		$("#btnCancle").click(function(){
			location.href = "${pageContext.request.contextPath}/system/templateList.do?topNum=4";
		});
		
	    $('#btnRegist').click(function(e) {
			if(formCheck()){
				
				document.uploadForm.action="${pageContext.request.contextPath}/system/templateUpdate.do?topNum=4";
				uploadForm.submit();
				window.returnValue = true;
			}
			
			return false;
	    });
	});	// end of ready	
	
	function formCheck(){
		var fo = document.uploadForm;

		var result = true;
		var falseForm;
		if(result && fo.masterWOCntsTmptTypCd.value == ""){
			result = false;
			falseForm = fo.masterWOCntsTmptTypCd;
		}
		if(result && fo.templateName.value == ""){
			result = false;
			falseForm = fo.templateName;
		}
		
		if(!result){
			alert("필수 입력 항목을 확인하세요.");
			falseForm.focus();
			
			return false;
		}
		
		return true;
	}
	
	function goList(){
		location.href = "${pageContext.request.contextPath}/system/templateList.do?topNum=4";
	}
	
	function downloadFile(data1, data2){
		if(confirm("이 파일을 열거나 저장하시겠습니까?\n이름: "+data1+"\n유형: xml ")) {
			document.download.action = "${pageContext.request.contextPath}/system/templateFile.do";
			document.download.fileOrgName.value = data1;
			document.download.fileFullPath.value = data2;
			document.download.target = "resultFrame";
			document.download.submit();
		}
	}
</script>
<div id="lnb">
	<ul>
		<li><a
			href="${pageContext.request.contextPath}/system/codeList.do?topNum=4">코드관리</a>
			<span>&#8250;</span></li>
		<li><a
			href="${pageContext.request.contextPath}/system/templateList.do?topNum=4">Template
				관리</a> <span>&#8250;</span></li>
		<li class="visited"><a
			href="${pageContext.request.contextPath}/admin/adminList.do?topNum=4">관리자
				계정 관리</a> <span>&#8250;</span></li>
	</ul>
</div>
<div id="contentBody">

	<h3>
		<img
			src="${pageContext.request.contextPath}/html/images/${skin}/tit_manageTemplate.png"
			alt="Template 관리">
	</h3>
	<form name="uploadForm" method="post" enctype="multipart/form-data"
		method="post" action="" target="resultFrame">
		<input type="hidden" name="masterWOCntsTmptId"
			value="${templateInfo.masterWOCntsTmptId}" />
		<table class="tbl_type_b">
			<colgroup>
				<col width="15%">
				<col width="">
			</colgroup>
			<tr>
				<th>Content Type</th>
				<td><select id="masterWOCntsTmptTypCd"
					name="masterWOCntsTmptTypCd">
						<option value="" disabled selected hidden>----선택----</option>
						<c:forEach var="contentTypeList" items="${contentTypeList}">
							<option value='${contentTypeList.lookupCode}'
								<c:if test="${templateInfo.masterWOCntsTmptTypCd == contentTypeList.lookupCode}">selected</c:if>>${contentTypeList.lookupName}</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<th>Template Type</th>
				<td><select name="templateName">
						<option value="">- 선택 -</option>
						<option value="Aircraft"
							<c:if test="${templateInfo.templateName == 'Aircraft'}">selected='selected'</c:if>>Aircraft</option>
						<option value="Engine"
							<c:if test="${templateInfo.templateName == 'Engine'}">selected='selected'</c:if>>Engine</option>
						<option value="Component"
							<c:if test="${templateInfo.templateName == 'Component'}">selected='selected'</c:if>>Component</option>
				</select></td>
			</tr>
			<tr>
				<th>사용여부</th>
				<td><input type="radio" name="useYn" id="radio" value="Y"
					<c:if test="${templateInfo.useYn == 'Y'}">checked='checked'</c:if>>
					<label for="radio">예</label> <input type="radio" name="useYn"
					id="radio2" value="N"
					<c:if test="${templateInfo.useYn == 'N'}">checked='checked'</c:if>>
					<label for="radio2">아니오</label></td>
			</tr>
			<tr>
				<th>File</th>
				<td><a
					href="javascript:downloadFile('${templateInfo.fileName}','${templateInfo.urlAddress}');">${templateInfo.fileName}</a></td>
			</tr>
			<tr>
				<th>Upload File</th>
				<td><input type="file" id="upFile" name="upFile" class="file" /></td>
			</tr>
		</table>
	</form>
	<div class="under_tbl_area">
		<button class="btn_type_a" id="btnRegist">
			수정<span></span>
		</button>
		<button class="btn_type_a" id="btnCancle">
			취소<span></span>
		</button>
	</div>

</div>
<form id="download" name="download" method="post" target="downloadFrame">
	<input type="hidden" name="fileFullPath" value="" /> <input
		type="hidden" name="fileName" value="" /> <input type="hidden"
		name="fileOrgName" value="" />
</form>
<iframe width="0" height="0" frameborder="0" marginwidth="0"
	marginheight="0" name="resultFrame"></iframe>