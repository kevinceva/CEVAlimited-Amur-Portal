package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class AjaxDAO {

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;

	private Logger logger = Logger.getLogger(AjaxDAO.class);

	public ResponseDTO checkMerchantId(RequestDTO requestDTO) {
		logger.debug("Inside AjaxDAO... ");

		Connection connection = null;

		HashMap<String, Object> merchantDataMap = null;
		JSONObject resultJson = null;

		String merchantId = "";

		PreparedStatement merchantChkPstmt = null;
		ResultSet merchantChkRS = null;
		int ResCount = 0;
		String merchantChkQry = "";
		try {
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			merchantChkQry = "Select count(*) from MERCHANT_MASTER where MERCHANT_ID =?";

			resultJson = new JSONObject();
			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is  [" + connection + "]");
			merchantDataMap = new HashMap<String, Object>();
			merchantId = requestJSON.getString(CevaCommonConstants.MERCHANT_ID);

			merchantChkPstmt = connection.prepareStatement(merchantChkQry);
			merchantChkPstmt.setString(1, merchantId);

			merchantChkRS = merchantChkPstmt.executeQuery();
			while (merchantChkRS.next()) {
				ResCount = merchantChkRS.getInt(1);
			}
			logger.debug("Res Count [" + ResCount + "]");

			resultJson.put(CevaCommonConstants.RESULT_COUNT, ResCount);
			merchantDataMap.put(CevaCommonConstants.MERCHANT_CHECK_INFO,
					resultJson);

			logger.debug("MerchantDataMap [" + merchantDataMap + "]");
			responseDTO.setData(merchantDataMap);
			logger.debug("Response DTO    [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in checkMerchantId [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in checkMerchantId [" + e.getMessage()
					+ "]");
		} finally {
			merchantDataMap = null;
			resultJson = null;
			ResCount = 0;
			merchantChkQry = null;
			merchantId = null;
			DBUtils.closeResultSet(merchantChkRS);
			DBUtils.closePreparedStatement(merchantChkPstmt);
			DBUtils.closeConnection(connection);
			requestDTO = null;
			requestJSON = null;
		}
		return responseDTO;
	}

	public ResponseDTO generateMerchantId(RequestDTO requestDTO) {
		logger.debug("Inside GenerateMerchantId... ");

		StringBuffer merchantId = null;
		StringBuffer storeId = null;
		String merchantName = "";
		String merchantChkQry = "";
		String merchantKey = "";

		HashMap<String, Object> merchantDataMap = null;
		JSONObject resultJson = null;

		Connection connection = null;
		ResultSet merchantChkRS = null;
		PreparedStatement merchantChkPstmt = null;
		int resCount = 0;

		try {
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resultJson = new JSONObject();
			merchantDataMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection  + "]");

			merchantChkQry = "Select count(*) from MERCHANT_MASTER where MERCHANT_NAME like ?";
			merchantName = requestJSON
					.getString(CevaCommonConstants.MERCHANT_NAME);
			merchantKey = merchantName.substring(0, 5);

			merchantChkPstmt = connection.prepareStatement(merchantChkQry);
			merchantChkPstmt.setString(1, merchantKey + "%");

			merchantChkRS = merchantChkPstmt.executeQuery();
			if (merchantChkRS.next()) {
				resCount = merchantChkRS.getInt(1);
			}

			logger.debug("ResCount [" + resCount + "]");

			merchantId = new StringBuffer(10);
			merchantId.append("");

			storeId = new StringBuffer(10);
			storeId.append("");

			resCount += 1;

			/*
			 * merchantId = merchantKey + "0000000000" + i; storeId =
			 * merchantKey + i + "-S000" + i;
			 */
			merchantId.append(merchantKey).append(
					StringUtils.leftPad(String.valueOf(resCount), 10, '0'));
			storeId.append(merchantKey)
					.append("-S")
					.append(StringUtils.leftPad(String.valueOf(resCount), 4,
							'0'));

			resultJson.put(CevaCommonConstants.RESULT_COUNT, resCount);
			resultJson.put(CevaCommonConstants.MERCHANT_ID,
					merchantId.toString());
			resultJson.put(CevaCommonConstants.STORE_ID, storeId.toString());

			merchantDataMap.put(CevaCommonConstants.MERCHANT_INFO, resultJson);

			logger.debug("MerchantDataMap [" + merchantDataMap + "]");
			responseDTO.setData(merchantDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in GenerateMerchantId ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in GenerateMerchantId [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(merchantChkRS);
			DBUtils.closePreparedStatement(merchantChkPstmt);
			DBUtils.closeConnection(connection);

			merchantId.delete(0, merchantId.length());
			merchantId = null;
			storeId.delete(0, storeId.length());
			storeId = null;
			merchantName = null;
			merchantChkQry = null;
			merchantKey = null;

			merchantDataMap = null;
			resultJson = null;
		}
		return responseDTO;
	}

	public ResponseDTO getTerminals(RequestDTO requestDTO) {
		logger.debug("Inside GetTerminalsDAO... ");
		Connection connection = null;

		HashMap<String, Object> terminalMap = null;
		JSONObject resultJson = null;
		JSONArray terminalJSONArray = null;

		String merchantId = "";
		String terminalQry = "";

		PreparedStatement terminalPstmt = null;
		ResultSet terminalRS = null;
		JSONObject json = null;
		try {
			terminalMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			terminalJSONArray = new JSONArray();

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			merchantId = requestJSON.getString(CevaCommonConstants.MERCHANT_ID);
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,MAKER_DTTM"
					+ " from TERMINAL_MASTER_TEMP  where  MERCHANT_ID=?";

			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1, merchantId);

			terminalRS = terminalPstmt.executeQuery();

			json = new JSONObject();
			while (terminalRS.next()) {

				json.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(1));
				json.put(CevaCommonConstants.STORE_ID, terminalRS.getString(2));
				json.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE,
						terminalRS.getString(4));
				terminalJSONArray.add(json);
				json.clear();
			}
			logger.debug("Terminal JSONArray [" + terminalJSONArray + "]");
			resultJson
					.put(CevaCommonConstants.TERMINAL_LIST, terminalJSONArray);
			terminalMap.put(CevaCommonConstants.TERMINAL_LIST, resultJson);
			responseDTO.setData(terminalMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in getTerminals [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in getTerminals [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);
			terminalMap = null;
			resultJson = null;
			terminalJSONArray = null;
			merchantId = null;
			terminalQry = null;
		}
		return responseDTO;
	}

	public ResponseDTO getStores(RequestDTO requestDTO) {
		logger.debug("Inside GetStores DAO.. ");
		String storeQry = "";

		HashMap<String, Object> resultMap = null;
		JSONArray storeJSONArray = null;
		Connection connection = null;

		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;
		
		JSONObject json = null;
		try {
			resonseJSON = requestJSON;
			logger.debug("Request JSON [" + requestJSON + "]");
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			resultMap = new HashMap<String, Object>();
			storeJSONArray = new JSONArray();
			connection = connection == null ? DBConnector.getConnection():connection;
			
			logger.debug("Connection is [" + connection + "]");
			storeQry = "SELECT STORE_ID,STORE_NAME from STORE_MASTER_TEMP  where MERCHANT_ID=?";
			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeRS = storePstmt.executeQuery();
			
			
			json = new JSONObject();
			while (storeRS.next()) { 
				json.put(CevaCommonConstants.SELECT_KEY, storeRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, storeRS.getString(1));
				storeJSONArray.add(json);
				json.clear();
			}
			resonseJSON.put(CevaCommonConstants.STORE_LIST, storeJSONArray);
			logger.debug("Response JSON [" + resonseJSON + "]");
			resultMap.put(CevaCommonConstants.STORE_LIST, resonseJSON);
			responseDTO.setData(resultMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in  GetStores DAO [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in  GetStores DAO [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
			storeQry = null;
			resultMap = null;
			storeJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO checkTransactionType(RequestDTO requestDTO) {

		logger.debug("Inside CheckTransactionType DAO ... ");
		Connection connection = null;

		HashMap<String, Object> UserDataMap = null;
		JSONObject resultJson = null;
		String accounttype = "";
		String service = "";

		PreparedStatement userChkPstmt = null;
		ResultSet USerChkRS = null;
		String userChkQry = "";
		int ResCount = 0;

		try {
			resultJson = new JSONObject();
			UserDataMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			accounttype = requestJSON
					.getString(CevaCommonConstants.ACCOUNT_TYPE);
			service = requestJSON.getString(CevaCommonConstants.SERVICE_TYPE);
			userChkQry = "Select count(*) from FINANCIAL_MASTER where trim(BANK_CODE)=trim(?) AND trim(TRANS_TYPE)=trim(?)";

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection+ "]");

			userChkPstmt = connection.prepareStatement(userChkQry);
			userChkPstmt.setString(1, service);
			userChkPstmt.setString(2, accounttype);

			USerChkRS = userChkPstmt.executeQuery();
			if (USerChkRS.next()) {
				ResCount = USerChkRS.getInt(1);
			}

			logger.debug("ResCount [" + ResCount + "]");

			resultJson.put(CevaCommonConstants.RESULT_COUNT, ResCount);
			UserDataMap.put(CevaCommonConstants.USER_CHECK_INFO, resultJson);

			logger.debug("MerchantDataMap [" + UserDataMap + "]");
			responseDTO.setData(UserDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in  CheckTransactionType DAO ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in  CheckTransactionType DAO ["
					+ e.getMessage() + "]");
		}

		finally {
			DBUtils.closeResultSet(USerChkRS);
			DBUtils.closePreparedStatement(userChkPstmt);
			DBUtils.closeConnection(connection);
			UserDataMap = null;
			resultJson = null;
			accounttype = null;
			service = null;
			userChkQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO checkUserId(RequestDTO requestDTO) {
		logger.debug("Inside checkUserId DAO... ");
		JSONObject json = null;

		JSONArray userJSONArray = null;
		HashMap<String, Object> bankDataMap = null;
		JSONObject resultJson = null;

		String rrn = "";
		String userChkQry = "";

		Connection connection = null;
		PreparedStatement userChkPstmt = null;
		ResultSet USerChkRS = null;

		try {
			userChkQry = "select RRN,RESPONSECODE from LIVE_TRANLOG where RRN=?";
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			userJSONArray = new JSONArray();
			resultJson = new JSONObject();

			bankDataMap = new HashMap<String, Object>();

			rrn = requestJSON.getString(CevaCommonConstants.RRN);

			userChkPstmt = connection.prepareStatement(userChkQry);
			userChkPstmt.setString(1, rrn);
			USerChkRS = userChkPstmt.executeQuery();
			json = new JSONObject();
			while (USerChkRS.next()) {
				
				json.put(CevaCommonConstants.RRN, USerChkRS.getString(1));
				json.put(CevaCommonConstants.responseCode,
						USerChkRS.getString(2));
				userJSONArray.add(json);
				json.clear();
			}

			logger.debug("UserJSONArray [" + userJSONArray + "]");
			resultJson.put(CevaCommonConstants.RESULT_COUNT, userJSONArray);
			bankDataMap.put(CevaCommonConstants.USER_CHECK_INFO, resultJson);

			logger.debug("MerchantDataMap [" + bankDataMap + "]");
			responseDTO.setData(bankDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in CheckUserId [" + e.getMessage() + "]");
		} finally {
			requestDTO = null;
			requestJSON = null;
			json = null;
			userJSONArray = null;
			bankDataMap = null;
			resultJson = null;
			rrn = null;

			DBUtils.closeResultSet(USerChkRS);
			DBUtils.closePreparedStatement(userChkPstmt);
			DBUtils.closeConnection(connection);
			userChkQry = null;
		}

		return responseDTO;
	}
}
