package eu.heliovo.queryservice.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.FileUtils;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;
import eu.heliovo.queryservice.common.util.LongRunningQueryIdHolders;

/**
 * Servlet implementation class HelioQueryService
 */
public class ResultQueryService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected final  Logger logger = Logger.getLogger(this.getClass());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResultQueryService() {
        super();        
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter pw = response.getWriter(); 
		try{
			
			//Creating a xml file 
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
	        //creating a new instance of a DOM to build a DOM tree.
	        Document doc = docBuilder.newDocument();
			
			String sID=request.getParameter("ID");
			String sMode=request.getParameter("MODE");
			if(sMode!=null && sMode.equalsIgnoreCase("phase")){
				String sStatus=LongRunningQueryIdHolders.getInstance().getProperty(sID);
				if(sStatus==null || sStatus.trim().equals(""))
				  sStatus=HsqlDbUtils.getInstance().getStatusFromHsqlDB(sID);
				String xmlString=createXmlTree(doc,sID,sStatus);
				System.out.println(" : XML String : "+xmlString);
				pw.write(xmlString);
			}else if(sMode!=null && sMode.equalsIgnoreCase("result")){
				String sStatus=LongRunningQueryIdHolders.getInstance().getProperty(sID);
				if(sStatus==null || sStatus.trim().equals(""))
				  sStatus=HsqlDbUtils.getInstance().getStatusFromHsqlDB(sID);
				String contextPath=CommonUtils.getUrl(request,sID);
				
				String xmlString=createXmlTree(doc,sID,sStatus,contextPath);
				System.out.println(" : XML String : "+xmlString);
				pw.write(xmlString);
			}else if(sMode!=null && sMode.equalsIgnoreCase("file")){
				String sUrl=HsqlDbUtils.getInstance().getUrlFromHsqlDB(sID);
				File xmlfile = new File(sUrl);
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document document = builder.parse(xmlfile);
		        String fileData=FileUtils.readDataFromFile(document);
		        logger.info(" : File data :   "+fileData);
				pw.write(fileData);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(" : Exception occured while creating the file :  "+e.getMessage());
		}		
		finally
		{
		
			if(pw!=null){
				pw.close();
				pw=null;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	
	 public String createXmlTree(Document doc,String randomUUIDString) throws Exception {
		 
		 return createXmlTree(doc,randomUUIDString,null,null);
	 }
	 
	 public String createXmlTree(Document doc,String randomUUIDString,String Status) throws Exception {
		 
		 return createXmlTree(doc,randomUUIDString,Status,null);
	 }
	
	/*
	 * 
	 */
	 public String createXmlTree(Document doc,String randomUUIDString,String Status,String sUrl) throws Exception {
	        //This method creates an element node
	        Element root = doc.createElement("SERVICE");
	        //adding a node after the last child node of the specified node.
	        doc.appendChild(root);

	        Element child = doc.createElement("RESPONSE");
	        root.appendChild(child);

	        Element child1 = doc.createElement("ID");
	        child.appendChild(child1);

	        Text text = doc.createTextNode(randomUUIDString);
	        child1.appendChild(text);

	        if(Status!=null){
	        	Element child2 = doc.createElement("STATUS");
	 	        child.appendChild(child2);

	 	        Text text1 = doc.createTextNode(Status);
	 	        child2.appendChild(text1);
	         }
	        
	        if(sUrl!=null){
	        	Element childRt = doc.createElement("URIS");
	        	child.appendChild(childRt);
	        	Element chilE = doc.createElement("URL");
	            chilE.setAttribute("name", "query");
	            Text text12 = doc.createTextNode(sUrl);
	            chilE.appendChild(text12);
	            childRt.appendChild(chilE);
	        }
	        
	        //TransformerFactory instance is used to create Transformer objects. 
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Transformer transformer = factory.newTransformer();
	       
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	        // create string from xml tree
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        DOMSource source = new DOMSource(doc);
	        transformer.transform(source, result);
	        String xmlString = sw.toString();

	       return xmlString;
	      
	    }
	 
	 

	 
	 

}
