package eu.heliovo.queryservice.common.util;

import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import eu.heliovo.queryservice.common.util.ConfigurationProfiler;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.Tables;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOSerializer;
import uk.ac.starlink.votable.VOTableWriter;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

public class VOTableMaker {
  
    protected final  Logger logger = Logger.getLogger(this.getClass());
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public VOTableMaker(){
    	
    }
    
    public static void writeResultSet( ResultSet rset, BufferedWriter out ) throws Exception {
        StarTable table = new SequentialResultSetStarTable( rset );
        new VOTableWriter().writeInlineStarTable(table, out );
    } 
    
    //Writing all the details into VOtable.
    public static void writeTables( CommonCriteriaTO comCriteriaTO ) throws Exception {
    	BufferedWriter out =null;
    	String status=comCriteriaTO.getStatus();
    	String longRunning= comCriteriaTO.getLongRunningQueryStatus();
    	
    	if(longRunning!=null  && longRunning.equals("LongRunning")){
    		out=(BufferedWriter) comCriteriaTO.getPrintWriter();
    	}else{
    		out = new BufferedWriter( comCriteriaTO.getPrintWriter() );
    	}
    	StarTable[] tables=comCriteriaTO.getTables();
    	
    	try{    		
	    	//Adding response header start for WebService VOTABLE.
			if(status!=null && !status.equals("")  &&  (longRunning==null || longRunning.equals(""))){
				 out.write("<helio:queryResponse xmlns:helio=\"http://helio-vo.eu/xml/QueryService/v0.1\">");
			}else if(longRunning!=null && longRunning.equals("LongRunning")){
				out.write("<helio:resultResponse xmlns:helio=\"http://helio-vo.eu/xml/LongQueryService/v0.1\">");
			}
	        out.write( "<VOTABLE version='1.1' xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">\n" );
	        if(tables!=null){
		        for ( int i = 0; i < tables.length; i++ ){
		        	String tableName=tables[ i ].getName();
		        	//
		        	if(tableName!=null && !tableName.trim().equals(""))
		        		tableName=tableName.substring(tableName.lastIndexOf("_")+1, tableName.length());
		        	out.write( "<RESOURCE>\n" );
		 	        out.write( "<DESCRIPTION>"+ConfigurationProfiler.getInstance().getProperty("sql.votable.head.desc")+"</DESCRIPTION>\n" );
		 	        out.write( "<INFO name=\"QUERY_STATUS\" value=\""+comCriteriaTO.getQueryStatus()+"\"/>");
		 	        out.write( "<INFO name=\"EXECUTED_AT\" value=\""+now()+"\"/>");
		 	        out.write( "<INFO name=\"MAX_RECORD_ALLOWED\" value=\""+ConfigurationProfiler.getInstance().getProperty("sql.query.maxrecord.constraint."+tableName)+"\"/>");
		 	        out.write("<INFO name=\"QUERY_STRING\" >"+"<![CDATA["+comCriteriaTO.getQueryArray()[i]+"]]>"+"</INFO>");
		            VOSerializer.makeSerializer( DataFormat.TABLEDATA, tables[ i ] ).writeInlineTableElement( out );
		            out.write( "</RESOURCE>\n" );
		        }
	        }else{
	        	//Error resource
	        	out.write( "<RESOURCE>\n" );
	 	        out.write( "<DESCRIPTION>"+ConfigurationProfiler.getInstance().getProperty("sql.votable.head.desc")+"</DESCRIPTION>\n" );
	 	        out.write( "<INFO name=\"QUERY_STATUS\" value=\""+comCriteriaTO.getQueryStatus()+"\"/>");
	 	        out.write( "<INFO name=\"EXECUTED_AT\" value=\""+now()+"\"/>");
	 	        if(comCriteriaTO.getQueryStatus().equals("ERROR")){
	 	        	 out.write( "<INFO name=\"QUERY_ERROR\" value=\""+comCriteriaTO.getQueryDescription()+"\"/>");
	 	        }
	 	       	out.write("<INFO name=\"QUERY_STRING\" >"+"<![CDATA["+comCriteriaTO.getQuery()+"]]>"+"</INFO>");
	 	        out.write( "</RESOURCE>\n" );
	        }
	        out.write( "</VOTABLE>\n" );
	        //Adding response header start for WebService VOTABLE.
	        if(status!=null && !status.equals("") &&  (longRunning==null || longRunning.equals(""))){
				 out.write("</helio:queryResponse>");
			}else if(longRunning!=null && longRunning.equals("LongRunning")){
				 out.write("</helio:resultResponse>");
			}
    	}catch (Exception e) {
    		System.out.println(" Exception occured writeTables():While creating VOTable...!!! "+e.getMessage());
    		throw new Exception("Couldn't create VO TABLE!!!");
		}
        out.flush();
        out.close();
    }
        
    //Setting column property.This method not in use presently
    public static void setColInfoProperty(StarTable[] tables,String[] listName) throws Exception{
    	try{  
    	 	// Table name list.
    		for(int count=0;count<listName.length;count++){
    		// Start table array.
	    	for ( int i = 0; i < tables.length; i++ ) {
	    		//Column Description
	    		String[] columnDesc=ConfigurationProfiler.getInstance().getProperty("sql.columndesc."+listName[count]).split("::");
	    		//Column UCD's
	    		String[] columnUcd=ConfigurationProfiler.getInstance().getProperty("sql.columnucd."+listName[count]).split("::");
	    		//Column U Types.
	    		String[] columnUTypes=ConfigurationProfiler.getInstance().getProperty("sql.columnutypes."+listName[count]).split("::");
	    		
	    		for(int j=0;j<tables[ i ].getColumnCount();j++){
	    			//Setting UCD's for column.
	    			if(columnUcd.length>0 && columnUcd.length==tables[ i ].getColumnCount()){
	    				tables[ i ].getColumnInfo( j ).setUCD(columnUcd[j]);
	    			}
	    			//Setting Description for column.
	    			if(columnDesc.length>0 && columnDesc.length==tables[ i ].getColumnCount()){
	    				tables[ i ].getColumnInfo( j ).setDescription(columnDesc[j]);
	    			}
	    			//Setting Utypes for column
	    			if(columnUTypes.length>0 && columnUTypes.length==tables[ i ].getColumnCount()){
	    				Tables.setUtype( tables[ i ].getColumnInfo( j ), columnUTypes[j] );
	    			}
	    		}
	    	}//end of for
    	}// end of for
    	
    }catch(Exception e){
    	System.out.println(" Exception occured setColInfoProperty() "+e.getMessage());
    	throw new Exception("Couldn't set ucd's||Desc||UTypes. Please check configuration property file.");
    }
    	
    }	
    
    
    //Setting column property.( New method in use presently)
    public static void setColInfoProperty(StarTable tables,String listName) throws Exception{
    	try{  
    		   System.out.println(":  Setting UCD and UTYPES :");
    			//Column Description
	    		String[] columnDesc=getAllColumnDesc(listName);
	    		//Column UCD's
	    		String[] columnUcd=getAllColumnUCD(listName);
	    		//Column U Types.
	    		String[] columnUTypes=getAllColumnUTypes(listName);
	    		//
	    		String[] coulumnNames=getAllColumnNames(listName);
	    		
	    		for(int j=0;j<tables.getColumnCount();j++){
	    			//Setting UCD's for column.
	    			//System.out.println(" UCD Length "+columnUcd.length+" Table Length"+tables.getColumnCount());
	    			if(columnUcd.length>0 && columnUcd.length==tables.getColumnCount()){
	    				tables.getColumnInfo( j ).setUCD(columnUcd[j]);
	    			}
	    			//Setting Description for column.
	    			if(columnDesc.length>0 && columnDesc.length==tables.getColumnCount()){
	    				tables.getColumnInfo( j ).setDescription(columnDesc[j]);
	    			}
	    			//Setting Utypes for column
	    			if(columnUTypes.length>0 && columnUTypes.length==tables.getColumnCount()){
	    				Tables.setUtype( tables.getColumnInfo( j ), columnUTypes[j] );
	    			}
	    			//Handling NAR coulmn value( setting value as 1 if NAR is null)
	    			if(coulumnNames.length>0 && coulumnNames.length==tables.getColumnCount() && coulumnNames[j]!=null && coulumnNames[j].toLowerCase().equals("nar")){
	    				tables.getColumnInfo( j ).setAuxDatum( new DescribedValue( Tables.NULL_VALUE_INFO,new Integer( 1 )));

	    			}
	    		}
	    		System.out.println(":  End setting UCD and UTYPES :");
    }catch(Exception e){
    	System.out.println(" Exception occured setColInfoProperty() "+e.getMessage());
    	throw new Exception("Couldn't set Ucd's||Desc||UTypes. Please check configuration property file.");
    }
    	
    }	
    /**
     * 
     * @param listName
     * @return
     */
    private static String[] getAllColumnNames(String listName){
    	String[] concatArray={};
    	String[] listNameArray=listName.split(",");
    	for(int count=0;count<listNameArray.length;count++){
    		String[] columnName=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+listNameArray[count]).split("::");
    		concatArray=addArrays(concatArray,columnName);
    	}
    	return concatArray;
    }
    
