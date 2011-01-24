/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class XsdParserUtils {
	private static final String ARGSUFFIX = ":]";

	private static final String ARGPREFIX = "[:";
	
	protected final  Logger logger = Logger.getLogger(this.getClass());
		private  String schemaNamespace="http://helio-vo.eu/xml/Instruments/v0.1";
		
	/**
     * <p>
     *  This will do the work of parsing the schema.
     * </p>
     *
     * @throws <code>IOException</code> - when parsing errors occur.
     */
    @SuppressWarnings({ "unchecked", "unused" })
	public void parseSchema(String filePath) throws IOException {       /**
         * Create builder to generate JDOM representation of XML Schema,
         *   without validation and using Apache Xerces.
         */ 
        SAXBuilder builder = new SAXBuilder();
        try {
            Document schemaDoc = builder.build(new File(filePath));
            // Schema name space should be changed based on changes in xsd.
            List attributes = schemaDoc.getRootElement().getChildren();
            for (Iterator<Element> i = attributes.iterator(); i.hasNext(); ) {
                // Iterate and handle
                Element attribute = (Element)i.next();
                handleAttribute(attribute);
            }
            // Handle attributes nested within complex types
        } catch (JDOMException e) {
            throw new IOException(e.getMessage());
        }
    }
    
    
    /**
     * <p>
     *  This will convert an attribute into constraints.
     * </p>
     *
     * @throws <code>IOException</code> - when parsing errors occur.
     */
    @SuppressWarnings("unchecked")
	private void handleAttribute(Element attribute)  throws IOException {
        // Get the attribute name and create a Constraint
       HashMap<String,String>  hashMap=new  HashMap<String,String>();
       // Get the simpleType - if none, we are done with this attribute
       Element simpleType = attribute.getChild("simpleType");
       if (simpleType == null) {
           return;
       }

        // Handle any allowed values
        List allowedValues = simpleType.getChildren("enumeration");
        if (allowedValues != null) {
            for (Iterator i=allowedValues.iterator(); i.hasNext(); ) {
                Element allowedValue = (Element)i.next();
                System.out.println(allowedValue.getAttributeValue("value"));
            }
        }
      
        hashMap.put("", "");
    }


				
}
