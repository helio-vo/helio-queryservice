package eu.heliovo.queryservice.common.util;

import java.io.BufferedWriter;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
import eu.heliovo.queryservice.common.util.ConfigurationProfiler;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.Tables;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOSerializer;
import uk.ac.starlink.votable.VOTableWriter;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

public class VOTableMaker {
  
    protected final  Logger logger = Logger.getLogger(this.getClass());
    
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
			if(status!=null && !status.equals("") &&  longRunning!=null && longRunning.equals("")){
				 out.write("<helio:queryResponse xmlns:helio=\"http://helio-vo.eu/xml/QueryService/v0.1\">");
			}else if(longRunning!=null && longRunning.equals("LongRunning")){
				out.write("<helio:resultResponse xmlns:helio=\"http://helio-vo.eu/xml/LongQueryService/v0.1\">");
			}
	        out.write( "<VOTABLE version='1.1' xmlns=\"http://www.ivoa.net/xml/VOTable/v1.1\">\n" );
	        out.write( "<RESOURCE>\n" );
	        out.write( "<DESCRIPTION>"+ConfigurationProfiler.getInstance().getProperty("sql.votable.head.desc")+"</DESCRIPTION>\n" );
	        out.write( "<INFO name=\"QUERY_STATUS\" value=\""+comCriteriaTO.getQueryStatus()+"\"/>");
	        if(comCriteriaTO.getQueryStatus().equals("ERROR")){
	        	 out.write( "<INFO name=\"QUERY_STATUS\" value=\""+comCriteriaTO.getQueryDescription()+"\"/>");
	        }
	       	out.write("<INFO ID=\""+comCriteriaTO.getQueryStatus()+"\" name=\""+comCriteriaTO.getQueryStatus()+"\" >"+"<![CDATA["+comCriteriaTO.getQuery()+"]]>"+"</INFO>");
	        if(tables!=null){
		        for ( int i = 0; i < tables.length; i++ ) {
		            VOSerializer.makeSerializer( DataFormat.TABLEDATA, tables[ i ] )
		                        .writeInlineTableElement( out );
		        }
	        }
	        out.write( "</RESOURCE>\n" );
	        out.write( "</VOTABLE>\n" );
	      //Adding response header start for WebService VOTABLE.
	        if(status!=null && !status.equals("") &&  longRunning!=null && longRunning.equals("")){
				 out.write("</helio:queryResponse>");
			}else if(longRunning!=null && longRunning.equals("LongRunning")){
				 out.write("</helio:resultResponse>");
			}
    	}catch (Exception e) {
    		System.out.println(" Exception occured writeTables() "+e.getMessage());
    		throw new Exception("Couldn't create VO TABLE.");
		}
        out.flush();
        out.close();
    }
        
    //Setting column property.
    public static void setColInfoProperty(StarTable[] tables,String[] listName) throws Exception{
    	try{   	
    	for ( int i = 0; i < tables.length; i++ ) {
    		//Column Description
    		String[] columnDesc=ConfigurationProfiler.getInstance().getProperty("sql.columndesc."+listName[i]).split("::");
    		//Column UCD's
    		String[] columnUcd=ConfigurationProfiler.getInstance().getProperty("sql.columnucd."+listName[i]).split("::");
    		//Column U Types.
    		String[] columnUTypes=ConfigurationProfiler.getInstance().getProperty("sql.columnutypes."+listName[i]).split("::");
    		
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
    	}
    	
    }catch(Exception e){
    	System.out.println(" Exception occured setColInfoProperty() "+e.getMessage());
    	throw new Exception("Couldn't set ucd's||Desc||UTypes. Please check configuration property file.");
    }
    	
    }	
   
}
