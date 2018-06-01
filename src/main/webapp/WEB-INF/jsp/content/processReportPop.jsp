<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	var errorOnce = true;

	$(document).ready(function(){
		
		$(".btn_popup_close").click(function(){
			popClose();
			return false;
		});
		
		$(".btn_type_b").click(function(){
			alert("처리중입니다.");
			return false;
		});			
		
		setTimeout("init();", 300);
	});
	
	function popClose(){
		self.close();
	}
	
	function init(){
		createReport(0);
		createReport(1);
	}
	
	function toggleAlertwrap(flag){
		if(flag){
			$("#popup_type_c").show();
			$("#alertwrap").hide();
		} else {
			$("#popup_type_c").hide();
			$("#alertwrap").show();
		}
	}
	
	function createReport(idx){
		var $report_type = $(".btn_type_b").eq(idx);
		var taskId = '${param.masterWOCntsTskId}';
		var reportType = $report_type.attr("id");
		var url = "${pageContext.request.contextPath}/content/processCreateReport.do";

		$.ajax({
			async : true,
			type : "POST",
			url : url,
			data : "masterWOCntsTskId="+taskId+"&reportType="+reportType,
			dataType: "json",
			success : function(json) {
				if(json.result == 'OK'){
					$report_type.unbind('click');
					$report_type.bind('click',function(event){	
						downloadFile(json.fileName, json.urlAddress);
						return false;
		            });		
					
					toggleAlertwrap(true);
				} else {
					if(errorOnce) {
						errorOnce = false;
						alert(json.result);
						
						var html = '<tr><td colspan="5">Report 파일 저리중 오류 입니다.&nbsp;</td></tr>';
						
						$(".popup_tbl_a").append(html);
						$(".trData").hide();
						
						toggleAlertwrap(true);
					}
				}
			},
			beforeSend : null,
			error : function(e) {
				alert(e.responseText);
				return false;
			}
		});
		
		return true;
	}
	
	function downloadFile(data1, data2){
		if(confirm("이 파일을 열거나 저장하시겠습니까?\n이름: "+data1+"\n유형: csv ")) {
			document.download.action = "${pageContext.request.contextPath}/content/downloadFile.do";
			document.download.fileOrgName.value = data1;
			document.download.fileFullPath.value = data2;
			document.download.submit();
		}
	}	
</script>
<!-- center-c-c -->
<!-- Body-content -->
<form id="reportForm" name="reportForm"  method="post"> 
	<input type="hidden" name="topNum" value="${param.topNum}"/>
	<input type="hidden" name="masterWOCntsTskId" value="${param.masterWOCntsTskId}"/>
<div id="popup_type_c" style="display:none;">
	<h1>Report</h1>
	<table class="popup_tbl_a">
		<colgroup>
			<col width="*" />
			<col width="20%" />
			<col width="20%" />
			<col width="20%" />
			<col width="24%" />
		</colgroup>
		<thead>
			<tr>
				<th>Model</th>
				<th>Manual Type</th>
				<th>Content Type</th>
				<th>Author</th>
				<th>Download File</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${reportList != null && fn:length(reportList) > 0}">
			<c:forEach var="reportInfo" items="${reportList}" varStatus="idx">
			<tr class="trData" style="cursor:pointer">
				<td>${reportInfo.maintDocModelCode}</td>	
				<td><emp:value mainCode="2000" subCode="${reportInfo.WOCntsDocSubtypCd}" /></td>
				<td>${reportInfo.reportType}</td>							
				<td>${reportInfo.createdBy}</td>
				<td><button class="btn_type_b" id="${reportInfo.reportType}" data1="" data2="">Download<span></span></button></td>
			</tr>
			</c:forEach>
		</c:if>
		<c:if test="${reportList == null || fn:length(reportList) == 0}">
			<tr > 
				<td colspan="5"> 해당 내역이 없습니다.&nbsp;	</td>
			</tr>
		</c:if>
		</tbody>
	</table>
</div>
</form>
<div id="alertwrap">
	<h1>Content Report <button class="btn_popup_close"><img src="${pageContext.request.contextPath}/html/images/${skin}/ico_close.png" alt="close"></button></h1>
	<p class="comment">
	<img src="${pageContext.request.contextPath}/html/images/${skin}/ajax-loader.gif" width="50" height="50" alt="load"> <br>파일 처리중입니다. 조금만 기다려주세요.	
	</p>	
</div>
<!-- /Body-content -->
<form id="download" name="download"  method="post" target="downloadFrame">
	<input type="hidden" name="fileFullPath" value=""/>
	<input type="hidden" name="fileName" value=""/>
	<input type="hidden" name="fileOrgName" value=""/>
</form>
<iframe width="0" height="0" frameborder="0" marginwidth="0" marginheight="0" name="downloadFrame"></iframe>