package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class SMSTransactionsServlet extends HttpServlet {

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
	Logger logger = Logger.getLogger(SMSTransactionsServlet.class);

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

			query ="SELECT count(*) FROM CIC_TWOWAYSMS_REQ_LOG TL WHERE (TL.REQUEST_FROM like ? or TL.REQUEST_TEXT like ? ) ";
					/*+ "and NOT EXISTS   (SELECT 1 "
					+" FROM CIC_CALL_SMS_REQ_LOG SL WHERE SL.REQUESTED_DATE=TL.REQUESTED_DATE AND SL.REQUEST_FROM    =TL.REQUEST_FROM   AND "
					+" SL.REQUEST_TEXT    =TL.REQUEST_TEXT ) ORDER BY TL.REQUESTED_DATE DESC ";*/
					
			logger.debug("serach count qery::"+query);
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			st.setString(2,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
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
			
			query="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS from "
		 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid, rownum rn from (select c1.AIRTIME_ID, c1.POLICY_ID,c1.AMOUNT,c1.DATE_CREATED, c1.STATUS,"
		 		+ " ( select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16)))  "
		 		+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID ) mpid "
				+ " from cic_c2b_airtime_log c1  "
				+ " where (c1.policy_id like ? or c1.status like ? ) order by date_created desc ) )"
				+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
			
			 query = " select RD, RF, RT, RTT from ( select RD, RF, RT, RTT, rownum rn from "
			 		+ " ( select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY') RD, TL.REQUEST_FROM RF, TL.REQUEST_TEXT RT, TL.REQUEST_TO RTT "
		    		+ " from CIC_TWOWAYSMS_REQ_LOG  TL "
		    		/*+ " where trunc(requested_date)=trunc(current_date) and not exists (select 1 from CIC_CALL_SMS_REQ_LOG SL"*/
		    		+ " where (TL.REQUEST_FROM like ? or TL.REQUEST_TEXT like ?) and not exists (select 1 from CIC_CALL_SMS_REQ_LOG SL"
		    		+ " where SL.REQUESTED_DATE=TL.REQUESTED_DATE and SL.REQUEST_FROM=TL.REQUEST_FROM"
		    		+ "	and SL.REQUEST_TEXT=TL.REQUEST_TEXT ) order by TL.REQUESTED_DATE desc ) ) "
		    		+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
				
			System.out.println(query);
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			stmt.setString(2, "%" + GLOBAL_SEARCH_TERM.trim() + "%");
			ResultSet rs = stmt.executeQuery();
			int i=1;
			String uId1="";
			String uId2="";
			String uId3="";
			while (rs.next()) {
				uId1="";
				uId2="";
				uId3="";
				JSONArray ja = new JSONArray();
				//uId=rs.getString(2)+"$"+rs.getString(1);
				uId1="Repl"+"$"+rs.getString(2)+"$"+rs.getString(1);
				uId2="Edit"+"$"+rs.getString(2)+"$"+rs.getString(1);
				uId3="Notr"+"$"+rs.getString(2)+"$"+rs.getString(1);
				ja.put(i);
				ja.put(rs.getString(1));
				//ja.put("<a href='#' id='"+uId+"' value='"+uId+"' >"+rs.getString(1)+"</a>");
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
				ja.put("<p>  &nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='"+uId1+"'  title='Reply' data-rel='tooltip'><i class='icon icon-reply icon-white'></i></a>&nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='"+uId2+"'  title='Edit' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='"+uId3+"'  title='Not Relevant' data-rel='tooltip'><i class='icon icon-cancel icon-white'></i></a>&nbsp;</p>");
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

			
				sql ="select to_char(DATE_CREATED,'HH:MI AM Month DD, YYYY'),POLICY_ID, AMOUNT, STATUS from "
				 		+ " ( select DATE_CREATED,POLICY_ID, AMOUNT, STATUS,AIRTIME_ID,mpid, rownum rn from (select c1.AIRTIME_ID, c1.POLICY_ID,c1.AMOUNT,c1.DATE_CREATED, c1.STATUS,"
				 		+ " ( select substr(c2.request_data,(instr(c2.request_data,'providerRefId',1,1)+16),((instr(c2.request_data,'providerMetadata',1,1)-3)-(instr(c2.request_data,'providerRefId',1,1)+16))) "
				 		+ " from cic_c2b_log c2 where c2.C2B_LOG_ID=c1.C2B_LOG_ID ) mpid "
						+ " from cic_c2b_airtime_log c1  "
						+ " order by date_created desc ) )"
						+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;	
				
				sql = " select RD, RF, RT, RTT from ( select RD, RF, RT, RTT, rownum rn from "
				 		+ " ( select to_char(TL.REQUESTED_DATE,'HH:MI AM Month DD, YYYY') RD, TL.REQUEST_FROM RF, TL.REQUEST_TEXT RT, TL.REQUEST_TO RTT "
			    		+ " from CIC_TWOWAYSMS_REQ_LOG  TL "
			    		/*+ " where trunc(requested_date)=trunc(current_date) and not exists (select 1 from CIC_CALL_SMS_REQ_LOG SL"*/
			    		+ " where not exists (select 1 from CIC_CALL_SMS_REQ_LOG SL"
			    		+ " where SL.REQUESTED_DATE=TL.REQUESTED_DATE and SL.REQUEST_FROM=TL.REQUEST_FROM"
			    		+ "	and SL.REQUEST_TEXT=TL.REQUEST_TEXT ) order by TL.REQUESTED_DATE desc ) ) "
			    		+ " where rn between " + intiaCnt + " and " + (INITIAL+RECORD_SIZE) ;
				
						stmt=con.prepareStatement(sql);

			System.out.println(sql);
			
			ResultSet rs = stmt.executeQuery();
			int i=1;
			String uId1="";
			String uId2="";
			String uId3="";
			while (rs.next()) {
				//uId="";
				JSONArray ja = new JSONArray();
				//uId=rs.getString(2)+"$"+rs.getString(1);
				uId1="Repl"+"$"+rs.getString(2)+"$"+rs.getString(1);
				uId2="Edit"+"$"+rs.getString(2)+"$"+rs.getString(1);
				uId3="Notr"+"$"+rs.getString(2)+"$"+rs.getString(1);
				ja.put(i);
				ja.put(rs.getString(1));
				//ja.put("<a href='#' id='"+uId+"' value='"+uId+"' >"+rs.getString(2)+"</a>");
				ja.put(rs.getString(2));
				ja.put(rs.getString(3));
				ja.put(rs.getString(4));
				ja.put("<p>  &nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='"+uId1+"'  title='Reply' data-rel='tooltip'><i class='icon icon-reply icon-white'></i></a>&nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='"+uId2+"'  title='Edit' data-rel='tooltip'><i class='icon icon-edit icon-white'></i></a>&nbsp;<a class='btn btn-primary' href='#' index='"+i+"' id='"+uId3+"'  title='Not Relevant' data-rel='tooltip'><i class='icon icon-cancel icon-white'></i></a>&nbsp;</p>");
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
		
		
			sql ="select count(*) from cic_c2b_airtime_log ";
			
			sql ="SELECT count(*) FROM CIC_TWOWAYSMS_REQ_LOG TL WHERE NOT EXISTS   (SELECT 1 "
					+" FROM CIC_CALL_SMS_REQ_LOG SL WHERE SL.REQUESTED_DATE=TL.REQUESTED_DATE AND SL.REQUEST_FROM    =TL.REQUEST_FROM   AND "
					+" SL.REQUEST_TEXT    =TL.REQUEST_TEXT ) ";
			
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