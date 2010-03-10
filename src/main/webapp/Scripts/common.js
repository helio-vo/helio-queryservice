function SwitchMenu(obj,obj1,obj2){
	if(document.getElementById){
	var el = document.getElementById(obj);
	var e2 = document.getElementById(obj1);
	var e3 = document.getElementById(obj2);
	var ar = document.getElementById("masterdiv").getElementsByTagName("span");
	//DynamicDrive.com change
	var ar2 = document.getElementById("masterdiv").getElementsByTagName("td");

	var e=document.getElementById("div");


	//DynamicDrive.com change
		if(el.style.display != "block"){ //DynamicDrive.com change
			el.style.display = "block";
			e2.style.display = "none";
			e3.style.display = "block";
			//e.style.height = '390px';	
		}else{
			el.style.display = "none";
			e2.style.display = "block";
			e3.style.display = "none";
			//e.style.height = '600px';	

		}
	}

}

function toolTipObj(id,description){
	this.id=id
	this.description=description
	
}

function commonAjaxOpen(request, url) {
	
	var s = "&"
	if (url.indexOf("?") == -1) s = "?";
	request.open("POST",url + s + (new Date()).getTime(),false);
}

function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}
function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
}
function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
}



function confirmYesNo(str)
{
    execScript('n = msgbox("'+str+'","4132","Yes/No")', "vbscript");
    return(n == 6);
}



 function confirmDel(ver,sessionKey){
			var inputtype;
			inputtype=confirmYesNo("The size of the file is greater than threshold value. Click Yes to show first 2000 rows and NO to export to excel?")
	    	if(inputtype)
	    	{		    
		   		document.forms[0].action ="displayReportDetails.action?bLoad=true&sReqSessionKey="+sessionKey;
  				document.forms[0].method = "post";
  				document.forms[0].submit();
  			}
			else{
				document.forms[0].action ="getAdhocExcelReport.action?status="+inputtype;
  				document.forms[0].method = "post";
  				document.forms[0].submit();
				
			}
		
	}
 

var dtCh= "/";
var minYear=1900;
var maxYear=2100;



function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strMonth=dtStr.substring(0,pos1)
	var strDay=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		return false
	}
	return true
}


function validateFormat(dtStr)
{
	var newStr="";
	dtStr = replaceAll(dtStr,"-","/");
		
	var pos1=dtStr.indexOf(dtCh);
	if(pos1==-1)
	{
			var strMonth=dtStr.substring(0,2);
			var strDay=dtStr.substring(2,4);
			var strYear=dtStr.substring(4);
			newStr=strMonth+"/"+strDay+"/"+strYear;
	}
	else
	{
		newStr=dtStr;
	}
	return newStr;
}

function replaceAll( str, from, to ) {

    var idx = str.indexOf( from );


    while ( idx > -1 ) {
        str = str.replace( from, to ); 
        idx = str.indexOf( from );
    }

    return str;
}

