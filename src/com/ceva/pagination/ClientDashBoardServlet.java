package com.ceva.pagination;

import java.io.*;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.json.*;

import com.ceva.base.common.utils.DBConnector;

public class ClientDashBoardServlet extends HttpServlet {

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
	Logger logger = Logger.getLogger(ClientDashBoardServlet.class);

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

			query ="SELECT count(*) from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB "
			+ " WHERE CC.MOBILE_NUMBER=CB.POLICY_ID(+)"
			+ " and CC.MOBILE_NUMBER like ? ";
					
			
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1,  "%" + GLOBAL_SEARCH_TERM.trim() + "%");
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
			
			query =" SELECT MOBILE, NAME, IDNO, STATS, DTC, BMOB  from (SELECT MOBILE, NAME, IDNO, STATS, DTC, BMOB, rownum rn from ( SELECT CC.MOBILE_NUMBER MOBILE,"
			+ " replace((CC.FNAME||' '||CC.MNAME||' '||CC.LNAME),'''',' ') NAME, "
			+ " CC.ID_NUMBER,substr(CC.ID_NUMBER,1,1)||REGEXP_REPLACE(substr(CC.ID_NUMBER,2,2),'[A-Za-z0-9]', 'x')||substr(CC.ID_NUMBER,4,1)||REGEXP_REPLACE((substr(CC.ID_NUMBER,5,length(CC.ID_NUMBER)-5)),'[A-Za-z0-9]', 'x')||substr(CC.ID_NUMBER,-1,1) IDNO, "
			+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
			+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
			+ " 'BV','Client Verified and Beneficiary Verified', 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Verification In Progress'), "
			+ " 'CS','Active','Client or Beneficiary Verification In Progress') STATS, to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY') DTC, "
			+ " DECODE(NVL(CB.MOBILENUMBER,'-'),'-','-',substr(CB.MOBILENUMBER,1,6)||REGEXP_REPLACE(substr(CB.MOBILENUMBER,7,3),'[A-Za-z0-9]', 'x')||substr(CB.MOBILENUMBER,-3,3)) BMOB "
			+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB "
			+ " WHERE CC.CLIENT_ID=CB.CLIENT_ID(+)"
			+ " and CC.MOBILE_NUMBER like ? "
			+ " ORDER BY CC.DATE_CREATED DESC ) ) "
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
			
			sql =" SELECT MOBILE, NAME, IDNO, STATS, DTC, BMOB from (SELECT MOBILE, NAME, IDNO, STATS, DTC, BMOB, rownum rn from "
			+ " ( SELECT CC.MOBILE_NUMBER MOBILE,replace((CC.FNAME||' '||CC.MNAME||' '||CC.LNAME),'''',' ') NAME, "
			+ " substr(CC.ID_NUMBER,1,1)||REGEXP_REPLACE(substr(CC.ID_NUMBER,2,2),'[A-Za-z0-9]', 'x')||substr(CC.ID_NUMBER,4,1)||REGEXP_REPLACE((substr(CC.ID_NUMBER,5,length(CC.ID_NUMBER)-5)),'[A-Za-z0-9]', 'x')||substr(CC.ID_NUMBER,-1,1) IDNO, "
			+ " decode(CC.STATUS,'CC','Client Created','CVF','Client Verification Failed','CV',decode(CB.STATUS,NULL,'Client Verified But Beneficiary Not Registered', "
			+ " 'BR','Client Verified and Beneficiary Mobile Number Submitted', 'BC','Client Verified and Beneficiary ID Number Received',"
			+ " 'BV','Client Verified and Beneficiary Verified', 'BF','Beneficiary Verification Failed','Client Verified and Beneficiary Verification In Progress'), "
			+ " 'CS','Active','Client or Beneficiary Verification In Progress') STATS,to_char(CC.DATE_CREATED,'HH:MI AM Month DD, YYYY') DTC, "
			+ " DECODE(NVL(CB.MOBILENUMBER,'-'),'-','-',substr(CB.MOBILENUMBER,1,6)||REGEXP_REPLACE(substr(CB.MOBILENUMBER,7,3),'[A-Za-z0-9]', 'x')||substr(CB.MOBILENUMBER,-3,3)) BMOB "
			+ " from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB "
			+ " WHERE CC.CLIENT_ID=CB.CLIENT_ID(+)"
			+ " ORDER BY CC.DATE_CREATED DESC ) ) "
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
				ja.put(rs.getString(5));
				ja.put(rs.getString(6));
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
		
		
		sql ="SELECT count(*) from CIC_clients CC, CIC_CLIENT_BENEFICIARIES CB "
		+ " WHERE CC.MOBILE_NUMBER=CB.POLICY_ID(+)";
		
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

}