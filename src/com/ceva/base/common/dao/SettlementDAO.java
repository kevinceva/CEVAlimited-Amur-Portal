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

public class SettlementDAO {

	private Logger logger = Logger.getLogger(SettlementDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO settlementData(RequestDTO requestDTO) {

		logger.debug("Inside SettlementData... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject json = null;

		JSONArray jsonArray = null;

		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		String merchantQry = "select BANKID,FILENAME,decode(FILE_STATUS,'S','SUCCESS','F','FAILED','FR','FAILED',FILE_STATUS),"
				+ "decode(FILE_STATUS,'F','Failed To Upload To Web Server.','FR',"
				+ "'Failed To Upload From Web To External Server.','S','Successfully Uploaded To FTP Server.','Failed To Upload.'),"
				+ "to_char(TXN_DATE,'DD-MON-YYYY HH24:MI:SS'),TXN_REF_NO,(select bank_name from bank_master where bank_code=PSD.BANKID) from POSTA_SETTLEMENT_DATA PSD";

		String settleDate = "";
		String tosettleDate = "";
		String fileName = "";
		String bankId = "";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			bankId = requestJSON.getString("bankid");
			settleDate = requestJSON.getString("settlementdate");
			tosettleDate = requestJSON.getString("tosettlementdate");

			logger.debug("Request JSON [" + requestJSON + "]");

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			json = new JSONObject();
			jsonArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			if (bankId.indexOf("NO_VALUE") != -1 || bankId.equalsIgnoreCase("")
					|| bankId == null) {
				merchantQry += " where trunc(TXN_DATE) between to_date(?,'DD/MM/YYYY')  and to_date(?,'DD/MM/YYYY')  order by TXN_DATE desc";
			} else {
				merchantQry += " where (trunc(TXN_DATE) between to_date(?,'DD/MM/YYYY')  and to_date(?,'DD/MM/YYYY')) and BANKID=?  order by TXN_DATE desc";
			}

			prepareStatement = connection.prepareStatement(merchantQry);

			if (bankId.indexOf("NO_VALUE") != -1 || bankId.equalsIgnoreCase("")
					|| bankId == null) {
				prepareStatement.setString(1, settleDate);
				prepareStatement.setString(2, tosettleDate);
			} else {
				prepareStatement.setString(1, settleDate);
				prepareStatement.setString(2, tosettleDate);
				prepareStatement.setString(3, bankId);
			}
			resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				json.put("bank_id", resultSet.getString(1));
				bankId = resultSet.getString(1);
				fileName = resultSet.getString(2);

				json.put("file_name",
						fileName.substring(fileName.indexOf(bankId + "_")));
				json.put("file_status", resultSet.getString(3));
				json.put("file_desc", resultSet.getString(4));
				json.put("file_date", resultSet.getString(5));
				json.put("txnrefno", resultSet.getString(6));
				json.put("bank_name", resultSet.getString(7));
				jsonArray.add(json);
				json.clear();
			}

			resultJson.put("settle_data", jsonArray);
			storeMap.put("settle_data", resultJson);
			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in SettlementData [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in  SettlementData [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(resultSet);
			DBUtils.closePreparedStatement(prepareStatement);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			jsonArray = null;
			json = null;
		}

		return responseDTO;
	}

	public ResponseDTO settlementBankData(RequestDTO requestDTO) {

		logger.debug("Inside SettlementBankData... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject json = null;

		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		String merchantQry = "select distinct bank_code,bank_code||'~'||bank_name "
				+ "from bank_master where bank_code not in ('POSTAKEN','MPESAKEN','DEPOSITS')";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			json = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is null [" + connection == null + "]");

			prepareStatement = connection.prepareStatement(merchantQry);
			resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				json.put(resultSet.getString(1), resultSet.getString(2));
			}

			resultJson.put("bank_data", json);
			storeMap.put("bank_data", resultJson);

			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in SettlementBankData ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in SettlementBankData [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(resultSet);
			DBUtils.closePreparedStatement(prepareStatement);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			json = null;
		}

		return responseDTO;
	}

	public ResponseDTO addSettlementBank(RequestDTO requestDTO) {

		logger.debug("Inside AddSettlementBank... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject json = null;

		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		String merchantQry = "select bank_code,bank_code||'~'||bank_name "
				+ "from NBIN where bank_code not in ('POSTAKEN','MPESAKEN','DEPOSITS')";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			json = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is null [" + connection == null + "]");

			prepareStatement = connection.prepareStatement(merchantQry);
			resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				json.put(resultSet.getString(1), resultSet.getString(2));
			}

			resultJson.put("bank_data", json);
			storeMap.put("bank_data", resultJson);

			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in AddSettlementBankData ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in AddSettlementBankData ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(resultSet);
			DBUtils.closePreparedStatement(prepareStatement);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			json = null;
		}

		return responseDTO;
	}

	public ResponseDTO ackSettlementBank(RequestDTO requestDTO) {
		logger.debug("Inside AckSettlementBank... ");

		Connection connection = null;
		CallableStatement callableStatement = null;

		String errorMessage = "";

		String updateUserDet = "{call SETTLEMENTRAWDATA.pSettlementBankInsert(?,?,?,?)}";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			callableStatement = connection.prepareCall(updateUserDet);
			callableStatement.setString(1, requestJSON.getString("bankid"));
			callableStatement.setString(2, requestJSON.getString("user_id"));
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();

			errorMessage = callableStatement.getString(3);
			logger.debug("errorMessage [" + errorMessage + "]");

			if (errorMessage.equalsIgnoreCase("SUCCESS")) {
				responseDTO.addMessages(callableStatement.getString(4));
			} else {
				responseDTO.addError(callableStatement.getString(4));
			}

		} catch (SQLException e) {
			logger.debug("SQLException in AckSettlementBank [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in AckSettlementBank  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			errorMessage = null;
			updateUserDet = null;
		}
		return responseDTO;
	}

	public ResponseDTO viewSettlementBanks(RequestDTO requestDTO) {

		logger.debug("Inside ViewSettlementBanks... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;
		JSONObject json = null;

		JSONArray jsonArray = null;

		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		String merchantQry = "select SM.bank_id,(select bank_name "
				+ "from nbin where bank_code=SM.bank_id),SM.ISSUED_BY,to_char(SM.ISSUED_DATE,'DD-MON-YYYY HH24:MI:SS') from settlement_master SM";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			json = new JSONObject();

			jsonArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is null [" + connection == null + "]");

			prepareStatement = connection.prepareStatement(merchantQry);
			resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				json.put("bank_id", resultSet.getString(1));
				json.put("bank_name", resultSet.getString(2));
				json.put("issued_by", resultSet.getString(3));
				json.put("issued_date", resultSet.getString(4));
				jsonArray.add(json);
				json.clear();
			}

			resultJson.put("bank_data", jsonArray);
			storeMap.put("bank_data", resultJson);

			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in ViewSettlementBanks ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in ViewSettlementBanks [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {

			DBUtils.closeResultSet(resultSet);
			DBUtils.closePreparedStatement(prepareStatement);
			DBUtils.closeConnection(connection);

			storeMap = null;
			resultJson = null;
			json = null;
		}

		return responseDTO;
	}
}
