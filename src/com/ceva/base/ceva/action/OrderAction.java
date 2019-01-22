package com.ceva.base.ceva.action;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CallCenterDAO;
import com.ceva.base.common.dao.CevaPowerAdminDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.HttpPostRequestHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;



public class OrderAction extends ActionSupport {
	
		
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(OrderAction.class);
	ResourceBundle resourceBundle = ResourceBundle.getBundle("pathinfo_config");
	public String serverIp = resourceBundle.getString("AMUR_WEB_SERVICE");
	
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject responseJSON2 = null;
	JSONObject riderRespJSON = null;
		
	JSONObject resultSet = null;
	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	ResponseDTO responseDTO2 = null;
	
	protected HttpServletRequest request;
	private HttpSession httpSession = null;
	
	public String srchval;
	
	public String ordid;
	public String type;
	private String userid;
	private String orderInfoPage;
	public String rider;
	
	private String txn_date;
	private String assign_order_Id = null;
	
	
	
	
/*	public String getMerchantDashBoard(){
		return "success";
	}*/
	
	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}
	

	
	public String callServlet(){
		
		logger.debug("inside [CustomerAction][callServlet].. ");
		
		
		try {
						
			result = "success";
			logger.info("[CustomerAction][callServlet result..:"+result);
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CustomerAction][callServlet] [" + e.getMessage()
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

	public String fetchOrdersDetails() {

		System.out.println("Inside OrderAction fetchOrdersDetails");
		OrdersDAO orderDetailsDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			orderDetailsDAO = new OrdersDAO();
			responseDTO = orderDetailsDAO.fetchAllOrders(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("ORDERS");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [OrderAction][fetchOrdersDetails] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			orderDetailsDAO = null;
		}

		return result;
	}
	
	public String fetchPendingOrders() {

		System.out.println("Inside OrderAction fetchPendingOrders");
		OrdersDAO orderDetailsDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			orderDetailsDAO = new OrdersDAO();
			responseDTO = orderDetailsDAO.fetchUnassignedOrders(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("NEW_ORDERS");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [OrderAction][fetchOrdersDetails] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			orderDetailsDAO = null;
		}

		return result;
	}
	
	public String orderInfo() {

		System.out.println("Inside [OrderAction] [OrderInfo]");
		OrdersDAO OrdersDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			String ordid = getOrdid();
			System.out.println("Order Id "+ordid);

			requestJSON.put("ORDID", ordid);
			requestDTO.setRequestJSON(requestJSON);

			OrdersDAO = new OrdersDAO();
			responseDTO = OrdersDAO.orderInformation(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("ORDER_INFO");
				logger.debug("Order Info Response JSON :: [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [OrderAction][fetchOrdersDetails] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			OrdersDAO = null;
		}

		return result;
	}
	
	/*public String orderInfo() {
		logger.debug("Inside orderInfo..");
		OrdersDAO OrdersDAO = null;
		ArrayList<String> errors = null;
		
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			String ordid = getOrdid();

			requestJSON.put("ORDID", ordid);
			requestDTO.setRequestJSON(requestJSON);

			OrdersDAO = new OrdersDAO();
			responseDTO = OrdersDAO.orderInformation(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			logger.debug("fetching riders");
			String riderURL = "http://104.42.234.123:5555/amurcore/amur/rider/fetchriders";
			logger.debug("riderURL  :::: " + riderURL);
			String riderJson = httpPostRequestHandler.sendRestPostRequest(riderURL);
			JSONObject riderObj = JSONObject.fromObject(riderJson);
			logger.debug("catalogobj to string["+riderObj.toString()+"]");
			riderRespJSON = riderObj;

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("ORDER_DETAILS");
				
				JSONArray resultArray = responseJSON.getJSONArray("ORDER_DETAILS");//Getting Array from object				
				JSONObject resultObject = resultArray.getJSONObject(0); //Get object from array
				
				/*JSONObject orderObject = resultObject.getJSONObject("ORDERLIST");
				JSONArray orderArray = orderObject.getJSONArray("PROD_DETAILS");

				for( int i = 0; i < orderArray.size(); i++){
					JSONObject orderDetail = orderArray.getJSONObject(i);
					String product_id = orderDetail.getString("prodID");
					
					System.out.println("Product id "+product_id);
				}*
				
				
				String order_id = resultObject.getString("ORD_ID");
				String txn_date = resultObject.getString("TXN_DATE");
				String channel = resultObject.getString("CHANNEL");
				String cust_name = resultObject.getString("CUSTOMERNAME");
				String cust_email = resultObject.getString("CUSTOMEREMAIL");
				String cust_mobile = resultObject.getString("CUSTOMERMOBILENO");
				String shipping_address = resultObject.getString("SHIPPINGADDRESS");
				String payment_mode = resultObject.getString("PAYMENTMODE");
				String customer_id = resultObject.getString("CIN");				
				
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
				
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Order Information [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			OrdersDAO = null;
			errors = null;
		}
		return result;
	}*/
	
	public String cancelOrder() {
		
		System.out.println("Inside [OrderAction] [OrderInfo]");
		OrdersDAO OrdersDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			String ordid = getAssign_order_Id();
			System.out.println("Order Id "+ordid);

			requestJSON.put("ORDID", ordid);
			requestDTO.setRequestJSON(requestJSON);

			OrdersDAO = new OrdersDAO();
			responseDTO = OrdersDAO.cancelOrder(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("remarks");
				logger.debug("Order Info Response JSON :: [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [OrderAction][fetchOrdersDetails] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			OrdersDAO = null;
		}
		
		return result;
	}
	
	public String assignRiderOrder(){
		System.out.println("Inside Assign Order :: ");
		ArrayList<String> errors = null;		
		
		
		try {
			session = ServletActionContext.getRequest().getSession();
			String user_Id = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();		
			String orderId = getOrdid();
			String rider = getRider();
			
			System.out.println("prdid ["+orderId+"] rider["+rider+"]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp+"/amurcore/amur/rider/assignorerstorider/"+rider+"/"+orderId+"/"+user_Id;
			
			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			result = "success";		
					
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}

		return result;
	}
	
	
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	
	public JSONObject getRiderRespJSON() {
		return riderRespJSON;
	}
	
	public void setRiderRespJSON(JSONObject riderRespJSON) {
		this.riderRespJSON = riderRespJSON;
	}
	
	public JSONObject getResponseJSON2() {
		return responseJSON2;
	}

	public void setResponseJSON2(JSONObject responseJSON2) {
		this.responseJSON2 = responseJSON2;
	}

	
	public JSONObject getResultSet() {
		return resultSet;
	}

	public void setResultSet(JSONObject resultSet) {
		this.resultSet = resultSet;
	}



	public String getUserid() {
		return userid;
	}



	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	public String getRider() {
		return rider;
	}

	public void setRider(String rider) {
		this.rider = rider;
	}



	public String getOrdid() {
		return ordid;
	}



	public void setOrdid(String ordid) {
		this.ordid = ordid;
	}
	
	
	public String getTxn_date() {
		return txn_date;
	}



	public void setTxn_date(String txn_date) {
		this.ordid = txn_date;
	}



	public String getOrderInfoPage() {
		return orderInfoPage;
	}



	public void setOrderInfoPage(String orderInfoPage) {
		this.orderInfoPage = orderInfoPage;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getAssign_order_Id() {
		return assign_order_Id;
	}



	public void setAssign_order_Id(String assign_order_Id) {
		this.assign_order_Id = assign_order_Id;
	}
	

}
