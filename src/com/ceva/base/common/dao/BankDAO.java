package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.sql.CallableStatement;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class BankDAO {

	Logger logger = Logger.getLogger(BankDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;

	public ResponseDTO getlocationlist(RequestDTO requestDTO) {

		logger.debug("Inside Getlocationlist DAO ... ");

		HashMap<String, Object> storeDataMap = null;
		JSONObject resultJson = null;
		JSONArray locationJSONArray = null;

		PreparedStatement locationPstmt = null;
		ResultSet locationRS = null;
		Connection connection = null;

		String entityQry = "";
		JSONObject json = null;
		try {
			storeDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			locationJSONArray = new JSONArray();

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			entityQry = "(SELECT HUDUMA_SERVICE_CODE,HUDUMA_SERVICE_DESC FROM HUDUMA_MASTER ) "
					+ "UNION (SELECT  BANK_CODE, BANK_NAME FROM BANK_MASTER)";

			locationPstmt = connection.prepareStatement(entityQry);
			locationRS = locationPstmt.executeQuery();
			json = new JSONObject();
			while (locationRS.next()) { 
				json.put(CevaCommonConstants.SELECT_KEY,
						locationRS.getString(1) + "-" + locationRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL,
						locationRS.getString(1));
				locationJSONArray.add(json);
				json.clear();
			}
			 
			resultJson
					.put(CevaCommonConstants.LOCATION_LIST, locationJSONArray);
			storeDataMap.put(CevaCommonConstants.STORE_INFO, resultJson);

			logger.debug("StoreDataMap [" + storeDataMap + "]");
			responseDTO.setData(storeDataMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

		} catch (SQLException e) { responseDTO.addError("Internal Error Occured While Executing.");
			 
			logger.debug("SQLException in getlocationlist [" + e.getMessage()
					+ "]");
		} catch (Exception e) { responseDTO.addError("Internal Error Occured While Executing.");
			 
			logger.debug("Exception in getlocationlist [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(locationRS);
			DBUtils.closePreparedStatement(locationPstmt);
			DBUtils.closeConnection(connection);
			storeDataMap = null;
			resultJson = null;
			locationJSONArray = null;
			entityQry = null;
		}
		return responseDTO;
	}

	public ResponseDTO insertBankConfig(RequestDTO requestDTO) {
		logger.debug("Inside InsertBankConfig DAO .. ");

		Connection connection = null;

		CallableStatement callableStatement = null;
		String insertBankInfoProc = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			insertBankInfoProc = "{call INSERTBANKCONFIGPROC(?,?,?)}";

			callableStatement = connection.prepareCall(insertBankInfoProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.BANK_MULTI_DATA));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);
			logger.debug("ResultCnt from DB [" + resCnt);
			if (resCnt == 1) {
				responseDTO
						.addMessages("Bank Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Bank Information Already Exists. ");
			} else {
				responseDTO.addError("Bank Information Creation failed.");
			}

		} catch (SQLException e) { responseDTO.addError("Internal Error Occured While Executing.");
			 
			logger.debug("SQLException in insertBankConfig [" + e.getMessage()
					+ "]");
		} catch (Exception e) { responseDTO.addError("Internal Error Occured While Executing.");
			 
			logger.debug("Exception in insertBankConfig [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			insertBankInfoProc = null;
		}

		return responseDTO;
	}

	public ResponseDTO checkUserId(RequestDTO requestDTO) {

		logger.debug("Inside CheckUserID DAO... ");
		HashMap<String, Object> UserDataMap = null;
		JSONObject resultJson = null;

		String service = "";
		String userChkQry = "";
		int ResCount = 0;

		PreparedStatement userChkPstmt = null;
		ResultSet USerChkRS = null;
		Connection connection = null;

		try {

			UserDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			service = requestJSON.getString(CevaCommonConstants.SERVICE);
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			userChkQry = "Select count(*) from FINANCIAL_MASTER where trim(BANK_CODE)=trim(?)";

			userChkPstmt = connection.prepareStatement(userChkQry);
			userChkPstmt.setString(1, service);
			USerChkRS = userChkPstmt.executeQuery();
			if (USerChkRS.next()) {
				ResCount = USerChkRS.getInt(1);
			}
			logger.debug("Res Count [" + ResCount + "]");

			resultJson.put(CevaCommonConstants.RESULT_COUNT, ResCount);
			UserDataMap.put(CevaCommonConstants.USER_CHECK_INFO, resultJson);

			logger.debug("MerchantDataMap [" + UserDataMap + "]");
			responseDTO.setData(UserDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) { responseDTO.addError("Internal Error Occured While Executing.");
			 
			logger.debug("SQLException in checkUserId [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			 
			logger.debug("Exception in checkUserId [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(USerChkRS);
			DBUtils.closePreparedStatement(userChkPstmt);
			DBUtils.closeConnection(connection);
			UserDataMap = null;
			resultJson = null;

			service = null;
			userChkQry = null;
			ResCount = 0;
		}

		return responseDTO;
	}
}
