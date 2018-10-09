package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.BillerBean;
import com.ceva.base.common.dao.BillerManagementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BillerManagementAction extends ActionSupport implements
		ServletRequestAware, ModelDriven<BillerBean> {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(BillerManagementAction.class);

	private String result;
	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;
	protected HttpServletRequest request;

	@Autowired
	private BillerBean billerBean = null;
	private HttpSession session = null;

	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		responseJSON = constructToResponseJson(request);
		result = "success";
		return result;
	}

	public String billerInfo() {
		logger.debug("Inside  GetBillerRelated Info.");
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BILLER_REL_INFO);
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
			logger.debug("Exception in Get BillerRelated Info ["
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

	public String billerInfoAck() {
		logger.debug("Inside  GetBillerRelated Info Ack.");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new BillerManagementDAO();

			requestJSON.put("billerBean", billerBean);
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.insertBillerData(requestDTO);

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
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Get BillerRelated Info ["
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
	
	public String editBillerAck() {
		logger.debug("Inside  EditBillerAck Info Ack.");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new BillerManagementDAO();

			requestJSON.put("billerBean", billerBean);
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.updateBillerData(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BILLER_REL_INFO);
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
			logger.debug("Exception in EditBillerAck Info ["
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

	public String viewRegisteredBillers() {
		logger.debug("Inside ViewRegisteredBillers... ");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;

		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.viewRegisteredBillers(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				billerBean = ((BillerBean) responseDTO.getData().get(
						"bank_data"));
				logger.debug("Response JSON [" + billerBean.getResponseJSON()
						+ "]");
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
			logger.debug("Exception in ViewRegisteredBillers ["
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

	public String viewBillerInDetails() {
		logger.debug("Inside viewBillerInDetails... ");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;

		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("billerid", billerBean.getBillerCode());
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.viewBillerInDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				billerBean = ((BillerBean) responseDTO.getData().get(
						"bank_data"));
				logger.debug("Response JSON [" + billerBean.getResponseJSON()
						+ "]");
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
			logger.debug("Exception in viewBillerInDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}

	public String uploadBillerFile() {

		return result;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public BillerBean getBillerBean() {
		return billerBean;
	}

	public void setBillerBean(BillerBean billerBean) {
		this.billerBean = billerBean;
	}

	@Override
	public BillerBean getModel() {
		return billerBean;
	}

}
