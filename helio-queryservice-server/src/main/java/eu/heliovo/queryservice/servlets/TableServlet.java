package eu.heliovo.queryservice.servlets;

import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import eu.heliovo.queryservice.common.util.RegistryUtils;

/**
 * Servlet implementation class TableServlet
 */
public class TableServlet extends VosiServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TableServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    /** 
     * Emits an XML document describing the tables.
     */
    protected void output(Writer   writer) throws ServletException {

      try {
        writer.write(
             "<tab:tables\n" +
             "   xmlns:vr='http://www.ivoa.net/xml/VOResource/v1.0'\n" +
             "   xmlns:vs='http://www.ivoa.net/xml/VODataService/v1.0'\n" +
             "   xmlns:tab='urn:astrogrid:schema:TableMetadata'\n" +
             "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n" +
             "   xsi:schemaLocation='" +
             "      http://www.ivoa.net/xml/VOResource/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd\n" +
             "      http://www.ivoa.net/xml/VODataService/v1.0 http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd\n" +
             "      urn:astrogrid:schema:TableMetadata Tables.xsd'>\n");
        writer.write(RegistryUtils.getInstance().getTableDescriptions());
        writer.write("</tab:tables>\n");
      }
      catch (Exception ex) {
    	  ex.printStackTrace();
        throw new ServletException(ex.getMessage());
      }
    }

}
