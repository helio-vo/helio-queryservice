<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java"%>
<%
	response.setHeader("Cache-Control","no-cache"); 
	response.setHeader("Pragma","no-cache");  
	response.setDateHeader ("Expires", 0); 
%>
 
<HTML lang=en xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
  <HEAD>
    <TITLE> 
    		Helio Query Service - Database configuration. 
    </TITLE>
    <META http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

    <%!String contextPath="n";%>
    <%contextPath=request.getContextPath();%>
    <LINK rel="stylesheet" href="<%=contextPath%>/Style/Style.css" type="text/css">
      <script src="<%=contextPath%>/Scripts/common.js"></script>
      <script src="<%=contextPath%>/Scripts/toolTip.js"></script>
      <script src="<%=contextPath%>/Scripts/menu.js"></script>
  </HEAD>

  <BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  onLoad="MM_preloadImages('<%=contextPath%>/Images/but_save_click.gif','<%=contextPath%>Images/but_cancel_click.gif');">
    <DIV id="ToolTip">
    </DIV>
    
    <form name="frmAdmin" id="frmAdmin" method="post" action="">
     <s:hidden name="insId" theme="simple"/>
    <TABLE width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
        <TR>
          <TD align="left" valign="top">
            <%@ include file="./includes/MenuHeader.jsp" %>
          </TD>
        </TR>
        <TR>
          <TD align="left" valign="top">
            <!-- Body starts here -->
            <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
              <TR>
                <TD  align="left" valign="middle" class="txtError"> 
                	<!--Error message can be displayed here....-->
                	<s:actionerror/>
                </TD>
              </TR>
            </TABLE>
           </TD>
        </TR>
	    <TR>
          <TD align="left" valign="top">
          &nbsp;
          </TD>
       </TR>
	  </table>
  	
		<TABLE width="100%" border="0" cellspacing="1" cellpadding="0" >
			<TR height=20 class="PopupAltDataRow"  >
				<TD nowrap class="txtblackBP" width="15%"> Database Configuration Page : </TD>
			</TR>
	     </TABLE>
	     <TABLE width="100%" border="0" cellspacing="1" cellpadding="0" >
	   		<TR height=20 class="PopupAltDataRow">
				<TD nowrap class="txtblackBP" width="15%">Jdbc Driver Name:</TD>
				<td >&nbsp;&nbsp;&nbsp;<s:textfield id="jdbcDriverName" name="jdbcDriverName" cssClass="textfield" size="20" maxlength="50"  theme="simple"/></td>				
			</TR>
			<TR height=20 class="PopupAltDataRow">
				<TD nowrap class="txtblackBP" >Jdbc URL:</TD>
				<td>&nbsp;&nbsp;&nbsp;<s:textfield id="jdbcUrl" name="jdbcUrl" cssClass="textfield" size="20" maxlength="50"  theme="simple"/></td>				
			</TR>
				<TR height=20 class="PopupAltDataRow">
				<TD nowrap class="txtblackBP" >Jdbc User:</TD>
				<td>&nbsp;&nbsp;&nbsp;<s:textfield id="jdbcUser" name="jdbcUser" cssClass="textfield" size="20" maxlength="50"  theme="simple"/></td>				
			</TR>
			<TR height=20 class="PopupAltDataRow">
				<TD nowrap class="txtblackBP" >Jdbc Password:</TD>
				<td>&nbsp;&nbsp;&nbsp;<s:textfield id="jdbcPassword" name="jdbcPassword" cssClass="textfield" size="20" maxlength="50"  theme="simple"/></td>				
			</TR>			
	     </TABLE>
  		 <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    <tr align="left" valign="middle">
				<td align="left">&nbsp;
					<a  onclick="javascript: doneDatabaseConnection()" style="cursor:hand" onMouseOver="MM_swapImage('addcol','','Images/but_addColumn_click.gif',1)" onMouseOut="MM_swapImgRestore()"><img src="Images/but_addColumn_normal.gif" name="Close"  border="0" align="absmiddle" id="addcol"></a>
				</td>
	        </tr>
	     </table>
	  	<table width="100%">
		<tr>
			 <td align="left" valign="top">
			   <!-- Footer starts here --> 
			 		 <%@ include file="./includes/footer.jsp" %>
			   <!-- Footer ends here -->
			 </td>
		 </tr> 
		</table>
</form>
</body></html>







