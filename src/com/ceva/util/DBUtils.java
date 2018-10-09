package com.ceva.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class DBUtils {
	protected static Logger logger = Logger.getLogger(DBUtils.class);

	public static String getValue(String key) throws Exception {
		String value = null;
		ResourceBundle bundle = ResourceBundle.getBundle("common");
		value = bundle.getString(key);
		if ("DBPWD".equalsIgnoreCase(key))
			logger.debug("|getValue| Key - Value : " + key
					+ " - *******");
		else
			logger.debug("|getValue| Key - Value : " + key + " - "
					+ value);
		return value;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (Exception e) {
			logger.error("|closeConnection| e :|" + e.getMessage()
					+ "|");
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
				logger.debug("|closeStatement| ResultSet Closed ... ");
			}
		} catch (Exception e) {
			logger.error("|closeResultSet| e :|" + e + "|");
		}
	}

	public static void closeStatement(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
				logger.debug("|closeStatement| Statement Closed. ");
			}
		} catch (Exception e) {
			logger.error("|closeStatement| e :|" + e.getMessage()
					+ "|");
		}
	}

	public static void closePreparedStatement(PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
				logger.debug("|closePreparedStatement| PreparedStatement Closed. ");
			}
		} catch (Exception e) {
			logger.error("|closePreparedStatement| e :|"
					+ e.getMessage() + "|");
		}
	}

	public static void closeCallableStatement(CallableStatement callable) {
		try {
			if (callable != null) {
				callable.close();
				callable = null;
				logger.debug("|closeCallableStatement| CallableStatement Closed. ");
			}
		} catch (Exception e) {
			logger.error("|CallableStatement| e :|" + e.getMessage()
					+ "|");
		}
	}
}
