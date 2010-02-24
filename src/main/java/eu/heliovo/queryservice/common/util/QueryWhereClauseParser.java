package com.org.helio.common.util;

import com.org.helio.common.transfer.criteriaTO.CommonCriteriaTO;

public class QueryWhereClauseParser {
	
	private static String whereClauseString="";
		
	private static void checkIfNull(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].equalsIgnoreCase("null")){
			whereClauseString=whereClauseString+" "+data[0]+" is null"+" AND";
		}
		
	}
	
	private static void checkIfNotNull(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].equalsIgnoreCase("!null")){
			whereClauseString=whereClauseString+" "+data[0]+" is not null"+" AND";
		}
		
	}
	
	private static void checkIfGreaterThanEqualTo(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].endsWith("/")){
			whereClauseString=whereClauseString+" "+data[0]+">="+data[1].replace("/", "")+" AND";
		}
		
	}
	

	private static void checkIfLessThanEqualTo(String value){
		String data[]=value.split(",");
		if(data.length>1 && data[1].startsWith("/")){
			whereClauseString=whereClauseString+" "+data[0]+"<="+data[1].replace("/", "")+" AND";
		}
		
	}
	
	private static void checkIfBetween(String value){
		String data[]=value.split(",");
		if(data.length>1 && !data[1].startsWith("/") && !data[1].endsWith("/") && data[1].split("/").length>1 && data[1].split("/")[1]!=null && data[1].split("/")[1].trim()!=""){
			whereClauseString=whereClauseString+" "+data[0]+" BETWEEN "+data[1].split("/")[0]+" AND "+data[1].split("/")[1]+" AND ";
		}
		
	}
	
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
					whereClauseString=whereClauseString+data[0]+"="+data[i]+orClause;
		    }	
		   whereClauseString=whereClauseString+" ) AND";
		}
		
		
	}
		
	private static void checkIfLike(String value){ 
		String data[]=value.split(",");
		if(data.length>1 && data[1].startsWith("*") && data[1].endsWith("*")){
			whereClauseString=whereClauseString+" "+data[0]+" LIKE '"+data[1].replace("*", "%")+"' AND";
		}
		
	}
		
	// Not in use now.
	private static void checkIfEqual(String value){
		String data[]=value.split(",");
		if(data.length>1){
			whereClauseString=whereClauseString+" "+data[0]+"="+data[1]+" AND";
		}
		
	}

	//Yet to be done.
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
		
	// Count of search string.
	private static int count(String input, String countString){
        return input.split("\\Q"+countString+"\\E", -1).length - 1;
    }
	
	// check is string is a join query
	public static boolean checkIfJoinQuery(CommonCriteriaTO comCriteriaTO)
	{
		boolean status=false;
		String sWhereClause=comCriteriaTO.getWhereClause();
		
		if(count(sWhereClause,"J.")>0){
			status=true;
		}
		return status;
	}
	
	public static String generateWhereClause(String sWhereClause){
		//Checking All type of cluase
		checkAllType(sWhereClause);
		//Checking if it ends with AND.
		if(whereClauseString.endsWith("AND"))
			whereClauseString=whereClauseString.substring(0, whereClauseString.length()-3);
		
		return whereClauseString;
	}
	
	public static void deAllocateStringToNull(){
		whereClauseString=null;
	}

	/*
	public static void main(String arg[]){
		String sWhere="vmag,4.5/5.5;imag,4.5/;bmag,/5.5;flag,4,5,6;jmag,4.5/5.5,/3.0,9.0/;name,*Lon*;kmag,4.5/5.5;flux,null;last,1,3,5;flux,!null";
		//String sWhere="flag,4,5,6;last,1";
		System.out.println(generateWhereClause(sWhere));
	  }
	*/
	
}

