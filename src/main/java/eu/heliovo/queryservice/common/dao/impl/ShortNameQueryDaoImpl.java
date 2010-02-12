/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.ValueInfo;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;

import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.exception.ShortNameQueryException;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;
import eu.heliovo.queryservice.common.transfer.CommonTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.ConfigurationProfiler;
import eu.heliovo.queryservice.common.util.ConnectionManager;
import eu.heliovo.queryservice.common.util.StandardTypeTable;
import eu.heliovo.queryservice.common.util.VOTableMaker;


public class ShortNameQueryDaoImpl implements ShortNameQueryDao {
		
	public ShortNameQueryDaoImpl() { 
						
	}
	
	protected final  Logger logger = Logger.getLogger(this.getClass());
	
	/*
	 * (non-Javadoc)
	 * @see eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao#generateVOTableDetails(eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO)
	 */
	public void generateVOTableDetails(CommonCriteriaTO comCriteriaTO) throws DetailsNotFoundException,Exception {
		Connection con = null;
		Statement st = null;
		ResultSetMetaData rms =null;
		ResultSet rs=null;
		String[] listName=comCriteriaTO.getListName().split(",");
		StarTable[] tables=new StarTable[listName.length];
		try{
		//For loop start
		for(int intCnt=0;intCnt<listName.length;intCnt++){
			String sRepSql = CommonUtils.replaceParams(generateQuery(listName[intCnt],comCriteriaTO), comCriteriaTO.getParamData());
			logger.info(" : Query String After Replacing Value :"+sRepSql);	
			comCriteriaTO.setUpdatedQuery(sRepSql);
			//Setting Table Name.
			comCriteriaTO.setTableName(listName[intCnt]);
			//Setting query with values.
			comCriteriaTO.setQuery(sRepSql);
			//Connecting to database.						
			con = ConnectionManager.getConnection();
			st = con.createStatement();
			rs= st.executeQuery(sRepSql);
			comCriteriaTO.setQueryStatus("OK");
			comCriteriaTO.setQueryDescription(sRepSql);
			
			tables[intCnt] = new StandardTypeTable( new SequentialResultSetStarTable( rs ) );
			tables[intCnt].setName(listName[intCnt]);			
		}
		comCriteriaTO.setTables(tables);
		//Editing column property.
		VOTableMaker.setColInfoProperty(tables, listName);
		//Writing all details into table.
		VOTableMaker.writeTables(comCriteriaTO);
				
		} catch (Exception e) {		
			//Writing all details into table.
			comCriteriaTO.setQueryStatus("ERROR");
			VOTableMaker.writeTables(comCriteriaTO);
			comCriteriaTO.setQueryDescription(e.getMessage());
			throw new DetailsNotFoundException("EXCEPTION ", e);
		}
		finally
		{
			try {
				if(rms!=null)
				{
					rms = null;
				}
				if(rs!=null)
				{
					rs.close();
					rs=null;
				}
				if(st!=null)
				{
					st.close();
					st=null;
				}
				if(con!=null)
				{
					con.close();
					con=null;
				}
				
			} catch (Exception e) {
				
			}
	}		
	}
	
	
	/*
	 * Get the list of Tables in a Database.
	 */
	@SuppressWarnings("unused")
	public HashMap<String,String> getDatabaseTableNames(Connection con) throws Exception 
	{
		HashMap<String,String> hmbDatabaseTableList=new HashMap<String,String>();
		DatabaseMetaData md=null;
		ResultSet rs=null;
		try{
			 md = con.getMetaData();
		     rs = md.getTables(null, null, "%", null);
		    while (rs.next()) {
		      // System.out.println(rs.getString(3));
		      hmbDatabaseTableList.put(rs.getString(3), rs.getString(3));
		    }
		} catch (Exception e) {			
			throw new Exception("EXCEPTION ", e);
		}
		finally
		{
			try {
				if(con!=null)
				{
					con.close();
					con=null;
				}
				if(md!=null)
				{
					md=null;
				}
				if(rs!=null)
				{
					rs.close();
					rs=null;
				}
			} catch (Exception e) {
				
			}
	}	
	    return hmbDatabaseTableList;
	}
	
