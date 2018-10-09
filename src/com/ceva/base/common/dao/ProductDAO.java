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
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.util.DBUtils;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;


public class ProductDAO {
	Logger logger=Logger.getLogger(ProductDAO.class);
	HashMap<String,Object> merchantDataMap=new HashMap<String,Object>();

	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;
	Connection connection=null;
	PreparedStatement transferCodePstmt=null;
	PreparedStatement merchantTypePstmt=null;
	PreparedStatement locationPstmt=null;
	//private JSONObject json5;



	public ResponseDTO getlocationlist(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		 String productCode;
		 String bingroupcode;

		requestJSON=requestDTO.getRequestJSON();
		logger.debug("inside [ProductDAO][getlocationlist]");
		JSONObject resultJson=new JSONObject();
		JSONArray locationJSONArray=new JSONArray();
		JSONArray binJSONArray=new JSONArray();
		JSONArray transferCodeJSONArray=new JSONArray();
		JSONArray merchantTypeJSONArray= new JSONArray();
		JSONArray bingroupJSONArray= new JSONArray();
		JSONArray binrangeJSONArray=new JSONArray();
		PreparedStatement serviceIdPstmt=null;

		logger.debug(" [ProductDAO][getProductlist][connection:::"+connection+"]");

		 PreparedStatement productrChkPstmt=null;
		 PreparedStatement bingroupPstmt=null;

		 HashMap<String,Object> limitDataMap=new HashMap<String,Object>();
		//	JSONObject resultJson=new JSONObject();
			JSONArray tidJSONArray=new JSONArray();
			JSONArray serviceJSONArray=new JSONArray();
			JSONArray tranJSONArray=new JSONArray();
			JSONArray frqJSONArray=new JSONArray();
			JSONArray currencyJSONArray=new JSONArray();
			JSONArray benifitJSONArray=new JSONArray();
			PreparedStatement transactionPstmt=null;
			PreparedStatement limitidpsmt=null;
			ResultSet transactionRS=null;
			PreparedStatement tidPstmt=null;
			ResultSet tidRS=null;
			ResultSet locationRS=null;
			PreparedStatement serivePstmt=null;
			ResultSet serviceRS=null;
			PreparedStatement locationPstmt=null;
			PreparedStatement binPstmt=null;
			PreparedStatement frqPstmt=null;
			PreparedStatement currencyPstmt=null;
			PreparedStatement serviceChkPstmt=null;
			PreparedStatement benifiPstmt=null;
			PreparedStatement benefitChkPstmt=null;
			ResultSet binRS=null;
			ResultSet frqRS=null;
			ResultSet currencyRS=null;
			ResultSet benifitRS=null;
			String limitId;
			String seriveId;
			String benefitId;

		String productName="PRODCUT";
		String prodcodeQry="Select count(*) from PRD_MASTER ";
		String productKey=productName.substring(0, 2);
		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			int ResCount=0;
			productrChkPstmt=connection.prepareStatement(prodcodeQry);

			   ResultSet prodcodeRS=null;
			   prodcodeRS=productrChkPstmt.executeQuery();
			   JSONObject json1=null;
		    	while(prodcodeRS.next()){
				 ResCount=prodcodeRS.getInt(1);
			   }
			      if(ResCount==0){

				    int i=ResCount;
				    i++;
				    productCode=productKey+"000"+i;
				    logger.debug("inside [ProductDAO][get autogenterat code][productCode:::"+productCode+"]");

				   }else{
				    int i=ResCount;
				    i++;
				    productCode=productKey+"000"+i;
				    logger.debug("inside [ProductDAO][get autogenterat code][merchantId:::"+productCode+"]");
				   }
			      //json1.put("productCode", productCode);
			   //requestDTO.setRequestJSON(requestJSON);
			      resultJson.put("productCode", productCode);
			   logger.debug("[ProductDAO][get autogenterat code][requestJSON==>"+resultJson);
			   	//add new
			   String bingroupName="BINGROUP";
			   String bingrpQry="Select count(*) from BINGRP ";
			   String bingroupKey=bingroupName.substring(0, 2);
			   connection=DBConnector.getConnection();
				int resCount=0;
				bingroupPstmt=connection.prepareStatement(bingrpQry);
				 ResultSet bingroupRS=null;
				 bingroupRS=bingroupPstmt.executeQuery();
				   JSONObject json2=null;

				   while(bingroupRS.next()){
					   resCount=bingroupRS.getInt(1);
					   }
				   if(resCount==0){

					    int i=resCount;
					    i++;
					    bingroupcode=bingroupKey+"000"+i;
					    logger.debug("inside [ProductDAO][get autogenterat code][productCode:::"+bingroupcode+"]");

					   }else{
					    int i=resCount;
					    i++;
					    bingroupcode=bingroupKey+"000"+i;
					    logger.debug("inside [ProductDAO][get autogenterat code][merchantId:::"+bingroupcode+"]");
					   }

				   resultJson.put("bingroupcode", bingroupcode);
				   logger.debug("[ProductDAO][get autogenterat code][requestJSON==>"+resultJson);

			 	//add new

			String entityQry="select  CRCY_CODE,CRCY_DESC from CURRENCY_MASTER where CRCY_DESC is not null order by  CRCY_SYMBOL";
			 locationPstmt=connection.prepareStatement(entityQry);
			 locationRS=locationPstmt.executeQuery();
			JSONObject json=null;
			while(locationRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, locationRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, locationRS.getString(2));
				 locationJSONArray.add(json);
			}
			logger.debug(" [ProductDAO][getLimitcode][merchantTypeJSONArray:::"+locationJSONArray+"]");

