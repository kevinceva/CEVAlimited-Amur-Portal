package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.base.common.utils.EncryptTransactionPin;
import com.ceva.util.DBUtils;

public class BillPaymentDAO {

	private Logger logger = Logger.getLogger(BillPaymentDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO billerDetails(RequestDTO requestDTO) {

		logger.debug("Inside BillerDetails ... ");
		HashMap<String, Object> billerMap = null;

		Connection connection = null;
		PreparedStatement billerPstmt = null;
		ResultSet billerRS = null;

		String billerQry = "";
		JSONObject json = null;

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			json = new JSONObject();

			billerMap = new HashMap<String, Object>();

			billerQry = "Select DISTINCT BILLER_TYPE from  BILLER_REGISTRATION where BILLER_TYPE<>'BILLER' order by BILLER_TYPE";

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			billerPstmt = connection.prepareStatement(billerQry);
			billerRS = billerPstmt.executeQuery();

			while (billerRS.next()) {
				json.put(billerRS.getString(1), billerRS.getString(1));
			}
			json.put("Other","Other Billers");
			responseJSON.put(CevaCommonConstants.BILLER_LIST, json);

			json.clear();

			billerMap.put(CevaCommonConstants.BILLER_LIST, responseJSON);

			logger.debug("BillerMap [" + billerMap + "]");
			responseDTO.setData(billerMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in BillPaymentCreateDetails["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in BillPaymentCreateDetails ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(billerRS);
			DBUtils.closePreparedStatement(billerPstmt);
			DBUtils.closeConnection(connection);
			billerQry = null;
			json = null;
			billerMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO billPayVerifyPin(RequestDTO requestDTO) {

		logger.debug("Inside BillPayVerifyPin... ");

		Connection connection = null;
		PreparedStatement billerPstmt = null;
		ResultSet billerRS = null;

		int resCount = 0;
		String cryptedPassword = "";
		String key1 = "";

		String billerQry = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			key1 = "97206B46CE46376894703ECE161F31F2";
			logger.debug("Before Enryption Value is  ["
					+ requestJSON.getString(CevaCommonConstants.PIN) + "]");

			try {
				cryptedPassword = EncryptTransactionPin.encrypt(key1,
						requestJSON.getString(CevaCommonConstants.PIN), 'F');

			} catch (Exception e) {
				logger.debug("Exception in encrypting password cryptedPassword ["
						+ cryptedPassword + "] message[" + e.getMessage() + "]");
				cryptedPassword = "";
			}

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			billerQry = "Select COUNT(*) from USER_LOGIN_CREDENTIALS  where PIN=? and LOGIN_USER_ID=trim(?)";
			billerPstmt = connection.prepareStatement(billerQry);
			billerPstmt.setString(1, cryptedPassword);
			billerPstmt.setString(2, requestJSON.getString(CevaCommonConstants.MAKER_ID));
			billerRS = billerPstmt.executeQuery();

			if (billerRS.next()) {
				resCount = billerRS.getInt(1);
			}

			if (resCount == 1) {
				responseDTO.addMessages("Pin Verification Success");
			} else {
				responseDTO.addError("Pin Verification Failed");
			}

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in BillPayVerifyPin [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in BillPayVerifyPin [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(billerRS);
			DBUtils.closePreparedStatement(billerPstmt);
			DBUtils.closeConnection(connection);
			cryptedPassword = null;
			key1 = null;
			billerQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertBillDetails(RequestDTO requestDTO) {
		logger.debug("Inside InsertBillDetails DAO ... ");

		CallableStatement callableStatement = null;
		Connection connection = null;

		String mobileNo = null;
		String refNo = "";
		String insertBillDetailsProc = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			insertBillDetailsProc = "{call insertBillDetailsProc(?,?,?,?,?,?,?,?,?,?)}";
			callableStatement = connection.prepareCall(insertBillDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON.getString("billerCode"));
			
			logger.debug("Cust Account Number ::"+requestJSON.getString("customerAccount"));
			logger.debug("biller Account Number ::"+requestJSON.getString("accountNo"));
			String customerAccount = null;
			if(requestJSON.getString("customerAccount") == null || requestJSON.getString("customerAccount") == ""){
				customerAccount = requestJSON.getString("accountNo");
			}else{
				customerAccount = requestJSON.getString("customerAccount");
			}
			callableStatement.setString(3,
					customerAccount);
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.MODE_OF_PAYMENT));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.AMOUNT));
			callableStatement.setString(6, requestJSON.getString("mobileNo"));
			callableStatement.setString(7, requestJSON.getString("narration"));
			callableStatement.setString(8, requestJSON.getString("accountName"));
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			refNo = callableStatement.getString(9);

			logger.debug("Reference Number is  [" + refNo + "]  Message ["
					+ callableStatement.getString(10) + "]");

			if (refNo.indexOf("NO") == -1) {
				responseDTO.addMessages(callableStatement.getString(10));
			} else {
				responseDTO.addError(callableStatement.getString(10));
			}

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in InsertBillDetails [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in InsertBillDetails [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			mobileNo = null;
			refNo = null;
			insertBillDetailsProc = null;
		}
		return responseDTO;
	}

	public ResponseDTO getMPesaDashBoard(RequestDTO requestDTO) {

		logger.debug("Inside GetMPesaDashBoard.. ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJSONArray = null;

		PreparedStatement merchantPstmt = null;

		ResultSet merchantRS = null;
		Connection connection = null;

		String merchantQry = "";

		JSONObject json = null;
		try {

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJSONArray = new JSONArray();
			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			merchantQry = "SELECT REFERENCE_NO,KEY_ID,BILLER_ID,PAYMENT_AMOUNT,"
					+ "decode(PAYMENT_STATUS,'P','Pending','S','Success',PAYMENT_STATUS),"
					+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
					+ "from BILL_PAYMENT_MASTER where PAYMENT_MODE=?";
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, "MPESA");
			merchantRS = merchantPstmt.executeQuery();

			json = new JSONObject();

			while (merchantRS.next()) {
				json.put(CevaCommonConstants.REFERENCE_NO,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.CUSTOMER_KEY,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.BILLER_ID, merchantRS.getString(3));
				json.put(CevaCommonConstants.AMOUNT, merchantRS.getString(4));
				json.put(CevaCommonConstants.STATUS, merchantRS.getString(5));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(6));
				merchantJSONArray.add(json);
				json.clear();
			}
			logger.debug("MerchantJSONArray [" + merchantJSONArray + "]");
			resultJson.put(CevaCommonConstants.MERCHANT_DASHBOARD,
					merchantJSONArray);

			merchantMap.put(CevaCommonConstants.MERCHANT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in  GetMPesaDashBoard ["
					+ e.getMessage() + "]");
		} catch (Exception e) {

			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in  GetMPesaDashBoard [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			resultJson = null;
			merchantJSONArray = null;
			merchantQry = null;
		}
		return responseDTO;
	}
}
