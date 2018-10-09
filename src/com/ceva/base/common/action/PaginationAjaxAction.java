package com.ceva.base.common.action;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.*;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import com.ceva.base.common.utils.DBConnector;
import com.opensymphony.xwork2.ActionSupport;

public class PaginationAjaxAction extends ActionSupport {

	/**
	 * User Management Pagination
	 * @author Ravi Dirisam
	 * @Date : 10-05-2016
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(PaginationAjaxAction.class);

	private String result;
	
	private String GLOBAL_SEARCH_TERM;
	private int INITIAL;
	private int RECORD_SIZE;
	
	public String getUserGroupDetails() throws Exception
	{
		logger.debug("inside gtPagination....   ");
		try {
			
			JSONObject jsonResult = new JSONObject();
			int listDisplayAmount = 10;
			int start = 0;
			int column = 0;
			String dir = "asc";
			String pageNo = ServletActionContext.getRequest().getParameter("iDisplayStart");
			String pageSize = ServletActionContext.getRequest().getParameter("iDisplayLength");
			String colIndex = ServletActionContext.getRequest().getParameter("iSortCol_0");
			String sortDirection = ServletActionContext.getRequest().getParameter("sSortDir_0");
			
			
			
			
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
				totalRecords = getTotalRecordCount();
			
			
			logger.debug("totalRecords:::"+totalRecords);
			System.out.println("totalRecords:::"+totalRecords);
			
			RECORD_SIZE = listDisplayAmount;
			GLOBAL_SEARCH_TERM = ServletActionContext.getRequest().getParameter("sSearch");
			INITIAL = start;
			
				jsonResult = getPersonDetails(totalRecords);
				logger.debug("jsonResult:::"+jsonResult);
				HttpServletResponse response = ServletActionContext.getResponse();
			//responseJSON=jsonResult;
				PrintWriter out = response.getWriter();
				out.print(jsonResult);
				out.flush();
				out.close();
			
			result = "success";
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetProcessingCodeViewDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			
		}

		return result;
	}
	
	
	
	
	
	public JSONObject getPersonDetails(int totalRecords) {
		int totalAfterSearch = totalRecords;
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		
		Connection con = null;
		PreparedStatement stmt =null;
		ResultSet rs =null;
		try{
		con = DBConnector.getConnection();
		System.out.println("Connection :::"+con);
		
		int intiaCnt= 0;
		
		if(INITIAL == 0){
			intiaCnt=INITIAL;
		}else{
			intiaCnt =INITIAL+1;
		}
		String sql ="";
		if (GLOBAL_SEARCH_TERM != "") {
			
			System.out.println("kailash 11");
			System.out.println("intiaCnt  :: "+intiaCnt);
			System.out.println("intiaCnt 11 :: "+INITIAL+RECORD_SIZE);
			
			sql ="SELECT IDX,BANK_CODE,OLD_BANK_CODE,BANK_NAME,BRANCH,SWIFT_CODE,decode(STATUS,'1','Internal Nbk Bank','External Bank') STATUS from (SELECT IDX,BANK_CODE,OLD_BANK_CODE,BANK_NAME,BRANCH,SWIFT_CODE,decode(STATUS,'1','Internal Nbk Bank','External Bank') STATUS,rownum rn from "+
					" (SELECT IDX,BANK_CODE,OLD_BANK_CODE,BANK_NAME,BRANCH,SWIFT_CODE,decode(STATUS,'1','Internal Nbk Bank','External Bank') STATUS"+
					" FROM TBBANKS where BANK_CODE like '%"+GLOBAL_SEARCH_TERM+"%' )) where rn  between "+
					+ intiaCnt + " and " + (INITIAL+RECORD_SIZE);
		}else{
			
			System.out.println("kailash 22");
			System.out.println("intiaCnt  :: "+intiaCnt);
			System.out.println("intiaCnt 11 :: "+INITIAL+RECORD_SIZE);
			
			sql ="SELECT IDX,BANK_CODE,OLD_BANK_CODE,BANK_NAME,BRANCH,SWIFT_CODE,decode(STATUS,'1','Internal Nbk Bank','External Bank') STATUS from (SELECT IDX,BANK_CODE,OLD_BANK_CODE,BANK_NAME,BRANCH,SWIFT_CODE,decode(STATUS,'1','Internal Nbk Bank','External Bank') STATUS,rownum rn from "+
					" (SELECT IDX,BANK_CODE,OLD_BANK_CODE,BANK_NAME,BRANCH,SWIFT_CODE,decode(STATUS,'1','Internal Nbk Bank','External Bank') STATUS"+
					" FROM TBBANKS )) where  rn  between "+
					+ intiaCnt + " and " + (INITIAL+RECORD_SIZE);
		}
		
		
		
		System.out.println(sql);
		stmt = con.prepareStatement(sql);
		rs = stmt.executeQuery();

		while (rs.next()) {
			JSONArray ja = new JSONArray();
			ja.put(rs.getString(1)==null?"":rs.getString(1));
			ja.put(rs.getString(2)==null?"":rs.getString(2));
			ja.put(rs.getString(3)==null?"":rs.getString(3));
			ja.put(rs.getString(4)==null?"":rs.getString(4));
			ja.put(rs.getString(5)==null?"":rs.getString(5));
			ja.put(rs.getString(6)==null?"":rs.getString(6));
			ja.put(rs.getString(7)==null?"":rs.getString(7));
			System.out.println(ja);
			array.put(ja); 
		}
		

		String query = "SELECT " + "COUNT(*) as count " + "FROM " + "TBBANKS " + "WHERE BANK_CODE like '%";
		//for pagination
		System.out.println("query :: "+query);
		System.out.println("GLOBAL_SEARCH_TERM :: "+GLOBAL_SEARCH_TERM);
		
		if (GLOBAL_SEARCH_TERM != "") {
			query += GLOBAL_SEARCH_TERM+"%'";
			System.out.println("query search  :: "+query);
			PreparedStatement st = con.prepareStatement(query);
			ResultSet results = st.executeQuery();
			if (results.next()) {
				totalAfterSearch = results.getInt("count");
			}
			
		}
		try {
			result.put("iTotalRecords", totalRecords);
			result.put("iTotalDisplayRecords", totalAfterSearch);
			result.put("aaData", array.toString());
			System.out.println("array  :: "+array);
		} catch (Exception e) {
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
			stmt.close();
			rs.close();
			con.close();
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
			System.out.println("result::"+result);
			return result;
	}

	public int getTotalRecordCount(){
		int totalRecords = -1;
		
			logger.debug("inside totla records Count");
			String sql ="SELECT count(*) as count FROM TBBANKS";
			Connection con =null;
			PreparedStatement statement =null;
			ResultSet resultSet =null;
		try{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
		con = DBConnector.getConnection();
		statement = con.prepareStatement(sql);
		resultSet = statement.executeQuery();

		if (resultSet.next()) {
			totalRecords = resultSet.getInt("count");
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
		resultSet.close();
		statement.close();
		con.close();
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
		return totalRecords;
	}

	
}
