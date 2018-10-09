package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.posta.util.PostaMobileEncryptTripleDes;
import com.ceva.util.CommonUtil;
import com.ceva.util.DBUtils;

public class PayBillDAO {

	private Logger logger = Logger.getLogger(PayBillDAO.class);

	private ResponseDTO responseDTO = null;
	private JSONObject requestJSON = null;

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

			merchantQry = "SELECT MABT.BILLER_TYPE_NAME,MABI.BILLER_ID,MABT.DESCRIPTION,MABT.CREATED_BY,"
					+ "to_char(MABT.DATE_CREATED,'DD-MM-YYYY HH24:MI:SS')  "
					+ "from MPESA_ACCTMGT_BILLER_TYPE MABT, MPESA_ACCTMGT_BILLER_ID MABI where MABI.BILLER_TYPE_NAME=MABT.BILLER_TYPE_NAME";
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			json = new JSONObject();

			while (merchantRS.next()) {
				json.put("billerType", merchantRS.getString(1));
				json.put("billerId", merchantRS.getString(2));
				json.put("description", merchantRS.getString(3));
				json.put("createdBy", merchantRS.getString(4));
				json.put("createdDate", merchantRS.getString(5));
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

	public ResponseDTO getBillerTypeDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetMPesaDashBoard.. ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;

		PreparedStatement merchantPstmt = null;

		ResultSet merchantRS = null;
		Connection connection = null;

		String merchantQry = "";

		JSONObject json = null;
		try {

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			merchantQry = "SELECT MABT.BILLER_TYPE_NAME from MPESA_ACCTMGT_BILLER_TYPE MABT";
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			json = new JSONObject();

			while (merchantRS.next()) {
				json.put(merchantRS.getString(1), merchantRS.getString(1));
			}

			resultJson.put("billerTypeName", json);
			merchantMap.put(CevaCommonConstants.MERCHANT_DATA, resultJson);
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
 			merchantQry = null;
		}
		return responseDTO;
	}

	public ResponseDTO insertBillerType(RequestDTO requestDTO) {

		logger.debug("Inside InsertBillerType DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject payBillBean = null;

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			payBillBean = requestJSON.getJSONObject("payBillBean");
			billerDataMap = new HashMap<String, Object>();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callable = connection
					.prepareCall("{call insertBillerTypeDetails(?,?,?,?,?)}");
			callable.setString(1, payBillBean.getString("billerTypeName"));
			callable.setString(2, payBillBean.getString("description"));
			callable.setString(3, payBillBean.getString("bfubaccount"));
			callable.setString(4, requestJSON.getString("makerId"));
			callable.registerOutParameter(5, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(5).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}
			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in InsertBillerType [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in InsertBillerType [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertBillerId(RequestDTO requestDTO) {

		logger.debug("Inside InsertBillerId DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject payBillBean = null;

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			payBillBean = requestJSON.getJSONObject("payBillBean");
			billerDataMap = new HashMap<String, Object>();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callable = connection
					.prepareCall("{call insertBillerIdDetails(?,?,?,?,?,?)}");
			callable.setString(1, payBillBean.getString("billerTypeName"));
			callable.setString(2, payBillBean.getString("description"));
			callable.setString(3, payBillBean.getString("bfubaccount"));
			callable.setString(4, requestJSON.getString("makerId"));
			callable.setString(5, payBillBean.getString("billerId"));
			callable.registerOutParameter(6, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(6).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}
			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in InsertBillerId [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in InsertBillerId [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}
}
