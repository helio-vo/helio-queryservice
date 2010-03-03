/* #ident	"%W%" */
package eu.heliovo.queryservice.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import eu.heliovo.queryservice.common.util.InstanceHolders;

public class StartupServlet extends HttpServlet {
	 
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {

		try{
			
			ClassLoader loader = this.getClass().getClassLoader();
			// check id test.txt available.
			String sProfileFilePath=loader.getResource("test.txt").getFile();
			if(sProfileFilePath!=null && !sProfileFilePath.equals("")){
				InstanceHolders.getInstance().setProperty("hsqldb.database.path",sProfileFilePath.replaceAll("/test.txt", ""));
				System.out.println(" : HSQLDB database location : "+sProfileFilePath.replaceAll("/test.txt", ""));
			}
			
		}
		catch(Exception ex)
		{
			System.out.println("Exception: "+ex);
			ex.printStackTrace();
		}
	}
}
