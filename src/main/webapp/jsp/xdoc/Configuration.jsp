<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>Helio System</title>
<meta http-equiv="Content-Type" content="text/html;">
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
<table width="100%" height="100%" border="0" cellpadding="0"
cellspacing="0">
  <tr>
    <td align="left" valign="top">      
		 <%@ include file="./../includes/MenuHeader.jsp" %>
	</td>
  </tr>

  <tr>
  	<td>
 	<%@ include file="ConfigInclude.html" %>
	</td>
</tr>
 <tr>
	 <td align="left" valign="top">
	   <!-- Footer starts here --> 
		 		 <%@ include file="./../includes/footer.jsp" %>
	   <!-- Footer ends here -->
	 </td>
  </tr>
</table>
</body>
</html>
