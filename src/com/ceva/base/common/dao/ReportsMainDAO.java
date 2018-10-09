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

public class ReportsMainDAO {

	/**
	 * @Author : Ravi D
	 * @since : 14-07-2014
	 * @version : V1.0
	 */
	
	private static final long serialVersionUID = 1L;
	
	Logger logger=Logger.getLogger(ReportsMainDAO.class);

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;



	public ResponseDTO getReportNames(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		Connection connection=null;
		logger.debug("inside [ReportsMainDAO][getReportNames]");
		HashMap<String,Object> reportMap=null;
		JSONObject resultJson=null;
		JSONArray reportJSONArray=null;

		PreparedStatement reportPstmt=null;
		ResultSet reportRS=null;

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			reportMap=new HashMap<String,Object>();
			resultJson=new JSONObject();
			reportJSONArray=new JSONArray();
			
			requestJSON=requestDTO.getRequestJSON();
			logger.debug("inside [ReportsMainDAO][getReportNames][connection:::"+connection+"][requestJSON:::"+requestJSON+"]");

			String reportQry="Select REPORT_NAME,REPORT_DESC from REPORTS_CONFIG_TBL where REPORT_TYPE=trim(?)";
			
			reportPstmt=connection.prepareStatement(reportQry);
			reportPstmt.setString(1, requestJSON.getString("REPORT_TYPE"));
			reportRS=reportPstmt.executeQuery();
			
			JSONObject json=null;
			while(reportRS.next()){
				json=new JSONObject();
				json.put(CevaCommonConstants.SELECT_KEY, reportRS.getString(2));
				json.put(CevaCommonConstants.SELECT_VAL, reportRS.getString(1));
				reportJSONArray.add(json);
			}
			logger.debug("inside [ReportsMainDAO][getReportNames][reportJSONArray:::"+reportJSONArray+"]");

			resultJson.put("REPORT_LIST", reportJSONArray);					
			reportMap.put("REPORT_LIST", resultJson);
			logger.debug("inside [ReportsMainDAO][getReportNames][reportMap:::"+reportMap+"]");
			responseDTO.setData(reportMap);
			logger.debug("inside [ReportsMainDAO][getReportNames][responseDTO:::"+responseDTO+"]");

		} catch (SQLException e) {
			 
		}

		finally{

			DBUtils.closeResultSet(reportRS);
			DBUtils.closePreparedStatement(reportPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}
	
	
}
