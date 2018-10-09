package com.ceva.base.ceva.action;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.opensymphony.xwork2.ActionSupport;

public class StoreAjaxAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(StoreAjaxAction.class);

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String merchantId;
	
	public String getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}


	@Override
	public String execute() throws Exception {
		logger.debug("Inside Execute Method.");
		logger.debug("Execute merchantId [" + merchantId + "] ");
		String result = ERROR;

		try {
			
			result="success";
		} catch (Exception e) {
			logger.debug("Inside execute [" + e.getMessage() + "]");
		} finally {

		}

		return result;
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

}