//Added by Khadija for acceptin different date formats & checkin it 
function checkInputDateFormat(txtObject) 
{ 
        var flag = true; 
        var txtDate = txtObject.value; 
        var temp = ""; 
        var txtDate_len = txtObject.value.length; 
       // alert(txtDate_len); 
        var st=""; 
        
        //get todays date 
        var now  = new Date(); 
        var month = now.getMonth()+1; 
        var day = now.getDate(); 
        var year = now.getYear(); 
        
        if(day<10) 
                day = "0"+day; 
        if(month<10) 
                month = "0"+month; 
        
        if(txtDate_len ==0)      
	{
			return;
	}
        
        
        //if the entered date is mm/dd/yyyy the length will be 10 
        if(txtDate_len ==10) 
                { 
                        
                         temp=validateFormat(txtDate); 
                        
                         if (isDate(temp)==false) 
                                { 
                                        alert("Please Enter valid Date in MM/DD/YYYY format "); 
                                        flag=false; 
                                        
                                        
                                } 
                } 
        else if(txtDate_len == 8) //in mmddyyyy 
                {         
                        
                        
                        temp=validateFormat(txtDate); 
                        
                        if (isDate(temp)==false) 
                                { 
                                        alert("Please Enter valid Date in MMDDYYYY format"); 
                                        flag=false; 
                                        
                                } 
                        else //date entered is valid but has to be converted in  mm/dd/yyyy format 
                                { 
                                        txtObject.value=temp; 
                                }         
                        
                 } 
        else if(txtDate_len == 6) //in mmyyyy         
                { 
                        var month1 = txtDate.substring(0,2); 
                        var year1 = txtDate.substring(2); 
                        var temp_date = month1+"/01/"+year1; 
                        
                        st = validateFormat(temp_date); 
                        
                        if (isDate(st)==false) 
                                { 
                                        alert("Please Enter valid Date in MMYYYY format"); 
                                        flag=false; 
                                        
                                } 
                        else 
                                { 
                                        txtObject.value = temp_date; 
                                } 
                 } 
        else //Does not support any of the formats 
                { 
                        //alert("Not supported format"); 
                        flag=false; 
                }         
        
                if(!flag)//if false set to current date 
                        { 
                                //alert("i set") 
                                txtObject.value = month+"/"+day+"/"+year;         
                        } 
                
        return ;         
}         

function getDetails(){
	   document.frName.target="_self";
	   document.frName.action="commonaction.action";
	   document.frName.method="post";
	   document.frName.submit();
}





function functionDoRowSel(rowID)
	{
		var row = document.getElementById("colRowId"+rowID);
		var cBox = document.getElementsByName("sColumnName");
		//alert(row+" rowID :"+rowID+"cBox :"+cBox);
		if(row.className=="SelectionRow")
		{
			cBox[rowID-1].checked=false;
			if(rowID%2==0)
			{
				row.className = "PopupAltDataRow" ;
			}
			else
			{
				row.className  = "PopupDataRow";
			}
		}
		else
		{
			cBox[rowID-1].checked=true;
			row.className="SelectionRow";
		}
	}


var columnAjaxRequest=null;


function getTableColumns(){
	//var filterValues = document.forms[0].eventFilterNameValue;
	document.getElementById("columnTableDiv").style.display = "block";		
			
	commonColumnAJAXRequest();
}

//common AJAX call
function commonColumnAJAXRequest()
{
	var tableName = document.getElementById("cmbDatabaseTableList").value;
	
	var param = "tableName="+tableName;
	
	var url="getTableColumnList.action";
	
	try{
		columnAjaxRequest=new ActiveXObject("Microsoft.XMLHTTP");
	}catch(other){
	  try{
			columnAjaxRequest=new XMLHttpRequest();
	   }catch(browser){alert("Ajax Not Supported")}
	}
	columnAjaxRequest.open("POST",url,true);
	columnAjaxRequest.onreadystatechange=function(){
				var ready=columnAjaxRequest.readyState;
				if(ready == 4)
				{
					var data=columnAjaxRequest.responseText;
					document.getElementById("columnTableDiv").innerHTML=data;
				}	
	}
	
	columnAjaxRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	columnAjaxRequest.send(param);	
	
}



