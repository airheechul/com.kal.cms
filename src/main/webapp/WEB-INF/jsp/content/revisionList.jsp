<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script type="text/javascript">
	$(document).ready(function() {
		$(".btnPreview").click(function(){
			var contentId = $(this).attr("data1");
			openPreviewPop(contentId);
			return false;
		});
		
		$(".btnEdit").click(function(){
			uploadPop();
			return false;
		});
		
		$(".btnXmetal").click(function(){
			openInXMetaL("1111111", "1111111", $("#webDavPath").val(), $("#maintDocModelCode").val(), $("#wOCntsDocSubtypCd").val());
			return false;
		});
		
		$(".btnCancle").click(function(){
			searchList(1);
			return false;
		});
	});

	/**
	 *  리스트
	 */
	function searchList(currentPage) {
		if (currentPage != "") {
			document.contentForm.currentPage.value = currentPage;
		}
		document.contentForm.action = "${pageContext.request.contextPath}/content/searchList.do";
		document.contentForm.submit();
	}
	
	/**
	 *  HTML Preview
	 */
	function openPreviewPop(contentId) {
		alert("서비스 준비중입니다.");
		/*
		var pop = window.open(
						"${pageContext.request.contextPath}/content/previewHtmlPop.do?contentId="+contentId,
						"previewHtmlPop",
						"height=250,width=490,menubar=no,toolbar=no,location=no,resizable=no,status=no,scrollbars=no,top=300,left=700");
		if (pop) {
			pop.focus();
		}
		*/
	}
		
	// TODO 추후 xMetal 연동 후 수정
	function uploadPop() {
		var url = "${pageContext.request.contextPath}/content/contentUploadPop.do?addType=revise";
		var popupProps = "dialogWidth:" + 700 + "px;,dialogHeight:" + 550 + "px;resizable=no;scroll=no;location=no;";
		window.name = "uploadPop";
		var popupWin = window.showModalDialog(url, this, popupProps);
	}
	
	function downloadFile(data1, data2){
		if(confirm("이 파일을 열거나 저장하시겠습니까?\n이름: "+data1+"\n유형: xml ")) {
			document.download.action = "${pageContext.request.contextPath}/content/downloadFile.do";
			document.download.fileOrgName.value = data1;
			document.download.fileFullPath.value = data2;
			document.download.target = "downloadFrame";
			document.download.submit();
		}
	}
	
	function openInXMetaL(docName, dID, docUrl, modelID, manualType) {
		var hostName = window.location.hostname;
		var imageFileServiceBaseUrl = "";
		if (hostName.indexOf("10.0.42.78")>-1){
			imageFileServiceBaseUrl = "http://erpdev.koreanair.com:8015/kalppl/hq/images/";
		} else if (hostName.indexOf("pplstgap.koreanair.com")>-1){
			imageFileServiceBaseUrl = "http://erpint.koreanair.com:8070/kalppl/hq/images/";
		} else {
			imageFileServiceBaseUrl = "http://erp.koreanair.com:8080/kalppl/hq/images/";
		}
		var previewURL = docUrl;
		
		var imageViewBaseUrl = imageFileServiceBaseUrl + "&model="+modelID+"&manualType="+manualType+"&fileName=";
		var documentPreviewURL = previewURL+"?dDocName=" + docName ; //+ "&dID="+dID;

		var xmetal = null;
		try {
			if (!xmetal) {
				// open the application
				xmetal = new ActiveXObject("XMetaL.Application");

				// wait till XMetaL has finished opening
				xmetal.PreventExit("xMetaL", "Cannot exit");
				while(!xmetal.InitComplete) {} // wait loop...
				xmetal.AllowExit("xMetaL"); // ok now allow exit again
			}


			var custProps = xmetal.CustomProperties;

			custProps.Add("dDocName",				docName);
			custProps.Add("dID",					dID);

			custProps.Add("ModelID", 				modelID);
			custProps.Add("ManualType",			manualType);

			custProps.Add("WebDavDocUrl",			docUrl);
			custProps.Add("ImageViewBaseUrl", 		imageViewBaseUrl);
			custProps.Add("DocumentPreviewUrl", 	documentPreviewURL);

			xmetal.Run("OpenWebDav");

		} catch (err) {
			//alert(err);
			// @TODO currently there is an issue with reopening
			// xmetal from the same web page.
			// refreshing the page fixes the problem, but we need to
			// catch and hide the error from the user here (till its fixed!)
			/*
			if (err) {
				if (xmetal != null) {
					//xmetal.alert(err);
					//xmetal.Documents.Close();
					//xmetal.Quit(0);
					//xmetal.CancelOpenDocument();
				} else {
					//alert(err);
				}
				xmetal = null;
			}
			*/
		} finally {
			if (xmetal != null) {
				xmetal = null;
			}
		}
	}
	
