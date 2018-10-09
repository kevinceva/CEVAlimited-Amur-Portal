package com.ceva.filters;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpOnlyResponseWrapper extends HttpServletResponseWrapper {
	 private static SimpleDateFormat cookieFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz");

	 public HttpOnlyResponseWrapper(HttpServletResponse res) {
	   super(res);
	 }

	 public void addCookie(Cookie cookie) {
	   System.out.println("Adding cookie");
	   StringBuffer header = new StringBuffer();
	   if ((cookie.getName() != null) && (!cookie.getName().equals(""))) {
	     header.append(cookie.getName());
	   }
	   if (cookie.getValue() != null) {
	     // Empty values allowed for deleting cookie
	     header.append("=" + cookie.getValue());
	   }
	  
	   if (cookie.getVersion() == 1) {
	     header.append(";Version=1");
	     if (cookie.getComment() != null) {
	       header.append(";Comment=\"" + cookie.getComment() + "\"");
	     }
	     if (cookie.getMaxAge() > -1) {
	       header.append(";Max-Age=" + cookie.getMaxAge());
	     }
	   } else {
	     if (cookie.getMaxAge() > -1) {
	       Date now = new Date();
	       now.setTime(now.getTime() + (1000L * cookie.getMaxAge()));
	       header.append(";Expires=" + HttpOnlyResponseWrapper.cookieFormat.format(now));
	     }
	   }
	  
	   if (cookie.getDomain() != null) {
	     header.append(";Domain=" + cookie.getDomain());
	   }
	   if (cookie.getPath() != null) {
	     header.append(";Path=" + cookie.getPath());
	   }
	   if (cookie.getSecure()) {
	     header.append(";Secure");
	   }
	   header.append(";httpOnly");
	   addHeader("Set-Cookie", header.toString());
	 }
	}

	