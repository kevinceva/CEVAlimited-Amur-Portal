package com.ceva.base.common.utils;

/*import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBConnector {
	private static Logger logger = Logger.getLogger(DBConnector.class);
	private static BoneCP connectionPool = null;

	static {
		setupDataSource();
	}

	private static void setupDataSource() {
		BoneCPConfig config = null;
		InputStream stream = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			stream = DBConnector.class
					.getResourceAsStream("/resource/bone-cp-db-config.xml");
			logger.debug("|DBConnector| DB XML Config Loading.");

			config = new BoneCPConfig(stream, "");
			config.setUsername(new DESEncryption().decrypt(config.getUsername()));
			config.setPassword(new DESEncryption().decrypt(config.getPassword()));

			connectionPool = new BoneCP(config);

			setConnectionPool(connectionPool);
			logger.debug(" |DBConnector| DB Properties loaded.");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.debug(" |DBConnector| Getting error message in SQLException :: "
					+ e.getMessage());
			try {
				config = null;
				if (stream != null) {
					stream.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(" |DBConnector| Getting error message in Exception :: "
					+ e.getMessage());
			try {
				config = null;
				if (stream != null) {
					stream.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		} finally {
			try {
				config = null;
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void shutdownConnPool() {
		BoneCP connectionPool = null;
		try {
			connectionPool = getConnectionPool();
			logger.info(" |DBConnector| contextDestroyed....");
			if (connectionPool != null) {
				connectionPool.close();
				connectionPool.shutdown();

				logger.info(" |DBConnector| contextDestroyed.....Connection Pooling shut downed!");
			}
		} catch (Exception e) {
			logger.info(" |DBConnector| connection exception is :: "
					+ e.getMessage());
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = getConnectionPool().getConnection();

			logger.info(" |DBConnector| Total Created Connections ["
					+ getConnectionPool().getTotalCreatedConnections() + "]");
			logger.info(" |DBConnector| Total Free    Connections ["
					+ getConnectionPool().getTotalFree() + "]");
			logger.info(" |DBConnector| Total Leased  Connections ["
					+ getConnectionPool().getTotalLeased() + "]");

			logger.info(" |DBConnector| Total Statements  Executed ["
					+ getConnectionPool().getStatistics()
							.getStatementsExecuted() + "]");

			logger.info(" |DBConnector| Total Statements  Prepared ["
					+ getConnectionPool().getStatistics()
							.getStatementsPrepared() + "]");
		} catch (Exception e) {
			conn = null;
		}
		return conn;
	}

	public static BoneCP getConnectionPool() {
		return connectionPool;
	}

	public static void setConnectionPool(BoneCP connectionPool) {
		connectionPool = connectionPool;
	}

}
*/


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


public class DBConnector {

	private static Logger logger= Logger.getLogger(DBConnector.class);
	
//	private static String DBDRIVER=null;
//	private static String DBURL=null;
//	private static String DBUSER=null;
//	private static String DBPASSWORD=null;
	
//	
	private static String DBDRIVER="oracle.jdbc.driver.OracleDriver";
	private static String DBURL="jdbc:oracle:thin:@127.0.0.1:1521:xe";
	
	//private static String DBURL="jdbc:oracle:thin:@10.0.1.5:1521:orcl";

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

