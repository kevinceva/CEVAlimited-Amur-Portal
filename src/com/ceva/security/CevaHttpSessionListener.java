package com.ceva.security;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class CevaHttpSessionListener implements HttpSessionListener{
	  public void sessionCreated(HttpSessionEvent event){
	    event.getSession().setMaxInactiveInterval(15*60); //in seconds
	  }
	  public void sessionDestroyed(HttpSessionEvent event){}
	}