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
var dupCheckPassed = true;

$(document).ready(function() {
	toggleBtnSet("insert");
	
	toggleModelType(false);
	
	$("#btnRegistUpper").click(function(){
		initForm('');
		toggleBtnSet("insert");
		return false;
	});
	
	$("#btnRegistSub").click(function(){
		var upperCode = $("input[name='lookupTypeName']").val();
		initForm(upperCode);
		toggleBtnSet("insert");
		return false;
	});
	
	$("#btnDuplicateCheck").click(function(){
		var upperCode = $("input[name='lookupTypeName']").val();
		var code = $("input[name='lookupCode']").val();
		
		if($.trim(code) == ''){
			document.codeForm.lookupCode.focus();
			alert("코드를 입력하여 주십시오.");
			return false;
		}
		
		duplicateCheck(upperCode, code);
		return false;
	});
	
	$("#btnModify").click(function(){
		if(formCheck(false)){
			var conf = false;
			conf = confirm("수정하시겠습니까?");
			if(conf){
				modifyCode();
			}
		}
		
		return false;
	});
	
	$("#btnDelete").click(function(){
		var upperCd = $("input[name='lookupTypeName']").val();
		var subCd = $("input[name='lookupCode']").val();
		
		var conf = false;
		if(upperCd == subCd){
			conf = confirm("'"+upperCd+ "' 코드 그룹이 전체 삭제됩니다.\n 진행하시겠습니까?");
		} else {
			conf = confirm("코드를 삭제하시겠습니까?");
		}
		
		if(conf){
			deleteCode();
		}
		
		return false;
	});
	
	$("#btnRegist").click(function(){
		
		if(formCheck(true)){
			
			var conf = false;
			conf = confirm("등록하시겠습니까?");
			if(conf){
				registCode();
			}
		}
		
		return false;
	});
	
	$("input[name='lookupCode']").change(function(){
		dupCheckPassed = false;
	});
});

var selectedCode;

function toggleTree(upperCode, code, codeName, codeSeq, useYn, groupCnt){
	if($("#"+upperCode).parent().hasClass("sub")) {
		toggleBtnSet("insert");
		
		initForm();
		
		$("#"+upperCode).css("display","none");
		$("#"+upperCode).parent().removeClass("sub");	
		var height = "24px";	
		$("#"+upperCode).parent().css("height",height);
	} else {
		toggleBtnSet("update");
		
		setCode(upperCode, code, codeName, codeSeq, useYn, groupCnt);
		
		$("input[name='lookupTypeName']").val(upperCode);
		$("input[name='lookupCode']").val(code);
		$("input[name='lookupName']").val(codeName);

		$(".codeTreeMenu li ul").css("display","none");
		$(".codeTreeMenu li").removeClass("sub");	
		$(".codeTreeMenu li").css("height","24px");
		
		$("#"+upperCode).css("display","block");
		$("#"+upperCode).parent().addClass("sub");	
		var height = ""+ (groupCnt * 24)+"px";	
		$("#"+upperCode).parent().css("height",height);
	}
	
	//트리가 선택되면 모델 타입은 항상 닫는다.
	toggleModelType(false);
}

function setCode(upperCode, code, codeName, codeSeq, useYn, groupCnt, lookupClass){
	toggleBtnSet("update");
	
	selectedCode = [upperCode, code, codeName, codeSeq, useYn];
	
	$("input[name='lookupTypeName']").val(upperCode);
	$("input[name='lookupCode']").val(code);
	$("input[name='lookupCode']").attr("readonly",true);
	$("#btnDuplicateCheck").hide();
	$("input[name='lookupName']").val(codeName);

	$('input:radio[name=lookupClass]:input').attr("checked", false);
	$('input:radio[name=lookupClass]:input[value='+lookupClass+']').attr("checked", true);	
	
	$('input:radio[name=useYn]:input[value='+useYn+']').attr("checked", true);
	
	if(upperCode == "1000"){
		toggleModelType(true);
	} else {
		toggleModelType(false);
	}
}

function initForm(upperCode){
	selectedCode = null;
	dupCheckPassed = false;
	
	if(upperCode == ''){
		$("input[name='codeType']").val("0");
	} else {
		$("input[name='codeType']").val("1");
	}
	
	$("input[name='lookupTypeName']").val(upperCode);
	$("input[name='lookupCode']").val("");
	$("input[name='lookupCode']").attr("readonly", false);
	$("#btnDuplicateCheck").show();
	$("input[name='lookupName']").val("");
	
	$('input:radio[name=useYn]:input[value=Y]').attr("checked", true);
}

function toggleModelType(flag){
	if(flag){
		$("input[name='lookupClass']").removeAttr("disabled");
		$("#trModelType").show();
	} else {
		$("input[name='lookupClass']").attr("disabled",true);
		$("#trModelType").hide();
	}
}

function toggleBtnSet(action){
	if (action == "insert"){
		$("#btnRegistSub").hide();
		$("#btnRegist").show();
		$("#btnModify").hide();
		$("#btnDelete").hide();
	} else {
		$("#btnRegistSub").show();
		$("#btnRegist").hide();
		$("#btnModify").show();
		$("#btnDelete").show();
	}
}

function duplicateCheck(upperCode, code){
	if("0" == $("input[name='codeType']").val()) {
		upperCode = code;
	}
	
	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/system/ajaxCheckCodeId.do",
		cache : false, 
		data : "lookupTypeName=" + upperCode
			+"&lookupCode=" + code,
		dataType: "json",
		success : function(data){
			
			if(data.success == true) {
				alert("사용가능한 코드입니다.");
				dupCheckPassed = true;
			} else {
				alert("중복된 코드가 있습니다.");
			}
		},
		error: function(data){
			alert("에러가 발생하였습니다.");
		}
	});
}

