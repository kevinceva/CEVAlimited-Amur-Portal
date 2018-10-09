package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import com.ceva.base.common.utils.StringUtil;
import java.text.ParseException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class FinancialAccountingDAO {

	Logger logger=Logger.getLogger(FinancialAccountingDAO.class);

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject responseJSON=null;
	Connection connection=null;
	
	public ResponseDTO fetchProductDetails(RequestDTO requestDTO)
	{
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("inside [FinancialAccountingDAO][fetchProductDetails]");
		HashMap<String,Object> limitDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		JSONArray ProductJSONArray=new JSONArray();
		PreparedStatement ProductPstmt=null;
		ResultSet ProductRS=null;
		String prdQry="SELECT PRD_CODE,PRD_CODE||'~'||PRD_DESC from PRD_MASTER ORDER BY PRD_CODE";
		try {
			connection=DBConnector.getConnection();
			ProductPstmt=connection.prepareStatement(prdQry);
			ProductRS=ProductPstmt.executeQuery();
			JSONObject json=null;
			while(ProductRS.next()){
				 json=new JSONObject();
				 json.put("key", ProductRS.getString(2));
				 json.put("val", ProductRS.getString(1));
				 ProductJSONArray.add(json);
			}
	  		logger.debug("[FinancialAccountingDAO][fetchProductDetails][ProductJSONArray::::::::"+ProductJSONArray);
			resultJson.put("PRODUCT_LIST", ProductJSONArray);
			logger.debug("[FinancialAccountingDAO][fetchProductDetails][resultJson::::::::"+resultJson+"]");
			limitDataMap.put("PRODUCTDATA_DETAILS", resultJson);
			responseDTO.setData(limitDataMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally
		{
			DBUtils.closePreparedStatement(ProductPstmt);
			DBUtils.closeResultSet(ProductRS);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}
	
	public ResponseDTO fetchAccountTypeData(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		
		logger.debug("inside [FinancialAccountingDAO][fetchAccountTypeData]");
		HashMap<String,Object> PrdDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		JSONArray prcaccJSONArray=new JSONArray();
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[FinancialAccountingDAO][fetchAccountTypeData][requestJSON:::"+requestJSON+"]");
		PreparedStatement prdaccPstmt = null;
		ResultSet prdaccRS= null;
		
		String regionQry="SELECT FIN_ACCT_CODE,FIN_ACCT_NAME FROM GL_MASTER WHERE PRD_CODE=?";
		try {
			connection=DBConnector.getConnection();
			logger.debug("[FinancialAccountingDAO][fetchAccountTypeData][connection:::"+connection+"]");
			prdaccPstmt=connection.prepareStatement(regionQry);
			prdaccPstmt.setString(1, requestJSON.getString("PRODUCT_TYPE"));
			prdaccRS=prdaccPstmt.executeQuery();
				
			JSONObject json=null;
			while(prdaccRS.next()){
				json=new JSONObject();
				json.put("key", prdaccRS.getString(1));
				json.put("val", prdaccRS.getString(2));
				prcaccJSONArray.add(json);
			}
			logger.debug("[FinancialAccountingDAO][fetchAccountTypeData][prcaccJSONArray:::"+prcaccJSONArray+"]");

			resultJson.put("PRODUCT_ACCOUNT_TYPE", prcaccJSONArray);
			PrdDataMap.put("PRODUCT_ACCOUNT_TYPE", resultJson);
			responseDTO.setData(PrdDataMap);

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtils.closePreparedStatement(prdaccPstmt);
			DBUtils.closeResultSet(prdaccRS);
			DBUtils.closeConnection(connection);
		}

		
		return responseDTO;
	}
	
	public ResponseDTO fetchAccountTypeDetails(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		
		logger.debug("inside [FinancialAccountingDAO][fetchAccountTypeDetails]");
		HashMap<String,Object> PrdDataMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		JSONArray prcaccJSONArray=new JSONArray();
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[FinancialAccountingDAO][fetchAccountTypeDetails][requestJSON:::"+requestJSON+"]");
		PreparedStatement prdaccPstmt = null;
		ResultSet prdaccRS= null;
		
		String regionQry="SELECT FIN_ACCT_CODE,FIN_ACCT_NAME FROM GL_MASTER WHERE PRD_CODE=?";
		try {
			
			connection=DBConnector.getConnection();
			logger.debug("[FinancialAccountingDAO][fetchAccountTypeDetails][connection:::"+connection+"]");
			
			logger.debug("regionQry:"+regionQry);
			prdaccPstmt=connection.prepareStatement(regionQry);
			prdaccPstmt.setString(1, requestJSON.getString("PRODUCT_TYPE1"));
			prdaccRS=prdaccPstmt.executeQuery();
		
			
			JSONObject json=null;
			while(prdaccRS.next()){
				json=new JSONObject();
				json.put("key", prdaccRS.getString(1));
				json.put("val", prdaccRS.getString(2));
				prcaccJSONArray.add(json);
			}
			logger.debug("[FinancialAccountingDAO][fetchAccountTypeDetails][prcaccJSONArray:::"+prcaccJSONArray+"]");

			resultJson.put("PRODUCT_ACCOUNT_TYPE", prcaccJSONArray);
			PrdDataMap.put("PRODUCT_ACCOUNT_TYPE", resultJson);
			responseDTO.setData(PrdDataMap);

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
			DBUtils.closePreparedStatement(prdaccPstmt);
			DBUtils.closeResultSet(prdaccRS);
			DBUtils.closeConnection(connection);
		}
	
		return responseDTO;
	}
	
	public ResponseDTO glAdjustmentInsert(RequestDTO requestDTO){
			responseDTO=new  ResponseDTO();
			requestJSON=new JSONObject();
			Connection connection=null;
			logger.debug("inside [FinancialAccountingDAO][glAdjustmentInsert]");
			requestJSON=requestDTO.getRequestJSON();
			logger.debug("[FinancialAccountingDAO][glAdjustmentInsert][requestJSON:::"+requestJSON+"]");
		    String productType2=null;
		    String debitAccountType=null;
		    String productType3=null;
		    String creditAccountType=null;
		    String amount=null;
		    String exchangeRate=null;
		    String netAmount=null;
		    String  remarks=null;
			HashMap<String,Object> resultMap=new HashMap<String,Object>();
		    String makerId=requestJSON.getString("MAKER_ID");
		    productType2=requestJSON.getString("productType2");
		    debitAccountType=requestJSON.getString("debitAccountType");
		    productType3=requestJSON.getString("productType3");
		    logger.debug("inside [BlanceAdjustmentDAO][productDataInsert]"+productType3);
		    creditAccountType=requestJSON.getString("creditAccountType");
		    amount=requestJSON.getString("amount");
		    exchangeRate=requestJSON.getString("exchangeRate");
		    netAmount=requestJSON.getString("netAmount");
		    logger.debug("[FinancialAccountingDAO][glAdjustmentInsert][netAmount:"+netAmount+"]");
		    remarks=requestJSON.getString("remarks");
		    CallableStatement callableStatement = null;
		    String insertGlBal = "{call GlAdjustmentInsertProc(?,?,?,?,?,?,?,?,?,?)}";
		    try {
				connection=DBConnector.getConnection();
				callableStatement = connection.prepareCall(insertGlBal);
				callableStatement.setString(1, productType2);
				callableStatement.setString(2, debitAccountType);
				callableStatement.setString(3, productType3);
				callableStatement.setString(4, creditAccountType);
				callableStatement.setString(5, amount);
				callableStatement.setString(6, exchangeRate);
				callableStatement.setString(7, netAmount);
				callableStatement.setString(8, remarks);
				callableStatement.setString(9, makerId);
				callableStatement.registerOutParameter(10, java.sql.Types.INTEGER);
				callableStatement.executeUpdate();
				int resCnt=callableStatement.getInt(10);
				if(resCnt==1){
					responseDTO.addMessages("GL Balance  Details Stored Successfully. ");
					}else if(resCnt==-1){
						resultMap.put("RESPONSE_JSON", requestJSON);
						responseDTO.setData(resultMap);
						responseDTO.addError("GL Balance Details  Already Exists. ");
					}else{
						responseDTO.addError("GL Balance Details Creation failed.");
					}
		    }
		    catch (SQLException e)
				    {
						e.printStackTrace();
					}
		    	finally
		    	{
		    		DBUtils.closeCallableStatement(callableStatement);
		    		DBUtils.closeConnection(connection);
		    	}
			return responseDTO;
	}
	
	public ResponseDTO fetchFinAccountStatementDetails(RequestDTO requestDTO){
		logger.debug("inside [FinancialAccountingDAO][fetchFinAccountStatementDetails]");
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		
		HashMap<String,Object> AccStmtMap=new HashMap<String,Object>();
		JSONObject resultJson=new JSONObject();
		JSONArray AccStmtJSONArray=new JSONArray();
		requestJSON = requestDTO.getRequestJSON();
		
		String accountType=requestJSON.getString("accountType").split("-")[0];
		
		String toDate=requestJSON.getString("toDate");
		String fromDate=requestJSON.getString("fromDate");
		String todate2 = "";
        String fromDate2 = "";
  
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        if (!StringUtil.isNullOrEmpty(toDate)) {
            try {                
                todate2 = sdf2.format(sdf1.parse(toDate));
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if (!StringUtil.isNullOrEmpty(fromDate)) {
            try {
                
                fromDate2 = sdf2.format(sdf1.parse(fromDate));
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        
		String trnsqQry=" select TXN_DTTM,CARD_NO,FIN_ACCT_NAME,TXN_AMOUNT from GL_TRANS where FIN_ACCT_CODE=? and TXN_DTTM BETWEEN ? and ? ";
		try {
				Connection connection=DBConnector.getConnection();
				logger.debug("[FinancialAccountingDAO][fetchFinAccountStatementDetails][connection:::"+connection+"]");
				PreparedStatement AccStementPstmt=connection.prepareStatement(trnsqQry);
				AccStementPstmt.setString(1, accountType);
				AccStementPstmt.setString(2, fromDate2);
				AccStementPstmt.setString(3, todate2);
				ResultSet AccStmtRS=AccStementPstmt.executeQuery();
				JSONObject json=null;
				while(AccStmtRS.next())
				{
				 json=new JSONObject();
				 json.put("dateOfTransaction", AccStmtRS.getString(1));
				 json.put("cardNo", AccStmtRS.getString(2));
				 json.put("description", AccStmtRS.getString(3));
				 json.put("amount", AccStmtRS.getString(4));
				 AccStmtJSONArray.add(json);
				}
				 logger.debug("[FinancialAccountingDAO][fetchFinAccountStatementDetails][AccStmtJSONArray:::"+AccStmtJSONArray+"]");
					resultJson.put("ACCOUNTSTMT_DETAILS", AccStmtJSONArray);
					AccStmtMap.put("ACCOUNTSTMT_DETAILS", resultJson);
					logger.debug("[FinancialAccountingDAO][fetchFinAccountStatementDetails][AccStmtMap:::"+AccStmtMap+"]");
					responseDTO.setData(AccStmtMap);
		
				} catch (SQLException  e) {
					e.printStackTrace();
				}
		
		return responseDTO;
	}
}
