package com.ceva.posta.airtime.insurance.airtime.topup;

import org.json.JSONException;
import org.json.JSONObject;

public class AirtimeTopup {

	public String msisdn;
	public String amount;
	static org.json.JSONObject responseJSON=null;
	
	public static JSONObject sendAirtimeToupRequest(String msisdn,String amount){
		
		try {
			responseJSON=AirtimeRequest.sendAirTimeRequest(msisdn,amount);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseJSON;
	}
	
	
	public static void main(String args[]){
		
		String result;
		
		try {
			responseJSON=sendAirtimeToupRequest("23224234","2323");
			if(responseJSON.getString("STATUS").equals("Failed")){
				result = "Failed";
			}else {
				result = "Success";
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}	
	
	
}