function registCode(){
	if("0" == $("input[name='codeType']").val()) {
		$("input[name='lookupTypeName']").val($("input[name='lookupCode']").val());
	}
	
	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/system/ajaxInsertCode.do",
		cache : false, 
		data : $("#codeForm").serialize(),
		dataType: "json",
		success : function(data){

			if(data.success == true) {
				alert("등록되었습니다.");
				
				location.href="${pageContext.request.contextPath}/system/codeList.do?topNum=4";
			} else {
				alert("등록이 실패하였습니다.");
			}
		},
		error: function(data){
			alert("에러가 발생하였습니다.");
		}
	});
}

function modifyCode(){

	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/system/ajaxUpdateCode.do",
		cache : false, 
		data : $("#codeForm").serialize(),
		dataType: "json",
		success : function(data){			
			if(data.success == true) {
				alert("등록되었습니다.");
				
				location.href="${pageContext.request.contextPath}/system/codeList.do?topNum=4";
			} else {
				alert("등록이 실패하였습니다.");
			}
		},
		error: function(data){
			alert("에러가 발생하였습니다.");
		}
	});
}

function deleteCode(){
	
	$.ajax({
		type: "POST",
		url: "${pageContext.request.contextPath}/system/ajaxDeleteCode.do",
		cache : false, 
		data : $("#codeForm").serialize(),
		dataType: "json",
		success : function(data){			
			if(data.success == true) {
				alert("삭제되었습니다.");
				
				location.href="${pageContext.request.contextPath}/system/codeList.do?topNum=4";
			} else {
				alert("등록이 실패하였습니다.");
			}
		},
		error: function(data){
			alert("에러가 발생하였습니다.");
		}
	});
}

function formCheck(isNew){
	if($("input[name='lookupCode']").val().trim() == ""){
		document.codeForm.lookupCode.focus();
		alert("코드를 입력하여주십시오.");
		return false;
	} else {
		if(isNew && !dupCheckPassed){
			alert("코드 중복 체크가 필요합니다.");
			return false;
		}
	}	
	
	if($("input[name='lookupName']").val().trim() == ""){
		document.codeForm.lookupName.focus();
		alert("코드 명를 입력하여주십시오.");
		return false;
	}
	
	return true;
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
		<img src="/images/blue/tit_manageAdmin.png" alt="관리자 계정 관리">
	</h3>
	<div id="codeTree">
		<button class="btn_type_f" id="btnRegistUpper">
			+ 상위코드등록<span></span>
		</button>
		<ul class="codeTreeMenu">
			<c:if test="${codeList != null && fn:length(codeList) > 0}">
				<c:forEach var="codeInfo" items="${codeList}" varStatus="status">
					<c:if test="${codeInfo.codeType == '0'}">
						<c:if test="${status.index != '0' }">
				</ul>
				</li>
				</c:if>
				<li><a
					href="javascript:toggleTree('${codeInfo.lookupTypeName}','${codeInfo.lookupCode}','${codeInfo.lookupName}','${codeInfo.sortSequence}','${codeInfo.useYn}','${codeInfo.groupCnt}'); ">${codeInfo.lookupName}</a>
					<ul id="${codeInfo.lookupTypeName}" style="display: none">
						</c:if>
						<c:if test="${codeInfo.codeType == '1'}">
							<li><a
								href="javascript:setCode('${codeInfo.lookupTypeName}','${codeInfo.lookupCode}','${codeInfo.lookupName}','${codeInfo.sortSequence}','${codeInfo.useYn}','${codeInfo.groupCnt}','${codeInfo.lookupClass}')">-
									${codeInfo.lookupName}</a>
							</li>
						</c:if>
						<c:if test="${status.count == fn:length(codeList)}">
					</ul></li>
				</c:if>
				</c:forEach>
			</c:if>
		</ul>
	</div>
	<form name="codeForm" id="codeForm" method="post" action="">
		<input type="hidden" id="codeType" name="codeType" value=""> <input
			type="hidden" id="lookupTypeName" name="lookupTypeName" value="">
		<div id="contentBodyInside">
			<table class="tbl_type_b">
				<caption>코드등록</caption>
				<colgroup>
					<col width="25%">
					<col width="">
				</colgroup>
				<tr>
					<th>Code ID</th>
					<td><input type="text" id="lookupCode" name="lookupCode"
						class="w80p" value="">
						<button class="btn_type_c" id="btnDuplicateCheck">
							중복체크<span></span>
						</button></td>
				</tr>
				<tr>
					<th>Code Name</th>
					<td><input type="text" id="lookupName" name="lookupName"
						class="w80p" value=""></td>
				</tr>
				<tr id="trModelType">
					<th>Model Type</th>
					<td><emp:list mainCode="13000" radio="true" name="lookupClass" />
					</td>
				</tr>
				<tr>
					<th>사용여부</th>
					<td><input type="radio" name="useYn" id="radio" value="Y"
						checked> <label for="radio"> 예 <input type="radio"
							name="useYn" id="radio2" value="N"> 아니오
					</label></td>
				</tr>
			</table>

			<div class="under_tbl_area">
				<div class="under_tbl_area_left">
					<button class="btn_type_aa" id="btnRegistSub">
						하위코드 등록<span></span>
					</button>
				</div>
				<button class="btn_type_aa" id="btnRegist" value=""
					style="display: none;">
					등록<span></span>
				</button>
				<button class="btn_type_aa" id="btnModify">
					수정<span></span>
				</button>
				<button class="btn_type_aa" id="btnDelete">
					삭제<span></span>
				</button>
			</div>
		</div>
	</form>
</div>
