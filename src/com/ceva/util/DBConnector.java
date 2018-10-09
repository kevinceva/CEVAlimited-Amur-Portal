package com.ceva.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


public class DBConnector {

	private static Logger logger= Logger.getLogger(DBConnector.class);
	
	private static String DBDRIVER="oracle.jdbc.driver.OracleDriver";
	private static String DBURL="jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static String DBUSER="amur";
	private static String DBPASSWORD="amur";
	
	public DBConnector(){
		
		ResourceBundle resourceBunde=ResourceBundle.getBundle("auth");
			DBDRIVER=resourceBunde.getString("DBDRIVER");
			DBURL=resourceBunde.getString("DBURL");
			DBUSER=resourceBunde.getString("DBUSER");
			DBPASSWORD=resourceBunde.getString("DBPASSWORD");
	}
	
	public static Connection getConnection(){
		Connection conn=null;
		String driverName=DBDRIVER;
		String dbUrl=DBURL;
		String dbUser=DBUSER;
		String dbPassword=DBPASSWORD;
		try {
			Class.forName(driverName);
			try {
				conn=DriverManager.getConnection(dbUrl, dbUser, dbPassword);
				System.out.println("Connection ::"+conn);
			} catch (SQLException e) {
				logger.error("[DBConnector][getConnection]Exception raise due to DataBase details",e);
			}
		} catch (ClassNotFoundException e) {
			logger.error("[DBConnector][getConnection]DataBase Driver Not Availabe::::",e);
		}
		return conn;
		
	}
	
	public static void main(String args[]){
		DBConnector connector=new DBConnector();
		connector.getConnection();
	}
}

