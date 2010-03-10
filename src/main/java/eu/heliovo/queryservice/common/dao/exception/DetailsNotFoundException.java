/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.exception;

public class DetailsNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	String error="";
	public DetailsNotFoundException()
	{
		super();             
		error = "unknown";
	}

	public DetailsNotFoundException(String err)
	{
		super(err);   
		error = err;  
	}
	public DetailsNotFoundException(String err,Throwable tr)
	{
		super(err,tr);   
		error = err;  
	}	

	public String getError()
	{
		return error;
	}
}
