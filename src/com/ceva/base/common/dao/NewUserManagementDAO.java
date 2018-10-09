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

public class NewUserManagementDAO
{
  private Logger logger = Logger.getLogger(NewUserManagementDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  
  public ResponseDTO fetchUserGroupDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewUserManagementDAO][GetMerchantDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "SELECT GROUP_ID,GROUP_NAME,MAKER_ID,to_char(MAKER_DTTM,'HH:MI AM Month DD, YYYY') FROM  USER_GROUPS WHERE APPL_CODE='AgencyBanking' ORDER BY MAKER_DTTM";
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      
      userPstmt = connection.prepareStatement(merchantQry);
      userRS = userPstmt.executeQuery();
      
      json = new JSONObject();
      while (userRS.next())
      {
        json.put("GROUP_ID", 
          userRS.getString(1));
        json.put("GROUP_NAME", 
          userRS.getString(2));
        json.put("MAKER_ID", userRS.getString(3));
        json.put("MAKER_DTTM", 
          userRS.getString(4));
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
  
  public ResponseDTO fetchUsersList(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewUserManagementDAO][getStoreDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select A.LOGIN_USER_ID,B.USER_NAME,B.EMAIL ,decode(B.USER_STATUS,'A','Active','L','De-Active','F','InActive','N','Un-Authorize',B.USER_STATUS) from USER_LOGIN_CREDENTIALS A,USER_INFORMATION B WHERE A.COMMON_ID=B.COMMON_ID AND UPPER(B.USER_GROUPS) =?";
    try
    {
      this.requestJSON = requestDTO.getRequestJSON();
      
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      
      userPstmt = connection.prepareStatement(merchantQry);
      
      userPstmt.setString(1, this.requestJSON.getString("GROUP_ID"));
      
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        json = new JSONObject();
        json.put("LOGIN_USER_ID",  userRS.getString(1));
        json.put("GROUP_ID",  requestJSON.getString("GROUP_ID"));
        
        String[] data = userRS.getString(2).split("\\ ");
        String fName = "";
        String lName = "";
        if (data.length == 1)
        {
          fName = data[0];
        }
        else
        {
          fName = data[0];
          lName = data[1];
        }
        
        json.put("FNAME",  fName);
        json.put("LNAME",  lName);
        
        
        json.put("EMAIL", 
          userRS.getString(3));
        json.put("USER_STATUS", 
          userRS.getString(4));
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
  
  public ResponseDTO fetchUserRights(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewUserManagementDAO][fetchUserRights].. ");
    
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
      
      
      merchantQry = "select count(*) from user_linked_action where upper(user_id)=?";
      
      userPstmt = connection.prepareStatement(merchantQry);
      userPstmt.setString(1, userId.toUpperCase());
      userRS = userPstmt.executeQuery();
      int count=0;
      if (userRS.next()) {
        count = userRS.getInt(1);
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      
      if (count > 0) {
        merchantQry = "select NAME from user_linked_action where user_id=? order by id ";
      } else {
        merchantQry = "select NAME from user_linked_action where  GROUP_ID in (select user_groups from user_information where common_id in (select common_id from user_login_credentials where upper(login_user_id)=?)) and user_id is null order by id";
      }
      
      userPstmt = connection.prepareStatement(merchantQry);
      userPstmt.setString(1, userId.toUpperCase());
      userRS = userPstmt.executeQuery();
      while (userRS.next()) {
          rights = rights + userRS.getString(1) + ",";
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      
      json = new JSONObject();
          json.put("name", rights.substring(0, rights.lastIndexOf(",")));
          json.put("user_id", userId);
          rights = "";
      useerJSONArray.add(json);
      
      logger.debug("Rights json::::"+json);
      
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("RIGHTS_LIST", useerJSONArray);
      merchantMap.put("RIGHTS_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in fetchUserRights [" + 
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
}
