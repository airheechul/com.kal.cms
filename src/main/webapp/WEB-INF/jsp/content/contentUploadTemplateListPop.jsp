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
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/loading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/jquery.blockUI.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		setOpener();
	});
	
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
        
	function getXmetalTemplate(param, urlAddress){
		startLoading();
		var templatePath = "";
		var templateFileName = "";
		switch(param){
			case '1' : 
				templatePath = "kor_model_kor_0_op_0";
				templateFileName = "MODEL_KOR_AIRCRAFT_TEMPLATE.xml";
				break;
			case '2' : 
				templatePath = "kor_model_kor_0_rt_0";
				templateFileName = "MODEL_KOR_AIRCRAFT_TEMPLATE.xml";
				break;
			case '3' : 
				templatePath = "kor_model_kor_0_op_0";
				templateFileName = "MODEL_KOR_ENGINE_TEMPLATE.xml";
				break;
			case '4' : 
				templatePath = "kor_model_kor_0_rt_0";
				templateFileName = "MODEL_KOR_ENGINE_TEMPLATE.xml";
				break;
			case '5' : 
				templatePath = "kor_model_kor_0_op_0";
				templateFileName = "MODEL_KOR_COMPONENT_TEMPLATE.xml";
				break;
			case '6' : 
				templatePath = "kor_model_kor_0_rt_0";
				templateFileName = "MODEL_KOR_COMPONENT_TEMPLATE.xml";
				break;
		}
		$("#templateFileName").val(templateFileName);
		$("#templatePath").val(templatePath);
		$("#templateUrlAddress").val(urlAddress);
		
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/content/createUploadCallXmetal.do",
			cache : false, 
			data : $("#xmetalForm").serialize(),
			dataType: "json",
			success : function(data){
				openInXMetaL("1111111", "1111111", data.webDavPath, data.maintDocModelCode, data.wOCntsDocSubtypCd);
				window.close();
			},
			error: function(data){
				stopLoading();
				alert("에러가 발생하였습니다.");
				window.close();
			}
		});
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
			stopLoading();
		} finally {
			if (xmetal != null) {
				xmetal = null;
			}
			stopLoading();
		}
	}
</script>
<!-- contents : str -->

	<form name="xmetalForm" id="xmetalForm" method="post" action="" target="resultFrame">
		<input type="hidden" name="templatePath" id="templatePath"/>
		<input type="hidden" name="templateFileName" id="templateFileName"/>
		<input type="hidden" name="templateUrlAddress" id="templateUrlAddress"/>
	</form>
	<div id="popup_type_b">
		<h1>Template 선택</h1>
		<div align="center" style="margin-top:10px;">생성할 템플릿을 선택해 주세요.</div>
		<table border="0" cellspacing="0" cellpadding="0" class="popup_tbl">
			<colgroup>
				<col width="50%">
				<col width="50%">
			</colgroup>
			<tr>
				<td>
					KAL Operation Template
				</td>
				<td>
					KAL Route Template
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${operationAircraft eq 'Y'}">
							<button class="btn_type_c" style="margin-bottom:10px;width:180px;" onclick="getXmetalTemplate('1','${operationAircraftUrlAddress}')">Aircraft KOR Operation<span></span></button>
						</c:when>
						<c:otherwise>
							<button class="btn_type_c_cancel" style="margin-bottom:10px;width:180px;cursor: default;">Aircraft KOR Operation<span></span></button>	
						</c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test="${routeAircraft eq 'Y'}">
							<button class="btn_type_c" style="margin-bottom:10px;width:180px;" onclick="getXmetalTemplate('2','${routeAircraftUrlAddress}')">Aircraft KOR Route<span></span></button>
						</c:when>
						<c:otherwise>	
							<button class="btn_type_c_cancel" style="margin-bottom:10px;width:180px;cursor: default;">Aircraft KOR Route<span></span></button>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${operationEngine eq 'Y'}">
							<button class="btn_type_c" style="margin-bottom:10px;width:180px;" onclick="getXmetalTemplate('3','${operationEngineUrlAddress}')">Engine KOR Operation<span></span></button>
						</c:when>
						<c:otherwise>
							<button class="btn_type_c_cancel" style="margin-bottom:10px;width:180px;cursor: default;">Engine KOR Operation<span></span></button>
						</c:otherwise>
					</c:choose>				
				</td>
				<td>
					<c:choose>
						<c:when test="${routeEngine eq 'Y'}">	
							<button class="btn_type_c" style="margin-bottom:10px;width:180px;" onclick="getXmetalTemplate('4','${routeEngineUrlAddress}')">Engine KOR Route<span></span></button>
						</c:when>
						<c:otherwise>
							<button class="btn_type_c_cancel" style="margin-bottom:10px;width:180px;cursor: default;">Engine KOR Route<span></span></button>
						</c:otherwise>
					</c:choose>				
				</td>
			</tr>
			<tr>
				<td>
					<c:choose>
						<c:when test="${operationComponent eq 'Y'}">
							<button class="btn_type_c" style="margin-bottom:10px;width:180px;" onclick="getXmetalTemplate('5','${operationComponentUrlAddress}')">Component KOR Operation<span></span></button>
						</c:when>
						<c:otherwise>
							<button class="btn_type_c_cancel" style="margin-bottom:10px;width:180px;cursor: default;">Component KOR Operation<span></span></button>
						</c:otherwise>
					</c:choose>				
				</td>
				<td>
					<c:choose>
						<c:when test="${routeComponent eq 'Y'}">
							<button class="btn_type_c" style="margin-bottom:10px;width:180px;" onclick="getXmetalTemplate('6','${routeComponentUrlAddress}')">Component KOR Route<span></span></button>
						</c:when>
						<c:otherwise>
							<button class="btn_type_c_cancel" style="margin-bottom:10px;width:180px;cursor: default;">Component KOR Route<span></span></button>
						</c:otherwise>
					</c:choose>				
				</td>
			</tr>
		</table>
	</div>
    
	<iframe width="0" height="0" frameborder="0" marginwidth="0" marginheight="0" name="resultFrame"></iframe>
	<div id="dim-frame-div"></div>
 	<img id="displayBox" src="${pageContext.request.contextPath}/html/images/display_loading.gif"/>
<!-- contents : end -->