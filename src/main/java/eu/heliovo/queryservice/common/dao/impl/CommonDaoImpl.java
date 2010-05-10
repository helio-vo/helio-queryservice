/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.impl;

import java.io.BufferedWriter;

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

	public void generatelongRunningQueryXML(CommonCriteriaTO comCriteriaTO) throws Exception
	{
		BufferedWriter out =null;
		try{
			String status=comCriteriaTO.getStatus();
			out = new BufferedWriter( comCriteriaTO.getLongRunningPrintWriter() );
			//Adding response header start for WebService VOTABLE.
			if(status!=null && !status.equals("")){
				 out.write("<helio:resultResponse xmlns:helio=\"http://helio-vo.eu/xml/LongQueryService/v0.1\">");
			}
			out.write(comCriteriaTO.getDataXml());
			//Adding response header start for WebService VOTABLE.
			if(status!=null && !status.equals("")){
				 out.write("</helio:resultResponse>");
			}
		}catch(Exception pe) {
        	pe.printStackTrace();
        	logger.fatal("   : Exception in CommonDaoImpl:generatelongRunningQueryXML : ", pe);
    		throw new Exception("Couldn't create Long running response XML");
        }		
		
		out.flush();
        out.close();
	}
	
	
}