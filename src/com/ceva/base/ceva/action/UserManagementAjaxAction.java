package com.ceva.base.ceva.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.bean.BillerBean;
import com.ceva.base.common.dao.AjaxDAO;
import com.ceva.base.common.dao.BillerManagementDAO;
import com.ceva.base.common.dao.ServiceMgmtAjaxDAO;
import com.ceva.base.common.dao.SwitchUIDAO;
import com.ceva.base.common.dao.UserAjaxDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionSupport;

public class UserManagementAjaxAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(UserManagementAjaxAction.class);

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;


	private HttpSession session;

	private String method;
	private String searchData;
	
	
	@Override
	public String execute() throws Exception {
		logger.debug("Inside Execute Method.");
		logger.debug("Execute method [" + method + "] ");
		String result = ERROR;

		try {
			 if (method.equalsIgnoreCase("searchEntity")) {
				result = fetchUserDetails();
			} 		} catch (Exception e) {
			logger.debug("Inside execute [" + e.getMessage() + "]");
		} finally {

		}

		return result;
	}

	
	public String fetchUserDetails() {
		logger.debug("Inside fetchUserDetails.. ");
		String queryConst = "";
		logger.debug("Search Data:: [" + getSearchData() + "]");

		PreparedStatement entityPstmt = null;
		ResultSet entityRS = null;
		Connection connection = null;
		JSONObject json = null;
		JSONArray userJsonArray = new JSONArray();
		try {
			connection = DBConnector.getConnection();
				queryConst = "Select A.LOGIN_USER_ID,B.USER_NAME,B.EMP_ID,B.EMAIL "
						+ ",to_char(B.EXPIRY_DATE,'DD-MM-YYYY'),decode(B.USER_STATUS,'A','Active','L','De-Active','F','InActive','N','Un-Authorize',B.USER_STATUS) from USER_LOGIN_CREDENTIALS A,USER_INFORMATION B "
						+ "WHERE A.COMMON_ID=B.COMMON_ID AND UPPER(B.USER_GROUPS) =?";
				
			logger.debug("QueryConst [" + queryConst + "]");
			entityPstmt = connection.prepareStatement(queryConst);
			entityPstmt.setString(1, getSearchData());
			entityRS = entityPstmt.executeQuery();
			
			while (entityRS.next()) {
				
				json = new JSONObject();
				
				json.put(CevaCommonConstants.ROLE_GRP_ID, getSearchData());
				json.put(CevaCommonConstants.USER_ID,entityRS.getString(1));
				String data[] = entityRS.getString(2).split("\\ ");
				String fName = "";
				String lName = "";
				if (data.length == 1) {
					fName = data[0];
				} else {
					fName = data[0];
					lName = data[1];
				}
				json.put(CevaCommonConstants.F_NAME, fName);
				json.put(CevaCommonConstants.L_NAME, lName);
				json.put(CevaCommonConstants.EMPLOYEE_NO,
						entityRS.getString(3));
				json.put(CevaCommonConstants.EMAIL,
						entityRS.getString(4));
				json.put(CevaCommonConstants.EXPIRY_DATE,
						entityRS.getString(5));
				json.put("user_status", entityRS.getString(6));
				userJsonArray.add(json);
				json.clear();
			}
			
			responseJSON.put("USER_LIST", userJsonArray);
			
			logger.debug("Response JSON::"+responseJSON);
			
		} catch (Exception e) {
			logger.debug(" exception is  : " + e.getMessage());
		} finally {

			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeResultSet(entityRS);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}


	public JSONObject getResponseJSON() {
		return responseJSON;
	}


	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}


	public String getMethod() {
		return method;
	}


	public void setMethod(String method) {
		this.method = method;
	}


	public String getSearchData() {
		return searchData;
	}


	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}

}
