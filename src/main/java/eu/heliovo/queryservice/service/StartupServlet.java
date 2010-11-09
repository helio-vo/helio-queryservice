/* #ident	"%W%" */
package eu.heliovo.queryservice.service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import eu.heliovo.queryservice.common.util.InstanceHolders;

public class StartupServlet extends HttpServlet {
	 
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try{
			 /*ClassLoader loader = this.getClass().getClassLoader();
			 // check id test.txt available.
			 String sProfileFilePath=loader.getResource("test.txt").getFile();
			 if(sProfileFilePath!=null && !sProfileFilePath.equals("")){
					InstanceHolders.getInstance().setProperty("hsqldb.database.path",sProfileFilePath.replaceAll("/test.txt", ""));
					System.out.println(" : HSQLDB database location : "+sProfileFilePath.replaceAll("/test.txt", ""));
			 }
			*/
			String sProfileFilePath=getServletContext().getRealPath("/");
			if(sProfileFilePath!=null && !sProfileFilePath.equals("")){
				 	sProfileFilePath=sProfileFilePath+ "WEB-INF";
					InstanceHolders.getInstance().setProperty("hsqldb.database.path",sProfileFilePath);
					System.out.println(" : HSQLDB database location : "+sProfileFilePath);
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception: "+ex);
			ex.printStackTrace();
		}
	}
	

}

