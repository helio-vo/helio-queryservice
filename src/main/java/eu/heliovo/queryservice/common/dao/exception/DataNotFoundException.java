/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.exception;

public class DataNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	String error="";
	public DataNotFoundException()
	{
		super();             
		error = "unknown";
	}

	public DataNotFoundException(String err)
	{
		super(err);   
		error = err;  
	}
	public DataNotFoundException(String err,Throwable tr)
	{
		super(err,tr);   
		error = err;  
	}	

	public String getError()
	{
		return error;
	}
}
