package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class TMSDAO {

	
	Logger logger=Logger.getLogger(TMSDAO.class);
	
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	
	public ResponseDTO insertTMSFileData(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside [TMSDAO][insertTMSFileData].. ");
		JSONObject resultJson = null;
		PreparedStatement pstmt = null;
		HashMap<String, Object> dataMap = null;

		String qry = "INSERT INTO TMS_FILE_MASTER(FILE_NAME,UPLOADED_BY,UPLOADED_DATE) values (?,?,sysdate)";
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");
			
			dataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			resultJson = new JSONObject();
			
			
			pstmt = connection.prepareStatement(qry);
			pstmt.setString(1, requestJSON.getString("filename"));
			pstmt.setString(2, requestJSON.getString("makerId"));
			
			int cnt=pstmt.executeUpdate();
			
			connection.commit();
			
			logger.debug("cnt::"+cnt);
			
			if(cnt>0){
				resultJson.put("STATUS", "SUCCESS");
			}else{
				resultJson.put("STATUS", "FAIL");
			}
			
			logger.debug("resultJson::"+resultJson);
			
			dataMap.put("UPLOAD_INFO", resultJson);
			
			responseDTO.setData(dataMap);

		} catch (Exception e) {
			logger.debug("Got Exception in fetchBillerDataTableDetails ["+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {

			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			resultJson = null;

		}
		return responseDTO;
	}
	
	
	public ResponseDTO tmsFileUploadDashboard(RequestDTO requestDTO) {

		logger.debug("Inside tmsFileUploadDashboard... ");
		HashMap<String, Object> dataMap = null;
		JSONObject resultJson = null;
		JSONArray jsonArray = null;

		PreparedStatement pstmt = null;

		ResultSet rs = null;
		Connection connection = null;

		String merchantQry = "SELECT FILE_NAME,UPLOADED_BY,to_char(UPLOADED_DATE,'DD-MM-YYYY HH24:MI:SS') UPLOADED_DATE1  from TMS_FILE_MASTER order by UPLOADED_DATE desc";
		JSONObject json = null;
		try {
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();

			dataMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			jsonArray = new JSONArray();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			pstmt = connection.prepareStatement(merchantQry);
			rs = pstmt.executeQuery();

			json = new JSONObject();
			while (rs.next()) {
				
				String fileName = rs.getString(1).substring(9, rs.getString(1).length());
				
				json.put("FILE_NAME", fileName);
				json.put("UPLOADED_BY", rs.getString(2));
				json.put("UPLOADED_DATE", rs.getString(3));
				jsonArray.add(json);
				json.clear();
			}

			logger.debug("jsonArray [" + jsonArray + "]");
			resultJson.put("TMS_DASHBOARD",	jsonArray);

			dataMap.put("TMS_DASHBOARD", resultJson);
			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("ResponseDTO [" + responseDTO + "]");

		} catch (Exception e) {
			logger.debug("Exception in tmsFileUploadDashboard [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			resultJson = null;
			jsonArray = null;
			merchantQry = null;
		}
		return responseDTO;
	}


}
