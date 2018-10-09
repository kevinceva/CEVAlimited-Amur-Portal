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
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.util.DBUtils;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
public class ProductAjaxDAO {

	Logger logger=Logger.getLogger(ProductAjaxDAO.class);
	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;
	Connection connection=null;

	public ResponseDTO InsertNewServiceDetails(RequestDTO requestDTO){
		responseDTO=new  ResponseDTO();
		requestJSON=new JSONObject();
		Connection connection=null;
		
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ProductAjaxDAO][InsertServiceDetails][requestJSON:::"+requestJSON+"]");
	    String serviceCode=null;
	    String serviceName=null;
	    HashMap<String,Object> ServiceDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		HashMap<String,Object> resultMap=new HashMap<String,Object>();
		String makerId=requestJSON.getString("MAKER_ID");
		serviceCode=requestJSON.getString("serviceCode1");
		serviceName=requestJSON.getString("serviceName");
		PreparedStatement servicepsmt=null;
		ResultSet serviceRS=null;
		CallableStatement callableStatement = null;
		String insertBinInfoProc = "{call INSERT_SERVICEDETAILS(?,?,?,?,?)}";
		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			callableStatement = connection.prepareCall(insertBinInfoProc);
			callableStatement.setString(1, serviceCode);
			callableStatement.setString(2, serviceName);
			callableStatement.setString(3, makerId);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.INTEGER);
			callableStatement.executeUpdate();
			int resCnt=callableStatement.getInt(5);
			
			if(resCnt==1){
			responseDTO.addMessages("Service Details Stored Successfully. ");
			}else if(resCnt==-1){
				resultMap.put("RESPONSE_JSON", requestJSON);
				responseDTO.setData(resultMap);
				responseDTO.addError("Service Name Already Created. ");

			}else{
				resultMap.put("RESPONSE_JSON", requestJSON);
				responseDTO.setData(resultMap);
				responseDTO.addError("Service Creation Creation failed.");
			}
				}
				catch (SQLException e)
				{
				 e.printStackTrace();
				}finally{
				
					DBUtils.closeCallableStatement(callableStatement);
					DBUtils.closeConnection(connection);
					}

		return responseDTO;
	}


	public ResponseDTO getFeecodeDetails(RequestDTO requestDTO)
	{

		responseDTO=new ResponseDTO();
		requestJSON=requestDTO.getRequestJSON();
		resonseJSON=new JSONObject();
		HashMap<String,Object> feedataMap= new HashMap<String,Object>();
		JSONObject resultJson = null;
		JSONObject json=null;
		JSONArray binJSONArray = null;
		PreparedStatement feepsmt=null;
		ResultSet feeRS=null;
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ProductAjaxDAO][InsertServiceDetails][requestJSON:::"+requestJSON+"]");
	    String feeCode=null;
	    feeCode=requestJSON.getString("feecode");

		logger.debug("inside [ProductAjaxDAO][getFeecodeDetails][connection:::"+connection+"]");
		String feeQry = "SELECT LMT_CODE,LMT_DESC,substr(KEY,0,instr(KEY,'-')-1) ACQUIR,substr(KEY,instr(KEY,'-')+1,length(KEY)) SERVICE,"

				+"TXN_CODE,FREQUENCY,FRM_CNT,TO_CNT,FRM_AMT,TO_AMT FROM LMTFEE WHERE LMT_CODE=?";
		try {
			connection = connection == null ? DBConnector.getConnection():connection;


			resultJson=new JSONObject();
			feepsmt=connection.prepareStatement(feeQry);
			feepsmt.setString(1, feeCode);
			feeRS=feepsmt.executeQuery();
			binJSONArray = new JSONArray();
			while(feeRS.next()){

				resultJson=new JSONObject();

				resultJson.put("feeCode", feeRS.getString(1));
				resultJson.put("feeDescription", feeRS.getString(2));
				resultJson.put("acquirID", feeRS.getString(3));
				resultJson.put("service", feeRS.getString(4));
				resultJson.put("transcode", feeRS.getString(5));
				resultJson.put("freQuency", feeRS.getString(6));
				resultJson.put("fromCount", feeRS.getString(7));
				resultJson.put("toCount", feeRS.getString(8));
				resultJson.put("fromAmount", feeRS.getString(9));
				resultJson.put("toAmount", feeRS.getString(10));




			}
			
			feedataMap.put("Fee_Details", resultJson);
			
			responseDTO.setData(feedataMap);
			logger.debug("inside [ProductAjaxDAO][getFeecodeDetails][responseDTO:::"+responseDTO+"]");
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{

			DBUtils.closeResultSet(feeRS);
			DBUtils.closePreparedStatement(feepsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

}
