package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class BenifitRegistrationDAO {

	Logger logger = Logger.getLogger(BenifitRegistrationDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	Connection connection = null;
	JSONArray jsonArray=null;

	public ResponseDTO getBenifitRegistrationList(RequestDTO requestDTO) {
		logger.debug("Inside GetBenifitRegistrationList");
		

		JSONObject resultJson = new JSONObject();
		HashMap<String, Object> benifitDataMap = new HashMap<String, Object>();
		PreparedStatement balpsmt = null;

		ResultSet resultset = null;
		String benefitQry = null;
		try {
			responseDTO = new ResponseDTO();
 			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			
			benefitQry = "SELECT BR.BENEFIT_CODE, BR.BENEFIT_NAME, BR.DISCOUNT, BR.MAKER_ID FROM BENEFIT_REGISTRATION BR";
			connection = connection == null ? DBConnector.getConnection():connection;
			balpsmt = connection.prepareStatement(benefitQry);
			resultset = balpsmt.executeQuery();
			JSONObject json = null;
			String status = null;
			jsonArray=new JSONArray();
			while (resultset.next()) {
				json = new JSONObject();
				json.put("benefitId", resultset.getString(1));
				json.put("benefitName", resultset.getString(2));
				json.put("discount", resultset.getString(3));
				json.put("makerId", resultset.getString(4));
				jsonArray.add(json);
			}
			DBUtils.closeResultSet(resultset);
			DBUtils.closePreparedStatement(balpsmt);
			resultJson.put("BENIFIT_LIST", jsonArray);
			String idquery="SELECT TO_NUMBER(SUBSTR(MAX(BENEFIT_CODE),INSTR(MAX(BENEFIT_CODE),'_')+1))+1 FROM BENEFIT_REGISTRATION";
			Statement statement=connection.createStatement();
			resultset = statement.executeQuery(idquery);
			if(resultset.next())
				resultJson.put("BENIFITID", "BR_00"+resultset.getString(1));
			benifitDataMap.put("BENIFIT_LIST_DATA", resultJson);
			responseDTO.setData(benifitDataMap);
			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(resultset);
			DBUtils.closePreparedStatement(balpsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO insertBenifit(RequestDTO requestDTO) {
		logger.debug("insertBenifit");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();

		JSONObject resultJson = new JSONObject();
		HashMap<String, Object> benifitDataMap = new HashMap<String, Object>();
		PreparedStatement balpsmt = null;

		ResultSet resultset = null;
		String benefitQry = null;
		try {
			String benefitInsertQry = "INSERT INTO BENEFIT_REGISTRATION (BENEFIT_CODE, BENEFIT_NAME, DISCOUNT,MAKER_ID) VALUES(?,?,?,?)";
			connection = connection == null ? DBConnector.getConnection():connection;
			balpsmt = connection.prepareStatement(benefitInsertQry);
			balpsmt.setString(1, requestJSON.getString("benefitId"));
			balpsmt.setString(2, requestJSON.getString("benefitName"));
			balpsmt.setString(3, requestJSON.getString("discount"));
			balpsmt.setString(4, requestJSON.getString("makerId"));
			int insert=balpsmt.executeUpdate();
			benifitDataMap.put("INSERTED", insert);
			connection.commit();
			responseDTO.setData(benifitDataMap);
			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(balpsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO getBenifitRegistration(RequestDTO requestDTO) {
		logger.debug("getBenifitRegistrationList");
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();

		JSONObject resultJson = new JSONObject();
		HashMap<String, Object> benifitDataMap = new HashMap<String, Object>();
		PreparedStatement balpsmt = null;

		ResultSet resultset = null;
		String benefitQry = null;
		try {
			benefitQry = "SELECT BR.BENEFIT_CODE, BR.BENEFIT_NAME, BR.DISCOUNT, BR.MAKER_ID FROM BENEFIT_REGISTRATION BR WHERE BR.BENEFIT_CODE='"+requestJSON.getString("benefitId")+"'";
			connection = connection == null ? DBConnector.getConnection():connection;
			balpsmt = connection.prepareStatement(benefitQry);
			resultset = balpsmt.executeQuery();
			JSONObject json = null;
			String status = null;
			jsonArray=new JSONArray();
			while (resultset.next()) {
				resultJson.put("benefitId", resultset.getString(1));
				resultJson.put("benefitName", resultset.getString(2));
				resultJson.put("discount", resultset.getString(3));
				resultJson.put("makerId", resultset.getString(4));
			}
			benifitDataMap.put("BENIFIT_LIST_DATA", resultJson);
			responseDTO.setData(benifitDataMap);
			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(resultset);
			DBUtils.closePreparedStatement(balpsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

}
