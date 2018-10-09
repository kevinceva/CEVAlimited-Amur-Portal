package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class ServiceManagementDAO {

	private Logger logger = Logger.getLogger(ServiceManagementDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getBankDetails(RequestDTO requestDTO) {
		logger.debug("Inside  GetBankDetails.... ");

		HashMap<String, Object> serviceDataMap = null;
		JSONObject resultJson = null;
		JSONArray serviceJSONArray = null;

		Connection connection = null;

		PreparedStatement servicePstmt = null;

		ResultSet serviceIdRS = null;
		ResultSet serviceRS = null;
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			json = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			serviceDataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			serviceJSONArray = new JSONArray();

			// String
			// serviceQry="Select distinct STATUS||'-'||KEY_VALUE,STATUS from CONFIG_DATA where KEY_GROUP=? and KEY_TYPE=? order by STATUS";
			String serviceQry = "Select distinct BANK_CODE,BANK_NAME from NBIN";

			servicePstmt = connection.prepareStatement(serviceQry);
			serviceRS = servicePstmt.executeQuery();

			while (serviceRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, serviceRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, serviceRS.getString(1)
						+ "-" + serviceRS.getString(2));
				serviceJSONArray.add(json);
				json.clear();
			}

			resultJson.put("BANK_LIST", serviceJSONArray);

			serviceDataMap.put("BANK_LIST", resultJson);

			logger.debug("ServiceDataMap [" + serviceDataMap + "]");
			responseDTO.setData(serviceDataMap);

		} catch (SQLException e) {
			logger.debug("SQLException in GetBankDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetBankDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
				DBUtils.closeResultSet(serviceRS);
				DBUtils.closeResultSet(serviceIdRS);
				DBUtils.closePreparedStatement(servicePstmt);
				DBUtils.closeConnection(connection);
			serviceDataMap = null;
			resultJson = null;
			serviceJSONArray.clear();
			serviceJSONArray = null;
		}

		return responseDTO;
	}

	public ResponseDTO getServiceInfo(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside GetServiceInfo.. ");
		HashMap<String, Object> serviceMap = null;
		JSONObject resultJson = null;
		JSONObject subServiceJSON = null;
		JSONObject feeJSON = null;

		JSONArray serviceJSONArray = null;
		JSONArray subServiceJSONArray = null;
		JSONArray feeJSONArray = null;

		ArrayList<String> serviceArray = null;
		ArrayList<String> subServiceArray = null;

		PreparedStatement servicePstmt = null;
		PreparedStatement subServicePstmt = null;
		PreparedStatement subServPstmt = null;
		PreparedStatement feePstmt = null;

		ResultSet serviceRS = null;
		ResultSet subServiceRS = null;
		ResultSet subServRS = null;
		ResultSet feeRS = null;
		JSONObject json = null;

		String serviceQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SERVICE_NAME and rownum<2),"
				+ "MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from SERVICE_MASTER where SERVICE_TYPE is null  "
				+ "order by SERVICE_CODE";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			serviceMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			subServiceJSON = new JSONObject();
			feeJSON = new JSONObject();
			serviceJSONArray = new JSONArray();
			subServiceJSONArray = new JSONArray();
			feeJSONArray = new JSONArray();

			serviceArray = new ArrayList<String>();
			subServiceArray = new ArrayList<String>();

			logger.debug("connection is [" + connection + "]");

			servicePstmt = connection.prepareStatement(serviceQry);

			serviceRS = servicePstmt.executeQuery();

			json = new JSONObject();
			while (serviceRS.next()) {
				json.put(CevaCommonConstants.SERVICE_CODE,
						serviceRS.getString(1));
				json.put(CevaCommonConstants.SERVICE_NAME,
						serviceRS.getString(2));
				json.put(CevaCommonConstants.MAKER_ID, serviceRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE, serviceRS.getString(4));
				serviceArray.add(serviceRS.getString(1));
				serviceJSONArray.add(json);
				json.clear();
			}
			logger.debug("ServiceJSONArray [" + serviceJSONArray + "]");

			resultJson.put(CevaCommonConstants.SERVICE_INFO, serviceJSONArray);

			for (int i = 0; i < serviceArray.size(); i++) {
				// String
				// subServiceQry="Select SERVICE_CODE,SERVICE_NAME,SUB_SERVICE_CODE,SUB_SERVICE_NAME,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from SERVICE_MASTER where SERVICE_CODE=? and  SERVICE_TYPE is not null  order by SUB_SERVICE_CODE";
				String subServiceQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SERVICE_NAME and rownum<2),SUB_SERVICE_CODE,SUB_SERVICE_NAME,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from SERVICE_MASTER where SERVICE_CODE=? and  SERVICE_TYPE is not null  order by SUB_SERVICE_CODE";
				subServicePstmt = connection.prepareStatement(subServiceQry);
				subServicePstmt.setString(1, serviceArray.get(i));

				subServiceRS = subServicePstmt.executeQuery();
				while (subServiceRS.next()) {
					json.put(CevaCommonConstants.SERVICE_CODE,
							subServiceRS.getString(1));
					json.put(CevaCommonConstants.SERVICE_NAME,
							subServiceRS.getString(2));
					json.put(CevaCommonConstants.SUB_SERVICE_CODE,
							subServiceRS.getString(3));
					json.put(CevaCommonConstants.SUB_SERVICE_NAME,
							subServiceRS.getString(4));
					json.put(CevaCommonConstants.MAKER_DATE,
							subServiceRS.getString(5));
					subServiceJSONArray.add(json);
					json.clear();
				}
				subServiceJSON.put(serviceArray.get(i) + "_" + "SUBSERVICE",
						subServiceJSONArray);
			}

			String subServicesQry = "select distinct SUB_SERVICE_CODE from FEE_MASTER";
			subServPstmt = connection.prepareStatement(subServicesQry);

			subServRS = subServPstmt.executeQuery();
			while (subServRS.next()) {
				subServiceArray.add(subServRS.getString(1));
			}

			for (int i = 0; i < subServiceArray.size(); i++) {
				String feeQry = "Select FEE_CODE,SERVICE_CODE,SUB_SERVICE_CODE,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FEE_MASTER where SUB_SERVICE_CODE=?  order by FEE_CODE";
				feePstmt = connection.prepareStatement(feeQry);
				feePstmt.setString(1, subServiceArray.get(i));

				feeRS = feePstmt.executeQuery();
				while (feeRS.next()) {
					json.put(CevaCommonConstants.FEE_CODE, feeRS.getString(1));
					json.put(CevaCommonConstants.SERVICE_CODE,
							feeRS.getString(2));
					json.put(CevaCommonConstants.SUB_SERVICE_CODE,
							feeRS.getString(3));
					json.put(CevaCommonConstants.MAKER_ID, feeRS.getString(4));
					json.put(CevaCommonConstants.MAKER_DATE, feeRS.getString(5));
					feeJSONArray.add(json);
					json.clear();
				}
				feeJSON.put(subServiceArray.get(i) + "_" + "FEE", feeJSONArray);
			}

			serviceMap.put(CevaCommonConstants.SERVICE_INFO, resultJson);
			serviceMap
					.put(CevaCommonConstants.SUB_SERVICE_INFO, subServiceJSON);
			serviceMap.put(CevaCommonConstants.FEE_INFO, feeJSON);
			logger.debug("Service Map [" + serviceMap + "]");
			responseDTO.setData(serviceMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetBankDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetBankDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			serviceMap = null;
			resultJson = null;
			subServiceJSON = null;
			feeJSON = null;

			serviceJSONArray = null;
			subServiceJSONArray = null;
			feeJSONArray = null;

			serviceArray = null;
			subServiceArray = null;


			
				DBUtils.closeResultSet(serviceRS);
				DBUtils.closeResultSet(subServiceRS);
				DBUtils.closeResultSet(subServRS);
				DBUtils.closeResultSet(feeRS);
				
				DBUtils.closePreparedStatement(servicePstmt);
				DBUtils.closePreparedStatement(subServicePstmt);
				DBUtils.closePreparedStatement(subServPstmt);
				DBUtils.closePreparedStatement(feePstmt);
				
				DBUtils.closeConnection(connection);		
		}
		return responseDTO;
	}

	public ResponseDTO getNextServiceCode(RequestDTO requestDTO) {

		logger.debug("Inside GetNextServiceCode... ");
		HashMap<String, Object> serviceDataMap = null;

		JSONObject resultJson = null;

		JSONArray serviceJSONArray = null;
		JSONArray settlementJSONArray = null;

		Connection connection = null;

		PreparedStatement serviceIdPstmt = null;
		PreparedStatement servicePstmt = null;
		PreparedStatement settlementPstmt = null;

		ResultSet serviceIdRS = null;
		ResultSet serviceRS = null;
		ResultSet settlementRS = null;

		String serviceIdQry = "";
		JSONObject json = null;
		try {

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			serviceJSONArray = new JSONArray();
			settlementJSONArray = new JSONArray();

			resultJson = new JSONObject();

			serviceDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");
			serviceIdQry = "Select count(SERVICE_CODE) from SERVICE_MASTER where SERVICE_TYPE is null";

			serviceIdPstmt = connection.prepareStatement(serviceIdQry);

			serviceIdRS = serviceIdPstmt.executeQuery();
			int serviceId = 0;
			if (serviceIdRS.next()) {
				String serviceID = serviceIdRS.getString(1);
				serviceId = Integer.parseInt(serviceID);
				logger.debug("ServiceID [" + serviceID + "]");
			}

			serviceId++;
			resultJson.put(CevaCommonConstants.SERVICE_CODE, serviceId);

			String serviceQry = "Select distinct BANK_CODE,BANK_NAME from BANK_MASTER order by BANK_NAME";
			servicePstmt = connection.prepareStatement(serviceQry);

			serviceRS = servicePstmt.executeQuery();

			json = new JSONObject();
			while (serviceRS.next()) {

				json.put(CevaCommonConstants.SELECT_KEY, serviceRS.getString(1)
						+ "-" + serviceRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, serviceRS.getString(1));
				serviceJSONArray.add(json);
				json.clear();
			}

			resultJson.put(CevaCommonConstants.SERVICE_LIST, serviceJSONArray);

			String settlementQry = "Select distinct FINANCIAL_CODE,FINANCIAL_NAME from "
					+ "FINANCIAL_MASTER where TRANS_TYPE=? order by FINANCIAL_NAME";
			settlementPstmt = connection.prepareStatement(settlementQry);
			settlementPstmt.setString(1, "T");
			settlementRS = settlementPstmt.executeQuery();

			while (settlementRS.next()) {
				json.put(
						CevaCommonConstants.SELECT_KEY,
						settlementRS.getString(1) + "-"
								+ settlementRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL,
						settlementRS.getString(1));
				settlementJSONArray.add(json);
				json.clear();
			}

			resultJson.put(CevaCommonConstants.SETTLEMENT_LIST,
					settlementJSONArray);

			serviceDataMap.put(CevaCommonConstants.SERVICE_INFO, resultJson);

			logger.debug("ServiceDataMap [" + serviceDataMap + "]");
			responseDTO.setData(serviceDataMap);
			logger.debug("Response DTO " + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetNextServiceCode ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetNextServiceCode ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			serviceDataMap = null;

			resultJson = null;

			serviceJSONArray = null;
			settlementJSONArray = null;
				DBUtils.closeResultSet(serviceIdRS);
				DBUtils.closeResultSet(serviceRS);
				DBUtils.closeResultSet(settlementRS);
				
				DBUtils.closePreparedStatement(serviceIdPstmt);
				DBUtils.closePreparedStatement(servicePstmt);
				DBUtils.closePreparedStatement(settlementPstmt);
				
				DBUtils.closeConnection(connection);
			}

		return responseDTO;
	}

	public ResponseDTO insertService(RequestDTO requestDTO) {

		logger.debug("Inside InsertService... ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		String insertServiceProc = "{call insertServiceProc(?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + connection  + "]");

			callableStatement = connection.prepareCall(insertServiceProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.SERVICE_CODE));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.SERVICE_NAME));
			callableStatement.setString(4, requestJSON
					.getString(CevaCommonConstants.SETTLEMENT_ACCOUNT));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Service Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Service already available. ");
			} else {
				responseDTO.addError("Service Information Insertion Failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in GetNextServiceCode ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetNextServiceCode ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			insertServiceProc = null;
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);	
		}
		return responseDTO;
	}

	public ResponseDTO ServiceViewDetails(RequestDTO requestDTO) {

		logger.debug("Inside ServiceViewDetails... ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;

		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;

		String storeQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master "
				+ "where BANK_CODE=SERVICE_NAME and rownum<2),MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
				+ "from SERVICE_MASTER where  SERVICE_CODE=? and SERVICE_TYPE is null";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is  [" + connection  + "]");

			storeMap = new HashMap<String, Object>();

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.SERVICE_CODE));

			storeRS = storePstmt.executeQuery();
			while (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.SERVICE_CODE,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.SERVICE_NAME,
						storeRS.getString(2));
				responseJSON.put(CevaCommonConstants.MAKER_ID,
						storeRS.getString(3));
				responseJSON.put(CevaCommonConstants.MAKER_DATE,
						storeRS.getString(4));
			}
			storeMap.put(CevaCommonConstants.SERVICE_INFO, responseJSON);
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in ServiceViewDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in ServiceViewDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			storeMap = null;
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
			
		}
		return responseDTO;
	}

	public ResponseDTO SubServiceCreateScreenDetails(RequestDTO requestDTO) {

		logger.debug("Inside SubServiceCreateScreenDetails... ");
		HashMap<String, Object> serviceMap = null;
		JSONArray subServiceJSONArray = null;

		String serviceName = null;
		PreparedStatement storePstmt = null;
		PreparedStatement subServiceIdPstmt = null;
		PreparedStatement subServicePstmt = null;

		ResultSet storeRS = null;
		ResultSet subServiceIdRS = null;
		ResultSet subServiceRS = null;

		Connection connection = null;
		JSONObject json = null;
		String storeQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SERVICE_NAME and rownum<2) from SERVICE_MASTER where  SERVICE_CODE=? and SERVICE_TYPE is null";

		try {
			serviceMap = new HashMap<String, Object>();
			subServiceJSONArray = new JSONArray();
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("RequestJSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection  + "]");

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.SERVICE_CODE));

			storeRS = storePstmt.executeQuery();
			while (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.SERVICE_CODE,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.SERVICE_NAME,
						storeRS.getString(2));
				serviceName = storeRS.getString(2);
			}

			String subServiceIdQry = "Select count(SUB_SERVICE_CODE) from SERVICE_MASTER where SERVICE_TYPE is not null";

			subServiceIdPstmt = connection.prepareStatement(subServiceIdQry);

			subServiceIdRS = subServiceIdPstmt.executeQuery();
			int subServiceId = 0;
			while (subServiceIdRS.next()) {
				String serviceID = subServiceIdRS.getString(1);
				subServiceId = Integer.parseInt(serviceID);
				logger.debug("ServiceID [" + serviceID + "]");
			}
			subServiceId++;
			responseJSON
					.put(CevaCommonConstants.SUB_SERVICE_CODE, subServiceId);

			String subServiceQry;
			if (serviceName.equals("HUDUMA")) {
				subServiceQry = "Select HUDUMA_SERVICE_CODE,HUDUMA_SERVICE_DESC from HUDUMA_MASTER order by HUDUMA_SERVICE_DESC";
			} else {
				subServiceQry = "Select PROCESS_CODE,PROCESS_NAME from PROCESS_MASTER order by PROCESS_NAME";
			}

			subServicePstmt = connection.prepareStatement(subServiceQry);

			subServiceRS = subServicePstmt.executeQuery();
			json = new JSONObject();
			while (subServiceRS.next()) {

				json.put(
						CevaCommonConstants.SELECT_KEY,
						subServiceRS.getString(1) + "-"
								+ subServiceRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL,
						subServiceRS.getString(1));
				subServiceJSONArray.add(json);
				json.clear();
			}

			responseJSON.put(CevaCommonConstants.SUBSERVICE_LIST,
					subServiceJSONArray);

			serviceMap.put(CevaCommonConstants.SERVICE_INFO, responseJSON);

			logger.debug("Store Map [" + serviceMap + "]");
			responseDTO.setData(serviceMap);

		} catch (SQLException e) {
			logger.debug("SQLException in SubServiceCreateScreenDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in SubServiceCreateScreenDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			serviceMap = null;
			subServiceJSONArray = null;
			DBUtils.closeResultSet(storeRS);
			DBUtils.closeResultSet(subServiceRS);
			DBUtils.closeResultSet(subServiceIdRS);
			
			DBUtils.closePreparedStatement(subServicePstmt);
			DBUtils.closePreparedStatement(subServiceIdPstmt);
			DBUtils.closePreparedStatement(storePstmt);

			DBUtils.closeConnection(connection);		
		}
		return responseDTO;
	}

	public ResponseDTO insertSubService(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertSubService... ");

		CallableStatement callableStatement = null;
		String insertServiceProc = "{call insertSubServiceProc(?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection.prepareCall(insertServiceProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.SERVICE_CODE));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.SERVICE_NAME));
			callableStatement
					.setString(4, requestJSON
							.getString(CevaCommonConstants.SUB_SERVICE_CODE));
			callableStatement
					.setString(5, requestJSON
							.getString(CevaCommonConstants.SUB_SERVICE_TEXT));
			callableStatement
					.setString(6, requestJSON
							.getString(CevaCommonConstants.SUB_SERVICE_NAME));
			callableStatement.registerOutParameter(7, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(7);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Sub Service Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Sub Service already available. ");
			} else {
				responseDTO
						.addError("Sub Service Inofrmation Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InsertSubService [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in InsertSubService [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);		}
		return responseDTO;
	}

	public ResponseDTO FeeCreateScrrenDetails(RequestDTO requestDTO) {

		logger.debug("Inside FeeCreateScrrenDetails.. ");
		HashMap<String, Object> serviceMap = null;

		JSONArray accountJSONArray = null;
		JSONArray servicesJSONArray = null;

		PreparedStatement storePstmt = null;
		PreparedStatement feeIdPstmt = null;
		PreparedStatement accountPstmt = null;
		PreparedStatement servicesPstmt = null;

		ResultSet storeRS = null;
		ResultSet accountRS = null;
		ResultSet servicesRS = null;
		ResultSet feeIdRS = null;

		Connection connection = null;

		String storeQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME "
				+ "from bank_master "
				+ "where BANK_CODE=SERVICE_NAME and rownum<2),SUB_SERVICE_CODE,SUB_SERVICE_NAME "
				+ "from SERVICE_MASTER  where  SUB_SERVICE_CODE=?";
		JSONObject json = null;
		try {
			serviceMap = new HashMap<String, Object>();

			accountJSONArray = new JSONArray();
			servicesJSONArray = new JSONArray();
			json = new JSONObject();

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");
			storePstmt = connection.prepareStatement(storeQry);
			storePstmt
					.setString(1, requestJSON
							.getString(CevaCommonConstants.SUB_SERVICE_CODE));

			storeRS = storePstmt.executeQuery();
			if (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.SERVICE_CODE,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.SERVICE_NAME,
						storeRS.getString(2));
				responseJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
						storeRS.getString(3));
				responseJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
						storeRS.getString(4));
			}

			storeQry = null;
			storeQry = "Select count(FEE_CODE) from FEE_MASTER";
			feeIdPstmt = connection.prepareStatement(storeQry);

			feeIdRS = feeIdPstmt.executeQuery();
			int feeId = 0;
			if (feeIdRS.next()) {
				String feeID = feeIdRS.getString(1);
				feeId = Integer.parseInt(feeID);
				logger.debug(" FeeID [" + feeID + "]");
				feeID = null;
			}
			feeId++;
			responseJSON.put(CevaCommonConstants.FEE_CODE, feeId);

			storeQry = null;
			storeQry = "Select FINANCIAL_CODE,FINANCIAL_NAME from FINANCIAL_MASTER "
					+ "where trans_type='F' order by FINANCIAL_NAME";
			accountPstmt = connection.prepareStatement(storeQry);
			accountRS = accountPstmt.executeQuery();

			while (accountRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY, accountRS.getString(1)
						+ "-" + accountRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, accountRS.getString(1));
				accountJSONArray.add(json);
				json.clear();
			}
			logger.debug("AccountJSONArray [" + accountJSONArray + "]");

			responseJSON
					.put(CevaCommonConstants.ACCOUNT_LIST, accountJSONArray);

			storeQry = null;

			storeQry = "Select HUDUMA_SERVICE_CODE,HUDUMA_SERVICE_DESC "
					+ "from HUDUMA_MASTER order by HUDUMA_SERVICE_DESC";
			servicesPstmt = connection.prepareStatement(storeQry);

			servicesRS = servicesPstmt.executeQuery();

			while (servicesRS.next()) {
				json.put(CevaCommonConstants.SELECT_KEY,
						servicesRS.getString(1) + "-" + servicesRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL,
						servicesRS.getString(1));
				servicesJSONArray.add(json);
				json.clear();
			}

			logger.debug("ServicesJSONArray [" + servicesJSONArray + "]");

			responseJSON.put(CevaCommonConstants.SERVICES_LIST,
					servicesJSONArray);

			storeQry = null;
			servicesPstmt.close();
			servicesRS.close();
			servicesJSONArray.clear();

			serviceMap.put(CevaCommonConstants.FEE_INFO, responseJSON);
			responseDTO.setData(serviceMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		} catch (SQLException e) {
			logger.debug("SQLException in FeeCreateScrrenDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in FeeCreateScrrenDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			serviceMap = null;
			accountJSONArray = null;
			servicesJSONArray = null;
			DBUtils.closeResultSet(feeIdRS);
			DBUtils.closeResultSet(servicesRS);
			DBUtils.closeResultSet(accountRS);
			DBUtils.closeResultSet(storeRS);
			
			DBUtils.closePreparedStatement(servicesPstmt);
			DBUtils.closePreparedStatement(feeIdPstmt);
			DBUtils.closePreparedStatement(accountPstmt);
			DBUtils.closePreparedStatement(storePstmt);
			
			DBUtils.closeConnection(connection);		
		}
		return responseDTO;
	}

	public ResponseDTO insertFeeDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InsertFeeDetails... ");
		CallableStatement callableStatement = null;

		String serviceType = "";
		String slabFrom = "";
		String slabTo = "";
		String hudumaServiceCode = "";
		String hudumaServiceName = "";
		String hudumaSubService = "";

		String insertServiceProc = "{call insertFeeDetailsProc(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			serviceType = requestJSON
					.getString(CevaCommonConstants.SERVICE_TYPE);

			slabFrom = requestJSON.getString(CevaCommonConstants.SLAB_FROM);
			slabTo = requestJSON.getString(CevaCommonConstants.SLAB_TO);

			if (serviceType.equals("Y")) {
				hudumaServiceCode = requestJSON
						.getString(CevaCommonConstants.SERVICE);
				hudumaServiceName = requestJSON
						.getString(CevaCommonConstants.HUDUMA_SERVICE_NAME);
				hudumaSubService = requestJSON
						.getString(CevaCommonConstants.HUDUMA_SUB_SERVICE);
			}

			callableStatement = connection.prepareCall(insertServiceProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.SERVICE_CODE));
			callableStatement
					.setString(3, requestJSON
							.getString(CevaCommonConstants.SUB_SERVICE_CODE));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.FEE_CODE));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.SERVICE_TYPE));
			callableStatement.setString(6, slabFrom);
			callableStatement.setString(7, slabTo);
			callableStatement.setString(8,
					requestJSON.getString(CevaCommonConstants.FALT_PERCENT));
			callableStatement.setString(9, requestJSON
					.getString(CevaCommonConstants.ACCOUNT_MULTI_DATA));
			callableStatement.setString(10, hudumaServiceCode);
			callableStatement.setString(11, hudumaServiceName);
			callableStatement.setString(12, hudumaSubService);
			callableStatement.setString(13, requestJSON.getString("feeFor"));
			callableStatement.registerOutParameter(14, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(14);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO
						.addMessages("Fee Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Fee Already Available.");
			} else {
				responseDTO.addError("Fee Information Insertion Failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InsertFeeDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in InsertFeeDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			serviceType = null;
			slabFrom = null;
			slabTo = null;
			hudumaServiceCode = null;
			hudumaServiceName = null;
			hudumaSubService = null;

			insertServiceProc = null;
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO viewSubServiceDetails(RequestDTO requestDTO) {

		logger.debug("Inside ViewSubServiceDetails... ");
		HashMap<String, Object> serviceMap = null;
		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;

		Connection connection = null;

		String storeQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SERVICE_NAME and "
				+ "rownum<2),SUB_SERVICE_CODE,SUB_SERVICE_NAME,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') "
				+ "from SERVICE_MASTER where  SUB_SERVICE_CODE=?";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("connection is [" + connection + "]");

			serviceMap = new HashMap<String, Object>();

			storePstmt = connection.prepareStatement(storeQry);
			storePstmt
					.setString(1, requestJSON
							.getString(CevaCommonConstants.SUB_SERVICE_CODE));

			storeRS = storePstmt.executeQuery();
			while (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.SERVICE_CODE,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.SERVICE_NAME,
						storeRS.getString(2));
				responseJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
						storeRS.getString(3));
				responseJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
						storeRS.getString(4));
				responseJSON.put(CevaCommonConstants.MAKER_ID,
						storeRS.getString(5));
				responseJSON.put(CevaCommonConstants.MAKER_DATE,
						storeRS.getString(6));
			}

			serviceMap.put(CevaCommonConstants.SERVICE_INFO, responseJSON);

			responseDTO.setData(serviceMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in ViewSubServiceDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in ViewSubServiceDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			serviceMap = null;

			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);		
	}
		return responseDTO;
	}

	public ResponseDTO viewFeeDetails(RequestDTO requestDTO) {

		logger.debug("Inside ViewFeeDetails.. ");
		HashMap<String, Object> serviceMap = null;

		PreparedStatement storePstmt = null;
		ResultSet storeRS = null;
		Connection connection = null;

		String storeQry = "Select (select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SM.SERVICE_NAME and rownum<2),SM.SUB_SERVICE_NAME,FM.FEE_CODE,FM.SERVICE_CODE,FM.SUB_SERVICE_CODE,FM.FLAT_PERCENT,FM.FEE_STRING,FM.SLAB_FROM,FM.SLAB_TO,FM.HUDUMMA,FM.MAKER_ID,to_char(FM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),FM.HUDUMA_SERVICE_NAME  from FEE_MASTER FM,SERVICE_MASTER SM where SM.SERVICE_CODE=FM.SERVICE_CODE and SM.SUB_SERVICE_CODE=FM.SUB_SERVICE_CODE and  FM.FEE_CODE=?";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			serviceMap = new HashMap<String, Object>();
			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1,
					requestJSON.getString(CevaCommonConstants.FEE_CODE));
			storeRS = storePstmt.executeQuery();
			while (storeRS.next()) {
				responseJSON.put(CevaCommonConstants.SERVICE_NAME,
						storeRS.getString(1));
				responseJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
						storeRS.getString(2));
				responseJSON.put(CevaCommonConstants.FEE_CODE,
						storeRS.getString(3));
				responseJSON.put(CevaCommonConstants.SERVICE_CODE,
						storeRS.getString(4));
				responseJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
						storeRS.getString(5));
				responseJSON.put(CevaCommonConstants.FALT_PERCENT,
						storeRS.getString(6));
				responseJSON.put(CevaCommonConstants.ACCOUNT_MULTI_DATA,
						storeRS.getString(7));
				responseJSON.put(CevaCommonConstants.SLAB_FROM,
						storeRS.getString(8));
				responseJSON.put(CevaCommonConstants.SLAB_TO,
						storeRS.getString(9));
				responseJSON.put(CevaCommonConstants.SERVICE_TYPE,
						storeRS.getString(10));
				responseJSON.put(CevaCommonConstants.MAKER_ID,
						storeRS.getString(11));
				responseJSON.put(CevaCommonConstants.MAKER_DATE,
						storeRS.getString(12));
				responseJSON.put(CevaCommonConstants.HUDUMA_SERVICE_NAME,
						storeRS.getString(13));
			}

			serviceMap.put(CevaCommonConstants.FEE_INFO, responseJSON);

			responseDTO.setData(serviceMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in ViewFeeDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in ViewFeeDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			serviceMap = null;
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);		
		}
		return responseDTO;
	}

	public ResponseDTO inserRegisterBin(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InserRegisterBin.. ");

		CallableStatement callableStatement = null;
		String insertRegisterBinProc = "{call inserRegisterBinProc(?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection.prepareCall(insertRegisterBinProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.BANK_CODE));
			callableStatement.setString(3,
					requestJSON.getString(CevaCommonConstants.BANK_NAME));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.BANK_MULTI_DATA));
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(5);

			logger.debug("ResultCnt from DB [" + resCnt + "]");

			if (resCnt == 1) {
				responseDTO
						.addMessages("Register Bin Information Stored Successfully. ");
			} else if (resCnt == -1) {
				responseDTO.addError("Registerd Bank Code already available. ");
			} else {
				responseDTO
						.addError("Register Bin Information Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InserRegisterBin [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in InserRegisterBin [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			insertRegisterBinProc = null;	
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO inserProcessingCode(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InserProcessingCode... ");
		CallableStatement callableStatement = null;
		String inserProcessingCodeProc = "{call inserProcessingCodeProc(?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection.prepareCall(inserProcessingCodeProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2,
					requestJSON.getString(CevaCommonConstants.BANK_MULTI_DATA));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(3);

			logger.debug(" resultCnt from DB:::" + resCnt);
			if (resCnt == 1) {
				responseDTO.addMessages(callableStatement.getString(4));
			} else if (resCnt == -1) {
				responseDTO.addError(callableStatement.getString(4));
			} else {
				responseDTO
						.addError("Processing Inofrmation Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InserProcessingCode ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in InserProcessingCode ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO inserhudumaService(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside InserhudumaService... ");

		CallableStatement callableStatement = null;
		String inserHudumaCodeProc = "{call inserHudumaServiceCodeProc(?,?,?,?,?,?,?)}";

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			callableStatement = connection.prepareCall(inserHudumaCodeProc);
			callableStatement.setString(1,
					requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.setString(2, requestJSON
					.getString(CevaCommonConstants.HUDUMA_SERVICE_CODE));
			callableStatement.setString(3, requestJSON
					.getString(CevaCommonConstants.HUDUMA_SERVICE_DESC));
			callableStatement.setString(4,
					requestJSON.getString(CevaCommonConstants.VIRTUAL_CARD));
			callableStatement.setString(5,
					requestJSON.getString(CevaCommonConstants.BANK_MULTI_DATA));
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);

			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(6);

			logger.debug("ResultCnt from DB [" + resCnt + "]");
			if (resCnt == 1) {
				responseDTO.addMessages(callableStatement.getString(7));
			} else if (resCnt == -1) {

				responseDTO.addError(callableStatement.getString(7));
			} else {
				responseDTO
						.addError("Huduma Service Information Insertion failed.");
			}

		} catch (SQLException e) {
			logger.debug("SQLException in InserhudumaService ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in InserhudumaService ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			inserHudumaCodeProc = null;
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getBinViewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetBinViewDetails... ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray binJSONArray = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String merchantQry = "SELECT  BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,MAKER_ID ,to_char(MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') "
				+ "FROM   BANK_MASTER order by BANK_CODE";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			binJSONArray = new JSONArray();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			JSONObject json = null;
			while (merchantRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.BANK_CODE, merchantRS.getString(1));
				json.put(CevaCommonConstants.BANK_NAME, merchantRS.getString(2));
				json.put(CevaCommonConstants.BIN, merchantRS.getString(3));
				json.put(CevaCommonConstants.BIN_DESC, merchantRS.getString(4));
				json.put(CevaCommonConstants.MAKER_ID, merchantRS.getString(5));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(6));
				binJSONArray.add(json);
				json.clear();
				json = null;
			}
			resultJson
					.put(CevaCommonConstants.MERCHANT_DASHBOARD, binJSONArray);

			merchantMap.put(CevaCommonConstants.MERCHANT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);

		} catch (SQLException e) {
			logger.debug("SQLException in GetBinViewDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetBinViewDetails [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			merchantMap = null;
			resultJson = null;
			binJSONArray = null;
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getProcessingCodeViewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetProcessingCodeViewDetails... ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray processCodeJSONArray = null;

		Connection connection = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		String merchantQry = "SELECT PROCESS_CODE ,PROCESS_NAME ,MAKER_ID,to_char(MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') FROM   PROCESS_MASTER order by PROCESS_CODE";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			processCodeJSONArray = new JSONArray();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();
			JSONObject json = null;
			while (merchantRS.next()) {
				json = new JSONObject();
				json.put(CevaCommonConstants.PROC_CODE, merchantRS.getString(1));
				json.put(CevaCommonConstants.PROCESS_NAME,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.MAKER_ID, merchantRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(4));
				processCodeJSONArray.add(json);
				json.clear();
				json = null;
			}

			resultJson.put(CevaCommonConstants.MERCHANT_DASHBOARD,
					processCodeJSONArray);

			merchantMap.put(CevaCommonConstants.MERCHANT_DASHBOARD, resultJson);
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetProcessingCodeViewDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetProcessingCodeViewDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			merchantMap = null;
			resultJson = null;
			processCodeJSONArray = null;
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getHudumaServiceViewDetails(RequestDTO requestDTO) {

		logger.debug("Inside GetHudumaServiceViewDetails... ");

		HashMap<String, Object> merchantMap = null;

		JSONObject resultJson = null;
		JSONArray processCodeJSONArray = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		Connection connection = null;
		String merchantQry = "SELECT HM.HUDUMA_SERVICE_CODE,HM.VIRTUAL_CARD ,HS.HPROCESS_CODE,HS.HPROCESS_NAME,"
				+ "HM.MAKER_ID,to_char(HS.MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') "
				+ "FROM  HUDUMA_MASTER HM,HUDUMA_SERVICES HS "
				+ "where trim(HM.REF_KEY)=trim(HS.REF_KEY) order by HM.HUDUMA_SERVICE_CODE,HS.HPROCESS_CODE";
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			json = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + connection + "]");

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			processCodeJSONArray = new JSONArray();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put(CevaCommonConstants.HUDUMA_SERVICE_CODE,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.VIRTUAL_CARD,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.HUDUMA_SUB_SERVICE,
						merchantRS.getString(3));
				json.put(CevaCommonConstants.HUDUMA_SUB_SERVICE_NAME,
						merchantRS.getString(4));
				json.put(CevaCommonConstants.MAKER_ID, merchantRS.getString(5));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(6));
				processCodeJSONArray.add(json);
				json.clear();
			}

			resultJson.put(CevaCommonConstants.MERCHANT_DASHBOARD,
					processCodeJSONArray);

			merchantMap.put(CevaCommonConstants.MERCHANT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetProcessingCodeViewDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetProcessingCodeViewDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			resultJson = null;
			processCodeJSONArray = null;
		}
		return responseDTO;
	}

	public ResponseDTO getFeeDashBoard(RequestDTO requestDTO) {

		logger.debug("Inside GetFeeDashBoard... ");
		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJSONArray = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		Connection connection = null;
		/*
		 * String merchantQry=
		 * "SELECT SM.SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SM.SERVICE_NAME and rownum<2),SM.SUB_SERVICE_CODE,SM.SUB_SERVICE_NAME,FM.FEE_CODE"
		 * +
		 * " FROM   SERVICE_MASTER SM FULL OUTER JOIN FEE_MASTER FM ON (FM.SERVICE_CODE = SM.SERVICE_CODE and FM.SUB_SERVICE_CODE = SM.SUB_SERVICE_CODE)  "
		 * + "order by SM.SERVICE_CODE";
		 */

		String merchantQry = "SELECT SM.SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SM.SERVICE_NAME and rownum<2),SM.SUB_SERVICE_CODE,SM.SUB_SERVICE_NAME,FM.FEE_CODE "
				+ "FROM SERVICE_MASTER SM, FEE_MASTER FM  "
				+ "where FM.SERVICE_CODE =+ SM.SERVICE_CODE and FM.SUB_SERVICE_CODE = SM.SUB_SERVICE_CODE "
				+ "order by SM.SERVICE_CODE";
		JSONObject json = null;
		try {

			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + connection + "]");

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJSONArray = new JSONArray();
			json = new JSONObject();
			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put(CevaCommonConstants.SERVICE_CODE,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.SERVICE_NAME,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.SUB_SERVICE_CODE,
						merchantRS.getString(3));
				json.put(CevaCommonConstants.SUB_SERVICE_NAME,
						merchantRS.getString(4));
				json.put(CevaCommonConstants.FEE_CODE, merchantRS.getString(5));
				merchantJSONArray.add(json);
				json.clear();
			}
			logger.debug("MerchantJSONArray [" + merchantJSONArray + "]");
			resultJson.put(CevaCommonConstants.MERCHANT_DASHBOARD,
					merchantJSONArray);

			merchantMap.put(CevaCommonConstants.MERCHANT_DASHBOARD, resultJson);
			logger.debug("MerchantMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in GetFeeDashBoard [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {

			logger.debug("SQLException in GetFeeDashBoard [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			merchantMap = null;
			resultJson = null;
			merchantJSONArray = null;
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

}
