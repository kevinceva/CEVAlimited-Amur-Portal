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

public class TellerActionDAO {

	Logger logger = Logger.getLogger(TellerActionDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO cashDepositEnquiry(RequestDTO requestDTO) {
		logger.debug("Inside CashDepositEnquiry... ");

		Connection connection = null;

		PreparedStatement cashdeptPstmt = null;
		ResultSet CashRS = null;
		String cashdepositQry = "Select MERCHANT_ID,MERC_NAME from AGENCY_MERCHANT_MASTER";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			cashdeptPstmt = connection.prepareStatement(cashdepositQry);
			CashRS = cashdeptPstmt.executeQuery();

		} catch (SQLException e2) {
			responseDTO.addError("Internal Error Occured.");
			logger.debug("Excpetion in CashDepositEnquiry [" + e2.getMessage()
					+ "]");
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception in CashDepositEnquiry [" + e.getMessage()
					+ "]");
			responseDTO
					.addError("Internal Error Occured.Please re-check and try again.");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(CashRS);
			DBUtils.closePreparedStatement(cashdeptPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

	public ResponseDTO getDepositeinfo(RequestDTO requestDTO) {

		logger.debug("Inside GetDepositeinfo.. ");

		HashMap<String, Object> BankDataMap = null;
		JSONObject resultJson = null;
		JSONArray banksJSONArray = null;

		PreparedStatement bankPstmt = null;
		Connection connection = null;
		ResultSet banksRS = null;

		String entityQry = "SELECT distinct BANK_CODE,BANK_NAME FROM BANK_MASTER";
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();
			BankDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			banksJSONArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

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

			logger.debug("MerchantDataMap [" + BankDataMap + "]");
			responseDTO.setData(BankDataMap);

		} catch (SQLException e) {
			logger.debug("SQLException in GetDepositeinfo [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
			e.printStackTrace();
		} catch (Exception e) {
			logger.debug("Exception in GetDepositeinfo  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(banksRS);
			DBUtils.closePreparedStatement(bankPstmt);
			DBUtils.closeConnection(connection);

			entityQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO pinEnquiry(RequestDTO requestDTO) {

		logger.debug("Inside PinEnquiry.. ");

		String pin = "";
		PreparedStatement pwdChkPstmt = null;
		ResultSet pwdChkRS = null;
		Connection connection = null;
		int ResCount = 0;
		String userChkQry = "Select count(*) from USER_LOGIN_CREDENTIALS where trim(PIN)=trim(?) and login_user_id=?";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");
			pin = requestJSON.getString(CevaCommonConstants.PIN);

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			pwdChkPstmt = connection.prepareStatement(userChkQry);
			pwdChkPstmt.setString(1, pin);
			pwdChkPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			pwdChkRS = pwdChkPstmt.executeQuery();
			if (pwdChkRS.next()) {
				ResCount = pwdChkRS.getInt(1);
			}

			logger.debug(" The count is [" + ResCount + "]");

			if (ResCount == 1) {
				responseDTO.addMessages("Pin Verification Succes!");

				logger.debug("Service Call done.");
			} else {
				responseDTO.addError("Please Enter Correct PIN Number.");
			}
		} catch (SQLException e) {
			logger.debug("SQLException in PinEnquiry [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in PinEnquiry  [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(pwdChkRS);
			DBUtils.closePreparedStatement(pwdChkPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

	public ResponseDTO insertcashdeposit(RequestDTO requestDTO) {
		logger.debug("Inside Insertcashdeposit... ");
		HashMap<String, Object> txnmap = null;
		JSONObject resultJson = null;
		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertCashDepositInfoProc = "{call CashDepositPkg.InsertCashDeposit(?,?,?,?,?,?,?,?,?,?,?)}";
		int resCnt = 0;
		try {
			txnmap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertCashDepositInfoProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.BANK_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.ACCOUNT_NO));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.PAYEER_NAME));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.AMOUNT));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.MOBILE));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.CASH));
			callableStatement.setString(8,
					requestJSON.getString("CUSTOMER_NAME"));
			callableStatement.setString(9,
					requestJSON.getString("NARRATION"));
			

			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			resCnt = callableStatement.getInt(11);
			String txnno = callableStatement.getString(10);

			logger.debug("The Result Count [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO
						.addMessages("Cash Deposit Stored Successfully. Reference No:"
								+ txnno);

				resultJson.put("TXT_NO", txnno);
				txnmap.put("TXT_NO", resultJson);
				logger.debug("ResultJson [" + resultJson + "]");
				responseDTO.setData(txnmap);

			} else if (resCnt == -1) {
				responseDTO.addError("Cash Deposit Already Exists. ");
			} else {
				responseDTO.addError("Cash Deposit Creation failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in Insertcashdeposit [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in Insertcashdeposit  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			txnmap = null;
			resultJson = null;
		}

		return responseDTO;
	}

	public ResponseDTO statuschange(RequestDTO requestDTO) {
		logger.debug("Inside Statuschange.. ");
		String txtno = "";
		Connection connection = null;
		PreparedStatement pstmt = null;
		String insQry = "UPDATE CASHDEPOSIT_TXN SET STATUS = 'S' WHERE TXN_REF_NO = ?";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			txtno = requestJSON.getString("TXT_NO");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			pstmt = connection.prepareStatement(insQry);
			pstmt.setString(1, txtno);

			int resCnt = pstmt.executeUpdate();
			if (resCnt == 1) {
				responseDTO.addMessages("User status Successfully Updated.");
			} else {
				responseDTO.addError("Updated failed due to some error. ");
			}
		} catch (SQLException e) {
			logger.debug("SQLException in Statuschange [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in Statuschange  [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

	public ResponseDTO getSwitchRelatedData(RequestDTO requestDTO) {

		logger.debug("Inside GetSwitchRelatedData.. ");

		HashMap<String, Object> UserDataMap = null;

		PreparedStatement userPstmt = null;
		PreparedStatement unamePstmt = null;
		PreparedStatement binPstmt = null;

		ResultSet binRS = null;
		ResultSet merchantRS = null;
		ResultSet unameRS = null;

		Connection connection = null;

		String UName = "";
		String location = "";
		String loginId = "";
		String bankId = "";
		String username = "";
		String User_infQry = "Select LOCATION from USER_INFORMATION where COMMON_ID in (SELECT COMMON_ID from USER_LOGIN_CREDENTIALS where LOGIN_USER_ID=?)";
		try {
			UserDataMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("RequestJSON [" + requestJSON + "]");
			loginId = requestJSON.getString(CevaCommonConstants.MAKER_ID);
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			userPstmt = connection.prepareStatement(User_infQry);
			userPstmt.setString(1, loginId);
			merchantRS = userPstmt.executeQuery();
			while (merchantRS.next()) {
				location = merchantRS.getString(1);
			}
			responseJSON.put(CevaCommonConstants.LOC_CODE, location);

			bankId = requestJSON.getString(CevaCommonConstants.BANK_ID);
			username = requestJSON.getString(CevaCommonConstants.MAKER_ID);

			User_infQry = "SELECT UI.USER_NAME FROM USER_INFORMATION UI,USER_LOGIN_CREDENTIALS ULC "
					+ "WHERE UI.COMMON_ID = ULC.COMMON_ID AND ULC.LOGIN_USER_ID=?";

			unamePstmt = connection.prepareStatement(User_infQry);
			unamePstmt.setString(1, username);

			unameRS = unamePstmt.executeQuery();

			if (unameRS.next()) {
				UName = unameRS.getString(1);
			}

			responseJSON.put("User_Name", UName);

			logger.debug("Response JSON before bin :: " + responseJSON);

			String bankQry = "Select BIN from BANK_MASTER where BANK_CODE=? and rownum<2";
			String bin = null;
			binPstmt = connection.prepareStatement(bankQry);
			binPstmt.setString(1, bankId);
			binRS = binPstmt.executeQuery();

			if (binRS.next()) {
				bin = binRS.getString(1);
			}

			responseJSON.put("BIN", bin);
			logger.debug("ResponseJSON  After Bin [" + responseJSON + "]");
			UserDataMap
					.put(CevaCommonConstants.ACCOUNTDET_CLIENT, responseJSON);
			responseDTO.setData(UserDataMap);
		} catch (SQLException e) {
			logger.debug("SQLException in GetSwitchRelatedData ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetSwitchRelatedData  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			DBUtils.closeResultSet(unameRS);
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeResultSet(binRS);

			DBUtils.closePreparedStatement(binPstmt);
			DBUtils.closePreparedStatement(unamePstmt);
			DBUtils.closePreparedStatement(userPstmt);

			DBUtils.closeConnection(connection);
			UName = null;
			location = null;
			loginId = null;
			bankId = null;
			username = null;
		}

		return responseDTO;
	}

	public ResponseDTO orgCashDepositAck(RequestDTO requestDTO) {
		logger.debug("Inside OrgCashDepositAck ... ");
		HashMap<String, Object> txnmap = null;
		JSONObject resultJson = null;
		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertCashDepositInfoProc = "{call pPwalletCashDeposit(?,?,?,?,?,?,?,?,?)}";
		int resCnt = 0;
		try {
			txnmap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertCashDepositInfoProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON.getString("orgId"));
			callableStatement.setString(3, requestJSON.getString("orgName"));
			callableStatement
					.setString(4, requestJSON.getString("paymentmode"));
			callableStatement.setString(5, requestJSON.getString("amount"));
			callableStatement.setString(6, requestJSON.getString("mobile"));
			callableStatement.setString(7, requestJSON.getString("payeername"));

			callableStatement.registerOutParameter(8, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();

			resCnt = callableStatement.getInt(8);
			String txnno = callableStatement.getString(9);

			logger.debug("The Result Count [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO.addMessages(txnno);

				resultJson.put("TXT_NO", txnno);
				txnmap.put("TXT_NO", resultJson);
				logger.debug("ResultJson [" + resultJson + "]");
				responseDTO.setData(txnmap);
			} else {
				responseDTO.addError("Organization Cash Deposit failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in orgCashDepositAck [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in orgCashDepositAck  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			txnmap = null;
			resultJson = null;
		}

		return responseDTO;
	}

	public ResponseDTO profileCashDepositAck(RequestDTO requestDTO) {
		logger.debug("Inside Profile CashDepositAck ... ");
		HashMap<String, Object> txnmap = null;
		JSONObject resultJson = null;
		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertCashDepositInfoProc = "{call pProfilewalletCashDeposit(?,?,?,?,?,?,?,?,?)}";
		int resCnt = 0;
		try {
			txnmap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertCashDepositInfoProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON.getString("profileid"));
			callableStatement.setString(3, requestJSON.getString("fullName"));
			callableStatement
					.setString(4, requestJSON.getString("paymentmode"));
			callableStatement.setString(5, requestJSON.getString("amount"));
			callableStatement.setString(6, requestJSON.getString("mobile"));
			callableStatement.setString(7, requestJSON.getString("payeername"));

			callableStatement.registerOutParameter(8, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();

			resCnt = callableStatement.getInt(8);
			String txnno = callableStatement.getString(9);

			logger.debug("The Result Count [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO.addMessages(txnno);

				resultJson.put("TXT_NO", txnno);
				txnmap.put("TXT_NO", resultJson);
				logger.debug("ResultJson [" + resultJson + "]");
				responseDTO.setData(txnmap);
			} else {
				responseDTO.addError("Profile with id '"
						+ requestJSON.getString("profileid")
						+ "' deposit got failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in Profile CashDepositAck ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in Profile CashDepositAck  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			txnmap = null;
			resultJson = null;
		}

		return responseDTO;
	}

}
