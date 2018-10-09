package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.ceva.util.HttpPostRequestHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

public class CallCenterDAO
{
  private Logger logger = Logger.getLogger(CallCenterDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  

  public ResponseDTO fetchClientMobDet(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [CallCenterDAO][getClientDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    PreparedStatement purchasePstmt = null;
    PreparedStatement champPstmt = null;
    ResultSet merchantRS = null;
    ResultSet purchaseRS = null;
    ResultSet champRS = null;
    
    JSONObject json = null;
    JSONObject pjson = null;
    JSONObject mchjson = null;
    
    String merchantQry = " SELECT CC.CLIENT_ID,replace(CC.FNAME||' '||CC.MNAME||' '||CC.LNAME,'''',' '), CC.ID_NUMBER, "
    		+ " CC.MOBILE_NUMBER,to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY'), "
    		+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
    		+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
    		+ " 'BV','Client Verified and Beneficiary Verified', "
    		+ " 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Creation In Progress'),'CS','Active','Client or Beneficiary Verification In Progress') "
    		+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB WHERE ( CC.MOBILE_NUMBER=? or CC.ID_NUMBER=substr(?,4)) and CC.CLIENT_ID=CB.CLIENT_ID(+) ORDER BY CC.DATE_CREATED DESC ";
    
  
    
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      merchantJsonArray = new JSONArray();
      requestJSON = requestDTO.getRequestJSON();
      this.logger.debug("requestJSON>>"+requestJSON);
      this.logger.debug("merchantQry["+merchantQry+"]");
      merchantPstmt = connection.prepareStatement(merchantQry);
      merchantPstmt.setString(1, requestJSON.getString("srchval"));
      merchantPstmt.setString(2, requestJSON.getString("srchval"));
      merchantRS = merchantPstmt.executeQuery();   
      
      String mob = "";
      String idno = "";
      String srchval = requestJSON.getString("srchval");
      int mchCnt=0;
      json = new JSONObject();
      while (merchantRS.next())
      {
        json.put("cid", merchantRS.getString(1));
        json.put("cname", merchantRS.getString(2));
        json.put("idno", merchantRS.getString(3));
        idno=merchantRS.getString(3);
        json.put("mob", merchantRS.getString(4));
        mob=merchantRS.getString(4);
        json.put("cdate", merchantRS.getString(5));
        json.put("status", merchantRS.getString(6));
        json.put("srchval", srchval);
        if (mchCnt==0)
        {
        	if (srchval.equalsIgnoreCase(mob))
        		mchCnt=1;
        	if (srchval.equalsIgnoreCase(idno))
        		mchCnt=2;
        }
        merchantJsonArray.add(json);
        json.clear();
      }
     
      this.logger.debug("mchCnt:"+mchCnt);
      mchjson = new JSONObject();
      mchjson.put("mchCnt", mchCnt);
      
      
      
      this.logger.debug("valueof mer json ar:"+merchantJsonArray.toString());
      resultJson.put("CLN_LIST", merchantJsonArray);
      resultJson.put("MCH_LIST", mchjson);
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
      merchantJsonArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO getClientDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [CallCenterDAO][getClientDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    PreparedStatement purchasePstmt = null;
    PreparedStatement champPstmt = null;
    ResultSet merchantRS = null;
    ResultSet purchaseRS = null;
    ResultSet champRS = null;
    
    JSONObject json = null;
    JSONObject pjson = null;
    
    String merchantQry = "SELECT CC.CLIENT_ID,replace(CC.FNAME||' '||CC.MNAME||' '||CC.LNAME,'''',' '),decode(CC.GENDER,'M','Male','F','Female',CC.GENDER),to_char(CC.DATE_OF_BIRTH,'DD-MM-YYYY'), "
    		+ " CC.ID_TYPE,CC.ID_NUMBER,CC.MOBILE_NUMBER,CC.POSTAL_ADDRESS,CC.POSTAL_CODE,CC.EMAIL,to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY'), "
    		+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
    		+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
    		+ " 'BV','Client Verified and Beneficiary Verified', "
    		+ " 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Creation In Progress'), "
    		+ " 'CS','Active','Client or Beneficiary Verification In Progress'), CC.IPRS_DESC, round(CP.CUMMULATIVE_PREMIUM,2), round(CP.CUMMULATIVE_SUM_ASSURED,2) "
    		+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB,CIC_POLICY CP WHERE ( CC.MOBILE_NUMBER=? or CC.ID_NUMBER=substr(?,4)) and CP.POLICY_ID=CC.MOBILE_NUMBER and CC.CLIENT_ID=CB.CLIENT_ID(+) ORDER BY CC.DATE_CREATED DESC";
    
    String purchaseQry = " select txn,PID,APM,SA, DC, STS, AID, RID from ( select txn, PID,APM,SA, DC, STS, AID, RID from ( select 'Airtime' txn, CPP.PURCHASE_ID PID,round(CPP.AIRTIME_PREMIUM_AMOUNT,2) APM,round(CPP.SUM_ASSURED,2) SA,to_char(CPP.DATE_CREATED,'HH:MI AM Month DD, YYYY') DC,"
    		+ " CPP.STATUS STS, CP.AIRTIME_ID AID, ( select  substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16)))"
    		+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=CP.C2B_LOG_ID ) RID, CPP.DATE_CREATED "
    		+ " from CIC_POLICY_PURCHASE CPP,CIC_C2B_AIRTIME_LOG CP "
    		+ " where CPP.PURCHASE_ID=CP.PURCHASE_ID AND CP.POLICY_ID=?   "
    		+ " union all "
    		+ " select 'Cash' txn, CPP.PURCHASE_ID PID,round(CPP.AIRTIME_PREMIUM_AMOUNT,2) APM,round(CPP.SUM_ASSURED,2) SA,to_char(CPP.DATE_CREATED,'HH:MI AM Month DD, YYYY') DC,"
    		+ " CPP.STATUS STS, CP.PURCHASE_ID AID, ( select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16)))"
    		+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=CP.C2B_LOG_ID ) RID, CPP.DATE_CREATED "
    		+ " from CIC_POLICY_PURCHASE CPP, CIC_POLICY_TOPUP_PURCHASE_LOG CP "
    		+ " where CPP.PURCHASE_ID=CP.PURCHASE_ID AND CP.POLICY_ID=?  ) order by DATE_CREATED desc)"
    		+ " where rownum<6 ";
    
    
    String champQry = "select count(*) from CIC_AGENT_MASTER where AGENT_MOBILE_NUMBER=?";
    
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      merchantJsonArray = new JSONArray();
      requestJSON = requestDTO.getRequestJSON();
      this.logger.debug("requestJSON>>"+requestJSON);
      merchantPstmt = connection.prepareStatement(merchantQry);
      merchantPstmt.setString(1, requestJSON.getString("srchval"));
      merchantPstmt.setString(2, requestJSON.getString("srchval"));
      merchantRS = merchantPstmt.executeQuery();   
      
      String mob = "";
      
      json = new JSONObject();
      while (merchantRS.next())
      {
        json.put("cid", merchantRS.getString(1));
        json.put("cname", merchantRS.getString(2));
        json.put("gender", merchantRS.getString(3));
        json.put("dob", merchantRS.getString(4));
        json.put("idtype", merchantRS.getString(5));
        json.put("idno", merchantRS.getString(6));
        json.put("mob", merchantRS.getString(7));
        mob=merchantRS.getString(7);
        json.put("addr", merchantRS.getString(8));
        json.put("pcode", merchantRS.getString(9));
        json.put("email", merchantRS.getString(10));
        json.put("cdate", merchantRS.getString(11));
        json.put("status", merchantRS.getString(12));
        json.put("iprs", merchantRS.getString(13));
        json.put("cprim", merchantRS.getString(14));
        json.put("casrd", merchantRS.getString(15));
        json.put("srchval", requestJSON.getString("srchval"));
       // merchantJsonArray.add(json);
        //json.clear();
      }
      this.logger.debug("srchval >>"+requestJSON.getString("srchval")+"]mob["+mob+"]");
      champPstmt = connection.prepareStatement(champQry);
      champPstmt.setString(1, mob);
      champRS = champPstmt.executeQuery(); 
      
      if (champRS.next())
      {
    	  json.put("champStatus", champRS.getString(1));
      }
      
      
      purchasePstmt = connection.prepareStatement(purchaseQry);
      purchasePstmt.setString(1, mob);
      purchasePstmt.setString(2, mob);
      purchaseRS = purchasePstmt.executeQuery();   
      
      this.logger.debug("value of purchaseQry:"+purchaseQry+"---"+requestJSON.getString("srchval")+"]mob["+mob+"]");
      
      pjson = new JSONObject();
      while (purchaseRS.next())
      {  
    	pjson.put("TXN", purchaseRS.getString(1));
    	pjson.put("PURCHASE_ID", purchaseRS.getString(2));
        pjson.put("PRIM_AMT", purchaseRS.getString(3));
        pjson.put("SUM_ASUR", purchaseRS.getString(4));
        pjson.put("MKRDT", purchaseRS.getString(5));
        pjson.put("STATUS", purchaseRS.getString(6));
        pjson.put("AID", purchaseRS.getString(7));
        pjson.put("RID", purchaseRS.getString(8));
        merchantJsonArray.add(pjson);
        pjson.clear();
      }
  
      this.logger.debug("valueof mer json ar:"+merchantJsonArray.toString());
      resultJson.put("PUR_LIST", merchantJsonArray);
      resultJson.put("CLN_LIST", json);
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
      merchantJsonArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO getBeneficiaryDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [CallCenterDAO][getBeneficiaryDetails].. ");
    
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
    this.logger.debug("Inside [CallCenterDAO][getRequestAsResponse].. ");
    
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
    this.logger.debug("Inside [CallCenterDAO][getPurchaseDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    ResultSet merchantRS = null;
    
    JSONObject json = null;
    
    String merchantQry = " select CPP.PURCHASE_ID,round(CPP.AIRTIME_PREMIUM_AMOUNT,2),round(CPP.SUM_ASSURED,2),CPP.DATE_CREATED,CPP.STATUS from CIC_POLICY_PURCHASE CPP,CIC_POLICY CP "
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
    this.logger.debug("Inside [CallCenterDAO][getScenarios].. ");
    
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
    this.logger.debug("Inside [CallCenterDAO][getScenarios].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    PreparedStatement scenarioPstmt = null;
    ResultSet merchantRS = null;
    ResultSet scenariosRS = null;
    
    JSONObject json = null;
    JSONObject caseJson = null;
    
    String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, round(AMOUNT,2), STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
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
  
	public ResponseDTO insertScenarios(RequestDTO requestDTO) {
		logger.debug("insertScenarios");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();
		String rnum=null;
		String caseid=null;
		JSONObject resultJson = null;
		JSONObject caseJson = null;
		
		Connection connection = null;
		ResultSet scenariosRS = null;

		HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
		PreparedStatement casepsmt = null;
		PreparedStatement insPstmt = null;

		try {
			
			caseid=requestJSON.getString("cases");
			connection = connection == null ? DBConnector.getConnection() : connection;
			resultJson = new JSONObject();
			
			if ("117".equalsIgnoreCase(caseid))
			{
				 String caseQry = "select round(CUMMULATIVE_SUM_ASSURED,2) from CIC_POLICY where POLICY_ID=?";
			      insPstmt = connection.prepareStatement(caseQry);
			      insPstmt.setString(1, requestJSON.getString("mob"));
			      scenariosRS = insPstmt.executeQuery();    
			      
			      caseJson = new JSONObject();
			      while (scenariosRS.next())
			      {
			    	  caseJson.put("CSA",  scenariosRS.getString(1));
			      }
			      resultJson.put("CSA", caseJson);
			}
			else if ("111".equalsIgnoreCase(caseid))
			{	
			String caseInsertQry = "INSERT INTO CIC_CALL_COMPLAINTS (CASE_NO, CLIENT_ID, MOBILE_NO,CASE_DESC,RAISED_DATE,RAISED_BY,CASE_STATUS,R_REMARKS,request_id) VALUES(?,?,?,?,sysdate,?,?,?,?)";
		    
		    
		   	    
		    DateFormat df = new SimpleDateFormat("ddmmss");
		    
          Date dateobj = new Date();
          rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
          
          System.out.println("requestJSON>>"+requestJSON);
		           
		        String rid= requestJSON.getString("rid");  
          
		    casepsmt = connection.prepareStatement(caseInsertQry);
		    casepsmt.setString(1,rnum );
		    casepsmt.setString(2, requestJSON.getString("cid"));
		    casepsmt.setString(3, requestJSON.getString("mob"));
		    casepsmt.setString(4, requestJSON.getString("cases"));		    
		    casepsmt.setString(5, requestJSON.getString("mkrid"));
		    casepsmt.setString(6, "QR");
		    casepsmt.setString(7,requestJSON.getString("remarks"));
		    casepsmt.setString(8,rid);
			int insert=casepsmt.executeUpdate();
			
			connection.commit();
			
			resultJson.put("REFNO", rnum);
			}
			else
			{	
			String caseInsertQry = "INSERT INTO CIC_CALL_COMPLAINTS (CASE_NO, CLIENT_ID, MOBILE_NO,CASE_DESC,RAISED_DATE,RAISED_BY,CASE_STATUS,R_REMARKS) VALUES(?,?,?,?,sysdate,?,?,?)";
		    
		    
		   	    
		    DateFormat df = new SimpleDateFormat("ddmmss");
		    
          Date dateobj = new Date();
          rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
          
          System.out.println("requestJSON>>"+requestJSON);
		           
		           
          
		    casepsmt = connection.prepareStatement(caseInsertQry);
		    casepsmt.setString(1,rnum );
		    casepsmt.setString(2, requestJSON.getString("cid"));
		    casepsmt.setString(3, requestJSON.getString("mob"));
		    casepsmt.setString(4, requestJSON.getString("cases"));		    
		    casepsmt.setString(5, requestJSON.getString("mkrid"));
		    casepsmt.setString(6, "QR");
		    casepsmt.setString(7,requestJSON.getString("remarks"));
			int insert=casepsmt.executeUpdate();
			
			connection.commit();
			
			resultJson.put("REFNO", rnum);
			}

			resultJson.put("srchval", requestJSON.getString("srchval"));
			resultJson.put("cases", requestJSON.getString("cases"));
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
	    this.logger.debug("Inside [CallCenterDAO][getRaisedCases].. ");
	    
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
	    this.logger.debug("Inside [CallCenterDAO][getRaisedCasedetails].. ");
	    
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
	    this.logger.debug("Inside [CallCenterDAO][submitCompAction].. ");
	    
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
	      if ("QR".equalsIgnoreCase(status.substring(0,2)))
	      {
	    	  //if (rsedby.equalsIgnoreCase(mkrid) || aprvdby.equalsIgnoreCase(mkrid))
	    	  if ( rsedby.equalsIgnoreCase(mkrid) )
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
/*				if ("QA".equalsIgnoreCase(vstatus))
				{*/	
					
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
					
				/*}
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
				*/
				
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
		    this.logger.debug("Inside [CallCenterDAO][getFailedTxns].. ");
		    
		    HashMap<String, Object> merchantMap = null;
		    JSONObject resultJson = null;
		    JSONArray merchantJsonArray = null;
		    PreparedStatement merchantPstmt = null;
		    ResultSet merchantRS = null;
		    
		    JSONObject json = null;
		    
		    /*String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, AMOUNT, STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
		    		+ " where  STATUS='FAILED' order by DATE_CREATED desc";*/
		    
		    String merchantQry = " select * from "
		    		+ " (SELECT TO_CHAR(CA.DATE_CREATED,'HH:MI AM MONTH DD, YYYY') , CA.AIRTIME_ID, CA.POLICY_ID, round(CA.AMOUNT,2), CA.STATUS, CA.REQUEST_ID, CA.DATE_CREATED DT, "
		    		+ " CC.CASE_NO FROM CIC_C2B_AIRTIME_LOG CA, CIC_CALL_COMPLAINTS CC WHERE  CC.REQUEST_ID=CA.REQUEST_ID AND CA.STATUS='FAILED' AND CC.CASE_STATUS='QC'"
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
		    this.logger.debug("Inside [CallCenterDAO][getForceTopUpDet].. ");
		    
		    HashMap<String, Object> merchantMap = null;
		    JSONObject resultJson = null;
		    JSONArray merchantJsonArray = null;
		    PreparedStatement merchantPstmt = null;
		    ResultSet merchantRS = null;
		    
		    JSONObject json = null;
		    String rsedby="";
		    String aprvdby="";
		    
		    String merchantQry = "select to_char(CA.DATE_CREATED,'HH:MI AM Month DD, YYYY'), CA.AIRTIME_ID, CA.POLICY_ID, round(CA.AMOUNT,2), CA.STATUS, CA.REQUEST_ID, CC.CASE_NO from CIC_C2B_AIRTIME_LOG CA ,"
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
		    this.logger.debug("Inside [CallCenterDAO][initiateForceTopUp].. ");
		    
		    HashMap<String, Object> merchantMap = null;
		    JSONObject resultJson = null;
		    JSONArray merchantJsonArray = null;
		    PreparedStatement merchantPstmt = null;
		    ResultSet merchantRS = null;
		    
		    JSONObject json = null;
		    String rsedby="";
		    String aprvdby="";
		    PreparedStatement caseUppsmt = null;
		    
		    String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, rouond(AMOUNT,2), STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
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

		      //Production IP : 52.166.1.232
		      //Test IO : 104.42.234.123
		      
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
		    this.logger.debug("Inside [CallCenterDAO][getTwoWaysSMSdetails].. ");
		    
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
		    this.logger.debug("Inside [CallCenterDAO][submitSMSAction].. ");
		    
		    HashMap<String, Object> merchantMap = null;
		    JSONObject resultJson = null;
		    JSONArray merchantJsonArray = null;
		    PreparedStatement merchantPstmt = null;
		    ResultSet merchantRS = null;
		    PreparedStatement ClnPstmt = null;
		    ResultSet ClnRS = null;
		    
		    JSONObject json = null;
		    String rsedby="";
		    String aprvdby="";
		    String mob="";
		    
		    String status ="";
		    String retstr ="";
		    String idno ="";
		    String acn ="";
		    
		    
		    String merchantQry = "select to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY'), REQUEST_FROM, REQUEST_TEXT, REQUEST_TO"
		    		+ " from CIC_TWOWAYSMS_REQ_LOG where REQUEST_FROM=? and to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY')=? ";
		    
		    String ClnStatusQry = "select status, id_number from cic_clients where mobile_number=? ";		    
		    try
		    {
		      this.responseDTO = new ResponseDTO();
		      
		      connection = connection == null ? DBConnector.getConnection() : connection;
		      this.logger.debug("connection is [" + connection + "]");
		      
		      merchantMap = new HashMap();
		      resultJson = new JSONObject();
		      merchantJsonArray = new JSONArray();
		      requestJSON = requestDTO.getRequestJSON();
		      this.logger.debug("requestJSON>>"+requestJSON);
		      acn=requestJSON.getString("acn");
		      mob=requestJSON.getString("rfrom");
		      mob=mob.substring(1);
		      
		      this.logger.debug("acn ["+ acn + "] mobile ["+mob+"] ClnStatusQry ["+ClnStatusQry +"]");
		      
		      ClnPstmt = connection.prepareStatement(ClnStatusQry);
		      ClnPstmt.setString(1, mob);
		      
		      ClnRS = ClnPstmt.executeQuery(); 
		      
/*		      if (ClnRS.next())
		      {
		    	  status = ClnRS.getString(1);
		    	  idno = ClnRS.getString(2);
		    	  this.logger.debug("status ["+status+"] idno ["+idno+"]");
		      
		      
			      if ("Edit".equalsIgnoreCase(acn))
				  {
			      
				      if (!"CS".equalsIgnoreCase(status))
				      {
					      this.logger.debug("merchantQry ["+merchantQry +"]");
					      
					      merchantPstmt = connection.prepareStatement(merchantQry);
					      merchantPstmt.setString(1, requestJSON.getString("rfrom"));
					      merchantPstmt.setString(2, requestJSON.getString("rdate"));
					      merchantRS = merchantPstmt.executeQuery();     
					      
					      json = new JSONObject();
					      while (merchantRS.next())
					      {
					    	  json.put("rdate",  merchantRS.getString(1));
					          json.put("rfrom",  merchantRS.getString(2));
					          json.put("rtxt",  merchantRS.getString(3));
					          json.put("rto",  merchantRS.getString(4));
					      }
					      json.put("retstr","Success");
				      }else
				      {
				    	  if ("CC".equalsIgnoreCase(status) && (idno.isEmpty() || idno.length() == 0) )
				    		  retstr="Client ID Number Not Submitted, Allowed Only for Verification Failed";
				    	  else if ("CC".equalsIgnoreCase(status) && (!idno.isEmpty() || idno.length()>0) )
				    		  retstr="Client ID Number Verification Pending, Allowed Only for Verification Failed";
				    	  else if ("CV".equalsIgnoreCase(status))
				    		  retstr="Client Status is Verified, Allowed Only for Verification Failed";
				    	  else if ("CS".equalsIgnoreCase(status))
				    		  retstr="Client Status is Active, Allowed Only for Verification Failed";
				    	  else
				    		  retstr="Allowed Only for Verification Failed";
				    	  json = new JSONObject();
				    	  json.put("retstr", retstr);
				      }
				  }else
				  {
					  this.logger.debug("merchantQry ["+merchantQry +"]");
				      
				      merchantPstmt = connection.prepareStatement(merchantQry);
				      merchantPstmt.setString(1, requestJSON.getString("rfrom"));
				      merchantPstmt.setString(2, requestJSON.getString("rdate"));
				      merchantRS = merchantPstmt.executeQuery();     
				      
				      json = new JSONObject();
				      while (merchantRS.next())
				      {
				    	  json.put("rdate",  merchantRS.getString(1));
				          json.put("rfrom",  merchantRS.getString(2));
				          json.put("rtxt",  merchantRS.getString(3));
				          json.put("rto",  merchantRS.getString(4));
				      }
				      json.put("retstr","Success"); 
				  }
		      }else
		      {
			      this.logger.debug("merchantQry ["+merchantQry +"]");
			      
			      merchantPstmt = connection.prepareStatement(merchantQry);
			      merchantPstmt.setString(1, requestJSON.getString("rfrom"));
			      merchantPstmt.setString(2, requestJSON.getString("rdate"));
			      merchantRS = merchantPstmt.executeQuery();     
			      
			      json = new JSONObject();
			      while (merchantRS.next())
			      {
			    	  json.put("rdate",  merchantRS.getString(1));
			          json.put("rfrom",  merchantRS.getString(2));
			          json.put("rtxt",  merchantRS.getString(3));
			          json.put("rto",  merchantRS.getString(4));
			      }
			      json.put("retstr","Success");
		      }*/
		      
		      if (ClnRS.next())
		      {
		    	  status = ClnRS.getString(1);
		    	  idno = ClnRS.getString(2);
		    	  this.logger.debug("status ["+status+"] idno ["+idno+"]");
		      }
		      
		      this.logger.debug("merchantQry ["+merchantQry +"]");
		      
		      merchantPstmt = connection.prepareStatement(merchantQry);
		      merchantPstmt.setString(1, requestJSON.getString("rfrom"));
		      merchantPstmt.setString(2, requestJSON.getString("rdate"));
		      merchantRS = merchantPstmt.executeQuery();     
		      
		      json = new JSONObject();
		      while (merchantRS.next())
		      {
		    	  json.put("rdate",  merchantRS.getString(1));
		          json.put("rfrom",  merchantRS.getString(2));
		          json.put("rtxt",  merchantRS.getString(3));
		          json.put("rto",  merchantRS.getString(4));
		      }
		      json.put("retstr","Success");
		      
		      json.put("anc",acn);
		      this.logger.debug("json [" + json.toString() + "]");
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
				String status=null;
				String rnum=null;
				String respType=null;
				String acn=null;
				String mob=null;
				String mapto=null;
				String name="Customer";
				String outMsg=null;
								
				HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
				PreparedStatement casepsmt = null;
				PreparedStatement smspsmt = null;
				PreparedStatement namepsmt = null;
				ResultSet nameRS = null;

				try {

					String smsInsertQry = "INSERT INTO CIC_CALL_SMS_REQ_LOG ( REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, R_REMARKS,  RAISED_BY, RAISED_DATE, "
							+ " FORMATTED_TEXT, REQ_ID, STATUS, RESP_TYPE,MAPTO ) select REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, ? , ? , sysdate, ?, ?, ?, ?, ? "
							+ " from CIC_TWOWAYSMS_REQ_LOG where REQUEST_FROM=? and to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY')=? ";
					
					String alertInsQry = "insert into alerts (msg_date, mobile_no, appl, fetch_status, txn_ref_no, out_message, subject, RETRY_COUNT ) values (sysdate,?,'SMS','P',?, ?, 'CIC_BIMA',0 ) ";
					
					String nameQry = "select fname,status from cic_clients where mobile_number = ? ";
					
				    connection = connection == null ? DBConnector.getConnection() : connection;
		          	resultJson = new JSONObject();
		          	
					System.out.println("values of inputs:"+requestJSON.toString());	
					acn = requestJSON.getString("acn");
					mob = requestJSON.getString("rfrom");
					mapto = requestJSON.getString("mapto");
					mob = mob.substring(1);
					
					if ("Repl".equalsIgnoreCase(acn))
					{	
						respType="R";
						status="SC";
					}
					else if ("Edit".equalsIgnoreCase(acn))
					{	
						respType="E";
						status="SR";
					}else
					{
						respType="N";
						status="SC";
					}
					
					
					  DateFormat df = new SimpleDateFormat("ddmmss");
					    
			          Date dateobj = new Date();
			          rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
					
			          logger.debug("rnum " + rnum + "] smsInsertQry : " + smsInsertQry + "]"); 
			          
					casepsmt = connection.prepareStatement(smsInsertQry);
										
				    casepsmt.setString(1,requestJSON.getString("remarks") );
				    casepsmt.setString(2,requestJSON.getString("mkrid") );
				    casepsmt.setString(3,requestJSON.getString("ftxt") );				    
				    casepsmt.setString(4,rnum );
				    casepsmt.setString(5,status );
				    casepsmt.setString(6,respType );
				    casepsmt.setString(7,mapto );
				    casepsmt.setString(8,requestJSON.getString("rfrom") );
				    casepsmt.setString(9,requestJSON.getString("rdate") );
				    
					int insrt=casepsmt.executeUpdate();	
					
					logger.debug("[insrt count:::" + insrt + "] acn ["+acn+"]");
					
					if (insrt>0 )
					{		
						if ("Repl".equalsIgnoreCase(acn))
						{
							logger.debug("[mobile number["+mob+"]");
							namepsmt = connection.prepareStatement(nameQry);
							namepsmt.setString(1, mob);
							nameRS = namepsmt.executeQuery(); 
						      
						      if (nameRS.next())
						      {
						    	  name = nameRS.getString(1);
						    	  this.logger.debug("name ["+name+"]");
						      }
						      
						      outMsg="Dear "+name+"! "+requestJSON.getString("ftxt");
							
							smspsmt = connection.prepareStatement(alertInsQry);
							smspsmt.setString(1,mob );
							smspsmt.setString(2,rnum );
							smspsmt.setString(3,outMsg );
						    
							int altins=smspsmt.executeUpdate();	
							
							if (altins >0 )
								logger.debug("[altins count:::" + altins + "] Alert Insertion Successfull");
							else
								logger.debug("[altins count:::" + altins + "] Alert Insertion Failed");
						}
						
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
			
			public ResponseDTO confirmReplySMSAction(RequestDTO requestDTO) {
				logger.debug("confirmRaiseSMSAction");
				responseDTO = new ResponseDTO();
				requestJSON = requestDTO.getRequestJSON();
				responseJSON = new JSONObject();
				requestJSON = requestDTO.getRequestJSON();
				
				JSONObject resultJson = null;
				
				Connection connection = null;
				String status="SR";
				String rnum=null;
				String acn="";

				HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
				PreparedStatement casepsmt = null;

				try {

					String smsInsertQry = "INSERT INTO CIC_CALL_SMS_REQ_LOG ( REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, R_REMARKS,  RAISED_BY, RAISED_DATE, "
							+ " FORMATTED_TEXT, REQ_ID, STATUS, RESP_TYPE ) select REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, ? , ? , sysdate, ?, ?, ?, ? "
							+ " from CIC_TWOWAYSMS_REQ_LOG where REQUEST_FROM=? and to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY')=? ";					
					
				    connection = connection == null ? DBConnector.getConnection() : connection;
		          	resultJson = new JSONObject();
		          	
					System.out.println("values of inputs:"+requestJSON.toString());	
					  DateFormat df = new SimpleDateFormat("ddmmss");
					    
			          Date dateobj = new Date();
			          rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
					
			          logger.debug("rnum " + rnum + "] smsInsertQry : " + smsInsertQry + "]"); 
			          
					casepsmt = connection.prepareStatement(smsInsertQry);
										
				    casepsmt.setString(1,requestJSON.getString("remarks") );
				    casepsmt.setString(2,requestJSON.getString("mkrid") );
				    casepsmt.setString(3,requestJSON.getString("ftxt") );				    
				    casepsmt.setString(4,rnum );
				    casepsmt.setString(5,status );	
				    casepsmt.setString(6,"N" );	
				    casepsmt.setString(7,requestJSON.getString("rfrom") );
				    casepsmt.setString(8,requestJSON.getString("rdate") );
				    
					int insrt=casepsmt.executeUpdate();	
					
					
					if (insrt>0 )
					{		
						logger.debug("[insrt count:::" + insrt + "]");
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
			    this.logger.debug("Inside [CallCenterDAO][getTwoWaysSMSdetails].. ");
			    
			    HashMap<String, Object> merchantMap = null;
			    JSONObject resultJson = null;
			    JSONArray merchantJsonArray = null;
			    PreparedStatement merchantPstmt = null;
			    ResultSet merchantRS = null;
			    
			    JSONObject json = null;
			    
			    String merchantQry = "select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY'), TL.REQUEST_FROM, replace(TL.REQUEST_TEXT,'''','@Q'), TL.REQUEST_TO, nvl(TL.R_REMARKS,'-'), "
			    		+ " TL.RAISED_BY, to_char(TL.RAISED_DATE,'HH:MI AM Month DD, YYYY') "
			    		+ " from CIC_CALL_SMS_REQ_LOG  TL "
			    		+ " order by TL.RAISED_DATE desc ";
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

			  public ResponseDTO smsstatus(RequestDTO requestDTO)
			  {
			    Connection connection = null;
			    this.logger.debug("Inside [CallCenterDAO][smsstatus].. ");
			    
			    HashMap<String, Object> merchantMap = null;
			    JSONObject resultJson = null;
			    JSONArray merchantJsonArray = null;
			    PreparedStatement merchantPstmt = null;
			    ResultSet merchantRS = null;
			    
			    JSONObject json = null;
			    
			    String merchantQry = 	" select status, cnt from "
			    					+ 	" ( select status,cnt, decode(status,'SA',2,'SR',1,3) ord from "
			    					+ 	" ( select status, count(*) cnt "
			    					+   " from CIC_CALL_SMS_REQ_LOG group by status)) order by ord ";
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
			      this.logger.debug("Got Exception in smsstatus [" + 
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
			  
			  public ResponseDTO smsDetSubmit(RequestDTO requestDTO)
			  {
			    Connection connection = null;
			    this.logger.debug("Inside [CallCenterDAO][smsDetSubmit].. ");
			    
			    HashMap<String, Object> merchantMap = null;
			    JSONObject resultJson = null;
			    JSONArray merchantJsonArray = null;
			    PreparedStatement merchantPstmt = null;
			    ResultSet merchantRS = null;
			    
			    JSONObject json = null;
			    String msgtxt="";
			    String rsedby="";
			    String aprvdby="";
			    String status="";
			    String mapto="";
			    
			    String merchantQry = "select to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY'), REQUEST_FROM, nvl(REQUEST_TEXT,'-'), REQUEST_TO, nvl(FORMATTED_TEXT,'-'), STATUS,"
			    		+ " RAISED_BY, APPROVED_BY, mapto "
			    		+ " from CIC_CALL_SMS_REQ_LOG where req_id=? ";
			    try
			    {
			      this.responseDTO = new ResponseDTO();
			      
			      connection = connection == null ? DBConnector.getConnection() : connection;
			      this.logger.debug("connection is [" + connection + "]");
			      
			      
			      
			      merchantMap = new HashMap();
			      resultJson = new JSONObject();
			      merchantJsonArray = new JSONArray();
			      requestJSON = requestDTO.getRequestJSON();
			      String mkrid = requestJSON.getString("mkrid");
			      this.logger.debug("mkrid ["+mkrid+"]");
			      
			      this.logger.debug("requestJSON>>"+requestJSON);
			      this.logger.debug("merchantQry ["+merchantQry +"]");
			      
			      /*msgtxt=requestJSON.getString("rdate");
			      System.out.println("msgtxt:="+msgtxt);
			      msgtxt=msgtxt.replace("@Q", "'");
			      System.out.println("replaced msgtxt:="+msgtxt);*/
			      
			      merchantPstmt = connection.prepareStatement(merchantQry);
			      merchantPstmt.setString(1, requestJSON.getString("refno"));
			      merchantRS = merchantPstmt.executeQuery();     
			      
			      json = new JSONObject();
			      while (merchantRS.next())
			      {
			    	  
			    	  logger.debug(merchantRS.getString(1)+"======>"+merchantRS.getString(2)+"=======>"+merchantRS.getString(3)+"========>"+merchantRS.getString(4));
			    	  json.put("rdate",  merchantRS.getString(1));
			          json.put("rfrom",  merchantRS.getString(2));		         
			          json.put("rtxt",  merchantRS.getString(3));
			          json.put("rto",  merchantRS.getString(4));
			          json.put("ftxt",  merchantRS.getString(5));
			          json.put("status",  merchantRS.getString(6));
			          status=merchantRS.getString(6);
			          rsedby=merchantRS.getString(7);
			          aprvdby=merchantRS.getString(8);
			          mapto=merchantRS.getString(9);
			      }
			     
			      this.logger.debug("rsedby [" + rsedby + "] aprvdby"+aprvdby+"] status ["+status+"] mapto["+mapto+"]");
			      if ("SA".equalsIgnoreCase(status))
			      {
			    	  if (rsedby.equalsIgnoreCase(mkrid) || aprvdby.equalsIgnoreCase(mkrid))
			    	  {
			    		  logger.debug("Un-Authorized User");
			    		  this.responseDTO.addError("Maker Cannot Be The Checker");
			    	  }else
			    	  {
			    		  json.put("refno",requestJSON.getString("refno")); 
					      merchantMap.put("MERCHANT_LIST", json);
					      this.logger.debug("PurMap [" + merchantMap + "]");
					      this.responseDTO.setData(merchantMap);  
			    	  }
			      }else
			      {	  
				      json.put("refno",requestJSON.getString("refno")); 
				      merchantMap.put("MERCHANT_LIST", json);
				      this.logger.debug("PurMap [" + merchantMap + "]");
				      this.responseDTO.setData(merchantMap);
			      }
			     
			     
			    }
			    catch (Exception e)
			    {
			      this.logger.debug("Got Exception in smsDetSubmit [" + 
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
			  
				public ResponseDTO smsDetAck(RequestDTO requestDTO) {
					logger.debug("smsDetAck");
					responseDTO = new ResponseDTO();
					requestJSON = requestDTO.getRequestJSON();
					responseJSON = new JSONObject();
					requestJSON = requestDTO.getRequestJSON();
					
					JSONObject resultJson = null;
					
					Connection connection = null;
					String status="";
					String mob="";
					//String rnum=null;

					HashMap<String, Object> caseDataMap = new HashMap<String, Object>();
					PreparedStatement casepsmt = null;					
					PreparedStatement smspsmt = null;
					PreparedStatement smsClnUpdpsmt = null;

					try {

/*						String smsInsertQry = "INSERT INTO CIC_CALL_SMS_REQ_LOG ( REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, R_REMARKS,  RAISED_BY, RAISED_DATE, "
								+ " FORMATTED_TEXT, REQ_ID, STATUS ) select REQUESTED_DATE, REQUEST_FROM, REQUEST_TEXT, REQUEST_TO, ? , ? , sysdate, ?, ?, ? "
								+ " from CIC_TWOWAYSMS_REQ_LOG where REQUEST_FROM=? and to_char(REQUESTED_DATE,'HH:MI AM Month DD, YYYY')=? ";*/
						
						status = requestJSON.getString("status");
						mob = requestJSON.getString("rfrom");
						mob = mob.substring(1);
						
						logger.debug("values of status:"+status+"[ mob :"+mob+"]");
						
						if ("SR".equalsIgnoreCase(status))
						{
	
							String smsUpdateQry = "UPDATE CIC_CALL_SMS_REQ_LOG set status='SA', A_REMARKS=?, APPROVED_BY=?, APPROVED_DATE=sysdate where req_id=? ";
							
						    connection = connection == null ? DBConnector.getConnection() : connection;
				          	resultJson = new JSONObject();
				          	
				          	logger.debug("values of inputs:"+requestJSON.toString());						
							
							casepsmt = connection.prepareStatement(smsUpdateQry);
							casepsmt.setString(1, requestJSON.getString("remarks"));
							casepsmt.setString(2, requestJSON.getString("mkrid"));
							casepsmt.setString(3, requestJSON.getString("refno"));
							int upStmt=casepsmt.executeUpdate();
							
							
							if (upStmt>0 )
							{		
								logger.debug("[insrt count:::" + upStmt + "]");
							resultJson.put("result", "Approved Successfully");
							}else
								resultJson.put("result", "Approval Failed");
						}else if ("SA".equalsIgnoreCase(status))
						{
							
							String smsUpdateQry = "UPDATE CIC_CALL_SMS_REQ_LOG set status='SC', C_REMARKS=?, COMPLETED_BY=?, COMPLETED_DATE=sysdate where req_id=? ";
							
							String smsInsertQry = "INSERT INTO CIC_CLIENTS_HIS ( CLIENT_ID,DATE_CREATED,DATE_OF_BIRTH,EMAIL,FNAME,GENDER,ID_NUMBER,ID_TYPE,INSERTED_DATE,INSTERD_BY,"
									+ " IPRS_DESC,LNAME,MNAME,MOBILE_NUMBER,PHOTO,POSTAL_ADDRESS,POSTAL_CODE,SALUTATION,SIGNATURE,STATUS,VALIDATION_CNT,INVALID_CNT ) "
									+ " select CLIENT_ID,DATE_CREATED,DATE_OF_BIRTH,EMAIL,FNAME,GENDER,ID_NUMBER,ID_TYPE,sysdate,?,IPRS_DESC,LNAME,MNAME,MOBILE_NUMBER,"
									+ " PHOTO,POSTAL_ADDRESS,POSTAL_CODE,SALUTATION,SIGNATURE,STATUS,VALIDATION_CNT,INVALID_CNT from cic_clients where mobile_number=?";
							
							String smsClnRestUpdQry = "update CIC_CLIENTS set ID_NUMBER=?, STATUS='CC',IPRS_DESC='', VALIDATION_CNT=1,INVALID_CNT=0 WHERE MOBILE_NUMBER= ? and status='CVF' ";
								
						    connection = connection == null ? DBConnector.getConnection() : connection;
				          	resultJson = new JSONObject();
				          	
				          	logger.debug("values of inputs:"+requestJSON.toString());	
				          	
				          	smspsmt = connection.prepareStatement(smsInsertQry);
				          	smspsmt.setString(1,requestJSON.getString("mkrid") );
				          	smspsmt.setString(2,mob );
							int insrt=smspsmt.executeUpdate();						
							
							System.out.println(insrt + " rows inserted");
							
							smsClnUpdpsmt = connection.prepareStatement(smsClnRestUpdQry);
							smsClnUpdpsmt.setString(1, requestJSON.getString("ftxt"));
							smsClnUpdpsmt.setString(2, mob);
							int cupd=smsClnUpdpsmt.executeUpdate();
							
							System.out.println(cupd + " rows updated");
							
							if (insrt == 1 & cupd == 1 )
							{
							
								casepsmt = connection.prepareStatement(smsUpdateQry);
								casepsmt.setString(1, requestJSON.getString("remarks"));
								casepsmt.setString(2, requestJSON.getString("mkrid"));
								casepsmt.setString(3, requestJSON.getString("refno"));
								int upStmt=casepsmt.executeUpdate();
								
								
								if (upStmt>0 )
								{		
									logger.debug("[insrt count:::" + upStmt + "]");
								resultJson.put("result", "ID Details Updated Successfully");
								}else
									resultJson.put("result", "Failed");								
							}
							else
							{
								resultJson.put("result", "Failed");	
							}
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
				
	  public ResponseDTO directFailedTxns(RequestDTO requestDTO)
	  {
	    Connection connection = null;
	    this.logger.debug("Inside [CallCenterDAO][getFailedTxns].. ");
	    
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    JSONArray merchantJsonArray = null;
	    PreparedStatement merchantPstmt = null;
	    ResultSet merchantRS = null;
	    
	    JSONObject json = null;
	    
	    /*String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, AMOUNT, STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
	    		+ " where  STATUS='FAILED' order by DATE_CREATED desc";*/
	    
	    String merchantQry = " select * from "
	    		+ " (SELECT TO_CHAR(CA.DATE_CREATED,'HH:MI AM MONTH DD, YYYY') , CA.AIRTIME_ID, CA.POLICY_ID, round(CA.AMOUNT,2), CA.STATUS, "
	    		+ " SUBSTR(CB.REQUEST_DATA,(INSTR(CB.REQUEST_DATA,'providerRefId',1,1)+16),((INSTR(CB.REQUEST_DATA,'providerMetadata',1,1)-3)-(INSTR(CB.REQUEST_DATA,'providerRefId',1,1)+16))) MPID , "
	    		+ " CA.DATE_CREATED DT,CB.C2B_LOG_ID FROM CIC_C2B_AIRTIME_LOG CA, CIC_C2B_LOG CB  WHERE CA.STATUS='FAILED' and CA.C2B_LOG_ID=CB.C2B_LOG_ID) order by DT desc" ;
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
	  
	  public ResponseDTO directForceTopUpDet(RequestDTO requestDTO)
	  {
	    Connection connection = null;
	    this.logger.debug("Inside [CallCenterDAO][getForceTopUpDet].. ");
	    
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    JSONArray merchantJsonArray = null;
	    PreparedStatement merchantPstmt = null;
	    ResultSet merchantRS = null;
	    
	    JSONObject json = null;
	    String rsedby="";
	    String aprvdby="";
	    
	    String merchantQry = " select * from "
	    		+ " (SELECT TO_CHAR(CA.DATE_CREATED,'HH:MI AM MONTH DD, YYYY') , CA.AIRTIME_ID, CA.POLICY_ID, round(CA.AMOUNT,2), CA.STATUS, CA.REQUEST_ID, "
	    		+ " CA.DATE_CREATED DT FROM CIC_C2B_AIRTIME_LOG CA, CIC_C2B_LOG CB  WHERE CA.STATUS='FAILED' and CA.C2B_LOG_ID=CB.C2B_LOG_ID and "
	    		+ " CB.C2B_LOG_ID=?) order by DT desc" ;
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
	      merchantPstmt.setString(1, requestJSON.getString("refno"));
	      /*merchantPstmt.setString(2, requestJSON.getString("rid"));*/
	      merchantRS = merchantPstmt.executeQuery();     
	      
	      json = new JSONObject();
	      while (merchantRS.next())
	      {
	          json.put("cdate",  merchantRS.getString(1));
	          json.put("cid",  merchantRS.getString(2));
	          json.put("mob",  merchantRS.getString(3));
	          json.put("amt",  merchantRS.getString(4));
	          json.put("status",  merchantRS.getString(5));
	          json.put("arid",  merchantRS.getString(6));
	          json.put("rid",  requestJSON.getString("rid")); 
	          json.put("refno", requestJSON.getString("refno")); 
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
	  		  
	  public ResponseDTO directForceTopUp(RequestDTO requestDTO)
	  {
	    Connection connection = null;
	    this.logger.debug("Inside [CallCenterDAO][directForceTopUp].. ");
	    
	    HashMap<String, Object> merchantMap = null;
	    JSONObject resultJson = null;
	    JSONArray merchantJsonArray = null;
	    PreparedStatement merchantPstmt = null;
	    ResultSet merchantRS = null;
	    
	    JSONObject json = null;
	    String rsedby="";
	    String aprvdby="";
	    PreparedStatement topUpsmt = null;
	    
	    String merchantQry = "select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'), AIRTIME_ID, POLICY_ID, rouond(AMOUNT,2), STATUS, REQUEST_ID from CIC_C2B_AIRTIME_LOG"
	    		+ " where  C2B_LOG_ID=?";
	    String insDirectTopUpQry = "insert into direct_force_topup_det (mobile_no,amount,request_id,c2b_req_id,airtime_date,topup_date,topup_by) "
	    		+ " values (?,?,?,?,to_date(?,'HH:MI AM Month DD, YYYY'),sysdate,?)";
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
	      String refno = requestJSON.getString("refno");
	      String arid = requestJSON.getString("arid");
	      String mkrid = requestJSON.getString("mkrid");
	      
	      
	      //mob amt rid
	      logger.debug("Force TopUp URL Params - request id ["
					+ arid + "] old mob [" + mob + "] amt["
					+ amt + "] airtimeref id [" + cid + "] refno [" + refno +"]");

	      //Production IP : 52.166.1.232
	      //Test IO : 104.42.234.123
	      HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = "http://104.42.234.123:1234/cicinsurancecore/rest/cicinsrest/forceTopUp"
					+ "?mobileNumber=" + mob + "&amount=" + amt + "&existingRequestId="
					+ arid ;
	
			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "]");
			String responseCode = obj.getString("RESPONSE_CODE");
			String responseMsg = obj.getString("RESPONSE_MSG");
			
			logger.debug("Response Code:"+responseCode);
			logger.debug("Response Msg:"+responseMsg);
			
			json = new JSONObject();			
			json.put("result", responseMsg);
			
			if ("00".equalsIgnoreCase(responseCode))
			{
	
				topUpsmt = connection.prepareStatement(insDirectTopUpQry);
				topUpsmt.setString(1,mob );
				topUpsmt.setString(2,amt );
				topUpsmt.setString(3,arid );
				topUpsmt.setString(4,rid );
				topUpsmt.setString(5,cdate );
				topUpsmt.setString(6,mkrid );
				int insrt=topUpsmt.executeUpdate();	
			}
	      
	      merchantMap.put("MERCHANT_LIST", json);
	      this.logger.debug("PurMap [" + merchantMap + "]");
	      this.responseDTO.setData(merchantMap);
	     
	     
	    }
	    catch (Exception e)
	    {
	      this.logger.debug("Got Exception in directForceTopUp [" + 
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

}
