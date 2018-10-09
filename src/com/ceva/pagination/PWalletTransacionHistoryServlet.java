package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class PWalletTransacionHistoryServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;
	
	Logger logger = Logger.getLogger(PWalletTransacionHistoryServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.debug("inside doGet......................");
		JSONObject jsonResult = new JSONObject();
		int listDisplayAmount = 10;
		int start = 0;
		int column = 0;
		String dir = "asc";
		String pageNo = request.getParameter("iDisplayStart");
		String pageSize = request.getParameter("iDisplayLength");
		String colIndex = request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0");
		GLOBAL_SEARCH_TERM = request.getParameter("sSearch");
		
		logger.debug("pageNo::"+pageNo);
		logger.debug("iDisplayLength::"+pageSize);
		logger.debug("Search Val::"+GLOBAL_SEARCH_TERM +"   length::"+GLOBAL_SEARCH_TERM.length());
		
		if (pageNo != null) {
			start = Integer.parseInt(pageNo);
			if (start < 0) {
				start = 0;
			}
		}

		if (pageSize != null) {
			listDisplayAmount = Integer.parseInt(pageSize);
			if (listDisplayAmount < 10 || listDisplayAmount > 50) {
				listDisplayAmount = 10;
			}
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
		
		RECORD_SIZE = listDisplayAmount;
		GLOBAL_SEARCH_TERM = request.getParameter("sSearch");
		INITIAL = start;
		try {
			jsonResult = getMerchantDashboardData(totalRecords, request);
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

	public JSONObject getMerchantDashboardData(int totalRecords, HttpServletRequest request) throws SQLException, ClassNotFoundException {
		int totalAfterSearch = totalRecords;
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		String searchSQL = "";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		con= con == null ? DBConnector.getConnection() : con;
		
		logger.debug("Connection :::"+con+" GLOBAL_SEARCH_TERM::::"+GLOBAL_SEARCH_TERM);
		
		if (GLOBAL_SEARCH_TERM != null && GLOBAL_SEARCH_TERM.length()>0) {

			//query += searchSQL;
			String query="select count(*) count from "
					+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,TXN_TYPE,TXNDATE,TXNAMOUNT,RESPONSECODE,DRCR,CHANNEL,TERMINAL_ID,ISREVERSAL,rownum rn from  "
					+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,decode(TXN_TYPE,'PFT','Fund Transfer','CRV','Credit Voucher','PATP','Airtime Topup','PFTF','Fund Transfer Fee','CRVF','Credit Voucher Fee','PATPF','Airtime Topup Fee','PCDP','Cash Deposit','PCDPF','Cash Deposit Fee','PWDL','Cash Withdrawal','PWDLF','Cash Withdrawal Fee',TXN_TYPE) TXN_TYPE,"
					+ "to_char(TXNDATE,'DD-MM-YYYY HH24:MI:SS') TXNDATE,TXNAMOUNT/100 TXNAMOUNT,RESPONSECODE,decode(DRCR,'DR','Debit','CR','Credit',DRCR) DRCR,"
					+ "CHANNEL,TERMINAL_ID,decode(ISREVERSAL,'N','Approved','Y','Reversal',ISREVERSAL) ISREVERSAL "
					+ "from WALLET_TXN_GL	where TXN_REF_NO like ? or FROM_ACCOUNT like ? order by TXNDATE asc))";				
			
			
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(2,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			
			ResultSet results = st.executeQuery();
			if (results.next()) {
				totalAfterSearch = results.getInt("count");
			}
			
			
			results.close();
			st.close();
			
			//total records after search
			logger.debug("totalAfterSearch::"+totalAfterSearch);
			int intiaCnt=0;
			if(INITIAL == 0)
				intiaCnt=INITIAL;
			else
				intiaCnt =INITIAL+1;

			query ="select TXN_REF_NO,POSRRN,FROM_ACCOUNT,TXN_TYPE,TXN_DATE,TXNAMOUNT,RESPONSECODE,DRCR,CHANNEL,TERMINAL_ID,ISREVERSAL from "
					+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,TXN_TYPE,TXN_DATE,TXNAMOUNT,RESPONSECODE,DRCR,CHANNEL,TERMINAL_ID,ISREVERSAL,rownum rn from  "
					+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,decode(TXN_TYPE,'PFT','Fund Transfer','CRV','Credit Voucher','PATP','Airtime Topup','PFTF','Fund Transfer Fee','CRVF','Credit Voucher Fee','PATPF','Airtime Topup Fee','PCDP','Cash Deposit','PCDPF','Cash Deposit Fee','PWDL','Cash Withdrawal','PWDLF','Cash Withdrawal Fee',TXN_TYPE) TXN_TYPE,"
					+ "to_char(TXNDATE,'DD-MM-YYYY HH24:MI:SS') TXN_DATE,TXNAMOUNT/100 TXNAMOUNT,RESPONSECODE,decode(DRCR,'DR','Debit','CR','Credit',DRCR) DRCR,"
					+ "CHANNEL,TERMINAL_ID,decode(ISREVERSAL,'N','Approved','Y','Reversal',ISREVERSAL) ISREVERSAL "
					+ "from WALLET_TXN_GL where TXN_REF_NO like ? or FROM_ACCOUNT like ? order by TXNDATE asc)) where rn between "+
					+ intiaCnt + " and " + (INITIAL+RECORD_SIZE);
				
				
			System.out.println(query);
			stmt = con.prepareStatement(query);
			stmt.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(2,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			rs = stmt.executeQuery();
			while (rs.next()) {
				JSONArray ja = new JSONArray();
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
				ja.put(rs.getString(5));
				ja.put(rs.getString(6)+" /=Kshs");
				ja.put(rs.getString(7));
				ja.put(rs.getString(8));
				ja.put(rs.getString(9));
				ja.put(rs.getString(10));
				ja.put(rs.getString(11));
				array.put(ja); 
			}
			rs.close();
			stmt.close();
		}else{
			int intiaCnt= 0;
			
			if(INITIAL == 0)
				intiaCnt=INITIAL;
			else
				intiaCnt =INITIAL+1;
			
			String sql ="select TXN_REF_NO,POSRRN,FROM_ACCOUNT,TXN_TYPE,TXN_DATE,TXNAMOUNT,RESPONSECODE,DRCR,CHANNEL,TERMINAL_ID,ISREVERSAL from "
					+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,TXN_TYPE,TXN_DATE,TXNAMOUNT,RESPONSECODE,DRCR,CHANNEL,TERMINAL_ID,ISREVERSAL,rownum rn from  "
					+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,decode(TXN_TYPE,'PFT','Fund Transfer','CRV','Credit Voucher','PATP','Airtime Topup','PFTF','Fund Transfer Fee','CRVF','Credit Voucher Fee','PATPF','Airtime Topup Fee','PCDP','Cash Deposit','PCDPF','Cash Deposit Fee','PWDL','Cash Withdrawal','PWDLF','Cash Withdrawal Fee',TXN_TYPE) TXN_TYPE,"
					+ "to_char(TXNDATE,'DD-MM-YYYY HH24:MI:SS') TXN_DATE,TXNAMOUNT/100 TXNAMOUNT,RESPONSECODE,decode(DRCR,'DR','Debit','CR','Credit',DRCR) DRCR,"
					+ "CHANNEL,TERMINAL_ID,decode(ISREVERSAL,'N','Approved','Y','Reversal',ISREVERSAL) ISREVERSAL "
					+ "from WALLET_TXN_GL order by TXNDATE desc)) where rn between "+
					+ intiaCnt + " and " + (INITIAL+RECORD_SIZE);
			
			System.out.println(sql);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				JSONArray ja = new JSONArray();
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
				ja.put(rs.getString(5));
				ja.put(rs.getString(6)+" /=Kshs");
				ja.put(rs.getString(7));
				ja.put(rs.getString(8));
				ja.put(rs.getString(9));
				ja.put(rs.getString(10));
				ja.put(rs.getString(11));
				array.put(ja); 
			}
			rs.close();
			stmt.close();

		}
		con.close();
		
		try {
			result.put("iTotalRecords", totalRecords);
			result.put("iTotalDisplayRecords", totalAfterSearch);
			result.put("aaData", array);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			System.out.println("result::"+result);
			return result;
	}

	public int getTotalRecordCount() throws SQLException {
		int totalRecords = -1;
		String sql ="select count(*) from "
				+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,TXN_TYPE,TXNDATE,TXNAMOUNT,RESPONSECODE,DRCR,CHANNEL,TERMINAL_ID,ISREVERSAL,rownum rn from  "
				+ "(select TXN_REF_NO,POSRRN,FROM_ACCOUNT,decode(TXN_TYPE,'PFT','Fund Transfer','CRV','Credit Voucher','PATP','Airtime Topup','PFTF','Fund Transfer Fee','CRVF','Credit Voucher Fee','PATPF','Airtime Topup Fee','PCDP','Cash Deposit','PCDPF','Cash Deposit Fee','PWDL','Cash Withdrawal','PWDLF','Cash Withdrawal Fee',TXN_TYPE) TXN_TYPE,"
				+ "to_char(TXNDATE,'DD-MM-YYYY HH24:MI:SS') TXNDATE,TXNAMOUNT/100 TXNAMOUNT,RESPONSECODE,decode(DRCR,'DR','Debit','CR','Credit',DRCR) DRCR,"
				+ "CHANNEL,TERMINAL_ID,decode(ISREVERSAL,'N','Approved','Y','Reversal',ISREVERSAL) ISREVERSAL from WALLET_TXN_GL order by TXNDATE asc))";				
		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement statement = con.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt(1);
		}
		resultSet.close();
		statement.close();
		con.close();
	
		return totalRecords;
	}

}