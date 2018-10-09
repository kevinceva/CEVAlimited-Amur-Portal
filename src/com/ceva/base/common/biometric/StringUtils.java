package com.ceva.base.common.biometric;
public class StringUtils {

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

	public static void main(String[] args) {
		String data = null;
		//data = "  ";
		//data = "null";
		data = "data";

		System.out.println(isValidString(data));
	}
}
