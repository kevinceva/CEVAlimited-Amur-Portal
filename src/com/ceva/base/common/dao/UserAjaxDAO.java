package com.ceva.base.common.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class UserAjaxDAO {

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;


	Logger logger=Logger.getLogger(AjaxDAO.class);

	public ResponseDTO checkuserId(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		Connection connection=null;
		logger.debug("inside [UserAjaxDAO][checkuserId]");
		HashMap<String,Object> userDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("inside [UserAjaxDAO][checkuserId][requestJSON:::"+requestJSON+"]");
		String userId=requestJSON.getString("User_Id");
		logger.debug("inside [UserAjaxDAO][checkuserId][userId:::"+userId+"]");
		ResultSet userChkRS= null;
		PreparedStatement userChkPstmt=null;
		String merchantChkQry="";
		int ResCount=0;
		try {
			
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("inside [UserAjaxDAO][checkuserId][connection:::"+connection+"]");
			merchantChkQry="Select count(*) from USER_LOCKING_INFO where USER_ID =?";
			
			userChkPstmt=connection.prepareStatement(merchantChkQry);
			userChkPstmt.setString(1, userId);
			userChkRS=userChkPstmt.executeQuery();
			if(userChkRS.next()){
				ResCount=userChkRS.getInt(1);
			}
			logger.debug("inside [UserAjaxDAO][checkuserId][ResCount:::"+ResCount+"]");
			resultJson.put("RESULT_COUNT", ResCount);

			DBUtils.closeResultSet(userChkRS);
			DBUtils.closePreparedStatement(userChkPstmt);

			merchantChkQry="Select count(*) from USER_LOGIN_CREDENTIALS where LOGIN_USER_ID=?";

			userChkPstmt=connection.prepareStatement(merchantChkQry);
			userChkPstmt.setString(1, userId);
			userChkRS=userChkPstmt.executeQuery();
			if(userChkRS.next()){
				ResCount=userChkRS.getInt(1);
			}
			

			resultJson.put("RESULT_COUNT_USER", ResCount);

			userDataMap.put("RESULT_COUNT_DATA", resultJson);

			logger.debug("inside [UserAjaxDAO][checkuserId][userDataMap:::"+userDataMap+"]");
			responseDTO.setData(userDataMap);
			logger.debug("inside [UserAjaxDAO][checkuserId][responseDTO:::"+responseDTO+"]");

		} catch (SQLException e) {
			  
		}

		finally{

			DBUtils.closeResultSet(userChkRS);
			DBUtils.closePreparedStatement(userChkPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

}
