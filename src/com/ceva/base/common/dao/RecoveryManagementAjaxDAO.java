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

public class RecoveryManagementAjaxDAO {

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;


	Logger logger=Logger.getLogger(RecoveryManagementAjaxDAO.class);



	public ResponseDTO getStoreDetails(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		Connection connection=null;
		logger.debug("inside [RecoveryManagementAjaxDAO][getStoreDetails]");
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
			logger.debug("inside [RecoveryManagementAjaxDAO][getStoreDetails][connection:::"+connection+"]");

			String merchantQry="Select STORE_ID,STORE_ID||'~'||STORE_NAME from STORE_MASTER where MERCHANT_ID=trim(?)";
			
			String merchantData=requestJSON.getString(CevaCommonConstants.MERCHANT_ID);
			String merchantId=merchantData.split("~")[0];
			logger.debug("Merchant Id::::"+merchantId);
			storePstmt=connection.prepareStatement(merchantQry);
			storePstmt.setString(1, merchantId);
			storeRS=storePstmt.executeQuery();
			
			JSONObject json=null;
			while(storeRS.next()){
				json=new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, storeRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, storeRS.getString(1));
				storeJSONArray.add(json);
			}
			logger.debug("inside [RecoveryManagementAjaxDAO][getStoreDetails][storeJSONArray:::"+storeJSONArray+"]");

			resultJson.put(CevaCommonConstants.STORE_LIST, storeJSONArray);					
			storeMap.put(CevaCommonConstants.STORE_LIST, resultJson);
			logger.debug("inside [RecoveryManagementAjaxDAO][getStoreDetails][storeMap:::"+storeMap+"]");
			responseDTO.setData(storeMap);
			logger.debug("inside [RecoveryManagementAjaxDAO][getStoreDetails][responseDTO:::"+responseDTO+"]");

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
