package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.BankDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class BankDetialsAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(BankDetialsAction.class);

	private String service;
	private String accountname;
	private String openbalance;
	private String closebalance;
	private String accounttype;
	private String bankMultiData;

	private String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject terminalJSON = null;
	JSONObject userJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	public String getCommonScreen() {
		String result = "success";
		return result;
	}

	public String getLocationList() {
		logger.debug("Inside Location List.");
		BankDAO bankDAO = null;
		ArrayList<String> errors = null;
		try {
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");

			bankDAO = new BankDAO();
			responseDTO = bankDAO.getlocationlist(requestDTO);
			logger.debug("Response DTO is ["+responseDTO+"]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_INFO);
				logger.debug("[getLocationList][responseJSON:::::"
						+ responseJSON + "]");

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO
						.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Location List ["+e.getMessage()+"]");
			addActionError("Internal error occured.");
		} finally{
			bankDAO = null;
			requestDTO = null;
			responseDTO = null;
			errors = null;
		}

		return result;
	}

	public String insertBankDetails() {
		HttpSession session = null;
		BankDAO bankDAO = null;
		ArrayList<String> messages =  null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			logger.debug("inside insertBankDetails.");
			if (bankMultiData == null) {
				addActionError(" Records Missing");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest()
						.getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put(CevaCommonConstants.SERVICE, service);
				requestJSON.put(CevaCommonConstants.ACCOUNT_NAME, accountname);
				requestJSON.put(CevaCommonConstants.OPEN_BALANCE, openbalance);
				requestJSON.put(CevaCommonConstants.CLOSE_BALANCE, closebalance);
				requestJSON.put(CevaCommonConstants.ACCOUNT_TYPE, accounttype); 
				requestJSON.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);
				
				logger.debug("Request JSON [" + requestJSON + "]");
				
				requestDTO.setRequestJSON(requestJSON);
				
				bankDAO = new BankDAO();
				responseDTO = bankDAO.insertBankConfig(requestDTO);
				logger.debug("ResponseDTO [" + responseDTO + "]");

				if (responseDTO != null 
						&& responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ENTITY_LIST);
					logger.debug("ResponseJSON ["+responseJSON+"]");
					messages = (ArrayList<String>) responseDTO
							.getMessages();
					logger.debug("Messages[" + messages	+ "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					result = "success";
				} else {
					logger.debug("Getting error from DB");
					responseJSON = (JSONObject) responseDTO.getData().get(
							CevaCommonConstants.ENTITY_LIST);
					logger.debug("ResponseJSON ["+ responseJSON + "]");
					errors = (ArrayList<String>) responseDTO
							.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}

			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Insert Bank Details ["+e.getMessage()+"]");
			addActionError("Internal error occured.");
		} finally{
			bankDAO = null;
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			session = null;
			messages = null;
			errors = null;
		}

		return result;

	}

	public String checkUserId() { 
		logger.debug("inside CheckUserId.");
		BankDAO bankDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			
			requestJSON.put(CevaCommonConstants.USER_ID, service);
			
			logger.debug("Request JSON[" + requestJSON + "]");
			
 			requestDTO.setRequestJSON(requestJSON); 
			
			bankDAO = new BankDAO();
			responseDTO = bankDAO.checkUserId(requestDTO);

			logger.debug("ResponseDTO [" + responseDTO + "]");
			
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.USER_CHECK_INFO);
				logger.debug("ResponseJSON [" + responseJSON + "]");
			} else {
				 errors = (ArrayList<String>) responseDTO
						.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InsertBankDetails [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally{
			bankDAO = null;
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
  			errors = null;
		}

		return SUCCESS;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getOpenbalance() {
		return openbalance;
	}

	public void setOpenbalance(String openbalance) {
		this.openbalance = openbalance;
	}

	public String getClosebalance() {
		return closebalance;
	}

	public void setClosebalance(String closebalance) {
		this.closebalance = closebalance;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getBankMultiData() {
		return bankMultiData;
	}

	public void setBankMultiData(String bankMultiData) {
		this.bankMultiData = bankMultiData;
	}

	public JSONObject getRequestJSON() {
		return requestJSON;
	}

	public void setRequestJSON(JSONObject requestJSON) {
		this.requestJSON = requestJSON;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

}
