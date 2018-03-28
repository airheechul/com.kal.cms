function ShowFlash(url,s_width,s_height,bg_colors,option_string,s_value)
{
	html_text = '<object type="application/x-shockwave-flash" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" id="param" width="'+s_width+'" height="'+s_height+'">';
	html_text += '<param name="movie" value="'+url+'">';
	html_text += '<param name="quality" value="high">';
	html_text += option_string;
	html_text += '<param name="FlashVars" value="'+s_value+'">';
	html_text += '<param name="base" value=.>';
	html_text += '<PARAM NAME=wmode VALUE=transparent>';
	html_text += '<param name="bgcolor" value="'+bg_colors+'">';
	html_text += '<embed src="'+url+'" quality=high bgcolor="#ffffff" width="'+s_width+'" height="'+s_height+'" name="topSWF" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" base="." wmode="transparent" FlashVars="'+s_value+'" />';
	html_text += '<\/object>';
	document.write(html_text);
}