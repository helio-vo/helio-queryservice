package eu.heliovo.queryservice.common.util;

import java.io.File;
import java.io.FileInputStream;
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
	 
	 public synchronized Properties loadPropertyValues()
	 {
		try{
			String sProfileFilePath=loader.getResource("test.txt").getFile();
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
	 
	 /**
	  * 
	  * @param randomUUIDString
	  * @param status
	  * @throws DetailsNotFoundException
	  */
	 public synchronized void  insertStatusIntoHsqlDB(String randomUUIDString,String status) throws DetailsNotFoundException
	 {
		 LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		 shortNameDao.insertStatusToHsqlDB(randomUUIDString, status);
	 }
	 
	 /**
	  * 
	  * @param randomUUIDString
	  * @param url
	  * @throws DetailsNotFoundException
	  */
	 public synchronized void insertURLToHsqlDB(String randomUUIDString,String url) throws DetailsNotFoundException
	 {
		 LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		 shortNameDao.insertURLToHsqlDB(randomUUIDString, url);
	 }
	 
	 /**
	  * 
	  * @param randomUUIDString
	  * @return
	  * @throws DetailsNotFoundException
	  */
	 public synchronized String  getStatusFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException
	 {
		LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		String sStatus=shortNameDao.getStatusFromHsqlDB(randomUUIDString);
		return sStatus;
	 }
	 
	 /**
	  * 
	  * @param randomUUIDString
	  * @return
	  * @throws DetailsNotFoundException
	  */
	 public synchronized String  getUrlFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException
	 {
		LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		String sUrl=shortNameDao.getUrlFromHsqlDB(randomUUIDString);
		return sUrl;
	 }
	 
	 /**
	  * 
	  * @throws DetailsNotFoundException
	  */
	 public synchronized void  deleteUrlFromHsqlDB() throws DetailsNotFoundException
	 {
		LongRunningQueryDao longRunningDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		longRunningDao.deleteUrlFromHsqlDB();
	 }
	 
	 /**
	  * 
	  * @throws DetailsNotFoundException
	  */
	 public synchronized void  deleteStatusFromHsqlDB() throws DetailsNotFoundException
	 {
		LongRunningQueryDao longRunningDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		longRunningDao.deleteStatusFromHsqlDB();
	 }
	 
	 /**
	  * 
	  * @throws DetailsNotFoundException
	  */
	 public synchronized void  deleteSavedVoTable() throws DetailsNotFoundException
	 {
		LongRunningQueryDao longRunningDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		longRunningDao.deleteSavedVoTable();
	 }
	 
	 /**
	  * 
	  * @param fileName
	  * @param tableName
	  * @throws DetailsNotFoundException
	  */
	 public synchronized void  loadProviderAccessTable(String fileName,String tableName) throws DetailsNotFoundException
	 {
		 LongRunningQueryDao shortNameDao= CommonDaoFactory.getInstance().getLongRunningQueryDao();
		 shortNameDao.loadProviderAccessTable(fileName,tableName);
	 }
	 
	 /*
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
    */
}