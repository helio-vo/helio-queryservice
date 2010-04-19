package eu.heliovo.queryservice.common.util;

import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

public class RunService implements Runnable {
	private String statusValue="PENDING";
	private boolean isCompleted=false;
	CommonCriteriaTO comCriteriaTO=null;
	String randomUUIDString=null;
	
	public RunService(CommonCriteriaTO comCriteriaTO,String uuid)
	{
		this.randomUUIDString=uuid;
		this.comCriteriaTO = comCriteriaTO;
	}
	
	public void run()
	{
		try{
	
			LongRunningQueryIdHolders.getInstance().setProperty(randomUUIDString, statusValue+"");
			CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
			commonNameDao.generateVOTableDetails(comCriteriaTO);
			isCompleted=true;
		}
		catch(Exception ex)
		{
			System.out.println("Exception ex:"+ex);
			ex.printStackTrace();
			isCompleted=false;
		}
		if(isCompleted){
			try {
				//Inserting status into HSQL database.
				HsqlDbUtils.getInstance().insertStatusIntoHsqlDB(randomUUIDString, "COMPLETED");
				//Removing status from instance holder.
				LongRunningQueryIdHolders.getInstance().removeProperty(randomUUIDString);
				//Inserting URL into HSQLDB.
				HsqlDbUtils.getInstance().insertURLToHsqlDB(randomUUIDString, comCriteriaTO.getSaveto());
			} catch (Exception e) {
				LongRunningQueryIdHolders.getInstance().removeProperty(randomUUIDString);
				
			}
		}else{
			try {
				//Inserting status into HSQL database
				HsqlDbUtils.getInstance().insertStatusIntoHsqlDB(randomUUIDString, "ERROR");
			} catch (Exception e) {
				//Removing status from instance holder.
				LongRunningQueryIdHolders.getInstance().removeProperty(randomUUIDString);
			}
			
		}
		
	}
	
	public String getReturnStatus()
	{
		return statusValue;
	}
	public boolean isCompleted()
	{
		return isCompleted;	
	}
	
}
