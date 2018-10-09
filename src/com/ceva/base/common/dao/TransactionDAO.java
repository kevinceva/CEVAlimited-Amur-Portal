package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransactionDAO {
	private Logger logger = Logger.getLogger(OrdersDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	//Fetching Transactions
	public ResponseDTO fetchTransactions(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [TransactionDAO][fetchTransactions].. ");

		HashMap<String, Object> txnDetailsMap = null;
		JSONObject resultJson = null;
		JSONArray txnJSONArray = null;
		PreparedStatement txnPstmt = null;
		ResultSet txnRS = null;
		JSONObject json = null;
		
		String txnType = null;
		String startDate = null;
		String endDate = null;

		String orderQRY = "SELECT OM.ORDER_ID, CM.FNAME||' '||CM.LNAME, OM.TXN_AMT, OM.PAYMENT_MODE, OM.CHANNEL, to_char( OM.TXN_DATE , 'DD-MM-YYYY HH:MM:SS' ) FROM ORDER_MASTER OM, CUSTOMER_MASTER CM WHERE NOT(OM.TXN_DATE > TO_DATE(?,'DD/MM/YY') OR OM.TXN_DATE < TO_DATE(?,'DD/MM/YY')) AND OM.CIN = CM.CIN AND OM.ORDER_TYPE = ?";
		try {
			
			requestJSON = requestDTO.getRequestJSON();
			
			txnType = requestJSON.getString("txnType");
			startDate = requestJSON.getString("startDate");
			endDate = requestJSON.getString("endDate");
			
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			txnDetailsMap = new HashMap();
			resultJson = new JSONObject();
			txnJSONArray = new JSONArray();

			txnPstmt = connection.prepareStatement(orderQRY);
			txnPstmt.setString(1, endDate);
			txnPstmt.setString(2, startDate);
			txnPstmt.setString(3, txnType);
			txnRS = txnPstmt.executeQuery();

			json = new JSONObject();
			while (txnRS.next()) {
				json.put("ORDER_ID", txnRS.getString(1));
				json.put("CUSTOMER_NAME", txnRS.getString(2));
				json.put("TXN_AMOUNT", txnRS.getString(3));
				json.put("PAYMENT_MODE", txnRS.getString(4));
				json.put("CHANNEL", txnRS.getString(5));
				json.put("TXN_DATE", txnRS.getString(6));
				txnJSONArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(txnRS);
			DBUtils.closePreparedStatement(txnPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("TXNS", txnJSONArray);
			txnDetailsMap.put("TXNS", resultJson);
			this.logger.info("EntityMap [" + txnDetailsMap + "]");
			this.responseDTO.setData(txnDetailsMap);

		} catch (Exception e) {
			this.logger.debug("Got Exception in fetchUnassignedOrders DAO [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(txnRS);
			DBUtils.closePreparedStatement(txnPstmt);
			DBUtils.closeConnection(connection);

			txnDetailsMap = null;
			resultJson = null;
			txnJSONArray = null;
		}

		return this.responseDTO;
	}
	
}
