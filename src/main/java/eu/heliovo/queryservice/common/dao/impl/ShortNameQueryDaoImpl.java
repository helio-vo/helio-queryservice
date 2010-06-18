/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.log4j.Logger;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;
import eu.heliovo.queryservice.common.transfer.CommonTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.ConfigurationProfiler;
import eu.heliovo.queryservice.common.util.ConnectionManager;
import eu.heliovo.queryservice.common.util.QueryWhereClauseParser;
import eu.heliovo.queryservice.common.util.StandardTypeTable;
import eu.heliovo.queryservice.common.util.VOTableMaker;

public class ShortNameQueryDaoImpl implements ShortNameQueryDao {
		
	public ShortNameQueryDaoImpl(){ 
						
	}
	
	protected final  Logger logger = Logger.getLogger(this.getClass());
	
	/*
	 * (non-Javadoc)
	 * @see com.org.helio.common.dao.interfaces.ShortNameQueryDao#generateVOTableDetails(com.org.helio.common.transfer.criteriaTO.CommonCriteriaTO)
	 */
	public void generateVOTableDetails(CommonCriteriaTO comCriteriaTO) throws DetailsNotFoundException,Exception {
		Connection con = null;
		Statement st = null;
		ResultSetMetaData rms =null;
		StarTable[] tables=null;
		ResultSet rs=null;
		//List data or table names
		String[] listName=comCriteriaTO.getListTableName();
		//start date vales
		String startDateTimeList[]=comCriteriaTO.getStartDateTimeList();
		//end date values
		String endDateTimeList[]=comCriteriaTO.getEndDateTimeList();
		
	  try{
		  
	  if(listName!=null){
		if(startDateTimeList!=null && endDateTimeList!=null){
		//Count of tables in respionse.
		int count=listName.length*startDateTimeList.length;
		tables=new StarTable[count];
		int tableCount=0;
		if(startDateTimeList.length==endDateTimeList.length){
		//For loop start
		for(int intCnt=0;intCnt<listName.length;intCnt++){
			//loop for start date.
		  for(int intTimeCnt=0;intTimeCnt<startDateTimeList.length;intTimeCnt++){
			 String startDate=startDateTimeList[intTimeCnt];
			 String endDate=endDateTimeList[intTimeCnt];
			 logger.info(" : Start Date ; End Date and List Name : "+startDate+"  : "+endDate+"  : "+listName[intCnt]);
			 //Setting start time 
			comCriteriaTO.setStartDateTime(startDate);
			//Setting end time
			comCriteriaTO.setEndDateTime(endDate);
			//Checking if start date or end date is null or no value.
			if((startDate!=null && !startDate.trim().equals("")) && (endDate!=null && !endDate.trim().equals(""))){
			//Comparing 2 date value.
			if(compareToDates(startDate,endDate)){
				String sRepSql = CommonUtils.replaceParams(generateQuery(listName[intCnt],comCriteriaTO), comCriteriaTO.getParamData());
				logger.info(" : Query String After Replacing Value :"+sRepSql);	
				//Setting Table Name.
				comCriteriaTO.setTableName(listName[intCnt]);
				//Setting query with values.
				comCriteriaTO.setQuery(sRepSql);
				//Connecting to database.						
				con = getConnectionObject();
				st = con.createStatement();
				rs= st.executeQuery(sRepSql);
				comCriteriaTO.setQueryStatus("OK");
				comCriteriaTO.setQuery(sRepSql);
				
				tables[tableCount] = new StandardTypeTable( new SequentialResultSetStarTable( rs ) );
				tables[tableCount].setName(comCriteriaTO.getContextPath()+"_"+listName[intCnt]);
				tableCount++;
			}else{
				comCriteriaTO.setQueryStatus("ERROR");
				comCriteriaTO.setQueryDescription("Start Date should always be less than End Date.");
				VOTableMaker.writeTables(comCriteriaTO);
			}
			}else{
				comCriteriaTO.setQueryStatus("ERROR");
				comCriteriaTO.setQueryDescription("Start date and End date cannot be null or no value");
				VOTableMaker.writeTables(comCriteriaTO);
			}
		  }
		}
		comCriteriaTO.setTables(tables);
		//Editing column property.
		VOTableMaker.setColInfoProperty(tables, listName);
		//Writing all details into table.
		VOTableMaker.writeTables(comCriteriaTO);
		logger.info(" : VOTable succesfully created :");	
		
		}else{
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription("Start date and End date should have same no of values.");
			VOTableMaker.writeTables(comCriteriaTO);
		}
		}else{
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription("Start date and End date cannot be null or no value");
			VOTableMaker.writeTables(comCriteriaTO);
		}
		
	  }else{
		  comCriteriaTO.setQueryStatus("ERROR");
		  comCriteriaTO.setQueryDescription("FROM clause value is missing in request xml.");
		  VOTableMaker.writeTables(comCriteriaTO);
	  }
	 }catch (Exception e){		
			//Writing all details into table.
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription(e.getMessage());
			
			VOTableMaker.writeTables(comCriteriaTO);
			logger.info(" Exception occured while generating VOTABLE: ",e);
			logger.fatal(" Exception occured while generating VOTABLE: ",e);
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
		String colNames=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+tableName);
		//Column names array
		String[] columnNames=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+tableName).split("::");
		String colNamesForTable="";
		
