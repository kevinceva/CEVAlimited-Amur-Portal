package com.ceva.base.ceva.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.HttpPostRequestHandler;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RiderAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(OrderAction.class);
	
	ResourceBundle resourceBundle=ResourceBundle.getBundle("pathinfo_config");
		
	public String serverIp = resourceBundle.getString("AMUR_WEB_SERVICE");
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject responseJSON2 = null;
	JSONObject responseJSON3 = null;
	JSONObject fullJourneyObject = null;
		
	JSONObject resultSet = null;
	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	ResponseDTO responseDTO2 = null;
	
	private HttpServletRequest httpRequest = null;
	private HttpSession httpSession = null;
	
	public String riderid;
	public String type;
	private String riderInfoPage = null;
	
	/*public String userId;
	public String confirm_fname;
	public String confirm_lname;
	public String confirm_id;
	public String confirm_mobile;
	public String confirm_email;*/
	
	public String userId;
	private String riderFname;
	private String riderLname;
	private String riderId;
	private String riderMobile;
	private String riderEmail;
	
	private String orderList = null;
	
	private String assign_order_Id = null;
	
	
	
	public String fetchRiders() {
		
		
		try {
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/fetchriders";

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("Riders List [" + obj + "] obj to string["+obj.toString()+"]");
			responseJSON = obj;
			result = obj.getString("STATUS");		
			
			result = "success";	
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
			
		return result;
	}
	
	public String callServlet(){
		
		logger.debug("inside [RiderAction][callServlet].. ");
		
		
		try {
						
			result = "success";
			logger.info("[RiderAction][callServlet result..:"+result);
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [RiderAction][callServlet] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}	
	
	public String fetchRiderActivity() {		
		logger.info("Fetching Riders Activity");
		
		return result;
	}
	
	public String riderInformation(){
		logger.debug("Inside OfferInformation");
		ArrayList<String> errors = null;
		OrdersDAO newOrdersDAO = null;
		try {
			
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			newOrdersDAO = new OrdersDAO();
			responseDTO = newOrdersDAO.fetchUnassignedOrders(requestDTO);
					
			String riderid = getRiderid();
			String type = getType();
			
			System.out.println("prdid ["+riderid+"] type["+type+"]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/viewrider/"+riderid;
			String webServiceURL2 = serverIp+"/amurcore/amur/rider/fetchassignedorders/"+riderid;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			String json2 = httpPostRequestHandler.sendRestPostRequest(webServiceURL2);
			JSONObject obj = JSONObject.fromObject(json1);
			JSONObject obj2 = JSONObject.fromObject(json2);
			logger.debug("Rider String [" + obj + "] obj to string["+obj.toString()+"]");
			responseJSON = obj;
			responseJSON2 = obj2;
			responseJSON.put("type", type);
			
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON3 = (JSONObject) responseDTO.getData().get("NEW_ORDERS");
				System.out.println("nEW ORDERS Response JSON :: "+responseJSON3);
				result = "success";	
				
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			
			if (getType().equalsIgnoreCase("Modify")) {
				riderInfoPage = "riderModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				riderInfoPage = "riderViewInformation";
			} 
		
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}

		return result;
	}
	
	public String modRider() {
		
		session = ServletActionContext.getRequest().getSession();
		String userId = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		String riderid = getRiderid();
		String riderFirstName = getRiderFname();
		String riderLastName = getRiderLname();
		String riderMobile = getRiderMobile();
		String riderEmail = getRiderEmail();
		String riderIdNumber = getRiderId();
		
		try {			
			
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/modifyrider/"+riderid+"/"+riderFirstName+"/"+riderLastName+"/"+riderMobile+"/"+riderEmail+"/"+riderIdNumber+"/"+userId;
			
			logger.debug("Web Service URL  :::: " + webServiceURL);
			String modifyRiderJson = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject modifyRiderObject = JSONObject.fromObject(modifyRiderJson);
			logger.debug("Modify Rider [" + modifyRiderObject + "] obj to string["+modifyRiderObject.toString()+"]");
			responseJSON = modifyRiderObject;	
			System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
			
			if("SUCCESS".equalsIgnoreCase(modifyRiderObject.getString("STATUS")))
			{	
				responseJSON.put("remarks", "SUCCESS");
				result = "success";
			}
			else
			{	
				responseJSON.put("remarks", "FAILURE");
				result = "fail";
			}
			
		} catch (Exception e) {
			result = "fail";
			responseJSON.put("remarks", "FAILURE");
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
			
		return result;
	}
	
	public String addRider() {
		
		session = ServletActionContext.getRequest().getSession();
		String userId = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		String riderFirstName = getRiderFname();
		String riderLastName = getRiderLname();
		String riderMobile = getRiderMobile();
		String riderEmail = getRiderEmail();
		String riderIdNumber = getRiderId();
			
		try {			
			
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/createrider/"+riderFirstName+"/"+riderLastName+"/"+riderMobile+"/"+riderEmail+"/"+riderIdNumber+"/"+userId;
			
			logger.debug("Web Service URL  :::: " + webServiceURL);
			String addRiderJson = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject addRiderObject = JSONObject.fromObject(addRiderJson);
			logger.debug("Create Rider [" + addRiderObject + "] obj to string["+addRiderObject.toString()+"]");
			responseJSON = addRiderObject;
			
			if("SUCCESS".equalsIgnoreCase(addRiderObject.getString("STATUS")))
			{	
				responseJSON.put("remarks", "SUCCESS");
				result = "success";
			}
			else
			{	
				responseJSON.put("remarks", "FAILURE");
				result = "fail";
			}
			
		} catch (Exception e) {
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
			
		return result;
	}
	
	public String fetchRiderOrders(){
		System.out.println("Inside Rider Order Information");
		ArrayList<String> errors = null;
		try {
					
			String riderid = getRiderid();
			
			System.out.println("riderid ["+riderid+"] type["+type+"]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/fetchassignedorders/"+riderid;
						
			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("Rider String [" + obj + "] obj to string["+obj.toString()+"]");
			responseJSON = obj;
			responseJSON.put("type", type);
			result = "success";		
			System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
								
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}

		return result;
	}
	
	public String activateDeactivateRider() {
		logger.info("Inside Rider Activation");
		try {
			
			String riderid = getRiderId();			
			System.out.println("Rider ID :: "+riderid);
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/activatedeactivaterider/"+riderid;
			
			logger.debug("Activate Deactivate Rider service  :::: " + webServiceURL);
			String activateRiderJson = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject activateRiderObj = JSONObject.fromObject(activateRiderJson);
			logger.debug("Rider String [" + activateRiderObj + "] obj to string["+activateRiderObj.toString()+"]");
			responseJSON = activateRiderObj;
			
			if("SUCCESS".equalsIgnoreCase(activateRiderObj.getString("STATUS")))
			{	
				responseJSON.put("remarks", "SUCCESS");
				result = "success";
			}
			else
			{	
				responseJSON.put("remarks", "FAILURE");
				result = "fail";
			}
			
		}catch(Exception e) {
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		}
		return result;
	}
	
	public String assignOrder() {
		logger.info("Inside Rider Activation");
		try {
			session = ServletActionContext.getRequest().getSession();
			String user_Id = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
			String order_id = getAssign_order_Id();
			String riderId = getRiderid();
			System.out.println("user ["+order_id+"] order ["+order_id+"] rider["+riderId+"]");
			
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/assignorerstorider/"+riderId+"/"+order_id+"/"+user_Id;
			
			logger.debug("Activate Deactivate Rider service  :::: " + webServiceURL);
			String activateRiderJson = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject activateRiderObj = JSONObject.fromObject(activateRiderJson);
			logger.debug("Rider String [" + activateRiderObj + "] obj to string["+activateRiderObj.toString()+"]");
			responseJSON = activateRiderObj;
			
			if("SUCCESS".equalsIgnoreCase(activateRiderObj.getString("STATUS")))
			{	
				responseJSON.put("remarks", "SUCCESS");
				result = "success";
			}
			else
			{	
				responseJSON.put("remarks", "FAILURE");
				result = "fail";
			}
			
			System.out.println("ResponseJSON :: "+responseJSON.toString());
			
		}catch(Exception e) {
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		}
		return result;
	}
	
	public String assignOrders() {
		logger.info("Inside assignOrder");
		ArrayList<String> errors = null;
		
		try {
			session = ServletActionContext.getRequest().getSession();
			String user_Id = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();	
			String orderList = getOrderList();
			String riderId = getRiderid();
			String[] orderArray =  orderList.split("/,");
			
			for(int i=0; i< orderArray.length; i++){
				String order_number = orderArray[i];
				System.out.println("prdid ["+order_number+"] rider["+riderId+"]");
				HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
				String webServiceURL = serverIp+"/amurcore/amur/rider/assignorerstorider/"+riderId+"/"+order_number+"/"+user_Id;
				
				logger.debug("Web Service URL  :::: " + webServiceURL);
				String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
				result = "success";
			}
			
			
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}
		
		return result;
	}
	
	public String fetchETA() {
		System.out.println("Inside fetch ETA");
		ArrayList<String> errors = null;	
		OrdersDAO fetchDetail = null;
		String orderList = null;
		String[] orderArray = null;
		
		try {			
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			System.out.println("New Order List ::"+orderList);
			orderList = getOrderList();
			//System.out.println("New Order List ::"+orderList);
			String originArea = "9+West,+Mkungu+Cl,+Nairobi,+Kenya";
			String rawList = orderList;
			
			//Splitting ORDER_ID string from form submission
			System.out.println("Raw List ::"+rawList);
			orderArray =  orderList.split("/,");
			
			JSONObject journeyObject = new JSONObject();
			JSONArray journeyArray = new JSONArray();
			fullJourneyObject = new JSONObject();
			
			//Looping through the ORDER_ID array 
			for(int i=0; i< orderArray.length; i++){
				String order_number = orderArray[i];
				System.out.println("Order Number :: "+order_number);
				String cleanOrderId = order_number.replace(',',' ');
				
				System.out.println("Order Number :: "+cleanOrderId);
				requestJSON.put("ORDID", cleanOrderId);
				requestDTO.setRequestJSON(requestJSON);
				fetchDetail = new OrdersDAO();
				
				responseDTO = fetchDetail.orderInformation(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get("ORDER_INFO");
					
					logger.info("Response JSON is "+responseJSON.toString());
					
					JSONArray resultArray = responseJSON.getJSONArray("CUSTOMER_INFO");//Getting Array from object				
					JSONObject resultObject = resultArray.getJSONObject(0); //Get object from array
					
					String shipping_address = resultObject.getString("SHIPPING_ADDRESS");
					String new_shipping_address = shipping_address.replaceAll(" ", "+");//Using the last destination as the new origin
					System.out.println("Shipping Address :: "+shipping_address);
					
					//Api to compute distance from one point to the next
					HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
					String webServiceURL = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="+originArea+"&destinations="+new_shipping_address+"&key=AIzaSyDb49z4qUXWmz6SUSJbPdFdRLM9mEsmftA";
					logger.debug("Distance URL  :::: " + webServiceURL);
					String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
					JSONObject obj = JSONObject.fromObject(json1);
					
					JSONArray locationArray = obj.getJSONArray("rows");
					JSONObject locationObject = locationArray.getJSONObject(0);
					JSONArray elementArray = locationObject.getJSONArray("elements");
					JSONObject elementObject = elementArray.getJSONObject(0);
					JSONObject distanceObject = elementObject.getJSONObject("distance");
					JSONObject durationObject = elementObject.getJSONObject("duration");
					String time = durationObject.getString("text");
					String distance = distanceObject.getString("text");
					
					journeyObject.put("DISTANCE", distance);
					journeyObject.put("TIME", time);
					journeyObject.put("ORDER_NUMBER", order_number);
					journeyArray.add(journeyObject);
					
					originArea = new_shipping_address;
					result = "success";
				} else {
					errors = (ArrayList<String>) responseDTO.getErrors();
					for (int j = 0; j < errors.size(); j++) {
						addActionError(errors.get(j));
					}
					result = "fail";
				}
			}			
			
			fullJourneyObject.put("JOURNEY", journeyArray);
			
		}catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Order Information [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		}finally {
			requestDTO = null;
			requestJSON = null;
			orderList = null;
		}	
		
		return result;
	}
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	
	public JSONObject getFullJourneyObject() {
		return fullJourneyObject;
	}

	public void setFullJourneyObject(JSONObject fullJourneyObject) {
		this.fullJourneyObject = fullJourneyObject;
	}
	
	public JSONObject getResponseJSON2() {
		return responseJSON2;
	}

	public void setResponseJSON2(JSONObject responseJSON2) {
		this.responseJSON2 = responseJSON2;
	}
	
	public JSONObject getResponseJSON3() {
		return responseJSON3;
	}

	public void setResponseJSON3(JSONObject responseJSON3) {
		this.responseJSON3 = responseJSON3;
	}

	public String getRiderid() {
		return riderid;
	}

	public void setRiderid(String riderid) {
		this.riderid = riderid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRiderInfoPage() {
		return riderInfoPage;
	}

	public void setRiderInfoPage(String riderInfoPage) {
		this.riderInfoPage = riderInfoPage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

	public String getOrderList() {
		return orderList;
	}

	public void setOrderList(String orderList) {
		this.orderList = orderList;
	}

	public String getRiderFname() {
		return riderFname;
	}

	public void setRiderFname(String riderFname) {
		this.riderFname = riderFname;
	}

	public String getRiderLname() {
		return riderLname;
	}

	public void setRiderLname(String riderLname) {
		this.riderLname = riderLname;
	}

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = riderId;
	}

	public String getRiderMobile() {
		return riderMobile;
	}

	public void setRiderMobile(String riderMobile) {
		this.riderMobile = riderMobile;
	}

	public String getRiderEmail() {
		return riderEmail;
	}

	public void setRiderEmail(String riderEmail) {
		this.riderEmail = riderEmail;
	}

	public String getAssign_order_Id() {
		return assign_order_Id;
	}

	public void setAssign_order_Id(String assign_order_Id) {
		this.assign_order_Id = assign_order_Id;
	}
	
	
	

}
