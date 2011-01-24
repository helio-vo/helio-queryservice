package eu.heliovo.queryservice.server.query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode; 
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.apache.log4j.Logger; 
import org.w3c.dom.Element;
import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.transfer.FileResultTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.FileUtils;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;
import eu.heliovo.queryservice.common.util.InstanceHolders;
import eu.heliovo.queryservice.common.util.LongRunningQueryIdHolders;
import eu.heliovo.queryservice.common.util.RunService;
import eu.heliovo.queryservice.server.util.QueryThreadAnalizer;

/**
 * Class: SoapDispatcher
 * Description: The dispatcher handles all soap requests and responses for a Query.  Called via the
 * SoapServlet. SoapRequests (Bodies) are placed into a DOM and by analyzing the uri 
 * determine the correct query service for the correct contract.  Responses are Stream based (NOT DOM) into an 
 * XMLStreamReader with the help of PipedStreams.
 *
 */
@WebServiceProvider(targetNamespace="",
	      serviceName="",
	      portName="")
	      
@ServiceMode(value=javax.xml.ws.Service.Mode.PAYLOAD)

public class SoapDispatcher implements Provider<Source> {
	@javax.annotation.Resource(type=Object.class)
	 protected WebServiceContext wsContext; 
	
  protected final  Logger logger = Logger.getLogger(this.getClass());

  /**
   * Method: Constructor
   * Description: Setup any initiations, mainly hashtable of uri to query 
   * interface versions.
   */
  public SoapDispatcher() {
	 
  }

  /**
   * Method: invoke
   * Description:For all soap requests and 
   * responses.Using Metro.
   * @param request - Source that is used to extract the soap request.
   * @return Source - response StreamSource that contains the 
   * soap response populated by InputStream (PipedInputStream) 
   */
  
