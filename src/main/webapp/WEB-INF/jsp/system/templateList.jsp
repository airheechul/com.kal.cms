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
	$(document)
			.ready(
					function() {
						$("#btnCreate")
								.click(
										function() {
											location.href = "${pageContext.request.contextPath}/system/templateCreate.do?topNum=4";
											return false;
										});

						$(".btn_modify").click(function() {
							var id = $(this).attr("id");
							moveList(id);

							return false;
						});

					}); // end of ready	

	function moveList(id) {
		var fo = document.template;
		fo.action = "${pageContext.request.contextPath}/system/templateModify.do";
		fo.act.value = "modify";
		fo.masterWOCntsTmptId.value = id;
		fo.submit();
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
	<form id="template" name="template" method="post">
		<input type="hidden" name="topNum" value="${param.topNum}" /> <input
			type="hidden" name="masterWOCntsTmptId" value="" /> <input
			type="hidden" name="act" value="" />
		<table class="tbl_type_a">
			<colgroup>
				<col width="12%">
				<col width="">
				<col width="12%">
				<col width="12%">
				<col width="12%">
				<col width="10%">
			</colgroup>
			<thead>
				<tr>
					<th>구분</th>
					<th>Template Name</th>
					<th>Author</th>
					<th>Use</th>
					<th>Date</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${templateList != null && fn:length(templateList) > 0}">
					<c:forEach var="templateInfo" items="${templateList}"
						varStatus="row">
						<tr>
							<td><select id="masterWOCntsTmptTypCd"
								name="masterWOCntsTmptTypCd">
									<option value="" disabled selected hidden>----선택----</option>
									<c:forEach var="contentTypeList" items="${contentTypeList}">
										<option value='${contentTypeList.lookupCode}'
											<c:if test="${templateInfo.masterWOCntsTmptTypCd == contentTypeList.lookupCode}">selected</c:if>>${contentTypeList.lookupName}</option>
									</c:forEach>
							</select></td>
							<td>${templateInfo.templateName}</td>
							<td>${templateInfo.lastUpdatedBy}</td>
							<td>${templateInfo.useYn}</td>
							<td><emp:date value="${templateInfo.lastUpdateDate}"
									format="yyyy-MM-dd" /></td>
							<td><button id="${templateInfo.masterWOCntsTmptId}"
									class="btn_type_c btn_modify">
									수정<span></span>
								</button></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${templateList == null || fn:length(templateList) == 0}">
					<tr>
						<td colspan="6">해당 내역이 없습니다.&nbsp;</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</form>
	<div class="under_tbl_area">
		<button id="btnCreate" class="btn_type_a">
			등록<span></span>
		</button>
	</div>
</div>