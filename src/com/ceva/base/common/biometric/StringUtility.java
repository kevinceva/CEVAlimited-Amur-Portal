package com.ceva.base.common.biometric;

import java.util.StringTokenizer;

public class StringUtility {
	public static boolean isValidString(String data)
	{
		boolean bool = false;
		if(null == data)
		{
			return false;
		}
		if("null".equals(data))
		{
			return false;
		}
		if (data != null && data.trim().length()>0)
		{
			bool = true;
		}
		
		
		return bool;
	}
	
	public static String Split(String str, int indx , String delim)
	{
		String resp= "0000";
		if(!isValidString(str))
		{
			return "0000";
		}
		
		String [] chopit = str.split(delim);
		if(chopit.length>indx)
		{
			resp= chopit[indx];
		}
		return resp;
	}

	public static void main(String[] args) {
		String data = null;
		//data = "  ";
		//data = "null";
		//data = "data";
		
		//System.out.println(isValidString(data));
		
		System.out.println(Split("Raj||Kumar||pandey", 2, "\\|\\|"));
	}
}
