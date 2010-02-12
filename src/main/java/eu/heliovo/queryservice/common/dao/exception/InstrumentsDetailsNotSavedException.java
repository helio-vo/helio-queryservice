package eu.heliovo.queryservice.common.dao.exception;

public class InstrumentsDetailsNotSavedException extends Exception {
	private static final long serialVersionUID = 1L;
	String error="";
	public InstrumentsDetailsNotSavedException()
	{
		super();             
		error = "unknown";
	}

	public InstrumentsDetailsNotSavedException(String err)
	{
		super(err);   
		error = err;  
	}
	public InstrumentsDetailsNotSavedException(String err,Throwable tr)
	{
		super(err,tr);   
		error = err;  
	}	

	public String getError()
	{
		return error;
	}
	
	public String getMessage()
	{
		return "Exception while Saving History Details";
	}
}
