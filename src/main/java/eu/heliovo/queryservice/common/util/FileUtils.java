package eu.heliovo.queryservice.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.heliovo.queryservice.common.transfer.FileResultTO;

public class FileUtils {
	
	 protected final  Logger logger = Logger.getLogger(this.getClass());
		
	/**
	  * Change the contents of text file in its entirety, overwriting any
	  * existing text.
	  *
	  * This style of implementation throws all exceptions to the caller.
	  *
	  * @param aFile is an existing file which can be written to.
	  * @throws IllegalArgumentException if param does not comply.
	  * @throws FileNotFoundException if the file does not exist.
	  * @throws IOException if problem encountered during write.
	  */
	  static public void setContents(String aFileName, String aContents)  throws FileNotFoundException, IOException {
		
		File aFile=new File(aFileName);
		
		//Creating a new file; if doesn't exist.
	    if (!aFile.exists()) {
	    	aFile.createNewFile();
	    }else{
	    	boolean success =aFile.delete();
	    	if(success){
	    		aFile.createNewFile();
	    	}else{
	    		throw new IllegalArgumentException("Cannot delete the file at  "+aFileName+".Please delete the file and start again. "+ aFile);
	    	}
	    }
		
	    if (aFile == null) {
	      throw new IllegalArgumentException("File should not be null.");
	    }
	    
	    if (!aFile.isFile()) {
	      throw new IllegalArgumentException("Should not be a directory: " + aFile);
	    }
	    if (!aFile.canWrite()) {
	      throw new IllegalArgumentException("File cannot be written: " + aFile);
	    }
	    
	    
	    //use buffering
	    Writer output = new BufferedWriter(new FileWriter(aFile));
	    try {
	      //FileWriter always assumes default encoding is OK!
	      output.write( aContents );
	    }
	    finally {
	      output.close();
	    }
	  }

	  
	  /**
	   * Fetch the entire contents of a text file, and return it in a String.
	   * This style of implementation does not throw Exceptions to the caller.
	   *
	   * @param aFile is a file which already exists and can be read.
	   */
	   static public String getContents(FileResultTO[] fileResultTO) {
	     //...checks on aFile are elided
	     StringBuilder contents = new StringBuilder();
	     if(fileResultTO!=null && fileResultTO.length>0){
	     for(int i=0;i<fileResultTO.length;i++){
	    	 
	    	 if(i==0){
	    		 contents.append(fileResultTO[i].getJdbcDriverName());
	    		 contents.append(System.getProperty("line.separator"));
	    		 contents.append(fileResultTO[i].getJdbcUrl());
	    		 contents.append(System.getProperty("line.separator"));
	    		 contents.append(fileResultTO[i].getJdbcUser());
	    		 contents.append(System.getProperty("line.separator"));
	    		 contents.append(fileResultTO[i].getJdbcPassword());
	    		 contents.append(System.getProperty("line.separator"));
	    		 contents.append(System.getProperty("line.separator"));
	    		 contents.append(fileResultTO[i].getServiceDesc());
	        	 contents.append(System.getProperty("line.separator"));
	        	 contents.append(System.getProperty("line.separator"));
	    	  }
	    	 
	    	 contents.append(fileResultTO[i].getTimeConstraint());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getInstrumentConstraint());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getCoordinateConstraint());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getOrderByConstraint());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getLimitConstraint());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(System.getProperty("line.separator"));
	    	 
	    	 contents.append(fileResultTO[i].getColumnNames());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getColumnDesc());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getColumnUCD());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(fileResultTO[i].getColumnUType());
	    	 contents.append(System.getProperty("line.separator"));
	    	 contents.append(System.getProperty("line.separator"));
	     }
	     
	     
	     }
	     return contents.toString();
	   }


	  
	 private static Document readXml(StreamSource is) throws SAXException, IOException, ParserConfigurationException {

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	    dbf.setValidating(false);
	    dbf.setIgnoringComments(false);
	    dbf.setIgnoringElementContentWhitespace(true);
	    dbf.setNamespaceAware(true);
	    // dbf.setCoalescing(true);
	    // dbf.setExpandEntityReferences(true);

	    DocumentBuilder db = null;
	    db = dbf.newDocumentBuilder();
	    db.setEntityResolver(new NullResolver());

	    // db.setErrorHandler( new MyErrorHandler());
	    InputSource is2 = new InputSource();
	    is2.setSystemId(is.getSystemId());
	    is2.setByteStream(is.getInputStream());
	    is2.setCharacterStream(is.getReader());

	    return db.parse(is2);
	  }
	   

		// This method writes a DOM document to a file 
	 public static void writeXmlFile(StreamSource is, String filename) throws IOException, ParserConfigurationException { 
		 try { 
			 // Prepare the DOM document for writing 
			 Source source = new DOMSource(readXml(is)); 
			 // Prepare the output file 
			 File file = new File(filename); 
			 Result result = new StreamResult(file);
			 // Write the DOM document to the file 
			 Transformer xformer = TransformerFactory.newInstance().newTransformer(); xformer.transform(source, result); 
		 } catch (TransformerConfigurationException e) { 
			System.out.println("  :   Exception occured while TransformerConfigurationException  : "+e.getMessage());
		 } catch (TransformerException e) {
			 System.out.println("  :   Exception occured while TransformerException  : "+e.getMessage());
		 } catch (SAXException e) {
			 System.out.println("  :   Exception occured while SAXException  : "+e.getMessage());
		 }
		 
	   }    

	 
	 public static void createXmlFileFromInputStream(InputStream inputStream,String fileName)
	 {
		 try {
			 File f=new File(fileName);
			 OutputStream out=new FileOutputStream(f);
		     byte buf[]=new byte[1024];
		     int len;
		     while((len=inputStream.read(buf))>0)
		     out.write(buf,0,len);
		     out.close();
		     inputStream.close();
		 }catch (Exception e) {
	 			// System.err.println("error applying the xslt file "+e);
	 			e.printStackTrace();
	 		}
	 }
	 
	 public static void writeStringIntoFile(String xmlData,String fileName)
	 {
	    try {
	    	
        	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(xmlData);
			out.close();
			out.flush();
	    	/*XMLOutputFactory xof =  XMLOutputFactory.newInstance();
	        XMLStreamWriter xtw = xof.createXMLStreamWriter(new FileWriter(fileName));
	        xtw.writeCData(xmlData);
	        xtw.flush();
	        xtw.close();*/	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	 }
	 
	 public static XMLStreamReader streamXmlDataFromFile(String fileName){
		 try{
			 
			 URL url = Class.forName("FileUtils").getResource(fileName);            
			 InputStream in = url.openStream();
			 XMLInputFactory factory = XMLInputFactory.newInstance();
			 XMLStreamReader parser = factory.createXMLStreamReader(in);
			 
			 return parser;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		return null;
	 }
	 
  
    @SuppressWarnings("unused")
    public static void exportToOoWriter(File file,PipedReader reader) throws Exception
	{
		  OutputStreamWriter zipout = new OutputStreamWriter(new FileOutputStream(file));
	 		
	 		Result result = new StreamResult(zipout);
	
	 		// create an instance of TransformerFactory
	 		try {
	 			// System.out.println("make transform instance");
	 			TransformerFactory transFact = TransformerFactory.newInstance();
	
	 			Transformer trans = transFact.newTransformer(new StreamSource(reader));
	 			trans.transform(new StreamSource(reader), result);
	 		} catch (Exception e) {
	 			// System.err.println("error applying the xslt file "+e);
	 			e.printStackTrace();
	 		}
	 		
	 		zipout.close();
	
	}
  
  
  public static StringBuilder readDataFromFile(Document doc) throws Exception {
	  
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);
      StringBuilder sb=new StringBuilder(writer.toString());
      return sb;
    }
  
  /*
   * Save the file to ftp.
   */
  public static void saveFileToFtp(String ftpUrl,String saveFile) throws Exception
  {
	  if (ftpUrl != null && saveFile != null)
      {
         BufferedInputStream bis = null;
         BufferedOutputStream bos = null;
         try
         {
            URL url = new URL(ftpUrl);
            URLConnection urlc = url.openConnection();
            bos = new BufferedOutputStream( urlc.getOutputStream() );
            bis = new BufferedInputStream( new FileInputStream(saveFile) );
            int i;
            // read byte by byte until end of stream
            while ((i = bis.read()) != -1)
            {
               bos.write( i );
            }
         }
         finally
         {
            if (bis != null)
               try
               {
                  bis.close();
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
            if (bos != null)
               try
               {
                  bos.close();
               }
               catch (IOException ioe)
               {
                  ioe.printStackTrace();
               }
         }
      }
      else
      {
         System.out.println( "Input not available." );
      }

  }
  
  
  /*
   * Get the data from the file
   */
  public static StringBuilder  getFileDataFromFtp(String ftpUrl){
	  StringBuilder  fTextArea=new StringBuilder();
	  try {
		    URL url = new URL(ftpUrl);
		    URLConnection urlconnection = url.openConnection();
		    long l = urlconnection.getContentLength();
		    //fTextArea.append("Content Length = " + l);
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String line;
		    while ((line = in.readLine()) != null)
		    {
		        fTextArea.append("\n"+line);
		    }
		    in.close();
        
		} catch (Exception e) {
		   System.out.println("  :  Couldn't get data from the file  :    "+e.getMessage());
		}  
		return fTextArea;
  }
  
  public static String readFileFromHttpServer(String httpUrl)
  {
	   StringBuffer sb=new StringBuffer();
	    try
	    {
	      URL                url; 
	      URLConnection      urlConn; 
	      DataInputStream    dis;
	      
	      url = new URL(httpUrl);

	      // Note:  a more portable URL: 
	      //url = new URL(getCodeBase().toString() + "/ToDoList/ToDoList.txt");

	      urlConn = url.openConnection(); 
	      urlConn.setDoInput(true); 
	      urlConn.setUseCaches(false);

	      dis = new DataInputStream(urlConn.getInputStream()); 
	      String s; 
	    
	    
	      while ((s = dis.readLine()) != null)
	      { 
	    	  sb.append(s);
	      } 
	        dis.close(); 
	    }

	      catch (MalformedURLException mue) {
	    	  System.out.println(" Exception while creating readFileFromHttpServer() "+mue);
	      } 
	      catch (IOException ioe) {
	    	  System.out.println(" Exception while creating readFileFromHttpServer() "+ioe);
	      } 
	      
	      return sb.toString();
	    } 
  
  
  
  public static void createFileInHttpServer(String locUrl,String fileName) {  
	  try {

	    URL                url; 
	    URLConnection      urlConn; 
	    DataOutputStream   dos; 
	    DataInputStream    dis;

	    url = new URL(locUrl); 
	    urlConn = url.openConnection(); 
	    urlConn.setDoInput(true); 
	    urlConn.setDoOutput(true); 
	    urlConn.setUseCaches(false); 
	    urlConn.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");

	    dos = new DataOutputStream (urlConn.getOutputStream()); 
	    String message = "NEW_ITEM=" + URLEncoder.encode(fileName); 
	    dos.writeBytes(message); 
	    dos.flush(); 
	    dos.close();

	  } // end of "try"

	  catch (MalformedURLException mue) { 
	    System.out.println(" Exception while creating createFileInHttpServer() "+mue);
	  } 
	  catch (IOException ioe) { 
		  System.out.println(" Exception while creating createFileInHttpServer() "+ioe);
	  }

	}  // end of createFileInHttpServer() method 

  

  }
  
    
  class NullResolver implements EntityResolver {
	   public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
	       IOException {
	     return new InputSource(new StringReader(""));
	   }

}