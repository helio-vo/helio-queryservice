/* #ident	"%W%" */
package com.org.helio.common.transfer;

import java.io.Serializable;

public class FileResultTO implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	private String jdbcDriverName;
	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPassword;
	private String fileNamePath;
	private String timeConstraint;
	private String instrumentConstraint;
	private String coordinateConstraint;
	private String columnNames;
	private String columnUCD;
	private String columnUType;
	private String columnDesc;
	private String orderByConstraint;
	private String limitConstraint;
	private String serviceDesc;
	
	public String getJdbcDriverName() {
		return jdbcDriverName;
	}
	public void setJdbcDriverName(String jdbcDriverName) {
		this.jdbcDriverName = jdbcDriverName;
	}
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	public String getJdbcUser() {
		return jdbcUser;
	}
	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}
	public String getJdbcPassword() {
		return jdbcPassword;
	}
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
	public String getFileNamePath() {
		return fileNamePath;
	}
	public void setFileNamePath(String fileNamePath) {
		this.fileNamePath = fileNamePath;
	}
	public String getTimeConstraint() {
		return timeConstraint;
	}
	public void setTimeConstraint(String timeConstraint) {
		this.timeConstraint = timeConstraint;
	}
	public String getInstrumentConstraint() {
		return instrumentConstraint;
	}
	public void setInstrumentConstraint(String instrumentConstraint) {
		this.instrumentConstraint = instrumentConstraint;
	}
	public String getCoordinateConstraint() {
		return coordinateConstraint;
	}
	public void setCoordinateConstraint(String coordinateConstraint) {
		this.coordinateConstraint = coordinateConstraint;
	}
	public String getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String columnNames) {
		this.columnNames = columnNames;
	}
	public String getColumnUCD() {
		return columnUCD;
	}
	public void setColumnUCD(String columnUCD) {
		this.columnUCD = columnUCD;
	}
	public String getColumnUType() {
		return columnUType;
	}
	public void setColumnUType(String columnUType) {
		this.columnUType = columnUType;
	}
	public String getColumnDesc() {
		return columnDesc;
	}
	public void setColumnDesc(String columnDesc) {
		this.columnDesc = columnDesc;
	}
	public String getOrderByConstraint() {
		return orderByConstraint;
	}
	public void setOrderByConstraint(String orderByConstraint) {
		this.orderByConstraint = orderByConstraint;
	}
	public String getLimitConstraint() {
		return limitConstraint;
	}
	public void setLimitConstraint(String limitConstraint) {
		this.limitConstraint = limitConstraint;
	}
	public String getServiceDesc() {
		return serviceDesc;
	}
	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	
	
	

}
