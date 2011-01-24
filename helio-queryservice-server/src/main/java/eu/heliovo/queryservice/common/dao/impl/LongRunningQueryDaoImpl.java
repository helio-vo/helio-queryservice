/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.LongRunningQueryDao;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.ConnectionManager;
import eu.heliovo.queryservice.common.util.ConstantKeywords;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;
import eu.heliovo.queryservice.common.util.InstanceHolders;

public class LongRunningQueryDaoImpl implements LongRunningQueryDao { 

	protected final Logger logger = Logger.getLogger(this.getClass());
	Connection con = null;
	Statement st = null;
	ResultSet rs=null;
	Properties prop=null;

	@Override
	public void insertStatusToHsqlDB(String randomUUIDString,String status) throws DetailsNotFoundException {
		try{
		    //prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
			String query=" insert into status_table values('"+randomUUIDString+"','"+status+"','"+CommonUtils.changeDateFormat(ConstantKeywords.HrsSQLFORMAT, new Date())+"')";
			logger.info("'   :  Query to execute  : ' "+query);
		    st.executeUpdate(query);
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
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
				if(prop!=null)
				{
					prop=null;
				}
			} catch (Exception e) {
				
			}
	    }
	}

	
	@Override
	public void insertURLToHsqlDB(String randomUUIDString,String url) throws DetailsNotFoundException {
	
		try{
			//prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
			String query="insert into url_table values('"+randomUUIDString+"','"+url+"','"+CommonUtils.changeDateFormat(ConstantKeywords.HrsSQLFORMAT, new Date())+"')";
		    st.executeUpdate(query);
		    logger.info("   :  Query to execute  :  "+query);
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
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
					if(prop!=null)
					{
						prop=null;
					}
				} catch (Exception e) {
					
				}
		}
	}
	
	
	@Override
	public String getStatusFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException {
	     String sStatus="";
		try{
			//prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
		    rs=st.executeQuery("select * from status_table where job_id='"+randomUUIDString+"'");
			con.commit();
			while(rs.next()){
				sStatus=rs.getString(2);
			}
			}catch(Exception e){
				e.printStackTrace();
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
					if(prop!=null)
					{
						prop=null;
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
			//prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
		    rs=st.executeQuery("select * from url_table where job_id='"+randomUUIDString+"'");
			con.commit();
			
			while(rs.next()){
				sUrl=rs.getString(2);
			}
			
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal(" Exception occured in deleteStatusFromHsqlDB() : ",e);
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
					if(prop!=null)
					{
						prop=null;
					}
				} catch (Exception e) {
					
				}
		}
			
		return sUrl;
	}
	
	@Override
	public void deleteUrlFromHsqlDB() throws DetailsNotFoundException {
		try{
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
		    st.executeUpdate("delete from url_table where insert_date >= '"+InstanceHolders.getInstance().getProperty("hsql.status.delete.date")+"' and  insert_date <= '"+CommonUtils.date2String(new Date())+"'");
			con.commit();
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal(" Exception occured in deleteUrlFromHsqlDB() : ",e);
			}
			
			finally
			{
				try {
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
					if(prop!=null)
					{
						prop=null;
					}
				} catch (Exception e) {
					
				}
		}
	}
	
	@Override
	public void deleteStatusFromHsqlDB() throws DetailsNotFoundException {
		try{
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
		    st.executeUpdate("delete from status_table where insert_date >= '"+InstanceHolders.getInstance().getProperty("hsql.status.delete.date")+"' and  insert_date <= '"+CommonUtils.date2String(new Date())+"'");
			con.commit();
			}catch(Exception e){
				e.printStackTrace();
				logger.fatal(" Exception occured in getUrlFromHsqlDB() : ",e);
			}
			
			finally
			{
				try {
					
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
					if(prop!=null)
					{
						prop=null;
					}
				} catch (Exception e) {
					
				}
		}

	}
	
	
	@Override
	public String deleteSavedVoTable() throws DetailsNotFoundException {
			String sUrl=null;
			try{
				logger.info(" Deleting Saved file from the system ");
				//Connecting to database.						
				con = ConnectionManager.getConnectionLongRunningQuery();
				st = con.createStatement();
			    rs=st.executeQuery("select * from url_table where insert_date >= '"+InstanceHolders.getInstance().getProperty("hsql.status.delete.date")+"' and  insert_date <= '"+CommonUtils.date2String(new Date())+"'");
				con.commit();
				
				while(rs.next()){
					CommonUtils.deleteFile(rs.getString(2));
				}
				
				}catch(Exception e){
					e.printStackTrace();
					logger.fatal(" Exception occured in deleteStatusFromHsqlDB() : ",e);
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
	
	
	@Override
	public void loadProviderAccessTable(String fileName,String tableName) throws DetailsNotFoundException {
		String sUrl=null;
		try{
			System.out.println("  :  -----> Setting uploaded file for provider access table ----->");
			String query="SET TABLE "+tableName+" SOURCE "+"\""+fileName+"\"";
			System.out.println("loadProviderAccessTable() method "+query);
			//prop=HsqlDbUtils.getInstance().loadPropertyValues();
			//Connecting to database.						
			con = ConnectionManager.getConnectionLongRunningQuery();
			st = con.createStatement();
			st.execute(query);
			con.commit();
			System.out.println("  :  -----> Done ----->");
		}catch(Exception e){
			logger.fatal(" Exception occured in loadProviderAccessTable() : ",e);
			throw new DetailsNotFoundException("Exception: ",e);
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
				throw new DetailsNotFoundException("Exception: ",e);
			}
		}
	}
	
	
}