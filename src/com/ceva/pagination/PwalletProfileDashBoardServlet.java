package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class PwalletProfileDashBoardServlet extends HttpServlet {

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
	Logger logger = Logger.getLogger(PwalletProfileDashBoardServlet.class);

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

			query ="select count(*) from (select FULLNAME,MSISDN,EMAILADDRESS,CREATEDBY,DATECREATED,PROFILEID,rownum rn from  "
					+ "(select wp.FULLNAME FULLNAME,wp.MSISDN MSISDN,wp.EMAILADDRESS EMAILADDRESS,wp.CREATEDBY CREATEDBY,  "
					+ "to_char(wp.DATECREATED,'DD-MON-YYYY HH24:MI:SS') DATECREATED,  wp.PROFILEID PROFILEID "
					+ "from W_PROFILES wp where  wp.PROFILEID not in (select PROFILEID from W_ORGANIZATION_PROFILES)  "
					+ "and (wp.PROFILEID like ? or wp.MSISDN like ? or wp.FULLNAME like ?) order by DATECREATED)) ";
			
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(2,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(3,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
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

			query ="select FULLNAME,MSISDN,EMAILADDRESS,CREATEDBY,DATECREATED,PROFILEID from "
					+ "(select FULLNAME,MSISDN,EMAILADDRESS,CREATEDBY,DATECREATED,PROFILEID,rownum rn from "
					+ " (select wp.FULLNAME FULLNAME,wp.MSISDN MSISDN,wp.EMAILADDRESS EMAILADDRESS,wp.CREATEDBY CREATEDBY,  "
					+ "to_char(wp.DATECREATED,'DD-MON-YYYY HH24:MI:SS') DATECREATED,  wp.PROFILEID PROFILEID "
					+ "from W_PROFILES wp where  wp.PROFILEID not in (select PROFILEID from W_ORGANIZATION_PROFILES)  "
					+ "and (wp.PROFILEID like ? or wp.MSISDN like ? or wp.FULLNAME like ?) order by DATECREATED)) "+
					" where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE);
				
				
			System.out.println(query);
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(2, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(3, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			ResultSet rs = stmt.executeQuery();
			int i=1;
			while (rs.next()) {
				JSONArray ja = new JSONArray();
				ja.put(i);
				ja.put(rs.getString(6));
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(4));
				ja.put(rs.getString(5));
				ja.put("<p><a class='btn btn-warning' href='#' id='edit-profile' index='"+i+"' title='Edit Profile' data-toggle='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;<a class='btn btn-info' href='#' id='view-profile' index='"+i+"' title='View Profile' data-toggle='tooltip'><i class='icon icon-page icon-white'></i></a>  &nbsp;<a class='btn btn-warning' href='#' id='profile-account-credit' index='"+i+"' title='Profile Account Credit' data-toggle='tooltip'><i class='icon icon-book icon-white'></i></a><a class='btn btn-info' href='#' id='profile-pin-reset' index='"+i+"' title='Profile Pin Reset' data-toggle='tooltip'><i class='icon icon-key icon-white'></i></a></p>");

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


				sql ="select FULLNAME,MSISDN,EMAILADDRESS,CREATEDBY,DATECREATED,PROFILEID from "
						+ "(select FULLNAME,MSISDN,EMAILADDRESS,CREATEDBY,DATECREATED,PROFILEID,rownum rn from  "
						+ "(select wp.FULLNAME FULLNAME,wp.MSISDN MSISDN,wp.EMAILADDRESS EMAILADDRESS,wp.CREATEDBY CREATEDBY,  "
						+ "to_char(wp.DATECREATED,'DD-MON-YYYY HH24:MI:SS') DATECREATED,  wp.PROFILEID PROFILEID "
						+ "from W_PROFILES wp where wp.PROFILEID not in (select PROFILEID from W_ORGANIZATION_PROFILES) order by DATECREATED))"+
						" where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE);
				
						stmt=con.prepareStatement(sql);

			System.out.println(sql);
			
			ResultSet rs = stmt.executeQuery();
			int i=1;
			while (rs.next()) {
				JSONArray ja = new JSONArray();
				ja.put(i);
				ja.put(rs.getString(6));
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(4));
				ja.put(rs.getString(5));
				ja.put("<p><a class='btn btn-warning' href='#' id='edit-profile' index='"+i+"' title='Edit Profile' data-toggle='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;<a class='btn btn-info' href='#' id='view-profile' index='"+i+"' title='View Profile' data-toggle='tooltip'><i class='icon icon-page icon-white'></i></a>  &nbsp;<a class='btn btn-warning' href='#' id='profile-account-credit' index='"+i+"' title='Profile Account Credit' data-toggle='tooltip'><i class='icon icon-book icon-white'></i></a><a class='btn btn-info' href='#' id='profile-pin-reset' index='"+i+"' title='Profile Pin Reset' data-toggle='tooltip'><i class='icon icon-key icon-white'></i></a></p>");


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
		
		sql ="select count(*) from (select FULLNAME,MSISDN,EMAILADDRESS,CREATEDBY,DATECREATED,PROFILEID,rownum rn from  "
				+ "(select wp.FULLNAME FULLNAME,wp.MSISDN MSISDN,wp.EMAILADDRESS EMAILADDRESS,wp.CREATEDBY CREATEDBY,  "
				+ "to_char(wp.DATECREATED,'DD-MON-YYYY HH24:MI:SS') DATECREATED,  wp.PROFILEID PROFILEID "
				+ "from W_PROFILES wp where wp.PROFILEID not in (select PROFILEID from W_ORGANIZATION_PROFILES) order by DATECREATED)) ";
	
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