			/*add bin*/

			String binQry="select BIN||'-'||BIN_DESC,BIN from BIN";
			 binPstmt=connection.prepareStatement(binQry);
			 binRS=binPstmt.executeQuery();
			//JSONObject json=null;
			while(binRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, binRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, binRS.getString(2));
				 binJSONArray.add(json);
			}
			logger.debug(" [ProductDAO][getLimitcode][binJSONArray:::"+binJSONArray+"]");

			/*add bin*/





			String merchantTypeQry="select distinct LMT_CODE, LMT_DESC from LMTFEE where LMT_FLAG ='F'";
			merchantTypePstmt=connection.prepareStatement(merchantTypeQry);
			ResultSet merchantTypeRS=null;
			merchantTypeRS=merchantTypePstmt.executeQuery();
			while(merchantTypeRS.next()){
				 json=new JSONObject();
				 json.put(CevaCommonConstants.SELECT_KEY, merchantTypeRS.getString(1));
				 json.put(CevaCommonConstants.SELECT_VAL, merchantTypeRS.getString(2));
				 merchantTypeJSONArray.add(json);
			}
			logger.debug(" [ProductDAO][getFeecode][merchantTypeJSONArray:::"+merchantTypeJSONArray+"]");

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
			logger.debug(" [ProductDAO][getProductcode][transferCodeJSONArray:::"+transferCodeJSONArray+"]");

			//add new for limit

			String entityQry1="SELECT TERMINAL_ID from TERMINAL_MASTER";
			tidPstmt=connection.prepareStatement(entityQry1);
			 tidRS=tidPstmt.executeQuery();
			//JSONObject json=null;
			while(tidRS.next()){
				 json=new JSONObject();
				 json.put("key", tidRS.getString(1));
				 json.put("val", tidRS.getString(1));
				 tidJSONArray.add(json);
			}
			logger.debug(" [PrepaidActionDAO][getDetailsoflimitManagement][tidJSONArray:::"+tidJSONArray+"]");

			String seriveQry="SELECT SERVICE_NAME,SERVICE_CODE from SERVICE_REGISTRATION";
			 serivePstmt=connection.prepareStatement(seriveQry);
			 serviceRS=serivePstmt.executeQuery();
			while(serviceRS.next()){
				 json=new JSONObject();
				 json.put("key", serviceRS.getString(1));
				 json.put("val", serviceRS.getString(2));
				 serviceJSONArray.add(json);
			}
			logger.debug(" [PrepaidActionDAO][getDetailsoflimitManagement][serviceJSONArray:::"+serviceJSONArray+"]");
			String transactionQry="SELECT TXN_NAME,TXN_CODE FROM TRANS_MASTER WHERE TXN_TYPE='T' AND DISPLAY_FLAG is Null";
			 transactionPstmt=connection.prepareStatement(transactionQry);
			 transactionRS=transactionPstmt.executeQuery();
			while(transactionRS.next()){
				 json=new JSONObject();
				 json.put("key", transactionRS.getString(1));
				 json.put("val", transactionRS.getString(2));
				 tranJSONArray.add(json);
			}
			logger.debug(" [PrepaidActionDAO][getDetailsoflimitManagement][tranJSONArray:::"+tranJSONArray+"]");
			String frqQry="select KEY_VALUE,KEY_ID FROM CONFIG_DATA WHERE KEY_GROUP='FREQUENCYDATA' AND KEY_TYPE='FREQUENCYPERIOD'";
			 frqPstmt=connection.prepareStatement(frqQry);
			 frqRS=frqPstmt.executeQuery();
			while(frqRS.next()){
				 json=new JSONObject();
				 json.put("key", frqRS.getString(1));
				 json.put("val", frqRS.getString(2));
				 frqJSONArray.add(json);
			}
			logger.debug(" [PrepaidActionDAO][getDetailsoflimitManagement][frqJSONArray:::"+frqJSONArray+"]");
			String currencyQry="SELECT CRCY_CODE FROM CURRENCY_MASTER";
			 currencyPstmt=connection.prepareStatement(currencyQry);
			 currencyRS=currencyPstmt.executeQuery();
			while(currencyRS.next()){
				 json=new JSONObject();
				 json.put("key", currencyRS.getString(1));
				 json.put("val", currencyRS.getString(1));
				 currencyJSONArray.add(json);
			}
			logger.debug(" [PrepaidActionDAO][getDetailsoflimitManagement][currencyJSONArray:::"+currencyJSONArray+"]");

			String benifiQry="SELECT BENEFIT_CODE,BENEFIT_NAME FROM BENEFIT_REGISTRATION";
			 benifiPstmt=connection.prepareStatement(benifiQry);
			 benifitRS=benifiPstmt.executeQuery();

				while(benifitRS.next()){
					 json=new JSONObject();
					 json.put("key", benifitRS.getString(2));
					 json.put("val", benifitRS.getString(1));
					 benifitJSONArray.add(json);
				}
				logger.debug(" [PrepaidActionDAO][getDetailsoflimitManagement][benifitJSONArray:::"+benifitJSONArray+"]");

			String limitName="LIMIT";
			String limitChkQry="Select count(*) from LMTFEE ";
			String limitKey=limitName.substring(0, 2);

				connection=DBConnector.getConnection();
				int ResCount1=0;
				limitidpsmt=connection.prepareStatement(limitChkQry);

				ResultSet limitChkRS=null;
				limitChkRS=limitidpsmt.executeQuery();
				while(limitChkRS.next()){
					ResCount1=limitChkRS.getInt(1);
				}
				logger.debug("inside [PrepaidActionDAO][getservice][ResCount:::"+ResCount1+"]");

				if(ResCount==0){
					int i=ResCount1;
					i++;
					limitId=limitKey+"000"+i;
					logger.debug("inside [PrepaidActionDAO][getservice][limitId:::"+limitId+"]");

				}else{
					int i=ResCount1;
					i++;
					limitId=limitKey+"000"+i;
					logger.debug("inside [PrepaidActionDAO][getservice][limitId:::"+limitId+"]");
				}

				//add new Action 02/06/2014
				String serviceName="SERVICE";
				ResultSet ServiceChkRS=null;
				String serviceChkQry="Select count(*) from SERVICE_REGISTRATION ";
				String serviceKey=serviceName.substring(0, 2);

					connection=DBConnector.getConnection();
					int RsCount=0;
					serviceChkPstmt=connection.prepareStatement(serviceChkQry);


					ServiceChkRS=serviceChkPstmt.executeQuery();
					while(ServiceChkRS.next()){
						RsCount=ServiceChkRS.getInt(1);
					}
					logger.debug("inside [PrepaidActionDAO][getservice][ResCount:::"+RsCount+"]");

					if(RsCount==0){
						int i=RsCount;
						i++;
						seriveId=serviceKey+"000"+i;
						logger.debug("inside [PrepaidActionDAO][getservice][merchantId:::"+seriveId+"]");

					}else{
						int i=RsCount;
						i++;
						seriveId=serviceKey+"000"+i;
						logger.debug("inside [PrepaidActionDAO][getservice][merchantId:::"+seriveId+"]");
					}

					//benefit

					String benefitName="BENEFIT";
					String benefitChkQry="Select count(*) from BENEFIT_REGISTRATION ";
					String benefitKey=benefitName.substring(0, 2);

						connection=DBConnector.getConnection();
						int RCount=0;
						benefitChkPstmt=connection.prepareStatement(benefitChkQry);

						ResultSet benefitChkRS=null;
						benefitChkRS=benefitChkPstmt.executeQuery();
						while(benefitChkRS.next()){
							RCount=benefitChkRS.getInt(1);
						}
						logger.debug("inside [PrepaidActionDAO][getbenefitDetails][ResCount:::"+ResCount+"]");

						if(RCount==0){
							int i=RCount;
							i++;
							benefitId=benefitKey+"000"+i;
							logger.debug("inside [PrepaidActionDAO][getbenefitDetails][benefitId:::"+benefitId+"]");

						}else{
							int i=RCount;
							i++;
							benefitId=benefitKey+"000"+i;
							logger.debug("inside [PrepaidActionDAO][getbenefitDetails][benefitId:::"+benefitId+"]");
						}

						String BINCODE = "select lpad(lpad(count(*)+1, 6, '0'),10,'BNFT') from BENIFIT_SETUP";
						String benifitCode = "";
						Statement stmt = connection.createStatement();
						ResultSet rs = stmt.executeQuery(BINCODE);
						while(rs.next()) {
							benifitCode = rs.getString(1);
						}
						logger.debug("inside [PrepaidActionDAO][getbenefitDetails][benifitCode:::"+benifitCode+"]");

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


			resultJson.put(CevaCommonConstants.LOCATION_LIST, locationJSONArray);
			resultJson.put(CevaCommonConstants.TRANSFER_CODES, transferCodeJSONArray);
			resultJson.put(CevaCommonConstants.MERCHANT_TYPE, merchantTypeJSONArray);
			resultJson.put("BIN_GROUP",bingroupJSONArray);
			resultJson.put("BIN_RANGE",binrangeJSONArray);

			logger.debug("inside [ProductDAO][getMerchantCreatePageInfo][resultJson:::"+resultJson+"]");

            merchantDataMap.put("PRODUCT_INFO", resultJson);
			logger.debug("inside [ProductDAO][getMerchantCreatePageInfo][merchantDataMap:::"+merchantDataMap+"]");
			responseDTO.setData(merchantDataMap);
			logger.debug("inside [ProductDAO][getMerchantCreatePageInfo][responseDTO:::"+responseDTO+"]");

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
				DBUtils.closePreparedStatement(locationPstmt);
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


