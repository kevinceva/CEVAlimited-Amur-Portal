package com.ceva.base.ceva.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CategoryManagementDAO;
import com.ceva.base.common.dao.CevaPowerAdminDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class CategoryManagemtAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CategoryManagemtAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	public String groupId;
	public String userId;
	public String catId;
	public String catDesc;
	private String type = null;
	
	
	private HttpSession session = null;

	
	public String dashboardUsers(){
		return "success";
	}
	
	public String fetchCategoryDetails() {
		logger.debug("inside [CategoryManagemtAction][fetchCateoryDetails].. ");
		CategoryManagementDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new CategoryManagementDAO();
			responseDTO = merchantDAO.fetchCategoryDetails(requestDTO);
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
			logger.debug("Exception in [CategoryManagemtAction][fetchCateoryDetails] [" + e.getMessage()
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

	public String fetchSubCatList() {
		logger.debug("inside [CategoryManagemtAction][fetchSubCatList].. ");
		CategoryManagementDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			logger.debug("cat_id [" + getCatId() + "]");
			requestJSON.put("cat_id", getCatId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new CategoryManagementDAO();
			responseDTO = merchantDAO.fetchSubCatList(requestDTO);
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
			logger.debug("Exception in [CategoryManagemtAction][fetchSubCatList] [" + e.getMessage()
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
	
	public String createCategories() {

		String result="success";
		return result;
	}
	
	public String viewCategory() {
		logger.debug("Inside viewCategory. ");
		CategoryManagementDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("GROUP_ID", getCatId());
			requestJSON.put("TYPE", getType());

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CategoryManagementDAO();
			responseDTO = cevaPowerDAO.viewUserGroup(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
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
			logger.debug("Exception in viewCategory [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
			e.printStackTrace();
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}
		return result;
	}
	
	public String modifyGroupDetails() {
		logger.debug("Inside ModifyGroupDetails. ");
		CategoryManagementDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("GROUP_ID", getCatId());
			requestJSON
					.put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_DATA"
									: (String) session.getAttribute("makerId"));
			requestJSON.put("entity_id", (String) session
					.getAttribute("loginEntity") == null ? "NO_DATA"
					: (String) session.getAttribute("loginEntity"));
			requestJSON.put("applCode", (String) session
					.getAttribute("ACCESS_APPL_NAME") == null ? "NO_DATA"
					: (String) session.getAttribute("ACCESS_APPL_NAME"));

			logger.debug("Request JSON  [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);
			cevaPowerDAO = new CategoryManagementDAO();

			responseDTO = cevaPowerDAO.modifyGroupDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
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
			e.printStackTrace();
			result = "fail";
			logger.debug("Exception in Modify GroupDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String saveCategoryDetails() {
		logger.debug("Inside SaveGroupDetails. ");
		ArrayList<String> errors = null;
		CategoryManagementDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();

			requestJSON.put("catid", getCatId());
			requestJSON.put("catid", getCatDesc());
			requestJSON.put("user_id",(String) session.getAttribute("makerId") == null ? "NO_DATA" 
									: (String) session.getAttribute("makerId"));
			

			logger.debug(" Request JSON  [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CategoryManagementDAO();
			responseDTO = cevaPowerDAO.insertCategoryDetails(requestDTO);
			logger.debug(" Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
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
			logger.debug("Exception in Save GroupDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}	
	
	public String confirmCategoryDetails() {
		logger.debug("Inside confirmCategoryDetails. ");
		ArrayList<String> errors = null;
		CategoryManagementDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();

			requestJSON.put("catid", getCatId());
			requestJSON.put("catdesc", getCatDesc());
			

			logger.debug(" Request JSON  [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CategoryManagementDAO();
			responseDTO = cevaPowerDAO.confirmCategoryDetails(requestDTO);
			logger.debug(" Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
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
			logger.debug("Exception in  confirmCategoryDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	


	
	
}
