package com.ceva.base.common.dao;

import java.awt.image.DataBufferUShort;
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

public class AuthFetchSubDAO {

	private Logger logger = Logger.getLogger(AuthFetchSubDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO UserAuthRecordData(RequestDTO requestDTO) {

		logger.debug("Inside UserAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		// String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			// auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			merchantQry = "select UIT.user_name ,UIT.emp_id ,to_char(EXPIRY_DATE,'DD-MM-YYYY'),"
					+ " UIT.user_level,(select distinct cbl.OFFICE_NAME from POSTA_BRANCH_MASTER cbl where cbl.office_code=location),"
					+ " UIT.email,UIT.LOCAL_RES_NUM,UIT.LOCAL_OFF_NUM,UIT.mobile_no,UIT.fax,UL.login_user_id,decode(UIT.USER_STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized',USER_STATUS)"
					+ " from user_information_temp UIT,USER_LOGIN_CREDENTIALS_TEMP UL"
					+ " where UIT.ref_num=? AND UL.COMMON_ID=UIT.COMMON_ID";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);
			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {
				name = merchantRS.getString(1).split("\\ ");
				resultJson.put("first_name", name[0]);
				resultJson.put("last_name", name[1]);
				resultJson.put("emp_id", merchantRS.getString(2));
				resultJson.put("expiry_date", merchantRS.getString(3));
				resultJson.put("user_level", merchantRS.getString(4));
				resultJson.put("location", merchantRS.getString(5));
				resultJson.put("email", merchantRS.getString(6));
				resultJson.put("res_no", merchantRS.getString(7));
				resultJson.put("off_no", merchantRS.getString(8));
				resultJson.put("mobile_no", merchantRS.getString(9));
				resultJson.put("fax", merchantRS.getString(10));
				resultJson.put("user_id", merchantRS.getString(11));
				resultJson.put("user_status", merchantRS.getString(12));
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			
			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO ServiceAuthRecordData(RequestDTO requestDTO) {

		logger.debug("Inside UserAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		// String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			// auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			merchantQry = "select SERVICE_CODE,SERVICE_NAME,decode(STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized') from SERVICE_MASTER_TEMP where REF_NUM=?";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);

			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {

				resultJson.put("ServiceCode", merchantRS.getString(1));
				resultJson.put("ServiceName", merchantRS.getString(2));
				resultJson.put("Status", merchantRS.getString(3));

			}
			
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO binAuthRecordData(RequestDTO requestDTO) {

		logger.debug("Inside binAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			merchantQry = "Select BANK_CODE,BANK_NAME,BIN,BIN_DESC,decode(STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized') from BANK_MASTER_TEMP WHERE REF_NUM=?";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);

			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {

				resultJson.put("bankCode", merchantRS.getString(1));
				resultJson.put("bankName", merchantRS.getString(2));
				resultJson.put("bin", merchantRS.getString(3));
				resultJson.put("binDescription", merchantRS.getString(4));
				resultJson.put("status", merchantRS.getString(5));

			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO subServiceAuthRecordData(RequestDTO requestDTO) {

		logger.debug("Inside SubServiceAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + (connection ) + "]");

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			merchantQry = "select SERVICE_CODE,SERVICE_NAME,decode(STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized'),SUB_SERVICE_CODE,SUB_SERVICE_NAME from SERVICE_MASTER_TEMP where  SERVICE_TYPE ='SUB' and  REF_NUM=?";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);

			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {
				resultJson.put("ServiceCode", merchantRS.getString(1));
				resultJson.put("ServiceName", merchantRS.getString(2));
				resultJson.put("Status", merchantRS.getString(3));
				resultJson.put("subserviceCode", merchantRS.getString(4));
				resultJson.put("subServiceName", merchantRS.getString(5));

			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			

			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO viewFeeDetails(RequestDTO requestDTO) {

		logger.debug("Inside FeeCodeAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + (connection ) + "]");

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			String slabDet = "";
			/*
			 * merchantQry =
			 * "select SERVICE_CODE,SERVICE_NAME,decode(STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized'),SUB_SERVICE_CODE,SUB_SERVICE_NAME from SERVICE_MASTER_TEMP where  SERVICE_TYPE ='SUB' and  REF_NUM=?"
			 * ;
			 */

			merchantQry = "Select (select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SM.SERVICE_NAME and rownum<2),"
					+ "SM.SUB_SERVICE_NAME,FM.FEE_CODE,FM.SERVICE_CODE,FM.SUB_SERVICE_CODE,NVL(FM.FLAT_PERCENT,' '),FM.FEE_STRING,"
					+ "FM.SLABDETAILS,FM.ACQDETAILS,FM.MAKER_ID,to_char(FM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FM.ONUSOFUSFLAG  "
					+ "from FEE_MASTER_TEMP FM,SERVICE_MASTER SM where SM.SERVICE_CODE=FM.SERVICE_CODE and "
					+ "SM.SUB_SERVICE_CODE=FM.SUB_SERVICE_CODE and  FM.REF_NUM=?";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);

			merchantRS = merchantPstmt.executeQuery();
			int i = 0;
			while (merchantRS.next()) {

				resultJson.put("SERVICE_NAME", merchantRS.getString(1));
				resultJson.put("SUB_SERVICE_NAME", merchantRS.getString(2));
				resultJson.put("FEE_CODE", merchantRS.getString(3));
				resultJson.put("SERVICE_CODE", merchantRS.getString(4));
				resultJson.put("SUB_SERVICE_CODE", merchantRS.getString(5));
				resultJson.put("FALT_PERCENT", merchantRS.getString(6));
				resultJson.put("ACCOUNT_MULTI_DATA", merchantRS.getString(7));

				if (i == 0) {
					slabDet += merchantRS.getString(8);
				} else {
					slabDet += "#" + merchantRS.getString(8);
				}

				resultJson.put("ACQDET", merchantRS.getString(9));
				resultJson.put("MAKER_ID", merchantRS.getString(10));
				resultJson.put("MAKER_DATE", merchantRS.getString(11));

				resultJson.put("ONUS_OFFUS_FLAG", merchantRS.getString(12));
				i++;
			}

			resultJson.put("SLAB", slabDet);

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO viewTerminalDetails(RequestDTO requestDTO) {

		logger.debug("Inside TerminalAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + (connection) + "]");

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			merchantQry = " SELECT MERCHANT_ID ,STORE_ID,TERMINAL_ID,MODEL_NO,SERIAL_NO,TERMINAL_MAKE,TERMINAL_DATE,decode(STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized') "
					+ " FROM TERMINAL_MASTER_TEMP WHERE REF_NUM = ?";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);

			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {

				resultJson.put("merchantID", merchantRS.getString(1));
				resultJson.put("StoreID", merchantRS.getString(2));
				resultJson.put("TerminalID", merchantRS.getString(3));
				resultJson.put("ModelNumber", merchantRS.getString(4));
				resultJson.put("SerialNumber", merchantRS.getString(5));
				resultJson.put("TerminalMake", merchantRS.getString(6));
				resultJson.put("terminaldate", merchantRS.getString(7));
				resultJson.put("status", merchantRS.getString(8));

			}

			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO viewStoreDetails(RequestDTO requestDTO) {

		logger.debug("Inside TerminalAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;
		PreparedStatement psmt = null;

		String ref_no = "";
		JSONArray bankArray = null;
		JSONArray agentArray = null;
		ResultSet rs = null;
		String name[] = null;
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;
			json = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			logger.debug("connection is  [" + (connection) + "]");

			ref_no = requestJSON.getString("ref_no");

			merchantQry = " Select MERCHANT_ID,STORE_ID,STORE_NAME,LOCATION,MANAGER_NAME,EMAIL,ADDRESS,CITY,PO_BOX_NO,TELEPHONE_NO,MOBILE_NO,FAX_NO,PRI_CONTACT_NAME,PRI_CONTACT_NO,KRA_PIN,TILL_ID,"
					+ " (select AGEN_OR_BILLER  from MERCHANT_MASTER where MERCHANT_ID=SM.MERCHANT_ID) ,AREA,POSTALCODE,LRNUMBER,COUNTRY,"
					+ " (select MM.MERCHANT_ADMIN  from MERCHANT_MASTER MM where MM.MERCHANT_ID=SM.MERCHANT_ID), "
					+ " SM.MAKER_ID,to_char(SM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'), SM.AUTHID,to_char(SM.AUTHDTTM ,'DD-MM-YYYY HH24:MI:SS')"
					+ " from STORE_MASTER_TEMP SM where  SM.REF_NUM=?";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);

			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {

				resultJson.put("merchantID", merchantRS.getString(1));
				resultJson.put("StoreID", merchantRS.getString(2));
				resultJson.put("StoreName", merchantRS.getString(3));
				resultJson.put("location", merchantRS.getString(4));
				resultJson.put("managername", merchantRS.getString(5));
				resultJson.put("email", merchantRS.getString(6));
				resultJson.put("address", merchantRS.getString(7));
				resultJson.put("city", merchantRS.getString(8));
				resultJson.put("poboxnum", merchantRS.getString(9));
				resultJson.put("telephonenum", merchantRS.getString(10));
				resultJson.put("mobile", merchantRS.getString(11));
				resultJson.put("fax", merchantRS.getString(12));
				resultJson.put("pricontactname", merchantRS.getString(13));
				resultJson.put("pricontactnum", merchantRS.getString(14));
				resultJson.put("kra_pin", merchantRS.getString(15));
				resultJson.put("titleid", merchantRS.getString(16));
				resultJson.put("merchnatType", merchantRS.getString(17));
				resultJson.put("area", merchantRS.getString(18));
				resultJson.put("postalcode", merchantRS.getString(19));
				resultJson.put("lrnumber", merchantRS.getString(20));
				resultJson.put("merchanttest", merchantRS.getString(21));
				resultJson.put("makerid", merchantRS.getString(22));
				resultJson.put("makerdttm", merchantRS.getString(23));
				resultJson.put("authid", merchantRS.getString(24));
				resultJson.put("authidttm", merchantRS.getString(25));

			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			String merchantQry1 = "SELECT MM.MERCHANT_NAME FROM MERCHANT_MASTER MM,STORE_MASTER_TEMP SM WHERE SM.MERCHANT_ID=MM.MERCHANT_ID AND SM.REF_NUM=?";
			psmt = connection.prepareStatement(merchantQry1);
			psmt.setString(1, ref_no);
			rs = psmt.executeQuery();
			String merchantName;

			if (rs.next()) {
				merchantName = rs.getString(1);
				logger.debug("Merchant Name:[" + merchantName + "]");
				resultJson.put("merchantName", merchantName);
			}
			
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(psmt);

			String DoucnemtQry = "SELECT ACCT_NO,ACCT_DESC,BANK_NAME,BANK_BRANCH FROM BANK_ACCT_MASTER WHERE REF_NUM=?";

			psmt = connection.prepareStatement(DoucnemtQry);
			psmt.setString(1, ref_no);
			rs = psmt.executeQuery();

			bankArray = new JSONArray();
			if (rs.next()) { 
				json.put("accountNO", rs.getString(1));
				json.put("accountDesc", rs.getString(2));
				json.put("bankcode", rs.getString(3));
				json.put("branccode", rs.getString(4));
				bankArray.add(json);
				json.clear();
			}

			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(psmt);
			logger.debug("Bankaccountmultidata:::[" + bankArray + "]");
			resultJson.put("BankmultData", bankArray);

			String AgentQry1 = "SELECT BANK_AGENT_NO,MPESA_AGENT_NO,AIRTEL_AGENT_NO,ORANGE_AGENT_NO,MAKER_ID FROM AGENT_INFORMATION WHERE REF_NUM=?";

			psmt = connection.prepareStatement(AgentQry1);
			psmt.setString(1, ref_no);
			rs = psmt.executeQuery();
			agentArray = new JSONArray();
			if (rs.next()) {
				json.put("bankagentno", rs.getString(1));
				json.put("mpesaagentno", rs.getString(2));
				json.put("airtelagentno", rs.getString(3));
				json.put("orangeagentno", rs.getString(4));
				json.put("makerid", rs.getString(5));
				agentArray.add(json);
				json.clear();
			}

			logger.debug("agent json:::[" + agentArray + "]");
			resultJson.put("agentmultidate", agentArray);

			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(psmt);
			
			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO getMerchantViewDetails(RequestDTO requestDTO) {

		PreparedStatement merchantprmPstmt = null;
		ResultSet merchantPrmRS = null;
		Connection connection = null;

		logger.debug("Inside GetMerchantViewDetails... ");
		HashMap<String, Object> merchantDataMap = null;

		JSONArray jsonArray = null;
		JSONObject json = null;
		String merchantPrmQry = "Select MM.MERCHANT_ID,MM.MERCHANT_NAME,(select office_name from POSTA_BRANCH_MASTER where office_code=MM.LOCATION), "
				+ "MM.KRA_PIN,(select CATE_DESCRIPTION from CATEGORY_MASTER where cate_code=MM.MERCHANT_TYPE),"
				+ " MM.MANAGER_NAME,MM.EMAIL,MM.ADDRESS,MM.CITY,MM.PO_BOX_NO,MM.TELEPHONE_NO,MM.MOBILE_NO,MM.FAX_NO,MM.PRI_CONTACT_NAME, "
				+ "MM.PRI_CONTACT_NO,MM.AGEN_OR_BILLER,MM.AREA,MM.POSTALCODE,MM.LRNUMBER,MM.COUNTRY,MM.MERCHANT_ADMIN,MM.STATUS,MM.MAKER_ID,"
				+ "to_char(MM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),MM.AUTHID,to_char(MM.AUTHDTTM ,'DD-MM-YYYY HH24:MI:SS')"
				+ "from MERCHANT_MASTER_TEMP MM where ref_num=?";
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  " + connection + "]");
			merchantDataMap = new HashMap<String, Object>();
			merchantprmPstmt = connection.prepareStatement(merchantPrmQry);
			merchantprmPstmt.setString(1, requestJSON.getString("ref_no"));

			merchantPrmRS = merchantprmPstmt.executeQuery();
			while (merchantPrmRS.next()) {
				responseJSON.put(CevaCommonConstants.MERCHANT_ID,
						merchantPrmRS.getString(1));
				responseJSON.put(CevaCommonConstants.MERCHANT_NAME,
						merchantPrmRS.getString(2));
				responseJSON.put(CevaCommonConstants.LOCATION_NAME,
						merchantPrmRS.getString(3));
				responseJSON.put(CevaCommonConstants.KRA_PIN,
						merchantPrmRS.getString(4));
				responseJSON.put(CevaCommonConstants.MERCHANT_TYPE,
						merchantPrmRS.getString(5));
				responseJSON.put(CevaCommonConstants.MANAGER_NAME,
						merchantPrmRS.getString(6));
				responseJSON.put(CevaCommonConstants.EMAIL,
						merchantPrmRS.getString(7));

				String address = merchantPrmRS.getString(8);
				String[] addressVal = address.split("#");
				String address1 = "";
				String address2 = "";
				// String address3 = "";

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
					// address3 = addressVal[2];
				}

				/*
				 * String telphone = merchantPrmRS.getString(11); String[]
				 * telPhoneArr = telphone.split("#"); String telephone1 = "";
				 * String telephone2 = ""; if (telPhoneArr.length == 1)
				 * telephone1 = telPhoneArr[0]; if (telPhoneArr.length == 2) {
				 * telephone1 = telPhoneArr[0]; telephone2 = telPhoneArr[1]; }
				 */

				responseJSON.put(CevaCommonConstants.ADDRESS1, address1);
				responseJSON.put(CevaCommonConstants.ADDRESS2, address2);
				// responseJSON.put(CevaCommonConstants.ADDRESS3, address3);
				responseJSON.put(CevaCommonConstants.CITY,
						merchantPrmRS.getString(9));
				responseJSON.put(CevaCommonConstants.POBOXNUMBER,
						merchantPrmRS.getString(10));
				// responseJSON.put(CevaCommonConstants.TELEPHONE1, telephone1);
				// responseJSON.put(CevaCommonConstants.TELEPHONE2, telephone2);
				responseJSON.put(CevaCommonConstants.MOBILE_NUMBER,
						merchantPrmRS.getString(11));
				responseJSON.put(CevaCommonConstants.FAX_NUMBER,
						merchantPrmRS.getString(12));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						merchantPrmRS.getString(13));
				responseJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						merchantPrmRS.getString(14));
				responseJSON.put(CevaCommonConstants.MEMBER_TYPE,
						merchantPrmRS.getString(15));
				responseJSON.put("AREA", merchantPrmRS.getString(16));
				responseJSON.put("POSTALCODE", merchantPrmRS.getString(17));
				responseJSON.put("LRNUMBER", merchantPrmRS.getString(18));
				responseJSON.put("COUNTRY", merchantPrmRS.getString(19));
				responseJSON.put("merchantAdmin", merchantPrmRS.getString(20));
				responseJSON.put("merstatus", merchantPrmRS.getString(21));
				responseJSON.put("createId", merchantPrmRS.getString(22));
				responseJSON.put("createDate", merchantPrmRS.getString(23));
				responseJSON.put("authId", merchantPrmRS.getString(24));
				responseJSON.put("authDate", merchantPrmRS.getString(25));

				address = null;
				addressVal = null;
				address1 = null;
				address2 = null;
				/*
				 * address3 = null; telphone = null; telPhoneArr = null;
				 * telephone1 = null; telephone2 = null;
				 */
			}

			DBUtils.closeResultSet(merchantPrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);

			merchantPrmQry = "select USER_NAME,decode(USER_STATUS,'F','First Time Login','A','Active','B','Blocked',USER_STATUS),EMAIL,EMP_ID "
					+ " from USER_INFORMATION UI,USER_LOGIN_CREDENTIALS ULC "
					+ "where ULC.COMMON_ID=UI.COMMON_ID and ULC.LOGIN_USER_ID=(select trim(MERCHANT_ADMIN) from merchant_master_temp where ref_num=?)";
			merchantprmPstmt = connection.prepareStatement(merchantPrmQry);
			merchantprmPstmt.setString(1, requestJSON.getString("ref_no"));

			merchantPrmRS = merchantprmPstmt.executeQuery();
			if (merchantPrmRS.next()) {
				responseJSON.put("userName", merchantPrmRS.getString(1));
				responseJSON.put("userStatus", merchantPrmRS.getString(2));
				responseJSON.put("emailId", merchantPrmRS.getString(3));
				responseJSON.put("employeeNo", merchantPrmRS.getString(4));
			}

			
			DBUtils.closeResultSet(merchantPrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);
			
			String merchantBankAcctQry = "SELECT distinct ACCT_NO,ACCT_DESC,BANK_NAME,BANK_BRANCH,TRANSFER_CODE "
					+ "from BANK_ACCT_MASTER where ref_num=?";

			merchantprmPstmt = connection.prepareStatement(merchantBankAcctQry);
			merchantprmPstmt.setString(1, requestJSON.getString("ref_no"));

			merchantPrmRS = merchantprmPstmt.executeQuery();
			String bankAcctData = "";
			String eachrow = "";
			int i = 0;

			while (merchantPrmRS.next()) {
				eachrow = merchantPrmRS.getString(1) + ","
						+ merchantPrmRS.getString(2) + ","
						+ merchantPrmRS.getString(3) + ","
						+ merchantPrmRS.getString(4) + ","
						+ merchantPrmRS.getString(5);

				if (i == 0) {
					bankAcctData += eachrow;
				} else {
					bankAcctData += "#" + eachrow;
				}
				i++;
			}

			responseJSON.put("bankAcctMultiData", bankAcctData);

			DBUtils.closeResultSet(merchantPrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);

			merchantBankAcctQry = "SELECT distinct BANK_AGENT_NO,MPESA_AGENT_NO,AIRTEL_AGENT_NO,ORANGE_AGENT_NO,MPESA_TILL_NO "
					+ "from AGENT_INFORMATION where ref_num=?";
			merchantprmPstmt = connection.prepareStatement(merchantBankAcctQry);
			merchantprmPstmt.setString(1, requestJSON.getString("ref_no"));

			merchantPrmRS = merchantprmPstmt.executeQuery();
			String agentData = "";
			eachrow = "";
			i = 0;
			while (merchantPrmRS.next()) {
				eachrow = merchantPrmRS.getString(1) + ","
						+ merchantPrmRS.getString(2) + ","
						+ merchantPrmRS.getString(3) + ","
						+ merchantPrmRS.getString(4) + ","
						+ merchantPrmRS.getString(5);

				if (i == 0) {
					agentData += eachrow;
				} else {
					agentData += "#" + eachrow;
				}

				i++;
			}
			responseJSON.put("AgenctAcctMultiData", agentData);

			DBUtils.closeResultSet(merchantPrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);

			merchantBankAcctQry = "SELECT distinct DOC_ID,DOC_DESC,GRACE_PERIOD,MANDATORY "
					+ "from DOCUMENT_DETAILS where ref_num=?";
			merchantprmPstmt = connection.prepareStatement(merchantBankAcctQry);
			merchantprmPstmt.setString(1, requestJSON.getString("ref_no"));

			merchantPrmRS = merchantprmPstmt.executeQuery();
			String documentData = "";
			eachrow = "";
			i = 0;
			while (merchantPrmRS.next()) {
				eachrow = merchantPrmRS.getString(1) + ","
						+ merchantPrmRS.getString(2) + ","
						+ merchantPrmRS.getString(3) + ","
						+ merchantPrmRS.getString(4);

				if (i == 0) {
					documentData += eachrow;
				} else {
					documentData += "#" + eachrow;
				}

				i++;
			}
			responseJSON.put("documentMultiData", documentData);

			// Below Are Fetching For Store Details
			DBUtils.closeResultSet(merchantPrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);

			jsonArray = new JSONArray();
			json = new JSONObject();

			merchantBankAcctQry = "Select SM.STORE_ID,SM.STORE_NAME,"
					+ "Decode(SM.STATUS,'A','Active','B','Inactive','N','Un-Authorize'),"
					+ "SM.MAKER_ID,to_char(SM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),"
					+ "SM.AUTHID,to_char(SM.AUTHDTTM ,'DD-MM-YYYY HH24:MI:SS')"
					+ " from MERCHANT_MASTER_TEMP MM,STORE_MASTER_TEMP SM "
					+ "where SM.MERCHANT_ID=MM.MERCHANT_ID and MM.REF_NUM=?  order by SM.STORE_ID";

			merchantprmPstmt = connection.prepareStatement(merchantBankAcctQry);
			merchantprmPstmt.setString(1, requestJSON.getString("ref_no"));
			merchantPrmRS = merchantprmPstmt.executeQuery();
			json.clear();

			while (merchantPrmRS.next()) {
				json.put(CevaCommonConstants.STORE_ID,
						merchantPrmRS.getString(1));
				json.put(CevaCommonConstants.STORE_NAME,
						merchantPrmRS.getString(2));
				json.put(CevaCommonConstants.STATUS, merchantPrmRS.getString(3));
				json.put(CevaCommonConstants.MAKER_ID,
						merchantPrmRS.getString(4));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantPrmRS.getString(5));
				json.put("authid", merchantPrmRS.getString(6));
				json.put("authdttm", merchantPrmRS.getString(7));

				jsonArray.add(json);
				json.clear();
			}

			responseJSON.put("storeData", jsonArray);

			DBUtils.closeResultSet(merchantPrmRS);
			DBUtils.closePreparedStatement(merchantprmPstmt);

			logger.debug("Response JSON [" + responseJSON + "]");
			merchantDataMap.put("user_rights", responseJSON);
			logger.debug("MerchantData Map [" + merchantDataMap + "]");
			responseDTO.setData(merchantDataMap);

			merchantBankAcctQry = null;

		} catch (SQLException e) {
			logger.debug("SQLException in GetMerchantViewDetails is ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			logger.debug("Exception in GetMerchantViewDetails is ["
					+ e.getMessage() + "]");
		} finally {
			try {

				DBUtils.closeResultSet(merchantPrmRS);
				DBUtils.closePreparedStatement(merchantprmPstmt);

				DBUtils.closeConnection(connection);

			} catch (Exception e) {

			}
			merchantPrmQry = null;
			merchantDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO getUserActiveDeactiveDetails(RequestDTO requestDTO) {

		logger.debug("Inside UserAuthRecordData.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + (connection ) + "]");

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			/*
			 * merchantQry =
			 * "select user_name ,emp_id ,to_char(EXPIRY_DATE,'DD-MM-YYYY')," +
			 * "user_level,(select distinct cbl.OFFICE_NAME from IMPERIAL_BRANCH_MASTER cbl where cbl.office_code=location),"
			 * +
			 * "email,LOCAL_RES_NUM,LOCAL_OFF_NUM,mobile_no,fax,(select login_user_id from user_login_credentials_temp where ref_no =? ),decode(USER_STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized',USER_STATUS) from user_information_temp "
			 * + "where ref_no=?";
			 */
			merchantQry = "select user_name ,emp_id ,to_char(EXPIRY_DATE,'DD/MM/YYYY'),"
					+ " user_level,location,email,LOCAL_RES_NUM,LOCAL_OFF_NUM,mobile_no,fax,"
					+ " decode(USER_STATUS,'A','Active','L','De-Active','F','InActive',USER_STATUS) "
					+ " from user_information where    common_id =(select common_id from USER_INFORMATION_TEMP where  REF_NO =?) ";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);
			// merchantPstmt.setString(2, ref_no);
			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {
				name = merchantRS.getString(1).split("\\ ");
				resultJson.put("CV0001", "BANK0001");
				resultJson.put("CV0003", name[0]);
				resultJson.put("CV0004", name[1]);
				resultJson.put("CV0002", merchantRS.getString(2));
				resultJson.put("CV0011", merchantRS.getString(3));
				resultJson.put("CV0009", merchantRS.getString(4));
				resultJson.put("CV0010", merchantRS.getString(5));
				resultJson.put("CV0012", merchantRS.getString(6));
				resultJson.put("CV0005", merchantRS.getString(7));
				resultJson.put("CV0006", merchantRS.getString(8));
				resultJson.put("CV0007", merchantRS.getString(9));
				resultJson.put("CV0008", merchantRS.getString(10));
				resultJson.put("CV0013", merchantRS.getString(11));

			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

	public ResponseDTO TerminalMigrationDetails(RequestDTO requestDTO) {

		logger.debug("Inside Terminal Migration Confirm.. ");

		HashMap<String, Object> merchantMap = null;
		String merchantQry = "";
		JSONObject resultJson = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String auth_code = "";
		String status = "";
		String ref_no = "";

		String name[] = null;
		try {
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			resonseJSON = requestJSON;

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + (connection) + "]");

			auth_code = requestJSON.getString("auth_code");
			status = requestJSON.getString("status");
			ref_no = requestJSON.getString("ref_no");

			/*
			 * merchantQry =
			 * "select user_name ,emp_id ,to_char(EXPIRY_DATE,'DD-MM-YYYY')," +
			 * "user_level,(select distinct cbl.OFFICE_NAME from IMPERIAL_BRANCH_MASTER cbl where cbl.office_code=location),"
			 * +
			 * "email,LOCAL_RES_NUM,LOCAL_OFF_NUM,mobile_no,fax,(select login_user_id from user_login_credentials_temp where ref_no =? ),decode(USER_STATUS,'A','Active','L','De-Active','F','InActive','N','Not Authorized',USER_STATUS) from user_information_temp "
			 * + "where ref_no=?";
			 */
			merchantQry = "SELECT OLD_MERCHANTID,OLD_STOREID,TERMINALID,NEW_MERCHANTID,NEW_STOREID FROM TERMINAL_MIGRATION_TEMP WHERE REF_NO=? ";

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, ref_no);
			// merchantPstmt.setString(2, ref_no);
			merchantRS = merchantPstmt.executeQuery();

			if (merchantRS.next()) {

				resultJson.put("merchantID", merchantRS.getString(1));
				resultJson.put("storeID", merchantRS.getString(2));
				resultJson.put("terminalID", merchantRS.getString(3));
				resultJson.put("updatemerchantID", merchantRS.getString(4));
				resultJson.put("updatestoreID", merchantRS.getString(5));
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetUserDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in GetUserDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			merchantQry = null;
			resultJson = null;
			name = null;
		}

		return responseDTO;
	}

}
