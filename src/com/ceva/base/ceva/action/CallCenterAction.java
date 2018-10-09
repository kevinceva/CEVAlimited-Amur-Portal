package com.ceva.base.ceva.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CallCenterDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;

public class CallCenterAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CallCenterAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	public String merchantId;
	public String storeId;
	public String srchval;
	
	private String cid = null;
	private String cname = null;
	private String gender = null;
	private String dob = null;
	private String idtype = null;
	private String idno = null;
	private String mob = null;
	private String addr = null;
	private String pcode = null;
	private String email = null;
	private String cdate = null;
	private String status = null;
	
	
	private String bid = null;
	private String bname = null;
	private String bgender = null;
	private String bdob = null;
	private String bidno = null;
	private String bmob = null;
	private String bcdate = null;
	private String bstatus = null;
	private String photo = null;
	private String vcnt = null;
	private String bsign = null;
	private String cases = null;
	private String remarks = null;
	
	private String fromDate = null;
	private String toDate = null;
	
	private String refno = null;
	private String rsddt = null;
	private String rsdby = null;
	private String compid = null;
	private String rid = null;
	private String newmob = null;
	private String amt = null;
	
	private String rfrom = null;
	private String rdate = null;
	private String rtxt = null;
	private String rto = null;
	private String ftxt = null;
	
	private String retstr = null;
	private String acn = null;
	private String mapto = null;
	
	private String cprim = null;
	private String casrd = null;
	
	private String arid = null;
	
	
	
