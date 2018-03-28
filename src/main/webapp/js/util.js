/**
 * 관리자 권한에 따라 그룹사, 관계사 항목이 다르게 보여진다. (검색 조건 화면)
 * 
 * @param authCd
 * @return
 */	
function groupCompanySetView(superAdminAuthCd, groupAdminAuthCd, myAuthCd) { 
	if (myAuthCd == superAdminAuthCd) { // 수퍼관리자일 경우 그룹사 선택항목이 보여진다.
		 var groupView = document.getElementById("groupView"); 
		 if(groupView != null) {
			 groupView.style.display = "inline";
		 }
	}
	
	if (myAuthCd == superAdminAuthCd || myAuthCd == groupAdminAuthCd) {// 수퍼관리자, 그룹사관리자 일 경우  관계사 선택항목이 보여진다.
		 var companyView = document.getElementById("companyView");
		 if(companyView != null) {
			 companyView.style.display = "inline";
		 } 
	} 
}

/**
 * 관리자 권한에 따라 그룹사, 관계사 항목이 다르게 보여진다. (검색 조건 화면)
 * 
 * @param authCd
 * @return
 */	
function groupCompanySetTd(superAdminAuthCd, groupAdminAuthCd, myAuthCd) { 
	if (myAuthCd == superAdminAuthCd) { // 수퍼관리자일 경우 그룹사선택항목이 보여진다.
		var groupTd = $("#groupTd");
		if(groupTd != null) {
			groupTd.show();
		}
	}
	
	if (myAuthCd == superAdminAuthCd || myAuthCd == groupAdminAuthCd) {// 수퍼관리자, 그룹사관리자 일 경우  관계사선택항목이 보여진다.
		var companyTd = $("#companyTd");
		if(companyTd != null) {
			companyTd.show();
		}
	}	 
}

/**
 * 관리자 권한에 따라 그룹사, 관계사 항목이 다르게 보여진다. (등록, 수정 화면)
 * 
 * @param authCd
 * @return
 */	
function groupCompanySetTr(superAdminAuthCd, groupAdminAuthCd, myAuthCd) { 
	if (myAuthCd == superAdminAuthCd) { // 수퍼관리자일 경우 그룹사선택항목이 보여진다.
		 var groupTr = $("#groupTr");
		 if(groupTr != null) {
			 groupTr.show();
		 }
	}
	
	if (myAuthCd == superAdminAuthCd || myAuthCd == groupAdminAuthCd) {// 수퍼관리자, 그룹사관리자 일 경우  관계사선택항목이 보여진다.
		 var companyTr = $("#companyTr");
		 if(companyTr != null) {
			 companyTr.show();
		 }
	}	 
}

/**
 * 그룹사에 해당하는 관계사 목록을 조회하여 select Tag에 해당 값을 넣어준다.
 * 
 * @param groupCd - 그룹사 코드
 * @param selectObjectName - 관계사 목록 select box Object 명
 * @param isViewGrpCommonYn - 관계사 목록에 그룹공통 요소 표시 여부
 * @param isExistSpanTag - span tag 존재여부
 * @param isAppExeYn	 - changeAppList 사용여부
 * @param optText	     - Select Tag 디폴트 옵션 텍스트
 * @return
 */	

