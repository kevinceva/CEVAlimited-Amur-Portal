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

public class RecoveryManagementAjaxDAO1 {

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;


	Logger logger=Logger.getLogger(RecoveryManagementAjaxDAO1.class);



	

	
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
		PreparedStatement recoveryPstmt=null;

		ResultSet storeRS=null;
		ResultSet RecoveryRS=null;

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			storeMap=new HashMap<String,Object>();
			resultJson=new JSONObject();
			storeJSONArray=new JSONArray();
			
			requestJSON=requestDTO.getRequestJSON();
			logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails][connection:::"+connection+"]");

			String storeQry="Select TERMINAL_ID from TERMINAL_MASTER where STORE_ID=trim(?)";
			
			String storeData=requestJSON.getString(CevaCommonConstants.STORE_ID);
			String storeId=storeData.split("~")[0];
			logger.debug("Store Id::::"+storeId);
			storePstmt=connection.prepareStatement(storeQry);
			storePstmt.setString(1, storeId);
			storeRS=storePstmt.executeQuery();
			
			JSONObject json=null;
			while(storeRS.next()){
				json=new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, storeRS.getString(1));
				json.put(CevaCommonConstants.SELECT_VAL, storeRS.getString(1));
				storeJSONArray.add(json);
			}
			logger.debug("inside [RecoveryManagementAjaxDAO][getTerminalDetails][storeJSONArray:::"+storeJSONArray+"]");
			
			String storeRecoverQry="Select SFT.INC_CASH_ALLOWED-SFT.CURR_CASH_DPT_LMT "
					+ "from STORE_MASTER SM,STORE_FLOAT_TBL SFT where SFT.MERCHANT_ID=SM.MERCHANT_ID and SM.STORE_ID=SFT.STORE_ID and SFT.STORE_ID=trim(?)";
			recoveryPstmt=connection.prepareStatement(storeRecoverQry);
			recoveryPstmt.setString(1, storeId);
			RecoveryRS=recoveryPstmt.executeQuery();
			String recoveryAmt=null;
			while(RecoveryRS.next()){
				recoveryAmt=RecoveryRS.getString(1);
			}
			
			resultJson.put(CevaCommonConstants.TERMINAL_LIST, storeJSONArray);	
			resultJson.put("RECOVERY_AMT", recoveryAmt);	
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
