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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Embedded;
import org.apache.catalina.realm.MemoryRealm;

@SuppressWarnings("unused")
public class JunitWebserveTest {

	   protected static Endpoint ep;
	   protected static String address;
	   protected static URL wsdlURL;
	   protected static QName serviceName;
	   protected static QName portName;
	   protected static Embedded server;
	   
	   @BeforeClass
	   public static void setUp() throws Exception {
	      address = "http://localhost:9090/helio-queryservice/HelioService";
	      wsdlURL = new URL(address + "?wsdl");
	      serviceName = new QName("http://helio-vo.eu/xml/QueryService/v0.1","HelioQueryServiceService");
	      portName = new QName("http://helio-vo.eu/xml/QueryService/v0.1", "HelioQueryServicePort");
	      server = new Embedded();
	      //server.setRealm(new MemoryRealm());  // if using the tomcat-users.xml file.
	      Engine baseEngine = server.createEngine();
	      baseEngine.setDefaultHost("helio-queryservice");
	      Host baseHost = server.createHost("helio-queryservice","/Users/vineethtshetty/HELIO/workspace/helio-queryservice/target");
	      baseEngine.addChild(baseHost);
	      Context appCtx = server.createContext("/helio-queryservice", "helio-queryservice");
	      baseHost.addChild(appCtx);      
	      server.addEngine(baseEngine);
	      Connector httpConnector = server.createConnector((java.net.InetAddress) null, 9090, false);
	      server.addConnector(httpConnector);      

	      try {
	          server.start();
	      } catch (Exception e) {
	          e.printStackTrace();
	      }     
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
		 try{
			 
			Service jaxwsService = Service.create(wsdlURL, serviceName);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
		      factory.setNamespaceAware(true);
		      DocumentBuilder builder = factory.newDocumentBuilder();
		      InputStream is = getClass().getClassLoader().getResourceAsStream("reqSOAPMessage.xml");
		      Document newDoc = builder.parse(is);
		      DOMSource request = new DOMSource(newDoc);
		      // Both CXF and Metro.
		      Dispatch disp = jaxwsService.createDispatch(portName,Source.class, Service.Mode.PAYLOAD);
		      Source result = (Source) disp.invoke(request);
		      DOMResult domResponse = new DOMResult();
		      Transformer trans = TransformerFactory.newInstance().newTransformer();
		      trans.transform(result, domResponse);
		      assertNotNull(domResponse);

		      System.out.println("  : Response Result :  "+domResponse.getNode().getFirstChild().getTextContent().trim());

		 }
		 catch(MalformedURLException e){
			   e.printStackTrace();
		 }
		 catch(Exception e){
			   e.printStackTrace();
		 }
		  
		  System.out.println(" Testing results ");
	   }
	  
}
