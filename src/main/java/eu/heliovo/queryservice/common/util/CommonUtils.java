/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
	
	static SimpleDateFormat df=new SimpleDateFormat(ConstantKeywords.HrsSQLFORMAT);
	
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

	public static String changeDateFormat(String formatString,Date date){ 
			String str=""; 
			try{ 
				DateFormat sd1 = new SimpleDateFormat(formatString); 
				str=sd1.format(date); 
			}catch(Exception e){ 
				//Exception
			} 
			return str; 
	}

	/**
	 * 
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static Calendar dateString2Calendar(String s) throws ParseException {
	    Calendar cal=Calendar.getInstance();
	    Date d1=df.parse(s);
	    cal.setTime(d1);
	    return cal;
	  }
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String date2String(Date date)
	{
		// Get the date today using Calendar object.
		Date today = Calendar.getInstance().getTime();       
		// Using DateFormat format method we can create a string
		// representation of a date with the defined format.
		String reportDate = df.format(today);
		
		return reportDate;
	}
	
	/**
	 * 
	 * @param sRegion
	 * @return
	 */
	public static Map<String,String> parseRegionParameter(String sRegion)
	{
		Map<String,String> map=new LinkedHashMap<String, String>();
		String[] splitRegion=null;
		if(sRegion!=null && !sRegion.trim().equals(""))
		{
			splitRegion=sRegion.split("::");
			if(splitRegion.length>0)
				map.put("region", splitRegion[0]);
			if(splitRegion.length>2)
				map.put("regionvalues", arrayToString(getRegionValues(splitRegion),"::"));
		}
		
		return map;
		
	}
	
	
	/**
	 * 
	 * @param sRegionValues
	 * @return
	 */
	public static String[] getRegionValues(String[] sRegionValues){
		List<String> list = Arrays.asList(sRegionValues);
		list.remove(0);list.remove(1);
		sRegionValues=(String[]) list.toArray();
		return sRegionValues;
	}
	
	
	/**
	 * 
	 * @param sDate
	 * @return
	 * @throws ParseException
	 */
	public static int getNoOfMonths(String sDate) throws ParseException
	{
		int diff=DifferenceInMonths(dateString2Calendar(sDate),Calendar.getInstance());
		System.out.println(" :  No of months between dates  :  "+diff);
		return diff;
	}
	
	/**
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public static void deleteFile(String fileName) throws Exception{
		System.out.println("Deleting saved VOTABLE "+fileName);
		 // A File object to represent the filename
		 File f = new File(fileName);
		 // Make sure the file or directory exists and isn't write protected
		 if (!f.exists())
		      throw new IllegalArgumentException(
		          "Delete: no such file or directory: " + fileName);
    	 // If it is a directory, make sure it is empty
		 if (f.isDirectory()) {
		      String[] files = f.list();
		      if (files.length > 0)
		        throw new IllegalArgumentException("Delete: directory not empty: " + fileName);
		 }
		 // Attempt to delete it
		 boolean success = f.delete();
		 if (!success)
		      throw new IllegalArgumentException("Delete: deletion failed");
	}
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int DifferenceInMonths(Calendar date1, Calendar date2)
    {
	return (int)Math.round(DifferenceInYears(date1, date2) * 12);
    }
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
    public static double DifferenceInYears(Calendar date1, Calendar date2)
    {
	double days = DifferenceInDays(date1, date2);
	return  days / 365.2425;
    }
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static double DifferenceInDays(Calendar date1, Calendar date2)
    {
	return DifferenceInHours(date1, date2) / 24.0;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static double DifferenceInHours(Calendar date1, Calendar date2)
    {
	return DifferenceInMinutes(date1, date2) / 60.0;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static double DifferenceInMinutes(Calendar date1, Calendar date2)
    {
	return DifferenceInSeconds(date1, date2) / 60.0;
    }
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static double DifferenceInSeconds(Calendar date1, Calendar date2)
    {
	return DifferenceInMilliseconds(date1, date2) / 1000.0;
    }
    /**
     * 
     * @param date1
     * @param date2
     * @return
     */
    private static double DifferenceInMilliseconds(Calendar date1, Calendar date2)
    {
	return Math.abs(GetTimeInMilliseconds(date1) - GetTimeInMilliseconds(date2));
    }
    /**
     * 
     * @param cal
     * @return
     */
    private static long GetTimeInMilliseconds(Calendar cal)
    {
	return cal.getTimeInMillis() + cal.getTimeZone().getOffset(cal.getTimeInMillis());
    }

	
}
