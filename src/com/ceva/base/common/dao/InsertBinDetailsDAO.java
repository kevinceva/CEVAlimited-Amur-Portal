package com.ceva.base.common.dao;

import java.io.Console;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.util.DBUtils;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;

public class InsertBinDetailsDAO {

	Logger logger=Logger.getLogger(InsertBinDetailsDAO.class);
	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;
	Connection connection=null;

	public ResponseDTO InsertBinDetails(RequestDTO requestDTO){
		responseDTO=new  ResponseDTO();
		requestJSON=new JSONObject();
		Connection connection=null;
	
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[InsertBinDetailsDAO][InsertBinDetails][requestJSON:::"+requestJSON+"]");
	    String bin=null;
	    String binDescription=null;
	    String binType=null;
	    PreparedStatement binPstmt=null;
	    ResultSet binRS=null;
	    JSONObject json=null;
	    JSONArray binJSONArray=null;
	    JSONObject resultJson=null;

		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		String makerId=requestJSON.getString("MAKER_ID");
		bin=requestJSON.getString("BIN");
		binDescription=requestJSON.getString("binDescription");
		binType=requestJSON.getString("binType");
		
		logger.debug("[InsertBinDetailsDAO][InsertBinDetails] connection :::"+connection);
		CallableStatement callableStatement = null;
		String insertBinInfoProc = "{call INSERT_BINDETAILS(?,?,?,?,?,?)}";
		try {
			connection=DBConnector.getConnection();
			callableStatement = connection.prepareCall(insertBinInfoProc);
			callableStatement.setString(1, bin);
			callableStatement.setString(2, binDescription);
			callableStatement.setString(3, binType);
			callableStatement.setString(4, makerId);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt=callableStatement.getInt(6);
			
			if(resCnt==1){
			responseDTO.addMessages("Bin Details Stored Successfully. ");
			}else if(resCnt==-1){

				responseDTO.addError("Bin  Already Exists. ");
			}else{
				responseDTO.addError("Bin Creation Creation failed.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

}
