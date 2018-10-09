package com.ceva.user.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.ceva.base.common.utils.DBConnector;
import com.ceva.user.utils.Util;
import com.ceva.util.DBUtils;

public class UserLockingProcess {
	/*
	 * DATA FLOW FROM WEB REQ FROM WEB --> USERID=REGISTER|USLIDN|TOKEN RES TO
	 * WEB --> USERID=USLIDN|SERIALNO|TOKEN
	 * 
	 * REQ FROM WEB --> USERID=VALIDATE|DBDATA(USLIDN|SERIALNO|TOKEN) Server
	 * Side Token Validation. RES FROM WEB --> USERID=[VALIDATEUSER /
	 * INVALIDATEUSER]|TOKEN
	 */

	private static Logger logger = Logger.getLogger(UserLockingProcess.class);

	public static String regiterReq(String USLIDN, String userId, String token) {
		String req = "";
		if (userId != null && USLIDN != null) {
			req = userId.toUpperCase() + "=REGISTER|" + USLIDN + "|" + token;
			try {
				req = RSASecurity.encryptData(req);
			} catch (IOException e) {

			}
		}
		return req;
	}

	public static String validateReq(String userId, String userAuthData,
			String token) {
		String req = "";
		if (userId != null && userAuthData != null) {
			req = userId.toUpperCase() + "=VALIDATE|" + userAuthData + "|"
					+ token;
			try {
				req = RSASecurity.encryptData(req);
			} catch (IOException e) {

			}
		}
		return req;
	}

	public static boolean processRegistration(String userId, String clientRes,
			String token, String webuslidn) {
		boolean resp = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int resCnt = 0;

		String decryptClnData = "";
		StringTokenizer st = null;
		String ui = "";
		String uslidn = "";
		String serialno = "";
		String tkn = "";
		String dbresp = "";
		if (userId != null && clientRes != null) {

			try {
				logger.debug("clientRes - " + clientRes);
				if (userId != null && clientRes != null && token != null
						&& webuslidn != null) {
					decryptClnData = RSASecurity.decryptData(Util
							.hex2byte(clientRes));
					st = new StringTokenizer(decryptClnData, "=,|");
					ui = st.nextToken();
					uslidn = st.nextToken();
					serialno = st.nextToken();
					tkn = st.nextToken();

					logger.debug(userId.toUpperCase() + "=" + ui);
					logger.debug(tkn + "=" + token);

					if (userId.toUpperCase().equals(ui)
							&& webuslidn.equals(uslidn) && tkn.equals(token)) {
						// insert into DB
						dbresp = uslidn + "|" + serialno;
						try {
							conn = DBConnector.getConnection();
							String updateQry = "UPDATE USER_LOCKING_INFO set USER_DATA=? where USER_ID=? ";
							pstmt = conn.prepareStatement(updateQry);
							pstmt.setString(1, dbresp);
							pstmt.setString(2, userId);
							resCnt = pstmt.executeUpdate();
						} catch (SQLException e) {
							logger.debug("SQLException in processRegistration ::: "+ e.getMessage());
						} catch (Exception e) {
							logger.debug("Exception in processRegistration ::: "+ e.getMessage());
						}

						// UserLockingProcess.workoutData(dbresp, true);
						if (resCnt == 1) {
							resp = true;
						}
					}
				}
			} catch (IOException e) {
				logger.debug(e.getMessage());
			} finally {
				
				DBUtils.closePreparedStatement(pstmt);
				DBUtils.closeConnection(conn);
				
				decryptClnData = null;
				st = null;
				ui = null;
				uslidn = null;
				serialno = null;
				tkn = null;

			}
		}

		return resp;
	}

	public static boolean isValidUser(String userId, String clientRes,
			String token, String authValue) {
		boolean auth = false;

		String decryptClnData;
		try {
			if (clientRes != null) {
				decryptClnData = RSASecurity.decryptData(Util
						.hex2byte(clientRes));
				StringTokenizer st = new StringTokenizer(decryptClnData, "=,|");
				String ui = st.nextToken();
				String valid = st.nextToken();
				String tkn = st.nextToken();
				logger.debug(userId + "=" + ui);
				logger.debug(token + "=" + tkn);
				logger.debug("Valid =" + valid);
				if (userId.toUpperCase().equals(ui) && tkn.equals(token)) {
					if (valid.equals("VALIDATEUSER")) {
						auth = true;
					}
				}
			}

		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.debug("Exception is ::: " + e.getMessage());
		}

		return auth;
	}

	public static boolean isvalidUSLIDN(String userId, String uslidn,
			String appData) {
		boolean valid = false;

		StringTokenizer st = new StringTokenizer(appData, "=,|");
		if (userId != null && uslidn != null && appData != null) {
			String ui = st.nextToken();
			String usl = st.nextToken();
			String sln = st.nextToken();
			if (userId.equals(ui) && usl.equals(uslidn)) {
				valid = true;
			}
		}

		return valid;
	}

	public static String workoutData(String data, boolean isInsert) {

		FileWriter fw = null;
		BufferedWriter bw = null;
		BufferedReader br = null;
		String res = "";
		String path = "C:\\Users\\Rajkumar\\POSTA\\db.pan";
		try {
			if (isInsert) {
				if (data != null) {
					fw = new FileWriter(new File(path));
					bw = new BufferedWriter(fw);
					bw.write(data);
				}
			} else {
				br = new BufferedReader(new InputStreamReader(
						new FileInputStream(new File(path))));

				res = br.readLine();
			}

		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {

			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					logger.debug(e.getMessage());
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					logger.debug(e.getMessage());
				}
			}

			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					logger.debug(e.getMessage());
				}
			}
		}

		return res;

	}

}
