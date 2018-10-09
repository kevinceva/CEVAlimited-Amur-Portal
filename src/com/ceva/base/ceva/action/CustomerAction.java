package com.ceva.base.ceva.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.CustomerDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CustomerAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CustomerAction.class);

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject responseJSON2 = null;
	JSONObject responseJSON3 = null;

	private HttpSession session = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	ResponseDTO responseDTO2 = null;
	ResponseDTO responseDTO3 = null;

	protected HttpServletRequest request;

	public String custId;
	private String orderId;

	public String fetchCustomers() {

		CustomerDAO customersDAO = null;
		ArrayList<String> errors = null;

		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);

			customersDAO = new CustomerDAO();
			responseDTO = customersDAO.fetchAllCustomers(requestDTO);
			logger.info("All customers responseDTO :: " + responseDTO);

			if (responseDTO != null || responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("CUSTOMERS");
				logger.info("Customers Response Json is :: " + responseJSON);
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
			logger.debug("Exception in [CustomerAction][fetchCCustomers] [" + e.getMessage() + "]");
			addActionError("Internal error occured in fetchCustomers");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			customersDAO = null;
		}

		return result;
	}

	public String fetchCustomerInfo() {
		CustomerDAO customerDAO = null;
		ArrayList<String> errors = null;
		String custId = getCustId();

		try {

			requestJSON = new JSONObject();
			requestJSON.put("customerId", custId);

			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);

			customerDAO = new CustomerDAO();
			responseDTO = customerDAO.fetchCustomerInfo(requestDTO);

			if (responseDTO != null || responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("CUSTOMER_DETAILS");
				logger.info("Customers Info Response Json is :: " + responseJSON.get("CUSTOMER_INFO"));
				logger.info("Customers Order Response Json is :: " + responseJSON.get("CUSTOMER_ORDERS"));
				logger.info("Customers Wallet Response Json is :: " + responseJSON.get("CUSTOMER_WALLET"));
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
			logger.debug("Exception in [CustomerAction][fetchCustomersInfo] [" + e.getMessage() + "]");
			addActionError("Internal error occured in fetchCustomers");
		} finally {
			requestDTO = null;
			requestJSON = null;

			errors = null;
			customerDAO = null;
		}

		return result;
	}

	/*public String fetchCustomerInfoS() {

		CustomerDAO customersDAO = null;
		ArrayList<String> errors = null;

		String custId = getCustId();

		try {

			requestJSON = new JSONObject();
			requestJSON.put("customer_id", custId);
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);

			System.out.println("Customer ID requestJSON :: " + requestJSON);

			customersDAO = new CustomerDAO();
			responseDTO = customersDAO.fetchCustomerOrder(requestDTO);
			responseDTO2 = customersDAO.fetchCustomerInfo(requestDTO);
			logger.info("All customers responseDTO :: " + responseDTO);

			if (responseDTO != null || responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("CUSTOMER_ORDERS");
				responseJSON2 = (JSONObject) responseDTO2.getData().get("CUSTOMER_INFO");
				logger.info("Customers Orders Response Json is :: " + responseJSON2);
				logger.info("Customers Orders ResponseDTO2 is :: " + responseDTO2);
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
			logger.debug("Exception in [CustomerAction][fetchCustomersInfo] [" + e.getMessage() + "]");
			addActionError("Internal error occured in fetchCustomers");
		} finally {
			requestDTO = null;
			responseDTO = null;
			responseDTO2 = null;
			requestJSON = null;

			errors = null;
			customersDAO = null;
		}

		return result;
	}*/

	public String fetchOrderList() {
		logger.debug("inside [CustomerAction][fetchUsersList].. ");
		OrdersDAO orderListDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("ORDER_ID", getOrderId());

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			orderListDAO = new OrdersDAO();
			responseDTO = orderListDAO.orderList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("ORDER_PRODUCTS");
				logger.debug("Product List Response JSON [" + responseJSON + "]");
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
			logger.debug("Exception in [NewUserManagemtAction][fetchUsersList] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			orderListDAO = null;
		}

		return result;
	}

	public String activateDeactivateCustomer() {

		CustomerDAO customersDAO = null;
		ArrayList<String> errors = null;
		JSONObject resultJson = null;
		String custId = getCustId();

		try {

			requestJSON = new JSONObject();
			requestJSON.put("customer_id", custId);
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			resultJson = new JSONObject();

			System.out.println("Customer ID requestJSON :: " + requestJSON);

			customersDAO = new CustomerDAO();
			responseDTO = customersDAO.customerActivateDeactivate(requestDTO);

			if (responseDTO != null || responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("RESULT_JSON");
				System.out.println("Customer activation status response json is " + resultJson);
				if ("SUCCESS".equalsIgnoreCase(responseJSON.getString("RESULT"))) {
					responseJSON.put("remarks", "SUCCESS");
					result = "success";
				} else {
					responseJSON.put("remarks", "FAILURE");
					result = "fail";
				}
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

		} catch (Exception e) {
			result = "fail";
			logger.error("Exception in [CustomerAction][activateDeactivateCustomer] [" + e.getMessage() + "]");
			addActionError("Internal error occured in fetchCustomers");
		} finally {
			requestDTO = null;
			responseDTO = null;
			responseDTO2 = null;
			requestJSON = null;

			errors = null;
			customersDAO = null;
		}
		return result;
	}

	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}

	public String callServlet() {

		logger.debug("inside [CustomerAction][callServlet].. ");

		try {

			result = "success";
			logger.info("[CustomerAction][callServlet result..:" + result);

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CustomerAction][callServlet] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
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

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
