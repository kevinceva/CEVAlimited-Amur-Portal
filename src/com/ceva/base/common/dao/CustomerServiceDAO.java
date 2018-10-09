package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.ceva.util.DateUtil;

public class CustomerServiceDAO {

	Logger logger = Logger.getLogger(CustomerServiceDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	Connection connection = null;

	public ResponseDTO retriveNonPersonalisedDetails(RequestDTO requestDTO) {

		logger.debug("inside [retriveNonPersonalisedDetails]");
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();
		requestJSON = requestDTO.getRequestJSON();

		HashMap<String, Object> limitDataMap = new HashMap<String, Object>();
		JSONObject resultJson = new JSONObject();
		JSONArray cardTypeJSONArray = new JSONArray();
		JSONArray branchJSONArray = new JSONArray();
		PreparedStatement cardTypePstmt = null;
		ResultSet cardTypeRS = null;
		PreparedStatement branchPstmt = null;
		ResultSet branchRS = null;

		String cardTypeQry = "select PRD_CODE||'-'||PRD_DESC,PRD_CODE from PRD_MASTER";
		try {
			connection = DBConnector.getConnection();
			logger.debug("[retriveNonPersonalisedDetails][connection:::"
					+ connection + "]");
			cardTypePstmt = connection.prepareStatement(cardTypeQry);
			cardTypeRS = cardTypePstmt.executeQuery();
			JSONObject json = null;
			while (cardTypeRS.next()) {
				json = new JSONObject();
				json.put("key", cardTypeRS.getString(1));
				json.put("val", cardTypeRS.getString(2));

				cardTypeJSONArray.add(json);
			}
			logger.debug("[retriveNonPersonalisedDetails][cardTypeJSONArray:::"
					+ cardTypeJSONArray + "]");
			String branchQry = "select OFFICE_CODE from CEVA_BRANCH_MASTER";
			branchPstmt = connection.prepareStatement(branchQry);
			branchRS = branchPstmt.executeQuery();
			while (branchRS.next()) {
				json = new JSONObject();
				json.put("key", branchRS.getString(1));
				json.put("val", branchRS.getString(1));
				branchJSONArray.add(json);
			}
			logger.debug("[retriveNonPersonalisedDetails][branchJSONArray:::"
					+ branchJSONArray + "]");

			resultJson.put("CARD_TYPE", cardTypeJSONArray);
			resultJson.put("BRANCH_CODE", branchJSONArray);

			limitDataMap.put("CARD_MANAGEMENT_DETAILS", resultJson);
			responseDTO.setData(limitDataMap);
			logger.debug("[retriveNonPersonalisedDetails][responseDTO:::"
					+ responseDTO + "]");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(cardTypePstmt);
			DBUtils.closePreparedStatement(branchPstmt);
			DBUtils.closeResultSet(cardTypeRS);
			DBUtils.closeResultSet(branchRS);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

	public ResponseDTO insertCustomerDetails(RequestDTO requestDTO) {
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		Connection connection = null;
		logger.debug("inside [InsertCustomerDetails]");
		requestJSON = requestDTO.getRequestJSON();
		logger.debug("[InsertCustomerDetails][requestJSON:::" + requestJSON
				+ "]");
		String firstName = null;
		String middleName = null;
		String lastName = null;
		String dateOfBirth = null;
		String securityQuestion = null;
		String securityAnswer = null;
		String gender = null;
		String customerNumber = null;
		String addressLine1 = null;
		String addressLine2 = null;
		// String suburb=null;
		String county = null;
		String postalCode = null;
		String country = null;
		String phoneNo1 = null;
		String phoneNo2 = null;
		String mobileNumber = null;
		String email = null;
		String cardType = null;
		String cardNumber = null;
		String accountNumber = null;
		String branchNumber = null;
		String stafId = null;
		String address = null;
		String psuburb = null;
		String pstate = null;
		String postCode = null;
		String pcountry = null;
		String documentType = null;
		String application = null;
		String randomNum = null;
		String idNumber = null;
		String expiryDate = null;
		String password = null;

		String makerId = requestJSON.getString("MAKER_ID");
		firstName = requestJSON.getString("firstName");
		middleName = requestJSON.getString("middleName");
		lastName = requestJSON.getString("lastName");
		dateOfBirth = requestJSON.getString("dateOfBirth");
		securityQuestion = requestJSON.getString("securityQuestion");
		securityAnswer = requestJSON.getString("securityAnswer");
		gender = requestJSON.getString("gender");
		customerNumber = requestJSON.getString("customerNumber");
		addressLine1 = requestJSON.getString("addressLine1");
		addressLine2 = requestJSON.getString("addressLine2");
		county = requestJSON.getString("county");
		postalCode = requestJSON.getString("postalCode");
		country = requestJSON.getString("country");
		phoneNo1 = requestJSON.getString("phoneNo1");
		phoneNo2 = requestJSON.getString("phoneNo2");
		mobileNumber = requestJSON.getString("mobileNumber");
		email = requestJSON.getString("email");
		cardType = requestJSON.getString("cardType");
		cardNumber = requestJSON.getString("cardNumber");
		accountNumber = requestJSON.getString("accountNumber");
		branchNumber = requestJSON.getString("branchNumber");
		stafId = requestJSON.getString("stafId");
		address = requestJSON.getString("address");
		psuburb = requestJSON.getString("psuburb");
		pstate = requestJSON.getString("pstate");
		postCode = requestJSON.getString("postCode");
		pcountry = requestJSON.getString("pcountry");
		documentType = requestJSON.getString("documentType");
		application = requestJSON.getString("application");
		randomNum = requestJSON.getString("randomNum");
		idNumber = requestJSON.getString("idNumber");
		expiryDate = requestJSON.getString("expiryDate");

		password = requestJSON.getString("password");
		CallableStatement callableStatement = null;
		String insertProc = "{call INSERT_CARDMANAGEMENTDETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		try {

			/*
			 * SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			 * java.util.Date parsed = format.parse(dateOfBirth); java.sql.Date
			 * dob = new java.sql.Date(parsed.getTime());
			 * 
			 * java.util.Date parsed2 = format.parse(expiryDate); java.sql.Date
			 * expdate = new java.sql.Date(parsed2.getTime());
			 */

			connection = DBConnector.getConnection();
			callableStatement = connection.prepareCall(insertProc);
			callableStatement.setString(1, firstName);
			callableStatement.setString(2, middleName);
			callableStatement.setString(3, lastName);
			// callableStatement.setDate(4, dob);
			callableStatement.setString(4, dateOfBirth);
			callableStatement.setString(5, securityQuestion);
			callableStatement.setString(6, securityAnswer);
			callableStatement.setString(7, gender);
			callableStatement.setString(8, customerNumber);
			callableStatement.setString(9, addressLine1);
			callableStatement.setString(10, addressLine2);
			// callableStatement.setString(11, suburb);
			callableStatement.setString(11, county);
			callableStatement.setString(12, postalCode);
			callableStatement.setString(13, country);
			callableStatement.setString(14, phoneNo1);
			callableStatement.setString(15, phoneNo2);
			callableStatement.setString(16, mobileNumber);
			callableStatement.setString(17, email);
			callableStatement.setString(18, cardType);
			callableStatement.setString(19, cardNumber);
			callableStatement.setString(20, accountNumber);
			callableStatement.setString(21, branchNumber);
			callableStatement.setString(22, stafId);
			callableStatement.setString(23, address);
			callableStatement.setString(24, psuburb);
			callableStatement.setString(25, pstate);
			callableStatement.setString(26, postCode);
			callableStatement.setString(27, pcountry);
			callableStatement.setString(28, documentType);
			callableStatement.setString(29, idNumber);
			// callableStatement.setDate(30, expdate);
			callableStatement.setString(30, expiryDate);
			callableStatement.setString(31, makerId);

			callableStatement.registerOutParameter(32, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(33, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(34, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(35, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(36, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(37, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(38, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(39, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(40, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(41, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(42, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(43, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(44, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(45, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(46, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(47, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(48, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(49, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(50, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(51, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(52, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(53, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(54, java.sql.Types.VARCHAR);

			callableStatement.setString(55, application);
			callableStatement.setString(56, randomNum);
			callableStatement.setString(57, password);
			callableStatement.executeUpdate();
			int resCnt = callableStatement.getInt(54);
			logger.debug("  resultCnt from DB::::"
					+ resCnt);
			if (resCnt == 1) {
				responseDTO
						.addMessages("Customer Details Inserted Successfully!");
			} else if (resCnt == -1) {

				responseDTO.addError(" Already Exists. ");
			} else {
				responseDTO.addError("Customer Details Creation failed.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

	/*
	 * New Card Account Starts
	 */

	public ResponseDTO newCardAccountRecords(RequestDTO requestDTO) {
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();

		logger.debug("inside [newCardAccountRecords]");
		HashMap<String, Object> cardDataMap = new HashMap<String, Object>();
		requestJSON = requestDTO.getRequestJSON();
		JSONObject custJSON = new JSONObject();
		JSONArray cardJSONArray = new JSONArray();
		PreparedStatement newcardpsmt = null;
		ResultSet cardRS = null;
		logger.debug("[newCardAccountRecords][requestJSON:::" + requestJSON
				+ "]");
		String cardNumber = requestJSON.getString("cardNumber");

		try {
			String getcustQry = "select a.F_NAME||' '||M_NAME,a.DOB,MOBILE_NO,a.SEC_QUES,a.SEC_QUES_ANS,a.EMAIL_ID,a.PASSPORT_NO from CUST_MASTER a, CRD_MASTER b where CARD_NO=? and b.CIN=a.CIN ";
			connection = DBConnector.getConnection();
			logger.debug("[newCardAccountRecords][connection:::" + connection
					+ "]");
			newcardpsmt = connection.prepareStatement(getcustQry);
			newcardpsmt.setString(1, cardNumber);
			cardRS = newcardpsmt.executeQuery();
			if (cardRS.next()) {
				custJSON = new JSONObject();
				custJSON.put("name", cardRS.getString(1));
				custJSON.put("dob", cardRS.getString(2));
				custJSON.put("mobileNumber", cardRS.getString(3));
				custJSON.put("securityQuestion", cardRS.getString(4));
				custJSON.put("securityAnswer", cardRS.getString(5));
				custJSON.put("email", cardRS.getString(6));
				custJSON.put("passportno", cardRS.getString(7));
			}
			cardRS.close();
			newcardpsmt.close();

			String getCardQry = "select pm.prd_desc,am.prd_Code,am.card_acct_ccy, decode(am.card_acct_status, 'O','Active', 'C', 'InActive')"
					+ "from acct_master am, prd_master pm where card_acct in(select card_acct from crd_acct_link where card_no=?) and am.prd_code=pm.prd_code";
			newcardpsmt = connection.prepareStatement(getCardQry);
			newcardpsmt.setString(1, cardNumber);
			cardRS = newcardpsmt.executeQuery();
			JSONObject json = null;
			while (cardRS.next()) {
				json = new JSONObject();
				json.put("cardAccount", cardRS.getString(1));
				json.put("prodCode", cardRS.getString(2));
				json.put("currency", cardRS.getString(3));
				json.put("cardStatus", cardRS.getString(4));
				json.put("cardNumber", cardNumber);
				cardJSONArray.add(json);
			}
			cardRS.close();
			logger.debug("[newCardAccountRecords][cardJSONArray:::"
					+ cardJSONArray + "]");

			JSONArray prodJsonArray = generateDropdowns("select PRD_CRCY, PRD_CODE ||'-'||PRD_DESC  from PRD_MASTER");
			custJSON.put("CARD_INFO", cardJSONArray);
			custJSON.put("PRD_DROP", prodJsonArray);
			cardDataMap.put("CUSTOMER_INFO", custJSON);
			logger.debug("[newCardAccountRecords][cardDataMap:::" + cardDataMap
					+ "]");
			responseDTO.setData(cardDataMap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			DBUtils.closePreparedStatement(newcardpsmt);
			DBUtils.closeResultSet(cardRS);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	private JSONArray generateDropdowns(String qry) {
		Statement stmt = null;
		ResultSet rs = null;
		JSONArray dropdown = new JSONArray();
		try {
			JSONObject json = null;
			connection = DBConnector.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(qry);
			while (rs.next()) {
				json = new JSONObject();
				json.put("key", rs.getString(1));
				json.put("value", rs.getString(2));
				dropdown.add(json);
			}
		} catch (Exception ex) {
			logger.debug("Error Occured while generating dropdowns..!"
					+ ex.getLocalizedMessage());
		} finally {
			try {
				rs.close();
				stmt.close();
				connection.close();
				rs = null;
				stmt = null;
				connection = null;
			} catch (Exception e) {
			}
		}
		return dropdown;
	}

	public ResponseDTO insertNewCardAccount(RequestDTO requestDTO) {
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();

		logger.debug("inside [insertNewCardAccount]");
		PreparedStatement pstmt = null;
		try {
			requestJSON = requestDTO.getRequestJSON();

			String ACCT_MASTER = "insert into ACCT_MASTER(CARD_ACCT, CARD_ACCT_CCY, CARD_ACCT_BAL, MAKER_ID, MAKER_DTTM, PRD_CODE, PRIM_FLAG, PRD_NAME, REC_STATUS, CARD_ACCT_STATUS, APP_CODE)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?)";
			requestJSON = requestDTO.getRequestJSON();

			connection = DBConnector.getConnection();
			logger.debug("inside [insertNewCardAccount][connection:"
					+ connection + "]");
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(ACCT_MASTER);
			pstmt.setString(1, requestJSON.getString("accountNumber")); // CARD_ACCT
			pstmt.setString(2, requestJSON.getString("currency")); // CARD_ACCT_CCY
			pstmt.setString(3, requestJSON.getString("amountReloaded"));// CARD_ACCT_BAL
			pstmt.setString(4, requestJSON.getString("makerId")); // MAKER_ID
			pstmt.setDate(5, DateUtil.getCurrentDate()); // MAKER_DTTM
			pstmt.setString(6, requestJSON.getString("prdCode")); // PRD_CODE
			pstmt.setString(7, "Y"); // PRIM_FLAG
			pstmt.setString(8, requestJSON.getString("prdDesc")); // PRD_NAME
			pstmt.setString(9, "CA"); // REC_STATUS
			pstmt.setString(10, "O"); // CARD_ACCT_STATUS
			pstmt.setString(11, requestJSON.getString("applCode")); // APP_CODE

			pstmt.execute();
			pstmt.close();
			String CRD_ACCT_LINK = "insert into CRD_ACCT_LINK(CARD_ACCT,CIN,CARD_NO,MAKER_ID,MAKER_DTTM,REC_STATUS,APP_CODE)"
					+ " values (?,?,?,?,?,?,?)";
			pstmt = connection.prepareStatement(CRD_ACCT_LINK);
			pstmt.setString(1, requestJSON.getString("accountNumber"));
			pstmt.setString(2, "737387742278");
			pstmt.setString(3, requestJSON.getString("cardNumber"));
			pstmt.setString(4, requestJSON.getString("makerId"));
			pstmt.setDate(5, DateUtil.getCurrentDate());
			pstmt.setString(6, "CA");
			pstmt.setString(7, requestJSON.getString("applCode"));
			pstmt.execute();
			connection.commit();
		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.debug(ex.getCause());
			responseDTO.addError("Not Inserted Successfully..");
			try {
				connection.rollback();
			} catch (Exception EXx) {
			}

		} finally {
			try {
				pstmt.close();
				connection.close();
				pstmt = null;
				connection = null;
			} catch (Exception ex) {

			}
		}

		return responseDTO;
	}

	/*
	 * Card Activation
	 */

	public ResponseDTO cardActivation(RequestDTO requestDTO) {
		logger.debug("[cardActivation]");
		responseDTO = new ResponseDTO();
		requestJSON = requestDTO.getRequestJSON();
		String cardnumber = (String) requestJSON.get("CARDNUMBER");
		logger.debug("[cardActivation][cardnumber]::[" + cardnumber + "]");
		PreparedStatement pstmt = null;
		String saveQry = "UPDATE CRD_MASTER SET CARD_STATUS='O' WHERE CARD_NO = ?";
		try {
			connection = DBConnector.getConnection();
			pstmt = connection.prepareStatement(saveQry);
			// pstmt.setString(1, "DP");
			pstmt.setString(1, cardnumber);
			pstmt.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);

		}

		return responseDTO;
	}

	/**
	 * Refund
	 */

	public ResponseDTO fetchRefundCardAccountDetails(RequestDTO requestDTO) {
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();

		logger.debug("inside [fetchRefundCardAccountDetails]");
		HashMap<String, Object> cardDataMap = new HashMap<String, Object>();
		requestJSON = requestDTO.getRequestJSON();
		JSONObject custJSON = new JSONObject();
		JSONArray cardJSONArray = new JSONArray();
		PreparedStatement newcardpsmt = null;
		ResultSet cardRS = null;
		logger.debug("[fetchRefundCardAccountDetails][requestJSON:::"
				+ requestJSON + "]");
		String cardNumber = requestJSON.getString("cardNumber");
		try {
			String getcustQry = "select a.F_NAME||' '||M_NAME,a.DOB,MOBILE_NO,a.SEC_QUES,a.SEC_QUES_ANS,a.EMAIL_ID,a.PASSPORT_NO from CUST_MASTER a, CRD_MASTER b where CARD_NO=? and b.CIN=a.CIN ";
			connection = DBConnector.getConnection();
			logger.debug("[fetchRefundCardAccountDetails][connection:::"
					+ connection + "]");
			newcardpsmt = connection.prepareStatement(getcustQry);
			newcardpsmt.setString(1, cardNumber);
			cardRS = newcardpsmt.executeQuery();
			if (cardRS.next()) {
				custJSON = new JSONObject();
				custJSON.put("name", cardRS.getString(1));
				custJSON.put("dob", cardRS.getString(2));
				custJSON.put("mobileNumber", cardRS.getString(3));
				custJSON.put("securityQuestion", cardRS.getString(4));
				custJSON.put("securityAnswer", cardRS.getString(5));
				custJSON.put("email", cardRS.getString(6));
				custJSON.put("passportno", cardRS.getString(7));
			}
			cardRS.close();
			newcardpsmt.close();
			logger.debug("[fetchRefundCardAccountDetails][custJSON:::"
					+ custJSON + "]");
			String getCardQry = "select pm.prd_desc,am.prd_Code,am.card_acct_ccy, decode(am.card_acct_status, 'O','Active', 'C', 'InActive')"
					+ "from acct_master am, prd_master pm where card_acct in(select card_acct from crd_acct_link where card_no=?) and am.prd_code=pm.prd_code";
			newcardpsmt = connection.prepareStatement(getCardQry);
			newcardpsmt.setString(1, cardNumber);
			cardRS = newcardpsmt.executeQuery();
			JSONObject json = null;
			while (cardRS.next()) {
				json = new JSONObject();
				json.put("cardAccount", cardRS.getString(1));
				json.put("prodCode", cardRS.getString(2));
				json.put("currency", cardRS.getString(3));
				json.put("cardStatus", cardRS.getString(4));
				json.put("cardNumber", cardNumber);
				cardJSONArray.add(json);
			}
			cardRS.close();
			logger.debug("[fetchRefundCardAccountDetails][cardJSONArray:::"
					+ cardJSONArray + "]");
			JSONArray prodJsonArray = generateDropdowns("select PRD_CRCY, PRD_CODE ||'-'||PRD_DESC  from PRD_MASTER");
			custJSON.put("CARD_INFO", cardJSONArray);
			custJSON.put("PRD_DROP", prodJsonArray);
			cardDataMap.put("CUSTOMER_INFO", custJSON);
			responseDTO.setData(cardDataMap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			DBUtils.closePreparedStatement(newcardpsmt);
			DBUtils.closeResultSet(cardRS);
			DBUtils.closeConnection(connection);

		}
		return responseDTO;
	}

	public ResponseDTO refundAccount(RequestDTO requestDTO) {
		PreparedStatement pstmt = null;
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();
		logger.debug("inside [refundAccount]");
		try {
			requestJSON = requestDTO.getRequestJSON();

			String ACCT_MASTER = "insert into ACCT_MASTER(CARD_ACCT, CARD_ACCT_CCY, CARD_ACCT_BAL, MAKER_ID, MAKER_DTTM, PRD_CODE, PRIM_FLAG, PRD_NAME, REC_STATUS, CARD_ACCT_STATUS, APP_CODE)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?)";

			connection = DBConnector.getConnection();
			logger.debug("[refundAccount][connection:" + connection + "]");
			connection.setAutoCommit(false);
			pstmt = connection.prepareStatement(ACCT_MASTER);
			pstmt.setString(1, requestJSON.getString("accountNumber")); // CARD_ACCT
			pstmt.setString(2, requestJSON.getString("currency")); // CARD_ACCT_CCY
			pstmt.setString(3, requestJSON.getString("amountReloaded"));// CARD_ACCT_BAL
			pstmt.setString(4, requestJSON.getString("makerId")); // MAKER_ID
			pstmt.setDate(5, DateUtil.getCurrentDate()); // MAKER_DTTM
			pstmt.setString(6, requestJSON.getString("prdCode")); // PRD_CODE
			pstmt.setString(7, "Y"); // PRIM_FLAG
			pstmt.setString(8, requestJSON.getString("prdDesc")); // PRD_NAME
			pstmt.setString(9, "CA"); // REC_STATUS
			pstmt.setString(10, "O"); // CARD_ACCT_STATUS
			pstmt.setString(11, requestJSON.getString("applCode")); // APP_CODE

			pstmt.execute();
			pstmt.close();
			String CRD_ACCT_LINK = "insert into CRD_ACCT_LINK(CARD_ACCT,CIN,CARD_NO,MAKER_ID,MAKER_DTTM,REC_STATUS,APP_CODE)"
					+ " values (?,?,?,?,?,?,?)";
			pstmt = connection.prepareStatement(CRD_ACCT_LINK);
			pstmt.setString(1, requestJSON.getString("accountNumber"));
			pstmt.setString(2, "737387742278");
			pstmt.setString(3, requestJSON.getString("cardNumber"));
			pstmt.setString(4, requestJSON.getString("makerId"));
			pstmt.setDate(5, DateUtil.getCurrentDate());
			pstmt.setString(6, "CA");
			pstmt.setString(7, requestJSON.getString("applCode"));
			pstmt.execute();
			connection.commit();
		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.debug(ex.getCause());
			responseDTO.addError("Not Inserted Successfully..");
			try {
				connection.rollback();
			} catch (Exception EXx) {
			}

		} finally {
			try {
				pstmt.close();
				pstmt = null;
				connection.close();
				connection = null;
			} catch (Exception ex) {

			}
		}

		return responseDTO;
	}

	/**
	 * Reload
	 */

	public ResponseDTO fetchReloadCardAccountDetails(RequestDTO requestDTO) {
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();

		logger.debug("Inside FetchRefundCardAccountDetails... ");
		HashMap<String, Object> cardDataMap = new HashMap<String, Object>();
		requestJSON = requestDTO.getRequestJSON();
		JSONObject custJSON = new JSONObject();
		JSONArray cardJSONArray = new JSONArray();
		PreparedStatement newcardpsmt = null;
		ResultSet cardRS = null;
		logger.debug("[fetchReloadCardAccountDetails][requestJSON:::"
				+ requestJSON + "]");
		String cardNumber = requestJSON.getString("cardNumber");
		try {
			String getcustQry = "select a.F_NAME||' '||M_NAME,a.DOB,MOBILE_NO,a.SEC_QUES,a.SEC_QUES_ANS,a.EMAIL_ID,a.PASSPORT_NO from CUST_MASTER a, CRD_MASTER b where CARD_NO=? and b.CIN=a.CIN ";
			connection = DBConnector.getConnection();
			logger.debug("[fetchReloadCardAccountDetails][connection:::"
					+ connection + "]");
			newcardpsmt = connection.prepareStatement(getcustQry);
			newcardpsmt.setString(1, cardNumber);
			cardRS = newcardpsmt.executeQuery();
			if (cardRS.next()) {
				custJSON = new JSONObject();
				custJSON.put("name", cardRS.getString(1));
				custJSON.put("dob", cardRS.getString(2));
				custJSON.put("mobileNumber", cardRS.getString(3));
				custJSON.put("securityQuestion", cardRS.getString(4));
				custJSON.put("securityAnswer", cardRS.getString(5));
				custJSON.put("email", cardRS.getString(6));
				custJSON.put("passportno", cardRS.getString(7));
			}
			cardRS.close();
			newcardpsmt.close();
			logger.debug("[fetchReloadCardAccountDetails][custJSON:::"
					+ custJSON + "]");
			String getCardQry = "select pm.prd_desc,am.prd_Code,am.card_acct_ccy, decode(am.card_acct_status, 'O','Active', 'C', 'InActive')"
					+ "from acct_master am, prd_master pm where card_acct in(select card_acct from crd_acct_link where card_no=?) and am.prd_code=pm.prd_code";
			newcardpsmt = connection.prepareStatement(getCardQry);
			newcardpsmt.setString(1, cardNumber);
			cardRS = newcardpsmt.executeQuery();
			JSONObject json = null;
			while (cardRS.next()) {
				json = new JSONObject();
				json.put("cardAccount", cardRS.getString(1));
				json.put("prodCode", cardRS.getString(2));
				json.put("currency", cardRS.getString(3));
				json.put("cardStatus", cardRS.getString(4));
				json.put("cardNumber", cardNumber);
				cardJSONArray.add(json);
			}
			cardRS.close();
			logger.debug("[fetchReloadCardAccountDetails][cardJSONArray:::"
					+ cardJSONArray + "]");
			JSONArray prodJsonArray = generateDropdowns("select PRD_CRCY, PRD_CODE ||'-'||PRD_DESC  from PRD_MASTER");
			custJSON.put("CARD_INFO", cardJSONArray);
			custJSON.put("PRD_DROP", prodJsonArray);
			cardDataMap.put("CUSTOMER_INFO", custJSON);
			responseDTO.setData(cardDataMap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			DBUtils.closePreparedStatement(newcardpsmt);
			DBUtils.closeResultSet(cardRS);
			DBUtils.closeConnection(connection);
		}
		return responseDTO;
	}

	public ResponseDTO reloadAccount(RequestDTO requestDTO) {

		logger.debug("inside [reloadAccount]");
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();

		PreparedStatement cinpstmt = null;
		ResultSet cinrs = null;
		CallableStatement cstmt = null;
		PreparedStatement prestmt = null;
		PreparedStatement balpsmt = null;
		String cin = null;
		String seq_NUMBER = null;
		ResultSet rset = null;
		try {

			requestJSON = requestDTO.getRequestJSON();
			connection = DBConnector.getConnection();

			String cinQry = "SELECT CIN FROM CRD_MASTER WHERE CARD_NO= ?";
			cinpstmt = connection.prepareStatement(cinQry);
			cinpstmt.setString(1, requestJSON.getString("cardNumber"));
			cinrs = cinpstmt.executeQuery();
			while (cinrs.next()) {

				cin = cinrs.getString(1);
			}
			logger.debug("[reloadAccount]:::::::[" + cin + "]");

			prestmt = connection
					.prepareStatement("select  TXN_SEQ_NUM.nextval from DUAL");

			rset = prestmt.executeQuery();
			while (rset.next()) {
				seq_NUMBER = rset.getString(1);
			}
			logger.debug("[reloadAccount][seq_NUMBER:::::::[" + seq_NUMBER
					+ "]");

			/*
			 * String acntTxnQry=
			 * "select MAP.ACT_TXN_CODE, TXN.TXN_TYPE, MAP.DRCR_FLAG from" +
			 * " TXN_MASTER TXN, ACTIVITY_TXN_MAPPING MAP " +
			 * "where MAP.ACT_TXN_CODE=TXN.TXN_CODE and MAP.ACT_TXN_CODE in (select ACT_TXN_CODE  from ACTIVITY_TXN_MAPPING, TXN_MASTER txn where TXN.TRANS_CODE=?  and txn.txn_code=MAP.TXN_CODE) order by MAP.TXN_ORDER;"
			 * ;
			 */

			cstmt = connection
					.prepareCall("{call PrepaidGeneralizeCheckPkg.pGeneralizeCheck(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			cstmt.setString(1, requestJSON.getString("cardNumber"));
			cstmt.setString(2, requestJSON.getString("accountNumber"));
			cstmt.setString(3, "100060");
			cstmt.setString(4, "WEB");
			cstmt.setString(5, cin);// cin
			cstmt.setString(6, null);// fee
			cstmt.setString(7, "100061-0-AED");// v_FeeDetails
			cstmt.setString(8, null);// v_LimitCode
			cstmt.setString(9, requestJSON.getString("amountReloaded"));// v_TxnAmtd
			cstmt.setString(10, requestJSON.getString("currency"));// v_Txn_Ccy
			cstmt.setString(11, requestJSON.getString("amountReloaded"));// v_Bill_Amt
			cstmt.setString(12, requestJSON.getString("currency"));// v_Bill_CCY
			cstmt.setString(13, "1");// i_Conv_Rate
			cstmt.setString(14, null);// RRNO
			cstmt.setString(15, null);// v_Term_ID
			cstmt.setString(16, null);// v_Msg_type
			cstmt.setString(17, null);// v_Sys_Trace_no
			cstmt.setString(18, null);// v_Agent_type
			cstmt.setString(19, null);// v_Exp_dt
			cstmt.setString(20, null);// v_Merc_Id
			cstmt.setString(21, null);// v_Merc_Name
			cstmt.setString(22, null);// v_Merc_Loc
			cstmt.setString(23, null);// v_Merc_city
			cstmt.setString(24, null);// v_MCC
			cstmt.setString(25, null);// v_Auth_Id
			cstmt.setString(26, null);// v_POS_Entry
			cstmt.setString(27, null);// v_Acq_Id
			cstmt.setString(28, null);// v_Device_Type
			cstmt.setString(29, null);// v_OnOff_Flag
			cstmt.setString(30, null);// v_Settle_Amt
			cstmt.setString(31, null);// v_Settle_Ccy
			cstmt.setString(32, seq_NUMBER);// v_UniqueRef
			cstmt.setString(33, "00");// v_Resp_code
			cstmt.setString(34, null);// v_ChnCode
			cstmt.setString(35, null);// v_TransDttm
			cstmt.setString(36, null);// v_FraudInd
			cstmt.setString(37, null);// v_FuncCode
			cstmt.setString(38, null);// v_Address
			cstmt.setString(39, null);// v_Acq_ref_no
			cstmt.setString(40, "Approved");// v_Error_Desc
			cstmt.setString(41, null);// v_base1_bill_ccy
			cstmt.setString(42, null);// v_base1_bill_amt
			cstmt.setString(43, null);// i_src_conv_rate
			cstmt.setString(44, null);// i_dest_conv_rate
			cstmt.setString(45, null);// v_multi_ccy_flag
			cstmt.setString(46, null);// v_emvdata
			cstmt.setString(47, null);// v_pos_cond_cd
			cstmt.setString(48, null);// v_appcode
			cstmt.registerOutParameter(49, java.sql.Types.VARCHAR);// ErrorMsg
			cstmt.registerOutParameter(50, java.sql.Types.INTEGER);// RetVal
			cstmt.executeUpdate();
			String message = cstmt.getString(49);
			int retval = cstmt.getInt(50);
			logger.debug("[reloadAccount][retval:::::::[" + retval + "]");
			if (retval == 0) {
				String balupdateQry = "UPDATE ACCT_MASTER SET CARD_ACCT_BAL = CARD_ACCT_BAL+? WHERE CARD_ACCT = ?";
				balpsmt = connection.prepareStatement(balupdateQry);
				balpsmt.setString(1, requestJSON.getString("amountReloaded"));
				balpsmt.setString(2, requestJSON.getString("accountNumber"));
				balpsmt.executeQuery();
			} else {
				responseDTO.addError("Refund failed");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("Error !" + ex.getLocalizedMessage());
			logger.debug("Error !" + ex.getCause());
		}
		return responseDTO;
	}
}
