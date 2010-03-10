/* #ident	"%W%" */
package eu.heliovo.queryservice.common.util;

import java.util.HashMap;



public class InstanceHolders {
	private HashMap<String,String> hmRunningProcess=new HashMap<String,String>();
	private static InstanceHolders oInstanceHolders =null;
	private InstanceHolders ()
	{
	}
	public static InstanceHolders  getInstance()
	{
		if(oInstanceHolders ==null)
			oInstanceHolders = new InstanceHolders();
 
		return oInstanceHolders; 
	}

	public synchronized  void setProperty(String key,String value) throws Exception
	{
		if(hmRunningProcess.containsKey(key))
		{
			hmRunningProcess.remove(key);
		}
		hmRunningProcess.put(key, value);
	}
	
	public synchronized  void removeProperty(String sUser)
	{
		if(!hmRunningProcess.containsKey(sUser))
		{
			return;
		}
		hmRunningProcess.remove(sUser);
	}
	
	public synchronized  String getProperty(String key)
	{
		if(!hmRunningProcess.containsKey(key))
		{
			return "";
		}
		return hmRunningProcess.get(key);
	}
	
	
		
}
