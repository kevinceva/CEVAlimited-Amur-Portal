package com.ceva.interceptors;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.util.ServletContextAware;

import com.ceva.base.common.utils.AesUtil;
import com.ceva.util.CommonUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoggingInterceptor extends AbstractInterceptor{

	private static final long serialVersionUID = 1L;

	private Map<String, Object> parameters = null;
	private Map<String, Object> session = null;
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	private String makerId = null;
	private String className = null;
	private String nameSpace = null;
	private String actionName = null;
	private String method = null;

	private Logger log = Logger.getLogger(getClass());

	public LoggingInterceptor() {
		if (log.isDebugEnabled()) {
			log.debug("|LoggingInterceptor| Calling Logging Interceptor....");
		}
	}

	public String intercept(ActionInvocation invocation) throws Exception {

		log.debug("============================= " + getClass()
				+ " Interceptor Starts.");
		long startTime = System.currentTimeMillis();
		long endTime = 0L;

		String result = null;
		String requestkeys="";
		String totval="";
		String requestval="";
		String randomVal= null;
		HttpSession session1 = null;
		try {

			request = (HttpServletRequest) invocation.getInvocationContext()
					.get(StrutsStatics.HTTP_REQUEST);
			session1 = ServletActionContext.getRequest().getSession(false);
			/* server side validation start */
			requestkeys=request.getParameter("keys");
			if(requestkeys!=null){
				requestkeys=request.getParameter("keys");
				requestval=request.getParameter("keyvalue");
				for(int i=0;i<(requestkeys.split(",")).length;i++){
					if(requestkeys!=""){
					//System.out.println((requestkeys.split(","))[i]+"---"+request.getParameter((requestkeys.split(","))[i]));
						totval=totval+""+request.getParameter((requestkeys.split(","))[i])+",";	
					}
				}
				log.debug("client data ::: "+requestkeys+"::"+totval);
				log.debug("Server key ::: "+CommonUtil.b64_sha256(totval+session1.getAttribute("SALT")));
				log.debug("client key  "+request.getParameter("keyvalue"));	
				
			/*	if(!requestval.equalsIgnoreCase(CommonUtil.b64_sha256(totval+session1.getAttribute("SALT")))){
					session.clear();
					return "hackspage";
				}*/
				
				
				session1.removeAttribute("SALT");
				randomVal=CommonUtil.getRandomInteger();
				session1.setAttribute("SALT", AesUtil.md5(randomVal));
			}
			
			
			response = (HttpServletResponse) invocation.getInvocationContext()
					.get(StrutsStatics.HTTP_RESPONSE);
			
			/* server side validation end */
			className = invocation.getAction().getClass().getName();
			nameSpace = invocation.getProxy().getNamespace();
			method = invocation.getProxy().getMethod();
			actionName = invocation.getProxy().getActionName();

			log.debug("|LoggingInterceptor| Class Name [" + className
					+ "] NameSpace [" + nameSpace + "] actionName ["
					+ actionName + "] method [" + method + "]");

			// Get the request Parameters
			parameters = invocation.getInvocationContext().getParameters();
			log.debug("|LoggingInterceptor| Request Paramerters are : "
					+ parameters);

			// save it in session
			session = invocation.getInvocationContext().getSession();
			/* Session id Validation start */
			ServletContext ctx=ServletActionContext.getServletContext(); 
			if(session.get("makerId") != null){
				
					String obj=(String)ctx.getAttribute((session.get("makerId")).toString()); 
					if(obj!=null){
					//	System.out.println("kailash-----"+obj+"-------------"+request.getSession().getId());	
						
						if(!obj.equalsIgnoreCase(request.getSession().getId())){
							session.clear();
							return "loginanother";
						}
					}
			}
	       
			/* Session id Validation end */
			
			log.debug("|LoggingInterceptor| session Paramerters are : "
					+ session);

			if (session.get("makerId") == null
					&& nameSpace.indexOf("AgencyBanking") != -1
					&& actionName.equalsIgnoreCase("Home")) {
				log.debug("|LoggingInterceptor| Maker id is null : "
						+ session.get("makerId") + " redirecting to loginpage.");
				return "error";
			} else if (session.containsKey("sessionLoginTime")
					&& session.containsKey("sessionMaxAge")) {
				// To check the session time
				log.debug("|LoggingInterceptor| Inside Session Check....");
				try {
					long loginTime = Long.parseLong(session.get(
							"sessionLoginTime").toString());
					long maxLoginTime = Long.parseLong(session.get(
							"sessionMaxAge").toString());

					long currentTime = new Date().getTime();

					/* long diffSeconds = diff / 1000 % 60; */
					/* long diffMinutes = diff / (60 * 1000) % 60; */
					/* long diffHours = diff / (60 * 60 * 1000) % 24; */
					/* long diffDays = diff / (24 * 60 * 60 * 1000); */

					//long seconds = (currentTime - loginTime);
					long seconds = (currentTime - Long.parseLong(session.get(
							"endTime").toString()));

					log.debug("|LoggingInterceptor| Active Milli Seconds ::: "
							+ seconds + " MaxLogin Milli Seconds ::: "
							+ maxLoginTime);

					if (seconds > maxLoginTime)
						return "error";
					
					 
						
				} catch (Exception e) {
					log.debug(" Exception while checking session interval is  |LoggingInterceptor| "
							+ e.getMessage());
				}

			} else {
				log.debug("|LoggingInterceptor| Doesnot have session makerId|sessionLoginTime.. Please check the session values.");
			}

			result = invocation.invoke();

			endTime = System.currentTimeMillis();
			log.debug("|LoggingInterceptor| After calling action: " + className
					+ " Time taken: " + (endTime - startTime) + " ms");
			
			session.put("endTime", endTime);

		} catch (Exception e) {
			log.debug(" Exception while executing |LoggingInterceptor| "
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
		log.debug("|LoggingInterceptor| Destroying LoggingInterceptor...");

		try {
			makerId = (String) request.getSession(false)
					.getAttribute("makerId");
		} catch (Exception e) {
		}

		log.debug("|LoggingInterceptor| makerId [" + makerId + "]");
		try {
			if (makerId != null)
				request.getSession(false).invalidate();
		} catch (Exception e) {
			log.debug("|LoggingInterceptor| Exception in Destroy is   ["
					+ e.getMessage() + "]");
		}

		log.debug("|LoggingInterceptor| Destroying Valid Session...");

		parameters = null;
		session = null;

	}

	public void init() {
		log.debug("|LoggingInterceptor| Initializing LoggingInterceptor...");
		if (parameters == null)
			parameters = new HashMap<String, Object>();

		if (session == null)
			session = new HashMap<String, Object>();
	}
}
