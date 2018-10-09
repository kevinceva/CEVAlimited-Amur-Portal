package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;



import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.TreasuryAuthorizationDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class TreasuryManagementAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(TreasuryManagementAction.class);

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	JSONObject terminalJSON = null;
	
	private HttpSession session = null;
	
	private String cardNumber = null;
	private String referenceno = null;
	private String name = null;
	private String dob = null;
	private String mobileNumber = null;
	private String email = null;
	private String passportno = null;
	private String paymentMode = null;
	private String loadAmount = null;
	private String chequeNo = null;
	private String bankName = null;
	private String approveReject = null;
	private String cardAcctData = null;
	private String acctNo = null;
	
	
	public String commonScreen(){
		logger.debug("Inside CommonScreen... "); 
		return SUCCESS;
	}
	
	public String fetchForAuthorizationData() {
		logger.debug("Inside fetchForAuthorizationData... ");
		ArrayList<String> errors = null;
		TreasuryAuthorizationDAO treasuryDAO = null;
		try {
			requestJSON = new JSONObject();
 			requestDTO = new RequestDTO();
 			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			treasuryDAO = new TreasuryAuthorizationDAO();
			responseDTO = treasuryDAO.fetchForAuthorizationData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				terminalJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_DATA);
				logger.debug("[MerchantAction][terminalJSON:::::"	+ terminalJSON + "]");
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
			logger.debug("Exception in fetchForAuthorizationData ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}
	
	

	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}
	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	
	public JSONObject getTerminalJSON() {
		return terminalJSON;
	}


	public void setTerminalJSON(JSONObject terminalJSON) {
		this.terminalJSON = terminalJSON;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getReferenceno() {
		return referenceno;
	}

	public void setReferenceno(String referenceno) {
		this.referenceno = referenceno;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassportno() {
		return passportno;
	}
	public void setPassportno(String passportno) {
		this.passportno = passportno;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getLoadAmount() {
		return loadAmount;
	}
	public void setLoadAmount(String loadAmount) {
		this.loadAmount = loadAmount;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getApproveReject() {
		return approveReject;
	}
	public void setApproveReject(String approveReject) {
		this.approveReject = approveReject;
	}

	public String getCardAcctData() {
		return cardAcctData;
	}
	public void setCardAcctData(String cardAcctData) {
		this.cardAcctData = cardAcctData;
	}
	public String getAcctNo() {
		return acctNo;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

}
