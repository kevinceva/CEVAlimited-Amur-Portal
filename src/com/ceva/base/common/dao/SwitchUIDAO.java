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

public class SwitchUIDAO {

	Logger logger = Logger.getLogger(SwitchUIDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;

	public ResponseDTO getBankDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetAllUserGrps... ");

		Connection connection = null;
		HashMap<String, Object> bankdataMap = null;

		JSONObject resultJson = null;
		JSONObject json = null;
		JSONArray switchJSONArray = null;

		PreparedStatement switchtPstmt = null;
		ResultSet switchRS = null;

		String bankdet = "select bankName,bankId,acqid,nvl(status,'NO') from tbl_bank_info";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			resonseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			bankdataMap = new HashMap<String, Object>(100);
			resultJson = new JSONObject();

			switchtPstmt = connection.prepareStatement(bankdet);
			switchRS = switchtPstmt.executeQuery();

			switchJSONArray = new JSONArray();
			json = new JSONObject();
			while (switchRS.next()) {
				json.put(CevaCommonConstants.bankname, switchRS.getString(1));
				json.put(CevaCommonConstants.bankcode, switchRS.getString(2));
				json.put(CevaCommonConstants.acquirerId, switchRS.getString(3));
				json.put(CevaCommonConstants.status, switchRS.getString(4));
				switchJSONArray.add(json);
				json.clear();
			}

			resultJson.put("BANK_DATA", switchJSONArray);
			bankdataMap.put("BANK_DATA", resultJson);
			responseDTO.setData(bankdataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetDetails [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("SQLException in GetDetails [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(switchRS);
			DBUtils.closePreparedStatement(switchtPstmt);
			DBUtils.closeConnection(connection);
			
			bankdataMap = null;

			resultJson = null;
			json = null;
			switchJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO getDetails(RequestDTO requestDTO) {

		logger.debug("inside GetDetails.. ");

		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		resonseJSON = new JSONObject();

		HashMap<String, Object> bankdataMap = null;

		JSONObject resultJson = null;
		JSONObject json = null;

		JSONArray switchJSONArray = null;

		PreparedStatement switchtPstmt = null;
		ResultSet switchRS = null;

		Connection connection = null;

		String bankdet = "select txndate,txntype,mid,tid,rrn,amt,authcode,resp from ( select to_char(TXNDATE,'DD-MON-YYYY HH24:MI:SS') txndate,"
				+ "DECODE(nvl(TXNTYPE,' '), 'CDP', 'CASH DEPOSIT', 'WDL', 'CASH WITHDRAWL','BEQ','BALANCE ENQUIRY','HDM','HUDUMA',LT.TXNTYPE) txntype,MERCHANTID mid,TERMINALNUMBER  tid,nvl(RRN,' ') rrn, (AMOUNT/100) amt,' ' authcode,"
				+ "RESPONSECODE resp from TBL_TRANLOG LT order by LT.TXNDATE desc ) where rownum < 51";

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			bankdataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			switchtPstmt = connection.prepareStatement(bankdet);
			switchRS = switchtPstmt.executeQuery();

			switchJSONArray = new JSONArray();
			json = new JSONObject();
			while (switchRS.next()) {
				json.put(CevaCommonConstants.txn_date, switchRS.getString(1));
				json.put(CevaCommonConstants.tran_type, switchRS.getString(2));
				json.put(CevaCommonConstants.mid, switchRS.getString(3));
				json.put(CevaCommonConstants.tid, switchRS.getString(4));
				json.put(CevaCommonConstants.RRN, switchRS.getString(5));
				json.put(CevaCommonConstants.amount, switchRS.getString(6));
				json.put(CevaCommonConstants.authcode, switchRS.getString(7));
				json.put(CevaCommonConstants.responsecode,
						switchRS.getString(8));
				switchJSONArray.add(json);
				json.clear();
			}

			logger.debug("SwitchJSONArray [" + switchJSONArray + "]");

			resultJson.put(CevaCommonConstants.LIVE_TRANLOG, switchJSONArray);
			bankdataMap.put(CevaCommonConstants.LIVE_TRANLOG_DATA, resultJson);
			responseDTO.setData(bankdataMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetDetails [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("SQLException in GetDetails [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(switchRS);
			DBUtils.closePreparedStatement(switchtPstmt);
			DBUtils.closeConnection(connection);
			bankdataMap = null;
			resultJson = null;
			json = null;
			switchJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO getDepositeinfo(RequestDTO requestDTO) {

		logger.debug("Inside GetDepositeinfo.. ");
		HashMap<String, Object> BankDataMap = null;
		JSONObject resultJson = null;
		JSONArray banksJSONArray = null;
		PreparedStatement bankPstmt = null;
		ResultSet banksRS = null;
		Connection connection = null;
		JSONObject json = null;
		String entityQry = "SELECT  BANK_CODE , BANK_NAME FROM BANK_MASTER";
		try {
			responseDTO = new ResponseDTO();
			resonseJSON = new JSONObject();

			BankDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			banksJSONArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" Connection is [" + connection + "]");
			bankPstmt = connection.prepareStatement(entityQry);
			banksRS = bankPstmt.executeQuery();

			json = new JSONObject();
			while (banksRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, banksRS.getString(1)
						+ "-" + banksRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, banksRS.getString(1));
				banksJSONArray.add(json);
				json.clear();
			}
			resultJson.put(CevaCommonConstants.BANK_LIST, banksJSONArray);
			BankDataMap.put(CevaCommonConstants.BANK_NAME_INFO, resultJson);

			responseDTO.setData(BankDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetDepositeinfo [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("SQLException in GetDepositeinfo [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(banksRS);
			DBUtils.closePreparedStatement(bankPstmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO insertBankInfo(RequestDTO requestDTO) {

		HashMap<String, Object> resultMap = null;

		String bankcode = null;
		String bankname = null;
		String bankIp = null;
		String c1 = null;
		String bankport = null;
		String c2 = null;
		String c3 = null;
		String acquirerId = null;
		String zpk = null;
		String bin = null;
		String kcv = null;
		String type = null;

		logger.debug("Inside InsertEntityInfo...");

		Connection connection = null;
		CallableStatement callableStatement = null;

		String insertStoreProc = "{call BANK_CREATE_PROC(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try {
			resultMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			bankcode = requestJSON.getString(CevaCommonConstants.bankcode);
			bankname = requestJSON.getString(CevaCommonConstants.bankname);

			bankIp = requestJSON.getString(CevaCommonConstants.bankIp);
			c1 = requestJSON.getString(CevaCommonConstants.c1);

			bankport = requestJSON.getString(CevaCommonConstants.bankport);
			c3 = requestJSON.getString(CevaCommonConstants.c3);

			c2 = requestJSON.getString(CevaCommonConstants.c2);
			acquirerId = requestJSON.getString(CevaCommonConstants.acquirerId);

			zpk = requestJSON.getString(CevaCommonConstants.zpk);

			bin = requestJSON.getString(CevaCommonConstants.bin);
			kcv = requestJSON.getString(CevaCommonConstants.kcv);
			type = requestJSON.getString(CevaCommonConstants.type).trim();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callableStatement = connection.prepareCall(insertStoreProc);

			callableStatement.setString(1, bankcode);
			callableStatement.setString(2, bankname);
			callableStatement.setString(3, bankIp);
			callableStatement.setString(4, c1);
			callableStatement.setString(5, bankport);
			callableStatement.setString(6, c2);
			callableStatement.setString(7, c3);
			callableStatement.setString(8, acquirerId);
			callableStatement.setString(9, zpk);
			callableStatement.setString(10, bin);
			callableStatement.setString(11, kcv);
			callableStatement.setString(12, type);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();

			int resCnt = callableStatement.getInt(14);

			logger.debug("[CavePowerAdminDAO][insertEntityInfo] resultCnt from DB:::"
					+ resCnt);

			if (resCnt == 1) {
				responseDTO.addMessages("Bank Successfully Craeted ");
			} else if (resCnt == -1) {
				resultMap.put(CevaCommonConstants.RESPONSE_JSON, requestJSON);
				responseDTO.setData(resultMap);
				responseDTO.addError("Bank Already Created. ");
			} else {
				resultMap.put(CevaCommonConstants.RESPONSE_JSON, requestJSON);
				responseDTO.setData(resultMap);
				responseDTO.addError("Bank Creation failed.");
			}

		} catch (SQLException e) {
			resultMap.put(CevaCommonConstants.RESPONSE_JSON, requestJSON);
			responseDTO.setData(resultMap);
			responseDTO.addError("Bank Creation failed.");
		} catch (Exception e) {
			resultMap.put(CevaCommonConstants.RESPONSE_JSON, requestJSON);
			responseDTO.setData(resultMap);
			responseDTO.addError("Bank Creation failed.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			resultMap = null;

			bankcode = null;
			bankname = null;
			bankIp = null;
			c1 = null;
			bankport = null;
			c2 = null;
			c3 = null;
			acquirerId = null;
			zpk = null;
			bin = null;
			kcv = null;
			type = null;
		}
		return responseDTO;
	}

	public ResponseDTO getBankSwitchStatus(RequestDTO requestDTO) {

		logger.debug("Inside GetBankSwitchStatus... ");
		HashMap<String, Object> terminalMap = null;
		JSONObject resultJson = null;
		JSONArray switchJSONArray = null;
		Connection connection = null;
		PreparedStatement terminalPstmt = null;
		ResultSet switchRS = null;
		String terminalQry = "select bankname,bankid,status from tbl_bank_info";
		JSONObject json = null;
		try {
			terminalMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			switchJSONArray = new JSONArray();

			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connectionis [" + connection + "]");

			terminalPstmt = connection.prepareStatement(terminalQry);
			switchRS = terminalPstmt.executeQuery();
			json = new JSONObject();

			while (switchRS.next()) {
				json.put("bankname", switchRS.getString(1));
				json.put("bankid", switchRS.getString(2));
				json.put("status", switchRS.getString(3));
				switchJSONArray.add(json);
				json.clear();
			}

			logger.debug("SwitchJSONArray [" + switchJSONArray + "]");

			resultJson.put(CevaCommonConstants.SWITCH_BANK_DATA,
					switchJSONArray);
			terminalMap.put(CevaCommonConstants.SWITCH_BANK_DATA, resultJson);

			logger.debug("TerminalMap [" + terminalMap + "]");
			responseDTO.setData(terminalMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetBankSwitchStatus ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("SQLException in GetBankSwitchStatus ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(switchRS);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closeConnection(connection);
			terminalMap = null;
			resultJson = null;
			switchJSONArray = null;
		}

		return responseDTO;

	}

	public ResponseDTO getBankData(RequestDTO requestDTO) {

		logger.debug("Inside GetBankData... ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;
		String merchantQry = "select acqid,bankName,itmIp,itmPort,key1,key2,key3,nvl(nbin,' '),"
				+ "nvl(zpk,' '),nvl(kcv,' '),bankId from tbl_bank_info  "
				+ "where bankName=? and bankId=? and acqid=?";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			logger.debug("Request JSON [" + requestJSON + "]");

			String bnkname = requestJSON
					.getString(CevaCommonConstants.bankname).trim();
			String bankcode = requestJSON.getString(
					CevaCommonConstants.bankcode).trim();
			String acqid = requestJSON
					.getString(CevaCommonConstants.acquirerId).trim();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, bnkname);
			merchantPstmt.setString(2, bankcode);
			merchantPstmt.setString(3, acqid);

			merchantRS = merchantPstmt.executeQuery();

			logger.debug("After executing query");

			if (merchantRS.next()) {

				logger.debug("Response Data from DB");

				resultJson.put(CevaCommonConstants.acquirerId,
						merchantRS.getString(1));

				resultJson.put(CevaCommonConstants.bankname,
						merchantRS.getString(2));

				resultJson.put(CevaCommonConstants.bankIp,
						merchantRS.getString(3));
				resultJson.put(CevaCommonConstants.bankport,
						merchantRS.getString(4));
				resultJson.put(CevaCommonConstants.c1, merchantRS.getString(5));
				resultJson.put(CevaCommonConstants.c2, merchantRS.getString(6));
				resultJson.put(CevaCommonConstants.c3, merchantRS.getString(7));

				resultJson
						.put(CevaCommonConstants.bin, merchantRS.getString(8));
				resultJson
						.put(CevaCommonConstants.zpk, merchantRS.getString(9));
				resultJson.put(CevaCommonConstants.kcv,
						merchantRS.getString(10));
				resultJson.put(CevaCommonConstants.bankcode,
						merchantRS.getString(11));

				logger.debug("After Setting data");
			}

			logger.debug("Response Json Object [" + resultJson + "]");

			merchantMap.put("BANK_DATA", resultJson);

			responseDTO.setData(merchantMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetBankData [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("SQLException in GetBankData [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			resultJson = null;
		}

		return responseDTO;
	}

}
