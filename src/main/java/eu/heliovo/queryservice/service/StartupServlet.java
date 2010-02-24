/* #ident	"%W%" */
package com.org.helio.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.org.helio.common.util.InstanceHolders;


public class StartupServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {

		try{
			//Setting database connectivity while starting server.
			InstanceHolders.getInstance().setProperty("jdbc.driver","org.apache.derby.jdbc.EmbeddedDriver");
			InstanceHolders.getInstance().setProperty("jdbc.url","jdbc:derby:helio;create=true");
			InstanceHolders.getInstance().setProperty("jdbc.user","helio");
			InstanceHolders.getInstance().setProperty("jdbc.password","helio");
			System.out.println("StartUp Servlet");
			
		}
		catch(Exception ex)
		{
			System.out.println("Exception: "+ex);
			ex.printStackTrace();
		}
	}
}