	/*
	 * Get the list of Columns in a Table.
	 */
	public CommonTO[] getTableColumnNames(Connection con,String tableName) throws Exception
	{
		ResultSet rsColumns = null;
		CommonTO[] columnTO = null;
		DatabaseMetaData meta =null;
		try{
			meta = con.getMetaData();
		    rsColumns = meta.getColumns(null, null, tableName, null);
		    rsColumns.last();
		    int intCount=rsColumns.getRow();
		    rsColumns.beforeFirst();
		    if(rsColumns!=null){
		    	columnTO = new CommonTO[intCount];
	     		int i=0;
			    while (rsColumns.next()) {
			      columnTO[i]=new CommonTO();
			      columnTO[i].setColumnName(rsColumns.getString("COLUMN_NAME"));
			      columnTO[i].setColumnType(rsColumns.getString("TYPE_NAME"));
			      i++;
			    }
		    }
		} catch (Exception e) {			
			throw new Exception("EXCEPTION ", e);
		}
		finally
		{
			try {
				if(rsColumns!=null)
				{
					rsColumns.close();
					rsColumns=null;
				}
				if(con!=null)
				{
					con.close();
					con=null;
				}
				if(meta!=null)
				{
					meta=null;
				}
			} catch (Exception e) {
				
			}
	}	
		return columnTO;
	}

	
	/*
	 * Creating column names.
	 */
	private String getColumnNamesFromProperty(String tableName) throws Exception
	{
		String[] columnNames=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+tableName).split("::");
		String colNamesForTable="";
				
		for(int i=0;i<columnNames.length;i++)
		{
			colNamesForTable=colNamesForTable+columnNames[i]+",";
		}
		
		if(colNamesForTable.endsWith(",")){
			colNamesForTable=colNamesForTable.substring(0, colNamesForTable.length()-1);
		}
		logger.info(" : Table Name : "+tableName +" List of columns "+colNamesForTable);
		
		return colNamesForTable;
	}
	
	
	@SuppressWarnings("unused")
	private String  generateQuery(String listName,CommonCriteriaTO comCriteriaTO) throws Exception{
			 String queryConstraint="";
			 String query="";
		
			 HashMap<String,String> params  = new HashMap<String,String>();
			 
			 params.put("kwstartdate."+listName, comCriteriaTO.getStartDateTime());
			 params.put("kwenddate."+listName, comCriteriaTO.getEndDateTime());
			 params.put("kwinstrument."+listName, comCriteriaTO.getInstruments());
					
			 //Setting parameter value
			 comCriteriaTO.setParamData(params);
			 query="SELECT "+getColumnNamesFromProperty(listName)+" FROM "+listName;
			 logger.info(" : Query String with 'Select' and 'From' : "+query);
			 
			 //Appending Time clause.
			 String queryTimeContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.time.constraint."+listName);
			 if(queryTimeContraint!=null && !queryTimeContraint.equals("") ){
				 queryConstraint=queryConstraint+" "+queryTimeContraint;
			 }
			 
			 logger.info(" : Appending Time Constraint If Avialable : "+queryConstraint);
			 
			 //Appending Instrument clause.
			 String queryInstContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.instr.constraint."+listName);
			 if(queryInstContraint!=null && !queryInstContraint.equals("")){
				 if(queryConstraint!="")
					 queryConstraint=queryConstraint+" AND "+queryInstContraint; 
				 else
					 queryConstraint=queryConstraint+" "+queryInstContraint; 
				
			 }
			
			 logger.info(" : Appending Instrument Constraint If Avialable : "+queryConstraint);
			 
			 //Appending Order By clause.
			 String queryOrderByContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.orderby.constraint."+listName);
			
			 //Appending 'Select Part' ; 'Where Constraints' .
			 if(queryConstraint!=null && queryConstraint!=""){
				 query=query+" WHERE "+queryConstraint;
			 }
			 
			 //Appending ; 'Order By Constraints' .
			 query=query+" "+queryOrderByContraint;
			 
			 logger.info(" : Appending OderBy Constraint If Avialable : "+query);
			 
			 //Appending limit clause.
			 String querylimitContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.limit.constraint."+listName);
			 
			 if(querylimitContraint==null || querylimitContraint.trim().equals("")){
				 //Setting start row.
				 if(comCriteriaTO.getNoOfRows()!=null && !comCriteriaTO.getNoOfRows().equals("")){
					 querylimitContraint=" LIMIT "+comCriteriaTO.getNoOfRows();
				 }
				 //Setting No Of Rows
				 if(comCriteriaTO.getStartRow()!=null && !comCriteriaTO.getStartRow().equals("") && querylimitContraint!=null && !querylimitContraint.equals("")){
					 querylimitContraint=querylimitContraint+" OFFSET "+comCriteriaTO.getStartRow();
				 }
			 }
			 //Appending ; 'Limit Constraints' .
			 query=query+" "+querylimitContraint;
			 
			 logger.info(" : Full query for execution : "+query);
			 
			 
		 return query;
	}
}