package com.ceva.base.common.action;

import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.AjaxActionBean;
import com.ceva.base.common.dao.AjaxDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class AjaxAction extends ActionSupport implements
		ModelDriven<AjaxActionBean> {

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(AjaxAction.class);

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String result;

	@Autowired
	private AjaxActionBean ajaxActionBean = null;

	@Override
	public String execute() throws Exception {
		logger.debug("Inside Execute method [" + ajaxActionBean.getMethod()
				+ "]");
		if (ajaxActionBean.getMethod().equalsIgnoreCase("checkTransactionType")) {
			result = checkTransactionType();
		}
		logger.debug("Before returning result[" + result + "]");
		return result;
	}

	 
	public String checkMerchantId() {

		logger.debug("Inside CheckMerchantId MerchantId["
				+ ajaxActionBean.getMerchantId() + "]");
		ArrayList<String> errors = null;
		AjaxDAO ictAdminDAO = null;
		try {
			requestJSON = new JSONObject(); 
			requestDTO = new RequestDTO();
			
 			requestJSON.put(CevaCommonConstants.MERCHANT_ID,
					ajaxActionBean.getMerchantId());

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO[" + requestDTO + "]");
			ictAdminDAO = new AjaxDAO();
			responseDTO = ictAdminDAO.checkMerchantId(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_CHECK_INFO);

				ajaxActionBean.setResponseJSON(responseJSON);

				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in checkMerchantId [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			ictAdminDAO = null;
		}

		return SUCCESS;
	}

	public String generateMerchantId() {

		logger.debug("Inside GenerateMerchantId  Merchantname ["
				+ ajaxActionBean.getMerchantName() + "]");
		ArrayList<String> errors = null;
		AjaxDAO ictAdminDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestJSON.put(CevaCommonConstants.MERCHANT_NAME,
					ajaxActionBean.getMerchantName());
			logger.debug("Response JSON [" + responseJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			ictAdminDAO = new AjaxDAO();
			responseDTO = ictAdminDAO.generateMerchantId(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_INFO);

				ajaxActionBean.setResponseJSON(responseJSON);

				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in checkSwitchStaus [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			ictAdminDAO = null;
		}

		return SUCCESS;
	}

	public String getStores() {
		logger.debug("Inside GetStores MerchantId["
				+ ajaxActionBean.getMerchantId() + "]");
		ArrayList<String> errors = null;
		AjaxDAO merchantDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			requestJSON.put(CevaCommonConstants.MERCHANT_ID,
					ajaxActionBean.getMerchantId());
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new AjaxDAO();
			responseDTO = merchantDAO.getStores(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.STORE_LIST);
				ajaxActionBean.setResponseJSON(responseJSON);

				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in checkSwitchStaus [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			merchantDAO = null;
		}

		return SUCCESS;
	}

	public String getTerminals() {
		logger.debug("Inside GetTerminals... ");
		AjaxDAO merchantDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.MERCHANT_ID,
					ajaxActionBean.getMerchantId());
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			merchantDAO = new AjaxDAO();
			responseDTO = merchantDAO.getTerminals(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.TERMINAL_LIST);

				ajaxActionBean.setResponseJSON(responseJSON);
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getTerminals [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			merchantDAO = null;
		}

		return SUCCESS;
	}

	public String checkTransactionType() {
		logger.debug("Inside checkTransactionType.... ");
		AjaxDAO ictAdminDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.ACCOUNT_TYPE,
					ajaxActionBean.getAccounttype());
			requestJSON.put(CevaCommonConstants.SERVICE_TYPE,
					ajaxActionBean.getService());
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			ictAdminDAO = new AjaxDAO();
			responseDTO = ictAdminDAO.checkTransactionType(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.USER_CHECK_INFO);

				ajaxActionBean.setResponseJSON(responseJSON);
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in checkTransactionType [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			ictAdminDAO = null;
		}

		return SUCCESS;
	}

	public AjaxActionBean getAjaxActionBean() {
		return ajaxActionBean;
	}

	public void setAjaxActionBean(AjaxActionBean ajaxActionBean) {
		this.ajaxActionBean = ajaxActionBean;
	}

	@Override
	public AjaxActionBean getModel() {
		return ajaxActionBean;
	}

}
