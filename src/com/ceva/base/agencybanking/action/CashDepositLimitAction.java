package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CashDepositLimitDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class CashDepositLimitAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(CashDepositLimitAction.class);

	private String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject terminalJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

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
	private String accountMultiData;

	private HttpSession session = null;

	public String getCommonScreen() {
		logger.debug("Inside GetCommonScreen.. ");
		result = "success";
		return result;
	}

	public String getCashDepositLimitGenerateGrid() {
		logger.debug("Inside GetCashDepositLimitGenerateGrid... ");
		CashDepositLimitDAO cashDepositDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			cashDepositDAO = new CashDepositLimitDAO();
			responseDTO = cashDepositDAO
					.getCashDepositLimitGenerateGrid(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
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
			logger.debug("Exception in GetCashDepositLimitGenerateGrid ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cashDepositDAO = null;
			errors = null;
		}

		return result;
	}

	public String getStoreCashDepositLimitCreateScreen() {
		logger.debug("Inside GetStoreCashDepositLimitCreateScreen.. ");
		CashDepositLimitDAO cashDepositDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cashDepositDAO = new CashDepositLimitDAO();
			responseDTO = cashDepositDAO
					.getStoreCashDepositLimitCreateScreen(requestDTO);
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
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("TerminalJSON [" + terminalJSON + "]");
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetStoreCashDepositLimitCreateScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cashDepositDAO = null;
			errors = null;
		}
		return result;
	}

	public String getStoreCashDepositLimitCreateDetails() {
		logger.debug("Inside GetStoreCashDepositLimitCreateDetails.. ");

		try {
			responseJSON = new JSONObject();
			result = "success";
			responseJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			responseJSON.put(CevaCommonConstants.STORE_ID, storeId);
			responseJSON.put(CevaCommonConstants.MERCHANT_NAME, merchantName);
			responseJSON.put(CevaCommonConstants.STORE_NAME, storeName);
			responseJSON.put(CevaCommonConstants.LOCATION_NAME, location);
			responseJSON.put(CevaCommonConstants.KRA_PIN, kraPin);
			responseJSON.put(CevaCommonConstants.STORE_LIMIT, storeLimit);
		} catch (Exception e) {
			result = "success";
			logger.debug("Exception in GetStoreCashDepositLimitCreateDetails ["
					+ e.getMessage() + "]");
		}

		return result;
	}

	public String insertStoreCashDepositLimitDetails() {
		logger.debug("Inside InsertStoreCashDepositLimitDetails.. ");
		CashDepositLimitDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

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
			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new CashDepositLimitDAO();
			responseDTO = floatDAO
					.insertStoreCashDepositLimitDetails(requestDTO);
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
			logger.debug("Exception in GetStoreCashDepositLimitCreateScreen ["
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

	public String getStoreCashDepositLimitModifyScreen() {
		logger.debug("Inside GetStoreCashDepositLimitModifyScreen... ");
		CashDepositLimitDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new CashDepositLimitDAO();
			responseDTO = floatDAO
					.getStoreCashDepositLimitModifyScreen(requestDTO);
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
			logger.debug("Exception in GetStoreCashDepositLimitModifyScreen ["
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

	public String updateStoreCashDepositLimitDetails() {
		logger.debug("Inside UpdateStoreCashDepositLimitDetails.. ");
		CashDepositLimitDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
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
			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new CashDepositLimitDAO();
			responseDTO = floatDAO
					.updateStoreCashDepositLimitDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
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
			logger.debug("Exception in UpdateStoreCashDepositLimitDetails ["
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

	public String cashDepositLimitAuthMgmtScreen() {
		logger.debug("Inside CashDepositLimitAuthMgmtScreen... ");
		CashDepositLimitDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new CashDepositLimitDAO();
			responseDTO = floatDAO.cashDepositLimitAuthMgmtScreen(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
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
			logger.debug("Exception in CashDepositLimitAuthMgmtScreen ["
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

	public String getStoreCashDepositLimitApproveDetails() {
		logger.debug("Inside GetStoreCashDepositLimitApproveDetails... ");
		CashDepositLimitDAO floatDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID, merchantID);
			requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			floatDAO = new CashDepositLimitDAO();
			responseDTO = floatDAO
					.getStoreCashDepositLimitApproveDetails(requestDTO);
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
			logger.debug("Exception in GetStoreCashDepositLimitApproveDetails ["
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

	public String StoreCashDepositLimitApproveSubmitDetails() {
		logger.debug("Inside StoreCashDepositLimitApproveSubmitDetails... ");
		result = "success";
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
		} catch (Exception e) {
			logger.debug("Exception in StoreCashDepositLimitApproveSubmitDetails ["
					+ e.getMessage() + "]");
		}

		return result;
	}

	public String StoreCashDepositLimitApprove() {
		logger.debug("Inside StoreCashDepositLimitApprove.. ");
		CashDepositLimitDAO floatDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

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
			floatDAO = new CashDepositLimitDAO();
			responseDTO = floatDAO.StoreCashDepositLimitApprove(requestDTO);
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
			logger.debug("Exception in StoreCashDepositLimitApprove ["
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

	public String getRecoveryGenerateGrid() {
		logger.debug("Inside GetRecoveryGenerateGrid.. ");
		CashDepositLimitDAO cashDepositDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cashDepositDAO = new CashDepositLimitDAO();
			responseDTO = cashDepositDAO.getRecoveryGenerateGrid(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_LIST);
				responseJSON
						.put(CevaCommonConstants.REF_NO, getRandomInteger());
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
			logger.debug("Exception in GetRecoveryGenerateGrid ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cashDepositDAO = null;
			errors = null;
		}

		return result;
	}

	public String insertRecoveryDetails() {
		logger.debug("Inside InsertRecoveryDetails... ");
		CashDepositLimitDAO cashDepositDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.ACCOUNT_MULTI_DATA,
					accountMultiData);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cashDepositDAO = new CashDepositLimitDAO();
			responseDTO = cashDepositDAO.insertRecoveryDetails(requestDTO);
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
			logger.debug("Exception in InsertRecoveryDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cashDepositDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String insertRefundDetails() {
		logger.debug("Inside InsertRecoveryDetails... ");
		CashDepositLimitDAO cashDepositDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.ACCOUNT_MULTI_DATA,
					accountMultiData);

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cashDepositDAO = new CashDepositLimitDAO();
			responseDTO = cashDepositDAO.insertRefundDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
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
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InsertRecoveryDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cashDepositDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String floatDashboard() {
		logger.debug("Inside FloatDashboard... ");
		CashDepositLimitDAO cashDepositDAO = null;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("loc_name", session.getAttribute("userLevel"));

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cashDepositDAO = new CashDepositLimitDAO();
			responseDTO = cashDepositDAO.floatDashboard(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
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
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InsertRecoveryDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cashDepositDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	private static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = (Long) (fraction + aStart);
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

	public String getAccountMultiData() {
		return accountMultiData;
	}

	public void setAccountMultiData(String accountMultiData) {
		this.accountMultiData = accountMultiData;
	}

}
