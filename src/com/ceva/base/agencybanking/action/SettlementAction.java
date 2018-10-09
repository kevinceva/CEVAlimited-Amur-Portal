package com.ceva.base.agencybanking.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.base.common.bean.SettlementBean;
import com.ceva.base.common.dao.SettlementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SettlementAction extends ActionSupport implements
		ModelDriven<SettlementBean>, ServletRequestAware {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(SettlementAction.class);

	private String result;
	private JSONObject requestJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	// @Autowired
	// private SettlementBean settlementBean;

	private JSONObject responseJSON;

	private String bankid;
	private String bankname;
	private String settlementdate;
	private String tosettlementdate;
	private String fileId;
	private String txnrefno;
	private String fileStatus; 

	private HttpSession session = null;
	private HttpServletRequest httpRequest = null;

	public String commonScreen() {
		logger.debug("Inside CommonScreen... ");
		return SUCCESS;
	}

	public String fetchSettlementBank() {
		logger.debug("Inside FetchSettlementBank... ");
		ArrayList<String> errors = null;
		SettlementDAO settlementDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			settlementDAO = new SettlementDAO();

			responseDTO = settlementDAO.settlementBankData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = ((JSONObject) responseDTO.getData().get(
						"bank_data"));
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
			logger.debug("Exception in FetchSettlementBank [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String fetchSettlementData() {
		logger.debug("Inside FetchSettlementData... ");
		ArrayList<String> errors = null;
		SettlementDAO settlementDAO = null;

		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("bankid", bankid == null ? "NO_VALUE" : bankid);
			requestJSON.put("settlementdate", settlementdate);
			requestJSON.put("tosettlementdate", tosettlementdate);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			settlementDAO = new SettlementDAO();
			responseDTO = settlementDAO.settlementData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = ((JSONObject) responseDTO.getData().get(
						"settle_data"));
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
			logger.debug("Exception in FetchSettlementData [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}

	public String downloadSettlementFile() {
		logger.debug("Inside DownloadSettlementFile... ");
		ResourceBundle resource = null;
		File settlementFile = null;
		String folderName = null;
		SettlementDAO settlementDAO = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			resource = ResourceBundle.getBundle("pathinfo_config");
			folderName = fileId.substring(0, fileId.indexOf("_"));

			settlementFile = new File(
					resource.getString("SETTLE_BKP_FILE_PATH") + folderName
							+ File.separator + getFileId());

			fileInputStream = new FileInputStream(settlementFile);
			result = SUCCESS;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			result = ERROR;
		} finally {

			if (result.equalsIgnoreCase("error")) {
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request DTO [" + requestDTO + "]");
				settlementDAO = new SettlementDAO();

				responseDTO = settlementDAO.settlementBankData(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

				responseJSON = ((JSONObject) responseDTO.getData().get(
						"bank_data"));
				logger.debug("Response JSON [" + responseJSON + "]");

				addActionError("Unable to download file ... " + getFileId());
			}
		}

		return result;
	}

	public String addSettlementBank() {
		logger.debug("Inside AddSettlementBank... ");
		ArrayList<String> errors = null;
		SettlementDAO settlementDAO = null;

		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			settlementDAO = new SettlementDAO();
			responseDTO = settlementDAO.addSettlementBank(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = ((JSONObject) responseDTO.getData().get(
						"bank_data"));
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
			logger.debug("Exception in AddSettlementBank [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}

	public String confirmSettlementBank() {
		logger.debug("Inside ConfirmSettlementBank... ");
		try {
			responseJSON = constructToResponseJson(httpRequest);
			logger.debug(" ResponseJSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in ConfirmSettlementBank ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {
		Enumeration enumParams = httpRequest.getParameterNames();
		JSONObject jsonObject = null;
		logger.debug(" Inside constructToResponseJson ...");
		try {
			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug("  inside exception [" + e.getMessage() + "]");
		}

		return jsonObject;
	}

	public String ackSettlementBank() {
		logger.debug("Inside AckSettlementBank... ");
		ArrayList<String> errors = null;
		SettlementDAO settlementDAO = null;

		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestJSON
					.put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));
			requestJSON.put("bankid", bankid);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			settlementDAO = new SettlementDAO();
			responseDTO = settlementDAO.ackSettlementBank(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = ((JSONObject) responseDTO.getData().get(
						"bank_data"));
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
			logger.debug("Exception in AddSettlementBank [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}

	public String viewSettlementBanks() {
		logger.debug("Inside FetchSettlementData... ");
		ArrayList<String> errors = null;
		SettlementDAO settlementDAO = null;

		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			settlementDAO = new SettlementDAO();
			responseDTO = settlementDAO.viewSettlementBanks(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = ((JSONObject) responseDTO.getData().get(
						"bank_data"));
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
			logger.debug("Exception in FetchSettlementData [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}

	public String getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getTxnrefno() {
		return txnrefno;
	}

	public void setTxnrefno(String txnrefno) {
		this.txnrefno = txnrefno;
	}

	private InputStream fileInputStream;

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getBankid() {
		return bankid;
	}

	public void setBankid(String bankid) {
		this.bankid = bankid;
	}

	public String getSettlementdate() {
		return settlementdate;
	}

	public void setSettlementdate(String settlementdate) {
		this.settlementdate = settlementdate;
	}

	public String getTosettlementdate() {
		return tosettlementdate;
	}

	public void setTosettlementdate(String tosettlementdate) {
		this.tosettlementdate = tosettlementdate;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	@Override
	public SettlementBean getModel() {
		return null;
	}

	@Override
	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
 
}
