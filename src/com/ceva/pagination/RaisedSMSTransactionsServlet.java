package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class RaisedSMSTransactionsServlet extends HttpServlet {

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
	Logger logger = Logger.getLogger(RaisedSMSTransactionsServlet.class);

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
		String status = request.getParameter("status");
		GLOBAL_SEARCH_TERM = request.getParameter("sSearch");
		makerId = (String) session.getAttribute("makerId");
		location = (String) session.getAttribute("location");
		logger.debug("Login Id::"+makerId +"        colIndex::"+colIndex);
		logger.debug("Location::"+location);

		logger.debug("Search Val::"+GLOBAL_SEARCH_TERM +"   length::"+GLOBAL_SEARCH_TERM.length());
		logger.debug("pageNo::"+pageNo);
		logger.debug("iDisplayLength::"+pageSize);
		logger.debug("status::"+status);

		

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
			totalRecords = getTotalRecordCount(status);
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
		String status = request.getParameter("status");
		Connection con = null;
		con= con == null ? DBConnector.getConnection() : con;
		logger.debug("Connection :::"+con+" GLOBAL_SEARCH_TERM::::"+GLOBAL_SEARCH_TERM);
		logger.debug("status :::"+status);

		int intiaCnt= 0;

		String query = "SELECT " + "COUNT(*) as count " + "FROM " + "TBL_TRANLOG " + "WHERE ";
		//for pagination
		if (GLOBAL_SEARCH_TERM != null && GLOBAL_SEARCH_TERM.length()>0) {

			logger.debug("Inside if condition...");
			
			query ="SELECT count(*) FROM CIC_CALL_SMS_REQ_LOG TL WHERE TL.status= ? and (TL.REQUEST_FROM like ? or TL.REQUEST_TEXT like ? ) ";
					/*+ "and NOT EXISTS   (SELECT 1 "
					+" FROM CIC_CALL_SMS_REQ_LOG SL WHERE SL.REQUESTED_DATE=TL.REQUESTED_DATE AND SL.REQUEST_FROM    =TL.REQUEST_FROM   AND "
					+" SL.REQUEST_TEXT    =TL.REQUEST_TEXT ) ORDER BY TL.REQUESTED_DATE DESC ";*/
					
			logger.debug("serach count qery::"+query);
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  status);
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

			
			 query = " select RQ, RD, RF, RT, RTT, RM, IB, DI from ( select RD, RF, RT, RTT, RM, IB, DI, RQ, rownum rn from "
			 		+ " ( select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY') RD, TL.REQUEST_FROM RF, TL.REQUEST_TEXT RT, TL.REQUEST_TO RTT, "
		    		+ " nvl(TL.R_REMARKS,'-') RM, RAISED_BY IB, to_char(TL.RAISED_DATE,'HH:MI AM Month DD, YYYY') DI, TL.REQ_ID RQ from CIC_CALL_SMS_REQ_LOG  TL "
		    		+ " where TL.status = ? and (TL.REQUEST_FROM like ? or TL.REQUEST_TEXT like ?)  order by TL.RAISED_DATE desc ) ) "
		    		+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
				
			System.out.println(query);
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, status);
			stmt.setString(2, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(3, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			ResultSet rs = stmt.executeQuery();
			int i=1;
			String uId="";
			while (rs.next()) {
				uId="";
				JSONArray ja = new JSONArray();
				uId=rs.getString(1);
				ja.put(i);
				logger.debug("while status::"+status);
				if ("SC".equalsIgnoreCase(status))
				{	
					ja.put(rs.getString(1));
				}
				else
				{	
					ja.put("<a href='#' id='"+uId+"' value='"+uId+"' >"+rs.getString(1)+"</a>");
				}
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
				
				sql = " select RQ, RD, RF, RT, RTT, RM, IB, DI  from ( select RD, RF, RT, RTT, RM, IB, DI, RQ,  rownum rn from "
				 		+ " ( select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY') RD, TL.REQUEST_FROM RF, TL.REQUEST_TEXT RT, TL.REQUEST_TO RTT, "
			    		+ " nvl(TL.R_REMARKS,'-') RM, RAISED_BY IB, to_char(TL.RAISED_DATE,'HH:MI AM Month DD, YYYY') DI, TL.REQ_ID RQ from CIC_CALL_SMS_REQ_LOG  TL "
			    		+ " where TL.status=? order by TL.RAISED_DATE desc ) ) "
			    		+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
				
						stmt=con.prepareStatement(sql);
						stmt.setString(1, status);
						
			System.out.println(sql);
			
			ResultSet rs = stmt.executeQuery();
			int i=1;
			String uId="";
			while (rs.next()) {
				uId="";
				JSONArray ja = new JSONArray();
				uId=rs.getString(1);
				ja.put(i);
				logger.debug("while status::"+status);
				if ("SC".equalsIgnoreCase(status))
				{	
					ja.put(rs.getString(1));
				}
				else
				{	
					ja.put("<a href='#' id='"+uId+"' value='"+uId+"' >"+rs.getString(1)+"</a>");
				}
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

	public int getTotalRecordCount(String status) throws SQLException {
		int totalRecords = -1;
		String sql ="";		
		Connection con =null;
/*		HttpServletRequest request = null;
		String status = request.getParameter("status");*/
		con= con == null ? DBConnector.getConnection() : con;
		PreparedStatement stmt = null;
		
			
			sql ="SELECT count(*) FROM CIC_CALL_SMS_REQ_LOG TL where TL.status = ?";
			logger.debug("status ["+status+"] sql ["+sql+"]");
			
					stmt=con.prepareStatement(sql);
					stmt.setString(1, status);
	
		
		
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