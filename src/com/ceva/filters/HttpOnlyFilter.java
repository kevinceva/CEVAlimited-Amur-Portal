package com.ceva.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class HttpOnlyFilter implements Filter {
	
	
	 private FilterConfig config;

	 public void init(FilterConfig config) throws ServletException {
	   this.config = config;
	 }
	 
	 public void destroy() {
	   this.config = null;
	 }

	 public void doFilter(ServletRequest req, ServletResponse res,
	     FilterChain chain) throws IOException, ServletException {
	   System.out.println("Got here");
	  
	   HttpOnlyResponseWrapper hres = new HttpOnlyResponseWrapper((HttpServletResponse)res);
	   chain.doFilter(req, hres);
	 }

	 public FilterConfig getFilterConfig() {
	   return this.config;
	 }

	 
	}
