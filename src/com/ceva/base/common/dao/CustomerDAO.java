package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.print.attribute.standard.RequestingUserName;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CustomerDAO {
	
	private Logger logger = Logger.getLogger(OrdersDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	String customerName;
	
	public ResponseDTO fetchAllCustomers(RequestDTO requestDTO) {
		
		Connection connection = null;
		logger.info("Inside [CustomerDAO]  [fetchAllCustomers]");
		
		HashMap<String, Object> customerMap = null;
	    JSONObject resultJson = null;
	    JSONArray customerArray = null;
	    PreparedStatement customerPstmt = null;
	    ResultSet customerRS = null;
	    
	    JSONObject json = null;
	    customerName =null;
	    
	    String customerSQL = "SELECT CIN, FNAME||' '||LNAME, MOBILE_NUMBER, EMAIL_ID, to_char(CREATED_DATE,'DD-MM-YYYY HH:MM:SS') FROM CUSTOMER_MASTER order by CREATED_DATE DESC";
	    
	    try {
	    	
	    	this.responseDTO = new ResponseDTO();
	    	
	    	connection = connection == null? DBConnector.getConnection() : connection;	    	
	    	logger.info("Connection is :: "+connection);
	    	
	    	customerMap = new HashMap<>();
	    	resultJson = new JSONObject();
	    	customerArray = new JSONArray();
	    	
	    	customerPstmt = connection.prepareStatement(customerSQL);
	    	customerRS = customerPstmt.executeQuery();
	    	
	    	json = new JSONObject();
	    	
	    	while(customerRS.next()) {
	    		
	    		customerName = customerRS.getString(2);
	    		customerName = customerName.replaceAll("\\'", "_"); 
	    		
	    		json.put("CUSTOMER_ID", customerRS.getString(1));
	    		json.put("CUSTOMER_NAME", customerName);
	    		json.put("MOBILE_NUMBER", customerRS.getString(3));
	    		json.put("EMAIL", customerRS.getString(4));
	    		json.put("DATE_CREATED", customerRS.getString(5));
	    		customerArray.add(json);
	    		json.clear();
	    	}
	    	
	    	DBUtils.closeResultSet(customerRS);
	    	DBUtils.closePreparedStatement(customerPstmt);
	    	DBUtils.closeConnection(connection);
	    	resultJson.put("CUSTOMERS", customerArray);
	    	customerMap.put("CUSTOMERS", resultJson);
	    	this.responseDTO.setData(customerMap);
	    	
	    } catch (Exception e) {
	    	this.logger.debug("Error in FetchAllCustomers :: "+e.getMessage());
	    }
	    
		return responseDTO;
	}
	
	public ResponseDTO fetchCustomerOrder(RequestDTO requestDTO) {
		
		Connection connection = null;
		logger.info("Inside [CustomerDAO]  [fetchAllCustomers]");
		
		HashMap<String, Object> customerMap = null;
	    JSONObject resultJson = null;
	    JSONArray customerArray = null;
	    PreparedStatement customerPstmt = null;
	    ResultSet customerRS = null;
	    
	    JSONObject json = null;
	    
	    requestJSON = requestDTO.getRequestJSON();
		String customer_id = requestJSON.getString("customer_id");		
		String customerOrderSQL = "SELECT ORDER_ID, TXN_AMT, to_char(TXN_DATE,'HH:MI AM Month DD, YYYY'), ORDER_STATUS FROM ORDER_MASTER WHERE CIN = ? AND ORDER_STATUS<>'Failed'";
		
		try {			
			this.responseDTO = new ResponseDTO();
	    	
	    	connection = connection == null? DBConnector.getConnection() : connection;	    	
	    	logger.info("Connection is :: "+connection);
	    	
	    	customerMap = new HashMap<>();
	    	resultJson = new JSONObject();
	    	customerArray = new JSONArray();
	    	
	    	customerPstmt = connection.prepareStatement(customerOrderSQL);
	    	customerPstmt.setString(1, customer_id);
	    	customerRS = customerPstmt.executeQuery();
	    	
	    	json = new JSONObject();
	    	
	    	while(customerRS.next()) {
	    		json.put("ORDER_ID", customerRS.getString(1));
	    		json.put("TXN_AMOUNT", customerRS.getString(2));
	    		json.put("TXN_DATE", customerRS.getString(3));
	    		json.put("ORDER_STATUS", customerRS.getString(4));
	    		customerArray.add(json);
	    		json.clear();
	    	}
	    	
	    	DBUtils.closeResultSet(customerRS);
	    	DBUtils.closePreparedStatement(customerPstmt);
	    	DBUtils.closeConnection(connection);
	    	resultJson.put("CUSTOMER_ORDERS", customerArray);
	    	customerMap.put("CUSTOMER_ORDERS", resultJson);
	    	this.responseDTO.setData(customerMap);	    
			
			
		} catch (Exception e) {
			logger.info("Exception in [CustomerDAO] [fetchCustomerInfo] :: "+e.getMessage());
		}
		
		return responseDTO;
	}
	
	public ResponseDTO fetchCustomerInfo(RequestDTO requestDTO) {
		Connection connection = null;
		logger.info("Inside [CustomerDAO]  [fetchAllCustomers]");
		
		HashMap<String, Object> customerMap = null;
	    JSONObject resultJson = null;
	    JSONArray customerInfoArray = null;
	    JSONArray customerOrderArray = null;
	    JSONArray customerWalletArray = null;
	    PreparedStatement customerPstmt = null;
	    ResultSet customerRS = null;
	    String customerName;
	    
	    JSONObject CustomerJSON = null;
	    JSONObject OrderJSON = null;
	    JSONObject WalletJSON = null;
		
	    requestJSON = requestDTO.getRequestJSON();
		String customer_id = requestJSON.getString("customerId");		
		String customerInfoSQL =  "SELECT CIN, FNAME||' '||LNAME, MOBILE_NUMBER, EMAIL_ID, to_char(CREATED_DATE,'HH:MI AM Month DD, YYYY'), STATUS FROM CUSTOMER_MASTER WHERE CIN = ?";
		String customerOrderSQL = "SELECT ORDER_ID, TXN_AMT, to_char(TXN_DATE,'HH:MI AM Month DD, YYYY'), ORDER_STATUS FROM ORDER_MASTER WHERE CIN = ? AND ORDER_STATUS<>'Failed'";
		String customerWalletSQL = "SELECT ACCOUNT_NUMBER, ACCOUNT_TYPE, PREMIUM, SUMASSURED FROM ACCOUNT_MASTER WHERE CIN = ?";
		
		try {			
			this.responseDTO = new ResponseDTO();
	    	
	    	connection = connection == null? DBConnector.getConnection() : connection;	    	
	    	logger.info("Connection is :: "+connection);
	    	
	    	customerMap = new HashMap<>();
	    	resultJson = new JSONObject();
	    	customerInfoArray = new JSONArray();
	    	customerOrderArray = new JSONArray();
	    	customerWalletArray = new JSONArray();
	    	
	    	CustomerJSON = new JSONObject();
	    	OrderJSON = new JSONObject();
	    	WalletJSON = new JSONObject();
	    	
	    	//Fetching Customer Information
	    	customerPstmt = connection.prepareStatement(customerInfoSQL);
	    	customerPstmt.setString(1, customer_id);
	    	customerRS = customerPstmt.executeQuery();
	    		    		    	
	    	while(customerRS.next()) {
	    		
	    		customerName = customerRS.getString(2);
	    		customerName = customerName.replaceAll("\\'", "_"); 
	    		
	    		CustomerJSON.put("CUSTOMER_ID", customerRS.getString(1));
	    		CustomerJSON.put("CUSTOMER_NAME", customerName);
	    		CustomerJSON.put("MOBILE_NUMBER", customerRS.getString(3));
	    		CustomerJSON.put("EMAIL", customerRS.getString(4));
	    		CustomerJSON.put("DATE_CREATED", customerRS.getString(5));
	    		customerInfoArray.add(CustomerJSON);
	    		CustomerJSON.clear();
	    	}
	    	
	    	//Fetching Order Information
	    	customerPstmt = connection.prepareStatement(customerOrderSQL);
	    	customerPstmt.setString(1, customer_id);
	    	customerRS = customerPstmt.executeQuery();
	    		    	
	    	while(customerRS.next()) {
	    		OrderJSON.put("ORDER_ID", customerRS.getString(1));
	    		OrderJSON.put("TXN_AMOUNT", customerRS.getString(2));
	    		OrderJSON.put("TXN_DATE", customerRS.getString(3));
	    		OrderJSON.put("ORDER_STATUS", customerRS.getString(4));
	    		customerOrderArray.add(OrderJSON);
	    		OrderJSON.clear();
	    	}
	    	
	    	//Fetching Wallet Information
	    	customerPstmt = connection.prepareStatement(customerWalletSQL);
	    	customerPstmt.setString(1, customer_id);
	    	customerRS = customerPstmt.executeQuery();
	    		    	
	    	while(customerRS.next()) {
	    		WalletJSON.put("ACCOUNT_NUMBER", customerRS.getString(1));
	    		WalletJSON.put("ACCOUNT_TYPE", customerRS.getString(2));
	    		WalletJSON.put("PREMIUM", customerRS.getString(3));
	    		WalletJSON.put("SUMASSURED", customerRS.getString(4));
	    		customerWalletArray.add(WalletJSON);
	    		WalletJSON.clear();
	    	}
	    	
	    	
	    	
	    	DBUtils.closeResultSet(customerRS);
	    	DBUtils.closePreparedStatement(customerPstmt);
	    	DBUtils.closeConnection(connection);
	    	resultJson.put("CUSTOMER_INFO", customerInfoArray);
	    	resultJson.put("CUSTOMER_WALLET", customerWalletArray);
	    	resultJson.put("CUSTOMER_ORDERS", customerOrderArray);
	    	customerMap.put("CUSTOMER_DETAILS", resultJson);
	    	this.responseDTO.setData(customerMap);	    
			
			
		} catch (Exception e) {
			logger.info("Exception in [CustomerDAO] [fetchCustomerInfo] :: "+e.getMessage());
		}
		
		
		return responseDTO;
	}
	
	public ResponseDTO fetchCustomerInfos(RequestDTO requestDTO) {
		
		Connection connection = null;
		logger.info("Inside [CustomerDAO]  [fetchCustomerInfo]");
		
		HashMap<String, Object> customerMap = null;
	    JSONObject resultJson = null;
	    JSONArray customerArray = null;
	    PreparedStatement customerPstmt = null;
	    ResultSet customerRS = null;
	    
	    JSONObject json = null;
	    
	    requestJSON = requestDTO.getRequestJSON();
		String customer_id = requestJSON.getString("customer_id");		
		String customerOrderSQL =  "SELECT CIN, FNAME||' '||LNAME, MOBILE_NUMBER, EMAIL_ID, to_char(CREATED_DATE,'HH:MI AM Month DD, YYYY'), STATUS FROM CUSTOMER_MASTER WHERE CIN = ?";
		
		try {
			
			this.responseDTO = new ResponseDTO();
	    	
	    	connection = connection == null? DBConnector.getConnection() : connection;	    	
	    	logger.info("Connection is :: "+connection);
	    	
	    	customerMap = new HashMap<>();
	    	resultJson = new JSONObject();
	    	customerArray = new JSONArray();
	    	
	    	customerPstmt = connection.prepareStatement(customerOrderSQL);
	    	customerPstmt.setString(1, customer_id);
	    	customerRS = customerPstmt.executeQuery();
	    	
	    	json = new JSONObject();
	    	
	    	while(customerRS.next()) {
	    		json.put("CUSTOMER_ID", customerRS.getString(1));
	    		json.put("CUSTOMER_NAME", customerRS.getString(2));
	    		json.put("MOBILE_NUMBER", customerRS.getString(3));
	    		json.put("EMAIL", customerRS.getString(4));
	    		json.put("DATE_CREATED", customerRS.getString(5));
	    		json.put("STATUS", customerRS.getString(6));
	    		customerArray.add(json);
	    		json.clear();
	    	}
	    	
	    	DBUtils.closeResultSet(customerRS);
	    	DBUtils.closePreparedStatement(customerPstmt);
	    	DBUtils.closeConnection(connection);
	    	resultJson.put("CUSTOMER_INFO", customerArray);
	    	customerMap.put("CUSTOMER_INFO", resultJson);
	    	this.responseDTO.setData(customerMap);	    
			
			
		} catch (Exception e) {
			logger.info("Exception in [CustomerDAO] [fetchCustomerInfo] :: "+e.getMessage());
		}
		
		return responseDTO;
	}

	public ResponseDTO customerActivateDeactivate(RequestDTO requestDTO) {
		
		Connection connection = null;
		CallableStatement cs = null;
		logger.info("Inside [CustomerDAO]  [customerActivateDeactivate]");
		
		HashMap<String, Object> statusMap = null;
	    JSONObject resultJson = null;
	    String customer_id = null;
	    
	    try {
	    	
	    	this.responseDTO = new ResponseDTO();
	    	
	    	requestJSON = requestDTO.getRequestJSON();
	 		customer_id = requestJSON.getString("customer_id");
	 		statusMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
	 		
	 		connection = connection == null ? DBConnector.getConnection():connection;
	 		logger.debug("Connection is [" + (connection) + "]");
			logger.debug("Customer ID is [" + customer_id + "]");
			
			String callProc = "{call CUSTOMERSTATUS(?,?)}";
			
			cs = connection.prepareCall(callProc);
			cs.setString(1, customer_id); 
			cs.registerOutParameter(2, java.sql.Types.NUMERIC);
			
			cs.execute();
												
			resultJson.put("RESULT","SUCCESS");
			statusMap.put("RESULT_JSON", resultJson);
		    this.logger.info("EntityMap [" + statusMap + "]");
		    this.responseDTO.setData(statusMap);
	    	
	    } catch (SQLException e) {
	    	logger.error("SQLException in customerActivateDeactivate [" + e.getMessage() + "]");
	    	e.printStackTrace();
	    	resultJson.put("RESULT","FAILURE");
			statusMap.put("RESULT_JSON", resultJson);
		    this.logger.info("EntityMap [" + statusMap + "]");
		    this.responseDTO.setData(statusMap);
		} catch (Exception e) {	    	
	    	logger.error("Internal Error Occured While Executing.");	
	    	e.printStackTrace();
	    	resultJson.put("RESULT","FAILURE");
			statusMap.put("RESULT_JSON", resultJson);
		    this.logger.info("EntityMap [" + statusMap + "]");
		    this.responseDTO.setData(statusMap);
	    } finally {
	    	DBUtils.closeCallableStatement(cs);
			DBUtils.closeConnection(connection);			
			customer_id = null;
			statusMap = null;
		}

		return this.responseDTO;
	    
	}
	
	

}
