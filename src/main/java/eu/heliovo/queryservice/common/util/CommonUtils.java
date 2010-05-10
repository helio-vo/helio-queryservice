/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.io.StringWriter;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public class CommonUtils {
	private static final String ARGSUFFIX = ":]";

	private static final String ARGPREFIX = "[:";
	
	protected final  Logger logger = Logger.getLogger(this.getClass());
	
	public static String replaceParams(String sText , HashMap<String,String> hmArgs)
	{
		StringBuffer sTextQuery = new StringBuffer(sText);
		if (hmArgs != null) {
			int argBeginIndex;
			while ((argBeginIndex = sTextQuery.indexOf(ARGPREFIX)) != -1) {
				int argEndIndex = sTextQuery.indexOf(ARGSUFFIX);
				String argKey = sTextQuery.substring(argBeginIndex + ARGPREFIX.length(), argEndIndex);
				String argValue = (String) hmArgs.get(argKey);
				if (argValue == null || argValue.equals("")) {
					//sTextQuery = sTextQuery.delete(argBeginIndex, argEndIndex + sTextQuery.length());
					sTextQuery = sTextQuery.replace(argBeginIndex, argEndIndex + ARGSUFFIX.length(), "");
				} else {
					sTextQuery = sTextQuery.replace(argBeginIndex, argEndIndex + ARGSUFFIX.length(), argValue);
				}
			}
		}
		return sTextQuery.toString();
	}
	
	
	public static String getPropertyFilePath() throws NamingException{
			InitialContext initCtx = new InitialContext();			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			String  propertyBean = (String ) envCtx.lookup("property/context");
			return propertyBean;
	 }	
	
	
	public static String getColumnNamesForQuery(){
		String[] columnNames=ConfigurationProfiler.getInstance().getProperty("sql.columnnames").split("::");
		StringBuffer result = new StringBuffer(); 
	    if (columnNames.length > 0) {
	        result.append(columnNames[0]);
	        for (int i=1; i<columnNames.length; i++) {
	            result.append(columnNames[i]);
	        }
	    }
	    return result.toString();
	}
	
	
	/**
	   * Reveals the base URI of the web application.
	   *
	   * @return The URI.
	   */
	  public static  String getUrl(HttpServletRequest req,String job_Id) { 
		  
		  String scheme = req.getScheme(); // http 
		  String serverName = req.getServerName(); // hostname.com 
		  int serverPort = req.getServerPort(); // 80 
		  String contextPath = req.getContextPath(); // mywebapp 
		  String url = scheme+"://"+serverName+":"+serverPort+contextPath+"/ServiceJobStatus?MODE=file&amp;ID="+job_Id; 
		 		  
		  return url; 
	  }
	  
    /*
	 * create Xml for webservice request.
	 */
	 public static String createXmlForWebService(String randomUUIDString,String Status,String sUrl) throws Exception {
		 StringBuilder xmlString = new StringBuilder();
		 xmlString.append("<ResultInfo>");
		 xmlString.append("<ID>");
		 xmlString.append(randomUUIDString);
		 xmlString.append("</ID>");
		 String sDes=null;
		 String statusArray[]=null;
		 //Status of completion.
		 if(Status!=null && !Status.trim().equals(""))
			 statusArray=Status.split("::");
		 //Description if the error occured
		 if(statusArray!=null && statusArray.length>1)
			 sDes=statusArray[1];
		 //Status for the service
		 if(Status!=null && !Status.trim().equals("")){
			 xmlString.append("<status>");
			 xmlString.append(statusArray[0]);
			 xmlString.append("</status>");
		 }
		 
		// Des for the file location.
		 if(sDes!=null && !sDes.trim().equals("")){
			 xmlString.append("<statusdescription>");
			 xmlString.append(sDes);
			 xmlString.append("</statusdescription>");
		 }
		 // Url for the file location.
		 if(sUrl!=null && !sUrl.trim().equals("")){
			 xmlString.append("<resultURI>");
			 xmlString.append(sUrl);
			 xmlString.append("</resultURI>");
		 }
		 
		 //Result info
		 xmlString.append("<fileInfo>");
		 xmlString.append("</fileInfo>");
		 //Result end
		 xmlString.append("</ResultInfo>");
		 
	     return xmlString.toString();
      }
 
	 public static String createXmlForWebService(String randomUUIDString) throws Exception {
		 return createXmlForWebService(randomUUIDString,null,null);
	 }
	 
	 public static String createXmlForWebService(String randomUUIDString,String Status) throws Exception {
		 
		 return createXmlForWebService(randomUUIDString,Status,null);
	 }
	 
				
}
