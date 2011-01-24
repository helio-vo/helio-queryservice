/* #ident	"%W%" */
package eu.heliovo.queryservice.common.transfer;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.HashMap;

public class CommonResultTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private Object[] result;	
	private int count;
	private HashMap<String,CommonTO> columnNameList;
	private ResultSet resultSet;
	String[] colNames;
	
	
	public String[] getColNames() {
		return colNames;
	}
	public void setColNames(String[] colNames) {
		this.colNames = colNames;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Object[] getResult() {
		return result;
	}
	public void setResult(Object[] result) {
		this.result = result;
	}
	public HashMap<String, CommonTO> getColumnNameList() {
		return columnNameList;
	}
	public void setColumnNameList(HashMap<String, CommonTO> columnNameList) {
		this.columnNameList = columnNameList;
	}
	

}