	@Override
	public Source invoke(Source request) {
		// TODO Auto-generated method stub
		PipedReader pr=null;
		PipedWriter pw=null;
		CommonCriteriaTO comCriteriaTO=null;
		StreamSource responseReader = null;	
		File file=null;
		BufferedWriter bw =null;
		String saveFilePath=null;
		String saveTo =null;
		//Creating UUID and generating unique ID.
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		FileResultTO fileTO=new FileResultTO();
		try {
			
			 Element inputDoc=toDocument(request);
			 MessageContext mc = wsContext.getMessageContext();
			 HttpServletRequest req = (HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST);
			 
		     String interfaceName = inputDoc.getLocalName().intern();
		     //since this service will be used a lot, supposedly .intern() can be quicker
	   	     //each method should return a XMLStreamReader that is streamed back to the client.
		     comCriteriaTO=new CommonCriteriaTO(); 
			 pr = new PipedReader();
			 pw = new PipedWriter(pr);	 
			 if(req.getContextPath()!=null){
				 comCriteriaTO.setContextPath(req.getContextPath().replace("-", "_").replace("/", ""));
			 }
			 //Indicator to define VOTABLE for Web Service request
			 comCriteriaTO.setStatus("WebService");
		
			 // This is common for Time. interface.
	    	 //Setting for START TIME parameter.
			 if(inputDoc.getElementsByTagNameNS("*","STARTTIME").getLength()>0 && inputDoc.getElementsByTagNameNS("*","STARTTIME").item(0).getFirstChild()!=null){
				 NodeList nodeList=inputDoc.getElementsByTagNameNS("*","STARTTIME");
    			 String[] startTime=new String[nodeList.getLength()];
    			 //List Name
		    	 for(int i=0;i<nodeList.getLength();i++){
		    		 startTime[i]=nodeList.item(i).getFirstChild().getNodeValue();
		    	 }

	    		 comCriteriaTO.setStartDateTimeList(startTime);
			 }
			 
    		 //Setting for TIME parameter.
    		 if(inputDoc.getElementsByTagNameNS("*","ENDTIME").getLength()>0 && inputDoc.getElementsByTagNameNS("*","ENDTIME").item(0).getFirstChild()!=null){
    			 NodeList nodeList=inputDoc.getElementsByTagNameNS("*","ENDTIME");
    			 String[] endTime=new String[nodeList.getLength()];
    			 //List Name
		    	 for(int i=0;i<nodeList.getLength();i++){
		    		 endTime[i]=nodeList.item(i).getFirstChild().getNodeValue();
		    	 }
				 comCriteriaTO.setEndDateTimeList(endTime);	
    		 }
    		 
	    	 //Setting for ListName parameter.
    		 if(inputDoc.getElementsByTagNameNS("*","FROM").getLength()>0){
    			 //Node list
    			 NodeList nodeList=inputDoc.getElementsByTagNameNS("*","FROM");
    			 String[] listName=new String[nodeList.getLength()];
    			 //List Name
		    	 for(int i=0;i<nodeList.getLength();i++){
		    		 listName[i]=nodeList.item(i).getFirstChild().getNodeValue();
		    	 }
		    	 comCriteriaTO.setListTableName(listName);
    		 }	
    		 //Setting value for independent query.
    		 comCriteriaTO.setAllStartDate(CommonUtils.arrayToString(comCriteriaTO.getStartDateTimeList(), ","));
 		     comCriteriaTO.setAllEndDate(CommonUtils.arrayToString(comCriteriaTO.getEndDateTimeList(), ","));
 		     comCriteriaTO.setContextUrl(CommonUtils.getUrl(req));
	    	 
    		//Setting for Start Row parameter.
			 if(inputDoc.getElementsByTagNameNS("*","STARTINDEX").getLength()>0 && inputDoc.getElementsByTagNameNS("*","STARTINDEX").item(0).getFirstChild()!=null){
				 String startRow = inputDoc.getElementsByTagNameNS("*","STARTINDEX").item(0).getFirstChild().getNodeValue();
				 comCriteriaTO.setStartRow(startRow);
			 }
			 
			//Setting for No Of Rows parameter.
			 if(inputDoc.getElementsByTagNameNS("*","MAXRECORDS").getLength()>0 && inputDoc.getElementsByTagNameNS("*","MAXRECORDS").item(0).getFirstChild()!=null){
				 String noOfRows = inputDoc.getElementsByTagNameNS("*","MAXRECORDS").item(0).getFirstChild().getNodeValue();
				 comCriteriaTO.setNoOfRows(noOfRows);
			 }
			 
			//Setting for Join query status.
			 if(inputDoc.getElementsByTagNameNS("*","JOIN").getLength()>0 && inputDoc.getElementsByTagNameNS("*","JOIN").item(0).getFirstChild()!=null){
				 String join = inputDoc.getElementsByTagNameNS("*","JOIN").item(0).getFirstChild().getNodeValue();
				 if(join!=null && !join.trim().equals(""))
					 comCriteriaTO.setJoin(join);
			 }
	    	 
			 //Full query interface
		     if(interfaceName == "Query".intern() || interfaceName == "LongQuery".intern()) {
		    	 //Setting for Instrument parameter.
				 if(inputDoc.getElementsByTagNameNS("*","INSTRUMENT").getLength()>0 && inputDoc.getElementsByTagNameNS("*","INSTRUMENT").item(0).getFirstChild()!=null){
					 String instruments = inputDoc.getElementsByTagNameNS("*","INSTRUMENT").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setInstruments(instruments);
				 }
				//Setting for WHERE parameter.
				 if(inputDoc.getElementsByTagNameNS("*","WHERE").getLength()>0 && inputDoc.getElementsByTagNameNS("*","WHERE").item(0).getFirstChild()!=null){
					 String whereClause = inputDoc.getElementsByTagNameNS("*","WHERE").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setWhereClause(whereClause);
				 }
				 //Coordinates interface
	    	 } else if(interfaceName == "Coordinates".intern()) {
	    		 //Setting for POS( RA & DEC) parameter.
				 if(inputDoc.getElementsByTagNameNS("*","POS").getLength()>0 && inputDoc.getElementsByTagNameNS("*","POS").item(0).getFirstChild()!=null){
					 String pos = inputDoc.getElementsByTagNameNS("*","POS").item(0).getFirstChild().getNodeValue();
					 if(pos!=null && !pos.equals("")){
						 String[] arrPos=pos.split(",");
						 if(arrPos.length>0)
							 comCriteriaTO.setPosRa(arrPos[0]);
						 if(arrPos.length>1)
							 comCriteriaTO.setPosDec(arrPos[1]);
						 if(arrPos.length>2)
							 comCriteriaTO.setPosRef(arrPos[2]);
					 }
				 }
				 if(inputDoc.getElementsByTagNameNS("*","REGION").getLength()>0 && inputDoc.getElementsByTagNameNS("*","REGION").item(0).getFirstChild()!=null){
					 String sRegion = inputDoc.getElementsByTagNameNS("*","REGION").item(0).getFirstChild().getNodeValue();
					 //Getting parse region.
					 Map<String,String> map=CommonUtils.parseRegionParameter(sRegion);
					 //Region.
					 comCriteriaTO.setsRegion(map.get("region"));
					 //Region values.
					  comCriteriaTO.setsRegionValues(map.get("regionvalues"));
				 }
				//Setting for SIZE parameter.
				 if(inputDoc.getElementsByTagNameNS("*","SIZE").getLength()>0 && inputDoc.getElementsByTagNameNS("*","SIZE").item(0).getFirstChild()!=null){
					 String size = inputDoc.getElementsByTagNameNS("*","SIZE").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setSize(size);
				 }
				 //Long running query interface.
	    	 }else if(interfaceName == "LongTimeQuery".intern() || interfaceName == "LongQuery".intern()){
						 
	    		 //Setting for No Of Rows parameter.
				 if(inputDoc.getElementsByTagNameNS("*","SAVETO").getLength()>0 && inputDoc.getElementsByTagNameNS("*","SAVETO").item(0).getFirstChild()!=null){
					 saveTo = inputDoc.getElementsByTagNameNS("*","SAVETO").item(0).getFirstChild().getNodeValue();
				 } 
				 // Save To file.
				 if(saveTo==null || saveTo==""){
				    saveTo= InstanceHolders.getInstance().getProperty("hsqldb.database.path")+"/files";
				    File f = new File(saveTo);
				    //Checking if directry present; if not create one.
				    if(!f.exists())
				    	f.mkdir();		    
				 }
				 logger.info(" : save to file location :  "+saveTo);
				 //passing save to value to common TO.	
				 comCriteriaTO.setSaveto(saveTo);
				 comCriteriaTO.setLongRunningQueryStatus("LongRunning");
				 //
				 fileTO.setRandomUUIDString(randomUUIDString);

				 String xmlString=CommonUtils.createXmlForWebService(fileTO);
				 System.out.println(" : XML String : "+xmlString);
				 
				 //Setting piped reader 
				 comCriteriaTO.setLongRunningPrintWriter(pw);
				 //Set data to print writer.
				 comCriteriaTO.setDataXml(xmlString);
				 //Thread created to load data into response.
				 CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				 commonNameDao.generatelongRunningQueryXML(comCriteriaTO);	
				 logger.info(" : Done VOTABLE : ");	
				 if(saveTo!=null && saveTo.startsWith("http")){
				 //Save file to http.
				 }else if(saveTo!=null && saveTo.contains("ftp")){
				    FileUtils.saveFileToFtp(saveTo,"votable_"+randomUUIDString+".xml");	    			    	
				 }else{
				 //Save the file to local system.
				    saveFilePath=saveTo+"/votable_"+randomUUIDString+".xml";
				    file = new File(saveFilePath);
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
					comCriteriaTO.setPrintWriter(bw);
				 }
				
				 //Running the service in back round
				 RunService oRunReport= new RunService(comCriteriaTO,randomUUIDString);
				 Thread th = new Thread(oRunReport);
				 th.start();
				 //Long running query status of completion.
		 }else if(interfaceName == "GetStatus".intern()){
			 String sID =null;
			 if(inputDoc.getElementsByTagNameNS("*","ID").getLength()>0 && inputDoc.getElementsByTagNameNS("*","ID").item(0).getFirstChild()!=null){
	    		 sID = inputDoc.getElementsByTagNameNS("*","ID").item(0).getFirstChild().getNodeValue();
			 }
			 
			 String sStatus=LongRunningQueryIdHolders.getInstance().getProperty(sID);
				if(sStatus==null || sStatus.trim().equals(""))
				  sStatus=HsqlDbUtils.getInstance().getStatusFromHsqlDB(sID);
				//Setting file TO
				fileTO.setRandomUUIDString(sID);
				fileTO.setStatus(sStatus);
				
				String xmlString=CommonUtils.createXmlForWebService(fileTO);
				System.out.println(" : XML String : "+xmlString);	
				//Setting piped reader 
				 comCriteriaTO.setLongRunningPrintWriter(pw);
				//Set data to print writer.
				comCriteriaTO.setDataXml(xmlString);
				//Thread created to load data into response.
				CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				commonNameDao.generatelongRunningQueryXML(comCriteriaTO);	
				//Long running query file location path result.
		 }else if(interfaceName == "GetResults".intern()){
			 String sID =null;
			 if(inputDoc.getElementsByTagNameNS("*","ID").getLength()>0 && inputDoc.getElementsByTagNameNS("*","ID").item(0).getFirstChild()!=null){
	    		 sID = inputDoc.getElementsByTagNameNS("*","ID").item(0).getFirstChild().getNodeValue();
			 }
			 
			 String sStatus=LongRunningQueryIdHolders.getInstance().getProperty(sID);
				if(sStatus==null || sStatus.trim().equals(""))
				  sStatus=HsqlDbUtils.getInstance().getStatusFromHsqlDB(sID);
				String contextPath=CommonUtils.getUrl(req,sID);
				//Setting file TO
				fileTO.setRandomUUIDString(sID);
				fileTO.setStatus(sStatus);
				fileTO.setsUrl(contextPath);
				String xmlString=CommonUtils.createXmlForWebService(fileTO);
				System.out.println(" : XML String : "+xmlString);	
				//Setting piped reader 
				comCriteriaTO.setLongRunningPrintWriter(pw);
				//Set data to print writer.
				comCriteriaTO.setDataXml(xmlString);
				//Thread created to load data into response.
				CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				commonNameDao.generatelongRunningQueryXML(comCriteriaTO);	
				//Long running query response result.
		 }else if(interfaceName == "GetResponseFile".intern()){
			 //Presently not in use.
			 StringBuilder fileData=null;
			 String sID =null;
			 if(inputDoc.getElementsByTagNameNS("*","ID").getLength()>0 && inputDoc.getElementsByTagNameNS("*","ID").item(0).getFirstChild()!=null){
	    		 sID = inputDoc.getElementsByTagNameNS("*","ID").item(0).getFirstChild().getNodeValue();
			 }
			 String sUrl=HsqlDbUtils.getInstance().getUrlFromHsqlDB(sID);
				if(sUrl.contains("ftp")){
					String ftpUrl=HsqlDbUtils.getInstance().getUrlFromHsqlDB(sID);
					fileData=FileUtils.getFileDataFromFtp(ftpUrl);
				}else if(sUrl.startsWith("http")) {
				
				}
				else{
					File xmlfile = new File(sUrl);
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder builder = factory.newDocumentBuilder();
			        Document document = builder.parse(xmlfile);
			        fileData=FileUtils.readDataFromFile(document);
				}
				
				pw.write(fileData.toString());
		 }
		 
		 if(interfaceName == "Query".intern() || interfaceName == "Coordinates".intern() || interfaceName == "TimeQuery".intern()){ 
			 //Setting piped reader 
			 comCriteriaTO.setPrintWriter(pw);
	    	 //Thread created to load data into PipeReader.
			 new QueryThreadAnalizer(comCriteriaTO).start();				
			 logger.info(" : Done VOTABLE : ");				
		 }
			
		 responseReader= new StreamSource(pr); 
			 
		 logger.info(" : Returning response reader soap output : ");
		 return responseReader; 
		}catch (Exception e) {
			e.printStackTrace();
			logger.fatal(" Exception occured while creating soap output : ",e);
	    }
		return null;
	}
	
	
	
	@SuppressWarnings("unused")
	private  StreamSource getInputSourceByFile(String filePath) throws IOException 
	{
		InputStream is = null;
		try {
			File path=new File(filePath);
			is = new FileInputStream(path);
			return new StreamSource(is, path.getName());
		} catch (IOException e) {
			if (is != null) {
				try {
					is.close();
					} catch (Exception x) { /** ignore */ }
			}
			throw e;
		}
	}

	
	/*
	 * Method used to convert Source to dom object.
	 */
	private synchronized Element toDocument(Source src) throws TransformerException {
        DOMResult result = new DOMResult();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        try {
            transformer.transform(src, result);
        } catch (TransformerException te) {
            throw new TransformerException("Error while applying template", te);
        }
        Element root = ((Document)result.getNode()).getDocumentElement();
       return root;
    }

}