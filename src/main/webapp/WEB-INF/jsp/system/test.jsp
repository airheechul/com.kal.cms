<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>

	<script>
		$(document).ready(function(){

			$('#btnTest').click(function(){

	    		var downFormData = new Object();
	    		downFormData.model = "1000";
	    		downFormData.manualtype = "2000";

	    		$.ajax({
	    			url : './system/params.do',
	    			type : 'POST',
	    			//contentType:'application/json',
	    			data : downFormData,
	    			//dataType : 'html',
	    	        success: function(result, textStatus, jqXHR)
	    	        {
	    	            $("#results").html(result);

	    	        },
	    			error : function(jqXHR, textStatus, errorThrown) {
	    				alert(jqXHR.status);
	    			}

	    		});
			

		    });
		});
		
		
	</script>

</head>

<body>

	<form method="POST" name="frmContents" id="frmContents">
		<table border="1" " width="95%">
			<tr>
				<td>Lookup Types :</td>
				<td>
<%-- 					<select id="roleCode" name="roleCode">
						<option value="" disabled selected hidden> ----선택---- </option>
						<c:forEach var="roleInfo" items="${roleList}">
							<option value='${roleInfo.lookupCode}'>${roleInfo.lookupName}</option>
						</c:forEach>
					</select> --%>
				</td>
			</tr>
			<tr>
				<td>Message</td>
				<td><div id="results" style="font-size: 8pt; color: blue;"></div></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" id="btnTest"
					value="Go Rest ~ " style="font-weight: bold;"></td>
			</tr>
		</table>
	</form>


</body>

</html>