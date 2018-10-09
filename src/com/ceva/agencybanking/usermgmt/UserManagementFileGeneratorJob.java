package com.ceva.agencybanking.usermgmt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.ceva.base.common.utils.DBConnector;
import com.ceva.base.common.utils.EncryptTransactionPin;
import com.ceva.util.DBUtils;
import com.ceva.util.TerminalFileData;

public class UserManagementFileGeneratorJob {
	protected static Logger logger = Logger
			.getLogger(UserManagementFileGeneratorJob.class);

	public static String generateCsvFile() {
		logger.debug("------- UserManagementFileGenerator Starts-------");
		String fileName = "";
		String eachFileData = null;
		// FileWriter writer = null;
		String merchantId = null;
		String sid = null;
		String terminalid = null;
		String serialNo = null;
		String generatedFileName = null;
		String sourcepath = null;
		String fileQry = "";
		String Username = null;
		String password = null;
		String admin = null;
		String supervisor = null;
		// String cryptedPassword = null;
		String key = "97206B46CE46376894703ECE161F31F2";

		String tmknum = null;
		String eachData = "";
		String tmkinfo = "";

		ResourceBundle bundle = null;

		Connection connection = null;

		PreparedStatement terminalPstmt = null;
		PreparedStatement filePstmt = null;
		PreparedStatement tmkpsmt = null;
		PreparedStatement updatePstmt = null;

		ResultSet terminalRS = null;
		ResultSet fileRS = null;
		ResultSet tmkRS = null;

		// File file = null;
		HashMap<String, String> users = null;

		String terminalInfo = null;
		String updateQry = "";
		StringBuffer location = null;

		try {
			logger.debug("Inside try block.");

			bundle = ResourceBundle.getBundle("pathinfo_config");

			sourcepath = bundle.getString("USER_FILE_PATH");

			logger.debug("sourcepath[" + sourcepath + "]");

			terminalInfo = "select mid,sid,tid,serial,admpwd,suppwd,makerid from ( "
					+ "select MERCHANT_ID mid,STORE_ID sid,TERMINAL_ID tid,SERIAL_NO serial,ADMIN_PWD admpwd, SUPERVISOR_PWD suppwd,"
					+ "MAKER_ID makerid from USER_TERMINAL_MAPPING UTM where ASSIGN_FALG= ?) "
					+ "group by mid,sid,tid,serial,admpwd,suppwd,makerid having (COUNT(tid)>0)";
			try {

				connection = DBConnector.getConnection();
				logger.debug("Connection is null [" + connection == null + "]");
				terminalPstmt = connection.prepareStatement(terminalInfo);
				terminalPstmt.setString(1, "I");
				terminalRS = terminalPstmt.executeQuery();

				if (terminalRS.next()) {
					merchantId = terminalRS.getString(1);
					sid = terminalRS.getString(2);
					terminalid = terminalRS.getString(3);
					serialNo = terminalRS.getString(4);
					admin = terminalRS.getString(5);
					supervisor = terminalRS.getString(6);

					eachData = admin + supervisor;

					logger.debug("MerchantId[" + merchantId + "]" + " sid["
							+ sid + "]" + " terminalid[" + terminalid
							+ "]  serialNo[" + serialNo + "] admin[" + admin
							+ "]  supervisor[" + supervisor + "]");

					logger.debug("EachData [" + eachData + "]");

					eachFileData = "";
					fileQry = "select ULC.LOGIN_USER_ID,ULC.PIN from USER_LOGIN_CREDENTIALS ULC,"
							+ "USER_TERMINAL_MAPPING UTM where ULC.LOGIN_USER_ID=UTM.USER_ID and"
							+ " UTM.MERCHANT_ID=? and UTM.STORE_ID=? and UTM.TERMINAL_ID=? and ASSIGN_FALG=? ";

					generatedFileName = sourcepath + serialNo + ".txt";

					logger.debug("generatedFileName[" + generatedFileName + "]");

					fileName = serialNo + ".txt";
					logger.debug("|generateCsvFile| fileName[" + fileName + "]");

					// file = new File(generatedFileName);
					users = new HashMap<String, String>();
					// writer = new FileWriter(file);

					filePstmt = connection.prepareStatement(fileQry);
					filePstmt.setString(1, merchantId);
					filePstmt.setString(2, sid);
					filePstmt.setString(3, terminalid);
					filePstmt.setString(4, "I");

					fileRS = filePstmt.executeQuery();

					while (fileRS.next()) {
						Username = fileRS.getString(1);
						password = fileRS.getString(2);
						users.put(Username, password);

						logger.debug("Username[" + Username + "] Password["
								+ password + "]");
					}

					logger.debug("serialNo [" + serialNo + "]");

					tmkinfo = "select TMK FROM TERMINAL_MASTER where MERCHANT_ID=? and "
							+ "STORE_ID=? and TERMINAL_ID=? and SERIAL_NO=? ";

					logger.debug("tmkinfo [" + tmkinfo + "]");

					tmkpsmt = connection.prepareStatement(tmkinfo);
					tmkpsmt.setString(1, merchantId);
					tmkpsmt.setString(2, sid);
					tmkpsmt.setString(3, terminalid);
					tmkpsmt.setString(4, serialNo);

					tmkRS = tmkpsmt.executeQuery();

					if (tmkRS.next()) {
						tmknum = tmkRS.getString(1);
					}

					logger.debug("Tmknum [" + tmknum + "]");

					filePstmt.close();
					fileRS.close();
					tmkpsmt.close();

					// fileQry = "select
					// nvl(merchant_name,' '),nvl(city,' '),'KEN','KE'
					// from merchant_master where merchant_id=?";
					fileQry = "select nvl(store_name,' '),(select nvl(OFFICE_NAME,' ') "
							+ "from ceva_branch_master where OFFICE_CODE=sm.location),'KEN','KE' "
							+ "from store_master sm where store_id=?";

					filePstmt = connection.prepareStatement(fileQry);
					filePstmt.setString(1, sid.trim());
					fileRS = filePstmt.executeQuery();

					location = new StringBuffer(50);

					if (fileRS.next()) {
						location.append(
								fileRS.getString(1) == null ? " " : fileRS
										.getString(1)).append("##");
						location.append(
								fileRS.getString(2) == null ? " " : fileRS
										.getString(2)).append("##");
						location.append(
								fileRS.getString(3) == null ? " " : fileRS
										.getString(3)).append("##");
						location.append(fileRS.getString(4) == null ? " "
								: fileRS.getString(4));
					}

					filePstmt.close();
					fileRS.close();

					logger.debug("location[" + location.toString() + "]");
					try {
						eachFileData = TerminalFileData.CreateFileData(users,
								terminalid, merchantId, EncryptTransactionPin
										.add2Encrypt(key, supervisor, admin,
												'F'), tmknum, location
										.toString());
					} catch (Exception e) {
						logger.error(e);
					}

					logger.debug("Data Loading into file [" + generatedFileName
							+ "] Data [" + eachFileData + "]");

					/*
					 * writer.append(eachFileData); writer.flush();
					 * writer.close();
					 */

					updateQry = "update USER_TERMINAL_MAPPING set ASSIGN_FALG=?"
							+ " where MERCHANT_ID=? and STORE_ID=? and "
							+ "TERMINAL_ID=? and ASSIGN_FALG=? ";
					updatePstmt = connection.prepareStatement(updateQry);
					updatePstmt.setString(1, "C");
					updatePstmt.setString(2, merchantId);
					updatePstmt.setString(3, sid);
					updatePstmt.setString(4, terminalid);
					updatePstmt.setString(5, "I");
					int updateCnt = updatePstmt.executeUpdate();

					logger.debug("updateCnt[" + updateCnt + "]");

					fileQry = "select * from ftp_pos where serialno=?";
					filePstmt = connection.prepareStatement(fileQry);
					filePstmt.setString(1, serialNo);
					fileRS = filePstmt.executeQuery();

					if (fileRS.next()) {
						logger.debug("Inside ftp_pos if.");
						//tmkinfo = "update ftp_pos set data=? where serialno=? and TERMINALID=?";
						tmkinfo = "update ftp_pos set data=?,terminalid=?,MAKERDTTM=sysdate where serialno=trim(?)";
						tmkpsmt = connection.prepareStatement(tmkinfo);
						tmkpsmt.setString(1, eachFileData);
						tmkpsmt.setString(2, terminalid);
						tmkpsmt.setString(3, serialNo); 
					} else {
						logger.debug(" inside ftp_pos else.");
						tmkinfo = "insert into ftp_pos "
								+ "(serialno,data,TERMINALID)"
								+ " values(?,?,?)";
						tmkpsmt = connection.prepareStatement(tmkinfo);
						tmkpsmt.setString(1, serialNo);
						tmkpsmt.setString(2, eachFileData);
						tmkpsmt.setString(3, terminalid);
					}

					updateCnt = tmkpsmt.executeUpdate();
					connection.commit();
					logger.debug("updateCnt[" + updateCnt + "]");
				}
			} catch (SQLException e) { 
				logger.error(e);
			}

		} catch (Exception e) {
			logger.error(e);
			fileName = "dummy.txt";
		} finally {
			
			DBUtils.closeResultSet(tmkRS);
			DBUtils.closeResultSet(fileRS);
			DBUtils.closeResultSet(terminalRS);

			DBUtils.closePreparedStatement(updatePstmt);
			DBUtils.closePreparedStatement(filePstmt);
			DBUtils.closePreparedStatement(terminalPstmt);
			DBUtils.closePreparedStatement(tmkpsmt);

			DBUtils.closeConnection(connection);
			
			eachFileData = null;
			merchantId = null;
			sid = null;
			terminalid = null;
			serialNo = null;
			generatedFileName = null;
			sourcepath = null;
			fileQry = null;
			Username = null;
			password = null;
			admin = null;
			supervisor = null;
			key = null;

			tmknum = null;
			eachData = null;
			tmkinfo = null;

			bundle = null;

			// file = null;
			users = null;
			location.delete(0, location.length());
			location = null;
			terminalInfo = null;
			logger.debug("------- UserManagementFileGenerator Ends-------");
		}

		return fileName;
	}

}