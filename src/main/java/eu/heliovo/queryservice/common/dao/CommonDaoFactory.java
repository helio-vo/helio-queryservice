/* #ident	"%W%" */
package eu.heliovo.queryservice.common.dao;

import eu.heliovo.queryservice.common.dao.impl.CommonDaoImpl;
import eu.heliovo.queryservice.common.dao.impl.ShortNameQueryDaoImpl;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.dao.interfaces.ShortNameQueryDao;


public class CommonDaoFactory {

	private static CommonDaoFactory instance = null;
	
	private CommonDaoFactory(){
		//Constructor
	}

	public static CommonDaoFactory getInstance(){
		if(instance==null){
			instance = new CommonDaoFactory();
		}
		return instance;
	}

	public CommonDao getCommonDAO(){
		return new CommonDaoImpl();
	}
	public ShortNameQueryDao getShortNameQueryDao(){
		return new ShortNameQueryDaoImpl();
	}
	
	
}
