package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.AuthorizationAllDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class AuthorizationAllAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(AuthorizationAllAction.class);

	private HttpSession session = null;

	private String result;
	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String AUTH_CODE;
	private String userInfoPage = null;

	private String STATUS;
	private String REF_NO;
	private String DECISION;

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String AuthorizationList() {
		logger.debug("Inside AuthorizationList.. ");
		ArrayList<String> errors = null;
		AuthorizationAllDAO authDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			authDAO = new AuthorizationAllDAO();
			responseDTO = authDAO.getAuthorizationList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"AUTH_PENDING_LIST");
				logger.debug("Response JSON[" + responseJSON + "]");
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
			logger.debug("Exception in getAuthorizationList [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			authDAO = null;
		}

		return result;
	}

	public String CommonAuthList() {

		logger.debug("Inside CommonAuthList... ");
		AuthorizationAllDAO authDAO = null;
		ArrayList<String> errors = null;

		ResourceBundle rb = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			try {
				rb = ResourceBundle.getBundle("resource/headerdata");
			} catch (Exception e) {
				logger.debug("Exception while loading bundle please check the file.... headerdata under 'classes/resource' folder.");
			}

			requestJSON.put("auth_code", AUTH_CODE);
			requestJSON.put("status", STATUS);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			authDAO = new AuthorizationAllDAO();
			responseDTO = authDAO.CommonAuthList(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);

				logger.debug("Search String is [" + AUTH_CODE + "]");

				responseJSON.put("key_data", AUTH_CODE);
				responseJSON.put("headerData",
						rb.getString(AUTH_CODE + ".header"));
				responseJSON.put("search", AUTH_CODE == null ? " " : AUTH_CODE);
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
			logger.debug("Exception in CommonAuthList [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			e.printStackTrace();
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			authDAO = null;
		}

		return result;
	}

	public String CommonAuthCnf() {

		logger.debug("Inside CommonAuthCnf... ");
		AuthorizationAllDAO authDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("auth_code", AUTH_CODE);
			requestJSON.put("status", STATUS);
			requestJSON.put("ref_no", REF_NO);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			authDAO = new AuthorizationAllDAO();
			responseDTO = authDAO.CommonAuthConfirmationBefore(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

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

			if (AUTH_CODE.equalsIgnoreCase("USERAUTH")) {
				userInfoPage = "createuserauth";
			} else if (AUTH_CODE.equalsIgnoreCase("MODUSERAUTH")) {
				userInfoPage = "createuserauth";
			} else if (AUTH_CODE.equalsIgnoreCase("View")) {
				userInfoPage = "userViewInformation";
			} else if (AUTH_CODE.equalsIgnoreCase("ActiveDeactive")) {
				userInfoPage = "userActivate";
			} else if (AUTH_CODE.equalsIgnoreCase("SERVICEAUTH")) {
				userInfoPage = "CreateServiceAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("BINAUTH")) {
				userInfoPage = "CreateBinAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("SUBSEAUTH")) {
				userInfoPage = "CreateSubServiceAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("FEEAUTH")) {
				userInfoPage = "viewFeeDetailsAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("TERMAUTH")) {
				userInfoPage = "viewTerminalDetailsAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("STOREAUTH")) {
				userInfoPage = "viewStoreDetailsAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("MERCHAUTH")) {
				userInfoPage = "viewMerchantDetailsAuth";
			} else if (AUTH_CODE.equalsIgnoreCase("USERSTATUSAUTH")) {
				userInfoPage = "UserActiveDeactiveAuth";
			} else {
				userInfoPage = "userPasswordReset";
			}
			logger.debug("userInfoPage  [" + userInfoPage + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in CommonAuthCnf [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
			e.printStackTrace();
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			authDAO = null;
		}

		return result;
	}

	public String CommonAuthCnfsubmition() {

		logger.debug("Inside CommonAuthCnfsubmition... ");
		AuthorizationAllDAO authDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestJSON.put("auth_code", AUTH_CODE);
			requestJSON.put("status", STATUS);
			requestJSON.put("ref_no", REF_NO);
			requestJSON.put("decs", DECISION);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("RequestJSON [" + requestJSON + "]");

			logger.debug("Request DTO [" + requestDTO + "]");
			authDAO = new AuthorizationAllDAO();
			responseDTO = authDAO.CommonAuthCnfsubmition(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

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
			logger.debug("Exception in CommonAuthCnf [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
			e.printStackTrace();
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			authDAO = null;
		}

		return result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public JSONObject getRequestJSON() {
		return requestJSON;
	}

	public void setRequestJSON(JSONObject requestJSON) {
		this.requestJSON = requestJSON;
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

	public String getAUTH_CODE() {
		return AUTH_CODE;
	}

	public void setAUTH_CODE(String aUTH_CODE) {
		AUTH_CODE = aUTH_CODE;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getREF_NO() {
		return REF_NO;
	}

	public void setREF_NO(String rEF_NO) {
		REF_NO = rEF_NO;
	}

	public String getUserInfoPage() {
		return userInfoPage;
	}

	public void setUserInfoPage(String userInfoPage) {
		this.userInfoPage = userInfoPage;
	}

	public String getDECISION() {
		return DECISION;
	}

	public void setDECISION(String dECISION) {
		DECISION = dECISION;
	}

}
