package eu.heliovo.queryservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Embedded;
import org.apache.catalina.realm.MemoryRealm;
import org.apache.log4j.Logger;
import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.transfer.FileResultTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.InstanceHolders;
import eu.heliovo.queryservice.server.util.QueryThreadAnalizer;

@SuppressWarnings("unused")
public class JunitWebserveTest {
		
	   protected final  Logger logger = Logger.getLogger(this.getClass());
	   protected static Endpoint ep;
	   protected static String address;
	   protected static URL wsdlURL;
	   protected static QName serviceName;
	   protected static QName portName;
	   protected static Embedded server;  
	  
	   /*
	   @BeforeClass
	   public static void setUp() throws Exception {
	      address = "http://localhost:9090/helio-queryservice/HelioService";
	      wsdlURL = new URL(address + "?wsdl");
	      serviceName = new QName("http://helio-vo.eu/xml/QueryService/v0.1","HelioQueryServiceService");
	      portName = new QName("http://helio-vo.eu/xml/QueryService/v0.1", "HelioQueryServicePort");
	      server = new Embedded();
	      //server.setRealm(new MemoryRealm());  // if using the tomcat-users.xml file.
	      //Creating embedded tomcat.
	      Engine baseEngine = server.createEngine();
	      baseEngine.setDefaultHost("helio-queryservice");
	    
	      File fi = new File("target");
	      String helioDbPath=fi.getAbsolutePath();
		  System.out.println(" : helio db file path  : "+helioDbPath); 
	      
	      Host baseHost = server.createHost("helio-queryservice",helioDbPath);
	      baseEngine.addChild(baseHost);
	      Context appCtx = server.createContext("/helio-queryservice", "helio-queryservice");
	      baseHost.addChild(appCtx);      
	      server.addEngine(baseEngine);
	      Connector httpConnector = server.createConnector((java.net.InetAddress) null, 9090, false);
	      server.addConnector(httpConnector);   
	      
          server.start();
	          
	   }
   
	   @AfterClass
	   public static void tearDown() throws Exception {
		  if (server != null) {
	           server.stop();
	           server = null;
	      }
	   }
	   
	   @Test
	   public void testWeService() throws Exception {
					 
			  logger.info(" : Start Testing Junit test case method testWebService()  : ");
			  Service jaxwsService = Service.create(wsdlURL, serviceName);
			  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
		      factory.setNamespaceAware(true);
		      DocumentBuilder builder = factory.newDocumentBuilder();
		      InputStream is = getClass().getClassLoader().getResourceAsStream("reqSOAPMessage.xml");
		      logger.info(" : Input details are loaded into input stream : ");
		      Document newDoc = builder.parse(is);
		      DOMSource request = new DOMSource(newDoc);
		      // Both CXF and Metro.
		      Dispatch disp = jaxwsService.createDispatch(portName,Source.class, Service.Mode.PAYLOAD);
		      Source result = (Source) disp.invoke(request);
		      DOMResult domResponse = new DOMResult();
		      Transformer trans = TransformerFactory.newInstance().newTransformer();
		      trans.transform(result, domResponse);
		      logger.info(" : Checking if response is not null : ");
		      assertNotNull(domResponse);
		      logger.info(" : Done : ");
		      logger.info(" : Response from the webservice  : "+domResponse.getNode().getFirstChild().getTextContent().trim());
  
		      System.out.println(" Testing results ");
	   }	   
	  */
	
