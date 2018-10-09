package com.ceva.base.common.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;

public class FeeManagementDAO {
	Logger logger = Logger.getLogger(FeeManagementDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;
	Connection connection = null;

	public ResponseDTO getFeeCreateScreen(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		requestJSON=requestDTO.getRequestJSON();
		HashMap<String,Object> limitDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		JSONArray tidJSONArray=new JSONArray();
		JSONArray serviceJSONArray=new JSONArray();
		JSONArray tranJSONArray=new JSONArray();
		JSONArray frqJSONArray=new JSONArray();
		JSONArray currencyJSONArray=new JSONArray();
		PreparedStatement transactionPstmt=null;
		PreparedStatement feePstmt=null;
		ResultSet transactionRS=null;
		String feeID;
		String entityQry="SELECT NETWORK_ID FROM ACQUIRER_MASTER";
		try {
			connection=DBConnector.getConnection();
			logger.debug(" [FeeManagementDAO][getFeeCreateScreen][connection:::"+connection+"]");
			PreparedStatement tidPstmt=connection.prepareStatement(entityQry);
			ResultSet tidRS=tidPstmt.executeQuery();
			JSONObject json=null;
			while(tidRS.next()){
				 json=new JSONObject();
				 json.put("key", tidRS.getString(1));
				 json.put("val", tidRS.getString(1));
				 tidJSONArray.add(json);
			}
			
			String seriveQry="SELECT SERVICE_CODE||'-'||SERVICE_NAME,SERVICE_CODE from SERVICE_REGISTRATION";
				PreparedStatement serivePstmt=connection.prepareStatement(seriveQry);
				ResultSet serviceRS=serivePstmt.executeQuery();
				while(serviceRS.next()){
					 json=new JSONObject();
					 json.put("key", serviceRS.getString(1));
					 json.put("val", serviceRS.getString(2));
					 serviceJSONArray.add(json);
				}
				
				
				String transactionQry="SELECT TXN_NAME,TXN_CODE FROM TRANS_MASTER WHERE TXN_TYPE='F' AND DISPLAY_FLAG is Null";
				 transactionPstmt=connection.prepareStatement(transactionQry);
				 transactionRS=transactionPstmt.executeQuery();
				while(transactionRS.next()){
					 json=new JSONObject();
					 json.put("key", transactionRS.getString(1));
					 json.put("val", transactionRS.getString(2));
					 tranJSONArray.add(json);
				}
			
				
				String frqQry="select KEY_VALUE,KEY_ID FROM CONFIG_DATA WHERE KEY_GROUP='FREQUENCYDATA' AND KEY_TYPE='FREQUENCYPERIOD'";
				PreparedStatement frqPstmt=connection.prepareStatement(frqQry);
				ResultSet frqRS=frqPstmt.executeQuery();
				while(frqRS.next()){
					 json=new JSONObject();
					 json.put("key", frqRS.getString(1));
					 json.put("val", frqRS.getString(2));
					 frqJSONArray.add(json);
				}
				
				String currencyQry="SELECT CRCY_CODE FROM CURRENCY_MASTER";
				PreparedStatement currencyPstmt=connection.prepareStatement(currencyQry);
				ResultSet currencyRS=currencyPstmt.executeQuery();
				while(currencyRS.next()){
					 json=new JSONObject();
					 json.put("key", currencyRS.getString(1));
					 json.put("val", currencyRS.getString(1));
					 currencyJSONArray.add(json);
				}
			
				
				String feeName="FEE";
				String limitChkQry="Select count(*) from LMTFEE ";
				String feeKey=feeName.substring(0, 2);
				int ResCount=0;
				feePstmt=connection.prepareStatement(limitChkQry);

				ResultSet limitChkRS=null;
				limitChkRS=feePstmt.executeQuery();
				while(limitChkRS.next()){
					ResCount=limitChkRS.getInt(1);
				}
		
				if(ResCount==0){
					int i=ResCount;
					i++;
					feeID=feeKey+"000"+i;
				
				}else{
					int i=ResCount;
					i++;
					feeID=feeKey+"000"+i;
				
				}
				
				requestJSON.put("feeID", feeID);
				requestDTO.setRequestJSON(requestJSON);
				resultJson.put("NETWORK_LIST", tidJSONArray);
				resultJson.put("SERVICES_LIST", serviceJSONArray);
				resultJson.put("TRANSACTION_LIST", tranJSONArray);
				resultJson.put("FREQUENCY_LIST", frqJSONArray);
	            resultJson.put("CURRENCY_LIST", currencyJSONArray);
	            
				limitDataMap.put("LIMIT_DETAILS", resultJson);
				responseDTO.setData(limitDataMap);
				
				logger.debug("inside [FeeManagementDAO][getFeeCreateScreen][responseDTO:::"+responseDTO+"]");
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseDTO;
	}
	
	public ResponseDTO inserFeeDetails(RequestDTO requestDTO) {
	
		responseDTO = new ResponseDTO();
		resonseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();
		String tabData=(String) requestJSON.get("TABDATA");
		String feecode=requestJSON.get("FEE_CODE").toString();
		String feeDescription=requestJSON.get("FEE_DESCRIPTION").toString();
		String makerid=requestJSON.get("MAKER_ID").toString();
		
		PreparedStatement pstmt=null;
		String psql="INSERT INTO LMTFEE (LMT_CODE,LMT_DESC,LMT_FLAG,TXN_CODE,FREQUENCY,COND2CHECK,FRM_AMT,TO_AMT,FRM_CNT,TO_CNT,PER_FLAT, FLAT_AMT, PERCENTAGE,MAKER_ID,CCY,MAKER_DTTM,KEY) "+
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String[] rows=tabData.split("#");
		try {
			connection=DBConnector.getConnection();
			pstmt=connection.prepareStatement(psql);
			pstmt.clearBatch();
			for(int i=0;i<rows.length;i++){
				String[] row=rows[i].split(",");
				pstmt.setString(1, feecode);
				pstmt.setString(2, feeDescription);
				pstmt.setString(3, "F");
				pstmt.setString(4, row[0]);
				pstmt.setString(5, row[1]);
				pstmt.setString(6, row[3]);
				pstmt.setString(7, row[4]);
				pstmt.setString(8, row[5]);
				pstmt.setInt(9, Integer.parseInt(row[6]));
				pstmt.setInt(10, Integer.parseInt(row[7]));
				pstmt.setString(11, row[10]);
				pstmt.setString(12, row[9]);
				pstmt.setString(13, row[9]);
				pstmt.setString(14, makerid);
				pstmt.setString(15, row[2]);
				pstmt.setDate(16, getCurrentDate());
				pstmt.setString(17, requestJSON.get("AQUIRID").toString()+"-"+requestJSON.get("SERVICES").toString());
				pstmt.addBatch();
			}
			int[] resCnt=pstmt.executeBatch();
			

		
			if(resCnt.length>=1){
			responseDTO.addMessages("Fee Details Stored Successfully. ");
			}else{
				responseDTO.addError("Fee Creation failed.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return responseDTO;
	}
	
	
	private static java.sql.Date getCurrentDate() {
	    java.util.Date today = new java.util.Date();
	    return new java.sql.Date(today.getTime());
	}

	
}
