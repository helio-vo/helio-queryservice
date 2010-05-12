package eu.heliovo.queryservice.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import eu.heliovo.queryservice.common.util.InstanceHolders;

public class ConnectionManager {
	/**
	 * @return
	 * This method returns connection to the data source
	 */
	public static Connection getConnection() { 		
		java.sql.Connection con = null;
		boolean noConnection = true;		
		int i = 0;
		int count=0;
		String jdbcString = ConfigurationProfiler.getInstance().getProperty("jdbc.driver");
	    String jdbcURL = getHsqlDBEmbeddedDatabasePath(ConfigurationProfiler.getInstance().getProperty("jdbc.url"));
	    String user = ConfigurationProfiler.getInstance().getProperty("jdbc.user");
	    String passwd = ConfigurationProfiler.getInstance().getProperty("jdbc.password");
	    System.out.println(" Driver Name : "+jdbcString+" Database URL  : "+jdbcURL+" User : "+user+" Password  "+passwd);
		while(noConnection){
			try {
				  System.out.println(" Driver manager tring to get connection --> count "+i);
	              Class.forName(jdbcString);                  
			      con = DriverManager.getConnection(jdbcURL,user,passwd);     
			      System.out.println(" Connected succesfully !!!");
			      noConnection = false;    
			}catch(SQLException sqlex) {  
				//logger.info(" SQLException Occoured in Connection Manager :getConnection()"+sqlex.getMessage()+" trying to reconeect for "+i+" time ");
				if(i < 3){ 
					i++;
					noConnection = true;
				}else {
					try {
						break;
					} catch (Exception e) {
						//logger.info(" SQLException Occoured in Connection Manager :getConnection()"+e.getMessage()+" trying to reconeect for "+i+" time ");
						e.printStackTrace();
					}        
				}
			}catch(ClassNotFoundException classexe) {				
				try {					
					count++;
					if(count<3){
						throw new Exception(classexe.toString());
					}else{
						noConnection=false;
						throw new Exception(classexe.toString());
					}    
				} catch (Exception e) {
					//logger.info("NamingException Occoured in Connecton manager class getConnection() "+e.getMessage()+" trying to reconeect for "+count+" time ");
					e.printStackTrace(); 
				}
			}
		}	
		return con;
	} 
	
	
	/**
	 * @return
	 * This method returns connection to the data source
	 */
	public static Connection getConnectionForWebApp() { 		
		java.sql.Connection con = null;
		boolean noConnection = true;		
		int i = 0;
		int count=0;
		String jdbcString = InstanceHolders.getInstance().getProperty("jdbc.driver");
	    String jdbcURL = InstanceHolders.getInstance().getProperty("jdbc.url");
	    String user = InstanceHolders.getInstance().getProperty("jdbc.user");
	    String passwd = InstanceHolders.getInstance().getProperty("jdbc.password");
	    System.out.println(" Driver Name : "+jdbcString+" Database URL  : "+jdbcURL+" User : "+user+" Password  "+passwd);
		while(noConnection){
			try {
				System.out.println(" Driver manager tring to get connection --> count "+i);
	            Class.forName(jdbcString);                  
			    con = DriverManager.getConnection(jdbcURL,user,passwd);     
			    System.out.println(" Connected succesfully !!!"); 
			    noConnection = false;  
			}catch(SQLException sqlex) {  
				//logger.info(" SQLException Occoured in Connection Manager :getConnection()"+sqlex.getMessage()+" trying to reconeect for "+i+" time ");
				if(i < 3){
					i++;
					noConnection = true;
				}else {
					try {
						break;
					} catch (Exception e) {
						//logger.info(" SQLException Occoured in Connection Manager :getConnection()"+e.getMessage()+" trying to reconeect for "+i+" time ");
						e.printStackTrace();
					}        
				}
			}catch(ClassNotFoundException classexe) {				
				try {					
					count++;
					if(count<3){
						throw new Exception(classexe.toString());
					}else{
						noConnection=false;
						throw new Exception(classexe.toString());
					}    
				} catch (Exception e) {
					//logger.info("NamingException Occoured in Connecton manager class getConnection() "+e.getMessage()+" trying to reconeect for "+count+" time ");
					e.printStackTrace(); 
				}
			}
		}	
		return con;
	} 
	
	
	/**
	 * @return
	 * This method returns connection to the data source
	 */
	public static Connection getConnectionLongRunningQuery(Properties prop) { 		
		java.sql.Connection con = null;
		boolean noConnection = true;		
		int i = 0;
		int count=0;
		String jdbcString = prop.getProperty("jdbc.driver");
	    String jdbcURL = getHsqlDBEmbeddedDatabasePath(prop.getProperty("jdbc.url"));
	    String user = prop.getProperty("jdbc.user");
	    String passwd = prop.getProperty("jdbc.password");
	    System.out.println(" Driver Name : "+jdbcString+" Database URL  : "+jdbcURL+" User : "+user+" Password  "+passwd);
		while(noConnection){
			try {
				System.out.println(" Driver manager tring to get connection --> count "+i);
	            Class.forName(jdbcString);                  
			    con = DriverManager.getConnection(jdbcURL,user,passwd);     
			    System.out.println(" Connected succesfully !!!");    
			    noConnection = false;  
			}catch(SQLException sqlex) {  
				//logger.info(" SQLException Occoured in Connection Manager :getConnection()"+sqlex.getMessage()+" trying to reconeect for "+i+" time ");
				if(i < 3){ 
					i++;
					noConnection = true;
				}else {
					try {
						break;
					} catch (Exception e) {
						//logger.info(" SQLException Occoured in Connection Manager :getConnection()"+e.getMessage()+" trying to reconeect for "+i+" time ");
						e.printStackTrace();
					}        
				}
			}catch(ClassNotFoundException classexe) {				
				try {					
					count++;
					if(count<3){
						throw new Exception(classexe.toString());
					}else{
						noConnection=false;
						throw new Exception(classexe.toString());
					}    
				} catch (Exception e) {
					//logger.info("NamingException Occoured in Connecton manager class getConnection() "+e.getMessage()+" trying to reconeect for "+count+" time ");
					e.printStackTrace(); 
				}
			}
		}	
		return con;
	} 
	
	//To get HSQL DB embedded database path.
	private static String getHsqlDBEmbeddedDatabasePath(String url){
		if(url!=null && InstanceHolders.getInstance().getProperty("hsqldb.database.path")!=null){
			url=url.replaceAll("kwpath",InstanceHolders.getInstance().getProperty("hsqldb.database.path") );
		}
		return url;
	}	
}