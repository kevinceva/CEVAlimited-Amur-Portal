package com.ceva.pagination;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.*;

public class UserManagementPagination extends HttpServlet {

	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
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
		RECORD_SIZE = listDisplayAmount;
		GLOBAL_SEARCH_TERM = request.getParameter("sSearch");
		INITIAL = start;
		try {
			jsonResult = getPersonDetails(totalRecords, request);
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
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("totalRecords:::"+totalRecords);
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "postadb", "postadb");
		System.out.println("Connection :::"+con);
		
		int intiaCnt= 0;
		
		if(INITIAL == 0)
			intiaCnt=INITIAL;
		else
			intiaCnt =INITIAL+1;
		
		
		String sql = "SELECT GROUP_ID,GROUP_NAME,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS')  "
						+ "from (SELECT GROUP_ID,GROUP_NAME,MAKER_ID,MAKER_DTTM,rownum rn "
						+ "from  (SELECT GROUP_ID,GROUP_NAME,MAKER_ID,MAKER_DTTM  FROM USER_GROUPS  order by GROUP_ID)) where  rn  between "+
				+ intiaCnt + " and " + (INITIAL+RECORD_SIZE);
		System.out.println(sql);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		int i=1;
		while (rs.next()) {
			JSONArray ja = new JSONArray();
			ja.put(i);
			ja.put("<a href='#' id='USERS~"+rs.getString(1)+"' value='"+rs.getString(1)+"'>"+rs.getString(1)+"</a>");
			ja.put(rs.getString(2));
			ja.put(rs.getString(3));
			ja.put(rs.getString(4));
			ja.put("<a id='create-user' class='btn btn-info'  href='#' index='1' title='Create User' data-rel='tooltip' ><i class='icon icon-plus icon-white'></i></a>&nbsp;<a id='modify-group'  class='btn btn-warning' href='#' index='1'  title='Modify Group' data-rel='tooltip' ><i class='icon icon-edit icon-white'></i></a> &nbsp;&nbsp;<a id='view-group' class='btn btn-success' index='1'  href='#' title='View Group' data-rel='tooltip' ><i class='icon icon-book icon-white'></i></a>&nbsp;");
			array.put(ja); 
			i++;
		}
		stmt.close();
		rs.close();

		String query = "SELECT " + "COUNT(*) as count " + "FROM " + "USER_GROUPS " + "WHERE ";
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
		String sql ="SELECT count(*) as count FROM USER_GROUPS order by GROUP_ID desc";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "postadb", "postadb");
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