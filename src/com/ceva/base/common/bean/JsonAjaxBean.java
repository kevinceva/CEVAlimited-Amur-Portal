package com.ceva.base.common.bean;

import java.util.Map;

import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;

public class JsonAjaxBean {

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String hoffice = null;
	private String region = null;
	private String location = null;
	private String method = null;
	private String selectedSelBox = null;
	private String fillSelectBox = null;

	private String entity = null;
	private String groupId = null;
	private String userId = null;
	private String employeeNo = null;
	private String dlNo = null;

	private Map<String, String> details;
	private int finalCount = 0;

	private String dlName;
	private String dlIdnumber;
	private String status;
	private String serialNo;

	// String agencyName;
	private String accounttype;
	private String service;

	private String mobile;
	private String amount;

	private String bin;
	private String bankIndex;
	private String bankCode;
	private String message;

	private String hudumaService;

	public String getHoffice() {
		return hoffice;
	}

	public void setHoffice(String hoffice) {
		this.hoffice = hoffice;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getSelectedSelBox() {
		return selectedSelBox;
	}

	public void setSelectedSelBox(String selectedSelBox) {
		this.selectedSelBox = selectedSelBox;
	}

	public String getFillSelectBox() {
		return fillSelectBox;
	}

	public void setFillSelectBox(String fillSelectBox) {
		this.fillSelectBox = fillSelectBox;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
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

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public int getFinalCount() {
		return finalCount;
	}

	public void setFinalCount(int finalCount) {
		this.finalCount = finalCount;
	}

	public String getDlName() {
		return dlName;
	}

	public void setDlName(String dlName) {
		this.dlName = dlName;
	}

	public String getDlIdnumber() {
		return dlIdnumber;
	}

	public void setDlIdnumber(String dlIdnumber) {
		this.dlIdnumber = dlIdnumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getBankIndex() {
		return bankIndex;
	}

	public void setBankIndex(String bankIndex) {
		this.bankIndex = bankIndex;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHudumaService() {
		return hudumaService;
	}

	public void setHudumaService(String hudumaService) {
		this.hudumaService = hudumaService;
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

}
