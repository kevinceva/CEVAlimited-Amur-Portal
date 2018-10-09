package com.ceva.base.common.biometric;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import util.StringUtil;


/**
 * Servlet implementation class CheckBioValTrkStatus
 */
@WebServlet("/CheckBioValTrkStatus")
public class CheckBioValTrkStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckBioValTrkStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("welcome to ajax val serv");

		try {        

			HttpSession session=ServletActionContext.getRequest().getSession(false); 
			System.out.println("Session Id - "+session.getId());
			String valstat = (String) session.getAttribute("valstat");
			System.out.println("values of session attribute:"+valstat);
			JSONObject resJSON2=new JSONObject();

			String formVal = null;

			if (!StringUtil.isNullOrEmpty(valstat) && "S".equals(valstat))
			{
				formVal = "Y";
			}
			else
			{
				formVal = "N";
			}

			System.out.println("formVal value:"+formVal);
			resJSON2.put("valstat", formVal);
			System.out.println(resJSON2.toString());
			PrintWriter out = response.getWriter();
			out.println(resJSON2.toString());
			out.flush();
			out.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