</script>
<!-- center-c-c -->
<!-- Body-content -->
<form id="contentForm" name="contentForm"  method="post"> 
	<input type="hidden" name="topNum" value="${param.topNum}"/>
	<input type="hidden" name="act" value="search"/>
	
	<input type="hidden" id="currentPage" name="currentPage" value="${param.currentPage}" />
	<input type="hidden" id="pageSize"    name="pageSize"    value="${param.pageSize}" />
	<input type="hidden" id="searchMaintDocModelCode"    name="searchMaintDocModelCode"    value="${param.searchMaintDocModelCode}" />
	<input type="hidden" id="searchWOCntsDocSubtypCd"    name="searchWOCntsDocSubtypCd"    value="${param.searchWOCntsDocSubtypCd}" />
	<input type="hidden" id="searchMasterWOCntsTypCd"    name="searchMasterWOCntsTypCd"    value="${param.searchMasterWOCntsTypCd}" />
	<input type="hidden" id="searchReleaseYn"    name="searchReleaseYn"    value="${param.searchReleaseYn}" />
	<input type="hidden" id="searchReviseType"    name="searchReviseType"    value="${param.searchReviseType}" />
	<input type="hidden" id="searchStartDt"    name="searchStartDt"    value="${param.searchStartDt}" />
	<input type="hidden" id="searchEndDt"    name="searchEndDt"    value="${param.searchEndDt}" />
	<input type="hidden" id="searchFileName"    name="searchFileName"    value="${param.searchFileName}" />
	<input type="hidden" id="webDavPath" name="webDavPath" value="${pMap.webDavPath}" />
	<input type="hidden" id="maintDocModelCode" name="maintDocModelCode" value="${pMap.maintDocModelCode}" />
	<input type="hidden" id="wOCntsDocSubtypCd" name="wOCntsDocSubtypCd" value="${pMap.wOCntsDocSubtypCd}" />
		
	<input type="hidden" id="searchValue" name="searchValue" value="${param.searchValue}"/>
	<input type="hidden" id="contentId" name="contentId" value=""/>
		<h2>
		<img src="${pageContext.request.contextPath}/html/images/${skin}/tit_revision.png" width="171" height="15" alt="content Revision">
		<button class="btn_type_a btnCancle">Cancel<span></span></button>
		</h2>

	<table class="tbl_type_a">
		<colgroup>
			<col width="7%" />
			<col width="*" />
			<col width="8%" />
			<col width="12%" />
			<col width="7%" />
			<col width="8%" />
			<col width="22%" />
		</colgroup>
		<thead>
			<tr>
				<th>Rev.<br/>No.</th>
				<th>File Name</th>
				<th>Manual<br/>Type</th>
				<th>Release Date</th>
				<th>Release</th>
				<th>Author</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${contentRevisionList != null && fn:length(contentRevisionList) > 0}">
			<c:forEach var="contentRevisionInfo" items="${contentRevisionList}" varStatus="idx">
			<tr style="cursor:pointer">
				<td>${contentRevisionInfo.masterWOCntsRevzNo}</td>	
				<td><div class="dv_file_name"><span>${contentRevisionInfo.fileName}</span></div></td>	
				<td><emp:value mainCode="2000" subCode="${contentRevisionInfo.WOCntsDocSubtypCd}" /></td>
				<td>
					<div class="dv_date"><span><emp:date value="${contentRevisionInfo.releaseDate}" format="yyyy-MM-dd HH:mm:ss" /></span></div>
				</td>
				<td>${contentRevisionInfo.releaseYn}</td>							
				<td>${contentRevisionInfo.lastUpdatedByV}</td>
				<td>
					<button class="btn_type_b btnPreview" data1="${contentRevisionInfo.masterWorkorderContentsId}">Preview<span></span></button>
				</td>
			</tr>
			
			<c:if test="${idx.index == (idx.count-1)}">
			
			<tr style="cursor:pointer">
				<td><c:if test="${contentRevisionInfo.releaseDate != null}">${contentRevisionInfo.masterWOCntsRevzNo +1}</c:if>
					<c:if test="${contentRevisionInfo.releaseDate == null}">${contentRevisionInfo.masterWOCntsRevzNo}</c:if>
				</td>	
				<td><div class="dv_file_name"><span><a href="javascript:downloadFile('${contentRevisionInfo.fileName}','${contentRevisionInfo.urlAddress}');">${contentRevisionInfo.fileName}</a></span></div> </td>	
				<td><emp:value mainCode="2000" subCode="${contentRevisionInfo.WOCntsDocSubtypCd}" /></td>
				<td>
				&nbsp;
				</td>
				<td>N</td>							
				<td>${contentRevisionInfo.lastUpdatedBy}</td>
				<td>
					<button class="btn_type_b btnPreview" data1="${contentRevisionInfo.masterWorkorderContentsId}">Preview<span></span></button>&nbsp;
					<button class="btn_type_c btnEdit">Edit<span></span></button>
					<button class="btn_type_c btnXmetal">Xmetal<span></span></button> 
				</td>
			</tr>
			</c:if>
			
			</c:forEach>
			
		</c:if>
		<c:if test="${contentRevisionList == null || fn:length(contentRevisionList) == 0}">  
		<tr > 
			<td colspan="9"> 해당 내역이 없습니다.&nbsp;	</td>
		</tr>
		</c:if>
		</tbody>
	</table>	
<!-- /Body-content -->
</form>

<form id="download" name="download"  method="post" target="downloadFrame">
	<input type="hidden" name="fileFullPath" value=""/>
	<input type="hidden" name="fileName" value=""/>
	<input type="hidden" name="fileOrgName" value=""/>
</form>

<iframe width="0" height="0" frameborder="0" marginwidth="0" marginheight="0" name="downloadFrame"></iframe>

