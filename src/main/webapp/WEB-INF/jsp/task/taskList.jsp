<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="org.apache.commons.logging.Log"%>
<%@ page import="org.apache.commons.logging.LogFactory"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<script type="text/javascript">
	$(document).ready(function() {
		Common.importJS("datepicker.js");

		toggleManualTypeOption();

		if ($("select[name=searchTaskType] option:selected").val() == "5006") {
			$("#request_title_id").text("Request id.");
			$("#isDescriptionSearch").val("Y");
		} else {
			$("#request_title_id").text("Request.");
			$("#isDescriptionSearch").val("N");
		}

		if ('${param.taskId}' != '') {
			$(".subCell").show();

			toggleMinus($(".aToggoleSub img"), false);
			toggleMinus($(":first-child", $(".aToggoleAllSub")), false);
			$(".rowTask").removeClass("subOff");
			$(".aToggoleAllSub").removeClass("on");
		}

		$(".aToggoleSub").click(function() {
			if ($(this).parent().parent().hasClass("subOff")) {
				$(this).parent().parent().removeClass("subOff");
				$("." + $(this).attr("id")).show();
				toggleMinus($(":first-child", this), false);
			} else {
				$(this).parent().parent().addClass("subOff");
				$("." + $(this).attr("id")).hide();
				toggleMinus($(":first-child", this), true);
			}

			return false;
		});

		$(".aToggoleAllSub").click(function() {
			if ($(this).hasClass("on")) {
				toggleMinus($(".aToggoleSub img"), false);
				toggleMinus($(":first-child", this), false);
				$(".rowTask").removeClass("subOff");
				$(".subRow").show();
				$(this).removeClass("on");
			} else {
				toggleMinus($(".aToggoleSub img"), true);
				toggleMinus($(":first-child", this), true);
				$(".rowTask").addClass("subOff");
				$(".subRow").hide();
				$(this).addClass("on");
			}

			return false;
		});

		$("#btnResume").click(function() {
			taskStatusCheck("R");
			return false;
		});

		$("#btnAbort").click(function() {
			taskStatusCheck("A");
			return false;
		});

		$("#btnDelete").click(function() {
			taskStatusCheck("D");
			return false;
		});

		$(".btnDetail").click(function() {
			detailPop($(this).attr("id"));
			return false;
		});

		$("#searchTermsBox").keyup(function(e) {
			if (e.keyCode == 13) {
				searchList('1');
				return false;
			}
		});

		$('#btnSearch').click(function() {
			searchList('1');
			return false;
		});

		$('#btnOptionClear').click(function() {
			searchOptionClear();
			return false;
		});

		$('.pageNavi').change(function() {
			searchList($("option:selected", this).val());
			return false;
		});

		$('.btnPagePrev, .btnPageNext').click(function() {
			searchList($(this).val());
			return false;
		});

		$('#searchMaintDocModelCode').change(function() {
			toggleManualTypeOption();

			return false;
		});

		$("#searchTaskType").change(function() {
			if ($(this).val() == "5006") {
				$("#request_title_id").text("Request id.");
				$("#isDescriptionSearch").val("Y");
			} else {
				$("#request_title_id").text("Request.");
				$("#isDescriptionSearch").val("N");
			}
		});

		if ("${sessionScope.LOGIN_SESSION.userDivisionCd}" == "50005") {
			$("#tr01").css("display", "none");
			$("#request_title_id").text("Request id.");
			$("#searchTaskType").html("<option value='5006'>PDF[제조]</option>");
		}

	});

	function toggleMinus($obj, flag) {
		var srcBase = "${pageContext.request.contextPath}/images/blue/";

		if (flag) {
			$obj.attr("src", srcBase + "plus.gif");
		} else {
			$obj.attr("src", srcBase + "minus.gif");
		}
	}

	function toggleManualTypeOption() {
		var sgb = $("#searchMaintDocModelCode option:selected").val();
		var mgb = $("#searchWOCntsDocSubtypCd option:selected").val();

		$('#searchWOCntsDocSubtypCd').children().each(function() {
			if ($(this).val() != "KOR" && $(this).val() != "") {
				$(this).remove();
			}
		});

		if ("" != sgb) {
			if (sgb.indexOf("A3") == 0) {
				$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]")
						.before('<option value="AMM">AMM</option>');
				$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]")
						.before('<option value="TSM">TSM</option>');

			} else if (sgb.indexOf("B7") == 0) {
				$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]")
						.before('<option value="AMM">AMM</option>');
				$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]")
						.before('<option value="FRMFIM">FRMFIM</option>');
			}

		} else {
			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before(
					'<option value="AMM">AMM</option>');
			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before(
					'<option value="TSM">TSM</option>');
			$("select[name=searchWOCntsDocSubtypCd] option[value=KOR]").before(
					'<option value="FRMFIM">FRMFIM</option>');
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

	function detailPop(id) {
		var url = "${pageContext.request.contextPath}/task/taskDetailLogPop.do?detailId="
				+ id;
		var popupProps = "dialogWidth:" + 700 + "px;,dialogHeight:" + 550
				+ "px;resizable=no;scroll=no;location=no;";
		window.name = "popup";
		var popupWin = window.showModalDialog(url, this, popupProps);
	}

	/**
	 *  리스트 조회
	 */
	function searchList(pageIndex) {
		document.taskform.pageIndex.value = pageIndex;
		document.taskform.action = "${pageContext.request.contextPath}/task/taskList.do";
		document.taskform.submit();
	}
	/**
	 *  페이지 이동
	 */
	function naviTaskList() {
		document.taskform.action = "${pageContext.request.contextPath}/task/taskList.do";
		document.taskform.submit();
	}

	/* clear search pannel */
	function searchOptionClear() {
		/* clear selectBox */
		$("#searchTermsBox select").each(function(idx) {
			$(this).children().selected = false;
			$(this).children().eq(0).attr("selected", "selected");
		});

		/* clear textBox */
		$("#searchTermsBox input").each(function(idx) {
			$(this).val("");
		});
	}

	function taskStatusCheck(act) {
		document.taskform.act.value = act;

		if ($(".tm_check:checked").length == 0) {
			alert("선택된 리스트가 없습니다.");
			return false;
		}

		$.ajax({
			type : "POST",
			url : "${pageContext.request.contextPath}/task/taskStatusCheck.do",
			cache : false,
			data : $("#taskform").serialize(),
			dataType : "json",
			success : function(data) {
				if (data.result) {

					if (act == 'D') {
						if (confirm("삭제하시겠습니까?")) {
							taskManage(act);
						}
					} else {
						taskManage(act);
					}

				} else {
					alert(data.errorMsg);

					document.taskform.act.value = "search";
					return false;
				}
			},
			error : function(data) {
				alert("에러가 발생하였습니다.");
				document.taskform.act.value = "search";
				return false;
			}
		});
	}

	function taskManage(act) {
		var actUrl = "";
		if (act == 'R') {
			actUrl = "${pageContext.request.contextPath}/task/taskResume.do";
		} else if (act == 'A') {
			actUrl = "${pageContext.request.contextPath}/task/taskAbort.do";
		} else if (act == 'D') {
			actUrl = "${pageContext.request.contextPath}/task/taskDelete.do";
		} else {
			return false;
		}

		$.ajax({
			type : "POST",
			url : actUrl,
			cache : false,
			data : $("#taskform").serialize(),
			dataType : "json",
			success : function(data) {
				if (data.result) {
					alert("반영되었습니다.");
				} else {
					alert(data.errorMsg);
				}

				document.taskform.act.value = "search";
				naviTaskList();
				return false;
			},
			error : function(data) {
				alert("에러가 발생하였습니다.");
				document.taskform.act.value = "search";
				return false;
			}
		});
	}
</script>
<form:form id="taskform" name="taskform" method="get">
<%-- 	<input type="hidden" id="currentPage" name="currentPage"
		value="${searchMap.currentPage}" />  
	<input type="hidden"
		name="topNum" value="${searchMap.topNum}" /> 
	<input type="hidden"
		name="leftNum" value="${searchMap.leftNum}" />--%>
	<input type="hidden"
		name="act" value="search" /> 
	<input type="hidden"
		name="isDescriptionSearch" id="isDescriptionSearch" />

	<table class="tbl_type_b" id="searchTermsBox">
		<colgroup>
			<col width="17%">
			<col width="33%">
			<col width="17%">
			<col width="">
		</colgroup>
		<tr id="tr01">
			<th>Model</th>
			<td><select id="searchMaintDocModelCode"
				name="searchMaintDocModelCode">
					<option value="" selected>- 선택 -</option>

					<c:forEach var="LOV" items="${LOVs}">
						<c:if test="${LOV.lookupTypeName == 1000}">
							<option value='${LOV.lookupCode}'
								<c:if test="${LOV.lookupCode == searchMap.searchMaintDocModelCode}">selected='selected'</c:if>>${LOV.lookupName}</option>
						</c:if>
					</c:forEach>
			</select></td>
			<th>Manual Type</th>
			<td><select id="searchWOCntsDocSubtypCd"
				name="searchWOCntsDocSubtypCd">
					<option value="" selected>- 선택 -</option>

					<c:forEach var="LOV" items="${LOVs}">
						<c:if test="${LOV.lookupTypeName == 2000}">
							<option value='${LOV.lookupCode}'
								<c:if test="${LOV.lookupCode == searchMap.searchWOCntsDocSubtypCd}">selected='selected'</c:if>>${LOV.lookupName}</option>
						</c:if>
					</c:forEach>
			</select></td>
		</tr>
		<tr>
			<th>Task</th>
			<td><select id="searchTaskType" name="searchTaskType">
					<option value="" selected>- 선택 -</option>

					<c:forEach var="LOV" items="${LOVs}">
						<c:if test="${LOV.lookupTypeName == 5000}">
							<option value='${LOV.lookupCode}'
								<c:if test="${LOV.lookupCode == searchMap.searchTaskType}">selected='selected'</c:if>>${LOV.lookupName}</option>
						</c:if>
					</c:forEach>
			</select></td>

			<th><span id="request_title_id">Request.</span></th>
			<td><input type="text" name="searchRequest" id="searchRequest"
				class="w40p" value="${searchMap.searchRequest}" /></td>
		</tr>
		<tr>
			<th>Date</th>
			<td colspan="3"><label for="searchStartDt">From</label> <input
				type="text" name="searchStartDt" id="searchStartDt"
				value="${searchMap.searchStartDt}" class="date"
				style="width: 100px;" readonly /> &nbsp;&nbsp;~&nbsp;&nbsp; <label
				for="searchEndDt">To</label> <input type="text" name="searchEndDt"
				id="searchEndDt" value="${searchMap.searchEndDt}" class="date"
				style="width: 100px;" readonly /></td>
		</tr>
		<tr>
			<th>Filter</th>
			<td colspan="3"><select id="searchMasterWOCntsTskSttsCd"
				name="searchMasterWOCntsTskSttsCd">
					<option value="" selected>- 선택 -</option>

					<c:forEach var="LOV" items="${LOVs}">
						<c:if test="${LOV.lookupTypeName == 7000}">
							<option value='${LOV.lookupCode}'
								<c:if test="${LOV.lookupCode == searchMap.searchMasterWOCntsTskSttsCd}">selected='selected'</c:if>>${LOV.lookupName}</option>
						</c:if>
					</c:forEach>
			</select></td>
		</tr>
	</table>

	<div class="under_tbl_area">
		<button class="btn_type_a" id="btnSearch">
			Search<span></span>
		</button>
		<button class="btn_type_a" id="btnOptionClear">
			Clear<span></span>
		</button>
	</div>

	<div class="space"></div>

	<div class="top_tbl_area">
		<c:if
			test="${sessionScope.UserAccount.roleCode == 50001 || sessionScope.UserAccount.roleCode == 50002}">
			<button class="btn_type_a btnBig" id="btnResume">
				Resume<span></span>
			</button>
			<button class="btn_type_a" id="btnAbort">
				Abort<span></span>
			</button>
			<button class="btn_type_a" id="btnDelete">
				Delete<span></span>
			</button>
		</c:if>

		<input type="hidden" id="pageSize" name="pageSize" value="${pMap.pageSize}"/>

	</div>

	<table class="tbl_type_a">
		<colgroup>
			<col width="4%">
			<col width="4%">
			<col width="8%">
			<col width="6%">
			<col width="12%">
			<col width="*">
			<col width="7%">
			<col width="8%">
			<col width="8%">
			<col width="8%">
			<col width="8%">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" name="checkAll"
					onclick="javascript:onClickChkAll(this);" /></th>
				<th><a class="aToggoleAllSub on" href="javascript:void(0);">
						<img
						src="${pageContext.request.contextPath}/images/blue/plus.gif"
						width="15px" height="15px" alt="openSubTask">
				</a></th>
				<th>Model</th>
				<th>Manual<br> Type
				</th>
				<th>Task</th>
				<th>Description</th>
				<th>Author</th>
				<th>Start Time</th>
				<th>End Time</th>
				<th>Status</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${taskList != null && fn:length(taskList) > 0}">
				<c:forEach var="taskInfo" items="${taskList}" varStatus="idx">
					<c:if test="${taskInfo.subTaskId == null}">
						<tr class="rowTask subOff">
							<td><input class="tm_check" type="checkbox" name="checkedId"
								value="${taskInfo.taskId}"></td>
							<td><a class="aToggoleSub" id="task_${taskInfo.taskId}"
								href="javascript:void(0);"> <img
									src="${pageContext.request.contextPath}/images/blue/plus.gif"
									width="15px" height="15px" alt="openSubTask"></a></td>
							<td>${taskInfo.model}</td>
							<td>${taskInfo.manualType}</td>
							<td>
								<c:forEach var="LOV" items="${LOVs}">
									<c:if test="${LOV.lookupTypeName == 5000}">
										<c:if test="${LOV.lookupCode == taskInfo.taskType}">${LOV.lookupName}</c:if>
									</c:if>
								</c:forEach>
							</td>
							<td><div class="dv_file_name">
									<span>${taskInfo.description}</span>
								</div></td>
							<td><c:choose>
									<c:when
										test="${taskInfo.createdByV == null || taskInfo.createdByV == ''}">${taskInfo.createdBy}</c:when>
									<c:otherwise>${taskInfo.createdByV}</c:otherwise>
								</c:choose></td>
							<td><div class="dv_date"><fmt:formatDate value="${taskInfo.startDate}" pattern="yyyy-MM-dd KK:mm:ss"/></span>
								</div></td>
							<td><div class="dv_date"><fmt:formatDate value="${taskInfo.endDate}" pattern="yyyy-MM-dd KK:mm:ss"/></span>
								</div></td>
							<td>
								<c:forEach var="LOV" items="${LOVs}">
									<c:if test="${LOV.lookupTypeName == 7000}">
										<c:if test="${LOV.lookupCode == taskInfo.status}">${LOV.lookupName}</c:if>
									</c:if>
								</c:forEach>
							</td>
							<td></td>
						</tr>
					</c:if>

					<c:if test="${taskInfo.subTaskId != null}">
						<c:if test="${taskInfo.subTaskRank == '1'}">
							<tr class="subCell subRow subCellTop task_${taskInfo.taskId}"
								style="display: none;">
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td class="subCell">${taskInfo.model}</td>
								<td class="subCell">${taskInfo.manualType}</td>
								<td class="subCell"></td>
								<td class="subCell"><div class="dv_file_name">
										<span>${taskInfo.subTaskDesc}</span>
									</div></td>
								<td class="subCell"></td>
								<td><div class="dv_date">
										<span><fmt:formatDate value="${taskInfo.subTaskStartDate}" pattern="yyyy-MM-dd KK:mm:ss"/></span>
									</div></td>
								<td><div class="dv_date">
										<span><fmt:formatDate value="${taskInfo.subTaskEndDate}" pattern="yyyy-MM-dd KK:mm:ss"/></span>
									</div></td>
								<td class="subCell">
									<c:forEach var="LOV" items="${LOVs}">
										<c:if test="${LOV.lookupTypeName == 7000}">
											<c:if test="${LOV.lookupCode == taskInfo.subTaskStatus}">${LOV.lookupName}</c:if>
										</c:if>
									</c:forEach>
								</td>
								<td class="subCell"><button class="btn_type_c btnDetail"
										id="${taskInfo.subTaskId}">
										Detail<span></span>
									</button></td>
							</tr>
						</c:if>
						<c:if test="${taskInfo.subTaskRank != '1'}">
							<tr class="subCell subRow task_${taskInfo.taskId}"
								style="display: none;">
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<td>${taskInfo.model}</td>
								<td>${taskInfo.manualType}</td>
								<td></td>
								<td class="subCell"><div class="dv_file_name">
										<span>${taskInfo.subTaskDesc}</span>
									</div></td>
								<td></td>
								<td><div class="dv_date">
										<span><fmt:formatDate value="${taskInfo.subTaskStartDate}" pattern="yyyy-MM-dd KK:mm:ss"/></span>
									</div></td>
								<td><div class="dv_date">
										<span><fmt:formatDate value="${taskInfo.subTaskEndDate}" pattern="yyyy-MM-dd KK:mm:ss"/></span>
									</div></td>
								<td>
									<c:forEach var="LOV" items="${LOVs}">
										<c:if test="${LOV.lookupTypeName == 7000}">
											<c:if test="${LOV.lookupCode == taskInfo.subTaskStatus}">${LOV.lookupName}</c:if>
										</c:if>
									</c:forEach>
								</td>
								<td><button class="btn_type_c btnDetail"
										id="${taskInfo.subTaskId}">
										Detail<span></span>
									</button></td>
							</tr>
						</c:if>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${taskList == null || fn:length(taskList) == 0}">
				<tr>
					<td colspan="11">해당 내역이 없습니다.&nbsp;</td>
				</tr>
			</c:if>
		</tbody>
	</table>
<%-- 	<div class="under_tbl_area_b">
		<table width="100%">
			<tr>
				<td align='center'>
					<div id="paging">
						<ui:pagination paginationInfo="${paginationInfo}" type="text"
							jsFunction="searchList" />
					</div>
				</td>
			</tr>
		</table>
	</div> --%>
	
	<input type="hidden" name="pageIndex">
	
</form:form>
	<div class="under_tbl_area_b">
		<table width="100%">
			<tr>
				<td align='center'>
					<div id="paging">
						<ui:pagination paginationInfo="${paginationInfo}" type="text"
							jsFunction="searchList" />
					</div>
				</td>
			</tr>
		</table>
	</div>