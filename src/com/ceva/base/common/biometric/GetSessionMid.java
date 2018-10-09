package com.ceva.base.common.biometric;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GetSessionMid
 */
@WebServlet("/GetSessionMid")
public class GetSessionMid extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetSessionMid() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		ServletContext sc = this.getServletContext(); 
		BufferedOutputStream bf = null;
		int i=0;
		String bio_mid="";

		try 
		{
			HttpSession session = request.getSession(false);
			System.out.println("getses mid Session Id - "+session.getId());


			bio_mid= (String)session.getAttribute("BIO_MID");
			System.out.println(" getses mid session bio_mid:"+bio_mid);
			
			if(!StringUtility.isValidString(bio_mid))
			{
				bio_mid = "0000";
			}


			bf = new BufferedOutputStream(response.getOutputStream());
			bf.write(bio_mid.getBytes());
			bf.close();

			System.out.println("getses mid after get ses mid buffer close");


		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
