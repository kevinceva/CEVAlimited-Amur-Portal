package com.ceva.base.cevaswitch.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.SwitchUIDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class SwitchStatusCheckAction extends ActionSupport {

	private static final long serialVersionUID = 658735490187966840L;
	private Logger logger = Logger.getLogger(SwitchStatusCheckAction.class);

	private String result;
	private HttpSession session = null;

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String checkSwitchStaus() {
		logger.debug("Inside CheckSwitchStaus.. ");
		SwitchUIDAO cevaPowerDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
 			requestDTO = new RequestDTO();
 
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			cevaPowerDAO = new SwitchUIDAO();

			responseDTO = cevaPowerDAO.getDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.LIVE_TRANLOG_DATA);
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
 			logger.debug("Exception in checkSwitchStaus [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null; 
		}

		return result;
	}

}
