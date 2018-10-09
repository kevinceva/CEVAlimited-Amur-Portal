package com.ceva.base.common.bean;

import net.sf.json.JSONObject;

public class PayBillBean {

	private String billerId = null;

	private String billerTypeName = null;

	private String description = null;

	private String bfubaccount = null;

	private String createdBy;

	private String createdDate;

	private JSONObject responseJSON = null;

	public PayBillBean() {
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getBillerTypeName() {
		return billerTypeName;
	}

	public void setBillerTypeName(String billerTypeName) {
		this.billerTypeName = billerTypeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBfubaccount() {
		return bfubaccount;
	}

	public void setBfubaccount(String bfubaccount) {
		this.bfubaccount = bfubaccount;
	}

}
