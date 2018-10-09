package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class ReportsDAO {

	private static Logger logger = Logger.getLogger(ReportsDAO.class);

	ResponseDTO responseDTO = null;
	JSONObject reqJSON = null;
	JSONObject mJSON = null;

	public ResponseDTO getData(RequestDTO reqDTO) {
		logger.debug("Inside GetData Of Tracking...");
		Connection connection = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;

		HashMap<String, Object> trackedMap = null;

		String txnCntQuery = "";
		String fromDate = null;
		String toDate = null;
		String userid = null;
		try {
			trackedMap = new HashMap<String, Object>();
			reqJSON = reqDTO.getRequestJSON();
			responseDTO = new ResponseDTO();

			logger.debug("Request JSON [" + reqJSON + "]");

			fromDate = reqJSON.getString("fromDate");
			toDate = reqJSON.getString("toDate");
			userid = reqJSON.getString("userID");
			
			

			connection = connection == null ? DBConnector.getConnection():connection;
			if (userid != null && !userid.isEmpty())
			{	
				txnCntQuery = "select count(*) from AUDIT_TRAIL "
						+ "WHERE (trunc(DATETIME) between to_date(?,'DD/MM/YYYY') and to_date(?,'DD/MM/YYYY')) "
						+ "and  NET_ID=upper(?) ";
				
				preStmt = connection.prepareStatement(txnCntQuery);
				preStmt.setString(1, fromDate);
				preStmt.setString(2, toDate);
				preStmt.setString(3, userid);
				logger.debug("getData [txnCntQuery: "+txnCntQuery+" ]");
			}
			else
			{
				txnCntQuery = "select count(*) from AUDIT_TRAIL "
						+ "WHERE (trunc(DATETIME) between to_date(?,'DD/MM/YYYY') and to_date(?,'DD/MM/YYYY')) ";
				
				preStmt = connection.prepareStatement(txnCntQuery);
				preStmt.setString(1, fromDate);
				preStmt.setString(2, toDate);
				logger.debug("getData [txnCntQuery: "+txnCntQuery+" ]");
			}
				

			rs = preStmt.executeQuery();
			int resCount = 0;
			if (rs.next()) {
				resCount = rs.getInt(1);
			}
			logger.debug("RESULT COUNT OF RECRDS : " + resCount);
			if (resCount > 0) { 
				mJSON = getDetails(fromDate, toDate, userid);
				logger.debug("Genereated JSON is :: " + mJSON);
				trackedMap.put(CevaCommonConstants.TXN_DATA, mJSON);
				responseDTO.setData(trackedMap);
			}
		} catch (Exception e) {
			logger.debug(" The Exception inside getData is  [" + e.getMessage()
					+ "]");
			e.printStackTrace();
			responseDTO.addError("Internal error occured, while executing.");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(preStmt);
			DBUtils.closeConnection(connection);

			trackedMap = null;

			txnCntQuery = null;
			fromDate = null;
			toDate = null;
			userid = null;
		}
		return responseDTO;
	}

	public JSONObject getDetails(String fromDate, String toDate, String userid) {
		JSONObject jres = null;
		JSONArray reportsList = null;
		Connection connection = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
		JSONObject txn = null; 
		String query = "";
		try {
			jres = new JSONObject();
			reportsList = new JSONArray();
			connection = DBConnector.getConnection(); 
			
			if (userid != null && !userid.isEmpty())
			{
				query =	"select A.CHANNEL_ID,nvl(A.NET_ID,' '),to_char(A.DATETIME,'DD-MON-YYYY HH24:MI:SS'),nvl(CML.MENU_NAME,' '),nvl(A.DATA_1,' '),"
						+"nvl(A.CARD_NO,' '),nvl(A.MESSAGE,' ') from AUDIT_TRAIL A,CEVA_MENU_LIST CML  WHERE CML.MENU_ACTION=A.TRANS_CODE"
						+" and ((trunc(A.DATETIME) between to_date(?,'DD/MM/YYYY') and to_date(?,'DD/MM/YYYY' ) ) and A.NET_ID=upper(?))"
						+"and rownum<200 order by A.DATETIME desc";	
				
				preStmt = connection.prepareStatement(query);

				preStmt.setString(1, fromDate);
				preStmt.setString(2, toDate);
				preStmt.setString(3, userid);
			}
			else
			{
				query =	"select A.CHANNEL_ID,nvl(A.NET_ID,' '),to_char(A.DATETIME,'DD-MON-YYYY HH24:MI:SS'),nvl(CML.MENU_NAME,' '),nvl(A.DATA_1,' '),"
						+"nvl(A.CARD_NO,' '),nvl(A.MESSAGE,' ') from AUDIT_TRAIL A,CEVA_MENU_LIST CML  WHERE CML.MENU_ACTION=A.TRANS_CODE"
						+" and ((trunc(A.DATETIME) between to_date(?,'DD/MM/YYYY') and to_date(?,'DD/MM/YYYY' ) ) )"
						+"and rownum<200 order by A.DATETIME desc";	
				
				preStmt = connection.prepareStatement(query);

				preStmt.setString(1, fromDate);
				preStmt.setString(2, toDate);
			}
			

			logger.debug("getDetails [query: "+query+" ]");
			rs = preStmt.executeQuery();
			txn = new JSONObject();
			while (rs.next()) {				 
				txn.put("channel", rs.getString(1));
				txn.put("netID", rs.getString(2));
				txn.put("datetime", rs.getString(3));
				txn.put("action", rs.getString(4));
				txn.put("accessingIP", rs.getString(5));
				txn.put("card_no", rs.getString(6));
				txn.put("message", rs.getString(7));
				reportsList.add(txn);
				txn.clear();				
			}

			jres.put("reportsList", reportsList);

 		} catch (Exception e) {
			logger.debug(" The Exception inside getDetails is  ["
					+ e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(rs); 
			DBUtils.closePreparedStatement(preStmt);
			DBUtils.closeConnection(connection);
 			reportsList = null;
		}
		return jres;
	}
}
