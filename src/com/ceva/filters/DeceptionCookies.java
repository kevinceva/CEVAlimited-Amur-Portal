package com.ceva.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class DeceptionCookies extends HttpServletResponseWrapper 
{
	
	public DeceptionCookies(HttpServletRequest servletRequest,HttpServletResponse servletResponse)
	{
		super(servletResponse);
		if (servletResponse.containsHeader( "SET-COOKIE" )) 
		{
			String sessionid = servletRequest.getSession().getId();
			servletResponse.setHeader( "SET-COOKIE", "JSESSIONID=" + sessionid 
					+ ";Path="+(String)servletRequest.getContextPath()+"; HttpOnly" );
			System.out.println("||| Set cookie will have only session id, and it is type Httponly |||");
		}
	}

}
