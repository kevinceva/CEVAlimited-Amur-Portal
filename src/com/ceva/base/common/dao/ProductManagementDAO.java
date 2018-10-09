package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ceva.util.DBUtils;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;

public class ProductManagementDAO {
	Logger logger = Logger.getLogger(ProductManagementDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject resonseJSON = null;
	Connection connection = null;
	HashMap<String,Object> productDataMap=null;

	public ResponseDTO getExistingProductsDetails(RequestDTO requestDTO) {
		
		logger.debug("Inside GetExistingProductsDetails..... ");
		HashMap<String, Object> limitDataMap = new HashMap<String, Object>();
		JSONObject resultJson = new JSONObject();
		JSONArray grid = new JSONArray();
		PreparedStatement tidPstmt=null;
		ResultSet tidRS =null;
		String listQry = "SELECT PM.PRD_CODE, PM.PRD_DESC, SR.SERVICE_NAME, BR.BENEFIT_NAME, LF.LMT_DESC from "
				+" PRD_MASTER PM , LMTFEE LF, SERVICE_REGISTRATION SR, BENEFIT_REGISTRATION BR, BENIFIT_SETUP BS WHERE SUBSTR(PM.KEY_VALUE,0, INSTR(PM.KEY_VALUE, '-')-1)=LF.LMT_CODE AND "
				+" SUBSTR(LF.KEY, INSTR(LF.KEY,'-')+1)=SR.SERVICE_CODE AND BS.PRD_CODE=PM.PRD_CODE AND br.benefit_code=bs.BENIFITS";
		JSONObject json = null;
		try { 
			responseDTO = new ResponseDTO();
 			resonseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			
			connection = DBConnector.getConnection();
			 tidPstmt = connection.prepareStatement(listQry);
			 tidRS = tidPstmt.executeQuery();
			
			while (tidRS.next()) {
				json = new JSONObject();
				json.put("prdcode", tidRS.getString(1));
				json.put("prdDesc", tidRS.getString(2));
				json.put("serviceDesc", tidRS.getString(3));
				json.put("benefitDesc", tidRS.getString(4));
				json.put("limitDesc", tidRS.getString(5));
				grid.add(json);
			}

			resultJson.put("DASHBOARD_LIST", grid);
			limitDataMap.put("PRODUCT_DASHBOARD", resultJson);
			responseDTO.setData(limitDataMap);
			logger.debug("[ProductManagementDAO][getExistingProductsDetails]responseDTO:::"+ responseDTO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			DBUtils.closeResultSet(tidRS);
			DBUtils.closePreparedStatement(tidPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}
	
	
	public ResponseDTO getProductCreateInformation(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		 String productCode;
		 String bingroupcode;

		requestJSON=requestDTO.getRequestJSON();
		logger.debug("inside [ProductManagementDAO][getProductCreateInformation]");
		
		JSONObject resultJson=new JSONObject();
		JSONArray currencyJSONArray=new JSONArray();
		JSONArray binJSONArray=new JSONArray();
		JSONArray transferCodeJSONArray=new JSONArray();
		JSONArray limitCodeJSONArray= new JSONArray();
		JSONArray bingroupJSONArray= new JSONArray();
		JSONArray binrangeJSONArray=new JSONArray();
	
		PreparedStatement productrChkPstmt=null;
		PreparedStatement bingroupPstmt=null;
		JSONArray tidJSONArray=new JSONArray();
		JSONArray serviceJSONArray=new JSONArray();
		JSONArray tranJSONArray=new JSONArray();
		JSONArray frqJSONArray=new JSONArray();
		JSONArray benifitJSONArray=new JSONArray();
		PreparedStatement transactionPstmt=null;
		PreparedStatement limitidpsmt=null;
		ResultSet transactionRS=null;
		PreparedStatement tidPstmt=null;
		ResultSet tidRS=null;
		ResultSet currencyRS=null;
		PreparedStatement serivePstmt=null;
		ResultSet serviceRS=null;
		PreparedStatement currencyPstmt=null;
		PreparedStatement binPstmt=null;
		PreparedStatement frqPstmt=null;
		PreparedStatement serviceChkPstmt=null;
		PreparedStatement benifiPstmt=null;
		PreparedStatement benefitChkPstmt=null;
		PreparedStatement transferCodePstmt=null;
		PreparedStatement limitCodePstmt=null;
		ResultSet binRS=null;
		ResultSet frqRS=null;
		ResultSet benifitRS=null;
		ResultSet limitCodeRS=null;
		String limitId;
		String seriveId;
		String benefitId;

		String productName="PRODCUT";
		String prodcodeQry="Select count(*) from PRD_MASTER ";
		String productKey=productName.substring(0, 2);
		try {
			productDataMap=new HashMap<String, Object>();
			connection=DBConnector.getConnection();
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][connection:::"+connection+"]");
			int ResCount=0;
			productrChkPstmt=connection.prepareStatement(prodcodeQry);

			   ResultSet prodcodeRS=null;
			   prodcodeRS=productrChkPstmt.executeQuery();
		    	while(prodcodeRS.next()){
				 ResCount=prodcodeRS.getInt(1);
			   }
			      if(ResCount==0){
				    int i=ResCount;
				    i++;
				    productCode=productKey+"000"+i;
				   }else{
				    int i=ResCount;
				    i++;
				    productCode=productKey+"000"+i;
				   }
			      logger.debug("inside [ProductManagementDAO][getProductCreateInformation][Auto Genterated Product Code:::"+productCode+"]");
			      resultJson.put("productCode", productCode);
			   	//add new
			   String bingroupName="BINGROUP";
			   String bingrpQry="Select count(*) from BINGRP ";
			   String bingroupKey=bingroupName.substring(0, 2);
			   int resCount=0;
			   bingroupPstmt=connection.prepareStatement(bingrpQry);
			   ResultSet bingroupRS=null;
			   bingroupRS=bingroupPstmt.executeQuery();
			   
				   while(bingroupRS.next()){
					   resCount=bingroupRS.getInt(1);
					   }
				   if(resCount==0){
					    int i=resCount;
					    i++;
					    bingroupcode=bingroupKey+"000"+i;
					    

					   }else{
					    int i=resCount;
					    i++;
					    bingroupcode=bingroupKey+"000"+i;
					   }
				   logger.debug("inside [ProductManagementDAO][getProductCreateInformation][Auto Genterated Bin Group Code:::"+bingroupcode+"]");
				   resultJson.put("bingroupcode", bingroupcode);

			 	//add new

			String currencyQry="select  CRCY_CODE,CRCY_DESC from CURRENCY_MASTER where CRCY_DESC is not null order by  CRCY_SYMBOL";
			 currencyPstmt=connection.prepareStatement(currencyQry);
			 currencyRS=currencyPstmt.executeQuery();
			JSONObject json=null;
			while(currencyRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, currencyRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, currencyRS.getString(2));
				 currencyJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getLimitcode][getProductCreateInformation][currencyJSONArray:::"+currencyJSONArray+"]");

			/*add bin*/

			String binQry="select BIN||'-'||BIN_DESC,BIN from BIN";
			 binPstmt=connection.prepareStatement(binQry);
			 binRS=binPstmt.executeQuery();
			while(binRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, binRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, binRS.getString(2));
				 binJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][binJSONArray:::"+binJSONArray+"]");

			/*add bin*/
			String limitCodeQry="select distinct LMT_CODE, LMT_DESC from LMTFEE where LMT_FLAG ='F'";
			limitCodePstmt=connection.prepareStatement(limitCodeQry);
			
			limitCodeRS=limitCodePstmt.executeQuery();
			while(limitCodeRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, limitCodeRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, limitCodeRS.getString(2));
				 limitCodeJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][limitCodeJSONArray:::"+limitCodeJSONArray+"]");

			 String transferCodeQry="select distinct LMT_CODE, LMT_DESC from LMTFEE where LMT_FLAG ='L'";
			 transferCodePstmt=connection.prepareStatement(transferCodeQry);
			 ResultSet transferCodeRS=null;
			transferCodeRS=transferCodePstmt.executeQuery();
			while(transferCodeRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, transferCodeRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, transferCodeRS.getString(2));
				 transferCodeJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][transferCodeJSONArray:::"+transferCodeJSONArray+"]");

			//add new for limit

			String terminalQry="SELECT TERMINAL_ID from TERMINAL_MASTER";
			tidPstmt=connection.prepareStatement(terminalQry);
			 tidRS=tidPstmt.executeQuery();
			while(tidRS.next()){
				 json=new JSONObject();
				 json.put("key", tidRS.getString(1));
				 json.put("val", tidRS.getString(1));
				 tidJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][tidJSONArray:::"+tidJSONArray+"]");

			String seriveQry="SELECT SERVICE_NAME,SERVICE_CODE from SERVICE_REGISTRATION";
			 serivePstmt=connection.prepareStatement(seriveQry);
			 serviceRS=serivePstmt.executeQuery();
			while(serviceRS.next()){
				 json=new JSONObject();
				 json.put("key", serviceRS.getString(1));
				 json.put("val", serviceRS.getString(2));
				 serviceJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getDetailsoflimitManagement][serviceJSONArray:::"+serviceJSONArray+"]");
			String transactionQry="SELECT TXN_NAME,TXN_CODE FROM TRANS_MASTER WHERE TXN_TYPE='T' AND DISPLAY_FLAG is Null";
			 transactionPstmt=connection.prepareStatement(transactionQry);
			 transactionRS=transactionPstmt.executeQuery();
			while(transactionRS.next()){
				 json=new JSONObject();
				 json.put("key", transactionRS.getString(1));
				 json.put("val", transactionRS.getString(2));
				 tranJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][tranJSONArray:::"+tranJSONArray+"]");
			String frqQry="select KEY_VALUE,KEY_ID FROM CONFIG_DATA WHERE KEY_GROUP='FREQUENCYDATA' AND KEY_TYPE='FREQUENCYPERIOD'";
			 frqPstmt=connection.prepareStatement(frqQry);
			 frqRS=frqPstmt.executeQuery();
			while(frqRS.next()){
				 json=new JSONObject();
				 json.put("key", frqRS.getString(1));
				 json.put("val", frqRS.getString(2));
				 frqJSONArray.add(json);
			}
			logger.debug(" [ProductManagementDAO][getProductCreateInformation][frqJSONArray:::"+frqJSONArray+"]");
			

			String benifiQry="SELECT BENEFIT_CODE,BENEFIT_NAME FROM BENEFIT_REGISTRATION";
			 benifiPstmt=connection.prepareStatement(benifiQry);
			 benifitRS=benifiPstmt.executeQuery();

				while(benifitRS.next()){
					 json=new JSONObject();
					 json.put("key", benifitRS.getString(2));
					 json.put("val", benifitRS.getString(1));
					 benifitJSONArray.add(json);
				}
				logger.debug(" [ProductManagementDAO][getProductCreateInformation][benifitJSONArray:::"+benifitJSONArray+"]");

			String limitName="LIMIT";
			String limitChkQry="Select count(*) from LMTFEE ";
			String limitKey=limitName.substring(0, 2);

				int ResCount1=0;
				limitidpsmt=connection.prepareStatement(limitChkQry);

				ResultSet limitChkRS=null;
				limitChkRS=limitidpsmt.executeQuery();
				while(limitChkRS.next()){
					ResCount1=limitChkRS.getInt(1);
				}
				logger.debug("inside [ProductManagementDAO][getProductCreateInformation][ResCount:::"+ResCount1+"]");

				if(ResCount==0){
					int i=ResCount1;
					i++;
					limitId=limitKey+"000"+i;
				}else{
					int i=ResCount1;
					i++;
					limitId=limitKey+"000"+i;
					
				}
				logger.debug("inside [ProductManagementDAO][getProductCreateInformation][limitId:::"+limitId+"]");
				//add new Action 02/06/2014
				String serviceName="SERVICE";
				ResultSet ServiceChkRS=null;
				String serviceChkQry="Select count(*) from SERVICE_REGISTRATION ";
				String serviceKey=serviceName.substring(0, 2);

					int RsCount=0;
					serviceChkPstmt=connection.prepareStatement(serviceChkQry);


					ServiceChkRS=serviceChkPstmt.executeQuery();
					while(ServiceChkRS.next()){
						RsCount=ServiceChkRS.getInt(1);
					}
					logger.debug("inside [ProductManagementDAO][getProductCreateInformation][ResCount:::"+RsCount+"]");

					if(RsCount==0){
						int i=RsCount;
						i++;
						seriveId=serviceKey+"000"+i;
					}else{
						int i=RsCount;
						i++;
						seriveId=serviceKey+"000"+i;
					}
					logger.debug("inside [ProductManagementDAO][getProductCreateInformation][merchantId:::"+seriveId+"]");

					//benefit

					String benefitName="BENEFIT";
					String benefitChkQry="Select count(*) from BENEFIT_REGISTRATION ";
					String benefitKey=benefitName.substring(0, 2);

						int RCount=0;
						benefitChkPstmt=connection.prepareStatement(benefitChkQry);

						ResultSet benefitChkRS=null;
						benefitChkRS=benefitChkPstmt.executeQuery();
						while(benefitChkRS.next()){
							RCount=benefitChkRS.getInt(1);
						}
						logger.debug("inside [ProductManagementDAO][getProductCreateInformation][ResCount:::"+ResCount+"]");

						if(RCount==0){
							int i=RCount;
							i++;
							benefitId=benefitKey+"000"+i;
						}else{
							int i=RCount;
							i++;
							benefitId=benefitKey+"000"+i;
						}
						logger.debug("inside [ProductManagementDAO][getProductCreateInformation][benefitId:::"+benefitId+"]");

						String BINCODE = "select lpad(lpad(count(*)+1, 6, '0'),10,'BNFT') from BENIFIT_SETUP";
						String benifitCode = "";
						Statement stmt = connection.createStatement();
						ResultSet rs = stmt.executeQuery(BINCODE);
						while(rs.next()) {
							benifitCode = rs.getString(1);
						}
						logger.debug("inside [ProductManagementDAO][getProductCreateInformation][benifitCode:::"+benifitCode+"]");

				//add new Action 02/06/2014
				resultJson.put("limitId", limitId);
				resultJson.put("seriveId", seriveId);
				resultJson.put("benefitId", benefitId);
				resultJson.put("benifitCode", benifitCode);
				resultJson.put("BIN_DATA", binJSONArray);
				resultJson.put("TID_LIST", tidJSONArray);
				resultJson.put("Service_LIST", serviceJSONArray);
				resultJson.put("TRANSACTION_LIST", tranJSONArray);
				resultJson.put("Frequency_LIST", frqJSONArray);
	            resultJson.put("Currency_LIST", currencyJSONArray);
	            resultJson.put("BENEFIT_LIST", benifitJSONArray);

			//add new for limit

			resultJson.put(CevaCommonConstants.CURRENCY_LIST, currencyJSONArray);
			resultJson.put(CevaCommonConstants.TRANSFER_CODES, transferCodeJSONArray);
			resultJson.put(CevaCommonConstants.MERCHANT_TYPE, limitCodeJSONArray);
			resultJson.put("BIN_GROUP",bingroupJSONArray);
			resultJson.put("BIN_RANGE",binrangeJSONArray);

			logger.debug("inside [ProductManagementDAO][getProductCreateInformation][resultJson:::"+resultJson+"]");

            productDataMap.put("PRODUCT_INFO", resultJson);
			logger.debug("inside [ProductManagementDAO][getProductCreateInformation][productDataMap:::"+productDataMap+"]");
			responseDTO.setData(productDataMap);
			logger.debug("inside [ProductManagementDAO][getProductCreateInformation][responseDTO:::"+responseDTO+"]");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
				DBUtils.closeResultSet(currencyRS);
				DBUtils.closeResultSet(frqRS);
				DBUtils.closeResultSet(transactionRS);
				DBUtils.closeResultSet(serviceRS);
				DBUtils.closeResultSet(tidRS);
				DBUtils.closeResultSet(transactionRS);
				
				DBUtils.closePreparedStatement(productrChkPstmt);
				DBUtils.closePreparedStatement(bingroupPstmt);
				DBUtils.closePreparedStatement(currencyPstmt);
				DBUtils.closePreparedStatement(frqPstmt);
				DBUtils.closePreparedStatement(binPstmt);
				DBUtils.closePreparedStatement(serivePstmt);
				DBUtils.closePreparedStatement(tidPstmt);
				DBUtils.closePreparedStatement(limitidpsmt);
				DBUtils.closePreparedStatement(currencyPstmt);
				DBUtils.closePreparedStatement(transactionPstmt);
				DBUtils.closeConnection(connection);

	}

