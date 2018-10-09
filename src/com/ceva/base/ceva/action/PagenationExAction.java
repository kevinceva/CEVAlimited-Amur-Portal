package com.ceva.base.ceva.action;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

public class PagenationExAction extends ActionSupport implements  RequestAware, ServletRequestAware,ServletResponseAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(PagenationExAction.class);
	
	HttpServletResponse httpServletResponse = null;
	HttpServletRequest httpServletRequest = null;
	
	
	
	public String commonMethod(){
		
		logger.debug("inside method:::");
		return "success";
	}
	
	@Override
	public void setServletResponse(HttpServletResponse httpServletResponse) {
		// TODO Auto-generated method stub
		
		this.httpServletResponse = httpServletResponse;
		
	}



	@Override
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		// TODO Auto-generated method stub
		this.httpServletRequest = httpServletRequest;
	}



	@Override
	public void setRequest(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
 		
		
}
