package com.ceva.base.common.action;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.AjaxDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class SwitchAjaxAction extends ActionSupport {

	Logger logger = Logger.getLogger(SwitchAjaxAction.class);
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject responseJSON1 = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	private String RRN;

	public String RRNnumberDetails() {

		logger.debug("Inside GetRRNTerminals RRN [" + RRN + "]");
		AjaxDAO ictAdminDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestJSON.put(CevaCommonConstants.RRN, RRN);
			logger.debug("Response JSON [" + responseJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			ictAdminDAO = new AjaxDAO();
			responseDTO = ictAdminDAO.checkUserId(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON1 = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.USER_CHECK_INFO);

				logger.debug("Response JSON1 [" + responseJSON1 + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			// result = "fail";
			logger.debug("Exception in GetBillerCustomerData ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			errors = null;
			ictAdminDAO = null;
		}

		return SUCCESS;
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

	public JSONObject getResponseJSON1() {
		return responseJSON1;
	}

	public void setResponseJSON1(JSONObject responseJSON1) {
		this.responseJSON1 = responseJSON1;
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

	public String getRRN() {
		return RRN;
	}

	public void setRRN(String rRN) {
		RRN = rRN;
	}

}
