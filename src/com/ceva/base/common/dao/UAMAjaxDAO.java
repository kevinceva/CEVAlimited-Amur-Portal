package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class UAMAjaxDAO {

	Logger logger=Logger.getLogger(UAMAjaxDAO.class);
	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null; 

	public ResponseDTO fetchBranchDetailsAct(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		Connection connection=null;
		
		HashMap<String,Object> regionDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		JSONArray branchJSONArray=new JSONArray();
		JSONArray designatonJSONArray=new JSONArray();
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[UAMAjaxDAO][fetchBranchDetailsAct][requestJSON:::"+requestJSON+"]");
		String typeOfReg=requestJSON.getString("TYPE_OF_REG");
	
		PreparedStatement branchPstmt = null;
		CallableStatement callableStatement = null;
		ResultSet branchRS= null;
		
		logger.debug(" [UAMAjaxDAO][fetchBranchDetailsAct][connection:::"+connection+"]");
		String regionQry="Select BRANCH_ID,BRANCH_ID||'-'||BRANCH_NAME from BRANCH_MASTER where APPL_TYPE=? order by BRANCH_ID";
		String designationQry="Select STATUS,KEY_VALUE from CONFIG_DATA where KEY_GROUP='USER_DESIGNATION' and KEY_TYPE=? and STATUS not in(?)   order by KEY_VALUE";
		String getUserLevels = "{call getUserLevelsProc(?,?)}";
		
		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			
			branchPstmt=connection.prepareStatement(regionQry);
			branchPstmt.setString(1, typeOfReg);
			branchRS=branchPstmt.executeQuery();
			JSONObject json=null;
			while(branchRS.next()){
				json=new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, branchRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, branchRS.getString(1));
				branchJSONArray.add(json);
			}
			
			branchPstmt=null;
			branchRS=null;
			
			callableStatement = connection.prepareCall(getUserLevels);
			callableStatement.setString(1, requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			
			callableStatement.executeUpdate();
			String userLevels = callableStatement.getString(2);
			branchPstmt=connection.prepareStatement(designationQry);
			branchPstmt.setString(1, typeOfReg);
			branchPstmt.setString(2, userLevels);
			branchRS=branchPstmt.executeQuery();
			while(branchRS.next()){
				json=new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, branchRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, branchRS.getString(1));
				designatonJSONArray.add(json);
			}
			resultJson.put("BRANCH_LIST", branchJSONArray);
			resultJson.put("DESIG_LIST", designatonJSONArray);
			regionDataMap.put("BRANCH_LIST", resultJson);
			responseDTO.setData(regionDataMap);

		} catch (SQLException e) {
			 
		}finally{

			DBUtils.closeResultSet(branchRS);
			DBUtils.closePreparedStatement(branchPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}
}
