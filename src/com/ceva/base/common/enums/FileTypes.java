package com.ceva.base.common.enums;

public enum FileTypes {
	CUS("CUS"),
	BTP("BTP"),
	REL("REL"),
	CUD("CUD"),
	BNT("BNT"),
	BNM("BNM"),
	BUR("BUR"),
	CAT("CAT"),
	SAF("SAF"),
	CUF("CUF"),
	SHF("SHF"),
	SUR("SUR");
	private String fileType;
	
	private FileTypes(String value){
		this.fileType = value;
	}
}
