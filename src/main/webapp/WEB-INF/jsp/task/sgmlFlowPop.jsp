<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


	<style type="text/css">
		#selectAppTmp .ui-selecting { background: #FECA40; }
		#selectAppTmp .ui-selected { background: #F39814; color: white; }
		#selectAppTmp { list-style-type: none; margin: 0; padding: 0; width: 100%; }
		#selectAppTmp li { margin: 3px; padding: 0.4em; font-size: 11px; height: 18px; }
		
		#selectAppList .ui-selecting { background: #FECA40; }
		#selectAppList .ui-selected { background: #F39814; color: white; }
		#selectAppList { list-style-type: none; margin: 0; padding: 0; width: 100%; }
		#selectAppList li { margin: 3px; padding: 0.4em; font-size: 11px; height: 18px; }
	</style>	
	<script type="text/javascript">
	$(document).ready(function(){
		
		init();
		
		
	    $('#btnSubmit').click(function(e) {
	    	doSubmit();
	    	return false;
	    });
	    $('#btnCancle').click(function(e) {
	    	self.close();
	    	return false;
	    });
	    
	    $("#model").change(function(){
	    	if($(this).val()!=""){
	    		setUserData($(this).val());
		    	
		    	return false;	
	    	}
	    	
	    });
	});

	function init(){
		getUserData();
		recordDefaultData();
	}
	
	var defaultData;
	function recordDefaultData(){
		defaultData = [$("#inputFolder").val(), $("#sgmlFile").val(), $("#entityDtdFile").val(), $("#manualDtdFile").val(), $("#imageFile").val()] ;
	}
	
	function restoreDefaultData(){
		if(defaultData){
			$("#inputFolder").val(defaultData[0]);
			$("#sgmlFile").val(defaultData[1]);
			$("#entityDtdFile").val(defaultData[2]);
			
			$("#manualDtdFile").val(defaultData[3]);
			$("#imageFile").val(defaultData[4]);
		}
	}
	
    function doSubmit() {
<%--     	
    	if (Common.isEmpty($.trim($("#model").val()))) {
            Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<%= Util.getLayerDialogStyle("알림", "모델을 선택해 주세요.") %>"); 
            return;
        }
        if (Common.isEmpty($.trim($("#manualType").val()))) {
            Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<%= Util.getLayerDialogStyle("알림", "매뉴얼 타입을 선택해 주세요.") %>"); 
            return;
        }
        if (Common.isEmpty($.trim($("#inputFolder").val()))) {
            Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<%= Util.getLayerDialogStyle("알림", "inputFolder를 입력해 주세요.") %>"); 
            return;
        }
        if (Common.isEmpty($.trim($("#sgmlFile").val()))) {
            Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<%= Util.getLayerDialogStyle("알림", "SGML File 경로를 입력해 주세요.") %>"); 
            return;
        }
        if (Common.isEmpty($.trim($("#entityDtdFile").val()))) {
            Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<%= Util.getLayerDialogStyle("알림", "Entity DTD 경로를 입력해 주세요.") %>"); 
            return;
        }
	     --%>
		$('#frm').ajaxSubmit({
			url: './doSgmlFlow.do',
			type: 'post',
			dataType: 'json' , 
			success: function (json) {	
				if(json.success){
					var rt = Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>Task 실행</h1><p>"+json.message+"</p>", "fncClose()");
				} else {
					Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>에러</h1><p>"+json.message+"</p>");
				}
			},
			error : function(){
				Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>에러</h1><p>"+json.message+"</p>");
			}
		});
    }
    
    function fncClose() {
    	window.returnValue='ok';
	    self.close();
	}
    
	var userJsonData;
	function getUserData(){
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/task/getUserData.do",
			cache : false, 
			data : "",
			dataType: "json",
			success : function(data){
				if(data.result) {	
					userJsonData = data.userData;
				}
				
				return false;
			},
			error: function(data){
				return false;
			}
		});
	}

	function setUserData(model){
		opt = eval("(userJsonData."+model+")");

		if(opt) {
			$("#manualType").val(opt.manualType);
			
			$("#inputFolder").val(opt.inputFolder);
			$("#sgmlFile").val(opt.sgmlFile);
			$("#entityDtdFile").val(opt.entityDtdFile);
			
			$("#manualDtdFile").val(opt.manualDtdFile);
			$("#imageFile").val(opt.imageFile);
		} else {
			restoreDefaultData();
		}
	}

</script>

           <!-- Body-content -->
<div id="popup_type_a">
	<form id="frm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="currentPage" value="${param.currentPage}" />
		<input type="hidden" name="pageSize" value="${param.pageSize}" />
			

		<h1>SGML Flow</h1>
		<table border="0" cellspacing="0" cellpadding="0" class="popup_tbl">
			<colgroup>
				<col width="30%">
				<col width="">
			</colgroup>
			<tr>
				<th scope="row">Model</th>
				<td>
					<emp:list mainCode="14000" name="model" optionTitle="- 선택 -"  subCode=""  />
				</td>
			</tr>
			<tr>
				<th scope="row">Manual Type</th>
				<td>
					<emp:list mainCode="2000" name="manualType" optionTitle="- 선택 -"  subCode=""  /> 				
				</td>
			</tr>
			<tr>
				<th scope="row">In Put Folder</th>
				<td>
					<input type="text" id="inputFolder" class="inputText w90pc" name="inputFolder"  maxlength="100" value="${basePath}" />		
				</td>
			</tr>
			<tr>
				<th scope="row">SGML File</th>
				<td>
					<input type="text" id="sgmlFile" class="inputText w90pc" name="sgmlFile"  maxlength="100" value="${sgmFilePath}" />			
				</td>
			</tr>
			<tr>
				<th scope="row">Entity DTD File</th>
				<td>
					<input type="text" id="entityDtdFile" class="inputText w90pc" name="entityDtdFile"  maxlength="100" value="${cgmDTDPath}" />			
				</td>
			</tr>
			<tr>
				<th scope="row">Manual DTD File</th>
				<td>
					<input type="text" id="manualDtdFile" class="inputText w90pc" name="manualDtdFile"  maxlength="100"/>			
				</td>
			</tr>
			<tr>
				<th scope="row">Image File</th>
				<td>
					<input type="text" id="imageFile" class="inputText w90pc" name="imageFile"  maxlength="100" value="${imageFile}"/>
				</td>
			</tr>
		</table>
		<p class="btns">
			<button class="btn_type_d" id="btnSubmit">확인<span></span></button>
			<button class="btn_type_e" id="btnCancle">취소<span></span></button>
		</p>
	</form>
</div><!-- /Body-content -->