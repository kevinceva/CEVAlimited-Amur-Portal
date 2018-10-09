package com.ceva.base.memory.cache.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class CacheMemoryDAO {

	
	static Logger logger = Logger.getLogger(CacheMemoryDAO.class);
	
	private static ResponseDTO responseDTO = null;
	private static JSONObject requestJSON = null;
	private static JSONObject responseJSON = null;
	
	public static ResponseDTO getAllUserGrps(RequestDTO requestDTO) {

		System.out.println("Inside Get All User Groups ... ");

		HashMap<String, Object> merchantMap = null;

		JSONObject resultJson = null;
		JSONObject json = null;

		JSONArray userGroupsJSONArray = null;

		Connection connection = null;
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;

		List<String> groupsArray = null;
		List<String> usersSet = null;

		String merchantQry = "SELECT GROUP_ID,GROUP_NAME,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') FROM  "
				+ "USER_GROUPS WHERE APPL_CODE='AgencyBanking' ORDER BY MAKER_DTTM";
		String data[] = null;
		String fName = "";
		String lName = "";
		String rights = "";
		int count = 0;
		try {

			responseDTO = new ResponseDTO();

			connection = DBConnector.getConnection();
			System.out.println("connection is null [" + connection == null + "]");

			merchantMap = new HashMap<String, Object>(100);
			resultJson = new JSONObject();
			groupsArray = new ArrayList<String>();
			usersSet = new ArrayList<String>();

			userGroupsJSONArray = new JSONArray();

			json = new JSONObject();

			merchantPstmt = connection.prepareStatement(merchantQry);
			merchantRS = merchantPstmt.executeQuery();

			while (merchantRS.next()) {
				json.put(CevaCommonConstants.ROLE_GRP_ID,
						merchantRS.getString(1));
				json.put(CevaCommonConstants.ROLE_GRP_NAME,
						merchantRS.getString(2));
				json.put(CevaCommonConstants.MAKER_ID, merchantRS.getString(3));
				json.put(CevaCommonConstants.MAKER_DATE,
						merchantRS.getString(4));

				groupsArray.add(merchantRS.getString(1));
				userGroupsJSONArray.add(json);
				json.clear();
			}

			resultJson.put("USER_GROUPS", userGroupsJSONArray);

			userGroupsJSONArray.clear();

			merchantPstmt.close();
			merchantRS.close();

			System.out.println("GroupsArray is [" + groupsArray + "]");
			for (String getGrps : groupsArray) {
				merchantQry = "Select A.LOGIN_USER_ID,B.USER_NAME,B.EMP_ID,B.EMAIL "
						+ ",to_char(B.EXPIRY_DATE,'DD-MM-YYYY'),decode(B.USER_STATUS,'A','Active','L','De-Active','F','InActive','N','Un-Authorize',B.USER_STATUS) from USER_LOGIN_CREDENTIALS A,USER_INFORMATION B "
						+ "WHERE A.COMMON_ID=B.COMMON_ID AND UPPER(B.USER_GROUPS) =?";
				try {
					merchantPstmt = connection.prepareStatement(merchantQry);
					merchantPstmt.setString(1, getGrps.toUpperCase());
					merchantRS = merchantPstmt.executeQuery();

					while (merchantRS.next()) {
						json.put(CevaCommonConstants.ROLE_GRP_ID, getGrps);
						json.put(CevaCommonConstants.USER_ID,
								merchantRS.getString(1));
						data = merchantRS.getString(2).split("\\ ");
						fName = "";
						lName = "";
						if (data.length == 1) {
							fName = data[0];
						} else {
							fName = data[0];
							lName = data[1];
						}
						json.put(CevaCommonConstants.F_NAME, fName);
						json.put(CevaCommonConstants.L_NAME, lName);
						json.put(CevaCommonConstants.EMPLOYEE_NO,
								merchantRS.getString(3));
						json.put(CevaCommonConstants.EMAIL,
								merchantRS.getString(4));
						json.put(CevaCommonConstants.EXPIRY_DATE,
								merchantRS.getString(5));
						json.put("user_status", merchantRS.getString(6));
						userGroupsJSONArray.add(json);
						usersSet.add(merchantRS.getString(1));
						json.clear();
					}

					if (userGroupsJSONArray != null
							&& userGroupsJSONArray.size() > 0) {
						resultJson.put(getGrps + "_USERS", userGroupsJSONArray);
						userGroupsJSONArray.clear();
					}
					merchantPstmt.close();
					merchantRS.close();

				} catch (SQLException e) {
					System.out.println("Got SQLexception in loop [" + e.getMessage()
							+ "]");
				} catch (Exception e) {
					System.out.println("Got exception in loop [" + e.getMessage()
							+ "]");
				} finally {
					merchantPstmt.close();
					merchantRS.close();

				}
			}

			for (String getUser : usersSet) {
				rights = "";
				merchantQry = "select count(*) from user_linked_action where upper(user_id)=?";

				merchantPstmt = connection.prepareStatement(merchantQry);
				merchantPstmt.setString(1, getUser.toUpperCase());
				merchantRS = merchantPstmt.executeQuery();

				if (merchantRS.next()) {
					count = merchantRS.getInt(1);
				}

				merchantPstmt.close();
				merchantRS.close();

				if (count > 0) {
					merchantQry = "select NAME from user_linked_action where user_id=? order by id ";
				} else {
					merchantQry = "select NAME from user_linked_action where  GROUP_ID in (select user_groups from "
							+ "user_information where common_id in (select common_id from user_login_credentials where "
							+ "upper(login_user_id)=?)) and user_id is null order by id";
				}

				try {

					merchantPstmt = connection.prepareStatement(merchantQry);
					merchantPstmt.setString(1, getUser.toUpperCase());
					merchantRS = merchantPstmt.executeQuery();

					while (merchantRS.next()) {
						rights += merchantRS.getString(1) + ",";
					}

					merchantPstmt.close();
					merchantRS.close();

					try {
						json.put("name",
								rights.substring(0, rights.lastIndexOf(",")));
						json.put("user_id", getUser);
						userGroupsJSONArray.add(json);
						rights = "";
						json.clear();
					} catch (Exception e) {
						System.out.println("Exception in getting rights for user"
								+ " [" + getUser + "] ");
					}

					if (userGroupsJSONArray != null
							&& userGroupsJSONArray.size() > 0) {
						resultJson
								.put(getUser + "_RIGHTS", userGroupsJSONArray);
						userGroupsJSONArray.clear();
					}

				} catch (SQLException e) {
					System.out.println("Got SQLException in loop  [" + e.getMessage()
							+ "]");
				}
			}

			merchantMap.put("USER_ACCESS_MNG", resultJson);

			System.out.println("Before getting Links [" + merchantMap + "]");

			System.out.println("Entity Map [" + merchantMap + "]");
			responseDTO.setData(merchantMap);
			System.out.println("Response DTO [" + responseDTO + "]");

		} catch (SQLException e) {
			System.out.println("SQLException in GetAllUserGrps [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} catch (Exception e) {
			System.out.println("SQLException in GetAllUserGrps [" + e.getMessage()
					+ "]");
			responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeResultSet(merchantRS);
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);

			merchantQry = null;
			data = null;
			fName = null;
			lName = null;
			rights = null;
		}

		return responseDTO;
	}
	
	
}
