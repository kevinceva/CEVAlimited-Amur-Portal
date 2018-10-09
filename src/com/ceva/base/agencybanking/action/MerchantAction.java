package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.agencybanking.usermgmt.UserManagementFileGeneratorJob;
import com.ceva.base.common.dao.MerchantDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class MerchantAction extends ActionSupport implements
		ServletRequestAware {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(MerchantAction.class);

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject terminalJSON = null;
	JSONObject userJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;

	static char[] hexval = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	private String merchantName;
	private String merchantID;
	private String storeId;
	private String storeName;
	private String location;
	private String kraPin;
	private String merchantType;
	private String managerName;
	private String email;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String poBoxNumber;
	private String telephoneNumber1;
	private String telephoneNumber2;
	private String mobileNumber;
	private String faxNumber;
	private String prmContactPerson;
	private String prmContactNumber;
	private String bankMultiData;
	private String bankAcctMultiData;
	private String documentMultiData;
	private String terminalMakeMultiData;
	private String terminalID;
	private String terminalUsage;
	private String terminalMake;
	private String modelNumber;
	private String serialNumber;
	private String pinEntry;
	private String validFrom;
	private String validThru;
	private String status;
	private String terminalDate;
	private String selectUsers;
	private String memberType;
	private String tpkIndex;
	private String tpkKey;
	private String selectedServices;
	private String tillId;
	private String agentMultiData;
	private String locationVal;
	private String merchantTypeVal;
	private String supervisor;
	private String admin;
	private String selectedUserText;

	private HttpSession session = null;

	public String merchantCreatePage() {
		logger.debug("inside GetMerchantCreatePage.. ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getMerchantCreatePageInfo(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_INFO);
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
			logger.debug("Exception in getMerchantCreatePage ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			merchantDAO = null;
			errors = null;
		}

		return result;
	}

	public String getMerchantCreateSubmitDetails() {

		logger.debug("Inside GetMerchantCreateSubmitDetails.. ");
		MerchantDAO merchantDAO = null;
		JSONObject responseJSON1 = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			responseJSON1 = new JSONObject();
			
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getMerchantCreatePageInfo(requestDTO);
			synchronized (this) {
				responseJSON1 = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_INFO);

				responseJSON = constructToResponseCurrentJson(request, responseJSON1);
			}

			logger.debug("ResponseJSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetMerchantCreateSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			merchantDAO = null;
		}

		return result;
	}

	private JSONObject constructToResponseCurrentJson(
			HttpServletRequest httpRequest, JSONObject jsonObject) {

		Enumeration<?> enumParams = null;
		logger.debug("Start ConstructToResponseJson... ");
		String key = "";
		String val = "";

		try {
			enumParams = httpRequest.getParameterNames();
			if (jsonObject == null) {
				jsonObject = new JSONObject();
			} else {
				while (enumParams.hasMoreElements()) {
					key = (String) enumParams.nextElement();
					val = httpRequest.getParameter(key);
					jsonObject.put(key, val);
				}
			}

		} catch (Exception e) {
			logger.debug("Exception while converting to httpreq to bean["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
			key = null;
			val = null;
		}
		logger.debug("End ConstructToResponseJson... ");
		return jsonObject;
	}

	public String StoreCreateSubmittedDetails() {

		logger.debug("Inside StoreCreateSubmittedDetails.. ");
		MerchantDAO merchantDAO = null;
		JSONObject responseJSON1 = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			responseJSON1 = new JSONObject();
			
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getMerchantCreatePageInfo(requestDTO);
			synchronized (this) {
				responseJSON1 = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_INFO);

				responseJSON = constructToResponseCurrentJson(request, responseJSON1);
				
				responseJSON.put(CevaCommonConstants.MAKER_ID, session
						.getAttribute(CevaCommonConstants.MAKER_ID) == null ? " "
						: session.getAttribute(CevaCommonConstants.MAKER_ID));
				
			}

			logger.debug("ResponseJSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in StoreCreateSubmittedDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			merchantDAO = null;
		}
		return result;
	}

	public String insertMerchantDetails() {

		logger.debug("Inside InsertMerchantDetails... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (bankAcctMultiData == null) {
				addActionError("Records Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON
						.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
				requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
				requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
				requestJSON
						.put(CevaCommonConstants.MERCHANT_TYPE, merchantType);
				requestJSON.put(CevaCommonConstants.MEMBER_TYPE, memberType);
				requestJSON.put(CevaCommonConstants.MANAGER_NAME, managerName);
				requestJSON.put(CevaCommonConstants.EMAIL_ID, email);
				requestJSON.put(CevaCommonConstants.ADDRESS1, addressLine1);
				requestJSON.put(CevaCommonConstants.ADDRESS2, addressLine2);
				if (addressLine3 != null) {
					requestJSON.put(CevaCommonConstants.ADDRESS3, addressLine3);
				}
				requestJSON.put(CevaCommonConstants.CITY, city);
				requestJSON.put(CevaCommonConstants.POBOXNUMBER, poBoxNumber);
				requestJSON.put(CevaCommonConstants.TELEPHONE1,
						telephoneNumber1);
				if (telephoneNumber2 != null) {
					requestJSON.put(CevaCommonConstants.TELEPHONE2,
							telephoneNumber2);
				}
				requestJSON
						.put(CevaCommonConstants.MOBILE_NUMBER, mobileNumber);
				requestJSON.put(CevaCommonConstants.FAX_NUMBER, faxNumber);
				requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						prmContactPerson);
				requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						prmContactNumber);

				requestJSON.put(CevaCommonConstants.BANK_ACCT_MULTI_DATA,
						bankAcctMultiData);
				if (documentMultiData != null) {
					requestJSON.put(CevaCommonConstants.DOCUMENT_MULTI_DATA,
							documentMultiData);
				}
				if (agentMultiData != null) {
					requestJSON.put(CevaCommonConstants.AGENT_MULTI_DATA,
							agentMultiData);
				}

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request DTO [" + requestDTO + "]");
				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.insertMerchantDetails(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ENTITY_LIST);
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
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ENTITY_LIST);
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
			logger.debug("Exception in insertMerchantDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			messages = null;
			merchantDAO = null;
		}

		return result;
	}

	public String getMerchantDetails() {
		logger.debug("inside getMerchantDetails.. ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getMerchantDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("Terminal JSON [" + terminalJSON + "]");
				userJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.USER_DATA);
				logger.debug("User JSON [" + userJSON + "]");
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
			logger.debug("Exception in getMerchantDetails [" + e.getMessage()
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

	public String getMerchantModifySubmitDetails() {

		logger.debug("Inside GetMerchantModifySubmitDetails....");
		// JSONObject result = null;
		try {
			constructToResponseCurrentJson(request, responseJSON);
			logger.debug("ResponseJSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in StoreCreateSubmittedDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
		}

		return result;
	}

	public String getStoreCreatePage() {
		logger.debug("Inside MerchantID].. ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;

		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getStoreCreatePageInfo(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				storeId = responseJSON.getString(CevaCommonConstants.STORE_ID);

				storeId = merchantID.substring(0, 5) + "-" + "S"
						+ StringUtils.leftPad(storeId, 3, "0");

				logger.debug("Store Id [" + storeId + "]");
				responseJSON.put(CevaCommonConstants.STORE_ID, storeId);

				tillId = getRandomInteger();
				logger.debug("Till Id [" + tillId + "]");
				responseJSON.put(CevaCommonConstants.TILL_ID, tillId);
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
			logger.debug("Exception in getStoreCreatePage [" + e.getMessage()
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

	public String getStoreCreateSubmitDetails() {

		logger.debug("Inside [getStoreCreateSubmitDetails]");
		MerchantDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestDTO.setRequestJSON(requestJSON);
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getStoreCreatePageInfo(requestDTO);
			responseJSON = (JSONObject) responseDTO.getData().get(
					CevaCommonConstants.STORE_INFO);
			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreCreatePage [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			merchantDAO = null;
		}

		return result;
	}

	public String insertStoreDetails() {

		logger.debug("Inside InsertStoreDetails.. ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (storeId == null) {
				addActionError(" storeId Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON
						.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				logger.debug("Storeid:=" + storeId);
				requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
				requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
				requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
				logger.debug("kraPin:"+kraPin);
				requestJSON.put(CevaCommonConstants.MANAGER_NAME, managerName);
				requestJSON.put(CevaCommonConstants.EMAIL_ID, email);
				requestJSON.put(CevaCommonConstants.ADDRESS1, addressLine1);
				requestJSON.put(CevaCommonConstants.ADDRESS2, addressLine2);
				if (addressLine3 != null) {
					requestJSON.put(CevaCommonConstants.ADDRESS3, addressLine3);
				}
				requestJSON.put(CevaCommonConstants.CITY, city);
				requestJSON.put(CevaCommonConstants.POBOXNUMBER, poBoxNumber);
				requestJSON.put(CevaCommonConstants.TELEPHONE1,
						telephoneNumber1);
				if (telephoneNumber2 != null) {
					requestJSON.put(CevaCommonConstants.TELEPHONE2,
							telephoneNumber2);
				}
				requestJSON
						.put(CevaCommonConstants.MOBILE_NUMBER, mobileNumber);
				requestJSON.put(CevaCommonConstants.FAX_NUMBER, faxNumber);
				requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						prmContactPerson);
				requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						prmContactNumber);
				requestJSON.put(CevaCommonConstants.TILL_ID, tillId);

				requestJSON.put(CevaCommonConstants.BANK_ACCT_MULTI_DATA,
						bankAcctMultiData);
				if (documentMultiData != null) {
					requestJSON.put(CevaCommonConstants.DOCUMENT_MULTI_DATA,
							documentMultiData);
				}
				if (agentMultiData != null) {
					requestJSON.put(CevaCommonConstants.AGENT_MULTI_DATA,
							agentMultiData);
				}
				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Response DTO [" + requestDTO + "]");
				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.insertStoreDetails(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

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
			logger.debug("Result [" + result + "]");

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertStoreDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			messages = null;
			merchantDAO = null;
		}

		return result;

	}

	public String insertTerminalDetails() {

		logger.debug("inside insertTerminalDetails.. ");

		MerchantDAO merchantDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;

		try {

			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (merchantID == null) {
				addActionError("MerchantID Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
				requestJSON.put(CevaCommonConstants.TERMINAL_USAGE,
						terminalUsage);
				requestJSON
						.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
				requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
				requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
				requestJSON
						.put(CevaCommonConstants.PINENTRY,
								(pinEntry == null || pinEntry
										.equalsIgnoreCase("")) ? "NO_VAL"
										: pinEntry);
				requestJSON.put(CevaCommonConstants.VALID_FROM, validFrom);
				requestJSON.put(CevaCommonConstants.VALID_THRU, validThru);
				requestJSON.put(CevaCommonConstants.STATUS, status);
				requestJSON
						.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);
				requestJSON.put(CevaCommonConstants.TMK_INDEX, tpkIndex);
				requestJSON.put(CevaCommonConstants.TPK_KEY, tpkKey);

				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request DTO [" + requestDTO + "]");
				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.insertTerminalDetails(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

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
			logger.debug("Result [" + result + "]");

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertStoreDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			messages = null;
			merchantDAO = null;
		}

		return result;
	}

	public String getMerchantViewDetails() {
		logger.debug("Inside GetMerchantViewDetails... ");
		ArrayList<String> errors = null;
		MerchantDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getMerchantViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_INFO);
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
			logger.debug("Exception in insertStoreDetails [" + e.getMessage()
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

	public String merchantTerminate() {
		logger.debug("Inside MerchantTerminate... ");
		ArrayList<String> errors = null;
		MerchantDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.merchantTerminate(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
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
			logger.debug("Exception in merchantTerminate [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			merchantDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreViewDetails() {
		logger.debug("Inside GetStoreViewDetails..  ");
		ArrayList<String> errors = null;
		MerchantDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			if (merchantID != null) {
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			}
			if (merchantID != null) {
				requestJSON
						.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			}
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getStoreViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
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
			logger.debug("Exception in GetStoreViewDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			merchantDAO = null;
			errors = null;
		}

		return result;
	}

	public String storeTerminate() {
		logger.debug("Inside StoreTerminate.. ");
		ArrayList<String> errors = null;
		MerchantDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.storeTerminate(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
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
			logger.debug("Exception in storeTerminate [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			merchantDAO = null;
			errors = null;
		}

		return result;
	}

	public String getTerminalviewDetails() {

		logger.debug("Inside GetTerminalviewDetails... ");
		ArrayList<String> errors = null;
		MerchantDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [::" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getTerminalviewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
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
			logger.debug("Exception in getTerminalviewDetails ["
					+ e.getMessage() + "]");
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

	public String terminateTerminal() {

		logger.debug("Inide TerminateTerminal... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [::" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.terminateTerminal(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
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
			logger.debug("Exception in TerminateTerminal [" + e.getMessage()
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

	public String modifyTerminal() {

		logger.debug("Inside ModifyTerminal... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (merchantID == null) {
				addActionError(" merchantID Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
				requestJSON.put(CevaCommonConstants.TERMINAL_USAGE,
						terminalUsage);
				requestJSON
						.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
				requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
				requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
				requestJSON.put(CevaCommonConstants.PINENTRY, pinEntry);
				requestJSON.put(CevaCommonConstants.VALID_FROM, validFrom);
				requestJSON.put(CevaCommonConstants.VALID_THRU, validThru);
				requestJSON.put(CevaCommonConstants.STATUS, status);
				requestJSON
						.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request DTO [" + requestDTO + "]");
				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.updateTerminalDetails(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.MERCHANT_LIST);
					logger.debug("Response JSON [" + responseJSON + "]");
					logger.debug("Getting messages from DB");
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
			logger.debug("Result [" + result + "]");

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in ModifyTerminal [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			merchantDAO = null;

		}

		return result;
	}

	public String updateStore() {

		logger.debug("Inside UpdateStore... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			if (storeId == null) {
				addActionError("StoreId Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON
						.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
				requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
				requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
				requestJSON.put(CevaCommonConstants.MANAGER_NAME, managerName);
				requestJSON.put(CevaCommonConstants.EMAIL_ID, email);
				requestJSON.put(CevaCommonConstants.ADDRESS1, addressLine1);
				requestJSON.put(CevaCommonConstants.ADDRESS2, addressLine2);
				if (addressLine3 != null) {
					requestJSON.put(CevaCommonConstants.ADDRESS3, addressLine3);
				}
				requestJSON.put(CevaCommonConstants.CITY, city);
				requestJSON.put(CevaCommonConstants.POBOXNUMBER, poBoxNumber);
				requestJSON.put(CevaCommonConstants.TELEPHONE1,
						telephoneNumber1);
				if (telephoneNumber2 != null) {
					requestJSON.put(CevaCommonConstants.TELEPHONE2,
							telephoneNumber2);
				}
				requestJSON
						.put(CevaCommonConstants.MOBILE_NUMBER, mobileNumber);
				requestJSON.put(CevaCommonConstants.FAX_NUMBER, faxNumber);
				requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
						prmContactPerson);
				requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
						prmContactNumber);

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request DTO [" + requestDTO + "]");
				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.updateStoreDetails(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

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
					logger.debug("Getting error from DB");

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
			logger.debug("Exception in UpdateStore [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			merchantDAO = null;

		}

		return result;

	}

	public String getUserstoTerminal() {

		logger.debug("Inside GetUserstoTerminal... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getUserstoTerminal(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
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
			logger.debug("Exception in getUserstoTerminal [" + e.getMessage()
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

	public String assignUserToTerminal() {

		logger.debug(" Inside AssignUserToTerminal... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;

		String fileName = "";
		try {
			
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			if (merchantID == null) {
				addActionError("Merchant ID Missing");
				result = "fail";
			} else if (storeId == null) {
				addActionError("Store Id Missing");
				result = "fail";
			} else if (terminalID == null) {
				addActionError("Terminal Id Missing");
				result = "fail";
			} else if (selectUsers == null) {
				addActionError("Selected Users Missing");
				result = "fail";
			} else if (supervisor == null) {
				addActionError("Supervisor Missing");
				result = "fail";
			} else if (admin == null) {
				addActionError("Admin Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
				requestJSON
						.put(CevaCommonConstants.SELECTED_USERS, selectUsers);
				requestJSON.put("SUPERVISOR", supervisor);
				requestJSON.put("ADMIN", admin);
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request JSON [" + requestJSON);

				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.assignUsers(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ROLE_DATA);
					logger.debug("Response JSON [" + responseJSON);
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}

					fileName = UserManagementFileGeneratorJob.generateCsvFile();
					logger.debug("FileName [" + fileName + "]");
					/*
					 * status = FTPUpload.doSftp(fileName); if (status) { result
					 * = "success"; } else { result = "fail"; addActionError(
					 * "Unable to do FTP, please check the Connection."); }
					 */
					result = "success";
				} else {
					logger.debug("Getting error from DAO.");
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ROLE_DATA);
					logger.debug("Response JSON [" + responseJSON);
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
			logger.debug("Exception in assignUserToTerminal [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			merchantDAO = null;
			fileName = null;

		}

		return result;

	}

	public String assignUserterminalSubmittedDetails() {

		logger.debug("Inside AssignUserterminalSubmittedDetails... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getUserstoTerminal(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
				responseJSON.put(CevaCommonConstants.SELECTED_USERS,
						selectUsers);
				requestJSON.put("SUPERVISOR", supervisor);
				requestJSON.put("ADMIN", admin);
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
			logger.debug("Exception in assignUserterminalSubmittedDetails ["
					+ e.getMessage() + "]");
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

	public String getAutoGeneratedTerminal() {

		logger.debug("Inside GetAutoGeneratedTerminal.. ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		String tpkKey = null;

		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getAutoGeneratedTerminal(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalID = responseJSON
						.getString(CevaCommonConstants.TERMINAL_ID);
				if (terminalID.length() == 1) {
					terminalID = "T000000" + terminalID;
				}
				if (terminalID.length() == 2) {
					terminalID = "T00000" + terminalID;
				}
				if (terminalID.length() == 3) {
					terminalID = "T0000" + terminalID;
				}
				tpkKey = getRandomValue();
				responseJSON.put(CevaCommonConstants.TPK_KEY, tpkKey);
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
			logger.debug("Exception in GetAutoGeneratedTerminal ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			merchantDAO = null;

			tpkKey = null;
		}

		return result;

	}

	public String getMerchantDashBoard() {

		logger.debug("Inside GetMerchantDashBoard.... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getMerchantDashBoard(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DASHBOARD);
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
			logger.debug("Exception in GetMerchantDashBoard [" + e.getMessage()
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

	public String getStoreViewDashboardDetails() {
		logger.debug("Inside GetStoreViewDashboardDetails.. ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request JSON [" + requestJSON + "]");

			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getStoreViewDashboardDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
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
			logger.debug("Exception in getStoreViewDashboardDetails ["
					+ e.getMessage() + "]");
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

	public String getTerminalDashboardviewDetails() {

		logger.debug("Inside GetTerminalDashboardviewDetails... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO
					.getTerminalDashboardviewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
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
			logger.debug("Exception in getTerminalDashboardviewDetails ["
					+ e.getMessage() + "]");
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

	public String getUserstoServices() {

		logger.debug("Inside GetUserstoServices... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getUserstoServices(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
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
			logger.debug("Exception in getUserstoServices [" + e.getMessage()
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

	public String ServiceTerminalMappingDetails() {

		logger.debug("Inside ServiceTerminalMappingDetails... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.getUserstoServices(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
				responseJSON.put(CevaCommonConstants.SELECTED_SERVICES,
						selectedServices);
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
			logger.debug("Exception in ServiceTerminalMappingDetails ["
					+ e.getMessage() + "]");
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

	public String assignServiceToTerminal() {

		logger.debug(" Inside AssignServiceToTerminal... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			if (merchantID == null) {
				addActionError(" Merchant ID Missing");
				result = "fail";
			} else if (storeId == null) {
				addActionError("Store Id Missing");
				result = "fail";
			} else if (terminalID == null) {
				addActionError("Terminal Id Missing");
				result = "fail";
			} else if (selectedServices == null) {
				addActionError("Selected Services Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
				requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
				requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
				requestJSON.put(CevaCommonConstants.SELECTED_SERVICES,
						selectedServices);
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request JSON [" + requestJSON);

				merchantDAO = new MerchantDAO();
				responseDTO = merchantDAO.assignServices(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
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
					logger.debug("Response JSON [" + responseJSON);
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
			logger.debug("Exception in AssignServiceToTerminal ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;

			merchantDAO = null;
		}

		return result;

	}

	public String assignServiceToTerminalView() {

		logger.debug("Inside AssignServiceToTerminalView... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.assignServiceToTerminalView(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
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
			logger.debug("Exception in AssignServiceToTerminalView ["
					+ e.getMessage() + "]");
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

	public String merchantModify() {

		logger.debug("Inside MerchantModify... ");
		MerchantDAO merchantDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.MANAGER_NAME, managerName);
			requestJSON.put(CevaCommonConstants.EMAIL_ID, email);
			requestJSON.put(CevaCommonConstants.ADDRESS1, addressLine1);
			requestJSON.put(CevaCommonConstants.ADDRESS2, addressLine2);
			if (addressLine3 != null) {
				requestJSON.put(CevaCommonConstants.ADDRESS3, addressLine3);
			}
			requestJSON.put(CevaCommonConstants.CITY, city);
			requestJSON.put(CevaCommonConstants.POBOXNUMBER, poBoxNumber);
			requestJSON.put(CevaCommonConstants.TELEPHONE1, telephoneNumber1);
			if (telephoneNumber2 != null) {
				requestJSON.put(CevaCommonConstants.TELEPHONE2,
						telephoneNumber2);
			}
			requestJSON.put(CevaCommonConstants.MOBILE_NUMBER, mobileNumber);
			requestJSON.put(CevaCommonConstants.FAX_NUMBER, faxNumber);
			requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NAME,
					prmContactPerson);
			requestJSON.put(CevaCommonConstants.PRIMARY_CONTACT_NUMBER,
					prmContactNumber);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new MerchantDAO();
			responseDTO = merchantDAO.merchantModify(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.ENTITY_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				logger.debug("Getting messages from DB");
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				result = "success";
			} else {
				logger.debug("Getting error from DB.");
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug(" Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				responseJSON = requestJSON;

				result = "fail";
			}

			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in MerchantModify [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;

			merchantDAO = null;
		}

		return result;
	}

	public static String getRandomValue() {
		Random random = null;
		StringBuilder sb = null;
		try {
			random = new Random();
			sb = new StringBuilder();
			for (int i = 0; i < 32; i++) {
				sb.append(hexval[random.nextInt(15)]);
			}
		} catch (Exception e) {
			return " ";
		}

		return sb.toString();

	}

	private static String getRandomInteger() {
		int aStart = 0;
		int aEnd = 0;
		Random aRandom = null;
		long range = 0;
		long fraction = 0;
		Long randomNumber = 0L;
		try {
			aStart = 100000;
			aEnd = 999999;
			aRandom = new Random();
			if (aStart > aEnd) {
				throw new IllegalArgumentException("Start cannot exceed End.");
			}
			range = (long) aEnd - (long) aStart + 1;
			fraction = (long) (range * aRandom.nextDouble());
			randomNumber = (Long) (fraction + aStart);

		} catch (Exception e) {
			randomNumber = 0L;
		}

		return randomNumber.toString();

	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantID() {
		return merchantID;
	}

	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getKraPin() {
		return kraPin;
	}

	public void setKraPin(String kraPin) {
		this.kraPin = kraPin;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPoBoxNumber() {
		return poBoxNumber;
	}

	public void setPoBoxNumber(String poBoxNumber) {
		this.poBoxNumber = poBoxNumber;
	}

	public String getTelephoneNumber1() {
		return telephoneNumber1;
	}

	public void setTelephoneNumber1(String telephoneNumber1) {
		this.telephoneNumber1 = telephoneNumber1;
	}

	public String getTelephoneNumber2() {
		return telephoneNumber2;
	}

	public void setTelephoneNumber2(String telephoneNumber2) {
		this.telephoneNumber2 = telephoneNumber2;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getPrmContactPerson() {
		return prmContactPerson;
	}

	public void setPrmContactPerson(String prmContactPerson) {
		this.prmContactPerson = prmContactPerson;
	}

	public String getPrmContactNumber() {
		return prmContactNumber;
	}

	public void setPrmContactNumber(String prmContactNumber) {
		this.prmContactNumber = prmContactNumber;
	}

	public String getBankMultiData() {
		return bankMultiData;
	}

	public void setBankMultiData(String bankMultiData) {
		this.bankMultiData = bankMultiData;
	}

	public String getBankAcctMultiData() {
		return bankAcctMultiData;
	}

	public void setBankAcctMultiData(String bankAcctMultiData) {
		this.bankAcctMultiData = bankAcctMultiData;
	}

	public String getDocumentMultiData() {
		return documentMultiData;
	}

	public void setDocumentMultiData(String documentMultiData) {
		this.documentMultiData = documentMultiData;
	}

	public String getTerminalMakeMultiData() {
		return terminalMakeMultiData;
	}

	public void setTerminalMakeMultiData(String terminalMakeMultiData) {
		this.terminalMakeMultiData = terminalMakeMultiData;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	public String getTerminalUsage() {
		return terminalUsage;
	}

	public void setTerminalUsage(String terminalUsage) {
		this.terminalUsage = terminalUsage;
	}

	public String getTerminalMake() {
		return terminalMake;
	}

	public void setTerminalMake(String terminalMake) {
		this.terminalMake = terminalMake;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getPinEntry() {
		return pinEntry;
	}

	public void setPinEntry(String pinEntry) {
		this.pinEntry = pinEntry;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidThru() {
		return validThru;
	}

	public void setValidThru(String validThru) {
		this.validThru = validThru;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminalDate() {
		return terminalDate;
	}

	public void setTerminalDate(String terminalDate) {
		this.terminalDate = terminalDate;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public JSONObject getTerminalJSON() {
		return terminalJSON;
	}

	public void setTerminalJSON(JSONObject terminalJSON) {
		this.terminalJSON = terminalJSON;
	}

	public String getSelectUsers() {
		return selectUsers;
	}

	public void setSelectUsers(String selectUsers) {
		this.selectUsers = selectUsers;
	}

	public JSONObject getUserJSON() {
		return userJSON;
	}

	public void setUserJSON(JSONObject userJSON) {
		this.userJSON = userJSON;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public String getTpkIndex() {
		return tpkIndex;
	}

	public void setTpkIndex(String tpkIndex) {
		this.tpkIndex = tpkIndex;
	}

	public String getTpkKey() {
		return tpkKey;
	}

	public void setTpkKey(String tpkKey) {
		this.tpkKey = tpkKey;
	}

	public String CommonScreen() {
		logger.debug("UInside GetCommonScreen...");
		responseJSON = constructToResponseJson(request);
		result = "success";
		return result;
	}

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {

		JSONObject jsonObject = null;
		Enumeration<?> enumParams = null;
		logger.debug("Inside ConstructToResponseJson... ");
		String key = "";
		String val = "";

		try {
			enumParams = httpRequest.getParameterNames();
			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				key = (String) enumParams.nextElement();
				val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}
		} catch (Exception e) {
			logger.debug("Exception while converting to httpreq to bean["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
			key = null;
			val = null;
		}

		return jsonObject;
	}

	public String getMerchantSubmitDetails() {
		logger.debug("Inside GetMerchantSubmitDetails....");
		responseJSON = constructToResponseJson(request);
		result = "success";
		return result;
	}

	public String getSelectedServices() {
		return selectedServices;
	}

	public void setSelectedServices(String selectedServices) {
		this.selectedServices = selectedServices;
	}

	public String getTillId() {
		return tillId;
	}

	public void setTillId(String tillId) {
		this.tillId = tillId;
	}

	public String getAgentMultiData() {
		return agentMultiData;
	}

	public void setAgentMultiData(String agentMultiData) {
		this.agentMultiData = agentMultiData;
	}

	public String getLocationVal() {
		return locationVal;
	}

	public void setLocationVal(String locationVal) {
		this.locationVal = locationVal;
	}

	public String getMerchantTypeVal() {
		return merchantTypeVal;
	}

	public void setMerchantTypeVal(String merchantTypeVal) {
		this.merchantTypeVal = merchantTypeVal;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getSelectedUserText() {
		return selectedUserText;
	}

	public void setSelectedUserText(String selectedUserText) {
		this.selectedUserText = selectedUserText;
	}

}
