document.write("<script type='text/javascript' src='../Scripts/commonHeader.js'></script>");

var topColor, subColor, ContentInfo;

ContentInfo = "";

topColor = "#808080"
subColor = "#C0C0C0"

var mouse_X;
var mouse_Y;

var tip_active = 0;
function update_tip_pos(){
		document.getElementById('ToolTip').style.left = mouse_X + 20;
		document.getElementById('ToolTip').style.top  = mouse_Y;
}

var ie = document.all?true:false;
if (!ie) document.captureEvents(Event.MOUSEMOVE)
document.onmousemove = getMouseXY;

function getMouseXY(e) {
if (ie) { // grab the x-y pos.s if browser is IE
	if(event && document && document.body)
	{
		mouse_X = event.clientX + document.body.scrollLeft;
		mouse_Y = event.clientY + document.body.scrollTop;
	}
}
else { // grab the x-y pos.s if browser is NS
	mouse_X = e.pageX;
	mouse_Y = e.pageY;
}
if (mouse_X < 0){mouse_X = 0;}
if (mouse_Y < 0){mouse_Y = 0;}

if(tip_active){update_tip_pos();}
}



function EnterContent(TTitle, TContent){
ContentInfo = '<table border="0" cellspacing="0" cellpadding="0">'+
'<tr><td width="100%" bgcolor="#000000">'+

'<table border="0" width="100%" cellspacing="1" cellpadding="0">'+
'<tr><td width="100%" bgcolor='+subColor+'>'+

'<table border="0" width="90%" cellpadding="0" cellspacing="1" align="center">'+

'<tr><td width="100%" nowrap>'+

'<font class="tooltipcontent">'+TContent+'</font>'+

'</td></tr>'+
'</table>'+

'</td></tr>'+
'</table>'+

'</td></tr>'+
'</table>';

}

function tip_it(which, TTitle, TContent){


	if(which){
		update_tip_pos();
	
		tip_active = 1;
		
		document.getElementById('ToolTip').style.visibility = "visible";
		EnterContent(TTitle, TContent);
		
		document.getElementById('ToolTip').innerHTML = ContentInfo;
	
	}else{
		tip_active = 0;
	
		document.getElementById('ToolTip').style.visibility = "hidden";
	}

}
function tip_it1(which, TTitle, TContent,obj,length){

	
	
	if(which){
		update_tip_pos();
	
		tip_active = 1;
		
		document.getElementById('ToolTip').style.visibility = "visible";
		for(i=0;i<length;i++){
			
		   if(TContent==obj[i].id){
		   		EnterContent(TTitle, obj[i].description);
				
			}
		}
		
		document.getElementById('ToolTip').innerHTML = ContentInfo;
		
	}else{
		tip_active = 0;
	
		document.getElementById('ToolTip').style.visibility = "hidden";
	}
	
}
function formcontent(opt,desc,content){
try{
	if(content!=""){
		if(content.charAt(1)){
			content=content.substring(2);
			var arrString=content.split(":");
			if(arrString.length<2){
				if(arrString[0].split("_").length>1)
					content= "<li>" + "SERVER : " + arrString[0];
				else
					content= "<li>" + "SERVER : " + "OASYS_FIN_" + arrString[0];
						
				content=content + "<li>" + "DATABASE : " + "OasysFinDb";	
			}else{
				if(arrString[0].split("_").length>1){
					content="<li>" + "SERVER : " + arrString[0];
					content= content + "<li>" + "DATABASE : " + arrString[1];
				}else{
					content="<li>" + "SERVER : " + "OASYS_FIN_" + arrString[0];
					content= content + "<li>" + "DATABASE : " + arrString[1];
				}
				
			}
		}
	}
	tip_it(opt,desc,content);
  }catch(err){
  }
}
