package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.base.common.dao.CevaPowerAdminDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class CevaGroupAction extends ActionSupport implements
		ServletRequestAware {

	private static final long serialVersionUID = 2875278258308981804L;

	Logger logger = Logger.getLogger(CevaGroupAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject locationJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String result = null;
	private String jsonVal = null;
	private String groupID = null;
	private String groupDesc = null;
	private String keyVal = null;
	private String empNo = null;
	private String userId = null;
	private String type = null;
	private String prevJsonString = null;
	private String userRights = null;
	private String entity = null;
	private String userInfoPage = null;
	private String multiData = null;
	private String method = null;

	private HttpSession session = null;

	private HttpServletRequest httpRequest = null;

	@Override
	public String execute() throws Exception {
		logger.debug("Inside  execute method is [" + getMethod() + "]");
		if (getMethod() != null) {
			logger.debug("prevJsonString  [" + prevJsonString + "]");
			logger.debug("JsonVal         [" + jsonVal + "]");
			logger.debug("GroupID         [" + groupID + "]");
			logger.debug("GroupDesc 	  [" + groupDesc + "]");
			if (userRights != null) {
				userRights = userRights.replaceAll("\"chkDisabled\":\"false\"",
						"\"chkDisabled\":\"true\"");
			}
			if (prevJsonString != null) {
				prevJsonString = prevJsonString
						.replaceAll("\"chkDisabled\":\"false\"",
								"\"chkDisabled\":\"true\"");
			}
			if (jsonVal != null) {
				jsonVal = jsonVal.replaceAll("\"chkDisabled\":\"false\"",
						"\"chkDisabled\":\"true\"");
			}
		}

		return super.execute();
	}

	public String createUserGrps() {
		logger.debug("Inside CreateUserGrps..");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getUserGroupDetails(requestDTO);
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
			logger.debug("Exception in CreateUserGrps [" + e.getMessage() + "]");
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

	public String userModifyConfirm() {
		logger.debug("Inside UserModifyConfirm...");

		logger.debug("Type[" + getType() + "]");

		responseJSON = constructToResponseJson(httpRequest);

		if (getType().equalsIgnoreCase("Modify")) {
			userInfoPage = "userModifyConfirm";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			userInfoPage = "userActivate";
		} else if (getType().equalsIgnoreCase("PasswordReset")) {
			userInfoPage = "userPasswordReset";
		} else if (getType().equalsIgnoreCase("PasswordResetConfirm")) {
			userInfoPage = "userPasswordResetConfirm";
		}

		return SUCCESS;
	}

	public String userModifyAck() {

		logger.debug("Inside UserModifyAck Type[" + getType() + "]");

		String modData = "";
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			modData = constructStanderedString(httpRequest);
			requestDTO.getRequestJSON().put("user_info", modData);
			requestDTO
					.getRequestJSON()
					.put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));

			logger.debug("Request DTO [" + requestDTO + "]");

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.userModifyAck(requestDTO);

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

			responseJSON = constructToResponseJson(httpRequest);

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in UserModifyAck [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
			modData = null;

		}
		return result;
	}

	private String constructStanderedString(HttpServletRequest httpRequest) {
		String key = null;
		String value = null;

		Enumeration enumParams = null;
		StringBuffer totalInfo = null;
		String totVal = "";

		logger.debug(" [constructStanderedString] inside");

		try {
			totalInfo = new StringBuffer(100);
			totalInfo.append("");
			enumParams = httpRequest.getParameterNames();

			while (enumParams.hasMoreElements()) {

				key = (String) enumParams.nextElement();
				value = httpRequest.getParameter(key);

				value = (value == null || value.equals("")) ? "NO_VALUE"
						: value;

				if (key.indexOf("CV") != -1) {
					totVal = key
							+ StringUtils.leftPad(
									String.valueOf(value.length()), 3, "0")
							+ value;
					totalInfo.append(totVal);
				}
			}

		} catch (Exception e) {
			logger.debug("Exception is :: " + e.getMessage());
		} finally {
			key = null;
			value = null;
			enumParams = null;
			totVal = null;
			totVal = totalInfo.toString();
			totalInfo.delete(0, totalInfo.length());
		}

		return totVal;
	}

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {
		Enumeration enumParams = null;
		JSONObject jsonObject = null;
		logger.debug("Inside ConstructToResponseJson...");
		try {
			enumParams = httpRequest.getParameterNames();

			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" Exception in ConstructToResponseJson ["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
		}
		logger.debug(" jsonObject[" + jsonObject + "]");

		return jsonObject;
	}

	public String getUserDetails() {
		logger.debug("Inside GetUserDetails .....");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("USER_ID", getUserId());

			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getUserDetails(requestDTO);
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
			logger.debug("Exception in Get User Details [" + e.getMessage()
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

	public String userInformation() {
		logger.debug("Inside UserInformation..");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("USER_ID", getUserId());
			requestJSON.put("TYPE", getType());
			requestJSON.put("PARENT_ID", "");
			requestJSON.put("MAKER_ID",
					session.getAttribute(CevaCommonConstants.MAKER_ID)
							.toString());

			logger.debug("Request JSON [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.userInformation(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (getType().equalsIgnoreCase("Modify")) {
				userInfoPage = "userModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				userInfoPage = "userViewInformation";
			} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
				userInfoPage = "userActivate";
			} else {
				userInfoPage = "userPasswordReset";
			}

			logger.debug(" UserInfoPage [" + userInfoPage + "]");

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
			logger.debug("Exception in User Information [" + e.getMessage()
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

	public String userProfile() {
		logger.debug("Inside UserProfile.. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID)
							.toString());

			requestDTO.setRequestJSON(requestJSON);
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.userProfile(requestDTO);

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
			logger.debug("Exception in User Profile [" + e.getMessage() + "]");
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

	public String userDashInformation() {
		logger.debug("Inside User DashInformation. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("USER_ID", getUserId());
			requestJSON.put("TYPE", getType());
			requestJSON.put("MAKER_ID",
					(String) session.getAttribute("makerId"));

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO[" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.userDashInformation(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (getType().equalsIgnoreCase("View")) {
				userInfoPage = "userDashboardInformation";
			} else {
				userInfoPage = "userGroupDashboardInformation";
			}

			logger.debug(" UserInfoPage [" + userInfoPage + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData()
						.get("DETAILS");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.dashboardUsers(requestDTO);
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_ACCESS_MNG");
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in User Dash Information ["
					+ e.getMessage() + "]");
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

	public String viewUserGroup() {
		logger.debug("Inside ViewUserGroup. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("TYPE", getType());
			requestJSON.put("ENTITY", getEntity());

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
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
			logger.debug("Exception in View UserGroup [" + e.getMessage() + "]");
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

	public String saveUserAssignDetails() {
		logger.debug("inside SaveUser Assign Details. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			session = ServletActionContext.getRequest().getSession();

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("keyVal", getKeyVal());
			requestJSON.put("jsonVal", getJsonVal());
			requestJSON.put("empNo", getEmpNo());
			requestJSON.put("user_id", getUserId() == null ? "NO_DATA"
					: getUserId());
			requestJSON.put("emp_no", getEmpNo() == null ? "NO_DATA"
					: getEmpNo());
			requestJSON.put("entity_id", (String) session
					.getAttribute("loginEntity") == null ? "NO_DATA"
					: (String) session.getAttribute("loginEntity"));
			requestJSON.put("applCode", (String) session
					.getAttribute("ACCESS_APPL_NAME") == null ? "NO_DATA"
					: (String) session.getAttribute("ACCESS_APPL_NAME"));

			logger.debug("Request JSON  [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.saveUserAssignDetails(requestDTO);

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
			logger.debug("Exception in Save UserAssignDetails ["
					+ e.getMessage() + "]");
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

	public String modifyGroupDetails() {
		logger.debug("Inside ModifyGroupDetails. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("GROUP_DESC", getGroupDesc());
			requestJSON.put("keyVal", getKeyVal());
			requestJSON.put("jsonVal", getJsonVal());
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
			cevaPowerDAO = new CevaPowerAdminDAO();

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

	public String saveGroupDetails() {
		logger.debug("Inside SaveGroupDetails. ");
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();

			requestJSON.put("GROUP_ID", getGroupID());
			requestJSON.put("GROUP_DESC", getGroupDesc());
			requestJSON.put("keyVal", getKeyVal());
			requestJSON.put("jsonVal", getJsonVal());
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

			logger.debug(" Request JSON  [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.insertUserGroupDetails(requestDTO);
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

	public String modifyAccessRights() {
		logger.debug("inside Modify Access Rights. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("USER_ID", getUserId());
			requestJSON.put("TYPE", getType());

			logger.debug("Request JSON [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.modifyAccessRights(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
				logger.debug("Response JSON [" + responseJSON + "]");
				session.setAttribute("responseJSON", responseJSON);
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
			logger.debug("Exception in Modify AccessRights [" + e.getMessage()
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

	public String getUnAuthUsersList() {
		logger.debug("Inside GetUnAuthUsersList.. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("GROUP_ID", groupID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getUnAuthUsersList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_LIST");
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
			logger.debug("Exception in GetUnAuthUsersList [" + e.getMessage()
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

	public String authUsersListAck() {
		logger.debug("Inside AuthUsersListAck.. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			session = ServletActionContext.getRequest().getSession();

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("GROUP_ID", groupID);
			requestJSON.put("USERS", keyVal);
			requestJSON.put("MULDATA", multiData);
			requestJSON
					.put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.userAuthorizationAck(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			responseJSON = requestJSON;
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				result = "success";
				responseJSON.put("msg", "SUCCESS");
			} else {
				responseJSON.put("msg", "FAIL");
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in AuthUsersListAck [" + e.getMessage()
					+ "]");
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getJsonVal() {
		return jsonVal;
	}

	public void setJsonVal(String jsonVal) {
		this.jsonVal = jsonVal;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getKeyVal() {
		return keyVal;
	}

	public void setKeyVal(String keyVal) {
		this.keyVal = keyVal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrevJsonString() {
		return prevJsonString;
	}

	public void setPrevJsonString(String prevJsonString) {
		this.prevJsonString = prevJsonString;
	}

	public String getUserRights() {
		return userRights;
	}

	public void setUserRights(String userRights) {
		this.userRights = userRights;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getUserInfoPage() {
		return userInfoPage;
	}

	public void setUserInfoPage(String userInfoPage) {
		this.userInfoPage = userInfoPage;
	}

	@Override
	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public String getMultiData() {
		return multiData;
	}

	public void setMultiData(String multiData) {
		this.multiData = multiData;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
