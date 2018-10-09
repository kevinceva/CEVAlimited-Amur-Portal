package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class OrdersDAO {

	private Logger logger = Logger.getLogger(OrdersDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	JSONObject requestJSON2 = null;
	JSONObject responseJSON2 = null;
	ResponseDTO responseDTO2 = null;

	public String product_name;

	public ResponseDTO fetchAllOrders(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [OrdersDAO][fetchAllOrders].. ");

		HashMap<String, Object> orderDetailsMap = null;
		JSONObject resultJson = null;
		
		JSONArray newOrderArray = null;
		JSONArray assignedOrderArray = null;
		JSONArray deliveredOrderArray = null;
		JSONArray failedOrdersArray = null;
		JSONArray enrouteOrderArray = null;
		
		PreparedStatement orderPstmt = null;
		ResultSet orderRS = null;

		JSONObject newOrderJSON = null;
		JSONObject assignedOrderJSON = null;
		JSONObject deliveredOrderJSON = null;
		JSONObject failedOrderJSON = null;
		JSONObject enrouteOrderJSON = null;

		String OrderQry = "SELECT OM.ORDER_ID, OM.ORDER_STATUS, OM.PAYMENT_STATUS, OM.TXN_AMT, (SELECT SUM(QUANTITY) FROM ORDER_DETAILS WHERE ORDER_ID = OM.ORDER_ID), to_char( OM.TXN_DATE , 'DD-MM-YYYY HH:MM:SS' ), OM.SHIPPING_ADDRESS FROM ORDER_MASTER OM, CUSTOMER_MASTER CM WHERE OM.CIN = CM.CIN AND OM.ORDER_ID NOT IN (SELECT ORDER_ID FROM RIDER_ORDER_MASTER) AND OM.ORDER_TYPE = 1 order by OM.ORDER_STATUS, OM.TXN_DATE DESC";
		String riderOrderQry = "SELECT OM.ORDER_ID, ROM.STATUS, OM.TXN_AMT, (SELECT SUM(QUANTITY) FROM ORDER_DETAILS WHERE ORDER_ID = OM.ORDER_ID), TO_CHAR(OM.TXN_DATE, 'DD-MM-YYYY HH:MM:SS'), OM.SHIPPING_ADDRESS, RM.RIDER_F_NAME||' '|| RM.RIDER_L_NAME, TO_CHAR(ROM.ASSIGNED_DATE, 'DD-MM-YYYY HH:MM:SS'), TO_CHAR(ROM.DELIVERY_COMMENCE_DATE, 'DD-MM-YYYY HH:MM:SS'), TO_CHAR(ROM.DELIVERED_DATE, 'DD-MM-YYYY HH:MM:SS'), OM.PAYMENT_STATUS, OM.ORDER_STATUS FROM ORDER_MASTER OM, RIDER_ORDER_MASTER ROM , RIDER_MASTER RM WHERE OM.ORDER_ID = ROM.ORDER_ID AND ROM.RIDER_ID = RM.RIDER_ID order by OM.ORDER_STATUS, OM.TXN_DATE DESC";
		
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			orderDetailsMap = new HashMap();
			resultJson = new JSONObject();
			
			newOrderArray = new JSONArray();
			failedOrdersArray = new JSONArray();
			assignedOrderArray = new JSONArray();
			enrouteOrderArray = new JSONArray();
			deliveredOrderArray = new JSONArray();

			newOrderJSON = new JSONObject();
			failedOrderJSON = new JSONObject();
			assignedOrderJSON = new JSONObject();
			enrouteOrderJSON = new JSONObject();
			deliveredOrderJSON = new JSONObject();
			
			orderPstmt = connection.prepareStatement(OrderQry);
			orderRS = orderPstmt.executeQuery();

			while (orderRS.next()) {
				
				//Sorting successful unassigned orders
				if((orderRS.getString(3).equals("Paid") || orderRS.getString(3).equals("Not Paid")) && (orderRS.getString(2).equals("Processing") || !orderRS.getString(2).equals("Cancelled"))) {
					newOrderJSON.put("ORDER_ID", orderRS.getString(1));
					newOrderJSON.put("ORDER_STATUS", orderRS.getString(2));
					newOrderJSON.put("PAYMENT_STATUS", orderRS.getString(3));
					newOrderJSON.put("TXN_AMT", orderRS.getString(4));
					newOrderJSON.put("ORDER_ITEMS", orderRS.getString(5));
					newOrderJSON.put("TXN_DATE", orderRS.getString(6));
					newOrderJSON.put("SHIPPING_ADDRESS", orderRS.getString(7));
					newOrderArray.add(newOrderJSON);
					newOrderJSON.clear();
				}
				
				//Sorting all failed orders
				else if(!orderRS.getString(3).equals("failed")) {
					failedOrderJSON.put("ORDER_ID", orderRS.getString(1));
					failedOrderJSON.put("ORDER_STATUS", orderRS.getString(2));
					failedOrderJSON.put("PAYMENT_STATUS", orderRS.getString(3));
					failedOrderJSON.put("TXN_AMT", orderRS.getString(4));
					failedOrderJSON.put("ORDER_ITEMS", orderRS.getString(5));
					failedOrderJSON.put("TXN_DATE", orderRS.getString(6));
					newOrderJSON.put("SHIPPING_ADDRESS", orderRS.getString(7));
					failedOrdersArray.add(failedOrderJSON);
					failedOrderJSON.clear();
				}
								
			}
			
			//Fetching all assigned and delivered orders
			orderPstmt = connection.prepareStatement(riderOrderQry);
			orderRS = orderPstmt.executeQuery();
			
			while (orderRS.next()) {
				
				//Fetching all orders assigned to riders
				if(orderRS.getString(2).equals("Processed")) {
					assignedOrderJSON.put("ORDER_ID", orderRS.getString(1));
					assignedOrderJSON.put("STATUS", orderRS.getString(2));
					assignedOrderJSON.put("TXN_AMT", orderRS.getString(3));
					assignedOrderJSON.put("ITEMS_COUNT", orderRS.getString(4));
					assignedOrderJSON.put("TXN_DATE", orderRS.getString(5));
					assignedOrderJSON.put("SHIPPING_ADDRESS", orderRS.getString(6));
					assignedOrderJSON.put("RIDER_NAME", orderRS.getString(7));
					assignedOrderJSON.put("ASSIGNED_DATE", orderRS.getString(8));
					assignedOrderJSON.put("DELIVERY_COMMENCE_DATE", orderRS.getString(9));
					assignedOrderJSON.put("DELIVERED_DATE", orderRS.getString(10));
					assignedOrderJSON.put("PAYMENT_STATUS", orderRS.getString(11));
					assignedOrderJSON.put("ORDER_STATUS", orderRS.getString(12));
					assignedOrderArray.add(assignedOrderJSON);
					System.out.println("Assigned Orders :: "+assignedOrderJSON);
					assignedOrderJSON.clear();
				}
				
				//Fetching all assigned orders
				else if(orderRS.getString(2).equals("Enroute")) {
					enrouteOrderJSON.put("ORDER_ID", orderRS.getString(1));
					enrouteOrderJSON.put("STATUS", orderRS.getString(2));
					enrouteOrderJSON.put("TXN_AMT", orderRS.getString(3));
					enrouteOrderJSON.put("ITEMS_COUNT", orderRS.getString(4));
					enrouteOrderJSON.put("TXN_DATE", orderRS.getString(5));
					enrouteOrderJSON.put("SHIPPING_ADDRESS", orderRS.getString(6));
					enrouteOrderJSON.put("RIDER_NAME", orderRS.getString(7));
					enrouteOrderJSON.put("ASSIGNED_DATE", orderRS.getString(8));
					enrouteOrderJSON.put("DELIVERY_COMMENCE_DATE", orderRS.getString(9));
					enrouteOrderJSON.put("DELIVERED_DATE", orderRS.getString(10));
					enrouteOrderJSON.put("PAYMENT_STATUS", orderRS.getString(11));
					enrouteOrderJSON.put("PAYMENT_STATUS", orderRS.getString(12));
					enrouteOrderArray.add(enrouteOrderJSON);
					System.out.println("Enroute Orders :: "+enrouteOrderJSON);
					enrouteOrderJSON.clear();
				}		
				
				//Fetching all orders delivered successfully
				else if(orderRS.getString(2).equals("Delivered")) {
					deliveredOrderJSON.put("ORDER_ID", orderRS.getString(1));
					deliveredOrderJSON.put("STATUS", orderRS.getString(2));
					deliveredOrderJSON.put("TXN_AMT", orderRS.getString(3));
					deliveredOrderJSON.put("ITEMS_COUNT", orderRS.getString(4));
					deliveredOrderJSON.put("TXN_DATE", orderRS.getString(5));
					deliveredOrderJSON.put("SHIPPING_ADDRESS", orderRS.getString(6));
					deliveredOrderJSON.put("RIDER_NAME", orderRS.getString(7));
					deliveredOrderJSON.put("ASSIGNED_DATE", orderRS.getString(8));
					deliveredOrderJSON.put("DELIVERY_COMMENCE_DATE", orderRS.getString(9));
					deliveredOrderJSON.put("DELIVERED_DATE", orderRS.getString(10));
					deliveredOrderJSON.put("PAYMENT_STATUS", orderRS.getString(11));
					deliveredOrderJSON.put("ORDER_STATUS", orderRS.getString(12));
					deliveredOrderArray.add(deliveredOrderJSON);
					System.out.println("Delivered Orders :: "+deliveredOrderJSON);
					deliveredOrderJSON.clear();
				}				
				
			}

			DBUtils.closePreparedStatement(orderPstmt);
			DBUtils.closeResultSet(orderRS);
			DBUtils.closeConnection(connection);
						
			//All orders JSON Mapping
			resultJson.put("NEW_ORDERS", newOrderArray);
			resultJson.put("FAILED_ORDERS", failedOrdersArray);
			resultJson.put("ASSIGNED_ORDERS", assignedOrderArray);
			resultJson.put("ENROUTE_ORDERS", enrouteOrderArray);
			resultJson.put("DELIVERED_ORDERS", deliveredOrderArray);
					
			orderDetailsMap.put("ORDERS", resultJson);
			this.logger.info("EntityMap [" + orderDetailsMap + "]");
			this.responseDTO.setData(orderDetailsMap);

		} catch (Exception e) {
			this.logger.debug("Got Exception in OrdersDAO fetchAllOrders [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {

			orderDetailsMap = null;
			resultJson = null;
			
			newOrderArray = null;
			failedOrdersArray = null;
			assignedOrderArray = null;
			deliveredOrderArray = null;
			enrouteOrderArray = null;
		}

		return this.responseDTO;
	}
	
	public ResponseDTO cancelOrder(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [OrdersDAO][fetchUnassignedOrders].. ");

		String orderId = null;
		HashMap<String, Object> orderDetailsMap = null;
		JSONObject resultJson = null;
		JSONArray orderJSONArray = null;
		PreparedStatement orderPstmt = null;
		ResultSet orderRS = null;
		JSONObject json = null;

		String orderQRY = "UPDATE ORDER_MASTER SET ORDER_STATUS = 'Cancelled' WHERE ORDER_ID = ?" ;
		try {
			requestJSON = requestDTO.getRequestJSON();
			orderId = requestJSON.getString("ORDID");
			
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			orderDetailsMap = new HashMap();
			resultJson = new JSONObject();
			orderJSONArray = new JSONArray();

			orderPstmt = connection.prepareStatement(orderQRY);
			orderPstmt.setString(1, orderId);
			orderRS = orderPstmt.executeQuery();
			
			DBUtils.closeResultSet(orderRS);
			DBUtils.closePreparedStatement(orderPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("remarks", "SUCCESS");
			orderDetailsMap.put("remarks", resultJson);
			this.logger.info("EntityMap [" + orderDetailsMap + "]");
			this.responseDTO.setData(orderDetailsMap);

		} catch (Exception e) {
			this.logger.debug("Got Exception in fetchUnassignedOrders DAO [" + e.getMessage() + "]");
			e.printStackTrace();
			resultJson.put("remarks", "FAILURE");
			orderDetailsMap.put("remarks", resultJson);
		} finally {
			DBUtils.closeResultSet(orderRS);
			DBUtils.closePreparedStatement(orderPstmt);
			DBUtils.closeConnection(connection);

			orderDetailsMap = null;
			resultJson = null;
			orderJSONArray = null;
		}

		return this.responseDTO;
	}

	public ResponseDTO fetchUnassignedOrders(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [OrdersDAO][fetchUnassignedOrders].. ");

		HashMap<String, Object> orderDetailsMap = null;
		JSONObject resultJson = null;
		JSONArray orderJSONArray = null;
		PreparedStatement orderPstmt = null;
		ResultSet orderRS = null;
		JSONObject json = null;

		String orderQRY = "SELECT OM.ORDER_ID, OM.TXN_AMT, CM.FNAME||' '||CM.LNAME, CM.MOBILE_NUMBER, OM.SHIPPING_ADDRESS, to_char( OM.TXN_DATE , 'DD-MM-YYYY HH:MM:SS' ) FROM ORDER_MASTER OM, CUSTOMER_MASTER CM WHERE OM.ORDER_STATUS = 'processing' AND OM.CIN = CM.CIN AND ORDER_ID NOT IN (SELECT ORDER_ID FROM RIDER_ORDER_MASTER) ORDER BY OM.TXN_DATE ASC";
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			orderDetailsMap = new HashMap();
			resultJson = new JSONObject();
			orderJSONArray = new JSONArray();

			orderPstmt = connection.prepareStatement(orderQRY);
			orderRS = orderPstmt.executeQuery();

			json = new JSONObject();
			while (orderRS.next()) {
				json.put("ORDER_ID", orderRS.getString(1));
				json.put("TXN_AMT", orderRS.getString(2));
				json.put("CUSTOMER_NAME", orderRS.getString(3));
				json.put("MOBILE_NUMBER", orderRS.getString(4));
				json.put("SHIPPING_ADDRESS", orderRS.getString(5));
				json.put("TXN_DATE", orderRS.getString(6));
				orderJSONArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(orderRS);
			DBUtils.closePreparedStatement(orderPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("NEW_ORDERS", orderJSONArray);
			orderDetailsMap.put("NEW_ORDERS", resultJson);
			this.logger.info("EntityMap [" + orderDetailsMap + "]");
			this.responseDTO.setData(orderDetailsMap);

		} catch (Exception e) {
			this.logger.debug("Got Exception in fetchUnassignedOrders DAO [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(orderRS);
			DBUtils.closePreparedStatement(orderPstmt);
			DBUtils.closeConnection(connection);

			orderDetailsMap = null;
			resultJson = null;
			orderJSONArray = null;
		}

		return this.responseDTO;
	}

	public ResponseDTO orderList(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [OrdersDAO][fetchOrdersList].. ");

		HashMap<String, Object> orderDetailsMap = null;
		JSONObject resultJson = null;
		JSONArray productJSONArray = null;
		PreparedStatement userPstmt = null;
		ResultSet userRS = null;

		String orderID = null;

		JSONObject json = null;

		try {

			requestJSON = requestDTO.getRequestJSON();
			orderID = requestJSON.getString("ORDER_ID");

			System.out.println("Order id resquested is   " + orderID);
			String orderQry = "SELECT PRODUCT_MASTER.PRD_NAME, ORDER_DETAILS.PRICE, ORDER_DETAILS.QUANTITY, to_char(ORDER_DETAILS.CREATED_DATE,'HH:MI AM Month DD, YYYY') FROM ORDER_DETAILS INNER JOIN PRODUCT_MASTER ON ORDER_DETAILS.PRD_ID = PRODUCT_MASTER.PRD_ID AND ORDER_DETAILS.ORDER_ID = ?";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			orderDetailsMap = new HashMap();
			resultJson = new JSONObject();
			productJSONArray = new JSONArray();

			userPstmt = connection.prepareStatement(orderQry);

			userPstmt.setString(1, orderID);
			userRS = userPstmt.executeQuery();

			json = new JSONObject();
			while (userRS.next()) {
				json.put("PRODUCT_NAME", userRS.getString(1));
				json.put("PRODUCT_PRICE", userRS.getString(2));
				json.put("PRODUCT_QUANTITY", userRS.getString(3));
				json.put("PRODUCT_DATE", userRS.getString(4));
				productJSONArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("PRODUCT_LIST", productJSONArray);
			orderDetailsMap.put("ORDER_PRODUCTS", resultJson);

			this.logger.info("EntityMap [" + orderDetailsMap + "]");
			this.responseDTO.setData(orderDetailsMap);

		} catch (Exception e) {
			this.logger.debug("Got Exception in orderList DAO [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);

			orderDetailsMap = null;
			resultJson = null;
			productJSONArray = null;
		}

		return this.responseDTO;
	}

	// Fetching Specific Order Information
	public ResponseDTO orderInformation(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [OrdersDAO][fetchOrderInformation].. ");

		HashMap<String, Object> orderDetailsMap = null;
		JSONObject resultJson = null;
		JSONArray customerInfoArray = null;
		JSONArray orderListArray = null;
		JSONArray riderArray = null;

		PreparedStatement orderPstmt = null;
		ResultSet orderRS = null;

		JSONObject customerJSON = null;
		JSONObject orderListJSON = null;
		JSONObject riderJSON = null;

		requestJSON = requestDTO.getRequestJSON();
		String orderid = requestJSON.getString("ORDID");
		System.out.println("Order ID is :: "+orderid);
		String customerQRY = "SELECT OM.CIN, CM.FNAME||' '||CM.LNAME, CM.EMAIL_ID, CM.MOBILE_NUMBER, to_char(OM.TXN_DATE, 'dd/MM/yyyy hh:mm:ss'), (SELECT PAYMENT_TYPE FROM PAYMENT_TYPE_MASTER WHERE PAYMENT_TYPE_ID = PAYMENT_MODE), OM.CHANNEL, OM.SHIPPING_ADDRESS, OM.TXN_AMT FROM ORDER_MASTER OM, CUSTOMER_MASTER CM WHERE CM.CIN = OM.CIN AND  OM.ORDER_ID = ?";
		String orderLstQRY = "SELECT PM.PRD_NAME, PM.PRICE, OD.PRICE, OD.QUANTITY FROM PRODUCT_MASTER PM, ORDER_DETAILS OD WHERE PM.PRD_ID = OD.PRD_ID AND OD.ORDER_ID = ?";
		//String riderQRY = "SELECT RM.RIDER_F_NAME||' '||RM.RIDER_L_NAME, RM.RIDER_ID FROM RIDER_MASTER RM WHERE RM.RIDER_ID NOT IN (SELECT RIDER_ID FROM RIDER_ORDER_MASTER WHERE STATUS = 'In Transit')";
		String riderQRY = "SELECT RM.RIDER_F_NAME||' '||RM.RIDER_L_NAME, RM.RIDER_ID FROM RIDER_MASTER RM";
		

		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			logger.info("Connection is :: " + connection);

			orderDetailsMap = new HashMap<>();
			resultJson = new JSONObject();
			customerInfoArray = new JSONArray();
			orderListArray = new JSONArray();
			riderArray = new JSONArray();

			customerJSON = new JSONObject();
			orderListJSON = new JSONObject();
			riderJSON = new JSONObject();

			// Fetching Customer Information
			orderPstmt = connection.prepareStatement(customerQRY);
			orderPstmt.setString(1, orderid);
			orderRS = orderPstmt.executeQuery();

			while (orderRS.next()) {
				customerJSON.put("ORDER_ID", orderid);
				customerJSON.put("CUSTOMER_ID", orderRS.getString(1));
				customerJSON.put("CUSTOMER_NAME", orderRS.getString(2));
				customerJSON.put("EMAIL_ID", orderRS.getString(3));
				customerJSON.put("MOBILE_NUMBER", orderRS.getString(4));
				customerJSON.put("TXN_DATE", orderRS.getString(5));
				customerJSON.put("PAYMENT_MODE", orderRS.getString(6));
				customerJSON.put("CHANNEL", orderRS.getString(7));
				customerJSON.put("SHIPPING_ADDRESS", orderRS.getString(8));
				customerJSON.put("TXN_AMT", orderRS.getString(9));
				customerInfoArray.add(customerJSON);
				customerJSON.clear();
				
			}

			// Fetching Order Information
			orderPstmt = connection.prepareStatement(orderLstQRY);
			orderPstmt.setString(1, orderid);
			orderRS = orderPstmt.executeQuery();

			while (orderRS.next()) {
				orderListJSON.put("PRD_NAME", orderRS.getString(1));
				orderListJSON.put("UNIT_PRICE", orderRS.getString(2));
				orderListJSON.put("TOTAL_PRICE", orderRS.getString(3));
				orderListJSON.put("QUANTITY", orderRS.getString(4));
				orderListArray.add(orderListJSON);
				orderListJSON.clear();
			}

			// Fetching Free Rider Information
			orderPstmt = connection.prepareStatement(riderQRY);
			orderRS = orderPstmt.executeQuery();

			while (orderRS.next()) {
				riderJSON.put("RIDER_NAME", orderRS.getString(1));
				riderJSON.put("RIDER_ID", orderRS.getString(2));
				riderArray.add(riderJSON);
				riderJSON.clear();
			}

			DBUtils.closeResultSet(orderRS);
			DBUtils.closePreparedStatement(orderPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("CUSTOMER_INFO", customerInfoArray);
			resultJson.put("ORDER_LIST", orderListArray);
			resultJson.put("RIDER_LIST", riderArray);
			orderDetailsMap.put("ORDER_INFO", resultJson);
			this.responseDTO.setData(orderDetailsMap);

		} catch (Exception e) {
			logger.info("Exception in [OrdersDAO] [fetchCustomerInfo] :: " + e.getMessage());
		}

		return this.responseDTO;
	}

	/*
	 * public ResponseDTO orderInformation(RequestDTO requestDTO) {
	 * 
	 * logger.debug("Inside OrdersDAO orderInformation... ");
	 * 
	 * Connection connection = null; CallableStatement ordPstmt = null; ResultSet
	 * ordRS = null;
	 * 
	 * //Select order list PreparedStatement listPstmt = null; ResultSet listRS =
	 * null;
	 * 
	 * JSONArray orderJSONArray = null; JSONObject resultJson = null; JSONObject
	 * productList = null; JSONObject json = null; JSONObject jout = null;
	 * 
	 * String ordid = null;
	 * 
	 * String orderQry = ""; String name[] = null; HashMap<String, Object> orderMap
	 * = null; try { responseDTO = new ResponseDTO(); requestJSON =
	 * requestDTO.getRequestJSON(); ordid = requestJSON.getString("ORDID"); orderMap
	 * = new HashMap<String, Object>(); resultJson = new JSONObject();
	 * 
	 * json = new JSONObject(); jout = new JSONObject();
	 * 
	 * requestJSON2 = new JSONObject();
	 * 
	 * productList = new JSONObject(); orderJSONArray = new JSONArray();
	 * 
	 * 
	 * connection = connection == null ? DBConnector.getConnection():connection;
	 * logger.debug("Connection is [" + (connection) + "]");
	 * logger.debug("Order ID is [" + ordid + "]");
	 * 
	 * String callProc = "{call GETORDERDETAILS(?,?,?,?,?,?,?,?,?,?)}";
	 * CallableStatement cs = null; cs = connection.prepareCall(callProc);
	 * cs.setString(1, ordid); cs.registerOutParameter(2, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(3, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(4, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(5, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(6, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(7, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(8, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(9, java.sql.Types.VARCHAR);
	 * cs.registerOutParameter(10, java.sql.Types.NUMERIC);
	 * 
	 * cs.execute();
	 * 
	 * requestJSON2.put("ORDER_ID", ordid); requestDTO.setRequestJSON(requestJSON2);
	 * 
	 * responseDTO2 = orderList(requestDTO); responseJSON2 = (JSONObject)
	 * responseDTO2.getData().get("ORDER_PRODUCTS");
	 * 
	 * JSONArray products = responseJSON2.getJSONArray("PRODUCT_LIST");
	 * System.out.println("PRODUCT array is :: "+products);
	 * 
	 * json.put("ORD_ID", ordid); json.put("PRODUCTS", products);
	 * json.put("TXN_DATE", cs.getString(2)); json.put("CHANNEL", cs.getString(3));
	 * json.put("CUSTOMERNAME", cs.getString(4)); json.put("CUSTOMEREMAIL",
	 * cs.getString(5)); json.put("CUSTOMERMOBILENO", cs.getString(6));
	 * json.put("SHIPPINGADDRESS", cs.getString(7)); json.put("PAYMENTMODE",
	 * cs.getString(8)); json.put("CIN", cs.getString(9)); json.put("RESULT",
	 * cs.getString(10)); orderJSONArray.add(json); json.clear();
	 * 
	 * resultJson.put("ORDER_DETAILS", orderJSONArray);
	 * orderMap.put("ORDER_DETAILS", resultJson); this.logger.info("EntityMap [" +
	 * orderMap + "]"); this.responseDTO.setData(orderMap);
	 * 
	 * } catch (SQLException e) { logger.debug("SQLException in orderInformation ["
	 * + e.getMessage() + "]");
	 * responseDTO.addError("Internal Error Occured While Executing."); } catch
	 * (Exception e) { logger.debug("Exception in orderInformation  [" +
	 * e.getMessage() + "]");
	 * responseDTO.addError("Internal Error Occured While Executing."); } finally {
	 * DBUtils.closeResultSet(ordRS); DBUtils.closeCallableStatement(ordPstmt);
	 * DBUtils.closeConnection(connection); ordid = null; orderQry = null;
	 * productList.clear(); orderMap = null; }
	 * 
	 * return this.responseDTO; }
	 */

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

}
