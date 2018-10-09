
package com.ceva.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XssFilter implements Filter {
	public void destroy() {
	}

	public void init(FilterConfig arg0) throws ServletException {	
	}	

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws  ServletException, IOException {
		
		DeceptionXSS xssreq = new DeceptionXSS((HttpServletRequest)req ,(HttpServletResponse) res);
		DeceptionCookies httponlyres = new DeceptionCookies((HttpServletRequest)req ,(HttpServletResponse) res);
		
		chain.doFilter(xssreq, httponlyres);
	}



}
