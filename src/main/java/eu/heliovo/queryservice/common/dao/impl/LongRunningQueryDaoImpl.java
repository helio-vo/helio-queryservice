/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.dao.interfaces.LongRunningQueryDao;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.ConnectionManager;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;



public class LongRunningQueryDaoImpl implements LongRunningQueryDao { 

	protected final  Logger logger = Logger.getLogger(this.getClass());
	Connection con = null;
	Statement st = null;
	ResultSet rs=null;

	@Override
	public void insertStatusToHsqlDB(String randomUUIDString,String status) throws DetailsNotFoundException {
		try{
			Properties prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery(prop);
			st = con.createStatement();
			String query=" insert into job_status_table values('"+randomUUIDString+"','"+status+"')";
			logger.info("   :  Query to execute  :  "+query);
		    st.executeUpdate(query);
			con.commit();
			prop=null;
		}catch(Exception e){
			logger.fatal(" Exception occured in insertStatusToHsqlDB() : ",e);
		}
		
		finally
		{
			try {
				
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

	@Override
	public void insertURLToHsqlDB(String randomUUIDString,String url) throws DetailsNotFoundException {
	
		try{
			Properties prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery(prop);
			st = con.createStatement();
			String query="insert into job_url_table values('"+randomUUIDString+"','"+url+"')";
		    st.executeUpdate(query);
		    logger.info("   :  Query to execute  :  "+query);
			con.commit();
			prop=null;
		}catch(Exception e){
			logger.fatal(" Exception occured in insertURLToHsqlDB() : ",e);
		}
			
			finally
			{
				try {
					
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
	
	@Override
	public String getStatusFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException {
	     String sStatus="";
		try{
			Properties prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery(prop);
			st = con.createStatement();
		    rs=st.executeQuery("select * from job_status_table where job_id='"+randomUUIDString+"'");
			con.commit();
			prop=null;
			while(rs.next()){
				sStatus=rs.getString(2);
			}
			}catch(Exception e){
				logger.fatal(" Exception occured in getStatusFromHsqlDB() : ",e);
			}
			
			finally
			{
				try {
					
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
			
			return sStatus;
	}
	
	@Override
	public String getUrlFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException {
		String sUrl=null;
		try{
			Properties prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery(prop);
			st = con.createStatement();
		    rs=st.executeQuery("select * from job_url_table where job_id='"+randomUUIDString+"'");
			con.commit();
			prop=null;
			while(rs.next()){
				sUrl=rs.getString(2);
			}
			}catch(Exception e){
				logger.fatal(" Exception occured in getUrlFromHsqlDB() : ",e);
			}
			
			finally
			{
				try {
					
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
			
			return sUrl;
	}
	
}