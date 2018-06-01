<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn"      uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
	function initLOVs(type, code, name) {
		
		var option = $("<option value='" + code + "'>" + name + "</option>");
		
		if (type == "1000") {
	        $('#searchMaintDocModelCode').append(option);	
		} else if (type == "2000") {
	        $('#searchWOCntsDocSubtypCd').append(option);	
		} else if (type == "3000") {
	        $('#searchMasterWOCntsTypCd').append(option);	
		} else if (type == "4000") {
	        $('#searchReleaseYn').append(option);	
		} else if (type == "8000") {
	        $('#searchMasterWOCntsSttsCd').append(option);	
		}
		
	}
	
	function initSelected(value) {
		
	}

	$(document).ready(function() {

/* 		var lovs; 
		
		<c:forEach var="LOV" items="${LOVs}">
		initLOVs("${LOV.lookupTypeName}", "${LOV.lookupCode}", "${LOV.lookupName}");
		</c:forEach> */

		
		init();
		
		//엔터 키 
		$("#searchTermsBox").keyup(function(e){
			if(e.keyCode == 13){
				searchList('1');
				return false;
			}
		});
		
		$('#btnSearch').click(function(){
			searchList('1');
			return false;
		});

		$('#btnOptionClear').click(function(){
			searchOptionClear();
			return false;
		});
		
		$('#btnCreate').click(function(){
			uploadPop();
			return false;
		});
		
		$("#btnXmetal").click(function(){
			uploadPopTemplateList();
			return false;
		});

		$('#btnRevision').click(function(){
			revision();
			return false;
		});

		$('#btnDelete').click(function(){
			deleteContents();
			return false;
		});
		
		$('.btnPreviewPop').click(function(){
			var model = $(this).attr("data1");
			var manualType = $(this).attr("data2");
			var urlAddress = $(this).attr("data3");
			
			openPreviewPop(model, manualType, urlAddress);
			return false;
		});
		
		$('.btnCheckIn').click(function(){
			var contentId = $(this).attr("data1");
			var revType = $(this).attr("data2");
			
			checkInConfirm(contentId, revType);
			return false;
		});
		
		$('.btnImagePop').click(function(){
			var fileName = $(this).attr("data1");
			var urlAddress = $(this).attr("data2");
			var imgSrc = "${pageContext.request.contextPath}/content/imageFile.do?fileOrgName="+fileName+"&fileFullPath="+urlAddress;
			
			displayImage(imgSrc, fileName);
			return false;
		});
		
	    $('#searchMaintDocModelCode').change(function(){
	    	toggleManualTypeOption();
	    	toggleContenTypeOptionOP();
	    	
	    	return false;
	    });
	    
	    $('.pageNavi').change(function(){
	    	searchList($("option:selected", this).val());
	    	return false;
	    });
	    
	    $('.btnPagePrev, .btnPageNext').click(function(){
	    	searchList($(this).val());
	    	return false;
	    });
	});
	
	function init(){
		Common.importJS("datepicker.js");
		
		setOpener();
		
		var model_type = $("#searchMaintDocModelCode option:selected").attr("class");
<%-- 		if("<%=ContentSearch.MODEL_TYPE_AIRCRAFT%>" == model_type){
			$('#searchMasterWOCntsTypCd').children("[value='<%=ContentSearch.CONTENT_TYPE_OP%>']").remove();
		} --%>
		
		//toggleManualTypeOption();
	}
	
	function toggleContenTypeOptionOP(){
    	var value = $("#searchMaintDocModelCode option:selected").attr("class");
    	
    	$('#searchMasterWOCntsTypCd').children("[value='OP']").remove();
    	if("13001" != value){
    		$("select[name=searchMasterWOCntsTypCd] option[value=RT]").before('<option value="OP">OP</option>');
    	}
	}

	function toggleManualTypeOption(){
    	var sgb = $("#searchMaintDocModelCode option:selected").val();
    	var mgb = $("#searchWOCntsDocSubtypCd option:selected").val();
    	
/*     	$('#searchWOCntsDocSubtypCd').children().each(function(){
    		if($(this).val() != "KOR" && $(this).val() != "") {
    			alert("remove method..");
    			$(this).remove();
    		}
    	}); */
    	
    	if(sgb != 'undefined' && sgb != null){
    		if(sgb.indexOf("A3") == 0){
    			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="AMM">AMM</option>');
    			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="TSM">TSM</option>');
    			
    		} else if (sgb.indexOf("B7") == 0){
    			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="AMM">AMM</option>');
    			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="FRMFIM">FRMFIM</option>');
    		}
    		
    	} else {
    		$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="AMM">AMM</option>');
    		$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="TSM">TSM</option>');
    		$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before('<option value="FRMFIM">FRMFIM</option>');
    	}
    	
    	$('#searchWOCntsDocSubtypCd').val(mgb);
	}
	
	function onClickChkAll(obj) {
		if (obj.checked) {
			$('input:checkbox').attr('checked', true);
		} else {
			$('input:checkbox').attr('checked', false);
		}
	}


	function searchList(pageIndex) {
		if (pageIndex != "") {
			document.contentForm.pageIndex.value = pageIndex;
		}
		document.contentForm.action = "${pageContext.request.contextPath}/content/searchList.do";
		document.contentForm.submit();
	}
	

	function naviSearchList() {

		document.contentForm.action = "${pageContext.request.contextPath}/content/searchList.do";
		document.contentForm.submit();

	}

	/**
	 *  템플릿(컨텐츠) 생성 선택 팝업 (추후 xMetal 연동)
	 */
	function chooseCreateTmplPop() {
		/*
		var pop = window.open(
						"${pageContext.request.contextPath}/content/chooseCreateTmplPop.do",
						"chooseCreateTmplPop",
						"height=,width=490,menubar=no,toolbar=no,location=no,resizable=no,status=no,scrollbars=no,top=300,left=700");
		if (pop) {
			pop.focus();
		}
		*/
		
		var url = "${pageContext.request.contextPath}/content/chooseCreateTmplPop.do?addType=new";
		var popupProps = "dialogWidth:" + 700 + "px;,dialogHeight:" + 550 + "px;resizable=no;scroll=no;location=no;";
		window.name = "chooseCreateTmplPop";
		var popupWin = window.showModalDialog(url, this, popupProps);
	}

	/**
	 *  HTML Preview
	 http://10.0.42.78:9081/paperless/service.wbp
	 	?primitive=KAL_JOBORDER_PREVIEW&model=B777&manualType=AMM&xmlPath=/kalppl/hq/manual/oem/b777/amm/1/rt/B777_KOR_SB_27-0062_ELEV_LUB_600_001.xml	 
	 
	 */
	function openPreviewPop(model, manualType, xmlPath) {
		// iframe을 사용할 필요가 없으므로 일반 팝업으로 사용함.
		//var baseUrl = "${pageContext.request.contextPath}/content/previewHtmlPop.do";
		//var pop = window.open(
		//		baseUrl+"?maintDocModelCode="+model+"&WOCntsDocSubtypCd="+manualType+"&urlAddress="+xmlPath,
		//				"previewHtmlPop",
		//				"height=1024,width=768,menubar=no,toolbar=no,location=no,resizable=yes,status=no,scrollbars=yes,top=100,left=100");
		var previewUrl = "${previewBaseUrl}${previewUrl}";
		//xmlPath = "D://daehanWorkspace01/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/com.kal.paperless/xsl/joborder/B777_AMM_26_23_00_5_730_802_KF7015B3E2721B922FA803F363407CCE_KAL.xml";
		//xmlPath = "D://daehanWorkspace01/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/com.kal.paperless/xsl/joborder/COMP_KOR_241_L3245021900000_790_202.xml";
		//xmlPath = "D://daehanWorkspace01/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/com.kal.paperless/xsl/joborder/COMP_KOR_241_L3245021900000_BC_999_001.xml";
		//xmlPath = "D://daehanWorkspace01/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/com.kal.paperless/xsl/joborder/B777_AMM_26_23_00_5_730_802_KF7015B3E2721B922FA803F363407CCE_KAL.xml";
		//previewUrl = "http://localhost:8090/com.kal.paperless/service.wbp";
		previewUrl = previewUrl+"?primitive=KAL_JOBORDER_PREVIEW&model="+model+"&manualType="+manualType+"&xmlPath="+xmlPath;
		window.open(previewUrl, "_blank", "width=1024,menubar=no,toolbar=no,location=no,resizable=yes,status=no,scrollbars=yes,top=100,left=100");
		//if (pop) {
		//	pop.focus();
		//}
	}

	/* clear search pannel */
	function searchOptionClear(){
		/* clear selectBox */
		$("#searchTermsBox select").each(function(idx){
			$(this).children().selected = false;
			$(this).children().eq(0).attr("selected", "selected");
		});
		
		/* clear textBox */
		$("#searchTermsBox input").each(function(idx){
			$(this).val("");
		});
	}	
	
	/**
	 *  Check In (Release)
	 */
	function checkIn(contentId, immediateYn) {

		url = "${pageContext.request.contextPath}/content/checkInContent.do";
		
		$.ajax({
			async : false,
			type : "GET",
			url : url,
			data : "masterWorkorderContentsId=" + contentId+"&immediateYn="+immediateYn,
			success : function(message) {
				winConfirm.close();
				alert(message);
				naviSearchList();
			},
			beforeSend : null,
			error : function(e) {
				alert(e.responseText);
				return false;
			}
		});
		
		return true;
	}

	var winConfirm = null;
	function checkInConfirm(contentId, revType){
		winConfirm = null;
		try{
			if(winConfirm == null){
				var cw=500; //새창의 크기
				var ch=350;
				var sw=screen.availWidth; //스크린의 크기
				var sh=screen.availHeight;
				var px=(sw-cw)/2; //열 창의 포지션
				var py=(sh-ch)/2;				
				
				winConfirm = window.open("" ,"imagePop","toolbar=no,scrollbars=no,resizable=no,location=no,width="+cw+"px,height="+ch+"px,top="+py+"px,left="+px+"px");
								
				var htmlString = "<!DOCTYPE HTML><html><head>";
				htmlString += "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>";
				htmlString += "<title>KOREAN AIR - Paperless Content Management System</title>";
				htmlString += "<link href='${pageContext.request.contextPath}/css/blue/style.css' rel='stylesheet' type='text/css'>";
				htmlString += "</head>";
				htmlString += "<body class='alertbody'>";
				htmlString += "	<div id='alertwrap'>";
				htmlString += "		<h1>체크인</h1>";
				
				if (revType == "R") {
					htmlString += "		<p class='comment'>"+'<spring:message code="content.search.checkin.update.confirm" arguments=""/>'+"</p>";	
					htmlString += "		<p class='btns'><button class='btn_type_d btnYes' onclick='opener.checkIn(\""+contentId+"\", \"Y\");self.close();'>예<span></span></button>&nbsp;";
					htmlString += "		<button class='btn_type_d btnNo' onclick='opener.checkIn(\""+contentId+"\", \"N\");self.close();'>아니오<span></span></button>&nbsp;";
				} else {
					htmlString += "		<p class='comment'>"+'<spring:message code="common.action.checkin.confirm" arguments=""/>'+"</p>";	
					htmlString += "		<p class='btns'><button class='btn_type_d btnYes' onclick='opener.checkIn(\""+contentId+"\", \"Y\");self.close();'>예<span></span></button>&nbsp;";
				}
				htmlString += "		<button class='btn_type_e btnCancle' onclick='self.close();'>취소<span></span></button></p>";
				htmlString += "	</div>";
				htmlString += "</body></html>";
				winConfirm.document.open();
				winConfirm.document.write(htmlString);
				winConfirm.document.close();
			}
			if(winConfirm != null)
				winConfirm.focus();
		}catch(e){
			alert(e);
		}
		

		return winConfirm;
	}
	
	/**
	 *  Revision
	 */
	function revision() {
		var $contentChecked = $(".contentCheck:checked");
		
		if ( $contentChecked.length == 0 ) {
			alert('<spring:message code="common.error.noselected" arguments=""/>');
			return false;
		}
		
		if ( $contentChecked.length > 1 ) {
			alert('<spring:message code="content.search.error.selectSingleFile" arguments=""/>');
			return false;
		}
		
		document.contentForm.fileName.value = $contentChecked.first().attr("fileName");
		document.contentForm.WOCntsDocSubtypCd.value = $contentChecked.first().attr("WOCntsDocSubtypCd");
		document.contentForm.masterWOCntsRevzNo.value = $contentChecked.first().attr("masterWOCntsRevzNo");
		
		document.contentForm.action = "${pageContext.request.contextPath}/content/revisionList.do";
		document.contentForm.submit();
		
		return true;
	}
	
	/**
	 *  Delete
	 */
	function deleteContents() {
		var $contentChecked = $(".contentCheck:checked");
		
		if ( $contentChecked.length == 0 ) {
			alert('<spring:message code="common.error.noselected" arguments=""/>');
			return false;
		}
		
		if (confirm('<spring:message code="common.action.delete.confirm" arguments=""/>')) {
			
		    var checkboxValues = [];
		    $("input[name='contentCheck']:checked").each(function(i) {
		        checkboxValues.push($(this).val());
		    });

		    var allData = { "contentCheck": checkboxValues };
			
			var url = "${pageContext.request.contextPath}/content/deleteContents.do";
			
			$.ajax({
				async : false,
				type : "POST",
				url : url,
				data : allData,
				success : function(message) {
					naviSearchList();
				},
				beforeSend : null,
				error : function(e) {
					alert(e.responseText);
					return false;
				}
			});
		}
		else {
			return false;
		}
	}
	
	// xmetal 연동하지 않은 일반 팝업
	function uploadPop() {
		var url = "${pageContext.request.contextPath}/content/contentUploadPop.do?addType=new";
		var popupProps = "dialogWidth:" + 700 + "px;,dialogHeight:" + 550 + "px;resizable=no;scroll=no;location=no;";
		window.name = "uploadPop";
		var popupWin = window.showModalDialog(url, this, popupProps);
		if(popupWin)searchList('1');
	}
	
	// xmetal 연동
	// 현재 template 체크
	function uploadPopTemplateList(){
		var url = "${pageContext.request.contextPath}/content/contentUploadTemplateListPop.do?addType=new";
		var popupProps = "dialogWidth:" + 700 + "px;,dialogHeight:" + 550 + "px;resizable=no;scroll=no;location=no;";
		window.name = "uploadPop";
		var popupWin = window.showModalDialog(url, this, popupProps);
	}
	
	function displayImage(picName, myText){
		var winHandle = null;
		try{
			if(winHandle == null){
				var cw=500;
				var ch=500;
				var sw=screen.availWidth;
				var sh=screen.availHeight;
				var px=(sw-cw)/2;
				var py=(sh-ch)/2;	
				winHandle = window.open("" ,"imagePop","toolbar=no,scrollbars=no,resizable=no,width="+cw+"px,height="+ch+"px,top="+py+"px,left="+px+"px");
				
				var htmlString = "<html><head><title>Picture</title><META http-equiv='imagetoolbar' content='no'></head>";
				htmlString += "<body leftmargin=0 topmargin=0 marginwidth=0 marginheight=0  oncontextmenu='return false;' ondragstart='return false;' onselectstart='return false;'>";
				htmlString += "<center>"+myText+"</center>";
				htmlString += "<a href=javascript:window.close()><img width='491' height='487' src=" + picName + " border=0 alt='닫기'></a>";
				htmlString += "</body></html>";
				winHandle.document.open();
				winHandle.document.write(htmlString);
				winHandle.document.close();
			}
			if(winHandle != null)
				winHandle.focus();
		}catch(e){
			alert(e);
		}
		

		return winHandle;
	}
	
	var opener = null;
    function setOpener() {
        if (window.dialogArguments) { // Internet Explorer supports window.dialogArguments{ 
            opener = window.dialogArguments;
        }else {// Firefox, Safari, Google Chrome and Opera supports window.opener 
            if (window.opener) {
                opener = window.opener;
            }
        }       
    }