    /**
     * 
     * @param listName
     * @return
     */
    private static String[] getAllColumnDesc(String listName){
    	String[] concatArray={};
    	String[] listNameArray=listName.split(",");
    	for(int count=0;count<listNameArray.length;count++){
    		String[] columnDesc=ConfigurationProfiler.getInstance().getProperty("sql.columndesc."+listNameArray[count]).split("::");
    		concatArray=addArrays(concatArray,columnDesc);
    	}
    	return concatArray;
    }
    /**
     * 
     * @param listName
     * @return
     */
    private static String[] getAllColumnUCD(String listName){
    	String[] concatArray={};
    	String[] listNameArray=listName.split(",");
    	for(int count=0;count<listNameArray.length;count++){
    		String[] columnUcd=ConfigurationProfiler.getInstance().getProperty("sql.columnucd."+listNameArray[count]).split("::");
    		concatArray=addArrays(concatArray,columnUcd);
    	}
    	return concatArray;
    }
    /**
     * 
     * @param listName
     * @return
     */
    private static String[] getAllColumnUTypes(String listName){
    	String[] concatArray={};
    	String[] listNameArray=listName.split(",");
    	for(int count=0;count<listNameArray.length;count++){
    		String[] columnUcd=ConfigurationProfiler.getInstance().getProperty("sql.columnutypes."+listNameArray[count]).split("::");
    		concatArray=addArrays(concatArray,columnUcd);
    	}
    	return concatArray;
    }
    
   private static String[] addArrays(String[] first, String[] second) {
	    List<String> both = new ArrayList<String>(first.length + second.length);
	    Collections.addAll(both, first);
	    Collections.addAll(both, second);
	    return both.toArray(new String[] {});
	}
    
    private static String now() {
    	
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

      }

   
}
