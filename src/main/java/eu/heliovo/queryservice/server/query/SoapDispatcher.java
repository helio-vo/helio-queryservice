package com.org.helio.server.query;

import javax.xml.stream.*;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Hashtable;
import com.org.helio.common.util.DomHelper;
import org.apache.log4j.Logger;
import org.codehaus.xfire.util.STAXUtils; 
import org.codehaus.xfire.MessageContext;
import com.org.helio.common.transfer.criteriaTO.CommonCriteriaTO;
import com.org.helio.server.util.QueryThreadAnalizer;

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
	  CommonCriteriaTO comCriteriaTO=null;
	  try {
		 logger.info("  : Starting Webservice Call :  ");
		 //get the soap request.
	     XMLStreamReader reader = context.getInMessage().getXMLStreamReader();

	     //form a DOM of the request.
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	     DocumentBuilder builder = dbf.newDocumentBuilder();
	     Document inputDoc = STAXUtils.read(builder,reader,true);
	     logger.info(" : Soap input values : "+DomHelper.getStringFromDoc(inputDoc));	
	     //all the soap requests in the body will have a namespaceuri that      
	     String inputURI = inputDoc.getDocumentElement().getNamespaceURI();
	    
	     XMLStreamReader responseReader = null;	        	 	    	
	     String interfaceName = inputDoc.getDocumentElement().getLocalName().intern();
	     //since this service will be used a lot, supposedly .intern() can be quicker
   	     //each method should return a XMLStreamReader that is streamed back to the client.
	     comCriteriaTO=new CommonCriteriaTO(); 
		 pr = new PipedReader();
		 pw = new PipedWriter(pr);	    		   		   		  		   	 
		 comCriteriaTO.setPrintWriter(pw);
		 
		 //Indicator to define VOTABLE for Web Service request
		 comCriteriaTO.setStatus("WebService");
		 
	    	 if(interfaceName == "Query".intern()) {
	    		 //Setting for START TIME parameter.
	    		 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTTIME").getLength()>0){
		    		 String startTime = inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTTIME").item(0).getFirstChild().getNodeValue();
		    		 comCriteriaTO.setStartDateTime(startTime);
				 }
	    		 //Setting for TIME parameter.
	    		 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","ENDTIME").getLength()>0){
		    		 String endTime = inputDoc.getDocumentElement().getElementsByTagNameNS("*","ENDTIME").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setEndDateTime(endTime);	
	    		 }
				//Setting for ListName parameter.
	    		 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","FROM").getLength()>0){
	    			 String listName = inputDoc.getDocumentElement().getElementsByTagNameNS("*","FROM").item(0).getFirstChild().getNodeValue();
	    			 comCriteriaTO.setListName(listName);
	    		 }
				 
				 //Setting for Instrument parameter.
				 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","INSTRUMENT").getLength()>0){
					 String instruments = inputDoc.getDocumentElement().getElementsByTagNameNS("*","INSTRUMENT").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setInstruments(instruments);
				 }
				 
				//Setting for Start Row parameter.
				 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTINDEX").getLength()>0){
					 String startRow = inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTINDEX").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setStartRow(startRow);
				 }
				 
				//Setting for No Of Rows parameter.
				 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","MAXRECORDS").getLength()>0){
					 String noOfRows = inputDoc.getDocumentElement().getElementsByTagNameNS("*","MAXRECORDS").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setNoOfRows(noOfRows);
				 }
				 
				//Setting for WHERE parameter.
				 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","WHERE").getLength()>0){
					 String whereClause = inputDoc.getDocumentElement().getElementsByTagNameNS("*","WHERE").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setWhereClause(whereClause);
				 }
											
	    	 }else if(interfaceName == "TimeQuery".intern()) {
	    		
	    		 //Setting for START TIME parameter.
	    		 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTTIME").getLength()>0){
		    		 String startTime = inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTTIME").item(0).getFirstChild().getNodeValue();
		    		 comCriteriaTO.setStartDateTime(startTime);
				 }
	    		 //Setting for TIME parameter.
	    		 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","ENDTIME").getLength()>0){
		    		 String endTime = inputDoc.getDocumentElement().getElementsByTagNameNS("*","ENDTIME").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setEndDateTime(endTime);	
	    		 }
				//Setting for ListName parameter.
	    		 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","FROM").getLength()>0){
	    			 String listName = inputDoc.getDocumentElement().getElementsByTagNameNS("*","FROM").item(0).getFirstChild().getNodeValue();
	    			 comCriteriaTO.setListName(listName);
	    		 }	 
				//Setting for Start Row parameter.
				 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTINDEX").getLength()>0){
					 String startRow = inputDoc.getDocumentElement().getElementsByTagNameNS("*","STARTINDEX").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setStartRow(startRow);
				 }
				 
				//Setting for No Of Rows parameter.
				 if(inputDoc.getDocumentElement().getElementsByTagNameNS("*","MAXRECORDS").getLength()>0){
					 String noOfRows = inputDoc.getDocumentElement().getElementsByTagNameNS("*","MAXRECORDS").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setNoOfRows(noOfRows);
				 }
	    	 }	 
	    	 
	    	 //Thread created to load data into PipeReader.
			 new QueryThreadAnalizer(comCriteriaTO).start();				
			 logger.info(" : Done VOTABLE : ");												
			 responseReader = STAXUtils.createXMLStreamReader(pr);	
	 	 logger.info(" : returning response reader soap output : ");
	 	 return responseReader;
	 }catch(Exception e) {
		 logger.fatal("   : Exception in SoapDispatcher:invoke : ", e);
	 }
	 
	 /*finally
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
	 }*/
	 
	 return null;
  }
}