</script>

<!-- center-c-c -->
<!-- Body-content -->

<form:form id="contentForm" name="contentForm"  method="get"> 
<%-- 	<input type="hidden" name="topNum" value="${searchMap.topNum}"/>	
	<input type="hidden" id="currentPage" name="currentPage" value="${searchMap.currentPage}" />	 --%>
	<input type="hidden" id="fileName" name="fileName" value=""/>
	<input type="hidden" id="WOCntsDocSubtypCd" name="WOCntsDocSubtypCd" value=""/>
	<input type="hidden" id="masterWOCntsRevzNo" name="masterWOCntsRevzNo" value=""/>
	<input type="hidden" name="act" value="search"/>


<%-- 	<table>
	<c:forEach var="lookupList" items="${lookupListVOs}">		
		<tr>
 			<td>
 				<c:forEach var="LOVs" items="${lookupList.LookupVO}">		
	
			 			${LOVs.lookupTypeName},
			 			${LOVs.lookupCode},
			 			${LOVs.lookupName} <br>
		
				</c:forEach>
 			
 			</td>
		</tr>
	</c:forEach>
	</table> --%>

	<table class="tbl_type_b"  id="searchTermsBox">
		<colgroup>
			<col width="17%">
			<col width="33%">
			<col width="17%">
			<col width="">
		</colgroup>
			<tr>
				<th>Model</th>
				<td>
					<select id="searchMaintDocModelCode" name="searchMaintDocModelCode">
						<option value="" disabled selected> - 선택 - </option>
						
						<c:forEach var="LOV" items="${LOVs}">
							<c:if test="${LOV.lookupTypeName == 1000}">
								<option value='${LOV.lookupCode}' <c:if test="${LOV.lookupCode == searchMap.searchMaintDocModelCode}">selected='selected'</c:if>>${LOV.lookupName}</option>
							</c:if>
						</c:forEach>
					</select>
					<%-- <emp:list mainCode="1000" name="searchMaintDocModelCode" optionTitle="- 선택 -"  subCode="${param.searchMaintDocModelCode}"  /> --%>				
				</td>
				<th>Manual Type</th>
				<td>
					<select id="searchWOCntsDocSubtypCd" name="searchWOCntsDocSubtypCd">
						<option value="" disabled selected> - 선택 - </option>
						
						<c:forEach var="LOV" items="${LOVs}">
							<c:if test="${LOV.lookupTypeName == 2000}">
								<option value='${LOV.lookupCode}' <c:if test="${LOV.lookupCode == searchMap.searchWOCntsDocSubtypCd}">selected='selected'</c:if>>${LOV.lookupName}</option>
							</c:if>
						</c:forEach>
					</select>
					<%-- <emp:list mainCode="2000" name="searchWOCntsDocSubtypCd" optionTitle="- 선택 -"  subCode="${param.searchWOCntsDocSubtypCd}"  /> --%>
				</td>
			</tr>
			<tr>
				<th>Content Type</th>
				<td>
					<select id="searchMasterWOCntsTypCd" name="searchMasterWOCntsTypCd">
						<option value="" disabled selected> - 선택 - </option>
						
						<c:forEach var="LOV" items="${LOVs}">
							<c:if test="${LOV.lookupTypeName == 3000}">
								<option value='${LOV.lookupCode}' <c:if test="${LOV.lookupCode == searchMap.searchMasterWOCntsTypCd}">selected='selected'</c:if>>${LOV.lookupName}</option>
							</c:if>
						</c:forEach>
					</select>
					<%-- <emp:list mainCode="3000" name="searchMasterWOCntsTypCd" optionTitle="- 선택 -"  subCode="${param.searchMasterWOCntsTypCd}"  /> --%>
				</td>
				<th>Release Type</th>
				<td>
					<select id="searchReleaseYn" name=searchReleaseYn>
						<option value="" disabled selected> - 선택 - </option>
						
						<c:forEach var="LOV" items="${LOVs}">
							<c:if test="${LOV.lookupTypeName == 4000}">
								<option value='${LOV.lookupCode}' <c:if test="${LOV.lookupCode == searchMap.searchReleaseYn}">selected='selected'</c:if>>${LOV.lookupName}</option>
							</c:if>
						</c:forEach>
					</select>
				 	<%-- <emp:list mainCode="4000" name="searchReleaseYn" optionTitle="- 선택 -"  subCode="${param.searchReleaseYn}"  />  --%>
				</td>
			</tr>
			<tr>
				<th>Revise Type</th>
				<td>
					<select id="searchMasterWOCntsSttsCd" name=searchMasterWOCntsSttsCd>
						<option value="" disabled selected> - 선택 - </option>
						
						<c:forEach var="LOV" items="${LOVs}">
							<c:if test="${LOV.lookupTypeName == 8000}">
								<option value='${LOV.lookupCode}' <c:if test="${LOV.lookupCode == searchMap.searchMasterWOCntsSttsCd}">selected='selected'</c:if>>${LOV.lookupName}</option>
							</c:if>
						</c:forEach>
					</select>
					<%-- <emp:list mainCode="8000" name="searchMasterWOCntsSttsCd" optionTitle="- 선택 -"  subCode="${param.searchMasterWOCntsSttsCd}"  /> --%>
				</td>
				<th>Author</th>
				<td>
					<input type="text" name="searchAuthor" id="searchAuthor" class="w40p" style="width:280px;" value="${searchMap.searchAuthor}" />
				</td>
			</tr>			
			<tr>
				<th>File Name</th>
				<td>
					<input type="text" name="searchFileName" id="searchFileName" class="w40p" style="width:280px;" value="${searchMap.searchFileName}" /><!--  PW4000_KOR_72_32_CPL003P_INS_005.xml -->
				</td>
				<th>keyword</th>
				<td>
					<input type="text" name="searchValue" id="searchValue" class="w40p" style="width:280px;" value="${searchMap.searchValue}" />
				</td>				
			</tr>			
			<tr>
				<th>Date</th>
				<td colspan="3">
					<label for="searchStartDt">From</label>
					<input type="text" name="searchStartDt" id="searchStartDt" value="${searchMap.searchStartDt}" class="date" style="width:100px;" readonly/>
					&nbsp;&nbsp;~&nbsp;&nbsp;
					<label for="searchEndDt">To</label>
					<input type="text" name="searchEndDt" id="searchEndDt"  value="${searchMap.searchEndDt}" class="date" style="width:100px;" readonly/>				
				</td>
			</tr>	
	</table>	
	
	<div class="under_tbl_area">
		<button class="btn_type_a" id="btnSearch">Search<span></span></button>
		<button class="btn_type_a" id="btnOptionClear">Clear<span></span></button>
	</div>

	<div class="space"></div>

	<div class="top_tbl_area">
		<button class="btn_type_a btnBig" id="btnCreate">Create<span></span></button>
		<button class="btn_type_a btnBig" id="btnXmetal">xMetal_Create<span></span></button>
		<button class="btn_type_a" id="btnRevision">Revision<span></span></button>
		<button class="btn_type_a" id="btnDelete">Delete<span></span></button>
		
		<input type="hidden" id="pageSize" name="pageSize" value="${pMap.pageSize}"/>
		<%-- <emp:pageNavi pageList="${contentSearchList}" doSubmit="naviSearchList();" styleClass="pageNum" pageNaviSize="${pMap.pageSize}"/> --%>
	</div>

	<table class="tbl_type_a">
		<colgroup>
			<col width="5%">
			<col width="7%">
			<col width="5%">
			<col width="*">
			<col width="7%">
			<col width="10%">
			<col width="7%">
			<col width="7%">
			<col width="9%">
			<col width="9%">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" name="checkAll" onclick="javascript:onClickChkAll(this);"></th>
				<th>REV.<br/>Type</th>
				<th>REV<br/>No.</th>
				<th>File Name</th>
				<th>Manual Type</th>
				<th>Release date</th>
				<th>Release</th>
				<th>Author</th>
				<th colspan="2">Action</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${contentSearchList != null && fn:length(contentSearchList) > 0}">  
			<c:forEach var="contentSearchInfo" items="${contentSearchList}" varStatus="idx">
			<tr style="cursor:pointer"> 
				<td>
					<input type="checkbox" name="contentCheck" class="contentCheck" value="${contentSearchInfo.masterWorkorderContentsId}${contentSearchInfo.masterWOCntsImgId}"
						fileName="${contentSearchInfo.fileName}" WOCntsDocSubtypCd="${contentSearchInfo.WOCntsDocSubtypCd}" masterWOCntsRevzNo="${contentSearchInfo.masterWOCntsRevzNo}"/>
				</td>
				<td>${contentSearchInfo.masterWOCntsSttsNm}</td>
				<td>${contentSearchInfo.masterWOCntsRevzNo}</td>	
				<td><div class="dv_file_name"><span>${contentSearchInfo.fileName}</span></div></td>	
				<td>${contentSearchInfo.WOCntsDocSubtypCd}</td>
				<td><div class="dv_date"><span>${contentSearchInfo.releaseDate}</span></div></td>
				<td>${contentSearchInfo.releaseYn}</td>							
				<td>${contentSearchInfo.lastUpdatedByV}</td>
				<td class="border_none">
					<c:if test="${contentSearchInfo.masterWOCntsTypCd != 'IMAGE'}">
					<button class="btn_type_b btnPreviewPop" data1="${contentSearchInfo.maintDocModelCode}" data2="${contentSearchInfo.WOCntsDocSubtypCd}" data3="${contentSearchInfo.urlAddress}">Preview<span></span></button>
					</c:if>
					<c:if test="${contentSearchInfo.masterWOCntsTypCd == 'IMAGE'}">
					<button class="btn_type_b btnImagePop" data1='${contentSearchInfo.fileName}' data2='${contentSearchInfo.urlAddress}'>Preview<span></span></button>
					</c:if>
				</td>
				<td class="border_none_last">
					<c:if test="${contentSearchInfo.releaseYn != 'Y' && contentSearchInfo.masterWOCntsTypCd != 'IMAGE' && contentSearchInfo.masterWOCntsSttsCd != 'D'}">
					<button class="btn_type_c btnCheckIn" data1="${contentSearchInfo.masterWorkorderContentsId}" data2="${contentSearchInfo.masterWOCntsSttsCd}">Check In<span></span></button>
					</c:if>
				</td>
			</tr>
			</c:forEach>
			</c:if>
			<c:if test="${contentSearchList == null || fn:length(contentSearchList) == 0}">  
			<tr > 
				<td colspan="10"> <spring:message code="common.list.nocontent" arguments=""/>	</td>
			</tr>
			</c:if>		
		</tbody>
	</table>
	
<%-- 	<div class="under_tbl_area_b"><span>* Revision Type : N(New), R(Revise), D(Delete)</span>
		<emp:pageNavi pageList="${contentSearchList}" doSubmit="naviSearchList();" styleClass="pageNum" pageNaviSize="${pMap.pageSize}"/>
	</div> --%>
	

</form:form>
<div>
	<table width = "100%">
		<tr>
			<td align='center'>
	        	<div id="paging">
	        		<ui:pagination paginationInfo="${paginationInfo}" type="text" jsFunction="searchList" />
	        	</div>
			</td>
		</tr>
	</table>
</div>
