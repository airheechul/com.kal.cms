/**
 * JS 페이지 공통 환경 변수 정의.
 * layout.jsp에서 아래 형식으로 세팅합니다.
	$(document).ready(function(){
		setContextPath("${pageContext.request.contextPath }");
	});
 */
var env = {
	contextPath : ""
};

function setContextPath(paramContextPath){
	env.contextPath = paramContextPath;
	Common.contextPath = paramContextPath;
}

/**
 * 문자열 왼쪽,오른쪽 공백을 제거.
 * @return {String} 공백제거된 문자열.
 */
String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
}

/**
 * 문자열의 왼쪽 공백을 제거.
 * @return {String} 공백제거된 문자열.
 */
String.prototype.ltrim = function() {
	return this.replace(/^\s+/,"");
}

/**
 * 문자열의 오른쪽 공백을 제거.
 * @return {String} 공백제거된 문자열.
 */
String.prototype.rtrim = function() {
	return this.replace(/\s+$/,"");
}

/**
 * 해당문자열에 특정문자의 포함 여부 반환.
 * @param {String} findStr 찾을문자.
 * @return {Boolean} 특정문자포함여부
 */
String.prototype.contains = function(findStr){
	return this.indexOf(findStr) >= 0;
}

/**
 * 해당문자열의 특정문자(findStr)를 특정문자(newStr)로 전체치환.
 * @param {Object} findStr 찾을문자
 * @param {Object} newStr 대체문자
 * @return {String} 치환된문자열
 */
String.prototype.replaceAll = function(findStr, newStr){
    return this.replace(new RegExp(findStr, "gi"), newStr);
}

/**
 * 해당문자열의 바이트 수를 반환.
 * @return {Number} 문자열의 바이트 수
 */
String.prototype.getBytes = function() {
	var size = 0;
	for(i=0; i<this.length; i++) {
		var temp = this.charAt(i);
		if(escape(temp) == '%0D') continue;
		if(escape(temp).indexOf("%u") != -1) {
			size += 2;
		}else {
			size++;
		}
	}
	return size;
}

/**
 * Common Class
 * 공통으로 사용될 기능들을 구현한 샘플 클래스.
 * 스트링 NULL 체크 / NameSpace / Import
 * 
 * @author: 김융규
 */
