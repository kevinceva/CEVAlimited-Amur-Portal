package com.ceva.base.common.action;

import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.AgentRegDAOAction;
import com.ceva.base.common.dao.CICAgencyDAO;
import com.ceva.base.common.dao.CallCenterDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class AgentRegAction extends ActionSupport {

	Logger logger = Logger.getLogger(AgentRegAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	private HttpSession session = null;

	private String result = null;
	private String userId = null;
	private String macAddress = null;
	private String lanIp = null;
	private String ulsid = null;
	private String ulid = null;
	
	private String mob = null;
	private String refmob = null;
	private String comp = null;
	private String email = null;
	private String cname = null;
	private String cdate = null;
	private String acn = null;
	private String remarks = null;
	private String ackmsg = null;

	public String getUlid() {
		return ulid;
	}

	public void setUlid(String ulid) {
		this.ulid = ulid;
	}

	static char[] hexval = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F' };
	
	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}

	
	/*public String InsertWebLoginDetails() {
		logger.debug(" Inside AgentRegAction InsertWebLoginDetails... ");
		String randomNum = "";
		AgentRegDAOAction agentDAO = null;
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

				agentDAO = new AgentRegDAOAction();
				responseDTO = agentDAO.WebLoginDetails(requestDTO);

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
					logger.debug("Response JSON [" + responseJSON + "]");
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					//ulid = randomNum;

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
			logger.debug("Exception in  AgentRegAction InsertWebLoginDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			agentDAO = null;
			randomNum = null;
		}

		return result;
	}*/
	
public String fetchtSelfRegAgents(){
		
		logger.debug("inside [AgentRegAction][fetchtSelfRegAgents].. ");
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
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [AgentRegAction][fetchtSelfRegAgents] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}	

public String fetchAgentDetails(){
	
	logger.debug("inside [AgentRegAction][fetchAgentDetails].. ");
	AgentRegDAOAction agentDAO = null;
	ArrayList<String> errors = null;
	String msgtxt = "";
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		
		System.out.println("value of get status :"+getMob());
		
		requestJSON.put("mob", getMob());
		requestJSON.put("acn", getAcn());
		logger.debug("requestJSON [" + requestJSON.toString() + "]");
		//requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		agentDAO = new AgentRegDAOAction();
		responseDTO = agentDAO.fetchSelfRegAgentDet(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
			logger.debug("Response JSON [" + responseJSON + "]");
			
/*			if ("Success".equalsIgnoreCase(responseJSON.getString("retstr")))
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
			}*/
			
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
		logger.debug("Exception in [AgentRegAction][fetchAgentDetails] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		agentDAO = null;
	}

	return result;

}

public String validateAgent(){
	
	logger.debug("inside [AgentRegAction][validateAgent].. ");
	AgentRegDAOAction agentDAO = null;
	ArrayList<String> errors = null;
	String msgtxt = "";
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		
		System.out.println("value of get status :"+getMob());
		
		requestJSON.put("mob", getMob());
		logger.debug("requestJSON [" + requestJSON.toString() + "]");
		//requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "]");
		agentDAO = new AgentRegDAOAction();
		responseDTO = agentDAO.validateAgent(requestDTO);
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
		logger.debug("Exception in [AgentRegAction][validateAgent] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		agentDAO = null;
	}

	return result;

}

public String agentSave(){
	
	logger.debug("inside [AgentRegAction][agentSave].. ");
	AgentRegDAOAction agentDAO = null;
	ArrayList<String> errors = null;
	try {
		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		session = ServletActionContext.getRequest().getSession();
		
		requestJSON.put("mob", getMob());
		requestJSON.put("remarks", getRemarks());	
		requestJSON.put("acn", getAcn());
		requestJSON.put("cname", getCname());
		requestJSON.put("email", getEmail());
		requestJSON.put("mkrid", session.getAttribute(CevaCommonConstants.MAKER_ID));
		
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO.toString() + "] ");
		agentDAO = new AgentRegDAOAction();
		responseDTO = agentDAO.activeAgent(requestDTO);
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
		logger.debug("Exception in [AgentRegAction][insertScenarios] [" + e.getMessage()
				+ "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;

		errors = null;
		agentDAO = null;
	}

	return result;

}

	public String InsertAgency() {
		logger.debug(" Inside InsertAgency... ");
		String randomNum = "";
		AgentRegDAOAction agentDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			
			logger.debug("Request JSON [" + mob + "--" + refmob + "--" + comp + "--" + email + "--" + getMob() + "]");

			if (mob == null) {
				addActionError(" Mobile Number Missing");
				result = "fail";
			} else if (refmob == null) {
				addActionError(" Reference Number Missing");
				result = "fail";
			} else if (comp == null) {
				addActionError("Company / University Missing");
				result = "fail";
			} else if (email == null) {
				addActionError("E-mail ID Missing");
				result = "fail";				
			} else {

				requestJSON.put("mob", mob);
				requestJSON.put("refmob", refmob);
				requestJSON.put("comp", comp);
				requestJSON.put("email", email);

				//randomNum = getRandomValue();

				//requestJSON.put("ULSID", randomNum);

				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request JSON [" + requestJSON + "]");

				agentDAO = new AgentRegDAOAction();
				responseDTO = agentDAO.WebLoginDetails(requestDTO);

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get(CevaCommonConstants.MERCHANT_LIST);
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					ulid = randomNum;

					for (int i = 0; i < messages.size(); i++) {
						logger.debug("sdadasd:"+messages.get(i));
						ackmsg=messages.get(i);
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
			agentDAO = null;
			randomNum = null;
		}

		return result;
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

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getAcn() {
		return acn;
	}

	public void setAcn(String acn) {
		this.acn = acn;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAckmsg() {
		return ackmsg;
	}

	public void setAckmsg(String ackmsg) {
		this.ackmsg = ackmsg;
	}

	
}
