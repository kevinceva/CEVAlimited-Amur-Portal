package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
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

import org.apache.log4j.Logger;

public class DashBoardLinkDAO
{
  private Logger logger = Logger.getLogger(DashBoardLinkDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  
  public ResponseDTO getDashBoardLinks(RequestDTO requestDTO)
  {
    HashMap<String, Object> resultMap = null;
    JSONArray userJSONArray = null;
    
    this.logger.debug("Inside GetDashBoardLinks... ");
    
    Connection connection = null;
    ResultSet userRS = null;
    PreparedStatement userPstmt = null;
    
    JSONObject json = null;
    
    String userQry = "SELECT ORDER_REF_NO,DASHBOARD_LABLE FROM DASHBOARD_MASTER WHERE GROUP_ID='DASHBOARD' and ORDER_REF_NO not in (select ORDER_REF_NO FROM DASHBOARD_USER_GROUP WHERE GROUP_ID=? and USER_ID=?)  order by to_number(ORDER_REF_NO)";
    String userQry1 = "SELECT distinct ORDER_REF_NO,DASHBOARD_LABLE FROM DASHBOARD_USER_GROUP WHERE GROUP_ID=? and USER_ID=? order by to_number(ORDER_REF_NO) ";
    String userQry2 = "select NVL(GROUP_ID,' '),NVL(GROUP_NAME,' ') from user_groups where GROUP_ID=?";
    try
    {
      this.responseJSON = new JSONObject();
      this.responseDTO = new ResponseDTO();
      this.requestJSON = requestDTO.getRequestJSON();
      
      this.logger.debug("Request JSON [" + this.requestJSON + "]");
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      
      this.logger.debug("Connection is [" + connection + "]");
      resultMap = new HashMap();
      userJSONArray = new JSONArray();
      json = new JSONObject();
      
      userPstmt = connection.prepareStatement(userQry);
      userPstmt.setString(1, 
        this.requestJSON.getString("roleGroupId"));
      userPstmt.setString(2, this.requestJSON.getString("USER_ID"));
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        json.put("key", userRS.getString(1) + 
          "-" + userRS.getString(2));
        json.put("val", userRS.getString(1) + 
          "-" + userRS.getString(2));
        userJSONArray.add(json);
        json.clear();
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      

      userPstmt = connection.prepareStatement(userQry1);
      userPstmt.setString(1, 
        this.requestJSON.getString("roleGroupId"));
      userPstmt.setString(2, this.requestJSON.getString("USER_ID"));
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        json.put("key", userRS.getString(1) + 
          "-" + userRS.getString(2));
        json.put("val", userRS.getString(1) + 
          "-" + userRS.getString(2));
        userJSONArray.add(json);
        json.clear();
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      

      userPstmt = connection.prepareStatement(userQry2);
      userPstmt.setString(1, 
        this.requestJSON.getString("roleGroupId"));
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        this.responseJSON.put("roleGroupId", 
          userRS.getString(1));
        this.responseJSON.put("roleGroupName", 
          userRS.getString(2));
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      

      this.responseJSON.put("dash_main", userJSONArray);
      this.responseJSON
        .put("dash_user_grp", userJSONArray);
      
      resultMap.put("responseJSON", this.responseJSON);
      this.responseDTO.setData(resultMap);
    }
    catch (SQLException e)
    {
      this.logger.debug("SQLException is  InsertDashBoardLinks [" + 
        e.getMessage() + "]");
      this.responseDTO.addError("Internal Error Occured.");
      e.printStackTrace();
    }
    catch (Exception e)
    {
      this.logger.debug("Exception is  InsertDashBoardLinks [" + 
        e.getMessage() + "]");
      this.responseDTO.addError("Internal Error Occured.");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
    }
    return this.responseDTO;
  }
  
  public ResponseDTO insertDashBoardLinks(RequestDTO requestDTO)
  {
    Connection connection = null;
    
    this.logger.debug("Inside InsertDashBoardLinks... ");
    
    String makerId = "";
    
    CallableStatement callableStatement = null;
    String insertBankInfoProc = "{call DashBoardPkg.dashBoardInsert(?,?,?,?,?)}";
    try
    {
      this.responseDTO = new ResponseDTO();
      this.requestJSON = requestDTO.getRequestJSON();
      this.logger.debug("Request JSON [" + this.requestJSON + "]");
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      
      makerId = this.requestJSON.getString("makerId");
      
      callableStatement = connection.prepareCall(insertBankInfoProc);
      callableStatement.setString(1, makerId);
      callableStatement.setString(2, 
        this.requestJSON.getString("roleGroupId"));
      callableStatement.setString(3, 
        this.requestJSON.getString("transactionData"));
      callableStatement.setString(4, this.requestJSON.getString("user_id"));
      callableStatement.registerOutParameter(5, 4);
      callableStatement.executeUpdate();
      int resCnt = callableStatement.getInt(5);
      
      this.logger.debug("ResultCnt from DB [" + resCnt + "]");
      if (resCnt == 1) {
        this.responseDTO.addMessages("DashBoard Links Assigned to Group Successfully. ");
      } else if (resCnt == -1) {
        this.responseDTO.addError("Bank Information Already Exists. ");
      } else {
        this.responseDTO.addError("Bank Information Creation failed.");
      }
    }
    catch (SQLException e)
    {
      this.logger.debug("SQLException is  InsertDashBoardLinks [" + 
        e.getMessage() + "]");
      this.responseDTO.addError("Internal Error Occured.");
    }
    catch (Exception e)
    {
      this.logger.debug("Exception is  InsertDashBoardLinks [" + 
        e.getMessage() + "]");
      this.responseDTO.addError("Internal Error Occured.");
    }
    finally
    {
      DBUtils.closeCallableStatement(callableStatement);
      DBUtils.closeConnection(connection);
    }
    return this.responseDTO;
  }
  
  public ResponseDTO getAssinedDashLinks(RequestDTO requestDTO)
  {
    this.logger.debug("Inside GetAssinedDashLinks.. ");
    
    Connection connection = null;
    CallableStatement callableStmt = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    HashMap<String, Object> resultMap = null;
    
    JSONArray userJSONArray = null;
    JSONObject resultJson = null;
    JSONObject userJson = null;
    
    String userQry1 = "SELECT distinct ORDER_REF_NO,DASHBOARD_LABLE FROM DASHBOARD_USER_GROUP WHERE GROUP_ID=? and USER_ID=? order by to_number(ORDER_REF_NO)";
    



    String halfpageQry = "{call DashBoardPkg.getHalfPage(?,?,?,?,?)}";
    String dashboarditem = "{call DashBoardPkg.getDashResult(?,?,?,?)}";
    String maker_id = "";
    String group_id = "";
    String locationName = "";
    
    String message = "";
    //String userFloatQry = "{call DashBoardPkg.getFloatData(?,?,?)}";
    try
    {
      this.responseDTO = new ResponseDTO();
      this.requestJSON = requestDTO.getRequestJSON();
      this.responseJSON = new JSONObject();
      if (this.requestJSON.containsKey("makerId")) {
        maker_id = this.requestJSON.getString("makerId");
      }
      if (this.requestJSON.containsKey("roleGroupId")) {
        group_id = this.requestJSON.getString("makerId");
      }
      if (this.requestJSON.containsKey("loc_name")) {
        locationName = this.requestJSON.getString("loc_name");
      }
      this.logger.debug(" Request JSON in DashBoard DAO [" + this.requestJSON + "]");
      userJSONArray = new JSONArray();
      userJson = new JSONObject();
      resultJson = new JSONObject();
      resultMap = new HashMap();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      try
      {
        userPstmt = connection.prepareStatement(userQry1);
        userPstmt.setString(1, 
          this.requestJSON.getString("roleGroupId"));
        userPstmt.setString(2, maker_id);
        userRS = userPstmt.executeQuery();
        while (userRS.next())
        {
          callableStmt = connection.prepareCall(dashboarditem);
          callableStmt.setString(1, maker_id);
          callableStmt.setString(2, userRS.getString(1));
          callableStmt.registerOutParameter(3, 12);
          callableStmt.registerOutParameter(4, 4);
          
          callableStmt.execute();
          String data = callableStmt.getString(3);
          
          DBUtils.closeCallableStatement(callableStmt);
          
          String[] ar = data.split("#");
          if (ar.length == 2)
          {
            userJson.put("key", ar[0]);
            userJson.put("val", ar[1]);
          }
          userJSONArray.add(userJson);
          userJson.clear();
          


          this.responseJSON.put("dashboarddata", userJSONArray);
          resultMap.put("dashboarddata", 
            this.responseJSON);
          ar = null;
        }
        DBUtils.closeResultSet(userRS);
        DBUtils.closePreparedStatement(userPstmt);
      }
      catch (Exception e)
      {
        this.logger.debug("Exception In Getting DashboardLabel [" + 
          e.getMessage() + "]");
        DBUtils.closeResultSet(userRS);
        DBUtils.closePreparedStatement(userPstmt);
        DBUtils.closeCallableStatement(callableStmt);
      }
      DBUtils.closeResultSet(userRS);
      DBUtils.closePreparedStatement(userPstmt);
      
      userJSONArray.clear();
      this.logger.debug("Dashboard Link's Loaded Into Array.");
     /* try
      {
        callableStmt = connection.prepareCall(halfpageQry);
        callableStmt.setString(1, maker_id);
        callableStmt.setString(2, group_id);
        callableStmt.setString(3, locationName);
        callableStmt.registerOutParameter(4, -10);
        callableStmt.registerOutParameter(5, 12);
        
        callableStmt.execute();
        this.logger.debug(" block executed successfully with error_message[" + 
          callableStmt.getString(5) + 
          "]");
        
        userRS = (ResultSet)callableStmt.getObject(4);
        
        this.logger.debug(Boolean.valueOf("Result Set is not null [" + userRS == null + "]"));
        while (userRS.next())
        {
          userJson.put("SERVICE", userRS.getString(1));
          userJson.put("TXN_DESC", userRS.getString(2));
          userJson.put("CARD_NO", userRS.getString(3));
          userJson.put("ServiceAccount", userRS.getString(4));
          userJson.put("channel", userRS.getString(5));
          userJson.put("mobile_no", userRS.getString(6));
          userJson.put("rrn", userRS.getString(7));
          userJson.put("txnrefno", userRS.getString(8));
          userJson.put("TXN_DATE_TIME", userRS.getString(9));
          userJson.put("TXNAMT", userRS.getString(10));
          userJson.put("MAKERID", userRS.getString(11));
          userJson.put("Branch", userRS.getString(12));
          userJson.put("status", userRS.getString(13));
          userJson.put("posrrn", userRS.getString(14));
          userJson.put("responseCode", userRS.getString(15));
          userJSONArray.add(userJson);
          userJson.clear();
        }
        DBUtils.closeResultSet(userRS);
        DBUtils.closeCallableStatement(callableStmt);
      }
      catch (Exception e)
      {
        this.logger.debug("Exception in getting Live Data ::: " + 
          e.getMessage());
        
        DBUtils.closeResultSet(userRS);
        DBUtils.closeCallableStatement(callableStmt);
      }*/
      //resultJson.put("halfpagedata", userJSONArray);
      
      //this.logger.debug("Live Transactions Loaded Into Array.");
      
      //userJSONArray.clear();
     /* try
      {
        halfpageQry = "{call DashBoardPkg.getAnnouncement(?,?,?,?)}";
        callableStmt = connection.prepareCall(halfpageQry);
        callableStmt.setString(1, maker_id);
        callableStmt.registerOutParameter(2, 12);
        callableStmt.registerOutParameter(3, 12);
        callableStmt.registerOutParameter(4, 2);
        callableStmt.execute();
        
        this.logger.debug("Announcement block executed successfully with Error Message[" + 
          callableStmt.getInt(4) + "]");
        
        resultJson.put("USER_ANNOUNCEMENT", callableStmt.getString(2));
        resultJson.put("GROUP_ANNOUNCEMENT", callableStmt.getString(3));
        DBUtils.closeCallableStatement(callableStmt);
      }
      catch (Exception e)
      {
        this.logger.debug("Exception In Getting AnnouncementData ::: " + 
          e.getMessage());
        DBUtils.closeCallableStatement(callableStmt);
      }
      userJSONArray.clear();
      
      this.logger.debug("Announcement Loaded Into Array.");*/
     /* try
      {
        callableStmt = connection.prepareCall(userFloatQry);
        callableStmt.setString(1, maker_id);
        callableStmt.registerOutParameter(2, -10);
        callableStmt.registerOutParameter(3, 12);
        
        callableStmt.execute();
        message = callableStmt.getString(3);
        
        this.logger.debug("Message from DB Float [" + message + "]");
        if (message != null) {
          if (!message.equalsIgnoreCase("NO"))
          {
            userRS = (ResultSet)callableStmt.getObject(2);
            if ((message.equalsIgnoreCase("USER")) && (userRS != null))
            {
              while (userRS.next())
              {
                userJson.put("mer_id", userRS.getString(1));
                userJson.put("str_id", userRS.getString(2));
                userJson.put("term_id", userRS.getString(3));
                userJson.put("term_lmt", userRS.getString(4));
                userJson.put("term_curr_lmt", userRS.getString(5));
                userJson.put("channel", userRS.getString(6));
                userJson.put("serialno", userRS.getString(7));
                userJSONArray.add(userJson);
                userJson.clear();
              }
              DBUtils.closeResultSet(userRS);
              DBUtils.closeCallableStatement(callableStmt);
            }
            else if ((message.equalsIgnoreCase("BRANCH")) && 
              (userRS != null))
            {
              while (userRS.next())
              {
                userJson.put("mer_id", userRS.getString(1));
                userJson.put("str_id", userRS.getString(2));
                userJson.put("str_dpt_lmt", userRS.getString(3));
                userJson.put("str_curr_amt", userRS.getString(4));
                userJson.put("curr_csh_dtp_lmt", 
                  userRS.getString(5));
                userJson.put("rec_amt", userRS.getString(6));
                userJson.put("channel", userRS.getString(7));
                userJson.put("tot_cdp_amt", userRS.getString(8));
                userJson.put("tot_wdl_amt", userRS.getString(9));
                userJson.put("cash_allow", userRS.getString(10));
                userJson.put("unall_amt", userRS.getString(11));
                userJSONArray.add(userJson);
                userJson.clear();
              }
              DBUtils.closeResultSet(userRS);
              DBUtils.closeCallableStatement(callableStmt);
            }
            else
            {
              userJSONArray = new JSONArray();
              DBUtils.closeCallableStatement(callableStmt);
            }
            resultJson.put("FLOAT_DATA", userJSONArray);
            resultJson.put("FLOAT_MSG", message);
          }
          else
          {
            this.responseDTO.addError("Float Data Not Found.");
          }
        }
        DBUtils.closeCallableStatement(callableStmt);
      }
      catch (Exception e)
      {
        this.logger.debug("Exception in getting Float Data For User OR Branch Manager ::: " + 
          e.getMessage());
        e.printStackTrace();
        DBUtils.closeResultSet(userRS);
        DBUtils.closeCallableStatement(callableStmt);
      }
      finally
      {
        DBUtils.closeResultSet(userRS);
        DBUtils.closeCallableStatement(callableStmt);
        DBUtils.closeConnection(connection);
      }
      this.logger.debug("Float Data Loaded Into Array.");*/
      
      this.logger.debug("Result Json is  [" + resultJson + "]");
      resultMap.put("halfpagedata", resultJson);
      this.responseDTO.setData(resultMap);
    }
    catch (Exception e)
    {
      this.logger.debug("The Exception is   [" + e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(userRS);
      DBUtils.closeCallableStatement(callableStmt);
      DBUtils.closePreparedStatement(userPstmt);
      DBUtils.closeConnection(connection);
      
      resultMap = null;
      userJSONArray = null;
      resultJson = null;
      userQry1 = null;
      halfpageQry = null;
      dashboarditem = null;
      maker_id = null;
      group_id = null;
      locationName = null;
    }
    return this.responseDTO;
  }
}