	return responseDTO;
}

	public ResponseDTO productCreationInsert(RequestDTO requestDTO){
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		responseDTO=new ResponseDTO();


		Connection connection=null;
		logger.debug("inside [ProductManagementDAO][productCreationInsert]");
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ProductManagementDAO][productCreationInsert][requestJSON:::"+requestJSON+"]");

		String serviceName=requestJSON.getString("serviceName");

		logger.debug("[ProductManagementDAO][productCreationInsert][serviceName:::"+serviceName+"]");


		logger.debug("[ProductManagementDAO][productCreationInsert] connection :::"+connection);
		CallableStatement callableStatement = null;
		String productinsertproc = "{call PRODUCTINSERTPROC(?,?,?,?,?,?,?,?,?,?,?)}";


		try {
				connection = connection == null ? DBConnector.getConnection():connection;
			   callableStatement = connection.prepareCall(productinsertproc);
			   callableStatement.setString(1, requestJSON.getString("MAIN_DATA"));
			   logger.debug("[ProductManagementDAO][insertProductDetails] MAIN_DATA:::"+requestJSON.getString("MAIN_DATA"));
			   callableStatement.setString(2, requestJSON.getString(CevaCommonConstants.MAKER_ID));
			   callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);
			   callableStatement.setString(4, requestJSON.getString("limitCode"));
			   callableStatement.setString(5, requestJSON.getString("description"));
			   callableStatement.setString(6, requestJSON.getString("tid"));
			   callableStatement.setString(7, requestJSON.getString("serviceName"));
			   callableStatement.setString(8, requestJSON.getString("multiData"));
			   callableStatement.setString(9, requestJSON.getString("status"));
			   callableStatement.setString(10, requestJSON.getString("Status1"));
			   callableStatement.setString(11, requestJSON.getString("multiData1"));
			   callableStatement.executeUpdate();
			   int resCnt=callableStatement.getInt(3);

			logger.debug("[ProductManagementDAO][productCreationInsert] resultCnt from DB:::"+resCnt);
			if(resCnt==1){
				responseDTO.addMessages("insertProductDetails Information Stored Successfully. ");
			}else if(resCnt==-1){
				responseDTO.addError("insertProductDetails Information Already Exists. ");
			}else{
				responseDTO.addError("insertProductDetails Information Insertion failed.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}	
	
}
