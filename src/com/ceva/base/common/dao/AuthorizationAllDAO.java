package com.ceva.base.common.dao;

import java.awt.image.DataBufferUShort;
import java.net.URLDecoder;
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
import com.ceva.util.DBUtils;

public class AuthorizationAllDAO {

	Logger logger = Logger.getLogger(AuthorizationAllDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getAuthorizationList(RequestDTO requestDTO) {

		logger.debug("Inside getAuthorizationList.. ");
		HashMap<String, Object> announceMap = null;
		JSONObject resultJson = null;
		JSONArray announceJSONArray = null;

		Connection connection = null;
		PreparedStatement announcePstmt = null;
		ResultSet announceRS = null;
		JSONObject json = null;
		String query = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();

			announceMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			announceJSONArray = new JSONArray();
			json = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection [" + connection + "]");

			query = "SELECT "
					+ "A.HEADING_NAMES,A.ACTION_URL,A.AUTH_CODE,"
					+ "(SELECT COUNT(*) FROM AUTH_PENDING B WHERE B.AUTH_CODE=A.AUTH_CODE AND B.STATUS='P' ),"
					+ "(SELECT COUNT(*) FROM AUTH_PENDING B WHERE B.AUTH_CODE=A.AUTH_CODE AND B.STATUS='C' ),"
					+ "(SELECT COUNT(*) FROM AUTH_PENDING B WHERE B.AUTH_CODE=A.AUTH_CODE AND B.STATUS='R' ),"
					+ "(SELECT COUNT(*) FROM AUTH_PENDING B WHERE B.AUTH_CODE=A.AUTH_CODE AND B.STATUS='P' AND  trunc(B.MAKER_DTTM)=trunc(sysdate)  )  FROM AUTH_MASTER A ";

			announcePstmt = connection.prepareStatement(query); 
 
			announceRS = announcePstmt.executeQuery();

			while (announceRS.next()) {
				json.put("HEADING_NAME", announceRS.getString(1));
				json.put("ACTION_URL", announceRS.getString(2));
				json.put("AUTH_CODE", announceRS.getString(3));
				json.put("PENDING", announceRS.getString(4));
				json.put("CLOSED", announceRS.getString(5));
				json.put("REJECTED", announceRS.getString(6));
				json.put("NEW", announceRS.getString(7));
				announceJSONArray.add(json);
				json.clear();
			}

			resultJson.put("AUTH_PENDING_LIST", announceJSONArray);
			announceMap.put("AUTH_PENDING_LIST", resultJson);

			DBUtils.closeResultSet(announceRS);
			DBUtils.closePreparedStatement(announcePstmt);
			
			logger.debug("Auth Map [" + announceMap + "]");
			responseDTO.setData(announceMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got SQLException in getAuthorizationList ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Got Exception in getAuthorizationList ["
					+ e.getMessage() + "]");
			logger.error(
					"Got Exception in getAuthorizationList [" + e.getMessage()
							+ "]", e);
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
		}

		return responseDTO;
	}

	public ResponseDTO CommonAuthList(RequestDTO requestDTO) {

		logger.debug("Inside  CommonAuthList.. ");
		Connection connection = null;
		HashMap<String, Object> resultMap = null;

		String userQry = "{call AuthPkg.PendingAuth(?,?,?,?)}";
		String auth_code = "";
		String error = "";
		String status = "";

		CallableStatement callable = null;
		ResultSet userRS = null;

		JSONObject json = null;
		JSONArray subJsonArray = null;
		try {
			responseDTO = new ResponseDTO();
			resultMap = new HashMap<String, Object>();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			responseJSON = requestJSON;

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");

			logger.debug("Authorization  " + " auth_code " + auth_code
					+ "] status " + status);

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + connection + "]");

			callable = connection.prepareCall(userQry);
			callable.setString(1, auth_code);
			callable.setString(2, status);
			callable.registerOutParameter(3, OracleTypes.CURSOR);
			callable.registerOutParameter(4, OracleTypes.VARCHAR);

			callable.execute();
			logger.debug("Authorization block executed successfully "
					+ "with error_message[" + callable.getString(4) + "]");
			error = callable.getString(4);

			if (error.equalsIgnoreCase("SUCCESS")) {
				userRS = (ResultSet) callable.getObject(3);
				subJsonArray = new JSONArray();
				json = new JSONObject();

				if (auth_code.equalsIgnoreCase("SERVICEAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("SERVICE_CODE", userRS.getString(2));
						json.put("SERVICE_NAME", userRS.getString(3));
						json.put("MAKER_ID", userRS.getString(4));
						json.put("MAKER_DATE", userRS.getString(5));
						json.put("status", userRS.getString(6));

						subJsonArray.add(json);
						json.clear();
					}
					
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("SUBSEAUTH")) {
					while (userRS.next()) {

						json.put("REF_NO", userRS.getString(1));
						json.put("SERVICE_CODE", userRS.getString(2));
						json.put("SERVICE_NAME", userRS.getString(3));
						json.put("SUB_SERVICE_CODE", userRS.getString(4));
						json.put("SUB_SERVICE_NAME", userRS.getString(5));
						json.put("MAKER_ID", userRS.getString(6));
						json.put("MAKER_DATE", userRS.getString(7));
						json.put("STATUS", userRS.getString(8));

						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("PRCSSAUTH")) {
					while (userRS.next()) {
						json.put(CevaCommonConstants.PROC_CODE,
								userRS.getString(1));
						json.put(CevaCommonConstants.PROCESS_NAME,
								userRS.getString(2));
						json.put(CevaCommonConstants.MAKER_ID,
								userRS.getString(3));
						json.put(CevaCommonConstants.MAKER_DATE,
								userRS.getString(4));
						json.put(CevaCommonConstants.STATUS,
								userRS.getString(5));
						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("BINAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("BIN_CODE", userRS.getString(2));
						json.put("BANK_NAME", userRS.getString(3));
						json.put("BIN", userRS.getString(4));
						json.put("BIN_DESC", userRS.getString(5));
						json.put("MAKER_ID", userRS.getString(6));
						json.put("MAKER_DATE", userRS.getString(7));
						json.put("STATUS", userRS.getString(8));
						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("FEEAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("FEE_CODE", userRS.getString(2));
						json.put("SERVICE_CODE", userRS.getString(3));
						json.put("SUB_SERVICE_CODE", userRS.getString(4));
						json.put("MAKER_ID", userRS.getString(5));
						json.put("MAKER_DATE", userRS.getString(6));
						json.put("STATUS", userRS.getString(7));
						json.put("FEENAME", userRS.getString(8));
						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("USERAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("LOGIN_USER_ID", userRS.getString(2));
						json.put("USER_NAME", userRS.getString(3));
						json.put("MAKER_ID", userRS.getString(4));
						json.put("MAKER_DTTM", userRS.getString(5));
						json.put("LOCATION", userRS.getString(6));
						json.put("OFFICE_NAME", userRS.getString(7));
						json.put("AUTH_FLAG", userRS.getString(8));
						json.put("STATUS", userRS.getString(9));
						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("MODUSERAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("LOGIN_USER_ID", userRS.getString(2));
						json.put("USER_NAME", userRS.getString(3));
						json.put("MAKER_ID", userRS.getString(4));
						json.put("MAKER_DTTM", userRS.getString(5));
						json.put("LOCATION", userRS.getString(6));
						json.put("OFFICE_NAME", userRS.getString(7));
						json.put("AUTH_FLAG", userRS.getString(8));
						json.put("STATUS", userRS.getString(9));
						subJsonArray.add(json);
						json.clear();

					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("MERCHAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("MER_ID", userRS.getString(2));
						json.put("MER_NAME", userRS.getString(3));
						json.put("MER_ADM", userRS.getString(4));
						json.put(CevaCommonConstants.MAKER_ID,
								userRS.getString(5));
						json.put(CevaCommonConstants.MAKER_DATE,
								userRS.getString(6));
						json.put(CevaCommonConstants.STATUS,
								userRS.getString(7));
						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("STOREAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("STORE_ID", userRS.getString(2));
						json.put("STORE_NAME", userRS.getString(3));
						json.put("MERCHANT_ID", userRS.getString(4));
						json.put("MERCHANT_NAME", userRS.getString(5));
						json.put("MAKER_ID", userRS.getString(6));
						json.put("MAKER_DATE", userRS.getString(7));
						json.put("STATUS", userRS.getString(8));
						subJsonArray.add(json);
						json.clear();
					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("TERMAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("TERMINAL_ID", userRS.getString(2));
						json.put("STORE_ID", userRS.getString(3));
						json.put("MERCHANT_ID", userRS.getString(4));
						json.put("SERIAL_NO", userRS.getString(5));
						json.put("MAKER_ID", userRS.getString(6));
						json.put("MAKER_DATE", userRS.getString(7));
						json.put("STATUS", userRS.getString(8));
						subJsonArray.add(json);
						json.clear();

					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("USERSTATUSAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("User_id", userRS.getString(2));
						json.put("User_Name", userRS.getString(3));
						json.put("Location", userRS.getString(4));
						json.put("MAKER_ID", userRS.getString(5));
						json.put("MAKER_DATE", userRS.getString(6));
						json.put("STATUS", userRS.getString(7));
						subJsonArray.add(json);
						json.clear();

					}
					DBUtils.closeResultSet(userRS);
				} else if (auth_code.equalsIgnoreCase("TMMAUTH")) {
					while (userRS.next()) {
						json.put("REF_NO", userRS.getString(1));
						json.put("old_merchantid", userRS.getString(2));
						json.put("old_storeid", userRS.getString(3));
						json.put("new_merchantid", userRS.getString(4));
						json.put("new_storeid", userRS.getString(5));
						json.put("Terminalid", userRS.getString(6));
						json.put("makerid", userRS.getString(7));
						json.put("maker_date", userRS.getString(8));
						json.put("status", userRS.getString(9));
						subJsonArray.add(json);
						json.clear();

					}
					DBUtils.closeResultSet(userRS);
				} else {
					responseDTO.addError(error);
				}

				responseJSON.put("agentMultiData", subJsonArray);
				resultMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
				responseDTO.setData(resultMap);
				logger.debug(" CommonAuthList resultMap " + responseJSON);

			}
		} catch (SQLException e) {
			logger.debug("SQLException in CommonAuthList [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.debug("Exception in CommonAuthList 	  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(userRS);
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			resultMap = null;
			userQry = null;
			responseJSON = null;
			json = null;
			subJsonArray = null;
		}
		return responseDTO;
	}

	public ResponseDTO CommonAuthConfirmationBefore(RequestDTO requestDTO) {

		logger.debug("Inside  CommonAuthConfirmationBefore.. ");
		Connection connection = null;
		AuthFetchSubDAO authsubDTO = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		CallableStatement callable = null;

		try {
			responseDTO = new ResponseDTO();

			requestJSON = requestDTO.getRequestJSON();
			authsubDTO = new AuthFetchSubDAO();
			logger.debug("Request JSON [" + requestJSON + "]");
			responseJSON = requestJSON;

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			logger.debug("Authorization  " + " auth_code789 " + auth_code
					+ "] status " + status + "] refno " + ref_no);

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			if (auth_code.equalsIgnoreCase("USERAUTH")) {

				responseDTO = authsubDTO.UserAuthRecordData(requestDTO);

			} else if (auth_code.equalsIgnoreCase("MODUSERAUTH")) {

				responseDTO = authsubDTO.UserAuthRecordData(requestDTO);

			} else if (auth_code.equalsIgnoreCase("SERVICEAUTH")) {
				responseDTO = authsubDTO.ServiceAuthRecordData(requestDTO);
			} else if (auth_code.equalsIgnoreCase("BINAUTH")) {
				responseDTO = authsubDTO.binAuthRecordData(requestDTO);
			} else if (auth_code.equalsIgnoreCase("SUBSEAUTH")) {
				responseDTO = authsubDTO.subServiceAuthRecordData(requestDTO);
			} else if (auth_code.equalsIgnoreCase("FEEAUTH")) {
				responseDTO = authsubDTO.viewFeeDetails(requestDTO);
			}

			else if (auth_code.equalsIgnoreCase("TERMAUTH")) {
				responseDTO = authsubDTO.viewTerminalDetails(requestDTO);
			} else if (auth_code.equalsIgnoreCase("STOREAUTH")) {
				responseDTO = authsubDTO.viewStoreDetails(requestDTO);
			} else if (auth_code.equalsIgnoreCase("MERCHAUTH")) {
				responseDTO = authsubDTO.getMerchantViewDetails(requestDTO);
			} else if (auth_code.equalsIgnoreCase("USERSTATUSAUTH")) {
				responseDTO = authsubDTO
						.getUserActiveDeactiveDetails(requestDTO);
			} else if (auth_code.equalsIgnoreCase("TMMAUTH")) {
				responseDTO = authsubDTO.TerminalMigrationDetails(requestDTO);
			}

			logger.debug(" CommonAuthConfirmationBefore  resultMap "
					+ responseDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {

				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
				logger.debug("Response JSON CommonAuthConfirmationBefore ["
						+ responseJSON + "]");

			}

		} catch (Exception e) {
			logger.debug("Exception in CommonAuthList 	  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			responseJSON = null;

		}
		return responseDTO;
	}

	public ResponseDTO CommonAuthCnfsubmition(RequestDTO requestDTO) {

		logger.debug("Inside AuthorizationAllDAO CommonAuthCnfsubmition.. ");
		Connection connection = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";
		String decs = "";
		String error = "";
		String makerId = "";

		CallableStatement callable = null;

		String userQry = "{call AuthPkg.RecordConfirmation(?,?,?,?,?,?,?)}";
		try {
			responseDTO = new ResponseDTO();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			responseJSON = requestJSON;

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");
			decs = requestJSON.getString("decs");
			makerId = requestJSON.getString("makerId");

			logger.debug("Authorization  " + " auth_code" + auth_code
					+ "] status " + status + "] refno " + ref_no + " decs "
					+ decs);

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callable = connection.prepareCall(userQry);
			callable.setString(1, auth_code);
			callable.setString(2, status);
			callable.setString(3, decs);
			callable.setString(4, ref_no);
			callable.setString(5, makerId);
			callable.registerOutParameter(6, OracleTypes.CURSOR);
			callable.registerOutParameter(7, OracleTypes.VARCHAR);

			callable.execute();
			logger.debug("Authorization block executed successfully "
					+ "with error_message[" + callable.getString(7) + "]");
			error = callable.getString(7);
			if (!error.equalsIgnoreCase("SUCCESS")) {
				responseDTO.addError(error);
			}

			logger.debug(" AuthorizationAllDAO CommonAuthCnfsubmition  resultMap "
					+ responseDTO);

		} catch (SQLException e) {
			logger.debug("SQLException in CommonAuthList [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();

		} catch (Exception e) {
			logger.debug("Exception in CommonAuthList 	  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
			e.printStackTrace();
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);

			responseJSON = null;

		}
		return responseDTO;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

}
