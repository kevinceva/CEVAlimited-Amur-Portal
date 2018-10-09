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

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class FloatManagementDAO {

	Logger logger = Logger.getLogger(FloatManagementDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getLimitMgmtScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetLimitMgmtScreen.. ");

		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;
		JSONArray storeJSONArray = null;
		JSONArray terminalJSONArray = null;

		ArrayList<String> SID = null;
		ArrayList<String> MID = null;
		ArrayList<String> TMIDS = null;

		Connection connection = null;

		PreparedStatement merchantPstmt = null;
		PreparedStatement merchantPstmt1 = null;

		ResultSet merchantRS = null;
		ResultSet merchantRS1 = null;

		JSONObject json = null;

		String merchantQry = "select distinct MERCHANT_ID from MERCHANT_MASTER";
		try {

			json = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			terminalJSON = new JSONObject();
			storeJSONArray = new JSONArray();
			terminalJSONArray = new JSONArray();

			SID = new ArrayList<String>();
			MID = new ArrayList<String>();
			TMIDS = new ArrayList<String>();

			logger.debug("Connection is  [" + connection + "]");

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			while (merchantRS.next()) {
				MID.add(merchantRS.getString(1));
			}

			String storeQry = "Select STORE_ID,STORE_NAME,MERCHANT_ID,"
					+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
					+ "from STORE_MASTER order by MERCHANT_ID,STORE_ID";

			merchantPstmt = connection.prepareStatement(storeQry);

			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {

				json.put(CevaCommonConstants.STORE_ID, merchantRS.getString(1));
				json.put(CevaCommonConstants.STORE_NAME,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.MERCHANT_ID,
						merchantRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(4));
				storeJSONArray.add(json);
				json.clear();

			}
			logger.debug("StoreJSON Array [" + storeJSONArray + "]");

			
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			String MidQry = "select distinct MERCHANT_ID from TERMINAL_MASTER";

			merchantPstmt = connection.prepareStatement(MidQry);

			merchantRS = merchantPstmt.executeQuery();
			while (merchantRS.next()) {
				TMIDS.add(merchantRS.getString(1));
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			String sidQry = "";

			for (int i = 0; i < TMIDS.size(); i++) {
				sidQry = "select distinct STORE_ID  from TERMINAL_MASTER "
						+ "where MERCHANT_ID=?";
				merchantPstmt = connection.prepareStatement(sidQry);
				merchantPstmt.setString(1, TMIDS.get(i));

				merchantRS = merchantPstmt.executeQuery();

				while (merchantRS.next()) {

					SID.add(merchantRS.getString(1));
					String terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,STATUS,"
							+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
							+ " from TERMINAL_MASTER  where STORE_ID=? and MERCHANT_ID=?";

					merchantPstmt1 = connection.prepareStatement(terminalQry);

					merchantPstmt1.setString(1, merchantRS.getString(1));
					merchantPstmt1.setString(2, TMIDS.get(i));

					merchantRS1 = merchantPstmt1.executeQuery();

					while (merchantRS1.next()) {
						json.put(CevaCommonConstants.TERMINAL_ID,
								merchantRS1.getString(1));
						json.put(CevaCommonConstants.STORE_ID,
								merchantRS1.getString(2));
						json.put(CevaCommonConstants.MERCHANT_ID,
								merchantRS1.getString(3));
						json.put(CevaCommonConstants.STATUS,
								merchantRS1.getString(4));
						json.put(CevaCommonConstants.MAKER_DATE,
								merchantRS1.getString(5));
						terminalJSONArray.add(json);
						json.clear();
					}
					terminalJSON.put(
							TMIDS.get(i) + "_" + merchantRS.getString(1)
									+ "_TERMINALS", terminalJSONArray);

					terminalJSONArray.clear();
					
					DBUtils.closeResultSet(merchantRS1);
					DBUtils.closePreparedStatement(merchantPstmt1);

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

			sidQry = null;
			storeQry = null;
		} catch (SQLException e) {
			logger.debug("The SQLException in GetLimitMgmtScreen ::: ["
					+ e.getMessage() + "]");
			responseDTO.addError("Exception while executing data.");
		} catch (Exception e) {
			logger.debug("The Exception in GetLimitMgmtScreen ::: ["
					+ e.getMessage() + "]");
			responseDTO.addError("Exception while executing data.");
		} finally {

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeResultSet(merchantRS1);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closePreparedStatement(merchantPstmt1);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			terminalJSON = null;
			storeJSONArray = null;
			terminalJSONArray = null;

			SID = null;
			MID = null;
			TMIDS = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreLimitCreateScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreLimitCreateScreenDAO.. ");

		HashMap<String, Object> storeMap = null;

		Connection connection = null;
		PreparedStatement storePstmt = null;
		CallableStatement callableStatement = null;

		ResultSet storeRS = null;

		String cashDptCntQry = "";
		String validateStoreLimitDetailsProc = "{call validateStoreLimitDetailsProc(?,?,?)}";
		try {
			storeMap = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(validateStoreLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);
			logger.debug("ResCnt count is [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO = getLimitMgmtScreen(requestDTO);
				responseDTO.addError("Store already blocked.");
			} else if (resCnt == 2) {
				responseDTO = getLimitMgmtScreen(requestDTO);
				responseDTO.addError("Limit already assigned to this store.");
			} else {
				responseJSON = new JSONObject();
				cashDptCntQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN "
						+ "from STORE_MASTER  SM,MERCHANT_MASTER MM"
						+ " where MM.MERCHANT_ID=SM.MERCHANT_ID and trim(SM.MERCHANT_ID)=trim(?) and trim(SM.STORE_ID)=trim(?)";

				storePstmt = connection.prepareStatement(cashDptCntQry);
				storePstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
				storePstmt.setString(2,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				storeRS = storePstmt.executeQuery();

				if (storeRS.next()) {
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

				DBUtils.closeResultSet(storeRS);
				DBUtils.closePreparedStatement(storePstmt);

				cashDptCntQry = "select count(*) from STORE_CASHDPT_LIMIT_MASTER where STORE_ID=trim(?)";
				storePstmt = connection.prepareStatement(cashDptCntQry);
				storePstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				storeRS = storePstmt.executeQuery();

				int resultCnt = 0;
				if (storeRS.next()) {
					resultCnt = storeRS.getInt(1);
				}

				if (resultCnt > 0) {
					cashDptCntQry = "select CREDIT_LMT_AMT from STORE_CASHDPT_LIMIT_MASTER where STORE_ID=trim(?)";
					storePstmt = connection.prepareStatement(cashDptCntQry);
					storePstmt
							.setString(1, requestJSON
									.getString(CevaCommonConstants.STORE_ID));
					storeRS = storePstmt.executeQuery();
					if (storeRS.next()) {
						resultCnt = storeRS.getInt(1);
					}
					responseJSON.put("storeCashDepositLimit", resultCnt);
				} else {
					responseJSON.put("storeCashDepositLimit", resultCnt);
				}
				storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
				logger.debug("StoreMap [" + storeMap + "]");
				responseDTO.setData(storeMap);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("The SQLException is  GetStoreLimitCreateScreenDAO ["
					+ storeMap + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("The exception is  GetStoreLimitCreateScreenDAO ["
					+ storeMap + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			validateStoreLimitDetailsProc = null;
			storeMap = null;
			cashDptCntQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreCreditCreateScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCreditCreateScreen... ");
		HashMap<String, Object> storeMap = null;

		Connection connection = null;

		CallableStatement callableStatement = null;

		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;

		String storeQry = "";
		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;

			storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN "
					+ "from STORE_MASTER  SM,MERCHANT_MASTER MM"
					+ " where MM.MERCHANT_ID=SM.MERCHANT_ID and trim(SM.MERCHANT_ID)=trim(?) and trim(SM.STORE_ID)=trim(?)";

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storePstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storeRS = storePstmt.executeQuery();

			if (storeRS.next()) {
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

			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			
			storeQry = "select count(*) from STORE_CASHDPT_LIMIT_MASTER where STORE_ID=trim(?)";
			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storeRS = storePstmt.executeQuery();
			int resultCnt = 0;

			if (storeRS.next()) {
				resultCnt = storeRS.getInt(1);
			}

			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);

			if (resultCnt > 0) {
				storeQry = "select CREDIT_LMT_AMT from STORE_CASHDPT_LIMIT_MASTER where STORE_ID=trim(?)";
				storePstmt = connection.prepareStatement(storeQry);
				storePstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				storeRS = storePstmt.executeQuery();

				if (storeRS.next()) {
					resultCnt = storeRS.getInt(1);
				}

				responseJSON.put("storeCashDepositLimit", resultCnt);
			} else {
				responseJSON.put("storeCashDepositLimit", resultCnt);
			}

			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreCreditCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreCreditCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			storeMap = null;
			storeQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertStoreLimitDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertStoreLimitDetails.. ");

		CallableStatement callableStatement = null;
		String insertStoreLimitDetailsProc = "{call insertStoreLimitDetailsProc(?,?,?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

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
						.addMessages("Store Limit Information Stored Successfully. ");
			} else if (resCnt == -1) {

				responseDTO.addError("Limit already assigned to this store. ");
			} else {
				responseDTO
						.addError("Store Limit Inofrmation Insertion failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreCreditCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreCreditCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertStoreLimitDetailsProc = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreLimitModifyScreen(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside GetStoreLimitModifyScreen]");
		HashMap<String, Object> storeMap = null;

		PreparedStatement storePstmt = null;
		PreparedStatement terminalPstmt = null;

		ResultSet storeRS = null;
		ResultSet terminalRS = null;

		String storeQry = "";

		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN,FLM.LIMIT_AMT,FLM.STATUS,FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
					+ "from STORE_MASTER  SM, MERCHANT_MASTER MM, FLOAT_LIMIT_MASTER FLM"
					+ " where trim(MM.MERCHANT_ID)=trim(SM.MERCHANT_ID) and trim(MM.MERCHANT_ID)=trim(FLM.MERCHANT_ID) and trim(SM.MERCHANT_ID)=trim(FLM.MERCHANT_ID)  and trim(FLM.MERCHANT_ID)=trim(?) and trim(FLM.STORE_ID)=trim(?) and  FLM.TERMINAL_ID is null";

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storePstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeRS = storePstmt.executeQuery();
			if (storeRS.next()) {
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

			String terminalInfo = "SELECT TERMINAL_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
					+ "CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
					+ "from FLOAT_LIMIT_MASTER "
					+ "where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and TERMINAL_ID is not null";
			String terminalData = "";

			terminalPstmt = connection.prepareStatement(terminalInfo);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			terminalRS = terminalPstmt.executeQuery();

			while (terminalRS.next()) {
				terminalData = terminalData + "#" + terminalRS.getString(1)
						+ "," + terminalRS.getString(2) + ","
						+ terminalRS.getString(3) + ","
						+ terminalRS.getString(4) + ","
						+ terminalRS.getString(5) + ","
						+ terminalRS.getString(6) + ","
						+ terminalRS.getString(7);
			}

			responseJSON.put(CevaCommonConstants.TERMINAL_INFO, terminalData);
			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			terminalData = null;
			terminalInfo = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreLimitModifyScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreLimitModifyScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
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

	public ResponseDTO updateStoreLimitDetails(RequestDTO requestDTO) {

		logger.debug("Inside UpdateStoreLimitDetails.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertStoreLimitDetailsProc = "{call updateStoreLimitDetailsProc(?,?,?,?,?)}";

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
						.addMessages("Store Limit Information Updated Successfully.");
			} else if (resCnt == -1) {

				responseDTO.addError("Store Limit Information Already Exists.");
			} else {
				responseDTO
						.addError("Store Limit Inofrmation Updation failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreLimitModifyScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreLimitModifyScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertStoreLimitDetailsProc = null;
		}
		return responseDTO;
	}

	public ResponseDTO getTerminalLimitCreateScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetTerminalLimitCreateScreen.. ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;
		PreparedStatement terminalPstmt = null;
		PreparedStatement storeLmtPstmt = null;
		PreparedStatement allTermPstmt = null;
		CallableStatement callableStatement = null;

		ResultSet terminalRS = null;
		ResultSet storeLmtRS = null;
		ResultSet aalTermRS = null;

		try {
			storeMap = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is null [" + connection + "]");

			String validateTerminalLimitDetailsProc = "{call validateTerminalLimitDetProc(?,?,?,?)}";
			callableStatement = connection
					.prepareCall(validateTerminalLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.registerOutParameter(4, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(4);
			logger.debug("ResCnt [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO = getLimitMgmtScreen(requestDTO);
				responseDTO.addError("Terminal already blocked.");

			} else if (resCnt == 2) {
				responseDTO = getLimitMgmtScreen(requestDTO);
				responseDTO.addError("Limit not assigned to Store.");
			} else if (resCnt == 3) {
				responseDTO = getLimitMgmtScreen(requestDTO);
				responseDTO
						.addError("Limit already assigned to this Terminal.");
			} else {
				responseJSON = new JSONObject();

				String terminalQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_MAKE,MODEL_NO,SERIAL_NO,STATUS,"
						+ "to_char(TERMINAL_DATE,'DD-MM-YYYY HH24:MI:SS') "
						+ "from TERMINAL_MASTER "
						+ " where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?)";
				terminalPstmt = connection.prepareStatement(terminalQry);
				terminalPstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
				terminalPstmt.setString(2,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				terminalPstmt.setString(3,
						requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

				terminalRS = terminalPstmt.executeQuery();
				if (terminalRS.next()) {
					responseJSON.put(CevaCommonConstants.MERCHANT_ID,
							terminalRS.getString(1));
					responseJSON.put(CevaCommonConstants.STORE_ID,
							terminalRS.getString(2));
					responseJSON.put(CevaCommonConstants.TERMINAL_ID,
							terminalRS.getString(3));
					responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
							terminalRS.getString(4));
					responseJSON.put(CevaCommonConstants.MODEL_NO,
							terminalRS.getString(5));
					responseJSON.put(CevaCommonConstants.SERIAL_NO,
							terminalRS.getString(6));
					responseJSON.put(CevaCommonConstants.STATUS,
							terminalRS.getString(7));
					responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
							terminalRS.getString(8));
				}

				String storeLimitQry = "select LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
						+ "CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
						+ "from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) "
						+ " and TERMINAL_ID is null";
				storeLmtPstmt = connection.prepareStatement(storeLimitQry);
				storeLmtPstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
				storeLmtPstmt.setString(2,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				storeLmtRS = storeLmtPstmt.executeQuery();

				if (storeLmtRS.next()) {
					responseJSON.put(CevaCommonConstants.STORE_LIMIT,
							storeLmtRS.getString(1));
					responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
							storeLmtRS.getString(2));
					responseJSON.put(
							CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
							storeLmtRS.getString(3));
					responseJSON.put(
							CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
							storeLmtRS.getString(4));
					responseJSON.put(
							CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
							storeLmtRS.getString(5));
					responseJSON.put(
							CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
							storeLmtRS.getString(6));
				}

				String terminalInfo = "SELECT TERMINAL_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
						+ "CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
						+ "from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and "
						+ "TERMINAL_ID is not null";
				String terminalData = "";
				allTermPstmt = connection.prepareStatement(terminalInfo);
				allTermPstmt.setString(1,
						requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
				allTermPstmt.setString(2,
						requestJSON.getString(CevaCommonConstants.STORE_ID));
				aalTermRS = allTermPstmt.executeQuery();

				while (aalTermRS.next()) {
					terminalData = terminalData + "#" + aalTermRS.getString(1)
							+ "," + aalTermRS.getString(2) + ","
							+ aalTermRS.getString(3) + ","
							+ aalTermRS.getString(4) + ","
							+ aalTermRS.getString(5) + ","
							+ aalTermRS.getString(6) + ","
							+ aalTermRS.getString(7);
				}

				responseJSON.put(CevaCommonConstants.TERMINAL_INFO,
						terminalData);

				logger.debug("Response JSON [" + responseJSON + "]");
				storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
				logger.debug("StoreMap [" + storeMap + "]");
				responseDTO.setData(storeMap);

				terminalInfo = null;
				terminalData = null;
				terminalQry = null;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetTerminalLimitCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetTerminalLimitCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(aalTermRS);
			DBUtils.closeResultSet(storeLmtRS);
			DBUtils.closeResultSet(terminalRS);

			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(storeLmtPstmt);
			DBUtils.closePreparedStatement(allTermPstmt);
			DBUtils.closeCallableStatement(callableStatement);

			DBUtils.closeConnection(connection);
			storeMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertTerminalLimitDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertTerminalLimitDetails... ");

		CallableStatement callableStatement = null;
		String insertTerminalLimitDetailsProc = "{call insertTerminalLimitDetailsProc(?,?,?,?,?,?)}";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertTerminalLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.TERMINAL_LIMIT));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Terminal Limit Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Limit not assigned to Store ");
			} else if (resCnt == -2) {
				responseDTO
						.addError("Limit already assigned to this Terminal ");
			} else if (resCnt == -3) {
				responseDTO.addError("Insufficient funds at Store");
			} else {
				responseDTO
						.addError("Terminal Limit Information Insertion failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in InsertTerminalLimitDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  InsertTerminalLimitDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertTerminalLimitDetailsProc = null;
		}
		return responseDTO;
	}

	public ResponseDTO modifyTerminalLimitDetails(RequestDTO requestDTO) {

		logger.debug("Inside ModifyTerminalLimitDetails.. ");
		HashMap<String, Object> storeMap = null;
		PreparedStatement terminalPstmt = null;
		PreparedStatement storeLmtPstmt = null;
		PreparedStatement allTermPstmt = null;

		ResultSet terminalRS = null;
		ResultSet storeLmtRS = null;
		ResultSet aalTermRS = null;

		Connection connection = null;

		String terminalQry = "Select TM.MERCHANT_ID,TM.STORE_ID,TM.TERMINAL_ID,TM.TERMINAL_MAKE,TM.MODEL_NO,TM.SERIAL_NO,TM.STATUS,TM.TERMINAL_DATE,FLM.LIMIT_AMT,"
				+ "FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.STATUS "
				+ "from TERMINAL_MASTER TM, FLOAT_LIMIT_MASTER FLM"
				+ " where TM.TERMINAL_ID=FLM.TERMINAL_ID and TM.MERCHANT_ID=FLM.MERCHANT_ID and TM.STORE_ID=FLM.STORE_ID and"
				+ " trim(FLM.MERCHANT_ID)=trim(?) and trim(FLM.STORE_ID)=trim(?) and trim(FLM.TERMINAL_ID)=trim(?)";

		try {

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			storeMap = new HashMap<String, Object>();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();
			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT,
						terminalRS.getString(9));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
						terminalRS.getString(10));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
						terminalRS.getString(11));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_APPROVED_BY,
						terminalRS.getString(12));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_APPROVED_DATE,
						terminalRS.getString(13));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
						terminalRS.getString(14));
			}

			String storeLimitQry = "select LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,"
					+ "to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and "
					+ "trim(STORE_ID)=trim(?)";
			storeLmtPstmt = connection.prepareStatement(storeLimitQry);
			storeLmtPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeLmtPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeLmtRS = storeLmtPstmt.executeQuery();

			if (storeLmtRS.next()) {

				responseJSON.put(CevaCommonConstants.STORE_LIMIT,
						storeLmtRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeLmtRS.getString(2));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeLmtRS.getString(3));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeLmtRS.getString(4));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
						storeLmtRS.getString(5));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
						storeLmtRS.getString(6));
			}

			String terminalInfo = "SELECT TERMINAL_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
					+ "CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FLOAT_LIMIT_MASTER "
					+ "where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and TERMINAL_ID is not null";

			String terminalData = "";
			allTermPstmt = connection.prepareStatement(terminalInfo);
			allTermPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			allTermPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			aalTermRS = allTermPstmt.executeQuery();

			while (aalTermRS.next()) {
				terminalData = terminalData + "#" + aalTermRS.getString(1)
						+ "," + aalTermRS.getString(2) + ","
						+ aalTermRS.getString(3) + "," + aalTermRS.getString(4)
						+ "," + aalTermRS.getString(5) + ","
						+ aalTermRS.getString(6) + "," + aalTermRS.getString(7);
			}

			responseJSON.put(CevaCommonConstants.ALL_TERMINAL_INFO,
					terminalData);

			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in ModifyTerminalLimitDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  ModifyTerminalLimitDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closeResultSet(storeLmtRS);
			DBUtils.closeResultSet(aalTermRS);
			
			DBUtils.closePreparedStatement(allTermPstmt);
			DBUtils.closePreparedStatement(storeLmtPstmt);
			DBUtils.closePreparedStatement(allTermPstmt);

			DBUtils.closeConnection(connection);
			storeMap = null;

		}
		return responseDTO;
	}

	public ResponseDTO updateTerminalLimitDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside UpdateTerminalLimitDetails... ");

		CallableStatement callableStatement = null;
		String insertTerminalLimitDetailsProc = "{call updateTerminalLimitDetailsProc(?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertTerminalLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.TERMINAL_LIMIT));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);

			logger.debug(" resultCnt from DB:::" + resCnt);
			if (resCnt == 1) {
				responseDTO
						.addMessages("Terminal Limit Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Limit not assigned to Store ");
			} else if (resCnt == -2) {
				responseDTO
						.addError("Limit Already assigned to this Terminal ");
			} else if (resCnt == -3) {
				responseDTO.addError("Insufficient funds at Store");
			} else {
				responseDTO
						.addError("Terminal Limit Inofrmation Insertion failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in UpdateTerminalLimitDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  UpdateTerminalLimitDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getCreditMgmtScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetCreditMgmtScreen... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;
		JSONArray storeJSONArray = null;
		JSONArray terminalJSONArray = null;
		ArrayList<String> SID = null;
		ArrayList<String> MID = null;
		ArrayList<String> TMIDS = null;

		PreparedStatement merchantPstmt = null;
		PreparedStatement storePstmt = null;
		PreparedStatement sidPstmt = null;
		PreparedStatement terminalPstmt = null;

		ResultSet merchantRS = null;
		ResultSet storeRS = null;
		ResultSet mRS = null;
		ResultSet sidRS = null;
		ResultSet terminalRS = null;
		Connection connection = null;

		String merchantQry = "select distinct MERCHANT_ID from MERCHANT_MASTER";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

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

			String storeQry = "Select STORE_ID,STORE_NAME,MERCHANT_ID,MAKER_DTTM from STORE_MASTER order by MERCHANT_ID,STORE_ID";

			storePstmt = connection.prepareStatement(storeQry);

			storeRS = storePstmt.executeQuery();
			JSONObject json = null;
			while (storeRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.STORE_ID, storeRS.getString(1));
				json.put(CevaCommonConstants.STORE_NAME, storeRS.getString(2));
				json.put(CevaCommonConstants.MERCHANT_ID, storeRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE, storeRS.getString(4));
				storeJSONArray.add(json);
				json.clear();
				json = null;

			}
			logger.debug("StoreJSONArray [" + storeJSONArray + "]");

			String MidQry = "select distinct MERCHANT_ID from TERMINAL_MASTER";
			merchantPstmt.close();
			merchantPstmt = connection.prepareStatement(MidQry);

			mRS = merchantPstmt.executeQuery();
			while (mRS.next()) {
				TMIDS.add(mRS.getString(1));
			}

			for (int i = 0; i < TMIDS.size(); i++) {
				String sidQry = "select distinct STORE_ID from TERMINAL_MASTER where MERCHANT_ID=?";
				sidPstmt = connection.prepareStatement(sidQry);
				sidPstmt.setString(1, TMIDS.get(i));

				sidRS = sidPstmt.executeQuery();
				String sid = "";
				while (sidRS.next()) {
					terminalJSONArray = new JSONArray();
					SID.add(sidRS.getString(1));
					String terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,STATUS,MAKER_DTTM"
							+ " from TERMINAL_MASTER  where STORE_ID=? and MERCHANT_ID=?";

					terminalPstmt = connection.prepareStatement(terminalQry);
					sid = sidRS.getString(1);
					terminalPstmt.setString(1, sidRS.getString(1));
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
						json.clear();
						json = null;
					}
					terminalJSON.put(TMIDS.get(i) + "_" + sid + "_TERMINALS",
							terminalJSONArray);
					terminalJSONArray.clear();
					terminalJSONArray = null;
				}
			}

			resultJson.put(CevaCommonConstants.STORE_LIST, storeJSONArray);
			storeMap.put(CevaCommonConstants.STORE_LIST, resultJson);
			storeMap.put(CevaCommonConstants.TERMINAL_DATA, terminalJSON);
			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetCreditMgmtScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetCreditMgmtScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeResultSet(storeRS);
			DBUtils.closeResultSet(mRS);
			DBUtils.closeResultSet(sidRS);
			DBUtils.closeResultSet(terminalRS);
			
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closePreparedStatement(sidPstmt);
			
			
			DBUtils.closeConnection(connection);
			storeMap = null;
			resultJson = null;
			terminalJSON = null;
			storeJSONArray = null;
			terminalJSONArray = null;
			SID = null;
			MID = null;
			TMIDS = null;

		}

		return responseDTO;
	}

	public ResponseDTO insertStoreCreditDetails(RequestDTO requestDTO) {

		HashMap<String, Object> storeMap = null;
		Connection connection = null;
		logger.debug("Inside InsertStoreCreditDetails.. ");

		CallableStatement callableStatement = null;
		String insertStoreLimitDetailsProc = "{call insertStoreCreditDetailsProc(?,?,?,?,?,?)}";
		String refNo = "";
		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			responseJSON = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]s");

			callableStatement = connection
					.prepareCall(insertStoreLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement
					.setString(4, requestJSON
							.getString(CevaCommonConstants.STORE_CREDIT_AMT));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);
			refNo = callableStatement.getString(6);
			logger.debug("ResultCnt from DB resCnt [" + resCnt + "] RefNo ["
					+ refNo);

			if (resCnt == 1) {
				responseJSON.put(CevaCommonConstants.REF_NO, refNo);
				storeMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
				responseDTO.setData(storeMap);
				responseDTO
						.addMessages("Store Credit Information Stored Successfully. ");

			} else if (resCnt == -1) {
				storeMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
				responseDTO.setData(storeMap);
				responseDTO.addError("Limit not set to this Store ");
			} else if (resCnt == -2) {

				storeMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
				responseDTO.setData(storeMap);
				responseDTO.addError("Credit Amount Exceeds Limit ");
			} else {
				responseDTO
						.addError("Store Credit Information Insertion failed.");
			}
			responseJSON = requestJSON;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in InsertStoreCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  InsertStoreCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertStoreLimitDetailsProc = null;
			refNo = null;
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO getStoreCreditDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCreditDetails... ");
		HashMap<String, Object> storeMap = null;

		PreparedStatement storePstmt = null;
		PreparedStatement storeCreditPstmt = null;
		PreparedStatement terminalCreditPstmt = null;

		ResultSet storeRS = null;
		ResultSet storeCreditRS = null;
		ResultSet terminalCreditRS = null;

		Connection connection = null;

		String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN "
				+ "from STORE_MASTER  SM, MERCHANT_MASTER MM"
				+ " where trim(MM.MERCHANT_ID)=trim(SM.MERCHANT_ID) and  trim(SM.MERCHANT_ID)=trim(?) and trim(SM.STORE_ID)=trim(?)";

		try {

			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is null [" + connection + "]");

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storePstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeRS = storePstmt.executeQuery();
			if (storeRS.next()) {
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

			String storeCreditInfo = "SELECT REF_NO,STORE_ID,CREDIT_AMOUNT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from STORE_CREDIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) order by REF_NO,MAKER_DTTM ";
			String storeCreditData = "";
			storeCreditPstmt = connection.prepareStatement(storeCreditInfo);
			storeCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeCreditRS = storeCreditPstmt.executeQuery();

			while (storeCreditRS.next()) {
				storeCreditData = storeCreditData + "#"
						+ storeCreditRS.getString(1) + ","
						+ storeCreditRS.getString(2) + ","
						+ storeCreditRS.getString(3) + ","
						+ storeCreditRS.getString(4) + ","
						+ storeCreditRS.getString(5) + ","
						+ storeCreditRS.getString(6) + ","
						+ storeCreditRS.getString(7) + ","
						+ storeCreditRS.getString(8);
			}

			responseJSON.put(CevaCommonConstants.STORE_CREDIT_INFO,
					storeCreditData);

			String terminalCreditInfo = "SELECT REF_NO,TERMINAL_ID,CREDIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from TERMINAL_CREDIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) order by REF_NO,MAKER_DTTM";
			String terminalCreditData = "";
			terminalCreditPstmt = connection
					.prepareStatement(terminalCreditInfo);
			terminalCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			terminalCreditRS = terminalCreditPstmt.executeQuery();

			while (terminalCreditRS.next()) {
				terminalCreditData = terminalCreditData + "#"
						+ terminalCreditRS.getString(1) + ","
						+ terminalCreditRS.getString(2) + ","
						+ terminalCreditRS.getString(3) + ","
						+ terminalCreditRS.getString(4) + ","
						+ terminalCreditRS.getString(5) + ","
						+ terminalCreditRS.getString(6) + ","
						+ terminalCreditRS.getString(7) + ","
						+ terminalCreditRS.getString(8);
			}
			responseJSON.put(CevaCommonConstants.STORE_TERMINAL_CREDIT_INFO,
					terminalCreditData);

			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(storeRS);
			DBUtils.closeResultSet(storeCreditRS);
			DBUtils.closeResultSet(terminalCreditRS);
			
			DBUtils.closePreparedStatement(terminalCreditPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closePreparedStatement(storeCreditPstmt);

			DBUtils.closeConnection(connection);
			storeMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO getTerminalCreditCreateScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetTerminalCreditCreateScreen");
		HashMap<String, Object> storeMap = null;
		JSONArray referenceNoJSONArray = null;

		PreparedStatement terminalPstmt = null;
		PreparedStatement referenceNoPstmt = null;

		ResultSet terminalRS = null;
		ResultSet referenceNoRS = null;

		Connection connection = null;

		String terminalQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_MAKE,MODEL_NO,SERIAL_NO,STATUS,TERMINAL_DATE "
				+ "from TERMINAL_MASTER "
				+ " where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?)";

		try {
			storeMap = new HashMap<String, Object>();
			referenceNoJSONArray = new JSONArray();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("inside Request JSON [" + requestJSON + "]");

			connection = DBConnector.getConnection();
			logger.debug("inside Connection is null [" + connection + "]");
			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();

			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
			}

			String referenceNoQry = "select refno from( select sum(to_number(credit_amt)) amt,REF_NO refno "
					+ "from terminal_credit_master where trim(STORE_ID)=trim(?) and trim(MERCHANT_ID)=trim(?) group by ref_no ) "
					+ "where to_number(amt) < (select to_number(sc.credit_amount)  from store_credit_master sc where refno = sc.ref_no)  "
					+ "union all  select ref_no from store_credit_master where ref_no not in (select ref_no from terminal_credit_master) "
					+ "and trim(STORE_ID)=trim(?) and trim(MERCHANT_ID)=trim(?) ";

			referenceNoPstmt = connection.prepareStatement(referenceNoQry);
			referenceNoPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			referenceNoPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			referenceNoPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			referenceNoPstmt.setString(4,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			referenceNoRS = referenceNoPstmt.executeQuery();
			JSONObject json = null;
			while (referenceNoRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY,
						referenceNoRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL,
						referenceNoRS.getString(1));
				referenceNoJSONArray.add(json);
				json.clear();
				json = null;

			}
			logger.debug("ReferenceNoJSONArray [" + referenceNoJSONArray + "]");

			responseJSON.put(CevaCommonConstants.REFERENCE_LIST,
					referenceNoJSONArray);
			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("  Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("  Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetTerminalCreditCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetTerminalCreditCreateScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(referenceNoRS);
			DBUtils.closeResultSet(terminalRS);
			
			DBUtils.closePreparedStatement(referenceNoPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			
			DBUtils.closeConnection(connection);
			storeMap = null;
			referenceNoJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertTerminalCreateCreditDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertTerminalCreateCreditDetails.. ");

		CallableStatement callableStatement = null;
		String insertTerminalLimitDetailsProc = "{call insertTerminalCreditDetProc(?,?,?,?,?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = DBConnector.getConnection();
			logger.debug(" connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertTerminalLimitDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.setString(5, requestJSON
					.getString(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.REFERENCE_NO));
			callableStatement.registerOutParameter(7, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(7);

			logger.debug("ResultCnt from DB [" + resCnt);
			if (resCnt == 1) {
				responseDTO
						.addMessages("Terminal Credit Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Limit not assigned to Store. ");
			} else if (resCnt == -2) {
				responseDTO.addError("Credit Amount Exceeds Terminal Limit. ");
			} else if (resCnt == -3) {
				responseDTO
						.addError("No available amount for the entered 'Reference No'.");
			} else {
				responseDTO
						.addError("Terminal Credit Amount Inofrmation Insertion failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in InsertTerminalCreateCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  InsertTerminalCreateCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertTerminalLimitDetailsProc = null;
		}
		return responseDTO;
	}

	public ResponseDTO viewTerminalCreditDetails(RequestDTO requestDTO) {

		logger.debug("Inside ViewTerminalCreditDetails... ");
		HashMap<String, Object> storeMap = null;

		Connection connection = null;
		PreparedStatement terminalPstmt = null;
		PreparedStatement terminalCreditPstmt = null;

		ResultSet terminalRS = null;
		ResultSet terminalCreditRS = null;

		String terminalQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_MAKE,MODEL_NO,SERIAL_NO,STATUS,TERMINAL_DATE "
				+ "from TERMINAL_MASTER "
				+ " where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?)";

		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is null [" + connection + "]");
			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();
			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
			}

			String terminalCreditInfo = "SELECT REF_NO,TERMINAL_ID,CREDIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from TERMINAL_CREDIT_MASTER "
					+ "where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?) order by REF_NO,MAKER_DTTM";
			String terminalCreditData = "";
			terminalCreditPstmt = connection
					.prepareStatement(terminalCreditInfo);
			terminalCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalCreditPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalCreditRS = terminalCreditPstmt.executeQuery();

			while (terminalCreditRS.next()) {
				terminalCreditData = terminalCreditData + "#"
						+ terminalCreditRS.getString(1) + ","
						+ terminalCreditRS.getString(2) + ","
						+ terminalCreditRS.getString(3) + ","
						+ terminalCreditRS.getString(4) + ","
						+ terminalCreditRS.getString(5) + ","
						+ terminalCreditRS.getString(6) + ","
						+ terminalCreditRS.getString(7) + ","
						+ terminalCreditRS.getString(8);
			}

			responseJSON.put(CevaCommonConstants.TERMINAL_CREDIT_INFO,
					terminalCreditData);

			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in ViewTerminalCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  ViewTerminalCreditDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closeResultSet(terminalCreditRS);
			
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(terminalCreditPstmt);
			
			DBUtils.closeConnection(connection);
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO getLimitMgmtAuthScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetLimitMgmtAuthScreen... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;
		JSONArray storeJSONArray = null;
		JSONArray terminalJSONArray = null;
		ArrayList<String> SID = null;
		ArrayList<String> MID = null;
		ArrayList<String> TMIDS = null;

		Connection connection = null;

		PreparedStatement merchantPstmt = null;
		PreparedStatement storePstmt = null;
		PreparedStatement sidPstmt = null;
		PreparedStatement terminalPstmt = null;

		ResultSet merchantRS = null;
		ResultSet storeRS = null;
		ResultSet mRS = null;
		ResultSet sidRS = null;
		ResultSet terminalRS = null;

		String merchantQry = "select distinct MERCHANT_ID from FLOAT_LIMIT_MASTER";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

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

			String storeQry = "Select STORE_ID,MERCHANT_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FLOAT_LIMIT_MASTER where TERMINAL_ID is null  order by MERCHANT_ID,STORE_ID ";

			storePstmt = connection.prepareStatement(storeQry);

			storeRS = storePstmt.executeQuery();
			JSONObject json = null;
			while (storeRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.STORE_ID, storeRS.getString(1));
				json.put(CevaCommonConstants.MERCHANT_ID, storeRS.getString(2));
				json.put(CevaCommonConstants.STORE_LIMIT, storeRS.getString(3));
				json.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeRS.getString(4));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeRS.getString(5));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeRS.getString(6));
				storeJSONArray.add(json);
				json.clear();
				json = null;

			}
			logger.debug("StoreJSONArray [" + storeJSONArray + "]");

			String MidQry = "select distinct MERCHANT_ID from FLOAT_LIMIT_MASTER ";
			merchantPstmt.close();

			merchantPstmt = connection.prepareStatement(MidQry);

			mRS = merchantPstmt.executeQuery();
			while (mRS.next()) {
				TMIDS.add(mRS.getString(1));
			}

			for (int i = 0; i < TMIDS.size(); i++) {
				String sidQry = "select distinct STORE_ID from FLOAT_LIMIT_MASTER where MERCHANT_ID=?";
				sidPstmt = connection.prepareStatement(sidQry);
				sidPstmt.setString(1, TMIDS.get(i));

				sidRS = sidPstmt.executeQuery();
				String sid = "";
				while (sidRS.next()) {
					terminalJSONArray = new JSONArray();
					SID.add(sidRS.getString(1));
					String terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
							+ " from FLOAT_LIMIT_MASTER  where STORE_ID=? and MERCHANT_ID=? and TERMINAL_ID is not null";

					terminalPstmt = connection.prepareStatement(terminalQry);
					sid = sidRS.getString(1);
					terminalPstmt.setString(1, sidRS.getString(1));
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
						json.put(CevaCommonConstants.TERMINAL_LIMIT,
								terminalRS.getString(4));
						json.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
								terminalRS.getString(5));
						json.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
								terminalRS.getString(6));
						json.put(
								CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
								terminalRS.getString(7));
						terminalJSONArray.add(json);
						json.clear();
						json = null;

					}
					terminalJSON.put(TMIDS.get(i) + "_" + sid + "_TERMINALS",
							terminalJSONArray);

					terminalJSONArray.clear();
					terminalJSONArray = null;
				}

			}

			resultJson.put(CevaCommonConstants.STORE_LIST, storeJSONArray);
			storeMap.put(CevaCommonConstants.STORE_LIST, resultJson);
			storeMap.put(CevaCommonConstants.TERMINAL_DATA, terminalJSON);
			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetLimitMgmtAuthScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetLimitMgmtAuthScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
				
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closeResultSet(sidRS);
			DBUtils.closeResultSet(mRS);
			DBUtils.closeResultSet(storeRS);
			DBUtils.closeResultSet(merchantRS);
			
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(sidPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closePreparedStatement(merchantPstmt);
			
			DBUtils.closeConnection(connection);
			
			storeMap = null;
			resultJson = null;
			terminalJSON = null;
			storeJSONArray = null;
			terminalJSONArray = null;
			SID = null;
			MID = null;
			TMIDS = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreLimitApproveDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreLimitApproveDetails.. ");
		HashMap<String, Object> storeMap = null;

		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;

		Connection connection = null;

		String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN,FLM.LIMIT_AMT,FLM.STATUS,FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
				+ "from STORE_MASTER  SM, MERCHANT_MASTER MM, FLOAT_LIMIT_MASTER FLM"
				+ " where trim(MM.MERCHANT_ID)=trim(SM.MERCHANT_ID) and trim(MM.MERCHANT_ID)=trim(FLM.MERCHANT_ID) and trim(SM.MERCHANT_ID)=trim(FLM.MERCHANT_ID)  and trim(FLM.MERCHANT_ID)=trim(?) and trim(FLM.STORE_ID)=trim(?) and  trim(FLM.STATUS)=(?) and  FLM.TERMINAL_ID is null";

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
			if (storeRS.next()) {
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

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreLimitApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreLimitApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO StoreLimitApprove(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside StoreLimitApprove.. ");

		CallableStatement callableStatement = null;
		String insertTerminalLimitDetailsProc = "{call approveStoreLimitDetProc(?,?,?,?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

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

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Store Limit Information Approved Successfully. ");
			} else if (resCnt == -1) {
				responseDTO
						.addError("Store Limit Information Approved Already.");
			} else {
				responseDTO
						.addError("Store Limit Information Approved failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreLimitApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreLimitApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertTerminalLimitDetailsProc = null;

		}
		return responseDTO;
	}

	public ResponseDTO getterminalLimitApproveDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetterminalLimitApproveDetails... ");
		HashMap<String, Object> storeMap = null;

		PreparedStatement terminalPstmt = null;
		PreparedStatement storeLmtPstmt = null;
		PreparedStatement allTermPstmt = null;

		ResultSet terminalRS = null;
		ResultSet storeLmtRS = null;
		ResultSet aalTermRS = null;

		Connection connection = null;

		String terminalQry = "Select TM.MERCHANT_ID,TM.STORE_ID,TM.TERMINAL_ID,TM.TERMINAL_MAKE,TM.MODEL_NO,TM.SERIAL_NO,TM.STATUS,to_char(TM.TERMINAL_DATE,'DD-MM-YYYY HH24:MI:SS'),FLM.LIMIT_AMT,"
				+ "FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.STATUS "
				+ "from TERMINAL_MASTER TM, FLOAT_LIMIT_MASTER FLM"
				+ " where TM.TERMINAL_ID=FLM.TERMINAL_ID and "
				+ "TM.MERCHANT_ID=FLM.MERCHANT_ID and "
				+ "TM.STORE_ID=FLM.STORE_ID and"
				+ " trim(FLM.MERCHANT_ID)=trim(?) and "
				+ "trim(FLM.STORE_ID)=trim(?) and trim(FLM.TERMINAL_ID)=trim(?)";

		try {

			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is null [" + connection + "]");
			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();
			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT,
						terminalRS.getString(9));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
						terminalRS.getString(10));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
						terminalRS.getString(11));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_APPROVED_BY,
						terminalRS.getString(12));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_APPROVED_DATE,
						terminalRS.getString(13));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
						terminalRS.getString(14));
			}

			String storeLimitQry = "select LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
					+ "CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
					+ "from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and TERMINAL_ID is null";
			storeLmtPstmt = connection.prepareStatement(storeLimitQry);
			storeLmtPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeLmtPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeLmtRS = storeLmtPstmt.executeQuery();

			if (storeLmtRS.next()) {
				responseJSON.put(CevaCommonConstants.STORE_LIMIT,
						storeLmtRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeLmtRS.getString(2));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeLmtRS.getString(3));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeLmtRS.getString(4));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
						storeLmtRS.getString(5));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
						storeLmtRS.getString(6));
			}

			String terminalInfo = "SELECT TERMINAL_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
					+ "CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
					+ "from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) "
					+ "and TERMINAL_ID is not null";
			String terminalData = "";
			allTermPstmt = connection.prepareStatement(terminalInfo);
			allTermPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			allTermPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			aalTermRS = allTermPstmt.executeQuery();
			while (aalTermRS.next()) {
				terminalData += "#" + aalTermRS.getString(1) + ","
						+ aalTermRS.getString(2) + "," + aalTermRS.getString(3)
						+ "," + aalTermRS.getString(4) + ","
						+ aalTermRS.getString(5) + "," + aalTermRS.getString(6)
						+ "," + aalTermRS.getString(7);
			}

			responseJSON.put(CevaCommonConstants.ALL_TERMINAL_INFO,
					terminalData);

			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("StoreMap [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

			terminalData = null;
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetterminalLimitApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetterminalLimitApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(aalTermRS);
			DBUtils.closeResultSet(storeLmtRS);
			DBUtils.closeResultSet(terminalRS);

			DBUtils.closePreparedStatement(allTermPstmt);
			DBUtils.closePreparedStatement(storeLmtPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			
			DBUtils.closeConnection(connection);
			
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO TerminalLimitApprove(RequestDTO requestDTO) {

		logger.debug("Inside TerminalLimitApprove.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		String approveTerminalLimitDetProc = "{call approveTerminalLimitDetProc(?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is null [" + connection + "]");

			callableStatement = connection
					.prepareCall(approveTerminalLimitDetProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement
					.setString(
							5,
							requestJSON
									.getString(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);

			logger.debug(" resultCnt from DB:::" + resCnt);
			if (resCnt == 1) {
				responseDTO
						.addMessages("Terminal Limit Information Approved Successfully. ");
			} else if (resCnt == -1) {
				responseDTO
						.addError("Terminal Limit Information Approved Already");
			} else {
				responseDTO
						.addError("Terminal Limit Information Approved failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in TerminalLimitApprove ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  TerminalLimitApprove ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO TerminalLimitApproveView(RequestDTO requestDTO) {

		logger.debug("Inside TerminalLimitApproveView... ");
		HashMap<String, Object> storeMap = null;

		Connection connection = null;

		PreparedStatement terminalPstmt = null;
		PreparedStatement storeLmtPstmt = null;
		PreparedStatement allTermPstmt = null;

		ResultSet terminalRS = null;
		ResultSet storeLmtRS = null;
		ResultSet aalTermRS = null;

		String terminalQry = "Select TM.MERCHANT_ID,TM.STORE_ID,TM.TERMINAL_ID,TM.TERMINAL_MAKE,TM.MODEL_NO,TM.SERIAL_NO,TM.STATUS,to_char(TM.TERMINAL_DATE,'DD-MM-YYYY HH24:MI:SS'),FLM.LIMIT_AMT,"
				+ "FLM.MAKER_ID,to_char(FLM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.CHECKER_ID,to_char(FLM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FLM.STATUS "
				+ "from TERMINAL_MASTER TM, FLOAT_LIMIT_MASTER FLM"
				+ " where TM.TERMINAL_ID=FLM.TERMINAL_ID and TM.MERCHANT_ID=FLM.MERCHANT_ID and TM.STORE_ID=FLM.STORE_ID and"
				+ " trim(FLM.MERCHANT_ID)=trim(?) and trim(FLM.STORE_ID)=trim(?) and trim(FLM.TERMINAL_ID)=trim(?)";

		try {

			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is null [" + connection + "]");

			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();
			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT,
						terminalRS.getString(9));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
						terminalRS.getString(10));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
						terminalRS.getString(11));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_APPROVED_BY,
						terminalRS.getString(12));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_APPROVED_DATE,
						terminalRS.getString(13));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
						terminalRS.getString(14));
			}

			String storeLimitQry = "select LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?)";
			storeLmtPstmt = connection.prepareStatement(storeLimitQry);
			storeLmtPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeLmtPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeLmtRS = storeLmtPstmt.executeQuery();
			if (storeLmtRS.next()) {
				responseJSON.put(CevaCommonConstants.STORE_LIMIT,
						storeLmtRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeLmtRS.getString(2));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeLmtRS.getString(3));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeLmtRS.getString(4));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
						storeLmtRS.getString(5));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
						storeLmtRS.getString(6));
			}

			String terminalInfo = "SELECT TERMINAL_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FLOAT_LIMIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and TERMINAL_ID is not null";
			String terminalData = "";
			allTermPstmt = connection.prepareStatement(terminalInfo);
			allTermPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			allTermPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			aalTermRS = allTermPstmt.executeQuery();

			while (aalTermRS.next()) {
				terminalData = terminalData + "#" + aalTermRS.getString(1)
						+ "," + aalTermRS.getString(2) + ","
						+ aalTermRS.getString(3) + "," + aalTermRS.getString(4)
						+ "," + aalTermRS.getString(5) + ","
						+ aalTermRS.getString(6) + "," + aalTermRS.getString(7);
			}

			responseJSON.put(CevaCommonConstants.ALL_TERMINAL_INFO,
					terminalData);

			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in TerminalLimitApproveView ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  TerminalLimitApproveView ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(aalTermRS);
			DBUtils.closeResultSet(storeLmtRS);
			DBUtils.closeResultSet(terminalRS);
			
			DBUtils.closePreparedStatement(allTermPstmt);
			DBUtils.closePreparedStatement(storeLmtPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			
			DBUtils.closeConnection(connection);
			
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO getCreditMgmtAuthScreen(RequestDTO requestDTO) {

		logger.debug("Inside GetCreditMgmtAuthScreen... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;
		JSONArray storeJSONArray = null;
		JSONArray terminalJSONArray = null;
		ArrayList<String> SID = null;
		ArrayList<String> MID = null;
		ArrayList<String> TMIDS = null;

		PreparedStatement merchantPstmt = null;
		PreparedStatement storePstmt = null;
		PreparedStatement sidPstmt = null;
		PreparedStatement terminalPstmt = null;

		ResultSet merchantRS = null;
		ResultSet storeRS = null;
		ResultSet mRS = null;
		ResultSet sidRS = null;
		ResultSet terminalRS = null;

		Connection connection = null;

		String merchantQry = "select distinct MERCHANT_ID from STORE_CREDIT_MASTER";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			terminalJSON = new JSONObject();
			storeJSONArray = new JSONArray();
			terminalJSONArray = new JSONArray();
			SID = new ArrayList<String>();
			MID = new ArrayList<String>();
			TMIDS = new ArrayList<String>();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			while (merchantRS.next()) {
				MID.add(merchantRS.getString(1));
			}

			String storeQry = "Select STORE_ID,MERCHANT_ID,REF_NO,CREDIT_AMOUNT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from STORE_CREDIT_MASTER order by MERCHANT_ID,STORE_ID,REF_NO ";

			storePstmt = connection.prepareStatement(storeQry);
			storeRS = storePstmt.executeQuery();
			JSONObject json = null;
			while (storeRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.STORE_ID, storeRS.getString(1));
				json.put(CevaCommonConstants.MERCHANT_ID, storeRS.getString(2));
				json.put(CevaCommonConstants.REF_NO, storeRS.getString(3));
				json.put(CevaCommonConstants.STORE_CREDIT_AMT,
						storeRS.getString(4));
				json.put(CevaCommonConstants.STORE_CREDIT_STATUS,
						storeRS.getString(5));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeRS.getString(6));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeRS.getString(7));
				storeJSONArray.add(json);
				json.clear();
				json = null;

			}
			logger.debug("StoreJSONArray [" + storeJSONArray + "]");

			String MidQry = "select distinct MERCHANT_ID from STORE_CREDIT_MASTER ";
			merchantPstmt.close();
			merchantPstmt = connection.prepareStatement(MidQry);

			mRS = merchantPstmt.executeQuery();
			while (mRS.next()) {
				TMIDS.add(mRS.getString(1));
			}

			for (int i = 0; i < TMIDS.size(); i++) {
				String sidQry = "select distinct STORE_ID from TERMINAL_CREDIT_MASTER where MERCHANT_ID=?";
				sidPstmt = connection.prepareStatement(sidQry);
				sidPstmt.setString(1, TMIDS.get(i));

				sidRS = sidPstmt.executeQuery();
				String sid = "";
				while (sidRS.next()) {
					terminalJSONArray = new JSONArray();
					SID.add(sidRS.getString(1));
					String terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,REF_NO,TERMINAL_REF_NO,CREDIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
							+ " from TERMINAL_CREDIT_MASTER  where STORE_ID=? and MERCHANT_ID=? ";

					terminalPstmt = connection.prepareStatement(terminalQry);
					sid = sidRS.getString(1);
					terminalPstmt.setString(1, sidRS.getString(1));
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
						json.put(CevaCommonConstants.REF_NO,
								terminalRS.getString(4));
						json.put(CevaCommonConstants.TERMINAL_REF_NO,
								terminalRS.getString(5));
						json.put(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT,
								terminalRS.getString(6));
						json.put(CevaCommonConstants.TERMINAL_CREDIT_STATUS,
								terminalRS.getString(7));
						json.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
								terminalRS.getString(8));
						terminalJSONArray.add(json);
						json.clear();
						json = null;

					}
					terminalJSON.put(TMIDS.get(i) + "_" + sid + "_TERMINALS",
							terminalJSONArray);

					terminalJSONArray.clear();
					terminalJSONArray = null;
				}

			}

			resultJson.put(CevaCommonConstants.STORE_LIST, storeJSONArray);
			storeMap.put(CevaCommonConstants.STORE_LIST, resultJson);
			storeMap.put(CevaCommonConstants.TERMINAL_DATA, terminalJSON);
			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetCreditMgmtAuthScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetCreditMgmtAuthScreen ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closeResultSet(sidRS);
			DBUtils.closeResultSet(mRS);
			DBUtils.closeResultSet(storeRS);
			DBUtils.closeResultSet(merchantRS);
			
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(sidPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closePreparedStatement(merchantPstmt);
			
			DBUtils.closeConnection(connection);
			
			storeMap = null;
			resultJson = null;
			terminalJSON = null;
			storeJSONArray = null;
			terminalJSONArray = null;
			SID = null;
			MID = null;
			TMIDS = null;
		}
		return responseDTO;
	}

	public ResponseDTO getStoreCreditApproveDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCreditApproveDetails... ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;

		PreparedStatement storePstmt = null;
		PreparedStatement storeCreditPstmt = null;

		ResultSet storeRS = null;
		ResultSet storeCreditRS = null;

		String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN "
				+ "from STORE_MASTER  SM,MERCHANT_MASTER MM"
				+ " where MM.MERCHANT_ID=SM.MERCHANT_ID and trim(SM.MERCHANT_ID)=trim(?) and trim(SM.STORE_ID)=trim(?)";

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
			if (storeRS.next()) {
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

			String storeCreditInfo = "select CREDIT_AMOUNT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from STORE_CREDIT_MASTER "
					+ "where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(REF_NO)=trim(?)";

			storeCreditPstmt = connection.prepareStatement(storeCreditInfo);
			storeCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storeCreditPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.REF_NO));

			storeCreditRS = storeCreditPstmt.executeQuery();

			if (storeCreditRS.next()) {
				responseJSON.put(CevaCommonConstants.STORE_CREDIT_AMT,
						storeCreditRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_CREDIT_STATUS,
						storeCreditRS.getString(2));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeCreditRS.getString(3));
				responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeCreditRS.getString(4));
			}

			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreCreditApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreCreditApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(storeCreditRS);
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storeCreditPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
			storeMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO StoreCreditApprove(RequestDTO requestDTO) {

		logger.debug("Inside StoreCreditApprove... ");
		Connection connection = null;
		CallableStatement callableStatement = null;
		String approveStoreCreditDet = "{call approveStoreCreditDetProc(?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection.prepareCall(approveStoreCreditDet);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.REF_NO));
			callableStatement
					.setString(
							5,
							requestJSON
									.getString(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Store Credit Information Approved Successfully. ");
			} else if (resCnt == -1) {
				responseDTO
						.addError("Store Credit Information Approved Already");
			} else {
				responseDTO
						.addError("Store Credit Information Approved failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in StoreCreditApprove ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  StoreCreditApprove [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			approveStoreCreditDet = null;
		}
		return responseDTO;
	}

	public ResponseDTO getStoreCreditApproveViewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCreditApproveViewDetails... ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;

		PreparedStatement storePstmt = null;
		PreparedStatement storeCreditPstmt = null;
		PreparedStatement terminalCreditPstmt = null;

		ResultSet storeRS = null;
		ResultSet storeCreditRS = null;
		ResultSet terminalCreditRS = null;

		String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,SM.MERCHANT_ID,MM.MERCHANT_NAME,SM.LOCATION,SM.KRA_PIN "
				+ "from STORE_MASTER  SM, MERCHANT_MASTER MM"
				+ " where trim(MM.MERCHANT_ID)=trim(SM.MERCHANT_ID) and  trim(SM.MERCHANT_ID)=trim(?) and trim(SM.STORE_ID)=trim(?)";

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
			if (storeRS.next()) {
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

			String storeCreditInfo = "SELECT REF_NO,STORE_ID,CREDIT_AMOUNT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from STORE_CREDIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) order by REF_NO,MAKER_DTTM ";
			String storeCreditData = "";
			storeCreditPstmt = connection.prepareStatement(storeCreditInfo);
			storeCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			storeCreditRS = storeCreditPstmt.executeQuery();

			while (storeCreditRS.next()) {
				storeCreditData = storeCreditData + "#"
						+ storeCreditRS.getString(1) + ","
						+ storeCreditRS.getString(2) + ","
						+ storeCreditRS.getString(3) + ","
						+ storeCreditRS.getString(4) + ","
						+ storeCreditRS.getString(5) + ","
						+ storeCreditRS.getString(6) + ","
						+ storeCreditRS.getString(7) + ","
						+ storeCreditRS.getString(8);
			}

			responseJSON.put(CevaCommonConstants.STORE_CREDIT_INFO,
					storeCreditData);

			String terminalCreditInfo = "SELECT REF_NO,TERMINAL_ID,CREDIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from TERMINAL_CREDIT_MASTER where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) order by REF_NO,MAKER_DTTM";
			String terminalCreditData = "";
			terminalCreditPstmt = connection
					.prepareStatement(terminalCreditInfo);
			terminalCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));

			terminalCreditRS = terminalCreditPstmt.executeQuery();

			while (terminalCreditRS.next()) {
				terminalCreditData = terminalCreditData + "#"
						+ terminalCreditRS.getString(1) + ","
						+ terminalCreditRS.getString(2) + ","
						+ terminalCreditRS.getString(3) + ","
						+ terminalCreditRS.getString(4) + ","
						+ terminalCreditRS.getString(5) + ","
						+ terminalCreditRS.getString(6) + ","
						+ terminalCreditRS.getString(7) + ","
						+ terminalCreditRS.getString(8);
			}
			responseJSON.put(CevaCommonConstants.STORE_TERMINAL_CREDIT_INFO,
					terminalCreditData);

			storeMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetStoreCreditApproveViewDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetStoreCreditApproveViewDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(terminalCreditRS);
			DBUtils.closeResultSet(storeCreditRS);
			DBUtils.closeResultSet(storeRS);
			
			DBUtils.closePreparedStatement(terminalCreditPstmt);
			DBUtils.closePreparedStatement(storeCreditPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			
			DBUtils.closeConnection(connection);
			storeMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO getTerminalCreditApproveDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetTerminalCreditApproveDetails... ");
		HashMap<String, Object> storeMap = null;

		PreparedStatement terminalPstmt = null;
		PreparedStatement terminalCreditPstmt = null;

		ResultSet terminalRS = null;
		ResultSet terminalCreditRS = null;

		Connection connection = null;

		String terminalQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_MAKE,MODEL_NO,SERIAL_NO,STATUS,TERMINAL_DATE "
				+ "from TERMINAL_MASTER where trim(MERCHANT_ID)=trim(?) "
				+ "and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?)";

		try {
			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();
			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
			}

			String terminalCreditQry = "select REF_NO,CREDIT_AMT,TERMINAL_REF_NO,MAKER_ID,"
					+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from TERMINAL_CREDIT_MASTER "
					+ "where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and "
					+ "trim(TERMINAL_ID)=trim(?) and trim(REF_NO)=trim(?) and trim(TERMINAL_REF_NO)=trim(?)";

			terminalCreditPstmt = connection
					.prepareStatement(terminalCreditQry);
			terminalCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalCreditPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			terminalCreditPstmt.setString(4,
					requestJSON.getString(CevaCommonConstants.REF_NO));
			terminalCreditPstmt.setString(5,
					requestJSON.getString(CevaCommonConstants.TERMINAL_REF_NO));

			terminalCreditRS = terminalCreditPstmt.executeQuery();

			if (terminalCreditRS.next()) {
				responseJSON.put(CevaCommonConstants.REF_NO,
						terminalCreditRS.getString(1));
				responseJSON.put(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT,
						terminalCreditRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_REF_NO,
						terminalCreditRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
						terminalCreditRS.getString(4));
				responseJSON.put(
						CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
						terminalCreditRS.getString(5));
			}

			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetTerminalCreditApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetTerminalCreditApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(terminalCreditRS);
			DBUtils.closeResultSet(terminalRS);
			
			DBUtils.closePreparedStatement(terminalCreditPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			
			DBUtils.closeConnection(connection);
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO terminalCreditApprove(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside TerminalCreditApprove... ");
		CallableStatement callableStatement = null;
		String approveTerminalCreditDet = "{call approveTerminalCreditDetProc(?,?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is null [" + connection);

			callableStatement = connection
					.prepareCall(approveTerminalCreditDet);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.REF_NO));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.TERMINAL_REF_NO));
			callableStatement
					.setString(
							7,
							requestJSON
									.getString(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT));
			callableStatement.registerOutParameter(8, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(8);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Terminal Credit Information Approved Successfully.");
			} else if (resCnt == -1) {
				responseDTO
						.addError("Terminal Credit Information Approved Already.");
			} else {
				responseDTO
						.addError("Terminal Credit Information Approved failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in TerminalCreditApprove ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  TerminalCreditApprove ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			approveTerminalCreditDet = null;
		}
		return responseDTO;
	}

	public ResponseDTO viewTerminalCreditApproveDetails(RequestDTO requestDTO) {

		logger.debug("Inside ViewTerminalCreditApproveDetails... ");
		HashMap<String, Object> storeMap = null;

		PreparedStatement terminalPstmt = null;
		PreparedStatement terminalCreditPstmt = null;

		ResultSet terminalRS = null;
		ResultSet terminalCreditRS = null;

		Connection connection = null;

		String terminalQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_MAKE,MODEL_NO,SERIAL_NO,STATUS,TERMINAL_DATE "
				+ "from TERMINAL_MASTER "
				+ " where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?)";

		try {

			storeMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = DBConnector.getConnection();
			logger.debug("Connection is null [" + connection + "]");
			terminalPstmt = connection.prepareStatement(terminalQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalPstmt.executeQuery();
			if (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(8));
			}

			String terminalCreditInfo = "SELECT REF_NO,TERMINAL_ID,CREDIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),CHECKER_ID,to_char(CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from TERMINAL_CREDIT_MASTER "
					+ "where trim(MERCHANT_ID)=trim(?) and trim(STORE_ID)=trim(?) and trim(TERMINAL_ID)=trim(?) order by REF_NO,MAKER_DTTM";
			String terminalCreditData = "";
			terminalCreditPstmt = connection
					.prepareStatement(terminalCreditInfo);
			terminalCreditPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalCreditPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalCreditPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalCreditRS = terminalCreditPstmt.executeQuery();

			while (terminalCreditRS.next()) {
				terminalCreditData = terminalCreditData + "#"
						+ terminalCreditRS.getString(1) + ","
						+ terminalCreditRS.getString(2) + ","
						+ terminalCreditRS.getString(3) + ","
						+ terminalCreditRS.getString(4) + ","
						+ terminalCreditRS.getString(5) + ","
						+ terminalCreditRS.getString(6) + ","
						+ terminalCreditRS.getString(7) + ","
						+ terminalCreditRS.getString(8);
			}

			responseJSON.put(CevaCommonConstants.TERMINAL_CREDIT_INFO,
					terminalCreditData);

			storeMap.put(CevaCommonConstants.TERMINAL_INFO, responseJSON);
			logger.debug("Store Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in ViewTerminalCreditApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  ViewTerminalCreditApproveDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(terminalCreditRS);
			DBUtils.closeResultSet(terminalRS);
			
			DBUtils.closePreparedStatement(terminalCreditPstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			
			DBUtils.closeConnection(connection);
			storeMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO InboxDetailsInsert(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InboxDetailsInsert.. ");

		CallableStatement callableStatement = null;
		String inboxInsertDet = "{call InboxDetailsInsertProc(?,?,?,?,?,?,?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is null [" + connection);

			callableStatement = connection.prepareCall(inboxInsertDet);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.TO_EMAILS));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.CC_EMAILS));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.EMAIL_SUBJECT));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.EMAIL_MESSAGE));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.REFERENCE_NO));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.TICKET_ID));
			callableStatement.registerOutParameter(9, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(9);

			logger.debug(" resultCnt from DB:::" + resCnt);
			if (resCnt == 1) {
				responseDTO
						.addMessages("Inbox Information Stored Successfully. ");
			} else {
				responseDTO.addError("Inbox Information Store failed.");
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in InboxDetailsInsert ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  InboxDetailsInsert [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getcreditDashBoard(RequestDTO requestDTO) {

		logger.debug("Inside GetcreditDashBoard.. ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJSONArray = null;

		Connection connection = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String merchantQry = "SELECT    SM.MERCHANT_ID,SM.STORE_ID,TM.TERMINAL_ID,TM.STATUS,UTM.USER_ID,UTM.USER_STATUS "
				+ "FROM   STORE_MASTER SM   FULL OUTER JOIN TERMINAL_MASTER TM ON (TM.STORE_ID = SM.STORE_ID)  FULL OUTER JOIN  "
				+ "USER_TERMINAL_MAPPING UTM ON UTM.STORE_ID =  COALESCE (SM.STORE_ID, TM.STORE_ID)  order by SM.MERCHANT_ID,SM.STORE_ID,TM.TERMINAL_ID,UTM.USER_ID";
		try {

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJSONArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is null [" + connection + "]");

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			JSONObject json = null;
			while (merchantRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.MERCHANT_ID,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.STORE_ID, merchantRS.getString(2));
				json.put(CevaCommonConstants.TERMINAL_ID,
						merchantRS.getString(3));
				json.put(CevaCommonConstants.STATUS, merchantRS.getString(4));
				json.put(CevaCommonConstants.USER_ID, merchantRS.getString(5));
				json.put(CevaCommonConstants.USER_STATUS,
						merchantRS.getString(6));
				merchantJSONArray.add(json);
				json.clear();
				json = null;

			}
			logger.debug("MerchantJSONArray [" + merchantJSONArray + "]");
			resultJson.put(CevaCommonConstants.CREDIT_DASHBOARD,
					merchantJSONArray);

			merchantMap.put(CevaCommonConstants.CREDIT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetcreditDashBoard ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetcreditDashBoard [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getInboxDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetInboxDetails... ");

		JSONArray inboxMsgJSONArray = null;
		JSONArray terminalJSONArray = null;
		HashMap<String, Object> inboxMap = null;

		PreparedStatement terminalPstmt = null;
		ResultSet terminalRS = null;
		Connection connection = null;

		String loginId = "";
		String emailIdQry = "Select KEY_VALUE from CONFIG_DATA where KEY_GROUP=? and KEY_TYPE=? ";
		JSONObject json = null;
		String ticket;
		int ticketId = 0;
		
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			inboxMsgJSONArray = new JSONArray();
			terminalJSONArray = new JSONArray();
			inboxMap = new HashMap<String, Object>();
			json = new JSONObject();

			logger.debug("Request JSON [" + requestJSON + "]");
			loginId = requestJSON.getString(CevaCommonConstants.MAKER_ID);
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is null [" + connection + "]");

			terminalPstmt = connection.prepareStatement(emailIdQry);
			terminalPstmt.setString(1, "INBOX");
			terminalPstmt.setString(2, "INBOX_EMAIL");

			terminalRS = terminalPstmt.executeQuery();
			while (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.INBOX_EMAILS,
						terminalRS.getString(1));
			}

			terminalPstmt.close();
			terminalRS.close();
			
			String emailSubjectQry = "select KEY_VALUE from  CONFIG_DATA where KEY_GROUP=? and KEY_TYPE=?  ";

			terminalPstmt = connection.prepareStatement(emailSubjectQry);
			terminalPstmt.setString(1, "INBOX");
			terminalPstmt.setString(2, "INBOX_SUBJECT");

			terminalRS = terminalPstmt.executeQuery();

			while (terminalRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY,
						terminalRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL,
						terminalRS.getString(1));
				inboxMsgJSONArray.add(json);
				json.clear();
			}

			terminalPstmt.close();
			terminalRS.close();
			
			responseJSON.put(CevaCommonConstants.INBOX_SUBJECT_LIST,
					inboxMsgJSONArray);
			
			String ticketQry = "select count(*) from INBOX";
			terminalPstmt = connection.prepareStatement(ticketQry);

			terminalRS = terminalPstmt.executeQuery();
			while (terminalRS.next()) {
				ticketId = terminalRS.getInt(1);
			}
			
			terminalPstmt.close();
			terminalRS.close();
			
			ticketId = ticketId + 1;
			ticket = "BT00" + String.valueOf(ticketId);

			String storeIdQry = "select STORE_ID from STORE_MASTER where LOCATION=(select LOCATION from USER_INFORMATION UI,USER_LOGIN_CREDENTIALS ULC where UI.COMMON_ID=ULC.COMMON_ID and ULC.LOGIN_USER_ID=? )";
			terminalPstmt = connection.prepareStatement(storeIdQry);
			terminalPstmt.setString(1, loginId);

			terminalRS = terminalPstmt.executeQuery();
			String storeId = "";
			while (terminalRS.next()) {
				storeId = terminalRS.getString(1);
			}
			
			terminalPstmt.close();
			terminalRS.close();

			String terminalData = "select TM.TERMINAL_ID,UTM.USER_ID "
					+ "from  TERMINAL_MASTER TM FULL OUTER JOIN  USER_TERMINAL_MAPPING UTM ON (TM.TERMINAL_ID=UTM.TERMINAL_ID) "
					+ "where TM.STORE_ID=? order by TM.TERMINAL_ID";

			terminalPstmt = connection.prepareStatement(terminalData);
			terminalPstmt.setString(1, storeId);

			terminalRS = terminalPstmt.executeQuery();

			while (terminalRS.next()) {
				String terminal = terminalRS.getString(1);
				if (terminal == null) {
					terminal = " ";
				}
				String userid = terminalRS.getString(2);
				if (userid == null) {
					userid = " ";
				}
				json.put(CevaCommonConstants.TERMINAL_ID, terminal);
				json.put(CevaCommonConstants.USER_ID, userid);
				terminalJSONArray.add(json);
				json.clear();

			}
			responseJSON.put("TERMINAL_USERS", terminalJSONArray);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.TICKET_ID, ticket);

			inboxMap.put(CevaCommonConstants.INBOX_INFO, responseJSON);
			logger.debug("Store Map [" + inboxMap + "]");
			responseDTO.setData(inboxMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetInboxDetails [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  GetInboxDetails [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			
			DBUtils.closeResultSet(terminalRS);
			
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);
			inboxMsgJSONArray = null;
			terminalJSONArray = null;
			inboxMap = null;

		}
		return responseDTO;
	}

	public ResponseDTO getInboxDashBoard(RequestDTO requestDTO) {
		logger.debug("Inside GetInboxDashBoard DAO .. ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJSONArray = null;

		Connection connection = null;

		PreparedStatement terminalPstmt = null;

		ResultSet terminalRS = null;

		String storeId = "";
		String storeIdQry = "";
		String merchantQry = "";

		JSONObject json = null;

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJSONArray = new JSONArray();
			json = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			storeIdQry = "select STORE_ID from STORE_MASTER where "
					+ "LOCATION=(select LOCATION from USER_INFORMATION UI,USER_LOGIN_CREDENTIALS ULC"
					+ " where UI.COMMON_ID=ULC.COMMON_ID and ULC.LOGIN_USER_ID=? )";

			terminalPstmt = connection.prepareStatement(storeIdQry);
			terminalPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));

			terminalRS = terminalPstmt.executeQuery();

			if (terminalRS.next()) {
				storeId = terminalRS.getString(1);
			}
			
			terminalPstmt.close();
			terminalRS.close();
			
			merchantQry = "SELECT STORE_ID,TICKET_NO,REF_NO,MAKER_ID,MAKER_DTTM from INBOX where STORE_ID=?";

			terminalPstmt = connection.prepareStatement(merchantQry);
			terminalPstmt.setString(1, storeId);

			terminalRS = terminalPstmt.executeQuery();

			while (terminalRS.next()) {
				json.put(CevaCommonConstants.STORE_ID, terminalRS.getString(1));
				json.put(CevaCommonConstants.TICKET_ID, terminalRS.getString(2));
				json.put(CevaCommonConstants.REF_NO, terminalRS.getString(3));
				json.put(CevaCommonConstants.MAKER_ID, terminalRS.getString(4));
				json.put(CevaCommonConstants.MAKER_DATE,
						terminalRS.getString(5));
				merchantJSONArray.add(json);
				json.clear();
			}

			logger.debug("merchantJSONArray [" + merchantJSONArray + "]");
			resultJson.put(CevaCommonConstants.CREDIT_DASHBOARD,
					merchantJSONArray);

			merchantMap.put(CevaCommonConstants.CREDIT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetInboxDashBoard [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e) {
			logger.debug("Exception in GetInboxDashBoard [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			merchantMap = null;
			resultJson = null;
			merchantJSONArray = null;

			storeId = null;
			storeIdQry = null;
			merchantQry = null;

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);		}
		return responseDTO;
	}
}
