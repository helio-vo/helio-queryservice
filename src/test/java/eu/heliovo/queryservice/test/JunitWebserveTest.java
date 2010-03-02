package eu.heliovo.queryservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import eu.heliovo.queryservice.common.dao.impl.CommonDaoImpl;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

@SuppressWarnings("unused")
public class JunitWebserveTest {

	protected static Endpoint ep;
	   protected static String address;
	   protected static URL wsdlURL;
	   protected static QName serviceName;
	   protected static QName portName;

	   //@BeforeClass
	   public static void setUp() throws Exception {
	      address = "http://localhost:8080/HelioQueryService/services/HelioService";
	      wsdlURL = new URL(address + "?wsdl");
	      serviceName = new QName("http://helio-vo.eu/xml/QueryService/v0.1",
	         "HelioService");
	      portName = new QName("http://www.example.org/DoubleIt", "DoubleItPort");
	      ep = Endpoint.publish(address ,new CommonDaoImpl());
	   }

	   //@AfterClass
	   public static void tearDown() {
	      try {
	         ep.stop();
	      } catch (Throwable t) {
	         System.out.println("Error thrown: " + t.getMessage());
	      }
	   }
	   
	   @Test
	   public void testDummy() {
		   
	   }

	   /*
	    * This test uses raw Service class for service, wsimport/wsdl2java
	    * generated SEI
	    */
	   // @Test
	   public void testDoubleItWithNegativeNumbers() {
	      Service jaxwsService = Service.create(wsdlURL, serviceName);
	      
	   }

	   /*
	    * This test uses raw Service class for service, Dispatch<SOAPMessage> for
	    * client No wsimport/wsdl2java needed. Note works with full SOAP message
	    * (Service.Mode.MESSAGE)
	    */
	   // @Test
	   public void doubleItWorksForZero() throws Exception {
	      Service jaxwsService = Service.create(wsdlURL, serviceName);
	      Dispatch<SOAPMessage> disp = jaxwsService.createDispatch(portName,
	            SOAPMessage.class, Service.Mode.MESSAGE);
	      InputStream is = getClass().getClassLoader().getResourceAsStream("fullSOAPMessage.xml");
	      SOAPMessage reqMsg = MessageFactory.newInstance().createMessage(null,is);
	      assertNotNull(reqMsg);
	      SOAPMessage response = (SOAPMessage) disp.invoke(reqMsg);
	      assertEquals("Double-It not doubling zero correctly", "0", response
	            .getSOAPBody().getTextContent().trim());
	   }

	   /*
	    * This test uses raw Service class for service, Dispatch<Source> for
	    * client. No wsimport/wsdl2java run needed. Uses payload (soap:body contents)
	    * only (Service.Mode.PAYLOAD), but can be configured to use MESSAGE. Note
	    * CXF supports other options such as Dispatch<DOMSource>, Dispatch<SAXSource>,
	    * and Dispatch<StreamSource>, search CXF source code for examples.
	    */
	   // @Test
	   public void doubleItWorksForPrimeNumbers() throws Exception {
	      Service jaxwsService = Service.create(wsdlURL, serviceName);
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      factory.setNamespaceAware(true);
	      DocumentBuilder builder = factory.newDocumentBuilder();
	      InputStream is = getClass().getClassLoader().getResourceAsStream(
	            "justPayload.xml");
	      Document newDoc = builder.parse(is);
	      DOMSource request = new DOMSource(newDoc);
	      // Both CXF and Metro:
	      Dispatch<Source> disp = jaxwsService.createDispatch(portName,Source.class, Service.Mode.PAYLOAD);
	      Source result = (Source) disp.invoke(request);
	      DOMResult domResponse = new DOMResult();
	      Transformer trans = TransformerFactory.newInstance().newTransformer();
	      trans.transform(result, domResponse);
	      assertEquals("Double-It failing with prime numbers", "14", domResponse.getNode().getFirstChild().getTextContent().trim());
	      /*
	        Alternative for CXF, uses Dispatch: 
	        Dispatch disp = jaxwsService.createDispatch(portName, DOMSource.class,
	           Service.Mode.PAYLOAD); 
	        DOMSource domResponse = disp.invoke(request);
	        assertEquals("Double-It failing with prime numbers", "14",
	           domResponse.getNode().getFirstChild().getTextContent().trim());
	       */
	   }

	   /*
	    * This test uses raw Service class for service, Dispatch<JAXBContext> for
	    * client. Conveniently uses JAX-WS generated artifacts.
	    */
	   // @Test
	   public void doubleItWorksWithOddNumbers() throws Exception {
	      Service jaxwsService = Service.create(wsdlURL, serviceName);
	      JAXBContext jaxbContext = JAXBContext.newInstance("org.example.doubleit");
	      Dispatch jaxbDispatch = jaxwsService.createDispatch(portName,jaxbContext, Service.Mode.PAYLOAD);

	      //DoubleIt myDoubleIt = new DoubleIt();
	      //myDoubleIt.setNumberToDouble(new BigInteger("3"));

	      //JAXBElement doubleItElement = new JAXBElement(new QName(
	        //    "http://www.example.org/DoubleIt", "DoubleIt"), DoubleIt.class,
	         //   myDoubleIt);

	      //DoubleItResponse response = (DoubleItResponse) jaxbDispatch
	      //      .invoke(doubleItElement);
	     // assertNotNull(response);
	      //assertEquals("Double-It failing with odd numbers", "6", response
	          //  .getDoubledNumber().toString());
	   }

}
