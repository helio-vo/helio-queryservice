<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>Helio System</title>
<meta http-equiv="Content-Type" content="text/html; ">
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

  <tr><td>
<div>
    <h1>
        <a name=""></a>
        <a name="46560036"></a>
        <a name="78252788"></a>
        <a name="250982853"></a>
    </h1>
    <h1>
        <h2>
            <a name="85690014"></a>
            <a name="250982861"></a>
            <strong></strong>
        </h2>
    </h1>
    <h1>
        <a name="250982853">1. Introduction</a>
    </h1>
    <h2>
        <a name="46560037"></a>
        <a name="78252789"></a>
        <a name="250982854">1.1. Document Background</a>
    </h2>
    <h3>
        <a name="46560038"></a>
        <a name="78252790"></a>
        <a name="250982855">1.1.1. Purpose of Document</a>
    </h3>
    <p>
        The purpose of this document is to explain Helio Query Service Property File.
    </p>
    <p>
        This document is intended for HELIO IT team members responsible for deployment and configuration of HELIO Query Service.
    </p>
    <h3>
        <a name="250982857">1.1.2. Assumptions Dependencies and Constrains</a>
    </h3>
    <p>
        <strong></strong>
    </p>
    <p>
        0. In Release 1 we are testing this service with HEC.
    </p>
    <h3>
        1.1.3. Abbreviation Used
    </h3>
    <p>
        1. HEC: Helio Event Catalogue Service.
    </p>
    <h3>
        1.1.4. Step To Follow For Deployment
    </h3>
    <p>
        1. Find an appropriate JDBC library i.e mysql-connector-java-3.0.16-ga-bin.jar and place in location for webapp to find it. i.e tomcat/common/lib or
        WEB-INF/lib.
    </p>
    <p>
        2. Create a property file xxxxxx.txt . Refer section 2, 3 and 4 for more details.
    </p>
    <p>
        3. Save the property file to the system.
    </p>
    <p>
        4. Change JNDI environment (env) entry name in web.xml pointing to your property file. Refer section 4 for configuration of property file per context
        in tomcat.
    </p>
    <p>
        &nbsp;env-entry&nbsp;
    </p>
    <p>
        &nbsp;env-entry-name&nbsp;property/context&nbsp;/env-entry-name&nbsp;
    </p>
    <p>
        &nbsp;env-entry-type&nbsp;java.lang.String&nbsp;/env-entry-type&nbsp;
    </p>
    <p>
        &nbsp;env-entry-value&nbsp;/Users/<u>vineethtshetty</u>/HELIO/<u>sec</u>-hessi.txt&nbsp;/env-entry-value&nbsp;
    </p>
    <p>
        &nbsp;/env-entry&nbsp;
    </p>
    <p>
        5. Done. May required to restart the app server depending on which server you are using.
    </p>
    <h2>
        <a name="85690014"></a>
        <a name="250982861">2. </a>
        Helio Query Service Property File
    </h2>
    <p>
        <strong>jdbc.driver=</strong>
        com.mysql.jdbc.Driver
    </p>
    <p>
        <strong></strong>
    </p>
    <p>
        <strong>jdbc.url=</strong>
        jdbc:mysql://msslxt.mssl.ucl.ac.uk:3306/solarevent
    </p>
    <p>
        <strong>
            <p>
            </p>
            jdbc.user=
        </strong>
        sec<strong></strong>
    </p>
    <p>
        <strong>
            <p>
            </p>
            jdbc.password=
        </strong>
        xxxxxxx<strong></strong>
    </p>
    <p>
        <strong>
            <p>
            </p>
            sql.query=
        </strong>
        select * from hessi_flare where time_start&nbsp;='[:kwstartdate:]' AND time_end&nbsp;='[:kwenddate:]'
    </p>
    <p>
        <strong>
            <p>
            </p>
            sql.query.instr.constraint=
        </strong>
    </p>
    <p>
        <strong>
            <p>
            </p>
            sql.query.orderby.constraint=
        </strong>
        ORDER BY time_start LIMIT 50<strong></strong>
    </p>
    <p>
        <strong></strong>
    </p>
    <p>
        <strong>#Acces url should always be first in VOTABLE and Format always last in VOTable if you are using it as ur output column.</strong>
    </p>
    <p>
        <strong></strong>
    </p>
    <p>
        <strong>sql.columnnames=</strong>
        hef_id::time_start::time_peak::time_end::nar::x_arcsec::y_arcsec::radial_arcsec::duration::count_sec_peak::total_count::energy_kev::flare_number
    </p>
    <p>
        <strong>
            <p>
            </p>
            sql.columndesc=
        </strong>
        Sec hess Id::Time Start::Peak Time::End Time::Active Region::X sec::Y sec::Radial sec::Duration::Count sec peek::Total count::energy kev::flare number
    </p>
    <p>
        <strong>sql.columnucd= </strong>
        ::time.start::time.phase::time.end:: :: :: :: :: :: :: :: :: ::
    </p>
    <p>
        <strong>sql.votable.head.desc=</strong>
        Helio Sec time based query
    </p>
    <p>
        <strong>sql.votable.accesurl=</strong>
    </p>
    <p>
        <strong>sql.votable.format=</strong>
    </p>
    <h2>
        <a name="250982862">3. Property File Element Description</a>
    </h2>
    <table border="1" cellpadding="0" cellspacing="0">
        <tbody>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>Jdbc.driver</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        JDBC driver name.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>Jdbc.url</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        JDBC URL with database name.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>jdbc.user</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        Database User Name.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>jdbc.password</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        Database password.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.query</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        SQL query string with time constrain.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.query.instr.constraint</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        Where clause with Instrument.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.query.orderby.constraint</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        Oder by clause of the query.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.columnnames</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        VOTABLE column names. Separated by
                    </p>
                    <p>
                         <strong>:: </strong> .
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.columndesc</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        VOTable column description. Separated by  <strong>:: </strong> .
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.columnucd</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        VOTable column UCD names. Separated by  <strong>:: </strong> .
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.votable.head.desc</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        VOTable heading (Description of heading).
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.votable.accesurl</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        Hard coded Access URL for VOTABLE.
                    </p>
                </td>
            </tr>
            <tr>
                <td valign="top" width="221">
                    <p>
                        <strong>sql.votable.format</strong>
                    </p>
                </td>
                <td valign="top" width="221">
                    <p>
                        Hard coded Format.
                    </p>
                </td>
            </tr>
        </tbody>
    </table>
    <h2>
        4. Configuration of property file per context in Tomcat
    </h2>
    <p>
        Bellow is the steps to be followed to configure property file per context.
    </p>
    <p>
        1. Add &nbsp;Resource&nbsp; tag inside &nbsp;context&nbsp; tag. As shown bellow
    </p>
    <p>
        &nbsp;Resource name=<em>"property/context"</em> auth=<em>"Container"</em>
    </p>
    <p>
        type=<em>"java.lang.String"</em>
    </p>
    <p>
        factory=<em>"com.org.helio.common.util.BeanFactory"</em>
    </p>
    <p>
        propertyValue=<em>"C:/log/helio.txt"</em>/&nbsp;
    </p>
    <p>
        2. In the above &nbsp;Resource&nbsp; tag you can see a factory element referring to a Bean Factory class. Code for retrieving property Value is implemented
        in this class.
    </p>
    <p>
        3. propertyValue” element in &nbsp;Resource&nbsp; tag is the actual path of the property file. When I say path” it is path appended with file name as
        shown.
    </p>
    <p>
        4. type” element in &nbsp;Resource&nbsp; tag defines the return type which is a String in our case.
    </p>
    <p>
        5. If you are configuring in Tomcat server, &nbsp;Resource&nbsp; tag should be included in server.xml under &nbsp;context&nbsp; tag.
    </p>
    <h2>
        <a name="250982863">5. Query Service Output Sample</a>
    </h2>
    <p>
        &nbsp;?xml version="1.0" encoding="UTF-16"?&nbsp;
    </p>
    <p>
        &nbsp;helio:queryResponse xmlns:helio="http://helio.org/xml/QueryService/v1.0"&nbsp;&nbsp;VOTABLE version="1.1"
        xmlns="http://www.ivoa.net/xml/VOTable/v1.1"&nbsp;
    </p>
    <p>
        &nbsp;RESOURCE&nbsp;
    </p>
    <p>
        &nbsp;DESCRIPTION&nbsp;Helio ICS time based query&nbsp;/DESCRIPTION&nbsp;
    </p>
    <p>
        &nbsp;TABLE nrows="1"&nbsp;
    </p>
    <p>
        &nbsp;FIELD arraysize="*" datatype="char" name="<strong>ACCESS_URL</strong>" ucd="VOX:AccessReference"&nbsp;
    </p>
    <p>
        &nbsp;DESCRIPTION&nbsp;Url Pointing To Data File&nbsp;/DESCRIPTION&nbsp;
    </p>
    <p>
        &nbsp;/FIELD&nbsp;
    </p>
    <p>
        &nbsp;FIELD arraysize="*" datatype="char" name="<strong>INS_ID</strong>" ucd="ucd.ins.insid"&nbsp;
    </p>
    <p>
        &nbsp;DESCRIPTION&nbsp;Instrument Short Name&nbsp;/DESCRIPTION&nbsp;
    </p>
    <p>
        &nbsp;/FIELD&nbsp;
    </p>
    <p>
        &nbsp;FIELD arraysize="*" datatype="char" name="<strong>FORMAT</strong>" ucd="VOX:Format"&nbsp;
    </p>
    <p>
        &nbsp;DESCRIPTION&nbsp;Format&nbsp;/DESCRIPTION&nbsp;
    </p>
    <p>
        &nbsp;/FIELD&nbsp;
    </p>
    <p>
        &nbsp;DATA&nbsp;
    </p>
    <p>
        &nbsp;TABLEDATA&nbsp;
    </p>
    <p>
        &nbsp;TR&nbsp;
    </p>
    <p>
        &nbsp;TD&nbsp;http://localhost:8080/HelioQueryService/HelioQueryService&nbsp;/TD&nbsp;
    </p>
    <p>
        &nbsp;TD&nbsp;HXT&nbsp;/TD&nbsp;
    </p>
    <p>
        &nbsp;TD&nbsp;GRAPHICS&nbsp;/TD&nbsp;
    </p>
    <p>
        &nbsp;/TR&nbsp;
    </p>
    <p>
        &nbsp;/TABLEDATA&nbsp;
    </p>
    <p>
        &nbsp;/DATA&nbsp;
    </p>
    <p>
        &nbsp;/TABLE&nbsp;
    </p>
    <p>
        &nbsp;/RESOURCE&nbsp;
    </p>
    <p>
        &nbsp;/VOTABLE&nbsp;
    </p>
    <p>
        &nbsp;/helio:queryResponse&nbsp;
    </p>
    <br/>
    <strong></strong>
    <p>
        <strong>
            <br/>
        </strong>
    </p>
</div>
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
