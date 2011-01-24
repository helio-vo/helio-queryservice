/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.interfaces;

import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

 
public interface CommonDao {
	public void generateVOTableDetails(CommonCriteriaTO comCriteriaTO) throws DetailsNotFoundException;
	public void generatelongRunningQueryXML(CommonCriteriaTO comCriteriaTO) throws Exception;
}
