package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.ceva.base.common.dao.ReportsDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class HistoryTracking extends ActionSupport {
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(HistoryTracking.class);

	private String result = "success";
	private String userID;
	private String fromDate;
	private String toDate;
	private String role;
	JSONObject resJSON = null;
	JSONObject reqJSON = null;
	RequestDTO reqDTO = null;
	ResponseDTO resDTO = null;

	public String initliaze() {
		result = "success";
		return result;
	}

	public String getDetails() {

		logger.debug("Inside Get Audit Details..");
		ReportsDAO repDAO = null;
		ArrayList<String> errors = null;
		try {
			reqJSON = new JSONObject();
			reqDTO = new RequestDTO();

			reqJSON.put("userID", userID);
			reqJSON.put("fromDate", fromDate);
			reqJSON.put("toDate", toDate);
			reqJSON.put("role", role);
			
			reqDTO.setRequestJSON(reqJSON);

			logger.debug("Request Json is [" + reqDTO + "]");

			repDAO = new ReportsDAO();
			resDTO = repDAO.getData(reqDTO);

			if (resDTO != null && resDTO.getErrors().size() == 0) {
				resJSON = (JSONObject) resDTO.getData().get(CevaCommonConstants.TXN_DATA);
				result = "success";
			} else {
				errors = (ArrayList<String>) resDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getDetails [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			resDTO = null;
			reqDTO = null;
			reqJSON = null;
			repDAO = null;
			errors = null;
		}

		return result;
	}

	public JSONObject getResJSON() {
		return resJSON;
	}

	public void setResJSON(JSONObject resJSON) {
		this.resJSON = resJSON;
	}

	public JSONObject getReqJSON() {
		return reqJSON;
	}

	public void setReqJSON(JSONObject reqJSON) {
		this.reqJSON = reqJSON;
	}

	public RequestDTO getReqDTO() {
		return reqDTO;
	}

	public void setReqDTO(RequestDTO reqDTO) {
		this.reqDTO = reqDTO;
	}

	public ResponseDTO getResDTO() {
		return resDTO;
	}

	public void setResDTO(ResponseDTO resDTO) {
		this.resDTO = resDTO;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
