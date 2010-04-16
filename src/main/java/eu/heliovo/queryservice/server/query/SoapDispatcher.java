package eu.heliovo.queryservice.server.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode; 
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;

import org.w3c.dom.Document;
import org.apache.log4j.Logger; 
import org.w3c.dom.Element;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.common.util.HsqlDbUtils;
import eu.heliovo.queryservice.server.util.QueryThreadAnalizer;

/**
 * Class: SoapDispatcher
 * Description: The dispatcher handles all soap requests and responses for a Query.  Called via the
 * SoapServlet. SoapRequests (Bodies) are placed into a DOM and by analyzing the uri 
 * determine the correct query service for the correct contract.  Responses are Stream based (NOT DOM) into an 
 * XMLStreamReader with the help of PipedStreams.
 *
 */
@WebServiceProvider(targetNamespace="http://helio-vo.eu/xml/QueryService/v0.1",
	      serviceName="HelioQueryServiceService",
	      portName="HelioQueryServicePort")
	      
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
		
		try {
			
			 Element inputDoc=toDocument(request);
			 MessageContext mc = wsContext.getMessageContext();
			 javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST);
			 
			 //HsqlDbUtils.getInstance().dropTable();
			 
		     String interfaceName = inputDoc.getLocalName().intern();
		     //since this service will be used a lot, supposedly .intern() can be quicker
	   	     //each method should return a XMLStreamReader that is streamed back to the client.
		     comCriteriaTO=new CommonCriteriaTO(); 
			 pr = new PipedReader();
			 pw = new PipedWriter(pr);	    		   		   		  		   	 
			 comCriteriaTO.setPrintWriter(pw);
			 
			 //Indicator to define VOTABLE for Web Service request
			 comCriteriaTO.setStatus("WebService");
			 
		    	 if(interfaceName == "Query".intern()) {
		    					 
					 //Setting for Instrument parameter.
					 if(inputDoc.getElementsByTagNameNS("*","INSTRUMENT").getLength()>0){
						 String instruments = inputDoc.getElementsByTagNameNS("*","INSTRUMENT").item(0).getFirstChild().getNodeValue();
						 comCriteriaTO.setInstruments(instruments);
					 }
					 
					//Setting for WHERE parameter.
					 if(inputDoc.getElementsByTagNameNS("*","WHERE").getLength()>0){
						 String whereClause = inputDoc.getElementsByTagNameNS("*","WHERE").item(0).getFirstChild().getNodeValue();
						 comCriteriaTO.setWhereClause(whereClause);
					 }
		    	 } 
		    	
		    	 if(interfaceName == "Coordinates".intern()) {
		    		 
		    		 //Setting for POS( RA & DEC) parameter.
					 if(inputDoc.getElementsByTagNameNS("*","POS").getLength()>0){
						 String pos = inputDoc.getElementsByTagNameNS("*","POS").item(0).getFirstChild().getNodeValue();
						 if(pos!=null && !pos.equals("")){
							 String[] arrPos=pos.split(",");
							 if(arrPos.length>0)
							 comCriteriaTO.setAlpha(arrPos[0]);
							 if(arrPos.length>1)
							 comCriteriaTO.setDelta(arrPos[1]);
						 }
					 }
					 
					//Setting for SIZE parameter.
					 if(inputDoc.getElementsByTagNameNS("*","SIZE").getLength()>0){
						 String size = inputDoc.getElementsByTagNameNS("*","SIZE").item(0).getFirstChild().getNodeValue();
						 comCriteriaTO.setSize(size);
					 }
		    	 }
		    	 
		    	 // This is common for Time. interface.
		    	 
		    	 //Setting for START TIME parameter.
	    		 if(inputDoc.getElementsByTagNameNS("*","STARTTIME").getLength()>0){
		    		 String startTime = inputDoc.getElementsByTagNameNS("*","STARTTIME").item(0).getFirstChild().getNodeValue();
		    		 comCriteriaTO.setStartDateTime(startTime);
				 }
	    		 //Setting for TIME parameter.
	    		 if(inputDoc.getElementsByTagNameNS("*","ENDTIME").getLength()>0){
		    		 String endTime = inputDoc.getElementsByTagNameNS("*","ENDTIME").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setEndDateTime(endTime);	
	    		 }
		    	 
		    	//Setting for ListName parameter.
	    		 if(inputDoc.getElementsByTagNameNS("*","FROM").getLength()>0){
	    			 String listName = inputDoc.getElementsByTagNameNS("*","FROM").item(0).getFirstChild().getNodeValue();
	    			 comCriteriaTO.setListName(listName);
	    		 }	 
		    	 
		    	//Setting for Start Row parameter.
				 if(inputDoc.getElementsByTagNameNS("*","STARTINDEX").getLength()>0){
					 String startRow = inputDoc.getElementsByTagNameNS("*","STARTINDEX").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setStartRow(startRow);
				 }
				 
				//Setting for No Of Rows parameter.
				 if(inputDoc.getElementsByTagNameNS("*","MAXRECORDS").getLength()>0){
					 String noOfRows = inputDoc.getElementsByTagNameNS("*","MAXRECORDS").item(0).getFirstChild().getNodeValue();
					 comCriteriaTO.setNoOfRows(noOfRows);
				 }
		    	 
		    	 //Thread created to load data into PipeReader.
				 new QueryThreadAnalizer(comCriteriaTO).start();				
				 logger.info(" : Done VOTABLE : ");							
	   			
				 responseReader= new StreamSource(pr); 
				
				 logger.info(" : returning response reader soap output : ");
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