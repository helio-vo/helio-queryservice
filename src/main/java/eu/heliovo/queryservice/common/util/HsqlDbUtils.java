package eu.heliovo.queryservice.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import org.apache.log4j.Logger;
import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.LongRunningQueryDao;


public class HsqlDbUtils {
	
	 protected final  Logger logger = Logger.getLogger(this.getClass());
	 private static ClassLoader loader;
	 private HsqlDbUtils(){
		 loader = this.getClass().getClassLoader();
	 }
	 
	 public Properties loadPropertyValues()
		{
			/*
			 * New property variable is loaded to safeguard from losing the properties values in case of error while refresh.
			 * */
			try{
				
				String sProfileFilePath=loader.getResource("test.txt").getFile();
				//logger.info("  : Property File Path  : "+sProfileFilePath);
				Properties newProp= new Properties();
				newProp.load(new  FileInputStream( new File(sProfileFilePath)));		
				System.out.println(" : Hsql Property file loaded successfuly  : ");
				
				return newProp;
			}catch(Exception ex)
			{			
				System.out.println(" :  Exception occured in loading hsql property file : While loading property file "+ ex);
			}
			return null;
		}
	 
	 
	 public static HsqlDbUtils getInstance() {
			return HsqlProfilerHolder.instance;
		}

		private static class HsqlProfilerHolder {
			private static HsqlDbUtils instance = new HsqlDbUtils();
		}
	 
	 
	 public void insertStatusIntoHsqlDB(String randomUUIDString,String status) throws DetailsNotFoundException
	 {
		 LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		 shortNameDao.insertStatusToHsqlDB(randomUUIDString, status);
	 }
	 
	 public void insertURLToHsqlDB(String randomUUIDString,String url) throws DetailsNotFoundException
	 {
		 LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		 shortNameDao.insertURLToHsqlDB(randomUUIDString, url);
	 }
	 
	 public String  getStatusFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException
	 {
		LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		String sStatus=shortNameDao.getStatusFromHsqlDB(randomUUIDString);
		return sStatus;
		 
	 }
	 
	 public String  getUrlFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException
	 {
		LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		String sUrl=shortNameDao.getUrlFromHsqlDB(randomUUIDString);
		return sUrl;
		 
	 }
	 
	 public void dropTable(){
		 try{
		 Connection con = null;
		 Statement st = null;
		 ResultSet rs=null;
		 Properties prop=HsqlDbUtils.getInstance().loadPropertyValues();
		 //Connecting to database.						
		 con = ConnectionManager.getConnectionLongRunningQuery(prop);
		 st = con.createStatement();
	     st.executeUpdate("drop table job_url_table ");
		 con.commit();
		 prop=null;
		 }catch(Exception e ){
			 e.printStackTrace();
		 }
	 }

}