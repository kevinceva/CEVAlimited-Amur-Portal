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

import com.ceva.base.common.bean.PayBillBean;
import com.ceva.base.common.dao.PayBillDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class PaybillActions extends ActionSupport implements
		ServletRequestAware, ModelDriven<PayBillBean> {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(PaybillActions.class);

	private String result;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;

	private HttpServletRequest httpRequest;
	private HttpSession session = null;

	@Autowired
	private PayBillBean payBillBean = null;

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {
		Enumeration enumParams = httpRequest.getParameterNames();
		JSONObject jsonObject = null;
		logger.debug("Inside constructToResponseJson ...");
		try {
			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" inside exception [" + e.getMessage() + "]");

		}
		logger.debug(" jsonObject[" + jsonObject + "]");

		return jsonObject;
	}

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest,
			JSONObject jsonObject) {
		Enumeration enumParams = httpRequest.getParameterNames();

		logger.debug("Inside constructToResponseJson with JSON...");
		try {
			if (jsonObject == null)
				jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" inside exception [" + e.getMessage() + "]");

		}
		logger.debug(" jsonObject[" + jsonObject + "]");

		return jsonObject;
	}

	public String commonScreen() {
		logger.debug("Inside CommonScreen .. ");
		try {
			logger.debug("Biller Id :::: " + payBillBean.getBillerId());
			logger.debug("Biller Type Name ::::   "
					+ payBillBean.getBillerTypeName());
		} catch (Exception e) {
			logger.debug("Exception message is ::::: " + e.getMessage());
		}
		logger.debug("Inside CommonScreen  Ends.. ");
		return SUCCESS;
	}

	public String listPaybillAccounts() {
		logger.debug("Inside List Paybill Accounts .. ");
		ArrayList<String> errors = null;
		PayBillDAO cevaPowerDAO = null;
		try {
			requestDTO = new RequestDTO();

			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new PayBillDAO();
			responseDTO = cevaPowerDAO.getMPesaDashBoard(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DASHBOARD);
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
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in getAdminCraeteInfo [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String billerDetails() {
		logger.debug("Inside Biller Details .. ");
		ArrayList<String> errors = null;
		PayBillDAO cevaPowerDAO = null;
		try {
			requestDTO = new RequestDTO();
			logger.debug("Request DTO [" + requestDTO + "]");
			cevaPowerDAO = new PayBillDAO();
			responseDTO = cevaPowerDAO.getBillerTypeDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DATA);
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
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in getAdminCraeteInfo [" + e.getMessage()
					+ "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cevaPowerDAO = null;
			errors = null;
		}

		return result;
	}

	public String billerTypeAck() {
		logger.debug("Inside BillerType Ack.");
		ArrayList<String> errors = null;
		PayBillDAO billerDao = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new PayBillDAO();

			requestJSON.put("payBillBean", payBillBean);
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.insertBillerType(requestDTO);

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

	public String billerIdAck() {
		logger.debug("Inside BillerId Ack.");
		ArrayList<String> errors = null;
		PayBillDAO billerDao = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new PayBillDAO();

			requestJSON.put("payBillBean", payBillBean);
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.insertBillerId(requestDTO);

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

	@Override
	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public PayBillBean getPayBillBean() {
		return payBillBean;
	}

	public void setPayBillBean(PayBillBean payBillBean) {
		this.payBillBean = payBillBean;
	}

	@Override
	public PayBillBean getModel() {
		return payBillBean;
	}

}
