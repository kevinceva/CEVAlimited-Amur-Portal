package com.ceva.posta.airtime.insurance.airtime.topup;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.ceva.posta.airtime.insurance.sms.AfricasTalkingGateway;


public class AirtimeRequest {

	
	 public static JSONObject sendAirTimeRequest(String mobileNo,String amount) throws JSONException 
	    {
		 
		 JSONObject responseJSON = new JSONObject();
	       String status=""; 
	        //Specify your credentials
	        String username = "raynerono";
	        String apiKey   = "4219176a58662eb4232034b11150af1280a1ad66b9d47d951a4e389b25e6bbf9";
	        
	        //Specify the phone number/s and amount in the format shown
	        //Specify the country currency and the amount as shown below (+254 for Kenya in this case)
	        // Please ensure you include the country code for phone numbers (KES for Kenya in this case)
	        
	        //Declare an json array to hold the recipients and the amount to send
	        JSONArray recipients = new JSONArray();
	        
	        //Add the first recipient and the amount as a json object to the json array
	        recipients.put(new JSONObject().put("phoneNumber",mobileNo).put("amount","KES "+amount));
	        
	        //Add the second recipient and the amount as a json object to the json array
	        //recipients.put(new JSONObject().put("phoneNumber","+254733YYYZZZ").put("amount","KES 10"));
	        
	        //Convert the recipient array into a string. The string produced will have the format:
	        // [{"amount":"KES 100", "phoneNumber":+254711XXXYYY},{"amount":"KES 100", "phoneNumber":+254733YYYZZZ}]
	        //A string with the shown format may be created directly and skip the above steps
	        String recipientStringFormat = recipients.toString();
	        
	        //Create an instance of our awesome gateway class and pass your credentials
	        AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey);
	        
	        // Thats it, hit send and we'll take care of the rest. Any errors will
	   // be captured in the Exception class as shown below
	   
	   try {
	       
	       //That's it. Hit send and we will handle the rest 
	       JSONArray results = gateway.sendAirtime(recipientStringFormat);
	       int length = results.length();
	       for(int i = 0; i < length; i++) {
	        JSONObject result = results.getJSONObject(i);
	        System.out.println(result.getString("status"));
	        System.out.println(result.getString("amount"));
	        System.out.println(result.getString("phoneNumber"));
	        System.out.println(result.getString("discount"));
	        System.out.println(result.getString("requestId"));
	        
	        //Error message is important when the status is not Success
	        System.out.println(result.getString("errorMessage"));
	        
	        status=result.getString("status");
	        
	        responseJSON.put("STATUS", result.getString("status"));
	        responseJSON.put("REQUEST_ID", result.getString("requestId"));
	        responseJSON.put("ERROR_MSG", result.getString("errorMessage"));
	       }
	   }
	   catch(Exception e){
	       System.out.println(e.getMessage());
	   }
	   
	   return responseJSON;
   }

	
}
