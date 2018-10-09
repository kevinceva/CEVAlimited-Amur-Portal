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

public class RecoveryManagementAjaxDAO2 {

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;


	Logger logger=Logger.getLogger(RecoveryManagementAjaxDAO2.class);



	

	
	public ResponseDTO getTerminalDetails(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		Connection connection=null;
		logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails]");
		HashMap<String,Object> storeMap=null;
		JSONObject resultJson=null;
		JSONArray storeJSONArray=null;

		PreparedStatement storePstmt=null;

		ResultSet storeRS=null;

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			storeMap=new HashMap<String,Object>();
			resultJson=new JSONObject();
			storeJSONArray=new JSONArray();
			
			requestJSON=requestDTO.getRequestJSON();
			logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails][connection:::"+connection+"]");
			String task=null;
			String storeQry=null;
			if(requestJSON.containsKey("TASK"))
				task = requestJSON.getString("TASK");
			if(task!=null)
			 storeQry="Select TERMINAL_LIMIT from TERMINAL_FLOAT_TBL where TERMINAL_ID=trim(?)";
			else
				 storeQry="Select TERMINAL_CURR_AMT from TERMINAL_FLOAT_TBL where TERMINAL_ID=trim(?)";
			
			String terminalId=requestJSON.getString(CevaCommonConstants.TERMINAL_ID);
			storePstmt=connection.prepareStatement(storeQry);
			storePstmt.setString(1, terminalId);
			storeRS=storePstmt.executeQuery();
			String terminalAmt=null;
			while(storeRS.next()){
				terminalAmt=storeRS.getString(1);
			}
			logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails][storeJSONArray:::"+storeJSONArray+"]");
	
			resultJson.put("TERMINAL_AMT", terminalAmt);	
			storeMap.put(CevaCommonConstants.TERMINAL_LIST, resultJson);
			logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails][storeMap:::"+storeMap+"]");
			responseDTO.setData(storeMap);
			logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails][responseDTO:::"+responseDTO+"]");

		} catch (SQLException e) {
			 
		}

		finally{
			DBUtils.closeResultSet(storeRS);
			DBUtils.closePreparedStatement(storePstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}


}
