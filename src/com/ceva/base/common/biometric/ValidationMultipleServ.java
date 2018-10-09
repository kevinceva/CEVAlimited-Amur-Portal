package com.ceva.base.common.biometric;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import com.ceva.base.common.utils.DBConnector;



/**
 * Servlet implementation class GetBlobFromDB
 */
@WebServlet("/ValidationMultipleServ")
public class ValidationMultipleServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("welcome to val serv");
		String valres =  request.getParameter("valres");
		String bio_mid =  request.getParameter("mid");
		
		String mkrid="";
		BufferedOutputStream bf = null;
		int i=0;
		int cnt=0;
		ResultSet rs = null;

		try {        

			HttpSession session = request.getSession(false);
			System.out.println("Session Id - "+session.getId());
			System.out.println("value of valres:"+valres);
			
			System.out.println("val multi serv bio_mid value:"+bio_mid);

			mkrid= (String)session.getAttribute("userid");
			System.out.println("mkrid value:"+mkrid);

			if(!StringUtility.isValidString(valres))
			{
				valres = "F";
			}

			session.setAttribute("valres", valres);

			System.out.println("after set session valres:"+valres);

			Connection con=DBConnector.getConnection();
			if(StringUtility.isValidString(bio_mid) && "S".equalsIgnoreCase(valres))
			{
				bf = new BufferedOutputStream(response.getOutputStream());  

				PreparedStatement ext=con.prepareStatement("select count(mem_id) from bio_val_trk where trim(mem_id)=? and trunc(val_date)=trunc(sysdate)");	
				ext.setString(1, bio_mid);
				rs =ext.executeQuery();

				if (rs.next())
				{
					cnt=Integer.parseInt(rs.getString(1));
				}
				System.out.println("mem id ext cnt:"+cnt);

				if (cnt<1)
				{	 
					PreparedStatement ps=con.prepareStatement("insert into bio_val_trk (mem_id,admit_date,val_date,val_mkr,val_status) values (?,sysdate,sysdate,?,?)");	
					ps.setString(1, bio_mid);
					ps.setString(2,mkrid);
					ps.setString(3, valres);

					i=ps.executeUpdate();  
					System.out.println(i+" records affected"); 
				}
			}


			BufferedOutputStream vbf = new BufferedOutputStream(response.getOutputStream());
			vbf.write(valres.getBytes());
			vbf.close();

			System.out.println("after buffer close");


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
