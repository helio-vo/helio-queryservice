package com.org.helio.common.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import javax.servlet.http.HttpServletRequest;
import com.opensymphony.xwork2.ActionSupport;
import com.org.helio.common.dao.CommonDaoFactory;
import com.org.helio.common.dao.interfaces.ShortNameQueryDao;
import com.org.helio.common.transfer.CommonTO;
import com.org.helio.common.transfer.FileResultTO;
import com.org.helio.common.util.ConnectionManager;
import com.org.helio.common.util.FileUtils;
import com.org.helio.common.util.InstanceHolders;
 
public class CommonAction  extends ActionSupport
{	
	
    private static final long serialVersionUID = 1L;
    protected final  Logger logger = Logger.getLogger(this.getClass());
    
    public boolean statusDisplay;
    
	public boolean isStatusDisplay() {
		return statusDisplay;
	}

	public void setStatusDisplay(boolean statusDisplay) {
		this.statusDisplay = statusDisplay;
	}	
	
	public String showConfigurationDetials()
	{
		return "SUCCESS";
	}
	
	
	public String display() throws SQLException{ 
		String sReturnStatus="ERROR";
		Connection con=null;
		try{
			//Checking for connection
			con=ConnectionManager.getConnectionForWebApp();
			if(con!=null){
				setStatusDisplay(true);
				sReturnStatus="SUCCESS";
			}else{
				sReturnStatus="ERROR";
				setStatusDisplay(false);
				addActionError("Could not connect database, please check database configuration details.");
			}
		}catch (Exception e) {
			if(con!=null){
				con.close();
				con=null;
			}
		}
		finally{
			if(con!=null){
				con.close();
				con=null;
			}
		}
    	return sReturnStatus;
    }
	
	private String cmbDatabaseTableList;
	private HashMap<String,String> hmbDatabaseTableList;
	
		
	public String getCmbDatabaseTableList() {
		return cmbDatabaseTableList;
	}

	public void setCmbDatabaseTableList(String cmbDatabaseTableList) {
		this.cmbDatabaseTableList = cmbDatabaseTableList;
	}

	public HashMap<String, String> getHmbDatabaseTableList() {
		return hmbDatabaseTableList;
	}

	public void setHmbDatabaseTableList(HashMap<String, String> hmbDatabaseTableList) {
		this.hmbDatabaseTableList = hmbDatabaseTableList;
	}

