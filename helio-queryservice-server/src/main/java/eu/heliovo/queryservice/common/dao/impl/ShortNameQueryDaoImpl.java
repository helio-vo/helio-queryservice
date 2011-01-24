/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.jdbc.SequentialResultSetStarTable;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;
import eu.heliovo.queryservice.common.transfer.CommonTO;
import eu.heliovo.queryservice.common.transfer.ResultTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.ConfigurationProfiler;
import eu.heliovo.queryservice.common.util.ConnectionManager;
import eu.heliovo.queryservice.common.util.QueryWhereClauseParser;
import eu.heliovo.queryservice.common.util.StandardTypeTable;
import eu.heliovo.queryservice.common.util.VOTableMaker;

/**
 * Implementation of the ShortNameQueryDao.
 */
public class ShortNameQueryDaoImpl implements ShortNameQueryDao {
	/**
	 * The logger to use	
	 */
	private static final Logger _LOGGER = Logger.getLogger(ShortNameQueryDao.class);
	
	/**
	 * Create the dao
	 */
	public ShortNameQueryDaoImpl(){ 
						
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.org.helio.common.dao.interfaces.ShortNameQueryDao#generateVOTableDetails(com.org.helio.common.transfer.criteriaTO.CommonCriteriaTO)
	 */
	public void generateVOTableDetails(CommonCriteriaTO comCriteriaTO) throws DetailsNotFoundException,Exception {
		//List data or table names
		String[] listName=comCriteriaTO.getListTableName();
		//start date vales
		String startDateTimeList[]=comCriteriaTO.getStartDateTimeList();
		//end date values
		String endDateTimeList[]=comCriteriaTO.getEndDateTimeList();
		//Join clause
		String sJoin=comCriteriaTO.getJoin();
		
		try{
			//Checking for list name.
		  	if(listName!=null){
					if(startDateTimeList!=null && endDateTimeList!=null){
						//Start time & End time array of time.
						comCriteriaTO=handlingStartTimeAndEndTimeArray(comCriteriaTO);
					}else{
						//Start time and end time is null; Get all result values.
						comCriteriaTO=handlingNonTimeBased(comCriteriaTO);
					}
			}else{
				  comCriteriaTO.setQueryStatus("ERROR");
				  comCriteriaTO.setQueryDescription("FROM clause value is missing in request xml.");
				  VOTableMaker.writeTables(comCriteriaTO);
			}
		} catch (Exception e){		
				//Writing all details into table.
				comCriteriaTO.setQueryStatus("ERROR");
				comCriteriaTO.setQueryDescription(e.getMessage());
				
				VOTableMaker.writeTables(comCriteriaTO);
				_LOGGER.fatal(" Exception occured while generating VOTABLE: ",e);
				throw new DetailsNotFoundException("EXCEPTION ", e);
		 }
		
	}
	
	/*
	 * Get the list of Tables in a Database.
	 */
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
				_LOGGER.fatal(" Exception occured while closing connection: ",e);
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

	/**
	 * Overriding the method ;result will be a hashmap
	 */
	public HashMap<String, String> getTableColumnNames(String tableName) throws Exception
	{
		ResultSet rsColumns = null;
		DatabaseMetaData meta =null;
		Connection con=null;
		HashMap<String, String> dataTypeLookup=new LinkedHashMap<String, String>();
		try{
			con=getConnectionObject();
			meta = con.getMetaData();
		    rsColumns = meta.getColumns(null, null, tableName, null);
		    if(rsColumns!=null){
			    while (rsColumns.next()) {
			      dataTypeLookup.put(rsColumns.getString("COLUMN_NAME").toLowerCase(), rsColumns.getString("TYPE_NAME"));
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
		return dataTypeLookup;
	}

	
	/*
	 * Creating column names.
	 */
	private String getColumnNamesFromProperty(String tableName) throws Exception
	{
		String colNames=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+tableName);
		//Column names array
		String[] columnNames=colNames.split("::");
		String colNamesForTable="";
		
		if(colNames!=null && !colNames.trim().equals("")){		
			for(int i=0;i<columnNames.length;i++)
			{
				colNamesForTable=colNamesForTable+tableName+"."+columnNames[i]+",";
			}
		}else{
			throw new Exception("Couldn't find coulumn names for table name "+tableName+". Please check configuration property file.");
		}
		
		if(colNamesForTable.endsWith(",")){
			colNamesForTable=colNamesForTable.substring(0, colNamesForTable.length()-1);
		}
		
		_LOGGER.info(" : Table Name : "+tableName +" List of columns "+colNamesForTable);
		
		return colNamesForTable;
	}
	
	
	private String  generateQuery(String listName,CommonCriteriaTO comCriteriaTO) throws Exception{
			 String queryConstraint="";
			 String queryWhereClause="";
			 String query="";
			 String[] arrayRegionValues=null;
			 int maxRecordsAllowed=0;
			 HashMap<String,String> params  = new HashMap<String,String>();
			 
			 params.put("kwstartdate", comCriteriaTO.getStartDateTime());
			 params.put("kwenddate", comCriteriaTO.getEndDateTime());
			 params.put("kwinstrument", comCriteriaTO.getInstruments());
			 params.put("kwdec", comCriteriaTO.getPosDec());
			 params.put("kwra", comCriteriaTO.getPosRa());
			 params.put("kwsize", comCriteriaTO.getSize());
			 params.put("kwref", comCriteriaTO.getPosRef());
			 
			 //Region values
			 String stringArray=comCriteriaTO.getsRegionValues();
			 if(stringArray!=null) {
				 arrayRegionValues=stringArray.split("::");
				 for(int count=0;count<arrayRegionValues.length;count++)
				 {
					 params.put("kwPosNum"+count, arrayRegionValues[count]);
				 }
			 }
			 
			 //Setting parameter value
			 comCriteriaTO.setParamData(params);
			 //Checking for Where Clause 
			 if(comCriteriaTO.getJoin()!=null && !comCriteriaTO.getJoin().equals("") && comCriteriaTO.getJoin().equals("yes")){
				 //Method to get joined query.( Select items ).
				 query=getJoinSelectClause(comCriteriaTO);
			 }else{
				 //Normal query
				 query="SELECT "+getColumnNamesFromProperty(listName)+" FROM "+listName;
			 }
			 
			 //logger.info(" : Query String with 'Select' and 'From' : "+query);
			 //Getting where clause.
			 if(comCriteriaTO.getWhereClause()!=null && !comCriteriaTO.getWhereClause().equals("")){
				 queryWhereClause=QueryWhereClauseParser.generateWhereClause(comCriteriaTO);
				 
				 //Setting all declared string to null;
				 QueryWhereClauseParser.deAllocateStringToNull();
				 
			 }
			 
			
			 //Appending Time clause.
			 String queryTimeContraint=timeQueryConstraint(listName);
			 if(queryTimeContraint!=null && !queryTimeContraint.trim().equals("") && comCriteriaTO.getStartDateTime()!=null && !comCriteriaTO.getStartDateTime().trim().equals("")  && comCriteriaTO.getEndDateTime()!=null && !comCriteriaTO.getEndDateTime().trim().equals("")){
				 //Checking if it has where clause.
				 if(!queryWhereClause.equals("")){
					 queryConstraint=queryConstraint+" "+queryTimeContraint+" AND "+queryWhereClause;
				 }else{
					 queryConstraint=queryConstraint+" "+queryTimeContraint;
				 }
			 }else{
				 queryConstraint=queryConstraint+" "+queryWhereClause;
			 }
			 
			// logger.info(" : Appending Time Constraint If Avialable : "+queryConstraint);
			 
			 //Appending Instrument clause.
			 String queryInstContraint=instrumentsQueryConstraint(listName);
			 if(queryInstContraint!=null && !queryInstContraint.trim().equals("")){
				 if(queryConstraint!=null && !queryConstraint.trim().equals(""))
					 queryConstraint=queryConstraint+" AND "+queryInstContraint; 
				 else
					 queryConstraint=queryConstraint+" "+queryInstContraint; 
				
			 }
			
			 //logger.info(" : Appending Instrument Constraint If Avialable : "+queryConstraint); 
			 //Appending Coordinate clause.
			 String queryCoordinateContraint=coordinatesQueryConstraint(listName,comCriteriaTO);
			 if(queryCoordinateContraint!=null && !queryCoordinateContraint.trim().equals("")){
				 if(queryConstraint!=null && !queryConstraint.trim().equals(""))
					 queryConstraint=queryConstraint+" AND "+queryCoordinateContraint; 
				 else
					 queryConstraint=queryConstraint+" "+queryCoordinateContraint; 
				
			 }		 
			 
			// logger.info(" : Appending Coordinate Constraint If Avialable : "+queryConstraint);
			 
			 //Appending Order By clause.
			 String queryOrderByContraint=orderByQueryConstraint(listName);
			
			 //Appending 'Select Part' ; 'Where Constraints' .
			 if(queryConstraint!=null && !queryConstraint.trim().equals("")){
				 query=query+" WHERE "+queryConstraint;
			 }
			 //logger.info(" : Appending Where Clause If Avialable : "+query);
			 
			 //Appending ; 'Order By Constraints' .
			 query=query+" "+queryOrderByContraint;
			 
			 //logger.info(" : Appending OderBy Constraint If Avialable : "+query);
			 
			 //Appending limit clause.
			 String queryMaxRecords=maxRecordQueryConstraint(listName);
			 if(queryMaxRecords!=null && !queryMaxRecords.trim().equals("")){
				 maxRecordsAllowed=Integer.parseInt(queryMaxRecords);
			 }
			 //Setting max record
			 comCriteriaTO.setMaxRecordsAllowed(maxRecordsAllowed);
			 //Getting Limit Constraint. 
			 String querylimitContraint=generateLimitConstraintBasedOnDatabase(comCriteriaTO);
			
			 //Appending ; 'Limit Constraints' .
			 query=query+" "+querylimitContraint;
			 
			 //logger.info(" : Full query for execution : "+query);
			 
			 
		 return query;
	}
	
	@SuppressWarnings("unused")
	private String coordinateSystem()
	{
		return null;
	}
	
	/**
	 * 
	 * @param listName
	 * @return
	 */
	private String timeQueryConstraint(String listName)
	{
		String queryTimeContraint="";
		for(int intCnt=0;intCnt<listName.split(",").length;intCnt++){
				//Appending Time clause.
				 queryTimeContraint=queryTimeContraint+ConfigurationProfiler.getInstance().getProperty("sql.query.time.constraint."+listName.split(",")[intCnt]);
				 if(queryTimeContraint!=null && !queryTimeContraint.trim().equals(""))
				 {
					 queryTimeContraint=queryTimeContraint+" AND ";
				 }
		}
		//Substring
		if(queryTimeContraint!=null && !queryTimeContraint.trim().equals(""))
			queryTimeContraint=queryTimeContraint.substring(0, queryTimeContraint.length()-4);
		
		return queryTimeContraint;
	}
	
	/**
	 * 
	 * @param listName
	 * @return
	 */
	private String instrumentsQueryConstraint(String listName)
	{
		String queryInstContraint="";
		String instValue="";
		for(int intCnt=0;intCnt<listName.split(",").length;intCnt++){
				//Appending Time clause.
			instValue=ConfigurationProfiler.getInstance().getProperty("sql.query.instr.constraint."+listName.split(",")[intCnt]);
			if(instValue!=null && !instValue.trim().equals("")){
				queryInstContraint=queryInstContraint+instValue+" AND ";
			}
		}
		//Substring
		if(queryInstContraint!=null && !queryInstContraint.trim().equals(""))
			queryInstContraint=queryInstContraint.substring(0, queryInstContraint.length()-4);
		
		return queryInstContraint;
	}
	
	/**
	 * 
	 * @param listName
	 * @return
	 */
	private String coordinatesQueryConstraint(String listName,CommonCriteriaTO comCriteriaTO)
	{
		String queryCoordinateContraint="";
		for(int intCnt=0;intCnt<listName.split(",").length;intCnt++){
				//Appending Time clause.
			String propertyCordinateValue="";
			if(comCriteriaTO.getsRegion()!=null && !comCriteriaTO.getsRegion().equals("") && comCriteriaTO.getsRegion().trim().toLowerCase().equals("ellipse")){
				propertyCordinateValue=ConfigurationProfiler.getInstance().getProperty("sql.query.coordinates.constraint.ellipse."+listName.split(",")[intCnt]);
			}else{
				propertyCordinateValue=ConfigurationProfiler.getInstance().getProperty("sql.query.coordinates.constraint.defualt."+listName.split(",")[intCnt]);
			}
			queryCoordinateContraint=queryCoordinateContraint+propertyCordinateValue;
			if(queryCoordinateContraint!=null && !queryCoordinateContraint.trim().equals("")){
				queryCoordinateContraint=queryCoordinateContraint+queryCoordinateContraint+" AND ";
			}
		}
		//Substring
		if(queryCoordinateContraint!=null && !queryCoordinateContraint.trim().equals(""))
			queryCoordinateContraint=queryCoordinateContraint.substring(0, queryCoordinateContraint.length()-1);
		
		return queryCoordinateContraint;
	}
	
	
	/**
	 * 
	 * @param listName
	 * @return
	 */
	private String orderByQueryConstraint(String listName)
	{
		String queryOrderBYContraint="ORDER BY ";
		String orderByValue="";
		for(int intCnt=0;intCnt<listName.split(",").length;intCnt++){
				//Appending Time clause.
			orderByValue=ConfigurationProfiler.getInstance().getProperty("sql.query.orderby.constraint."+listName.split(",")[intCnt]);
			if(orderByValue!=null && !orderByValue.trim().equals("")){
				queryOrderBYContraint=queryOrderBYContraint+orderByValue.trim()+",";
			}
		}
		if(queryOrderBYContraint.trim().equals("ORDER BY"))
			queryOrderBYContraint="";
		//Substring
		if(queryOrderBYContraint!=null && !queryOrderBYContraint.trim().equals(""))
			queryOrderBYContraint=queryOrderBYContraint.substring(0, queryOrderBYContraint.length()-1);
		
		return queryOrderBYContraint;
	}
	/**
	 * 
	 * @param listName
	 * @return
	 */
	private String maxRecordQueryConstraint(String listName)
	{
		String[] queryMaxRecordContraint=new String[listName.split(",").length];
		String maxAllowedValue="";
		for(int intCnt=0;intCnt<listName.split(",").length;intCnt++){
				//Appending Time clause.
			queryMaxRecordContraint[intCnt]=ConfigurationProfiler.getInstance().getProperty("sql.query.maxrecord.constraint."+listName.split(",")[intCnt]);
		}
		if(queryMaxRecordContraint.length>0)
			maxAllowedValue=Collections.max(Arrays.asList(queryMaxRecordContraint));
		
		return maxAllowedValue;
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
	
	/*
	 * 
	 */
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
		
		_LOGGER.info(" : Limit constraints in method generateLimitConstraintBasedOnDatabase(), limit constraints based on database : "+querylimitConstarint);
		
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
		
		_LOGGER.info(" : Max allowed record/ Limit constriant value : "+comCriteriaTO.getNoOfRows());
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
	
	/*
	 * Method to compare to date.
	 */
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
	
	/*
	 * Method to add result set data to VOTable.
	 */
	private ResultTO addingResultSetToVOTable(CommonCriteriaTO comCriteriaTO) throws Exception
	{
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		ResultTO resultTO=new ResultTO();
		try{
		
			String sRepSql = CommonUtils.replaceParams(generateQuery(comCriteriaTO.getTableName(),comCriteriaTO), comCriteriaTO.getParamData());
			//logger.info(" : Query String After Replacing Value :"+sRepSql);	
			//Setting Table Name.
			comCriteriaTO.setTableName(comCriteriaTO.getListName());
			//Connecting to database.						
			con = comCriteriaTO.getConnection();
			st = con.createStatement();
			rs= st.executeQuery(sRepSql);
			comCriteriaTO.setQueryStatus("OK");
			comCriteriaTO.setQuery(sRepSql);
			resultTO.setResultSet(rs);
			resultTO.setQuery(sRepSql);
			if (_LOGGER.isDebugEnabled()) {
				_LOGGER.debug(" : End of addingResultSetToVOTable() method  : ");				
			}
			return resultTO;
		} catch(Exception e) {
			throw new RuntimeException("Please check sql syntax, some problem with executing this query.", e);
		}
	}
	
	/*
	 * start time and end time is null or no value.
	 */
	private CommonCriteriaTO handlingNonTimeBased(CommonCriteriaTO comCriteriaTO) throws SQLException, Exception
	{
		
		ResultSet rs=null;
		Connection con=null;
		try{
		
		_LOGGER.info(" : Start of method handlingStartTimeAndEndTime()--> Start time and End time is NULL,select all values. :");
		//List data or table names
		String[] listName=comCriteriaTO.getListTableName();
		String sJoin=comCriteriaTO.getJoin();
		StarTable[] tables=null;
		int count=listName.length;
		con=getConnectionObject();
		// array of queries
		String[] queryArray=new String[count];
		int tableCount=0;
		
		//Handling for join queries
		if(sJoin!=null && !sJoin.equals("") && sJoin.equals("yes"))
		{
			//When there is join queries; 
			count=1;
			
		}
		//tables count.
		tables=new StarTable[count];
		//For loop start
		for(int intCnt=0;intCnt<count;intCnt++){
			String tableName="";
			if(sJoin!=null && !sJoin.equals("") && sJoin.equals("yes"))
			{
				tableName=arrayToString(listName);
			}else{
				tableName=listName[intCnt];
			}
			comCriteriaTO.setTableName(tableName);
			//Connection object
			comCriteriaTO.setConnection(con);
			//getting the result set
			ResultTO resultTO= addingResultSetToVOTable(comCriteriaTO);
			rs=resultTO.getResultSet();
			queryArray[tableCount]=resultTO.getQuery();
			tables[tableCount] = new StandardTypeTable( new SequentialResultSetStarTable( rs ) );
			tables[tableCount].setName(comCriteriaTO.getContextPath()+"_"+tableName);
			//Editing column property.
			VOTableMaker.setColInfoProperty(tables[tableCount], tableName);
			tableCount++;
		}
		comCriteriaTO.setQueryArray(queryArray);
		comCriteriaTO.setTables(tables);
		//Editing column property.
		//VOTableMaker.setColInfoProperty(tables, listName);
		//Writing all details into table.
		VOTableMaker.writeTables(comCriteriaTO);
		_LOGGER.info(" : VOTable succesfully created :");
		
		}catch(Exception e){
			e.printStackTrace();
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription(e.getMessage());
			VOTableMaker.writeTables(comCriteriaTO);
		}
		
		finally
		{
		    try {
		    	if(rs!=null)
				{
					rs.close();
					rs=null;
				}
		    	if(con!=null)
				{
					con.close();
					con=null;
				}
			} catch (Exception e) {
				
			}
		}
		return comCriteriaTO;
	}
	
	private String arrayToString(String[] stringarray){
	    String str = "";
	    for (int i = 0; i < stringarray.length; i++) {
	      str = str + stringarray[i]+",";
	    }
	    if(str!=null && !str.equals(""))
	    	str=str.substring(0, str.length()-1);
	    return str;
	  }
	
	/*
	 * Array of start time and end time
	 */
	@SuppressWarnings("unused")
	private CommonCriteriaTO handlingStartTimeAndEndTimeArray(CommonCriteriaTO comCriteriaTO) throws SQLException, Exception
	{
		_LOGGER.info(" : Start of method handlingStartTimeAndEndTimeArray()--> Array of start time andend time. :");
		StarTable[] tables=null;
		ResultSet rs=null;
		Connection con=null;
		String[] queryArray=null;
		HashMap<String,String> helioInstName=new HashMap<String,String>();
		try{
		//Join query
		String sJoin=comCriteriaTO.getJoin();
		//List data or table names
		String[] listName=comCriteriaTO.getListTableName();
		//start date vales
		String startDateTimeList[]=comCriteriaTO.getStartDateTimeList();
		//end date values
		String endDateTimeList[]=comCriteriaTO.getEndDateTimeList();
		//Count of tables in respionse.
		int count=0;
		//Coccetion to database
		con=getConnectionObject();
		//Setting connection
		comCriteriaTO.setConnection(con);
		//
		int tableCount=0;
		int CountOfTable=listName.length;
		//Handling for join queries
		if(sJoin!=null && !sJoin.equals("") && sJoin.equals("yes"))
		{
			//When there is join queries; 
			CountOfTable=1;
			count=CountOfTable*startDateTimeList.length;
		}
		
		if(startDateTimeList.length==endDateTimeList.length){
		//For loop start
		for(int intCnt=0;intCnt<CountOfTable;intCnt++){
			//Setting table name.
			comCriteriaTO.setTableName(listName[intCnt]);
			
			//Checking for proper values
		if((startDateTimeList.length>1 && endDateTimeList.length>1 && listName.length==1) || (startDateTimeList.length==1 && endDateTimeList.length==1 && listName.length>1) || (startDateTimeList.length==1 && endDateTimeList.length==1 && listName.length==1)){
			//Count of tables in respionse.
			 count=listName.length*startDateTimeList.length;
			//table count
			if(tableCount==0){
				tables=new StarTable[count];
				queryArray=new String[count];
			}
			//loop for start date.
		  for(int intTimeCnt=0;intTimeCnt<startDateTimeList.length;intTimeCnt++){
			 String startDate=startDateTimeList[intTimeCnt];
			 String endDate=endDateTimeList[intTimeCnt];
			 //Setting start time 
			comCriteriaTO.setStartDateTime(startDate);
			//Setting end time
			comCriteriaTO.setEndDateTime(endDate);
			//Results for each values
			HashMap<Object, Object> ambResults=createStartTableForTimeBased(comCriteriaTO);
			queryArray[tableCount]=(String) ambResults.get("Query");
			//
			tables[tableCount]=(StarTable) ambResults.get("StartTable");
			tableCount++;
			//
	
			/*
			//Checking if start date or end date is null or no value.
			if((startDate!=null && !startDate.trim().equals("")) && (endDate!=null && !endDate.trim().equals(""))){
			//Comparing 2 date value.
			if(compareToDates(startDate,endDate)){
				String tableName="";
				if(sJoin!=null && !sJoin.equals("") && sJoin.equals("yes"))
				{
					tableName=arrayToString(listName);
				}else{
					tableName=listName[intCnt];
				}
				logger.info(" : Start Date ; End Date and List Name : "+startDate+"  : "+endDate+"  : "+tableName);
				comCriteriaTO.setTableName(tableName);
				//Connection object
				comCriteriaTO.setConnection(con);
				//getting the result set
				ResultTO resultTO= addingResultSetToVOTable(comCriteriaTO);
				rs=resultTO.getResultSet();
				queryArray[tableCount]=resultTO.getQuery();
				tables[tableCount] = new StandardTypeTable( new SequentialResultSetStarTable( rs ) );
				tables[tableCount].setName(tableName);
				//Editing column property.
				VOTableMaker.setColInfoProperty(tables[tableCount], tableName);
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
			*/
		  }
		}else if(startDateTimeList.length==endDateTimeList.length && listName.length==endDateTimeList.length){
			count=listName.length;
			//table count
			if(tableCount==0){
				tables=new StarTable[count];
				queryArray=new String[count];
			}
			String startDate=startDateTimeList[intCnt];
			String endDate=endDateTimeList[intCnt];
			 //Setting start time 
			comCriteriaTO.setStartDateTime(startDate);
			//Setting end time
			comCriteriaTO.setEndDateTime(endDate);
			//Results for each values
			HashMap<Object, Object> ambResults=createStartTableForTimeBased(comCriteriaTO);
			
			queryArray[tableCount]=(String) ambResults.get("Query");
			//
			tables[tableCount]=(StarTable) ambResults.get("StartTable");
			tableCount++;
		}else{
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription("Please send proper values, request is not succesfull.");
			VOTableMaker.writeTables(comCriteriaTO);
		}
		}
		comCriteriaTO.setQueryArray(queryArray);
		comCriteriaTO.setTables(tables);
		//Editing column property.
		//VOTableMaker.setColInfoProperty(tables, listName);
		//Writing all details into table.
		VOTableMaker.writeTables(comCriteriaTO);
		_LOGGER.info(" : VOTable succesfully created :");	
		
		}else{
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription("Start date and End date should have same no of values.");
			VOTableMaker.writeTables(comCriteriaTO);
		}
		}catch(Exception e){
			e.printStackTrace();
			comCriteriaTO.setQueryStatus("ERROR");
			comCriteriaTO.setQueryDescription(e.getMessage());
			VOTableMaker.writeTables(comCriteriaTO);
			
		}
		
		finally
		{
		    try {
		    	if(rs!=null)
				{
					rs.close();
					rs=null;
				}
		    	if(con!=null)
				{
					con.close();
					con=null;
				}
			} catch (Exception e) {
				if (_LOGGER.isDebugEnabled()) {
					_LOGGER.debug("Unable to close result set:  "+e.getMessage(), e);
				}
			}
		}
		return comCriteriaTO;
 }
	
	
public HashMap<Object, Object> createStartTableForTimeBased(CommonCriteriaTO comCriteriaTO) throws Exception
{
	//
	 String startDate=comCriteriaTO.getStartDateTime();
	 String endDate=comCriteriaTO.getEndDateTime();
	 //Join query
	 String sJoin=comCriteriaTO.getJoin();
	 //List data or table names
	 String[] listName=comCriteriaTO.getListTableName();
	 ResultSet rs=null;
	 HashMap<Object, Object> hmpResults=new LinkedHashMap<Object, Object>();
	if((startDate!=null && !startDate.trim().equals("")) && (endDate!=null && !endDate.trim().equals(""))){
		//Comparing 2 date value.
		if(compareToDates(startDate,endDate)){
			String tableName="";
			if(sJoin!=null && !sJoin.equals("") && sJoin.equals("yes"))
			{
				tableName=arrayToString(listName);
				_LOGGER.info(" : This is a JOIN query : ");
			}else{
				tableName=comCriteriaTO.getTableName();
			}
			_LOGGER.info(" : Start Date ; End Date and List Name : "+startDate+"  : "+endDate+"  : "+tableName);
			comCriteriaTO.setTableName(tableName);
			//getting the result set
			ResultTO resultTO= addingResultSetToVOTable(comCriteriaTO);
			rs=resultTO.getResultSet();
			StarTable startTable = new StandardTypeTable( new SequentialResultSetStarTable( rs ) );
			startTable.setName(tableName);
			//Editing column property.
			VOTableMaker.setColInfoProperty(startTable, tableName);
			hmpResults.put("StartTable", startTable);
			hmpResults.put("Query", resultTO.getQuery());
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
	
	return hmpResults;
   }


}