		if(colNames!=null && !colNames.trim().equals("")){		
			for(int i=0;i<columnNames.length;i++)
			{
				colNamesForTable=colNamesForTable+columnNames[i]+",";
			}
		}else{
			throw new Exception("Couldn't find coulumn names for table name "+tableName+". Please check configuration property file.");
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
			 String queryWhereClause="";
			 String query="";
			 int maxRecordsAllowed=0;
			 HashMap<String,String> params  = new HashMap<String,String>();
			 
			 params.put("kwstartdate", comCriteriaTO.getStartDateTime());
			 params.put("kwenddate", comCriteriaTO.getEndDateTime());
			 params.put("kwinstrument", comCriteriaTO.getInstruments());
			 params.put("kwdec", comCriteriaTO.getDelta());
			 params.put("kwra", comCriteriaTO.getAlpha());
			 params.put("kwsize", comCriteriaTO.getSize());
			 //Setting parameter value
			 comCriteriaTO.setParamData(params);
			 //Checking for Where Clause 
			 if(comCriteriaTO.getWhereClause()!=null && !comCriteriaTO.getWhereClause().equals("")){
				 //Method to get joined query.( Select items ).
				 query=getJoinSelectClause(comCriteriaTO);
			 }else{
				 //Normal query
				 query="SELECT "+getColumnNamesFromProperty(listName)+" FROM "+listName;
			 }
			 
			 logger.info(" : Query String with 'Select' and 'From' : "+query);
			 //Getting where clause.
			 if(comCriteriaTO.getWhereClause()!=null && !comCriteriaTO.getWhereClause().equals("")){
				 queryWhereClause=QueryWhereClauseParser.generateWhereClause(comCriteriaTO.getWhereClause());
				 
				 //Setting all declared string to null;
				 QueryWhereClauseParser.deAllocateStringToNull();
				 
			 }
			 
			
			 //Appending Time clause.
			 String queryTimeContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.time.constraint."+listName);
			 if(queryTimeContraint!=null && !queryTimeContraint.trim().equals("") ){
				 //Checking if it has where clause.
				 if(!queryWhereClause.equals("")){
					 queryConstraint=queryConstraint+" "+queryTimeContraint+" AND "+queryWhereClause;
				 }else{
					 queryConstraint=queryConstraint+" "+queryTimeContraint;
				 }
			 }else{
				 queryConstraint=queryConstraint+" "+queryWhereClause;
			 }
			 
			 logger.info(" : Appending Time Constraint If Avialable : "+queryConstraint);
			 
			 //Appending Instrument clause.
			 String queryInstContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.instr.constraint."+listName);
			 if(queryInstContraint!=null && !queryInstContraint.trim().equals("")){
				 if(queryConstraint!="")
					 queryConstraint=queryConstraint+" AND "+queryInstContraint; 
				 else
					 queryConstraint=queryConstraint+" "+queryInstContraint; 
				
			 }
			
			 logger.info(" : Appending Instrument Constraint If Avialable : "+queryConstraint); 
			 //Appending Coordinate clause.
			 String queryCoordinateContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.coordinates.constraint."+listName);
			 if(queryCoordinateContraint!=null && !queryCoordinateContraint.trim().equals("")){
				 if(queryConstraint!="")
					 queryConstraint=queryConstraint+" AND "+queryCoordinateContraint; 
				 else
					 queryConstraint=queryConstraint+" "+queryCoordinateContraint; 
				
			 }
			 
			 logger.info(" : Appending Coordinate Constraint If Avialable : "+queryConstraint);
			 
			 //Appending Order By clause.
			 String queryOrderByContraint=ConfigurationProfiler.getInstance().getProperty("sql.query.orderby.constraint."+listName);
			
			 //Appending 'Select Part' ; 'Where Constraints' .
			 if(queryConstraint!=null && !queryConstraint.trim().equals("")){
				 query=query+" WHERE "+queryConstraint;
			 }
			 logger.info(" : Appending Where Clause If Avialable : "+query);
			 
			 //Appending ; 'Order By Constraints' .
			 query=query+" "+queryOrderByContraint;
			 
			 logger.info(" : Appending OderBy Constraint If Avialable : "+query);
			 
			 //Appending limit clause.
			 String queryMaxRecords=ConfigurationProfiler.getInstance().getProperty("sql.query.maxrecord.constraint."+listName);
			 if(queryMaxRecords!=null && !queryMaxRecords.trim().equals("")){
				 maxRecordsAllowed=Integer.parseInt(queryMaxRecords);
			 }
			 //Setting max record
			 comCriteriaTO.setMaxRecordsAllowed(maxRecordsAllowed);
			//Getting Limit Constraint. 
			String querylimitContraint=generateLimitConstraintBasedOnDatabase(comCriteriaTO);
			
			 //Appending ; 'Limit Constraints' .
			 query=query+" "+querylimitContraint;
			 
			 logger.info(" : Full query for execution : "+query);
			 
			 
		 return query;
	}
	
	
	private String getJoinSelectClause(CommonCriteriaTO comCriteriaTO) throws Exception{
		String joinSelectList="";
		 String joinTableName="";
		 String query="";
		 String[] joinListName=null;
		 if(comCriteriaTO.getListName()!=null){
			 joinListName=comCriteriaTO.getListName().split(",");
		 }else{
			 joinListName=comCriteriaTO.getListTableName();
		 }
		 //looping for Table Names.
		 for(int intCnt=0;intCnt<joinListName.length;intCnt++){
			 joinSelectList=joinSelectList+getColumnNamesFromProperty(joinListName[intCnt])+",";
			 joinTableName=joinTableName+joinListName[intCnt]+",";
		 }
		 //join select list name.
		 if(joinSelectList.endsWith(",")){
			 joinSelectList=joinSelectList.substring(0, joinSelectList.length()-1);
		 }
		 //Join table name
		 if(joinTableName.endsWith(",")){
			 joinTableName=joinTableName.substring(0, joinTableName.length()-1); 
		 }
		 //Join query
		 query="SELECT "+joinSelectList+" FROM "+joinTableName;
		 
		 return query;
	}
	
	
	@SuppressWarnings("unused")
	private String generateLimitConstraintBasedOnDatabase(CommonCriteriaTO comCriteriaTO) throws Exception{
		String sDrive= ConfigurationProfiler.getInstance().getProperty("jdbc.driver");
		String querylimitConstarint="";
		//check for property max records.
		checkMaxRecordAllowedValue(comCriteriaTO);
		
		if(sDrive.contains("mysql") || sDrive.contains("hsqldb") || sDrive.contains("postgresql")){
			//Setting start row.
			 if(comCriteriaTO.getNoOfRows()!=null && !comCriteriaTO.getNoOfRows().equals("") && !comCriteriaTO.getNoOfRows().equals("0")){
				 querylimitConstarint=" LIMIT "+comCriteriaTO.getNoOfRows();
			 }
			 //Setting No Of Rows
			 if(comCriteriaTO.getStartRow()!=null && !comCriteriaTO.getStartRow().equals("") && querylimitConstarint!=null && !querylimitConstarint.equals("")){
				 querylimitConstarint=querylimitConstarint+" OFFSET "+comCriteriaTO.getStartRow();
			 }
			 		 
		}else if(sDrive.contains("oracle")){
			//Setting start row.
			 if(comCriteriaTO.getNoOfRows()!=null && !comCriteriaTO.getNoOfRows().equals("") && !comCriteriaTO.getNoOfRows().equals("0")){
				 querylimitConstarint=" ROWNUM>="+comCriteriaTO.getStartRow();
			 }
			 //Setting No Of Rows
			 if(comCriteriaTO.getStartRow()!=null && !comCriteriaTO.getStartRow().equals("") && querylimitConstarint!=null && !querylimitConstarint.equals("")){
				 querylimitConstarint="ROWNUM"+" BETWEEN "+comCriteriaTO.getStartRow()+" AND "+comCriteriaTO.getNoOfRows();
			 }
			 
		}
		
		logger.info(" : Limit constraints in method generateLimitConstraintBasedOnDatabase(), limit constraints based on database : "+querylimitConstarint);
		
		return querylimitConstarint;
	}
	