function changeCompanyList(groupCd, selectObjectName, isViewGrpCommonYn, isExistSpanTag , isAppExeYn, optText) {
	var selectObject = document.getElementById(selectObjectName);
 
	if (groupCd == "") {	// 그룹을 선택하지 않았을 때
		// 기존에 있던 관계사 목록 제거
		if (selectObject.options.length > 0) {
			
			var str = "그룹을 먼저 선택하세요.";
			for (var i = selectObject.options.length; i >= 0; i--) { 
				delOption(selectObject, i);
			}
	     
			addOption(selectObject, "", str);
			
			/* span tag width 사이즈 지정 */
			if (isExistSpanTag == "Y") {
				 var strLength = eval(str.length + 6);
				 var companySpan = document.getElementById("companyView");
				 companySpan.style.width = strLength + "em";
			 }
			 
			 /* select tag width 사이즈 지정 */
			 selectObject.style.width = (eval(str.length) + 2) + "em";
		}
	 
		return;
	}

	$.ajax({
		type	 : "POST",
		cache	 : false,
		url		 : env.contextPath + "/common/ajaxFindCompanyList.do",						 
		data     : {groupCd:groupCd},			
		dataType : "json",
		success  : function(json) {  
			if(json.success == "true") {				   			    					
			     var result = eval(json.codeList);	// json 데이터 자바스크립트 인식위해 String ->object 변환
	
			     var str = "";
			     
			     // 기존에 있던 관계사 목록 제거
				 if (selectObject.options.length > 0) {
				     for (var i = selectObject.options.length; i >= 0; i--) { 
				    	 delOption(selectObject, i);
					 }
	
				     if(optText != '' && optText != undefined)
				    	addOption(selectObject, "", optText);
				     else
				     	addOption(selectObject, "", "- 선택 -");
				     
				     if (isViewGrpCommonYn == "Y") {
				    	 str = "그룹공통";
				    	 addOption(selectObject, "000000", str);
				     }
				 }
				 
				 
				 for (var i = 0; i < result.length; i++) {
					 if (i == 0) {
						 if (isViewGrpCommonYn == "Y") {
							 if (str.length < result[i].CD_NM.length) {
								 str = result[i].CD_NM;
							 }
						 } else {
							 str = result[i].CD_NM;
						 }
					 } else {
						 if (str.length < result[i].CD_NM.length) {
							 str = result[i].CD_NM;
						 }
					 }
					 
					 addOption(selectObject, result[i].CD, result[i].CD_NM); 
				 }
				 
				 /* span tag width 사이즈 지정 */
				 if (isExistSpanTag == "Y") {
					 var strLength = eval(str.length + 6);
					 var companySpan = document.getElementById("companyView");
					 companySpan.style.width = strLength + "em";
				 }
				 
				 /* select tag width 사이즈 지정 */
				 selectObject.style.width = (eval(str.length) + 2) + "em";
				 
			} else {
				 var str = "";
			     // 기존에 있던 관계사 목록 제거
				 if (selectObject.options.length > 0) {
				     for (var i = selectObject.options.length; i >= 0; i--) { 
				    	 delOption(selectObject, i);
					 }
	
				     if(optText != '' && optText != undefined) {
				       str = optText;
					   addOption(selectObject, "", optText);
				     } else {
				       str = "- 선택 -";
					   addOption(selectObject, "", str);
				     }
				     
				     if (isViewGrpCommonYn == "Y") {
				    	 str = "그룹공통";
				    	 addOption(selectObject, "000000", str);
				     }
				     
				     /* span tag width 사이즈 지정 */
					 if (isExistSpanTag == "Y") {
						 var strLength = eval(str.length + 6);
						 var companySpan = document.getElementById("companyView");
						 companySpan.style.width = strLength + "em";
					 }
					 
					 /* select tag width 사이즈 지정 */
					 selectObject.style.width = (eval(str.length) + 2) + "em";
				 }
			}
			if(isAppExeYn == "Y"){
				 changeAppList();
			}
		}
	});
}

/**
 * 그룹사에 해당하는 관계사 목록을 조회하여 select Tag에 해당 값을 넣어준 뒤 특정 값을 선택하게 한다.
 * 
 * @param groupCd
 * @return
 */	
