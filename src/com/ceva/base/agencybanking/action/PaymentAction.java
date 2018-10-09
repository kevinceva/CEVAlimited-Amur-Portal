package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.base.common.dao.PaymentDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.EncryptTransactionPin;
import com.opensymphony.xwork2.ActionSupport;

public class PaymentAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;

	private ResourceBundle rb = ResourceBundle.getBundle("pathinfo_config");

	private Logger logger = Logger.getLogger(PaymentAction.class);

	private HttpSession session = null;

	private String result;
	private String method;
	private String successPage;
	private String errorPage;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private JSONObject responseJSON1 = null;

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

	private HttpServletRequest httpRequest;

	public String commonScreen() {
		responseJSON = constructToResponseJson(httpRequest);
		return SUCCESS;
	}

	public String serviceConfirmation() {

		logger.debug("Inside ServiceConfirmation... ");
		responseJSON = constructToResponseJson(httpRequest);

		logger.debug("  method [" + method + "]");

		try {
			result = SUCCESS;
			successPage = rb.getString("cnf_success");
		} catch (Exception e) {

			result = ERROR;
			errorPage = rb.getString("cnf_error");
		}

		logger.debug(" responseJSON [" + responseJSON + "]");
		logger.debug(" result [" + result + "]");
		logger.debug(" successPage [" + successPage + "]");
		logger.debug(" errorPage   [" + errorPage + "]");

		return result;
	}

	public String serviceTransactionPin() {
		logger.debug("Inside ServiceTransactionPin.. ");
		responseJSON = constructToResponseJson(httpRequest);
		logger.debug("  method [" + method + "]");

		return SUCCESS;
	}

	public String loadSerialConfirm() {
		logger.debug("Inside LoadSerialConfirm.. ");
		responseJSON = constructToResponseJson(httpRequest);
		logger.debug("  method [" + method + "]");
		return SUCCESS;
	}

	public String serviceAcknowledge() {

		logger.debug("Inside ServiceAcknowledge.. ");

		String data[] = null;
		PaymentDAO paymentDao = null;

		try {
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestJSON = new JSONObject();

			session = ServletActionContext.getRequest().getSession();

			data = constructStanderedString(httpRequest);

			logger.debug("Final Constructed String is Billing  [" + data[0]
					+ "]");
			logger.debug("Final Constructed String is Service  [" + data[1]
					+ "]");
			logger.debug("Final Constructed String is TotalVal [" + data[2]
					+ "]");

			requestJSON.put("billing_info", data[0]);
			requestJSON.put("service_info", data[1]);
			requestJSON.put("total_info", data[2]);
			requestJSON
					.put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));
			requestJSON.put("method", method);

			logger.debug("Request JSON [" + requestJSON + "]");

			requestDTO.setRequestJSON(requestJSON);

			paymentDao = new PaymentDAO();
			responseDTO = paymentDao.acknowledgeService(requestDTO);
			responseJSON = constructToResponseJson(httpRequest);
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON1 = (JSONObject) responseDTO.getData().get(
						"service_details");
				session.setAttribute("responseJSON1_txn",
						responseJSON1.getString("txn_ref_no"));
				session.setAttribute("print_details",
						responseJSON1.getString("print_details"));
				result = SUCCESS;
			} else {
				logger.debug("Getting error from DAO.");
				ArrayList<String> errors = (ArrayList<String>) responseDTO
						.getErrors();
				logger.debug("Errors[" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			session.setAttribute("JSON_SESS", responseJSON);

			logger.debug("Response JSON [" + responseJSON + "]");

		} catch (Exception e) {
			logger.debug("Exception [" + e.getMessage() + "]");

			result = "fail";
		} finally {
			data = null;
			paymentDao = null;
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}

		return result;
	}

	public String loadSerialData() {
		logger.debug("Inside LoadSerial Data... ");

		PaymentDAO paymentDAO = null;
		try {
			requestDTO = new RequestDTO(); 
			session = ServletActionContext.getRequest().getSession();
			requestJSON = constructToResponseJson(httpRequest);
			requestJSON
					.put("user_id",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));
			requestDTO.setRequestJSON(requestJSON);
			paymentDAO = new PaymentDAO();
			responseDTO = paymentDAO.loadSerial(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				ArrayList<String> messages = (ArrayList<String>) responseDTO
						.getMessages();
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				result = "success";
			} else {
				ArrayList<String> errors = (ArrayList<String>) responseDTO
						.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = constructToResponseJson(httpRequest);
			logger.debug("Response JSON [" + responseJSON + "]");

		} catch (Exception e) {
			logger.debug("Exception in loadSerialData [" + e.getMessage() + "]");

			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			paymentDAO = null;
		}
		return result;
	}

	public String loadSerial() {
		logger.debug("Inside LoadSerial... ");

		PaymentDAO paymentDAO = null;
		try {

			paymentDAO = new PaymentDAO();
			responseDTO = paymentDAO.loadSerialUsers(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"USERS_DATA");
				result = "success";
			} else {
				ArrayList<String> errors = (ArrayList<String>) responseDTO
						.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			logger.debug("Response JSON [" + responseJSON + "]");

		} catch (Exception e) {
			logger.debug("Exception in loadSerialData [" + e.getMessage() + "]");

			result = "fail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			paymentDAO = null;
		}
		return result;
	}

	public String getSuccessPage() {
		return successPage;
	}

	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	private String[] constructStanderedString(HttpServletRequest httpRequest) {
		String data[] = null;

		String key = null;
		String value = null;

		Enumeration enumParams = null;
		StringBuffer billingInfo = null;
		StringBuffer serviceInfo = null;
		StringBuffer totalInfo = null;
		String totVal = "";
		String key1 = "";
		String cryptedPassword = "";

		logger.debug("Inside ConstructStanderedString... ");

		try {

			enumParams = httpRequest.getParameterNames();
			billingInfo = new StringBuffer(100);
			serviceInfo = new StringBuffer(100);
			totalInfo = new StringBuffer(100);

			billingInfo.append("");
			serviceInfo.append("");
			totalInfo.append("");

			while (enumParams.hasMoreElements()) {

				key = (String) enumParams.nextElement();
				value = httpRequest.getParameter(key);

				if (key.equalsIgnoreCase("CV1116")) {
					key1 = "97206B46CE46376894703ECE161F31F2";
					logger.debug(" Before Enryption Value is  [" + value + "]");

					try {
						cryptedPassword = EncryptTransactionPin.encrypt(key1,
								value, 'F');

					} catch (Exception e) {
						logger.debug("Exception in encrypting password"
								+ " cryptedPassword [" + cryptedPassword
								+ "] message[" + e.getMessage() + "]");
						value = "";
					}

					value = cryptedPassword;
				}

				value = (value == null || value.equals("")) ? "NO_VALUE"
						: value;

				if (key.indexOf("CV") != -1) {
					totVal = key
							+ StringUtils.leftPad(
									String.valueOf(value.length()), 3, "0")
							+ value;
					totalInfo.append(totVal);
					if (rb.getString("auth_params").indexOf(key) != -1) {
						billingInfo.append(totVal);
					} else {
						serviceInfo.append(totVal);
					}
				}
			}

			data = new String[3];
			data[0] = billingInfo.toString();
			data[1] = serviceInfo.toString();
			data[2] = totalInfo.toString();
		} catch (Exception e) {
			logger.debug("Exception in ConstructStanderedString ["
					+ e.getMessage() + "]");
		} finally {

			key = null;
			value = null;

			enumParams = null;
			billingInfo = null;
			serviceInfo = null;
			totalInfo = null;
			totVal = null;
		}

		return data;
	}

	private String constructStanderedString(HttpServletRequest httpRequest,
			String vll) {
		String key = null;
		String value = null;

		Enumeration enumParams = null;

		StringBuffer totalInfo = null;
		String totVal = "";
		logger.debug("Inside Construct Standered String..... ");

		try {

			enumParams = httpRequest.getParameterNames();

			totalInfo = new StringBuffer(100);
			totalInfo.append("");
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
			logger.debug("ConstructStanderedString Exception is "
					+ e.getMessage());
		} finally {
			key = null;
			value = null;
			enumParams = null;
			totVal = "";
			totVal = totalInfo.toString();
			totalInfo.delete(0, totalInfo.length());
		}

		return totVal;
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

	private JSONObject constructToResponseCurrentJson(
			HttpServletRequest httpRequest, JSONObject jsonObject) {

		Enumeration<?> enumParams = null;
		logger.debug("Start ConstructToResponseJson... ");
		String key = "";
		String val = "";

		try {
			enumParams = httpRequest.getParameterNames();
			if (jsonObject == null) {
				jsonObject = new JSONObject();
			} else {
				while (enumParams.hasMoreElements()) {
					key = (String) enumParams.nextElement();
					val = httpRequest.getParameter(key);
					jsonObject.put(key, val);
				}
			}

		} catch (Exception e) {
			logger.debug("Exception while converting to httpreq to bean["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
			key = null;
			val = null;
		}
		logger.debug("End ConstructToResponseJson... ");
		return jsonObject;
	}
	 

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public void setServletRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;

	}

}
