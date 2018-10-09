package com.ceva.base.common.action;

import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.BillerManagementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.LoadPropFile;
import com.ceva.util.LoadUtils;
import com.opensymphony.xwork2.ActionSupport;

public class BillerMgmtAjaxAction1 extends ActionSupport {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(BillerMgmtAjaxAction1.class);

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

		if (methodKey.equals("customerData")) {
			result = getBillerCustomerData();
		}
		return result;
	}

	public String getBillerCustomerData() {
		logger.debug("Inside GetBillerCustomerData BillerId [" + billerId + "]");
		ResourceBundle pathConfigRB = null;
		String appFile = "";
		Properties appProp = null;
		String tableType = "";
		String tableName = "";
		String tableColumns = "";
		String tableHeaders = "";
		BillerManagementDAO billerDAo = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();
			requestJSON.put(CevaCommonConstants.BILLER_ID, billerId);
			requestJSON.put(CevaCommonConstants.ID, id);
			requestJSON.put(CevaCommonConstants.INPUT_DATA, dataVal);

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
			tableType = billerId + "_TABLE";
			tableName = appProp.getProperty(tableType);

			tableColumns = appProp.getProperty(billerId + "_COLUMNS");

			tableHeaders = appProp.getProperty(billerId + "_TAB_HEADER");

			requestJSON.put(CevaCommonConstants.TABLE_NAME, tableName);
			requestJSON.put(CevaCommonConstants.TABLE_COLUMNS, tableColumns);
			requestJSON.put(CevaCommonConstants.TABLE_HEADER, tableHeaders);
			logger.debug("Request JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			billerDAo = new BillerManagementDAO();
			responseDTO = billerDAo.getBillerCustomerData(requestDTO);
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"CUSTOMER_DATA");
				responseJSON
						.put(CevaCommonConstants.TABLE_HEADER, tableHeaders);
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetBillerCustomerData [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
 		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			pathConfigRB = null;
			appFile = null;
			appProp = null;
			tableType = null;
			tableName = null;
			tableColumns = null;
			tableHeaders = null;
			billerDAo = null;
			errors = null;
		}

		return SUCCESS;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
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
}
