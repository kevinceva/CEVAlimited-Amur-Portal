package com.ceva.base.agencybanking.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.ceva.base.common.dao.CevaPowerAdminDAO;
import com.ceva.base.common.dao.MerchantDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class CevaPowerAdminAction extends ActionSupport implements RequestAware, ServletRequestAware,ServletResponseAware {

	private static final long serialVersionUID = 2875278258308981804L;

	private Logger logger = Logger.getLogger(CevaPowerAdminAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject locationJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String result = null;
	private String redirectPage;

	private String entityName;
	private String entityLocation;
	private String entityAddress1;
	private String entityAddress2;
	private String address;
	private String entityCity;
	private String entityProvince;
	private String entityCountry;
	private String entityPostalCode;
	private String currencyConversionAgent;
	private String applicationType;
	private String contactPerson;
	private String mailingAddress;
	private String contactLocation;
	private String contactCity;
	private String contactProvince;
	private String contactCountry;
	private String contactEmail;
	private String contactPostalCode;
	private String telephoneOff;
	private String telephoneRes;
	private String mobile;
	private String fax;
	private String entity;
	private String userId;
	private String userid;
	private String groupID;
	private String pid;
	private String employeeNo;
	private String firstName;
	private String lastName;
	private String adminType;
	private String officeLocation;
	private String expiryDate;
	private String email;
	private String reason;
	private String roleGroupName;
	private String roleGroupID;
	private String selectedRoles;
	private String multiData;
	private String[] rolelist2;

	private HttpSession session = null;
	private ArrayList<String> applicationTypeList = null;
	private ArrayList<String> entityList = null;
	private ArrayList<String> currencyConversionAgentList = null;
	private ArrayList<String> adminTypeList = null;
	private ArrayList<String> officeLocationList = null;
	private ArrayList<String> roleGroupIdList = null;
	private ArrayList<String> usersList = null;

	private HttpServletRequest httpRequest = null;
	HttpServletResponse httpServletResponse = null;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	private InputStream stream;

	// getter here
	public InputStream getStream() {
		return stream;
	}

	public String getData() {
		String str = "sample string";
		entityList = new ArrayList<String>();
		entityList.add("data1");
		entityList.add("data2");
		stream = new ByteArrayInputStream(entityList.toString().getBytes());
		entityList.clear();
		entityList = null;
		return SUCCESS;
	}

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String insertEntityInfo() {
		logger.debug("inside Insert Entity Info. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (entityName == null) {
				addActionError(" Entity Name Missing");
				result = "fail";
			} else if (entityAddress1 == null) {
				addActionError("Entity Address1 Missing");
				result = "fail";
			} else if (entityCity == null) {
				addActionError("Entity City Missing");
				result = "fail";
			} else if (entityPostalCode == null) {
				addActionError("Entity Postal Code Missing");
				result = "fail";
			} else if (contactPerson == null) {
				addActionError("Contact Person Missing");
				result = "fail";
			} else if (mailingAddress == null) {
				addActionError("Mailing Address Missing");
				result = "fail";
			} else if (contactCity == null) {
				addActionError("Contact City Missing");
				result = "fail";
			} else if (contactEmail == null) {
				addActionError("Contact Email Missing");
				result = "fail";
			} else if (contactPostalCode == null) {
				addActionError("Contact Postal Code Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.ENTITY_NAME, entityName);
				if (entityLocation != null) {
					requestJSON.put(CevaCommonConstants.ENTITY_LOCATION,
							entityLocation);
				}

				address = entityAddress1 + "-" + entityAddress2;
				requestJSON.put(CevaCommonConstants.ENTITY_ADDRESS, address);
				requestJSON.put(CevaCommonConstants.ENTITY_CITY, entityCity);
				if (entityProvince != null) {
					requestJSON.put(CevaCommonConstants.ENTITY_PROVINCE,
							entityProvince);
				}
				if (entityCountry != null) {
					requestJSON.put(CevaCommonConstants.ENTITY_COUNTRY,
							entityCountry);
				}
				requestJSON.put(CevaCommonConstants.ENTITY_POSTAL_CODE,
						entityPostalCode);
				requestJSON.put(CevaCommonConstants.APPL_NAME, session
						.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME));
				requestJSON.put(CevaCommonConstants.CONTACT_PERSON,
						contactPerson);
				requestJSON.put(CevaCommonConstants.MAILING_ADDRESS,
						mailingAddress);

				if (telephoneOff != null) {
					requestJSON.put(CevaCommonConstants.OFF_TELEPHONE,
							telephoneOff);
				}
				if (telephoneRes != null) {
					requestJSON.put(CevaCommonConstants.RES_TELEPHONE,
							telephoneRes);
				}
				if (mobile != null) {
					requestJSON.put(CevaCommonConstants.MOBILE, mobile);
				}
				if (fax != null) {
					requestJSON.put(CevaCommonConstants.FAX, fax);
				}
				if (contactLocation != null) {
					requestJSON.put(CevaCommonConstants.CONTACT_LOCATION,
							contactLocation);
				}
				requestJSON.put(CevaCommonConstants.CONTACT_CITY, contactCity);
				if (contactProvince != null) {
					requestJSON.put(CevaCommonConstants.CONTACT_PROVINCE,
							contactProvince);
				}
				if (contactCountry != null) {
					requestJSON.put(CevaCommonConstants.CONTACT_COUNTRY,
							contactCountry);
				}
				requestJSON
						.put(CevaCommonConstants.CONTACT_EMAIL, contactEmail);
				requestJSON.put(CevaCommonConstants.CONTACT_POSTALCODE,
						contactPostalCode);
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);
				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.insertEntityInfo(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Getting messages from DB");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					result = "success";
				} else {
					logger.debug("Getting error from DB.");
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.RESPONSE_JSON);
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}

			}
			logger.debug(" Result ==> " + result);
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insert Entity Info [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getCevaEntitys() {
		logger.debug("Inside Get Ceva Entitys. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getCevaEntitys(requestDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {

				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.ENTITY_LIST);
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
			logger.debug("Exception in Get CevaEntitys [" + e.getMessage()
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

	public String getCevaApplications() {
		logger.debug("Inside Get Ceva Applications.. ");
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getCevaApplications(requestDTO);
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.APPL_TYPES);
				logger.debug("inside [getCevaApplications][responseJSON:::::"
						+ responseJSON);
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
			logger.debug("Exception in Get getCevaApplications ["
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

	public String getUserCreatePage() {
		logger.debug("Inside Get UserCreatePage .. ");
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.ENTITY, entity);
			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getUserCreatePage(requestDTO);
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.APPL_TYPES);
				logger.debug("Response JSON [" + responseJSON + "]");
				responseJSON.put(CevaCommonConstants.ENTITY, entity);
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Get getUserCreatePage ["
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

	public String resetPassword() {

		logger.debug(" Inside ResetPassword.. ");
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		CevaPowerAdminDAO cevaPowerDAO = null;

		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (userId == null) {
				addActionError(" User Id Missing");
				result = "fail";
			} else if (reason == null) {
				addActionError("Reason Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.USER_ID, userId);
				requestJSON.put(CevaCommonConstants.REASON, reason);
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestDTO.setRequestJSON(requestJSON);

				logger.debug("Request JSON [" + requestJSON + "]");
				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.resetPassword(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					result = "success";
				} else {
					logger.debug("Getting error from DAO.");
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}
			}
			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in resetPassword [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getCommonScreen() {
		result = "success";
		logger.debug("Result [" + result + "]");
		return result;
	}

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {
		@SuppressWarnings("rawtypes")
		Enumeration enumParams = httpRequest.getParameterNames();
		JSONObject jsonObject = null;
		logger.debug(" [constructToResponseJson] inside ...");
		try {
			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" [constructToResponseJson] inside exception ["
					+ e.getMessage() + "]");

		}
		logger.debug(" [constructToResponseJson] jsonObject["
				+ jsonObject + "]");

		return jsonObject;
	}

	// Update User Status
	public String updateUserStatus() {

		logger.debug("Inside UpdateUserStatus.... ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject(); 
			requestDTO = new RequestDTO();

			if (userId == null) {
				addActionError("User Id Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.USER_ID, userId);
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("RequestJSON [" + requestJSON + "]");

				responseJSON = constructToResponseJson(httpRequest);

				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.updateUserStatus(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug(" Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					responseJSON.put("error_flag", "success");

					result = "success";
				} else {
					logger.debug("Getting error from DAO.");
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}

					responseJSON.put("error_flag", "error");
					result = "fail";
				}
			}
			logger.debug("result ==>" + result + "]");
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in updateUserStatus [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getRoles() {
		logger.debug("Inside GetRoles... ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.APPL_NAME,
					session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME));
			requestDTO.setRequestJSON(requestJSON);

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getRoles(requestDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.ROLE_DATA);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				logger.debug("Getting error from DAO.. Error.");
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in updateUserStatus [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String createRoleGroup() {

		logger.debug(" Inside CreateRoleGroup.. ");
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (applicationType == null) {
				addActionError(" Application Type Missing");
				result = "fail";
			} else if (roleGroupID == null) {
				addActionError("Entity Missing");
				result = "fail";
			} else if (roleGroupName == null) {
				addActionError("Entity Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.APPL_NAME, applicationType);
				requestJSON.put(CevaCommonConstants.ROLE_GRP_ID, roleGroupID);
				requestJSON.put(CevaCommonConstants.ROLE_GRP_NAME,
						roleGroupName);
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request JSON [" + requestJSON + "]");

				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.createRoleGroup(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.APPL_TYPES);
					logger.debug("Response JSON [" + responseJSON);
					result = "success";
				} else {
					logger.debug("Getting error from DAO.");
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.APPL_TYPES);
					result = "fail";
				}
			}
			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in updateUserStatus [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String assignRoles() {

		logger.debug(" Inside AssignRoles.. ");
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (applicationType == null) {
				addActionError(" Application Type Missing");
				result = "fail";
			} else if (roleGroupID == null) {
				addActionError("Entity Missing");
				result = "fail";
			} else if (selectedRoles == null) {
				addActionError("Selected Roles Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.APPL_NAME, applicationType);
				requestJSON.put(CevaCommonConstants.ROLE_GRP_ID, roleGroupID);
				requestJSON.put(CevaCommonConstants.SELECTED_ROLES,
						selectedRoles);
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request JSON [" + requestJSON + "]");

				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.assignRoles(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ROLE_DATA);
					logger.debug("Response JSON [" + responseJSON);
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					result = "success";
				} else {
					logger.debug("Getting error from DAO.");
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ROLE_DATA);
					logger.debug("Response JSON [" + responseJSON + "]");
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}

					result = "fail";
				}
			}
			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in updateUserStatus [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
			messages = null;
		}

		return result;

	}

	public String getUsersToAssign() {
		logger.debug("Inside Get UsersToAssign. ");
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.APPL_NAME,
					session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME));
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getUsersToAssign(requestDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				logger.debug("Response DTO [" + responseDTO + "]");
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.ASSIGN_USER_DATA);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				logger.debug("Getting error from DAO.");
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in getUsersToAssign [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String adminCreateInfo() {
		logger.debug("Inside Get AdminCreate Info .. group Id"+groupID);
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject(); 
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.ENTITY, entity);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getAdminCreateInfo(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.LOCATION_INFO);
				responseJSON.put(CevaCommonConstants.ENTITY, entity);
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
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in getAdminCraeteInfo [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String insertIctAdminDetails() {
		logger.debug("inside InsertIctAdminDetails... ");
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (multiData == null) {
				addActionError(" Records Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put("GROUP_ID", getGroupID() == null ? "NO_DATA"
						: getGroupID());
				requestJSON.put("ENTITY_ID", getEntity() == null ? "NO_DATA"
						: getEntity());
				requestJSON.put(CevaCommonConstants.MULTI_DATA, multiData);

				logger.debug("Request JSON [" + requestJSON + "]");

				requestDTO.setRequestJSON(requestJSON);

				cevaPowerDAO = new CevaPowerAdminDAO();
				responseDTO = cevaPowerDAO.insertIctAdminDetails(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(
							"USER_DETAILS");
					logger.debug("Response JSON [" + responseJSON + "]");

					result = "success";
				} else {
					logger.debug("Getting error from DB.");
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.LOCATION_INFO);
					responseJSON.put(CevaCommonConstants.ENTITY, entity);
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}

			}
			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in insertIctAdminDetails ["
					+ e.getMessage() + "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String getIctAdminData() {
		logger.debug("inside getIctAdminData...  ");
		ArrayList<String> errors = null;
		CevaPowerAdminDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getIctAdminData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_LIST);
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
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in getIctAdminData [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String getviewICTUserAdminPage() {

		logger.debug("inside GetviewICTUserAdminPage..");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.USER_ID, userid);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("[requestDTO:::::" + requestDTO + "]");

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getICTAdminViewDetails(requestDTO);
			logger.debug("[responseDTO:::::" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.ICTADMIN_INFO);
				locationJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.LOCATION_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				logger.debug("Location JSON [" + locationJSON + "]");
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
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in getIctAdminData [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}
		return result;
	}

	public String updateICTUserAdminPage() {
		logger.debug("inside UpdateICTUserAdminPage.. ");
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		MerchantDAO merchantDAO = null;

		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (userid == null) {
				addActionError("StoreId Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.USER_ID, userId);
				requestJSON.put(CevaCommonConstants.EMPLOYEE_NO, employeeNo);
				requestJSON.put(CevaCommonConstants.FIRST_NAME, firstName);
				requestJSON.put(CevaCommonConstants.LAST_NAME, lastName);
				requestJSON.put(CevaCommonConstants.ADMIN_TYPE, adminType);
				requestJSON.put(CevaCommonConstants.OFFICE_LOCATON,
						officeLocation);
				requestJSON.put(CevaCommonConstants.EXPIRY_DATE, expiryDate);
				requestJSON.put(CevaCommonConstants.EMAIL_ID, email);
				requestJSON
						.put(CevaCommonConstants.OFF_TELEPHONE, telephoneOff);
				requestJSON
						.put(CevaCommonConstants.RES_TELEPHONE, telephoneRes);

				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);

				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.updateStoreDetails(requestDTO);

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.MERCHANT_LIST);
					logger.debug("Response JSON [" + responseJSON + "]");
					logger.debug("Getting messages from DB.");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					result = "success";
				} else {
					logger.debug("Getting error from DB.");

					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}

			}
			logger.debug(" Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in " + " updateICTUserAdminPage  ["
					+ e.getMessage() + "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			merchantDAO = null;
			errors = null;
			messages = null;
		}

		return result;

	}

	public String dashboardUsers() {
		logger.debug("Inside DashboardUsers.. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();

			requestJSON.put("PARENT_ID", getPid());
			requestJSON.put("USER_ID",
					session.getAttribute(CevaCommonConstants.MAKER_ID)
							.toString());

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.dashboardUsers(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_ACCESS_MNG");
				logger.debug("Response JSON  [" + responseJSON + "]");
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
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in  DashboardUsers  [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String listAllUserGrps() {
		logger.debug("Inside listAllUserGrps ... ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();

			requestJSON.put("PARENT_ID", getPid());
			requestJSON.put("USER_ID",
					session.getAttribute(CevaCommonConstants.MAKER_ID)
							.toString());

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getAllUserGrps(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_ACCESS_MNG");

				logger.debug("Response JSON  [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
	
			
			result = "success";
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal error occured, please try again.");
			logger.debug("Exception in  listAllUserGrps  [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String getAllUserDetails() {
		logger.debug("Inside getAllUserDetails.. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");

			cevaPowerDAO = new CevaPowerAdminDAO();
			responseDTO = cevaPowerDAO.getAllUserDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_LIST);
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
			addActionError("Internal Error Occured, Please try again.");
			logger.debug("Exception in  getAllUserDetails  [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String createUserGrps() {
		logger.debug("inside CreateUserGrps.. ");
		CevaPowerAdminDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
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
			addActionError("Internal Error Occured, Please try again.");
			logger.debug("Exception in  createUserGrps  [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityLocation() {
		return entityLocation;
	}

	public void setEntityLocation(String entityLocation) {
		this.entityLocation = entityLocation;
	}

	public String getEntityAddress1() {
		return entityAddress1;
	}

	public void setEntityAddress1(String entityAddress1) {
		this.entityAddress1 = entityAddress1;
	}

	public String getEntityAddress2() {
		return entityAddress2;
	}

	public void setEntityAddress2(String entityAddress2) {
		this.entityAddress2 = entityAddress2;
	}

	public String getEntityCity() {
		return entityCity;
	}

	public void setEntityCity(String entityCity) {
		this.entityCity = entityCity;
	}

	public String getEntityProvince() {
		return entityProvince;
	}

	public void setEntityProvince(String entityProvince) {
		this.entityProvince = entityProvince;
	}

	public String getEntityCountry() {
		return entityCountry;
	}

	public void setEntityCountry(String entityCountry) {
		this.entityCountry = entityCountry;
	}

	public String getEntityPostalCode() {
		return entityPostalCode;
	}

	public void setEntityPostalCode(String entityPostalCode) {
		this.entityPostalCode = entityPostalCode;
	}

	public String getCurrencyConversionAgent() {
		return currencyConversionAgent;
	}

	public void setCurrencyConversionAgent(String currencyConversionAgent) {
		this.currencyConversionAgent = currencyConversionAgent;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getContactLocation() {
		return contactLocation;
	}

	public void setContactLocation(String contactLocation) {
		this.contactLocation = contactLocation;
	}

	public String getContactCity() {
		return contactCity;
	}

	public void setContactCity(String contactCity) {
		this.contactCity = contactCity;
	}

	public String getContactProvince() {
		return contactProvince;
	}

	public void setContactProvince(String contactProvince) {
		this.contactProvince = contactProvince;
	}

	public String getContactCountry() {
		return contactCountry;
	}

	public void setContactCountry(String contactCountry) {
		this.contactCountry = contactCountry;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPostalCode() {
		return contactPostalCode;
	}

	public void setContactPostalCode(String contactPostalCode) {
		this.contactPostalCode = contactPostalCode;
	}

	public String getTelephoneOff() {
		return telephoneOff;
	}

	public void setTelephoneOff(String telephoneOff) {
		this.telephoneOff = telephoneOff;
	}

	public String getTelephoneRes() {
		return telephoneRes;
	}

	public void setTelephoneRes(String telephoneRes) {
		this.telephoneRes = telephoneRes;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getRedirectPage() {
		return redirectPage;
	}

	public void setRedirectPage(String redirectPage) {
		this.redirectPage = redirectPage;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public ArrayList<String> getApplicationTypeList() {
		return applicationTypeList;
	}

	public void setApplicationTypeList(ArrayList<String> applicationTypeList) {
		this.applicationTypeList = applicationTypeList;
	}

	public ArrayList<String> getCurrencyConversionAgentList() {
		return currencyConversionAgentList;
	}

	public void setCurrencyConversionAgentList(
			ArrayList<String> currencyConversionAgentList) {
		this.currencyConversionAgentList = currencyConversionAgentList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAdminType() {
		return adminType;
	}

	public void setAdminType(String adminType) {
		this.adminType = adminType;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ArrayList<String> getEntityList() {
		return entityList;
	}

	public void setEntityList(ArrayList<String> entityList) {
		this.entityList = entityList;
	}

	public ArrayList<String> getAdminTypeList() {
		return adminTypeList;
	}

	public void setAdminTypeList(ArrayList<String> adminTypeList) {
		this.adminTypeList = adminTypeList;
	}

	public ArrayList<String> getOfficeLocationList() {
		return officeLocationList;
	}

	public void setOfficeLocationList(ArrayList<String> officeLocationList) {
		this.officeLocationList = officeLocationList;
	}

	public String getRoleGroupName() {
		return roleGroupName;
	}

	public void setRoleGroupName(String roleGroupName) {
		this.roleGroupName = roleGroupName;
	}

	public String getRoleGroupID() {
		return roleGroupID;
	}

	public void setRoleGroupID(String roleGroupID) {
		this.roleGroupID = roleGroupID;
	}

	public ArrayList<String> getRoleGroupIdList() {
		return roleGroupIdList;
	}

	public void setRoleGroupIdList(ArrayList<String> roleGroupIdList) {
		this.roleGroupIdList = roleGroupIdList;
	}

	public String getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(String selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public ArrayList<String> getUsersList() {
		return usersList;
	}

	public void setUsersList(ArrayList<String> usersList) {
		this.usersList = usersList;
	}

	public String getMultiData() {
		return multiData;
	}

	public void setMultiData(String multiData) {
		this.multiData = multiData;
	}

	public String[] getRolelist2() {
		return rolelist2;
	}

	public void setRolelist2(String[] rolelist2) {
		this.rolelist2 = rolelist2;
	}

	/**
	 * @return the locationJSON
	 */
	public JSONObject getLocationJSON() {
		return locationJSON;
	}

	/**
	 * @param locationJSON
	 *            the locationJSON to set
	 */
	public void setLocationJSON(JSONObject locationJSON) {
		this.locationJSON = locationJSON;
	}

	@Override
	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	@Override
	public void setServletResponse(HttpServletResponse httpServletResponse) {
		// TODO Auto-generated method stub
		this.httpServletResponse =httpServletResponse;
	}

	@Override
	public void setRequest(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

}
