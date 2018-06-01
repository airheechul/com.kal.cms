<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<style type="text/css">
	body {
		overflow:hidden;
	}
	.btnList {
	    width: 400px; float: left; padding: 20px;
	}
	.btnList li {
        margin:15px;
	}
	.btnConfirm {
	    width: 80px; height: 30px;
	}

</style>
<script type="text/javascript">
	$(document).ready(function(){
		setOpener();
		
		//2014-04-18 Content [Create/Revision] 에서는 Manual Type[AMM/TR] 이 없고, Content Type[Image] 도 없다. by air
		//$('#WOCntsDocSubtypCd').children("[value='TSM'], [value='FRMFIM']").remove();
		//$('#masterWOCntsTypCd').children("[value='IMAGE']").remove();
		$('#WOCntsDocSubtypCd').children("[value='AMM'], [value='TR'], [value='TSM'], [value='FRMFIM']").remove();
		$('#masterWOCntsTypCd').children("[value='IMAGE']").remove();		
		
	    $('.confirm').click(function(e) {
			if(formCheck()){
				if ("${addType}" == "revise") {
					document.uploadForm.action="${pageContext.request.contextPath}/content/reviseContent.do";
				} else {
					document.uploadForm.action="${pageContext.request.contextPath}/content/contentUpload.do";
					document.uploadForm.target="resultFrame";
				}
				uploadForm.submit();
				window.returnValue = true;
			}
			
			return false;
	    });
	    $('.cancle').click(function(e) {
	    	self.close();
	    	
	    	return false;
	    });
	    
	  	//2014-04-18 Content [Create/Revision] 에서는 Manual Type[AMM/TR] 이 없고, Content Type[Image] 도 없다. by air
	    //$('#WOCntsDocSubtypCd').change(function(){
	    	//var value = $("#WOCntsDocSubtypCd option:selected").val();
	    	//$('#masterWOCntsTypCd').children("[value='IMAGE']").remove();
	    	//if(value != "AMM"){
	    	//	$('#masterWOCntsTypCd').append('<option value="IMAGE">Image</option>');
	    	//}
	    //});
	    
	    $('#maintDocModelCode').change(function(){
	    	toggleContenTypeOptionOP();
	    	
	    	
	    	//2014-04-18 Content [Create/Revision] 에서는 Manual Type[AMM/TR] 이 없고, Content Type[Image] 도 없다. by air
	    	//manualTypeOption();
	    	
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
		
		var fileExts = "${fileExts}".toUpperCase();
		var ext = fileName.substr(fileName.lastIndexOf('.') + 1).toUpperCase();
		
		if(contentType == "OP" || contentType == "RT") {
			if(fileExts.indexOf(ext) < 0) {
				alert("XML 파일만 업로드 가능합니다.");
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	//
	var opener = null;
    function setOpener()
    {
        if (window.dialogArguments) { // Internet Explorer supports window.dialogArguments{ 
            opener = window.dialogArguments;
        }else {// Firefox, Safari, Google Chrome and Opera supports window.opener 
            if (window.opener) {
                opener = window.opener;
            }
        }       
    }
</script>
<!-- contents : str -->

	<form name="uploadForm" method="post" enctype="multipart/form-data" method="post" action="" target="resultFrame">
		<input type="hidden" name="addType" value="${addType}"/>
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
			<button class="btn_type_d confirm">확인<span></span></button>
			<button class="btn_type_e cancle">취소<span></span></button>
			</p>
	</div>
    </form>
	<iframe width="0" height="0" frameborder="0" marginwidth="0" marginheight="0" name="resultFrame"></iframe>
<!-- contents : end -->