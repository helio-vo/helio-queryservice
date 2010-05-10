package eu.heliovo.queryservice.servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.UUID;
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
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.FileUtils;
import eu.heliovo.queryservice.common.util.InstanceHolders;
import eu.heliovo.queryservice.common.util.RunService;

/**
 * Servlet implementation class HelioQueryService
 */
public class LongRunningQueryService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected final  Logger logger = Logger.getLogger(this.getClass());
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LongRunningQueryService() {
        super();        
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
		 PrintWriter pw = response.getWriter(); 
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		    //Setting start time & end time parameter
		    String sStartTime=request.getParameter("STARTTIME");
		    String sEndTime=request.getParameter("ENDTIME");
		   	System.out.println(" sStartTime : "+sStartTime+" sEndTime : "+sEndTime);			
			comCriteriaTO.setStartDateTime(sStartTime);
			comCriteriaTO.setEndDateTime(sEndTime);					
		    //Setting for Instrument parameter.
		    String sInstrument=request.getParameter("INSTRUMENT");
		    comCriteriaTO.setInstruments(sInstrument);
		    //Setting for List Name parameter.
		    String sListName=request.getParameter("FROM");
		    comCriteriaTO.setListName(sListName);
		    //Setting where cluase parameter
		    String whereClause=request.getParameter("WHERE");
		    comCriteriaTO.setWhereClause(whereClause);
		    //Setting start row parameter
		    String startRow=request.getParameter("STARTINDEX");
		    comCriteriaTO.setStartRow(startRow);
		    //Setting no of row parameter
		    String noOfRows=request.getParameter("MAXRECORDS");
		    comCriteriaTO.setNoOfRows(noOfRows);
		    //Setting save to parameter
		    String saveTo=request.getParameter("SAVETO");
		    
		    // Save To file.
			if(saveTo==null || saveTo==""){
			    saveTo= InstanceHolders.getInstance().getProperty("hsqldb.database.path")+"/files";
			    File f = new File(saveTo);
			    //Checking if directry present; if not create one.
			    if(!f.exists())
			    	f.mkdir();
			    //passing save to value to common TO.	
			    comCriteriaTO.setSaveto(saveTo);
			    logger.info(" : save to file location :  "+saveTo);
			 }
			 
		    //Setting POS ( dec and ra ) parameter
		    String pos=request.getParameter("POS");
		    if(pos!=null && !pos.equals("")){
				 String[] arrPos=pos.split(",");
				 if(arrPos.length>0)
					 comCriteriaTO.setAlpha(arrPos[0]);
				 if(arrPos.length>1)
					 comCriteriaTO.setDelta(arrPos[1]);
			 }
		    //Setting SIZE parameter.
		    String size=request.getParameter("SIZE");
		    comCriteriaTO.setSize(size);
		    //Creating UUID and generating unique ID.
		    UUID uuid = UUID.randomUUID();
		    String randomUUIDString = uuid.toString();
		    //Checking for file or ftp.
		    File file=null;
		    BufferedWriter bw =null;
		    String saveFilePath=null;
		    //
		    if(saveTo!=null && saveTo.contains("ftp")){
		    	FileUtils.saveFileToFtp(saveTo,"votable_"+randomUUIDString+".xml");	    			    	
		    }else{
		    	 saveFilePath=saveTo+"/votable_"+randomUUIDString+".xml";
		    	 file = new File(saveFilePath);
			     bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		    }
		   
		    //Setting print writer.
		    comCriteriaTO.setPrintWriter(bw);
		    //Status set to inform long running query
		    comCriteriaTO.setLongRunningQueryStatus("LongRunning");
		    //Running the service in back round
		    RunService oRunReport= new RunService(comCriteriaTO,randomUUIDString);
			Thread th = new Thread(oRunReport);
			th.start();
			
			//Creating a xml file 
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
	        //creating a new instance of a DOM to build a DOM tree.
	        Document doc = docBuilder.newDocument();
	        //
	        String xmlString=CommonUtils.createXmlForWebService(randomUUIDString);
			System.out.println(" : XML String : "+xmlString);
			pw.write(xmlString);
			
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
	
	
	/*
	 * 
	 */
	 public String createXmlTree(Document doc,String randomUUIDString) throws Exception {
	        //This method creates an element node
	        Element root = doc.createElement("Service");
	        //adding a node after the last child node of the specified node.
	        doc.appendChild(root);

	        Element child = doc.createElement("Response");
	        root.appendChild(child);

	        Element child1 = doc.createElement("Id");
	        child.appendChild(child1);

	        Text text = doc.createTextNode(randomUUIDString);
	        child1.appendChild(text);

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
