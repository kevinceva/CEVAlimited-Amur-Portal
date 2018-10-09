package com.ceva.base.common.dao;

import com.ceva.base.common.bean.CevaCommonMenuBean;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.security.PasswordValidation;
import com.ceva.user.security.UserLocking;
import com.ceva.user.security.UserLockingClient;
import com.ceva.user.security.UserLockingProcess;
import com.ceva.util.CommonUtil;
import com.ceva.util.DBUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class CommonLoginDAO {
	private Logger logger = Logger.getLogger(CommonLoginDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON;
	JSONObject menuJSON = null;
	HashMap<String, Object> menuMap = null;
	private String userid;
	private String password;
	private String userGroup;
	private String applName;
	private String loginEntity;
	private String userLevel;
	private String location;
	private String loginTime;
	private String pidMenuQuery = null;
	private String randomVal = null;
	private String remoteIp = null;
	private String dbPassword = null;
	private String systemStatus = null;
	private String USLIDN = null;
	boolean isAuthUser = false;
	private String authRequired = null;
	private ResourceBundle rb = null;
	String sessionId = "";

	public ResponseDTO validatLogin(RequestDTO requestDTO) {
		this.logger.debug("Inside Validate Login Raaaa... ");
		Connection connection = null;

		PreparedStatement userGrpStmt = null;
		PreparedStatement userSystemStmt = null;
		PreparedStatement userSystemStatusStmt = null;
		PreparedStatement userSysStatusUpdateStmt = null;
		PreparedStatement validatePstmt = null;
		PreparedStatement validateUserPstmt = null;
		PreparedStatement pwdExpPstmt = null;
		PreparedStatement pstmt = null;
		CallableStatement callableStatement = null;

		ResultSet pwdExpRs = null;
		ResultSet validateRS = null;
		ResultSet validateUserRS = null;
		ResultSet userGrpRs = null;
		ResultSet systemRs = null;
		ResultSet systemStatusRs = null;
		ResultSet rs = null;

		String userStatus = null;
		String lockTIme = null;
		String validateSystem = "";
		String validateAppl = "";
		String userSystemStatusQry = "";
		String token = "";
		String authValue = "";
		String userdetails = "";
		String updateStatus = "";
		String userData = "";
		String validateQry = "";
		String validatePwdExpQry = "";
		String passQry = "";
		String userLockProc = "";
		String userGrpQry = "";

		int wrongPwdCnt = 0;
		int status = 0;
		int applCnt = 0;
		int sysCnt = 0;

		this.rb = ResourceBundle.getBundle("pathinfo_config");
		try {
			this.authRequired = this.rb.getString("AUTH_REQUIRED");
		} catch (Exception e) {
			this.logger.debug("AuthRequired not configured in property file.. By default setting to 'NO'");
			this.authRequired = "NO";
		}
		this.logger.debug("AuthRequired [" + this.authRequired + "]");
		try {
			this.menuJSON = new JSONObject();
			this.responseDTO = new ResponseDTO();

			this.requestJSON = requestDTO.getRequestJSON();
			this.logger.debug("Request JSON [" + this.requestJSON + "]");

			this.userid = this.requestJSON.getString("userId");
			this.password = this.requestJSON.getString("password");
			this.applName = this.requestJSON.getString("APPL_NAME");
			this.randomVal = this.requestJSON.getString("RANDOM_VALUE");
			this.remoteIp = this.requestJSON.getString("REMOTE_IP");
			sessionId = requestJSON.getString("SESSION_ID");

			if (this.userid == null) {
				this.responseDTO.addError("Invalid User Id");
			} else if (this.password == null) {
				this.responseDTO.addError("Invalid Password");
			} else if (this.authRequired == null) {
				this.responseDTO.addError("User Authentication not configured");
			} else {
				connection = connection == null ? DBConnector.getConnection() : connection;
				this.logger.debug(Boolean.valueOf("Connection is null [" + connection == null + "]"));
				if (connection == null) {
					throw new SQLException("DB Connection is null, please check the network configurations.");
				}
				validateAppl = "select count(*) from USER_LOGIN_CREDENTIALS where LOGIN_USER_ID=? and APPL_CODE=?";

				validatePstmt = connection.prepareStatement(validateAppl);
				validatePstmt.setString(1, this.userid);
				validatePstmt.setString(2, this.applName);
				validateRS = validatePstmt.executeQuery();
				if (validateRS.next()) {
					applCnt = validateRS.getInt(1);
				}
				DBUtils.closeResultSet(validateRS);
				DBUtils.closePreparedStatement(validatePstmt);

				this.logger.debug("User Login Count Check [" + applCnt + "]");
				if (applCnt == 1) {
					if (this.authRequired.equals("YES")) {
						validateSystem = "Select count(*) from USER_LOCKING_INFO where USER_ID=?";

						userSystemStmt = connection.prepareStatement(validateSystem);
						userSystemStmt.setString(1, this.userid);
						systemRs = userSystemStmt.executeQuery();
						if (systemRs.next()) {
							sysCnt = systemRs.getInt(1);
						}
						DBUtils.closeResultSet(systemRs);
						DBUtils.closePreparedStatement(userSystemStmt);

						this.logger.debug("SysCnt for USER_LOCKING_INFO is [" + sysCnt + "]");
						if (sysCnt == 1) {
							userSystemStatusQry = "Select STATUS,ULSID,USER_DATA from USER_LOCKING_INFO where USER_ID=?";
							userSystemStatusStmt = connection.prepareStatement(userSystemStatusQry);
							userSystemStatusStmt.setString(1, this.userid);
							systemStatusRs = userSystemStatusStmt.executeQuery();
							if (systemStatusRs.next()) {
								this.systemStatus = systemStatusRs.getString(1);
								this.USLIDN = systemStatusRs.getString(2);
								userData = systemStatusRs.getString(3);
							}
							this.logger.debug("SystemStatus [" + this.systemStatus + "] USLIDN [" + this.USLIDN + "]");

							DBUtils.closeResultSet(systemStatusRs);
							DBUtils.closePreparedStatement(userSystemStatusStmt);

							token = UserLocking.getRandomValue();
							if (this.systemStatus.equals("W")) {
								authValue = UserLockingProcess.regiterReq(this.USLIDN, this.userid, token);
								this.logger.debug("authValue - " + authValue);
								userdetails = UserLockingClient.callLockingServer(this.remoteIp, 7777, authValue);
								this.logger.debug("userdetails - " + userdetails);

								this.logger.debug("is data valid Data in W State::: - " + this.isAuthUser);
								if (this.isAuthUser) {
									updateStatus = "update USER_LOCKING_INFO set STATUS=? where USER_ID=?";
									userSysStatusUpdateStmt = connection.prepareStatement(updateStatus);
									userSysStatusUpdateStmt.setString(1, "R");
									userSysStatusUpdateStmt.setString(2, this.userid);
									int updCnt = userSysStatusUpdateStmt.executeUpdate();
									if (updCnt == 1) {
										this.logger.debug("Status Updated to R");
									}
									DBUtils.closePreparedStatement(userSysStatusUpdateStmt);
								}
							}
							if (this.systemStatus.equals("R")) {
								authValue = userData;
								this.logger.debug("R authValue - " + authValue);
								userData = UserLockingProcess.validateReq(this.userid, authValue, token);
								this.logger.debug("R userData - " + userData);
								userdetails = UserLockingClient.callLockingServer(this.remoteIp, 7777, userData);
								this.logger.debug("R userdetails - " + userdetails);
								this.isAuthUser = UserLockingProcess.isValidUser(this.userid, userdetails, token,
										authValue);
								this.logger.debug("R isAuthUser - " + this.isAuthUser);
							}
							if (this.isAuthUser) {
								this.logger.debug("User Authentication Success");
							} else {
								this.responseDTO.addError("Locking System Authetication failed.");
							}
						} else {
							this.responseDTO.addError("Please Register for User Locking System.");
						}
					}
					if ((this.isAuthUser) || (this.authRequired.equals("NO"))) {
						try {
							validateQry = "select PASSWORD from USER_LOGIN_CREDENTIALS where LOGIN_USER_ID=?";
							validateUserPstmt = connection.prepareStatement(validateQry);
							validateUserPstmt.setString(1, this.userid);
							validateUserRS = validateUserPstmt.executeQuery();
							if (validateUserRS.next()) {
								this.dbPassword = validateUserRS.getString(1);
							}
							DBUtils.closeResultSet(validateUserRS);
							DBUtils.closePreparedStatement(validateUserPstmt);

							this.logger.debug("DBPassword [" + this.dbPassword + "]");

							this.dbPassword = this.dbPassword.concat(this.randomVal);
							this.logger.debug("After adding random value dbPassword [" + this.dbPassword + "]");
							this.dbPassword = CommonUtil.b64_sha256(this.dbPassword);
							this.logger.debug(
									"After adding random value encrpted password dbPassword [" + this.dbPassword + "]");
							if (this.dbPassword.equals(this.password)) {
								userGrpQry = "select USER_GROUPS,USER_STATUS,ENTITY,USER_LEVEL,(select NVL(office_name,' ') from CEVA_BRANCH_MASTER where OFFICE_CODE=UI.LOCATION),to_char(sysdate,'HH:MI AM Month DD, YYYY'),to_char(USER_LOCK_TIME,'HH:MI AM Month DD, YYYY') from USER_INFORMATION UI,USER_LOGIN_CREDENTIALS ULC where UI.COMMON_ID=ULC.COMMON_ID and ULC.LOGIN_USER_ID=?";

								userGrpStmt = connection.prepareStatement(userGrpQry);
								userGrpStmt.setString(1, this.userid);
								userGrpRs = userGrpStmt.executeQuery();
								if (userGrpRs.next()) {
									this.userGroup = userGrpRs.getString(1);
									userStatus = userGrpRs.getString(2);
									this.loginEntity = userGrpRs.getString(3);
									this.userLevel = userGrpRs.getString(4);
									this.location = userGrpRs.getString(5);
									this.loginTime = userGrpRs.getString(6);
									lockTIme = userGrpRs.getString(7);
								}
								DBUtils.closeResultSet(userGrpRs);
								DBUtils.closePreparedStatement(userGrpStmt);

								this.logger.debug("lockTIme [" + lockTIme + "] User Group [" + this.userGroup
										+ "] LoginEntity[" + this.loginEntity + "]");
								if (this.userGroup == null) {
									this.responseDTO.addError("Rights not assigned");
								} else {
									this.menuMap = new HashMap();

									if ((lockTIme != null) && (userStatus.equals("L"))) {
										status = PasswordActivate(this.userid, userStatus, "VALID");
										this.logger.debug("Status from PasswordActivate [" + status + "]");

										userStatus = status == 1 ? (userStatus = "A") : "";
									}
									if (userStatus.equals("A")) {
										int PwdExpCnt = 0;
										validatePwdExpQry = "select to_number(abs(LAST_PASSWD_CHANGE - sysdate)) from USER_LOGIN_CREDENTIALS where LOGIN_USER_ID=?";

										pwdExpPstmt = connection.prepareStatement(validatePwdExpQry);
										pwdExpPstmt.setString(1, this.userid);
										pwdExpRs = pwdExpPstmt.executeQuery();
										if (pwdExpRs.next()) {
											PwdExpCnt = pwdExpRs.getInt(1);
										}
										this.logger.debug("PwdExpCnt [" + PwdExpCnt + "]");

										DBUtils.closeResultSet(pwdExpRs);
										DBUtils.closePreparedStatement(pwdExpPstmt);
										this.logger.debug("Inside Else For Getting Menus..");
										this.menuJSON = getMenuData(connection);
										this.menuMap.put("menu_data", this.menuJSON);
										this.menuMap.put("userid", this.userid);
										this.menuMap.put("userStatus", userStatus);
										this.menuMap.put("LoginEntity", this.loginEntity);
										this.menuMap.put("UserLevel", this.userLevel);
										this.menuMap.put("Location", this.location);
										this.menuMap.put("links_pid_query", this.pidMenuQuery);
										this.menuMap.put("LoginTime", this.loginTime);
										this.menuMap.put("userGroup", this.userGroup);

										PasswordActivate(this.userid, userStatus, "UPDATECNT");
										updateLastLogin(userid, sessionId);

									} else if (userStatus.equals("F")) {
										this.menuMap.put("userStatus", userStatus);
										this.menuMap.put("userid", this.userid);
										this.menuMap.put("userStatus", userStatus);
										this.menuMap.put("LoginEntity", this.loginEntity);
										this.menuMap.put("UserLevel", this.userLevel);
										this.menuMap.put("Location", this.location);
										this.menuMap.put("links_pid_query", this.pidMenuQuery);
										this.menuMap.put("LoginTime", this.loginTime);
										this.menuMap.put("userGroup", this.userGroup);
									} else if (userStatus.equals("N")) {
										this.responseDTO.addError("User Authorization not done.");
									} else if (userStatus.equals("L")) {
										if (status == -2) {
											this.responseDTO.addError(
													"Please wait until 30 minutes for Re-activate Your Login.");
										} else {
											this.responseDTO.addError("User Blocked for 30 minutes.");
										}
									} else {
										this.responseDTO.addError(
												"User Got Deactivated, Please activate in order to process the login.");
									}
									this.responseDTO.setData(this.menuMap);
								}
							} else {
								passQry = "select ULC.USER_LOCK_TIME,UI.USER_STATUS from USER_LOGIN_CREDENTIALS ULC,"
										+ "USER_INFORMATION UI where UI.COMMON_ID=ULC.COMMON_ID and ULC.LOGIN_USER_ID=?";

								pstmt = connection.prepareStatement(passQry);
								pstmt.setString(1, this.userid);
								rs = pstmt.executeQuery();
								if (rs.next()) {
									lockTIme = rs.getString(1);
									userStatus = rs.getString(2);
								}
								this.logger.debug("LockTime [" + lockTIme + "] UserStatus [" + userStatus + "]");

								DBUtils.closeResultSet(rs);
								DBUtils.closePreparedStatement(pstmt);
								if ((lockTIme != null) && (userStatus.equals("L"))) {
									status = PasswordActivate(this.userid, userStatus, "INVALID");
									this.logger.debug("The User Status is [" + status + "]");
									if (status == -2) {
										this.responseDTO
												.addError("Please wait until 30 minits for Re-activate Your Login.");
									}
									if (status == -1) {
										this.responseDTO.addError("Invalid User Id / Password.");
									}
								} else {
									userLockProc = "{call UserLockingProc(?,?)}";
									callableStatement = connection.prepareCall(userLockProc);
									callableStatement.setString(1, this.userid);
									callableStatement.registerOutParameter(2, 4);
									callableStatement.executeUpdate();
									wrongPwdCnt = callableStatement.getInt(2);
									this.logger.debug("Invalid Password Count wrongPwdCnt [" + wrongPwdCnt + "]");

									DBUtils.closeCallableStatement(callableStatement);
									if (wrongPwdCnt >= 6) {
										this.logger.debug("DbPassword [" + this.dbPassword + "]");
										this.responseDTO.addError(
												"User Account Blocked For 30 Minits. Please try with correct User Id and Password.");
									} else {
										this.logger.debug("DbPassword [" + this.dbPassword + "]");
										this.responseDTO.addError("Invalid User Id / Password.");
									}
								}
							}
						} catch (Exception e) {
							this.responseDTO.addError("Internal Error Occured At Login.Please re-check and try again.");
						}
					} else {
						this.responseDTO.addError("User Authentication not configured.");
					}
				} else {
					this.responseDTO.addError("User Doesn't belong to Amur Group" + " .Please re-check and try again.");
				}
			}
		} catch (SQLException e2) {
			this.responseDTO.addError("Unable to connect to DB.");
			this.logger.debug("DB connection is null, please check the network configurations. Excpetion is ["
					+ e2.getMessage() + "]");
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.debug("Internal Error Occured. the exception is [" + e.getMessage() + "]");
			this.responseDTO.addError("Internal Error Occured.Please re-check and try again.");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(pwdExpRs);
			DBUtils.closeResultSet(validateRS);
			DBUtils.closeResultSet(validateUserRS);
			DBUtils.closeResultSet(userGrpRs);
			DBUtils.closeResultSet(systemStatusRs);
			DBUtils.closeResultSet(systemRs);
			DBUtils.closeResultSet(rs);

			DBUtils.closePreparedStatement(userGrpStmt);
			DBUtils.closePreparedStatement(userSystemStmt);
			DBUtils.closePreparedStatement(userSystemStatusStmt);
			DBUtils.closePreparedStatement(userSysStatusUpdateStmt);
			DBUtils.closePreparedStatement(validatePstmt);
			DBUtils.closePreparedStatement(validateUserPstmt);
			DBUtils.closePreparedStatement(pwdExpPstmt);
			DBUtils.closePreparedStatement(pstmt);

			DBUtils.closeCallableStatement(callableStatement);

			DBUtils.closeConnection(connection);

			this.menuJSON = null;
			userStatus = null;
			lockTIme = null;
			validateSystem = null;
			validateAppl = null;
			userStatus = null;
			lockTIme = null;
			validateSystem = null;
			validateAppl = null;
			userSystemStatusQry = null;
			token = null;
			authValue = null;
			userdetails = null;
			updateStatus = null;
			userData = null;
			validateQry = null;
			validatePwdExpQry = null;
			passQry = null;
			userLockProc = null;
		}
		return this.responseDTO;
	}

	public static int PasswordActivate(String userId, String userStatus, String flag) {
		Connection conn = null;
		CallableStatement callableStatement = null;
		int status = 0;
		try {
			conn = conn == null ? DBConnector.getConnection() : conn;

			String userLockProc = "{call UserLockingCheckingProc(?,?,?,?)}";
			callableStatement = conn.prepareCall(userLockProc);
			callableStatement.setString(1, userId);
			callableStatement.setString(2, userStatus);
			callableStatement.setString(3, flag);
			callableStatement.registerOutParameter(4, 4);
			callableStatement.executeUpdate();
			status = callableStatement.getInt(4);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(conn);
		}
		return status;
	}

	public JSONObject getMenuData(Connection connection) throws Exception {
		JSONObject jres = null;
		HashMap<String, Object> map = null;
		List<CevaCommonMenuBean> menuAl = null;

		CevaCommonMenuBean menuBean = null;
		String menuQry = null;

		int count = 0;
		String toPrepStmt = null;

		PreparedStatement menuStmt = null;
		ResultSet menuRS = null;
		try {
			jres = new JSONObject();
			map = new HashMap();
			menuAl = new ArrayList();

			this.logger.info(
					"userGroup [" + this.userGroup + "] ApplName[" + this.applName + "] Userid [" + this.userid + "]");

			menuQry = "select count(*) from user_linked_action where upper(user_id)=?";

			menuStmt = connection.prepareStatement(menuQry);
			menuStmt.setString(1, this.userid.toUpperCase());
			menuRS = menuStmt.executeQuery();
			if (menuRS.next()) {
				count = menuRS.getInt(1);
			}
			DBUtils.closeResultSet(menuRS);
			DBUtils.closePreparedStatement(menuStmt);

			this.logger.info("The Query count is [" + count + "]");
			if (count == 0) {
				menuQry =

						"Select distinct ULA.ID,ULA.NAME,ULA.PID,(select action from USER_ACTION_LINKS where UPPER(NAME)=UPPER(ULA.NAME)),'Y','"
								+ this.applName
								+ "',ULA.title,(SELECT HASCHILD FROM USER_ACTION_LINKS WHERE ID = ULA.ID ),(SELECT ISCHILD FROM USER_ACTION_LINKS WHERE ID = ULA.ID ) from USER_LINKED_ACTION ULA  where UPPER(ULA.GROUP_ID)=?  and  "
								+ "ULA.ID in (select distinct id from USER_ACTION_LINKS where alias_name is null  )   "
								+ "and " + "ULA.USER_ID is null order by to_number(ULA.ID) ";
				toPrepStmt = this.userGroup.toUpperCase();
			} else {
				menuQry =

						"Select distinct ULA.ID,ULA.NAME,ULA.PID,(select action from USER_ACTION_LINKS where UPPER(NAME)=UPPER(ULA.NAME)),'Y','"
								+ this.applName
								+ "',ULA.title,(SELECT HASCHILD FROM USER_ACTION_LINKS WHERE ID = ULA.ID ),(SELECT ISCHILD FROM USER_ACTION_LINKS WHERE ID = ULA.ID ) from USER_LINKED_ACTION ULA  "
								+ "where UPPER(ULA.USER_ID)=?  and  "
								+ "ULA.ID in (select distinct id from USER_ACTION_LINKS where alias_name is null  ) "
								+ " order by to_number(ULA.ID) ";
				toPrepStmt = this.userid.toUpperCase();
			}
			this.logger.debug("After setting the query is  [" + menuQry + "]    ");

			menuStmt = null;
			menuRS = null;

			menuStmt = connection.prepareStatement(menuQry);
			menuStmt.setString(1, toPrepStmt);

			menuRS = menuStmt.executeQuery();
			this.logger.debug("After getting data from result set [" + menuRS + "]");
			while (menuRS.next()) {
				menuBean = new CevaCommonMenuBean();
				menuBean.setId(menuRS.getInt(1));
				menuBean.setMenuName(menuRS.getString(2));
				menuBean.setParentMenu(menuRS.getString(3));
				menuBean.setMenuaction(menuRS.getString(4));
				menuBean.setActionstatus(menuRS.getString(5));
				menuBean.setApplName(menuRS.getString(6));
				menuBean.setTitle(menuRS.getString(7));
				menuBean.setHasChild(menuRS.getString(8));
				menuBean.setIsChild(menuRS.getString(9));
				menuAl.add(menuBean);
			}
			DBUtils.closeResultSet(menuRS);
			DBUtils.closePreparedStatement(menuStmt);

			map.put("menudata", menuAl);

			menuQry = "select count(*) from user_linked_action where upper(user_id)=?";

			menuStmt = null;
			menuRS = null;

			menuStmt = connection.prepareStatement(menuQry);
			menuStmt.setString(1, this.userid.toUpperCase());
			menuRS = menuStmt.executeQuery();
			if (menuRS.next()) {
				count = menuRS.getInt(1);
			}
			DBUtils.closeResultSet(menuRS);
			DBUtils.closePreparedStatement(menuStmt);

			this.logger.debug("Count for checking links [" + count + "]");
			if (count > 0) {
				this.pidMenuQuery = "select (select alias_name from USER_ACTION_LINKS where name=ula.NAME),ula.CHECKED from user_linked_action ula where upper(ula.pid)=? and upper(ula.user_id)=?";
			} else {
				this.pidMenuQuery = "select (select alias_name from USER_ACTION_LINKS where name=ula.NAME),ula.CHECKED from user_linked_action ula  where ula.pid=? and upper(ula.group_id) in (select upper(user_groups) from  user_information where common_id in (select common_id from user_login_credentials where  upper(login_user_id)= ?)) and ula.user_id is null";
			}
			this.logger.debug("Inside  finally map[ " + map + "]");

			jres.putAll(map);
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			this.logger.debug("GetMenuData got Exception [" + e.getMessage() + "]");
			jres.putAll(new HashMap());
		} finally {
			DBUtils.closeResultSet(menuRS);
			DBUtils.closePreparedStatement(menuStmt);
			DBUtils.closeConnection(connection);
			map = null;
			menuAl = null;
			menuBean = null;
			menuQry = null;
			toPrepStmt = null;
		}
		this.logger.debug("Result MenuJSON object from JRES[" + jres + "]");
		return jres;
	}

	public ResponseDTO changePassword(RequestDTO requestDTO) {
		HashMap<String, String> passwordMap = null;

		String userId = null;
		String password = null;
		String applName = null;

		String oldPasswordCheckQry = null;
		String prevPassword = null;
		String oldPasswordVal = null;

		CallableStatement callableStatement = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String repeatPwdStatus = null;

		String resetPwdProc = "{call changePasswordProc(?,?,?,?,?,?)}";
		try {
			this.responseDTO = new ResponseDTO();
			this.requestJSON = requestDTO.getRequestJSON();

			userId = this.requestJSON.getString("userId");
			password = this.requestJSON.getString("password");
			applName = this.requestJSON.getString("APPL_NAME");

			oldPasswordCheckQry = "SELECT PASSWORD,OLD_PASSWORDS from USER_LOGIN_CREDENTIALS where LOGIN_USER_ID=?";

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug(Boolean.valueOf("Connection is null [" + connection == null + "]"));

			pstmt = connection.prepareStatement(oldPasswordCheckQry);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				prevPassword = rs.getString(1);
				oldPasswordVal = rs.getString(2);
			}
			this.logger.debug("oldPasswordVal [" + oldPasswordVal + "]");
			this.logger.debug("PrevPassword [" + prevPassword + "]");

			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);

			passwordMap = (HashMap) PasswordValidation.maxCheck(password, prevPassword, oldPasswordVal);
			repeatPwdStatus = (String) passwordMap.get("RESPCODE");
			this.logger.debug("RepeatPwdStatus [" + repeatPwdStatus + "]");
			if (repeatPwdStatus.equals("00")) {
				oldPasswordVal = (String) passwordMap.get("OLDPWDS");
				callableStatement = connection.prepareCall(resetPwdProc);
				callableStatement.setString(1, userId);
				callableStatement.setString(2, password);
				callableStatement.setString(3, applName);
				callableStatement.setString(4, prevPassword);
				callableStatement.setString(5, oldPasswordVal);
				callableStatement.registerOutParameter(6, 4);
				callableStatement.executeUpdate();
				int resCnt = callableStatement.getInt(6);
				if (resCnt == 1) {
					this.responseDTO.addMessages("Password Changed Successfully Completed. ");
				} else if (resCnt == -1) {
					this.responseDTO.addError("User Id Doen't Belongs to Current Admin. ");
				} else {
					this.responseDTO.addError("There is an issue in Reset Password ");
				}
				DBUtils.closeCallableStatement(callableStatement);
			} else {
				this.responseDTO.addError("New Password should not be equal to one of the last 5 passwords. ");
			}
		} catch (SQLException e) {
			this.logger.debug("SQLException in changePassword [" + e.getMessage() + "]");
			this.responseDTO.addError("Internal Error Occured At Change Password.Please re-check and try again.");
		} catch (Exception e) {
			this.logger.debug("Exception in changePassword  [" + e.getMessage() + "]");
			this.responseDTO.addError("Internal Error Occured While Executing.");
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);

			passwordMap = null;

			userId = null;
			password = null;
			applName = null;

			oldPasswordCheckQry = null;
			prevPassword = null;
			oldPasswordVal = null;

			repeatPwdStatus = null;

			resetPwdProc = null;
		}
		return this.responseDTO;
	}

	private void updateLastLogin(String userid, String sessionId) {
		PreparedStatement pstmt = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			if (con == null)
				con = DBConnector.getConnection();

			pstmt = con.prepareStatement("UPDATE USER_LOGIN_CREDENTIALS SET SESSION_ID=? WHERE LOGIN_USER_ID=?");
			pstmt.setString(1, sessionId);
			pstmt.setString(2, userid);
			pstmt.executeUpdate();
			pstmt.close();
			con.commit();
		} catch (Exception ex) {
			logger.debug("error while updating..:" + ex.getLocalizedMessage());
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(con);
		}
	}

	public int getDayTxnCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = "select count(*) from cic_c2b_airtime_log where trunc(date_created)=trunc(sysdate) and status='SUCCESS'  ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int getSaleValue() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = "select nvl(round(sum(amount),2),0) from cic_c2b_airtime_log where trunc(date_created)=trunc(sysdate) and status='SUCCESS' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int weeklyTxnCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = "select count(*) from cic_c2b_airtime_log where to_char(date_created,'WW-YYYY')=to_char(current_date,'WW-YYYY') and status='SUCCESS' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int weeklySaleValue() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = "select nvl(round(sum(amount),2),0) from cic_c2b_airtime_log where to_char(date_created,'WW-YYYY')=to_char(current_date,'WW-YYYY') and status='SUCCESS' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int activePolCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = "select count(*) from cic_clients where status='CS' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int totalSaleValue() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select nvl(round(sum(amount),2),0) from cic_c2b_airtime_log where status='SUCCESS' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int idPendCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from cic_clients where status='CC' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int benIdPendCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from cic_clients c where c.status='CV' and  not exists (select 1 from CIC_CLIENT_BENEFICIARIES b where b.policy_id=c.mobile_number) ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int activeAgentCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from CIC_AGENT_MASTER where status='A' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int activationsThruAgnetCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(distinct m.policy_id) from CIC_AGENT_CLIENT_MAPPING m where exists (select 1 from cic_clients c where c.mobile_number=m.policy_id ) ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int ClnFailureCount() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from cic_clients where status='CVF' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int DailyActivations() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from cic_clients where trunc(date_created)=trunc(sysdate) ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int TotalClinets() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from cic_clients ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int MultiplePolicies() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select count(*) from (select count(*) from cic_clients where id_number is not null group by id_number having count(mobile_number)>1) ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public int totalCashSaleValue() throws SQLException {
		int totalRecords = -1;
		String sql = "";
		Connection con = null;
		con = con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;

		sql = " select round(sum(amount),2) from CIC_POLICY_TOPUP_PURCHASE_LOG where status='SUCCESS' ";

		stmt = con.prepareStatement(sql);

		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

}
