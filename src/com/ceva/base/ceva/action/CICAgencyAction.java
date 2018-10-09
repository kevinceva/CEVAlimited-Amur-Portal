package com.ceva.base.ceva.action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.struts2.ServletActionContext;

import util.StringUtil;

import com.ceva.base.common.dao.CICAgencyDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;
import com.ceva.util.Validation;

public class CICAgencyAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CICAgencyAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject finalerrrespjson=new JSONObject();
	
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
	private String refmob = null;
	private String comp = null;
	
	
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
	
	private String refmob2 = null;
	private String refmob3 = null;
	private String refmob4 = null;
	private String refmob5 = null;
	private String refmob6 = null;
	private String refmob7 = null;
	private String refmob8 = null;
	private String refmob9 = null;
	private String refmob10 = null;
	private String refmob11 = null;
	private String refmob12 = null;
	private String refmob13 = null;
	private String refmob14 = null;
	private String refmob15 = null;
	private String refmob16 = null;
	private String refmob17 = null;
	private String refmob18 = null;
	private String refmob19 = null;
	private String refmob20 = null;
	
	
	private File uploadFile;
	private String uploadFileFileName;	
	private String fileName;
	
	
/*	public String getMerchantDashBoard(){
		return "success";
	}*/
	
	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}	
	public String fetchClientDetails() {
		logger.debug("inside [CICAgencyAction][fetchClientDetails].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			System.out.println("sercha value===="+getSrchval());
			requestJSON = new JSONObject();
			requestJSON.put("mob", getMob() );
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getClientDetails(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][fetchClientDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;
	}

	public String agentSave(){
		
		logger.debug("inside [CICAgencyAction][agentSave].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			requestJSON.put("cname", getCname());
			requestJSON.put("mob", getMob());
			requestJSON.put("email", getEmail());
			requestJSON.put("refmob", getRefmob());
			requestJSON.put("comp", getComp());
			requestJSON.put("remarks", getRemarks());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO.toString() + "] ");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.insertAgent(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][insertScenarios] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}	
	
	public String fetchRaisedCases(){
		
		logger.debug("inside [CICAgencyAction][fetchRaisedCases].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getRaisedCases(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][fetchRaisedCases] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}
	
	public String fetchRaisedCasedetails(){
		
		logger.debug("inside [CICAgencyAction][fetchRaisedCasedetails].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("status", getStatus());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getRaisedCasedetails(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][fetchRaisedCasedetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}	
	
	public String submitCompAction(){
		
		logger.debug("inside [CICAgencyAction][submitCompAction].. ");
		CICAgencyDAO cicagencyDAO = null;
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
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.submitCompAction(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][submitCompAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}	
	
	public String approveCompAction(){
		
		logger.debug("inside [CICAgencyAction][approveCompAction].. ");
		CICAgencyDAO cicagencyDAO = null;
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
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.approveCompAction(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][approveCompAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}	
	
	public String confirmCompAction(){
		
		logger.debug("inside [CICAgencyAction][confirmCompAction].. ");
		CICAgencyDAO cicagencyDAO = null;
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
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.confirmCompAction(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][confirmCompAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}	
	
public String fetchFailedTxns(){
		
		logger.debug("inside [CICAgencyAction][fetchFailedTxns].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("cid", getCid());
			requestJSON.put("srchval", getSrchval());
			requestJSON.put("status", getStatus());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getFailedTxns(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][fetchFailedTxns] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}	

	
	public String forceTopUpDet(){
		
		logger.debug("inside [CICAgencyAction][forceTopUpDet].. ");
		CICAgencyDAO cicagencyDAO = null;
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
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getForceTopUpDet(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][forceTopUpDet] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			cicagencyDAO = null;
		}
	
		return result;
	
	}	

	public String forceTopUpConf(){
		
		logger.debug("inside [CICAgencyAction][forceTopUpConf].. ");
		CICAgencyDAO cicagencyDAO = null;
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
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.initiateForceTopUp(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][forceTopUpConf] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			cicagencyDAO = null;
		}
	
		return result;
	
	}
	
public String fetchtwowaysmsdetails(){
		
		logger.debug("inside [CICAgencyAction][fetchtwowaysmsdetails].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			/*requestJSON = new JSONObject();
			requestDTO = new RequestDTO();		
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getTwoWaysSMSdetails(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][fetchtwowaysmsdetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}
		result = "success";
		return result;

	}	

public String raisetwowaysms(){
	
	logger.debug("inside [CICAgencyAction][raisetwowaysms].. ");
	CICAgencyDAO cicagencyDAO = null;
	ArrayList<String> errors = null;
	String msgtxt = "";
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		System.out.println("value of get status :"+getRtxt());
		
		requestJSON.put("rfrom", getRfrom());
		requestJSON.put("rdate", getRdate());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		cicagencyDAO = new CICAgencyDAO();
		responseDTO = cicagencyDAO.submitSMSAction(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
			logger.debug("Response JSON [" + responseJSON + "]");
			
			this.rfrom=responseJSON.getString("rfrom");
			this.rdate=responseJSON.getString("rdate");
			this.rtxt=responseJSON.getString("rtxt");
			this.rto=responseJSON.getString("rto");
			
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
		logger.debug("Exception in [CICAgencyAction][raisetwowaysms] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		cicagencyDAO = null;
	}

	return result;

}
	
	public String confirmRaiseSMSAction(){
		
		logger.debug("inside [CICAgencyAction][confirmRaiseSMSAction].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			session = ServletActionContext.getRequest().getSession();
			
			requestJSON.put("rfrom", getRfrom());
			requestJSON.put("rdate", getRdate());
			requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put("remarks", getRemarks());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("requestJSON [" + requestJSON.toString() + "]");
			logger.debug("request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.confirmRaiseSMSAction(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][confirmRaiseSMSAction] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
	
			errors = null;
			cicagencyDAO = null;
		}
	
		return result;
	
	}	
	
public String fetchraisedsmsdetails(){
		
		logger.debug("inside [CICAgencyAction][fetchtwowaysmsdetails].. ");
		CICAgencyDAO cicagencyDAO = null;
		ArrayList<String> errors = null;
		try {
			/*requestJSON = new JSONObject();
			requestDTO = new RequestDTO();		
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			cicagencyDAO = new CICAgencyDAO();
			responseDTO = cicagencyDAO.getRaisedSMSdetails(requestDTO);
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
			logger.debug("Exception in [CICAgencyAction][fetchtwowaysmsdetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			cicagencyDAO = null;
		}

		return result;

	}

public String agentMapSave(){
	
	logger.debug("inside [CICAgencyAction][agentSave].. ");
	CICAgencyDAO cicagencyDAO = null;
	ArrayList<String> errors = null;
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		requestJSON.put("mob", getMob());
		requestJSON.put("refmob", getRefmob());
		requestJSON.put("refmob2", getRefmob2());
		requestJSON.put("refmob3", getRefmob3());
		requestJSON.put("refmob4", getRefmob4());
		requestJSON.put("refmob5", getRefmob5());
		requestJSON.put("refmob6", getRefmob6());
		requestJSON.put("refmob7", getRefmob7());
		requestJSON.put("refmob8", getRefmob8());
		requestJSON.put("refmob9", getRefmob9());
		requestJSON.put("refmob10", getRefmob10());
		requestJSON.put("refmob11", getRefmob11());
		requestJSON.put("refmob12", getRefmob12());
		requestJSON.put("refmob13", getRefmob13());
		requestJSON.put("refmob14", getRefmob14());
		requestJSON.put("refmob15", getRefmob15());
		requestJSON.put("refmob16", getRefmob16());
		requestJSON.put("refmob17", getRefmob17());
		requestJSON.put("refmob18", getRefmob18());
		requestJSON.put("refmob19", getRefmob19());
		requestJSON.put("refmob20", getRefmob20());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "] ");
		cicagencyDAO = new CICAgencyDAO();
		responseDTO = cicagencyDAO.insertAgent(requestDTO);
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
		logger.debug("Exception in [CICAgencyAction][insertScenarios] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		cicagencyDAO = null;
	}

	return result;

}

public String execute()
{
	{		

		
		logger.debug("##############################  Agent Mapping Upload Sart ##################");

		boolean finalStatus=true;
		int exlrownum =0;
		String userId="";
		String fileName="";
		String result="";
		
		JSONArray finalData=new JSONArray();
		JSONObject reqJSON=new JSONObject();
		CICAgencyDAO cicagencyDAO = null;

		try{

			HttpSession session=ServletActionContext.getRequest().getSession(); 
			userId= (String) session.getAttribute("userid");

			String	pathx =  session.getServletContext().getRealPath("/Cards");	
			
			logger.debug("pathx value["+pathx+"]");

			File fileToCreate = new File(pathx, uploadFileFileName);
			FileUtils.copyFile(uploadFile, fileToCreate);
			fileName = uploadFileFileName; 

			String entitycode = (String) session.getAttribute("entitycode");

			logger.info("uploadFileFileName ["+uploadFileFileName+"] Path ["+pathx+"] entitycode ["+entitycode+"]");
			
			//Variable Declaration Started
		
			JSONArray errorresarayJSON=new JSONArray();

			String agentNo="";
			String custNo="";

			CICAgencyDAO finalobj = new CICAgencyDAO();
			
			
			logger.info("filename ["+fileName+"] ");
		
		

			if (!fileName.isEmpty()) {

				File empdepndexcel = new File(pathx + "/" + fileName);
				FileInputStream inputStream1 = new FileInputStream(empdepndexcel);
				POIFSFileSystem fileSystem1 = new POIFSFileSystem(inputStream1);
				HSSFWorkbook wb1 = new HSSFWorkbook(fileSystem1);
				List<String> custmobs = new ArrayList<String>();

				HSSFSheet hospsheet = wb1.getSheetAt(0);
				java.util.Iterator rowIterator1 = hospsheet.rowIterator();

				while (rowIterator1.hasNext()) {

					HSSFRow row = (HSSFRow) rowIterator1.next();
					

					logger.debug("Row Number ["+ row.getRowNum()+"]");

					if(row.getRowNum()==0)
					{
						logger.error("Error File");

					}else{ 
						boolean finalstatus=isBlankRow(row, 35);

						logger.info("Blank Status ["+finalstatus+"]");
						
						if(finalstatus)
						{
							break;

						}else{
							
							exlrownum = row.getRowNum()+1;								
							
							agentNo=getCellData(row, 0,"STR");
							custNo=getCellData(row, 1,"STR");
							
							if(!custmobs.contains(custNo)){
								custmobs.add(custNo);
							}
							else{
								logger.info("duplicate customer mobile no found..:"+custNo);
								errorresarayJSON = getJsonObject("Duplicate customer mobile no Found :"+custNo, exlrownum, errorresarayJSON);
							}							
							

						logger.info("Row Data [agentNo=" + agentNo + ", custNo=" + custNo + "]");
						
						if(StringUtil.isNullOrEmpty(agentNo)){						
								finalStatus=false;
								remarks="Please Enter the agentNo";
								errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);							
						}
						
						if(StringUtil.isNullOrEmpty(custNo)){						
								finalStatus=false;
								remarks="Please Enter the custNo";
								errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);							
						}

						reqJSON.put("agentNo", agentNo);
						reqJSON.put("custNo", custNo);
						
						
						
						Boolean valid = finalobj.agentnovalid(agentNo);
						if(!valid)
						{
							
							finalStatus=false;
							remarks="Champions Must Be The Active Bima Credo Clients";
							errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);
							
						}
						
						Boolean cvalid = finalobj.custnovalid(custNo);
						if(!cvalid)
						{
							
							finalStatus=false;
							remarks="Client Mobile Already Registered";
							errorresarayJSON= getJsonObject(remarks, exlrownum, errorresarayJSON);
							
						}

						finalData.add(reqJSON);
					}

					}
				}
				
				//Functional Validations Start
				logger.debug("Functional Validation Start");

				logger.debug("Final Validation Status ["+finalStatus+"]");

				if(finalStatus)
				{

					//EndorsementDAO finalobj = new EndorsementDAO();
					logger.debug("There is no Validation Errors........."+finalData.toString());
					
					String mapStatus =finalobj.agentClientMapping(finalData,userId,fileName); //Need To do Chnage
					logger.debug("After Saved All Excel meber Data into [agent map] Table mapStatus["+mapStatus+"]");
					if (null != mapStatus && "SUCCESS".equalsIgnoreCase(mapStatus))
					{	
						finalerrrespjson.put("STATUS","S");
						finalerrrespjson.put("NOOfRows",exlrownum-1);
						//finalerrrespjson.put("PremiumDetails",premiumDet);
						logger.info("Agent Mapping Upload Action Final Response Data ["+finalerrrespjson+"]");

						result = ActionSupport.SUCCESS;
						return result;
					}						
					else
					{
						JSONObject errorresJSON=new JSONObject();
						errorresJSON.put("uploadErrors", "Agent Mapping Upload FAILED DUE TO INTERNAL ERROR");
						//errorresJSON.put("excelRowNo", errorData[0]);
						errorresarayJSON.add(errorresJSON);
						finalerrrespjson.put("FINALJSON", errorresarayJSON);
						finalerrrespjson.put("STATUS","F");
						logger.debug(finalerrrespjson);
						result = ActionSupport.SUCCESS;
					}

				}else
				{
					logger.debug("Validation Error Happen ["+errorresarayJSON+"]");
					finalerrrespjson.put("FINALJSON", errorresarayJSON);
					finalerrrespjson.put("STATUS","F");
					logger.debug(finalerrrespjson);
					result = ActionSupport.SUCCESS;
				}

				//finalobj.membarendsmentProcess(finalData);

				logger.debug("##############################  Agent Mapping File Upload End ##################");

			}
			

		}catch(Exception e)
		{	

			finalerrrespjson.put("STATUS","E");
			e.printStackTrace();
			addActionError(e.getMessage());
			//Need To write Code Here for failure
			return ActionSupport.SUCCESS;


		}
		finally
		{
			finalStatus=true;

		}
		return result;
	}
}



public  String getCellData(HSSFRow row , int celno,String type)
{

	String str = "";
	try
	{

		if (row.getCell((short) celno) == null) {
			str = "";
		} else {       

			if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_NUMERIC){
				//long m1 = (long)row.getCell((short) celno).getNumericCellValue();
				
				 HSSFCell cell =  row.getCell((short) celno);
				
				if(type.equals("DATE"))
				{
					 
					str=(String)getDateValue(cell);
					
				}else
				{
					long m1 = (long)cell.getNumericCellValue();
					str=Long.toString(m1);
				}
			}
			else if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_STRING){
				//str = row.getCell((short) celno).getStringCellValue();

				str = row.getCell((short) celno).getStringCellValue();

			}  else	if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_STRING){
					str = row.getCell((short) celno).getBooleanCellValue()+"";

				}else
					{
						str="";
					}

		}

	}catch(Exception e)
	{
		logger.error("Exception Raised ["+e.getMessage()+"]");
		e.printStackTrace();
	}

	return str;

}	

 public static Object getDateValue(HSSFCell cell) 
  {
      String finalDate="";
      double numericValue = cell.getNumericCellValue();
      java.util.Date date = HSSFDateUtil.getJavaDate(numericValue);
      // Add the timezone offset again because it was subtracted automatically by Apache-POI (we need UTC)
      long tzOffset = TimeZone.getDefault().getOffset(date.getTime());
      date = new Date(date.getTime() + tzOffset);
      finalDate=Validation.convertDateToString(date, "dd/MM/YYYY");
      
      return finalDate;
    
  }
 
	public static boolean isBlankRow(HSSFRow row,int collength)
	{

		boolean status = false;
		int finalcount=0;

		try
		{
			if(row!=null){


				for(int celno=0;celno<collength;celno++)
				{

					
					if(row.getCell((short) celno)==null)
					{
						++finalcount;
					}else
					if(row.getCell((short) celno).getCellType()==Cell.CELL_TYPE_BLANK){
						++finalcount;
					}else
					{
						
					}
				}

				//logger.info("finalcount ["+finalcount+"] collength ["+collength+"]");
				
				
				if(finalcount==collength)
					status=true;

			}else
			{
				 status=true;
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
			status=true;
		}

		return status;

	}	
	
	public static JSONArray getJsonObject(String remarks,int exlrownum , JSONArray errorresarayJSON)
	{

		JSONObject errorresJSON=new JSONObject();
		errorresJSON.put("uploadErrors", remarks);
		errorresJSON.put("excelRowNo", ""+exlrownum);

		errorresarayJSON.add(errorresJSON);

		return errorresarayJSON;

	}
	
	public static JSONArray buildErrorJsonObj(ArrayList<String> errorList,JSONArray errorresarayJSON)
	{
		for (String string : errorList) {

			String []errorData = string.split("-");

			if(errorData.length==2)
			{
				System.out.println("Error Data : "+errorData[0]+"   "+errorData[1]);
				JSONObject errorresJSON=new JSONObject();
				errorresJSON.put("uploadErrors", errorData[1]);
				errorresJSON.put("excelRowNo", errorData[0]);
				errorresarayJSON.add(errorresJSON);
			}
		}

		return errorresarayJSON;
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
	public String getRefmob() {
		return refmob;
	}
	public void setRefmob(String refmob) {
		this.refmob = refmob;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
	public JSONObject getFinalerrrespjson() {
		return finalerrrespjson;
	}
	public void setFinalerrrespjson(JSONObject finalerrrespjson) {
		this.finalerrrespjson = finalerrrespjson;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getUploadFileFileName() {
		return uploadFileFileName;
	}
	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRefmob2() {
		return refmob2;
	}
	public void setRefmob2(String refmob2) {
		this.refmob2 = refmob2;
	}
	public String getRefmob3() {
		return refmob3;
	}
	public void setRefmob3(String refmob3) {
		this.refmob3 = refmob3;
	}
	public String getRefmob4() {
		return refmob4;
	}
	public void setRefmob4(String refmob4) {
		this.refmob4 = refmob4;
	}
	public String getRefmob5() {
		return refmob5;
	}
	public void setRefmob5(String refmob5) {
		this.refmob5 = refmob5;
	}
	public String getRefmob6() {
		return refmob6;
	}
	public void setRefmob6(String refmob6) {
		this.refmob6 = refmob6;
	}
	public String getRefmob7() {
		return refmob7;
	}
	public void setRefmob7(String refmob7) {
		this.refmob7 = refmob7;
	}
	public String getRefmob8() {
		return refmob8;
	}
	public void setRefmob8(String refmob8) {
		this.refmob8 = refmob8;
	}
	public String getRefmob9() {
		return refmob9;
	}
	public void setRefmob9(String refmob9) {
		this.refmob9 = refmob9;
	}
	public String getRefmob10() {
		return refmob10;
	}
	public void setRefmob10(String refmob10) {
		this.refmob10 = refmob10;
	}
	public String getRefmob11() {
		return refmob11;
	}
	public void setRefmob11(String refmob11) {
		this.refmob11 = refmob11;
	}
	public String getRefmob12() {
		return refmob12;
	}
	public void setRefmob12(String refmob12) {
		this.refmob12 = refmob12;
	}
	public String getRefmob13() {
		return refmob13;
	}
	public void setRefmob13(String refmob13) {
		this.refmob13 = refmob13;
	}
	public String getRefmob14() {
		return refmob14;
	}
	public void setRefmob14(String refmob14) {
		this.refmob14 = refmob14;
	}
	public String getRefmob15() {
		return refmob15;
	}
	public void setRefmob15(String refmob15) {
		this.refmob15 = refmob15;
	}
	public String getRefmob16() {
		return refmob16;
	}
	public void setRefmob16(String refmob16) {
		this.refmob16 = refmob16;
	}
	public String getRefmob17() {
		return refmob17;
	}
	public void setRefmob17(String refmob17) {
		this.refmob17 = refmob17;
	}
	public String getRefmob18() {
		return refmob18;
	}
	public void setRefmob18(String refmob18) {
		this.refmob18 = refmob18;
	}
	public String getRefmob19() {
		return refmob19;
	}
	public void setRefmob19(String refmob19) {
		this.refmob19 = refmob19;
	}
	public String getRefmob20() {
		return refmob20;
	}
	public void setRefmob20(String refmob20) {
		this.refmob20 = refmob20;
	}

	
}
