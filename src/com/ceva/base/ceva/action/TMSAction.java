package com.ceva.base.ceva.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.TMSDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class TMSAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(TMSAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	TMSDAO tmsdao = null;
	
	public String tmsVersionsList(){
		
		ArrayList<String> errors = null;
		
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();

		requestDTO.setRequestJSON(requestJSON);
		
		tmsdao = new TMSDAO();
		
		responseDTO = tmsdao.tmsFileUploadDashboard(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");
		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get("TMS_DASHBOARD");
			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} else {
			errors = (ArrayList<String>) responseDTO.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				addActionError(errors.get(i));
			}
			result = "fail";
		}
	
		return result;
	}
	
	public String commonScreen(){
		result="success";
		
		return result;
	}
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	
}
