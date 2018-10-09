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
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class WebLoginDAOAction {

	Logger logger = Logger.getLogger(SwitchUIDAO.class);

	ResponseDTO responseDTO = null;
	RequestDTO requestDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO WebLoginDetails(RequestDTO requestDTO) {

		logger.debug("Inside WebLoginDetails.. ");
		Connection connection = null;
		CallableStatement callableStatement = null;

		String userid = null;
		String macaddress = null;
		String lanip = null;
		String ulsid = null;

		String insertStoreProc = "{call INSERT_WEBLOGIN(?,?,?,?,?)}";
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			userid = requestJSON.getString("USER_ID");
			macaddress = requestJSON.getString("MAC_ADDRESS");
			lanip = requestJSON.getString("LAN_IP");
			ulsid = requestJSON.getString("ULSID");

			logger.debug("connection is  [" + connection + "]");

			callableStatement = connection.prepareCall(insertStoreProc);
			callableStatement.setString(1, userid);
			callableStatement.setString(2, macaddress);
			callableStatement.setString(3, lanip);
			callableStatement.setString(4, ulsid);
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);

			logger.debug("Result Count is  [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO.addMessages("ULSID Details Successfully Created.");

			} else if (resCnt == -1) {
				responseDTO.addError("Problem while creating ULSID.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in WebLoginDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while creating ULSID.");
		} catch (Exception e) {
			logger.debug("Exception in WebLoginDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while creating ULSID.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			userid = null;
			macaddress = null;
			lanip = null;
			ulsid = null;
			insertStoreProc = null;
		}
		return responseDTO;
	}

	public ResponseDTO getuseriddata(RequestDTO requestDTO) {

		logger.debug("Inside Getuseriddata... ");

		HashMap<String, Object> userDataMap = null;
		JSONObject resultJson = null;
		JSONArray userSONArray = null;

		PreparedStatement userPstmt = null;
		Connection connection = null;
		ResultSet userRS = null;

		String entityQry = "select USER_ID FROM USER_LOCKING_INFO";
		try {

			userDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			userSONArray = new JSONArray();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");
			userPstmt = connection.prepareStatement(entityQry);
			userRS = userPstmt.executeQuery();
			
			JSONObject json = null;
			while (userRS.next()) {
				json = new JSONObject();
				// json.put("key", userRS.getString(1));
				json.put("value", userRS.getString(1));
				userSONArray.add(json);
			}

			json.clear();
			json = null;
			resultJson.put("USER_DATA", userSONArray);
			userDataMap.put("USER_DATA", resultJson);

			logger.debug("StoreDataMap [" + userDataMap + "]");
			responseDTO.setData(userDataMap);

		} catch (SQLException e) {
			logger.debug("SQLException in Getuseriddata [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal error occured.");
		} catch (Exception e) {
			logger.debug("Exception in Getuseriddata [" + e.getMessage() + "]");
			responseDTO.addError("Internal error occured.");
		} finally {

			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);
			
			entityQry = null;
			userDataMap = null;
			resultJson = null;
			userSONArray = null;
		}

		return responseDTO;
	}

}