//insert details

	public ResponseDTO getProductInsert(RequestDTO requestDTO){
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
		responseDTO=new ResponseDTO();


		Connection connection=null;
		logger.debug("inside [ProductDAO][insertProductDetails]");
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ProductDAO][insertProductDetails][requestJSON:::"+requestJSON+"]");

		String serviceName=requestJSON.getString("serviceName");

		logger.debug("[ProductDAO][insertProductDetails][serviceName:::"+serviceName+"]");


		logger.debug("[ProductDAO][insertProductDetails] connection :::"+connection);
		CallableStatement callableStatement = null;
		String productinsertproc = "{call PRODUCTINSERTPROC(?,?,?,?,?,?,?,?,?,?,?)}";


		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			   callableStatement = connection.prepareCall(productinsertproc);
			   callableStatement.setString(1, requestJSON.getString("MAIN_DATA"));
			   logger.debug("[ProductDAO][insertProductDetails] MAIN_DATA:::"+requestJSON.getString("MAIN_DATA"));
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

			logger.debug("[ProductDAO][insertProductDetails] resultCnt from DB:::"+resCnt);
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


//	modify detials

	public ResponseDTO  getProductData(RequestDTO requestDTO){


		  logger.debug("inside [getProductDAO][getProductData]");
		  HashMap<String,Object> productMap=new HashMap<String,Object>();
		  JSONObject resultJson=new JSONObject();
		  JSONArray locationJSONArray=new JSONArray();
		  JSONArray reissueJSONArray=new JSONArray();
		  JSONArray bingroupJSONArray=new JSONArray();
		  JSONArray expiryperiodJSONArray=new JSONArray();
		  JSONArray weltempJSONArray=new JSONArray();
		  JSONArray seriveJSONArray=new JSONArray();
		  responseDTO=new ResponseDTO();
		  logger.debug("inside [getProductDAO][getProductData][connection:::"+connection+"]");

		   ResultSet productRS=null;
		   PreparedStatement productPstmt=null;



		    logger.debug(" [ProductDAO][get templet id][connection:::"+connection+"]");

		    String expiryQry="select EXPIRY_PERIOD from PRD_MASTER where PRD_CODE is not null";
			try {
				connection = connection == null ? DBConnector.getConnection():connection;
			   	   PreparedStatement prodPstmt3 = connection.prepareStatement(expiryQry);
				   ResultSet locationRS3=prodPstmt3.executeQuery();
				   JSONObject json3=null;
				   while(locationRS3.next()){
				   json3=new JSONObject();
				   json3.put(CevaCommonConstants.SELECT_KEY, locationRS3.getString(1));
				   json3.put(CevaCommonConstants.SELECT_VAL, locationRS3.getString(1));
				   expiryperiodJSONArray.add(json3);
					}
					logger.debug(" [ProductDAO][get  embossing code][expiryJSONArray:::"+expiryperiodJSONArray+"]");

			String servQry="select SERVICE_CODE from PRD_MASTER where PRD_CODE is not null";
			  	   PreparedStatement prodPstmt4 = connection.prepareStatement(servQry);
				   ResultSet locationRS4=prodPstmt4.executeQuery();
				   JSONObject json4=null;
				   while(locationRS4.next()){
				   json4=new JSONObject();
				   json4.put(CevaCommonConstants.SELECT_KEY, locationRS4.getString(1));
				   json4.put(CevaCommonConstants.SELECT_VAL, locationRS4.getString(1));
				   seriveJSONArray.add(json4);
								}
								logger.debug(" [ProductDAO][get  embossing code][serviceJSONArray:::"+seriveJSONArray+"]");


		       String entityQry="select ISSUANCE_TEMPLATE_ID from PRD_MASTER where PRD_CODE is not null";
			   	   PreparedStatement prodPstmt = connection.prepareStatement(entityQry);
				   ResultSet locationRS=prodPstmt.executeQuery();
				   JSONObject json=null;
				   while(locationRS.next()){
				   json=new JSONObject();
				   json.put(CevaCommonConstants.SELECT_KEY, locationRS.getString(1));
				   json.put(CevaCommonConstants.SELECT_VAL, locationRS.getString(1));
				   locationJSONArray.add(json);
					}
					logger.debug(" [ProductDAO][get  embossing code][emboss id:::"+locationJSONArray+"]");

			String tempQry="select REISSUANCE_TEMPLATE_ID from PRD_MASTER where PRD_CODE is not null";
		    PreparedStatement prodPstmt1 = connection.prepareStatement(tempQry);
	    	ResultSet locationRS1=prodPstmt1.executeQuery();
					JSONObject json1=null;
					while(locationRS1.next()){
					 json1=new JSONObject();
					 json1.put(CevaCommonConstants.SELECT_KEY, locationRS1.getString(1));
					 json1.put(CevaCommonConstants.SELECT_VAL, locationRS1.getString(1));
					 reissueJSONArray.add(json1);
							}
					logger.debug(" [ProductDAO][get re issue templet][replaceJSONArray:::"+reissueJSONArray+"]");


			String weltempQry="select WELCOMEPACK from PRD_MASTER where PRD_CODE is not null";
			     	   PreparedStatement prodPstmt5 = connection.prepareStatement(weltempQry);
					   ResultSet locationRS5=prodPstmt5.executeQuery();
					   JSONObject json5=null;
					   while(locationRS5.next()){
					   json5=new JSONObject();
					   json5.put(CevaCommonConstants.SELECT_KEY, locationRS5.getString(1));
					   json5.put(CevaCommonConstants.SELECT_VAL, locationRS5.getString(1));
					   weltempJSONArray.add(json5);
						}
						logger.debug(" [ProductDAO][get  embossing code][welcomeJSONArray:::"+weltempJSONArray+"]");




		   String binQry="select BINGRP_CODE,BIN_GRP_DESC||'-' ||BIN||RNG_FRM||'-'||BIN||RNG_TO||'-'||PLASTIC_CODE from BINGRP where BINGRP_CODE is not null";
		   PreparedStatement prodPstmt2 = connection.prepareStatement(binQry);
		     ResultSet locationRS2=prodPstmt2.executeQuery();
					JSONObject json2=null;
					while(locationRS2.next()){
					//logger.debug("locationRS2::"+ locationRS2);
					 json2=new JSONObject();
					 json2.put(CevaCommonConstants.SELECT_KEY, locationRS2.getString(1));
					 json2.put(CevaCommonConstants.SELECT_VAL, locationRS2.getString(2));
					 bingroupJSONArray.add(json2);
							}
					logger.debug(" [ProductDAO][get bin group][bingroupJSONArray:::"+bingroupJSONArray+"]");



		   String merchantQry="select PRD_CODE,PRD_DESC,PRD_GROUP,PRD_CRCY,EXPIRY_PERIOD,SERVICE_CODE,ISSUANCE_TEMPLATE_ID,REISSUANCE_TEMPLATE_ID,WELCOMEPACK,OVER_DRAFT_LMT,BINGRP_CODE from PRD_MASTER WHERE PRD_CODE=?";
		   String productCode =  requestDTO.getRequestJSON().getString("PRD_CODE").trim();
		   //logger.debug("["+requestDTO.getRequestJSON().getString("PRD_CODE")+"]");
		   productPstmt=connection.prepareStatement(merchantQry);
		   productPstmt.setString(1,productCode);
		   productRS=productPstmt.executeQuery();
		   logger.debug("After executing query");
		   if(productRS.next()){
		    logger.debug("Response Data from DB");
		    resultJson.put("productCode", productRS.getString(1));
		    resultJson.put("productDescription", productRS.getString(2));
		    resultJson.put("productGroup", productRS.getString(3));
		    resultJson.put("productCurrency", productRS.getString(4));
		    resultJson.put("expiryPeriod", productRS.getString(5));
		    resultJson.put("serviceCode", productRS.getString(6));
		     resultJson.put("embossingTempletId", productRS.getString(7));
		    resultJson.put("replacementTemplate", productRS.getString(8));
		    resultJson.put("welcomePackTemplate", productRS.getString(9));
		    resultJson.put("overDraftAmount", productRS.getString(10));
		    resultJson.put("binGroup1", productRS.getString(11));

		    logger.debug("After Setting data");
		   }
		   logger.debug("Response Json Object ["+resultJson+"]");

		   resultJson.put("EXPRIY_PRD",expiryperiodJSONArray);
		   resultJson.put("SERVICE_CODE",seriveJSONArray);
		   resultJson.put("EMBOS_ID",locationJSONArray);
		   resultJson.put("REP_TEMP",reissueJSONArray);
		   resultJson.put("WEL_TEMP",weltempJSONArray);
		   resultJson.put("BIN_GROUP",bingroupJSONArray);
		   productMap.put("PRODUCT_DATA",resultJson);



		   logger.debug("inside [getProductDAO][getProductData][bingroupMap:::"+productMap+"]");
		   responseDTO.setData(productMap);

		   logger.debug("inside [getProductDAO][getProductData][responseDTO:::"+responseDTO+"]");

		  } catch (SQLException e) {
		   e.printStackTrace();
		   logger.debug("inside [getProductDAO][getProductData][responseDTO:::"+responseDTO+"]");
		  }finally{
			  
			  DBUtils.closeResultSet(productRS);
			  DBUtils.closePreparedStatement(productPstmt);
			  DBUtils.closePreparedStatement(locationPstmt);
			  DBUtils.closePreparedStatement(merchantTypePstmt);
			  DBUtils.closePreparedStatement(transferCodePstmt);
			  
			  DBUtils.closeConnection(connection);
		  }
		  return responseDTO;
		 }


