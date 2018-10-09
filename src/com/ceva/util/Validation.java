package com.ceva.util;

/**
 * @author Sravana Kumar B
 */

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class Validation {
	
	
	public static Logger logger=Logger.getLogger(Validation.class);
	public static HashMap<String, String> hashmap = new HashMap<String, String>();
	
	
	
	
	
	public static boolean numberValidation(String str)
	{
		
		boolean status=true;
		
		try
		{
			
			double val = Double.parseDouble(str);
			
		}catch(Exception e)
		{
			status=false;
		}
		
		return status;
		
	}
	

	public static boolean dateValidation(String str,String dateFormate)
	{

		boolean status = true;
		
		final SimpleDateFormat  sdf = new SimpleDateFormat(dateFormate);
		sdf.setLenient(true);
		Date date = null;
		try {
		  date = sdf.parse(str);
		} catch (Exception e) {
		 // e.printStackTrace();
		  status=false;
		}
				
		return status;
		
	}

	
	public static java.sql.Date getDate(String formate,String input) throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat(formate);
		Date parsed = format.parse(input);
		java.sql.Date sqldate = new java.sql.Date(parsed.getTime());

		return sqldate;
	}
	
	
	public static java.sql.Date getSqlDate()
	{
		long time = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(time);
		return date;
	}
	
	public static String convertDateToString(java.util.Date mydate,String formate)
	{
		
		SimpleDateFormat formatter5=new SimpleDateFormat(formate);
		String formats = formatter5.format(mydate);
		return formats;
		
	}
	
	
	public static boolean dateDiffValidation(Date date1,Date date2)
	{
		boolean status=true;
		
		if(date1.compareTo(date2)>0){
			//logger.debug("Date1 is after Date2");
			logger.info("Date1 is greater than Date2");
			status=false;
		}else if(date1.compareTo(date2)<0){
			//logger.debug("Date1 is before Date2");
			logger.info("Date1 is less than Date2");
			status=false;
		}else if(date1.compareTo(date2)==0){
			//logger.debug("Date1 is equal to Date2");
			logger.info("both are same dates");
			status=true;
		}
		
		return status;
	}
	
	public static JSONArray buildErrorJsonObj(ArrayList<String> errorList,JSONArray errorresarayJSON)
	{
		
		logger.info("Functional Error Araay Json ["+errorresarayJSON+"]");
		for (String string : errorList) {
			
			String []errorData = string.split("-");
			
			if(errorData.length==2)
			{
				JSONObject errorresJSON=new JSONObject();
				errorresJSON.put("uploadErrors", errorData[1]);
				errorresJSON.put("excelRowNo", errorData[0]);
				errorresarayJSON.add(errorresJSON);
				
			}
			
		}
		
		return errorresarayJSON;
		
	}
	
	
	
	
	public static String trimSpclChar(String str)
	{
	
		String regexp = "[^a-zA-Z]+";
		
		str = str.replaceAll(regexp, "").replaceAll("\\s+", "+");
		
		return str;
		
	}
	
	public static HashMap<String, String> getMap(JSONArray jsonarray)
	{
		
		logger.info("Welcoe to Get Map Program Input json array ["+jsonarray+"]");
		
		for (int i = 0; i < jsonarray.size(); i++) {
			
			JSONObject jsonobj = jsonarray.getJSONObject(i);
			String dbval = jsonobj.getString("DESCRIPTION");
			String trimval = trimSpclChar(dbval).toUpperCase();
			hashmap.put(trimval, dbval);
			
		}
		
		logger.info("End Of get map program output map ["+hashmap+"]");
		
		return hashmap;
		
	}
	
	public static String validateRelation(String xlsrelation)
	{
		
		String finaldata="ERROR";
	
		xlsrelation = trimSpclChar(xlsrelation).toUpperCase();
		
		if(hashmap.containsKey(xlsrelation))
		{
			finaldata=hashmap.get(xlsrelation);
		}
		
		return finaldata;
		
	}
	
	public static void main(String[] args) {
		
		logger.debug("hai");
		//logger.debug(dateValidation("10/14/2012","M/dd/yyyy"));
		logger.debug("bye");
		
	}



	
	
	
	/*public static boolean checkmembempidValidation(String policyNo,String memberid,String empId,long numRenewalHID)
	{

		boolean status = true;
		
		 try
		  {			 
			
			 String query5="SELECT COUNT(*) FROM POLICY_RENEWAL_MEMBER_DELETE WHERE POLICYUW_NO=:POLNO AND MEMBERID=:MID and EMP_NO=:EID and RENEWAL_HID=:RHID";

			 Query q5 = se.createSQLQuery(query5);
			 q5.setParameter("POLNO", policyNo);
			 q5.setParameter("MID", memberid);
			 q5.setParameter("EID", empId);
			 q5.setParameter("RHID", (numRenewalHID-1));

			 List l5 = q5.list();
			 if(!l5.isEmpty()){
				 
				 BigDecimal count = (BigDecimal)l5.get(0);
				 
				 if(count.intValue() == 0)
				 {	
					 status = false;
				 }else{
					 status = true;
				 }			 
			}
		  }catch(Exception e)
		  {
		   
		   e.printStackTrace();
		  }finally{
			  HibernateSessionUtil.closeSession();
		  }
		  
				
		return status;
		
	}	*/
	
	

	
}
