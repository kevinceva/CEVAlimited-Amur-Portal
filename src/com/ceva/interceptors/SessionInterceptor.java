package com.ceva.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor {

	private Logger logger = Logger.getLogger(GetChildMenus.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		String methodName=null;
		Map<String, Object> session = ActionContext.getContext().getSession();  
		logger.debug("[SessionInterceptor]session:::"+session.isEmpty());
		HttpServletResponse response = (HttpServletResponse)invocation.  
				getInvocationContext().get(StrutsStatics.HTTP_RESPONSE); 

		HttpServletRequest request = (HttpServletRequest)invocation.  
				getInvocationContext().get(StrutsStatics.HTTP_REQUEST); 

		if(session.isEmpty()){
			return "sessionExpired";
		}else{
			methodName=invocation.getProxy().getMethod();
			logger.debug("[SessionInterceptor]methodName:::"+methodName);
			session.put("METHOD_NAME", methodName);
			return invocation.invoke();
		}
	}
}
