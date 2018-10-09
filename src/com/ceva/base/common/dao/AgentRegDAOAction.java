package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class AgentRegDAOAction {

	Logger logger = Logger.getLogger(SwitchUIDAO.class);

	ResponseDTO responseDTO = null;
	RequestDTO requestDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	public ResponseDTO WebLoginDetails(RequestDTO requestDTO) {

		logger.debug("Inside WebLoginDetails.. ");
		Connection connection = null;
		CallableStatement callableStatement = null;

		String mob = null;
		String refmob = null;
		String comp = null;
		String email = null;
		String rnum=null;
		String outMsg=null;
		
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    PreparedStatement clntPstmt = null;
	    PreparedStatement agntDetPstmt = null;
	    PreparedStatement agntRegPstmt = null;
	    PreparedStatement agntSMSPstmt = null;
	    ResultSet clntRS = null;
	    ResultSet agntDetRS = null;
	   
	    
	    JSONObject json = null;
	    JSONObject pjson = null;
	    int agntCnt=0;

		//String insertStoreProc = "{call INSERT_WEBLOGIN(?,?,?,?,?)}";
	    
	    String clntQry = "select nvl(fname,'-') from cic_clients where mobile_number=? and status='CS' ";
		
		String agntDetQry = "select count(*) from CIC_AGENT_MASTER where AGENT_MOBILE_NUMBER=? ";
		
		String agntRegQry = " INSERT INTO CIC_AGENT_MASTER (AGENT_MOBILE_NUMBER, AGENT_NAME, EMAIL,CREATED_BY,CREATED_DATE,REFERENCE_NO,ORGANIZATION,status,reg_type)"
				+ " VALUES(?,?,?,?,sysdate,?,?,'C','D') ";
		
		String alertInsQry = "insert into alerts (msg_date, mobile_no, appl, fetch_status, txn_ref_no, out_message, subject ) values (sysdate,?,'SMS','P',?, ?, 'CIC_BIMA' ) ";
		
		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			logger.debug("Request JSON [" + requestJSON + "]");

			connection = connection == null ? DBConnector.getConnection():connection;

			mob = requestJSON.getString("mob");
			refmob = requestJSON.getString("refmob");
			comp = requestJSON.getString("comp");
			email = requestJSON.getString("email");
						
			if(mob.startsWith("0"))
			{
				mob="254"+mob.substring(1);
			}else if(mob.startsWith("7")){
				mob="254"+mob;
			}else if(mob.startsWith("2")){
			}
			
			logger.debug("mob  [" + mob + "]");

			logger.debug("connection is  [" + connection + "]");
			
			 DateFormat df = new SimpleDateFormat("ddmmss");
			    
	          Date dateobj = new Date();
	          rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
			
		      resultJson = new JSONObject();
		      merchantMap = new HashMap();
		      requestJSON = requestDTO.getRequestJSON();
		      System.out.println("requestJSON>>"+requestJSON);
		      
		      json = new JSONObject();
		      
		      agntDetPstmt = connection.prepareStatement(agntDetQry);
		      agntDetPstmt.setString(1, mob);
		      agntDetRS = agntDetPstmt.executeQuery(); 
		      
		      if (agntDetRS.next())
		      {
		    	  agntCnt = Integer.parseInt(agntDetRS.getString(1));
		      }
		      
		      logger.debug("agent ext Count is  [" + agntCnt + "]");
    
		      if (agntCnt > 0)
			  {	
		    	  json.put("ackmsg", "Champion Already Registered.");
		    	  responseDTO.addMessages("Champion already Registered.");
			  }else
			  {    
			      clntPstmt = connection.prepareStatement(clntQry);
			      clntPstmt.setString(1, mob);
			      clntRS = clntPstmt.executeQuery();
			      
			      if (clntRS.next())
			      {
			    	  logger.debug("client name is  [" + clntRS.getString(1) + "]");
			    	  String cname = clntRS.getString(1);
			    	  
			    	  agntRegPstmt = connection.prepareStatement(agntRegQry);
			    	  agntRegPstmt.setString(1, mob );
			    	  agntRegPstmt.setString(2, cname);
			    	  agntRegPstmt.setString(3, requestJSON.getString("email"));
			    	  agntRegPstmt.setString(4, cname);
			    	  agntRegPstmt.setString(5, requestJSON.getString("refmob"));
			    	  agntRegPstmt.setString(6, requestJSON.getString("comp"));
			    	  int agntRegCnt=agntRegPstmt.executeUpdate();
			    	  logger.debug("agntRegCnt is  [" + agntRegCnt + "]");
			    	 /* if (agntRegCnt>0)
			    	  {
			    		  outMsg = "Dear Agent "+cname+", you are now Successfully Registered as a CIC Bima Credo Agent. Please proceed to send new07xxxxxxxx to 20818 to link Customers Activations to you.";
			    		  agntSMSPstmt = connection.prepareStatement(alertInsQry);
			    		  agntSMSPstmt.setString(1, requestJSON.getString("mob") );
			    		  agntSMSPstmt.setString(2, rnum );
			    		  agntSMSPstmt.setString(3, outMsg );
			    		  int agntSMSCnt=agntSMSPstmt.executeUpdate();
			    		  logger.debug("agntSMSCnt  [" + agntSMSCnt + "]");
			    	  }*/
					  connection.commit();
					  json.put("ackmsg", "Champion Successfully Registered.");
					  responseDTO.addMessages("Champion Successfully Registered.");
			    	  
			      }else
			      {
			    	  json.put("ackmsg", "Champions Must Be The Active Bima Credo Clients");
			    	  responseDTO.addMessages("Champions Must Be The Active Bima Credo Clients"); 
			      }
			         
			      
			  }    
		      
		      merchantMap.put("MERCHANT_LIST", json);
		      logger.debug("merchantMap ["+merchantMap.toString()+"]");
		      this.responseDTO.setData(merchantMap);


		} catch (SQLException e) {
			logger.debug("SQLException in Agent Registration [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while creating agent.");
		} catch (Exception e) {
			logger.debug("Exception in Agent Registration [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while creating agent.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			mob = null;
			refmob = null;
			comp = null;
			email = null;
			agntDetPstmt = null;
		}
		return responseDTO;
	}
	
	public ResponseDTO fetchSelfRegAgentDet(RequestDTO requestDTO) {

		logger.debug("Inside fetchSelfRegAgentDet.. ");
		Connection connection = null;
		CallableStatement callableStatement = null;

		String mob = null;
		String cname = null;
		String cdate = null;
		String email = null;
		String acn = null;
		
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

		//String insertStoreProc = "{call INSERT_WEBLOGIN(?,?,?,?,?)}";
	    
	    String clntQry = "select nvl((fname||' '||mname||' '||lname),'-') from cic_clients where mobile_number=? and status='CS' ";
		
		String agntDetQry = "select count(*) from CIC_AGENT_MASTER where AGENT_MOBILE_NUMBER=? and status='A' ";
		
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
			      clntPstmt = connection.prepareStatement(selfRegQry);
			      clntPstmt.setString(1, requestJSON.getString("mob"));
			      clntRS = clntPstmt.executeQuery();
			      
			      json = new JSONObject();
			      if (clntRS.next())
			      {
			    	  logger.debug("client name is  [" + clntRS.getString(1) + "]");
			    	  json.put("cdate", clntRS.getString(1));
			    	  json.put("mob", clntRS.getString(2));
			    	  json.put("cname", clntRS.getString(3));
			    	  json.put("email", clntRS.getString(4));
			    	  json.put("refmob", clntRS.getString(5));
			    	  json.put("comp", clntRS.getString(6));
			    	  json.put("acn", requestJSON.getString("acn"));
			    	 
					  //responseDTO.addMessages("Agent Successfully Registered.");
			    	  
			      }else
			      {
			    	  responseDTO.addMessages("Champions Must Be The Active Bima Credo Clients"); 
			      }
			  }
		      logger.debug("json ["+json+"]");
		      json.put("exst_cnt", agntCnt);
		      merchantMap.put("MERCHANT_LIST", json);
		      logger.debug("merchantMap ["+merchantMap.toString()+"]");
		      this.responseDTO.setData(merchantMap);

		} catch (SQLException e) {
			logger.debug("SQLException in Agent Registration [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while creating agent.");
		} catch (Exception e) {
			logger.debug("Exception in Agent Registration [" + e.getMessage()
					+ "]");
			responseDTO.addError("Problem while creating agent.");
		} finally {
			
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			mob = null;
			cdate = null;
			cname = null;
			email = null;
			agntDetPstmt = null;
		}
		return responseDTO;
	}
	
	public ResponseDTO validateAgent(RequestDTO requestDTO) {

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
			  }
		      
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
				  
			  
		      
		      json.put("exst_cnt", agntCnt);
		      json.put("cln_cnt", clnCnt);
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
	
	public ResponseDTO activeAgent(RequestDTO requestDTO) {
		logger.debug("insertAgent");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();
		
		JSONObject resultJson = null;
		
		Connection connection = null;

		HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
		PreparedStatement casepsmt = null;
		PreparedStatement agntSMSPstmt = null;
		PreparedStatement agntMailPstmt = null;
		
		String acn = null;
		String status=null;
		String outMsg=null;
		String cname=null;
		String rnum=null;
		String mailMsg=null;
		String email=null;

		try {
			
			connection = connection == null ? DBConnector.getConnection() : connection;
			resultJson = new JSONObject();	
			
			logger.debug("requestJSON>>"+requestJSON);
			acn=requestJSON.getString("acn");
			cname=requestJSON.getString("cname");
			email=requestJSON.getString("email");
			
			 DateFormat df = new SimpleDateFormat("ddmmss");
			    
	          Date dateobj = new Date();
	          rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
			
			if ("Rjct".equalsIgnoreCase(acn))
			{
				status="R";
			}else
			{
				status="A";
			}
					
			
			String caseInsertQry = "update CIC_AGENT_MASTER set status=?, APPROVED_BY=?, APPROVED_DATE=sysdate, A_REMARKS=? where AGENT_MOBILE_NUMBER=? ";
			
			String alertInsQry = "insert into alerts (msg_date, mobile_no, appl, fetch_status, txn_ref_no, out_message, subject,RETRY_COUNT ) values (sysdate,?,'SMS','P',?, ?, 'CIC_BIMA',0 ) ";
			
			String mailInsQry = "insert into ALERTS (MSG_DATE,EMAIL_ID,MOBILE_NO,APPL,FETCH_STATUS,MAILFROM,MAILTO,SUBJECT,DETAILS,TXN_TYPE,TXN_REF_NO,RETRY_COUNT,RESDTTM,UNIQUE_ID,OUT_MESSAGE)"
					+ "values (sysdate,?,?,'MAIL','P','bimacredo@cevaltd.com',?,'CIC BIMA CREDO','Success','INTERNALPASSWORD',?,4,sysdate,?,?) ";
          
		    casepsmt = connection.prepareStatement(caseInsertQry);
		    casepsmt.setString(1, status);
		    casepsmt.setString(2, requestJSON.getString("mkrid") );
		    casepsmt.setString(3, requestJSON.getString("remarks"));
		    casepsmt.setString(4, requestJSON.getString("mob"));
			int updCnt=casepsmt.executeUpdate();
			
			if (updCnt > 0)
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
	    		  
				connection.commit();
				resultJson.put("result", "Champion Activated Successfully");
			}else
			{
				resultJson.put("result", "Champion Registration Rejected");
			}
				
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



}
