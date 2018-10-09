package com.ceva.base.agencybanking.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.FloatManagementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class FloatManagementAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(FloatManagementAction.class);

	private String result;
	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private JSONObject terminalJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String merchantID;
	private String storeId;
	private String merchantName;
	private String storeName;
	private String location;
	private String kraPin;
	private String storeLimit;
	private String terminalID;
	private String terminalMake;
	private String modelNumber;
	private String serialNumber;
	private String status;
	private String terminalDate;
	private String terminalLimit;
	private String storeCreditAmt;
	private String terminalCreditAmt;
	private String storeCreditRefNo;
	private String storeLimitStatus;
	private String terminalInfo;
	private String requestedBy;
	private String requestedDate;
	private String approvedBy;
	private String approvedDate;
	private String approveReject;
	private String terminalLimitStatus;
	private String terminalRequestedBy;
	private String terminalRequestedDate;
	private String storeCreditStatus;
	private String terminalReferenceNo;
	private String emailId;
	private String mailSubject;
	private String emailMessage;
	private String bccEmailId;
	private String ccEmailId;
	private String referenceNo;
	private String ticketId;
	private String storeCashDepositLimit;

	private HttpSession session = null;

	public String getCommonScreen() {
		logger.debug("Inside GetCommonScreen.. ");
		return SUCCESS;
	}

	public String limitMgmtScreen() {
		logger.debug("Inside LimitMgmtScreen..");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getLimitMgmtScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("Terminal JSON [" + terminalJSON + "]");
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
			logger.debug("Exception in LimitMgmtScreen [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreLimitCreateScreen() {
		logger.debug("Inside GetStoreLimitCreateScreen... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreLimitCreateScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetStoreLimitCreateScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreCreditCreateScreen() {
		logger.debug("Inside GetStoreCreditCreateScreen.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreCreditCreateScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetStoreLimitCreateScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreLimitCreateDetails() {
		logger.debug("inside GetStoreLimitCreateDetails.. ");

		try {
			responseJSON = new JSONObject();
			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			responseJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			responseJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			responseJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);
			responseJSON.put("storeCashDepositLimit", storeCashDepositLimit);
			result = "success";

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreLimitCreateDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String insertStoreLimitDetails() {
		logger.debug("Inside InsertStoreLimitDetails");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();

			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.insertStoreLimitDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = requestJSON;
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertStoreLimitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getStoreLimitModifyScreen() {
		logger.debug("inside Get StoreLimitModifyScreen");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreLimitModifyScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreLimitModifyScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String updateStoreLimitDetails() {

		logger.debug("Inside UpdateStoreLimitDetails.. ");

		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();

			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.updateStoreLimitDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				logger.debug("Response JSON [" + responseJSON + "]");

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = requestJSON;
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreLimitModifyScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getTerminalLimitCreateScreen() {
		logger.debug("inside GetTerminalLimitCreateScreen... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getTerminalLimitCreateScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminalLimitCreateScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getTerminalLimitCreateDetails() {
		logger.debug("Inside GetTerminalLimitCreateDetails]");

		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			responseJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			responseJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			responseJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			responseJSON.put(CevaCommonConstants.STATUS, status);
			responseJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT, terminalLimit);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
					storeLimitStatus);
			responseJSON.put(CevaCommonConstants.TERMINAL_INFO, terminalInfo);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
					approvedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
					approvedDate);
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminalLimitCreateDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
		}

		return result;
	}

	public String insertTerminalLimitDetails() {
		logger.debug("Inside InsertTerminalLimitDetails .. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			requestJSON.put(CevaCommonConstants.STATUS, status);
			requestJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT, terminalLimit);
			requestJSON.put("TerminalInfo", terminalInfo);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.insertTerminalLimitDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				result = "fail";
			}
			responseJSON = requestJSON;
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertTerminalLimitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String modifyTerminalLimitDetails() {
		logger.debug("Inside ModifyTerminalLimitDetails");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.modifyTerminalLimitDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in modifyTerminalLimitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String updateTerminalLimitDetails() {
		logger.debug("inside updateTerminalLimitDetails.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			requestJSON.put(CevaCommonConstants.STATUS, status);
			requestJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT, terminalLimit);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.updateTerminalLimitDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" [Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				result = "fail";
			}
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in updateTerminalLimitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;

	}

	public String getCreditMgmtScreen() {
		logger.debug("inside getCreditMgmtScreen.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getCreditMgmtScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("Terminal JSON [" + terminalJSON + "]");
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
			logger.debug("Exception in getCreditMgmtScreen [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreCreditCreateDetails() {
		logger.debug("inside Get StoreCreditCreateDetails.. ");
		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			responseJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			responseJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			responseJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			responseJSON.put(CevaCommonConstants.STORE_CREDIT_AMT,
					storeCreditAmt);

			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreCreditCreateDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}
		return result;
	}

	public String insertStoreCreditDetails() {
		logger.debug("inside InsertStoreCreditDetails.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;

		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			requestJSON.put(CevaCommonConstants.STORE_CREDIT_AMT,
					storeCreditAmt);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.insertStoreCreditDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" [Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				responseJSON = requestJSON;
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InsertStoreCreditDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getStoreCreditDetails() {
		logger.debug("Inside getStoreCreditDetails.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreCreditDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreCreditDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getTerminalCreditCreateScreen() {
		logger.debug("inside getTerminalCreditCreateScreen... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getTerminalCreditCreateScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminalCreditCreateScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getTerminalCreditDetails() {
		logger.debug("inside [getTerminalCreditDetails]");

		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			responseJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			responseJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			responseJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			responseJSON.put(CevaCommonConstants.STATUS, status);
			responseJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);
			responseJSON
					.put(CevaCommonConstants.REFERENCE_NO, storeCreditRefNo);
			responseJSON.put(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT,
					terminalCreditAmt);

			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminalCreditDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String insertTerminalCreateCreditDetails() {
		logger.debug("Inside InsertTerminalCreateCreditDetails.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			requestJSON.put(CevaCommonConstants.STATUS, status);
			requestJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);
			requestJSON.put(CevaCommonConstants.REFERENCE_NO, storeCreditRefNo);
			requestJSON.put(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT,
					terminalCreditAmt);
			logger.debug("[Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO
					.insertTerminalCreateCreditDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" [Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertTerminalCreateCreditDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String viewTerminalCreditDetails() {
		logger.debug("Inside ViewTerminalCreditDetails.. ");
		ArrayList<String> errors = null;
		FloatManagementDAO floatDAO = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.viewTerminalCreditDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in viewTerminalCreditDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getLimitMgmtAuthScreen() {
		logger.debug("Inside GetLimitMgmtAuthScreen... ");
		ArrayList<String> errors = null;
		FloatManagementDAO floatDAO = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getLimitMgmtAuthScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("[MerchantAction][terminalJSON:::::"
						+ terminalJSON + "]");
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
			logger.debug("Exception in getLimitMgmtAuthScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreLimitApproveDetails() {
		logger.debug("inside getStoreLimitApproveDetails.. ");
		ArrayList<String> errors = null;
		FloatManagementDAO floatDAO = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreLimitApproveDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreLimitApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String StoreLimitApproveSubmitDetails() {
		logger.debug("inside [StoreLimitApproveSubmitDetails]");
		try {

			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			responseJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			responseJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			responseJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
					storeLimitStatus);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVE,
					approveReject);
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in StoreLimitApproveSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String StoreLimitApprove() {
		logger.debug("Inside StoreLimitApprove.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;

		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
					storeLimitStatus);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVE,
					approveReject);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.StoreLimitApprove(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" [Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in StoreLimitApprove [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getterminalLimitApproveDetails() {
		logger.debug("Inside GetterminalLimitApproveDetails.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getterminalLimitApproveDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getterminalLimitApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String TerminalLimitApproveSubmitDetails() {
		logger.debug("inside [TerminalLimitApproveSubmitDetails]");

		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			responseJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			responseJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			responseJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			responseJSON.put(CevaCommonConstants.STATUS, status);
			responseJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);

			responseJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
					storeLimitStatus);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
					approvedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
					approvedDate);

			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT, terminalLimit);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
					terminalLimitStatus);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
					terminalRequestedBy);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
					terminalRequestedDate);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT,
					approveReject);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);

			responseJSON.put(CevaCommonConstants.TERMINAL_INFO, terminalInfo);

			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in TerminalLimitApproveSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String TerminalLimitApprove() {
		logger.debug("inside ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			requestJSON.put(CevaCommonConstants.STATUS, status);
			requestJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);

			requestJSON.put(CevaCommonConstants.STORE_LIMIT_STATUS,
					storeLimitStatus);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_BY,
					approvedBy);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_APPROVED_DATE,
					approvedDate);

			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT, terminalLimit);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT_STATUS,
					terminalLimitStatus);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_BY,
					terminalRequestedBy);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT_REQUEST_DATE,
					terminalRequestedDate);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT,
					approveReject);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);

			requestJSON.put(CevaCommonConstants.TERMINAL_INFO, terminalInfo);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.TerminalLimitApprove(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				result = "fail";
			}
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getterminalLimitApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String TerminalLimitApproveView() {
		logger.debug("Inside TerminalLimitApproveView.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.TerminalLimitApproveView(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getterminalLimitApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getCreditMgmtAuthScreen() {
		logger.debug("inside getCreditMgmtAuthScreen... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getCreditMgmtAuthScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("Terminal JSON [" + terminalJSON + "]");
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
			logger.debug("Exception in getCreditMgmtAuthScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreCreditApproveDetails() {
		logger.debug("Inside GetStoreCreditApproveDetails... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreCreditApproveDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				responseJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreCreditApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String StoreCreditApproveSubmitDetails() {
		logger.debug("inside [StoreCreditApproveSubmitDetails]");

		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			responseJSON.put(CevaCommonConstants.KRA_PIN, kraPin);

			responseJSON.put(CevaCommonConstants.STORE_CREDIT_AMT,
					storeCreditAmt);
			responseJSON.put(CevaCommonConstants.STORE_CREDIT_STATUS,
					storeCreditStatus);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			responseJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT,
					approveReject);

			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in StoreCreditApproveSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String StoreCreditApprove() {
		logger.debug("Inside StoreCreditApprove... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			requestJSON.put(CevaCommonConstants.KRA_PIN, kraPin);

			requestJSON.put(CevaCommonConstants.STORE_CREDIT_AMT,
					storeCreditAmt);
			requestJSON.put(CevaCommonConstants.STORE_CREDIT_STATUS,
					storeCreditStatus);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			requestJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT,
					approveReject);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.StoreCreditApprove(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" [Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in StoreCreditApprove [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreCreditApproveViewDetails() {
		logger.debug("Inside getStoreCreditApproveViewDetails.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getStoreCreditApproveViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getStoreCreditApproveViewDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getTerminalCreditApproveDetails() {
		logger.debug("inside getTerminalCreditApproveDetails... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
			requestJSON.put(CevaCommonConstants.TERMINAL_REF_NO,
					terminalReferenceNo);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getTerminalCreditApproveDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminalCreditApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getTerminalCreditApproveSubmitDetails() {
		logger.debug("inside [getTerminalCreditApproveSubmitDetails]");
		try {
			responseJSON = new JSONObject();
			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			responseJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			responseJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			responseJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			responseJSON.put(CevaCommonConstants.STATUS, status);
			responseJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);

			responseJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
			responseJSON.put(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT,
					terminalCreditAmt);
			responseJSON.put(CevaCommonConstants.TERMINAL_REF_NO,
					terminalReferenceNo);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			responseJSON.put(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT,
					approveReject);
			result = "success";

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminalCreditApproveSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String terminalCreditApprove() {
		logger.debug("inside terminalCreditApprove... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestJSON.put(CevaCommonConstants.TERMINAL_MAKE, terminalMake);
			requestJSON.put(CevaCommonConstants.MODEL_NO, modelNumber);
			requestJSON.put(CevaCommonConstants.SERIAL_NO, serialNumber);
			requestJSON.put(CevaCommonConstants.STATUS, status);
			requestJSON.put(CevaCommonConstants.TERMINAL_DATE, terminalDate);

			requestJSON.put(CevaCommonConstants.REF_NO, storeCreditRefNo);
			requestJSON.put(CevaCommonConstants.TERMINAL_CREDIT_AMOUNT,
					terminalCreditAmt);
			requestJSON.put(CevaCommonConstants.TERMINAL_REF_NO,
					terminalReferenceNo);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_BY,
					requestedBy);
			requestJSON.put(CevaCommonConstants.STORE_LIMIT_REQUEST_DATE,
					requestedDate);
			requestJSON.put(CevaCommonConstants.TERMINAL_LIMIT_APPROVE_REJECT,
					approveReject);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.terminalCreditApprove(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" [Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				result = "fail";
			}

			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in terminalCreditApprove ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String viewTerminalCreditApproveDetails() {
		logger.debug("inside viewTerminalCreditApproveDetails... ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TERMINAL_ID, terminalID);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.viewTerminalCreditApproveDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in viewTerminalCreditApproveDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String getInboxDetails() {
		logger.debug("inside ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getInboxDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.INBOX_INFO);

				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getInboxDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String inboxSubmitDetails() {
		logger.debug("inside [inboxSubmitDetails]");
		Date dNow = null;
		SimpleDateFormat ft = null;
		String referenceNo = null;
		try {

			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.TO_EMAILS, emailId);
			responseJSON.put(CevaCommonConstants.CC_EMAILS, ccEmailId);
			responseJSON.put(CevaCommonConstants.BCC_EMAILS, bccEmailId);
			responseJSON.put(CevaCommonConstants.EMAIL_SUBJECT, mailSubject);
			responseJSON.put(CevaCommonConstants.EMAIL_MESSAGE, emailMessage);
			responseJSON.put(CevaCommonConstants.TICKET_ID, ticketId);

			dNow = new Date();
			ft = new SimpleDateFormat("yyyyMMddhhmmss");
			logger.debug("Current Date: " + ft.format(dNow));
			referenceNo = ft.format(dNow);
			referenceNo = referenceNo + getRandomInteger();
			logger.debug("ReferenceNo [" + referenceNo + "]");
			responseJSON.put(CevaCommonConstants.REFERENCE_NO, referenceNo);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			logger.debug("Response JSON [" + responseJSON + "]");

			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in inboxSubmitDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			dNow = null;
			ft = null;
			referenceNo = null;
		}

		return result;
	}

	public String InboxDetailsInsert() {
		logger.debug("inside ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.TO_EMAILS, emailId);
			requestJSON.put(CevaCommonConstants.CC_EMAILS, ccEmailId);
			requestJSON.put(CevaCommonConstants.EMAIL_SUBJECT, mailSubject);
			requestJSON.put(CevaCommonConstants.EMAIL_MESSAGE, emailMessage);
			requestJSON.put(CevaCommonConstants.REFERENCE_NO, referenceNo);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestJSON.put(CevaCommonConstants.TICKET_ID, ticketId);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.InboxDetailsInsert(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				result = "fail";
			}

			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InboxDetailsInsert [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getcreditDashBoard() {

		logger.debug("inside getcreditDashBoard.. ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getcreditDashBoard(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.CREDIT_DASHBOARD);
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
			logger.debug("Exception in getcreditDashBoard [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	public String BranchTicketDetails() {
		logger.debug("inside ");
		FloatManagementDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new FloatManagementDAO();
			responseDTO = floatDAO.getInboxDashBoard(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.CREDIT_DASHBOARD);
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
			logger.debug("Exception in BranchTicketDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			floatDAO = null;
			errors = null;
		}

		return result;
	}

	private static String getRandomInteger() {
		int aStart = 0;
		int aEnd = 0;
		Random aRandom = null;
		long range = 0;
		long fraction = 0;
		Long randomNumber = 0L;
		try {
			aStart = 1000;
			aEnd = 9999;
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

	public String getTerminalCreditAmt() {
		return terminalCreditAmt;
	}

	public void setTerminalCreditAmt(String terminalCreditAmt) {
		this.terminalCreditAmt = terminalCreditAmt;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public String getStoreLimit() {
		return storeLimit;
	}

	public void setStoreLimit(String storeLimit) {
		this.storeLimit = storeLimit;
	}

	public JSONObject getTerminalJSON() {
		return terminalJSON;
	}

	public void setTerminalJSON(JSONObject terminalJSON) {
		this.terminalJSON = terminalJSON;
	}

	public String getTerminalID() {
		return terminalID;
	}

	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
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

	public String getTerminalLimit() {
		return terminalLimit;
	}

	public void setTerminalLimit(String terminalLimit) {
		this.terminalLimit = terminalLimit;
	}

	public String getStoreCreditAmt() {
		return storeCreditAmt;
	}

	public void setStoreCreditAmt(String storeCreditAmt) {
		this.storeCreditAmt = storeCreditAmt;
	}

	public String getStoreCreditRefNo() {
		return storeCreditRefNo;
	}

	public void setStoreCreditRefNo(String storeCreditRefNo) {
		this.storeCreditRefNo = storeCreditRefNo;
	}

	public String getStoreLimitStatus() {
		return storeLimitStatus;
	}

	public void setStoreLimitStatus(String storeLimitStatus) {
		this.storeLimitStatus = storeLimitStatus;
	}

	public String getTerminalInfo() {
		return terminalInfo;
	}

	public void setTerminalInfo(String terminalInfo) {
		this.terminalInfo = terminalInfo;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(String approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApproveReject() {
		return approveReject;
	}

	public void setApproveReject(String approveReject) {
		this.approveReject = approveReject;
	}

	public String getTerminalLimitStatus() {
		return terminalLimitStatus;
	}

	public void setTerminalLimitStatus(String terminalLimitStatus) {
		this.terminalLimitStatus = terminalLimitStatus;
	}

	public String getTerminalRequestedBy() {
		return terminalRequestedBy;
	}

	public void setTerminalRequestedBy(String terminalRequestedBy) {
		this.terminalRequestedBy = terminalRequestedBy;
	}

	public String getTerminalRequestedDate() {
		return terminalRequestedDate;
	}

	public void setTerminalRequestedDate(String terminalRequestedDate) {
		this.terminalRequestedDate = terminalRequestedDate;
	}

	public String getStoreCreditStatus() {
		return storeCreditStatus;
	}

	public void setStoreCreditStatus(String storeCreditStatus) {
		this.storeCreditStatus = storeCreditStatus;
	}

	public String getTerminalReferenceNo() {
		return terminalReferenceNo;
	}

	public void setTerminalReferenceNo(String terminalReferenceNo) {
		this.terminalReferenceNo = terminalReferenceNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}

	public String getBccEmailId() {
		return bccEmailId;
	}

	public void setBccEmailId(String bccEmailId) {
		this.bccEmailId = bccEmailId;
	}

	public String getCcEmailId() {
		return ccEmailId;
	}

	public void setCcEmailId(String ccEmailId) {
		this.ccEmailId = ccEmailId;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getStoreCashDepositLimit() {
		return storeCashDepositLimit;
	}

	public void setStoreCashDepositLimit(String storeCashDepositLimit) {
		this.storeCashDepositLimit = storeCashDepositLimit;
	}
}
