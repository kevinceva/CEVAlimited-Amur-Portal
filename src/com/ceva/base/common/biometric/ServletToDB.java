package com.ceva.base.common.biometric;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;

import com.ceva.base.common.utils.DBConnector;

/**
 * Servlet implementation class ServletToDB
 */
@WebServlet("/ServletToDB")
public class ServletToDB extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletToDB() {
		super();        
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext sc = this.getServletContext(); 
		BufferedOutputStream bf = null;
		int i=0;
		int memCnt=0;
		ResultSet rs = null;
		try 
		{ 
			System.out.println("Welcomw to program");
			HttpSession session = request.getSession();
			System.out.println("Session Id - "+session.getId());


			InputStream in =request.getInputStream();
			System.out.println("before db connection");

			Connection con=DBConnector.getConnection();

			System.out.println("before insertion");
			String mmid =  request.getParameter("mmid");
			String demmid = URLDecoder.decode(mmid, "UTF-8");
			System.out.println("decoded mmid:"+demmid);
			String handval =  request.getParameter("handVal");
			String fingerval =  request.getParameter("fingVal");
			String mkrid =  request.getParameter("makerId");
			String demkrid =  URLDecoder.decode(mkrid, "UTF-8");
			System.out.println("decoded demkrid:"+demkrid);
			System.out.println("input parameter values are ="+mmid+"  "+handval+"  "+fingerval+" "+mkrid);
			System.out.println(in);
			String profileId="";

			if(StringUtility.isValidString(mmid))
			{
				bf = new BufferedOutputStream(response.getOutputStream()); 
		        Statement stmt = con.createStatement();
				//String query ="select count(mem_id) from BIO_IMAGE where trim(mem_id)='"+demmid+"' and hand_val='"+handval+"' and fing_val='"+fingerval+"' ";
		        String query ="select count(BI.mem_id) from BIO_IMAGE BI,W_PROFILES WP where WP.PROFILEID=BI.MEM_ID(+) and WP.IDNUMBER='"+demmid+"' and BI.hand_val='"+handval+"' and BI.fing_val='"+fingerval+"' ";
				System.out.println("Query - "+query);
		        rs =stmt.executeQuery(query);
		        
		        if (rs.next()) {
		        	memCnt = Integer.parseInt(rs.getString(1));
		        }
		        
		        
		        query ="select PROFILEID from W_PROFILES where trim(IDNUMBER)='"+demmid+"' ";
		        //String query ="select WP.PROFILEID,count(BI.mem_id) from BIO_IMAGE BI,W_PROFILES WP where WP.PROFILEID=BI.MEM_ID(+) and WP.IDNUMBER='"+demmid+"' and BI.hand_val='"+handval+"' and BI.fing_val='"+fingerval+"'  group by WP.PROFILEID ";
				System.out.println("Query - "+query);
		        rs =stmt.executeQuery(query);
		        
		        if (rs.next()) {
		        	profileId =rs.getString(1);
		        }
		        
		        
		        System.out.println("value of memCnt:"+memCnt+"---"+profileId.length());
		        

		        if(profileId.length()==0)
		        	profileId=demmid;
		        
		        System.out.println("value of memCnt:"+memCnt+"---"+profileId);
		        
		        if (memCnt<1)
		        {	
			        System.out.println("populating biometrics");
			        
			       
			        
			        
					PreparedStatement ps=con.prepareStatement("insert into bio_image (IMG_ID,mem_id,IMG_DATA,IMG_DATE,capturedby,hand_val,fing_val)values(bio_img_seq.nextval,?,?,current_date,?,?,?)");	
					ps.setString(1, profileId);
					ps.setBinaryStream(2,in,512);
					ps.setString(3, demkrid);
					ps.setString(4, handval);
					ps.setString(5, fingerval);
	
					i=ps.executeUpdate();  
					System.out.println(i+" records affected"); 
					ps.close();
					
					ps=con.prepareStatement("insert into BIO_PROFILE_BALANCES (PROFILE_ID,BALANCE)values(?,0)");	
					ps.setString(1, profileId);
	
					i=ps.executeUpdate();  
					System.out.println(i+" records affected"); 
					ps.close();
		        }
		        else
		        {
			        System.out.println("updating Biometrics");
					PreparedStatement ps=con.prepareStatement("update bio_image set img_data=?, updatedby=?,updated_date=current_date  where mem_id=? and hand_val=? and fing_val= ?");	
					ps.setBinaryStream(1,in,512);
					ps.setString(2, demkrid);
					ps.setString(3, profileId);
					ps.setString(4, handval);
					ps.setString(5, fingerval);
	
					i=ps.executeUpdate();  
					System.out.println(i+" records affected"); 
					ps.close();
		        }
			}

			if (i>0)
			{
				System.out.println("DB Success");
				bf.write("00".getBytes());
				bf.close();
			}
			else
			{
				System.out.println("DB Failure");
				bf.write("0000".getBytes());
				bf.close();
			}

		} 
		catch(java.sql.SQLIntegrityConstraintViolationException conex)
		{
			bf.write("1111".getBytes());
			bf.close();
		}
		catch(Exception e) 
		{ 
			bf.write("9999".getBytes());
			bf.close();
			e.printStackTrace(); 
		} 
	}
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {/*
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 */}

		return sb.toString();

	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