function addColumnsOfSelectedTable()
{
	var selectedColumnValues="";
	var strColumnDesc="";
	var strColumnUcd="";
	var sColNameDt=document.forms[0].sColumnName;
	var sColDesc=document.forms[0].columnDescription;
	var sColUcd=document.forms[0].columnUcd;
	
	for (i=0;i<sColNameDt.length;i++){
		if(sColNameDt[i].checked){		
			var sColDes="";
			var sColUcdValue="";
			selectedColumnValues+= sColNameDt[i].value+'::';	
			if(sColDesc[i].value!=null && sColDesc[i].value!=""){
				sColDes=sColDesc[i].value;
			}else{
				sColDes=" ";
			}
			if(sColUcd[i].value!=null && sColUcd[i].value!=""){
				sColUcdValue=sColUcd[i].value;
			}else{
				sColUcdValue=" ";
			}
			strColumnDesc+=sColDes+'::';
			strColumnUcd+=sColUcdValue+'::';
		}
	}
	
	var timeConstraint=document.forms[0].timeConstraint.value;
	var instrumentConstraint=document.forms[0].instrumentConstraint.value;
	var coordinateConstraint=document.forms[0].coordinateConstraint.value;
	var orderByConstraint=document.forms[0].orderByConstraint.value;
	var limitConstraint=document.forms[0].limitConstraint.value;
	var tableName=document.forms[0].cmbDatabaseTableList.value;
	//alert(" selectedColumnValues : "+selectedColumnValues +"timeConstraint "+timeConstraint+"instrumentConstraint "+instrumentConstraint+"coordinateConstraint "+coordinateConstraint );
	if(limitConstraint!=null && limitConstraint!=""){
		if(IsNumeric(limitConstraint)==false){
			alert("Max record allowed should be a numeric value.");
			document.forms[0].limitConstraint.focus();
			return true;
		}
	}
	if(selectedColumnValues==null || selectedColumnValues==""){
		alert("Please select atleast one column name and then click on AddColumn button.");
		return true;
	}else{
		selectedColumnValues=selectedColumnValues.substring(0,selectedColumnValues.length-2);
	}
	
	//Coulumn description.
	if(strColumnDesc!=null && strColumnDesc!=""){
		strColumnDesc=strColumnDesc.substring(0,strColumnDesc.length-2);
	}
	
	//Coulumn ucd's.
	if(strColumnUcd!=null && strColumnUcd!=""){
		strColumnUcd=strColumnUcd.substring(0,strColumnUcd.length-2);
	}
	
	//create the Filter Row
	var table = document.getElementById("addedColumns");
	var rows = table.rows;
	var lastRow = rows.length;

	//alert("rows "+rows);
	var rowsCount=0;
	if(rows.length>1){
		rowsCount = parseInt(rows.length-1); 
	}
	
	//Checking for time constraint
	if(timeConstraint==null || timeConstraint==""){
		timeConstraint=" ";
	}
	
	//Checking for instrument constraint
	if(instrumentConstraint==null || instrumentConstraint==""){
		instrumentConstraint=" ";
	}
	//Checking for coordinates constraint
	if(coordinateConstraint==null || coordinateConstraint==""){
		coordinateConstraint=" ";
	}
	
	//Checking for order by constraint
	if(orderByConstraint==null || orderByConstraint==""){
		orderByConstraint=" ";
	}
	
	//Checking for limit constraint
	if(limitConstraint==null || limitConstraint==""){
		limitConstraint=" ";
	}
	//alert(" rowsCount : "+rowsCount);
	var hiddenValue=tableName+"^$$^"+selectedColumnValues+"^$$^"+timeConstraint+"^$$^"+instrumentConstraint+"^$$^"+coordinateConstraint+"^$$^"+orderByConstraint+"^$$^"+limitConstraint+"^$$^"+strColumnDesc+"^$$^"+strColumnUcd;
	var columnHidValue= '<input type="hidden" name="addedTableDetails" id="addedTableDetails'+rowsCount+'" value="'+hiddenValue+'">';
	
	var previousRowClassName="";
	for(var i=0;i<rows.length;i++){
		if(rows[i].className!=null && rows[i].className!=""){
			previousRowClassName=rows[i].className;
		}
	}
	
	//alert("previousRowClassName "+previousRowClassName);
	
	//get new Row Class Name
	var newRowClassName;
	if(previousRowClassName=="PopupAltDataRow"){
		newRowClassName="PopupDataRow";
	}else{
		newRowClassName="PopupAltDataRow";
	}
	
	//alert("table "+table);
	
	var newRow= table.insertRow(lastRow);
	newRow.id="columnRow"+lastRow;
	newRow.className = newRowClassName;
	newRow.align = "left";
	
	var oCell = newRow.insertCell(0);
	oCell.innerHTML = '<a href="#"><img src="Images/delete.gif"  alt="" border="0" title="Remove Table" onClick=javascript:deleteTable("'+lastRow+'");></a>';
	oCell.align="left";
	oCell.width=20;
	
	oCell = newRow.insertCell(1);
	oCell.innerHTML =trim(tableName);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
   	
	oCell = newRow.insertCell(2);
	oCell.innerHTML =checkIfValueEmpty(selectedColumnValues);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(3);
	oCell.innerHTML =checkIfValueEmpty(strColumnDesc);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(4);
	oCell.innerHTML =checkIfValueEmpty(strColumnUcd);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(5);
	oCell.innerHTML =trim(timeConstraint);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(6);
	oCell.innerHTML =trim(instrumentConstraint);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(7);
	oCell.innerHTML =trim(coordinateConstraint);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(8);
	oCell.innerHTML =trim(orderByConstraint)+columnHidValue;
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	oCell = newRow.insertCell(9);
	oCell.innerHTML =trim(limitConstraint);
	oCell.align="left";
	oCell.style.paddingLeft="10px";
	oCell.width=300;
	
	var cmbTableList = document.getElementById("cmbDatabaseTableList");
	cmbTableList.remove(cmbTableList.selectedIndex);
	
	document.getElementById("columnTableDiv").style.display = "none";
	
}

