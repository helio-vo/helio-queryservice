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
		 <%@ include file="./includes/IndexHeader.jsp" %>
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
          Introduction
         </td>
         </tr>
         <tr>
           <td>
         		<p class="txtblackbf">
         		Helio Full Time Protocol (HFTP) is a Web-service protocol which enables retrieval of time based data.
         		HFTP is an extension to the Simple Time Protocol, with a set of advanced requirements added to this specification version. 
         		This document formalizes the syntax and meaning of HFTP as a higher level parameter-based query language for querying tabular data.
         		The specifications given in HSTP comply with in this protocol, with a larger set of parameters added.
         		Therefore this version is identified as a 'FULL' protocol. HFTP complements the requirements of the IVOA Parameterized Query Language (PQL) and offers a more expressive and powerful extensions described further in the document.
				Unlike the earlier version of HSTP, the parameter query can be used to query a set of tabular data. Parametric queries are straightforward to express and to implement for cases where the data model is sufficiently well defined for the data to be queried, hiding many of the details required to pose and evaluate the query.
				In this sense HFTP provides an added capability to query multi-table data.
         		
         		</p>
           </td>
         </tr>
          <tr><td colspan="2">&nbsp;</td></tr> 
   
	 <tr>
	  <td width="100%" height="20" class="txtHeading"colSpan="2">
          Design
         </td>
         </tr>
         <tr>
           <td>
         		<img  src="<%=contextPath%>/Images/query_service.jpg" width="750" height="450">
           </td>
         </tr>
          <tr><td colspan="2">&nbsp;</td></tr> 
          <tr  class="txtHeading">
          <td width="100%" height="20" class="txtHeading"colSpan="2">
         Deployment process
         </td>
         </tr>
         <tr><td class="txtblackbf"><ul>
                  <li>Find an appropriate JDBC library ( Query Service supports Mysql database and lib file mysql.jar is included in the pakage;for any other database ; might have to download appropriate driver.) and place in location for webapp to find it. i.e tomcat/common/lib
        or WEB-INF/lib.</li>	
                  <li> Create a property file xxxxxx.txt. Please go to admin page and refer section 2, 3 and 4 for more details.</li>	
                  <li> Save the property file to the system.</li>
                  <li> Change JNDI environment (env) entry name in web.xml pointing to your property file. please go to admin page and refer section 4 for configuration of property file per
        context in tomcat.</li>      
                  <li>Once Done. May required to restart the app server depending on which server you are using.</li> 
                  <li> To create the property file through web please go to admin page and refer section 2.</li>        
              </ul>
            </td>

          </tr>
           <tr><td colspan="2">&nbsp;</td></tr>     
        <tr  class="txtHeading">
          <td width="100%" height="20" class="txtHeading"colSpan="2">
          Link to Admin page
         </td>
         </tr>
         <tr><td class="txtblackbf"><ul>
                  <li><a href="javascript:doSub('display.action')"> Admin Page </a></li>		             
              </ul></td></tr>
               <tr><td colspan="2">&nbsp;</td></tr>     
                            
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
