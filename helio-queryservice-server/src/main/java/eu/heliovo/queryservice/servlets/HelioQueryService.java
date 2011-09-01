package eu.heliovo.queryservice.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import eu.heliovo.queryservice.common.dao.CommonDaoFactory;
import eu.heliovo.queryservice.common.dao.interfaces.CommonDao;
import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;
import eu.heliovo.queryservice.common.util.CommonUtils;
import eu.heliovo.queryservice.server.util.ServiceInfo;

/**
 * Servlet implementation class HelioQueryService
 */
public class HelioQueryService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelioQueryService() {
        super();        
    }

	/** 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
		PrintWriter printWriter = response.getWriter(); 

		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		    comCriteriaTO.setPrintWriter(printWriter);
		    
		    String tablenames=request.getParameter("TABLENAMES");
		    String fieldnames=request.getParameter("FIELDNAMES");
		    
		    if(tablenames != null){
		    	ServiceInfo.getInstance().getTableNames(printWriter);
		    } else if (fieldnames != null && !fieldnames.trim().equals("")){
		    	ServiceInfo.getInstance().getTableFields(fieldnames, printWriter);
		    } else {
		    
			    //Setting start time & end time parameter
			    String sStartTime=request.getParameter("STARTTIME");
			    String sEndTime=request.getParameter("ENDTIME");
			    //Setting SELECT parameter
			    String sSelect=request.getParameter("SELECT");
			    //Setting values for startime and endtime.
			    comCriteriaTO.setAllStartDate(sStartTime);
			    comCriteriaTO.setAllEndDate(sEndTime);
			    comCriteriaTO.setContextUrl(CommonUtils.getUrl(request));
			    
			    
			    if(request.getServletPath().toLowerCase().endsWith("b")) {
					 comCriteriaTO.setVotable1_2(true);
			    }
			    
			   	System.out.println(" sStartTime : "+sStartTime+" sEndTime : "+sEndTime);
			   	//Start date array list
			   	if(sStartTime!=null && !sStartTime.equals("")){
			    	comCriteriaTO.setStartDateTimeList(sStartTime.split(","));
			    }
			    //End date array list
			   	if(sEndTime!=null && !sEndTime.equals("")){
			    	comCriteriaTO.setEndDateTimeList(sEndTime.split(","));
			    }
			   	//Setting context path.
			    if(request.getContextPath()!=null){
					 comCriteriaTO.setContextPath(request.getContextPath().substring(request.getContextPath().indexOf("-")+1,request.getContextPath().length()));
				 }
			    if(sSelect!=null && !sSelect.trim().equals("")) {
			    	comCriteriaTO.setSelect(sSelect);
			    	comCriteriaTO.setSelections(new String[]{sSelect});
			    }
			    //Setting for Instrument parameter.
			    String sInstrument=request.getParameter("INSTRUMENT");
			    comCriteriaTO.setInstruments(sInstrument);
			    //Setting for List Name parameter.
			    String sListName=request.getParameter("FROM");
			    comCriteriaTO.setListName(sListName);
			    //String array.
			    if(sListName!=null && !sListName.equals("")){
			    	comCriteriaTO.setListTableName(sListName.split(","));
			    }
			    //Setting where clause parameter
			    String whereClause=request.getParameter("WHERE");
			    comCriteriaTO.setWhereClause(whereClause);
			    //Setting start row parameter
			    String startRow=request.getParameter("STARTINDEX");
			    comCriteriaTO.setStartRow(startRow);
			    //Setting no of row parameter
			    String noOfRows=request.getParameter("MAXRECORDS");
			    comCriteriaTO.setNoOfRows(noOfRows);
			    //Setting POS ( dec and ra ) parameter
			    String pos=request.getParameter("POS");
			    if(pos!=null && !pos.equals("")){
			    	 String[] posArr=pos.split(";");
					 if(posArr.length>1){
						 comCriteriaTO.setPosRef(posArr[1]);
					 }
					 String[] arrPos=posArr[0].split(",");
					 if(arrPos.length>0)
						 comCriteriaTO.setPosRa(arrPos[0]);
					 if(arrPos.length>1)
						 comCriteriaTO.setPosDec(arrPos[1]);
				 }
			    //Setting SIZE parameter.
			    String size=request.getParameter("SIZE");
			    comCriteriaTO.setSize(size);
			    //Setting region parameter
			    String sRegion=request.getParameter("REGION");
			    //Getting parse region.
			    Map<String,String> map=CommonUtils.parseRegionParameter(sRegion);
			    //Region.
			    comCriteriaTO.setsRegion(map.get("region"));
			    //Region values.
			    comCriteriaTO.setsRegionValues(map.get("regionvalues"));
			    //Setting join parameter
			    String sJoin=request.getParameter("JOIN");
			    if(sJoin!=null && !sJoin.trim().equals(""))
			    	comCriteriaTO.setJoin(sJoin.toLowerCase());
			    //sql query
			    whereClause=request.getParameter("SQLWHERE");
			    if(whereClause!=null && whereClause.trim().length()>0){
			    	comCriteriaTO.setSqlQuery(true);
			    	comCriteriaTO.setWhereClause(whereClause);
			    }
			    String what=request.getParameter("WHAT");
			    if(what!=null && what.trim().length()>0){
			    	comCriteriaTO.setSqlQuery(true);
			    	comCriteriaTO.setSelections(new String[]{what});
			    }
			    String order=request.getParameter("ORDER_BY");
			    if(order!=null && order.trim().length()>0){
			    	comCriteriaTO.setSqlQuery(true);
			    	comCriteriaTO.setOrderBy(order);
			    }
			    startRow=request.getParameter("OFFSET");
			    if(startRow!=null && startRow.trim().length()>0){
			    	comCriteriaTO.setSqlQuery(true);
			    	comCriteriaTO.setStartRow(startRow);
			    }
			    noOfRows=request.getParameter("LIMIT");
			    if(noOfRows!=null && noOfRows.trim().length()>0){
			    	comCriteriaTO.setSqlQuery(true);
			    	comCriteriaTO.setNoOfRows(noOfRows);
			    }
			    //Calling generate VOTable Details.
				CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				commonNameDao.generateVOTableDetails(comCriteriaTO);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}

		finally
		{
			if(printWriter!=null){
				printWriter.close();
				printWriter=null;
			}
		}
	}

}
