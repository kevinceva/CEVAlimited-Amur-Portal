package com.ceva.base.ceva.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.bean.BillerBean;
import com.ceva.base.common.dao.AjaxDAO;
import com.ceva.base.common.dao.BillerManagementDAO;
import com.ceva.base.common.dao.ServiceMgmtAjaxDAO;
import com.ceva.base.common.dao.SwitchUIDAO;
import com.ceva.base.common.dao.UserAjaxDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionSupport;

public class CICJsonAjaxAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(CICJsonAjaxAction.class);

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String hoffice = null;
	private String region = null;
	private String location = null;
	private String method = null;
	private String selectedSelBox = null;
	private String fillSelectBox = null;

	private String entity = null;
	private String groupId = null;
	private String userId = null;
	private String employeeNo = null;
	private String dlNo = null;

	private Map<String, String> details;
	private int finalCount = 0;

	private String dlName;
	private String dlIdnumber;
	private String status;
	private String serialNo;

	// String agencyName;
	private String accounttype;
	private String service;

	private String mobile;
	private String amount;

	private String bin;
	private String bankIndex;
	private String bankCode;
	private String message;
	private String hudumaService;
	private String billerId;
	private String customerAccount;
	private String billerType;
	private String billerCode;

	private BillerBean billerBean;
	
	private String accountNumber;

	private String user;

	private HttpSession session;

	@Override
	public String execute() throws Exception {
		logger.debug("Inside Execute Method.");
		logger.debug("Execute method [" + method + "] ");
		String result = ERROR;

		try {
			if (method.equalsIgnoreCase("searchData")) {
				result = searchData();
			} else if (method.equalsIgnoreCase("searchEntity")) {
				result = checkCountGroup();
			} else if (method.equalsIgnoreCase("searchUser")) {
				result = searchUser();
			} else if (method.equalsIgnoreCase("searchKraData")) {
				result = searchKraData();
			} else if (method.equalsIgnoreCase("searchSerial")) {
				result = searchSerial();
			} else if (method.equalsIgnoreCase("searchSerialData")) {
				result = searchSerialData();
			} else if (method.equalsIgnoreCase("checkTransactionType")) {
				result = checkTransactionType();
			} else if (method.equalsIgnoreCase("userIdStatus")) {
				result = userIdStatus();
			} else if (method.equalsIgnoreCase("searchHudumaMobile")) {
				result = searchHudumaMobile();
			} else if (method.equalsIgnoreCase("searchBin")) {
				result = searchBin();
			} else if (method.equalsIgnoreCase("checkHudumaSubService")) {
				result = checkHudumaSubService();
			} else if (method.equalsIgnoreCase("SwitchStatus")) {
				result = switchStatus();
			} else if (method.equalsIgnoreCase("searchBiller")) {
				result = billerDetails();
			}else if(method.equalsIgnoreCase("getBillerDetails")){
				result = fetchBillerDetails();
			}else if(method.equalsIgnoreCase("getBanks")){
				result = fetchBanks();
			}else if(method.equalsIgnoreCase("searchBillerAccount")){
				result = searchBillerAccount();
			}else if(method.equalsIgnoreCase("getBillerAccountDetails")){
				result = fetchBillerAccountDetails();
			}
		} catch (Exception e) {
			logger.debug("Inside execute [" + e.getMessage() + "]");
		} finally {

		}

		return result;
	}

	public String checkCountGroup() {
		logger.debug("Inside CheckCountGroup.. ");

		PreparedStatement entityPstmt = null;
		ResultSet entityRS = null;
		String queryConst = "";
		Connection connection = null;

		int grpCount = 0;
		int tempGrpCount = 0;
		try {
			connection = DBConnector.getConnection();
			queryConst = "select count(*) from user_groups where upper(group_id)=?";
			entityPstmt = connection.prepareStatement(queryConst.toUpperCase());
			entityPstmt.setString(1, groupId);
			entityRS = entityPstmt.executeQuery();

			if (entityRS.next()) {
				grpCount = entityRS.getInt(1);
			}

			entityPstmt.close();
			entityRS.close();

			queryConst = "select count(*) from user_groups_temp where upper(group_id)=? and auth_flag in ('AUP','AD') ";
			entityPstmt = connection.prepareStatement(queryConst.toUpperCase());
			entityPstmt.setString(1, groupId);
			entityRS = entityPstmt.executeQuery();

			if (entityRS.next()) {
				tempGrpCount = entityRS.getInt(1);
			}

			if (tempGrpCount > 0) {
				status = "AUTH";
				finalCount = tempGrpCount;
			} else {
				status = "NON_AUTH";
				finalCount = grpCount;
			}

			logger.debug("Got Count [" + finalCount + "]");

		} catch (Exception e) {
			logger.debug("Got error [" + e.getMessage() + "]");
			finalCount = 0;
		} finally {

			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeResultSet(entityRS);
			DBUtils.closeConnection(connection);
		}
		return SUCCESS;
	}

	public String searchKraData() {
		logger.debug("Inside SearchKraData.. ");
		logger.debug("	 Dlno [" + getDlNo() + "]");
		CallableStatement callableStatement = null;
		String queryConst = "{call getDlDetails(?,?,?,?,?)}";
		String message = "";
		Connection connection = null;

		try {
			connection = DBConnector.getConnection();
			logger.debug("	 queryConst [" + queryConst + "]");
			callableStatement = connection.prepareCall(queryConst);
			callableStatement.setString(1, getDlNo());
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			callableStatement.registerOutParameter(4, Types.VARCHAR);
			callableStatement.registerOutParameter(5, Types.VARCHAR);

			callableStatement.execute();

			logger.debug(" 	 After Executing callableStatement.");

			message = callableStatement.getString(4);
			setStatus(callableStatement.getString(5));

			logger.debug(" 	 After Executing message[" + message + "] status["
					+ callableStatement.getString(5) + "]");

			if (message.equalsIgnoreCase("SUCCESS")) {
				setDlName(callableStatement.getString(2));
				setDlIdnumber(callableStatement.getString(3));
			} else {
				setDlName("NO");
				setDlIdnumber("NO");
			}

			logger.debug(" 	After Executing query.");

		} catch (Exception e) {

			dlName = "NO";
			dlIdnumber = "NO";
			status = "";
			logger.debug("	  exception is  : " + e.getMessage());
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			queryConst = null;
			message = null;
		}

		logger.debug("   Details are : " + details);

		return SUCCESS;
	}

	public String searchSerial() {
		logger.debug("Inside SearchSerial... ");
		logger.debug("	 dlno [" + getDlNo() + "]");
		CallableStatement callableStatement = null;
		String queryConst = "{call getSerialDetails(?,?,?)}";
		String message = "";
		Connection connection = null;

		try {
			connection = DBConnector.getConnection();

			logger.debug("	 queryConst [" + queryConst + "]");
			callableStatement = connection.prepareCall(queryConst);
			callableStatement.setString(1, getSerialNo());
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			callableStatement.execute();

			logger.debug(" 	 After Executing callableStatement.");

			message = callableStatement.getString(2);
			setStatus(callableStatement.getString(3));

			logger.debug(" 	 After Executing message[" + message + "] "
					+ "status[" + callableStatement.getString(3) + "]");

			logger.debug(" 	After Executing query.");

		} catch (Exception e) {

			setStatus("NO");
			logger.debug("	  exception is  : " + e.getMessage());
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			queryConst = null;
			message = null;
		}

		logger.debug("   Details are : " + getStatus());

		return SUCCESS;
	}

	public String searchSerialData() {
		logger.debug("Inside 	SearchSerialData... ");
		logger.debug("	 dlno [" + getDlNo() + "]");
		CallableStatement callableStatement = null;
		String queryConst = "{call HudumaPkg.CHECKSERIALDATA(?,?,?,?,?)}";
		String message = "";
		Connection connection = null;

		try {
			connection = DBConnector.getConnection();

			logger.debug("QueryConst [" + queryConst + "]");
			callableStatement = connection.prepareCall(queryConst);
			callableStatement.setString(1, getSerialNo());
			callableStatement.setString(2, getUser());
			callableStatement.setString(3, getService());
			callableStatement.registerOutParameter(4, Types.VARCHAR);
			callableStatement.registerOutParameter(5, Types.VARCHAR);
			callableStatement.execute();

			logger.debug(" After Executing callableStatement.");

			message = callableStatement.getString(4);
			setStatus(callableStatement.getString(5));

			logger.debug(" 	 After Executing message[" + message + "] "
					+ "status [" + callableStatement.getString(5) + "]");

			logger.debug("After Executing query.");

		} catch (Exception e) {
			setStatus("NO");
			logger.debug(" exception is  : " + e.getMessage());
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			queryConst = null;
			message = null;
		}

		logger.debug(" Details are : " + getStatus());

		return SUCCESS;
	}

	public String searchData() {
		logger.debug("Inside SearchData.. ");
		String queryConst = "";
		logger.debug("Region [" + getRegion() + "]");
		logger.debug("Head Office [" + getHoffice() + "]");
		logger.debug("Location [" + getLocation() + "]");
		logger.debug("Selected Select Box [" + getSelectedSelBox() + "]");

		PreparedStatement entityPstmt = null;
		ResultSet entityRS = null;
		Connection connection = null;
		try {

			details = new HashMap<String, String>();
			connection = DBConnector.getConnection();

			if (getSelectedSelBox().equalsIgnoreCase("region")) {
				queryConst = "Select distinct HPO_DEPARTMENT_CODE,HPO_NAME from POSTA_BRANCH_MASTER where HPO_FLAG='HEAD' and REGION_CODE in ("
						+ getRegion() + ") ORDER BY HPO_DEPARTMENT_CODE";
				// queryConst =
				// "Select distinct HPO_DEPARTMENT_CODE,HPO_NAME from IMP_BRANCH_MASTER where HPO_FLAG='HEAD' and REGION_CODE in ("+getRegion()+") ORDER BY HPO_DEPARTMENT_CODE";
				fillSelectBox = "headOffice";
			} else if (getSelectedSelBox().equalsIgnoreCase("headOffice")) {
				queryConst = "Select distinct OFFICE_CODE,OFFICE_NAME from POSTA_BRANCH_MASTER where HPO_FLAG is null  and HPO_DEPARTMENT_CODE in ("
						+ getHoffice() + ") ORDER BY OFFICE_CODE";
				// queryConst =
				// "Select distinct OFFICE_CODE,OFFICE_NAME from IMP_BRANCH_MASTER where HPO_FLAG is null  and HPO_DEPARTMENT_CODE in ("+getHoffice()+") ORDER BY OFFICE_CODE";
				fillSelectBox = "Location";
			} else if (getSelectedSelBox().equalsIgnoreCase("Location")) {
				// queryConst =
				// "select distinct system_user_id,login_user_id from user_id_mapping where system_user_id in (select user_id from user_master where upper(branch_location) in ("
				// + getLocation().toUpperCase().trim() + "))";
				queryConst = "select LOGIN_USER_ID from USER_LOGIN_CREDENTIALS where COMMON_ID in "
						+ "(select COMMON_ID from USER_INFORMATION where upper(location) in ("
						+ getLocation() + ") and upper(USER_LEVEL)='USER')";
				fillSelectBox = "userid";
			}

			logger.debug("QueryConst [" + queryConst + "]");
			entityPstmt = connection.prepareStatement(queryConst);
			entityRS = entityPstmt.executeQuery();

			while (entityRS.next()) {

				if (fillSelectBox.equalsIgnoreCase("Location")) {

					details.put(entityRS.getString(1), entityRS.getString(1)
							+ "-" + entityRS.getString(2));
				} else if (fillSelectBox.equalsIgnoreCase("userid")) {

					details.put(entityRS.getString(1), entityRS.getString(1));
				} else {
					details.put(entityRS.getString(1), entityRS.getString(1)
							+ "-" + entityRS.getString(2));
				}
			}

		} catch (Exception e) {
			setStatus("NO");
			logger.debug(" exception is  : " + e.getMessage());
		} finally {

			DBUtils.closePreparedStatement(entityPstmt);
			DBUtils.closeResultSet(entityRS);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		logger.debug("Details are : " + details);

		return SUCCESS;
	}

	public String searchHudumaMobile() {
		logger.debug("Inside 	[searchHudumaMobile]");
		String queryConst = "";
		logger.debug("	[searchHudumaMobile] mobile [" + getMobile() + "]");
		logger.debug("	[searchHudumaMobile] amount [" + getAmount() + "]");

		CallableStatement callable = null;
		Connection connection = null;
		try {
			details = new HashMap<String, String>();
			connection = DBConnector.getConnection();

			queryConst = "{call HudumaPkg.CHECKMOBILESERVICES(?,?,?,?)}";

			logger.debug("  	[searchHudumaMobile] queryConst [" + queryConst
					+ "]");
			callable = connection.prepareCall(queryConst);
			callable.setString(1, getMobile());
			callable.setString(2, getAmount());
			callable.registerOutParameter(3, Types.VARCHAR);
			callable.registerOutParameter(4, Types.VARCHAR);
			callable.execute();

			logger.debug("  [searchHudumaMobile]  message["
					+ callable.getString(3) + "] Status ["
					+ callable.getString(4) + "] ");

			setMessage(callable.getString(3));
			setStatus(callable.getString(4));

		} catch (Exception e) {
			setStatus("NO");
			logger.debug(" exception is  : " + e.getMessage());
		} finally {

			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}
		return SUCCESS;
	}

	
	public String searchBin() {
		logger.debug("Inside searchBin... ");
		String queryConst = "";
		logger.debug("BIN        [" + getBin() + "]");
		logger.debug("Bank Index [" + getBankIndex() + "]");
		logger.debug("Bank Code  [" + getBankCode() + "]");

		CallableStatement callable = null;
		Connection connection = null;
		try {
			connection = DBConnector.getConnection();

			queryConst = "{call HudumaPkg.CHECKBIN(?,?,?,?,?)}";

			logger.debug(" queryConst [" + queryConst + "]");
			callable = connection.prepareCall(queryConst);
			callable.setString(1, getBin());
			callable.setString(2, getBankIndex());
			callable.setString(3, getBankCode());
			callable.registerOutParameter(4, Types.VARCHAR);
			callable.registerOutParameter(5, Types.VARCHAR);
			callable.execute();

			logger.debug("message [" + callable.getString(4) + "] Status ["
					+ callable.getString(5) + "] ");

			setStatus(callable.getString(5));
			setMessage(callable.getString(4));

		} catch (Exception e) {
			setStatus("NO");
			logger.debug("Exception is [" + e.getMessage() + "]");
		} finally {

			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}

	public String searchUser() {
		logger.debug("Inside SearchUser.. ");
		String queryConst = "";
		CallableStatement callable = null;
		Connection connection = null;
		try {

			connection = DBConnector.getConnection();

			queryConst = "{call HudumaPkg.CHECKUSER(?,?,?,?)}";

			logger.debug("QueryConst [" + queryConst + "]");
			callable = connection.prepareCall(queryConst);
			callable.setString(1, getUserId());
			callable.setString(2, getEmployeeNo());
			callable.registerOutParameter(3, Types.VARCHAR);
			callable.registerOutParameter(4, Types.VARCHAR);
			callable.execute();

			logger.debug("  message [" + callable.getString(3) + "]"
					+ " Status [" + callable.getString(4) + "] ");

			setMessage(callable.getString(3));
			setStatus(callable.getString(4));

		} catch (Exception e) {
			logger.debug(" Got error [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}

	public String checkTransactionType() {
		logger.debug("Inside CheckTransactionType [" + accounttype + "]");
		AjaxDAO ictAdminDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			logger.debug("Accounttype [" + accounttype + "]");
			logger.debug("Service     [" + service + "]");

			requestJSON.put(CevaCommonConstants.ACCOUNT_TYPE, accounttype);
			requestJSON.put(CevaCommonConstants.SERVICE_TYPE, service);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			ictAdminDAO = new AjaxDAO();
			responseDTO = ictAdminDAO.checkTransactionType(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.USER_CHECK_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception is  [" + e.getMessage() + "]");
		} finally {
			ictAdminDAO = null;
			errors = null;
		}

		return SUCCESS;
	}

	public String userIdStatus() {
		logger.debug("Inside UserIdStatus... ");
		ArrayList<String> errors = null;
		UserAjaxDAO userDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestJSON.put("User_Id", userId);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			userDAO = new UserAjaxDAO();
			responseDTO = userDAO.checkuserId(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"RESULT_COUNT_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception is  [" + e.getMessage() + "]");
		} finally {
			userDAO = null;
			errors = null;
		}

		return SUCCESS;
	}

	public String checkHudumaSubService() {
		logger.debug("Inside Service [" + hudumaService + "]");
		ArrayList<String> errors = null;
		ServiceMgmtAjaxDAO ajaxDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.SERVICE_CODE, hudumaService);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			ajaxDAO = new ServiceMgmtAjaxDAO();
			responseDTO = ajaxDAO.getHudumaSubService(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SERVICE_LIST);
				logger.debug("[responseJSON:::::" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception is  [" + e.getMessage() + "]");
		} finally {
			ajaxDAO = null;
			errors = null;
		}
		return SUCCESS;
	}

	public String switchStatus() {
		logger.debug("Inside SwitchStatus.. ");
		SwitchUIDAO dao = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			dao = new SwitchUIDAO();
			responseDTO = dao.getBankSwitchStatus(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SWITCH_BANK_DATA);
				logger.debug("Response JSON [" + responseJSON + "]");

			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

			}
		} catch (Exception e) {
			logger.debug("Exception is  [" + e.getMessage() + "]");
		} finally {
			dao = null;
			errors = null;
		}
		return SUCCESS;
	}

	public String billerDetails() {
		logger.debug("Inside Biller Details.. ");
		BillerManagementDAO dao = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession(false);

			requestJSON.put("billerid", billerId);
			requestJSON.put("customerAccount", customerAccount);
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestDTO.setRequestJSON(requestJSON);

			dao = new BillerManagementDAO();
			responseDTO = dao.getCustomerInDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				billerBean = (BillerBean) responseDTO.getData()
						.get("bank_data");

				responseJSON = (JSONObject) responseDTO.getData().get(
						"acc_data");

				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in BillerDetails [" + e.getMessage() + "]");
		} finally {
			dao = null;
			errors = null;
		}
		return SUCCESS;

	}
	
	
	public String fetchBillerDetails() {
		logger.debug("Inside Biller Details.. Biller Type:"+billerType);
		BillerManagementDAO dao = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession(false);

			requestJSON.put("billerType", billerType);

			requestDTO.setRequestJSON(requestJSON);

			dao = new BillerManagementDAO();
			responseDTO = dao.getBillerDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("BILLER_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in BillerDetails [" + e.getMessage() + "]");
		} finally {
			dao = null;
			errors = null;
		}
		return SUCCESS;

	}

	public String fetchBanks() {
		logger.debug("Inside fetch bank details.. Banks:");
		BillerManagementDAO dao = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession(false);

			requestDTO.setRequestJSON(requestJSON);

			dao = new BillerManagementDAO();
			responseDTO = dao.getBanks(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("BILLER_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
			}
		} catch (Exception e) {
			logger.debug("Exception in BillerDetails [" + e.getMessage() + "]");
		} finally {
			dao = null;
			errors = null;
		}
		return SUCCESS;

	}
	
	
	
	public String searchBillerAccount() {
		logger.debug("Inside searchBillerAccount... ");
		String queryConst = "";
		logger.debug("Biller Id        [" + getBillerId() + "]");
		logger.debug("Account Number [" + getAccountNumber() + "]");

		CallableStatement callable = null;
		Connection connection = null;
		try {
			/*connection = DBConnector.getConnection();

			queryConst = "{call HudumaPkg.CHECKBIN(?,?,?,?,?)}";

			logger.debug(" queryConst [" + queryConst + "]");
			callable = connection.prepareCall(queryConst);
			callable.setString(1, getBin());
			callable.setString(2, getBankIndex());
			callable.setString(3, getBankCode());
			callable.registerOutParameter(4, Types.VARCHAR);
			callable.registerOutParameter(5, Types.VARCHAR);
			callable.execute();

			logger.debug("message [" + callable.getString(4) + "] Status ["
					+ callable.getString(5) + "] ");*/

			/*setStatus(callable.getString(5));
			setMessage(callable.getString(4));*/
			
			setStatus("NOTFOUND");
			setMessage("SUCCESS");

		} catch (Exception e) {
			setStatus("NO");
			logger.debug("Exception is [" + e.getMessage() + "]");
		} finally {

			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}

	
	public String fetchBillerAccountDetails() {
		
		logger.debug("Inside fetchBillerAccountDetails... ");
		String queryConst = "";
		logger.debug("Biller Code        [" + getBillerCode() + "]");

		responseJSON = new JSONObject();
		
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = null;
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		
		try {
			connection = DBConnector.getConnection();

			queryConst = "select ACCOUNT_NUMBER,ACCOUNT_NAME from BILLER_ACCOUNT_MASTER where BILLER_ID=? ";
			preparedStatement = connection.prepareStatement(queryConst.toUpperCase());
			preparedStatement.setString(1, getBillerCode());
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				jsonObject = new JSONObject();
				jsonObject.put("val", resultSet.getString(1));
				jsonObject.put("key", resultSet.getString(2));
				jsonArray.add(jsonObject);
			}
			
			responseJSON.put("BILLER_ACCT_DATA", jsonArray);
			
			resultSet.close();
			preparedStatement.close();
			
			
			queryConst = "select NAME,COMM_TYPE,AMOUNT/100 from BILLER_REGISTRATION where BILLER_ID=? ";
			preparedStatement = connection.prepareStatement(queryConst.toUpperCase());
			preparedStatement.setString(1, getBillerCode());
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()){
				responseJSON.put("BILLER_ABBR", resultSet.getString(1));
				responseJSON.put("COMM_TYPE", resultSet.getString(2));
		        responseJSON.put("COMM_AMT", resultSet.getString(3));
			}
			
			logger.debug("Response JSON::"+responseJSON);
			
			resultSet.close();
			preparedStatement.close();
			
			
			setStatus("NOTFOUND");
			setMessage("SUCCESS");

		} catch (Exception e) {
			setStatus("NO");
			logger.debug("Exception is [" + e.getMessage() + "]");
		} finally {

			DBUtils.closePreparedStatement(preparedStatement);
			DBUtils.closeResultSet(resultSet);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}

	
	
	public String getHoffice() {
		return hoffice;
	}

	public void setHoffice(String hoffice) {
		this.hoffice = hoffice;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}

	public String getFillSelectBox() {
		return fillSelectBox;
	}

	public void setFillSelectBox(String fillSelectBox) {
		this.fillSelectBox = fillSelectBox;
	}

	public String getSelectedSelBox() {
		return selectedSelBox;
	}

	public void setSelectedSelBox(String selectedSelBox) {
		this.selectedSelBox = selectedSelBox;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getFinalCount() {
		return finalCount;
	}

	public void setFinalCount(int finalCount) {
		this.finalCount = finalCount;
	}

	public String getDlNo() {
		return dlNo;
	}

	public void setDlNo(String dlNo) {
		this.dlNo = dlNo;
	}

	public String getDlName() {
		return dlName;
	}

	public void setDlName(String dlName) {
		this.dlName = dlName;
	}

	public String getDlIdnumber() {
		return dlIdnumber;
	}

	public void setDlIdnumber(String dlIdnumber) {
		this.dlIdnumber = dlIdnumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public JSONObject getRequestJSON() {
		return requestJSON;
	}

	public void setRequestJSON(JSONObject requestJSON) {
		this.requestJSON = requestJSON;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public RequestDTO getRequestDTO() {
		return requestDTO;
	}

	public void setRequestDTO(RequestDTO requestDTO) {
		this.requestDTO = requestDTO;
	}

	public ResponseDTO getResponseDTO() {
		return responseDTO;
	}

	public void setResponseDTO(ResponseDTO responseDTO) {
		this.responseDTO = responseDTO;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getBankIndex() {
		return bankIndex;
	}

	public void setBankIndex(String bankIndex) {
		this.bankIndex = bankIndex;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getHudumaService() {
		return hudumaService;
	}

	public void setHudumaService(String hudumaService) {
		this.hudumaService = hudumaService;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public BillerBean getBillerBean() {
		return billerBean;
	}

	public void setBillerBean(BillerBean billerBean) {
		this.billerBean = billerBean;
	}

	public String getCustomerAccount() {
		return customerAccount;
	}

	public void setCustomerAccount(String customerAccount) {
		this.customerAccount = customerAccount;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getBillerType() {
		return billerType;
	}

	public void setBillerType(String billerType) {
		this.billerType = billerType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBillerCode() {
		return billerCode;
	}

	public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}

	
}
