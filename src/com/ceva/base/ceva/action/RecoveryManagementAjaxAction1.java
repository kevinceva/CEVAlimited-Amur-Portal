package com.ceva.base.ceva.action;


import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.RecoveryManagementAjaxDAO1;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class RecoveryManagementAjaxAction1 extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(RecoveryManagementAjaxAction1.class);
	
	JSONObject requestJSON=null;
	JSONObject responseJSON=null;
	RequestDTO requestDTO=null;
	ResponseDTO responseDTO=null;

	
	private String method = null;
	private String merchantId = null;
	private String storeId = null;
	
	
	private Map<String, String> details;
	

	@Override
	public String execute() throws Exception {
		logger.debug("Inside Execute Method."); 
		logger.debug(" [RecoveryManagementAjaxAction] [Execute] method["+method+"] "); 
		String result = ERROR;
		
		if(method.equalsIgnoreCase("getTerminalDetails")){
			result = getTerminalDetails();
		}
 
		return result;
	}

	

		
	
	public String getTerminalDetails(){
    	logger.debug("inside [RecoveryManagementAjaxAction][getTerminalDetails][storeId::"+storeId+"]");
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		requestDTO=new RequestDTO();
		responseDTO= new ResponseDTO();
		requestJSON.put(CevaCommonConstants.STORE_ID, storeId);
		requestDTO.setRequestJSON(requestJSON);	
		logger.debug("[RecoveryManagementAjaxAction][getTerminalDetails][requestDTO:::::"+requestDTO+"]");
		RecoveryManagementAjaxDAO1 ajaxDAO=new RecoveryManagementAjaxDAO1();
		responseDTO=ajaxDAO.getTerminalDetails(requestDTO);
		logger.debug("[RecoveryManagementAjaxAction][getTerminalDetails][responseDTO:::::"+responseDTO+"]");
		
		if (responseDTO != null && responseDTO.getErrors().size()==0) {		
			responseJSON=(JSONObject) responseDTO.getData().get(CevaCommonConstants.TERMINAL_LIST);
			logger.debug("[RecoveryManagementAjaxAction][getTerminalDetails][responseJSON:::::"+responseJSON+"]");
		}else{			
			ArrayList<String> errors=(ArrayList<String>) responseDTO.getErrors();
			for(int i=0;i<errors.size();i++){
				addActionError(errors.get(i));
			}
		}
    	return SUCCESS;
    }
	
	
	
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}


	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
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
