/* #ident	"%W%" */
package eu.heliovo.queryservice.common.transfer;

import java.io.Serializable;
import java.sql.ResultSet;

import uk.ac.starlink.table.StarTable;

public class ResultTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private ResultSet resultSet;
	private String query;
	private StarTable tables;
	private String[] listNameArray;
	private String listName;
	private int rowCount;
	public int getRowCount(){
		return rowCount;
	}
	public void setRowCount(int count){
		this.rowCount = count;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	public StarTable getTables() {
		return tables;
	}
	public void setTables(StarTable tables) {
		this.tables = tables;
	}
	public String[] getListNameArray() {
		return listNameArray;
	}
	public void setListNameArray(String[] listNameArray) {
		this.listNameArray = listNameArray;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}

	
}
