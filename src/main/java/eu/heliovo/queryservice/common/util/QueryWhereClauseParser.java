package eu.heliovo.queryservice.common.util;

import java.util.regex.Pattern;

import eu.heliovo.queryservice.common.transfer.criteriaTO.CommonCriteriaTO;

public class QueryWhereClauseParser {
	
	private static String whereClauseString="";
	
	/*
	 *Check for null cluase. 
	 */
	private static void checkIfNull(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].equalsIgnoreCase("null")){
			whereClauseString=whereClauseString+" "+data[0]+" is null"+" AND";
		}
		
	}
	
	/*
	 *Check for is not null cluase. 
	 */
	private static void checkIfNotNull(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].equalsIgnoreCase("!null")){
			whereClauseString=whereClauseString+" "+data[0]+" is not null"+" AND";
		}
		
	}
	
	/*
	 *Check for is greater cluase. 
	 */
	private static void checkIfGreaterThanEqualTo(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].endsWith("/")){
			// Value
			String sValue=data[1].replace("/", "");
			if(testAlphaString(sValue))
				sValue="'"+sValue+"'";
			whereClauseString=whereClauseString+" "+data[0]+">="+sValue+" AND";
		}
		
	}
	
	/*
	 *Check for less then equal to cluase. 
	 */	
	private static void checkIfLessThanEqualTo(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].startsWith("/")){
			// Value
			String sValue=data[1].replace("/", "");
			if(testAlphaString(sValue))
				sValue="'"+sValue+"'";
			whereClauseString=whereClauseString+" "+data[0]+"<="+sValue+" AND";
		}
		
	}
	
	/*
	 *Check for between cluase. 
	 */
	private static void checkIfBetween(String value){
		String data[]=value.split(",");
		if(data.length>1 && !data[1].startsWith("/") && !data[1].endsWith("/") && data[1].split("/").length>1 && data[1].split("/")[1]!=null && data[1].split("/")[1].trim()!=""){
			//First Value
			String firstValue=data[1].split("/")[0];
			//Secound Value
			String secoundValue=data[1].split("/")[1];
			//Test if it has any char.
			if(testAlphaString(firstValue))
				firstValue="'"+firstValue+"'";
			//Secound Value.
			if(testAlphaString(secoundValue))
				secoundValue="'"+secoundValue+"'";
			whereClauseString=whereClauseString+" "+data[0]+" BETWEEN "+firstValue+" AND "+secoundValue+" AND";
		}
		
	}
	
	/*
	 *Check for 'or' cluase. 
	 */
	private static void checkIfOR(String value){
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
					if(testAlphaString(sValue))
						sValue="'"+sValue+"'";
					whereClauseString=whereClauseString+data[0]+"="+sValue+orClause;
		    }	
		   whereClauseString=whereClauseString+" ) AND";
		}
		
		
	}
	
	/*
	 *Check for like cluase. 
	 */
	private static void checkIfLike(String value){ 
		String data[]=value.split(",");
		if(data.length>1 && data[1].startsWith("*") && data[1].endsWith("*")){
			whereClauseString=whereClauseString+" "+data[0]+" LIKE '"+data[1].replace("*", "%")+"' AND";
		}
		
	}

	/*
	 *Check for equals to cluase. 
	 *		
	// Not in use now.
	 */
	private static void checkIfEqual(String value){
		String data[]=value.split(",");
		if(data.length>1){
			whereClauseString=whereClauseString+" "+data[0]+"="+data[1]+" AND";
		}
		
	}

	/*
	 *Check for all type of cluase. 
	 */
	private static void checkAllType(String whereClause){
		String stringForOr="";
		String prevColumnName="";
		String coulumnName="";
		whereClauseString="";
		String[] whereClauseArray=whereClause.split(";");
		for(int count=0;count<whereClauseArray.length;count++){
			prevColumnName="";
			coulumnName="";
			String 	value=whereClauseArray[count];
			String data[]=value.split(",");
			for(int inCount=1;inCount<data.length;inCount++){
				coulumnName=data[0];
				if(count(data[inCount],"/")>0){
					checkIfGreaterThanEqualTo(coulumnName+","+data[inCount]);
					checkIfLessThanEqualTo(coulumnName+","+data[inCount]);
					checkIfBetween(coulumnName+","+data[inCount]);
				}else if(count(data[inCount],",")==0 && count(data[inCount],"*")>1){
					checkIfLike(coulumnName+","+data[inCount]);
				}else if(count(data[inCount],"!null")==1){
					checkIfNotNull(coulumnName+","+data[inCount]);					
				}else if(count(data[inCount],"null")==1){
					checkIfNull(coulumnName+","+data[inCount]);	
				}else{
					if(prevColumnName.equals(coulumnName) || inCount==1)
						stringForOr=stringForOr+data[inCount]+",";
					else if(inCount!=1 && !prevColumnName.equals(coulumnName)){
						checkIfOR(coulumnName+","+stringForOr.substring(0,stringForOr.length()-1));
						stringForOr="";
					}
					//setting column value.
					prevColumnName=coulumnName;
				}
			
			}
			
			//For OR condition.
			if(!stringForOr.equals("")){
				checkIfOR(coulumnName+","+stringForOr.substring(0,stringForOr.length()-1));
				stringForOr="";
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
	
	/*
	 *Check for join query. 
	*/
	public static boolean checkIfJoinQuery(CommonCriteriaTO comCriteriaTO)
	{
		boolean status=false;
		String sWhereClause=comCriteriaTO.getWhereClause();
		
		if(count(sWhereClause,"J.")>0){
			status=true;
		}
		return status;
	}
	/*
	 *Generate the sql based query.
	 */
	public static String generateWhereClause(String sWhereClause){
		//Checking All type of cluase
		checkAllType(sWhereClause);
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
		String sWhere="vmag,4.5/5.5;imag,4.5/;bmag,/5.5;flag,4,5,6,77y88;vinu,4;jmag,4.5/5.5,/3.0r,I9.0/;name,*Lon*;kmag,4.5/5.5;flux,null;last,1,3,5,u78;flux,!null;xray_class,9999y/X10";
		//String sWhere="xray_class,C6/X10";
		System.out.println(generateWhereClause(sWhere));
	  }
	*/

}

