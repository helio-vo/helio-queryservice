package eu.heliovo.queryservice.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.org.helio.common.dao.CommonDaoFactory;
import com.org.helio.common.dao.interfaces.CommonDao;
import com.org.helio.common.transfer.criteriaTO.CommonCriteriaTO;

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
		response.setContentType("text/xml;charset=UTF-8");
		 CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
		 PrintWriter printWriter = response.getWriter(); 
		try{
			System.out.println(System.getenv("file.path"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		    comCriteriaTO.setPrintWriter(printWriter);
		    //Setting start time & end time parameter
		    String sStartTime=request.getParameter("STARTTIME");
		    String sEndTime=request.getParameter("ENDTIME");
		   	System.out.println(" sStartTime : "+sStartTime+" sEndTime : "+sEndTime);			
			comCriteriaTO.setStartDateTime(sStartTime);
			comCriteriaTO.setEndDateTime(sEndTime);					
		    //Setting for Instrument parameter.
		    String sInstrument=request.getParameter("INSTRUMENT");
		    comCriteriaTO.setInstruments(sInstrument);
		    //Setting for List Name parameter.
		    String sListName=request.getParameter("FROM");
		    comCriteriaTO.setListName(sListName);
		    //Setting where cluase parameter
		    String whereClause=request.getParameter("WHERE");
		    comCriteriaTO.setWhereClause(whereClause);
		    //Setting start row parameter
		    String startRow=request.getParameter("STARTINDEX");
		    comCriteriaTO.setStartRow(startRow);
		    //Setting no of row parameter
		    String noOfRows=request.getParameter("MAXRECORDS");
		    comCriteriaTO.setNoOfRows(noOfRows);
			CommonDao commonNameDao= CommonDaoFactory.getInstance().getCommonDAO();
			commonNameDao.generateVOTableDetails(comCriteriaTO);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