var Common = {	
		
	/**
	 * 컨텍스트 패스.
	 */
	contextPath : "",
	
	/**
	 * 사용자 아이디.
	 */
	memberId : "",
	
	/**
	 * JSON 호출 결과 코드
	 */	
	JSON_CALL_RESULT_CODE : {
		SUCCESS : 1,
		FAIL : 0
	},
	
	/**
	 * 다이얼로그 타이틀
	 */		
	DIALOG_TITLE : {
		NOTICE : '알림',
		ALERT : '경고',
		INFO : '정보',
		CONFIRM : '확인'
	},

	/**
	 * 메세지
	 */		
	MESSAGE : { 
		BLOCK : '<h4>잠시만 기다려 주세요.</h4>'
	},	
	
	
	/**
	 * 스트링 널 체크.
	 * @param {String} str
	 */
	isEmpty : function(str){
		if(str == null) return true;
		return !(str.replace(/(^\s*)|(\s*$)/g, ""));
	},

	/**
	 * 숫자형 체크
	 * @param {String} str
	 */
	isNumber : function(str) {
		var number = str.match(/^\d+$/ig);
		if (number == null) {
			return false;
		} else { 
			return true;
		}
	},

	
	/**
	 * 네임스페이스 등록.
	 * @param {String} ns 네임스페이스
	 */
	registNamespace : function(ns){
	    var nsParts = ns.split(".");
	    var root = window;
	
	    for(var i=0; i<nsParts.length; i++){
	        if (typeof root[nsParts[i]] == "undefined") {
				root[nsParts[i]] = new Object();
			}
	        root = root[nsParts[i]];
	    }
	},
	
	importJS : function(jsFile){
		$.ajax({
				type: "GET",
				cache:false,
				url: Common.contextPath + "/html/js/" + jsFile,			
				async : false,
				dataType: "script"
			});
	},

	/**
	 * 팝업 윈도우 화면의 중간에 위치.
	 * @param {String} targetUrl	팝업 윈도우의 내용을 구성하기 위한 호출 URL
	 * @param {String} windowName	팝업 윈도우의 이름
	 * @param {Object} properties	팝업 윈도우의 속성(넓이, 높이, x/y좌표)
	 */	
	centerPopupWindow : function(targetUrl, windowName, properties) {
		var childWidth = properties.width;
		var childHeight = properties.height;
		var childTop = (screen.height - childHeight) / 2 - 50;    // 아래가 가리는 경향이 있어서 50을 줄임
		var childLeft = (screen.width - childWidth) / 2;
		var popupProps = "width=" + childWidth + ",height=" + childHeight + ", top=" + childTop + ", left=" + childLeft;
		if (properties.scrollBars == "YES") {
			popupProps += ", scrollbars=yes";
		}

		var popupWin = window.open(targetUrl, windowName, popupProps);
		popupWin.focus();
	},

	/**
	 * 업로드 하려는 파일의 이름 사이즈 체크.
	 * @param {String} uploadFileName 파일명
	 * @param {String} limitSize
	 */	
	checkUploadFileNameSize : function(uploadFileName, limitSize){
    	if(!Common.isEmpty(uploadFileName)){
    		var index = uploadFileName.lastIndexOf("\\");
    		if(index > -1){
    			uploadFileName = uploadFileName.substring(index+1);
    		}

	    	if(uploadFileName.getBytes() > limitSize){
				Common.alertDialog("알림", "파일 명이 너무 길어요.");
				return false;
	    	}

	    	return true;
    	}else{
			return false;
    	}
	},
	
	/**
	 * toString
	 */
	toString : function(){
		return "Common Object";
	},
	
	/**
	 * X,Y 좌표의 구글 맵 화면을 띄움.
	 * @param {integer} x GPS X 위치
	 * @param {integer} y GPS Y 위치
	 * @param {String} message 내용
	 */		
	viewMap : function(context, x, y, message){
	 	Common.centerPopupWindow(context+'/bbs/locationMap.do?x='+x+"&y="+y+"&message="+encodeURIComponent(message), 'mapPopup', {width : 500, height : 300});
		return false;
	},
	
	/**
	 * 두 날자 사이의 일수를 반환
	 * @param {String} fromDate 시작일자 (yyyy-mm-dd)
	 * @param {String} toDate 종료일자 (yyyy-mm-dd)
	 * @return {integer} 두 일자 사이의 일수
	 */ 
	intervalDate : function(fromDate, toDate){
       var FORMAT = "-";

       // FORMAT을 포함한 길이 체크
       if (fromDate.length != 10 || toDate.length != 10)
           return null;

       // FORMAT이 있는지 체크
       if (fromDate.indexOf(FORMAT) < 0 || toDate.indexOf(FORMAT) < 0)
           return null;

       // 년도, 월, 일로 분리
       var start_dt = fromDate.split(FORMAT);
       var end_dt = toDate.split(FORMAT);

       // 월 - 1(자바스크립트는 월이 0부터 시작하기 때문에...)
       // Number()를 이용하여 08, 09월을 10진수로 인식하게 함.
       start_dt[1] = (Number(start_dt[1]) - 1) + "";
       end_dt[1] = (Number(end_dt[1]) - 1) + "";

       var from_dt = new Date(start_dt[0], start_dt[1], start_dt[2]);
       var to_dt = new Date(end_dt[0], end_dt[1], end_dt[2]);

       return (to_dt.getTime() - from_dt.getTime()) / 1000 / 60 / 60 / 24;
   },

	/**
	 * Alert 다이얼로그 띄움.
	 * @param {String} title 타이틀
	 * @param {String} msg 내용
	 */	
	alertDialog : function(title, msg, proc){
		var dialogTag = "<div id='jQueryDialog' title=\"" + title + "\">" + msg + "</div>";
		$(dialogTag).dialog({
			modal: true,
			buttons: {
				'확인': function(){
					if (proc != null) {
						eval(proc);
					}
					$(this).dialog('destroy').remove();
				}
			}
		});	
	},
   
	/**
	 * Alert 다이얼로그 띄운 후 정해진 작업을 완료한 후 팝업창을 닫는다.
	 * @param {String} msg 내용
	 * @param {String} procArray 작업 목록 배열
	 */	
	alertProcDialog : function(msg, procArray){
		var dialogTag = "<div id='jQueryDialog' title=\""+Common.DIALOG_TITLE.NOTICE+"\">" + msg + "</div>";
		$(dialogTag).dialog({
			modal: true,
			buttons: {
				'확인': function(){
					$(this).dialog('destroy').remove();
					for(var i=0; i<procArray.length; i++){
						eval(procArray[i]);
					}
				}
			}
		});	
	},

	/**
	 * Alert 다이얼로그 띄운 후 취소, 확인 버튼을 출력 후 확인 버튼을 클릭할 경우에는 proc를 실행한다.
	 * @param {String} msg 내용
	 * @param {String} proc 작업
	 */	
	choiceDialog : function(msg, proc){
		var dialogTag = "<div id='jQueryDialog' title=\""+Common.DIALOG_TITLE.CONFIRM+"\">" + msg + "</div>";
		$(dialogTag).dialog({
			modal: true,
			buttons: {
				'확인': function(){
					if (proc != null) {
						eval(proc);
					}
					$(this).dialog('destroy').remove();
				},
				'취소': function(){		
					$(this).dialog('destroy').remove();
				}
			}
		});
		return false;	
	},
	
	/**
	 * 다이얼로그 닫기
	 */
	removeDialog : function() {
		$('#jQueryDialog').dialog('destroy').remove();
	},
	
	/**
	 * @param 
	 * @param 
	 */	
	makeTelTxt : function(object){
		var mdn = object.val();
		var telText1 = mdn.substring(0,3);
		var telText2 = mdn.substring(3,7);
		var telText3 = mdn.substring(7,11);

		return telText1+"-"+ telText2+"-"+telText3;	
	},
	
	checkObj : function checkObj(o,n) {
		if (o.val().length == 0) {
			o.addClass('ui-state-error');
			Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<br/><br/> "+ n +" 입력해주세요.",o.val(''));
			return false;
		} else {
			return true;
		}

	}
};


