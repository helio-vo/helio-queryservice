/* #ident	"%W%" */
package eu.heliovo.queryservice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

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
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception: "+ex);
			ex.printStackTrace();
		}
	}
	
	
	private void switchAppenders(String url) {
	      Properties props = new Properties();
	      url=url+InstanceHolders.getInstance().getProperty("hsqldb.database.path") +"/HelioDB/testdb;hsqldb.default_table_type=cached;shutdown=true";
	      System.out.println(" ------------> : URL of LOGS Table : ---------->"+url);
	      try {
	           InputStream configStream = getClass().getResourceAsStream("/log4j.properties");
	           props.load(configStream);
	           configStream.close();
	      } catch(IOException e) {
	          System.out.println("Error: Cannot laod configuration file ");
	      }
	      props.setProperty("log4j.appender.DB.URL",url);
	      LogManager.resetConfiguration();
	      PropertyConfigurator.configure(props);
	     }

	

}

