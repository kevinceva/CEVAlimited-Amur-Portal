package com.ceva.base.common.dao;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class ReportDetailsDAO {

	private Logger logger = Logger.getLogger(ReportDetailsDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getReportDetailsInfo(RequestDTO requestDTO) {
		logger.debug("Inside GetReportDetailsInfo.. ");

		HashMap<String, Object> reportDataMap = null;
		JSONObject resultJson = null;

		JSONArray jsonArray = null;

		Connection connection = null;
		PreparedStatement entityPstmt = null;
		ResultSet entityRS = null;
		JSONObject json = null;

		String genQuery = "";
		String makerId = "";
		String userLevel = "";
		String location = "";
		String regionCode = "";
		try {
			reportDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			responseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			jsonArray = new JSONArray();
			json = new JSONObject();

			makerId = requestDTO.getRequestJSON().getString(
					CevaCommonConstants.MAKER_ID);
			// Getting the User Level, Location

			genQuery = "select nvl(user_level,'NO'),location from user_information where common_id = (select common_id from user_login_credentials where login_user_id=?)";
			entityPstmt = connection.prepareStatement(genQuery);
			entityPstmt.setString(1, makerId);
			entityRS = entityPstmt.executeQuery();

			if (entityRS.next()) {
				userLevel = entityRS.getString(1);
				location = entityRS.getString(2);
			}

			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeResultSet(entityRS);
			genQuery = null;

			logger.debug(" The User Location is  [" + location + "]");

			if (userLevel.equalsIgnoreCase("REGION")) {
				genQuery = "Select REGION_CODE,REGION FROM POSTA_REGION_MASTER WHERE REGION_CODE IN (SELECT distinct REGION_CODE FROM  POSTA_BRANCH_MASTER where OFFICE_CODE in ('"
						+ location + "')) ORDER BY REGION_CODE";
			} else {
				genQuery = "Select REGION_CODE,REGION FROM POSTA_REGION_MASTER WHERE REGION_CODE NOT IN ('01') ORDER BY REGION_CODE";
			}

			entityPstmt = connection.prepareStatement(genQuery);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {

				regionCode = entityRS.getString(1);
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1)
						+ "-" + entityRS.getString(2));
				jsonArray.add(json);
				json.clear();
			}
			resultJson.put("region", jsonArray);
			logger.debug("Region JSON Array [" + jsonArray + "]");
			genQuery = null;
			jsonArray.clear();
			
			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeResultSet(entityRS);
			

			if (userLevel.equalsIgnoreCase("REGION")) {
				genQuery = "Select distinct HPO_DEPARTMENT_CODE,HPO_NAME from POSTA_BRANCH_MASTER where HPO_FLAG='HEAD' AND REGION_CODE='"
						+ regionCode + "'  ORDER BY HPO_DEPARTMENT_CODE";
			} else {
				genQuery = "Select distinct HPO_DEPARTMENT_CODE,HPO_NAME from POSTA_BRANCH_MASTER where HPO_FLAG='HEAD' AND REGION_CODE NOT IN ('01')  ORDER BY HPO_DEPARTMENT_CODE";
			}

			entityPstmt = connection.prepareStatement(genQuery);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1)
						+ "-" + entityRS.getString(2));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("Headoffice JSONArray [" + jsonArray + "]");

			resultJson.put("headoffice", jsonArray);

			genQuery = null;
			jsonArray.clear();

			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);
			

			if (userLevel.equalsIgnoreCase("BRANCH MANAGER")) {
				genQuery = "Select distinct PBM.OFFICE_CODE,PBM.OFFICE_NAME,(select store_id from store_master where location=PBM.OFFICE_CODE and rownum<2)  from POSTA_BRANCH_MASTER PBM where HPO_FLAG is null and upper(HPO_NAME) = (select upper(OFFICE_NAME) from POSTA_BRANCH_MASTER where office_code=?) ORDER BY OFFICE_CODE";
			} else {
				genQuery = "Select distinct PBM.OFFICE_CODE,PBM.OFFICE_NAME,(select store_id from store_master where location=PBM.OFFICE_CODE and rownum<2)  from POSTA_BRANCH_MASTER PBM where HPO_FLAG is null ORDER BY OFFICE_CODE";
			}

			entityPstmt = connection.prepareStatement(genQuery);

			if (userLevel.equalsIgnoreCase("BRANCH MANAGER")) {
				entityPstmt.setString(1, location);
			}

			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1)
						+ "-" + entityRS.getString(2));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("Location JSONArray [" + jsonArray + "]");

			resultJson.put("location", jsonArray);

			
			genQuery = null;
			jsonArray.clear();

			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);

			if (userLevel.equalsIgnoreCase("BRANCH MANAGER")) {
				genQuery = "select B.LOGIN_USER_ID from USER_INFORMATION A, USER_LOGIN_CREDENTIALS B , POSTA_BRANCH_MASTER PBM where A.COMMON_ID=B.COMMON_ID AND upper(A.USER_LEVEL)='USER' AND  PBM.HPO_NAME in  (select OFFICE_NAME from POSTA_BRANCH_MASTER where  OFFICE_CODE = (select location from  user_information where common_id in (select common_id from user_login_credentials where login_user_id=?) ) ) AND A.LOCATION=PBM.OFFICE_CODE  AND HPO_FLAG is null";
			} else if (userLevel.equalsIgnoreCase("BRANCHE OFFICE")) {
				genQuery = "select B.LOGIN_USER_ID from USER_INFORMATION A, USER_LOGIN_CREDENTIALS B , POSTA_BRANCH_MASTER PBM where A.COMMON_ID=B.COMMON_ID AND A.LOCATION=PBM.OFFICE_CODE  AND upper(A.USER_LEVEL)='USER' AND  PBM.OFFICE_CODE in (select location from  user_information where common_id in (select common_id from user_login_credentials where login_user_id=?) )";
			} else {
				genQuery = "select B.LOGIN_USER_ID from USER_INFORMATION A, USER_LOGIN_CREDENTIALS B , POSTA_BRANCH_MASTER PBM where "
						+ "A.COMMON_ID=B.COMMON_ID AND upper(A.USER_LEVEL)='USER' AND A.LOCATION=PBM.OFFICE_CODE";
			}

			entityPstmt = connection.prepareStatement(genQuery);

			if (userLevel.equalsIgnoreCase("BRANCH MANAGER")
					|| userLevel.equalsIgnoreCase("BRANCHE OFFICE")) {
				entityPstmt.setString(1, makerId);
			}

			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("UserId JSON Array [" + jsonArray + "]");

			resultJson.put("userid", jsonArray);

			genQuery = null;
			jsonArray.clear();


			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);

			
			genQuery = "Select distinct BANK_CODE,BANK_NAME from BANK_MASTER where BANK_CODE NOT IN ('POSTAKEN', 'MPESAKEN', 'DEPOSITS')";
			entityPstmt = connection.prepareStatement(genQuery);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1)
						+ "-" + entityRS.getString(2));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("Bank JSONArray [" + jsonArray + "]");

			resultJson.put("bank", jsonArray);

			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);
			
			genQuery = null;
			jsonArray.clear();

			genQuery = "SELECT RUG.REPORT_ID,(select REPORT_DESCRIPTION FROM REPORT_DETAILS WHERE REPORT_ID=RUG.REPORT_ID) "
					+ "FROM REPORT_USER_GROUP RUG WHERE RUG.GROUP_ID=(select user_groups from user_information where "
					+ "common_id=(select common_id from user_login_credentials where login_user_id=?)) and RUG.USER_ID=? "
					+ "order by to_number(RUG.REPORT_ID) ";

			entityPstmt = connection.prepareStatement(genQuery);
			entityPstmt.setString(1,
					requestDTO.getRequestJSON().getString("makerId"));
			entityPstmt.setString(2,
					requestDTO.getRequestJSON().getString("USER_ID"));
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1)
						+ "-" + entityRS.getString(2));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("ReportNameJSONArray [" + jsonArray + "]");

			resultJson.put("reportname", jsonArray);

			genQuery = null;
			jsonArray.clear();

			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeResultSet(entityRS);
			
			genQuery = "Select distinct PROFILEID,FULLNAME from W_PROFILES ";
			entityPstmt = connection.prepareStatement(genQuery);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, entityRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, entityRS.getString(1)
						+ "-" + entityRS.getString(2));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("Customer Profiles JSONArray [" + jsonArray + "]");

			resultJson.put("custprof", jsonArray);


			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);
			genQuery = null;
			jsonArray.clear();
			
			resultJson.put("rd_region", regionCode);

			reportDataMap.put("reportinfo", resultJson);
			logger.debug("EntityMap [" + reportDataMap.toString() + "]");
			responseDTO.setData(reportDataMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetReportDetailsInfo ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("Exception in GetReportDetailsInfo [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeConnection(connection);

			reportDataMap = null;
			resultJson = null;

			jsonArray = null;
			makerId = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertReportDetailsInfo(RequestDTO requestDTO) {

		logger.debug(" Inside InsertReportDetailsInfo.. ");

		List<String> freq = null;
		List<String> dateTime = null;
		List<String> emailIds = null;
		List<String> reports = null;

		Connection connection = null;
		PreparedStatement entityPstmt = null;

		String genQuery = "";
		int count[] = null;
		try {
			responseDTO = new ResponseDTO();
			freq = (List<String>) requestDTO.getRequestJSON().get("freq");
			dateTime = (List<String>) requestDTO.getRequestJSON().get(
					"dateTime");
			emailIds = (List<String>) requestDTO.getRequestJSON().get(
					"emailIds");
			reports = (List<String>) requestDTO.getRequestJSON().get("reports");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			connection.setAutoCommit(false);
			entityPstmt = connection
					.prepareStatement("DELETE FROM REPORT_SCHEDULER_CONFIG");
			entityPstmt.executeUpdate();
			connection.commit();

			logger.debug("Deleted the records from REPORT_SCHEDULER_CONFIG table.");
			entityPstmt.close();

			genQuery = "insert into REPORT_SCHEDULER_CONFIG(TXN_REF_ID,FREQUENCY, "
					+ "DATE_TIME, EMAIL_IDS, REPORT_NAMES, FLAG, STATUS, CREATED_DATE, "
					+ "CREATED_BY,CRON_EXPRESSION,PERIOD) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?)";

			entityPstmt = connection.prepareStatement(genQuery);

			for (int index = 0; index < freq.size(); index++) {
				String freqS = freq.get(index);
				String dateTimeS = dateTime.get(index);
				String emailIdsS = emailIds.get(index);
				String reportsS = reports.get(index);

				String cronDate = generateCronExpression(freqS, dateTimeS);

				entityPstmt.setString(1, getTxnRefNo());
				entityPstmt.setString(2, freqS);
				entityPstmt.setString(3, dateTimeS);
				entityPstmt.setString(4, emailIdsS);
				entityPstmt.setString(5, reportsS);
				entityPstmt.setString(6, "S");
				entityPstmt.setString(7, "ACTIVE");
				entityPstmt.setDate(8,
						new java.sql.Date(System.currentTimeMillis()));
				entityPstmt.setString(9, "TEMP");
				entityPstmt.setString(10, cronDate);
				entityPstmt.setString(11, getTriggerType(freqS).toUpperCase());

				entityPstmt.addBatch();
			}

			count = entityPstmt.executeBatch();
			connection.commit();
			logger.debug("Inserted count is [" + count + "]");

			responseDTO = getScheduledReportDetails(requestDTO);
			logger.debug("Final Response DTO[" + responseDTO + "]");

		} catch (SQLException e) {

			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			logger.debug("SQLException in insertReportDetailsInfo ["
					+ responseDTO + "]");
		} catch (Exception e) {

			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			logger.debug("Exception in insertReportDetailsInfo [" + responseDTO
					+ "]");
		} finally {

			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeConnection(connection);

			freq = null;
			dateTime = null;
			emailIds = null;
			reports = null;
		}
		return responseDTO;
	}

	public ResponseDTO getScheduledReportDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetScheduledReportDetails.. ");
		HashMap<String, Object> reportDataMap = null;

		JSONObject resultJson = null;

		JSONArray reportArray = null;
		JSONObject json = null;

		Connection connection = null;
		PreparedStatement entityPstmt = null;
		ResultSet entityRS = null;

		String genQuery = "";
		try {
			responseDTO = new ResponseDTO();
			reportDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			resultJson = new JSONObject();
			json = new JSONObject();

			reportArray = new JSONArray();

			genQuery = "select TXN_REF_ID,FREQUENCY,DATE_TIME,EMAIL_IDS,REPORT_NAMES "
					+ "from  REPORT_SCHEDULER_CONFIG order by CREATED_DATE";
			entityPstmt = connection.prepareStatement(genQuery);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put("TxnRefNo", entityRS.getString(1));
				json.put("frequencies", entityRS.getString(2));
				json.put("dateTime", entityRS.getString(3));
				json.put("emailids", entityRS.getString(4));
				json.put("reports", entityRS.getString(5));
				reportArray.add(json);
				json.clear();
			}

			logger.debug("RegionJSONArray [" + reportArray + "]");
			
			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);

			genQuery = "Select REPORT_ID,REPORT_DESCRIPTION from REPORT_DETAILS  "
					+ "ORDER BY REPORT_ID";
			entityPstmt = connection.prepareStatement(genQuery);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {
				json.put(entityRS.getString(1), entityRS.getString(1) + "-"
						+ entityRS.getString(2));
			}

			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);

			resultJson.put("reportDetails", json);
			resultJson.put("reportSchRecords", reportArray);
			reportDataMap.put("reportschinfo", resultJson);

			responseDTO.setData(reportDataMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

		} catch (Exception e) {
			logger.debug("Got Exception in  GetScheduledReportDetails["
					+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(entityRS);
			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeConnection(connection);

			reportDataMap = null;
			resultJson = null;
			genQuery = null;
			reportArray = null;
			json = null;
		}

		return responseDTO;
	}

	private static String getTxnRefNo() {
		String uniq = "";
		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssS");
		uniq = dateFormat.format(new Date());
		try {
			SecureRandom randomGenerator = SecureRandom.getInstance("SHA1PRNG");
			String randNo = String.valueOf(randomGenerator.nextInt())
					.replaceAll("-", "");
			uniq += randNo;
		} catch (NoSuchAlgorithmException nsae) {
			uniq += System.currentTimeMillis();
		}

		return uniq;
	}

	private String generateCronExpression(String frequency, String dateTime) {
		logger.debug("Inside GenerateCronExpression.. ");

		logger.debug("Frequency [" + frequency + "]");
		logger.debug("DateTime  [" + dateTime + "]");

		String quartArray[] = { "ALL", "JAN-MAR", "APR-JUN", "JUL-SEP",
				"OCT-DEC" };
		String cronExpression = "";
		String dat = "";
		try {

			dat = dateTime.split("##")[0];

			logger.debug("Daily :::: " + Arrays.asList(dat));

			if (frequency.equalsIgnoreCase("Daily")) {
				logger.debug("Daily..");
				cronExpression = dat;
			} else if (frequency.equalsIgnoreCase("Weekly")) {
				logger.debug("Weekly..");
				cronExpression = dat;
			} else if (frequency.equalsIgnoreCase("Monthly")) {
				logger.debug("Monthly..");
				cronExpression = dat;
			} else if (frequency.equalsIgnoreCase("Quarterly")) {
				logger.debug("Quarterly..");
				for (String quart : quartArray) {
					if (dat.toUpperCase().equalsIgnoreCase(quart)) {
						cronExpression = quart;
					}
				}
			} else if (frequency.equalsIgnoreCase("Yearly")
					|| frequency.equalsIgnoreCase("annually")) {
				logger.debug(" annually of  ");
				cronExpression = dat;
			}
		} catch (Exception e) {
			logger.debug("Exception is GenerateCronExpression ::: "
					+ e.getMessage());
		} finally {
			quartArray = null;
			cronExpression = null;
			dat = null;
		}

		return cronExpression;
	}

	private String getTriggerType(String triggerType) {
		String triggerString = null;
		try {
			if ("DAILY".equalsIgnoreCase(triggerType))
				triggerString = "D";
			else if ("WEEKLY".equalsIgnoreCase(triggerType))
				triggerString = "W";
			else if ("MONTHLY".equalsIgnoreCase(triggerType)) {
				triggerString = "M";
			} else if ("QUARTERLY".equalsIgnoreCase(triggerType)) {
				triggerString = "Q";
			} else if ("BIANNUALLY".equalsIgnoreCase(triggerType))
				triggerString = "B";
			else if ("ANNUALLY".equalsIgnoreCase(triggerType)
					|| "YEARLY".equalsIgnoreCase(triggerType))
				triggerString = "A";
		} catch (Exception e) {

			logger.debug("Exception is GetTriggerType ::: " + e.getMessage());
		}
		return triggerString;
	}

	public ResponseDTO getUserAssignedReports(RequestDTO requestDTO) {

		logger.debug("Inside GetUserAssignedReports... ");

		String userQry = "SELECT REPORT_ID,REPORT_DESCRIPTION FROM REPORT_DETAILS WHERE  REPORT_ID not in (select REPORT_ID FROM REPORT_USER_GROUP WHERE USER_ID=?)  order by to_number(REPORT_ID)";
		String userQry1 = "SELECT RUG.REPORT_ID,(select REPORT_DESCRIPTION FROM REPORT_DETAILS WHERE REPORT_ID=RUG.REPORT_ID) FROM REPORT_USER_GROUP RUG WHERE RUG.USER_ID=? order by to_number(RUG.REPORT_ID) ";
		String userQry2 = "SELECT UG.GROUP_ID,UG.GROUP_NAME,ULC.LOGIN_USER_ID,UI.EMP_ID "
				+ "FROM  USER_GROUPS UG,USER_LOGIN_CREDENTIALS ULC,USER_INFORMATION UI  WHERE UI.COMMON_ID = ULC.COMMON_ID and  "
				+ " UI.USER_GROUPS = UG.GROUP_ID and ULC.LOGIN_USER_ID=trim(?) and UG.APPL_CODE='AgencyBanking' ORDER BY UG.GROUP_ID,ULC.LOGIN_USER_ID";
		Connection connection = null;
		ResultSet userRS = null;
		PreparedStatement userPstmt = null;

		JSONObject json = null;

		HashMap<String, Object> resultMap = null;

		JSONArray userJSONArray = null;

		try {

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is  [" + connection + "]");

			resultMap = new HashMap<String, Object>();
			userJSONArray = new JSONArray();

			userPstmt = connection.prepareStatement(userQry);
			userPstmt.setString(1, requestJSON.getString("USER_ID"));
			userRS = userPstmt.executeQuery();
			json = new JSONObject();

			while (userRS.next()) {

				json.put(CevaCommonConstants.SELECT_KEY, userRS.getString(1)
						+ "-" + userRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, userRS.getString(1)
						+ "-" + userRS.getString(2));
				userJSONArray.add(json);
				json.clear();
			}

			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			
			responseJSON.put("REPORT_MAIN", userJSONArray);

			logger.debug("GROUP_ID :::" + requestJSON.getString("GROUP_ID"));
			userJSONArray.clear();

			userPstmt = connection.prepareStatement(userQry1);
			userPstmt.setString(1, requestJSON.getString("USER_ID"));
			userRS = userPstmt.executeQuery();

			while (userRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, userRS.getString(1)
						+ "-" + userRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, userRS.getString(1)
						+ "-" + userRS.getString(2));
				userJSONArray.add(json);
				json.clear();
			}

			responseJSON.put("REPORT_USER_GROUP", userJSONArray);

			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);

			logger.debug("UserQry2 [" + userQry2 + "]");

			userPstmt = connection.prepareStatement(userQry2);
			userPstmt.setString(1, requestJSON.getString("USER_ID"));
			userRS = userPstmt.executeQuery();

			if (userRS.next()) {
				responseJSON.put("GROUP_ID", userRS.getString(1));
				responseJSON.put("GROUP_NAME", userRS.getString(2));
				responseJSON.put("USER_ID", userRS.getString(3));
				responseJSON.put("EMP_NO", userRS.getString(4));
			}

			resultMap.put(CevaCommonConstants.RESPONSE_JSON, responseJSON);

			responseDTO.setData(resultMap);

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			DBUtils.closeResultSet(userRS);
			DBUtils.closePreparedStatement(userPstmt);
			DBUtils.closeConnection(connection);

			resultMap = null;
			userJSONArray = null;

			json = null;
		}
		return responseDTO;
	}

	public ResponseDTO insertUserAssignedReports(RequestDTO requestDTO) {

		logger.debug("Inside InsertUserAssignedReports... ");
		String makerId = "";
		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertBankInfoProc = "{call InsertUserReportDetails(?,?,?,?,?)}";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			makerId = requestJSON.getString(CevaCommonConstants.MAKER_ID);

			callableStatement = connection.prepareCall(insertBankInfoProc);
			callableStatement.setString(1, makerId);
			callableStatement.setString(2, requestJSON.getString("GROUP_ID"));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.TXN_DATA));
			callableStatement.setString(4, requestJSON.getString("USER_ID"));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);

			logger.debug("ResultCnt from DB :: " + resCnt);

			if (resCnt == 1) {
				responseDTO
						.addMessages("Reports Assigned to the '"
								+ requestJSON.getString("USER_ID")
								+ "' Successfully. ");
			} else {
				responseDTO.addError("Assigning Reports to user failed.");
			}

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			makerId = null;
			insertBankInfoProc = null;
		}
		return responseDTO;
	}
}
