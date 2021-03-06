<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">
	$(document).ready(function(){
		$('#WOCntsDocSubtypCd').children("[value='TSM'], [value='FRMFIM']").remove();
		$('#masterWOCntsTypCd').children("[value='IMAGE']").remove();
		
	    $("#btnConfirm").click(function(){
			if(formCheck()){
				document.uploadForm.action="${pageContext.request.contextPath}/content/processUpload.do";
				uploadForm.submit();
				window.returnValue = true;
			}
			
			return false;
	    });
	    
	    $("#btnCancle").click(function(){
	    	self.close();
	    	return false;
	    });
	    
	    $('#WOCntsDocSubtypCd').change(function(){
	    	var value = $("#WOCntsDocSubtypCd option:selected").val();
	    	$('#masterWOCntsTypCd').children("[value='IMAGE']").remove();
	    	if(value != "AMM"){
	    		$('#masterWOCntsTypCd').append('<option value="IMAGE">Image</option>');
	    	}
	    });
	    
	    $('#maintDocModelCode').change(function(){
	    	toggleContenTypeOptionOP();
	    	
	    	manualTypeOption();
	    	
	    	return false;
	    });	    
	});

	function toggleContenTypeOptionOP(){
    	var value = $("#maintDocModelCode option:selected").attr("class");
    	
    	$('#masterWOCntsTypCd').children("[value='OP']").remove();
    	if("13001" != value){
    		$("select[name=masterWOCntsTypCd] option[value=RT]").before('<option value="OP">OP</option>');
    	}
	}
	
	//model이 comp라면 amm은 보이지 않도록
	function manualTypeOption(){
		var value = $("#maintDocModelCode option:selected").val();
		$("#WOCntsDocSubtypCd").children("[value='AMM']").remove();
		if (value != "COMP"){
			$("select[name=WOCntsDocSubtypCd] option[value=KOR]").after('<option value="AMM">AMM</option>');
		}
	}
	
	function formCheck(){
		var fo = document.uploadForm;

		var result = true;
		var falseForm;
		if(result && fo.maintDocModelCode.value == ""){
			result = false;
			falseForm = fo.maintDocModelCode;
		}
		if(result && fo.WOCntsDocSubtypCd.value == ""){
			result = false;
			falseForm = fo.WOCntsDocSubtypCd;
		}
		
		var contentType = fo.masterWOCntsTypCd.value; 
		if(result && contentType == ""){
			result = false;
			falseForm = fo.masterWOCntsTypCd;
		}
		
		var fileName = fo.upFile.value;
		if(result && fileName == ""){
			result = false;
			falseForm = fo.upFile;
		}
		
		if(!result){
			alert("필수 입력 항목을 확인하세요.");
			falseForm.focus();
			
			return false;
		}
		
		var imageExts = "${imageExts}".toUpperCase() + ":ZIP";
		var ext = fileName.substr(fileName.lastIndexOf('.') + 1).toUpperCase();
		
		if(contentType == "OP" || contentType == "RT") {
			if("ZIP:XML".indexOf(ext) < 0) {
				alert("업로드 가능한 확장자가 아닙니다.");
				return false;
			}
		} else if (contentType == "IMAGE"){
			if(imageExts.indexOf(ext) < 0) {
				alert("업로드 가능한 확장자가 아닙니다.");
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	//
</script>

<!-- contents : str -->

	<form name="uploadForm" method="post" enctype="multipart/form-data" method="post" action="" target="resultFrame">
	<div id="popup_type_b">
		<h1>Content Upload</h1>
		<table border="0" cellspacing="0" cellpadding="0" class="popup_tbl">
			<colgroup>
				<col width="30%">
				<col width="">
			</colgroup>
			<tr>
				<th scope="row">Model</th>
				<td>
					<emp:list mainCode="1000" name="maintDocModelCode" optionTitle="- 선택 -"/>
				</td>
			</tr>
			<tr>
				<th scope="row">Manual Type</th>
				<td>
					<emp:list mainCode="2000" name="WOCntsDocSubtypCd" optionTitle="- 선택 -" />				
				</td>
			</tr>
			<tr>
				<th scope="row">Content Type</th>
				<td>
					<emp:list mainCode="3000" name="masterWOCntsTypCd" optionTitle="- 선택 -"/>			
				</td>
			</tr>
			<tr>
				<th scope="row">File</th>
				<td>
					<input type="file" id="upFile" name="upFile" size="50%" class="file" />
				</td>
			</tr>
		</table>
		<p class="btns">
			<button class="btn_type_d" id="btnConfirm">확인<span></span></button>
			<button class="btn_type_e" id="btnCancle">취소<span></span></button></p>
	</div>
    </form>
	<iframe width="0" height="0" frameborder="0" marginwidth="0" marginheight="0" name="resultFrame"></iframe>
<!-- contents : end -->