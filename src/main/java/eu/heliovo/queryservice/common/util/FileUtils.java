package eu.heliovo.queryservice.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import eu.heliovo.queryservice.common.transfer.FileResultTO;

public class FileUtils {
	
		
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
	      
	     return contents.toString();
	   }


	  
	  

}
