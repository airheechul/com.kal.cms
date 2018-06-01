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
	$(document).ready(function(){
		callInterval() ;
		
	    $('.btn_popup_close').click(function(e) {
	    	self.close();
	    	return false;
	    });
	});

    function fncClose() {
	    self.close();
	}
    
    function callInterval() {
        window.setInterval("getServerLog()", 10000);
    }
    
    var prevDetailLogId = '${prevDetailLogId}'; //''
    
    function getServerLog() {
    	
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/task/taskDetailLogPop.do",
			cache : false, 
			data : "detailId=${param.detailId}&prevDetailLogId="+prevDetailLogId,
			dataType: "json",
			success : function(data){
				
				if(data.taskDetailLog != null && data.taskDetailLog.length > 0) {

					var html = "";
					for (var i = 0; i <data.taskDetailLog.length ; i++) {
						var row = data.taskDetailLog[i];
						html += "["+row.logDateStr+"] : "+row.detailLog +"<br/>";
						
						if(i == (data.taskDetailLog.length-1)){
							prevDetailLogId = row.detailLogId;
						}
					}

					$(".logArea").append(html);
					
				} else {

				}
			},
			error: function(data){
				alert("에러가 발생하였습니다.");
			}
		});
    }    
</script>

<!-- Body-content -->
	<!-- Body-content -->
	<div id="popup_type_a">
		<h1>Server Log<button class="btn_popup_close"><img src="${pageContext.request.contextPath}/html/images/${skin}/ico_close.png" alt="close"></button></h1>
		<div class="logArea" id="dvLogArea">
		<c:if test="${taskDetailLog != null && fn:length(taskDetailLog) > 0}">  
			<c:forEach var="logInfo" items="${taskDetailLog}" varStatus="idx">
			[<emp:date value="${logInfo.logDate}" format="yyyy-MM-dd HH:mm:ss" />] : ${logInfo.detailLog}<br/> 
			</c:forEach>
		</c:if>
		</div>
	</div>
	<!-- /Body-content -->
<!-- /Body-content -->