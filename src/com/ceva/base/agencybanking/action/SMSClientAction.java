package com.ceva.base.agencybanking.action;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.util.PostaSMS;
import com.opensymphony.xwork2.ActionSupport;

public class SMSClientAction extends ActionSupport implements
		ServletRequestAware {

	private HttpServletRequest request;

	private String statusData;

	private String smsval;

	private Logger logger = Logger.getLogger(SMSClientAction.class);

	public String smsSend() {

		logger.debug("------- Inside SMS Processing -------");

		net.sf.json.JSONObject jsonData = null;

		try {

			jsonData = constructToResponseJson(this.request);
			logger.debug("The JSONData is [" + jsonData + "]");

			statusData = PostaSMS.sendJson(jsonData.getString("smsval"),
					jsonData.getString("gateway.url"));

			logger.debug("StatusData is ::: "+statusData);
			
		} catch (Exception e) {
			logger.debug("|SMSClientAction| The Exception Occured Due To ["
					+ e.getMessage() + "]");
			statusData = e.getMessage();
		} finally {
			smsval = "";
		}

		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private net.sf.json.JSONObject constructToResponseJson(
			HttpServletRequest httpRequest) {
		Enumeration enumParams = httpRequest.getParameterNames();
		net.sf.json.JSONObject jsonObject = null;
		logger.debug(" Inside ConstructToResponseJson ...");
		try {
			jsonObject = new net.sf.json.JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" Inside constructToResponseJson exception ["
					+ e.getMessage() + "]");

		}
		logger.debug(" JsonObject [" + jsonObject + "]");

		return jsonObject;
	}

	public String getStatusData() {
		return statusData;
	}

	public void setStatusData(String statusData) {
		this.statusData = statusData;
	}

	public String getSmsval() {
		return smsval;
	}

	public void setSmsval(String smsval) {
		this.smsval = smsval;
	}

}