function doneDatabaseConnection() 
{
	   
	   var jdbcDriverName=document.forms[0].jdbcDriverName.value;
	   var jdbcUrl=document.forms[0].jdbcUrl.value;
	   var jdbcUser=document.forms[0].jdbcUser.value;
	   var jdbcPassword=document.forms[0].jdbcPassword.value;
			
		   
		if(jdbcDriverName==null || jdbcDriverName==""){
			alert("Please enter Jdbc Driver Name.");
			document.forms[0].jdbcDriverName.focus();
			return true;
		}
		
		if(jdbcUrl==null || jdbcUrl==""){
			alert("Please enter Jdbc URL.");
			document.forms[0].jdbcUrl.focus();
			return true;
		}
		/*
		if(jdbcUser==null || jdbcUser==""){
			alert("Please enter Jdbc User Name.");
			return true;
		}
		
		if(jdbcPassword==null || jdbcPassword==""){
			alert("Please enter Jdbc Password.");
			return true;
		}
		*/
		
		
		document.forms[0].target="_self";
		document.forms[0].action="getConfigurationPropertyFilePage.action";
		document.forms[0].method="post";
		document.forms[0].submit();
		
		
}

function deleteTable(rowIndex){		    
  	//alert("  rowIndex  "+rowIndex);
    var table=document.getElementById("addedColumns");
    var Row = document.getElementById("columnRow"+rowIndex);
    var tableName = Row.cells[1].innerHTML;
	//alert("tableName "+tableName)
	table.deleteRow(Row.rowIndex);
	var cmbTableList = document.getElementById("cmbDatabaseTableList");
	var opt=document.createElement('option');
	opt.value=tableName;
	opt.text=tableName;
	try {
		cmbTableList.add(opt,null); // standard compliant; doesn't work in IE
	  }
	  catch(ex) {
		  cmbTableList.add(opt); // IE only
	  }

	
}


