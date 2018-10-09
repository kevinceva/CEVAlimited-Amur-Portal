package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class NewMerchantDAO
{
  private Logger logger = Logger.getLogger(NewMerchantDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  
  public ResponseDTO getMerchantDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewMerchantDAO][GetMerchantDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    ResultSet merchantRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select MERCHANT_ID,MERCHANT_NAME,Decode(STATUS,'A','Active','B','Inactive','N','Un-Authorize'),to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from MERCHANT_MASTER order by MERCHANT_ID";
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      merchantJsonArray = new JSONArray();
      
      merchantPstmt = connection.prepareStatement(merchantQry);
      merchantRS = merchantPstmt.executeQuery();
      
      json = new JSONObject();
      while (merchantRS.next())
      {
        json.put("merchantID", 
          merchantRS.getString(1));
        json.put("merchantName", 
          merchantRS.getString(2));
        json.put("status", merchantRS.getString(3));
        json.put("makerDate", 
          merchantRS.getString(4));
        merchantJsonArray.add(json);
        json.clear();
      }
      DBUtils.closeResultSet(merchantRS);
      DBUtils.closePreparedStatement(merchantPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("MERCHANT_LIST", merchantJsonArray);
      merchantMap.put("MERCHANT_LIST", resultJson);
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
      DBUtils.closeResultSet(merchantRS);
      DBUtils.closePreparedStatement(merchantPstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      merchantJsonArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO getStoreDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewMerchantDAO][getStoreDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    ResultSet merchantRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select SM.STORE_ID,SM.STORE_NAME,MM.MERCHANT_ID,MM.MERCHANT_NAME,Decode(SM.STATUS,'A','Active','B','Inactive','N','Un-Authorize'),to_char(SM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from MERCHANT_MASTER MM,STORE_MASTER SM where SM.MERCHANT_ID=MM.MERCHANT_ID and MM.MERCHANT_ID=?  order by SM.STORE_ID";
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
      
      merchantPstmt.setString(1, this.requestJSON.getString("merchantID"));
      
      merchantRS = merchantPstmt.executeQuery();
      while (merchantRS.next())
      {
        json = new JSONObject();
        json.put("storeId", 
          merchantRS.getString(1));
        json.put("storeName", 
          merchantRS.getString(2));
        json.put("merchantID", 
          merchantRS.getString(3));
        json.put("merchantName", 
          merchantRS.getString(4));
        json.put("status", 
          merchantRS.getString(5));
        json.put("makerDate", 
          merchantRS.getString(6));
        merchantJsonArray.add(json);
      }
      DBUtils.closeResultSet(merchantRS);
      DBUtils.closePreparedStatement(merchantPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("STORE_LIST", merchantJsonArray);
      merchantMap.put("STORE_LIST", resultJson);
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
      DBUtils.closeResultSet(merchantRS);
      DBUtils.closePreparedStatement(merchantPstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      merchantJsonArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO getTerminalDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewMerchantDAO][getTerminalDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray merchantJsonArray = null;
    PreparedStatement merchantPstmt = null;
    ResultSet merchantRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select TERMINAL_ID,STORE_ID,MERCHANT_ID,Decode(STATUS,'A','Active','B','Inactive','D','Deactive',STATUS),to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS'),SERIAL_NO from TERMINAL_MASTER  where trim(STORE_ID)=trim(?)";
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
      
      merchantPstmt.setString(1, this.requestJSON.getString("storeId"));
      
      merchantRS = merchantPstmt.executeQuery();
      while (merchantRS.next())
      {
        json = new JSONObject();
        json.put("terminalID", merchantRS.getString(1));
        json.put("storeId", merchantRS.getString(2));
        json.put("merchantID", merchantRS.getString(3));
        json.put("status", merchantRS.getString(4));
        json.put("makerDate", merchantRS.getString(5));
        json.put("serialNo", merchantRS.getString(6));
        merchantJsonArray.add(json);
      }
      DBUtils.closeResultSet(merchantRS);
      DBUtils.closePreparedStatement(merchantPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("TERMINAL_LIST", merchantJsonArray);
      merchantMap.put("TERMINAL_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in getTerminalDetails [" + 
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
