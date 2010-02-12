package eu.heliovo.queryservice.server.util;

import org.apache.log4j.Logger;

import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

public class QueryThreadAnalizer extends Thread{

	protected final  Logger logger = Logger.getLogger(this.getClass());
	private CommonCriteriaTO comCriteriaTO;
	public QueryThreadAnalizer(CommonCriteriaTO comCriTO){
		comCriteriaTO=comCriTO;
	}
	public void run () {
		 CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();			
			try {				
				commonNameDao.generateVOTableDetails(comCriteriaTO);				
			} catch (DetailsNotFoundException e) {			
				 logger.fatal("   : Exception in QueryThreadAnalizer:run : ", e);
			}
	 }
}
