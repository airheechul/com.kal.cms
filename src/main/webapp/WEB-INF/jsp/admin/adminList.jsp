<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@ taglib prefix="emp" uri="/mpframe-tags" --%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.math.BigDecimal"%>

<script type="text/javascript">

	$(document).ready(function() {
		$("#btnCreate").click(function(){
			location.href = "${pageContext.request.contextPath}/admin/adminCreate.do?topNum=4";
			
			return false;
		});
		
		$("#btnDelete").click(function(){
			adminDelete();
			
			return false;
		});
		
	});	// end of ready	

	/**
	 *  리스트 조회
	 */	
	function searchList(pageIndex) {
		
   	    document.adminForm.pageIndex.value = pageIndex;  
		document.adminForm.action = "${pageContext.request.contextPath}/admin/adminList.do";
		document.adminForm.submit();
	}
	/**
	 *  페이지 이동
	 */	
	function naviAdminList() {
		document.adminForm.action = "${pageContext.request.contextPath}/admin/adminList.do";
		document.adminForm.submit();
	}	
	
	function goAdminView(userId){
		document.adminForm.action = "${pageContext.request.contextPath}/admin/adminView.do?userId="+userId;
		document.adminForm.submit();
	}
	
	function onClickChkAll(obj) {
		if(obj.checked) {
			$('input:checkbox').attr('checked',true);
		} else {
			$('input:checkbox').attr('checked',false);
		}
	}
	
	function fnRowCnt(){	
		chkN = 0;
		$("input[name='userId']").each(function(idx){
			if(this.checked ) {
				chkN++;
			}
		});

	    if(chkN == 0){
	        alert("대상을 선택하여 주십시오.");
	        return false;
	    }
	}

	/**
	 * 삭제 버튼 클릭시
	 */
	function adminDelete(){
		if(fnRowCnt() != false){
			message = "선택된 대상을 삭제 하시겠습니까?";

			if(confirm(message) == true){
				
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/admin/adminDelete.do",
					cache : false, 
					data : $("#adminForm").serialize(),
					dataType: "json",
					success : function(data){
			
						if(data.status == true) {
							alert("삭제되었습니다.");
							
							naviAdminList();
						} else {
							alert("정보 수정이 실패하였습니다.");
						}
					},
					error: function(data){
						alert("에러가 발생하였습니다.");
					}
				});
			}
		}
	}
</script>

<html>
<body>
	<div id="lnb">
		<ul>
			<li><a href="${pageContext.request.contextPath}/system/codeList.do?topNum=4">코드관리</a> <span>&#8250;</span></li>
			<li><a href="${pageContext.request.contextPath}/system/templateList.do?topNum=4">Template 관리</a> <span>&#8250;</span></li>
			<li class="visited"><a href="${pageContext.request.contextPath}/admin/adminList.do?topNum=4">관리자 계정 관리</a> <span>&#8250;</span></li>
		</ul>
	</div>
	<div id="contentBody">
	
	<h3><img src="/images/blue/tit_manageAdmin.png" alt="관리자 계정 관리"></h3>

	<form:form id="adminForm" name="adminForm" action="" method="post">
		<input type="hidden" name="act" value="search"/>
		<table class="tbl_type_b">
			<colgroup>
				<col width="15%">
				<col width="">
				<col width="15%">
			</colgroup>
			<tr>
				<th>선택</th>
				<td>
					<select name="searchKeywordType" id="searchKeywordType" class="w20p">
						<option value="NM" <c:if test="${searchKeywordVO.searchKeywordType == 'NM'}">selected='selected'</c:if>>이름</option>
						<option value="ID" <c:if test="${searchKeywordVO.searchKeywordType == 'ID'}">selected='selected'</c:if>>아이디</option>
					</select>
					<input name="searchKeyword" type="text" class="w70p" value="${searchKeywordVO.searchKeyword}">
				</td>
				<td>
					<button class="btn_type_a">Search<span></span></button>
				</td>
			</tr>
		</table>	
	
		<div class="under_tbl_area">
			<table width="100%">
				<tr>
					<td align="right">
						<button class="btn_type_a" id="btnCreate">등록<span></span></button>
						<button class="btn_type_a" id="btnDelete">삭제<span></span></button>					
					</td>
				</tr>
			</table>
		</div>

		<table class="tbl_type_a">
			<colgroup>
				<col width="6%">
				<col width="13%">
				<col width="21%">
				<col width="">
				<col width="15%">
				<col width="15%">
			</colgroup>
			<thead>
				<tr>
					<th><input type="checkbox" name="checkAll" onclick="javascript:onClickChkAll(this);" value="Y"></th>
					<th>No.</th>
					<th>아이디</th>
					<th>이름</th>
					<th>권한</th>
					<th>등록일</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${adminList != null && fn:length(adminList) > 0}">  
				<c:forEach var="adminInfo" items="${adminList}" varStatus="idx">		
					<tr>
						<td><input type="checkbox" name="userId" value="${adminInfo.userId}"></td>
						<td>${adminInfo.rowIdx}</td>
						<td><a href="javascript:goAdminView('${adminInfo.userId}');">${adminInfo.userId}</a></td>
						<td>${adminInfo.userLocalName}</td>
						<td>
							<c:forEach var="roleInfo" items="${roleList}">
								<c:if test="${adminInfo.roleCode == roleInfo.lookupCode}">${roleInfo.lookupName}</c:if>
							</c:forEach>
						</td>
						<td><fmt:formatDate value="${adminInfo.creationDate}" pattern="yyyy-MM-dd"/></td>
					</tr>
				</c:forEach>
				</c:if>
				<c:if test="${adminList == null || fn:length(adminList) == 0}">  
				<tr > 
					<td colspan="7"> 해당 내역이 없습니다.&nbsp;	</td>
				</tr>
				</c:if>
			</tbody>
		</table>
		
		<input type="hidden" name="pageIndex">
	</form:form>
	
	<table width = "100%">
		<tr>
			<td align='center'>
	        	<div id="paging">
	        		<ui:pagination paginationInfo="${paginationInfo}" type="text" jsFunction="searchList" />
	        	</div>
			</td>
		</tr>
	</table>
</body>
</html>