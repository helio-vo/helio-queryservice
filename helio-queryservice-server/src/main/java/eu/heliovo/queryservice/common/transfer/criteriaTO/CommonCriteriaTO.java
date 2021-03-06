/* #ident	"%W%" */
package eu.heliovo.queryservice.common.transfer.criteriaTO;

import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import uk.ac.starlink.table.StarTable;
import eu.heliovo.queryservice.common.transfer.CommonTO;
import java.io.FileDescriptor;

public class CommonCriteriaTO implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer iPageNumber;
	private Integer iRowsPerPage;
	private Integer noOfPages;
	private String userId;
	private String commonQuery;
	private Integer noOfRecords;
	private String startDateTime;
	private String endDateTime;	
	private boolean paginated = false;
	private String[] commonName;
	private String[] commonDesc;
	private String[] commonUcd;
	private Writer printWriter;
	HashMap<String,CommonTO> hmbColumnList;
	private String status;
	private String instruments;
	private String listName;
	private String[] listTableName;
	private String query;
	HashMap<String,String> paramData;
	private String queryForm;
	private String tableName;
	private StarTable[] tables;
	private String whereClause;
	private String queryStatus;
	private String queryDescription;
	private String startRow;
	private String noOfRows;
	private String updatedQuery;
	private int maxRecordsAllowed;
	private String size;
	private String posRa;
	private String posDec;
	private String posRef;
	private String saveto;
	private String dataXml;
	private String longRunningQueryStatus;
	private Writer longRunningPrintWriter;
	private String startDateTimeList[];
	private String endDateTimeList[];	
	private String contextPath;
	private int tableCount=0;
	private String queryArray[];
	private Integer queryReturnCount[];
	private String join;
	private Connection connection;
	private String allStartDate;
	private String allEndDate;
	private String contextUrl;
	private String sRegion;
	private String sRegionValues;
	private String select;
	private int intListCount=0;
	// new sql query settings
	private boolean sqlquery=false;
	private String[] selections;
	private String orderBy;
	private ArrayList<String> warnings;
	
	private boolean votable1_2=false;
	
	private String namespaceURI;
	
	private FileDescriptor longFD;

		
	public CommonCriteriaTO(){
		this.setIPageNumber(0);
		this.setIRowsPerPage(10);
		this.warnings=new ArrayList<String>();
	}
	
	public String getQueryDescription() {
		return queryDescription;
	}

	public void setQueryDescription(String queryDescription) {
		this.queryDescription = queryDescription;
	}

	public StarTable[] getTables() {
		return tables;
	}

	public void setTables(StarTable[] tables) {
		this.tables = tables;
	}

	public String getQueryForm() {
		return queryForm;
	}
	
	public FileDescriptor getLongFD() {
		return longFD;
	}
	
	public void setLongFD(FileDescriptor longFD) {
		this.longFD = longFD;
	}

	public void setQueryForm(String queryForm) {
		this.queryForm = queryForm;
	}

		
	public String getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	public HashMap<String, String> getParamData() {
		return paramData;
	}
	
	public void setParamData(HashMap<String, String> paramData) {
		this.paramData = paramData;
	}

	public String getInstruments() {
		return instruments;
	}
	public void setInstruments(String instruments) {
		this.instruments = instruments;
	}
	
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HashMap<String, CommonTO> getHmbColumnList() {
		return hmbColumnList;
	}
	public void setHmbColumnList(HashMap<String, CommonTO> hmbColumnList) {
		this.hmbColumnList = hmbColumnList;
	}
	public Writer getPrintWriter() {
		return printWriter;
	}
	public void setPrintWriter(Writer printWriter) {
		this.printWriter = printWriter;
	}
	public String[] getCommonName() {
		return commonName;
	}
	public void setCommonName(String[] commonName) {
		this.commonName = commonName;
	}
	public String[] getCommonDesc() {
		return commonDesc;
	}
	public void setCommonDesc(String[] commonDesc) {
		this.commonDesc = commonDesc;
	}
	public String[] getCommonUcd() {
		return commonUcd;
	}
	public void setCommonUcd(String[] commonUcd) {
		this.commonUcd = commonUcd;
	}
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
	public String getCommonQuery() {
		return commonQuery;
	}
	public void setCommonQuery(String commonQuery) {
		this.commonQuery = commonQuery;
	}
	public Integer getNoOfPages() {
		return noOfPages;
	}
	public void setNoOfPages(Integer noOfPages) {
		this.noOfPages = noOfPages;
	}
	public Integer getIPageNumber() {
		return iPageNumber;
	}
	public void setIPageNumber(Integer pageNumber) {
		iPageNumber = pageNumber;
	}
	public Integer getIRowsPerPage() {
		return iRowsPerPage;
	}
	public void setIRowsPerPage(Integer rowsPerPage) {
		iRowsPerPage = rowsPerPage;
	}
	public void setPaginated(boolean paginated){
		this.paginated = paginated;
	}
	
	public boolean isPaginated(){
		return paginated;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getNoOfRecords() {
		return noOfRecords;
	}
	public void setNoOfRecords(Integer noOfRecords) {
		this.noOfRecords = noOfRecords;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public String getStartRow() {
		return startRow;
	}

	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}

	public String getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(String noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getUpdatedQuery() {
		return updatedQuery;
	}

	public void setUpdatedQuery(String updatedQuery) {
		this.updatedQuery = updatedQuery;
	}

	public int getMaxRecordsAllowed() {
		return maxRecordsAllowed;
	}

	public void setMaxRecordsAllowed(int maxRecordsAllowed) {
		this.maxRecordsAllowed = maxRecordsAllowed;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSaveto() {
		return saveto;
	}

	public void setSaveto(String saveto) {
		this.saveto = saveto;
	}

	public String getDataXml() {
		return dataXml;
	}

	public void setDataXml(String dataXml) {
		this.dataXml = dataXml;
	}

	public String getLongRunningQueryStatus() {
		return longRunningQueryStatus;
	}

	public void setLongRunningQueryStatus(String longRunningQueryStatus) {
		this.longRunningQueryStatus = longRunningQueryStatus;
	}

	public Writer getLongRunningPrintWriter() {
		return longRunningPrintWriter;
	}

	public void setLongRunningPrintWriter(Writer longRunningPrintWriter) {
		this.longRunningPrintWriter = longRunningPrintWriter;
	}

	public String[] getListTableName() {
		return listTableName;
	}

	public void setListTableName(String[] listTableName) {
		this.listTableName = listTableName;
	}

	public String[] getStartDateTimeList() {
		return startDateTimeList;
	}

	public void setStartDateTimeList(String[] startDateTimeList) {
		this.startDateTimeList = startDateTimeList;
	}

	public String[] getEndDateTimeList() {
		return endDateTimeList;
	}

	public void setEndDateTimeList(String[] endDateTimeList) {
		this.endDateTimeList = endDateTimeList;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public int getTableCount() {
		return tableCount;
	}

	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}

	public String[] getQueryArray() {
		return queryArray;
	}

	public void setQueryArray(String[] queryArray) {
		this.queryArray = queryArray;
	}
	
	public Integer[] getQueryReturnCountArray() {
		return queryReturnCount;
	}

	public void setQueryReturnCountArray(Integer[] countArray) {
		this.queryReturnCount = countArray;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getAllStartDate() {
		return allStartDate;
	}

	public void setAllStartDate(String allStartDate) {
		this.allStartDate = allStartDate;
	}

	public String getAllEndDate() {
		return allEndDate;
	}

	public void setAllEndDate(String allEndDate) {
		this.allEndDate = allEndDate;
	}

	public String getContextUrl() {
		return contextUrl;
	}

	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	public String getPosRa() {
		return posRa;
	}

	public void setPosRa(String posRa) {
		this.posRa = posRa;
	}

	public String getPosDec() {
		return posDec;
	}

	public void setPosDec(String posDec) {
		this.posDec = posDec;
	}

	public String getPosRef() {
		return posRef;
	}

	public void setPosRef(String posRef) {
		this.posRef = posRef;
	}

	public String getsRegion() {
		return sRegion;
	}

	public void setsRegion(String sRegion) {
		this.sRegion = sRegion;
	}

	public String getsRegionValues() {
		return sRegionValues;
	}

	public void setsRegionValues(String sRegionValues) {
		this.sRegionValues = sRegionValues;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public int getIntListCount() {
		return intListCount;
	}

	public void setIntListCount(int intListCount) {
		this.intListCount = intListCount;
	}
	
	public void setSqlQuery(boolean isSqlQuery){
		this.sqlquery=isSqlQuery;
	}
	
	public void setVotable1_2(boolean votable1_2){
		this.votable1_2=votable1_2;
	}
	
	public boolean getVotable1_2() {
		return this.votable1_2;
	}

	
	public boolean isSqlQuery(){
		return this.sqlquery;
	}
	
	public void setSelections(String[] what){
		this.selections=what;
	}
	
	public String[] getSelections(){
		return this.selections;
	}
	
	public void setOrderBy(String order){
		this.orderBy=order;
	}
	
	public String getOrderBy(){
		return this.orderBy;
	}
	
	public void setNamespaceURI(String namespaceURI){
		this.namespaceURI=namespaceURI;
	}
	
	public String getNamespaceURI(){
		return this.namespaceURI;
	}
	
	public void addWaring(String warn){
		this.warnings.add(warn);
	}
	
	public ArrayList<String> getWarnings(){
		return this.warnings;
	}
}
