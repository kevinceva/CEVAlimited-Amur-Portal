package com.ceva.base.common.action;

import java.util.ArrayList;
import java.util.Random;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.WebLoginDAOAction;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.opensymphony.xwork2.ActionSupport;

public class WebLoginAction extends ActionSupport {

	Logger logger = Logger.getLogger(WebLoginAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String result = null;
	private String userId = null;
	private String macAddress = null;
	private String lanIp = null;
	private String ulsid = null;
	private String ulid = null;

	public String getUlid() {
		return ulid;
	}

	public void setUlid(String ulid) {
		this.ulid = ulid;
	}

	static char[] hexval = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };

	public String getCommonScreen() {
		logger.debug("Inside GetCommonScreen.. ");
		ArrayList<String> errors = null;
		WebLoginDAOAction webDAO = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			requestDTO.setRequestJSON(requestJSON); 
			webDAO = new WebLoginDAOAction(); 
			responseDTO = webDAO.getuseriddata(requestDTO);
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USER_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetCommonScreen [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			webDAO = null;
		}

		return result;

	}

	public String InsertWebLoginDetails() {
		logger.debug(" Inside InsertWebLoginDetails... ");
		String randomNum = "";
		WebLoginDAOAction webDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

			if (userId == null) {
				addActionError(" User Id Missing");
				result = "fail";
			} else if (macAddress == null) {
				addActionError(" Mac Address Missing");
				result = "fail";
			} else if (lanIp == null) {
				addActionError("Lan IP Missing");
				result = "fail";
			} else {

				requestJSON.put("USER_ID", userId);
				requestJSON.put("MAC_ADDRESS", macAddress);
				requestJSON.put("LAN_IP", lanIp);

				randomNum = getRandomValue();

				requestJSON.put("ULSID", randomNum);

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request JSON [" + requestJSON + "]");

				webDAO = new WebLoginDAOAction();
				responseDTO = webDAO.WebLoginDetails(requestDTO);

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					ulid = randomNum;

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
			logger.debug("Exception in InsertWebLoginDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			webDAO = null;
			randomNum = null;
		}

		return result;
	}

	public static String getRandomValue() {

		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 32; i++) {
			sb.append(hexval[random.nextInt(15)]);
		}
		return sb.toString();

	}

	private static String getRandomInteger() {
		int aStart = 100000;
		int aEnd = 999999;
		Random aRandom = new Random();
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		Long randomNumber = (Long) (fraction + aStart);
		return randomNumber.toString();
	}

	public String getUlsid() {
		return ulsid;
	}

	public void setUlsid(String ulsid) {
		this.ulsid = ulsid;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getLanIp() {
		return lanIp;
	}

	public void setLanIp(String lanIp) {
		this.lanIp = lanIp;
	}

}