function changeSelectCompanyList(groupCd, selectObjectName, selectValue, isViewGrpCommonYn, isExistSpanTag, optText) {
	var selectObject = document.getElementById(selectObjectName);
 
	if (groupCd == "") {	// 그룹을 선택하지 않았을 때
		// 기존에 있던 관계사 목록 제거
		if (selectObject.options.length > 0) {
			 var str = "";
		     if(optText != '' && optText != undefined)
		    	 str = optText;
			 else
				 str = "- 선택 -";
			
			for (var i = selectObject.options.length; i >= 0; i--) { 
				delOption(selectObject, i);
			}
	     
			addOption(selectObject, "", str);
			
			/* span tag width 사이즈 지정 */
			if (isExistSpanTag == "Y") {
				 var strLength = eval(str.length + 6);
				 var companySpan = document.getElementById("companyView");
				 companySpan.style.width = strLength + "em";
			 }
			 
			 /* select tag width 사이즈 지정 */
			 selectObject.style.width = (eval(str.length) + 2) + "em";
		}
	 
		return;
	}

	$.ajax({
		type	 : "POST",
		cache	 : false,
		url		 : env.contextPath + "/common/ajaxFindCompanyList.do",						 
		data     : {groupCd:groupCd},			
		dataType : "json",
		success  : function(json) {  
			if(json.success == "true") {				   			    					
			     var result = eval(json.codeList);	// json 데이터 자바스크립트 인식위해 String -> Object 변환
				 var str = "";
			     if(optText != '' && optText != undefined)
			    	 str = optText;
				 else
					 str = "- 선택 -";
			     
			     // 기존에 있던 관계사 목록 제거
				 if (selectObject.options.length > 0) {
				     for (var i = selectObject.options.length; i >= 0; i--) { 
				    	 delOption(selectObject, i);
					 }
	
					 addOption(selectObject, "", str);
				     
				     if (isViewGrpCommonYn == "Y") {
				    	 str = "그룹공통";
				    	 addOptionChecked(selectObject, "000000", str, selectValue);
				     }
				 }
				 
				 var str = "";
				 for (var i = 0; i < result.length; i++) {
					 if (i == 0) {
						 if (isViewGrpCommonYn == "Y") {
							 if (str.length < result[i].CD_NM.length) {
								 str = result[i].CD_NM;
							 }
						 } else {
							 str = result[i].CD_NM;
						 }
					 } else {
						 if (str.length < result[i].CD_NM.length) {
							 str = result[i].CD_NM;
						 }
					 }
					 
					 addOptionChecked(selectObject, result[i].CD, result[i].CD_NM, selectValue); 
				 }
				 /* span tag width 사이즈 지정 */
				 var strLength = eval(str.length + 6);
				 var companySpan = document.getElementById("companyView");
				 companySpan.style.width = strLength + "em";
				 
				 /* select tag width 사이즈 지정 */
				 selectObject.style.width = (eval(str.length) + 2) + "em";
				 
			} else {
			     // 기존에 있던 관계사 목록 제거
				 if (selectObject.options.length > 0) {
				     for (var i = selectObject.options.length; i >= 0; i--) { 
				    	 delOption(selectObject, i);
					 }
					 var str = "";
				     if(optText != '' && optText != undefined)
				    	 str = optText;
					 else
						 str = "- 선택 -";
				     
				     addOption(selectObject, "", str);
				     
				     if (isViewGrpCommonYn == "Y") {
				    	 addOptionChecked(selectObject, "000000", "그룹공통", selectValue);
				     }
				     
				     /* span tag width 사이즈 지정 */
					 if (isExistSpanTag == "Y") {
						 var strLength = eval(str.length + 6);
						 var companySpan = document.getElementById("companyView");
						 companySpan.style.width = strLength + "em";
					 }
					 
					 /* select tag width 사이즈 지정 */
					 selectObject.style.width = (eval(str.length) + 2) + "em";
				 }				
			}
		}
	});
}

/**
 * 메인 코드에 해당하는 서브코드 목록을 조회하여 select Tag에 해당 값을 넣어준다.
 * 
 * @param groupCd
 * @return
 */	
function changeCodeList(mainCd, selectObjectName) {
	var selectObject = document.getElementById(selectObjectName); 
	
	if (mainCd == "") {	// 메인 코드를 선택하지 않았을 때

		 // 기존에 있던 서브코드 목록 제거
		 if (selectObject.options.length > 0) {
		     for (var i = selectObject.options.length; i >= 0; i--) { 
		    	 delOption(selectObject, i);
			 }
		     
		     addOption(selectObject, "", "메인코드를 먼저 선택하세요.");
		 }
		 
		 return;
	}
	
	$.ajax({
		type	 : "POST",
		cache	 : false,
		url	 	 : env.contextPath + "/common/ajaxFindCodeList.do",						 
		data	 : {mainCd:mainCd},			
		dataType : "json",
		success	 : function(json) {  
			if (json.success == "true") {				   			    					
			     var result = eval(json.companyList);	// json 데이터 자바스크립트 인식위해
														// String ->object 변환

			     // 기존에 있던 서브코드 목록 제거
			     if (selectObject.options.length > 0) {
			    	 for (var i = selectObject.options.length; i >= 0; i--) { 
				    	 delOption(selectObject, i);
					 } 
				 }
				 
				 for(var i = 0; i < result.length; i++) {  
					 addOption(selectObject, result[i].CD, result[i].CD_NM); 
				 } 
			} else {
			     // 기존에 있던 서브코드 목록 제거
			     if (selectObject.options.length > 0) {
			    	 for (var i = selectObject.options.length; i >= 0; i--) { 
				    	 delOption(selectObject, i);
					 } 
			    	 addOption(selectObject, "", "- 선택 -");
				 }
				
			}
		}
	});
}