/**
 * 이미지 리사이징
 */
function Resizing(img, vWidth, vHeigh){ 
    if(vWidth != ""){
        img.width = vWidth;
    }

    if(vHeigh != "") {
        img.height= vHeigh;
    }
}

/**
 * 필수입력값 체크
 * @param fieldId
 * @param msg
 * @return
 */
function requiredCheck(fieldId, msg) {
	if (Common.isEmpty($.trim($('#'+fieldId).val()))) {
		Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>" + msg + "</p>"); 
		return true;
	}
}
/**
 * undefined 체크
 * @param value
 * @param msg
 * @return
 */
function undefinedCheck(value, msg) {
	if(value == undefined){
		Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>" + msg + "</p>"); 
		return true;
	}
}
/**
 * 공통 메세지용 alert
 * @param msg
 * @return
 */
function alertInfo(msg) {
	Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>" + msg + "</p>"); 
}
/**
 * 글자수 체크
 * @param fieldId
 * @param min
 * @param max
 * @return
 */
function rangeCheck(fieldId, min, max) {
	if (max > 0 && $('#'+fieldId).val().getBytes() > max) {
		Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>"+max+"byte 이내로 입력해 주세요.</p>");
		return true;
	}
	if (min > 0 && $('#'+fieldId).val().getBytes() < min) {
		Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>"+min+"byte 이상 입력해 주세요.</p>"); 
		return true;
	}
}

