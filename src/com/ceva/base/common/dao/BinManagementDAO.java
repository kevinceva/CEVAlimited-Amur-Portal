package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class BinManagementDAO {

	Logger logger = Logger.getLogger(BinManagementDAO.class);
	
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONArray dataJsonArray = null;
	HashMap<String, Object> dataMap = null;
	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qry ="";

	public ResponseDTO fetchBinInformation(RequestDTO requestDTO) {
		
		qry="select BANK_CODE,BANK_NAME,BIN,BIN_DESC,ZPK_INDEX,DECODE(STATUS, NULL, 'Active','A','Active','B','Blocked',STATUS) STATUS from BANK_MASTER where BIN=?";
		
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			pstmt = connection.prepareStatement(qry);
			pstmt.setString(1, requestJSON.getString("BIN"));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				responseJSON.put("BANK_CODE", rs.getString(1));
				responseJSON.put("BANK_NAME", rs.getString(2));
				responseJSON.put("BIN", rs.getString(3));
				responseJSON.put("BIN_DESC", rs.getString(4));
				responseJSON.put("ZPK_INDEX", rs.getString(5));
				responseJSON.put("CURR_STATUS", rs.getString(6));
				String newStatus="";
				if(rs.getString(6).equals("Active"))
					newStatus="Blocked";
				else
					newStatus="Active";
				
				responseJSON.put("NEW_STATUS", newStatus);
				
			}
			
			logger.debug("Res JSON  [" + responseJSON + "]");

			dataMap.put("RESPONSE_DATA", responseJSON);

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in auth count [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in auth count [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}

	
public ResponseDTO changeBinStatus(RequestDTO requestDTO) {
		
		qry="update BANK_MASTER set STATUS=?,MODIFIED_BY=?,MODIFIED_DATE=sysdate where BIN=?";
		String status="";
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			logger.debug("requestJSON in DAO::"+requestJSON);
			
			if(requestJSON.getString("STATUS").equals("Active"))
				status="B";
			else
				status="A";
			
			pstmt = connection.prepareStatement(qry);
			
			pstmt.setString(1, status);
			pstmt.setString(2, requestJSON.getString(CevaCommonConstants.MAKER_ID));
			pstmt.setString(3, requestJSON.getString("BIN"));
			int i = pstmt.executeUpdate();
						
			logger.debug("Res JSON  [" + responseJSON + "]");

			if(i>0){
				responseJSON.put("STATUS", "Success");
				dataMap.put("RESPONSE_DATA", responseJSON);
			}else{
				responseDTO.addError("Internal Error Occured.Please check.");
			}

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in auth count [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in auth count [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}

	
}