	   @Test
	   public void testFullQueryQname() throws Exception {
		   
			PipedReader  pr = new PipedReader();
			PipedWriter pw = new PipedWriter(pr);
			
			try{
				 System.out.println("Testing Full Query Service ....");
				 ClassLoader loader = this.getClass().getClassLoader();
				 String helioDbPath=loader.getResource("test.txt").getFile();
				 System.out.println(" : helio db file path  : "+helioDbPath);
				 InstanceHolders.getInstance().setProperty("hsqldb.database.path",helioDbPath.replace("test.txt", ""));
				 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			     factory.setNamespaceAware(true);
				 DocumentBuilder builder = factory.newDocumentBuilder();  
				 //
				 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
				 String[]  arrStartTime=new String[1];
				 String[]  arrEndTime=new String[1];
				//Start time
				 arrStartTime[0]="1890-10-20T20:30:56";
				 arrEndTime[0]="2009-10-20T20:30:56";
				 // Setting data
				 comCriteriaTO.setStartDateTimeList(arrStartTime);
				 comCriteriaTO.setEndDateTimeList(arrEndTime);	
				 String[]  arr=new String[1];
				 arr[0]="helio";
				 comCriteriaTO.setListTableName(arr);
				 comCriteriaTO.setStatus("WebService");
				 comCriteriaTO.setPrintWriter(pw);
				 System.out.println("Creating a VOTable ....");
				 new QueryThreadAnalizer(comCriteriaTO).start();
				 Thread.sleep(20000);
				 //Print reader
				 System.out.println("Printing VOTable ....");
				 printPrintReader(pr);
				 
			 }catch(Exception e){
				  System.out.println(" Exception occured in testQueryQname : "+e);
			 }
			 
			 finally{
				 
				 if(pw!=null){
					pw.close(); 
				 }
			     //Setting hsqldb.database.path to null.
				 InstanceHolders.getInstance().setProperty("hsqldb.database.path",null);
			 }
			 
	   }
	   
	   @Test
	   public void testMaxRecordsQueryQname() throws Exception {
		   
			PipedReader  pr = new PipedReader();
			PipedWriter pw = new PipedWriter(pr);
			
			try{
				 System.out.println("Testing Full Query Service ....");
				 ClassLoader loader = this.getClass().getClassLoader();
				 String helioDbPath=loader.getResource("test.txt").getFile();
				 System.out.println(" : helio db file path  : "+helioDbPath);
				 InstanceHolders.getInstance().setProperty("hsqldb.database.path",helioDbPath.replace("test.txt", ""));
				 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			     factory.setNamespaceAware(true);
				 DocumentBuilder builder = factory.newDocumentBuilder();  
				 //
				 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
				 String[]  arrStartTime=new String[1];
				 String[]  arrEndTime=new String[1];
				 //Start time
				 arrStartTime[0]="1890-10-20T20:30:56";
				 arrEndTime[0]="2009-10-20T20:30:56";
				 // Setting data
				 comCriteriaTO.setStartDateTimeList(arrStartTime);
				 comCriteriaTO.setEndDateTimeList(arrEndTime);	
				 String[]  arr=new String[1];
				 arr[0]="helio";
				 comCriteriaTO.setListTableName(arr);
				 comCriteriaTO.setStatus("WebService");
				 comCriteriaTO.setNoOfRows("1");
				 comCriteriaTO.setPrintWriter(pw);
				 System.out.println("Creating a VOTable ....");
				 new QueryThreadAnalizer(comCriteriaTO).start();
				 Thread.sleep(20000);
				 //Print reader
				 System.out.println("Printing VOTable ....");
				 printPrintReader(pr);
				 
			 }catch(Exception e){
				  System.out.println(" Exception occured in testQueryQname : "+e);
			 }
			 
			 finally{
				 
				 if(pw!=null){
					pw.close(); 
				 }
			     //Setting hsqldb.database.path to null.
				 InstanceHolders.getInstance().setProperty("hsqldb.database.path",null);
			 }
			 
	   }
	   
