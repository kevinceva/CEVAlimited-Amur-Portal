package com.ceva.base.common.bean;

public class BulkDisbBean {

	/*
	 * private String profileid; private String fullname; private String msisdn;
	 * //private String txnamount; private String status; //private String
	 * nationalid;
	 */

	/*** New fields ***/

	private String accountNumber;
	private String lastName;
	private String otherName;
	private String subAgentId;
	private String txnamount;
	private String nationalid;
	private String idType;
	private String code;
	private String subAgentOne;
	private String mobileNo;

	/*
	 * public String getProfileid() { return profileid; }
	 * 
	 * public void setProfileid(String profileid) { this.profileid = profileid;
	 * }
	 * 
	 * public String getFullname() { return fullname; }
	 * 
	 * public void setFullname(String fullname) { this.fullname = fullname; }
	 * 
	 * public String getMsisdn() { return msisdn; }
	 * 
	 * public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
	 */

	public String getTxnamount() {
		return txnamount;
	}

	public void setTxnamount(String txnamount) {
		this.txnamount = txnamount;
	}

	/*
	 * public String getStatus() { return status; }
	 * 
	 * public void setStatus(String status) { this.status = status; }
	 */

	public String getNationalid() {
		return nationalid;
	}

	public void setNationalid(String nationalid) {
		this.nationalid = nationalid;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getSubAgentId() {
		return subAgentId;
	}

	public void setSubAgentId(String subAgentId) {
		this.subAgentId = subAgentId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSubAgentOne() {
		return subAgentOne;
	}

	public void setSubAgentOne(String subAgentOne) {
		this.subAgentOne = subAgentOne;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Override
	public String toString() {
		/*return String.format("%s,%s,%s,%s,%s,%s", profileid, fullname, msisdn,
				txnamount, status, nationalid);*/
		
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", (accountNumber.length() == 0 ? " " : accountNumber),lastName,otherName,subAgentId,txnamount,nationalid,idType,code,subAgentOne,mobileNo);
		
		
	}
}
