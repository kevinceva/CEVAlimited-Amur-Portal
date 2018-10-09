package com.ceva.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class SessionInvalidate extends ActionSupport implements 
SessionAware,ServletRequestAware {
 
	/**
	 * 
	 */ 
	private static final long serialVersionUID = 1L;

	private HttpServletRequest httpServletRequest ;
	
	private SessionMap<String,Object> session;

	Logger logger=Logger.getLogger(SessionInvalidate.class);

	@Override
	public String execute() throws Exception {

		//SessionMap<String,Object> session = (SessionMap<String,Object>) ActionContext.getContext().getSession();
		
		HttpSession httpSession = httpServletRequest.getSession(false);
		
 		logger.debug("Old Session id is ["+ httpSession.getId()+"]");

		logger.debug("Invalidating Session.");

		if(session != null) session.invalidate(); 
		
		httpSession = httpServletRequest.getSession(true);

		logger.debug("Assigning New Session.");

		logger.debug("New httpSession id is  ["+ httpSession.getId()+"]");
 
		return super.execute();
	}

	@Override
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = (SessionMap<String, Object>) session;
	}



}