	   @Test
	   public void testStartIndexAndMaxRecordsQueryQname() throws Exception {
		   
			PipedReader  pr = new PipedReader();
			PipedWriter pw = new PipedWriter(pr);
			
			try{
				 System.out.println("Testing Full Query Service ....");
				 ClassLoader loader = this.getClass().getClassLoader();
				 String helioDbPath=loader.getResource("test.txt").getFile();
				 System.out.println(" : helio db file path  : "+helioDbPath);
				 InstanceHolders.getInstance().setProperty("hsqldb.database.path",helioDbPath.replace("test.txt", ""));
				 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			     factory.setNamespaceAware(true);
				 DocumentBuilder builder = factory.newDocumentBuilder();  
				 //
				 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
				 
				 String[]  arrStartTime=new String[1];
				 String[]  arrEndTime=new String[1];
				 //Start time
				 arrStartTime[0]="1890-10-20T20:30:56";
				 arrEndTime[0]="2009-10-20T20:30:56";
				 // Setting data
				 comCriteriaTO.setStartDateTimeList(arrStartTime);
				 comCriteriaTO.setEndDateTimeList(arrEndTime);
				 String[]  arr=new String[1];
				 arr[0]="helio";
				 comCriteriaTO.setListTableName(arr);
				 comCriteriaTO.setStatus("WebService");
				 comCriteriaTO.setNoOfRows("2");
				 comCriteriaTO.setStartRow("1");
				 comCriteriaTO.setPrintWriter(pw);
				 System.out.println("Creating a VOTable ....");
				 new QueryThreadAnalizer(comCriteriaTO).start();
				 Thread.sleep(20000);
				 //Print reader
				 System.out.println("Printing VOTable ....");
				 printPrintReader(pr);
				 
			 }catch(Exception e){
				  System.out.println(" Exception occured in testQueryQname : "+e);
			 }
			 
			 finally{
				 
				 if(pw!=null){
					pw.close(); 
				 }
			     //Setting hsqldb.database.path to null.
				 InstanceHolders.getInstance().setProperty("hsqldb.database.path",null);
			 }
			 
	   }
	   
	   @Test
	   public void testLongRunningQueryQname() throws Exception {
		   
			PipedReader  pr = new PipedReader();
			PipedWriter pw = new PipedWriter(pr);
			BufferedWriter out = new BufferedWriter(pw);
			try{
				 System.out.println("Testing Long Running Query Service ....");
				 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			     factory.setNamespaceAware(true);
				 DocumentBuilder builder = factory.newDocumentBuilder();  
				 //Passing values to Trasfer Object.
				 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
				 String[]  arrStartTime=new String[1];
				 String[]  arrEndTime=new String[1];
				 //Start time
				 arrStartTime[0]="1890-10-20T20:30:56";
				 arrEndTime[0]="2009-10-20T20:30:56";
				 // Setting data
				 comCriteriaTO.setStartDateTimeList(arrStartTime);
				 comCriteriaTO.setEndDateTimeList(arrEndTime);
				 String[]  arr=new String[1];
				 arr[0]="helio";
				 comCriteriaTO.setListTableName(arr);
				 comCriteriaTO.setStatus("WebService");
				 comCriteriaTO.setLongRunningQueryStatus("LongRunning");
				 comCriteriaTO.setPrintWriter(out);
				 // Creating UUID
				 UUID uuid = UUID.randomUUID();
				 String randomUUIDString = uuid.toString();
				 //file TO
				 FileResultTO fileTO=new FileResultTO();
				 fileTO.setRandomUUIDString(randomUUIDString);
				 String xmlString=CommonUtils.createXmlForWebService(fileTO);
				 System.out.println(" : XML String : "+xmlString);
				
				 //Setting piped reader 
				 comCriteriaTO.setLongRunningPrintWriter(pw);
				 //Set data to print writer.
				 comCriteriaTO.setDataXml(xmlString);
				 System.out.println("Creating response XML ....");
				 //Thread created to load data into response.
				 CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				 commonNameDao.generatelongRunningQueryXML(comCriteriaTO);
				 System.out.println("Response XML done ....");
				 //Print print reader.
				 System.out.println("Printing response ....");
				 printPrintReader(pr);
				 
			 }catch(Exception e){
				  System.out.println(" Exception occured in testQueryQname : "+e);
			 }
			 
			 finally{
				 
				 if(pw!=null){
					pw.close(); 
				 }
			 }
			 
	   }
	     
