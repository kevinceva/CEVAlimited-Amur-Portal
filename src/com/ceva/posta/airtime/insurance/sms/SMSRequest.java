package com.ceva.posta.airtime.insurance.sms;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SMSRequest {

	public  String resultStatus;
	
	Logger logger=Logger.getLogger(SMSRequest.class);
	
	public String sendSMS(String mobileNumber,String smsMsg){
		
			System.out.println("inside send SMS ...................");
		  	 String username = "raynerono";
	         String apiKey   = "4219176a58662eb4232034b11150af1280a1ad66b9d47d951a4e389b25e6bbf9";
	    
	         
	        // Create a new instance of our awesome gateway class
	         AfricasTalkingGateway gateway  = new AfricasTalkingGateway(username, apiKey);
	    
	         // Thats it, hit send and we'll take care of the rest. Any errors will
	         // be captured in the Exception class below
	        try {
	        	
	        	logger.debug("inside SMSRequest========================");
	        	logger.debug("mobileNumber:"+mobileNumber);
	        	logger.debug("smsMsg:"+smsMsg);
	        	
	            JSONArray results = gateway.sendMessage(mobileNumber, smsMsg);
	            
	            logger.debug("results::"+results);
	            
	            for( int i = 0; i < results.length(); ++i ) {
	                  JSONObject result = results.getJSONObject(i);
	                  resultStatus=result.getString("status");
	                  logger.debug("Response Status::"+result.getString("status") + ","); // status is either "Success" or "error message"
	                  logger.debug(result.getString("number") + ",");
	                  logger.debug(result.getString("messageId") + ",");
	                  logger.debug(result.getString("cost"));
	        }
	       }
	       
	       catch (Exception e) {
	        System.out.println("Encountered an error while sending " + e.getMessage());
	        }
	 
	        logger.debug("resultStatus==>"+resultStatus);
	        
		return resultStatus;
		
	}
	
	
	/*public static void main(String args[]){
		sendSMS("254726605242","Hello ....................");
	}*/
}

