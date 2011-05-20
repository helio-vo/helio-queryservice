package eu.heliovo.queryservice.servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.heliovo.queryservice.common.util.ConfigurationProfiler;

/**
 * Servlet implementation class VosiServlet
 */
public abstract class VosiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private String url=null;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VosiServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		 Writer writer = response.getWriter();

	     response.setContentType("text/xml");
	     response.setCharacterEncoding("UTF-8");
	     //Setting URL string
	     setUrl(request);
	     //Printing out to writer.
	     output(writer);
	}
	
	
	  /**
	   * Writes the VOSI document to the given stream. 
	   */
	  protected abstract void output(Writer writer) throws IOException,ServletException;
	  
	  /**
	   * Reveals the base URI of the web application.
	   *
	   * @return The URI.
	   */
	  protected String getRootUri() {
		
	    String endpoint =ConfigurationProfiler.getInstance().getProperty("query.url");
	    if (!endpoint.endsWith("/")) {
	      endpoint = endpoint + "/";  // Add trailing separator if missing
	    }
	    return endpoint;
	  }
	  
	  /**
	   * Reveals the base URI of the web application.
	   *
	   * @return The URI.
	   */
	  public  String setUrl(HttpServletRequest req) { 
		  
		  String scheme = req.getScheme(); // http 
		  String serverName = req.getServerName(); // hostname.com 
		  int serverPort = req.getServerPort(); // 80 
		  String contextPath = req.getContextPath(); // mywebapp 
		  String servletPath = req.getServletPath(); // /servlet/MyServlet 
		  String pathInfo = req.getPathInfo(); // /a/b;c=123 
		  String queryString = req.getQueryString(); // d=789 // Reconstruct original requesting 
		  url = scheme+"://"+serverName+":"+serverPort+contextPath+"/"; 
		  if (pathInfo != null) {
			  url += pathInfo; 
		  } 
		 		  
		  return url; 
	  }
	  /**
	   * getting URL String.
	   *
	   * @return The URI.
	   */
	  public String getUrl(){
		  return url;
	  }

}
