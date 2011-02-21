package eu.heliovo.queryservice.common.util;

import java.util.regex.Pattern;

import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

public class QueryWhereClauseParser {
	
	private static String whereClauseString="";
	
	/*
	 *Check for null clause. 
	 */
	private static void checkIfNull(String value,String tableName,String join){
		String data[]=value.split(",");
		if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
			if(data.length>1 && data[1].equalsIgnoreCase("null")){
				 checkIfNull(data[0],data[1]);
			}
		}else if(data.length>1 && data[1].equalsIgnoreCase("null") && data[0].trim().contains(tableName)){
				 checkIfNull(data[0],data[1]);
		}else{
			if(data.length>1 && data[1].equalsIgnoreCase("null") && !data[0].trim().contains(".")){
				 checkIfNull(data[0],data[1]);
			}
		}		
	}
	
	/**
	 * Common method of logic
	 * @param columnName
	 * @param value
	 */
	private static void checkIfNull(String columnName,String value){
		whereClauseString=whereClauseString+" "+columnName+" is null"+" AND";
	}
	
	/**
	 * *Check for is not null cluase.
	 * @param value
	 * @param tableName
	 * @param join
	 */
	private static void checkIfNotNull(String value,String tableName,String join){
		String data[]=value.split(",");
		if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
			if(data.length>1 && data[1].equalsIgnoreCase("!null")){
				checkIfNotNull(data[0],data[1]);
			}
		}else if(data.length>1 && data[1].equalsIgnoreCase("!null") && data[0].trim().contains(tableName)){
				checkIfNotNull(data[0],data[1]);
		}else{
			if(data.length>1 && data[1].equalsIgnoreCase("!null") && !data[0].trim().contains("."))
				checkIfNotNull(data[0],data[1]);
		}
	}
	
	/**
	 * Common method of logic
	 * @param columnName
	 * @param value
	 */
	private static void checkIfNotNull(String columnName,String value){
		whereClauseString=whereClauseString+" "+columnName+" is not null"+" AND";
	}
	
	/**
	 * @param value
	 * @param tableName
	 * @param join
	 * Check for is greater clause. 
	 */
	private static void checkIfGreaterThanEqualTo(String value,String tableName,String join){
		String data[]=value.split(",");
		if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
			if(data.length>1 && data[1].endsWith("/")){
				 checkIfGreaterThanEqualTo(data[0],data[1]);
			}
		}else if(data.length>1 && data[0].trim().contains(tableName) && data[1].endsWith("/"))
		{
				checkIfGreaterThanEqualTo(data[0],data[1]);
		}else{
			if(data.length>1 && !data[0].trim().contains(".") && data[1].endsWith("/")){
				checkIfGreaterThanEqualTo(data[0],data[1]);
			}
		}
	}
	/**
	 * @param columnName
	 * @param value
	 * setting values for where clause.
	 */
	private static void checkIfGreaterThanEqualTo(String columnName,String value){
		String sValue=value.replace("/", "");
		if(testAlphaString(sValue))
			sValue="'"+sValue+"'";
		whereClauseString=whereClauseString+" "+columnName+">="+sValue+" AND";
	}
	/*
	 *Check for less then equal to clause. 
	 */	
	private static void checkIfLessThanEqualTo(String value,String tableName,String join){
		String data[]=value.split(",");
		if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
			if(data.length>1 && data[1].startsWith("/")){
				// Value
				checkIfLessThanEqualTo(data[0],data[1]);
			}
		}else if(data.length>1 && data[0].trim().contains(tableName) && data[1].startsWith("/")){
				checkIfLessThanEqualTo(data[0],data[1]);
		}else{
			if(data.length>1 && !data[0].trim().contains(".") && data[1].startsWith("/")){
				checkIfLessThanEqualTo(data[0],data[1]);
			}
		}
	}
	
	/**
	 * 
	 * @param columnName
	 * @param value
	 */
	private static void checkIfLessThanEqualTo(String columnName,String value){
		String sValue=value.replace("/", "");
		if(testAlphaString(sValue))
			sValue="'"+sValue+"'";
			whereClauseString=whereClauseString+" "+columnName+"<="+sValue+" AND";
	}
	
	/*
	 *Check for between clause. 
	 */
	private static void checkIfBetween(String value,String tableName,String join){
		String data[]=value.split(",");
		if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
			if(data.length>1 && !data[1].startsWith("/") && !data[1].endsWith("/") && data[1].split("/").length>1 && data[1].split("/")[1]!=null && data[1].split("/")[1].trim()!=""){
				checkIfBetween(data[0],data[1]);
			}
		}else if(data.length>1 && !data[1].startsWith("/") && !data[1].endsWith("/") && data[1].split("/").length>1 && data[1].split("/")[1]!=null && data[1].split("/")[1].trim()!="" && data[0].trim().contains(tableName)){
				checkIfBetween(data[0],data[1]);
		}else{
			if(data.length>1 && !data[0].trim().contains(".") && !data[1].startsWith("/") && !data[1].endsWith("/") && data[1].split("/").length>1 && data[1].split("/")[1]!=null && data[1].split("/")[1].trim()!=""){
				checkIfBetween(data[0],data[1]);
			}
		}
	}
	
	/**
	 * Check for between clause.
	 * @param columnName
	 * @param value
	 */
	private static void checkIfBetween(String columnName,String value)
	{
		//First Value
		String firstValue=value.split("/")[0];
		//Secound Value
		String secoundValue=value.split("/")[1];
		//Test if it has any char.
		if(testAlphaString(firstValue))
			firstValue="'"+firstValue+"'";
		//Secound Value.
		if(testAlphaString(secoundValue))
			secoundValue="'"+secoundValue+"'";
		whereClauseString=whereClauseString+" "+columnName+" BETWEEN "+firstValue+" AND "+secoundValue+" AND";
	}
	

	/**
	 * Check for 'OR' clause.
	 * @param value
	 * @param tableName
	 * @param join
	 */
	private static void checkIfOR(String value,String tableName,String join){
		String data[]=value.split(",");	
		if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
			checkIfOR(value,tableName);
		}else if(data.length>1 && data[0].trim().contains(tableName)){
			checkIfOR(value,tableName);
		}else{
			if(data.length>1 && !data[0].trim().contains("."))
				checkIfOR(value,tableName);
		}
	}
	
	
	private static void checkIfOR(String value,String tableName){
		String orClause="";
		String data[]=value.split(",");	
		whereClauseString=whereClauseString+" ( ";
		if(data.length>1){
		   for(int i=1;i<data.length;i++){
					if(i==data.length-1)
						orClause="";
					else
						orClause=" OR ";
					//Creating where clause.
					String sValue=data[i];
					//Checking if the value has alpha.
					if(!checkValueIsAColumn(sValue,tableName)){
						if(testAlphaString(sValue))
							sValue="'"+sValue+"'";
					}
					whereClauseString=whereClauseString+data[0]+"="+sValue+orClause;
		    }	
		   whereClauseString=whereClauseString+" ) AND";
		}	
	}
	
	/**
	 * 
	 * @param value
	 * @param tableName
	 * @param join
	 */
	private static void checkIfLike(String value,String tableName,String join){
		String orClause="";
		String data[]=value.split(",");	
		whereClauseString=whereClauseString+" ( ";
		if(data.length>1){
		   for(int i=1;i<data.length;i++){
					if(i==data.length-1)
						orClause="";
					else
						orClause=" OR ";
					//Creating where clause.
					String sValue=data[i];
					//
					if(join!=null && !join.trim().equals("") && join.trim().equals("yes")){
						if(sValue.startsWith("*") && sValue.endsWith("*")){
							checkIfMoreThenOneLikeValue(data[0],sValue,orClause);
						}
					}else if(sValue.startsWith("*") && data[1].endsWith("*") && data[0].trim().contains(tableName)){
						checkIfMoreThenOneLikeValue(data[0],sValue,orClause);
					}else{
						if(sValue.startsWith("*") && data[1].endsWith("*") && !data[0].trim().contains(".")){
							checkIfMoreThenOneLikeValue(data[0],sValue,orClause);
						}
					}
		    }	
		   whereClauseString=whereClauseString+" ) AND";
		}	
	}
	
	/**
	 * 
	 * @param coulumnName
	 * @param value
	 */
	private static void checkIfMoreThenOneLikeValue(String coulumnName,String value,String orClause){ 
		whereClauseString=whereClauseString+" "+coulumnName+" LIKE '"+value.replace("*", "%")+"' "+orClause;
	}
	
	/*
	 *Check for all type of cluase. 
	 */
	private static void checkAllType(String whereClause,String tableName,String join){
		String stringForOr="";
		String stringForLike="";
		String prevColumnName="";
		String columnName="";
		whereClauseString="";
		String[] whereClauseArray=whereClause.split(";");
		for(int count=0;count<whereClauseArray.length;count++){
			prevColumnName="";
			columnName="";
			String 	value=whereClauseArray[count];
			String data[]=value.split(",");
			for(int inCount=1;inCount<data.length;inCount++){
				columnName=data[0];
				if(count(data[inCount],"/")>0){
					checkIfGreaterThanEqualTo(columnName+","+data[inCount],tableName,join);
					checkIfLessThanEqualTo(columnName+","+data[inCount],tableName,join);
					checkIfBetween(columnName+","+data[inCount],tableName,join);
				}else if(count(data[inCount],",")==0 && count(data[inCount],"*")>1){
					if(prevColumnName.equals(columnName) || inCount==1)
						stringForLike=stringForLike+data[inCount]+",";
					else if(inCount!=1 && !prevColumnName.equals(columnName)){
						checkIfLike(columnName+","+stringForLike.substring(0,stringForLike.length()-1),tableName,join);
						stringForLike="";
					}
					//setting column value.
					prevColumnName=columnName;
				}else if(count(data[inCount],"!null")==1){
					checkIfNotNull(columnName+","+data[inCount],tableName,join);					
				}else if(count(data[inCount],"null")==1){
					checkIfNull(columnName+","+data[inCount],tableName,join);	
				}else{
					if(prevColumnName.equals(columnName) || inCount==1)
						stringForOr=stringForOr+data[inCount]+",";
					else if(inCount!=1 && !prevColumnName.equals(columnName)){
						checkIfOR(columnName+","+stringForOr.substring(0,stringForOr.length()-1),tableName,join);
						stringForOr="";
					}
					//setting column value.
					prevColumnName=columnName;
				}
			
			}
			
			//For OR condition.
			if(!stringForOr.equals("")){
				checkIfOR(columnName+","+stringForOr.substring(0,stringForOr.length()-1),tableName,join);
				stringForOr="";
			}
			//For LIKE condition.
			if(!stringForLike.equals("")){
				checkIfLike(columnName+","+stringForLike.substring(0,stringForLike.length()-1),tableName,join);
				stringForLike="";
			}
	   }
	}
		
	/*
	 *Getting the count of a search string. 
	*/
	private static int count(String input, String countString){
        return input.split("\\Q"+countString+"\\E", -1).length - 1;
    }
	
	/*
	 *Pattern search. 
	 */
	private static Pattern p = Pattern.compile("[A-Za-z]");
	public static boolean testAlphaString(String s)
	{		
	   return match(s);
	}
	
	/*
	 *Check the match value. 
	*/
	private static boolean match(String s) {
		return  p.matcher(s).find();
	}


	/**
	 * Check if the value is a column
	 * @param value
	 * @param tableName
	 * @return
	 */
	private static boolean checkValueIsAColumn(String value,String tableName){
		boolean status=false;
		if(value!=null){
			for(int i=0;i<tableName.split(",").length;i++){
				value=value.replace(tableName.split(",")[i]+".", "");
			}
		}
		//Checking if the value is a column name.
		for(int i=0;i<tableName.split(",").length;i++){
			String colNames=ConfigurationProfiler.getInstance().getProperty("sql.columnnames."+tableName.split(",")[i]);
			if(colNames.trim().contains(value))
				status=true;
		}
		return status;
	}
	
	/*
	 *Generate the sql based query.
	 */
	public static String generateWhereClause(CommonCriteriaTO comCriteriaTO){
		//Checking All type of clause
		checkAllType(comCriteriaTO.getWhereClause(),comCriteriaTO.getTableName(),comCriteriaTO.getJoin());
		//Checking if it ends with AND.
		if(whereClauseString!=null && whereClauseString.trim().endsWith("AND") )
			whereClauseString=whereClauseString.substring(0, whereClauseString.length()-3);
		
		return whereClauseString;
	}
	/*
	 *settng null value for all unused variable.. 
	 */
	public static void deAllocateStringToNull(){
		whereClauseString=null;
	}

	/*
	public static void main(String arg[]){
		CommonCriteriaTO comCriteriaTO=new CommonCriteriaTO();
		//String sWhere="ins.vmag,4.5/5.5;ins.imag,4.5/;ins.bmag,/5.5;ins.flag,4,5,6,77y88;vinu,4;obs.jmag,4.5/5.5,/3.0r,I9.0/;ins.name,*Lon*;ins.kmag,4.5/5.5;ins.flux,null;ins.last,1,3,5,u78;ins.flux,!null;obs.xray_class,9999y/X10";
		String sWhere="xray_class,C6/X10;xray_class,C6/;xray_class,/X10;xray_class,C6,X16,F56;xray_class,optical_class;xray_class,*hi*";
		comCriteriaTO.setWhereClause(sWhere);
		comCriteriaTO.setJoin("no");
		comCriteriaTO.setTableName("goes_xray_flare");
		System.out.println(generateWhereClause(comCriteriaTO));
	  }
	*/
}

