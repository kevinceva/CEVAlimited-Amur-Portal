package com.ceva.interceptors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoggingInterceptorNew extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private Map<String, Object> parameters = null;
	private Map<String, Object> session = null;
	private HttpServletRequest request = null;

	private String makerId = null;
	private String className = null;
	private String nameSpace = null;
	private String actionName = null;
	private String method = null;

	private Logger log = Logger.getLogger(getClass());

	public LoggingInterceptorNew() {
		if (log.isDebugEnabled()) {
			log.debug("|LoggingInterceptorNew| Calling Logging Interceptor....");
		}
	}

	public String intercept(ActionInvocation invocation) throws Exception {

		log.debug("============================= " + getClass()
				+ " Interceptor Starts.");
		long startTime = System.currentTimeMillis();
		long endTime = 0L;

		String result = null;
		try {

			request = (HttpServletRequest) invocation.getInvocationContext()
					.get(StrutsStatics.HTTP_REQUEST);

			className = invocation.getAction().getClass().getName();
			nameSpace = invocation.getProxy().getNamespace();
			method = invocation.getProxy().getMethod();
			actionName = invocation.getProxy().getActionName();

			log.debug("|LoggingInterceptorNew| Class Name [" + className
					+ "] NameSpace [" + nameSpace + "] actionName ["
					+ actionName + "] method [" + method + "]");

			// Get the request Parameters
			parameters = invocation.getInvocationContext().getParameters();
			log.debug("|LoggingInterceptorNew| Request Paramerters are : "
					+ parameters);

			// save it in session
			session = invocation.getInvocationContext().getSession();

			log.debug("|LoggingInterceptorNew| session Paramerters are : "
					+ session);

			 if (session.containsKey("sessionLoginTime")
					&& session.containsKey("sessionMaxAge")) {
				// To check the session time
				log.debug("|LoggingInterceptorNew| Inside Session ...."); 
				 
					session.put("endTime", 0);
			} else {
				log.debug("|LoggingInterceptorNew| Doesnot have session makerId|sessionLoginTime.. Please check the session values.");
			}

			result = invocation.invoke();

			endTime = System.currentTimeMillis();
			log.debug("|LoggingInterceptorNew| After calling action: " + className
					+ " Time taken: " + (endTime - startTime) + " ms");
			
			

		} catch (Exception e) {
			log.debug(" Exception while executing |LoggingInterceptorNew| "
					+ e.getMessage());
		} finally {
			result = null;

			System.gc();
			log.debug("============================= " + getClass()
					+ " Interceptor ends.");
		}

		return result;
	}

	public void destroy() {
		log.debug("|LoggingInterceptorNew| Destroying LoggingInterceptorNew...");

		try {
			makerId = (String) request.getSession(false)
					.getAttribute("makerId");
		} catch (Exception e) {
		}

		log.debug("|LoggingInterceptorNew| makerId [" + makerId + "]");
		try {
			if (makerId != null)
				request.getSession(false).invalidate();
		} catch (Exception e) {
			log.debug("|LoggingInterceptorNew| Exception in Destroy is   ["
					+ e.getMessage() + "]");
		}

		log.debug("|LoggingInterceptorNew| Destroying Valid Session...");

		parameters = null;
		session = null;

	}

	public void init() {
		log.debug("|LoggingInterceptorNew| Initializing LoggingInterceptorNew...");
		if (parameters == null)
			parameters = new HashMap<String, Object>();

		if (session == null)
			session = new HashMap<String, Object>();
	}
}