package com.ceva.base.common.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
/*
import org.apache.tomcat.dbcp.dbcp2.PStmtKey;

import javafx.css.PseudoClass;*/


public class GenericFunctions {

	
	/*public static java.sql.Timestamp oracleTimeStamp() {
		 
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	 
	}
	
	public static java.sql.Date oracleDate() {
		 
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	 
	}
	
	
	public static void generateSMS(String msisdn,String smsMsg ){
		
		String smsQry="insert into ALERTS (MSG_DATE,MOBILE_NO,APPL,FETCH_STATUS,TXN_REF_NO,RETRY_COUNT,UNIQUE_ID,OUT_MESSAGE) values(sysdate,?,'SMS','P',?,'0',?,?)";
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = DBConnector.getConnection();
			pstmt = connection.prepareStatement(smsQry);
			String refNo=String.valueOf(10000000 + new Random().nextInt(99999999));
			pstmt.setString(1, msisdn);
			pstmt.setString(2, refNo);
			pstmt.setString(3, refNo);
			pstmt.setString(4, smsMsg);
			int i=pstmt.executeUpdate();
			
			if(i==1){
				System.out.println("sms insertion done..");
			}
			
			connection.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				if(pstmt !=null)
					pstmt.close();
				if(connection !=null)
					connection.close();
			}catch(Exception e){
				
			}
		}
	}*/
}
