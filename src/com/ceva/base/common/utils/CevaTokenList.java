package com.ceva.base.common.utils;

public class CevaTokenList {

	public static String[] mdetails={
			"30d8ab67b330cf8b88527a9748c6c1fb","Ceva",
			"90554ec7584c1460e1935d49efe471c5","AgencyBanking",
			"0692f35ea0022e41dc0c23ee3b12ab9f","MMS",
			"db1b73ab06bd303c6dbc1ad332c16636","Prepaid"
	};
	
	public static String getMethod(String task){
		String methoName=null;
		for(int i=0;i<mdetails.length;i++){
			String md5hashdata=mdetails[i];
			if(task.equals(md5hashdata)){
				methoName=mdetails[i+1];
			}
		}
		return methoName;
	}
	
	public static void main(String args[]){
			System.out.println(getMethod("970c7956028654ac329b12c10b112058"));
	}
}
