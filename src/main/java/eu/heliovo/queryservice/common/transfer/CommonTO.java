/* #ident	"%W%" */
package eu.heliovo.queryservice.common.transfer;

import java.io.Serializable;

public class CommonTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String columnName;
	private String columnType;
	private String columnSize;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}
	
	
	
	

}
