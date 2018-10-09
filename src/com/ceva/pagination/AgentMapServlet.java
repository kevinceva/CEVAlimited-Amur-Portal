package com.ceva.pagination;

import java.io.IOException;



import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ceva.util.VerifyRecaptcha;

public class AgentMapServlet extends HttpServlet{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = -6506682026701304964L;
	
	//private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(AgentMapServlet.class);
	
	
	public AgentMapServlet() {
		super();
		// TODO Auto-generated constructor stub
		logger.debug("Hello inside servlet..................");
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Helloooooooooooooooooooooooo");
		String gRecaptchaResponse = request
				.getParameter("g-recaptcha-response");
		System.out.println(gRecaptchaResponse);
		boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
		if (verify) {
			System.out.println("<font color=red>Either user name or password is wrong.</font>");
		} else {
			System.out.println("<font color=red>You missed the Captcha.</font>");
		}
		RequestDispatcher RequetsDispatcherObj =request.getRequestDispatcher("jsp/agentMap.jsp");
		RequetsDispatcherObj.forward(request, response);
		
	}

	
	
}
