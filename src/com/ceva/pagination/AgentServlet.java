package com.ceva.pagination;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class AgentServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(AgentServlet.class);
	
	
	public AgentServlet() {
		super();
		// TODO Auto-generated constructor stub
		logger.debug("Hello inside servlet..................");
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Helloooooooooooooooooooooooo");
		RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("jsp/agentReg.jsp");
		RequetsDispatcherObj.forward(request, response);
		
	}

	
	
}