function doneColumnAdd()
{
	   var jdbcDriverName=document.forms[0].jdbcDriverName.value;
	   var jdbcUrl=document.forms[0].jdbcUrl.value;
	   var jdbcUser=document.forms[0].jdbcUser.value;
	   var jdbcPassword=document.forms[0].jdbcPassword.value;
	   var serviceDesc=document.forms[0].serviceDesc.value;
	   var sAddedTableDetails=document.forms[0].addedTableDetails;
	   var fileNamePath=document.forms[0].fileNamePath.value;
	   
	   //alert(jdbcDriverName);
	   
		if(jdbcDriverName==null || jdbcDriverName==""){
			alert("Please enter Jdbc Driver Name.");
			return true;
		}
		
		if(jdbcUrl==null || jdbcUrl==""){
			alert("Please enter Jdbc URL.");
			return true;
		}
		
		/*if(jdbcUser==null || jdbcUser==""){
			alert("Please enter Jdbc User Name.");
			return true;
		}
		
		if(jdbcPassword==null || jdbcPassword==""){
			alert("Please enter Jdbc Password.");
			return true;
		}*/
		
		if(serviceDesc==null || serviceDesc==""){
			alert("Please enter service description.");
			document.forms[0].serviceDesc.focus();
			return true;
		}
		
		if(fileNamePath==null || fileNamePath==""){
			alert("Please enter file name and path.");
			document.forms[0].fileNamePath.focus();
			return true;
		}		
	
		if(typeof sAddedTableDetails == "undefined"){
			alert("you connot create configuration file with no tables or columns. Please click on AddColumn button.");
			return true;
		}
		
		document.forms[0].target="_self";
		document.forms[0].action="createConfigurationFile.action?jdbcDriverName="+jdbcDriverName+"&jdbcUrl="+jdbcUrl+"&jdbcUser="+jdbcUser+"&jdbcPassword="+jdbcPassword;
		document.forms[0].method="post";
		document.forms[0].submit();
	
}


function trim(stringToTrim) {
	 return stringToTrim.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
}
function ltrim(stringToTrim) {
	return stringToTrim.replace(/^\s+/,"");
}
function rtrim(stringToTrim) {
	return stringToTrim.replace(/\s+$/,"");
}

function replaceAll( str, from, to ) {
	    var idx = str.indexOf( from );
	    while ( idx > -1 ) {
	        str = str.replace( from, to ); 
	        idx = str.indexOf( from );
	    }
	 return str;
}

function alphanumeric(alphane)
{
	var numaric = alphane;
	var count=0;
	for(var j=0; j<numaric.length; j++)
		{
		  var alphaa = numaric.charAt(j);
		  var hh = alphaa.charCodeAt(0);
		  if((hh > 47 && hh<58) || (hh > 64 && hh<91) || (hh > 96 && hh<123))
		  {
		  }
		else	{
                      
			 return false;
		  }
 		}

 return true;
}

function checkIfValueEmpty(value){
	//alert(value);
	var mytool_array=value.split("::");
	//alert(mytool_array);
	var sNoEmptyValue="";
	var count=0;
	for(var j=0; j<mytool_array.length; j++)
	{
		 var alphaa = trim(mytool_array[j]);
		 //alert(alphaa);
		 if(alphaa!=null && alphaa!="" && alphaa!=" ")
		 {
			 if(count==0){
				 sNoEmptyValue=sNoEmptyValue+alphaa+",";
			 }else{
				 sNoEmptyValue=sNoEmptyValue+","+alphaa;
			 }
			 count++;
			 //alert("sNoEmptyValue "+sNoEmptyValue+" count "+count);
		 }
		 if(count==1){
			 sNoEmptyValue=sNoEmptyValue.substring(0,sNoEmptyValue.length-1);
		 }
	}
	
	return sNoEmptyValue;
	
}


function IsNumeric(strString)
//  check for valid numeric strings	
{
var strValidChars = "0123456789";
var strChar;
var blnResult = true;

if (strString.length == 0) return false;

//  test strString consists of valid characters listed above
for (i = 0; i < strString.length && blnResult == true; i++)
   {
   strChar = strString.charAt(i);
   if (strValidChars.indexOf(strChar) == -1)
      {
      blnResult = false;
      }
   }
return blnResult;
}