/*	public String getMerchantDashBoard(){
		return "success";
	}*/
	
	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}
	
	public String fetchClientMobDetails() {
		logger.debug("inside [CallCenterAction][fetchClientMobDetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			System.out.println("sercha value===="+getSrchval());
			requestJSON = new JSONObject();
			requestJSON.put("srchval", getSrchval());
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.fetchClientMobDet(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchClientMobDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;
	}
	
	public String fetchClientDetails() {
		logger.debug("inside [CallCenterAction][fetchClientDetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			System.out.println("sercha value===="+getSrchval());
			requestJSON = new JSONObject();
			requestJSON.put("srchval", getSrchval());
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getClientDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchClientDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;
	}

	public String fetchBeneficiaryDetails() {
		logger.debug("inside [CallCenterAction][fetchBeneficiaryDetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getBeneficiaryDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchBeneficiaryDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;
	}
	
	public String fetchRequestAsResponse(){
		
		logger.debug("inside [CallCenterAction][fetchRequestAsResponse].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("fdate", getFromDate());
			requestJSON.put("tdate", getToDate());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getRequestAsResponse(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchRequestAsResponse] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
	public String fetchPurchaseDetails(){
		
		logger.debug("inside [CallCenterAction][fetchPurchaseDetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("fdate", getFromDate());
			requestJSON.put("tdate", getToDate());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getPurchaseDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchPurchaseDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}
	
	
	public String fetchScenarios(){
		
		logger.debug("inside [CallCenterAction][fetchScenarios].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getScenarios(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchScenarios] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}
	

	public String fetchClientFailedTxns(){
		
		logger.debug("inside [CallCenterAction][fetchClientFailedTxns].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("mob", getMob());
			requestJSON.put("srchval", getSrchval());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getClientFailedTxns(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchClientFailedTxns] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
	public String insertScenarios(){
		
		logger.debug("inside [CallCenterAction][insertScenarios].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			requestJSON.put("cid", getCid());
			requestJSON.put("mob", getMob());
			requestJSON.put("cases", getCases());
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("rid", getRid());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "] ");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.insertScenarios(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("INSERTED");
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
			logger.debug("Exception in [CallCenterAction][insertScenarios] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
	public String fetchRaisedCases(){
		
		logger.debug("inside [CallCenterAction][fetchRaisedCases].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getRaisedCases(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchRaisedCases] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}
	
	public String fetchRaisedCasedetails(){
		
		logger.debug("inside [CallCenterAction][fetchRaisedCasedetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("status", getStatus());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getRaisedCasedetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchRaisedCasedetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
	public String submitCompAction(){
		
		logger.debug("inside [CallCenterAction][submitCompAction].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			System.out.println("value of get status :"+getStatus());
			
			requestJSON.put("refno", getRefno());
			requestJSON.put("status", getStatus());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.submitCompAction(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				System.out.println("in hte action else");
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CallCenterAction][submitCompAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
	public String approveCompAction(){
		
		logger.debug("inside [CallCenterAction][approveCompAction].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			
			requestJSON.put("refno", getRefno());
			requestJSON.put("mob", getMob());
			requestJSON.put("status", getStatus());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("caseid", getCompid());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.approveCompAction(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("INSERTED");
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
			logger.debug("Exception in [CallCenterAction][approveCompAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
	public String confirmCompAction(){
		
		logger.debug("inside [CallCenterAction][confirmCompAction].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			requestJSON.put("refno", getRefno());
			requestJSON.put("mob", getMob());
			requestJSON.put("status", getStatus());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("compid", getCompid());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.confirmCompAction(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("INSERTED");
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
			logger.debug("Exception in [CallCenterAction][confirmCompAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
public String fetchFailedTxns(){
		
		logger.debug("inside [CallCenterAction][fetchFailedTxns].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("status", getStatus());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getFailedTxns(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][fetchFailedTxns] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	

	
	public String forceTopUpDet(){
		
		logger.debug("inside [CallCenterAction][forceTopUpDet].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			System.out.println("value of get status :"+getStatus());
			
			requestJSON.put("rid", getRid());
			requestJSON.put("refno", getRefno());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getForceTopUpDet(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
	
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				System.out.println("in hte action else");
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CallCenterAction][forceTopUpDet] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			callcenterDAO = null;
		}
	
		return result;
	
	}	

	public String forceTopUpConf(){
		
		logger.debug("inside [CallCenterAction][forceTopUpConf].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			System.out.println("value of get status :"+getStatus()+"--"+getNewmob());
			
			requestJSON.put("rid", getRid());
			requestJSON.put("newmob", getNewmob());
			requestJSON.put("mob", getMob());
			requestJSON.put("amt", getAmt());
			requestJSON.put("cid", getCid());
			requestJSON.put("cdate", getCdate());
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("refno", getRefno());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			System.out.println("requestJSON:"+requestJSON.toString());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.initiateForceTopUp(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
	
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				System.out.println("in hte action else");
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CallCenterAction][forceTopUpConf] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			callcenterDAO = null;
		}
	
		return result;
	
	}
	
public String fetchtwowaysmsdetails(){
		
		logger.debug("inside [CallCenterAction][fetchtwowaysmsdetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		
		try {
			/*requestJSON = new JSONObject();
			requestDTO = new RequestDTO();		
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getTwoWaysSMSdetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}*/
			
			result = "success";
			logger.info("[CallCenterAction][fetchtwowaysmsdetails result..:"+result);
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CallCenterAction][fetchtwowaysmsdetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}
		result = "success";
		return result;

	}	

public String raisetwowaysms(){
	
	logger.debug("inside [CallCenterAction][raisetwowaysms].. ");
	CallCenterDAO callcenterDAO = null;
	ArrayList<String> errors = null;
	String msgtxt = "";
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		System.out.println("value of get status :"+getRtxt());
		
		requestJSON.put("rfrom", getRfrom());
		requestJSON.put("rdate", getRdate());
		requestJSON.put("rto", getRto());
		requestJSON.put("rtxt", getRtxt());
		requestJSON.put("acn", getAcn());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		callcenterDAO = new CallCenterDAO();
		responseDTO = callcenterDAO.submitSMSAction(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
			logger.debug("Response JSON [" + responseJSON + "]");
			
			if ("Success".equalsIgnoreCase(responseJSON.getString("retstr")))
			{	
				this.rfrom=responseJSON.getString("rfrom");
				this.rdate=responseJSON.getString("rdate");
				this.rtxt=responseJSON.getString("rtxt");
				this.rto=responseJSON.getString("rto");
				this.retstr=responseJSON.getString("retstr");
				this.acn=responseJSON.getString("acn");
			}else
			{
				this.retstr=responseJSON.getString("retstr");
			}
			
			result = "success";
		} else {
			System.out.println("in hte action else");
			errors = (ArrayList<String>) responseDTO.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				addActionError(errors.get(i));
			}
			result = "fail";
		}
	} catch (Exception e) {
		result = "fail";
		logger.debug("Exception in [CallCenterAction][raisetwowaysms] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		callcenterDAO = null;
	}

	return result;

}
	
	public String confirmRaiseSMSAction(){
		
		logger.debug("inside [CallCenterAction][confirmRaiseSMSAction].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			requestJSON.put("rfrom", getRfrom());
			requestJSON.put("rdate", getRdate());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("ftxt", getFtxt());
			requestJSON.put("acn", getAcn());
			requestJSON.put("mapto", getMapto());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("requestJSON [" + requestJSON.toString() + "]");
			logger.debug("request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.confirmRaiseSMSAction(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
	
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("INSERTED");
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
			logger.debug("Exception in [CallCenterAction][confirmRaiseSMSAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			callcenterDAO = null;
		}
	
		return result;
	
	}
	
	public String replytwowaysmsdetails(){
		
		logger.debug("inside [CallCenterAction][confirmRaiseSMSAction].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			requestJSON.put("rfrom", getRfrom());
			requestJSON.put("rdate", getRdate());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("ftxt", getFtxt());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("requestJSON [" + requestJSON.toString() + "]");
			logger.debug("request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.confirmReplySMSAction(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
	
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("INSERTED");
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
			logger.debug("Exception in [CallCenterAction][confirmRaiseSMSAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			callcenterDAO = null;
		}
	
		return result;
	
	}	
	
	public String smsstatus(){
		
		logger.debug("inside [CallCenterAction][smsstatus].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.smsstatus(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
			logger.debug("Exception in [CallCenterAction][smsstatus] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}	
	
public String fetchraisedsmsdetails(){
		
		logger.debug("inside [CallCenterAction][fetchtwowaysmsdetails].. ");
		CallCenterDAO callcenterDAO = null;
		ArrayList<String> errors = null;
		logger.info("status..:"+status);
		try {
			/*requestJSON = new JSONObject();
			requestDTO = new RequestDTO();		
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			callcenterDAO = new CallCenterDAO();
			responseDTO = callcenterDAO.getRaisedSMSdetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}*/
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CallCenterAction][fetchtwowaysmsdetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			callcenterDAO = null;
		}

		return result;

	}

public String smsDetSubmit(){
	
	logger.debug("inside [CallCenterAction][raisetwowaysms].. ");
	CallCenterDAO callcenterDAO = null;
	ArrayList<String> errors = null;
	String msgtxt = "";
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		requestJSON.put("refno", getRefno());
		requestJSON.put("status", getStatus());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		callcenterDAO = new CallCenterDAO();
		responseDTO = callcenterDAO.smsDetSubmit(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
			logger.debug("Response JSON [" + responseJSON + "]");
			
			this.rfrom=responseJSON.getString("rfrom");
			this.rdate=responseJSON.getString("rdate");
			this.rtxt=responseJSON.getString("rtxt");
			this.rto=responseJSON.getString("rto");
			this.ftxt=responseJSON.getString("ftxt");
			this.status=responseJSON.getString("status");
			this.refno=responseJSON.getString("refno");
			
			result = "success";
		} else {
			System.out.println("in hte action else");
			errors = (ArrayList<String>) responseDTO.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				addActionError(errors.get(i));
			}
			result = "fail";
		}
	} catch (Exception e) {
		result = "fail";
		logger.debug("Exception in [CallCenterAction][raisetwowaysms] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		callcenterDAO = null;
	}

	return result;

}


public String smsDetAck(){
	
	logger.debug("inside [CallCenterAction][smsDetAck].. ");
	CallCenterDAO callcenterDAO = null;
	ArrayList<String> errors = null;
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		requestJSON.put("refno", getRefno());
		requestJSON.put("status", getStatus());
		requestJSON.put("rfrom", getRfrom());
		requestJSON.put("ftxt", getFtxt());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		requestJSON.put("remarks", getRemarks());
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("requestJSON [" + requestJSON.toString() + "]");
		logger.debug("request DTO [" + requestDTO + "]");
		callcenterDAO = new CallCenterDAO();
		responseDTO = callcenterDAO.smsDetAck(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get("INSERTED");
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
		logger.debug("Exception in [CallCenterAction][smsDetAck] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		callcenterDAO = null;
	}

	return result;

}

public String directFailedTxns(){
	
	logger.debug("inside [CallCenterAction][fetchFailedTxns].. ");
	CallCenterDAO callcenterDAO = null;
	ArrayList<String> errors = null;
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		
		
		requestJSON.put("cid", getCid());
		requestJSON.put("srchval", getSrchval());
		requestJSON.put("status", getStatus());
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO + "]");
		callcenterDAO = new CallCenterDAO();
		responseDTO = callcenterDAO.directFailedTxns(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
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
		logger.debug("Exception in [CallCenterAction][fetchFailedTxns] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		callcenterDAO = null;
	}

	return result;

}	

public String directTopUpDet(){
	
	logger.debug("inside [CallCenterAction][directTopUpDet].. ");
	CallCenterDAO callcenterDAO = null;
	ArrayList<String> errors = null;
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		System.out.println("value of get status :"+getStatus());
		
		requestJSON.put("cdate", getCdate());
		requestJSON.put("cid", getCid());
		requestJSON.put("mob", getMob());
		requestJSON.put("amt", getAmt());
		requestJSON.put("rid", getRid());
		requestJSON.put("status", getStatus());
		requestJSON.put("refno", getRefno());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		logger.debug("requestJSON [" + requestJSON.toString() + "]");
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		
		callcenterDAO = new CallCenterDAO();
		responseDTO = callcenterDAO.directForceTopUpDet(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} else {
			System.out.println("in hte action else");
			errors = (ArrayList<String>) responseDTO.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				addActionError(errors.get(i));
			}
			result = "fail";
		}
	} catch (Exception e) {
		result = "fail";
		logger.debug("Exception in [CallCenterAction][directTopUpDet] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		callcenterDAO = null;
	}

	return result;

}	

public String directTopUpConf(){
	
	logger.debug("inside [CallCenterAction][forceTopUpConf].. ");
	CallCenterDAO callcenterDAO = null;
	ArrayList<String> errors = null;
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		System.out.println("value of get status :"+getStatus()+"--"+getNewmob());
		
		requestJSON.put("rid", getRid());
		requestJSON.put("newmob", getNewmob());
		requestJSON.put("mob", getMob());
		requestJSON.put("amt", getAmt());
		requestJSON.put("cid", getCid());
		requestJSON.put("cdate", getCdate());
		requestJSON.put("remarks", getRemarks());
		requestJSON.put("refno", getRefno());
		requestJSON.put("arid", getArid());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		System.out.println("requestJSON:"+requestJSON.toString());
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		callcenterDAO = new CallCenterDAO();
		responseDTO = callcenterDAO.directForceTopUp(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} else {
			System.out.println("in hte action else");
			errors = (ArrayList<String>) responseDTO.getErrors();
			for (int i = 0; i < errors.size(); i++) {
				addActionError(errors.get(i));
			}
			result = "fail";
		}
	} catch (Exception e) {
		result = "fail";
		logger.debug("Exception in [CallCenterAction][forceTopUpConf] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		callcenterDAO = null;
	}

	return result;

}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}


	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
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

	public String getSrchval() {
		return srchval;
	}

	public void setSrchval(String srchval) {
		this.srchval = srchval;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getBgender() {
		return bgender;
	}

	public void setBgender(String bgender) {
		this.bgender = bgender;
	}

	public String getBdob() {
		return bdob;
	}

	public void setBdob(String bdob) {
		this.bdob = bdob;
	}

	public String getBidno() {
		return bidno;
	}

	public void setBidno(String bidno) {
		this.bidno = bidno;
	}

	public String getBmob() {
		return bmob;
	}

	public void setBmob(String bmob) {
		this.bmob = bmob;
	}

	public String getBcdate() {
		return bcdate;
	}

	public void setBcdate(String bcdate) {
		this.bcdate = bcdate;
	}

	public String getBstatus() {
		return bstatus;
	}

	public void setBstatus(String bstatus) {
		this.bstatus = bstatus;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getVcnt() {
		return vcnt;
	}

	public void setVcnt(String vcnt) {
		this.vcnt = vcnt;
	}

	public String getBsign() {
		return bsign;
	}

	public void setBsign(String bsign) {
		this.bsign = bsign;
	}

	public String getCases() {
		return cases;
	}

	public void setCases(String cases) {
		this.cases = cases;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getRefno() {
		return refno;
	}

	public void setRefno(String refno) {
		this.refno = refno;
	}

	public String getRsddt() {
		return rsddt;
	}

	public void setRsddt(String rsddt) {
		this.rsddt = rsddt;
	}

	public String getRsdby() {
		return rsdby;
	}

	public void setRsdby(String rsdby) {
		this.rsdby = rsdby;
	}

	public String getCompid() {
		return compid;
	}

	public void setCompid(String compid) {
		this.compid = compid;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getNewmob() {
		return newmob;
	}

	public void setNewmob(String newmob) {
		this.newmob = newmob;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getRfrom() {
		return rfrom;
	}
	public void setRfrom(String rfrom) {
		this.rfrom = rfrom;
	}
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public String getRtxt() {
		return rtxt;
	}
	public void setRtxt(String rtxt) {
		this.rtxt = rtxt;
	}
	public String getRto() {
		return rto;
	}
	public void setRto(String rto) {
		this.rto = rto;
	}
	public String getFtxt() {
		return ftxt;
	}
	public void setFtxt(String ftxt) {
		this.ftxt = ftxt;
	}
	public String getRetstr() {
		return retstr;
	}
	public void setRetstr(String retstr) {
		this.retstr = retstr;
	}
	public String getAcn() {
		return acn;
	}
	public void setAcn(String acn) {
		this.acn = acn;
	}
	public String getCprim() {
		return cprim;
	}
	public void setCprim(String cprim) {
		this.cprim = cprim;
	}
	public String getCasrd() {
		return casrd;
	}
	public void setCasrd(String casrd) {
		this.casrd = casrd;
	}
	public String getArid() {
		return arid;
	}
	public void setArid(String arid) {
		this.arid = arid;
	}
	public String getMapto() {
		return mapto;
	}
	public void setMapto(String mapto) {
		this.mapto = mapto;
	}

	
}
