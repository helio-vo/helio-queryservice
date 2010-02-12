/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.exception;

public class ShortNameQueryException extends Exception{
	private static final long serialVersionUID = 1L;
	String error="";
	public ShortNameQueryException()
	{
		super();             
		error = "unknown";
	}

	public ShortNameQueryException(String err)
	{
		super(err);   
		error = err;  
	}
	public ShortNameQueryException(String err,Throwable tr)
	{
		super(err,tr);   
		error = err;  
	}	

	public String getError()
	{
		return error;
	}
}