/**
 * 메인 코드에 해당하는 서브코드 목록을 조회하여 select Tag에 해당 값을 넣어준다.
 * 
 * @param groupCd
 * @return
 */	
function changeMainCategoryList(groupCode, selectObjectName) {
	var selectObject = document.getElementById(selectObjectName);
	
	if (groupCode == "") {	// 메인 코드를 선택하지 않았을 때
		 // 기존에 있던 서브코드 목록 제거
		 if (selectObject.options.length > 0) {
		     for (var i = selectObject.options.length; i >= 0; i--) { 
		    	 delOption(selectObject, i);
			 }
		     
		     addOption(selectObject, "", "메인코드를 먼저 선택하세요.");
		 }
		 
		 return;
	}
	
	$.ajax({
		type	 : "POST",
		cache	 : false,
		url		 : env.contextPath + "/common/ajaxFindCategoryList.do",						 
		data	 : {groupCode:groupCode},			
		dataType : "json",
		success	 : function(json) {  
			if (json.success == "true") {		
				var result = eval(json.codeList);	// json 데이터 자바스크립트 인식위해
													// String ->object 변환

			    // 기존에 있던 서브코드 목록 제거
				if (selectObject.options.length > 0) {
				   for (var i = selectObject.options.length; i >= 0; i--) { 
					   delOption(selectObject, i);
				   } 
				}
				
				for (var i = 0; i < result.length; i++) {  
					addOption(selectObject, result[i].CD, result[i].CD_NM); 
				} 
			} else {
			    // 기존에 있던 서브코드 목록 제거
				if (selectObject.options.length > 0) {
				   for (var i = selectObject.options.length; i >= 0; i--) { 
					   delOption(selectObject, i);
				   } 
				   addOption(selectObject, "", "- 선택 -");
				}
											
			}
		}
	});
}

/**
 * 메인 코드에 해당하는 서브코드 목록을 조회하여 select Tag에 해당 값을 넣어준다.
 * 
 * @param groupCd
 * @return
 */	
function changeSubCategoryList(groupCode, parentCategorySeq, selectObjectName) {
	var selectObject = document.getElementById(selectObjectName); 
	
	if(groupCode == "" || parentCategorySeq == "") {	// 메인 코드를 선택하지 않았을 때
		 // 기존에 있던 서브코드 목록 제거
		 if (selectObject.options.length > 0) {
		     for (var i = selectObject.options.length; i >= 0; i--) { 
		    	 delOption(selectObject, i);
			 }
		     
		     addOption(selectObject, "", "메인 카테고리를 먼저 선택하세요.");
		 }
		 
		 return;
	}
	
 
	 var subCategroySeq = document.getElementById("subCategroySeq"); 
	 subCategroySeq.style.display = "inline";	
 
	
	$.ajax({
		type	 : "POST",
		cache	 : false,
		url		 : env.contextPath + "/common/ajaxFindCategoryList.do",						 
		data	 : {groupCode:groupCode, parentCategorySeq:parentCategorySeq},			
		dataType : "json",
		success	 : function(json) {  
			if (json.success == "true") {				   			    					
				var result = eval(json.codeList);	// json 데이터 자바스크립트 인식위해
													// String ->object 변환

			    // 기존에 있던 서브코드 목록 제거
				if (selectObject.options.length > 0) {
				    for (var i = selectObject.options.length; i >= 0; i--) { 
				   	 delOption(selectObject, i);
				    } 
				}
				 
				for (var i = 0; i < result.length; i++) {  
					addOption(selectObject, result[i].CD, result[i].CD_NM); 
				} 
			} else {
			    // 기존에 있던 서브코드 목록 제거
				if (selectObject.options.length > 0) {
				    for (var i = selectObject.options.length; i >= 0; i--) { 
				   	 delOption(selectObject, i);
				    } 
				}				
				addOption(selectObject, "", "- 선택 -");				
			}
		}
	});
}
	
/**
 * select Tag 에 options 동적으로 등록
 * 
 * @param sel
 * @param val
 * @param txt
 * @return
 */
