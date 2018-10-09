package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class UserDashBoardServelt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;
	
	Logger logger = Logger.getLogger(UserDashBoardServelt.class);

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
		logger.debug("Connection :::"+con);
		
		int intiaCnt= 0;
		
		if(INITIAL == 0)
			intiaCnt=INITIAL;
		else
			intiaCnt =INITIAL+1;
		
		String sql ="select gid,lud,gn,mkr,mkd,ckr,ckd from ( select gid,lud,gn,mkr,mkd,ckr,ckd,rownum rn from ( SELECT UG.GROUP_ID gid,ULC.LOGIN_USER_ID lud,UG.GROUP_NAME gn,ULC.MAKER_ID mkr,to_char(ULC.MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS') mkd, UG.MAKER_ID ckr,to_char(UG.MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS') ckd FROM  USER_GROUPS UG,USER_LOGIN_CREDENTIALS ULC,USER_INFORMATION UI WHERE UI.COMMON_ID = ULC.COMMON_ID and   UI.USER_GROUPS = UG.GROUP_ID and UG.APPL_CODE='AgencyBanking' ORDER BY UG.GROUP_ID,ULC.LOGIN_USER_ID)) "+
               " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE);
		
		System.out.println(sql);
		PreparedStatement stmt = con.prepareStatement(sql);
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
			array.put(ja); 
			i++;
		}
		rs.close();
		stmt.close();
		con.close();

		String query = "SELECT " + "COUNT(*) as count " + "FROM " + "TBL_TRANLOG " + "WHERE ";
		//for pagination
		if (GLOBAL_SEARCH_TERM != "") {
			query += searchSQL;
			PreparedStatement st = con.prepareStatement(query);
			ResultSet results = st.executeQuery();
			if (results.next()) {
				totalAfterSearch = results.getInt("count");
			}
			results.close();
			st.close();
			con.close();
		}
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
		String sql ="select count(*) from ( select gid,lud,gn,mkr,mkd,ckr,ckd,rownum rn from ( SELECT UG.GROUP_ID gid,ULC.LOGIN_USER_ID lud,UG.GROUP_NAME gn,ULC.MAKER_ID mkr,to_char(ULC.MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS') mkd, UG.MAKER_ID ckr,to_char(UG.MAKER_DTTM,'DD-MON-YYYY HH24:MI:SS') ckd FROM  USER_GROUPS UG,USER_LOGIN_CREDENTIALS ULC,USER_INFORMATION UI WHERE UI.COMMON_ID = ULC.COMMON_ID and   UI.USER_GROUPS = UG.GROUP_ID and UG.APPL_CODE='AgencyBanking' ORDER BY UG.GROUP_ID,ULC.LOGIN_USER_ID))";
		
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