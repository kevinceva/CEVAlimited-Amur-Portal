package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class MarketsCheckerDAO {

	Logger logger=Logger.getLogger(MarketsCheckerDAO.class);
	
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONArray dataJsonArray = null;
	HashMap<String, Object> dataMap = null;
	Connection connection = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String qry ="";
	
	public ResponseDTO authRecordsCount(RequestDTO requestDTO) {
		
		qry="select STATUS,count(*) from ONLINE_PRODUCTS_MASTER group by STATUS";
		String status="";
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			pstmt = connection.prepareStatement(qry);
			rs = pstmt.executeQuery();
			
			dataJsonArray = new JSONArray();
			JSONObject json =null;
			while (rs.next()) {
				json = new JSONObject();
				
				if(rs.getString(1).equals("M"))
					status="Product Un-Authorized";
				else if(rs.getString(1).equals("A"))
					status="Product Authorized";
				else if(rs.getString(1).equals("R"))
					status="Product Rejected";
				
				json.put("STATUS", status);
				json.put("COUNT", rs.getString(2));
				
				dataJsonArray.add(json);
			}
			
			qry="select STATUS,count(*) from ONLINE_PRODUCT_OFFERS_DEALS group by STATUS";
			
			pstmt = connection.prepareStatement(qry);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				json = new JSONObject();
				if(rs.getString(1).equals("M"))
					status="Deal/Offer Un-Authorized";
				else if(rs.getString(1).equals("A"))
					status="Deal/Offer Authorized";
				else if(rs.getString(1).equals("R"))
					status="Deal/Offer Rejected";
				
				json.put("STATUS", status);
				json.put("COUNT", rs.getString(2));
				
				dataJsonArray.add(json);
			}
			
			
			logger.debug("Res JSON Array [" + dataJsonArray + "]");

			responseJSON.put("RESPONSE_DATA", dataJsonArray);
			dataMap.put("RESPONSE_DATA", responseJSON);

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in auth count [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in auth count [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}

	
	public ResponseDTO productInfoView(RequestDTO requestDTO) {
		
		qry="select OPM.PRODUCT_ID,OPM.PRODUCT_NAME,OPM.PRODUCT_PRICE,OPM.CATEGORY_ID,OCD.CATEGORY_DESC,  "
				+ "OPM.SUB_CATEGORY_ID,OSCD.SUB_CATEGORY_DESC,  decode(OPM.STATUS,'M','Un-Auth','A','Approved','R','Rejected',OPM.STATUS) STATUS,"
				+ "OPM.MAKER_ID,  to_char(OPM.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') MAKER_DTTM,CHECKER_ID, to_char(OPM.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') CHECKER_DTTM,OPM.COMMENTS "
				+ " from ONLINE_PRODUCTS_MASTER OPM,ONLINE_CATEGORY_DETAILS OCD,ONLINE_SUB_CATEGORY_DETAILS OSCD "
				+ " where OPM.CATEGORY_ID=OCD.CATEGORY_ID and OPM.SUB_CATEGORY_ID=OSCD.SUB_CATEGORY_ID  and "
				+ "OCD.CATEGORY_ID=OSCD.CATEGORY_ID and OPM.PRODUCT_ID=?";
		
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			pstmt = connection.prepareStatement(qry);
			pstmt.setString(1, requestJSON.getString("PRODUCT_ID"));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				responseJSON.put("PRODUCT_ID", rs.getString(1));
				responseJSON.put("PRODUCT_NAME", rs.getString(2));
				responseJSON.put("PRODUCT_PRICE", rs.getString(3)+" /=Kshs");
				responseJSON.put("CATEGORY_ID", rs.getString(4));
				responseJSON.put("CATEGORY_DESC", rs.getString(5));
				responseJSON.put("SUB_CATEGORY_ID", rs.getString(6));
				responseJSON.put("SUB_CATEGORY_DESC", rs.getString(7));
				responseJSON.put("STATUS", rs.getString(8));
				responseJSON.put("MAKER_ID", rs.getString(9));
				responseJSON.put("MAKER_DTTM", rs.getString(10));
				responseJSON.put("CHECKER_ID", rs.getString(11));
				responseJSON.put("CHECKER_DTTM", rs.getString(12));
				responseJSON.put("COMMENTS", rs.getString(13));
				
			}
			
			logger.debug("Res JSON  [" + responseJSON + "]");

			dataMap.put("RESPONSE_DATA", responseJSON);

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in auth count [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in auth count [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}


	public ResponseDTO productApproveOrReject(RequestDTO requestDTO) {
		
		qry="update ONLINE_PRODUCTS_MASTER set STATUS=?,COMMENTS=? where PRODUCT_ID=? ";
		
		String reason="Success";
		
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			
			if(requestJSON.getString("APPROVE_REJECT").equals("R")){
				reason=requestJSON.getString("REASON");
			}
			
			pstmt = connection.prepareStatement(qry);
			pstmt.setString(1, requestJSON.getString("APPROVE_REJECT"));
			pstmt.setString(2, reason);
			pstmt.setString(3, requestJSON.getString("PRODUCT_ID"));
			int cnt = pstmt.executeUpdate();
			
			connection.commit();
			
			if(cnt >0){
				responseJSON.put("STATUS", "Success");
			}else{
				responseDTO.addError("Updation Failed.");
			}
			
			
			logger.debug("Res JSON  [" + responseJSON + "]");

			dataMap.put("RESPONSE_DATA", responseJSON);

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in approve [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in approve [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}

	
	public ResponseDTO offersInfoView(RequestDTO requestDTO) {
		
		qry="select OPFD.ID ID,OPM.CATEGORY_ID,OCD.CATEGORY_DESC,OPM.SUB_CATEGORY_ID,OSCD.SUB_CATEGORY_DESC  ,"
				+ "OPM.PRODUCT_ID PRODUCT_ID,OPM.PRODUCT_NAME PRODUCT_NAME,OPM.PRODUCT_PRICE PRODUCT_PRICE,  "
				+ "decode(OPFD.OFFER_TYPE,'D','Deal','O','Offer',OPFD.OFFER_TYPE) OFFER_TYPE,  "
				+ "decode(OPFD.TIME_LIMIT,'Y','Yes','N','No',OPFD.TIME_LIMIT) TIME_LIMIT,  "
				+ "to_char(OPFD.END_DATE,'DD-MM-YYYY HH24:MI:SS') END_DATE,  "
				+ "decode(OPFD.DISCOUNT_CASHBACK,'D','Discount','C','Cash Back',OPFD.DISCOUNT_CASHBACK) DISCOUNT_CASHBACK,  "
				+ "OPFD.DIS_CASHBACK_PER DIS_CASHBACK_PER,  decode(OPFD.STATUS,'M','Un-Authorized','A','Approved','R','Rejected',OPFD.STATUS) STATUS,  "
				+ "OPFD.MAKER_ID MAKER_ID,to_char(OPFD.MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') MAKER_DTTM,  "
				+ "OPFD.CHECKER_ID CHECKER_ID,to_char(OPFD.CHECKER_DTTM,'DD-MM-YYYY HH24:MI:SS') CHECKER_DTTM,  "
				+ "OPFD.COMMENTS  from ONLINE_PRODUCTS_MASTER OPM,ONLINE_PRODUCT_OFFERS_DEALS OPFD  ,"
				+ "ONLINE_CATEGORY_DETAILS OCD,ONLINE_SUB_CATEGORY_DETAILS OSCD  "
				+ "where OPM.PRODUCT_ID=OPFD.PRODUCT_ID and OPM.STATUS='A' and  OPM.CATEGORY_ID=OCD.CATEGORY_ID "
				+ "and OPM.SUB_CATEGORY_ID=OSCD.SUB_CATEGORY_ID  and  OCD.CATEGORY_ID=OSCD.CATEGORY_ID and OPFD.ID=?";
		
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			pstmt = connection.prepareStatement(qry);
			pstmt.setString(1, requestJSON.getString("OFFER_ID"));
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				responseJSON.put("OFFER_ID", rs.getString(1));
				responseJSON.put("CATEGORY_ID", rs.getString(2));
				responseJSON.put("CATEGORY_DESC", rs.getString(3));
				responseJSON.put("SUB_CATEGORY_ID", rs.getString(4));
				responseJSON.put("SUB_CATEGORY_DESC", rs.getString(5));
				responseJSON.put("PRODUCT_ID", rs.getString(6));
				responseJSON.put("PRODUCT_NAME", rs.getString(7));
				responseJSON.put("PRODUCT_PRICE", rs.getString(8));
				responseJSON.put("OFFER_TYPE", rs.getString(9));
				responseJSON.put("TIME_LIMIT", rs.getString(10));
				responseJSON.put("END_DATE", rs.getString(11));
				responseJSON.put("DISCOUNT_CASHBACK", rs.getString(12));
				responseJSON.put("DIS_CASHBACK_PER", rs.getString(13));
				responseJSON.put("STATUS", rs.getString(14));
				responseJSON.put("MAKER_ID", rs.getString(15));
				responseJSON.put("MAKER_DTTM", rs.getString(16));
				responseJSON.put("CHECKER_ID", rs.getString(17));
				responseJSON.put("CHECKER_DTTM", rs.getString(18));
				responseJSON.put("COMMENTS", rs.getString(19));
				
			}
			
			logger.debug("Res JSON  [" + responseJSON + "]");

			dataMap.put("RESPONSE_DATA", responseJSON);

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in auth count [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in auth count [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}

	
	
	public ResponseDTO offerApproveOrReject(RequestDTO requestDTO) {
		
		qry="update ONLINE_PRODUCT_OFFERS_DEALS set STATUS=?,COMMENTS=? where ID=? ";
		
		String reason="Success";
		
		try{
			dataMap = new HashMap<String, Object>();
			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();
			requestJSON = requestDTO.getRequestJSON();
			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("Connection is [" + connection + "]");

			
			if(requestJSON.getString("APPROVE_REJECT").equals("R")){
				reason=requestJSON.getString("REASON");
			}
			
			pstmt = connection.prepareStatement(qry);
			pstmt.setString(1, requestJSON.getString("APPROVE_REJECT"));
			pstmt.setString(2, reason);
			pstmt.setString(3, requestJSON.getString("OFFER_ID"));
			int cnt = pstmt.executeUpdate();
			
			connection.commit();
			
			if(cnt >0){
				responseJSON.put("STATUS", "Success");
			}else{
				responseDTO.addError("Updation Failed.");
			}
			
			
			logger.debug("Res JSON  [" + responseJSON + "]");

			dataMap.put("RESPONSE_DATA", responseJSON);

			logger.debug("dataMap [" + dataMap + "]");
			responseDTO.setData(dataMap);
			logger.debug("Response DTO [" + responseDTO + "]");
			
		}catch (SQLException e) { 
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("SQLException in approve [" + e.getMessage() + "]");
		} catch (Exception e) {
			responseDTO.addError("Internal Error Occured While Executing.");
			logger.debug("Exception in approve [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			dataMap = null;
			responseJSON = null;
	
		}
		
		return responseDTO;
	}

	
}
