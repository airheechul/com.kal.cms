$.datepicker.regional['ko'] = {

		closeText: '닫기',
		prevText: '이전달',
		nextText: '다음달',
		currentText: '오늘',
		monthNames: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		dateFormat: 'yy-mm-dd', 
		firstDay: 0,
		isRTL: false
	};

	// datepicker 설정.
	$.datepicker.setDefaults(
			$.extend(
				{
					showMonthAfterYear: false, 
					showButtonPanel: true
				},
				$.datepicker.regional['ko']
			)
	);

	$(function(){
		$("#searchStartDt").datepicker(
									{
								   		showOn: 'button', 
							            showAnim: "show",
										buttonImage: env.contextPath +'/html/images/ko/ico_calendar.gif', 
										buttonImageOnly: true,
								   		buttonText: '등록일',
								   		maxDate: new Date(),
								   		beforeShow: function(input, inst) { // 달력 객체가 생성되기 전 이벤트
								   			// 확장 검색 버튼 숨기기
								   			$(".search-slide").hide();
								   			// 백그라운드 이미지 숨기기
								   			$(".bottomBgIner").hide();
								   		},
								   		onClose: function(dateText, inst) { // 달력 객체가 소멸 될 때 이벤트
								   			// 확장 검색 버튼 보이기
								   			$(".search-slide").show();
								   			// 백그라운드 이미지 보이기
								   			$(".bottomBgIner").show();
								   		}
									}
								);
	});
	$.datepicker.regional['ko'] = {

			closeText: '닫기',
			prevText: '이전달',
			nextText: '다음달',
			currentText: '오늘',
			monthNames: ['1월','2월','3월','4월','5월','6월', '7월','8월','9월','10월','11월','12월'],
			monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
			dayNames: ['일','월','화','수','목','금','토'],
			dayNamesShort: ['일','월','화','수','목','금','토'],
			dayNamesMin: ['일','월','화','수','목','금','토'],
			dateFormat: 'yy-mm-dd', 
			firstDay: 0,
			isRTL: false
		};

		// datepicker 설정.
		$.datepicker.setDefaults(
				$.extend(
					{
						showMonthAfterYear: false, 
						showButtonPanel: true
					},
					$.datepicker.regional['ko']
				)
		);

		$(function(){
			$("#searchEndDt").datepicker(
										{
									   		showOn: 'button', 
								            showAnim: "show",
											buttonImage: env.contextPath +'/html/images/ko/ico_calendar.gif', 
											buttonImageOnly: true,
									   		buttonText: '등록일',
									   		maxDate: new Date(),
									   		onSelect: function(){
									   		
											  var start_dates = $("#searchStartDt").val().split("-");
											  var end_dates = $("#searchEndDt").val().split("-");
											  
											  var date1 = new Date(start_dates[0],start_dates[1],start_dates[2]).valueOf();
											  var date2 = new Date(end_dates[0],end_dates[1],end_dates[2]).valueOf();
											  
											  if( (date2 - date1) < 0 ){ 
												  Common.alertDialog(Common.DIALOG_TITLE.NOTICE, "<h1>" + $("#currentMenuName").text() + "</h1><p>마지막날짜는 시작날짜 뒤로 선택하십시오.</p>");	
												  $("#searchEndDt").val("");
											   return;
											  }
								   			},
									   		beforeShow: function(input, inst) { // 달력 객체가 생성되기 전 이벤트
									   			// 확장 검색 버튼 숨기기
									   			$(".search-slide").hide();
									   			// 백그라운드 이미지 숨기기
									   			$(".bottomBgIner").hide();
									   		},
									   		onClose: function(dateText, inst) { // 달력 객체가 소멸 될 때 이벤트
									   			// 확장 검색 버튼 보이기
									   			$(".search-slide").show();
									   			// 백그라운드 이미지 보이기
									   			$(".bottomBgIner").show();
									   		}
										}
									);
		}); 
		
