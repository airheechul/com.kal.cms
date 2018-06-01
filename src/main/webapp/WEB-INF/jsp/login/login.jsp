<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.commons.logging.Log" %>
<%@ page import="org.apache.commons.logging.LogFactory" %>


<script type="text/javascript" src="${pageContext.request.contextPath}/js/aes-enc.js"></script> 
    
<script type="text/javascript">

	
	$(document).ready(function() {	
		
		$("#loginForm").keyup(function(e){
			if(e.keyCode == 13){
				actionLogin();
				
				return false;
			}
		});
	});		


	/**
	 * 로그인 처리
	 */
	function actionLogin() {
		
		if ($("#userName").val() == null || $("#userName").val() == "undefined")
		{
			
		}

		document.forms[0].action = "${pageContext.request.contextPath}/login/actionLogin.do";
	    document.forms[0].submit();
	}

/* 	// 엔터 키 
	$('input').keypress( function(e) {
        if (e.keyCode == 13) {
        	regularUserLogin2();            
            return false;
        }
    });
 */
	function doSubmit(){
		
		$.ajax({
			type: "POST",
			url: "${pageContext.request.contextPath}/login/actionLogin.do",
			cache : false, 
			data : $("#loginForm").serialize(),
			//dataType: "json",
			success : function(data){
	            var authorized = data.authorized;
	        	
	            if (authorized == 'T') {
	            	location.href = '${pageContext.request.contextPath}/content/searchList.do?topNum=1';
	            }
	            else {
	            	alert('접속 권한이 없습니다');
	            }
				console.log('data0 is ' + data.authorized);
			},
			error: function(data){
				alert("에러가 발생하였습니다.");
			}
		});
	}

</script>

<form id="loginForm" method="post">
	<input type="hidden" name="checkSSO" value=""/>
		<div id="loginWrap">
		<h1><img src="${pageContext.request.contextPath}/images/blue/bi_login.png" alt="KOREAN AIR"/></h1>
		<div class="loginBox">
			<p>
			  <label for="userName">User ID</label>
			  <input type="text" name="userName" id="userName" placeholder="input login id" required>
			</p>

			<p class="logBtn">
			  <input type="button" value="LOGIN" onclick="javascript:doSubmit();"/>
			</p>
			<p class="textBtn">
			</p>
		</div>
	</div>
	
</form>



