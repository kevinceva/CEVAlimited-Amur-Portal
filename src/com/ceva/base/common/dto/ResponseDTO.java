package com.ceva.base.common.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResponseDTO {
	private List<String> errors;
	private List<String> messages;
	private HashMap<String,Object> data;
	private List<String> selectData;
	private String result;
	
	public ResponseDTO() {
		data = new HashMap<String,Object>();
		errors = new ArrayList<String>();
		messages = new ArrayList<String>();
	}
	
	public void addError(String error){
		errors.add(error);
	}
	
	public void addSelectData(String data){
		selectData.add(data);
	}
	public void addMessages(String msg){
		messages.add(msg);
	}
	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public List<String> getMessages() {
		return messages;
	}

	public List<String> getSelectData() {
		return selectData;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public void setSelectData(List<String> selectData) {
		this.selectData = selectData;
	}
}
