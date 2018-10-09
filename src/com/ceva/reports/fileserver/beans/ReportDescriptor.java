package com.ceva.reports.fileserver.beans;

import java.io.Serializable;
import java.util.Date;

public class ReportDescriptor implements Serializable {
	private String fileame;
	private String uriString;
	private Date creationDate;
	private Date actualCreationDate;

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getFileame() {
		return this.fileame;
	}

	public void setFileame(String fileame) {
		this.fileame = fileame;
	}

	public String getUriString() {
		return this.uriString;
	}

	public void setUriString(String uriString) {
		this.uriString = uriString;
	}

	public Date getActualCreationDate() {
		return this.actualCreationDate;
	}

	public void setActualCreationDate(Date actualCreationDate) {
		this.actualCreationDate = actualCreationDate;
	}
}