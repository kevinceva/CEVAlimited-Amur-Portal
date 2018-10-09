package com.ceva.base.common.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.UAMAjaxDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class UAMAjaxAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger=Logger.getLogger(UAMAjaxAction.class);
	
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	private String typeOfReg;
	private String result;
	private HttpSession session = null;
	
	public String fetchBranchDetailsAct() throws Exception {

		logger.debug("Inside fetchBranchDetailsAct typeOfReg[" + typeOfReg + "]");
		UAMAjaxDAO uamAjaxDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("TYPE_OF_REG", typeOfReg);
			logger.debug("requestJSON  [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			uamAjaxDAO = new UAMAjaxDAO();
			responseDTO = uamAjaxDAO.fetchBranchDetailsAct(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("BRANCH_LIST");
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in fetchBranchDetailsAct [" + e.getMessage()+ "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			uamAjaxDAO = null;
		}

		return SUCCESS;
	}
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getTypeOfReg() {
		return typeOfReg;
	}

	public void setTypeOfReg(String typeOfReg) {
		this.typeOfReg = typeOfReg;
	}

}
