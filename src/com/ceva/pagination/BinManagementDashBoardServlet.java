package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class BinManagementDashBoardServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;
	private String makerId;
	private String location;
	Connection connection = null;
	Logger logger = Logger.getLogger(BinManagementDashBoardServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.debug("inside doGet......................");

		HttpSession session = request.getSession();

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
		makerId = (String) session.getAttribute("makerId");
		location = (String) session.getAttribute("location");
		logger.debug("Login Id::"+makerId +"        colIndex::"+colIndex);
		logger.debug("Location::"+location);

		logger.debug("Search Val::"+GLOBAL_SEARCH_TERM +"   length::"+GLOBAL_SEARCH_TERM.length());
		logger.debug("pageNo::"+pageNo);
		logger.debug("iDisplayLength::"+pageSize);

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

			query ="select count(*) from"
					+ " (select BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,MAKER_ID ,MAKER_DTTM,rownum rn from"
					+ "  (SELECT  BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,MAKER_ID ,to_char(MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') MAKER_DTTM  FROM"
					+ "   BANK_MASTER where BANK_CODE like ? or BANK_NAME like ? or BIN like ? or BIN_DESC like ?  order by BANK_CODE)) ";
			
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(2,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(3,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(4,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
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

			query ="select BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,STATUS,MAKER_ID ,MAKER_DTTM from"
					+ " (select BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,STATUS,MAKER_ID ,MAKER_DTTM,rownum rn from"
					+ "  (SELECT  BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,DECODE(STATUS, NULL, 'Active','A','Active','B','Blocked',STATUS) STATUS,MAKER_ID ,to_char(MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') MAKER_DTTM  FROM"
					+ "   BANK_MASTER where BANK_CODE like ? or BANK_NAME like ? or BIN like ? or BIN_DESC like ?  order by BANK_CODE)) "+
					" where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE);
			
				
			System.out.println(query);
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(2, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(3, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(4, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
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
				ja.put("<p>  &nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='bin-active-deactive'  title='Bin Active/De-Active' data-rel='tooltip'><i class='icon icon-calendar icon-white'></i></a>&nbsp;</p>");

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


				sql ="select BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,STATUS,MAKER_ID ,MAKER_DTTM from"
						+ " (select BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,STATUS,MAKER_ID ,MAKER_DTTM,rownum rn from"
						+ "  (SELECT  BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,DECODE(STATUS, NULL, 'Active','A','Active','B','Blocked',STATUS) STATUS,MAKER_ID ,to_char(MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') MAKER_DTTM  FROM"
						+ "   BANK_MASTER order by BANK_CODE)) "+
						" where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE);
				
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
				String status=rs.getString(5);
				String labelInfo="";
				
				//System.out.println("status::"+status);
				if(status.equals("Active"))
					labelInfo="icon icon-locked icon-white";
				else
					labelInfo="icon icon-unlocked icon-white";
				
				//System.out.println("labelInfo::"+labelInfo);
				
				ja.put("<p>  &nbsp;<a class='btn btn-success' href='#' index='"+i+"' id='bin-active-deactive'  title='Bin Active/De-Active' data-rel='tooltip'><i class='"+labelInfo+"'></i></a>&nbsp;</p>");


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
		
		sql ="select count(*) from (select BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,MAKER_ID ,MAKER_DTTM,rownum rn from"
				+ "  (SELECT  BANK_CODE ,BANK_NAME ,BIN ,BIN_DESC ,MAKER_ID ,to_char(MAKER_DTTM,'D-MM-YYYY HH24:MI:SS') MAKER_DTTM  "
				+ "FROM   BANK_MASTER order by BANK_CODE)) ";
	
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