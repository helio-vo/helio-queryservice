/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import eu.heliovo.queryservice.common.transfer.FileResultTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;


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
	 public static String createXmlForWebService(FileResultTO fileTO) throws Exception {
		 StringBuilder xmlString = new StringBuilder();
		 xmlString.append("<ResultInfo>");
		 xmlString.append("<ID>");
		 xmlString.append(fileTO.getRandomUUIDString());
		 xmlString.append("</ID>");
		 String sDes=null;
		 String statusArray[]=null;
		 String Status=fileTO.getStatus();
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
		 String sUrl=fileTO.getsUrl();
		 // Url for the file location.
		 if(sUrl!=null && !sUrl.trim().equals("")){
			 xmlString.append("<resultURI>");
			 xmlString.append(sUrl);
			 xmlString.append("</resultURI>");
		 }
		 String fileInfo=fileTO.getFileInfo();
		 //Result info
		 if(fileInfo!=null && !fileInfo.trim().equals("")){
			 xmlString.append("<fileInfo>");
			 xmlString.append(fileInfo);
			 xmlString.append("</fileInfo>");
		 }
		 //Result end
		 xmlString.append("</ResultInfo>");
		 
	     return xmlString.toString();
      }
 
	 /**
	   * Reveals the base URI of the web application.
	   *
	   * @return The URI.
	   */
	  public static String getUrl(HttpServletRequest req) { 
		  String scheme = req.getScheme(); // http 
		  String serverName = req.getServerName(); // hostname.com 
		  int serverPort = req.getServerPort(); // 80 
		  String contextPath = req.getContextPath(); // mywebapp 
		  String url = scheme+"://"+serverName+":"+serverPort+contextPath+"/HelioQueryService?"; 
		  return url; 
	  }	 
	 
	 /**
	  * 
	  * @param commonTO
	  * @return
	  */
	 public static String getFullRequestUrl(CommonCriteriaTO commonTO){
		 String sActualUrl=commonTO.getContextUrl();
		 if(commonTO.getAllStartDate()!=null && !commonTO.getAllStartDate().trim().equals(""))
			 sActualUrl=sActualUrl+"STARTTIME="+commonTO.getAllStartDate();
		 if(commonTO.getAllEndDate()!=null && !commonTO.getAllEndDate().trim().equals(""))
			 sActualUrl=sActualUrl+"&ENDTIME="+commonTO.getAllEndDate();
		 if(commonTO.getInstruments()!=null && !commonTO.getInstruments().trim().equals(""))
			 sActualUrl=sActualUrl+"&INSTRUMENT="+commonTO.getInstruments();
		 if(commonTO.getWhereClause()!=null && !commonTO.getWhereClause().trim().equals(""))
			 sActualUrl=sActualUrl+"&WHERE="+commonTO.getWhereClause();
		 //Appening From clause, table
		 sActualUrl=sActualUrl+"&FROM="+commonTO.getListName();
		 return sActualUrl;
	 }
	 
	 /**
	  * 
	  * @param commonTO
	  * @return
	  */
	 public static String getRequestUrl(CommonCriteriaTO commonTO){
		 String sActualUrl=commonTO.getContextUrl();
		 String where=getRequestWhereClause(commonTO);
		 if(commonTO.getStartDateTime()!=null && !commonTO.getStartDateTime().trim().equals(""))
			 sActualUrl=sActualUrl+"STARTTIME="+commonTO.getStartDateTime();
		 if(commonTO.getEndDateTime()!=null && !commonTO.getEndDateTime().trim().equals(""))
			 sActualUrl=sActualUrl+"&ENDTIME="+commonTO.getEndDateTime();
		 if(commonTO.getInstruments()!=null && !commonTO.getInstruments().trim().equals(""))
			 sActualUrl=sActualUrl+"&INSTRUMENT="+commonTO.getInstruments();
		 //WHERE Clause
		 if(where!=null && !where.trim().equals(""))
			 sActualUrl=sActualUrl+"&WHERE="+where;
		 else if(commonTO.getWhereClause()!=null && !commonTO.getWhereClause().trim().equals("") && commonTO.getWhereClause().trim().contains(commonTO.getTableName().trim()))
			 sActualUrl=sActualUrl+"&WHERE="+commonTO.getWhereClause();
		 
		 //Appening From clause, table
		 sActualUrl=sActualUrl+"&FROM="+commonTO.getTableName();
		 return sActualUrl;
	 }
	
	 /**
	  * 
	  * @param commonTO
	  * @return
	  */
	 public static String getRequestWhereClause(CommonCriteriaTO commonTO){
		 String sWhere=commonTO.getWhereClause();
		 String tableName=commonTO.getTableName();
		 String[] whereClauseArray=null;
		 String actualValue="";
		 if(sWhere!=null && !sWhere.trim().equals(""))
			 whereClauseArray=sWhere.split(";");
		 //
		  if(whereClauseArray!=null){
			for(int count=0;count<whereClauseArray.length;count++){
				String 	value=whereClauseArray[count];
				if(value.trim().contains(tableName)){
					actualValue=actualValue+value+";";
				}
			}
		  }
			if(actualValue!=null && !actualValue.trim().equals(""))
				actualValue=actualValue.substring(0, actualValue.length()-1);
			
		 return actualValue;
	 }
	 
	  /**
	   * 
	   * @param a
	   * @param separator
	   * @return
	   */
	  public static String arrayToString(String[] a, String separator) {
		    String result = "";
		    if (a.length > 0) {
		        result = a[0];    // start with the first element
		        for (int i=1; i<a.length; i++) {
		            result = result + separator + a[i];
		        }
		    }
		    return result;
		}
	
	  
	  @SuppressWarnings("unchecked")
	  public static Map<String,String> getLogLocations() {

	    Collection<Logger> allLoggers = new ArrayList<Logger>();
	    Logger rootLogger = Logger.getRootLogger();
	    allLoggers.add(rootLogger);
	    for (Enumeration<Logger> loggers =rootLogger.getLoggerRepository().getCurrentLoggers() ;loggers.hasMoreElements() ; ) {
	      allLoggers.add(loggers.nextElement());
	    }
	    
	    Set<FileAppender> fileAppenders =new LinkedHashSet<FileAppender>();

	    for (Logger logger : allLoggers) {
	      for (Enumeration<Appender> appenders =
	              logger.getAllAppenders() ;
	              appenders.hasMoreElements() ; ) {

	        Appender appender = appenders.nextElement();
	        if (appender instanceof FileAppender) {

	          fileAppenders.add((FileAppender) appender);
	        }
	      }
	    }

	    Map<String, String> locations =new LinkedHashMap<String,String>();

	    for (FileAppender appender : fileAppenders) {
	      locations.put(appender.getName(), appender.getFile());
	    }

	    return locations;
	  }
	  
	  public static StringBuffer readInputStreamAsString(InputStream in) throws IOException {
		StringBuffer sb=new StringBuffer();
	    BufferedInputStream bis = new BufferedInputStream(in);
	    ByteArrayOutputStream buf = new ByteArrayOutputStream();
	    int result = bis.read();
	    while(result != -1) {
	      byte b = (byte)result;
	      buf.write(b);
	      result = bis.read();
	    }        
	    return sb.append(buf.toString());
	}

	  
}
