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

public class InstanceReportsDAO {

	/**
	 * @Author : Ravi D
	 * @since : 14-07-2014
	 * @version : V1.0
	 */
	
	private static final long serialVersionUID = 1L;
	
	Logger logger=Logger.getLogger(InstanceReportsDAO.class);

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;

	
	public ResponseDTO getReportParameters(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		Connection connection=null;
		logger.debug("inside [ReportsMainDAO][getReportParameters]");
		HashMap<String,Object> reportMap=null;
		JSONObject resultJson=null;

		PreparedStatement reportPstmt=null;
		ResultSet reportRS=null;

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			reportMap=new HashMap<String,Object>();
			resultJson=new JSONObject();
			
			requestJSON=requestDTO.getRequestJSON();
			logger.debug("inside [ReportsMainDAO][getReportParameters][connection:::"+connection+"][requestJSON:::"+requestJSON+"]");

			String reportQry="Select PARAMETERS from REPORTS_CONFIG_TBL where REPORT_TYPE=trim(?) and REPORT_NAME=trim(?)";
			
			reportPstmt=connection.prepareStatement(reportQry);
			reportPstmt.setString(1, requestJSON.getString("REPORT_TYPE"));
			reportPstmt.setString(2, requestJSON.getString("REPORT_NAME"));
			reportRS=reportPstmt.executeQuery();
			
			while(reportRS.next()){
				resultJson.put("REPORT_PARAMETERS", reportRS.getString(1));	
			}			
			reportMap.put("REPORT_PARAMETERS", resultJson);
			logger.debug("inside [ReportsMainDAO][getReportParameters][reportMap:::"+reportMap+"]");
			responseDTO.setData(reportMap);
			logger.debug("inside [ReportsMainDAO][getReportParameters][responseDTO:::"+responseDTO+"]");

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
