package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.BinManagementDAO;
import com.ceva.base.common.dao.MarketsCheckerDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class BinManagementAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(BinManagementAction.class);
	
	public String result=null;
	private HttpServletRequest httpRequest;
	JSONObject requestJSON = null;
	public JSONObject responseJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	private HttpSession session = null;	
	BinManagementDAO binManagementDAO = null;
	
	public String bin;
	public String status;
	
	
	public String commonScreen(){
		logger.debug("Inside common Screen...");
		result="success";
		return result;
	}
	
	public String fetchBinInformation(){
	ArrayList<String> errors = null;
		
		try{
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			binManagementDAO = new BinManagementDAO();
			
			requestJSON.put("BIN", bin);
			responseDTO = binManagementDAO.fetchBinInformation(requestDTO);
			
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("RESPONSE_DATA");
				logger.debug("Response JSON in aCTION rAVI [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		
		}catch(Exception e){
			result = "fail";
			logger.debug("Exception in productInfoView [" + e.getMessage()+ "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			binManagementDAO = null;
		}
		
		return result;
	}


	public String changeBinStatus(){
		ArrayList<String> errors = null;
			
			try{
				session = ServletActionContext.getRequest().getSession();
				requestJSON = new JSONObject();
				requestDTO = new RequestDTO();
				
				
				requestJSON.put(CevaCommonConstants.MAKER_ID, session.getAttribute(CevaCommonConstants.MAKER_ID) == null ? " ": session.getAttribute(CevaCommonConstants.MAKER_ID));
				requestJSON.put("BIN", bin);
				requestJSON.put("STATUS", status);
				
				
				logger.debug("Request JSON::"+requestJSON);
				
				requestDTO.setRequestJSON(requestJSON);
				binManagementDAO = new BinManagementDAO();
				
				
				
				responseDTO = binManagementDAO.changeBinStatus(requestDTO);
				
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get("RESPONSE_DATA");
					logger.debug("Response JSON in aCTION rAVI [" + responseJSON + "]");
					result = "success";
				} else {
					errors = (ArrayList<String>) responseDTO.getErrors();
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					result = "fail";
				}
			
			}catch(Exception e){
				result = "fail";
				logger.debug("Exception in productInfoView [" + e.getMessage()+ "]");
				addActionError("Internal error occured.");
			}finally{
				requestDTO = null;
				responseDTO = null;
				requestJSON = null;
				errors = null;
				binManagementDAO = null;
			}
			
			return result;
		}

	
	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	
	
}
