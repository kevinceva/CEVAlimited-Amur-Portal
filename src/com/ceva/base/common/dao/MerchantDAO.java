package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.base.common.utils.EncryptTransactionPin;
import com.ceva.util.DBUtils;

public class MerchantDAO {

	private Logger logger = Logger.getLogger(MerchantDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getMerchantDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside GetMerchantDetails.. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;

		JSONArray merchantJsonArray = null;

		ArrayList<String> merchatArray = null;
		ArrayList<String> storeArray = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "Select MERCHANT_ID,MERCHANT_NAME,"
				+ "Decode(STATUS,'A','Active','B','Inactive','N','Un-Authorize'),"
				+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
				+ "from MERCHANT_MASTER order by MERCHANT_ID";
		try {

			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			terminalJSON = new JSONObject();

			merchatArray = new ArrayList<String>();
			storeArray = new ArrayList<String>();

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			json = new JSONObject();
			while (merchantRS.next()) {
				json.put(CevaCommonConstants.MERCHANT_ID,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.MERCHANT_NAME,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.STATUS, merchantRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(4));
				merchatArray.add(merchantRS.getString(1));
				merchantJsonArray.add(json);
				json.clear();
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			resultJson
					.put(CevaCommonConstants.MERCHANT_LIST, merchantJsonArray);

			merchantJsonArray.clear();

			for (int i = 0; i < merchatArray.size(); i++) {
				String storeQry = "Select SM.STORE_ID,SM.STORE_NAME,MM.MERCHANT_ID,MM.MERCHANT_NAME,"
						+ "Decode(SM.STATUS,'A','Active','B','Inactive','N','Un-Authorize'),to_char(SM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
						+ " from MERCHANT_MASTER MM,STORE_MASTER SM where SM.MERCHANT_ID=MM.MERCHANT_ID and MM.MERCHANT_ID=?  order by SM.STORE_ID";

				merchantPstmt = connection.prepareStatement(storeQry);
				merchantPstmt.setString(1, merchatArray.get(i));
				merchantRS = merchantPstmt.executeQuery();
				json.clear();

				while (merchantRS.next()) {
					json.put(CevaCommonConstants.STORE_ID,
							merchantRS.getString(1));
					json.put(CevaCommonConstants.STORE_NAME,
							merchantRS.getString(2));
					json.put(CevaCommonConstants.MERCHANT_ID,
							merchantRS.getString(3));
					json.put(CevaCommonConstants.MERCHANT_NAME,
							merchantRS.getString(4));
					json.put(CevaCommonConstants.STATUS,
							merchantRS.getString(5));
					json.put(CevaCommonConstants.MAKER_DATE,
							merchantRS.getString(6));
					storeArray.add(merchantRS.getString(1));
					merchantJsonArray.add(json);
					json.clear();
				}

				if (merchatArray != null && merchatArray.size() > 0) {
					resultJson.put(merchatArray.get(i) + "_STORES",
							merchantJsonArray);
					merchantJsonArray.clear();
				}

				DBUtils.closeResultSet(merchantRS);
				DBUtils.closePreparedStatement(merchantPstmt);
			}

			json.clear();
			String terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,Decode(STATUS,'A','Active','B','Inactive','D','Deactive'),"
					+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
					+ "SERIAL_NO from TERMINAL_MASTER  where trim(STORE_ID)=trim(?)";

			for (int i = 0; i < storeArray.size(); i++) {

				merchantPstmt = connection.prepareStatement(terminalQry);
				merchantPstmt.setString(1, storeArray.get(i));
				merchantRS = merchantPstmt.executeQuery();

				while (merchantRS.next()) {
					json.put(CevaCommonConstants.TERMINAL_ID,
							merchantRS.getString(1));
					json.put(CevaCommonConstants.STORE_ID,
							merchantRS.getString(2));
					json.put(CevaCommonConstants.MERCHANT_ID,
							merchantRS.getString(3));
					json.put(CevaCommonConstants.STATUS,
							merchantRS.getString(4));
					json.put(CevaCommonConstants.MAKER_DATE,
							merchantRS.getString(5));
					json.put("serialNo", merchantRS.getString(6));
					merchantJsonArray.add(json);
					json.clear();
				}

				DBUtils.closeResultSet(merchantRS);
				DBUtils.closePreparedStatement(merchantPstmt);
				
				if (merchatArray != null && merchatArray.size() > 0) {
					terminalJSON.put(storeArray.get(i) + "_TERMINALS",
							merchantJsonArray);
					merchantJsonArray.clear();
				}

			}
			merchantMap.put(CevaCommonConstants.MERCHANT_LIST, resultJson);
			merchantMap.put(CevaCommonConstants.TERMINAL_DATA, terminalJSON);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);

		} catch (Exception e) {
			logger.debug("Got Exception in GetMerchantDetails ["
					+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			merchantJsonArray = null;

			terminalJSON = null;

			merchatArray = null;
			storeArray = null;
		}
		return responseDTO;
	}

	public ResponseDTO getMerchantCreatePageInfo(RequestDTO requestDTO) {

		logger.debug("Inside GetMerchantCreatePageInfo.. ");
		HashMap<String, Object> merchantDataMap = null;
		JSONObject resultJson = null;
		PreparedStatement getMerchantPstmt = null;
		ResultSet getMerchantRs = null;
		Connection connection = null;

		String entityQry = "Select OFFICE_CODE,OFFICE_NAME||'~'||OFFICE_CODE from CEVA_BRANCH_MASTER where HPO_FLAG is null order by OFFICE_NAME";
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();

			merchantDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("connection is [" + connection + "]");
			getMerchantPstmt = connection.prepareStatement(entityQry);

			getMerchantRs = getMerchantPstmt.executeQuery();
			json = new JSONObject();

			while (getMerchantRs.next()) {
				json.put(getMerchantRs.getString(1), getMerchantRs.getString(2));
			}

			resultJson.put(CevaCommonConstants.LOCATION_LIST, json);

			json.clear();

			DBUtils.closeResultSet(getMerchantRs);
			DBUtils.closePreparedStatement(getMerchantPstmt);

			String acctCatQry = "Select KEY_VALUE from CONFIG_DATA where KEY_GROUP=? and KEY_TYPE=? order by KEY_VALUE";
			getMerchantPstmt = connection.prepareStatement(acctCatQry);
			getMerchantPstmt.setString(1, "AGENCY");
			getMerchantPstmt.setString(2, "AGENCY_ACCT_CAT");

			getMerchantRs = getMerchantPstmt.executeQuery();
			while (getMerchantRs.next()) {
				json.put(getMerchantRs.getString(1), getMerchantRs.getString(1));
			}
			resultJson.put(CevaCommonConstants.ACCOUNT_CATEGORYS, json);

			json.clear();

			DBUtils.closeResultSet(getMerchantRs);
			DBUtils.closePreparedStatement(getMerchantPstmt);

			String transferCodeQry = "Select KEY_VALUE from CONFIG_DATA where KEY_GROUP=? and KEY_TYPE=?";
			getMerchantPstmt = connection.prepareStatement(transferCodeQry);
			getMerchantPstmt.setString(1, "AGENCY");
			getMerchantPstmt.setString(2, "AGENCY_TRANSFER_CODE");

			getMerchantRs = getMerchantPstmt.executeQuery();
			while (getMerchantRs.next()) {
				json.put(getMerchantRs.getString(1), getMerchantRs.getString(1));
			}

			resultJson.put(CevaCommonConstants.TRANSFER_CODES, json);

			json.clear();

			DBUtils.closeResultSet(getMerchantRs);
			DBUtils.closePreparedStatement(getMerchantPstmt);

			String merchantTypeQry = "Select CATE_CODE,CATE_CODE||'~'||CATE_DESCRIPTION from CATEGORY_MASTER order by CATE_DESCRIPTION";
			getMerchantPstmt = connection.prepareStatement(merchantTypeQry);

			getMerchantRs = getMerchantPstmt.executeQuery();
			while (getMerchantRs.next()) {
				json.put(getMerchantRs.getString(1), getMerchantRs.getString(2));
			}
			resultJson.put(CevaCommonConstants.MERCHANT_TYPE, json);

			merchantDataMap.put(CevaCommonConstants.MERCHANT_INFO, resultJson);

			logger.debug("MerchantDataMap [" + merchantDataMap + "]");

			responseDTO.setData(merchantDataMap);

			DBUtils.closeResultSet(getMerchantRs);
			DBUtils.closePreparedStatement(getMerchantPstmt);

			
			merchantTypeQry = null;
			transferCodeQry = null;
			acctCatQry = null;
		} catch (Exception e) {
			logger.debug("Got Exception in GetMerchantCreatePageInfo ["
					+ e.getMessage() + "]");
		} finally {
			try {

				DBUtils.closeResultSet(getMerchantRs);
				DBUtils.closePreparedStatement(getMerchantPstmt);
				DBUtils.closeConnection(connection);
			} catch (Exception e) {
				e.printStackTrace();
			}

			merchantDataMap = null;
			resultJson = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertMerchantDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertMerchantDetails.. ");

		String address = null;
		String telephone = null;
		String addressLine1 = null;
		String addressLine2 = null;
		String addressLine3 = null;
		String telephoneNumber1 = null;
		String telephoneNumber2 = null;

		String documentMultiData = "";
		String agentMultiData = "";
		String tillId = "";

		CallableStatement callableStatement = null;
		String insertMerchantDetailsProc = "{call MerchantMgmtPkg.MerchantDetailsInsertProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String Msg = "";
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			addressLine1 = requestJSON.getString(CevaCommonConstants.ADDRESS1);
			addressLine2 = requestJSON.getString(CevaCommonConstants.ADDRESS2);
			if (requestJSON.containsKey(CevaCommonConstants.ADDRESS3)) {
				addressLine3 = requestJSON
						.getString(CevaCommonConstants.ADDRESS3);
			} else {
				addressLine3 = "";
			}

			telephoneNumber1 = requestJSON
					.getString(CevaCommonConstants.TELEPHONE1);
			if (requestJSON.containsKey(CevaCommonConstants.TELEPHONE2)) {
				telephoneNumber2 = requestJSON
						.getString(CevaCommonConstants.TELEPHONE2);
			} else {
				telephoneNumber2 = "";
			}

			address = addressLine1 + "#" + addressLine2 + "#" + addressLine3;
			telephone = telephoneNumber1 + "#" + telephoneNumber2;

			if (requestJSON
					.containsKey(CevaCommonConstants.DOCUMENT_MULTI_DATA)) {
				documentMultiData = requestJSON
						.getString(CevaCommonConstants.DOCUMENT_MULTI_DATA);
			}
			if (requestJSON.containsKey(CevaCommonConstants.AGENT_MULTI_DATA)) {
				agentMultiData = requestJSON
						.getString(CevaCommonConstants.AGENT_MULTI_DATA);
			}

			tillId = getRandomInteger();

			callableStatement = connection
					.prepareCall(insertMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.MERCHANT_NAME));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.STORE_NAME));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.LOCATION_NAME));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.KRA_PIN));
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.MERCHANT_TYPE));
			callableStatement.setString(9,
					requestJSON.getString(CevaCommonConstants.MEMBER_TYPE));
			callableStatement.setString(10,
					requestJSON.getString(CevaCommonConstants.MANAGER_NAME));
			callableStatement.setString(11,
					requestJSON.getString(CevaCommonConstants.EMAIL_ID));
			callableStatement.setString(12, address);
			callableStatement.setString(13,
					requestJSON.getString(CevaCommonConstants.CITY));
			callableStatement.setString(14,
					requestJSON.getString(CevaCommonConstants.POBOXNUMBER));
			callableStatement.setString(15, telephone);
			callableStatement.setString(16,
					requestJSON.getString(CevaCommonConstants.MOBILE_NUMBER));
			callableStatement.setString(17,
					requestJSON.getString(CevaCommonConstants.FAX_NUMBER));
			callableStatement.setString(18, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NAME));
			callableStatement.setString(19, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NUMBER));
			callableStatement.setString(20, requestJSON
					.getString(CevaCommonConstants.BANK_ACCT_MULTI_DATA));
			callableStatement.setString(21, documentMultiData);
			callableStatement.setString(22, agentMultiData);
			callableStatement.setString(23, tillId);

			callableStatement.registerOutParameter(24, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(25, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(24);
			Msg = callableStatement.getString(25);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			logger.debug("Msg [" + Msg + "]");

			responseDTO = getMerchantDetails(requestDTO);

			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in insertMerchantDetails ["
					+ e.getMessage() + "]");
		}

		finally {

			try {
				DBUtils.closeCallableStatement(callableStatement);
				DBUtils.closeConnection(connection);
			} catch (Exception e) {

			}
			insertMerchantDetailsProc = null;
			Msg = null;
			address = null;
			telephone = null;
			addressLine1 = null;
			addressLine2 = null;
			addressLine3 = null;
			telephoneNumber1 = null;
			telephoneNumber2 = null;
			insertMerchantDetailsProc = null;
			documentMultiData = null;
			agentMultiData = null;
			tillId = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreCreatePageInfo(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreCreatePageInfo.. ");
		HashMap<String, Object> storeDataMap = null;
		JSONObject resultJson = null;

		PreparedStatement getStorePstmt = null;
		ResultSet getStoreRs = null;

		Connection connection = null;
		String entityQry = "Select OFFICE_CODE,OFFICE_NAME||'~'||OFFICE_CODE from CEVA_BRANCH_MASTER where HPO_FLAG is null order by OFFICE_NAME";
		JSONObject json = null;
		try {
			storeDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			json = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			getStorePstmt = connection.prepareStatement(entityQry);

			getStoreRs = getStorePstmt.executeQuery();

			while (getStoreRs.next()) {
				json.put(getStoreRs.getString(1), getStoreRs.getString(2));
			}

			resultJson.put(CevaCommonConstants.LOCATION_LIST, json);

			json.clear();

			DBUtils.closeResultSet(getStoreRs);
			DBUtils.closePreparedStatement(getStorePstmt);
			

			String storeIdQry = "Select count(*) from STORE_MASTER where STORE_ID in (select distinct store_id from STORE_MASTER where MERCHANT_ID=?)";

			getStorePstmt = connection.prepareStatement(storeIdQry);
			getStorePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			getStoreRs = getStorePstmt.executeQuery();
			int storeId = 0;

			if (getStoreRs.next()) {
				storeId = Integer.parseInt(getStoreRs.getString(1));
				logger.debug("StoreId f [" + storeId + "]");
			}

			storeId++;
			resultJson.put("storeId", storeId);

			logger.debug("StoreId [" + storeId + "]");


			DBUtils.closeResultSet(getStoreRs);
			DBUtils.closePreparedStatement(getStorePstmt);
			

			String merchantPrmQry = "Select MERCHANT_ID,MERCHANT_NAME,LOCATION,KRA_PIN,MERCHANT_TYPE,MANAGER_NAME,EMAIL,"
					+ "ADDRESS,CITY,PO_BOX_NO,TELEPHONE_NO,MOBILE_NO,FAX_NO,PRI_CONTACT_NAME,PRI_CONTACT_NO,AGEN_OR_BILLER "
					+ "from MERCHANT_MASTER where MERCHANT_ID=?";

			getStorePstmt = connection.prepareStatement(merchantPrmQry);
			getStorePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			getStoreRs = getStorePstmt.executeQuery();
			while (getStoreRs.next()) {
				resultJson.put(CevaCommonConstants.MERCHANT_ID,
						getStoreRs.getString(1));
				resultJson.put(CevaCommonConstants.MERCHANT_NAME,
						getStoreRs.getString(2));
				resultJson.put(CevaCommonConstants.KRA_PIN,
						getStoreRs.getString(4));
				resultJson.put(CevaCommonConstants.MERCHANT_TYPE,
						getStoreRs.getString(5));
				resultJson.put(CevaCommonConstants.MANAGER_NAME,
						getStoreRs.getString(6));
				resultJson.put(CevaCommonConstants.EMAIL,
						getStoreRs.getString(7));

				String address = getStoreRs.getString(8);
				String[] addressVal = address.split("#");
				String address1 = "";
				String address2 = "";
				String address3 = "";
				if (addressVal.length == 1) {
					address1 = addressVal[0];
				}
				if (addressVal.length == 2) {
					address1 = addressVal[0];
					address2 = addressVal[1];
				}
				if (addressVal.length == 3) {
					address1 = addressVal[0];
					address2 = addressVal[1];
					address3 = addressVal[2];
				}

				String telphone = getStoreRs.getString(11);
				String[] telPhoneArr = telphone.split("#");
				String telephone1 = "";
				String telephone2 = "";
				if (telPhoneArr.length == 1)
					telephone1 = telPhoneArr[0];
				if (telPhoneArr.length == 2) {
					telephone1 = telPhoneArr[0];
					telephone2 = telPhoneArr[1];
				}

				resultJson.put(CevaCommonConstants.ADDRESS1, address1);
				resultJson.put(CevaCommonConstants.ADDRESS2, address2);
				resultJson.put(CevaCommonConstants.ADDRESS3, address3);
				resultJson.put(CevaCommonConstants.CITY,
						getStoreRs.getString(9));
				resultJson.put(CevaCommonConstants.POBOXNUMBER,
						getStoreRs.getString(10));
				resultJson.put(CevaCommonConstants.TELEPHONE1, telephone1);
				resultJson.put(CevaCommonConstants.TELEPHONE2, telephone2);
				resultJson.put(CevaCommonConstants.MOBILE_NUMBER,
						getStoreRs.getString(12));
				resultJson.put(CevaCommonConstants.FAX_NUMBER,
						getStoreRs.getString(13));
				resultJson.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						getStoreRs.getString(14));
				resultJson.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						getStoreRs.getString(15));
				resultJson.put(CevaCommonConstants.MEMBER_TYPE,
						getStoreRs.getString(16));

				telphone = null;
				telPhoneArr = null;
				telephone1 = null;
				telephone2 = null;
				address = null;
				addressVal = null;
				address1 = null;
				address2 = null;
				address3 = null;
			}

			DBUtils.closeResultSet(getStoreRs);
			DBUtils.closePreparedStatement(getStorePstmt);

			String firstStoreId = requestJSON.getString(
					CevaCommonConstants.MERCHANT_ID).substring(0, 4);
			firstStoreId = firstStoreId + "-S001";

			String merchantBankAcctQry = "SELECT distinct ACCT_NO,ACCT_DESC,BANK_NAME,BANK_BRANCH,TRANSFER_CODE "
					+ "from BANK_ACCT_MASTER where MERCHANT_ID=? and STORE_ID=?";
			getStorePstmt = connection.prepareStatement(merchantBankAcctQry);
			getStorePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			getStorePstmt.setString(2, firstStoreId);

			getStoreRs = getStorePstmt.executeQuery();
			String bankAcctData = "";
			String eachrow = "";
			int i = 0;
			while (getStoreRs.next()) {

				eachrow = getStoreRs.getString(1) + ","
						+ getStoreRs.getString(2) + ","
						+ getStoreRs.getString(3) + ","
						+ getStoreRs.getString(4) + ","
						+ getStoreRs.getString(5);

				if (i == 0) {
					bankAcctData += eachrow;
				} else {
					bankAcctData += "#" + eachrow;
				}
				i++;
			}
			resultJson.put("bankAcctMultiData", bankAcctData);

			DBUtils.closeResultSet(getStoreRs);
			DBUtils.closePreparedStatement(getStorePstmt);

			String merchantAgentAcctQry = "SELECT distinct BANK_AGENT_NO,MPESA_AGENT_NO,AIRTEL_AGENT_NO,ORANGE_AGENT_NO,MPESA_TILL_NO from AGENT_INFORMATION where MERCHANT_ID=? and STORE_ID=?";
			getStorePstmt = connection.prepareStatement(merchantAgentAcctQry);
			getStorePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			getStorePstmt.setString(2, firstStoreId);

			getStoreRs = getStorePstmt.executeQuery();
			String agentData = "";
			eachrow = "";
			i = 0;
			while (getStoreRs.next()) {
				eachrow = getStoreRs.getString(1) + ","
						+ getStoreRs.getString(2) + ","
						+ getStoreRs.getString(3) + ","
						+ getStoreRs.getString(4) + ","
						+ getStoreRs.getString(5);
				// agentData = agentData + "#" + eachrow;
				if (i == 0) {
					agentData += eachrow;
				} else {
					agentData += "#" + eachrow;
				}

				i++;
			}
			resultJson.put("AgenctAcctMultiData", agentData);

			DBUtils.closeResultSet(getStoreRs);
			DBUtils.closePreparedStatement(getStorePstmt);

			String merchantDocumentQry = "SELECT distinct DOC_ID,DOC_DESC,GRACE_PERIOD,MANDATORY "
					+ "from DOCUMENT_DETAILS where MERCHANT_ID=? and STORE_ID=?";
			getStorePstmt = connection.prepareStatement(merchantDocumentQry);
			getStorePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			getStorePstmt.setString(2, firstStoreId);

			getStoreRs = getStorePstmt.executeQuery();
			String documentData = "";
			eachrow = "";
			i = 0;
			while (getStoreRs.next()) {
				eachrow = getStoreRs.getString(1) + ","
						+ getStoreRs.getString(2) + ","
						+ getStoreRs.getString(3) + ","
						+ getStoreRs.getString(4);

				if (i == 0) {
					documentData += eachrow;
				} else {
					documentData += "#" + eachrow;
				}

				i++;
			}

			resultJson.put("documentMultiData", documentData);

			storeDataMap.put(CevaCommonConstants.STORE_INFO, resultJson);

			logger.debug("StoreDataMap [" + storeDataMap + "]");

			responseDTO.setData(storeDataMap);

			documentData = null;
			eachrow = null;
			merchantDocumentQry = null;
			agentData = null;
			merchantAgentAcctQry = null;
			bankAcctData = null;
			eachrow = null;
			firstStoreId = null;

		} catch (Exception e) {
			logger.debug("Exception in GetStoreCreatePageInfo ["
					+ e.getMessage() + "]");
		} finally {
			try {
				DBUtils.closeResultSet(getStoreRs);
				DBUtils.closePreparedStatement(getStorePstmt);
				DBUtils.closeConnection(connection);

			} catch (Exception e) {

			}
			entityQry = null;
			storeDataMap = null;
			resultJson = null;

		}
		return responseDTO;
	}

	public ResponseDTO insertStoreDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertStoreDetails.. ");

		String address = null;
		String telephone = null;
		String addressLine1 = null;
		String addressLine2 = null;
		String addressLine3 = null;
		String telephoneNumber1 = null;
		String telephoneNumber2 = null;
		String documentMultiData = "";
		String agentMultiData = "";
		CallableStatement callableStatement = null;
		String insertMerchantDetailsProc = "{call MerchantMgmtPkg.StoreDetailsInsertProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			addressLine1 = requestJSON.getString(CevaCommonConstants.ADDRESS1);
			addressLine2 = requestJSON.getString(CevaCommonConstants.ADDRESS2);
			if (requestJSON.containsKey(CevaCommonConstants.ADDRESS3)) {
				addressLine3 = requestJSON
						.getString(CevaCommonConstants.ADDRESS3);
			} else {
				addressLine3 = "";
			}

			telephoneNumber1 = requestJSON
					.getString(CevaCommonConstants.TELEPHONE1);
			if (requestJSON.containsKey(CevaCommonConstants.TELEPHONE2)) {
				telephoneNumber2 = requestJSON
						.getString(CevaCommonConstants.TELEPHONE2);
			} else {
				telephoneNumber2 = "";
			}

			address = addressLine1 + "#" + addressLine2 + "#" + addressLine3;
			telephone = telephoneNumber1 + "#" + telephoneNumber2;

			if (requestJSON
					.containsKey(CevaCommonConstants.DOCUMENT_MULTI_DATA)) {
				documentMultiData = requestJSON
						.getString(CevaCommonConstants.DOCUMENT_MULTI_DATA);
			}
			if (requestJSON.containsKey(CevaCommonConstants.AGENT_MULTI_DATA)) {
				agentMultiData = requestJSON
						.getString(CevaCommonConstants.AGENT_MULTI_DATA);
			}

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.MERCHANT_NAME));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.STORE_NAME));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.LOCATION_NAME));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.KRA_PIN));
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.MANAGER_NAME));
			callableStatement.setString(9,
					requestJSON.getString(CevaCommonConstants.EMAIL_ID));
			callableStatement.setString(10, address);
			callableStatement.setString(11,
					requestJSON.getString(CevaCommonConstants.CITY));
			callableStatement.setString(12,
					requestJSON.getString(CevaCommonConstants.POBOXNUMBER));
			callableStatement.setString(13, telephone);
			callableStatement.setString(14,
					requestJSON.getString(CevaCommonConstants.MOBILE_NUMBER));
			callableStatement.setString(15,
					requestJSON.getString(CevaCommonConstants.FAX_NUMBER));
			callableStatement.setString(16, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NAME));
			callableStatement.setString(17, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NUMBER));
			callableStatement.setString(18,
					requestJSON.getString(CevaCommonConstants.TILL_ID));
			callableStatement.setString(19, requestJSON
					.getString(CevaCommonConstants.BANK_ACCT_MULTI_DATA));
			callableStatement.setString(20, documentMultiData);
			callableStatement.setString(21, agentMultiData);
			callableStatement.registerOutParameter(22, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(23, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(22);
			String Msg = callableStatement.getString(23);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in InsertStoreDetails is ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			address = null;
			telephone = null;
			addressLine1 = null;
			addressLine2 = null;
			addressLine3 = null;
			telephoneNumber1 = null;
			telephoneNumber2 = null;
			documentMultiData = null;
			agentMultiData = null;
			insertMerchantDetailsProc = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertTerminalDetails(RequestDTO requestDTO) {

		logger.debug("Inside InsertTerminalDetails.. ");

		String tmkKey = "";
		String encTmkKey = "";

		CallableStatement callableStatement = null;
		Connection connection = null;
		String insertMerchantDetailsProc = "{call MerchantMgmtPkg.TerminalDetailsInsertProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			tmkKey = requestJSON.getString(CevaCommonConstants.TPK_KEY);

			try {
				encTmkKey = EncryptTransactionPin.encrypt(
						"97206B46CE46376894703ECE161F31F2", tmkKey, 'F');
			} catch (Exception e) {
				logger.debug(" Exception is ::: " + e.getMessage());
				encTmkKey = "";
			}

			logger.debug(" TmkKey[" + tmkKey + "] EncTmkKey[" + encTmkKey + "]");

			callableStatement = connection
					.prepareCall(insertMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.TERMINAL_USAGE));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.TERMINAL_MAKE));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.MODEL_NO));
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.SERIAL_NO));
			callableStatement.setString(9,
					requestJSON.getString(CevaCommonConstants.VALID_FROM));
			callableStatement.setString(10,
					requestJSON.getString(CevaCommonConstants.VALID_THRU));
			callableStatement.setString(11,
					requestJSON.getString(CevaCommonConstants.STATUS));
			callableStatement.setString(12,
					requestJSON.getString(CevaCommonConstants.TERMINAL_DATE));
			callableStatement.setString(13,
					requestJSON.getString(CevaCommonConstants.TMK_INDEX));
			callableStatement.setString(14, encTmkKey);
			callableStatement.registerOutParameter(15, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(15);
			String Msg = callableStatement.getString(16);

			logger.debug("ResultCnt from DB [" + resCnt + "]");

			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in InsertTerminalDetails is ["
					+ e.getMessage() + "]");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			
			insertMerchantDetailsProc = null;
			tmkKey = null;
			encTmkKey = null;
		}
		return responseDTO;
	}

	public ResponseDTO getMerchantViewDetails(RequestDTO requestDTO) {
		logger.debug("Inside GetMerchantViewDetails... ");
		PreparedStatement merchantprmPstmt = null;
		ResultSet storePrmRS = null;
		Connection connection = null;

		HashMap<String, Object> merchantDataMap = null;

		String merchantPrmQry = "Select MM.MERCHANT_ID,MM.MERCHANT_NAME,(select office_name from ceva_branch_master where office_code=MM.LOCATION), "
				+ "MM.KRA_PIN,(select CATE_DESCRIPTION from CATEGORY_MASTER where cate_code=MM.MERCHANT_TYPE),"
				+ " MM.MANAGER_NAME,MM.EMAIL,MM.ADDRESS,MM.CITY,MM.PO_BOX_NO,MM.TELEPHONE_NO,MM.MOBILE_NO,MM.FAX_NO,MM.PRI_CONTACT_NAME, "
				+ "MM.PRI_CONTACT_NO,MM.AGEN_OR_BILLER from MERCHANT_MASTER MM where MM.MERCHANT_ID=?";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			merchantDataMap = new HashMap<String, Object>();
			merchantprmPstmt = connection.prepareStatement(merchantPrmQry);
			merchantprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			storePrmRS = merchantprmPstmt.executeQuery();
			while (storePrmRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						storePrmRS.getString(1));
				responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
						storePrmRS.getString(2));
				responseJSON.put(CevaCommonConstants.LOCATION_NAME,
						storePrmRS.getString(3));
				responseJSON.put(CevaCommonConstants.KRA_PIN,
						storePrmRS.getString(4));
				responseJSON.put(CevaCommonConstants.MERCHANT_TYPE,
						storePrmRS.getString(5));
				responseJSON.put(CevaCommonConstants.MANAGER_NAME,
						storePrmRS.getString(6));
				responseJSON.put(CevaCommonConstants.EMAIL,
						storePrmRS.getString(7));

				String address = storePrmRS.getString(8);
				String[] addressVal = address.split("#");
				String address1 = "";
				String address2 = "";
				String address3 = "";

				if (addressVal.length == 1) {
					address1 = addressVal[0];
				}
				if (addressVal.length == 2) {
					address1 = addressVal[0];
					address2 = addressVal[1];
				}
				if (addressVal.length == 3) {
					address1 = addressVal[0];
					address2 = addressVal[1];
					address3 = addressVal[2];
				}

				String telphone = storePrmRS.getString(11);
				String[] telPhoneArr = telphone.split("#");
				String telephone1 = "";
				String telephone2 = "";
				if (telPhoneArr.length == 1)
					telephone1 = telPhoneArr[0];
				if (telPhoneArr.length == 2) {
					telephone1 = telPhoneArr[0];
					telephone2 = telPhoneArr[1];
				}

				responseJSON.put(CevaCommonConstants.ADDRESS1, address1);
				responseJSON.put(CevaCommonConstants.ADDRESS2, address2);
				responseJSON.put(CevaCommonConstants.ADDRESS3, address3);
				responseJSON.put(CevaCommonConstants.CITY,
						storePrmRS.getString(9));
				responseJSON.put(CevaCommonConstants.POBOXNUMBER,
						storePrmRS.getString(10));
				responseJSON.put(CevaCommonConstants.TELEPHONE1, telephone1);
				responseJSON.put(CevaCommonConstants.TELEPHONE2, telephone2);
				responseJSON.put(CevaCommonConstants.MOBILE_NUMBER,
						storePrmRS.getString(12));
				responseJSON.put(CevaCommonConstants.FAX_NUMBER,
						storePrmRS.getString(13));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						storePrmRS.getString(14));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						storePrmRS.getString(15));
				responseJSON.put(CevaCommonConstants.MEMBER_TYPE,
						storePrmRS.getString(16));

				address = null;
				addressVal = null;
				address1 = null;
				address2 = null;
				address3 = null;
				telphone = null;
				telPhoneArr = null;
				telephone1 = null;
				telephone2 = null;
			}

			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);

			String merchantBankAcctQry = "SELECT distinct ACCT_NO,ACCT_DESC,BANK_NAME,BANK_BRANCH,TRANSFER_CODE from BANK_ACCT_MASTER where MERCHANT_ID=?";
			merchantprmPstmt = connection.prepareStatement(merchantBankAcctQry);
			merchantprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			storePrmRS = merchantprmPstmt.executeQuery();
			String bankAcctData = "";
			String eachrow = "";
			int i = 0;
			while (storePrmRS.next()) {
				eachrow = storePrmRS.getString(1) + ","
						+ storePrmRS.getString(2) + ","
						+ storePrmRS.getString(3) + ","
						+ storePrmRS.getString(4) + ","
						+ storePrmRS.getString(5);
				if (i == 0) {
					bankAcctData += eachrow;
				} else {
					bankAcctData += "#" + eachrow;
				}
				i++;
				// bankAcctData = bankAcctData + "#" + eachrow;
			}
			responseJSON.put("bankAcctMultiData", bankAcctData);

			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);
			
			String merchantAgentAcctQry = "SELECT distinct BANK_AGENT_NO,MPESA_AGENT_NO,AIRTEL_AGENT_NO,ORANGE_AGENT_NO,MPESA_TILL_NO from AGENT_INFORMATION where MERCHANT_ID=?";
			merchantprmPstmt = connection
					.prepareStatement(merchantAgentAcctQry);
			merchantprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			storePrmRS = merchantprmPstmt.executeQuery();
			String agentData = "";
			eachrow = "";
			i = 0;
			while (storePrmRS.next()) {
				eachrow = storePrmRS.getString(1) + ","
						+ storePrmRS.getString(2) + ","
						+ storePrmRS.getString(3) + ","
						+ storePrmRS.getString(4) + ","
						+ storePrmRS.getString(5);
				// agentData = agentData + "#" + eachrow;
				if (i == 0) {
					agentData += eachrow;
				} else {
					agentData += "#" + eachrow;
				}

				i++;
			}
			responseJSON.put("AgenctAcctMultiData", agentData);

			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);
			
			String merchantDocumentQry = "SELECT distinct DOC_ID,DOC_DESC,GRACE_PERIOD,MANDATORY from DOCUMENT_DETAILS where MERCHANT_ID=?";
			merchantprmPstmt = connection.prepareStatement(merchantDocumentQry);
			merchantprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			storePrmRS = merchantprmPstmt.executeQuery();
			String documentData = "";
			eachrow = "";
			i = 0;
			while (storePrmRS.next()) {
				eachrow = storePrmRS.getString(1) + ","
						+ storePrmRS.getString(2) + ","
						+ storePrmRS.getString(3) + ","
						+ storePrmRS.getString(4);
				// documentData = documentData + "#" + eachrow;
				if (i == 0) {
					documentData += eachrow;
				} else {
					documentData += "#" + eachrow;
				}

				i++;
			}

			responseJSON.put("documentMultiData", documentData);

			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);
			
			logger.debug("Resonse JSON [" + responseJSON + "]");
			merchantDataMap
					.put(CevaCommonConstants.MERCHANT_INFO, responseJSON);
			logger.debug("MerchantData Map [" + merchantDataMap + "]");
			responseDTO.setData(merchantDataMap);

			merchantBankAcctQry = "";

		} catch (Exception e) {
			logger.debug("Exception in GetMerchantViewDetails is ["
					+ e.getMessage() + "]");
		}

		finally {
			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);
			DBUtils.closeConnection(connection);
			
			merchantPrmQry = null;
			merchantDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO merchantTerminate(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside MerchantTerminate.. ");

		CallableStatement callableStatement = null;
		String terminateMerchantDetailsProc = "{call MerchantMgmtPkg.MerchantTerminateProc(?,?,?)}";
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(terminateMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.registerOutParameter(2, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(2);
			String Msg = callableStatement.getString(3);

			logger.debug("ResCnt [" + resCnt + "]");
			logger.debug("Msg [" + Msg + "]");

			//responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in MerchantTerminate [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			terminateMerchantDetailsProc = null;
		}

		return responseDTO;
	}

	public ResponseDTO getStoreViewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreViewDetails... ");
		HashMap<String, Object> storeDataMap = null;

		PreparedStatement storeprmPstmt = null;
		ResultSet storePrmRS = null;
		Connection connection = null;

		String storePrmQry = "";

		JSONObject resultJson1 = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		String merchantName = "";
		try {

			storeDataMap = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			merchantName = requestJSON
					.getString(CevaCommonConstants.MERCHANT_NAME);

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Merchant Name [" + merchantName + "]");

			logger.debug("Connection is [" + connection + "]");
			storePrmQry = "Select MERCHANT_ID,STORE_ID,STORE_NAME,LOCATION,MANAGER_NAME,EMAIL,ADDRESS,CITY,PO_BOX_NO,TELEPHONE_NO,MOBILE_NO,FAX_NO,PRI_CONTACT_NAME,PRI_CONTACT_NO,KRA_PIN,TILL_ID from STORE_MASTER where MERCHANT_ID=? and STORE_ID=?";

			storeprmPstmt = connection.prepareStatement(storePrmQry);
			storeprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeprmPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storePrmRS = storeprmPstmt.executeQuery();
			while (storePrmRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						storePrmRS.getString(1));
				responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
						merchantName);
				responseJSON.put(CevaCommonConstants.STORE_ID,
						storePrmRS.getString(2));
				responseJSON.put(CevaCommonConstants.STORE_NAME,
						storePrmRS.getString(3));
				responseJSON.put(CevaCommonConstants.LOCATION_NAME,
						storePrmRS.getString(4));
				responseJSON.put(CevaCommonConstants.MANAGER_NAME,
						storePrmRS.getString(5));
				responseJSON.put(CevaCommonConstants.EMAIL,
						storePrmRS.getString(6));

				String address = storePrmRS.getString(7);
				String[] addressVal = address.split("#");
				String address1 = "";
				String address2 = "";
				String address3 = "";
				if (addressVal.length == 1) {
					address1 = addressVal[0];
				}
				if (addressVal.length == 2) {
					address1 = addressVal[0];
					address2 = addressVal[1];
				}
				if (addressVal.length == 3) {
					address1 = addressVal[0];
					address2 = addressVal[1];
					address3 = addressVal[2];
				}

				String telphone = storePrmRS.getString(10);
				String[] telPhoneArr = telphone.split("#");
				String telephone1 = "";
				String telephone2 = "";
				if (telPhoneArr.length == 1)
					telephone1 = telPhoneArr[0];
				if (telPhoneArr.length == 2) {
					telephone1 = telPhoneArr[0];
					telephone2 = telPhoneArr[1];
				}

				responseJSON.put(CevaCommonConstants.ADDRESS1, address1);
				responseJSON.put(CevaCommonConstants.ADDRESS2, address2);
				responseJSON.put(CevaCommonConstants.ADDRESS3, address3);
				responseJSON.put(CevaCommonConstants.CITY,
						storePrmRS.getString(8));
				responseJSON.put(CevaCommonConstants.POBOXNUMBER,
						storePrmRS.getString(9));
				responseJSON.put(CevaCommonConstants.TELEPHONE1, telephone1);
				responseJSON.put(CevaCommonConstants.TELEPHONE2, telephone2);
				responseJSON.put(CevaCommonConstants.MOBILE_NUMBER,
						storePrmRS.getString(11));
				responseJSON.put(CevaCommonConstants.FAX_NUMBER,
						storePrmRS.getString(12));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						storePrmRS.getString(13));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						storePrmRS.getString(14));
				responseJSON.put(CevaCommonConstants.KRA_PIN,
						storePrmRS.getString(15));
				responseJSON.put(CevaCommonConstants.TILL_ID,
						storePrmRS.getString(16));

				address = null;
				addressVal = null;
				address1 = null;
				address2 = null;
				address3 = null;
				telphone = null;
				telPhoneArr = null;
				telephone1 = null;
				telephone2 = null;
			}
			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(storeprmPstmt);

			String merchantBankAcctQry = "SELECT distinct ACCT_NO,ACCT_DESC,BANK_NAME,BANK_BRANCH,TRANSFER_CODE from BANK_ACCT_MASTER where MERCHANT_ID=? and STORE_ID=?";
			storeprmPstmt = connection.prepareStatement(merchantBankAcctQry);
			storeprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeprmPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storePrmRS = storeprmPstmt.executeQuery();
			String bankAcctData = "";
			String eachrow = "";
			int i = 0;
			while (storePrmRS.next()) {
				eachrow = storePrmRS.getString(1) + ","
						+ storePrmRS.getString(2) + ","
						+ storePrmRS.getString(3) + ","
						+ storePrmRS.getString(4) + ","
						+ storePrmRS.getString(5);
				// bankAcctData = bankAcctData + "#" + eachrow;
				if (i == 0) {
					bankAcctData += eachrow;
				} else {
					bankAcctData += "#" + eachrow;
				}
				i++;
			}
			responseJSON.put("bankAcctMultiData", bankAcctData);

			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(storeprmPstmt);
			
			String merchantAgentAcctQry = "SELECT BANK_AGENT_NO,MPESA_AGENT_NO,AIRTEL_AGENT_NO,ORANGE_AGENT_NO,MPESA_TILL_NO from AGENT_INFORMATION where MERCHANT_ID=? and STORE_ID=?";
			storeprmPstmt = connection.prepareStatement(merchantAgentAcctQry);
			storeprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeprmPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storePrmRS = storeprmPstmt.executeQuery();
			String agentData = "";
			i = 0;
			while (storePrmRS.next()) {
				eachrow = storePrmRS.getString(1) + ","
						+ storePrmRS.getString(2) + ","
						+ storePrmRS.getString(3) + ","
						+ storePrmRS.getString(4) + ","
						+ storePrmRS.getString(5);
				// agentData = agentData + "#" + eachrow;
				if (i == 0) {
					agentData += eachrow;
				} else {
					agentData += "#" + eachrow;
				}

				i++;
			}
			responseJSON.put("AgenctAcctMultiData", agentData);

			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(storeprmPstmt);
			
			String merchantDocumentQry = "SELECT DOC_ID,DOC_DESC,GRACE_PERIOD,MANDATORY from DOCUMENT_DETAILS where MERCHANT_ID=? and STORE_ID=?";
			storeprmPstmt = connection.prepareStatement(merchantDocumentQry);
			storeprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			storeprmPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storePrmRS = storeprmPstmt.executeQuery();
			String documentData = "";
			i = 0;
			while (storePrmRS.next()) {
				eachrow = storePrmRS.getString(1) + ","
						+ storePrmRS.getString(2) + ","
						+ storePrmRS.getString(3) + ","
						+ storePrmRS.getString(4);
				// documentData = documentData + "#" + eachrow;
				if (i == 0) {
					documentData += eachrow;
				} else {
					documentData += "#" + eachrow;
				}

				i++;
			}
			responseJSON.put("documentMultiData", documentData);
			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(storeprmPstmt);
			documentData = null;
			agentData = null;
			bankAcctData = null;
			eachrow = null;

			cevaPowerDAO = new CevaPowerAdminDAO();
			// Getting List Of Locations
			resultJson1 = (JSONObject) cevaPowerDAO
					.getAdminCreateInfo(requestDTO).getData()
					.get(CevaCommonConstants.LOCATION_INFO);

			responseJSON.put(CevaCommonConstants.LOCATION_LIST,
					resultJson1.get(CevaCommonConstants.LOCATION_LIST));

			logger.debug("Response JSON [" + responseJSON + "]");

			storeDataMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("MerchantDataMap [" + storeDataMap + "]");
			responseDTO.setData(storeDataMap);

		} catch (Exception e) {
			logger.debug("Exception in GetStoreViewDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		}

		finally {
				DBUtils.closeResultSet(storePrmRS);
				DBUtils.closePreparedStatement(storeprmPstmt);
				DBUtils.closeConnection(connection);
			
				cevaPowerDAO = null;
				merchantName = null;
				storeDataMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO storeTerminate(RequestDTO requestDTO) {
		logger.debug("Inside StoreTerminate.. ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		String terminateMerchantDetailsProc = "{call MerchantMgmtPkg.StoreTerminateProc(?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(terminateMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);
			String Msg = callableStatement.getString(4);

			logger.debug("ResCnt [" + resCnt + "]");
			logger.debug("Msg [" + Msg + "]");

			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in MerchantTerminate [" + e.getMessage()
					+ "]");
		}

		finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			terminateMerchantDetailsProc = null;
		}

		return responseDTO;
	}

	public ResponseDTO getTerminalviewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetTerminalviewDetails..");

		HashMap<String, Object> terminalDataMap = null;

		PreparedStatement terminalprmPstmt = null;
		ResultSet terminalRS = null;

		Connection connection = null;
		JSONObject json = null;
		String storePrmQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_USAGE,TERMINAL_MAKE,MODEL_NO,"
				+ "SERIAL_NO,PIN_ENTRY,to_char(VALID_FROM,'DD-MM-YYYY'),to_char(VALID_THRU,'DD-MM-YYYY'),STATUS,to_char(TERMINAL_DATE,'DD-MM-YYYY'),TMK_INDEX,TPK"
				+ " from TERMINAL_MASTER where MERCHANT_ID=? and trim(STORE_ID)=trim(?) and TERMINAL_ID=?";
		try {

			terminalDataMap = new HashMap<String, Object>();
			json = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			terminalprmPstmt = connection.prepareStatement(storePrmQry);
			terminalprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			terminalprmPstmt.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			terminalprmPstmt.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			terminalRS = terminalprmPstmt.executeQuery();
			while (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERM_USAGE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.PINENTRY,
						terminalRS.getString(8));
				responseJSON.put(CevaCommonConstants.VALID_FROM,
						terminalRS.getString(9));
				responseJSON.put(CevaCommonConstants.VALID_THRU,
						terminalRS.getString(10));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(11));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(12));
				responseJSON.put(CevaCommonConstants.TMK_INDEX,
						terminalRS.getString(13));
				responseJSON.put(CevaCommonConstants.TPK_KEY,
						terminalRS.getString(14));
			}

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalprmPstmt);

			String merchnatQry = "Select MERCHANT_ID,MERCHANT_NAME from MERCHANT_MASTER order by MERCHANT_NAME";
			terminalprmPstmt = connection.prepareStatement(merchnatQry);

			terminalRS = terminalprmPstmt.executeQuery();

			while (terminalRS.next()) {

				json.put(terminalRS.getString(1), terminalRS.getString(2));
			}
			responseJSON.put(CevaCommonConstants.MERCHANT_LIST, json);
			json.clear();

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalprmPstmt);

			String storeQry = "Select STORE_ID,STORE_NAME from STORE_MASTER where trim(MERCHANT_ID)=trim(?) order by STORE_NAME";
			terminalprmPstmt = connection.prepareStatement(storeQry);
			terminalprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));

			terminalRS = terminalprmPstmt.executeQuery();
			while (terminalRS.next()) {
				json.put(terminalRS.getString(2), terminalRS.getString(1));
			}
			responseJSON.put(CevaCommonConstants.STORE_LIST, json);
			logger.debug("Response JSON [" + responseJSON + "]");

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalprmPstmt);

			terminalDataMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("TerminalDataMap [" + terminalDataMap + "]");
			responseDTO.setData(terminalDataMap);
		} catch (Exception e) {
			logger.debug("Exception in GetTerminalviewDetails ["
					+ e.getMessage() + "]");
		}

		finally {

			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalprmPstmt);
			DBUtils.closeConnection(connection);
			terminalDataMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO terminateTerminal(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside TerminateTerminal.. ");

		CallableStatement callableStatement = null;
		String terminateMerchantDetailsProc = "{call MerchantMgmtPkg.TerminalTerminateProc(?,?,?,?,?)}";
		String Msg = "";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(terminateMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.registerOutParameter(4, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(4);
			Msg = callableStatement.getString(5);

			logger.debug("ResCnt [" + resCnt + "]");
			logger.debug("Msg [" + Msg + "]");

			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in TerminateTerminal [" + e.getMessage()
					+ "]");
		}

		finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			terminateMerchantDetailsProc = null;
			Msg = null;
		}

		return responseDTO;
	}

	public ResponseDTO updateTerminalDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside UpdateTerminalDetails... ");

		CallableStatement callableStatement = null;
		String insertMerchantDetailsProc = "{call MerchantMgmtPkg.TerminalDetailsUpdatePro(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String Msg = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.TERMINAL_USAGE));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.TERMINAL_MAKE));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.MODEL_NO));
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.SERIAL_NO));
			callableStatement.setString(9,
					requestJSON.getString(CevaCommonConstants.PINENTRY));
			callableStatement.setString(10,
					requestJSON.getString(CevaCommonConstants.VALID_FROM));
			callableStatement.setString(11,
					requestJSON.getString(CevaCommonConstants.VALID_THRU));
			callableStatement.setString(12,
					requestJSON.getString(CevaCommonConstants.STATUS));
			callableStatement.setString(13,
					requestJSON.getString(CevaCommonConstants.TERMINAL_DATE));
			callableStatement.registerOutParameter(14, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(14);
			Msg = callableStatement.getString(15);

			logger.debug("Res Cnt [" + resCnt + "]");
			logger.debug("Msg [" + Msg + "]");

			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in UpdateTerminalDetails ["
					+ e.getMessage() + "]");
		}

		finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			
			insertMerchantDetailsProc = null;
			Msg = null;
		}
		return responseDTO;
	}

	public ResponseDTO updateStoreDetails(RequestDTO requestDTO) {
		logger.debug("Inside UpdateStoreDetails.. ");

		Connection connection = null;
		String address = null;
		String telephone = null;
		String addressLine1 = null;
		String addressLine2 = null;
		String addressLine3 = null;
		String telephoneNumber1 = null;
		String telephoneNumber2 = null;
		String Msg = "";
		CallableStatement callableStatement = null;
		String insertMerchantDetailsProc = "{call MerchantMgmtPkg.StoreDetailsUpdateProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			addressLine1 = requestJSON.getString(CevaCommonConstants.ADDRESS1);
			addressLine2 = requestJSON.getString(CevaCommonConstants.ADDRESS2);
			if (requestJSON.containsKey(CevaCommonConstants.ADDRESS3)) {
				addressLine3 = requestJSON
						.getString(CevaCommonConstants.ADDRESS3);
			} else {
				addressLine3 = "";
			}

			telephoneNumber1 = requestJSON
					.getString(CevaCommonConstants.TELEPHONE1);
			if (requestJSON.containsKey(CevaCommonConstants.TELEPHONE2)) {
				telephoneNumber2 = requestJSON
						.getString(CevaCommonConstants.TELEPHONE2);
			} else {
				telephoneNumber2 = "";
			}

			address = addressLine1 + "#" + addressLine2 + "#" + addressLine3;
			telephone = telephoneNumber1 + "#" + telephoneNumber2;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection
					.prepareCall(insertMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.MERCHANT_NAME));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.STORE_NAME));
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.LOCATION_NAME));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.KRA_PIN));
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.MANAGER_NAME));
			callableStatement.setString(9,
					requestJSON.getString(CevaCommonConstants.EMAIL_ID));
			callableStatement.setString(10, address);
			callableStatement.setString(11,
					requestJSON.getString(CevaCommonConstants.CITY));
			callableStatement.setString(12,
					requestJSON.getString(CevaCommonConstants.POBOXNUMBER));
			callableStatement.setString(13, telephone);
			callableStatement.setString(14,
					requestJSON.getString(CevaCommonConstants.MOBILE_NUMBER));
			callableStatement.setString(15,
					requestJSON.getString(CevaCommonConstants.FAX_NUMBER));
			callableStatement.setString(16, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NAME));
			callableStatement.setString(17, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NUMBER));
			callableStatement.registerOutParameter(18, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(19, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(18);
			Msg = callableStatement.getString(19);

			logger.debug("ResCnt [" + resCnt + "]");
			logger.debug("Msg [" + Msg + "]");

			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {
				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in UpdateStoreDetails [" + e.getMessage()
					+ "]");
		}

		finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			
			insertMerchantDetailsProc = null;
			Msg = null;
			address = null;
			telephone = null;
			addressLine1 = null;
			addressLine2 = null;
			addressLine3 = null;
			telephoneNumber1 = null;
			telephoneNumber2 = null;
		}
		return responseDTO;
	}

	public ResponseDTO getUserstoTerminal(RequestDTO requestDTO) {

		HashMap<String, Object> resultMap = null;
		JSONArray userJSONArray = null;
		Connection connection = null;

		logger.debug("Inside Get Users to Terminal.. ");

		String userQry = "SELECT ULC.LOGIN_USER_ID,ULC.LOGIN_USER_ID||'-'||UI.USER_NAME "
				+ "from USER_LOGIN_CREDENTIALS ULC,USER_INFORMATION UI "
				+ "where UI.COMMON_ID=ULC.COMMON_ID";

		PreparedStatement userPstmt = null;
		ResultSet userRS = null;
		String terminalId = null;
		String storeId = null;
		String merchantId = null;
		JSONObject json = null;
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("RequestJSON [" + requestJSON + "]");
			responseJSON = requestJSON;

			resultMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			userPstmt = connection.prepareStatement(userQry);
			userRS = userPstmt.executeQuery();
			json = new JSONObject();

			while (userRS.next()) {
				json.put(userRS.getString(1), userRS.getString(2));
			}
			responseJSON.put(CevaCommonConstants.USERS_LIST, json);
			responseJSON.put("ADMIN_LIST", json);
			json.clear();
			userQry = "";
			
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeResultSet(userRS);
			
			userQry = "select USER_ID from USER_TERMINAL_MAPPING "
					+ "where MERCHANT_ID = ? and STORE_ID = ? and TERMINAL_ID = ? and "
					+ "trunc(MAKER_DTTM)=(select trunc(max(MAKER_DTTM)) from USER_TERMINAL_MAPPING "
					+ "where MERCHANT_ID = ? and STORE_ID = ? and TERMINAL_ID = ?) order by USER_ID";

			terminalId = requestJSON.getString(CevaCommonConstants.TERMINAL_ID);
			storeId = requestJSON.getString(CevaCommonConstants.STORE_ID);
			merchantId = requestJSON.getString(CevaCommonConstants.MERCHANT_ID);

			userPstmt = connection.prepareStatement(userQry);
			userPstmt.setString(1, merchantId.trim());
			userPstmt.setString(2, storeId.trim());
			userPstmt.setString(3, terminalId.trim());
			userPstmt.setString(4, merchantId.trim());
			userPstmt.setString(5, storeId.trim());
			userPstmt.setString(6, terminalId.trim());

			userRS = userPstmt.executeQuery();

			userJSONArray = new JSONArray();

			while (userRS.next()) {

				userJSONArray.add(userRS.getString(1));
			}

			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeResultSet(userRS);
			
			responseJSON.put("exist_users", userJSONArray);
			resultMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
			responseDTO.setData(resultMap);
		} catch (Exception e) {
			logger.debug("Exception in GetUserstoTerminal [" + e.getMessage()
					+ "]");
		} finally {

			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);
			terminalId = null;
			storeId = null;
			merchantId = null;
			userQry = null;
			resultMap = null;
			userJSONArray = null;
		}
		return responseDTO;
	}

	public ResponseDTO assignUsers(RequestDTO requestDTO) {

		Connection connection = null;
		String makerId = "";
		String merchantId = "";
		String terminalId = "";
		String selectedUser = "";
		String supervisor = "";
		String admin = "";
		String storeId = "";

		CallableStatement callableStatement = null;
		String assignUsers = "{call MerchantMgmtPkg.assignUsersToTerminalProc(?,?,?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			makerId = requestJSON.getString(CevaCommonConstants.MAKER_ID);
			merchantId = requestJSON.getString(CevaCommonConstants.MERCHANT_ID);
			storeId = requestJSON.getString(CevaCommonConstants.STORE_ID);
			terminalId = requestJSON.getString(CevaCommonConstants.TERMINAL_ID);
			selectedUser = requestJSON
					.getString(CevaCommonConstants.SELECTED_USERS);
			supervisor = requestJSON.getString("SUPERVISOR");
			admin = requestJSON.getString("ADMIN");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");
			callableStatement = connection.prepareCall(assignUsers);
			callableStatement.setString(1, makerId);
			callableStatement.setString(2, merchantId);
			callableStatement.setString(3, storeId);
			callableStatement.setString(4, terminalId);
			callableStatement.setString(5, selectedUser);
			callableStatement.setString(6, supervisor);
			callableStatement.setString(7, admin);
			callableStatement.registerOutParameter(8, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);

			logger.debug(" before calling executeUpdate Method :::");
			callableStatement.executeUpdate();
			logger.debug(" After calling executeUpdate Method :::");
			int resCnt = callableStatement.getInt(8);
			logger.debug(" resCnt[" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO.addMessages("Assigned Users to " + terminalId
						+ " is successfully Completed.");
			} else if (resCnt == -1 || resCnt == -2 || resCnt == -3) {
				responseDTO.addError(callableStatement.getString(9));
			} else {
				responseDTO.addError("some issue occured.");
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
			responseDTO.addError("Internal Issue Occured.");
		}

		finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			storeId = null;
			makerId = null;
			merchantId = null;
			terminalId = null;
			selectedUser = null;
			supervisor = null;
			admin = null;
			assignUsers = null;
		}

		return responseDTO;
	}

	public ResponseDTO getAutoGeneratedTerminal(RequestDTO requestDTO) {

		logger.debug("Inside GetAutoGeneratedTerminal.. ");
		HashMap<String, Object> storeDataMap = null;
		JSONObject resultJson = null;

		PreparedStatement terminalIdPstmt = null;
		ResultSet terminalIdRS = null;
		Connection connection = null;

		//String terminalIdQry = "Select count(*) from TERMINAL_MASTER_TEMP where TERMINAL_ID in (select distinct TERMINAL_ID from TERMINAL_MASTER_TEMP)";
		String terminalIdQry = "Select count(*) from TERMINAL_MASTER where TERMINAL_ID in (select distinct TERMINAL_ID from TERMINAL_MASTER)";
		String tmkIndexQry = "";
		String tmkIndex = "";
		try {
			storeDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug(" connection is [" + connection + "]");
			terminalIdPstmt = connection.prepareStatement(terminalIdQry);

			terminalIdRS = terminalIdPstmt.executeQuery();
			int terminalId = 0;

			while (terminalIdRS.next()) {
				String storeID = terminalIdRS.getString(1);
				terminalId = Integer.parseInt(storeID);
			}

			terminalId++;
			resultJson.put(CevaCommonConstants.TERMINAL_ID, terminalId);

			DBUtils.closeResultSet(terminalIdRS);
			DBUtils.closePreparedStatement(terminalIdPstmt);
			
			tmkIndexQry = "Select TMKINDEX_SEQ.nextval from DUAL";

			terminalIdPstmt = connection.prepareStatement(tmkIndexQry);

			terminalIdRS = terminalIdPstmt.executeQuery();
			if (terminalIdRS.next()) {
				tmkIndex = terminalIdRS.getString(1);
			}

			resultJson.put(CevaCommonConstants.TMK_INDEX, tmkIndex);
			storeDataMap.put(CevaCommonConstants.STORE_INFO, resultJson);

			DBUtils.closeResultSet(terminalIdRS);
			DBUtils.closePreparedStatement(terminalIdPstmt);
			
			logger.debug("StoreDataMap [" + storeDataMap + "]");
			responseDTO.setData(storeDataMap);

		} catch (Exception e) {
			logger.debug("Exception in GetAutoGeneratedTerminal ["
					+ e.getMessage() + "]");
		}

		finally {
			DBUtils.closeResultSet(terminalIdRS);
			DBUtils.closePreparedStatement(terminalIdPstmt);
			DBUtils.closeConnection(connection);

			storeDataMap = null;
			resultJson = null;
			terminalIdQry = null;
			tmkIndexQry = null;
			tmkIndex = null;
		}

		return responseDTO;
	}

	public ResponseDTO getMerchantDashBoard(RequestDTO requestDTO) {

		logger.debug("Inside GetMerchantDashBoard... ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJSONArray = null;

		PreparedStatement merchantPstmt = null;

		ResultSet merchantRS = null;
		Connection connection = null;

		String merchantQry = "SELECT SM.MERCHANT_ID,SM.STORE_ID,SM.STORE_NAME,(select office_name from ceva_branch_master where office_code=SM.location and rownum<2),"
				+ "TM.TERMINAL_ID,TM.STATUS,TM.SERIAL_NO,TM.MAKER_ID "
				+ "FROM   STORE_MASTER SM ,TERMINAL_MASTER TM "
				+ " where (TM.STORE_ID = SM.STORE_ID) "
				+ " order by SM.MERCHANT_ID,SM.STORE_ID,TM.TERMINAL_ID";
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJSONArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			json = new JSONObject();
			while (merchantRS.next()) {
				json.put(CevaCommonConstants.MERCHANT_ID,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.STORE_ID, merchantRS.getString(2));
				json.put("store_name", merchantRS.getString(3));
				json.put("store_location", merchantRS.getString(4));
				json.put(CevaCommonConstants.TERMINAL_ID,
						merchantRS.getString(5));
				json.put(CevaCommonConstants.STATUS, merchantRS.getString(6));
				json.put("serialno", merchantRS.getString(7));
				json.put("authterminalid", merchantRS.getString(8));

				merchantJSONArray.add(json);
				json.clear();
			}

			logger.debug("MerchantJSONArray [" + merchantJSONArray + "]");
			resultJson.put(CevaCommonConstants.MERCHANT_DASHBOARD,
					merchantJSONArray);

			merchantMap.put(CevaCommonConstants.MERCHANT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

		} catch (Exception e) {
			logger.debug("Exception in GetMerchantDashBoard [" + e.getMessage()
					+ "]");
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

	public ResponseDTO getStoreViewDashboardDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetStoreViewDashboardDetails..");
		HashMap<String, Object> storeDataMap = null;

		PreparedStatement storeprmPstmt = null;
		ResultSet storePrmRS = null;
		Connection connection = null;

		String storePrmQry = "Select SM.MERCHANT_ID,SM.STORE_ID,SM.STORE_NAME,SM.LOCATION,SM.MANAGER_NAME,SM.EMAIL,SM.ADDRESS,SM.CITY,SM.PO_BOX_NO,SM.TELEPHONE_NO,SM.MOBILE_NO,SM.FAX_NO,SM.PRI_CONTACT_NAME,SM.PRI_CONTACT_NO,SM.KRA_PIN,MM.MERCHANT_NAME from STORE_MASTER SM,MERCHANT_MASTER MM where MM.MERCHANT_ID=SM.MERCHANT_ID and trim(SM.STORE_ID)=trim(?)";
		try {
			storeDataMap = new HashMap<String, Object>();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			storeprmPstmt = connection.prepareStatement(storePrmQry);
			storeprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.STORE_ID));
			storePrmRS = storeprmPstmt.executeQuery();
			while (storePrmRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						storePrmRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						storePrmRS.getString(2));
				responseJSON.put(CevaCommonConstants.STORE_NAME,
						storePrmRS.getString(3));
				responseJSON.put(CevaCommonConstants.LOCATION_NAME,
						storePrmRS.getString(4));
				responseJSON.put(CevaCommonConstants.MANAGER_NAME,
						storePrmRS.getString(5));
				responseJSON.put(CevaCommonConstants.EMAIL,
						storePrmRS.getString(6));
				String address = storePrmRS.getString(7);
				String[] addressVal = address.split("#");
				String address1 = "";
				String address2 = "";
				String address3 = "";
				if (addressVal.length == 1) {
					address1 = addressVal[0];
				}
				if (addressVal.length == 2) {
					address1 = addressVal[0];
					address2 = addressVal[1];
				}
				if (addressVal.length == 3) {
					address1 = addressVal[0];
					address2 = addressVal[1];
					address3 = addressVal[2];
				}

				String telphone = storePrmRS.getString(10);
				String[] telPhoneArr = telphone.split("#");
				String telephone1 = "";
				String telephone2 = "";
				if (telPhoneArr.length == 1)
					telephone1 = telPhoneArr[0];
				if (telPhoneArr.length == 2) {
					telephone1 = telPhoneArr[0];
					telephone2 = telPhoneArr[1];
				}

				responseJSON.put(CevaCommonConstants.ADDRESS1, address1);
				responseJSON.put(CevaCommonConstants.ADDRESS2, address2);
				responseJSON.put(CevaCommonConstants.ADDRESS3, address3);
				responseJSON.put(CevaCommonConstants.CITY,
						storePrmRS.getString(8));
				responseJSON.put(CevaCommonConstants.POBOXNUMBER,
						storePrmRS.getString(9));
				responseJSON.put(CevaCommonConstants.TELEPHONE1, telephone1);
				responseJSON.put(CevaCommonConstants.TELEPHONE2, telephone2);
				responseJSON.put(CevaCommonConstants.MOBILE_NUMBER,
						storePrmRS.getString(11));
				responseJSON.put(CevaCommonConstants.FAX_NUMBER,
						storePrmRS.getString(12));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						storePrmRS.getString(13));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						storePrmRS.getString(14));
				responseJSON.put(CevaCommonConstants.KRA_PIN,
						storePrmRS.getString(15));
				responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
						storePrmRS.getString(16));

				address = null;
				addressVal = null;
				address1 = null;
				address2 = null;
				address3 = null;
				telphone = null;
				telPhoneArr = null;
				telephone1 = null;
				telephone2 = null;
			}

			logger.debug("Response JSON [" + responseJSON + "]");
			storeDataMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			logger.debug("MerchantData Map [" + storeDataMap + "]");
			responseDTO.setData(storeDataMap);

		} catch (Exception e) {
			logger.debug("Exception in GetStoreViewDashboardDetails ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(storePrmRS);
			DBUtils.closePreparedStatement(storeprmPstmt);
			DBUtils.closeConnection(connection);
			storeDataMap = null;
			storePrmQry = null;
		}

		return responseDTO;
	}

	public ResponseDTO getTerminalDashboardviewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetTerminalDashboardviewDetails.. ");
		HashMap<String, Object> terminalDataMap = null;
		PreparedStatement terminalprmPstmt = null;
		ResultSet terminalRS = null;
		Connection connection = null;

		String storePrmQry = "Select MERCHANT_ID,STORE_ID,TERMINAL_ID,TERMINAL_USAGE,TERMINAL_MAKE,MODEL_NO,"
				+ "SERIAL_NO,PIN_ENTRY,to_char(VALID_FROM,'DD-MM-YYYY'),to_char(VALID_THRU,'DD-MM-YYYY'),STATUS,"
				+ "to_char(TERMINAL_DATE,'DD-MM-YYYY')"
				+ " from TERMINAL_MASTER where  trim(TERMINAL_ID)=trim(?)";
		try {
			terminalDataMap = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			terminalprmPstmt = connection.prepareStatement(storePrmQry);
			terminalprmPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));
			terminalRS = terminalprmPstmt.executeQuery();
			while (terminalRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						terminalRS.getString(1));
				responseJSON.put(CevaCommonConstants.STORE_ID,
						terminalRS.getString(2));
				responseJSON.put(CevaCommonConstants.TERMINAL_ID,
						terminalRS.getString(3));
				responseJSON.put(CevaCommonConstants.TERM_USAGE,
						terminalRS.getString(4));
				responseJSON.put(CevaCommonConstants.TERMINAL_MAKE,
						terminalRS.getString(5));
				responseJSON.put(CevaCommonConstants.MODEL_NO,
						terminalRS.getString(6));
				responseJSON.put(CevaCommonConstants.SERIAL_NO,
						terminalRS.getString(7));
				responseJSON.put(CevaCommonConstants.PINENTRY,
						terminalRS.getString(8));
				responseJSON.put(CevaCommonConstants.VALID_FROM,
						terminalRS.getString(9));
				responseJSON.put(CevaCommonConstants.VALID_THRU,
						terminalRS.getString(10));
				responseJSON.put(CevaCommonConstants.STATUS,
						terminalRS.getString(11));
				responseJSON.put(CevaCommonConstants.TERMINAL_DATE,
						terminalRS.getString(12));
			}

			logger.debug("Response JSON [" + responseJSON + "]");
			terminalDataMap.put(CevaCommonConstants.STORE_INFO, responseJSON);
			responseDTO.setData(terminalDataMap);

		} catch (Exception e) {
			logger.debug("Exception in GetTerminalDashboardviewDetails ["
					+ e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalprmPstmt);
			DBUtils.closeConnection(connection);
			storePrmQry = null;
			terminalDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO getUserstoServices(RequestDTO requestDTO) {

		HashMap<String, Object> resultMap = null;
		JSONArray userJSONArray = null;
		Connection connection = null;

		logger.debug("Inside GetUserstoServices... ");

		String userQry = "SELECT SERVICE_CODE,SERVICE_CODE||'-'||SERVICE_NAME from SERVICE_MASTER where SERVICE_TYPE is null";
		PreparedStatement userPstmt = null;
		ResultSet userRS = null;
		JSONObject json = null;
		try {
			resultMap = new HashMap<String, Object>();
			userJSONArray = new JSONArray();
			json = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			responseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			userPstmt = connection.prepareStatement(userQry);
			userRS = userPstmt.executeQuery();

			while (userRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, userRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, userRS.getString(1));
				userJSONArray.add(json);
				json.clear();
			}

			responseJSON.put(CevaCommonConstants.SERVICE_LIST, userJSONArray);
			resultMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
			responseDTO.setData(resultMap);

		} catch (Exception e) {
			logger.debug("Exception in GetUserstoServices [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);
			resultMap = null;
			userJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO assignServices(RequestDTO requestDTO) {

		logger.debug("Inside AssignServices... ");

		Connection connection = null;
		String makerId = "";
		String merchantId = "";
		String storeId = "";
		String terminalId = "";
		String selectedUser = "";

		CallableStatement callableStatement = null;
		String assignUsers = "{call MerchantMgmtPkg.assignServicesToTerminalProc(?,?,?,?,?,?,?)}";
		String msg = "";
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			makerId = requestJSON.getString(CevaCommonConstants.MAKER_ID);
			merchantId = requestJSON.getString(CevaCommonConstants.MERCHANT_ID);
			storeId = requestJSON.getString(CevaCommonConstants.STORE_ID);
			terminalId = requestJSON.getString(CevaCommonConstants.TERMINAL_ID);
			selectedUser = requestJSON
					.getString(CevaCommonConstants.SELECTED_SERVICES);

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection.prepareCall(assignUsers);
			callableStatement.setString(1, makerId);
			callableStatement.setString(2, merchantId);
			callableStatement.setString(3, storeId);
			callableStatement.setString(4, terminalId);
			callableStatement.setString(5, selectedUser);
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			logger.debug(" Before calling executeUpdate Method :::");
			callableStatement.executeUpdate();
			logger.debug(" After calling executeUpdate Method :::");
			int resCnt = callableStatement.getInt(6);
			msg = callableStatement.getString(7);
			logger.debug("ResCnt  [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO.addMessages("Assigned Services to " + terminalId
						+ " is successfully Completed.");
			} else if (resCnt == -1) {
				responseDTO.addError(msg);
			} else {
				responseDTO.addError(msg);
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
			logger.debug("SQLException in AssignServices ["
					+ exception.getMessage() + "]");
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.debug("Exception in AssignServices ["
					+ exception.getMessage() + "]");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			makerId = null;
			merchantId = null;
			storeId = null;
			terminalId = null;
			selectedUser = null;
			assignUsers = null;
			msg = null;
		}
		return responseDTO;
	}

	public ResponseDTO assignServiceToTerminalView(RequestDTO requestDTO) {

		logger.debug("Inside  AssignServiceToTerminalView.. ");
		Connection connection = null;
		HashMap<String, Object> resultMap = null;

		String userQry = "SELECT TERMINAL_ID,SERVICE_CODE from TERMINAL_SERVICE_MAPPING where TERMINAL_ID=?";
		PreparedStatement userPstmt = null;
		ResultSet userRS = null;

		String allService = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			responseJSON = requestJSON;

			resultMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			userPstmt = connection.prepareStatement(userQry);
			userPstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.TERMINAL_ID));

			userRS = userPstmt.executeQuery();
			while (userRS.next()) {
				allService = allService + "#" + userRS.getString(1) + ","
						+ userRS.getString(2);
			}
			responseJSON.put(CevaCommonConstants.SERVICE_LIST, allService);

			resultMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);
			responseDTO.setData(resultMap);

		} catch (Exception e) {
			logger.debug("Exception in AssignServiceToTerminalView ["
					+ e.getMessage() + "]");
		} finally {

			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);
			resultMap = null;
			userQry = null;
		}
		return responseDTO;

	}

	public ResponseDTO merchantModify(RequestDTO requestDTO) {

		logger.debug("Inside MerchantModify.. ");

		String address = null;
		String telephone = null;
		String addressLine1 = null;
		String addressLine2 = null;
		String addressLine3 = null;
		String telephoneNumber1 = null;
		String telephoneNumber2 = null;

		Connection connection = null;
		CallableStatement callableStatement = null;
		String modifyMerchantDetailsProc = "{call MerchantMgmtPkg.MerchantDetailsModifyProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		String Msg = "";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			addressLine1 = requestJSON.getString(CevaCommonConstants.ADDRESS1);
			addressLine2 = requestJSON.getString(CevaCommonConstants.ADDRESS2);
			if (requestJSON.containsKey(CevaCommonConstants.ADDRESS3)) {
				addressLine3 = requestJSON
						.getString(CevaCommonConstants.ADDRESS3);
			} else {
				addressLine3 = "";
			}

			telephoneNumber1 = requestJSON
					.getString(CevaCommonConstants.TELEPHONE1);
			if (requestJSON.containsKey(CevaCommonConstants.TELEPHONE2)) {
				telephoneNumber2 = requestJSON
						.getString(CevaCommonConstants.TELEPHONE2);
			} else {
				telephoneNumber2 = "";
			}

			address = addressLine1 + "#" + addressLine2 + "#" + addressLine3;
			telephone = telephoneNumber1 + "#" + telephoneNumber2;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");
			callableStatement = connection
					.prepareCall(modifyMerchantDetailsProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.MERCHANT_ID));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.MANAGER_NAME));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.EMAIL_ID));
			callableStatement.setString(5, address);
			callableStatement.setString(6,
					requestJSON.getString(CevaCommonConstants.CITY));
			callableStatement.setString(7,
					requestJSON.getString(CevaCommonConstants.POBOXNUMBER));
			callableStatement.setString(8, telephone);
			callableStatement.setString(9,
					requestJSON.getString(CevaCommonConstants.MOBILE_NUMBER));
			callableStatement.setString(10,
					requestJSON.getString(CevaCommonConstants.FAX_NUMBER));
			callableStatement.setString(11, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NAME));
			callableStatement.setString(12, requestJSON
					.getString(CevaCommonConstants.PRIMARY_CONTACT_NUMBER));

			callableStatement.registerOutParameter(13, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(13);
			Msg = callableStatement.getString(14);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			responseDTO = getMerchantDetails(requestDTO);
			if (resCnt == 1) {
				responseDTO.addMessages(Msg);
			} else if (resCnt == -1) {

				responseDTO.addError(Msg);
			} else {
				responseDTO.addError(Msg);
			}

		} catch (Exception e) {
			logger.debug("Exception in MerchantModify [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			Msg = null;
			address = null;
			telephone = null;
			addressLine1 = null;
			addressLine2 = null;
			addressLine3 = null;
			telephoneNumber1 = null;
			telephoneNumber2 = null;
			modifyMerchantDetailsProc = null;
		}

		return responseDTO;
	}

	private static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = (Long) (fraction + aStart);
		return randomNumber.toString();
	}

}
