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

public class ServiceMgmtAjaxDAO {

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;


	Logger logger=Logger.getLogger(ServiceMgmtAjaxDAO.class);



	public ResponseDTO getHudumaSubService(RequestDTO requestDTO){
		responseDTO=new  ResponseDTO();
		requestJSON=new JSONObject();
		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		JSONArray storeJSONArray=new JSONArray();
		Connection connection=null;

		logger.debug("inside [ServiceMgmtAjaxDAO][getHudumaSubService]");
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ServiceMgmtAjaxDAO][getHudumaSubService][requestJSON:::"+requestJSON+"]");
		resonseJSON=requestJSON;

		String refKeyQry="select REF_KEY from HUDUMA_MASTER where HUDUMA_SERVICE_CODE=?";

		PreparedStatement refKeyPstmt=null;
		PreparedStatement storePstmt=null;
		ResultSet refKeyRS=null;
		ResultSet storeRS=null;
		
		String refKey="";

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("[ServiceMgmtAjaxDAO][getHudumaSubService] connection ["+connection);

			refKeyPstmt = connection.prepareStatement(refKeyQry);
			refKeyPstmt.setString(1, requestJSON.getString(CevaCommonConstants.SERVICE_CODE));
			refKeyRS=refKeyPstmt.executeQuery();
			
			
			if(refKeyRS.next()){
				refKey=refKeyRS.getString(1);
			}
			
			logger.debug("[ServiceMgmtAjaxDAO][getHudumaSubService] refKey["+refKey);
			
			String storeQry="SELECT HPROCESS_CODE,HPROCESS_NAME from HUDUMA_SERVICES  where REF_KEY=?";
			storePstmt = connection.prepareStatement(storeQry);
			storePstmt.setString(1, refKey);
			storeRS=storePstmt.executeQuery();
			JSONObject json=null;

			if(storeRS.next()){
				json=new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, storeRS.getString(1)+"-"+storeRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, storeRS.getString(1));
				storeJSONArray.add(json);
			}

			resonseJSON.put(CevaCommonConstants.SERVICE_LIST,storeJSONArray);
			logger.debug("[ServiceMgmtAjaxDAO][getHudumaSubService] resonseJSON ["+resonseJSON);
			resultMap.put(CevaCommonConstants.SERVICE_LIST, resonseJSON);
			responseDTO.setData(resultMap);

		} catch (SQLException e) {
			 
		}
		finally{
				DBUtils.closeResultSet(storeRS);
				DBUtils.closeResultSet(refKeyRS);
				DBUtils.closePreparedStatement(storePstmt);
				DBUtils.closePreparedStatement(refKeyPstmt);
				DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}


}
