package eu.heliovo.queryservice.server.query;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.util.STAXUtils;
import org.w3c.dom.Document;

import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.server.util.QueryThreadAnalizer;

/**
 * Class: SoapDispatcher
 * Description: The dispatcher handles all soap requests and responses for a Query.  Called via the
 * SoapServlet. SoapRequests (Bodies) are placed into a DOM and by analyzing the uri 
 * determine the correct query service for the correct contract.  Responses are Stream based (NOT DOM) into an 
 * XMLStreamReader with the help of PipedStreams.
 *
 */
public class SoapDispatcher {

  Hashtable interfaceMappings = null;
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
   * Description: Called by SoapServlet (XFire) for all soap requests and 
   * responses.
   * @param context - MessageContext that is ued to extract the soap request.
   * @return XMLStreamReader - response XMLStreamReader that contains the 
   * soap response populated by InputStream (PipedInputStream) 
   */
  public XMLStreamReader invoke(MessageContext context) {
	  PipedReader pr=null;
	  PipedWriter pw=null;
	 try {
		 logger.info("  : Starting Webservice Call :  ");
		 //get the soap request.
	     XMLStreamReader reader = context.getInMessage().getXMLStreamReader();

	     //form a DOM of the request.
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = dbf.newDocumentBuilder();
	     Document inputDoc = STAXUtils.read(builder,reader,true);
	     //all the soap requests in the body will have a namespaceuri that      
	     String inputURI = inputDoc.getDocumentElement().getNamespaceURI();
	    
	     XMLStreamReader responseReader = null;	        	 	    	
	     String interfaceName = inputDoc.getDocumentElement().getLocalName().intern();
	    	 //since this service will be used a lot, supposedly .intern() can be quicker
   	    	 //each method should return a XMLStreamReader that is streamed back to the client.
	    	 if(interfaceName == "Query".intern()) {
	    		 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO(); 
	    		 pr = new PipedReader();
	    		 pw = new PipedWriter(pr);	    		   		   		  		   	 
	    		 comCriteriaTO.setPrintWriter(pw);
	    		 //Indicator to define VOTABLE for Web Service request
	    		 comCriteriaTO.setStatus("WebService");
	    		 //Setting for TIME parameter.
	    		 String time = inputDoc.getDocumentElement().getElementsByTagNameNS("*","TIME").item(0).getFirstChild().getNodeValue();
	    		 String[] dateTime= time.split("/");			
				 logger.info(" : startDateTime : "+dateTime[0]+" : startEndTime : "+dateTime[1]);			
				 comCriteriaTO.setStartDateTime(dateTime[0]);
				 comCriteriaTO.setEndDateTime(dateTime[1]);	
				 //Setting for Instrument parameter.
				 String instruments = inputDoc.getDocumentElement().getElementsByTagNameNS("*","INSTRUMENT").item(0).getFirstChild().getNodeValue();
				 comCriteriaTO.setInstruments(instruments);
				 //Setting for ListName parameter.
				 String listName = inputDoc.getDocumentElement().getElementsByTagNameNS("*","LISTNAME").item(0).getFirstChild().getNodeValue();
				 comCriteriaTO.setListName(listName);
				 //Thread created to load data into PipeReader.
				 new QueryThreadAnalizer(comCriteriaTO).start();				
				 logger.info(" : Done VOTABLE : ");												
				 responseReader = STAXUtils.createXMLStreamReader(pr);									
	    	 }	  
	 	 logger.info(" : returning response reader soap output : ");
	 	 return responseReader;
	 }catch(Exception e) {
		 logger.fatal("   : Exception in SoapDispatcher:invoke : ", e);
	 }
	 
	 finally
	 {
		 try{
			 if(pr!=null){
				 pr.close();
				 pr=null;
			 }
			 
			 if(pw!=null){
				 pw.close();
				 pw=null;
			 }
		 
		 }catch (Exception e) {
			 logger.fatal("   : Exception in SoapDispatcher:invoke : ", e);
		}
	 }
	 
	 return null;
  }
}