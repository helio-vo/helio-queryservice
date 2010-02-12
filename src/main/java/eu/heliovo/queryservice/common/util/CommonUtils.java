/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


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
	
}
