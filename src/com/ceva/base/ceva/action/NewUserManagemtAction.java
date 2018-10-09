package com.ceva.base.ceva.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.NewUserManagementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class NewUserManagemtAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(NewUserManagemtAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	public String groupId;
	public String userId;
	
	public String dashboardUsers(){
		return "success";
	}
	
	public String fetchUserGroupDetails() {
		logger.debug("inside [NewUserManagemtAction][fetchUserGroupDetails].. ");
		NewUserManagementDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new NewUserManagementDAO();
			responseDTO = merchantDAO.fetchUserGroupDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("GROUP_LIST");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [NewUserManagemtAction][fetchUserGroupDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			merchantDAO = null;
		}

		return result;
	}

	public String fetchUsersList() {
		logger.debug("inside [NewUserManagemtAction][fetchUsersList].. ");
		NewUserManagementDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("GROUP_ID", getGroupId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new NewUserManagementDAO();
			responseDTO = merchantDAO.fetchUsersList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("USER_LIST");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [NewUserManagemtAction][fetchUsersList] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			merchantDAO = null;
		}

		return result;
	}
	
	public String fetchUserRights(){
		
		logger.debug("inside [NewUserManagemtAction][fetchUserRights].. ");
		NewUserManagementDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("USER_ID", getUserId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new NewUserManagementDAO();
			responseDTO = merchantDAO.fetchUserRights(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("RIGHTS_LIST");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [NewUserManagemtAction][fetchUserRights] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			merchantDAO = null;
		}

		return result;

	}
	

	public JSONObject getResponseJSON() {
		return responseJSON;
	}


	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	


	
	
}
