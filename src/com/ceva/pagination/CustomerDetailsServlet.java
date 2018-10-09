package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class CustomerDetailsServlet extends HttpServlet {

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
	Logger logger = Logger.getLogger(CustomerDetailsServlet.class);

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
		logger.debug("Login Id::"+makerId +"        colIndex::"+colIndex);

		logger.debug("Search Val::"+GLOBAL_SEARCH_TERM +"   length::"+GLOBAL_SEARCH_TERM.length());
		logger.debug("pageNo::"+pageNo);
		logger.debug("iDisplayLength::"+pageSize);

		try {
			userType=getUserType().toUpperCase();
			logger.debug("userType::::"+userType);
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
		
		RECORD_SIZE = listDisplayAmount;

		INITIAL = start;
		try {
			jsonResult = fetchClientDetails(totalRecords, request);
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

	public JSONObject fetchClientDetails(int totalRecords, HttpServletRequest request) throws SQLException, ClassNotFoundException {
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

			query ="SELECT count(*) from CUSTOMER_MASTER "
			+ " WHERE fname like ? or lanme like ? or email_id like ?";
					
			
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

			
			
			
	/*		 query ="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS,AIRTIME_ID from "
			 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID, rownum rn from (select AIRTIME_ID,POLICY_ID,AMOUNT,DATE_CREATED, STATUS "
					+ " from cic_c2b_airtime_log where  "
					+ " (policy_id like ? or status like ? ) order by date_created desc ) )"
					+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;*/
			
			query =" SELECT NAME, EMAIL_ID, STATUS, CDT  from ( SELECT NAME, EMAIL_ID, STATUS, CDT, rownum rn from"
					+ " ( SELECT nvl((fname||' '||lname),'-') NAME, "
			+ " EMAIL_ID , STATUS, to_char(created_date,'dd/mm/yyyy') CDT from  CUSTOMER_MASTER "
			+ " where fname like ? or lname like ? or email_id like ? "
			+ " order by created_date desc ))"
			+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
			
			/*query="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid from "
		 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid, rownum rn from (select c1.AIRTIME_ID, c1.POLICY_ID,c1.AMOUNT,c1.DATE_CREATED, c1.STATUS,"
		 		+ " substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) mpid "
				+ " from cic_c2b_airtime_log c1, cic_c2b_log c2  "
				+ " where c2.C2B_LOG_ID=c1.C2B_LOG_ID and (c1.policy_id like ? or c1.status like ? or instr(c2.request_data,?)>0  ) order by c1.date_created desc ) )"
				+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;*/
				
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
				ja.put(rs.getString(1));
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
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

			
/*				sql ="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS,AIRTIME_ID, mpid from "
				 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid, rownum rn from (select c1.AIRTIME_ID, c1.POLICY_ID,c1.AMOUNT,c1.DATE_CREATED, c1.STATUS,"
				 		+ " ( select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) "
				 		+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID ) mpid "
						+ " from cic_c2b_airtime_log c1  "
						+ " order by date_created desc ) )"
						+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;	*/
			
			sql =" SELECT  NAME, EMAIL_ID, STATUS, CDT from ( SELECT  NAME, EMAIL_ID, STATUS, CDT, "
					+ "rownum rn from ( SELECT nvl((fname||' '||lname),'-') NAME, "
			+ " EMAIL_ID , STATUS, to_char(created_date,'dd/mm/yyyy') CDT from  CUSTOMER_MASTER "
			+ " order by created_date desc )) "
			+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
				
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
		logger.info("hhhhhhhhhhhhhhresult::"+result);
		
		return result;
	}

	public int getTotalRecordCount() throws SQLException {
		int totalRecords = -1;
		String sql ="";		
		Connection con =null;
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
		
		sql ="SELECT count(*) from customer_master ";
		
			/*sql ="select count(*) from cic_c2b_airtime_log ";*/
			
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

}