package eu.heliovo.queryservice.common.dao.exception;

public class ObservatoryDetailsNotSavedException extends Exception {
	private static final long serialVersionUID = 1L;
	String error="";
	public ObservatoryDetailsNotSavedException()
	{
		super();             
		error = "unknown";
	}

	public ObservatoryDetailsNotSavedException(String err)
	{
		super(err);   
		error = err;  
	}
	public ObservatoryDetailsNotSavedException(String err,Throwable tr)
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
