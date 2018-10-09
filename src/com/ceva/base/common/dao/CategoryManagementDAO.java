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
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.json.JSONException;

public class CategoryManagementDAO
{
  private Logger logger = Logger.getLogger(CategoryManagementDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  
  public ResponseDTO fetchCategoryDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [CategoryManagementDAO][fetchCategoryDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = " SELECT  cm.CATEGORY_NAME , cm.CATEGORY_DESC , "
    		+ " to_char(cm.created_date,'dd/mm/yyyy')  , cm.CATEGORY_ID  from  CATEGORY_MASTER cm "
    		+ " order by cm.CATEGORY_NAME    ";
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      System.out.println("merchantQry ["+merchantQry+"]");
      userPstmt = connection.prepareStatement(merchantQry);
      userRS = userPstmt.executeQuery();
      
      json = new JSONObject();
      while (userRS.next())
      {
        json.put("CAT_NAME", 
          userRS.getString(1));
        json.put("CAT_DESC", 
          userRS.getString(2));
        json.put("CREATE_DT", userRS.getString(3));
        json.put("CAT_ID", userRS.getString(4));
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
      this.logger.debug("Got Exception in fetchCategoryDetails [" + 
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
  
  public ResponseDTO fetchSubCatList(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewUserManagementDAO][getStoreDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select SUB_CATEGORY_NAME, SUB_CATEGORY_DESC , to_char(created_date,'dd/mm/yyyy'), SUB_CATEGORY_ID from SUB_CATEGORY_MASTER where CATEGORY_ID=? order by SUB_CATEGORY_NAME ";
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
      System.out.println("cat_id ["+this.requestJSON.getString("cat_id")+"]");
      userPstmt.setString(1, this.requestJSON.getString("cat_id"));
      
      userRS = userPstmt.executeQuery();
      while (userRS.next())
      {
        json = new JSONObject();
        json.put("sub_cat_name",  userRS.getString(1));
        json.put("cat_id",  requestJSON.getString("cat_id"));        
        json.put("sub_cat_desc",userRS.getString(2));
        json.put("sub_cat_crt_dt", userRS.getString(3));
        json.put("sub_cat_id", userRS.getString(4));
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
  
  public ResponseDTO confirmCategoryDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [CategoryManagementDAO][confirmCategoryDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray useerJSONArray = null;
    PreparedStatement userPstmt = null;
    ResultSet userRS = null;
    
    JSONObject json = null;
    
    String merchantQry = " SELECT  cm.CATEGORY_NAME , cm.CATEGORY_DESC , "
    		+ " to_char(cm.created_date,'dd/mm/yyyy')  , cm.CATEGORY_ID  from  CATEGORY_MASTER cm "
    		+ " order by cm.CATEGORY_NAME    ";
    try
    {
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      useerJSONArray = new JSONArray();
      /* System.out.println("merchantQry ["+merchantQry+"]");
      userPstmt = connection.prepareStatement(merchantQry);
      userRS = userPstmt.executeQuery();*/
      
      json = new JSONObject();
      
      json.put("catname",requestJSON.getString("catname"));
      json.put("catdesc",requestJSON.getString("catdesc"));
      useerJSONArray.add(json);
      
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
      this.logger.debug("Got Exception in confirmCategoryDetails [" + 
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
  
  
	public ResponseDTO viewUserGroup(RequestDTO requestDTO) {

		logger.debug("Inside  ViewUserGroup... ");

		HashMap<String, Object> merchantMap = null;
		JSONObject resultJson = null;
		JSONArray jsonArray = null;
		JSONObject rightsJson = null;
		JSONObject rightsJson1 = null;

		String merchantQry = "";
		String type = "";
		String entity = "";
		String groupId = "";

		CallableStatement callableStmt = null;
		ResultSet merchantRS = null;
		Connection connection = null;

		try {
			merchantQry = "{call getUserGroupDetails(?,?,?,?)}";
			responseDTO = new ResponseDTO();
			merchantMap = new HashMap<String, Object>();
			resultJson = new JSONObject();
			logger.debug("Request JSON [" + requestDTO.getRequestJSON() + "]");

			type = requestDTO.getRequestJSON().getString("TYPE");
			entity = requestDTO.getRequestJSON().getString("ENTITY");
			groupId = requestDTO.getRequestJSON().getString("GROUP_ID");

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			callableStmt = connection.prepareCall(merchantQry);
			callableStmt.setString(1, groupId);
			callableStmt.setString(2, entity);
			callableStmt.registerOutParameter(3, OracleTypes.CURSOR);
			callableStmt.registerOutParameter(4, OracleTypes.VARCHAR);

			callableStmt.execute();
			logger.debug("Block executed successfully with error_message["
					+ callableStmt.getString(4) + "]");

			merchantRS = (ResultSet) callableStmt.getObject(3);

			if (merchantRS.next()) {
				resultJson.put("group_id", merchantRS.getString(1));
				resultJson.put("group_name", merchantRS.getString(2));
				resultJson.put("maker_id", merchantRS.getString(3));
				resultJson.put("maker_dttm", merchantRS.getString(4));
				resultJson.put("entity", merchantRS.getString(5));
			}

			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeCallableStatement(callableStmt);
			
			logger.debug(" Before Getting rights :"+groupId+"-"+type);

			merchantQry = "{call GetRightsPkg.pGetRights(?,?,?,?)}";
			callableStmt = connection.prepareCall(merchantQry);
			callableStmt.setString(1, groupId);
			callableStmt.setString(2, type);
			callableStmt.registerOutParameter(3, OracleTypes.VARCHAR);
			callableStmt.registerOutParameter(4, OracleTypes.CURSOR);

			jsonArray = new JSONArray();
			rightsJson = new JSONObject();
			rightsJson1 = new JSONObject();

			callableStmt.execute();
			logger.debug("Rights block executed successfully "
					+ "with error_message[" + callableStmt.getString(3) + "]");

			merchantRS = (ResultSet) callableStmt.getObject(4);

			while (merchantRS.next()) {
				rightsJson.put("id", merchantRS.getString(1));
				rightsJson.put("pId", merchantRS.getString(2));
				rightsJson.put("name", merchantRS.getString(3));
				rightsJson.put("checked", merchantRS.getString(4));
				rightsJson.put("open", merchantRS.getString(5));
				rightsJson.put("title", merchantRS.getString(6));
				rightsJson.put("chkDisabled", merchantRS.getString(7));
				jsonArray.add(rightsJson);
				rightsJson.clear();
			}

			rightsJson1.put("user_details", jsonArray);
			resultJson.put("json_val", rightsJson1);

			merchantMap.put("user_rights", resultJson);
			logger.debug("EntityMap [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			logger.debug("inside Response DTO [" + responseDTO + "]");
			jsonArray.clear();
			rightsJson1.clear();

		} catch (SQLException e) {
			logger.debug("SQLException in ViewUserGroup [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in ViewUserGroup  [" + e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closeCallableStatement(callableStmt);
			DBUtils.closeConnection(connection);

			merchantMap = null;
			resultJson = null;
			jsonArray = null;
			rightsJson = null;
			rightsJson1 = null;

			merchantQry = null;
			type = null;
			entity = null;
			groupId = null;

		}

		return responseDTO;
	}
	
	public ResponseDTO modifyGroupDetails(RequestDTO requestDTO) {

		logger.debug("Inside ModifyGroupDetails.. ");

		String keyVal = null;
		String groupId = null;
		String jsonVal = null;
		String merchantQry = null;

		org.json.JSONArray jArray = null;
		org.json.JSONObject jobj = null;

		PreparedStatement prepareStmt = null;
		PreparedStatement prepareStmt1 = null;
		ResultSet getId = null;

		Connection connection = null;

		try {

			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			groupId = requestJSON.getString("GROUP_ID");
			jsonVal = requestJSON.getString("jsonVal");
			keyVal = requestJSON.getString("keyVal");

			try {

				jobj = new org.json.JSONObject(jsonVal);
				logger.debug("Json object is [" + jobj.toString() + "]");
				jArray = new org.json.JSONArray(jobj.getString(keyVal));
			} catch (JSONException e) {
				logger.debug("Exception in parsing Json String ["
						+ e.getMessage() + "]");
			}

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			merchantQry = "update user_groups set json_Val=? where group_id=?";
			connection.setAutoCommit(false);
			prepareStmt = connection.prepareStatement(merchantQry);

			/*
			 * prepareStmt.setString(1, jsonVal.replaceAll(
			 * "\"chkDisabled\":\"true\"", "\"chkDisabled\":\"false\""));
			 */
			prepareStmt.setString(1, jsonVal);
			prepareStmt.setString(2, groupId.toUpperCase());

			int sizeJsonIns = prepareStmt.executeUpdate();
			connection.commit();

			logger.debug("Updated row are  [" + sizeJsonIns + "]");

			prepareStmt.close();

			merchantQry = "delete from user_linked_Action where group_id=? and user_id is null";
			connection.setAutoCommit(false);
			prepareStmt = connection.prepareStatement(merchantQry);
			prepareStmt.setString(1, groupId);

			int deleteSize = prepareStmt.executeUpdate();
			connection.commit();

			logger.debug("Rows deleted from user_linked_Action [" + deleteSize
					+ "]");

			merchantQry = "insert into user_linked_Action (ID ,PID ,NAME ,CHECKED ,OPEN ,TITLE ,GROUP_ID ) "
					+ "values (?,?,?,?,?,?,? )";
			connection.setAutoCommit(false);
			prepareStmt = connection.prepareStatement(merchantQry);

			org.json.JSONObject json_data = null;
			String id = "";
			String pid = "";
			String title = "";
			try {

				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);

					if (json_data.getString("checked").equalsIgnoreCase("true")) {

						prepareStmt1 = connection
								.prepareStatement("select id,pid,title from USER_ACTION_LINKS where upper(name)=upper(?)");
						prepareStmt1.setString(1, json_data.getString("name"));
						getId = prepareStmt1.executeQuery();

						if (getId.next()) {
							id = getId.getString(1);
							pid = getId.getString(2);
							title = getId.getString(3);
						}

						prepareStmt.setString(1, id);
						prepareStmt.setString(2, pid);
						prepareStmt.setString(3, json_data.getString("name"));
						prepareStmt
								.setString(4, json_data.getString("checked"));
						prepareStmt.setString(5, json_data.getString("open"));
						prepareStmt.setString(6, title);
						prepareStmt.setString(7, groupId);
						prepareStmt.addBatch();
					}
				}

			} catch (JSONException e) {
				logger.debug(" exception is [" + e.getMessage() + "]");
			}

			json_data = null;
			id = null;
			pid = null;
			title = null;

			int size[] = prepareStmt.executeBatch();
			connection.commit();

			logger.debug("Rows inserted in user_linked_Action [" + size.length
					+ "]");

			logger.debug(" Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in ModifyGroupDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in ModifyGroupDetails  [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(getId);
			DBUtils.closePreparedStatement(prepareStmt);
			DBUtils.closePreparedStatement(prepareStmt1);
			DBUtils.closeConnection(connection);
			keyVal = null;
			groupId = null;
			jsonVal = null;
			merchantQry = null;
			jArray = null;
			jobj = null;
		}

		return responseDTO;
	}

	public ResponseDTO insertCategoryDetails(RequestDTO requestDTO) {

		logger.debug("Inside insertCategoryDetails.... ");

		String catId = null;
		String catDesc = null;
		String userID = null;
		String entity = null;
		String applCode = null;

		org.json.JSONArray jArray = null;
		org.json.JSONObject jobj = null;

		String id = "";
		String pid = "";
		String title = "";
		String merchantQry = "";

		PreparedStatement prepareStmt1 = null;
		ResultSet getId = null;
		Connection connection = null;

		try {
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();

			catId = requestJSON.getString("catid");
			catDesc = requestJSON.getString("catdesc");
			userID = requestJSON.getString("user_id");
			
			connection = connection == null ? DBConnector.getConnection():connection;

			logger.debug("Connection is [" + connection + "]");

			connection.setAutoCommit(false);

			merchantQry = "insert into USER_GROUPS (GROUP_ID ,GROUP_NAME ,APPL_CODE ,MAKER_ID ,MAKER_DTTM ,ENTITY ,JSON_VAL) "
					+ "values (?,?,?,?,sysdate,?,?)";
			PreparedStatement merchantPstmt = connection
					.prepareStatement(merchantQry);

		/*	merchantPstmt.setString(1, groupId);
			merchantPstmt.setString(2, groupDesc);
			merchantPstmt.setString(3, applCode);
			merchantPstmt.setString(4, userID);
			merchantPstmt.setString(5, entity);
			merchantPstmt.setString(6, jsonVal.replaceAll(
					"\"chkDisabled\":\"true\"", "\"chkDisabled\":\"false\""));*/

			logger.debug("After assigning the values to USER_GROUPS. ");

			int count = merchantPstmt.executeUpdate();

			logger.debug("Getting the count after insertion to "
					+ "USER_GROUPS is [" + count + "] .");
			connection.commit();

			logger.debug("After second insertion to USER_GROUPS .");

			merchantPstmt = null;
			merchantQry = "insert into user_linked_Action (ID ,PID ,NAME ,CHECKED ,OPEN ,TITLE ,GROUP_ID) "
					+ "values (?,?,?,?,?,?,?)";
			connection.setAutoCommit(false);
			merchantPstmt = connection.prepareStatement(merchantQry);

			org.json.JSONObject json_data = null;
			try {

				for (int i = 0; i < jArray.length(); i++) {
					json_data = jArray.getJSONObject(i);

					if (json_data.getString("checked").equalsIgnoreCase("true")) {
						prepareStmt1 = connection
								.prepareStatement("select id,pid,title from USER_ACTION_LINKS where upper(name)=upper(?)");
						prepareStmt1.setString(1, json_data.getString("name"));
						getId = prepareStmt1.executeQuery();
						if (getId.next()) {
							id = getId.getString(1);
							pid = getId.getString(2);
							title = getId.getString(3);
						}

						logger.debug(" ID [" + id + "] PID [" + pid + "]");

						merchantPstmt.setString(1, id);
						merchantPstmt.setString(2, pid);
						merchantPstmt.setString(3, json_data.getString("name"));
						merchantPstmt.setString(4,
								json_data.getString("checked"));
						merchantPstmt.setString(5, json_data.getString("open"));
						merchantPstmt.setString(6, title);
					//	merchantPstmt.setString(7, groupId);
						merchantPstmt.addBatch();
					}
				}

			} catch (JSONException e) {
				logger.debug("inside  exception is [" + e.getMessage() + "].");
			}

			json_data = null;

			int size[] = merchantPstmt.executeBatch();
			connection.commit();

			logger.debug("Rows Inserted are [" + size.length + "]");

			//responseDTO = getUserGroupDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			logger.debug("SQLException in insertCategoryDetails ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			logger.debug("Exception in insertCategoryDetails  ["
					+ e.getMessage() + "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(getId);
			DBUtils.closePreparedStatement(prepareStmt1);
			DBUtils.closeConnection(connection);
			/*groupId = null;
			groupDesc = null;
			jsonVal = null;*/
			userID = null;
			entity = null;
			applCode = null;

			jArray = null;
			jobj = null;
			merchantQry = null;
			id = null;
			pid = null;
			title = null;
		}

		return responseDTO;
	}	
}
