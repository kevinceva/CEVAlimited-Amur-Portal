package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.base.common.dao.MarketsCheckerDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.opensymphony.xwork2.ActionSupport;

public class MarketsAction extends ActionSupport implements ServletRequestAware {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(MarketsAction.class);
	
	private HttpServletRequest httpRequest;
	
	private String result;
	JSONObject requestJSON = null;
	public JSONObject responseJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	private HttpSession session = null;
	MarketsCheckerDAO marketsCheckerDAO = null;
	
	public String status;
	public String productId;
	public String approveOrReject;
	public String reason;
	public String offerId;
	
	public String commonScreen(){
		logger.debug("inside [MarketsAction][commonScreen]");
		result = "success";
		return result;
	}

	
	public String authRecordsCount(){
		ArrayList<String> errors = null;
		
		try{
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			marketsCheckerDAO = new MarketsCheckerDAO();
			
			responseDTO = marketsCheckerDAO.authRecordsCount(requestDTO);
			
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
			logger.debug("Exception in authRecordsCount [" + e.getMessage()+ "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			marketsCheckerDAO = null;
		}
		
		return result;
	}
	
	
	public String productInfoView(){
		ArrayList<String> errors = null;
		
		try{
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			marketsCheckerDAO = new MarketsCheckerDAO();
			
			requestJSON.put("PRODUCT_ID", productId);
			responseDTO = marketsCheckerDAO.productInfoView(requestDTO);
			
			
			
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
			marketsCheckerDAO = null;
		}
		
		return result;
	}
	

	public String productApproveOrReject(){
		ArrayList<String> errors = null;
		
		try{
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			marketsCheckerDAO = new MarketsCheckerDAO();
			
			requestJSON.put("PRODUCT_ID", productId);
			requestJSON.put("APPROVE_REJECT", approveOrReject);
			
			if(approveOrReject.equals("R")){
				requestJSON.put("REASON", reason);
			}
			
			logger.debug("request json:"+requestJSON);
			
			responseDTO = marketsCheckerDAO.productApproveOrReject(requestDTO);
			
			
			
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
			marketsCheckerDAO = null;
		}
		
		return result;
	}

	
	public String offersInfoView(){
		ArrayList<String> errors = null;
		
		try{
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			marketsCheckerDAO = new MarketsCheckerDAO();
			
			requestJSON.put("OFFER_ID", offerId);
			responseDTO = marketsCheckerDAO.offersInfoView(requestDTO);
			
			
			
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
			marketsCheckerDAO = null;
		}
		
		return result;
	}


	public String offerApproveOrReject(){
		ArrayList<String> errors = null;
		
		try{
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			marketsCheckerDAO = new MarketsCheckerDAO();
			
			requestJSON.put("OFFER_ID", offerId);
			requestJSON.put("APPROVE_REJECT", approveOrReject);
			
			if(approveOrReject.equals("R")){
				requestJSON.put("REASON", reason);
			}
			
			logger.debug("request json:"+requestJSON);
			
			responseDTO = marketsCheckerDAO.offerApproveOrReject(requestDTO);
			
			
			
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
			marketsCheckerDAO = null;
		}
		
		return result;
	}

	
	
	public String getApproveOrReject() {
		return approveOrReject;
	}


	public void setApproveOrReject(String approveOrReject) {
		this.approveOrReject = approveOrReject;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	

	public String getOfferId() {
		return offerId;
	}


	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}


	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