public ResponseDTO updateProduct(RequestDTO requestDTO){
		responseDTO=new  ResponseDTO();
		requestJSON=new JSONObject();
		Connection connection=null;
		logger.debug("inside [ProductDAO][updateProduct]");
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ProductDAO][updateProduct][requestJSON:::"+requestJSON+"]");


		String multidate=requestJSON.getString("MAIN_DATA");
		logger.debug("[ProductDAO][updateProduct][multidate:::"+multidate+"]");
		logger.debug("[ProductDAO][updateProduct] connection :::"+connection);
		CallableStatement callableStatement = null;
			String productupdateproc = "{call PRODUCTUPDATEPROC(?,?,?)}";

		try {
			connection = connection == null ? DBConnector.getConnection():connection;
			callableStatement = connection.prepareCall(productupdateproc);
			callableStatement.setString(1, requestJSON.getString("MAIN_DATA"));
			callableStatement.setString(2, requestJSON.getString(CevaCommonConstants.MAKER_ID));
			callableStatement.registerOutParameter(3, java.sql.Types.INTEGER);

			callableStatement.executeUpdate();
			int resCnt=callableStatement.getInt(3);

			logger.debug("[ProductDAO][updateProduct] resultCnt from DB:::"+resCnt);
			//responseDTO=getMerchantDetails(requestDTO);
			if(resCnt==1){
				responseDTO.addMessages("Product Information Updated Successfully. ");
			}else if(resCnt==-1){
				/*resultMap.put(CevaCommonConstants.RESPONSE_JSON, requestJSON);
				responseDTO.setData(resultMap);*/
				responseDTO.addError("Product Information Already Exists. ");
			}else{
				responseDTO.addError("Product Inofrmation Updation failed.");
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




public ResponseDTO getViewProduct(RequestDTO requestDTO){


 	     logger.debug("inside [getProductDAO][getProductView]");
 		  HashMap<String,Object> productMap=new HashMap<String,Object>();
 		  JSONObject resultJson=new JSONObject();
 		  responseDTO=new ResponseDTO();
 		  logger.debug("inside [getProductDAO][getProductView][connection:::"+connection+"]");
 		 Connection connection=null;

 		   PreparedStatement productPstmt=null;
 		  ResultSet productRS=null;

 		 try
 	 	{
 			connection = connection == null ? DBConnector.getConnection():connection;
 			
 	  String productQry="SELECT  P.PRD_CODE,P.PRD_DESC,P.PRD_CRCY,P.PRD_GROUP,P.EXPIRY_PERIOD,P.SERVICE_CODE,P.ISSUANCE_TEMPLATE_ID,"
 	  		+ "P.REISSUANCE_TEMPLATE_ID,P.WELCOMEPACK,P.OVER_DRAFT_LMT,B.BINGRP_CODE,B.BIN_GRP_DESC,B.BIN||RNG_FRM,B.BIN||RNG_TO,"
 	  		+ "B.PLASTIC_CODE FROM PRD_MASTER P,BINGRP B WHERE P.BINGRP_CODE=B.BINGRP_CODE AND  P.PRD_CODE=?";
 	  String productCode=  requestDTO.getRequestJSON().getString("PRD_CODE").trim();
 	 productPstmt=connection.prepareStatement(productQry);
 	 productPstmt.setString(1,productCode);

 	 productRS=productPstmt.executeQuery();
 	 logger.debug("After executing query");
 	  while(productRS.next()){
 		      logger.debug("Response Data from DB");
 			    resultJson.put("productCode", productRS.getString(1));
 			    resultJson.put("productDescription", productRS.getString(2));
 			    resultJson.put("productGroup", productRS.getString(3));
 			    resultJson.put("productCurrency", productRS.getString(4));
 			    resultJson.put("expiryPeriod", productRS.getString(5));
 			    resultJson.put("serviceCode", productRS.getString(6));
 			     resultJson.put("embossingTempletId", productRS.getString(7));
 			    resultJson.put("replacementTemplate", productRS.getString(8));
 			    resultJson.put("welcomePackTemplate", productRS.getString(9));
 			    resultJson.put("overDraftAmount", productRS.getString(10));
 			    resultJson.put("binGroup1", productRS.getString(11));
 			   resultJson.put("bingrpdesc", productRS.getString(12));
 			    resultJson.put("binRangeFrom", productRS.getString(13));
 			    resultJson.put("binRangeTo", productRS.getString(14));
 			    resultJson.put("plastiCode", productRS.getString(15));

 			    logger.debug("After Setting data");
 			   }
 			   logger.debug("Response Json Object ["+resultJson+"]");


 			   productMap.put("PRODUCT_VIEW",resultJson);



 			   logger.debug("inside [getProductDAO][productview][productview:::"+productMap+"]");
 			   responseDTO.setData(productMap);

 			   logger.debug("inside [getProductDAO][productview][responseDTO:::"+responseDTO+"]");

 			  } catch (SQLException e) {
 			   e.printStackTrace();
 			   logger.debug("inside [getProductDAO][productview][responseDTO:::"+responseDTO+"]");
 			  }finally{
 				  
 				  DBUtils.closeResultSet(productRS);
 				  DBUtils.closePreparedStatement(productPstmt);
 				  DBUtils.closePreparedStatement(locationPstmt);
 				  DBUtils.closePreparedStatement(merchantTypePstmt);
 				  DBUtils.closePreparedStatement(transferCodePstmt);
 				  DBUtils.closeConnection(connection);
 			}

 			  return responseDTO;
 			 }


}



