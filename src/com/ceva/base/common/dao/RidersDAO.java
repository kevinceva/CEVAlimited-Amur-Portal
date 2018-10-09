package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RidersDAO {
	
	private Logger logger = Logger.getLogger(OrdersDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	public ResponseDTO fetchRiderActivity(RequestDTO requestDTO) {		
		Connection connection = null;
	    this.logger.debug("Inside [RidersDAO][fetchRiderActivity].. ");
	    
	    HashMap<String, Object> riderActivityMap = null;
	    JSONObject resultJson = null;
	    JSONArray riderJSONArray = null;
	    PreparedStatement riderPstmt = null;
	    ResultSet riderRS = null;
	    
	    
	    try {
	    	
	    	String activityQRY = "SELECT RIDER_MASTER.RIDER_F_NAME||' '||RIDER_MASTER.RIDER_L_NAME, RIDER_MASTER.MOBILE_NUMBER,RIDER_ORDER_MASTER.ORDER_ID FROM RIDER_MASTER INNER JOIN RIDER_ORDER_MASTER ON RIDER_MASTER.RIDER_ID = RIDER_ORDER_MASTER.RIDER_ID WHERE RIDER_ORDER_MASTER.STATUS = 'Shipped'";
	    	
	    }catch (Exception e){
	    	this.logger.debug("Got Exception in orderList DAO [" + e.getMessage() + "]");
	    	e.printStackTrace();
	    } finally {
	    	DBUtils.closeResultSet(riderRS);
	    	DBUtils.closePreparedStatement(riderPstmt);
		  	DBUtils.closeConnection(connection);
		      
		  	riderActivityMap = null;
		  	resultJson = null;
		  	riderJSONArray = null;
	    }
	    
		
		return this.responseDTO;
	}
	
	public ResponseDTO fetchAllRiders(RequestDTO requestDTO) {		
		Connection connection = null;
		logger.info("Inside [RidersDAO]  [fetchAllRiders]");
		
		HashMap<String, Object> ridersMap = null;
	    JSONObject resultJson = null;
	    JSONArray ridersArray = null;
	    PreparedStatement ridersPstmt = null;
	    ResultSet ridersRS = null;
	    
	    JSONObject json = null;
	    
	    String riderSQL = "SELECT RIDER_ID, RIDER_F_NAME||' '||RIDER_L_NAME, MOBILE_NUMBER FROM RIDER_MASTER";
	    
	    try {
	    	
	    	this.responseDTO = new ResponseDTO();
	    	
	    	connection = connection == null? DBConnector.getConnection() : connection;	    	
	    	logger.info("Connection is :: "+connection);
	    	
	    	ridersMap = new HashMap<>();
	    	resultJson = new JSONObject();
	    	ridersArray = new JSONArray();
	    	
	    	ridersPstmt = connection.prepareStatement(riderSQL);
	    	ridersRS = ridersPstmt.executeQuery();
	    	
	    	json = new JSONObject();
	    	
	    	while(ridersRS.next()) {
	    		json.put("RIDER_ID", ridersRS.getString(1));
	    		json.put("RIDER_NAME", ridersRS.getString(2));
	    		json.put("MOBILE_NUMBER", ridersRS.getString(3));
	    		ridersArray.add(json);
	    		json.clear();
	    	}
	    	
	    	DBUtils.closeResultSet(ridersRS);
	    	DBUtils.closePreparedStatement(ridersPstmt);
	    	DBUtils.closeConnection(connection);
	    	resultJson.put("RIDERS", ridersArray);
	    	ridersMap.put("RIDERS", resultJson);
	    	this.responseDTO.setData(ridersMap);	    	
	    	
	    } catch (Exception e) {
	    	this.logger.debug("Error in FetchAllRiders :: "+e.getMessage());
	    }
	    
		return this.responseDTO;
	}

}
