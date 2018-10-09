package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class PayBillTransacionHistoryServlet extends HttpServlet {

	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;
	
	Logger logger = Logger.getLogger(PayBillTransacionHistoryServlet.class);

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
		
		System.out.println("pageNo::"+pageNo);
		System.out.println("iDisplayLength::"+pageSize);
		
		
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
			jsonResult = getPersonDetails(totalRecords, request);
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

	public JSONObject getPersonDetails(int totalRecords, HttpServletRequest request) throws SQLException, ClassNotFoundException {
		int totalAfterSearch = totalRecords;
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		String searchSQL = "";
		
		Connection con = DBConnector.getConnection();
		System.out.println("Connection :::"+con);
		
		int intiaCnt= 0;
		
		if(INITIAL == 0)
			intiaCnt=INITIAL;
		else
			intiaCnt =INITIAL+1;
		
		String sql ="SELECT REFERENCE_NO,MAKER_DTTM, BILLER_ID, ACCOUNT_NO, CUSTOMER_NAME, PAYMENT_MODE,PAYMENT_AMOUNT,decode(PAYMENT_STATUS,'P','Pending','S','Success','F','Fail',PAYMENT_STATUS) PAYMENT_STATUS  "
				+ "from (SELECT REFERENCE_NO, to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') MAKER_DTTM, BILLER_ID, ACCOUNT_NO, CUSTOMER_NAME, PAYMENT_MODE,PAYMENT_AMOUNT,decode(PAYMENT_STATUS,'P','Pending','S','Success','F','Fail',PAYMENT_STATUS) PAYMENT_STATUS,rownum rn from "+
				" (SELECT REFERENCE_NO, MAKER_DTTM, BILLER_ID, ACCOUNT_NO, CUSTOMER_NAME, PAYMENT_MODE,(PAYMENT_AMOUNT/100) PAYMENT_AMOUNT,decode(PAYMENT_STATUS,'P','Pending','S','Success','F','Fail',PAYMENT_STATUS) PAYMENT_STATUS "+
				" FROM BILL_PAYMENT_MASTER order by MAKER_DTTM desc)) where  rn  between "+
				+ intiaCnt + " and " + (INITIAL+RECORD_SIZE);
		
		System.out.println(sql);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		while (rs.next()) {
			JSONArray ja = new JSONArray();
			ja.put(rs.getString(1));
			ja.put(rs.getString(2));
			ja.put(rs.getString(3));
			ja.put(rs.getString(4));
			ja.put(rs.getString(5));
			ja.put(rs.getString(6));
			ja.put(rs.getString(7)+" /-kshs");
			ja.put(rs.getString(8));
			array.put(ja); 
		}
		stmt.close();
		rs.close();

		String query = "SELECT " + "COUNT(*) as count " + "FROM " + "BILL_PAYMENT_MASTER " + "WHERE ";
		//for pagination
		if (GLOBAL_SEARCH_TERM != "") {
			query += searchSQL;
			PreparedStatement st = con.prepareStatement(query);
			ResultSet results = st.executeQuery();
			if (results.next()) {
				totalAfterSearch = results.getInt("count");
			}
			st.close();
			results.close();
			con.close();
		}
		try {
			result.put("iTotalRecords", totalRecords);
			result.put("iTotalDisplayRecords", totalAfterSearch);
			result.put("aaData", array);
		} catch (Exception e) {
		}
		
			System.out.println("result::"+result);
			return result;
	}

	public int getTotalRecordCount() throws SQLException {
		int totalRecords = -1;
		String sql ="SELECT count(*) as count FROM BILL_PAYMENT_MASTER  order by MAKER_DTTM desc";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		Connection con = DBConnector.getConnection();
		PreparedStatement statement = con.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt("count");
		}
		resultSet.close();
		statement.close();
		con.close();
	
		return totalRecords;
	}

}