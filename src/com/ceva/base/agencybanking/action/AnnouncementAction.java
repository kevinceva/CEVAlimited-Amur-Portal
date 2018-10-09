package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.AnnouncementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class AnnouncementAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(AnnouncementAction.class);

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String messageText;
	private String typeOfData;
	private String typeOfDataVal;
	private String referenceNo;
	private String status;

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String getAnnouncementInformation() {
		logger.debug("Inside GetAnnouncementInformation.. ");
		ArrayList<String> errors = null;
		AnnouncementDAO announceDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			announceDAO = new AnnouncementDAO();
			responseDTO = announceDAO.getAnnouncementInformation(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"ANNOUNCE_LIST");
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
			logger.debug("Exception in GetAnnouncementData [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			announceDAO = null;
		}

		return result;
	}

	public String getAnnouncementData() {
		logger.debug("Inside GetAnnouncementData.. ");
		AnnouncementDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try { 
			requestDTO = new RequestDTO();
			
			cevaPowerDAO = new AnnouncementDAO();
			responseDTO = cevaPowerDAO.getAnnouncementData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_DETAILS");
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
			logger.debug("Exception in GetAnnouncementData [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			cevaPowerDAO = null;
		}

		return result;
	}

	public String getCommonScreen() {
		result = "success";
		logger.debug("MessageText [" + messageText + "]");
		return result;
	}

	public String insertAnnouncementData() {

		logger.debug("Inside InsertAnnouncementData... ");
		HttpSession session = null;
		AnnouncementDAO announceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			if (typeOfData == null) {
				addActionError("Type Of Data Missing.");
				result = "fail";
			} else {
				requestJSON = new JSONObject();
				requestDTO = new RequestDTO();

				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put("TYPE_OF_DATA", typeOfData);
				requestJSON.put("TYPE_OF_DATA_VAL", typeOfDataVal);
				requestJSON.put("MESSAGE", messageText);

				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);

				announceDAO = new AnnouncementDAO();
				responseDTO = announceDAO.insertAnnouncementData(requestDTO);
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
			logger.debug("Exception in InsertAnnouncementData ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			announceDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String getAnnouncentmentModifyDetails() {
		logger.debug("Inside GetAnnouncentmentModifyDetails ReferenceNo ["
				+ referenceNo + "]");
		AnnouncementDAO announceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("REFERENCE_NO", referenceNo);
			requestDTO.setRequestJSON(requestJSON);
			announceDAO = new AnnouncementDAO();
			responseDTO = announceDAO
					.getAnnouncentmentModifyDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_DETAILS");
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
			logger.debug("Exception in UpdateAnnouncementData ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			announceDAO = null;
			errors = null;
		}

		return result;
	}

	public String updateAnnouncementData() {

		logger.debug("Inside UpdateAnnouncementData.. ");

		AnnouncementDAO announceDAO = null;
		HttpSession session = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;

		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			if (typeOfData == null) {
				addActionError("Type Of Data Missing.");
				result = "fail";
			} else {
				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put("REFERENCE_NO", referenceNo);
				requestJSON.put("TYPE_OF_DATA", typeOfData);
				requestJSON.put("TYPE_OF_DATA_VAL", typeOfDataVal);
				requestJSON.put("MESSAGE", messageText);

				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);

				announceDAO = new AnnouncementDAO();
				responseDTO = announceDAO.updateAnnouncementData(requestDTO);
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
			logger.debug("Exception in UpdateAnnouncementData ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			announceDAO = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String viewAnnouncementData() {
		logger.debug("Inside ViewAnnouncementData.. ");
		AnnouncementDAO announceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("REFERENCE_NO", referenceNo);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			announceDAO = new AnnouncementDAO();
			responseDTO = announceDAO.viewAnnouncementData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"ANNOUNCE_INFO");
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
			logger.debug("Exception in ViewAnnouncementData [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			announceDAO = null;
			errors = null;
		}

		return result;
	}

	public String announcementStatusChange() {
		logger.debug("Inside AnnouncementStatusChange.. ");
		AnnouncementDAO announceDAO = null;
		HttpSession session = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			if (status == null) {
				addActionError("Status Missing");
				result = "fail";
			} else {
				requestJSON = new JSONObject();
				requestDTO = new RequestDTO();

				session = ServletActionContext.getRequest().getSession();
				requestJSON.put(CevaCommonConstants.MAKER_ID,
						session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put("REFERENCE_NO", referenceNo);
				requestJSON.put("STATUS", status);

				logger.debug("Request JSON  [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);

				announceDAO = new AnnouncementDAO();
				responseDTO = announceDAO.announcementStatusChange(requestDTO);
				logger.debug("ResponseDTO [" + responseDTO + "]");

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
			logger.debug("Exception in AnnouncementStatusChange ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			announceDAO = null;
			messages = null;
			errors = null;
		}

		return result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public JSONObject getRequestJSON() {
		return requestJSON;
	}

	public void setRequestJSON(JSONObject requestJSON) {
		this.requestJSON = requestJSON;
	}

	public RequestDTO getRequestDTO() {
		return requestDTO;
	}

	public void setRequestDTO(RequestDTO requestDTO) {
		this.requestDTO = requestDTO;
	}

	public ResponseDTO getResponseDTO() {
		return responseDTO;
	}

	public void setResponseDTO(ResponseDTO responseDTO) {
		this.responseDTO = responseDTO;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getTypeOfData() {
		return typeOfData;
	}

	public void setTypeOfData(String typeOfData) {
		this.typeOfData = typeOfData;
	}

	public String getTypeOfDataVal() {
		return typeOfDataVal;
	}

	public void setTypeOfDataVal(String typeOfDataVal) {
		this.typeOfDataVal = typeOfDataVal;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
