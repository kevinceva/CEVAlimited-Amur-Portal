package com.ceva.base.common.action;

import java.util.Properties;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.LoadPropFile;
import com.ceva.util.LoadUtils;
import com.opensymphony.xwork2.ActionSupport;

public class BillerMgmtAjaxAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(BillerMgmtAjaxAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String billerId;
	private String methodKey;
	private String id;
	private String dataVal;

	private String result = null;

	public String execute() {
		logger.debug("Inside [execute] MethodKey[" + methodKey + "]");

		if (methodKey.equals("searchFields")) {
			result = billerSearchFields();
		}
		return result;
	}

	public String billerSearchFields() {
		logger.debug("Inside  BillerSerachFields Biller Id[" + billerId + "]");
		String searchFields = null;
		String appFile = null;
		ResourceBundle pathConfigRB = null;
		Properties appProp = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestJSON.put(CevaCommonConstants.BILLER_ID, billerId);
			logger.debug("Request JSON [" + requestJSON + "]");

			pathConfigRB = LoadUtils
					.getMyResource(CevaCommonConstants.PATH_CONFIG_FILE);

			/*** to read data from property File *****/

			/**** Code for read data from Application Files ***/
			appFile = pathConfigRB.getString(CevaCommonConstants.BASE_PATH)
					+ pathConfigRB.getString(CevaCommonConstants.APPFILE_PATH)
					+ pathConfigRB
							.getString(CevaCommonConstants.APPLICATION_FILE);
			logger.debug("Appliaction Name from " + appFile);
			appProp = LoadPropFile.generateResouce(appFile);
			searchFields = appProp.getProperty(billerId);
			logger.debug("SearchFields [" + searchFields + "]");
			responseJSON.put(CevaCommonConstants.SEARCH_FIELDS, searchFields);

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in BillerSerachFields [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
			e.printStackTrace();
		} finally {
			requestJSON = null;
			searchFields = null;
			appFile = null;
			pathConfigRB = null;
			appProp = null;
		}
		return SUCCESS;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getMethodKey() {
		return methodKey;
	}

	public void setMethodKey(String methodKey) {
		this.methodKey = methodKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataVal() {
		return dataVal;
	}

	public void setDataVal(String dataVal) {
		this.dataVal = dataVal;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

}
