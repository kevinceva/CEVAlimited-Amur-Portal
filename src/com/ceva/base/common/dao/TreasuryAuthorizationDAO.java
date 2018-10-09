package com.ceva.base.common.dao;

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

public class TreasuryAuthorizationDAO {

	private Logger logger = Logger.getLogger(TreasuryAuthorizationDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO fetchForAuthorizationData(RequestDTO requestDTO) {

		logger.debug("Inside fetchForAuthorizationData... ");
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

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			
			/*
			 * String storeQry =
			 * "Select STORE_ID,MERCHANT_ID,LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cash in Hand Limit','NO_DATA' from FLOAT_LIMIT_MASTER where TERMINAL_ID is null  "
			 * +
			 * "union all Select STORE_ID,MERCHANT_ID,CREDIT_AMOUNT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cash Credit',REF_NO from STORE_CREDIT_MASTER "
			 * +
			 * "union all Select STORE_ID,MERCHANT_ID,to_char(CREDIT_LMT_AMT),STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cash Deposit Limit','NO_DATA' from STORE_CASHDPT_LIMIT_MASTER where STATUS='Requested' "
			 * +
			 * "union all Select CARD_NO,BANK_NAME,to_char(AMOUNT),STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cheque Approval',REF_NO||'~'||ACC_NO from LOAD_CARD_CHEQUE_DETAILS where STATUS='Requested' "
			 * ;
			 */

			String storeQry = "Select STORE_ID,MERCHANT_ID,CREDIT_AMOUNT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cash Credit',REF_NO from STORE_CREDIT_MASTER "
					+ "union all Select STORE_ID,MERCHANT_ID,to_char(CREDIT_LMT_AMT),STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Terminal Limit','NO_DATA' from STORE_CASHDPT_LIMIT_MASTER where STATUS='Requested'";

			storePstmt = connection.prepareStatement(storeQry);

			storeRS = storePstmt.executeQuery();
			JSONObject json = new JSONObject();
			while (storeRS.next()) {

				json.put(CevaCommonConstants.STORE_ID, storeRS.getString(1));
				json.put(CevaCommonConstants.MERCHANT_ID, storeRS.getString(2));
				json.put(CevaCommonConstants.STORE_LIMIT, storeRS.getString(3));
				json.put(CevaCommonConstants.STORE_LIMIT_STATUS,
						storeRS.getString(4));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
						storeRS.getString(5));
				json.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
						storeRS.getString(6));
				json.put("AUTH_TYPE", storeRS.getString(7));
				json.put("REF_NO", storeRS.getString(8));
				storeJSONArray.add(json);
				json.clear();

			}
			logger.debug("StoreJSONArray [" + storeJSONArray + "]");

			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			
			String MidQry = "select distinct MERCHANT_ID from MERCHANT_MASTER ";
			

			merchantPstmt = connection.prepareStatement(MidQry);

			mRS = merchantPstmt.executeQuery();
			while (mRS.next()) {
				TMIDS.add(mRS.getString(1));
			}

			DBUtils.closeResultSet(mRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			for (int i = 0; i < TMIDS.size(); i++) {
				String sidQry = "select distinct STORE_ID from STORE_MASTER where MERCHANT_ID=?";
				sidPstmt = connection.prepareStatement(sidQry);
				sidPstmt.setString(1, TMIDS.get(i));

				sidRS = sidPstmt.executeQuery();
				String sid = "";
				while (sidRS.next()) {
					terminalJSONArray = new JSONArray();
					SID.add(sidRS.getString(1));
					String terminalQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,'NO_DATA','NO_DATA',LIMIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cash in Hand Limit','NO_DATA' from FLOAT_LIMIT_MASTER  where STORE_ID=? and MERCHANT_ID=? and TERMINAL_ID is not null "
							+ "union all "
							+ "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,REF_NO,TERMINAL_REF_NO,CREDIT_AMT,STATUS,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),'Cash Credit',REF_NO from TERMINAL_CREDIT_MASTER  where STORE_ID=? and MERCHANT_ID=?";

					terminalPstmt = connection.prepareStatement(terminalQry);
					sid = sidRS.getString(1);
					terminalPstmt.setString(1, sidRS.getString(1));
					terminalPstmt.setString(2, TMIDS.get(i));
					terminalPstmt.setString(3, sidRS.getString(1));
					terminalPstmt.setString(4, TMIDS.get(i));
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
								terminalRS.getString(6));
						json.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
								terminalRS.getString(7));
						json.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
								terminalRS.getString(8));
						json.put(
								CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
								terminalRS.getString(9));
						json.put("AUTH_TYPE", terminalRS.getString(10));
						json.put("REF_NO", terminalRS.getString(11));
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
			DBUtils.closeResultSet(terminalRS);
			DBUtils.closePreparedStatement(terminalPstmt);
			terminalPstmt = null;
			terminalRS = null;
			DBUtils.closeResultSet(sidRS);
			DBUtils.closePreparedStatement(sidPstmt);
			
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
			DBUtils.closePreparedStatement(sidPstmt);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			
			
			
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

}
