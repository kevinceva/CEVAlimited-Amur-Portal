package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class CashDepositLimitDAO {

	Logger logger = Logger.getLogger(CashDepositLimitDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getCashDepositLimitGenerateGrid(RequestDTO requestDTO) {

		logger.debug("Inside GetCashDepositLimitGenerateGrid... ");

		Connection connection = null;
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;
		JSONArray storeJSONArray = null;
		JSONArray terminalJSONArray = null;

		ArrayList<String> SID = null;
		ArrayList<String> MID = null;
		ArrayList<String> TMIDS = null;

		PreparedStatement merchantPstmt = null;
		PreparedStatement terminalPstmt = null;

		ResultSet merchantRS = null;
		ResultSet terminalRS = null;

		String terminalQry = "";
		String sid = "";
		String merchantQry = "select distinct MERCHANT_ID from MERCHANT_MASTER";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			terminalJSON = new JSONObject();
			storeJSONArray = new JSONArray();
			terminalJSONArray = new JSONArray();
			SID = new ArrayList<String>();
			MID = new ArrayList<String>();
			TMIDS = new ArrayList<String>();

			connection = connection == null ? DBConnector.getConnection():connection;
			
			logger.debug("Connection is null [" + connection + "]");

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			while (merchantRS.next()) {
				MID.add(merchantRS.getString(1));
			}

			merchantQry = "Select STORE_ID,STORE_NAME,MERCHANT_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from STORE_MASTER order by MERCHANT_ID,STORE_ID";
			merchantPstmt.close();
			merchantRS.close();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			JSONObject json = null;
			while (merchantRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.STORE_ID, merchantRS.getString(1));
				json.put(CevaCommonConstants.STORE_NAME,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.MERCHANT_ID,
						merchantRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(4));
				storeJSONArray.add(json);
			}
			logger.debug("Inside [storeJSONArray:::" + storeJSONArray + "]");

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			

			merchantQry = "select distinct MERCHANT_ID from TERMINAL_MASTER";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			while (merchantRS.next()) {
				TMIDS.add(merchantRS.getString(1));
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			for (int i = 0; i < TMIDS.size(); i++) {
				merchantQry = "select distinct STORE_ID from TERMINAL_MASTER where MERCHANT_ID=?";
				merchantPstmt = connection.prepareStatement(merchantQry);
				merchantPstmt.setString(1, TMIDS.get(i));

				merchantRS = merchantPstmt.executeQuery();

				while (merchantRS.next()) {
					terminalJSONArray = new JSONArray();
					SID.add(merchantRS.getString(1));
					terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,STATUS,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
							+ " from TERMINAL_MASTER  where STORE_ID=? and MERCHANT_ID=?";

					terminalPstmt = connection.prepareStatement(terminalQry);
					sid = merchantRS.getString(1);
					terminalPstmt.setString(1, merchantRS.getString(1));
					terminalPstmt.setString(2, TMIDS.get(i));

					terminalRS = terminalPstmt.executeQuery();

					while (terminalRS.next()) {
						json = new JSONObject();
						json.put(CevaCommonConstants.TERMINAL_ID,
								terminalRS.getString(1));
						json.put(CevaCommonConstants.STORE_ID,
								terminalRS.getString(2));
						json.put(CevaCommonConstants.MERCHANT_ID,
								terminalRS.getString(3));
						json.put(CevaCommonConstants.STATUS,
								terminalRS.getString(4));
						json.put(CevaCommonConstants.MAKER_DATE,
								terminalRS.getString(5));
						terminalJSONArray.add(json);
					}
					terminalJSON.put(TMIDS.get(i) + "_" + sid + "_TERMINALS",
							terminalJSONArray);

					DBUtils.closeResultSet(terminalRS);
					DBUtils.closePreparedStatement(terminalPstmt);
				}
				DBUtils.closeResultSet(merchantRS);
				DBUtils.closePreparedStatement(merchantPstmt);	
			}

			resultJson.put(CevaCommonConstants.STORE_LIST, storeJSONArray);
			storeMap.put(CevaCommonConstants.STORE_LIST, resultJson);
			storeMap.put(CevaCommonConstants.TERMINAL_DATA, terminalJSON);
			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in GetCashDepositLimitGenerateGrid["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in GetCashDepositLimitGenerateGrid ["
					+ e.getMessage() + "]");
		} finally {

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			terminalJSON = null;
			storeJSONArray = null;
			terminalJSONArray = null;
			requestJSON = null;
			SID = null;
			MID = null;
			TMIDS = null;
			terminalQry = null;
			sid = null;
			merchantQry = null;

		}

		return responseDTO;
	}

	public ResponseDTO getStoreCashDepositLimitCreateScreen(
			RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCashDepositLimitCreateScreen... ");
		HashMap<String, Object> storeMap = null;

		Connection connection = null;

		PreparedStatement storePstmt = null;
		CallableStatement callableStatement = null;
		ResultSet storeRS = null;

		String validateStoreLimitDetailsProc = "{call validateStoreCashDepositLimit(?,?,?,?)}";
		try {
			storeMap = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(validateStoreLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);

			logger.debug("ResCnt [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO = getCashDepositLimitGenerateGrid(requestDTO);
				responseDTO.addError("Store is in  Blocked state.");
			} else if (resCnt == 2) {
				responseDTO = getCashDepositLimitGenerateGrid(requestDTO);
				responseDTO
						.addError("Cash Deposit Limit already assigned to this store.");
			} else if (resCnt == 3) {
				responseDTO = getCashDepositLimitGenerateGrid(requestDTO);
				responseDTO
						.addError("Limit not assigned to terminals under this store.");
			} else {

				responseJSON = new JSONObject();

				String cashDepositLimit = callableStatement.getString(4);
				String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN "
						+ "from STORE_MASTER  SM,MERCHANT_MASTER MM"
						+ " where MM.MERCHANT_ID=SM.MERCHANT_ID and trim(SM.MERCHANT_ID)=trim(?) and trim(SM.STORE_ID)=trim(?)";

				storePstmt = connection.prepareStatement(storeQry);
				storePstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
				storePstmt.setString(2,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				storeRS = storePstmt.executeQuery();
				while (storeRS.next()) {
					responseJSON.put(CevaCommonConstants.STORE_ID,
							storeRS.getString(1));
					responseJSON.put(CevaCommonConstants.STORE_NAME,
							storeRS.getString(2));
					responseJSON.put(CevaCommonConstants.MERCHANT_ID,
							storeRS.getString(3));
					responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
							storeRS.getString(4));
					responseJSON.put(CevaCommonConstants.LOCATION_NAME,
							storeRS.getString(5));
					responseJSON.put(CevaCommonConstants.KRA_PIN,
							storeRS.getString(6));
				}

				if (cashDepositLimit != null) {
					responseJSON.put("storeLimit", cashDepositLimit);
				}

				storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
				logger.debug("StoreMap [" + storeMap + "]");
				responseDTO.setData(storeMap);
				logger.debug("Response DTO [" + responseDTO + "]");

			}
		} catch (SQLException e) {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in GetStoreCashDepositLimitCreateScreen ["
					+ e.getMessage() + "]");
			e.printStackTrace();
		} catch (Exception e) {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in GetStoreCashDepositLimitCreateScreen  ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			storeMap = null;
			requestJSON = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertStoreCashDepositLimitDetails(RequestDTO requestDTO) {

		logger.debug("Inside InsertStoreCashDepositLimitDetails... ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertStoreLimitDetailsProc = "{call insertStoreCashDptLimitProc(?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertStoreLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.STORE_LIMIT));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Store Cash Deposit Limit Information Stored Successfully. ");
			} else if (resCnt == -1) {

				responseDTO
						.addError("Store Cash Deposit Limit already assigned to this store. ");
			} else {
				responseDTO
						.addError("Store Cash Deposit Limit Information Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InsertStoreCashDepositLimitDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in InsertStoreCashDepositLimitDetails  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertStoreLimitDetailsProc = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreCashDepositLimitModifyScreen(
			RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCashDepositLimitModifyScreen.. ");
		HashMap<String, Object> storeMap = null;

		Connection connection = null;
		PreparedStatement storePstmt = null;
		PreparedStatement terminalPstmt = null;

		ResultSet storeRS = null;
		ResultSet terminalRS = null;

		String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN,FLM.CREDIT_LMT_AMT,FLM.STATUS,FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
				+ "from STORE_MASTER  SM, MERCHANT_MASTER MM, STORE_CASHDPT_LIMIT_MASTER FLM"
				+ " where trim(MM.MERCHANT_ID)=trim(SM.MERCHANT_ID) and "
				+ "trim(MM.MERCHANT_ID)=trim(FLM.MERCHANT_ID) and trim(SM.MERCHANT_ID)=trim(FLM.MERCHANT_ID)  and "
				+ "trim(FLM.MERCHANT_ID)=trim(?) and trim(FLM.STORE_ID)=trim(?)";

		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storePstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeRS = storePstmt.executeQuery();
			while (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.STORE_ID,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_NAME,
						storeRS.getString(2));
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						storeRS.getString(3));
				responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
						storeRS.getString(4));
				responseJSON.put(CevaCommonConstants.LOCATION_NAME,
						storeRS.getString(5));
				responseJSON.put(CevaCommonConstants.KRA_PIN,
						storeRS.getString(6));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT,
						storeRS.getString(7));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeRS.getString(8));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeRS.getString(9));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeRS.getString(10));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
						storeRS.getString(11));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
						storeRS.getString(12));
			}

			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetStoreCashDepositLimitModifyScreen ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetStoreCashDepositLimitModifyScreen  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
			e.printStackTrace();
		} finally {

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
			storeMap = null;
			storeQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO updateStoreCashDepositLimitDetails(RequestDTO requestDTO) {

		logger.debug("Inside UpdateStoreCashDepositLimitDetails.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertStoreLimitDetailsProc = "{call updateStoreCashDptLmtDetProc(?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertStoreLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.STORE_LIMIT));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Store Cash Deposit Limit Information Updated Successfully. ");
			} else if (resCnt == -1) {

				responseDTO
						.addError("Store Cash Deposit Limit Information Already Exists. ");
			} else {
				responseDTO
						.addError("Store Cash Deposit  Limit Information Updation failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in UpdateStoreCashDepositLimitDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in UpdateStoreCashDepositLimitDetails  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertStoreLimitDetailsProc = null;

		}
		return responseDTO;
	}

	public ResponseDTO cashDepositLimitAuthMgmtScreen(RequestDTO requestDTO) {

		logger.debug("Inside CashDepositLimitAuthMgmtScreen.. ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONArray storeJSONArray = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;
		JSONObject json = null;
		String merchantQry = "select distinct MERCHANT_ID from STORE_CASHDPT_LIMIT_MASTER";

		try {
			responseDTO = new ResponseDTO();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			storeJSONArray = new JSONArray();
			json = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			merchantQry = "Select STORE_ID,MERCHANT_ID,CREDIT_LMT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
					+ "from STORE_CASHDPT_LIMIT_MASTER "
					+ "where STATUS='Requested' order by MERCHANT_ID,STORE_ID ";

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put(CevaCommonConstants.STORE_ID, merchantRS.getString(1));
				json.put(CevaCommonConstants.MERCHANT_ID,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.STORE_LIMIT,
						merchantRS.getString(3));
				json.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						merchantRS.getString(4));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						merchantRS.getString(5));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						merchantRS.getString(6));
				storeJSONArray.add(json);

				json.clear();
			}

			resultJson.put(CevaCommonConstants.STORE_LIST, storeJSONArray);
			storeMap.put(CevaCommonConstants.STORE_LIST, resultJson);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in CashDepositLimitAuthMgmtScreen ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in CashDepositLimitAuthMgmtScreen  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			storeJSONArray = null;
			merchantQry = null;

		}

		return responseDTO;
	}

	public ResponseDTO getStoreCashDepositLimitApproveDetails(
			RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCashDepositLimitApproveDetails... ");
		HashMap<String, Object> storeMap = null;

		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;

		Connection connection = null;

		String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN,FLM.CREDIT_LMT_AMT,FLM.STATUS,FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
				+ "from STORE_MASTER  SM, MERCHANT_MASTER MM, STORE_CASHDPT_LIMIT_MASTER FLM"
				+ " where trim(MM.MERCHANT_ID)=trim(SM.MERCHANT_ID) and trim(MM.MERCHANT_ID)=trim(FLM.MERCHANT_ID) and trim(SM.MERCHANT_ID)=trim(FLM.MERCHANT_ID)  and  trim(SM.STORE_ID)=trim(FLM.STORE_ID)  and trim(FLM.MERCHANT_ID)=trim(?) and trim(FLM.STORE_ID)=trim(?) and  trim(FLM.STATUS)=(?)";

		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storePstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storePstmt.setString(3, "Requested");

			storeRS = storePstmt.executeQuery();
			while (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.STORE_ID,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_NAME,
						storeRS.getString(2));
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						storeRS.getString(3));
				responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
						storeRS.getString(4));
				responseJSON.put(CevaCommonConstants.LOCATION_NAME,
						storeRS.getString(5));
				responseJSON.put(CevaCommonConstants.KRA_PIN,
						storeRS.getString(6));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT,
						storeRS.getString(7));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeRS.getString(8));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeRS.getString(9));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeRS.getString(10));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
						storeRS.getString(11));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
						storeRS.getString(12));
			}

			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("StoreMap [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetStoreCashDepositLimitApproveDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetStoreCashDepositLimitApproveDetails  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
			storeMap = null;
			storeQry = null;
		}
		return responseDTO;
	}

	public ResponseDTO StoreCashDepositLimitApprove(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside StoreCashDepositLimitApprove... ");

		CallableStatement callableStatement = null;
		String insertTerminalLimitDetailsProc = "{call approveStoreCashDptLmtProc(?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertTerminalLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.STORE_LIMIT));
			callableStatement.setString(5, requestJSON
					.getString(CevaCommonConstants.STORE_LIMIT_APPROVE));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);

			logger.debug("ResultCnt from DB is [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Store Cash Deposit Limit Information Approved Successfully. ");
			} else if (resCnt == -1) {
				responseDTO
						.addError("Store Cash Deposit Limit Information Approved Already");
			} else {
				responseDTO
						.addError("Store Cash Deposit Limit Information Approved failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in StoreCashDepositLimitApprove ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in StoreCashDepositLimitApprove  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			insertTerminalLimitDetailsProc = null;
		}
		return responseDTO;
	}

	public ResponseDTO getRecoveryGenerateGrid(RequestDTO requestDTO) {

		logger.debug("Inside GetRecoveryGenerateGrid... ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJSONArray = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "";
		try {

			responseDTO = new ResponseDTO();

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJSONArray = new JSONArray();
			json = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			// storeQry="Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,a.AMT,SCM.CREDIT_LMT_AMT,a.AMT-SCM.CREDIT_LMT_AMT from (SELECT SUM(CDT.AMOUNT) AMT,SM.STORE_ID STORE_ID from CASHDEPOSIT_TXN CDT,USER_LOGIN_CREDENTIALS ULC,USER_INFORMATION UI,STORE_MASTER SM where CDT.MAKER_DTTM=trunc(sysdate) and CDT.MAKER_ID=ULC.LOGIN_USER_ID and ULC.COMMON_ID=UI.COMMON_ID and SM.LOCATION = UI.LOCATION group by SM.STORE_ID) a,STORE_MASTER SM,STORE_CASHDPT_LIMIT_MASTER SCM where SM.STORE_ID=SCM.STORE_ID and a.STORE_ID=SCM.STORE_ID";
			merchantQry = "Select MERCHANT_ID,MERCHANT_ID||'~'||MERCHANT_NAME from MERCHANT_MASTER";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL,
						merchantRS.getString(1));
				merchantJSONArray.add(json);
				json.clear();
			}

			resultJson
					.put(CevaCommonConstants.MERCHANT_LIST, merchantJSONArray);
			merchantMap.put(CevaCommonConstants.MERCHANT_LIST, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetRecoveryGenerateGrid ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetRecoveryGenerateGrid  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
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

	public ResponseDTO insertRecoveryDetails(RequestDTO requestDTO) {

		logger.debug("Inside InsertRecoveryDetails.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;

		String insertServiceProc = "{call insertRecoveryDetailsProc(?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callableStatement = connection.prepareCall(insertServiceProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON
					.getString(CevaCommonConstants.ACCOUNT_MULTI_DATA));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Recovery Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Recovery Information already available.");
			} else {
				responseDTO.addError("Recovery Information Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InsertRecoveryDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in InsertRecoveryDetails  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			insertServiceProc = null;
			requestJSON = null;
		}
		return responseDTO;
	}

	public ResponseDTO insertRefundDetails(RequestDTO requestDTO) {

		logger.debug("Inside InsertRecoveryDetails.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;

		String insertServiceProc = "{call insertRefundDetailsProc(?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callableStatement = connection.prepareCall(insertServiceProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON
					.getString(CevaCommonConstants.ACCOUNT_MULTI_DATA));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Refund Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Refund Information already available.");
			} else {
				responseDTO.addError("Refund Information Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InsertRecoveryDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in InsertRecoveryDetails  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			insertServiceProc = null;
			requestJSON = null;
		}
		return responseDTO;
	}

	public ResponseDTO floatDashboard(RequestDTO requestDTO) {

		logger.debug("Inside FloatDashboard.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;

		String insertServiceProc = "{call DASHBOARDPKG.getFloatData(?,?,?,?)}";
		String message = "";
		ResultSet rs = null;

		JSONArray userJSONArray = null;
		JSONObject resultJson = null;

		JSONObject json = null;

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callableStatement = connection.prepareCall(insertServiceProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON.getString("loc_name"));
			callableStatement.registerOutParameter(3, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(4, OracleTypes.VARCHAR);

			callableStatement.executeUpdate();
			message = callableStatement.getString(4);

			logger.debug("Message from DB [" + message + "]");

			if (!message.equalsIgnoreCase("NO")) {
				userJSONArray = new JSONArray();
				resultJson = new JSONObject();

				rs = (ResultSet) callableStatement.getObject(3);

				json = new JSONObject();
				if (message.equalsIgnoreCase("USER")) {
					while (rs.next()) {

						json.put("mer_id", rs.getString(1));
						json.put("str_id", rs.getString(2));
						json.put("term_id", rs.getString(3));
						json.put("term_lmt", rs.getString(4));
						json.put("term_curr_lmt", rs.getString(5));
						json.put("channel", rs.getString(6));
						json.put("serialno", rs.getString(7));
						userJSONArray.add(json);
						json.clear();
					}
				} else {
					while (rs.next()) {
						json.put("mer_id", rs.getString(1));
						json.put("str_id", rs.getString(2));
						json.put("str_dpt_lmt", rs.getString(3));
						json.put("str_curr_amt", rs.getString(4));
						json.put("curr_csh_dtp_lmt", rs.getString(5));
						json.put("rec_amt", rs.getString(6));
						json.put("channel", rs.getString(7));
						json.put("tot_cdp_amt", rs.getString(8));
						json.put("tot_wdl_amt", rs.getString(9));
						json.put("cash_allow", rs.getString(9));
						json.put("unall_amt", rs.getString(10));
						userJSONArray.add(json);
						json.clear();
					}
				}
				resultJson.put(message, userJSONArray);

			} else {
				responseDTO.addError("Float Data Not Found.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in FloatDashboard [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in FloatDashboard  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			insertServiceProc = null;
			requestJSON = null;
		}
		return responseDTO;
	}

}
