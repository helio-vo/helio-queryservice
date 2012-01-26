package eu.heliovo.queryservice.servlets;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import eu.heliovo.queryservice.common.transfer.FileResultTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.FileUtils;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;
import eu.heliovo.queryservice.common.util.LongRunningQueryIdHolders;

/**
 * Servlet implementation class HelioQueryService
 * @deprecated -- Now is handled by LongRunningQueryService
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
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter pw = response.getWriter(); 
		FileResultTO fileTO=new FileResultTO();
		try{
			
			//Creating a xml file 
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
	        //creating a new instance of a DOM to build a DOM tree.
	        Document doc = docBuilder.newDocument();
	       
			String sID=request.getParameter("ID");
			String sMode=request.getParameter("MODE");
			//Mode to deciede the type of Phase.
			if(sMode!=null && sMode.equalsIgnoreCase("phase")){
				String sStatus=LongRunningQueryIdHolders.getInstance().getProperty(sID);
				if(sStatus==null || sStatus.trim().equals(""))
				  sStatus=HsqlDbUtils.getInstance().getStatusFromHsqlDB(sID);
				//setting file TO.
				fileTO.setRandomUUIDString(sID);
				fileTO.setStatus(sStatus);
				String xmlString=CommonUtils.createXmlForWebService(fileTO);
				System.out.println(" : XML String : "+xmlString);
				pw.write(xmlString);
			}else if(sMode!=null && sMode.equalsIgnoreCase("result")){
				String sStatus=LongRunningQueryIdHolders.getInstance().getProperty(sID);
				if(sStatus==null || sStatus.trim().equals(""))
				  sStatus=HsqlDbUtils.getInstance().getStatusFromHsqlDB(sID);
				String contextPath=CommonUtils.getUrl(request,sID);
				//Setting file TO.
				fileTO.setRandomUUIDString(sID);
				fileTO.setStatus(sStatus);
				fileTO.setsUrl(contextPath);
				String xmlString=CommonUtils.createXmlForWebService(fileTO);
				System.out.println(" : XML String : "+xmlString);
				pw.write(xmlString);
			}else if(sMode!=null && sMode.equalsIgnoreCase("file")){
				StringBuilder fileData=null;
				String sUrl=HsqlDbUtils.getInstance().getUrlFromHsqlDB(sID);
				/*
				if(sUrl == null) {
					System.out.println("surl is null try again after a sleep");
					Thread.sleep((long)200);
					sUrl=HsqlDbUtils.getInstance().getUrlFromHsqlDB(sID);
					if(sUrl == null) {
						System.out.println("2surl is null try again");
					}
				}
				*/
				
				if(sUrl.startsWith("ftp")){
					//fttp
					String ftpUrl=HsqlDbUtils.getInstance().getUrlFromHsqlDB(sID);
					fileData=FileUtils.getFileDataFromFtp(ftpUrl);
				}else{
					File xmlfile = new File(sUrl);
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder builder = factory.newDocumentBuilder();
			        Document document = builder.parse(new InputSource(new FileReader(xmlfile)));
			        fileData=FileUtils.readDataFromFile(document);
				}
		        //logger.info(" : File data :   "+fileData);
				pw.write(fileData.toString());
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
	
	
	
	
	
	 
 }
