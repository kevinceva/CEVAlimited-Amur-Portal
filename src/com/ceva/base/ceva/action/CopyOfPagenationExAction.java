package com.ceva.base.ceva.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

public class CopyOfPagenationExAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSONObject resJson = null;
	
	int iTotalDisplayRecords = 10;
	
	public String commonMethod(){
		resJson = new JSONObject();
		
		JSONArray jsonArr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("TEXT1", "ONe");
		json.put("TEXT2", "Two");
		json.put("TEXT3", "Three");
		
		jsonArr.add(json);
		
		 json = new JSONObject();
		 int i=1;
		 for(;i<12 && jsonArr.size()< iTotalDisplayRecords;i++){
			 json.put("TEXT1", i+"1");
				json.put("TEXT2", i+"2");
				json.put("TEXT3", i+"3");
				if(jsonArr.size()< iTotalDisplayRecords)
					jsonArr.add(json); 
		 }
		resJson.put("sEcho", "1"); 
		resJson.put("iTotalRecords", "50");
		resJson.put("iTotalDisplayRecords", "30");
		resJson.put("aaData", jsonArr);
		
		System.out.println("Response JSON:::"+resJson);
		
		return "success";
	}
	
	public JSONObject getResJson() {
		return resJson;
	}
	public void setResJson(JSONObject resJson) {
		this.resJson = resJson;
	}

	
	
}
