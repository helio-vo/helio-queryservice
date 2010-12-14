/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao.interfaces;

import eu.heliovo.queryservice.common.dao.exception.DetailsNotFoundException;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

 
public interface LongRunningQueryDao {
	
		public void insertStatusToHsqlDB(String randomUUIDString,String status) throws DetailsNotFoundException; 
	    public void insertURLToHsqlDB(String randomUUIDString,String url) throws DetailsNotFoundException;
		public String getStatusFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException;
		public String getUrlFromHsqlDB(String randomUUIDString) throws DetailsNotFoundException;
		public void loadProviderAccessTable(String fileName,String tableName) throws DetailsNotFoundException;
		public void deleteUrlFromHsqlDB() throws DetailsNotFoundException;
		public void deleteStatusFromHsqlDB() throws DetailsNotFoundException;
		public String deleteSavedVoTable() throws DetailsNotFoundException;
}
