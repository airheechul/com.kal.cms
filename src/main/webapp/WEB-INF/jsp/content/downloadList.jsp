<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script type="text/javascript">
	var optionChecked = true;
	
	$(document).ready(function(){
		init();
		
		$('#searchMaintDocModelCode, #searchWOCntsDocSubtypCd, #searchMasterWOCntsTypCd').change(function(){
			optionChecked = false;
			return false;
		});
		 
		$("#btnSelectDownload").click(function(e){
			selectDownload(); 
			return false;
		});
		
		$("#btnAllDownload").click(function(e){
			allDownload();
			return false;
		});
		
		$('#btnSearch').click(function(){
			searchList('1');
			return false;
		});
		
		$("#searchTermsBox").keyup(function(e){
			if(e.keyCode == 13){
				searchList('1');
				return false;
			}
		});			
		
		$('#btnOptionClear').click(function(){
			searchOptionClear();
			return false;
		});
		
		$('#btnCancle').click(function(){
			cancleDownLoad();
			return false;
		});
		
	    $('#searchMaintDocModelCode').change(function(){
	    	toggleManualTypeOption();
	    	toggleContenTypeOption();
	    	return false;
	    });		
		
	    $('#searchWOCntsDocSubtypCd').change(function(){
	    	toggleContenTypeOption();
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
		
		toggleManualTypeOption();
		
		if('${param.searchWOCntsDocSubtypCd}' == "AMM"){
			$('#searchMasterWOCntsTypCd').children("[value='IMAGE']").remove();
    	}
	}
	
	function toggleContenTypeOption(){
    	var value = $("#searchWOCntsDocSubtypCd option:selected").val();
    	$('#searchMasterWOCntsTypCd').children("[value='IMAGE']").remove();
    	if(value != "AMM"){
    		$('#searchMasterWOCntsTypCd').append('<option value="IMAGE">Image</option>');
    	}
	}
	
	function toggleManualTypeOption(){
    	var sgb = $("#searchMaintDocModelCode option:selected").val();
    	var mgb = $("#searchWOCntsDocSubtypCd option:selected").val();
    	
    	$('#searchWOCntsDocSubtypCd').children().each(function(){
    		if($(this).val() != "KOR" && $(this).val() != "") {
    			$(this).remove();
    		}
    	});
    	
    	if("" != sgb){
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
		if(obj.checked) {
			$('input:checkbox').attr('checked',true);
		} else {
			$('input:checkbox').attr('checked',false);
		}
	}	
	
	/**
	 *  리스트
	 */	
	function searchList(currentPage) {
   	    document.contentForm.currentPage.value = currentPage;    	
		document.contentForm.action = "${pageContext.request.contextPath}/content/downloadList.do";
		document.contentForm.submit();
	}
	/**
	 *  리스트
	 */	
	function naviProcessList() {
		document.contentForm.action = "${pageContext.request.contextPath}/content/downloadList.do";
		document.contentForm.submit();
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
	
	function selectDownload(){
		//document.contentForm.action = "${pageContext.request.contextPath}/content/downloadList.do";
		if(formCheck()){
			
			if(!optionChecked){
				alert("[검색]을 눌러 리스트를 동기화 하셔야 합니다.");				
				return false;
			}
			
			var n = $("input:checked").length;
			if(n == 0) {
				alert("선택된 리스트가 없습니다.");
				return false;
			}
			
			contentZip();
		}
	}
	
	function allDownload(){
		$('#checkAll').val("Y");
		if(formCheck()){
			if(!optionChecked){
				alert("[검색]을 눌러 리스트를 동기화 하셔야 합니다.");
			} else {
				
				var n = $("input[name='contentCheck']").length;
				if(n == 0) {
					alert("조회된 리스트가 없습니다.");
					return false;
				}
				
				$('input:checkbox').attr('checked',true);
				contentZip();
			}
		}
	}
	
	function contentZip(){

		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/content/downloadCompress.do",
			cache : false, 
			data : $("#contentForm").serialize(),
			dataType: "json",
			success : function(data){
				if(data.result == "OK") {
					alert("처리되었습니다.\n 처리된 파일은 컨텐츠 프로세스 리스트에서\n 다운로드 아이콘을 클릭하십시오");
					
					cancleDownLoad();
				} else {
					alert(data.result);
				}
			},
			error: function(data){
				alert("실패");
			}
		});
	}
	
	function downloadFile(data1, data2){
		if(confirm("이 파일을 열거나 저장하시겠습니까?\n이름: "+data2+"\n유형: zip\n시작: "+data1+" ")) {
			document.download.action = "${pageContext.request.contextPath}/content/downloadFile.do";
			document.download.fileName.value = data1;
			document.download.fileOrgName.value = data2;
			document.download.target = "downloadFrame";
			document.download.submit();
		}
	}
	
	function cancleDownLoad(){
		document.contentForm.action = "${pageContext.request.contextPath}/content/processList.do?topNum=2&act=search";
		document.contentForm.act.value = "";
		document.contentForm.submit();
	}
	
	function formCheck(){
		var fo = document.contentForm;

		var result = true;
		var falseForm;
		if(fo.searchMaintDocModelCode.value == ""){
			result = false;
			falseForm = fo.searchMaintDocModelCode;
		}
		if(result && fo.searchWOCntsDocSubtypCd.value == ""){
			result = false;
			falseForm = fo.searchWOCntsDocSubtypCd;
		}
		
		if(result && fo.searchMasterWOCntsTypCd.value == ""){
			result = false;
			falseForm = fo.searchMasterWOCntsTypCd;
		}
		
		if(!result){
			alert("필수 입력 항목을 확인하세요.");
			falseForm.focus();
			
			return false;
		}
		return true;
	}
</script>

<!-- center-c-c -->
<!-- Body-content -->
<form id="contentForm" name="contentForm"  method="post"> 
	<input type="hidden" id="currentPage" name="currentPage" value="${param.currentPage}" />
	 
	<input type="hidden" name="topNum" value="${param.topNum}"/>
	<input type="hidden" name="leftNum" value="${param.leftNum}"/>
	<input type="hidden" name="act" value="search"/>
		<h2>
		<img src="${pageContext.request.contextPath}/html/images/${skin}/tit_contentDownload.png" width="171" height="15" alt="content download">
		<button class="btn_type_a" id="btnCancle">Cancel<span></span></button>
		</h2>
	<table class="tbl_type_b" id="searchTermsBox">
		<colgroup>
			<col width="17%">
			<col width="33%">
			<col width="17%">
			<col width="">
		</colgroup>
			<tr>
				<th class="mustSelect">Model</th>
				<td>
				<emp:list mainCode="1000" name="searchMaintDocModelCode" optionTitle="- 선택 -"  subCode="${param.searchMaintDocModelCode}"  />
				</td>
				<th class="mustSelect">Manual Type</th>
				<td>
				<emp:list mainCode="2000" name="searchWOCntsDocSubtypCd" optionTitle="- 선택 -"  subCode="${param.searchWOCntsDocSubtypCd}"  />
				</td>
			</tr>
			<tr>
				<th class="mustSelect">Content Type</th>
				<td>
				<emp:list mainCode="3000" name="searchMasterWOCntsTypCd" optionTitle="- 선택 -"  subCode="${param.searchMasterWOCntsTypCd}"  />
				</td>
				<th>Author</th>
				<td>
					<input type="text" name="searchAuthor" id="searchAuthor" class="w40p" value="${param.searchAuthor}" />
				</td>
			</tr>

			<tr>
				<th>Release Date</th>
				<td colspan="3"><label for="textfield">From</label>
				<input type="text" name="searchStartDt" id="searchStartDt" value="${param.searchStartDt}" class="date" style="width:100px;" readonly/>
				~ 
				<label for="textfield2">To</label>
				<input type="text" name="searchEndDt" id="searchEndDt"  value="${param.searchEndDt}" class="date" style="width:100px;" readonly/>
				</td>
			</tr>
			<tr>
				<th>File Name</th>
				<td colspan="3">
				<input type="text" name="searchFileName" id="searchFileName" class="w40p" value="${param.searchFileName}">
				<select name="searchFileNameGb" class="w20p">
					<option value="M">Matches</option>
                    <option value="S" selected="selected">Substring</option>
				</select>
				</td>
			</tr>
	</table>	

	<div class="under_tbl_area">
		<button class="btn_type_a" id="btnSearch">Search<span></span></button>
		<button class="btn_type_a" id="btnOptionClear">Clear<span></span></button>
	</div>

	<div class="space"></div>

	<div class="top_tbl_area">
		<button class="btn_type_a" id="btnSelectDownload">Select<span></span></button>	
		<button class="btn_type_a" id="btnAllDownload">All Select<span></span></button>
		<input type="hidden" id="pageSize" name="pageSize" value="${pMap.pageSize}"/>
		<emp:pageNavi pageList="${contentProcessList}" doSubmit="naviSearchList();" styleClass="pageNum" pageNaviSize="${pMap.pageSize}"/>
	</div>
	<table class="tbl_type_a">
		<colgroup>
			<col width="5%">
			<col width="5%">
			<col width="*">
			<col width="7%">
			<col width="7%">
			<col width="26%">
			<col width="10%">
			<col width="7%">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" name="checkAll" id="checkAll" onclick="javascript:onClickChkAll(this);" value="N"></th>
				<th>Rev<br/>
					No.</th>
				<th>File Name</th>
				<th>Manual<br>
					Type</th>
				<th>Content<br/>Type</th>
				<th>Title</th>
				<th>Release Date</th>
				<th>Author</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${contentProcessList != null && fn:length(contentProcessList) > 0}">  
			<c:forEach var="contentProcessInfo" items="${contentProcessList}" varStatus="idx">
			<tr>
				<td><input name="contentCheck" id="contentCheck" type="checkbox" value="${contentProcessInfo.masterWorkorderContentsId}${contentProcessInfo.masterWOCntsImgId}"></td>
				<td>${contentProcessInfo.masterWOCntsRevzNo}</td>
				<td><div class="dv_file_name"><span>${contentProcessInfo.fileName}</span></div></td>
				<td><emp:value mainCode="2000" subCode="${contentProcessInfo.WOCntsDocSubtypCd}" /></td>
				<td>${contentProcessInfo.masterWOCntsTypCd}</td>
				<td><div class="dv_file_name"><span>${contentProcessInfo.titleName}</span></div></td>	
				<td><div class="dv_date"><span><emp:date value="${contentProcessInfo.releaseDate}" format="yyyy-MM-dd HH:mm:ss" /></span></div></td>
				<td>${contentProcessInfo.lastUpdatedByV}</td>
			</tr>
			</c:forEach>
			</c:if>
			<c:if test="${contentProcessList == null || fn:length(contentProcessList) == 0}">  
			<tr > 
				<td colspan="9"> 해당 내역이 없습니다.&nbsp;	</td>
			</tr>
			</c:if>
		</tbody>
	</table>	
	<div class="under_tbl_area_b">
		<emp:pageNavi pageList="${contentProcessList}" doSubmit="naviSearchList();" styleClass="pageNum" pageNaviSize="${pMap.pageSize}"/>
	</div>
<!-- /Body-content -->
</form>
<iframe width="0" height="0" frameborder="0" marginwidth="0" marginheight="0" name="downloadFrame"></iframe>