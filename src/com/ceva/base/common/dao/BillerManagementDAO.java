package com.ceva.base.common.dao;

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

import com.ceva.base.common.bean.BillerBean;
import com.ceva.base.common.bean.BulkBean;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.posta.util.PostaMobileEncryptTripleDes;
import com.ceva.util.CommonUtil;
import com.ceva.util.DBUtils;

public class BillerManagementDAO {

	private Logger logger = Logger.getLogger(BillerManagementDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getBillerRelatedInfo(RequestDTO requestDTO) {

		logger.debug("Inside BillerCategoryPstmt DAO ... ");
		HashMap<String, Object> billerDataMap = null;
		JSONObject resultJson = null;
		JSONArray billerCategoryJSONArray = null;

		PreparedStatement billerCategoryPstmt = null;
		ResultSet billerCategoryRS = null;
		Connection connection = null;

		String billerCategoryQry = "";
		JSONObject json = null;

		try {
			billerDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			billerCategoryJSONArray = new JSONArray();
			responseDTO = new ResponseDTO();
			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection + "]");

			billerCategoryQry = "Select KEY_VALUE from CONFIG_DATA where KEY_GROUP=? and KEY_TYPE=? order by KEY_ID";

			billerCategoryPstmt = connection
					.prepareStatement(billerCategoryQry);
			billerCategoryPstmt.setString(1, CevaCommonConstants.KEY_BCL_GROUP);
			billerCategoryPstmt.setString(2, CevaCommonConstants.KEY_BCL_TYPE);
			billerCategoryRS = billerCategoryPstmt.executeQuery();

			json = new JSONObject();
			while (billerCategoryRS.next()) {

				json.put(CevaCommonConstants.SELECT_KEY,
						billerCategoryRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL,
						billerCategoryRS.getString(1));
				billerCategoryJSONArray.add(json);
				json.clear();
			}
			logger.debug("BillerCategoryJSONArray [" + billerCategoryJSONArray
					+ "]");

			resultJson.put(CevaCommonConstants.BILLER_CATEGORY_LIST,
					billerCategoryJSONArray);

			billerDataMap.put(CevaCommonConstants.BILLER_REL_INFO, resultJson);
			logger.debug("BillerDataMap [" + billerDataMap + "]");

			responseDTO.setData(billerDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in getBillerRelatedInfo ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in getBillerRelatedInfo [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closePreparedStatement(billerCategoryPstmt);
			DBUtils.closeResultSet(billerCategoryRS);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
			resultJson = null;
			billerCategoryJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO getBillerCustomerData(RequestDTO requestDTO) {

		logger.debug("Inside GetBillerCustomerData DAO ... ");

		JSONArray jsonArray = null;

		String dataVal = null;

		String query = null;

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		JSONObject json = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			billerDataMap = new HashMap<String, Object>();
			jsonArray = new JSONArray();
			json = new JSONObject();

			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection + "]");
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, dataVal + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				json.put("ACCT_NO", rs.getString(1));
				json.put("CUSTOMER_NAME", rs.getString(2));
				json.put("EMAIL", rs.getString(3));
				json.put("MAKER_ID", rs.getString(4));
				json.put("MAKER_DATE", rs.getString(5));
				jsonArray.add(json);
				json.clear();
			}

			responseJSON.put("CUSTOMER_DATA", jsonArray);
			billerDataMap.put("CUSTOMER_DATA", responseJSON);
			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in GetBillerCustomerData ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in GetBillerCustomerData ["
					+ e.getMessage() + "]");
		} finally {
			jsonArray = null;
			dataVal = null;
			query = null;
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertBulkData(RequestDTO requestDTO) {

		logger.debug("Inside InsertBulkData DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject bulkBean = null;
		String pin = "";

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			bulkBean = requestJSON.getJSONObject("bulkBean");
			pin = CommonUtil.generatePin(4);
			billerDataMap = new HashMap<String, Object>();
			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection+ "]");
			callable = connection
					.prepareCall("{call insertBulkDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			callable.setString(1, bulkBean.getString("name"));
			callable.setString(2, bulkBean.getString("abbreviation"));
			callable.setString(3, bulkBean.getString("fullName"));
			callable.setString(4, bulkBean.getString("email"));
			callable.setString(5, bulkBean.getString("mobileNo"));
			callable.setString(6, bulkBean.getString("phoneNumber1"));
			callable.setString(7, bulkBean.getString("phoneNumber2"));
			callable.setString(8, bulkBean.getString("phoneNumber3"));
			callable.setString(9, bulkBean.getString("daysToDisburse"));
			callable.setString(10, bulkBean.getString("organizationType"));
			callable.setString(11, pin);
			callable.setString(12, PostaMobileEncryptTripleDes.encrypt(pin));
			callable.setString(13, requestJSON.getString("makerId"));
			callable.registerOutParameter(14, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(14).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in InsertBillerData [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in InsertBillerData [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertBillerData(RequestDTO requestDTO) {

		logger.debug("Inside InsertBillerData DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			billerBean = requestJSON.getJSONObject("billerBean");

			billerDataMap = new HashMap<String, Object>();

			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection + "]");
			callable = connection.prepareCall("{call insertBillerDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			callable.setString(1, billerBean.getString("abbreviation"));
			callable.setString(2, billerBean.getString("billerCode"));
			callable.setString(3, billerBean.getString("name"));
			callable.setString(4, billerBean.getString("agencyType"));
			callable.setString(5, billerBean.getString("accountType"));
			callable.setString(6, billerBean.getString("accountNo"));
			callable.setString(7, billerBean.getString("bankName"));
			callable.setString(8, billerBean.getString("address"));
			callable.setString(9, billerBean.getString("telephone"));
			callable.setString(10, billerBean.getString("contactPerson"));
			callable.setString(11, billerBean.getString("email"));
			callable.setString(12, billerBean.getString("commissionType"));
			callable.setString(13, billerBean.getString("amount"));
			callable.setString(14, billerBean.getString("rate"));
			callable.setString(15, requestJSON.getString("makerId"));
			callable.registerOutParameter(16, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(16).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in InsertBillerData [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in InsertBillerData [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO editBulkOrgDetailsAck(RequestDTO requestDTO) {

		logger.debug("Inside InsertBulkData DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject bulkBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			bulkBean = requestJSON.getJSONObject("bulkBean");
			// pin = CommonUtil.generatePin(4);
			billerDataMap = new HashMap<String, Object>();
			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection + "]");
			callable = connection
					.prepareCall("{call modifyBulkDetails(?,?,?,?,?,?,?,?,?,?,?,?)}");
			callable.setString(1, bulkBean.getString("name"));
			callable.setString(2, bulkBean.getString("abbreviation"));
			callable.setString(3, bulkBean.getString("fullName"));
			callable.setString(4, bulkBean.getString("email"));
			callable.setString(5, bulkBean.getString("mobileNo"));
			callable.setString(6, bulkBean.getString("phoneNumber1"));
			callable.setString(7, bulkBean.getString("phoneNumber2"));
			callable.setString(8, bulkBean.getString("phoneNumber3"));
			callable.setString(9, bulkBean.getString("daysToDisburse"));
			callable.setString(10, bulkBean.getString("orgId"));
			callable.setString(11, requestJSON.getString("makerId"));
			callable.registerOutParameter(12, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(12).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in InsertBillerData [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in InsertBillerData [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO updateBillerData(RequestDTO requestDTO) {

		logger.debug("Inside UpdateBillerData DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			billerBean = requestJSON.getJSONObject("billerBean");

			billerDataMap = new HashMap<String, Object>();

			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection + "]");
			callable = connection
					.prepareCall("{call updateBillerDetails(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			callable.setString(1, billerBean.getString("abbreviation"));
			callable.setString(2, billerBean.getString("billerCode"));
			callable.setString(3, billerBean.getString("name"));
			callable.setString(4, billerBean.getString("agencyType"));
			callable.setString(5, billerBean.getString("accountType"));
			callable.setString(6, billerBean.getString("accountNo"));
			callable.setString(7, billerBean.getString("billerType"));
			callable.setString(8, billerBean.getString("address"));
			callable.setString(9, billerBean.getString("telephone"));
			callable.setString(10, billerBean.getString("contactPerson"));
			callable.setString(11, billerBean.getString("email"));
			callable.setString(12, billerBean.getString("commissionType"));
			callable.setString(13, billerBean.getString("amount"));
			callable.setString(14, billerBean.getString("rate"));
			callable.setString(15, requestJSON.getString("makerId"));
			callable.registerOutParameter(16, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(16).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in UpdateBillerData [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in UpdateBillerData [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO viewOrganizations(RequestDTO requestDTO) {

		logger.debug("Inside view Organizations DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";

		JSONArray jsonArray = null;
		JSONObject json = null;
		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();

			billerDataMap = new HashMap<String, Object>();
			json = new JSONObject();
			jsonArray = new JSONArray();

			query = "select WO.ORGANIZATIONID,WO.ORGANIZATIONNAME,WO.ORGANIZATIONABBREV,WO.CREATEDBY,"
					+ "to_char(WO.DATECREATED,'DD-MON-YYYY HH24:MI:SS'),decode(WO.ORGANIZATIONTYPE,'S','Small','L','Large',WO.ORGANIZATIONTYPE)  "
					+ "from W_Organizations WO";
			connection = DBConnector.getConnection();
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				json.put("ORG_ID", rs.getString(1));
				json.put("ORG_NAME", rs.getString(2));
				json.put("ORG_ABBR", rs.getString(3));
				json.put("MAKER_ID", rs.getString(4));
				json.put("MAKER_DTTM", rs.getString(5));
				json.put("ORG_TYPE", rs.getString(6));
				jsonArray.add(json);
				json.clear();
			}

			responseJSON.put("ORG_DATA", jsonArray);

			query = null;
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			jsonArray.clear();

			/*query = "select wp.FULLNAME,wp.MSISDN,wp.EMAILADDRESS,wp.CREATEDBY,to_char(wp.DATECREATED,'DD-MON-YYYY HH24:MI:SS'),"
					+ "wp.PROFILEID from W_PROFILES wp ";*/
			query = "select wp.FULLNAME,wp.MSISDN,wp.EMAILADDRESS,wp.CREATEDBY,to_char(wp.DATECREATED,'DD-MON-YYYY HH24:MI:SS'),"
					+ "wp.PROFILEID from W_PROFILES wp where wp.PROFILEID not in (select PROFILEID from W_ORGANIZATION_PROFILES)";
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				json.put("PRF_NAME", rs.getString(1));
				json.put("MOBILE", rs.getString(2));
				json.put("EMAIL", rs.getString(3));
				json.put("MAKER_ID", rs.getString(4));
				json.put("MAKER_DTTM", rs.getString(5));
				json.put("ID", rs.getString(6));
				jsonArray.add(json);
				json.clear();
			}

			responseJSON.put("PROFILE_DATA", jsonArray);

			billerDataMap.put("ORG_DATA", responseJSON);
			responseDTO.setData(billerDataMap);

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in view organizations ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in viewOrganizations [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO viewBulkOrgDetails(RequestDTO requestDTO) {
		logger.debug("Inside View Bulk Organization Details DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";

		BulkBean bulkBean = null;
		String contact[] = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.info("Request Json In Dao[" + requestJSON + "]");

			billerDataMap = new HashMap<String, Object>();

			query = "select WO.ORGANIZATIONNAME,WO.ORGANIZATIONABBREV,WO.CREATEDBY, "
					+ "to_char(WO.DATECREATED,'DD-MON-YYYY HH24:MI:SS'),"
					+ "(select PROFILEPINSTATUSNAME from W_PROFILEPINSTATUS where PROFILEPINSTATUSID=WO.STATUS)"
					+ ",WO.CONTACT_DETAILS,WP.FULLNAME,WP.MSISDN,WO.DAYS_TO_DISBURSE, WO.EMAILADDRESS "
					+ "from W_Organizations WO,W_PROFILES WP,W_ORGANIZATION_PROFILES WOP "
					+ "where WO.ORGANIZATIONID=WOP.ORGANIZATIONID AND WP.PROFILEID = WOP.PROFILEID AND WO.ORGANIZATIONID=?";
			connection = DBConnector.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, requestJSON.getString("orgId"));
			rs = pstmt.executeQuery();
			bulkBean = new BulkBean();

			if (rs.next()) {

				bulkBean.setName(rs.getString(1));
				bulkBean.setAbbreviation(rs.getString(2));
				bulkBean.setCreatedBy(rs.getString(3));
				bulkBean.setCreatedDate(rs.getString(4));
				bulkBean.setStatus(rs.getString(5));
				bulkBean.setFullName(rs.getString(7));
				bulkBean.setMobileNo(rs.getString(8));
				bulkBean.setDaysToDisburse(rs.getString(9));

				try {
					contact = (rs.getString(6) == null ? new String[] { " ",
							" ", " " } : rs.getString(6).split("#"));

					bulkBean.setPhoneNumber1(contact[0]);
					bulkBean.setPhoneNumber2(contact[1]);
					bulkBean.setPhoneNumber3(contact[2]);
				} catch (Exception e) {
					bulkBean.setPhoneNumber1(" ");
					bulkBean.setPhoneNumber2(" ");
					bulkBean.setPhoneNumber3(" ");
				}

				bulkBean.setEmail(rs.getString(10));
				bulkBean.setOrgId(requestJSON.getString("orgId"));
			}

			// responseJSON.put("ORG_DATA", bulkBean);
			billerDataMap.put("ORG_DATA", bulkBean);
			responseDTO.setData(billerDataMap);

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in view Bulk organizations ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in view Organizations [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO viewProfileDetails(RequestDTO requestDTO) {
		logger.debug("Inside View Profile Details DAO ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "";

		BulkBean bulkBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.info("Request Json In Dao[" + requestJSON + "]");

			billerDataMap = new HashMap<String, Object>();

			query = "select  WP.FULLNAME,WP.EMAILADDRESS,WP.CREATEDBY, "
					+ "to_char(WP.DATECREATED,'DD-MON-YYYY HH24:MI:SS'),"
					+ "(select PROFILEPINSTATUSNAME from W_PROFILEPINSTATUS where PROFILEPINSTATUSID=WP.STATUS)"
					+ ", WP.MSISDN, to_number(WPB.BALANCE)/100 "
					+ "from  W_PROFILES WP,W_PROFILEBALANCES WPB  where WP.PROFILEID=WPB.PROFILE_ID and WP.PROFILEID=?";

			connection = DBConnector.getConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, requestJSON.getString("id"));
			rs = pstmt.executeQuery();
			bulkBean = new BulkBean();

			if (rs.next()) {
				bulkBean.setName(rs.getString(1));
				bulkBean.setEmail(rs.getString(2));
				bulkBean.setCreatedBy(rs.getString(3));
				bulkBean.setCreatedDate(rs.getString(4));
				bulkBean.setStatus(rs.getString(5));
				bulkBean.setMobileNo(rs.getString(6));
				bulkBean.setBalance(rs.getString(7));
				bulkBean.setId(requestJSON.getString("id"));
			}

			// responseJSON.put("ORG_DATA", bulkBean);
			billerDataMap.put("PRF_DATA", bulkBean);
			responseDTO.setData(billerDataMap);

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in view Profiles [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in view Organizations [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}
		return responseDTO;
	}

	public ResponseDTO viewRegisteredBillers(RequestDTO requestDTO) {

		logger.debug("Inside View Registered Billers... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;

		JSONArray jsonArray = null;

		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		BillerBean billerBean = null;

		String query = "select BILLER_ID,NAME,ABBREVATION,EMAIL,MAKER_ID,to_char(MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS'),"
				+ "COMM_TYPE,to_number(AMOUNT/100) from Biller_Registration ";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			jsonArray = new JSONArray();

			connection = DBConnector.getConnection();

			logger.debug("Connection is [" + connection + "]");

			prepareStatement = connection.prepareStatement(query);
			resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				billerBean = new BillerBean();
				billerBean.setBillerCode(resultSet.getString(1));
				billerBean.setName(resultSet.getString(2));
				billerBean.setAbbreviation(resultSet.getString(3));
				billerBean.setEmail(resultSet.getString(4));
				billerBean.setMakerId(resultSet.getString(5));
				billerBean.setMakerDttm(resultSet.getString(6));
				billerBean.setCommissionType(resultSet.getString(7));
				billerBean.setAmount(resultSet.getString(8));
				jsonArray.add(billerBean);
				billerBean.clear();
			}

			resultJson.put("bank_data", jsonArray);
			BillerBean biller1 = new BillerBean();
			biller1.setResponseJSON(resultJson);

			storeMap.put("bank_data", biller1);

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
		}

		return responseDTO;
	}

	public ResponseDTO viewBillerInDetails(RequestDTO requestDTO) {

		logger.debug("Inside View Registered Billers... ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		BillerBean billerBean = null;

		String query = "select NAME,ABBREVATION,EMAIL,MAKER_ID,to_char(MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS'),"
				+ "COMM_TYPE,to_number(AMOUNT/100),ADDRESS,ACCOUNT_TYPE,AGENCY_TYPE,AUTHORIZE_ID,"
				+ "to_char(AUTHORIZE_DTTM,'DD-MON-YYYY HH24:MI:SS'),CONTACT_PERSON,TELEPHONE,ACCT_NO,BILLER_TYPE from Biller_Registration where BILLER_ID=?";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();

			storeMap = new HashMap<String, Object>();

			connection = DBConnector.getConnection();

			logger.debug("Connection is [" + connection + "]");

			prepareStatement = connection.prepareStatement(query);
			prepareStatement.setString(1, requestJSON.getString("billerid"));
			resultSet = prepareStatement.executeQuery();
			billerBean = new BillerBean();

			if (resultSet.next()) {
				billerBean.setBillerCode(requestJSON.getString("billerid"));
				billerBean.setName(resultSet.getString(1));
				billerBean.setAbbreviation(resultSet.getString(2));
				billerBean.setEmail(resultSet.getString(3));
				billerBean.setMakerId(resultSet.getString(4));
				billerBean.setMakerDttm(resultSet.getString(5));
				billerBean.setCommissionType(resultSet.getString(6));
				billerBean.setAmount(resultSet.getString(7));
				billerBean.setAddress(resultSet.getString(8));
				billerBean.setAccountType(resultSet.getString(9));
				billerBean.setAgencyType(resultSet.getString(10));
				billerBean.setAuthorizeId(resultSet.getString(11));
				billerBean.setAuthorizeDttm(resultSet.getString(12));
				billerBean.setContactPerson(resultSet.getString(13));
				billerBean.setTelephone(resultSet.getString(14));
				billerBean.setAccountNo(resultSet.getString(15));
				billerBean.setBillerType(resultSet.getString(16));
			}

			storeMap.put("bank_data", billerBean);

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
		}

		return responseDTO;
	}

	public ResponseDTO getCustomerInDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetCustomerInDetails... ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;

		CallableStatement callableStatement = null;
		ResultSet resultSet = null;

		BillerBean billerBean = null;

		String query = "";
		String message = "";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();

			storeMap = new HashMap<String, Object>();

			connection = DBConnector.getConnection();

			logger.debug("Connection is [" + connection + "]");

			query = "{call pGetBillerDetails(?,?,?,?,?,?)}";

			callableStatement = connection.prepareCall(query);
			callableStatement.setString(1, requestJSON.getString("billerid"));
			callableStatement.setString(2,
					requestJSON.getString("customerAccount"));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, OracleTypes.VARCHAR);
			callableStatement.registerOutParameter(6, OracleTypes.VARCHAR);

			callableStatement.execute();

			message = callableStatement.getString(6);

			logger.debug("Message is [" + message + "]");

			resultSet = (ResultSet) callableStatement.getObject(4);
			
			if (resultSet.next()) {
				billerBean = new BillerBean();
				billerBean.setBillerCode(requestJSON.getString("billerid"));
				billerBean.setName(resultSet.getString(1));
				billerBean.setAbbreviation(resultSet.getString(2));
				billerBean.setEmail(resultSet.getString(3));
				billerBean.setMakerId(resultSet.getString(4));
				billerBean.setMakerDttm(resultSet.getString(5));
				billerBean.setCommissionType(resultSet.getString(6));
				billerBean.setAmount(resultSet.getString(7));
				billerBean.setAddress(resultSet.getString(8));
				billerBean.setAccountType(resultSet.getString(9));
				billerBean.setAgencyType(resultSet.getString(10));
				billerBean.setAuthorizeId(resultSet.getString(11));
				billerBean.setAuthorizeDttm(resultSet.getString(12));
				billerBean.setContactPerson(resultSet.getString(13));
				billerBean.setTelephone(resultSet.getString(14));
				billerBean.setAccountNo(resultSet.getString(15));
			}

			storeMap.put("bank_data", billerBean);

			responseJSON.put("name", callableStatement.getString(5));

			responseJSON.put("message", callableStatement.getString(6));

			storeMap.put("acc_data", responseJSON);

			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in GetCustomerInDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in GetCustomerInDetails ["
					+ e1.getMessage() + "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeResultSet(resultSet);
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			storeMap = null;
		}

		return responseDTO;
	}

	public ResponseDTO profilePinReset(RequestDTO requestDTO) {

		logger.debug("Inside Pin Reset Details... ");
		Connection connection = null;

		CallableStatement callableStatement = null;

		String query = "";
		String message = "";

		try {
			responseDTO = new ResponseDTO();

			requestJSON = requestDTO.getRequestJSON();

			connection = DBConnector.getConnection();

			logger.debug("Connection is [" + connection + "]");

			query = "{call pProfilePinReset(?,?,?,?,?)}";
			String pin = CommonUtil.generatePin(4);

			callableStatement = connection.prepareCall(query);
			callableStatement.setString(1, requestJSON.getString("id"));
			callableStatement.setString(2, pin);
			callableStatement.setString(3,
					PostaMobileEncryptTripleDes.encrypt(pin));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.registerOutParameter(5, OracleTypes.VARCHAR);

			callableStatement.execute();

			message = callableStatement.getString(5);

			logger.debug("Message is [" + message + "]");

			if (!message.contains("Success")) {
				responseDTO.addError(message);
			} else {

			}

			logger.debug("Response DTO [" + responseDTO + "]");
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in profilePinReset [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in profilePinReset [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}
	
	
	public ResponseDTO getBillerDetails(RequestDTO requestDTO) {

		logger.debug("Inside getBillerDetails in DAO ... ");

		JSONArray jsonArray = null;

		String query = null;

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		JSONObject json = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			billerDataMap = new HashMap<String, Object>();
			jsonArray = new JSONArray();
			
			
			String billerType = requestJSON.getString("billerType"); 
			
			connection = DBConnector.getConnection();
			logger.debug("Connection is [" + connection + "]");
			
			if(billerType.equals("Other")){
				query = "Select BILLER_ID,NAME from  BILLER_REGISTRATION where BILLER_TYPE='BILLER' order by BILLER_ID";
				pstmt = connection.prepareStatement(query);
			}else{
				query = "Select BILLER_ID,NAME from  BILLER_REGISTRATION where BILLER_TYPE=? order by BILLER_ID";
				pstmt = connection.prepareStatement(query);
				pstmt.setString(1,billerType);
			}
			rs = pstmt.executeQuery();

			while (rs.next()) {
				json = new JSONObject();
				json.put("val", rs.getString(1));
				json.put("key", rs.getString(2));
				jsonArray.add(json);
				json.clear();
			}

			responseJSON.put("BILLER_DATA", jsonArray);
			billerDataMap.put("BILLER_DATA", responseJSON);
			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in GetBillerCustomerData ["
					+ e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in GetBillerCustomerData ["
					+ e.getMessage() + "]");
		} finally {
			jsonArray = null;
			query = null;
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

public ResponseDTO getBanks(RequestDTO requestDTO) {

	logger.debug("Inside getBanks in DAO ... ");

	JSONArray jsonArray = null;

	String query = null;

	HashMap<String, Object> billerDataMap = null;
	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	JSONObject json = null;

	try {
		responseJSON = new JSONObject();
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();

		billerDataMap = new HashMap<String, Object>();
		jsonArray = new JSONArray();
		
		
		
		connection = DBConnector.getConnection();
		logger.debug("Connection is [" + connection + "]");
		
	
		query = "Select bank_id,bank_name from  biller_banks order by bank_id";
		pstmt = connection.prepareStatement(query);
	
		rs = pstmt.executeQuery();

		while (rs.next()) {
			json = new JSONObject();
			json.put("val", rs.getString(1));
			json.put("key", rs.getString(2));
			jsonArray.add(json);
			json.clear();
		}

		responseJSON.put("BILLER_DATA", jsonArray);
		billerDataMap.put("BILLER_DATA", responseJSON);
		responseDTO.setData(billerDataMap);
	} catch (SQLException e) {
		responseDTO.addError("Internal Error Occured While Executing.");

		logger.debug("SQLException in GetBillerCustomerData ["
				+ e.getMessage() + "]");
	} catch (Exception e) {
		responseDTO.addError("Internal Error Occured While Executing.");

		logger.debug("Exception in GetBillerCustomerData ["
				+ e.getMessage() + "]");
	} finally {
		jsonArray = null;
		query = null;
		DBUtils.closeResultSet(rs);
		DBUtils.closePreparedStatement(pstmt);
		DBUtils.closeConnection(connection);
		billerDataMap = null;
	}

	return responseDTO;
}

}
