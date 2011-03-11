package eu.heliovo.queryservice.client.axis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class DomHelper {

	DomHelper(){
		
	}
	
	//Converts document to string.
	public static  String getStringFromDoc(Document doc)    {
		    DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
		    LSSerializer lsSerializer = domImplementation.createLSSerializer();
		    return lsSerializer.writeToString(doc);   
		}
	
	//Creates a new DOM object
	public static Document newDocument() throws Exception{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    return documentBuilder.newDocument();
	}
	
}