function addOption(sel, val, txt) {
	
	// IE 9.0 Ver
	if( navigator.appVersion.indexOf('MSIE 9.0') > -1 ){
		$(sel).append("<option value='"+val+"'>"+txt+"</option>");
		return;
	}
	
	var opt = sel.ownerDocument.createElement("option");

	if( navigator.appName.indexOf("Microsoft") > -1 ){ 

		var opt = sel.ownerDocument.createElement("<option value=" + val + ">");
        
   } else {                          
  	  opt.setAttribute("value", val);
   }

	var t = sel.ownerDocument.createTextNode(txt);

	opt.appendChild(t);

	sel.appendChild(opt);
}

/**
 * select Tag 에 options 동적으로 등록 뒤 선택값 selected
 * 
 * @param sel
 * @param val
 * @param txt
 * @return
 */
function addOptionChecked(sel, val, txt, selVal) {
	
	// IE 9.0 Ver
	if( navigator.appVersion.indexOf('MSIE 9.0') > -1 ){
		if(val == selVal) {
			$(sel).append("<option value='"+val+"' selected>"+txt+"</option>");			
		}else {
			$(sel).append("<option value='"+val+"'>"+txt+"</option>");			
		}		
		return;
	} 
	
	if( navigator.appName.indexOf("Microsoft") > -1 ){//IE 6,7,8인 경우 		
		if(val == selVal) {
			var opt = sel.ownerDocument.createElement("<option value=" + val + " selected=\"selected\">");
		}else {
			var opt = sel.ownerDocument.createElement("<option value=" + val + ">");
		}
      } else {                          
    		var opt = sel.ownerDocument.createElement("option");
    		opt.setAttribute("value", val);
    		if(val == selVal) {
    			opt.setAttribute("selected", "selected");
    		}
      }

	var t = sel.ownerDocument.createTextNode(txt);
	opt.appendChild(t);
	sel.appendChild(opt);
}
/**
 * select Tag 에 options 동적으로 삭제
 * 
 * @param sel
 * @param txt
 * @return
 */
function delOption(sel, idx) {
	var opts = sel.getElementsByTagName("option");

	if (idx < 0 || idx > (opts.length - 1)) {
		return;
	}

    sel.removeChild(opts[idx]);
} 

/**
 * 
 * @param editorInstance
 * @return
 */
function FCKeditor_OnComplete(editorInstance) {
	window.status = editorInstance.Description;
}
	
/**
 * fckeditor 글자수 없는 경우 체크
 */
function fckeditorValidCheck(name) {
	var oEditor = FCKeditorAPI.GetInstance(name); // contents 는 fckeditor 생성시
													// id
	
	if (oEditor) { 
		if (oEditor.GetXHTML() == "") {
			oEditor.Focus();
			return false;
		}
	}

	return true;
}

/**
 * fckeditor 글자 수 제한
 */
function fckeditorLengthCheck(name, maxsize) {
	var oEditor = FCKeditorAPI.GetInstance(name); // contents 는 fckeditor 생성시
													// id
	
	if (oEditor) { 
		var div = document.createElement("DIV");   
		div.innerHTML = oEditor.GetXHTML();   
		var contents = div.innerHTML;  
		var charlength = 0; 
	  
		for (var i = 0; i < contents.length; i++) {
			ch = escape(contents.charAt(i));
	 
			if (ch.length == 1) {
				charlength++;
			} else if (ch.indexOf("%u") != -1) {
				charlength += 2;
			} else if (ch.indexOf("%") != -1) {
				charlength += ch.length/3;
			}
		}
		
		if (charlength > maxsize) { 
			return false; 
		}
	}
	
	return true;
}

/**
 * 숫자 체크 확인
 */
function isNum(objValue) { 	
	if (objValue.match(/^\d+$/ig) == null) { 
 		return false;
	} 
	
	return true;
}

/**
 * 사업자등록번호 체크 
 */
function checkBizID(bizID) { 
    // bizID는 숫자만 10자리로 해서 문자열로 넘긴다. 
	var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1); 
    var tmpBizID, i, chkSum=0, c2, remander;

    for (var i = 0; i <= 7; i++) chkSum += checkID[i] * bizID.charAt(i); 
    c2 = "0" + (checkID[8] * bizID.charAt(8)); 
    c2 = c2.substring(c2.length - 2, c2.length); 
    chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1)); 
    remander = (10 - (chkSum % 10)) % 10 ; 

    if (Math.floor(bizID.charAt(9)) != remander) {
        return false;
    } else {
    	return true;
    }
}

