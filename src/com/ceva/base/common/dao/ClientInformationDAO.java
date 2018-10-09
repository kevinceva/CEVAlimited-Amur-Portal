package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

public class ClientInformationDAO
{
  private Logger logger = Logger.getLogger(ClientInformationDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  
  public ResponseDTO fetchClientDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [ClientInformationDAO][GetMerchantDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "SELECT CC.CLIENT_ID,replace((CC.FNAME||' '||CC.MNAME||' '||CC.LNAME),'''',' '),decode(CC.GENDER,'M','Male','F','Female',CC.GENDER),to_char(CC.DATE_OF_BIRTH,'DD-MM-YYYY'), "
    		+ " CC.ID_TYPE,CC.ID_NUMBER,CC.MOBILE_NUMBER,CC.POSTAL_ADDRESS,CC.POSTAL_CODE,CC.EMAIL,to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY'), "
    		+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
    		+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
    		+ " 'BV','Client Verified and Beneficiary Verified', "
    		+ " 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Verification In Progress'),"
    		+ " 'CS','Active','Client or Beneficiary Verification In Progress') "
    		+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB WHERE CC.CLIENT_ID=CB.CLIENT_ID(+) ORDER BY CC.DATE_CREATED DESC";
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      
      userPstmt = connection.prepareStatement(merchantQry);
      this.logger.debug("Client Qry is [" + merchantQry + "]");
      userRS = userPstmt.executeQuery();
      
      json = new JSONObject();
      while (userRS.next())
      {
        json.put("CLIENT_ID", userRS.getString(1));
        json.put("CLIENT_NAME",  userRS.getString(2));
        json.put("GENDER", userRS.getString(3));
        json.put("DOB", userRS.getString(4));
        json.put("ID_TYPE", userRS.getString(5));
        json.put("ID_NUMBER", userRS.getString(6));
        json.put("MOB", userRS.getString(7));
        json.put("ADDR", userRS.getString(8));
        json.put("PCODE", userRS.getString(9));
        json.put("EMAIL", userRS.getString(10));
        json.put("MKRDT", userRS.getString(11));
        json.put("STATUS", userRS.getString(12));
        useerJSONArray.add(json);
        json.clear();
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("GROUP_LIST", useerJSONArray);
      merchantMap.put("GROUP_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in GetMerchantDetails [" + 
        e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      useerJSONArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO fetchPolicyList(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [ClientInformationDAO][getStoreDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "select POLICY_ID,round(nvl(CUMMULATIVE_PREMIUM,0),2),round(nvl(CUMMULATIVE_SUM_ASSURED,0),2),to_char(DATE_STARTED,'HH:MI AM Month DD, YYYY'),to_char(DATE_ENDING,'HH:MI AM Month DD, YYYY'),decode(STATUS,'PS','SUCCESS','PV','Policy Verified','Policy Created') "
    		+ " from CIC_POLICY where client_id=? ";
    try
    {
      this.requestJSON = requestDTO.getRequestJSON();
      
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      
      System.out.println("value of cid:"+this.requestJSON.getString("CLIENT_ID")+"-gid:"+this.requestJSON.getString("GROUP_ID"));
      userPstmt = connection.prepareStatement(merchantQry);
      
      userPstmt.setString(1, this.requestJSON.getString("CLIENT_ID"));
      
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        json = new JSONObject();
        json.put("POLICY_ID",  userRS.getString(1));
        json.put("PRIM_AMT",  userRS.getString(2));
        json.put("SUM_ASUR",  userRS.getString(3));
        json.put("SDT",  userRS.getString(4));
        json.put("EDT",  userRS.getString(5));
        json.put("STATUS",  userRS.getString(6));
        useerJSONArray.add(json);
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("USER_LIST", useerJSONArray);
      merchantMap.put("USER_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in getStoreDetails [" + 
        e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      useerJSONArray = null;
    }
    return this.responseDTO;
  }

  public ResponseDTO viewClientInfo(RequestDTO requestDTO) {

		logger.debug("Inside  viewClientInfo... ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		String merchantQry = "";
		String groupId = "";
		String type = "";
		CallableStatement callableStmt = null;
	    PreparedStatement userPstmt = null;
		ResultSet merchantRS = null;
		ResultSet userRS = null;
		Connection connection = null;

		try {
			merchantQry = "SELECT CC.CLIENT_ID,replace((CC.FNAME||' '||CC.MNAME||' '||CC.LNAME),'''',' '),decode(CC.GENDER,'M','Male','F','Female',CC.GENDER),to_char(CC.DATE_OF_BIRTH,'DD-MM-YYYY'), "
		    		+ " CC.ID_TYPE,CC.ID_NUMBER,CC.MOBILE_NUMBER,CC.POSTAL_ADDRESS,CC.POSTAL_CODE,CC.EMAIL,to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY'), "
		    		+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
		    		+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
		    		+ " 'BV','Client Verified and Beneficiary Verified', "
		    		+ " 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Creation In Progress'),"
		    		+ " 'CS','Active', 'Client or Beneficiary Verification In Progress') "
		    		+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB WHERE CC.CLIENT_ID=? and CC.CLIENT_ID=CB.CLIENT_ID(+) ORDER BY CC.DATE_CREATED DESC";
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			logger.debug("Request JSON [" + requestDTO.getRequestJSON() + "]");

			groupId = requestDTO.getRequestJSON().getString("CLIENT_ID");
			System.out.println("value of group id:"+groupId);
			type = requestDTO.getRequestJSON().getString("TYPE");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			userPstmt = connection.prepareStatement(merchantQry);
			userPstmt.setString(1, groupId);
		      userRS = userPstmt.executeQuery();
		      
		      while (userRS.next())
		      {
		    	resultJson.put("CLIENT_ID", userRS.getString(1));
		    	resultJson.put("CLIENT_NAME",  userRS.getString(2));
		    	resultJson.put("GENDER", userRS.getString(3));
		    	resultJson.put("DOB", userRS.getString(4));
		    	resultJson.put("ID_TYPE", userRS.getString(5));
		    	resultJson.put("ID_NUMBER", userRS.getString(6));
		    	resultJson.put("MOB", userRS.getString(7));
		    	resultJson.put("ADDR", userRS.getString(8));
		    	resultJson.put("PCODE", userRS.getString(9));
		    	resultJson.put("EMAIL", userRS.getString(10));
		    	resultJson.put("MKRDT", userRS.getString(11));
		    	resultJson.put("STATUS", userRS.getString(12));
		      }
		      
		    

			merchantMap.put("client_info", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("inside Response DTO [" + responseDTO + "]");
			

		} catch (SQLException e) {
			logger.debug("SQLException in ViewClientInfo [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Exception in ViewClientInfo  [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeCallableStatement(callableStmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			merchantQry = null;
			groupId = null;

		}

		return responseDTO;
	}  
  
  public ResponseDTO fetchPurchaseList(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [ClientInformationDAO][fetchPurchaseList].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "";
    try
    {
      this.requestJSON = requestDTO.getRequestJSON();
      
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      
      this.logger.debug("connection is [" + connection + "]");
      
      String userId=requestJSON.getString("USER_ID");
      String rights="";
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      
      
      merchantQry = "select PURCHASE_ID,round(AIRTIME_PREMIUM_AMOUNT,2),round(SUM_ASSURED,2),to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),STATUS from CIC_POLICY_PURCHASE where UPPER(POLICY_ID)=? order by DATE_CREATED desc";
      userPstmt = connection.prepareStatement(merchantQry);
      userPstmt.setString(1, userId);
      
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        json = new JSONObject();
        json.put("PURCHASE_ID",  userRS.getString(1));
        json.put("PRIM_AMT",  userRS.getString(2));
        json.put("SUM_ASUR",  userRS.getString(3));
        json.put("MKRDT",  userRS.getString(4));
        json.put("STATUS",  userRS.getString(5));
        useerJSONArray.add(json);
      }
      
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("PURG_STMT", useerJSONArray);
      merchantMap.put("PURG_STMT", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in fetchPurchaseList [" + 
        e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      useerJSONArray = null;
    }
    return this.responseDTO;
  }
  
	public ResponseDTO clientInformation(RequestDTO requestDTO) {
		logger.debug("Inside clientInformation... ");

		Connection connection = null;
		CallableStatement callableStmt = null;
		PreparedStatement userPstmt = null;
		PreparedStatement benPstmt = null;
		ResultSet merchantRS = null;
		ResultSet benRS = null;

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		String groupId = "";
		String type = "";
		
		String merchantQry = "";
		String benQry = "";

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");
			
			groupId = requestJSON.getString("GROUP_ID");
			type = requestJSON.getString("TYPE"); // ActiveDeactive
			logger.debug("GroupId[" + groupId + "] type[" + type + "]");
			
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			
			merchantQry = "SELECT CC.CLIENT_ID,replace((CC.FNAME||' '||CC.MNAME||' '||CC.LNAME),'''',' '),decode(CC.GENDER,'M','Male','F','Female',CC.GENDER),to_char(CC.DATE_OF_BIRTH,'DD-MM-YYYY'), "
		    		+ " CC.ID_TYPE,CC.ID_NUMBER,CC.MOBILE_NUMBER,CC.POSTAL_ADDRESS,CC.POSTAL_CODE,CC.EMAIL,to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY'), "
		    		+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
		    		+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
		    		+ " 'BV','Client Verified and Beneficiary Verified', "
		    		+ " 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Creation In Progress'), "
		    		+ " 'CS','Active','Client or Beneficiary Verification In Progress') "
		    		+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB WHERE CC.CLIENT_ID=? and CC.CLIENT_ID=CB.CLIENT_ID(+) ORDER BY CC.DATE_CREATED DESC";
			userPstmt = connection.prepareStatement(merchantQry);
			userPstmt.setString(1, groupId);
			logger.debug("merchantQry [" + merchantQry + "]");
			System.out.println("merchantQry [" + merchantQry + "]");
			merchantRS = userPstmt.executeQuery();

			if (merchantRS.next()) {			
		    	resultJson.put("CLIENT_ID", merchantRS.getString(1));
		    	resultJson.put("CLIENT_NAME",  merchantRS.getString(2));
		    	resultJson.put("GENDER", merchantRS.getString(3));
		    	resultJson.put("DOB", merchantRS.getString(4));
		    	resultJson.put("ID_TYPE", merchantRS.getString(5));
		    	resultJson.put("ID_NUMBER", merchantRS.getString(6));
		    	resultJson.put("MOB", merchantRS.getString(7));
		    	resultJson.put("ADDR", merchantRS.getString(8));
		    	resultJson.put("PCODE", merchantRS.getString(9));
		    	resultJson.put("EMAIL", merchantRS.getString(10));
		    	resultJson.put("MKRDT", merchantRS.getString(11));
		    	resultJson.put("STATUS", merchantRS.getString(12));
			}
			System.out.println("First Json>>"+resultJson);
			benQry = "SELECT BENEFICIARY_ID,replace(FNAME||' '||MNAME||' '||LNAME,'''',' '),decode(GENDER,'M','Male','F','Female',GENDER),to_char(DATE_OF_BIRTH,'DD-MM-YYYY'),ID_NUMBER,MOBILENUMBER,VALIDATION_CNT,PHOTO,SIGNATURE,to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),STATUS from CIC_CLIENT_BENEFICIARIES where CLIENT_ID=? ORDER BY DATE_CREATED";
			benPstmt = connection.prepareStatement(benQry);
			benPstmt.setString(1, groupId);
			logger.debug("benQry [" + benQry + "]");
			benRS = benPstmt.executeQuery();

			if (benRS.next()) {			
		    	resultJson.put("BEN_ID", benRS.getString(1));
		    	resultJson.put("BEN_NAME",  benRS.getString(2));
		    	resultJson.put("BGENDER", benRS.getString(3));
		    	resultJson.put("BDOB", benRS.getString(4));
		    	resultJson.put("BID_NUMBER", benRS.getString(5));
		    	resultJson.put("BMOB", benRS.getString(6));
		    	resultJson.put("VAL_CNT", benRS.getString(7));
		    	resultJson.put("PHOTO", benRS.getString(8));
		    	resultJson.put("SIGN", benRS.getString(9));
		    	resultJson.put("BMKRDT", benRS.getString(10));
		    	resultJson.put("BSTATUS", benRS.getString(11));
		    	System.out.println("second Json1>>"+resultJson);
			}			
			System.out.println("second Json1>>"+resultJson);
			logger.debug("Inside  before if type [" + type + "]");

/*			if (type.equalsIgnoreCase("Modify")) {
				resultJson1 = (JSONObject) getAdminCreateInfo(requestDTO)
						.getData().get(CevaCommonConstants.LOCATION_INFO);
				logger.debug("ResultJson1 [" + resultJson1 + "]");
				resultJson.put(CevaCommonConstants.LOCATION_LIST,
						resultJson1.get(CevaCommonConstants.LOCATION_LIST));
			}*/

			logger.debug("ResultJson>> [" + resultJson + "]");

			merchantMap.put("user_rights", resultJson);

			logger.debug("EentityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in UserInformation [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in UserInformation  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeCallableStatement(callableStmt);
			DBUtils.closeConnection(connection);
			
			groupId = null;
			type = null;
			
		}

		return responseDTO;
	} 
	
	public ResponseDTO clientModifyAck(RequestDTO requestDTO) {
		logger.debug("Inside clientModifyAck... ");

		Connection connection = null;
		CallableStatement callableStatement = null;
		PreparedStatement updatePstmt = null;
		PreparedStatement benUpPstmt = null;


		String cid = "";
		String bid = "";
		
		String errorMessage = "";

		String updateQry = "";
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			 cid= requestJSON.getString("cid");
			 bid= requestJSON.getString("bid");
			 logger.debug("cid [" + cid + "]bid[" + bid + "] ");

			connection = connection == null ? DBConnector.getConnection():connection;
	/*		callableStatement = connection.prepareCall(updateUserDet);
			callableStatement.setString(1, userString);
			callableStatement.setString(2, userMakerId);
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);*/
			
			updateQry = "update CIC_CLIENTS set ID_NUMBER=?, POSTAL_ADDRESS=?, GENDER=decode(upper(?),'MALE','M','FEMALE','F',?), DATE_OF_BIRTH=to_date(?,'dd-mm-yyyy'), POSTAL_CODE=?, EMAIL=?  "
					+ " where CLIENT_ID=?  ";
			updatePstmt = connection.prepareStatement(updateQry);
			updatePstmt.setString(1, requestJSON.getString("idno"));
			updatePstmt.setString(2, requestJSON.getString("addr"));
			updatePstmt.setString(3, requestJSON.getString("gender"));
			updatePstmt.setString(4, requestJSON.getString("gender"));
			updatePstmt.setString(5, requestJSON.getString("dob"));
			updatePstmt.setString(6, requestJSON.getString("pcode"));
			updatePstmt.setString(7, requestJSON.getString("email"));
			updatePstmt.setString(8, cid);
			int updateCnt = updatePstmt.executeUpdate();
			
			if (updateCnt==0)
			{	
				errorMessage="Client Modify Details Failed";
				responseDTO.addError(errorMessage);
			}	
			else
			{
				
				if (!bid.isEmpty())
				{	
					updateQry = "update CIC_CLIENT_BENEFICIARIES set ID_NUMBER=?, GENDER=decode(upper(?),'MALE','M','FEMALE','F',?), DATE_OF_BIRTH=to_date(?,'dd-mm-yyyy')  "
							+ " where BENEFICIARY_ID=?  ";
					benUpPstmt = connection.prepareStatement(updateQry);
					benUpPstmt.setString(1, requestJSON.getString("bidno"));
					benUpPstmt.setString(2, requestJSON.getString("bgender"));
					benUpPstmt.setString(3, requestJSON.getString("bgender"));
					benUpPstmt.setString(4, requestJSON.getString("bdob"));
					benUpPstmt.setString(5, requestJSON.getString("bid"));
					int benUpCnt = benUpPstmt.executeUpdate();
					if (benUpCnt==0)
					{	
						errorMessage="Beneficiary Modify Details Failed";
						responseDTO.addError(errorMessage);
					}else
					{
						responseDTO.addMessages("Client Modify Details Acknowledged. ");
					}
				}else
				{
					responseDTO.addMessages("Client Modify Details Acknowledged. ");
				}
				
			}

		} catch (SQLException e) {
			logger.debug("SQLException in clientModifyAck [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in clientModifyAck  [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			cid = null;
			errorMessage = null;
		}
		return responseDTO;
	}	
}
