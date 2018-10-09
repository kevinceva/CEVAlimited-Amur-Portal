package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.base.common.utils.EncryptTransactionPin;
import com.ceva.util.DBUtils;

public class NewBillPaymentDAO {
	
	
	Logger logger = Logger.getLogger(NewBillPaymentDAO.class);
	
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	
	
	public ResponseDTO fetchBillerDataTableDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside [NewBillPaymentDAO][fetchBillerDataTableDetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONObject terminalJSON = null;

		JSONArray merchantJsonArray = null;

		//ArrayList<String> merchatArray = null;
		ArrayList<String> storeArray = null;

		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "Select BILLER_ID,NAME,ABBREVATION,COMM_TYPE,AMOUNT/100,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from BILLER_REGISTRATION order by BILLER_ID";
		try {

			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			terminalJSON = new JSONObject();

			//merchatArray = new ArrayList<String>();
			storeArray = new ArrayList<String>();

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			json = new JSONObject();
			while (merchantRS.next()) {
				json.put("BILLER_ID",merchantRS.getString(1));
				json.put("NAME",merchantRS.getString(2));
				json.put("ABBREVATION",merchantRS.getString(3));
				json.put("COMM_TYPE",merchantRS.getString(4));
				json.put("AMOUNT",merchantRS.getString(5));
				json.put("MAKER_ID", merchantRS.getString(6));
				json.put("MAKER_DATE",merchantRS.getString(7));
				//merchatArray.add(merchantRS.getString(1));
				merchantJsonArray.add(json);
				json.clear();
			}
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			resultJson.put("BILLER_LIST", merchantJsonArray);

			merchantJsonArray.clear();

			/*for (int i = 0; i < merchatArray.size(); i++) {
				String storeQry = "Select BILLER_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
						+"from BILLER_ACCOUNT_MASTER where BILLER_ID=?  order by ACCOUNT_NUMBER";

				merchantPstmt = connection.prepareStatement(storeQry);
				merchantPstmt.setString(1, merchatArray.get(i));
				merchantRS = merchantPstmt.executeQuery();
				json.clear();

				while (merchantRS.next()) {
					json.put("BILLER_ID",
							merchantRS.getString(1));
					json.put("ACCOUNT_NUMBER",
							merchantRS.getString(2));
					json.put("ACCOUNT_NAME",
							merchantRS.getString(3));
					json.put("MAKER_ID",
							merchantRS.getString(4));
					json.put("MAKER_DATE",
							merchantRS.getString(5));
					storeArray.add(merchantRS.getString(1));
					merchantJsonArray.add(json);
					json.clear();
				}

				if (merchatArray != null && merchatArray.size() > 0) {
					resultJson.put(merchatArray.get(i) + "_ACCOUNTS",
							merchantJsonArray);
					merchantJsonArray.clear();
				}
				DBUtils.closePreparedStatement(merchantPstmt);
				DBUtils.closeResultSet(merchantRS);
			}*/

			json.clear();
			
			merchantMap.put("BILLER_LIST", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);

		} catch (Exception e) {
			logger.debug("Got Exception in fetchBillerDataTableDetails ["+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {

			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			merchantJsonArray = null;

			terminalJSON = null;

			//merchatArray = null;
			storeArray = null;
		}
		return responseDTO;
	}

	
	
	
	public ResponseDTO registerBiller(RequestDTO requestDTO) {

		logger.debug("Inside [NewBillPaymentDAO][registerBiller] ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		//JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			//billerBean = requestJSON.getJSONObject("billerBean");

			billerDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callable = connection.prepareCall("{call PAYBILLPKG.registerBillerProc(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			callable.setString(1, requestJSON.getString("abbreviation"));
			callable.setString(2, requestJSON.getString("billerCode"));
			callable.setString(3, requestJSON.getString("name"));
			callable.setString(4, requestJSON.getString("agencyType"));
			callable.setString(5, requestJSON.getString("address"));
			callable.setString(6, requestJSON.getString("telephone"));
			callable.setString(7, requestJSON.getString("contactPerson"));
			callable.setString(8, requestJSON.getString("email"));
			callable.setString(9, requestJSON.getString("commissionType"));
			callable.setString(10, requestJSON.getString("amount"));
			callable.setString(11, requestJSON.getString("rate"));
			callable.setString(12, requestJSON.getString("makerId"));
			callable.registerOutParameter(13, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(13).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in register Biller [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in Register Biller [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	
	
	public ResponseDTO registerBillerAccountDetails(RequestDTO requestDTO) {

		logger.debug("Inside [NewBillPaymentDAO][registerBillerAccountDetails] ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			billerDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection  + "]");
			callable = connection.prepareCall("{call PAYBILLPKG.registerBillerAccountProc(?,?,?)}");

			callable.setString(1, requestJSON.getString("BILLER_ACCT_DATA"));
			callable.setString(2, requestJSON.getString("makerId"));
			callable.registerOutParameter(3, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(3).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in register Biller [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in Register Biller [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	
	
	public ResponseDTO fetchBillerDetails(RequestDTO requestDTO){
		
		logger.debug("[NewBillPaymentDAO][fetchBillerDetails]..");
		
		HashMap<String, Object> billerMap = null;

		Connection connection = null;
		PreparedStatement billerPstmt = null;
		ResultSet billerRS = null;

		String billerQry = "";
		
		try{
			
			responseDTO = new ResponseDTO();
			responseJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();
			JSONArray jsonArray = null;
			JSONObject jsonObject = null;
			logger.debug("[NewBillPaymentDAO][fetchBillerDetails][requestJSON..."+requestJSON+"]");
			
			billerMap = new HashMap<String, Object>();

			/*billerQry = "select BR.BILLER_ID,BR.NAME,BAM.ACCOUNT_NUMBER,BAM.ACCOUNT_NAME"
					+ " from BILLER_REGISTRATION BR,BILLER_ACCOUNT_MASTER BAM "
					+ "where BR.BILLER_ID = BAM.BILLER_ID and BR.BILLER_ID =trim(?) and BAM.ACCOUNT_NUMBER =trim(?)";*/
			
			billerQry = "select BR.BILLER_ID,BR.NAME "
					+ " from BILLER_REGISTRATION BR order by BR.BILLER_ID";

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			billerPstmt = connection.prepareStatement(billerQry);
			
			billerRS = billerPstmt.executeQuery();

			jsonArray = new JSONArray();
			
			while (billerRS.next()) {
				jsonObject = new JSONObject();
				jsonObject.put("val", billerRS.getString(1));
				jsonObject.put("key", billerRS.getString(2));
				jsonArray.add(jsonObject);
			}
			responseJSON.put("BILLER_DATA", jsonArray);
			
			jsonArray.clear();
			jsonArray = null;
			billerRS.close();
			billerPstmt.close();
			
			billerMap.put("BILLER_INFO", responseJSON);

			logger.debug("BillerMap [" + billerMap + "]");
			responseDTO.setData(billerMap);
			logger.debug("Response DTO [" + responseDTO + "]");			
			
		}catch(SQLException sqlException){
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in BillPaymentCreateDetails["+ sqlException.getMessage() + "]");
		}catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in BillPaymentCreateDetails ["+ e.getMessage() + "]");
		}finally{
				DBUtils.closeResultSet(billerRS);
				DBUtils.closePreparedStatement(billerPstmt);
				DBUtils.closeConnection(connection);
				responseJSON = null;
				billerQry = null;
				billerMap = null;
		}
		return responseDTO;
	}
	
	
	public ResponseDTO billPayVerifyPin(RequestDTO requestDTO) {

		logger.debug("Inside [NewBillPaymentDAO][fetchBillerDetails]... ");

		Connection connection = null;
		PreparedStatement billerPstmt = null;
		ResultSet billerRS = null;

		int resCount = 0;
		String cryptedPassword = "";
		String key1 = "";

		String billerQry = "";
		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			key1 = "97206B46CE46376894703ECE161F31F2";
			logger.debug("Before Enryption Value is  ["
					+ requestJSON.getString(CevaCommonConstants.PIN) + "]");

			try {
				cryptedPassword = EncryptTransactionPin.encrypt(key1,
						requestJSON.getString(CevaCommonConstants.PIN), 'F');

			} catch (Exception e) {
				logger.debug("Exception in encrypting password cryptedPassword ["
						+ cryptedPassword + "] message[" + e.getMessage() + "]");
				cryptedPassword = "";
			}

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			billerQry = "Select COUNT(*) from USER_LOGIN_CREDENTIALS  where PIN=? and LOGIN_USER_ID=trim(?)";
			billerPstmt = connection.prepareStatement(billerQry);
			billerPstmt.setString(1, cryptedPassword);
			billerPstmt.setString(2, requestJSON.getString(CevaCommonConstants.MAKER_ID));
			billerRS = billerPstmt.executeQuery();

			if (billerRS.next()) {
				resCount = billerRS.getInt(1);
			}

			if (resCount == 1) {
				responseDTO.addMessages("Pin Verification Success");
			} else {
				responseDTO.addError("Pin Verification Failed");
			}

		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in BillPayVerifyPin [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in BillPayVerifyPin [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeResultSet(billerRS);
			DBUtils.closePreparedStatement(billerPstmt);
			DBUtils.closeConnection(connection);
			cryptedPassword = null;
			key1 = null;
			billerQry = null;
		}

		return responseDTO;
	}

	
	public ResponseDTO payBillInsertDetails(RequestDTO requestDTO) {

		logger.debug("Inside [NewBillPaymentDAO][payBillInsertDetails] ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			billerDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callable = connection.prepareCall("{call PAYBILLPKG.paybillInsertProc(?,?,?,?,?,?,?,?,?,?,?)}");

			callable.setString(1, requestJSON.getString("billerCode"));
			callable.setString(2, requestJSON.getString("accountNo"));
			callable.setString(3, requestJSON.getString("accountName"));
			callable.setString(4, requestJSON.getString("customerName"));
			callable.setString(5, requestJSON.getString("telephone"));
			callable.setString(6, requestJSON.getString("modeOfPayment"));
			callable.setString(7, requestJSON.getString("amount"));
			callable.setString(8, requestJSON.getString("narration"));
			callable.setString(9, requestJSON.getString("makerId"));
			callable.registerOutParameter(10, OracleTypes.VARCHAR);
			callable.registerOutParameter(11, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(10).indexOf("SUCCESS") == -1) {
				responseDTO.addError(callable.getString(11));
			} else {
				responseDTO.addMessages(callable.getString(11));
			}

			requestJSON.put("Message", callable.getString(11));
			responseJSON = requestJSON;
			billerDataMap.put("BILLPAY", responseJSON);
			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in register Biller [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in Register Biller [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}


	
	
	
	public ResponseDTO viewBillerDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside [NewBillPaymentDAO][viewBillerDetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;
		String merchantQry = "Select BILLER_ID,NAME,ABBREVATION,COMM_TYPE,AMOUNT/100,MAKER_ID,"
				+ "to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),AGENCY_TYPE,ADDRESS,TELEPHONE,CONTACT_PERSON"
				+ ",EMAIL from BILLER_REGISTRATION where BILLER_ID=?";
		try {

			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			
			requestJSON = requestDTO.getRequestJSON();
			
			merchantPstmt = connection.prepareStatement(merchantQry);
			
			merchantPstmt.setString(1, requestJSON.getString("billerCode"));
			
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				resultJson.put("BILLER_ID",merchantRS.getString(1));
				resultJson.put("NAME",merchantRS.getString(2));
				resultJson.put("ABBREVATION",merchantRS.getString(3));
				resultJson.put("COMM_TYPE",merchantRS.getString(4));
				resultJson.put("AMOUNT",merchantRS.getString(5));
				resultJson.put("MAKER_ID", merchantRS.getString(6));
				resultJson.put("MAKER_DATE",merchantRS.getString(7));
				resultJson.put("AGENCY_TYPE",merchantRS.getString(8));
				resultJson.put("ADDRESS",merchantRS.getString(9));
				resultJson.put("TELEPHONE",merchantRS.getString(10));
				resultJson.put("CONTACT_PERSON",merchantRS.getString(11));
				resultJson.put("EMAIL",merchantRS.getString(12));
				
			}
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			merchantMap.put("BILLER_INFO", resultJson);
			logger.debug("resultJson:::: [" + resultJson + "]");
			responseDTO.setData(merchantMap);

		} catch (Exception e) {
			logger.debug("Got Exception in viewBillerDetails ["+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {

			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
		}
		return responseDTO;
	}


	public ResponseDTO updateBillerDetails(RequestDTO requestDTO) {

		logger.debug("Inside [NewBillPaymentDAO][updateBillerDetails] ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		//JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			//billerBean = requestJSON.getJSONObject("billerBean");

			billerDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callable = connection.prepareCall("{call PAYBILLPKG.updateBillerProc(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			callable.setString(1, requestJSON.getString("abbreviation"));
			callable.setString(2, requestJSON.getString("billerCode"));
			callable.setString(3, requestJSON.getString("name"));
			callable.setString(4, requestJSON.getString("agencyType"));
			callable.setString(5, requestJSON.getString("address"));
			callable.setString(6, requestJSON.getString("telephone"));
			callable.setString(7, requestJSON.getString("contactPerson"));
			callable.setString(8, requestJSON.getString("email"));
			callable.setString(9, requestJSON.getString("commissionType"));
			callable.setString(10, requestJSON.getString("amount"));
			callable.setString(11, requestJSON.getString("rate"));
			callable.setString(12, requestJSON.getString("makerId"));
			callable.registerOutParameter(13, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(13).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}

			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in register Biller [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in Register Biller [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}

	
	public ResponseDTO billerAccountDetails(RequestDTO requestDTO) {

		Connection connection = null;
		logger.debug("Inside [NewBillPaymentDAO][billerAccountDetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "select BR.BILLER_ID,BR.NAME,BAM.ACCOUNT_NUMBER,BAM.ACCOUNT_NAME,BAM.ACCOUNT_TYPE,BAM.BILLER_CODE from BILLER_REGISTRATION BR,BILLER_ACCOUNT_MASTER BAM where BR.BILLER_ID = BAM.BILLER_ID and BAM.BILLER_ID=? and BAM.ACCOUNT_NUMBER=?";
		try {

			responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is [" + connection + "]");

			
			requestJSON = requestDTO.getRequestJSON();
			
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();

			merchantPstmt = connection.prepareStatement(merchantQry);
			
			merchantPstmt.setString(1, requestJSON.getString("billerId"));
			merchantPstmt.setString(2, requestJSON.getString("accountNumber"));
			
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				resultJson.put("BILLER_ID",merchantRS.getString(1));
				resultJson.put("NAME",merchantRS.getString(2));
				resultJson.put("ACCOUNT_NUMBER",merchantRS.getString(3));
				resultJson.put("ACCOUNT_NAME",merchantRS.getString(4));
				resultJson.put("ACCOUNT_TYPE",merchantRS.getString(5));
				resultJson.put("BILLER_CODE",merchantRS.getString(6));
			}
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			resultJson.put("BILLER_ACCT_INFO", resultJson);			
			merchantMap.put("BILLER_ACCT_INFO", resultJson);
			logger.debug("Result Json [" + resultJson + "]");
			responseDTO.setData(merchantMap);

		} catch (Exception e) {
			logger.debug("Got Exception in billerAccountDetails ["+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {

			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;		}
		return responseDTO;
	}

	
	
	public ResponseDTO modifyBillerAccountDetails(RequestDTO requestDTO) {

		logger.debug("Inside [NewBillPaymentDAO][modifyBillerAccountDetails] ... ");

		HashMap<String, Object> billerDataMap = null;
		Connection connection = null;
		CallableStatement callable = null;

		JSONObject billerBean = null;

		try {
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			logger.debug("Request JSON [" + requestJSON + "]");

			billerDataMap = new HashMap<String, Object>();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");
			callable = connection.prepareCall("{call PAYBILLPKG.updateBillerAccountProc(?,?,?,?,?)}");

			callable.setString(1, requestJSON.getString("billerId"));
			callable.setString(2, requestJSON.getString("accountNumber"));
			callable.setString(3, requestJSON.getString("accountName"));
			callable.setString(4, requestJSON.getString("accountType"));
			callable.registerOutParameter(5, OracleTypes.VARCHAR);

			callable.execute();

			if (callable.getString(5).indexOf("SUCCESS") == -1) {
				responseDTO.addError("Internal Error Occured.");
			} else {

			}
			responseDTO.setData(billerDataMap);
		} catch (SQLException e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("SQLException in register Biller [" + e.getMessage()
					+ "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");

			logger.debug("Exception in Register Biller [" + e.getMessage()
					+ "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			billerDataMap = null;
		}

		return responseDTO;
	}


	
	public ResponseDTO fetchBillerAccountDetails(RequestDTO requestDTO)
	  {
	    Connection connection = null;
	    this.logger.debug("Inside [NewBillPaymentDAO][fetchBillerAccountDetails].. ");
	    
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    JSONArray merchantJsonArray = null;
	    PreparedStatement merchantPstmt = null;
	    ResultSet merchantRS = null;
	    
	    JSONObject json = null;
	    
	    
	    String merchantQry = "Select BILLER_ID,ACCOUNT_NUMBER,ACCOUNT_NAME,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')"
				+"from BILLER_ACCOUNT_MASTER where BILLER_ID=?  order by ACCOUNT_NUMBER";
	    try
	    {
	      this.requestJSON = requestDTO.getRequestJSON();
	      
	      this.responseDTO = new ResponseDTO();
	      
	      connection = connection == null ? DBConnector.getConnection() : connection;
	      this.logger.debug("connection is [" + connection + "]");
	      
	      merchantMap = new HashMap();
	      resultJson = new JSONObject();
	      merchantJsonArray = new JSONArray();
	      
	      merchantPstmt = connection.prepareStatement(merchantQry);
	      
	      merchantPstmt.setString(1, this.requestJSON.getString("billerId"));
	      
	      merchantRS = merchantPstmt.executeQuery();
	      while (merchantRS.next())
	      {
	        json = new JSONObject();
	        json.put("BILLER_ID", 
	          merchantRS.getString(1));
	        json.put("ACCOUNT_NUMBER", 
	          merchantRS.getString(2));
	        json.put("ACCOUNT_NAME", 
	          merchantRS.getString(3));
	        json.put("MAKER_ID", 
	          merchantRS.getString(4));
	        json.put("MAKER_DTTM", 
	          merchantRS.getString(5));
	        merchantJsonArray.add(json);
	      }
	      DBUtils.closeResultSet(merchantRS);
	      DBUtils.closePreparedStatement(merchantPstmt);
	      DBUtils.closeConnection(connection);
	      resultJson.put("BILLER_ACCT_LIST", merchantJsonArray);
	      merchantMap.put("BILLER_ACCT_LIST", resultJson);
	      this.logger.debug("EntityMap [" + merchantMap + "]");
	      this.responseDTO.setData(merchantMap);
	    }
	    catch (Exception e)
	    {
	      this.logger.debug("Got Exception in fetchBillerAccountDetails [" + 
	        e.getMessage() + "]");
	      e.printStackTrace();
	    }
	    finally
	    {
	      DBUtils.closeResultSet(merchantRS);
	      DBUtils.closePreparedStatement(merchantPstmt);
	      DBUtils.closeConnection(connection);
	      
	      merchantMap = null;
	      resultJson = null;
	      merchantJsonArray = null;
	    }
	    return this.responseDTO;
	  }

	
	
	public ResponseDTO fetchBillerCount(RequestDTO requestDTO)
	  {
	    Connection connection = null;
	    this.logger.debug("Inside [NewBillPaymentDAO][fetchBillerCount].. ");
	    
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    PreparedStatement merchantPstmt = null;
	    ResultSet merchantRS = null;
	    
	    JSONObject json = null;
	    
	    
	    String merchantQry = "Select COUNT(*) FROM BILLER_REGISTRATION";
	    try
	    {
	      this.requestJSON = requestDTO.getRequestJSON();
	      
	      this.responseDTO = new ResponseDTO();
	      
	      connection = connection == null ? DBConnector.getConnection() : connection;
	      this.logger.debug("connection is [" + connection + "]");
	      
	      merchantMap = new HashMap();
	      resultJson = new JSONObject();
	      
	      merchantPstmt = connection.prepareStatement(merchantQry);
	      
	      merchantRS = merchantPstmt.executeQuery();
	      while (merchantRS.next())
	      {
	        resultJson.put("BILLER_COUNT",   merchantRS.getInt(1)+1);
	      }
	      DBUtils.closeResultSet(merchantRS);
	      DBUtils.closePreparedStatement(merchantPstmt);
	      DBUtils.closeConnection(connection);
	      merchantMap.put("BILLER_COUNT", resultJson);
	      this.logger.debug("EntityMap [" + merchantMap + "]");
	      this.responseDTO.setData(merchantMap);
	    }
	    catch (Exception e)
	    {
	      this.logger.debug("Got Exception in fetchBillerCount [" + 
	        e.getMessage() + "]");
	      e.printStackTrace();
	    }
	    finally
	    {
	      DBUtils.closeResultSet(merchantRS);
	      DBUtils.closePreparedStatement(merchantPstmt);
	      DBUtils.closeConnection(connection);
	      
	      merchantMap = null;
	      resultJson = null;
	    }
	    return this.responseDTO;
	  }

}
