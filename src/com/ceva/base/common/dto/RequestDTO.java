package com.ceva.base.common.dto;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject; 
public class RequestDTO {
	private Logger logger = Logger.getLogger(RequestDTO.class);

	public JSONObject requestJSON=new JSONObject();
	public Object bean;

	public JSONObject getRequestJSON() {
		return requestJSON;
	}

	public void setRequestJSON(JSONObject requestJSON) {
		
		logger.info("JSON - "+requestJSON);
		if(requestJSON !=null)
		{
			try {
				logger.info("JSON - "+requestJSON.toString());
				requestJSON =JsonValueValidator.call(requestJSON);
			} catch (Exception e) {
				logger.error("JSON - "+requestJSON.toString());
				e.printStackTrace();
			}
		}
		this.requestJSON = requestJSON;
	}
	
	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}
	
}
