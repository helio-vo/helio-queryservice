package eu.heliovo.queryservice.servlets;


import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
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
	
	private String findParameterValue(HttpServletRequest request, String keyName) {
		String val = null;
		val = request.getParameter(keyName);
		if(val != null) { return val;}
		
		Enumeration<String> paramEnum = request.getParameterNames();
		while(paramEnum.hasMoreElements()) {
			String paramMapName = paramEnum.nextElement();
			if(keyName.toUpperCase().equals(paramMapName.toUpperCase())) {
				return request.getParameter(paramMapName);
			}
		}
		return null;
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
		    System.out.println("dopost of servet");
		    if(tablenames != null){
		    	new ServiceInfo(printWriter).run();
		    } else if (fieldnames != null && !fieldnames.trim().equals("")){
		    	new ServiceInfo(fieldnames, printWriter).run();
		    } else {
		    
			    //Setting start time & end time parameter
			    String ivoaTapQuery = findParameterValue(request,"QUERY");
			    String ivoaTapLang = findParameterValue(request,"LANG");
			    String sListName = null;
			    String whereClause = null;
			    String noOfRows = null;
			    String sSelect = null;
			    if(ivoaTapQuery == null && ivoaTapLang == null) {
			    
				    String sStartTime=findParameterValue(request,"STARTTIME");
				    String sEndTime=findParameterValue(request,"ENDTIME");
				    //Setting SELECT parameter
				    sSelect=findParameterValue(request,"SELECT");
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
				    String sInstrument=findParameterValue(request,"INSTRUMENT");
				    comCriteriaTO.setInstruments(sInstrument);
				    //Setting for List Name parameter.
				    sListName=findParameterValue(request,"FROM");
				    comCriteriaTO.setListName(sListName);
				    //String array.
				    if(sListName!=null && !sListName.equals("")){
				    	comCriteriaTO.setListTableName(sListName.split(","));
				    }
				    //Setting where clause parameter
				    whereClause=findParameterValue(request,"WHERE");
				    comCriteriaTO.setWhereClause(whereClause);
				    //Setting start row parameter
				    String startRow=findParameterValue(request,"STARTINDEX");
				    comCriteriaTO.setStartRow(startRow);
				    //Setting no of row parameter
				    noOfRows=findParameterValue(request,"MAXRECORDS");
				    comCriteriaTO.setNoOfRows(noOfRows);
				    //Setting POS ( dec and ra ) parameter
				    String pos=findParameterValue(request,"POS");
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
				    String size=findParameterValue(request,"SIZE");
				    comCriteriaTO.setSize(size);
				    //Setting region parameter
				    String sRegion=findParameterValue(request,"REGION");
				    //Getting parse region.
				    Map<String,String> map=CommonUtils.parseRegionParameter(sRegion);
				    //Region.
				    comCriteriaTO.setsRegion(map.get("region"));
				    //Region values.
				    comCriteriaTO.setsRegionValues(map.get("regionvalues"));
				    //Setting join parameter
				    String sJoin=findParameterValue(request,"JOIN");
				    if(sJoin!=null && !sJoin.trim().equals(""))
				    	comCriteriaTO.setJoin(sJoin.toLowerCase());
				    //sql query
				    whereClause=findParameterValue(request,"SQLWHERE");
				    if(whereClause!=null && whereClause.trim().length()>0){
				    	comCriteriaTO.setSqlQuery(true);
				    	comCriteriaTO.setWhereClause(whereClause);
				    	/*
				    	 String sListName2=request.getParameter("FROM");
						 if(sListName2!=null && !sListName2.equals("")){
							if(sListName2.indexOf(",") != -1) {
								comCriteriaTO.setListTableName(sListName.split(","));
							}else if(sListName2.indexOf("join") != -1 && sListName2.indexOf(" ") != -1 && sListName2.split("join").length > 1) {
								comCriteriaTO.setListTableName(new String[]{sListName2.split("join")[0].split(" ")[0], sListName2.split("join")[1].split(" ")[0]});
							}
						 }
						 */
				    }
				    
				    
				    String what=findParameterValue(request,"WHAT");
				    if(what!=null && what.trim().length()>0){
				    	comCriteriaTO.setSqlQuery(true);
				    	comCriteriaTO.setSelections(new String[]{what});
				    }
				    String order=findParameterValue(request,"ORDER_BY");
				    if(order!=null && order.trim().length()>0){
				    	comCriteriaTO.setSqlQuery(true);
				    	comCriteriaTO.setOrderBy(order);
				    }
				    startRow=findParameterValue(request,"OFFSET");
				    if(startRow!=null && startRow.trim().length()>0){
				    	comCriteriaTO.setSqlQuery(true);
				    	comCriteriaTO.setStartRow(startRow);
				    }
				    noOfRows=findParameterValue(request,"LIMIT");
				    if(noOfRows!=null && noOfRows.trim().length()>0){
				    	//comCriteriaTO.setSqlQuery(true);
				    	comCriteriaTO.setNoOfRows(noOfRows);
				    	
				    }
			    }else {
				    if(ivoaTapLang != null && ivoaTapLang.trim().equals("ADQL") && ivoaTapQuery != null && ivoaTapQuery.trim().length() > 0) {
				    	System.out.println("query: " + ivoaTapQuery);
				    	comCriteriaTO.setSqlQuery(true);			    
				    	String []ivoaTapAnalysisCompare;
				    	ivoaTapAnalysisCompare = ivoaTapQuery.split(" ");
				    	int adqlFromClause = ivoaTapQuery.toLowerCase().indexOf(" from ");
				    	int adqlWhereClause = ivoaTapQuery.toLowerCase().indexOf("where");
				    	String selectClause = ivoaTapQuery.substring(0,adqlFromClause);
				    	String sqlFromAndWhereClause = ivoaTapQuery.substring(adqlFromClause+5);
				    	boolean adqlAS = false;
				    	boolean adqlWhere = false;
				    	boolean adqlTop = false;
				    	String []adqlTmp;
				    	ivoaTapAnalysisCompare = sqlFromAndWhereClause.split(" ");
				    	System.out.println("sqlFromAndWhereClause: " + sqlFromAndWhereClause);
				    	for(int i = 0;i < ivoaTapAnalysisCompare.length;i++) {
				    		if(ivoaTapAnalysisCompare[i].trim().equalsIgnoreCase("WHERE")) {
				    			adqlWhere = true;
				    		}
				    		if(!adqlWhere) {
				    			if(sListName == null) {sListName = "";}
				    			adqlTmp =  ivoaTapAnalysisCompare[i].split(",");
				    			for(int k = 0;k < adqlTmp.length;k++) {
				    				sListName += adqlTmp[k].trim();
			    					if(k < (adqlTmp.length-1)) { sListName += ","; }
			    				}//for				    		
				    		}//if	
				    	}//for
				    	comCriteriaTO.setListName(sListName);
				    	
		    			if(sListName!=null && !sListName.equals("")){
		    				comCriteriaTO.setListTableName(sListName.split(","));
		    			}
		    			
		    			if(adqlWhereClause > 0) {
		    				whereClause = ivoaTapQuery.substring(adqlFromClause+5);
		    			}
				    	if(selectClause.indexOf("*") == -1) { 
				    		ivoaTapAnalysisCompare = selectClause.split(" ");
				    		System.out.println("ivoa length: " + ivoaTapAnalysisCompare.length);
				    		for(int i = 0;i < ivoaTapAnalysisCompare.length;i++) {
				    			System.out.println("tap analysisecmapre for select: " + ivoaTapAnalysisCompare[i] + " i: " + i );
					    		if(ivoaTapAnalysisCompare[i].trim().equalsIgnoreCase("TOP")) {
					    			adqlTop = true;
					    			
					    		}//if
					    		if(adqlTop) {
					    			try {
					    				
					    				int adqlLimit = Integer.parseInt(ivoaTapAnalysisCompare[i]);
					    				comCriteriaTO.setNoOfRows(ivoaTapAnalysisCompare[i]);
					    			} catch(Exception e) {
					    				
					    			}//try...catch
					    		}else {
					    			
					    			if(ivoaTapAnalysisCompare[i].trim().length() > 0 && adqlAS) {
					    				if(sSelect == null){sSelect = "";}
					    				adqlAS = false;
					    				adqlTmp = ivoaTapAnalysisCompare[i].split(",");
					    				for(int k = 0;k < adqlTmp.length;k++) {
					    					sSelect += adqlTmp[k].trim();
					    					if(k < (adqlTmp.length-1)) { sSelect += ","; }
					    				}
					    			}
					    			if(ivoaTapAnalysisCompare[i].trim().equalsIgnoreCase("AS")) {
					    				adqlAS = true;
					    			}
					    			if(ivoaTapAnalysisCompare[i].trim().length() > 0 && !ivoaTapAnalysisCompare[i].trim().equalsIgnoreCase("SELECT") && !adqlAS) {
					    				if(sSelect == null){sSelect = "";}
					    				adqlTmp = ivoaTapAnalysisCompare[i].split(",");
					    				
					    				for(int k = 0;k < adqlTmp.length;k++) {
					    					System.out.println("adqltmp: " + adqlTmp[k]);
					    					sSelect += adqlTmp[k].trim();
					    					if(k < (adqlTmp.length-1)) { sSelect += ",";}
					    				}
					    			}
					    		}//else
				    		}//for
				    		if(sSelect!=null && !sSelect.trim().equals("")) {
							    	comCriteriaTO.setSelect(sSelect);
							    	comCriteriaTO.setSelections(new String[]{sSelect});
							}
				    	}//if for *
				    	System.out.println("Select: " + sSelect + " From: " + sListName + " where: " + whereClause);
				    }//if
			    }//else
			    System.out.println("calling generation votables");
			    //Calling generate VOTable Details.
				CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
				commonNameDao.generateVOTableDetails(comCriteriaTO);
				System.out.println("votable should be generated");
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
