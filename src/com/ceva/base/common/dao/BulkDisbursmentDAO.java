package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.log4j.Logger;

import com.ceva.base.common.bean.BulkBean;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class BulkDisbursmentDAO {

	private Logger logger = Logger.getLogger(BulkDisbursmentDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO bulkDisbursement(List<String> data, RequestDTO requestDTO) {
		logger.debug("Inside Bulk Disbursment... ");

		Connection connection = null;
		CallableStatement callableStatement = null;

		String errorMessage = "";
		String arrayData[] = null;
		String updateUserDet = "{call pBulkWithdrawalProfilewallet(?,?,?,?,?)}";

		DatabaseMetaData dmd = null;
		Connection metaDataConnection = null;
		OracleConnection oraConnection = null;

		ArrayDescriptor des = null;
		ARRAY array_to_pass = null;
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");
			arrayData = data.toArray(new String[data.size()]);
			logger.debug("arrayData::::"+arrayData.toString());
			connection = DBConnector.getConnection();

			dmd = connection.getMetaData();

			if (dmd != null) {
				metaDataConnection = dmd.getConnection();
			}

			if (!(metaDataConnection instanceof OracleConnection)) {
				throw new Exception("This is not oracle instance.....");
			}

			oraConnection = (OracleConnection) metaDataConnection;

			des = ArrayDescriptor
					.createDescriptor("ARRAY_TABLE", oraConnection);
			array_to_pass = new ARRAY(des, oraConnection, arrayData);

			callableStatement = connection.prepareCall(updateUserDet);
			callableStatement.setArray(1, array_to_pass);
			callableStatement.setString(2, requestJSON.getString("makerId"));
			callableStatement.setString(3, requestJSON.getString("filename"));
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.setString(5, requestJSON.getString("org"));

			callableStatement.execute();

			errorMessage = callableStatement.getString(4);
			logger.debug("errorMessage [" + errorMessage + "]");

			if (errorMessage.contains("success")) {
				responseDTO.addMessages(errorMessage);
			} else {
				responseDTO.addError(errorMessage);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug("SQLException in Bulk Disbursment [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception in Bulk Disbursment  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {

			try {

				des = null;
				array_to_pass = null;

				if (metaDataConnection != null) {
					metaDataConnection.close();
				}
				if (dmd != null) {
					dmd = null;
				}
				if (oraConnection != null) {
					oraConnection.close();
				}

			} catch (Exception e2) {
			}

			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);

			errorMessage = null;
			updateUserDet = null;
		}
		return responseDTO;
	}

	public static void main(String[] args) {
		String data ="Bulk disbursement is done successfully with reference number : 08092015161358415854000";
		System.out.println(data.contains("success"));
	}
	public ResponseDTO viewBulkWithdrawals(RequestDTO requestDTO) {

		logger.debug("Inside View Bulk Withdrawers... ");
		HashMap<String, Object> storeMap = null;
		JSONObject resultJson = null;

		JSONArray jsonArray = null;

		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		BulkBean bulkBean = null;

		String query = "select WBD.REF_NO,WBD.MAKER_ID,to_char(WBD.MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS'),"
				+ "decode(UPLOAD_STATUS,'S','Success',UPLOAD_STATUS),WBD.FILE_NAME,"
				+ "(select count(*) from W_BULK_DISB_DATA  where BULK_DISB_ID=WBD.REF_NO and ERROR_CODE=00) SUCCESS,"
				+ " (select count(*) from W_BULK_DISB_DATA  where BULK_DISB_ID=WBD.REF_NO and ERROR_CODE<>00) FAIL from W_BULK_DISB WBD "
				+ "order by WBD.MAKER_DTTM desc ";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			jsonArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			prepareStatement = connection.prepareStatement(query);
			resultSet = prepareStatement.executeQuery();

			while (resultSet.next()) {
				bulkBean = new BulkBean();
				bulkBean.setRefNo(resultSet.getString(1));
				bulkBean.setCreatedBy(resultSet.getString(2));
				bulkBean.setCreatedDate(resultSet.getString(3));
				bulkBean.setStatus(resultSet.getString(4));
				bulkBean.setFileName(resultSet.getString(5));
				bulkBean.setSuccessRec(resultSet.getInt(6));
				bulkBean.setFailureRec(resultSet.getInt(7));
				jsonArray.add(bulkBean);
				bulkBean.clear();
			}

			resultJson.put("bulk_data", jsonArray);
			BulkBean biller1 = new BulkBean();
			biller1.setResponseJSON(resultJson);

			storeMap.put("bulk_data", biller1);

			logger.debug("Entity Map [" + storeMap + "]");
			responseDTO.setData(storeMap);
			logger.debug("Response DTO [" + responseDTO + "]");
		} catch (SQLException e1) {
			e1.printStackTrace();
			logger.debug("SQLException in Bulk Withdrawers [" + e1.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured.");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.debug("Exception in Bulk Withdrawers [" + e1.getMessage()
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

	public ResponseDTO viewIndividualBulkUploadData(RequestDTO requestDTO) {

		logger.debug("Inside View Registered Billers... ");
		HashMap<String, Object> storeMap = null;
		Connection connection = null;

		PreparedStatement prepareStatement = null;
		ResultSet resultSet = null;

		BulkBean billerBean = null;
		JSONObject resultJson = null;
		JSONArray jsonArray = null;

		String query = "select  ERROR_CODE,ERROR_MESSAGE,IND_BULK_DATA,MAKER_ID,to_char(MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS'),ID "
				+ "from W_BULK_DISB_DATA where BULK_DISB_ID=? order by MAKER_DTTM desc";

		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			requestJSON = requestDTO.getRequestJSON();

			storeMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			jsonArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			prepareStatement = connection.prepareStatement(query);
			prepareStatement.setString(1, requestJSON.getString("id"));
			resultSet = prepareStatement.executeQuery();
			billerBean = new BulkBean();

			while (resultSet.next()) {
				billerBean.setRefNo(resultSet.getString(6));
				billerBean.setErrorCode(resultSet.getString(1));
				billerBean.setErrorDescription(resultSet.getString(2));
				billerBean.setData(resultSet.getString(3));
				billerBean.setCreatedBy(resultSet.getString(4));
				billerBean.setCreatedDate(resultSet.getString(5));

				jsonArray.add(billerBean);

				billerBean.clear();
			}

			resultJson.put("bulk_data", jsonArray);
			BulkBean biller1 = new BulkBean();
			biller1.setResponseJSON(resultJson);

			storeMap.put("bulk_data", biller1);

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

}
