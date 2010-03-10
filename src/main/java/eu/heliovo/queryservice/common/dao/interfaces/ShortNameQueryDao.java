/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.interfaces;

import java.sql.Connection;
import java.util.HashMap;

import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.transfer.CommonTO;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;



public interface ShortNameQueryDao {

	public void generateVOTableDetails(CommonCriteriaTO comCriteriaTO) throws DetailsNotFoundException,Exception;
	public HashMap<String,String> getDatabaseTableNames(Connection con) throws Exception;
	public CommonTO[] getTableColumnNames(Connection con,String tableName) throws Exception;
	
	
}