	/*
	 * Setting max record allowed to query.
	 */
	private void checkMaxRecordAllowedValue(CommonCriteriaTO comCriteriaTO){
		int userMaxRecord=0;
		if(comCriteriaTO.getNoOfRows()!=null && !comCriteriaTO.getNoOfRows().equals("")){
			userMaxRecord=Integer.parseInt(comCriteriaTO.getNoOfRows());
		}else{
			//No value setting max record.
			userMaxRecord=comCriteriaTO.getMaxRecordsAllowed();
			comCriteriaTO.setNoOfRows(Integer.toString(userMaxRecord));
		}
		//Changing the no of row value.
		if(comCriteriaTO.getMaxRecordsAllowed()>0){
			if(userMaxRecord>comCriteriaTO.getMaxRecordsAllowed()){
				userMaxRecord=comCriteriaTO.getMaxRecordsAllowed();
				comCriteriaTO.setNoOfRows(Integer.toString(userMaxRecord));
			}
		}
		
		logger.info(" : Max allowed record/ Limit constriant value : "+comCriteriaTO.getNoOfRows());
	}
	
	private Connection getConnectionObject() throws Exception
	{
		Connection con=null;
		con = ConnectionManager.getConnection();		
		if(con==null){
			throw new Exception("Couldn't connect database. Please connection details!!!");
		}
		return con;
	}
	
	
	@SuppressWarnings("deprecation")
	private  boolean compareToDates(String startDate,String endDate) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 =null;
		Date d2 =null;
		df.setLenient(false);
		boolean status=false;
         // Get Date 1
	    d1 = (Date) df.parse(startDate.replace("T", " "));
	    // Get Date 2
	    d2 = (Date) df.parse(endDate.replace("T", " "));

	    if (d1.equals(d2)){
	    	status=true;
	    }else if (d1.before(d2)){
	    	status=true;
	    }else{
	    	status=false;
	    }
	     
	    return status;
	}
	
}
