/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import org.apache.log4j.Logger;

import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;



public class CommonDaoImpl implements CommonDao { 

	protected final  Logger logger = Logger.getLogger(this.getClass());
	
	public void generateVOTableDetails(CommonCriteriaTO comCriteriaTO) throws DetailsNotFoundException {
		
		try{
				
			ShortNameQueryDao shortNameDao= CommonDaoFactory.getInstance().getShortNameQueryDao();
			shortNameDao.generateVOTableDetails(comCriteriaTO);
		  				 
		}catch(Exception pe) {
        	pe.printStackTrace();
        	logger.fatal("   : Exception in CommonDaoImpl:generateVOTableDetails : ", pe);
        	
        	return;
        }		
		
	}

	
	
}