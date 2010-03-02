/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class ConfigurationProfiler {

	private Properties prop=new Properties();	
	private String sProfileFilePath ;
	protected final  Logger logger = Logger.getLogger(this.getClass());
	
	private ConfigurationProfiler()
	{
		try
		{				
			
			if(sProfileFilePath==null || sProfileFilePath.equals("")){			
				sProfileFilePath=CommonUtils.getPropertyFilePath();
			}
			
			
			if(sProfileFilePath!=null && sProfileFilePath.trim().equals("test.txt")){
				//Configuring test.txt inside the webapp.
				ClassLoader loader = this.getClass().getClassLoader();
				sProfileFilePath=loader.getResource(sProfileFilePath).getFile();	
			}
			
			logger.info("  : Property File Path  : "+sProfileFilePath);
	
			
			loadPropertyValues();			
			TimerTask task = new FileWatcher(new File(sProfileFilePath)) {
			      protected void onChange( File file ){
			      	try{			      		
			      		loadPropertyValues();
				    }catch (Exception e) {
				    	logger.fatal(" :  Exception occured in ConfigurationProfiler : While loading property file ", e);
			      	}
			      }
				};

			    Timer timer = new Timer();
			    // check every 10 min 
			    timer.schedule(task , new Date(), 1000 * 60); // refresh after 600000 miliseconds  = 10 mins
		
		}catch(Exception ex)
		{
			logger.fatal(" :  Exception occured in ConfigurationProfiler : While loading property file ", ex);
		}
		
	}
		
	private void loadPropertyValues()
	{
		/*
		 * New property variable is loaded to safeguard from losing the properties values in case of error while refresh.
		 * */
		try{
			//logger.info("  : Property File Path  : "+sProfileFilePath);
			Properties newProp= new Properties();
			newProp.load(new  FileInputStream( new File(sProfileFilePath)));
			prop=newProp;			
			logger.info(" : Property file loaded successfuly  : ");
		}catch(Exception ex)
		{			
			logger.fatal(" :  Exception occured in ConfigurationProfiler : While loading property file ", ex);
		}
	}

	public static ConfigurationProfiler getInstance() {
		return ConfigurationProfilerHolder.instance;
	}

	private static class ConfigurationProfilerHolder {
		private static ConfigurationProfiler instance = new ConfigurationProfiler();
	}
	
	public String getProperty(String sKey)
	{
		try{
			return prop.getProperty(sKey).toString();
		}catch(Exception ex)
		{
			return "";
		}
	}
	
	public Enumeration getAllKeys(){
		return prop.propertyNames();
	}
	
	public void setProperty(String sKey,String value) throws IOException 
    { 
         prop.setProperty(sKey, value); 
    } 

	
}
