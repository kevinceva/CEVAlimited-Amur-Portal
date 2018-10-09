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
import com.ceva.util.DBUtils;

public class AnnouncementDAO {

	Logger logger = Logger.getLogger(AnnouncementDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;

	public ResponseDTO getAnnouncementInformation(RequestDTO requestDTO) {

		logger.debug("Inside GetAnnouncementInformation.. ");
		HashMap<String, Object> announceMap = null;
		JSONObject resultJson = null;
		JSONArray announceJSONArray = null;

		Connection connection = null;
		PreparedStatement announcePstmt = null;
		ResultSet announceRS = null;

		String query = "";

		JSONObject json = null;

		try {
			responseDTO = new ResponseDTO();
			requestJSON = new JSONObject();
			resonseJSON = new JSONObject();

			announceMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			announceJSONArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is[" + connection + "]");

			query = "Select TXN_REF_NO,"
					+ "decode(TYPE_OF_SETTING,'G','Group','M','Merchant','U','User',TYPE_OF_SETTING),"
					+ "DETAILS,MESSAGE,decode(STATUS,'A','Active','B','In-active'),"
					+ "MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from ANNOUNCE_MASTER where STATUS='A' ";

			announcePstmt = connection.prepareStatement(query);
			announceRS = announcePstmt.executeQuery();
			json = new JSONObject();
			while (announceRS.next()) {

				json.put("TXN_REF_NO", announceRS.getString(1));
				json.put("TYPE_OF_SETTING", announceRS.getString(2));
				json.put("ID", announceRS.getString(3));
				json.put("MESSAGE", announceRS.getString(4));
				json.put("STATUS", announceRS.getString(5));
				json.put(CevaCommonConstants.MAKER_ID, announceRS.getString(6));
				json.put(CevaCommonConstants.MAKER_DATE,
						announceRS.getString(7));
				announceJSONArray.add(json);

				json.clear();
			}

			resultJson.put("ANNOUNCE_LIST", announceJSONArray);

			announcePstmt.close();
			announceRS.close();
			announceJSONArray.clear();

			query = "SELECT GROUP_ID,GROUP_NAME FROM  "
					+ "USER_GROUPS WHERE APPL_CODE='AgencyBanking' ORDER BY MAKER_DTTM";

			announcePstmt = connection.prepareStatement(query);
			announceRS = announcePstmt.executeQuery();
			while (announceRS.next()) {
				json.put("key",
						announceRS.getString(1) + "-" + announceRS.getString(2));
				json.put("val", announceRS.getString(1));
				announceJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_GROUPS", announceJSONArray);

			announcePstmt.close();
			announceRS.close();
			announceJSONArray.clear();

			query = "Select A.LOGIN_USER_ID  from USER_LOGIN_CREDENTIALS A order by A.LOGIN_USER_ID";

			announcePstmt = connection.prepareStatement(query);

			announceRS = announcePstmt.executeQuery();
			while (announceRS.next()) {
				json.put("key", announceRS.getString(1));
				json.put("val", announceRS.getString(1));
				announceJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_INFO", announceJSONArray);

			announcePstmt.close();
			announceRS.close();
			announceJSONArray.clear();

			query = "Select MM.MERCHANT_ID  from MERCHANT_MASTER MM order by MM.MERCHANT_ID ";

			announcePstmt = connection.prepareStatement(query);

			announceRS = announcePstmt.executeQuery();
			while (announceRS.next()) {
				json.put("key", announceRS.getString(1));
				json.put("val", announceRS.getString(1));

				announceJSONArray.add(json);
				json.clear();
			}

			resultJson.put("MERCHANT_GROUPS", announceJSONArray);
			announceMap.put("ANNOUNCE_LIST", resultJson);

			logger.debug("AnnounceMap [" + announceMap + "]");
			responseDTO.setData(announceMap);

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got SQLException in GetAnnouncementInformation ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in GetAnnouncementInformation ["
					+ e.getMessage() + "]");
		} finally {
			try {

				DBUtils.closeResultSet(announceRS);
				DBUtils.closePreparedStatement(announcePstmt);
				DBUtils.closeConnection(connection);

			} catch (Exception e) {
				e.printStackTrace();
			}

			announceMap = null;
			resultJson = null;
			announceJSONArray = null;
			json = null;
		}

		return responseDTO;
	}

	public ResponseDTO getAnnouncementData(RequestDTO requestDTO) {

		logger.debug("Inside GetAnnouncementData..");

		Connection connection = null;
		HashMap<String, Object> merchantMap = null;

		JSONObject resultJson = null;
		JSONObject json = null;

		JSONArray userGroupsJSONArray = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String merchantQry = "SELECT GROUP_ID||'~'||GROUP_NAME FROM USER_GROUPS ORDER BY GROUP_NAME";
		try {
			responseDTO = new ResponseDTO();
			resonseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is[" + connection + "]");

			merchantMap = new HashMap<String, Object>(10);
			resultJson = new JSONObject();

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			userGroupsJSONArray = new JSONArray();
			json = new JSONObject();
			while (merchantRS.next()) {
				json.put("key", merchantRS.getString(1));
				json.put("val", merchantRS.getString(1));

				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_GROUPS", userGroupsJSONArray);

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			userGroupsJSONArray.clear();

			merchantQry = "Select  merchant_id||'~'||merchant_name from merchant_master order by merchant_name";

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put("key", merchantRS.getString(1));
				json.put("val", merchantRS.getString(1));

				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("MERCHANT_GROUPS", userGroupsJSONArray);


			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			userGroupsJSONArray.clear();

			merchantQry = "select ulc.login_user_id||'~'||(select USER_NAME from user_information where common_id=ulc.common_id) "
					+ "from user_login_credentials ulc order by ulc.login_user_id";

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put("key", merchantRS.getString(1));
				json.put("val", merchantRS.getString(1));
				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_INFO", userGroupsJSONArray);

			merchantMap.put("USER_DETAILS", resultJson);

			logger.debug(" merchantMap[" + merchantMap + "]");

			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got SQLException in GetAnnouncementData ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in GetAnnouncementData ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			resultJson = null;
			json = null;
			userGroupsJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertAnnouncementData(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertAnnouncementData.. ");

		CallableStatement callableStatement = null;
		String insertAnnouncementProc = "{call insertAnnouncementProc(?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("RequestJSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

			callableStatement = connection.prepareCall(insertAnnouncementProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString("TYPE_OF_DATA"));
			callableStatement.setString(3,
					requestJSON.getString("TYPE_OF_DATA_VAL"));
			callableStatement.setString(4, requestJSON.getString("MESSAGE"));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);
			String Msg = callableStatement.getString(6);

			logger.debug("ResultCnt from DB [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (SQLException e) {
			logger.debug("Got SQLException [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e) {
			logger.debug("Got Exception [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			try {
				
				DBUtils.closeCallableStatement(callableStatement);
				DBUtils.closeConnection(connection);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseDTO;
	}

	public ResponseDTO getAnnouncentmentModifyDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetAnnouncentmentModifyDetails.. ");

		Connection connection = null;
		HashMap<String, Object> merchantMap = null;

		JSONObject resultJson = null;
		JSONObject json = null;

		JSONArray userGroupsJSONArray = null;

		PreparedStatement merchantPstmt = null;

		ResultSet merchantRS = null;

		String merchantQry = "SELECT GROUP_ID,GROUP_ID||'~'||GROUP_NAME FROM USER_GROUPS ORDER BY GROUP_NAME";
		try {
			responseDTO = new ResponseDTO();
			resonseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("   connection[" + connection + "]");

			merchantMap = new HashMap<String, Object>(10);
			resultJson = new JSONObject();

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			userGroupsJSONArray = new JSONArray();

			while (merchantRS.next()) {
				json = new JSONObject();
				json.put("key", merchantRS.getString(2));
				json.put("val", merchantRS.getString(1));
				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_GROUPS", userGroupsJSONArray);


			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			userGroupsJSONArray.clear();

			merchantQry = "Select  merchant_id,merchant_id||'~'||merchant_name from merchant_master order by merchant_name";

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json = new JSONObject();
				json.put("key", merchantRS.getString(2));
				json.put("val", merchantRS.getString(1));
				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("MERCHANT_GROUPS", userGroupsJSONArray);

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			userGroupsJSONArray.clear();

			merchantQry = "select ulc.login_user_id,ulc.login_user_id||'~'||(select USER_NAME from user_information where common_id=ulc.common_id) "
					+ "from user_login_credentials ulc order by ulc.login_user_id";

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json = new JSONObject();
				json.put("key", merchantRS.getString(2));
				json.put("val", merchantRS.getString(1));
				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_INFO", userGroupsJSONArray);

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			userGroupsJSONArray.clear();

			String announcementDataQry = "select TYPE_OF_SETTING,DETAILS,MESSAGE,STATUS,TXN_REF_NO "
					+ "from ANNOUNCE_MASTER where TXN_REF_NO=?";

			merchantPstmt = connection.prepareStatement(announcementDataQry);
			merchantPstmt.setString(1, requestJSON.getString("REFERENCE_NO"));

			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				resultJson.put("TYPE_OF_SETTING", merchantRS.getString(1));
				resultJson.put("DETAILS", merchantRS.getString(2));
				resultJson.put("MESSAGE", merchantRS.getString(3));
				resultJson.put("STATUS", merchantRS.getString(4));
				resultJson.put("TXN_REF_NO", merchantRS.getString(5));
			}

			merchantMap.put("USER_DETAILS", resultJson);

			logger.debug("MerchantMap[" + merchantMap + "]");

			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
			logger.debug("Got SQLException  in GetAnnouncentmentModifyDetails ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in GetAnnouncentmentModifyDetails ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			json = null;
			userGroupsJSONArray = null;

		}

		return responseDTO;
	}

	public ResponseDTO updateAnnouncementData(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside UpdateAnnouncementData... ");

		CallableStatement callableStatement = null;
		String updateAnnouncementDataProc = "{call updateAnnouncementDataProc(?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON " + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			
			logger.debug("connection is [" + connection+ "]");

			callableStatement = connection
					.prepareCall(updateAnnouncementDataProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString("REFERENCE_NO"));
			callableStatement.setString(3,
					requestJSON.getString("TYPE_OF_DATA"));
			callableStatement.setString(4,
					requestJSON.getString("TYPE_OF_DATA_VAL"));
			callableStatement.setString(5, requestJSON.getString("MESSAGE"));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);
			String Msg = callableStatement.getString(7);

			logger.debug("ResultCnt from DB:::" + resCnt);

			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
			logger.debug("Got SQLException  in UpdateAnnouncementData ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in UpdateAnnouncementData ["
					+ e.getMessage() + "]");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO viewAnnouncementData(RequestDTO requestDTO) {

		logger.debug("Inside ViewAnnouncementData... ");
		HashMap<String, Object> announceMap = null;
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement announcePstmt = null;
		ResultSet announceRS = null;

		try {
			responseDTO = new ResponseDTO();
			resonseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			announceMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("connection is[" + connection + "]");

			String storeQry = "Select TXN_REF_NO,decode(TYPE_OF_SETTING,'G','Group','M','Merchant','U','User',TYPE_OF_SETTING),DETAILS,MESSAGE,decode(STATUS,'A','Active','B','Blocked'),MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from ANNOUNCE_MASTER where TXN_REF_NO=? ";

			announcePstmt = connection.prepareStatement(storeQry);
			announcePstmt.setString(1, requestJSON.getString("REFERENCE_NO"));
			announceRS = announcePstmt.executeQuery();
			while (announceRS.next()) {
				resultJson.put("TXN_REF_NO", announceRS.getString(1));
				resultJson.put("TYPE_OF_SETTING", announceRS.getString(2));
				resultJson.put("ID", announceRS.getString(3));
				resultJson.put("MESSAGE", announceRS.getString(4));
				resultJson.put("STATUS", announceRS.getString(5));
				resultJson.put(CevaCommonConstants.MAKER_ID,
						announceRS.getString(6));
				resultJson.put(CevaCommonConstants.MAKER_DATE,
						announceRS.getString(7));
			}

			announceMap.put("ANNOUNCE_INFO", resultJson);

			logger.debug("AnnouncementMap [" + announceMap + "]");
			responseDTO.setData(announceMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
			logger.debug("Got SQLException  in ViewAnnouncementData ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in ViewAnnouncementData ["
					+ e.getMessage() + "]");
		} finally {
			try {
				DBUtils.closeResultSet(announceRS);
				DBUtils.closePreparedStatement(announcePstmt);
				DBUtils.closeConnection(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return responseDTO;
	}

	public ResponseDTO announcementStatusChange(RequestDTO requestDTO) {

		logger.debug("Inside AnnouncementStatusChange.. ");
		Connection connection = null;
		CallableStatement callableStatement = null;
		String updateAnnouncementStatusProc = "{call updateAnnouncementStatusProc(?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON " + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(updateAnnouncementStatusProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString("REFERENCE_NO"));
			callableStatement.setString(3, requestJSON.getString("STATUS"));
			callableStatement.registerOutParameter(4, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(4);
			String Msg = callableStatement.getString(5);

			logger.debug("ResultCnt from DB [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
			logger.debug("Got SQLException  in AnnouncementStatusChange ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in AnnouncementStatusChange ["
					+ e.getMessage() + "]");
		} finally {
			try {
				
				DBUtils.closeCallableStatement(callableStatement);
				DBUtils.closeConnection(connection);
				} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseDTO;
	}

}
