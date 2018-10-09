package com.ceva.base.common.biometric;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

public class BiometricAction  extends ActionSupport implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String result = "success";
	HttpServletRequest request;
	
	public String registerBioMember(){
		
		request = ServletActionContext.getRequest();
		
        try {
        	System.out.println("before input stream....");
			InputStream in =request.getInputStream();
			byte [] bytes =IOUtils.toByteArray(new InputStreamReader(in), "UTF-8");
			System.out.println("Input Stream Length:::"+bytes.length);
			String mmid =  request.getParameter("mmid");
	        System.out.println("mmid:::::"+mmid);
	        System.out.println("before db connection");
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(   "jdbc:oracle:thin:@localhost:1521:orcl","insdb2","insdb2");  
			System.out.println("before insertion");
			PreparedStatement ps=con.prepareStatement("insert into bio_image (IMG_ID,IMG_DESC,IMG_DATA,IMG_DATE)values(bio_img_seq.nextval,?,?,sysdate)");	
			ps.setString(1, mmid);
			ps.setBinaryStream(2,in,bytes.length);
			int i=ps.executeUpdate();  
			System.out.println(i+" records affected"); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
		return result;
	}

	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getServletRequest() {
		return this.request;
	}
	
	
}
