/* #ident	"%W%" */
package eu.heliovo.queryservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.DeleteStatusTimer;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;
import eu.heliovo.queryservice.common.util.InstanceHolders;

public class StartupServlet extends HttpServlet {
	 
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try{
			String sProfileFilePath=getServletContext().getRealPath("/");
			if(sProfileFilePath!=null && !sProfileFilePath.equals("")){
				 	sProfileFilePath=sProfileFilePath+ "WEB-INF";
					InstanceHolders.getInstance().setProperty("hsqldb.database.path",sProfileFilePath);
					//switchAppenders("jdbc:hsqldb:file:");
					System.out.println(" : HSQLDB database location : "+sProfileFilePath);
					switchAppenders();
			}
			//
			InstanceHolders.getInstance().setProperty("hsql.status.delete.date",CommonUtils.date2String(new Date()));
			//
			TimerTask task = new DeleteStatusTimer() {
				@Override
				 protected void execute(){
			      	try{			      		
			      		int noOfMonth=CommonUtils.getNoOfMonths(InstanceHolders.getInstance().getProperty("hsql.status.delete.date"));
			      		System.out.println(" ------ No of months ------"+noOfMonth );
			      		if(noOfMonth>=6){
			      			deleteLongRunningQueryStatus();
			      			InstanceHolders.getInstance().setProperty("hsql.status.delete.date",CommonUtils.date2String(new Date()));
			      		}
			      		//System.out.println("-------- End ------------");
				    }catch (Exception e){
				    	System.out.println(" :  Exception occured in while deleting Long running query saved status and url "+ e);
			      	}
			    }
			};

			Timer timer = new Timer();
			// check every 2 day 
			timer.schedule(task , new Date(), 86400000*2); // refresh after 86400000 miliseconds  = 2 day
			
			
			
		}
		catch(Exception ex)
		{
			System.out.println("Exception: "+ex);
			ex.printStackTrace();
		}
	}
	
	
	private void switchAppenders() {
	      Properties props = new Properties();
	      try {
	           InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
	           props.load(configStream);
	           configStream.close();
	      } catch(IOException e) {
	          System.out.println("Error: Cannot laod configuration file ");
	      }
	      props.setProperty("log4j.appender.LOGFILE.File",InstanceHolders.getInstance().getProperty("hsqldb.database.path")+"/query_service.log");
	      LogManager.resetConfiguration();
	      PropertyConfigurator.configure(props);
	     }

	private void deleteLongRunningQueryStatus() throws DetailsNotFoundException{
		System.out.println("----------- Deleting Long Running Query --------------");
		//Deleting Status from JOB_STATUS
		HsqlDbUtils.getInstance().deleteStatusFromHsqlDB();
		//Deleting Status from JOB_URL
		HsqlDbUtils.getInstance().deleteUrlFromHsqlDB();
	}

}