/**
 * enter key submit 방지
 * 
 * @param e
 * @return
 */
function stopRKey(evt) { 
	var evt = (evt) ? evt : ((event) ? event : null); 
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null); 
	if ((evt.keyCode == 13) && (node.type=="text"))  {return false;} 
} 

document.onkeypress = stopRKey;

/**
 * java.lang.String.startsWith() 구현
 * @param str
 * @param checker
 * @return
 */
function startsWith(str, checker){    
	if(str !=null && checker !=null && str.length > checker.length){        
    	if(str.toUpperCase().substr(0,checker.toUpperCase().length) == checker.toUpperCase()){            
        	return true;        
        }else{            
        	return false;      
        }    
	}else{       
    	return false; 
  	}
}

/**
 * java.lang.String.endsWith() 구현
 * @param str
 * @param checker
 * @return
 */
function endsWith(str, checker){   
	if(str != null && checker !=null && str.length > checker.length){        
		if(str.substr(str.length-checker.length).toUpperCase() == checker.toUpperCase()){            
			return true;       
		}else{            
			return false;       
	 	}    
	}else{       
	    return false;   
	}
}

/**
 * 영문 숫자만 가능
 * @param str
 * @return
 */
function isAlphaNumber(str) {

	var pattern = /[^(a-zA-Z0-9)]/;

	if(pattern.test(str)){
		return true;
	}else {
		return false;		
	}
}

/**
* 라디오 또는 체크 박스 동적 생성
*/ 
function createRadioOrCheckbox(element, type, name, id, value, text, linkUrl) { 
	var div = document.createElement("div");

    var radio = document.createElement(element);
    
    radio.setAttribute("type", type);
    radio.setAttribute("name", name);
    radio.setAttribute("id", id); 
    radio.setAttribute("value", value);  
    div.appendChild(radio);
    
    if(linkUrl != undefined) { 
       	var linkTag = document.createElement("a");
  	  	linkTag.setAttribute("href", linkUrl);
  	  	linkTag.appendChild(document.createTextNode(text));
  	  	div.appendChild(linkTag); 
		  
    }else {
    	div.appendChild(document.createTextNode(text)); 
    }
	 
    return div;
}

/**
 * ajax 로 해당 관계사의 부서 리스트 조회
 */
function ajaxDeptList(companyCd, groupCdFieldId) {
    var selectObject = document.getElementById("deptCd");
    var searchGroupCd = $("#"+groupCdFieldId).val();

    if (companyCd != "") {
        $.ajax({
            type: "POST",
            cache:false,
            url: env.contextPath + "/user/ajaxDeptList.do",                       
            data: {searchGroupCd: searchGroupCd, searchCompanyCd: companyCd},           
            dataType: "json",
            success: function(json) {
                if(json.success == "true") {
                    var deptList = eval(json.deptList);   // json 데이터 자바스크립트 인식위해 String -> Object 변환

                    // 기존에 있던 부서 목록 제거
                    if (selectObject.options.length > 0) {
                        for (var i = selectObject.options.length; i >= 0; i--) { 
                            delOption(selectObject, i);
                        }
                        addOption(selectObject, "", "- 전체 -");
                    } 

                    for (var i = 0; i < deptList.length; i++) {  
                        addOptionChecked(selectObject, deptList[i].DEPT_CD, deptList[i].DEPT_NM, "");
                    } 
                } else {
                	// 기존에 있던 부서 목록 제거
                    if (selectObject.options.length > 0) {
                        for (var i = selectObject.options.length; i >= 0; i--) { 
                            delOption(selectObject, i);
                        }
                        addOption(selectObject, "", "부서 없음");
                    }
                }
            }
        });
    }
}

function initSelect(id,str){
	var selectObject = document.getElementById(id);		
	if (selectObject.options.length > 0) {		
		for (var i = selectObject.options.length; i >= 0; i--) { 
			delOption(selectObject, i);
		}
		addOption(selectObject, "", str);
		 /* select tag width 사이즈 지정 */
		 selectObject.style.width = (eval(str.length) + 2) + "em";
	}
}