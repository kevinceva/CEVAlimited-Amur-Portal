package com.ceva.base.common.bean;

import net.sf.json.JSONObject;

public class BillerBean {

	private String name = null;
	private String billerCode = null;
	private String abbreviation = null;
	private String agencyType = null;
	private String accountType = null;	
	private String accountNo = null;
	private String billerType = null;
	private String bankName = null;
	private String address = null;
	private String telephone = null;
	private String contactPerson = null;
	private String email = null;
	private String commissionType = null;
	private String amount = null;
	private String rate = null;
	private String makerId = null;
	private String makerDttm = null;
	private String authorizeId = null;
	private String authorizeDttm = null;
	private JSONObject responseJSON = null;
	private String accountName = null;
	private String customerName = null;
	private String modeOfPayment = null;
	private String narration = null;
	
	
	

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBillerCode() {
		return billerCode;
	}

	public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBillerType() {
		return billerType;
	}

	public void setBillerType(String billerType) {
		this.billerType = billerType;
	}
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public String getMakerDttm() {
		return makerDttm;
	}

	public void setMakerDttm(String makerDttm) {
		this.makerDttm = makerDttm;
	}

	public String getAuthorizeId() {
		return authorizeId;
	}

	public void setAuthorizeId(String authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getAuthorizeDttm() {
		return authorizeDttm;
	}

	public void setAuthorizeDttm(String authorizeDttm) {
		this.authorizeDttm = authorizeDttm;
	}

	public void clear() {
		this.name = null;
		this.billerCode = null;
		this.abbreviation = null;
		this.agencyType = null;
		this.accountType = null;
		this.accountNo = null;
		this.billerType = null;
		this.bankName = null;
		this.address = null;
		this.telephone = null;
		this.contactPerson = null;
		this.email = null;
		this.commissionType = null;
		this.amount = null;
		this.rate = null;
		this.makerId = null;
		this.makerDttm = null;
		this.authorizeId = null;
		this.authorizeDttm = null;
	}


}
