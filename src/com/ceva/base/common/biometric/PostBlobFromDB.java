package com.ceva.base.common.biometric;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
@WebServlet("/PostBlobFromDB")
public class PostBlobFromDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
   	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
			System.out.println("Oracle Connect START.");
			String mmid =  request.getParameter("mmid");
			
	        ResultSet rs = null;
	        try {        
	        
	        HttpSession session = request.getSession();
	        System.out.println("Session Id - "+session.getId());
	        session.setAttribute("mem_id", mmid);
	        
	        String demmid = URLDecoder.decode(mmid, "UTF-8");
	        System.out.println("decoded demmid:"+demmid);
	        
			String handval =  request.getParameter("handVal");
			String fingerval =  request.getParameter("fingVal");
			String capby =  "cevapoweruser";
			System.out.println("input parameter values are ="+mmid+"  "+handval+"  "+fingerval+" "+capby);
			
	   	   /* Class.forName("oracle.jdbc.driver.OracleDriver");  
	   	    Connection con=DriverManager.getConnection(   
			"jdbc:oracle:thin:@localhost:1521:orcl","insdb2","insdb2");  */
			
			Connection con=DBConnector.getConnection();
				   	    BufferedOutputStream bf = new BufferedOutputStream(response.getOutputStream());

				
	        Statement stmt = con.createStatement();
			System.out.println("before selection");
			String query ="select IMG_DATA from BIO_IMAGE where trim(mem_id)='"+demmid+"' and hand_val='"+handval+"' and fing_val='"+fingerval+"' ";
			System.out.println("Query - "+query);
	         rs =stmt.executeQuery(query);
	         Blob lob = null;
	         if (rs.next()) {
	         lob=rs.getBlob("IMG_DATA"); 
	         InputStream in = lob.getBinaryStream();
		        byte[] bytes = IOUtils.toByteArray(in);
				bf.write(bytes);
				System.out.println(bytesToHex(bytes));
				bf.close();
				in.close(); 
		        System.out.println("after lob stream");
	         }
	         else
	         {
	        	 bf.write("0000".getBytes());
	        	 bf.close();
	         }
	         System.out.println("before lob stream");
	        //in.close();
	       // Process p1 =Runtime.getRuntime().exec("mspaint blobImage.png");
	 
	        }catch (Exception e) {
	            e.printStackTrace();
	            }
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
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("welcome to postdb serv............................");
		doPost(req, resp);
	}

}
