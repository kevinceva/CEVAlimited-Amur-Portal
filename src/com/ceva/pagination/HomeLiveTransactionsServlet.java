package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class HomeLiveTransactionsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;
	private String makerId;
	private String userType;
	private String location;
	private String locationCode;
	Connection connection = null;
	Logger logger = Logger.getLogger(HomeLiveTransactionsServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.debug("inside doGet......................");

		HttpSession session = request.getSession();

		JSONObject jsonResult = new JSONObject();
		int listDisplayAmount = 100;
		int start = 0;
		int column = 0;
		String dir = "asc";
		String pageNo = request.getParameter("iDisplayStart");
		String pageSize = request.getParameter("iDisplayLength");
		String colIndex = request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0");
		GLOBAL_SEARCH_TERM = request.getParameter("sSearch");
		makerId = (String) session.getAttribute("makerId");
		location = (String) session.getAttribute("location");
		logger.debug("Login Id::"+makerId +"        colIndex::"+colIndex);
		logger.debug("Location::"+location);

		logger.debug("Search Val::"+GLOBAL_SEARCH_TERM +"   length::"+GLOBAL_SEARCH_TERM.length());
		logger.debug("pageNo::"+pageNo);
		logger.debug("iDisplayLength::"+pageSize);

		try {
			userType=getUserType().toUpperCase();
			locationCode = getLocationCode();
			logger.debug("userType::::"+userType);
			logger.debug("Location::"+location+":::locationCode:"+locationCode);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (pageNo != null) {
			start = Integer.parseInt(pageNo);
			if (start < 0) {
				start = 0;
			}
		}

		if (pageSize != null) {
			listDisplayAmount = Integer.parseInt(pageSize);
			/*if (listDisplayAmount < 10 || listDisplayAmount > 100) {
				listDisplayAmount = 10;
			}*/
		}
		if (colIndex != null) {
			column = Integer.parseInt(colIndex);
			if (column < 0 || column > 5)
				column = 0;
		}
		if(sortDirection != null){
			if (!sortDirection.equals("asc"))
				dir = "desc";
		}

		int totalRecords= -1;
		try {
			totalRecords = getTotalRecordCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("totalRecords:::"+totalRecords);
		
		int dayCount= -1;
		try {
			dayCount = getDayTxnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("dayCount:::"+dayCount);
		
		int daySale= -1;
		try {
			daySale = getSaleValue();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("daySale:::"+daySale);
		
		session.setAttribute("dayCount",dayCount);
		session.setAttribute("daySale",daySale);
		
		int weekCount= -1;
		try {
			weekCount = weeklyTxnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("weekCount:::"+weekCount);
		
		int weekSale= -1;
		try {
			weekSale = weeklySaleValue();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("weekSale:::"+weekSale);
		
		session.setAttribute("weekCount",weekCount);
		session.setAttribute("weekSale",weekSale);
		
		int policyCount= -1;
		try {
			policyCount = activePolCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("policyCount:::"+policyCount);
		
		int totalSale= -1;
		try {
			totalSale = totalSaleValue();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("totalSale:::"+totalSale);
		
		session.setAttribute("policyCount",policyCount);
		session.setAttribute("totalSale",totalSale);

		int ClnCount= -1;
		try {
			ClnCount = idPendCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("ClnCount:::"+ClnCount);
		
		int BenCount= -1;
		try {
			BenCount = benIdPendCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("BenCount:::"+BenCount);
		
		session.setAttribute("ClnCount",ClnCount);
		session.setAttribute("BenCount",BenCount);		
		
		int AgntCnt= -1;
		try {
			AgntCnt = activeAgentCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("AgntCnt:::"+AgntCnt);
		
		int CustCnt= -1;
		try {
			CustCnt = activationsThruAgnetCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("CustCnt:::"+CustCnt);
		
		session.setAttribute("AgntCnt",AgntCnt);
		session.setAttribute("CustCnt",CustCnt);
		
		int ClnFailCnt= -1;
		try {
			ClnFailCnt = ClnFailureCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("ClnFailCnt:::"+ClnFailCnt);
		session.setAttribute("ClnFailCnt",ClnFailCnt);
		
		int DailyClnActvCnt= -1;
		try {
			DailyClnActvCnt = DailyActivations();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("DailyClnActvCnt:::"+DailyClnActvCnt);
		session.setAttribute("DailyClnActvCnt",DailyClnActvCnt);		
		
		int TotalClnCnt= -1;
		try {
			TotalClnCnt = TotalClinets();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("TotalClnCnt:::"+TotalClnCnt);
		session.setAttribute("TotalClnCnt",TotalClnCnt);
		
		int MulPolCnt= -1;
		try {
			MulPolCnt = MultiplePolicies();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("MulPolCnt:::"+MulPolCnt);
		session.setAttribute("MulPolCnt",MulPolCnt);	
		
		int totalCashSale= -1;
		try {
			totalCashSale = totalCashSaleValue();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		logger.debug("totalCashSale:::"+totalCashSale);
		session.setAttribute("totalCashSale",totalCashSale);		
		
		RECORD_SIZE = listDisplayAmount;

		INITIAL = start;
		try {
			jsonResult = getLiveTransactionDetails(totalRecords, request);
			logger.debug("jsonResult:::"+jsonResult);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(jsonResult);
	}

	public JSONObject getLiveTransactionDetails(int totalRecords, HttpServletRequest request) throws SQLException, ClassNotFoundException {
		int totalAfterSearch = totalRecords;
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		String searchSQL = "";
		Connection con = null;
		con= con == null ? DBConnector.getConnection() : con;
		logger.debug("Connection :::"+con+" GLOBAL_SEARCH_TERM::::"+GLOBAL_SEARCH_TERM);

		int intiaCnt= 0;

		String query = "SELECT " + "COUNT(*) as count " + "FROM " + "TBL_TRANLOG " + "WHERE ";
		//for pagination
		if (GLOBAL_SEARCH_TERM != null && GLOBAL_SEARCH_TERM.length()>0) {

			logger.debug("Inside if condition...");

			/*query ="select count(*) "
					+ "from (SELECT SERVICE,TXN_DESC,CARD_NO,ServiceAccount,channel,mobile_no,rrn,txnrefno,TXN_DATE_TIME,TXNAMT,MAKERID, "
					+ "Branch,status,posrrn,responsecode,rownum rn     FROM (  SELECT LT.BANKNAME SERVICE, "
					+ " DECODE(LT.TXNTYPE,'ENQ', 'P-WALLET ENQUIRY','PCDP', 'P-WALLET CASH DEPOSIT', 'PWDL', 'P-WALLET CASH WITHDRAWL', 'CDP', 'CASH DEPOSIT', 'WDL', 'CASH WITHDRAWL','BEQ','BALANCE ENQUIRY','HDM','HUDUMA','REV','REVERSAL',LT.TXNTYPE) TXN_DESC, "
					+ "SUBSTR(LT.PAN,1,4) ||'XXXXXXXX'  ||SUBSTR(LT.PAN,13,16) CARD_NO, "
					+ " ' ' ServiceAccount, LT.TERMINALNUMBER channel,' ' mobile_no,"
					+ "  LT.RRN rrn, ' ' txnrefno,"
					+ " TO_CHAR(LT.TXNDATE,'DD-MON-YYYY HH24:MI:SS') TXN_DATE_TIME, TO_CHAR(LT.amount/100) TXNAMT,  LT.APPROVEDBY MAKERID,"
					+ " (SELECT PBM.OFFICE_CODE||'-'||PBM.OFFICE_NAME  FROM user_information A, USER_LOGIN_CREDENTIALS B , POSTA_BRANCH_MASTER PBM "
					+ "  WHERE A.COMMON_ID  =B.COMMON_ID    AND b.LOGIN_USER_ID=LT.APPROVEDBY    AND A.LOCATION     =PBM.OFFICE_CODE  ) Branch,"
					+ " DECODE(LT.ISREVERSED,'Y','REVERSAL',LT.STATUS) status, LT.POSRRN posrrn, LT.RESPONSECODE responsecode"
					+ "   FROM TBL_TRANLOG LT      WHERE ( (LT.TXNTYPE <> 'BEQ' ) OR ( LT.TXNTYPE ='BEQ' AND NVL(LT.RESPONSECODE,'NO_VAL') = '00' )  ) "
					+ " AND LT.RRN like ?      AND TRUNC(LT.TXNDATE) =TRUNC(sysdate)  ORDER BY LT.TXNDATE DESC ) )";*/

			query =" select a+p+b from (select count(*) a from cic_c2b_airtime_log c1, cic_c2b_log c2  "
					+ " where c2.C2B_LOG_ID=c1.C2B_LOG_ID and (c1.policy_id like ? or c1.status like upper(?) or 'AIRTIME' like upper(?) or instr(c2.request_data,?)>0 or c1.channel like upper(?))) al,"
					+ " (select count(*) p from CIC_POLICY_TOPUP_PURCHASE_LOG c1, cic_c2b_log c2 "
					+ " where c2.C2B_LOG_ID=c1.C2B_LOG_ID and (c1.policy_id like ? or c1.status like upper(?) or 'CASH' like upper(?) or instr(c2.request_data,?)>0 or c1.channel like upper(?))) pl,"
					+ " (select count(*) b from  CIC_BORROW_AIRTIME cb where cb.policy_id like ? or cb.status like upper(?) or 'Borrow' like upper(?) or cb.channel like upper(?)) ";
					
					
			
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(2,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(3,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(4,  GLOBAL_SEARCH_TERM.trim() );
			st.setString(5,  GLOBAL_SEARCH_TERM.trim() );
			st.setString(6,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(7,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(8,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(9,  GLOBAL_SEARCH_TERM.trim() );
			st.setString(10,  GLOBAL_SEARCH_TERM.trim() );
			st.setString(11, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(12, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(13, GLOBAL_SEARCH_TERM.trim() );
			st.setString(14, GLOBAL_SEARCH_TERM.trim() );
			ResultSet results = st.executeQuery();
			if (results.next()) {
				totalAfterSearch = results.getInt(1);
			}
			results.close();
			st.close();
			//con.close();

			logger.debug("totalAfterSearch::"+totalAfterSearch);

			if(INITIAL == 0)
				intiaCnt=INITIAL;
			else
				intiaCnt =INITIAL+1;

			
			
			
	/*		 query ="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS,AIRTIME_ID from "
			 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID, rownum rn from (select AIRTIME_ID,POLICY_ID,AMOUNT,DATE_CREATED, STATUS "
					+ " from cic_c2b_airtime_log where  "
					+ " (policy_id like ? or status like ? ) order by date_created desc ) )"
					+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;*/
			
/*			query="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid from "
		 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid, rownum rn from (select c1.AIRTIME_ID, c1.POLICY_ID,c1.AMOUNT,c1.DATE_CREATED, c1.STATUS,"
		 		+ " substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) mpid "
				+ " from cic_c2b_airtime_log c1, cic_c2b_log c2  "
				+ " where c2.C2B_LOG_ID=c1.C2B_LOG_ID and (c1.policy_id like ? or c1.status like ? or instr(c2.request_data,?)>0  ) order by c1.date_created desc ) )"
				+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;*/
			
			query="select to_char(dt,'HH:MI AM Month DD, YYYY'), txn, POLICY_ID,   AMOUNT, STATUS, chn, indv_id , mpid  from ( "
					+ " select txn,chn, dt,POLICY_ID, AMOUNT, STATUS,indv_id,mpid, rownum rn from "
					+ " (select txn, indv_id, POLICY_ID, AMOUNT, chn, dt, STATUS, mpid from "
					+ " (select 'Airtime' txn, c1.AIRTIME_ID indv_id, c1.POLICY_ID,c1.AMOUNT,decode(nvl(c1.CHECKOUT_ID,'NO_DATA'),'NO_DATA','181818','USSD') chn, "
					+ " c1.DATE_CREATED dt, c1.STATUS, substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) mpid "
					+ " from cic_c2b_airtime_log c1, cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID and (c1.policy_id like ? or c1.status like upper(?) or 'AIRTIME' like upper(?) or instr(c2.request_data,?)>0 or (decode(nvl(c1.CHECKOUT_ID,'NO_DATA'),'NO_DATA','181818','USSD') like upper(?)) ) "
					+ " union all "
					+ " select 'Cash' txn, c1.PURCHASE_ID indv_id, c1.POLICY_ID,c1.AMOUNT,c1.CHANNEL chn, c1.DATE_CREATED dt, c1.STATUS, "
					+ " substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) mpid "
					+ " from CIC_POLICY_TOPUP_PURCHASE_LOG c1, cic_c2b_log c2 "
					+ " where c2.C2B_LOG_ID=c1.C2B_LOG_ID and (c1.policy_id like ? or c1.status like upper(?) or  'CASH' like upper(?) or instr(c2.request_data,?)>0 or c1.channel like upper(?) ) "
					+ " union all"
					+ " select 'Borrow' txn, cb.REFERENCE_NUMBER indv_id,cb.policy_id, cb.AMOUNT_ISSUED amount, cb.channel chn, cb.ISSUED_DATE dt, cb.status, '' mpid "
					+ " from CIC_BORROW_AIRTIME cb"
					+ "	where cb.policy_id like ? or cb.status like upper(?) or 'Borrow' like upper(?) or cb.channel like upper(?) )  "
					+ " order by dt desc ) "
					+ "  ) "
					+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE)  ;
				
			System.out.println(query);
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(2, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(3, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(4, GLOBAL_SEARCH_TERM.trim() );
			stmt.setString(5, GLOBAL_SEARCH_TERM.trim() );
			stmt.setString(6, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(7, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(8, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(9, GLOBAL_SEARCH_TERM.trim() );
			stmt.setString(10, GLOBAL_SEARCH_TERM.trim() );
			stmt.setString(11, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(12, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(13, GLOBAL_SEARCH_TERM.trim() );
			stmt.setString(14, GLOBAL_SEARCH_TERM.trim() );
			ResultSet rs = stmt.executeQuery();
			int i=1;
			while (rs.next()) {
				JSONArray ja = new JSONArray();
				ja.put(i);
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
				ja.put(rs.getString(5));
				ja.put(rs.getString(6));
				ja.put(rs.getString(7));
				ja.put(rs.getString(8));
				array.put(ja); 
				i++;
			}
			rs.close();
			stmt.close();

		}else{

			if(INITIAL == 0)
				intiaCnt=INITIAL;
			else
				intiaCnt =INITIAL+1;
			String sql ="";

			PreparedStatement stmt = null;

			
				/*sql ="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS,AIRTIME_ID, mpid from "
				 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid, rownum rn from (select c1.AIRTIME_ID, c1.POLICY_ID,c1.AMOUNT,c1.DATE_CREATED, c1.STATUS,"
				 		+ " ( select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) "
				 		+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID ) mpid "
						+ " from cic_c2b_airtime_log c1  "
						+ " order by date_created desc ) )"
						+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;	*/
			
				sql="select to_char(dt,'HH:MI AM Month DD, YYYY'), txn, POLICY_ID,   AMOUNT, STATUS, chn, indv_id , mpid  from ( "
					+ " select txn,chn, dt,POLICY_ID, AMOUNT, STATUS,indv_id,mpid, rownum rn from "
					+ " ( select txn, indv_id, POLICY_ID, AMOUNT, chn, dt, STATUS, mpid from "
					+ " (select 'Airtime' txn, c1.AIRTIME_ID indv_id, c1.POLICY_ID,c1.AMOUNT,decode(nvl(c1.CHECKOUT_ID,'NO_DATA'),'NO_DATA','181818','USSD') chn, "
					+ " c1.DATE_CREATED dt, c1.STATUS, (select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) "
					+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID ) mpid  "
					+ " from cic_c2b_airtime_log c1 ) "
					+ " union all "
					+ " (select 'Cash' txn, c1.PURCHASE_ID indv_id, c1.POLICY_ID,c1.AMOUNT,c1.CHANNEL chn, c1.DATE_CREATED dt, c1.STATUS, "
					+ " (select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) "
					+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID ) mpid "
					+ " from CIC_POLICY_TOPUP_PURCHASE_LOG c1 ) "
					+ " union all"
					+ " (select 'Borrow' txn, cb.REFERENCE_NUMBER indv_id,cb.policy_id, cb.AMOUNT_ISSUED amount, cb.channel chn, cb.ISSUED_DATE dt, cb.status, '' mpid "
					+ " from CIC_BORROW_AIRTIME cb ) "
					+ "  order by dt desc ) "
					+ "  ) "
					+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE)  ;
				
						stmt=con.prepareStatement(sql);

			System.out.println(sql);
			
			ResultSet rs = stmt.executeQuery();
			int i=1;
			while (rs.next()) {
				JSONArray ja = new JSONArray();
				ja.put(i);
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
				ja.put(rs.getString(5));
				ja.put(rs.getString(6));
				ja.put(rs.getString(7));
				ja.put(rs.getString(8));
				array.put(ja); 
				i++;
			}
			rs.close();
			stmt.close();
		}
		try {
			result.put("iTotalRecords", totalRecords);
			result.put("iTotalDisplayRecords", totalAfterSearch);
			result.put("aaData", array);
		} catch (Exception e) {
		}finally{
			con.close();
		}

		logger.debug("result::"+result);
		return result;
	}

	public int getTotalRecordCount() throws SQLException {
		int totalRecords = -1;
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql ="select a+p from (select count(*) a from cic_c2b_airtime_log) al,(select count(*) p from CIC_POLICY_TOPUP_PURCHASE_LOG) pl  ";
			
					stmt=con.prepareStatement(sql);
	
		
		
		ResultSet resultSet = stmt.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		stmt.close();
		con.close();

		return totalRecords;
	}

	public String getUserType() throws SQLException{
		String userType = "NO_DATA";
		String sql ="select A.USER_LEVEL "
				+ "from user_information A, USER_LOGIN_CREDENTIALS B  where A.COMMON_ID=B.COMMON_ID AND   b.LOGIN_USER_ID=?";

		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement statement = con.prepareStatement(sql);
		statement.setString(1, makerId);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			userType = resultSet.getString(1);
		}
		resultSet.close();
		statement.close();
		con.close();
		return userType;
	}

	public String getLocationCode() throws SQLException{
		String locationCode = "";
		String sql ="select  PBM.OFFICE_CODE  from POSTA_BRANCH_MASTER PBM  where PBM.OFFICE_NAME=?";

		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement statement = con.prepareStatement(sql);
		statement.setString(1, location);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			locationCode = resultSet.getString(1);
		}
		resultSet.close();
		statement.close();
		con.close();
		return locationCode;
	}
	
	public int getDayTxnCount() throws SQLException {
		int totalRecords = -1;
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql ="select count(*) from cic_c2b_airtime_log where trunc(date_created)=trunc(sysdate) and status='SUCCESS'  ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql ="select round(sum(amount),2) from cic_c2b_airtime_log where trunc(date_created)=trunc(sysdate) and status='SUCCESS' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql ="select count(*) from cic_c2b_airtime_log where to_char(date_created,'WW-YYYY')=to_char(current_date,'WW-YYYY') and status='SUCCESS' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql ="select round(sum(amount),2) from cic_c2b_airtime_log where to_char(date_created,'WW-YYYY')=to_char(current_date,'WW-YYYY') and status='SUCCESS' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql ="select count(*) from cic_clients where status='CS' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select round(sum(amount),2) from cic_c2b_airtime_log where status='SUCCESS' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select round(sum(amount),2) from CIC_POLICY_TOPUP_PURCHASE_LOG where status='SUCCESS' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from cic_clients where status='CC' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from cic_clients c where c.status='CV' and  not exists (select 1 from CIC_CLIENT_BENEFICIARIES b where b.policy_id=c.mobile_number) ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from CIC_AGENT_MASTER where status='A' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(distinct m.policy_id) from CIC_AGENT_CLIENT_MAPPING m where exists (select 1 from cic_clients c where c.mobile_number=m.policy_id ) ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from cic_clients where status='CVF' ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from cic_clients where trunc(date_created)=trunc(sysdate) ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from cic_clients ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
			sql =" select count(*) from (select count(*) from cic_clients where id_number is not null group by id_number having count(mobile_number)>1) ";
			
					stmt=con.prepareStatement(sql);
	
		
		
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