	   @Test
	   public void testLongRunningQueryAssertEquals() throws Exception {
		   
			PipedReader  pr = new PipedReader();
			PipedWriter pw = new PipedWriter(pr);
			BufferedWriter out = new BufferedWriter(pw);
			try{
				 System.out.println("Testing Long Running Query Service ....");
				 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			     factory.setNamespaceAware(true);
				 DocumentBuilder builder = factory.newDocumentBuilder();  
				 //Passing values to Trasfer Object.
				 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
				 String[]  arrStartTime=new String[1];
				 String[]  arrEndTime=new String[1];
				 //Start time
				 arrStartTime[0]="1890-10-20T20:30:56";
				 arrEndTime[0]="2009-10-20T20:30:56";
				 // Setting data
				 comCriteriaTO.setStartDateTimeList(arrStartTime);
				 comCriteriaTO.setEndDateTimeList(arrEndTime);	
				 String[]  arr=new String[1];
				 arr[0]="helio";
				 comCriteriaTO.setListTableName(arr);
				 comCriteriaTO.setStatus("WebService");
				 comCriteriaTO.setLongRunningQueryStatus("LongRunning");
				 comCriteriaTO.setPrintWriter(out);
				 // Creating UUID
				 UUID uuid = UUID.randomUUID();
				 String randomUUIDString = uuid.toString();
				 //file TO
				 FileResultTO fileTO=new FileResultTO();
				 fileTO.setRandomUUIDString(randomUUIDString);
				 String xmlString=CommonUtils.createXmlForWebService(fileTO);
				 System.out.println(" : XML String : "+xmlString);
				
				 //Setting piped reader 
				 comCriteriaTO.setLongRunningPrintWriter(pw);
				 //Set data to print writer.
				 comCriteriaTO.setDataXml(xmlString);
				 System.out.println("Creating response XML ....");
				 //Thread created to load data into response.
				 CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				 commonNameDao.generatelongRunningQueryXML(comCriteriaTO);
				 System.out.println("Response XML done ....");
				 String sComStr="<helio:resultResponse xmlns:helio=\"http://helio-vo.eu/xml/LongQueryService/v0.1\">"
				 +"<ResultInfo><ID>"+randomUUIDString+"</ID></ResultInfo></helio:resultResponse>";
				 //Print print reader.
				 System.out.println("Checking for assert equals...");
				 assertEquals(sComStr,printPrintReaderStringRes(pr));
				 System.out.println("Success....!!!");
			 }catch(Exception e){
				  System.out.println(" Exception occured in testQueryQname : "+e);
			 }
			 
			 finally{
				 
				 if(pw!=null){
					pw.close(); 
				 }
			 }
			 
	   }
	      
	   private void printPrintReader(PipedReader  reader) throws IOException
	   {
		   try{
			   while (reader.ready())
	            {
				   Thread.sleep(10);
	                System.out.print((char)reader.read());
	            }
		    }catch(Exception e){ 
			   System.out.println(" Exception in printPrintReader : "+e);
			   e.printStackTrace();
		    }
		   
		   finally{
			   
			   if(reader!=null){
				   reader.close();
			   }
		   }
	   }
	   
	   private String printPrintReaderStringRes(PipedReader  reader) throws IOException
	   {
		   try{
			   StringBuffer sb=new StringBuffer(); 
			   while (reader.ready())
	            {
				   Thread.sleep(10);
				   sb.append((char)reader.read());
	            }
			   
			   return sb.toString();
		    }catch(Exception e){ 
			   System.out.println(" Exception in printPrintReader : "+e);
			   e.printStackTrace();
		    }
		   
		   finally{
			   
			   if(reader!=null){
				   reader.close();
			   }
		   }
		return "";
	   }
	   
	  	    
 }
