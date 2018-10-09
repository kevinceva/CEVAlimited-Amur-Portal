package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.ceva.util.HttpPostRequestHandler;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

public class CICAgencyDAO
{
	private Logger logger = Logger.getLogger(CICAgencyDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO getClientDetails(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getClientDetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		PreparedStatement merchantPstmt = null;
		PreparedStatement purchasePstmt = null;
		ResultSet merchantRS = null;
		ResultSet purchaseRS = null;

		JSONObject json = null;
		JSONObject pjson = null;

		String mob=null;

		String merchantQry = "select FNAME from cic_clients where mobile_number=? and status='CS' ";

		String purchaseQry = " select count(*) from CIC_AGENT_MASTER where AGENT_MOBILE_NUMBER=? ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			resultJson = new JSONObject();
			merchantMap = new HashMap();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);

			mob=requestJSON.getString("mob");

			if(mob.startsWith("0"))
			{
				mob="254"+mob.substring(1);
			}else if(mob.startsWith("7")){
				mob="254"+mob;
			}else if(mob.startsWith("2")){
			}

			logger.debug("mob  [" + mob + "]");

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, mob);
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			if (merchantRS.next())
			{       
				resultJson.put("cname", merchantRS.getString(1));
				resultJson.put("mob", mob);
				resultJson.put("remarks", "Y");
			}else
			{
				resultJson.put("remarks", "N"); 
			}

			purchasePstmt = connection.prepareStatement(purchaseQry);
			purchasePstmt.setString(1, mob);
			purchaseRS = purchasePstmt.executeQuery();     

