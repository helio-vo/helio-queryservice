<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>Helio System</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
 <%!String contextPath="n";%>
 <%contextPath=request.getContextPath();%>
<link rel="stylesheet" href="<%=contextPath%>/Style/Style.css" type="text/css">

<script src="<%=contextPath%>/Scripts/common.js"></script>
<script>
		 function doSub(action)
		 {
		 		 document.frmAdmin.action=action; 
		 		 document.frmAdmin.method="post";
		 	     document.frmAdmin.submit();		 		 
		 }
	
</script>
</head>

<body leftmargin="0" topmargin="0"  marginheight="0" marginwidth="0">
<DIV id="ToolTip">
  </DIV>

<table width="100%" height="100%" border="0" cellpadding="0"
cellspacing="0">
  <tr>
    <td align="left" valign="top">      
		 <%@ include file="./includes/MenuHeader.jsp" %>
	</td>
  </tr>
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
  <tr>
    <td height="100%" align="left" valign="top">
		 		   
   <s:form name="frmAdmin" id="frmAdmin" method="post" action="">
	 <table width="100%" border="0" cellspacing="0" cellpadding="1" >
		  <tr  class="txtHeading">
          <td width="100%" height="20" class="txtHeading"colSpan="2">
          Admin Page To Create Property File
         </td>
         </tr>
         <tr><td>&nbsp;</td></tr>
         <tr class="txtblackBP" > 
           <td>
              <ul>
                  <li><a href="javascript:doSub('showConfigurationDetials.action')"> How to configure a property file </a></li>		             
              </ul>            
          </td>
         </tr> 
         <s:if test="statusDisplay == true">
         
         <tr class="txtblackBP" > 
           <td>
              <ul>
                  <li><a href="javascript:doSub('getConfigurationPropertyFilePage.action')"> Create Property File </a></li>		             
              </ul>            
          </td>
         </tr> 
         </s:if>
         <tr class="txtblackBP" > 
           <td>
              <ul>
                  <li><a href="javascript:doSub('getDatabaseConfigurationPage.action')"> Configuration for connecting database </a></li>		             
              </ul>            
          </td>
         </tr>  
         <tr class="txtblackBP" > 
           <td>
              <ul>
                  <li><a href="javascript:doSub('showLogFile.action')"> Show log file </a></li>		             
              </ul>            
          </td>
         </tr> 
         <!--  <tr class="txtblackBP" > 
           <td>
              <ul>
                  <li><a href="javascript:doSub('instrumentMapperPage.action')"> Instrument mapper </a></li>		             
              </ul>            
          </td>
         </tr>    -->                    
      </table>   
     </s:form>
 	</td>
   </tr>
  <tr>
	 <td align="left" valign="top">
	   <!-- Footer starts here --> 
		 		 <%@ include file="./includes/footer.jsp" %>
	   <!-- Footer ends here -->
	 </td>
  </tr>
</table>

</body>
</html> 
