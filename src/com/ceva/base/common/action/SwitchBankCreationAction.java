package com.ceva.base.common.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CevaPowerAdminDAO;
import com.ceva.base.common.dao.SwitchUIDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class SwitchBankCreationAction extends ActionSupport {

	private static final long serialVersionUID = 658735490187966840L;
	Logger logger = Logger.getLogger(SwitchBankCreationAction.class);

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private HttpSession session = null;

	private String bankcode;
	private String bankname;
	private String bankIp;
	private String c1;
	private String bankport;
	private String c2;
	private String cloudmode;
	private String c3;
	private String acquirerId;
	private String zpk;
	private String bin;
	private String kcv;
	private String type;
	// String status;

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getBankIp() {
		return bankIp;
	}

	public void setBankIp(String bankIp) {
		this.bankIp = bankIp;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getBankport() {
		return bankport;
	}

	public void setBankport(String bankport) {
		this.bankport = bankport;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getCloudmode() {
		return cloudmode;
	}

	public void setCloudmode(String cloudmode) {
		this.cloudmode = cloudmode;
	}

	public String getC3() {
		return c3;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public String getAcquirerId() {
		return acquirerId;
	}

	public void setAcquirerId(String acquirerId) {
		this.acquirerId = acquirerId;
	}

	public String getZpk() {
		return zpk;
	}

	public void setZpk(String zpk) {
		this.zpk = zpk;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getKcv() {
		return kcv;
	}

	public void setKcv(String kcv) {
		this.kcv = kcv;
	}

	public String createBank() {
		logger.debug("Inside CreateBank.... ");
		ArrayList<String> errors = null;
		SwitchUIDAO dao = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			dao = new SwitchUIDAO();
			responseDTO = dao.getDepositeinfo(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BANK_NAME_INFO);
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
			logger.debug("Exception in createBank [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			dao = null;
		}

		return result;
	}

	public String insertBank() {
		logger.debug("Inside insertBank.... ");
		SwitchUIDAO dao = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			if (bankcode == null) {
				addActionError("bankcode Missing");
				result = "fail";
			} else if (bankname == null) {
				addActionError("bankname Missing");
				result = "fail";
			} else if (bankIp == null) {
				addActionError("bankIp Missing");
				result = "fail";
			} else if (c1 == null) {
				addActionError("c1 Missing");
				result = "fail";
			} else if (bankport == null) {
				addActionError("bankport Missing");
				result = "fail";
			} else if (c3 == null) {
				addActionError("c3 Missing");
				result = "fail";

			} else if (c2 == null) {
				addActionError("c2 Missing");
				result = "fail";
			} else if (acquirerId == null) {
				addActionError("acquirerId Missing");
				result = "fail";
			} else if (zpk == null) {
				addActionError("zpk Missing");
				result = "fail";

			} else {

				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.bankcode, bankcode);
				requestJSON.put(CevaCommonConstants.bankname, bankname);
				requestJSON.put(CevaCommonConstants.bankIp, bankIp);
				requestJSON.put(CevaCommonConstants.c1, c1);
				requestJSON.put(CevaCommonConstants.bankport, bankport);
				requestJSON.put(CevaCommonConstants.c3, c3);
				requestJSON.put(CevaCommonConstants.c2, c2);
				requestJSON.put(CevaCommonConstants.acquirerId, acquirerId);
				requestJSON.put(CevaCommonConstants.zpk, zpk);
				if (type != null) {
					requestJSON.put(CevaCommonConstants.type, type);
				}
				if (bin != null) {
					requestJSON.put(CevaCommonConstants.bin, bin);
				}
				if (kcv != null) {
					requestJSON.put(CevaCommonConstants.kcv, kcv);
				}

				logger.debug("Request JSON [" + requestJSON + "]");

				requestDTO.setRequestJSON(requestJSON);

				dao = new SwitchUIDAO();
				responseDTO = dao.insertBankInfo(requestDTO);

				logger.debug("Response DTO [" + responseDTO + "]");

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {

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
							CevaCommonConstants.RESPONSE_JSON);
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}
			}

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertBank [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			messages = null;
			errors = null;
		}

		return result;

	}

	public String getBankData() {

		logger.debug("Inside GetUserDetails.. ");
		ArrayList<String> errors = null;
		SwitchUIDAO dao = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestJSON.put(CevaCommonConstants.bankcode, bankcode);
			requestJSON.put(CevaCommonConstants.bankname, bankname);
			requestJSON.put(CevaCommonConstants.acquirerId, acquirerId);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");

			dao = new SwitchUIDAO();
			responseDTO = dao.getBankData(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BANK_DATA);
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
			logger.debug("Exception in GetUserDetails [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			dao = null;
			errors = null;
		}

		return result;
	}

	public String switchuimainmethod() {

		logger.debug("Inside Switchuimainmethod... ");
		ArrayList<String> errors = null;
		SwitchUIDAO cevaPowerDAO = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			cevaPowerDAO = new SwitchUIDAO();

			responseDTO = cevaPowerDAO.getBankDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"BANK_DATA");

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
			logger.debug("Exception in Switchuimainmethod ["
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

}
