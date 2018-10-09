package com.ceva.interceptors;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ClearCacheInterceptor extends AbstractInterceptor{

	private static final long serialVersionUID = 1L;
	
	public String intercept(ActionInvocation invocation)throws Exception{ 
 
   		ActionContext context=(ActionContext)invocation.getInvocationContext();
 		
 		//HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
 		
 		HttpServletResponse response = (HttpServletResponse)context.get(StrutsStatics.HTTP_RESPONSE);

		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.addHeader("Cache-Control","pre-check=0,post-check=0"); 
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies. 
		 
 		String result = invocation.invoke();		
 		
		
        return result;

	}
}