	/*
	 * Configuration of database table.
	 */
	public String getDatabaseConfigurationPage() throws SQLException
	{
		Connection con=null;
		String sReturnStatus="ERROR";
		try{
			//Setting jdbc driver name
			setJdbcDriverName(InstanceHolders.getInstance().getProperty("jdbc.driver"));
			//Setting jdbc connection url
			setJdbcUrl(InstanceHolders.getInstance().getProperty("jdbc.url"));
			//Setting jdbc connection user
			setJdbcUser(InstanceHolders.getInstance().getProperty("jdbc.user"));
			//Setting jdbc connection password.
			setJdbcPassword(InstanceHolders.getInstance().getProperty("jdbc.password"));
			
			//Checking for connection
			con=ConnectionManager.getConnectionForWebApp();
			if(con!=null){
				setStatusDisplay(true);
				sReturnStatus="SUCCESS";
			}else{
				sReturnStatus="ERROR";
				setStatusDisplay(false);
				addActionError(" Database configuration details is incorrect.");
			}
		}catch (Exception e) {
			if(con!=null){
				con.close();
				con=null;
			}
		}
		finally{
			if(con!=null){
				con.close();
				con=null;
			}
		}
		return sReturnStatus;
	}
	
	
	public String getConfigurationPropertyFilePage() throws SQLException{
		String sReturnStatus="ERROR";
		Connection con=null;
		ShortNameQueryDao shortNameDao= CommonDaoFactory.getInstance().getShortNameQueryDao();
		try {
			System.out.println(""+getJdbcDriverName()+""+getJdbcUrl()+""+getJdbcUser()+""+getJdbcPassword());
			//Setting jdbc driver name
			if(getJdbcDriverName()!=null && !getJdbcDriverName().equals(""))
				InstanceHolders.getInstance().setProperty("jdbc.driver",getJdbcDriverName());
			else
				setJdbcDriverName(InstanceHolders.getInstance().getProperty("jdbc.driver"));
			//Setting jdbc connection url
			if(getJdbcUrl()!=null && !getJdbcUrl().equals(""))
				InstanceHolders.getInstance().setProperty("jdbc.url",getJdbcUrl());
			else
				setJdbcUrl(InstanceHolders.getInstance().getProperty("jdbc.url"));	
			//Setting jdbc connection user
			if(getJdbcUser()!=null && !getJdbcUser().equals(""))
				InstanceHolders.getInstance().setProperty("jdbc.user",getJdbcUser());
			else
				setJdbcUser(InstanceHolders.getInstance().getProperty("jdbc.user"));
			//Setting jdbc connection password.
			if(getJdbcPassword()!=null && !getJdbcPassword().equals(""))
				InstanceHolders.getInstance().setProperty("jdbc.password",getJdbcPassword());
			else
				setJdbcPassword(InstanceHolders.getInstance().getProperty("jdbc.password"));
			//Checking for connection
			con= ConnectionManager.getConnectionForWebApp();
			if(con!=null){
				setStatusDisplay(true);
				sReturnStatus="SUCCESS";
				//List of table names of the database
				hmbDatabaseTableList=shortNameDao.getDatabaseTableNames(con);
			}else{
				sReturnStatus="ERROR";
				setStatusDisplay(false);
				addActionError("Could not connect database, please check database configuration details.");
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(" Exception occured in getDatabaseTableNames method of CommonAction :",e);
			addActionError("Couldn't retrieve database table name.");
			sReturnStatus="ERROR";
			if(con!=null){
				con.close();
				con=null;
			}
		}
		finally{
			if(con!=null){
				con.close();
				con=null;
			}
		}
		return sReturnStatus;
	}
	
	
	private CommonTO[] columnTO;
	private String tableName;
	public CommonTO[] getColumnTO() {
		return columnTO;
	}

	public void setColumnTO(CommonTO[] columnTO) {
		this.columnTO = columnTO;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableColumnList(){
		//to get a table name selected.
		HttpServletRequest req = ServletActionContext.getRequest();
		String tableName = req.getParameter("tableName");
		
		ShortNameQueryDao shortNameDao= CommonDaoFactory.getInstance().getShortNameQueryDao();
		try {
			columnTO=shortNameDao.getTableColumnNames(ConnectionManager.getConnectionForWebApp(),tableName);
			setTableName(tableName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.fatal(" Exception occured in getTableColumnList method of CommonAction :",e);
	}
	
	return "SUCCESS";
   }
	
    String[] addedTableDetails;
	private String filenameandpath;
	private String jdbcDriverName;
	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPassword;
	private String limitConstraint;
	private String serviceDesc;
	
	public String[] getAddedTableDetails() {
		return addedTableDetails;
	}
	
	public void setAddedTableDetails(String[] addedTableDetails) {
		this.addedTableDetails = addedTableDetails;
	}

	public String getFilenameandpath() {
		return filenameandpath;
	}

	public void setFilenameandpath(String filenameandpath) {
		this.filenameandpath = filenameandpath;
	}

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
	
	private String fileNamePath;
	

	public String getFileNamePath() {
		return fileNamePath;
	}

	public void setFileNamePath(String fileNamePath) {
		this.fileNamePath = fileNamePath;
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

	public String createConfigurationFile()
	{
		String sReturnStatus="SUCCESS";
		FileResultTO[] fileResultTO=null;
		HttpServletRequest req = ServletActionContext.getRequest();
		try{
			
			System.out.println(" :  Added Table Details  : "+getAddedTableDetails());
			fileResultTO = new FileResultTO[getAddedTableDetails().length];
			if(getAddedTableDetails()!=null){
				for(int i=0;i<getAddedTableDetails().length;i++){
					if(getAddedTableDetails()[i]!=null && !getAddedTableDetails()[i].equals("")){
						String[] details=getAddedTableDetails()[i].split("\\^\\$\\$\\^");
						System.out.println(getAddedTableDetails()[i]);
						fileResultTO[i]=new FileResultTO();
						fileResultTO[i].setJdbcDriverName("jdbc.driver="+req.getParameter("jdbcDriverName"));
						fileResultTO[i].setJdbcUrl("jdbc.url="+req.getParameter("jdbcUrl"));
						fileResultTO[i].setJdbcPassword("jdbc.password="+req.getParameter("jdbcPassword"));
						fileResultTO[i].setJdbcUser("jdbc.user="+req.getParameter("jdbcUser"));
						fileResultTO[i].setServiceDesc("sql.votable.head.desc="+serviceDesc);
						fileResultTO[i].setColumnNames("sql.columnnames."+details[0]+"="+details[1]);
						fileResultTO[i].setColumnUCD("sql.columnucd."+details[0]+"="+details[8]);
						fileResultTO[i].setColumnUType("sql.columnutypes."+details[0]+"=");
						fileResultTO[i].setColumnDesc("sql.columndesc."+details[0]+"="+details[7]);
						fileResultTO[i].setTimeConstraint("sql.query.time.constraint."+details[0]+"="+details[2]);
						fileResultTO[i].setCoordinateConstraint("sql.query.coordinates.constraint."+details[0]+"="+details[4]);
						fileResultTO[i].setInstrumentConstraint("sql.query.instr.constraint."+details[0]+"="+details[3]);
						fileResultTO[i].setOrderByConstraint("sql.query.orderby.constraint."+details[0]+"="+details[5]);
						fileResultTO[i].setLimitConstraint("sql.query.limit.constraint."+details[0]+"="+details[6]);
					}
				}
			}
			
			//Getting content to write the file.
			String aContents=FileUtils.getContents(fileResultTO);
			
			//Writing all value into the file.
			FileUtils.setContents(fileNamePath, aContents);
			
			//Setting flag to true, database details are correct.
			setStatusDisplay(true);
		
		}catch (Exception e) {
			setStatusDisplay(true);
			logger.fatal(" Exception occured in createConfigurationFile method of CommonAction :",e);
			addActionError("Couldn't create the property file at "+fileNamePath);
		}
		
		return sReturnStatus;
	}
	
       
}
