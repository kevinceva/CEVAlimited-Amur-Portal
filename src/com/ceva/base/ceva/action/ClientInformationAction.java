package com.ceva.base.ceva.action;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CevaPowerAdminDAO;
import com.ceva.base.common.dao.ClientInformationDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ClientInformationAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(ClientInformationAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	private HttpSession session = null;
	
	private HttpServletRequest httpRequest = null;

	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}


	private String groupId = null;
	private String clientId = null;
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
	
	private String method = null;
	private String prevJsonString = null;
	private String clientInfoPage = null;

	private String jsonVal = null;
	private String groupDesc = null;
	private String getPrevJsonString() {
		return prevJsonString;
	}

	public void setPrevJsonString(String prevJsonString) {
		this.prevJsonString = prevJsonString;
	}



	public String getClientInfoPage() {
		return clientInfoPage;
	}

	public void setClientInfoPage(String clientInfoPage) {
		this.clientInfoPage = clientInfoPage;
	}

	public String getJsonVal() {
		return jsonVal;
	}

	public void setJsonVal(String jsonVal) {
		this.jsonVal = jsonVal;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String type;
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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


	public String userId;
	
	public String dashboardUsers(){
		return "success";
	}
	
	public String fetchClientDetails() {
		logger.debug("inside [ClientInformationAction][fetchClientDetails].. ");
		ClientInformationDAO clientInfoDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			clientInfoDAO = new ClientInformationDAO();
			responseDTO = clientInfoDAO.fetchClientDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("GROUP_LIST");
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
			logger.debug("Exception in [ClientInformationAction][fetchClientDetails] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			clientInfoDAO = null;
		}

		return result;
	}

	public String fetchPolicyList() {
		logger.debug("inside [ClientInformationAction][fetchPolicyList].. ");
		ClientInformationDAO clientInfoDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			System.out.println("value of id:"+getGroupId());
			requestJSON.put("GROUP_ID", getGroupId());
			requestJSON.put("CLIENT_ID", getGroupId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			clientInfoDAO = new ClientInformationDAO();
			responseDTO = clientInfoDAO.fetchPolicyList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("USER_LIST");
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
			logger.debug("Exception in [ClientInformationAction][fetchUsersList] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			clientInfoDAO = null;
		}

		return result;
	}
	
	public String viewClientInfo() {
		logger.debug("Inside viewClientInfo. ");
		ClientInformationDAO clientInfoDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			System.out.println("value of client id:"+getGroupId());
			requestJSON.put("CLIENT_ID", getGroupId());

			requestJSON.put("TYPE", getType());

			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			clientInfoDAO = new ClientInformationDAO();
			responseDTO = clientInfoDAO.viewClientInfo(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"client_info");
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
			logger.debug("Exception in View UserGroup [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
			e.printStackTrace();
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			clientInfoDAO = null;
			errors = null;
		}
		return result;
	}	
	
	public String fetchPurchaseList(){
		
		logger.debug("inside [ClientInformationAction][fetchPurchaseList].. ");
		ClientInformationDAO clientInfoDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			
			requestJSON.put("USER_ID", getUserId());
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			clientInfoDAO = new ClientInformationDAO();
			responseDTO = clientInfoDAO.fetchPurchaseList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("PURG_STMT");
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
			logger.debug("Exception in [ClientInformationAction][fetchPurchaseList] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			clientInfoDAO = null;
		}

		return result;

	}

	public String clientInformation() {
		logger.debug("Inside clientInformation..");
		ClientInformationDAO clientInfoDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("GROUP_ID", getGroupId());
			requestJSON.put("USER_ID", getUserId());
			requestJSON.put("TYPE", getType());
			requestJSON.put("PARENT_ID", "");
			requestJSON.put("MAKER_ID",
					session.getAttribute(CevaCommonConstants.MAKER_ID)
							.toString());

			logger.debug("Request JSON [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);

			clientInfoDAO = new ClientInformationDAO();
			responseDTO = clientInfoDAO.clientInformation(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (getType().equalsIgnoreCase("Modify")) {
				clientInfoPage = "clientModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				clientInfoPage = "clientViewInformation";
			} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
				clientInfoPage = "userActivate";
			} else {
				clientInfoPage = "userPasswordReset";
			}

			logger.debug(" clientInfoPage [" + clientInfoPage + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
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
			logger.debug("Exception in Client Information [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			clientInfoDAO = null;
			errors = null;
		}

		return result;
	}
	
	public String clientModifyConfirm() {
		logger.debug("Inside clientModifyConfirm...");

		logger.debug("Type[" + getType() + "]");
		logger.debug("bstatus[" + getBstatus() + "]");
		logger.debug("httpRequest[" + httpRequest + "]");

		responseJSON = constructToResponseJson(httpRequest);
		
		logger.debug("responseJSON[" + responseJSON + "]");
		if (getType().equalsIgnoreCase("Modify")) {
			clientInfoPage = "clientModifyConfirm";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			clientInfoPage = "clientActivate";
		} else if (getType().equalsIgnoreCase("PasswordReset")) {
			clientInfoPage = "clientPasswordReset";
		} else if (getType().equalsIgnoreCase("PasswordResetConfirm")) {
			clientInfoPage = "clientPasswordResetConfirm";
		}

		return SUCCESS;
	}

	public String clientModifyAck() {

		logger.debug("Inside clientModifyAck Type[" + getType() + "]");

		String modData = "";
		ClientInformationDAO clientInfoDAO = null;
		ArrayList<String> errors = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();
			/*modData = constructStanderedString(httpRequest);
			requestDTO.getRequestJSON().put("user_info", modData);
			requestDTO.getRequestJSON().put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));*/
			requestJSON.put("cid", getCid());
			requestJSON.put("cname", getCname());
			requestJSON.put("gender", getGender());
			requestJSON.put("dob", getDob());
			requestJSON.put("idtype", getIdtype());
			requestJSON.put("idno", getIdno());
			requestJSON.put("mob", getMob());
			requestJSON.put("addr", getAddr());
			requestJSON.put("pcode", getPcode());
			requestJSON.put("email", getEmail());
			requestJSON.put("cdate", getCdate());
			requestJSON.put("status", getStatus());
			
			requestJSON.put("bid", getBid());
			requestJSON.put("bname", getBname());
			requestJSON.put("bgender", getBgender());
			requestJSON.put("bdob", getBdob());
			requestJSON.put("bidno", getBidno());
			requestJSON.put("bmob", getBmob());
			requestJSON.put("bcdate", getBcdate());
			requestJSON.put("bstatus", getBstatus());
			requestJSON.put("photo", getPhoto());
			requestJSON.put("vcnt", getVcnt());
			
			logger.debug("Request JSON [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");

			clientInfoDAO = new ClientInformationDAO();
			responseDTO = clientInfoDAO.clientModifyAck(requestDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"user_rights");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			responseJSON = constructToResponseJson(httpRequest);

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in clientModifyAck [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			clientInfoDAO = null;
			errors = null;
			modData = null;

		}
		return result;
	}
	

	private String constructStanderedString(HttpServletRequest httpRequest) {
		String key = null;
		String value = null;

		Enumeration enumParams = null;
		StringBuffer totalInfo = null;
		String totVal = "";

		logger.debug("Client Information - [constructStanderedString] inside");

		try {
			totalInfo = new StringBuffer(100);
			totalInfo.append("");
			enumParams = httpRequest.getParameterNames();

			while (enumParams.hasMoreElements()) {

				key = (String) enumParams.nextElement();
				value = httpRequest.getParameter(key);

				value = (value == null || value.equals("")) ? "NO_VALUE"
						: value;

				if (key.indexOf("CV") != -1) {
					totVal = key
							+ StringUtils.leftPad(
									String.valueOf(value.length()), 3, "0")
							+ value;
					totalInfo.append(totVal);
				}
			}

		} catch (Exception e) {
			logger.debug("Exception is :: " + e.getMessage());
		} finally {
			key = null;
			value = null;
			enumParams = null;
			totVal = null;
			totVal = totalInfo.toString();
			totalInfo.delete(0, totalInfo.length());
		}

		return totVal;
	}
	
	
	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {
		Enumeration enumParams = null;
		JSONObject jsonObject = null;
		logger.debug("Client Information - Inside ConstructToResponseJson...");
		try {
			enumParams = httpRequest.getParameterNames();

			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" Client Information - Exception in ConstructToResponseJson ["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
		}
		logger.debug(" jsonObject[" + jsonObject + "]");

		return jsonObject;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}


	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	


	
	
}
