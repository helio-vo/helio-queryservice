package eu.heliovo.queryservice.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;

public class RegistryUtils {
		
	private RegistryUtils()
	{
		
	}
		
	public static RegistryUtils getInstance() {
		return RegistryUtilsHolder.instance;
	}

	private static class RegistryUtilsHolder {
		private static RegistryUtils instance = new RegistryUtils();
	}
	
	  
	public String[] getTableNames()  throws Exception{
		StringBuffer sb=new StringBuffer();
		Enumeration<?> eum=ConfigurationProfiler.getInstance().getAllKeys();
		while (eum.hasMoreElements()) {
		    String key = (String) eum.nextElement();
		   if(key.contains("sql.columnnames.")){
			   sb.append(key.substring(key.lastIndexOf(".")+1, key.length()));
			   sb.append(",");
		   }
		}
		sb.delete(sb.lastIndexOf(","), sb.length());
		
		return sb.toString().split(",");
	}

	/*
	 * getColumnNames(String tableName) : to get column names.
	 */
	public String[] getColumnNames(String tableName) throws Exception{
		String column[]=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+tableName).split("::");
		return column;
	}
	
	/*
	 * getUCDNames(String tableName): to get UCD names.
	 */
	public String[] getUCDNames(String tableName) throws Exception{
		String ucd[]=ConfigurationProfiler.getInstance().getProperty("sql.columnucd."+tableName).split("::");
		return ucd;
	}
	
	/*
	 *  getUTypes(String tableName): to get Utypes.
	 */
	public String[] getUTypes(String tableName) throws Exception{
		String utypes[]=ConfigurationProfiler.getInstance().getProperty("sql.columnutypes."+tableName).split("::");
		return utypes;
	}
	
	/*
	 *  getUTypes(String tableName): to get Utypes.
	 */
	public String[] getColumnDesc(String tableName) throws Exception{
		String columnDesc[]=ConfigurationProfiler.getInstance().getProperty("sql.columndesc."+tableName).split("::");
		return columnDesc;
	}
	
	/*
	 *  getUTypes(String tableName): to get Utypes.
	 */
	public String[] getColumnUnits(String tableName) throws Exception{
		String columnUnits[]=ConfigurationProfiler.getInstance().getProperty("sql.columnunits."+tableName).split("::");
		return columnUnits;
	}
	
	/** Generates the table descriptions for the CatalogService registration
	 * for the specified catalog.
	 */
	 public String getTableDescriptions()  throws Exception {
		 return getTableDescriptions(null);
	 }

	
	/** Generates the table descriptions for the CatalogService registration
	 * for the specified catalog.
	 */
	 public String getTableDescriptions(String tableName)  throws Exception {
		  //System.out.println("TABLENAME IN TABLEDESCRPTIONS: " + tableName);
	      StringBuffer tables = new StringBuffer();
	      String[] catalogNames=getTableNames();
	      ShortNameQueryDao shortNameDao= CommonDaoFactory.getInstance().getShortNameQueryDao();
	      //System.out.println("catalognames length: " + catalogNames.length);
	      for (int t = 0; t < catalogNames.length; t++) {
	    	  if(tableName == null || catalogNames[t].equals(tableName)) {
	         tables.append(
	           " <schema><name>" + catalogNames[t]+"</name>\n" + 
	           "  <table>\n" +
	           "    <name>"+catalogNames[t]+"</name>\n" 
	          );
	          HashMap<String, String> dataTypeLookup=shortNameDao.getTableColumnNames(catalogNames[t]);
	          String columns[]=getColumnNames(catalogNames[t]);
	    	  String columnDesc[]=getColumnDesc(catalogNames[t]);
	    	  String ucd[]=getUCDNames(catalogNames[t]);
	    	  String utypes[]=getUTypes(catalogNames[t]);	  
	    	  String units[]=getColumnUnits(catalogNames[t]);	
	    	  //System.out.println("catalogNames[t]: " + catalogNames[t]);
	    	  String dataType = null;
	         for (int c = 0; c < columns.length; c++) {
	        	
	            tables.append(
	               "    <column>\n"+
	               "    <name>"+columns[c]+"</name>\n");
	               if(columnDesc.length == (columns.length-1)) {
	            	   //tables.append( "    <description> </description>\n");
		           }else {
		        	   tables.append( "    <description>"+columnDesc[c]+"</description>\n");
		           }
	               if(units!=null && units.length>0 && units.length==columns.length){
			            if ((units[c] != null) && (units[c].trim().length()>0)) {
			               tables.append(
			                  "      <units>"+units[c].trim()+"</units>\n"
			               );
			            }
		            }
	               if(ucd!=null && ucd.length>0 && ucd.length==columns.length){
			            if ((ucd[c] != null) && (ucd[c].trim().length()>0)) {
			               tables.append(
			                  "      <ucd>"+ucd[c].trim()+"</ucd>\n"
			               );
			            }
		            }
	               if(utypes!=null && utypes.length>0 && utypes.length==columns.length){
			            if ((utypes[c] != null) && (utypes[c].trim().length()>0)) {
			            tables.append(
			                  "      <utype>"+utypes[c]+"</utype>\n"
			            );
			            }
		            }
	               dataType = dataTypeLookup.get(columns[c].toLowerCase());
	               if(dataType != null) {
		               if(dataType.indexOf("float") != -1) {
		            	   tables.append("    <dataType  arraysize=\"1\" xsi:type=\"vs:VOTableType\">float</dataType>\n");   
		               }else if(dataType.indexOf("int") != -1) {
		            	   tables.append("    <dataType  arraysize=\"1\" xsi:type=\"vs:VOTableType\">int</dataType>\n");   
		               }else if(dataType.indexOf("bool") != -1) {
		            	   tables.append("    <dataType  arraysize=\"1\" xsi:type=\"vs:VOTableType\">boolean</dataType>\n");   
		               }else if(dataType.indexOf("serial") != -1) {
		            	   tables.append("    <dataType  arraysize=\"1\" xsi:type=\"vs:VOTableType\">long</dataType>\n");   
		               }else if(dataType.indexOf("char") != -1 || dataType.indexOf("time") != -1 || dataType.indexOf("date") != -1) {
		            	   tables.append("    <dataType  arraysize=\"*\" xsi:type=\"vs:VOTableType\">char</dataType>\n");   
		               }else {
		            	   tables.append("    <dataType  arraysize=\"1\" xsi:type=\"vs:VOTableType\">" + dataType + "</dataType>\n");   
		               }
	               }
	          
	           
	            //
	            // Patch fix - Moved the xml fragment generation to VoTypes.
	            // KONA TOFIX look at the points below
	            // Todo - Need access to the column size for strings.
	            // Todo - Need to create a VoType format for dates.
	            //tables.append("    "+
	            //    VoTypes.getVoTypeXml(columns[c].getJavaType()) + "\n"
	            //);
	            tables.append("    </column>\n");
	         }
	         tables.append("  </table></schema>\n");
	    	  }//if
	      }//for
	      return tables.toString();
	   }
	

}


