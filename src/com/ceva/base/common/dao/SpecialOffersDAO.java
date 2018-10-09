package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class SpecialOffersDAO {
	
	private Logger logger = Logger.getLogger(SpecialOffersDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	public ResponseDTO fetchSpecialOffersDetails(RequestDTO requestDTO){
		Connection connection = null;
	    this.logger.debug("Inside [SpecialOffersDAO][GetSpecialOffersDetails].. ");
	    
	    HashMap<String, Object> offerDetailsMap = null;
	    JSONObject resultJson = null;
	    JSONArray offerJSONArray = null;
	    PreparedStatement userPstmt = null;
	    ResultSet userRS = null;
	    
	    JSONObject json = null;
	    
	    String merchantQry = "SELECT TRAVELOFFERID,DEALNAME,DEALCOUNTRY,to_char(STARTDATE,'HH:MI AM Month DD, YYYY'),to_char(ENDDATE,'HH:MI AM Month DD, YYYY'),OPERATORNAME FROM TRAVEL_OFFERS_MASTER ORDER BY TRAVELOFFERID";
	    try
	    {
	      this.responseDTO = new ResponseDTO();
	      
	      connection = connection == null ? DBConnector.getConnection() : connection;
	      this.logger.debug("connection is [" + connection + "]");
	      
	      offerDetailsMap = new HashMap();
	      resultJson = new JSONObject();
	      offerJSONArray = new JSONArray();
	      
	      userPstmt = connection.prepareStatement(merchantQry);
	      userRS = userPstmt.executeQuery();
	      
	      json = new JSONObject();
	      while (userRS.next())
	      {
	        json.put("TRAVELOFFERID", userRS.getString(1));
	        json.put("DEALNAME", userRS.getString(2));
	        json.put("DEALCOUNTRY", userRS.getString(3));
	        json.put("STARTDATE", userRS.getString(4));
	        json.put("ENDDATE", userRS.getString(5));
	        json.put("OPERATOR_NAME", userRS.getString(6));
	        offerJSONArray.add(json);
	        json.clear();
	      }
	      DBUtils.closeResultSet(userRS);
	      DBUtils.closePreparedStatement(userPstmt);
	      DBUtils.closeConnection(connection);
	      resultJson.put("OFFER_LIST", offerJSONArray);
	      offerDetailsMap.put("OFFER_LIST", resultJson);
	      this.logger.info("EntityMap [" + offerDetailsMap + "]");
	      this.responseDTO.setData(offerDetailsMap);
	      
	    }
	    catch (Exception e)
	    {
	      this.logger.debug("Got Exception in GetSpecialOffersDetails DAO [" + 
	        e.getMessage() + "]");
	      e.printStackTrace();
	    }
	    finally
	    {
	      DBUtils.closeResultSet(userRS);
	      DBUtils.closePreparedStatement(userPstmt);
	      DBUtils.closeConnection(connection);
	      
	      offerDetailsMap = null;
	      resultJson = null;
	      offerJSONArray = null;
	    }
	    return this.responseDTO;
	}
	
	public ResponseDTO fetchOfferInfo(RequestDTO requestDTO){
		Connection connection = null;
	    this.logger.debug("Inside [SpecialOffersDAO][fetchOfferInfo].. ");
	    
	    HashMap<String, Object> offerDetailsMap = null;
	    JSONObject resultJson = null;
	    JSONArray offerJSONArray = null;
	    PreparedStatement userPstmt = null;
	    ResultSet userRS = null;
	    
	    JSONObject json = null;
	    
	    String merchantQry = "SELECT TRAVELOFFERID,DEALNAME,DEALCOUNTRY,to_char(STARTDATE,'HH:MI AM Month DD, YYYY'),to_char(ENDDATE,'HH:MI AM Month DD, YYYY'),OPERATORNAME FROM TRAVEL_OFFERS_MASTER ORDER BY TRAVELOFFERID";
	    try
	    {
	      this.responseDTO = new ResponseDTO();
	      
	      connection = connection == null ? DBConnector.getConnection() : connection;
	      this.logger.debug("connection is [" + connection + "]");
	      
	      offerDetailsMap = new HashMap();
	      resultJson = new JSONObject();
	      offerJSONArray = new JSONArray();
	      
	      userPstmt = connection.prepareStatement(merchantQry);
	      userRS = userPstmt.executeQuery();
	      
	      json = new JSONObject();
	      while (userRS.next())
	      {
	        json.put("TRAVELOFFERID", userRS.getString(1));
	        json.put("DEALNAME", userRS.getString(2));
	        json.put("DEALCOUNTRY", userRS.getString(3));
	        json.put("STARTDATE", userRS.getString(4));
	        json.put("ENDDATE", userRS.getString(5));
	        json.put("OPERATOR_NAME", userRS.getString(6));
	        offerJSONArray.add(json);
	        json.clear();
	      }
	      DBUtils.closeResultSet(userRS);
	      DBUtils.closePreparedStatement(userPstmt);
	      DBUtils.closeConnection(connection);
	      resultJson.put("OFFER_LIST", offerJSONArray);
	      offerDetailsMap.put("OFFER_LIST", resultJson);
	      this.logger.info("EntityMap [" + offerDetailsMap + "]");
	      this.responseDTO.setData(offerDetailsMap);
	      
	    }
	    catch (Exception e)
	    {
	      this.logger.debug("Got Exception in GetSpecialOffersDetails DAO [" + 
	        e.getMessage() + "]");
	      e.printStackTrace();
	    }
	    finally
	    {
	      DBUtils.closeResultSet(userRS);
	      DBUtils.closePreparedStatement(userPstmt);
	      DBUtils.closeConnection(connection);
	      
	      offerDetailsMap = null;
	      resultJson = null;
	      offerJSONArray = null;
	    }
	    return this.responseDTO;
	}

}
