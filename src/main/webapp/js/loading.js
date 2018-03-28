$(document).ready(function(){
	$("#displayBox")
	.attr("width","32")
	.attr("height","32")
	.css("display","none");
	
	
});

function startLoading(){
	$.blockUI({ message: $('#displayBox')
		  ,css:{opacity:0.5,
			  	top:($(window).height() - 32) /2 + 'px',
				left: ($(window).width() - 32) /2 + 'px',
				border:'none',
				backgroundColor: '#ffffff',
				width: '32px'},
				overlayCSS:  {
                    backgroundColor: '#ffffff',
                    opacity:         0.5,
                    cursor:          'wait'
                }
		});
}

function stopLoading(){
	$.unblockUI();
}