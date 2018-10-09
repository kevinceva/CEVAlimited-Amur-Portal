package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.BillerBean;
import com.ceva.base.common.dao.NewBillPaymentDAO;
import com.ceva.base.common.dao.NewMerchantDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class NewBillPaymentAction extends ActionSupport implements ServletRequestAware {

	
	
	Logger logger = Logger.getLogger(NewBillPaymentAction.class);
	
	private String result;
	JSONObject requestJSON = null;
	public JSONObject responseJSON = null;
	public JSONObject responseJSON1 = null;
	

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	private HttpServletRequest httpRequest;
	
	/*@Autowired
	private BillerBean billerBean = null;*/
	private HttpSession session = null;
	
	JSONObject terminalJSON = null;
	JSONObject userJSON = null;
	
	public String bankMultiData = null;
	public String billerCode = null;
	public String billerCode1 = null;
	public String accountNumber = null;
	public String billerId = null;
	
	public String commonScreen(){
		logger.debug("inside [NewBillPaymentAction][commonScreen]");
		
		requestDTO = new RequestDTO();
		requestJSON = new JSONObject();
		responseJSON = constructToResponseJson(httpRequest);
		responseJSON1 = responseJSON;
		logger.debug("responseJSON:::"+responseJSON1);
		
		result = "success";
		return result;
	}

	
	
	
	
	public String fetchBillerDataTableDetails() {
		logger.debug("inside [NewBillPaymentAction][fetchBillerDataTableDetails].. ");
		NewBillPaymentDAO newBillPaymentDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			newBillPaymentDAO = new NewBillPaymentDAO();
			responseDTO = newBillPaymentDAO.fetchBillerDataTableDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON1 = (JSONObject) responseDTO.getData().get("BILLER_LIST");
				logger.debug("Response JSON in aCTION rAVI [" + responseJSON1 + "]");
				/*terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("Terminal JSON [" + terminalJSON + "]");
				userJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.USER_DATA);
				logger.debug("User JSON [" + userJSON + "]");*/
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getMerchantDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			newBillPaymentDAO = null;
		}

		return result;
	}

	
	
	
	public String registerBiller(){
		logger.debug("inside [NewBillPaymentAction][registerBiller]");
		ArrayList<String> errors = null;
		NewBillPaymentDAO newBillPaymentDAO = null;
		try{
			
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			newBillPaymentDAO = new NewBillPaymentDAO();
			
			//requestJSON.put("billerBean", billerBean);
			requestJSON= constructToResponseJson(httpRequest);
			requestJSON.put("makerId",session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);
			
			
			responseDTO = newBillPaymentDAO.registerBiller(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {  
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.BILLER_REL_INFO); 
				logger.debug("Response JSON [" + responseJSON + "]");
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
			logger.debug("Exception in Get BillerRelated Info ["+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}
	
	
	public String billerAccountSubmitDetails(){
		
		logger.debug("inside [NewBillPaymentAction][billerAccountSubmitDetails]");
		
		responseJSON1 = new JSONObject();
		responseJSON1.put("BANK_MULTI_DATA", getBankMultiData());
		logger.debug("responseJSON:::"+responseJSON1);
		
		result = "success";
		return result;
	}
	
	
	public String registerBillerAccountDetails(){
		
		logger.debug("inside [NewBillPaymentAction][registerBillerAccountDetails]");
		ArrayList<String> errors = null;
		NewBillPaymentDAO newBillPaymentDAO = null;
		try{
			
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			newBillPaymentDAO = new NewBillPaymentDAO();
			
			requestJSON.put("BILLER_ACCT_DATA", getBankMultiData());
			requestJSON.put("makerId",session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			logger.debug("[requestJSON::"+requestJSON+"]");
			
			requestDTO.setRequestJSON(requestJSON);
			
			responseDTO = newBillPaymentDAO.registerBillerAccountDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {  
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.BILLER_REL_INFO); 
				responseJSON1=responseJSON;
				logger.debug("Response JSON [" + responseJSON + "]");
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
			logger.debug("Exception in Get BillerRelated Info ["+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		
		
		return result;
	}
	
	public String payBillInitDetails(){
		
		logger.debug("inside [NewBillPaymentAction][payBillInitDetails]");
		NewBillPaymentDAO newBillPaymentDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			/*logger.debug("Biller Code::"+getBillerBean().getBillerCode());
			
			requestJSON.put("billerId", getBillerBean().getBillerCode() );
			requestJSON.put("accountNumber", getAccountNumber());*/
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			newBillPaymentDAO = new NewBillPaymentDAO();
			
			responseDTO = newBillPaymentDAO.fetchBillerDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON1 = (JSONObject) responseDTO.getData().get("BILLER_INFO");
				logger.debug("Response JSON in aCTION rAVI [" + responseJSON1 + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getMerchantDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			newBillPaymentDAO = null;
		}

		return result;
	}
	
	
	public String payBillSubmitDetails(){
		logger.debug("inside [NewBillPaymentAction][payBillSubmitDetails]");
		
		requestDTO = new RequestDTO();
		requestJSON = new JSONObject();
		responseJSON1 = constructToResponseJson(httpRequest);
		
		logger.debug("responseJSON:::"+responseJSON1);
		
		result = "success";
		return result;
	}
	
	
	
	public String billPayVerifyPin() {
		logger.debug("Inside [NewBillPaymentAction][billPayVerifyPin]. ");
		HttpSession session = null;
		NewBillPaymentDAO newBillPaymentDAO = null;

		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON = constructToResponseJson(httpRequest);
			requestJSON.put(CevaCommonConstants.MAKER_ID,session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			logger.debug("Request JSON [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);

			newBillPaymentDAO = new NewBillPaymentDAO();
			responseDTO = newBillPaymentDAO.billPayVerifyPin(requestDTO);
			logger.debug("ResponseDTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = requestJSON;
			responseJSON1= responseJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
			logger.debug("Response JSON [" + responseJSON1 + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in BillPayment Submit Details ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			newBillPaymentDAO = null;
		}

		return result;
	}

	
	public String payBillInsertDetails(){
		
		logger.debug("inside [NewBillPaymentAction][payBillInsertDetails]");

		ArrayList<String> errors = null;
		NewBillPaymentDAO newBillPaymentDAO = null;
		try{
			
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			newBillPaymentDAO = new NewBillPaymentDAO();
			
			requestJSON = constructToResponseJson(httpRequest);
			requestJSON.put(CevaCommonConstants.MAKER_ID,session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			logger.debug("Request JSON [" + requestJSON + "]");
			
			requestDTO.setRequestJSON(requestJSON);
			
			responseDTO = newBillPaymentDAO.payBillInsertDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {  
				responseJSON = (JSONObject) responseDTO.getData().get("BILLPAY"); 
				responseJSON1= responseJSON;
				logger.debug("Response JSON [" + responseJSON + "]");
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
			logger.debug("Exception in Get BillerRelated Info ["+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}
	
	
	public String billerTransactionHistory(){
		
		logger.debug("inside Biller Transaction History:::");
		return "success";
	}
	

	public String viewBillerDetails(){
		
		logger.debug("inside [NewBillPaymentAction][viewBillerDetails]");
		ArrayList<String> errors = null;
		NewBillPaymentDAO newBillPaymentDAO = null;
		try{
			
			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();
			requestJSON = constructToResponseJson(httpRequest);
			
			logger.debug("responseJSON:::"+responseJSON);
			
			newBillPaymentDAO = new NewBillPaymentDAO();
			logger.debug("[requestJSON::"+requestJSON+"]");
			
			requestDTO.setRequestJSON(requestJSON);
			
			responseDTO = newBillPaymentDAO.viewBillerDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {  
				responseJSON = (JSONObject) responseDTO.getData().get("BILLER_INFO"); 
				logger.debug("Response JSON [" + responseJSON + "]");
				responseJSON1= responseJSON;
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
			logger.debug("Exception in Get BillerRelated Info ["+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		
		
		return result;

		
		
	}
	
	
	public String updateBillerDetails(){
		logger.debug("inside [NewBillPaymentAction][updateBillerDetails]");
		ArrayList<String> errors = null;
		NewBillPaymentDAO newBillPaymentDAO = null;
		try{
			
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			newBillPaymentDAO = new NewBillPaymentDAO();
			
			//requestJSON.put("billerBean", billerBean);
			requestJSON=constructToResponseJson(httpRequest);
			requestJSON.put("makerId",session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);
			
			
			responseDTO = newBillPaymentDAO.updateBillerDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {  
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.BILLER_REL_INFO); 
				logger.debug("Response JSON [" + responseJSON + "]");
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
			logger.debug("Exception in Get BillerRelated Info ["+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	
	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {

		JSONObject jsonObject = null;
		Enumeration<?> enumParams = null;
		logger.debug("Inside ConstructToResponseJson... ");
		String key = "";
		String val = "";

		try {
			enumParams = httpRequest.getParameterNames();
			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				key = (String) enumParams.nextElement();
				val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}
			logger.debug("jsonObject Ravi...::::::"+jsonObject);
		} catch (Exception e) {
			logger.debug("Exception while converting to httpreq to bean["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
			key = null;
			val = null;
		}
		return jsonObject;
	}
	
	

	public String billerAccountDetails() {
		logger.debug("inside [NewBillPaymentAction][billerAccountDetails].. ");
		NewBillPaymentDAO newBillPaymentDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("billerId", getBillerCode() );
			requestJSON.put("accountNumber", getAccountNumber());
			logger.debug("billerId:::"+getBillerCode());
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			newBillPaymentDAO = new NewBillPaymentDAO();
			responseDTO = newBillPaymentDAO.billerAccountDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON1 = (JSONObject) responseDTO.getData().get("BILLER_ACCT_INFO");
				logger.debug("Response JSON in aCTION rAVI [" + responseJSON1 + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getMerchantDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			newBillPaymentDAO = null;
		}

		return result;
	}

	
	public String modifyBillerAccountDetails(){
		
		logger.debug("inside [NewBillPaymentAction][modifyBillerAccountDetails]");
		ArrayList<String> errors = null;
		NewBillPaymentDAO newBillPaymentDAO = null;
		try{
			
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			newBillPaymentDAO = new NewBillPaymentDAO();
			
			requestJSON = constructToResponseJson(httpRequest);
			
			logger.debug("requestJSON::::"+requestJSON);
			
			requestDTO.setRequestJSON(requestJSON);
			
			responseDTO = newBillPaymentDAO.modifyBillerAccountDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {  
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}	
			
			result = "success";
		}catch(Exception e){
			result = "fail";
			logger.debug("Exception in Get modifyBillerAccountDetails Info ["+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		}finally{
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String fetchBillerAccountDetails(){
		logger.debug("inside [NewBillPaymentAction][fetchBillerAccountDetails].. ");
		NewBillPaymentDAO newBillPaymentDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("billerId", getBillerId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			newBillPaymentDAO = new NewBillPaymentDAO();
			responseDTO = newBillPaymentDAO.fetchBillerAccountDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("BILLER_ACCT_LIST");
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
			result = "fail";
			logger.debug("Exception in [NewBillPaymentAction][fetchBillerAccountDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			newBillPaymentDAO = null;
		}

		return result;
	}

	
	public String fetchBillerCount(){
		logger.debug("inside [NewBillPaymentAction][fetchBillerCount].. ");
		NewBillPaymentDAO newBillPaymentDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("billerId", getBillerId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			newBillPaymentDAO = new NewBillPaymentDAO();
			responseDTO = newBillPaymentDAO.fetchBillerCount(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("BILLER_COUNT");
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
			result = "fail";
			logger.debug("Exception in [NewBillPaymentAction][fetchBillerCount] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			newBillPaymentDAO = null;
		}

		return result;
	}

	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public JSONObject getResponseJSON1() {
		return responseJSON1;
	}

	public void setResponseJSON1(JSONObject responseJSON1) {
		this.responseJSON1 = responseJSON1;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
/*	public BillerBean getBillerBean() {
		return billerBean;
	}

	public void setBillerBean(BillerBean billerBean) {
		this.billerBean = billerBean;
	}

	@Override
	public BillerBean getModel() {
		return billerBean;
	}*/

	public String getBankMultiData() {
		return bankMultiData;
	}

	public void setBankMultiData(String bankMultiData) {
		this.bankMultiData = bankMultiData;
	}

	public String getBillerCode() {
		return billerCode;
	}

	public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}





	public String getBillerCode1() {
		return billerCode1;
	}

	public void setBillerCode1(String billerCode1) {
		this.billerCode1 = billerCode1;
	}





	public String getBillerId() {
		return billerId;
	}





	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	
	
	
}