			json = new JSONObject();
			if (purchaseRS.next())
			{        
				resultJson.put("exst_cnt", purchaseRS.getString(1));
			}

			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("EntityMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getClientDetails [" + 
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

	public ResponseDTO getBeneficiaryDetails(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getBeneficiaryDetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "SELECT BENEFICIARY_ID,replace(FNAME||' '||MNAME||' '||LNAME,'''',' '),decode(GENDER,'M','Male','F','Female',GENDER),to_char(DATE_OF_BIRTH,'DD-MM-YYYY'),ID_NUMBER,MOBILENUMBER,VALIDATION_CNT,PHOTO,SIGNATURE,to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),STATUS from CIC_CLIENT_BENEFICIARIES where CLIENT_ID=?";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("cid"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{       
				json.put("bid", merchantRS.getString(1));
				json.put("bname",  merchantRS.getString(2));
				json.put("bgender", merchantRS.getString(3));
				json.put("bdob", merchantRS.getString(4));
				json.put("bidno", merchantRS.getString(5));
				json.put("bmob", merchantRS.getString(6));
				json.put("bvalcnt", merchantRS.getString(7));
				json.put("bphoto", merchantRS.getString(8));
				json.put("bsign", merchantRS.getString(9));
				json.put("bcdate", merchantRS.getString(10));
				json.put("bstatus", merchantRS.getString(11));
				json.put("srchval", requestJSON.getString("srchval"));
				System.out.println("second Json1>>"+resultJson);
			}
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			//   resultJson.put("MERCHANT_LIST", merchantJsonArray);
			merchantMap.put("MERCHANT_LIST", json);
			this.logger.debug("EntityMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getBeneficiaryDetails [" + 
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


	public ResponseDTO getRequestAsResponse(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getRequestAsResponse].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		try
		{
			this.responseDTO = new ResponseDTO();

			/*      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");*/

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			/* merchantPstmt = connection.prepareStatement(merchantQry);
      merchantPstmt.setString(1, requestJSON.getString("cid"));
      merchantPstmt.setString(2, requestJSON.getString("fdate"));
      merchantPstmt.setString(3, requestJSON.getString("tdate"));
      merchantRS = merchantPstmt.executeQuery();    

      json = new JSONObject();
      while (merchantRS.next())
      {
          json.put("PURCHASE_ID",  merchantRS.getString(1));
          json.put("PRIM_AMT",  merchantRS.getString(2));
          json.put("SUM_ASUR",  merchantRS.getString(3));
          json.put("MKRDT",  merchantRS.getString(4));
          json.put("STATUS",  merchantRS.getString(5));
        merchantJsonArray.add(json);
        json.clear();
      }
      DBUtils.closeResultSet(merchantRS);
      DBUtils.closePreparedStatement(merchantPstmt);
      DBUtils.closeConnection(connection);

      json = new JSONObject();
      json.put("cid",  requestJSON.getString("cid"));
      json.put("srchval",  requestJSON.getString("srchval"));
      merchantJsonArray.add(json);*/

			/*resultJson.put("MERCHANT_LIST", merchantJsonArray);*/
			resultJson.put("srchval", requestJSON.getString("srchval"));
			resultJson.put("cid", requestJSON.getString("cid"));
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getRequestAsResponse [" + 
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

	public ResponseDTO getPurchaseDetails(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getPurchaseDetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = " select CPP.PURCHASE_ID,CPP.AIRTIME_PREMIUM_AMOUNT,CPP.SUM_ASSURED,CPP.DATE_CREATED,CPP.STATUS from CIC_POLICY_PURCHASE CPP,CIC_POLICY CP "
				+ " where UPPER(CPP.POLICY_ID)=CP.POLICY_ID AND CP.CLIENT_ID=? and trunc(CPP.DATE_CREATED) between to_date(?,'dd/mm/yyyy') and to_date(?,'dd/mm/yyyy') "
				+ " order by CPP.DATE_CREATED desc ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("cid"));
			merchantPstmt.setString(2, requestJSON.getString("fdate"));
			merchantPstmt.setString(3, requestJSON.getString("tdate"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("PURCHASE_ID",  merchantRS.getString(1));
				json.put("PRIM_AMT",  merchantRS.getString(2));
				json.put("SUM_ASUR",  merchantRS.getString(3));
				json.put("MKRDT",  merchantRS.getString(4));
				json.put("STATUS",  merchantRS.getString(5));
				merchantJsonArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANT_LIST", merchantJsonArray);
			resultJson.put("srchval", requestJSON.getString("srchval"));
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getPurchaseDetails [" + 
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

	public ResponseDTO getScenarios(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getScenarios].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		PreparedStatement scenarioPstmt = null;
		ResultSet merchantRS = null;
		ResultSet scenariosRS = null;

		JSONObject json = null;
		JSONObject caseJson = null;

		String merchantQry = "SELECT CLIENT_ID,replace(FNAME||' '||MNAME||' '||LNAME,'''',' '),MOBILE_NUMBER,EMAIL from CIC_clients where mobile_number=?";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("srchval"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("CID",  merchantRS.getString(1));
				json.put("CNAME",  merchantRS.getString(2));
				json.put("MOB",  merchantRS.getString(3));
				json.put("EMAIL",  merchantRS.getString(4));
			}

			resultJson.put("CLDET", json);


			String caseQry = "select key_id,key_value from config_data where key_group='CALLCENTER' and KEY_TYPE='CASE' ";
			scenarioPstmt = connection.prepareStatement(caseQry);
			scenariosRS = scenarioPstmt.executeQuery();    

			caseJson = new JSONObject();
			while (scenariosRS.next())
			{
				caseJson.put(scenariosRS.getString(1),  scenariosRS.getString(2));
			}

			resultJson.put("caseJson", caseJson);
			resultJson.put("srchval", requestJSON.getString("srchval"));
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("CaseMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getScenarios [" + 
					e.getMessage() + "]");
			e.printStackTrace();
		}
		finally
		{
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(scenariosRS);
			DBUtils.closePreparedStatement(scenarioPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			merchantJsonArray = null;
		}
		return this.responseDTO;
	}  

	public ResponseDTO getClientFailedTxns(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getScenarios].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		PreparedStatement scenarioPstmt = null;
		ResultSet merchantRS = null;
		ResultSet scenariosRS = null;

		JSONObject json = null;
		JSONObject caseJson = null;

		String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, AMOUNT, STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
				+ " where  POLICY_ID=? and STATUS='FAILED' order by DATE_CREATED desc";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("mob"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("cdate",  merchantRS.getString(1));
				json.put("aid",  merchantRS.getString(2));
				json.put("pid",  merchantRS.getString(3));
				json.put("amt",  merchantRS.getString(4));
				json.put("status",  merchantRS.getString(5));
				json.put("rid",  merchantRS.getString(6));
				merchantJsonArray.add(json);
				json.clear();
			}

			resultJson.put("CLNT_FLD_TXNS", merchantJsonArray);

			resultJson.put("mob", requestJSON.getString("mob"));
			resultJson.put("srchval", requestJSON.getString("srchval"));
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("CaseMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getScenarios [" + 
					e.getMessage() + "]");
			e.printStackTrace();
		}
		finally
		{
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeResultSet(scenariosRS);
			DBUtils.closePreparedStatement(scenarioPstmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			merchantJsonArray = null;
		}
		return this.responseDTO;
	}   

	public ResponseDTO insertAgent(RequestDTO requestDTO) {
		logger.debug("insertAgent");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();
		String rnum=null;
		String caseid=null;
		String outMsg=null;
		String cname=null;
		String mob=null;
		String email=null;

		String mailMsg=null;

		JSONObject resultJson = null;
		JSONObject caseJson = null;

		Connection connection = null;
		ResultSet scenariosRS = null;

		HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
		PreparedStatement casepsmt = null;
		PreparedStatement insPstmt = null;
		PreparedStatement agntSMSPstmt = null;
		PreparedStatement agntMailPstmt = null;

		try {

			connection = connection == null ? DBConnector.getConnection() : connection;
			resultJson = new JSONObject();	

			cname=requestJSON.getString("cname");
			mob=requestJSON.getString("mob");
			email=requestJSON.getString("email");

			if(mob.startsWith("0"))
			{
				mob="254"+mob.substring(1);
			}else if(mob.startsWith("7")){
				mob="254"+mob;
			}else if(mob.startsWith("2")){
			}

			logger.debug("mob  [" + mob + "]");

			String caseInsertQry = "INSERT INTO CIC_AGENT_MASTER (AGENT_MOBILE_NUMBER, AGENT_NAME, EMAIL,CREATED_BY,CREATED_DATE,REFERENCE_NO,ORGANIZATION,REMARKS,STATUS,REG_TYPE) "
					+ " VALUES(?,?,?,?,sysdate,?,?,?,'A','B' )";

			String alertInsQry = "insert into alerts (msg_date, mobile_no, appl, fetch_status, txn_ref_no, out_message, subject,RETRY_COUNT ) values (sysdate,?,'SMS','P',?, ?, 'CIC_BIMA',0 ) ";

			String mailInsQry = "insert into ALERTS (MSG_DATE,EMAIL_ID,MOBILE_NO,APPL,FETCH_STATUS,MAILFROM,MAILTO,SUBJECT,DETAILS,TXN_TYPE,TXN_REF_NO,RETRY_COUNT,RESDTTM,UNIQUE_ID,OUT_MESSAGE)"
					+ "values (sysdate,?,?,'MAIL','P','bimacredo@cevaltd.com',?,'CIC BIMA CREDO','Success','INTERNALPASSWORD',?,4,sysdate,?,?) ";

			DateFormat df = new SimpleDateFormat("ddmmss");

			Date dateobj = new Date();
			rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);

			System.out.println("requestJSON>>"+requestJSON);



			casepsmt = connection.prepareStatement(caseInsertQry);
			casepsmt.setString(1, mob );
			casepsmt.setString(2, cname);
			casepsmt.setString(3, requestJSON.getString("email"));
			casepsmt.setString(4, requestJSON.getString("mkrid"));
			casepsmt.setString(5, requestJSON.getString("refmob"));
			casepsmt.setString(6, requestJSON.getString("comp"));
			casepsmt.setString(7, requestJSON.getString("remarks"));
			int intAgent=casepsmt.executeUpdate();

			if ( intAgent >0 )
			{
				outMsg = "Successfully Registered "+cname+" as a CIC BimaCredo Champion. Pls send new07xxxxxxxx to 20818 to link Customers Activations to you.";
				agntSMSPstmt = connection.prepareStatement(alertInsQry);
				agntSMSPstmt.setString(1, requestJSON.getString("mob") );
				agntSMSPstmt.setString(2, rnum );
				agntSMSPstmt.setString(3, outMsg );
				int agntSMSCnt=agntSMSPstmt.executeUpdate();
				logger.debug("agntSMSCnt  [" + agntSMSCnt + "]");

				mailMsg = "Dear Champion "+cname+", Congratulations!";
				mailMsg = mailMsg+"\n"+"\t"+ "You are now successfully registered as a Champion of CIC Bima Credo and will receive a commission of:";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"Kshs 5/= if your customer purchases an Airtime of Kshs 50/= or above";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"Kshs 10/= if your customer purchases an Airtime of Kshs 100/= or above";
				mailMsg = mailMsg+"\n"+"\t"+"Please note the following:";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"1. You will receive at End of Each Day at 9pm from today, an SMS and email informing you of your successful activations done and commission earned as follows:";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"\t"+"Dear Champion Yatin, you have activated 5 customers today, you have earned a commission of Kshs 50/= today.";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"2. You will receive an Email Daily with accumulative activations and commissions done as follows:";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"\t"+"Day 1: Activations = 5, Commission = Kshs 50/=";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"\t"+"Day 2: Activations = 10, Commissions = Kshs 100/=";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"\t"+"Day 3: .....etc";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"\t"+"Dear Champion Yatin, you have activated 5 customers today, you have earned a commission of Kshs 50/= today.";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"3. You will receive an SMS at 9pm each day informing you the Total Commission Kitty available. At present CIC Life has provided a Champion Commission Kitty of total Kshs 1 million, which will be drawn down daily by active champions based on their number of activations.";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"4. You will be paid your Commission on every Friday at 9am to your MPESA.";
				mailMsg = mailMsg+"\n"+"\t"+"\t"+"5. For you to qualify for your commission each week, you will need to have activated a minimum of 20 customers each week.";
				mailMsg = mailMsg+"\n"+"\n";
				mailMsg = mailMsg+"Thanking you,";
				mailMsg = mailMsg+"\n"+"CIC Life Insurance Team";


				agntMailPstmt = connection.prepareStatement(mailInsQry);
				agntMailPstmt.setString(1, email);
				agntMailPstmt.setString(2, requestJSON.getString("mob"));
				agntMailPstmt.setString(3, email);
				agntMailPstmt.setString(4, rnum );
				agntMailPstmt.setString(5, rnum );
				agntMailPstmt.setString(6, mailMsg );
				int agntMailCnt=agntMailPstmt.executeUpdate();
				logger.debug("agntMailCnt  [" + agntMailCnt + "]");	
			}

			connection.commit();

			resultJson.put("result", "Champion Registered Successfully");
			caseDataMap.put("INSERTED", resultJson);

			this.logger.debug("CaseMap [" + caseDataMap + "]");
			responseDTO.setData(caseDataMap);


			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(casepsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}  


	public ResponseDTO getRaisedCases(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getRaisedCases].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = 	" select status, cnt from "
				+ 	" ( select status,cnt, decode(status,'Query Approved',2,'Query Pending',1,3) ord from "
				+ 	" ( select decode(case_status,'QR','Query Pending','QA','Query Approved','QC','Query Completed','Query Pending') status, count(*) cnt "
				+   " from CIC_CALL_COMPLAINTS group by case_status)) order by ord ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("STATUS",  merchantRS.getString(1));
				json.put("COUNT",  merchantRS.getString(2));
				merchantJsonArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANT_LIST", merchantJsonArray);
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getRaisedCases [" + 
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

	public ResponseDTO getRaisedCasedetails(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getRaisedCasedetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "select cc.case_no,cc.mobile_no,cg.key_value,to_char(cc.raised_date,'HH:MI AM Month DD, YYYY'),cc.raised_by from CIC_CALL_COMPLAINTS CC, CONFIG_DATA CG "
				+ " where CC.CASE_STATUS=substr(?,1,2) and CC.CASE_DESC=cg.KEY_ID and CG.key_group='CALLCENTER' and CG.KEY_TYPE='CASE' order by CC.raised_date desc";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("status"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("refno",  merchantRS.getString(1));
				json.put("mob",  merchantRS.getString(2));
				json.put("comp",  merchantRS.getString(3));
				json.put("rsddt",  merchantRS.getString(4));
				json.put("rsdby",  merchantRS.getString(5));
				merchantJsonArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANT_LIST", merchantJsonArray);
			resultJson.put("status", requestJSON.getString("status"));
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getRaisedCasedetails [" + 
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

	public ResponseDTO submitCompAction(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][submitCompAction].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;
		String rsedby="";
		String aprvdby="";

		String merchantQry = "select cc.case_no,cc.mobile_no,cg.key_value,to_char(cc.raised_date,'HH:MI AM Month DD, YYYY'),cc.raised_by,cg.key_id,cc.APPROVED_BY from CIC_CALL_COMPLAINTS CC, CONFIG_DATA CG "
				+ " where CC.case_no=? and CC.CASE_DESC=cg.KEY_ID and CG.key_group='CALLCENTER' and CG.KEY_TYPE='CASE' ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			String mkrid = requestJSON.getString("mkrid");
			String status = requestJSON.getString("status");

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("refno"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("refno",  merchantRS.getString(1));
				json.put("mob",  merchantRS.getString(2));
				json.put("comp",  merchantRS.getString(3));
				json.put("rsddt",  merchantRS.getString(4));
				json.put("rsdby",  merchantRS.getString(5));
				json.put("compid",  merchantRS.getString(6));	          
				rsedby=merchantRS.getString(5);
				aprvdby=merchantRS.getString(7);
			}
			json.put("status", requestJSON.getString("status"));

			System.out.println("Validation Strings:"+status+"-"+rsedby+"-"+aprvdby+"-"+mkrid+"-"+status.substring(0,2));
			if ("QA".equalsIgnoreCase(status.substring(0,2)))
			{
				if (rsedby.equalsIgnoreCase(mkrid) || aprvdby.equalsIgnoreCase(mkrid))
				{
					System.out.println("Un-Authorized User");
					this.responseDTO.addError("Maker Cannot Be The Checker");
				}else
				{
					System.out.println("Authorized User");
					merchantMap.put("MERCHANT_LIST", json);
					this.logger.debug("PurMap [" + merchantMap + "]");
					this.responseDTO.setData(merchantMap);  
				}

			}
			else
			{	  

				merchantMap.put("MERCHANT_LIST", json);
				this.logger.debug("PurMap [" + merchantMap + "]");
				this.responseDTO.setData(merchantMap);
			}

		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in submitCompAction [" + 
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

	public ResponseDTO confirmCompAction(RequestDTO requestDTO) {
		logger.debug("confirmCompAction");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();
		String rnum=null;
		String vstatus = "";
		String vmkrid = "";
		String vremarks = "";
		String vcompid = "";
		JSONObject resultJson = null;

		Connection connection = null;

		HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
		PreparedStatement casepsmt = null;
		PreparedStatement caseDelpsmt = null;
		PreparedStatement caseUppsmt = null;
		PreparedStatement casePolpsmt = null;
		PreparedStatement casePurpsmt = null;
		PreparedStatement caseBenpsmt = null;
		PreparedStatement casePolDelpsmt = null;
		PreparedStatement casePurDelpsmt = null;
		PreparedStatement caseBenDelpsmt = null;
		PreparedStatement caseBenUpdpsmt = null;
		PreparedStatement caseBenClnUpdpsmt = null;
		PreparedStatement caseClnUpdpsmt = null;

		try {

			String caseInsertQry = "INSERT INTO CIC_CLIENTS_HIS ( CLIENT_ID,DATE_CREATED,DATE_OF_BIRTH,EMAIL,FNAME,GENDER,ID_NUMBER,ID_TYPE,INSERTED_DATE,INSTERD_BY,"
					+ " IPRS_DESC,LNAME,MNAME,MOBILE_NUMBER,PHOTO,POSTAL_ADDRESS,POSTAL_CODE,SALUTATION,SIGNATURE,STATUS,VALIDATION_CNT,INVALID_CNT ) "
					+ " select CLIENT_ID,DATE_CREATED,DATE_OF_BIRTH,EMAIL,FNAME,GENDER,ID_NUMBER,ID_TYPE,sysdate,?,IPRS_DESC,LNAME,MNAME,MOBILE_NUMBER,"
					+ " PHOTO,POSTAL_ADDRESS,POSTAL_CODE,SALUTATION,SIGNATURE,STATUS,VALIDATION_CNT,INVALID_CNT from cic_clients where mobile_number=?";
			String caseDeleteQry = "DELETE FROM CIC_CLIENTS WHERE MOBILE_NUMBER= ?";

			String caseClnRestUpdQry = "update CIC_CLIENTS set ID_NUMBER='', STATUS='CC',IPRS_DESC='', VALIDATION_CNT=1,INVALID_CNT=0 WHERE MOBILE_NUMBER= ?";

			String casePolInsertQry = "INSERT INTO CIC_POLICY_HIS ( CLIENT_ID,CUMMULATIVE_PREMIUM,CUMMULATIVE_SUM_ASSURED,DATE_ENDING,DATE_STARTED,INSERTED_DATE,"
					+ " INSTERD_BY,POLICY_ID,POLICY_NUMBER,PRODUCT_ID,STATUS,TOTAL_PURCHASE_AMT ) "
					+ " select CLIENT_ID,CUMMULATIVE_PREMIUM,CUMMULATIVE_SUM_ASSURED,DATE_ENDING,DATE_STARTED,sysdate,?,POLICY_ID,POLICY_NUMBER,"
					+ " PRODUCT_ID,STATUS,TOTAL_PURCHASE_AMT from CIC_POLICY where POLICY_ID=?";
			String casePolDeleteQry = "DELETE FROM CIC_POLICY WHERE POLICY_ID = ?";

			String casePurInsertQry = "INSERT INTO CIC_POLICY_PURCHASE_HIS ( AIRTIME_PREMIUM_AMOUNT,C2B_LOG_ID,DATE_CREATED,INSERTED_DATE,INSTERD_BY,"
					+ " POLICY_ID,PURCHASE_ID, STATUS,SUM_ASSURED ) "
					+ " select AIRTIME_PREMIUM_AMOUNT,C2B_LOG_ID,DATE_CREATED,sysdate,?,POLICY_ID,PURCHASE_ID,STATUS,SUM_ASSURED from CIC_POLICY_PURCHASE "
					+ " where POLICY_ID=?";
			String casePurDeleteQry = "DELETE FROM CIC_POLICY_PURCHASE WHERE POLICY_ID = ?";

			String caseBenInsertQry = "INSERT INTO CIC_CLIENT_BENEFICIARIES_HIS ( BENEFICIARY_ID,CLIENT_ID,DATE_CREATED,DATE_OF_BIRTH,FNAME,GENDER,ID_NUMBER,"
					+ " INSERTED_DATE,INSTERD_BY,INVALID_CNT,IPRS_DESC,LNAME,MNAME,MOBILENUMBER,PHOTO,POLICY_ID,SIGNATURE,STATUS,VALIDATION_CNT ) "
					+ " select BENEFICIARY_ID,CLIENT_ID,DATE_CREATED,DATE_OF_BIRTH,FNAME,GENDER,ID_NUMBER,sysdate,?,INVALID_CNT,IPRS_DESC,"
					+ " LNAME,MNAME,MOBILENUMBER,PHOTO,POLICY_ID,SIGNATURE,STATUS,VALIDATION_CNT from CIC_CLIENT_BENEFICIARIES "
					+ " where policy_id=?";
			String caseBenDeleteQry = "DELETE FROM CIC_CLIENT_BENEFICIARIES WHERE policy_id = ?";

			String caseClientUpdQry = "update CIC_CLIENTS set STATUS='CV' where MOBILE_NUMBER=? ";

			String caseBenfUpdQry = "update CIC_CLIENT_BENEFICIARIES set ID_NUMBER='', STATUS='BC',IPRS_DESC='', VALIDATION_CNT=1,INVALID_CNT=0 where policy_id=? ";

			String caseUpdateQry = "UPDATE CIC_CALL_COMPLAINTS set case_status='QC',completed_by=?, completed_date=sysdate,c_remarks=?  WHERE CASE_NO=?";

			connection = connection == null ? DBConnector.getConnection() : connection;
			resultJson = new JSONObject();

			vstatus = requestJSON.getString("status");
			vstatus = vstatus.substring(0,2);
			/*vmkrid = requestJSON.getString("mkrid");
				vremarks = requestJSON.getString("remarks");*/
			vcompid = requestJSON.getString("compid");

			System.out.println("values of inputs:"+vstatus+"-"+"-"+vcompid);
			if ("QA".equalsIgnoreCase(vstatus))
			{	

				if ("112".equalsIgnoreCase(vcompid))
				{	

					casepsmt = connection.prepareStatement(caseInsertQry);
					casepsmt.setString(1,requestJSON.getString("mkrid") );
					casepsmt.setString(2,requestJSON.getString("mob") );
					int insrt=casepsmt.executeUpdate();						

					System.out.println(insrt + " rows inserted");

					/*	caseDelpsmt = connection.prepareStatement(caseDeleteQry);
						caseDelpsmt.setString(1, requestJSON.getString("mob"));
						int dele=caseDelpsmt.executeUpdate();

						System.out.println(dele + " rows deleted");

						casePolpsmt = connection.prepareStatement(casePolInsertQry);
						casePolpsmt.setString(1,requestJSON.getString("mkrid") );
						casePolpsmt.setString(2,requestJSON.getString("mob") );
						int insrtPol=casePolpsmt.executeUpdate();						

						System.out.println(insrtPol + " rows inserted");

						casePolDelpsmt = connection.prepareStatement(casePolDeleteQry);
						casePolDelpsmt.setString(1, requestJSON.getString("mob"));
						int delePol=casePolDelpsmt.executeUpdate();

						System.out.println(delePol + " rows deleted");

						casePurpsmt = connection.prepareStatement(casePurInsertQry);
						casePurpsmt.setString(1,requestJSON.getString("mkrid") );
						casePurpsmt.setString(2,requestJSON.getString("mob") );
						int insrtPur=casePurpsmt.executeUpdate();						

						System.out.println(insrtPur + " rows inserted");

						casePurDelpsmt = connection.prepareStatement(casePurDeleteQry);
						casePurDelpsmt.setString(1, requestJSON.getString("mob"));
						int delePur=casePurDelpsmt.executeUpdate();

						System.out.println(delePur + " rows deleted");

						caseBenpsmt = connection.prepareStatement(caseBenInsertQry);
						caseBenpsmt.setString(1,requestJSON.getString("mkrid") );
						caseBenpsmt.setString(2,requestJSON.getString("mob") );
						int insrtBen=caseBenpsmt.executeUpdate();						

						System.out.println(insrtBen + " rows inserted");

						caseBenDelpsmt = connection.prepareStatement(caseBenDeleteQry);
						caseBenDelpsmt.setString(1, requestJSON.getString("mob"));
						int deleBen=caseBenDelpsmt.executeUpdate();

						System.out.println(deleBen + " rows deleted");*/

					caseClnUpdpsmt = connection.prepareStatement(caseClnRestUpdQry);
					caseClnUpdpsmt.setString(1, requestJSON.getString("mob"));
					int cupd=caseClnUpdpsmt.executeUpdate();

					System.out.println(cupd + " rows updated");

					if (insrt == 1 & cupd == 1 )
					{
						caseUppsmt = connection.prepareStatement(caseUpdateQry);
						caseUppsmt.setString(1, requestJSON.getString("mkrid"));
						caseUppsmt.setString(2, requestJSON.getString("remarks"));
						caseUppsmt.setString(3, requestJSON.getString("refno"));
						int upStmt=caseUppsmt.executeUpdate();

						connection.commit();
						resultJson.put("result", "Reset Data Successfully");
					}					
					else
					{
						resultJson.put("result", "Reset Data Failed");
					}
				}
				else if ("113".equalsIgnoreCase(vcompid))
				{

					casepsmt = connection.prepareStatement(caseInsertQry);
					casepsmt.setString(1,requestJSON.getString("mkrid") );
					casepsmt.setString(2,requestJSON.getString("mob") );
					int insrt=casepsmt.executeUpdate();						

					System.out.println(insrt + " rows inserted");

					caseBenpsmt = connection.prepareStatement(caseBenInsertQry);
					caseBenpsmt.setString(1,requestJSON.getString("mkrid") );
					caseBenpsmt.setString(2,requestJSON.getString("mob") );
					int insrtBen=caseBenpsmt.executeUpdate();						

					System.out.println(insrtBen + " rows inserted");

					/*						caseBenDelpsmt = connection.prepareStatement(caseBenDeleteQry);
						caseBenDelpsmt.setString(1, requestJSON.getString("mob"));
						int deleBen=caseBenDelpsmt.executeUpdate();

						System.out.println(deleBen + " rows deleted");*/

					caseBenUpdpsmt = connection.prepareStatement(caseBenfUpdQry);
					caseBenUpdpsmt.setString(1, requestJSON.getString("mob"));
					int UpdBen=caseBenUpdpsmt.executeUpdate();

					System.out.println(UpdBen + " rows Updated");

					caseBenClnUpdpsmt = connection.prepareStatement(caseClientUpdQry);
					caseBenClnUpdpsmt.setString(1, requestJSON.getString("mob"));
					int UpdBenCln=caseBenClnUpdpsmt.executeUpdate();

					System.out.println(UpdBenCln + " rows Updated");

					if (insrtBen == 1 & UpdBen == 1 )
					{
						caseUppsmt = connection.prepareStatement(caseUpdateQry);
						caseUppsmt.setString(1, requestJSON.getString("mkrid"));
						caseUppsmt.setString(2, requestJSON.getString("remarks"));
						caseUppsmt.setString(3, requestJSON.getString("refno"));
						int upStmt=caseUppsmt.executeUpdate();

						connection.commit();
						resultJson.put("result", "Reset Data Successfully");
					}					
					else
					{
						resultJson.put("result", "Reset Data Failed");
					}
				}
				else
				{
					caseUppsmt = connection.prepareStatement(caseUpdateQry);
					caseUppsmt.setString(1, requestJSON.getString("mkrid"));
					caseUppsmt.setString(2, requestJSON.getString("remarks"));
					caseUppsmt.setString(3, requestJSON.getString("refno"));
					int upStmt=caseUppsmt.executeUpdate();

					connection.commit();
					resultJson.put("result", "Complaint resolved Successfully");
				}

			}
			else
			{
				caseUpdateQry = "UPDATE CIC_CALL_COMPLAINTS set case_status='QA', approved_by=?, approved_date=sysdate,a_remarks=? WHERE CASE_NO=?";
				caseUppsmt = connection.prepareStatement(caseUpdateQry);
				caseUppsmt.setString(1, requestJSON.getString("mkrid"));
				caseUppsmt.setString(2, requestJSON.getString("remarks"));
				caseUppsmt.setString(3, requestJSON.getString("refno"));
				int upStmt=caseUppsmt.executeUpdate();

				connection.commit();
				resultJson.put("result", "Approved Successfully");

			}


			resultJson.put("srchval", requestJSON.getString("mob"));
			resultJson.put("compid", requestJSON.getString("compid"));
			caseDataMap.put("INSERTED", resultJson);

			this.logger.debug("CaseMap [" + caseDataMap + "]");
			responseDTO.setData(caseDataMap);


			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(casepsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	} 	  

	public ResponseDTO approveCompAction(RequestDTO requestDTO) {
		logger.debug("approveCompAction");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();
		String rnum=null;
		JSONObject resultJson = null;

		Connection connection = null;

		HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
		PreparedStatement casepsmt = null;
		PreparedStatement caseDelpsmt = null;
		PreparedStatement caseUppsmt = null;

		try {

			String caseUpdateQry = "UPDATE CIC_CALL_COMPLAINTS set case_status='QA' WHERE CASE_NO=?";
			connection = connection == null ? DBConnector.getConnection() : connection;

			resultJson = new JSONObject();

			caseUppsmt = connection.prepareStatement(caseUpdateQry);
			caseUppsmt.setString(1, requestJSON.getString("refno"));
			int upd=caseUppsmt.executeUpdate();

			if (upd>0)
			{	
				resultJson.put("result", "Complaint Approved Successfully");
			}else
			{
				resultJson.put("result", "Complaint Approval Failed");
			}

			connection.commit();
			resultJson.put("srchval", requestJSON.getString("srchval"));
			caseDataMap.put("INSERTED", resultJson);

			this.logger.debug("CaseMap [" + caseDataMap + "]");
			responseDTO.setData(caseDataMap);


			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(casepsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}  	

	public ResponseDTO getFailedTxns(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getFailedTxns].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		/*String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, AMOUNT, STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
		    		+ " where  STATUS='FAILED' order by DATE_CREATED desc";*/

		String merchantQry = " select * from "
				+ " (SELECT TO_CHAR(CA.DATE_CREATED,'HH:MI AM MONTH DD, YYYY') , CA.AIRTIME_ID, CA.POLICY_ID, CA.AMOUNT, CA.STATUS, CA.REQUEST_ID, CA.DATE_CREATED DT, "
				+ " CC.CASE_NO FROM CIC_C2B_AIRTIME_LOG CA, CIC_CALL_COMPLAINTS CC WHERE  CC.REQUEST_ID=CA.REQUEST_ID AND CA.STATUS='FAILED' AND CC.CASE_STATUS='QA'"
				+ " and CC.CASE_DESC='111' ) order by DT desc" ;
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("cdate",  merchantRS.getString(1));
				json.put("cid",  merchantRS.getString(2));
				json.put("mob",  merchantRS.getString(3));
				json.put("amt",  merchantRS.getString(4));
				json.put("status",  merchantRS.getString(5));
				json.put("rid",  merchantRS.getString(6));
				json.put("refno",  merchantRS.getString(8));
				merchantJsonArray.add(json);
				json.clear(); 
			}
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANT_LIST", merchantJsonArray);
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getFailedTxns [" + 
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

	public ResponseDTO getForceTopUpDet(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getForceTopUpDet].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;
		String rsedby="";
		String aprvdby="";

		String merchantQry = "select to_char(CA.DATE_CREATED,'HH:MI AM Month DD, YYYY'), CA.AIRTIME_ID, CA.POLICY_ID, CA.AMOUNT, CA.STATUS, CA.REQUEST_ID, CC.CASE_NO from CIC_C2B_AIRTIME_LOG CA ,"
				+ " CIC_CALL_COMPLAINTS CC WHERE  CA.REQUEST_ID=? AND CC.REQUEST_ID=CA.REQUEST_ID AND CC.CASE_STATUS='QA' AND CC.CASE_NO=? ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			String mkrid = requestJSON.getString("mkrid");

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("rid"));
			merchantPstmt.setString(2, requestJSON.getString("refno"));
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("cdate",  merchantRS.getString(1));
				json.put("cid",  merchantRS.getString(2));
				json.put("mob",  merchantRS.getString(3));
				json.put("amt",  merchantRS.getString(4));
				json.put("status",  merchantRS.getString(5));
				json.put("rid",  merchantRS.getString(6)); 
				json.put("refno",  merchantRS.getString(7)); 
			}

			merchantMap.put("MERCHANT_LIST", json);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);


		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getForceTopUpDet [" + 
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

	public ResponseDTO initiateForceTopUp(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][initiateForceTopUp].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;
		String rsedby="";
		String aprvdby="";
		PreparedStatement caseUppsmt = null;

		String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, AMOUNT, STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
				+ " where  request_id=?";
		String caseUpdateQry = "UPDATE CIC_CALL_COMPLAINTS set case_status='QC',completed_by=?, completed_date=sysdate,c_remarks=?,topup_remarks=?  WHERE CASE_NO=?";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			String rid = requestJSON.getString("rid");
			String newmob = requestJSON.getString("newmob");
			String mob = requestJSON.getString("mob");
			String amt = requestJSON.getString("amt");
			String cid = requestJSON.getString("cid");
			String cdate = requestJSON.getString("cdate");
			String remarks = requestJSON.getString("remarks");
			String refno = requestJSON.getString("refno");


			//mob amt rid
			logger.debug("Force TopUp URL Params - request id ["
					+ rid + "] old mob [" + mob + "] amt["
					+ amt + "] airtimeref id [" + cid + "] refno [" + refno +"]");

			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = "http://104.42.234.123:1234/cicinsurancecore/rest/cicinsrest/forceTopUp"
					+ "?mobileNumber=" + mob + "&amount=" + amt + "&existingRequestId="
					+ rid ;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "]");
			String responseCode = obj.getString("RESPONSE_CODE");
			String responseMsg = obj.getString("RESPONSE_MSG");

			logger.debug("Response Code:"+responseCode);
			logger.debug("Response Msg:"+responseMsg);
			json = new JSONObject();

			if ("00".equalsIgnoreCase(responseCode))
			{
				caseUppsmt = connection.prepareStatement(caseUpdateQry);
				caseUppsmt.setString(1, requestJSON.getString("mkrid"));
				caseUppsmt.setString(2, requestJSON.getString("remarks"));
				caseUppsmt.setString(3, responseMsg);
				caseUppsmt.setString(4, requestJSON.getString("refno"));
				int upStmt=caseUppsmt.executeUpdate();

				connection.commit();
				json.put("result", responseMsg);
			}else
			{
				caseUppsmt = connection.prepareStatement(caseUpdateQry);
				caseUppsmt.setString(1, requestJSON.getString("mkrid"));
				caseUppsmt.setString(2, requestJSON.getString("remarks"));
				caseUppsmt.setString(3, responseMsg);
				caseUppsmt.setString(4, requestJSON.getString("refno"));
				int upStmt=caseUppsmt.executeUpdate();

				connection.commit();
				json.put("result", responseMsg);
			}

			merchantMap.put("MERCHANT_LIST", json);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);


		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in initiateForceTopUp [" + 
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

	public ResponseDTO getTwoWaysSMSdetails(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getTwoWaysSMSdetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;
		String msgtxt="";

		JSONObject json = null;
		//org.json.simple.JSONObject json =new org.json.simple.JSONObject();

		String merchantQry = "select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY'), TL.REQUEST_FROM, replace(TL.REQUEST_TEXT,'''','@Q'), TL.REQUEST_TO"
				+ " from CIC_TWOWAYSMS_REQ_LOG  TL "
				/*+ " where trunc(requested_date)=trunc(current_date) and not exists (select 1 from CIC_CALL_SMS_REQ_LOG SL"*/
				+ " where not exists (select 1 from CIC_CALL_SMS_REQ_LOG SL"
				+ " where SL.REQUESTED_DATE=TL.REQUESTED_DATE and SL.REQUEST_FROM=TL.REQUEST_FROM"
				+ "	and SL.REQUEST_TEXT=TL.REQUEST_TEXT ) and TL.REQUEST_FROM not in ('+254725728083','+254723484524') order by TL.REQUESTED_DATE desc ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();

			while (merchantRS.next())
			{
				json.put("rdate",  merchantRS.getString(1));
				json.put("rfrom",  merchantRS.getString(2));
				json.put("rtxt",  merchantRS.getString(3));
				json.put("rto",  merchantRS.getString(4));
				merchantJsonArray.add(json);
				json.clear();
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			System.out.println("MARRAY "+merchantJsonArray);
			resultJson.put("MERCHANT_LIST", (merchantJsonArray));
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getTwoWaysSMSdetails [" + 
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

	public ResponseDTO submitSMSAction(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][submitSMSAction].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;
		String msgtxt="";

		String merchantQry = "select to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY'), REQUEST_FROM, REQUEST_TEXT, REQUEST_TO"
				+ " from CIC_TWOWAYSMS_REQ_LOG where REQUEST_FROM=? and to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY')=? ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");

			msgtxt=requestJSON.getString("rdate");
			System.out.println("msgtxt:="+msgtxt);
			/*msgtxt=msgtxt.replace("@Q", "'");
		      System.out.println("replaced msgtxt:="+msgtxt);*/

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantPstmt.setString(1, requestJSON.getString("rfrom"));
			merchantPstmt.setString(2, msgtxt);
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{

				logger.debug(merchantRS.getString(1)+"======>"+merchantRS.getString(2)+"=======>"+merchantRS.getString(3)+"========>"+merchantRS.getString(4));
				json.put("rdate",  merchantRS.getString(1));
				json.put("rfrom",  merchantRS.getString(2));		         
				json.put("rtxt",  merchantRS.getString(3));
				json.put("rto",  merchantRS.getString(4));
			}

			merchantMap.put("MERCHANT_LIST", json);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);


		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in submitSMSAction [" + 
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

	public ResponseDTO confirmRaiseSMSAction(RequestDTO requestDTO) {
		logger.debug("confirmRaiseSMSAction");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();

		JSONObject resultJson = null;

		Connection connection = null;
		String msgtxt="";

		HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
		PreparedStatement casepsmt = null;

		try {

			String smsInsertQry = "INSERT INTO CIC_CALL_SMS_REQ_LOG ( REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, REMARKS,  INSERTED_BY, INSERTED_DATE) "
					+ " select REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, ? ,  ? , sysdate "
					+ " from CIC_TWOWAYSMS_REQ_LOG where REQUEST_FROM=? and to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY')=? ";					

			connection = connection == null ? DBConnector.getConnection() : connection;
			resultJson = new JSONObject();

			System.out.println("values of inputs:"+requestJSON.toString());	

			casepsmt = connection.prepareStatement(smsInsertQry);
			casepsmt.setString(1,requestJSON.getString("remarks") );
			casepsmt.setString(2,requestJSON.getString("mkrid") );
			casepsmt.setString(3,requestJSON.getString("rfrom") );
			casepsmt.setString(4,requestJSON.getString("rdate") );
			int insrt=casepsmt.executeUpdate();		

			if (insrt>0 )
			{		
				System.out.println(insrt + " rows inserted");
				resultJson.put("result", "Remarks Updated Successfully");
			}else
				resultJson.put("result", "Remarks Update Failed");
			caseDataMap.put("INSERTED", resultJson);

			this.logger.debug("CaseMap [" + caseDataMap + "]");
			responseDTO.setData(caseDataMap);


			logger.debug("[responseDTO:::" + responseDTO + "]");
		} catch (SQLException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(casepsmt);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	} 	

	public ResponseDTO getRaisedSMSdetails(RequestDTO requestDTO)
	{
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][getTwoWaysSMSdetails].. ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray merchantJsonArray = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		JSONObject json = null;

		String merchantQry = "select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY'), TL.REQUEST_FROM, replace(TL.REQUEST_TEXT,'''','@Q'), TL.REQUEST_TO, nvl(TL.REMARKS,'-'), "
				+ " TL.INSERTED_BY, to_char(TL.INSERTED_DATE,'HH:MI AM Month DD, YYYY') "
				+ " from CIC_CALL_SMS_REQ_LOG  TL "
				+ " order by TL.INSERTED_DATE desc ";
		try
		{
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			resultJson = new JSONObject();
			merchantJsonArray = new JSONArray();
			requestJSON = requestDTO.getRequestJSON();
			System.out.println("requestJSON>>"+requestJSON);
			System.out.println("merchantQry ["+merchantQry +"]");
			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();     

			json = new JSONObject();
			while (merchantRS.next())
			{
				json.put("rdate",  merchantRS.getString(1));
				json.put("rfrom",  merchantRS.getString(2));
				json.put("rtxt",  merchantRS.getString(3));
				json.put("rto",  merchantRS.getString(4));
				json.put("remarks",  merchantRS.getString(5));
				json.put("cid",  merchantRS.getString(6));
				json.put("cdate",  merchantRS.getString(7));
				merchantJsonArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANT_LIST", merchantJsonArray);
			merchantMap.put("MERCHANT_LIST", resultJson);
			this.logger.debug("PurMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);
		}
		catch (Exception e)
		{
			this.logger.debug("Got Exception in getTwoWaysSMSdetails [" + 
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

	public String agentClientMapping(JSONArray finalData, String userId, String fileName){

		JSONObject jres = new JSONObject();
		String status = "";
		Connection connection = null;
		PreparedStatement casepsmt = null;
		int inpSize=0;
		int j=0;
		this.responseDTO = new ResponseDTO();
		try
		{

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");
			logger.debug("Inserting Data into card inventory Table");
			connection.setAutoCommit(false);

			logger.info("finalData size:::: ["+finalData.size()+"]");
			logger.info("finalData ["+finalData.toString()+"]");

			String insQuery="insert into CIC_AGENT_CLIENT_MAPPING ( AGENT_ID,AGEN_CLIENT_ID,CREATED_DATE,POLICY_ID,STATUS) values  "+ 
					" (?,'CLIAGE'||CIC_CLIENT_AGENT_SEQ.NEXTVAL,sysdate,?,'P')";

			logger.info("card inventory Query Is ["+insQuery+"] ");
			
			j=0;
			int retval=0;
			inpSize=finalData.size();
			logger.debug("no of inputs ["+inpSize+"]");
			casepsmt = connection.prepareStatement(insQuery);
			for (int i = 0; i < finalData.size(); i++) {				

				jres = (JSONObject) finalData.get(i);
				casepsmt.setString(1, (String) jres.get("agentNo"));
				casepsmt.setString(2, (String) jres.get("custNo"));
				
				casepsmt.addBatch();
			}
			connection.commit();
			int a[]=casepsmt.executeBatch();
			
			status="SUCCESS";
			responseDTO.addMessages("Uploaded Successfully");
			
			logger.info("finalData.size() >>>>>>>>> ["+finalData.size()+"] No Lo ["+a.length+"]");
			/*if (inpSize == a.length)
			{	
				connection.commit();
				status="SUCCESS";
				responseDTO.addMessages("Uploaded Successfully");
			}else
			{
				connection.rollback();
				status="FAILURE";
				responseDTO.addMessages("Insertion failed due to some error");
			
			}*/
			

		} catch (Exception e) {
			try{
				status="FAILURE";
				responseDTO.addMessages("Insertion failed due to some error");
				connection.rollback();
			}catch(Exception e1){
				
			}
			e.printStackTrace();

		}finally{
			DBUtils.closePreparedStatement(casepsmt);
			DBUtils.closeConnection(connection);
		}
		logger.debug(" agent map insrt sataus:"+status);
		return status;

	}
	
	public ResponseDTO validateChamp(RequestDTO requestDTO) {

		logger.debug("Inside validateAgent.. ");
		Connection connection = null;
		CallableStatement callableStatement = null;

		String mob = null;
		
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    PreparedStatement clntPstmt = null;
	    PreparedStatement agntDetPstmt = null;
	    PreparedStatement agntRegPstmt = null;
	    ResultSet clntRS = null;
	    ResultSet agntDetRS = null;
	    ResultSet agntRegRS = null;
	    
	    JSONObject json = null;
	    JSONObject pjson = null;
	    int agntCnt=0;
	    int clnCnt=0;

		//String insertStoreProc = "{call INSERT_WEBLOGIN(?,?,?,?,?)}";
	    
	    String clntQry = "select count(*) from cic_clients where mobile_number=? and status='CS' ";
		
		String agntDetQry = "select count(*) from CIC_AGENT_MASTER where AGENT_MOBILE_NUMBER=?  ";
		
		String agntRegQry = " INSERT INTO CIC_AGENT_MASTER (AGENT_MOBILE_NUMBER, AGENT_NAME, EMAIL,CREATED_BY,CREATED_DATE) VALUES(?,?,?,?,sysdate) ";
		
		String selfRegQry = "select to_char(TL.CREATED_DATE,'HH:MI AM Month DD, YYYY') CD, TL.AGENT_MOBILE_NUMBER MOB, TL.AGENT_NAME NAME, TL.EMAIL EMAIL,TL.REFERENCE_NO, "
				+ " TL.ORGANIZATION from CIC_AGENT_MASTER  TL  	 where TL.STATUS='C'  and TL.AGENT_MOBILE_NUMBER=?";
		
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("connection is  [" + connection + "]");
			
		      resultJson = new JSONObject();
		      merchantMap = new HashMap();
		      requestJSON = requestDTO.getRequestJSON();
		      System.out.println("requestJSON>>"+requestJSON);
		      json = new JSONObject();
		      agntDetPstmt = connection.prepareStatement(agntDetQry);
		      agntDetPstmt.setString(1, requestJSON.getString("mob"));
		      agntDetRS = agntDetPstmt.executeQuery(); 
		      
		      if (agntDetRS.next())
		      {
		    	  agntCnt = Integer.parseInt(agntDetRS.getString(1));
		      }
		      
		      logger.debug("agent ext Count is  [" + agntCnt + "]");
    
		      if (agntCnt > 0)
			  {	
		    	  responseDTO.addMessages("Champion already Registered.");
			  }else
			  {
				  clntPstmt = connection.prepareStatement(clntQry);
				  clntPstmt.setString(1, requestJSON.getString("mob"));
				  clntRS = clntPstmt.executeQuery(); 
				  
				  if (clntRS.next())
			      {
					  clnCnt = Integer.parseInt(clntRS.getString(1));
			      }
				  if (clnCnt <1)
				  {	
			    	  responseDTO.addMessages("Champions Must Be The Active Bima Credo Clients.");
				  }
				  
			  }
		      
		      json.put("exst_cnt", agntCnt);
		      logger.debug("json ["+json+"]");
		      merchantMap.put("MERCHANT_LIST", json);
		      logger.debug("merchantMap ["+merchantMap.toString()+"]");
		      this.responseDTO.setData(merchantMap);

		} catch (SQLException e) {
			logger.debug("SQLException in validate Agent [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while validate Agent.");
		} catch (Exception e) {
			logger.debug("Exception in validateAgent [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while validate Agent.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			mob = null;
			agntDetPstmt = null;
		}
		return responseDTO;
	}	

	public static String replaceNewLineChar(String str) {
		try {
			if (!str.isEmpty()) {
				return str.replaceAll("\n\r", "\\n")
						.replaceAll("\n", "\\n")
						.replaceAll(System.lineSeparator(), "\\n");
			}
			return str;
		} catch (Exception e) {
			// Log this exception
			return str;
		}
	}

	public boolean agentnovalid(String agentNo)
	{

		boolean status = true;
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][agentnovalid].. ");

		PreparedStatement agentPstmt = null;
		ResultSet agentRS = null;

		try
		{


			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");
			String agentQry="SELECT COUNT(*) FROM cic_clients WHERE mobile_number=? and status='CS' ";
			this.logger.debug("agentQry ["+agentQry+"] agentNo["+agentNo+"]");

			agentPstmt = connection.prepareStatement(agentQry);
			agentPstmt.setString(1,agentNo );
			agentRS = agentPstmt.executeQuery();				

			if (agentRS.next())
			{	 
				int agnetCnt = (Integer.parseInt(agentRS.getString(1)));
				this.logger.debug("agnetCnt ["+agnetCnt+"]");
				if( agnetCnt > 0)
				{
					status = true;
				}else{
					status = false;
				}
			}

		}catch(Exception e)
		{

			e.printStackTrace();
		}finally{
			DBUtils.closeResultSet(agentRS);
			DBUtils.closePreparedStatement(agentPstmt);
			DBUtils.closeConnection(connection);
		}


		return status;

	}
	
	public boolean custnovalid(String custNo)
	{

		boolean status = true;
		Connection connection = null;
		this.logger.debug("Inside [CICAgencyDAO][custnovalid].. ");

		PreparedStatement agentPstmt = null;
		ResultSet agentRS = null;

		try
		{
			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");
			String agentQry="SELECT COUNT(*) FROM cic_clients WHERE mobile_number=?  ";
			this.logger.debug("agentQry ["+agentQry+"] custNo["+custNo+"]");

			agentPstmt = connection.prepareStatement(agentQry);
			agentPstmt.setString(1,custNo );
			agentRS = agentPstmt.executeQuery();				

			if (agentRS.next())
			{	 
				int custCnt = (Integer.parseInt(agentRS.getString(1)));
				this.logger.debug("custCnt ["+custCnt+"]");
				if( custCnt > 0)
				{
					status = false;
				}else{
					status = true;
				}
			}

		}catch(Exception e)
		{

			e.printStackTrace();
		}finally{
			DBUtils.closeResultSet(agentRS);
			DBUtils.closePreparedStatement(agentPstmt);
			DBUtils.closeConnection(connection);
		}


		return status;

	}	
}
