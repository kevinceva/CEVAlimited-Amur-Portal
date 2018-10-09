package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.ResetIDDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.EncryptTransactionPin;
import com.opensymphony.xwork2.ActionSupport;

public class ResetIDAction extends ActionSupport {

	Logger logger = Logger.getLogger(ResetIDAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String result = null;
	private String userId;
	private String resetID;

	public String getCommonScreen() {
		result = "success";
		logger.debug("Inside [getCommonScreen]..");
		return result;
	}

	public String resetID() {

		logger.debug("Inside ResetID.. ");
		ResetIDDAO resetDAO = null;
		String randomNum = "";
		String cryptedPassword = null;
		String key = "97206B46CE46376894703ECE161F31F2";

		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			if (userId == null) {
				addActionError("User Id Missing.");
				result = "fail";
			} else {
				requestJSON.put(CevaCommonConstants.USER_ID, userId);
				randomNum = getRNum(new Date().getTime());

				logger.debug("Random number [" + randomNum + "]");

				try {
					cryptedPassword = EncryptTransactionPin.encrypt(key,
							randomNum, 'F');
				} catch (Exception e) {

				}
				
				requestJSON.put(CevaCommonConstants.RESETID, cryptedPassword);
				requestJSON.put(CevaCommonConstants.randomNUM, randomNum);
				requestDTO.setRequestJSON(requestJSON); 
 
				resetDAO = new ResetIDDAO();
				responseDTO = resetDAO.resetID(requestDTO);

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					result = "success";
				} else {
					logger.debug("Getting error from DAO.");
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
			logger.debug("Exception in resetID [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			resetDAO = null;
			randomNum = null;
			cryptedPassword = null;
			key = null;

			messages = null;
			errors = null;
		}

		return result;
	}

	public String getRNum(long x) {
		long rs = 0, rem;
		String ret = "";
		long n = x;
		for (int i = 0; i < 4; i++) {
			rem = n % 10;
			ret = ret + rem;
			rs = rs * 10 + rem;
			n = (n - rem) / 10;
		}

		return ret;
	}

	public String getResetID() {
		return resetID;
	}

	public void setResetID(String resetID) {
		this.resetID = resetID;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
