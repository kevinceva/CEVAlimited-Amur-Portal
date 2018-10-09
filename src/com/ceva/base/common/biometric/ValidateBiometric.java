package com.ceva.base.common.biometric;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import util.StringUtil;


@WebServlet("/AgencyBanking/ValidateBiometric")
public class ValidateBiometric extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		

		System.out.println("welcome to ajax val serv");

		try {        

			HttpSession session=request.getSession(false); 
			System.out.println("Session Id - "+session.getId());
			String valres = (String) session.getAttribute("valres");
			System.out.println("values of session attribute:"+valres);
			JSONObject resJSON2=new JSONObject();

			String formVal = null;

			if (!StringUtil.isNullOrEmpty(valres) && "S".equals(valres))
			{
				formVal = "Y";
			}
			else
			{
				formVal = "N";
			}

			session.removeAttribute("valres");
			System.out.println("formVal value:"+formVal);
			resJSON2.put("biores", formVal);
			System.out.println(resJSON2.toString());
			PrintWriter out = response.getWriter();
			out.println(resJSON2.toString());
			out.flush();
			out.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("welcome to val serv............................");
		doPost(req, resp);
	}

}