/**
 * 특정 필드값이 비교값과 같은지 체크
 * @param fieldId
 * @param compareValue
 * @param msg
 * @return 같으면 true
 */
function sameValueCheck(fieldId, compareValue, msg) {
	if ($('#'+fieldId).val() == compareValue) {
		Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>"+msg+"</p>"); 
		return true;
	}
}


















function appIdList(){
	if($("#flagMode").val() == "U"){
		Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>App 관리</h1><p>이동할 수 없습니다.</p>");
		return;
	}
	if($("#curState").val() != "2500"){
		$("#curState").val("2500");
		$("#app").attr("action", "./appMgmtCreateAppId.do");
		$("#app").submit();
	}
}

function appCreateBasic(){
	if($("#curState").val() != "2501"){
		$("#curState").val("2501");
		$("#app").attr("action", "./appMgmtCreateBasic.do");
		$("#app").submit();
	}
}

function appCreateDetail(check){
	if($("#curState").val() != "2502"){
		if(check !== 'N' && $("#stat").val() < 2501){
			Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>App 관리</h1><p>이동할 수 없습니다.</p>");
			return;
		}
		$("#curState").val("2502");
		$("#app").attr("action", "./appMgmtCreateDetail.do");
		$("#app").submit();
	}
}

function appCreateExplain(check){
	if($("#curState").val() != "2503"){
		if(check !== 'N' && $("#stat").val() < 2502){
			Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>App 관리</h1><p>이동할 수 없습니다.</p>");
			return;
		}
		$("#curState").val("2503");
		$("#app").attr("action", "./appMgmtCreateExplain.do");
		$("#app").submit();
	}
}

function appCreateImage(check){
	if($("#curState").val() != "2504"){
		if(check !== 'N' && $("#stat").val() < 2503){
			Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>App 관리</h1><p>이동할 수 없습니다.</p>");
			return;
		}
		$("#curState").val("2504");
		$("#app").attr("action", "./appMgmtCreateImage.do");
		$("#app").submit();
	}
}

function appCreateDefine(check){
	if($("#curState").val() != "2505"){
		if(check !== 'N' && $("#stat").val() < 2503){
			Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>App 관리</h1><p>이동할 수 없습니다.</p>");
			return;
		}
		$("#curState").val("2505");
		$("#app").attr("action", "./appMgmtCreateDefine.do");
		$("#app").submit();
	}
}



//탭
function show(lay){ 

 document.getElementById(lay).style.display="block"; 
} 
function hide(lay){ 
 document.getElementById(lay).style.display="none"; 
}

//A선택영역 없애기
function bluring(){ 
if(event.srcElement.id == "popDeviceId") return;	
if(event.srcElement.tagName=="A"||event.srcElement.tagName=="IMG") document.body.focus(); 
} 
document.onfocusin=bluring; 


//쿠키 설정하기 -- 창 닫히면 쿠키 삭제....
function setCookie(cName, cValue) {
//   var todayDate = new Date();
//   todayDate.setDate( todayDate.getDate() + cDate );
   document.cookie = cName + "=" + escape( cValue ) + "; path=/;";
   // expires=" + todayDate.toGMTString() + ";"
}

//쿠키 가져오기
function getCookie(cName) {
    var nameOfCookie = cName + "=";
    var x = 0;
    while ( x <= document.cookie.length )
 
    {
            var y = (x+nameOfCookie.length);
            if ( document.cookie.substring( x, y ) == nameOfCookie ) {
                    if ( (endOfCookie=document.cookie.indexOf( ";", y )) == -1 )
                            endOfCookie = document.cookie.length;
                    return unescape( document.cookie.substring( y, endOfCookie ) );
            }
            x = document.cookie.indexOf( " ", x ) + 1;
            if ( x == 0 )
                    break;
    }
    return "";
}	
