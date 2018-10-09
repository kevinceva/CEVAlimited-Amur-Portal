package com.ceva.interceptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;


public class SessionCheck extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	Logger logger=Logger.getLogger(SessionCheck.class);
	private Map<String, Object> sessionMap = null;
	private HttpServletRequest request;
	private HttpSession session = null;
	ServletContext context;
	private String result = null;
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String userId = null;
		String appl = null;

		logger.debug("|SessionCheck| Inside." );
		// Get the session   

		sessionMap = invocation.getInvocationContext().getSession();
		request = ServletActionContext.getRequest();
		session = ServletActionContext.getRequest().getSession();
		
		try {
			logger.debug("session id.:"+session.getId());
			logger.debug("Maker id.:"+sessionMap.get("makerId"));
			boolean isRegisteredSession = checkSession(sessionMap.get("makerId").toString(), session.getId());
			logger.debug("isRegisteredSession.:"+isRegisteredSession);
			
			if(isRegisteredSession){
				result = invocation.invoke();
			}else{
				logger.debug("Suspected access Please login using your credentials.");
				result = "loginRedirect";
			}
			
		} catch (Exception e) {
			logger.debug("|SessionCheck| exception is : "+ e.getMessage());
			userId = null;
			appl = null;
		}

		if(userId == null //||userId.equalsIgnoreCase("null") 
				|| appl == null)//|| appl.equalsIgnoreCase("null")) 
		{
			logger.debug("|SessionCheck| Inside null check." );
			//request.getRequestDispatcher("logout.action").forward(request, response);
			return "loginRedirect";
		} 

		return result;
	}
	
	
	private boolean checkSession(String makerId, String sessionId) {
		Connection connection = null;
		PreparedStatement pstmt=null;
		ResultSet resultSet = null;
		String registerdSession = null;
		boolean registered = false;
		try{
			connection = connection ==null ? DBConnector.getConnection() : connection;
			pstmt = connection.prepareStatement("SELECT SESSION_ID FROM USER_LOGIN_CREDENTIALS WHERE LOGIN_USER_ID=?");
			pstmt.setString(1, makerId);
			resultSet = pstmt.executeQuery();
			if(resultSet.next()){
				registerdSession = resultSet.getString(1);
			}
			logger.debug("registerdSession id..:"+registerdSession);
			logger.debug("sessionId..:"+sessionId);
			if(sessionId.equals(registerdSession)){
				registered=true;
			}

		}catch(Exception e){
			logger.debug("Error occured..:"+e.getLocalizedMessage());
		}finally{
			DBUtils.closeResultSet(resultSet);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
		}
		// TODO Auto-generated method stub
		return registered;
	}

}
