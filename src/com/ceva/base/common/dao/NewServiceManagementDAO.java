package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class NewServiceManagementDAO
{
  private Logger logger = Logger.getLogger(NewServiceManagementDAO.class);
  ResponseDTO responseDTO = null;
  JSONObject requestJSON = null;
  JSONObject responseJSON = null;
  
  public ResponseDTO fetchServiceDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewServiceManagementDAO][fetchServiceDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray serviceJSONArray = null;
    PreparedStatement servicePstmt = null;
    ResultSet serviceRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SERVICE_NAME and rownum<2),MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from SERVICE_MASTER where SERVICE_TYPE is null  order by SERVICE_CODE";
    try
    {
      this.responseDTO = new ResponseDTO();
      merchantMap = new HashMap();
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      resultJson = new JSONObject();
      serviceJSONArray = new JSONArray();
      
      servicePstmt = connection.prepareStatement(merchantQry);
      serviceRS = servicePstmt.executeQuery();
      
      json = new JSONObject();
      while (serviceRS.next())
      {
        json.put("SERVICE_CODE", 
          serviceRS.getString(1));
        json.put("SERVICE_NAME", 
          serviceRS.getString(2));
        json.put("MAKER_ID", serviceRS.getString(3));
        json.put("MAKER_DTTM", 
          serviceRS.getString(4));
        serviceJSONArray.add(json);
        json.clear();
      }
      DBUtils.closeResultSet(serviceRS);
      DBUtils.closePreparedStatement(servicePstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("SERVICE_LIST", serviceJSONArray);
      merchantMap.put("SERVICE_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in fetchServiceDetails [" + 
        e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(serviceRS);
      DBUtils.closePreparedStatement(servicePstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      serviceJSONArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO fetchSubServiceDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewServiceManagementDAO][fetchSubServiceDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray serviceJSONArray = null;
    PreparedStatement servicePstmt = null;
    ResultSet serviceRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select SERVICE_CODE,(select BANK_CODE||'-'||BANK_NAME from bank_master where BANK_CODE=SERVICE_NAME and rownum<2),"
    		+ "SUB_SERVICE_CODE,SUB_SERVICE_NAME,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from SERVICE_MASTER where SERVICE_CODE=? and  SERVICE_TYPE is not null  order by SUB_SERVICE_CODE";
    try
    {
      this.requestJSON = requestDTO.getRequestJSON();
      
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      serviceJSONArray = new JSONArray();
      
      servicePstmt = connection.prepareStatement(merchantQry);
      
      servicePstmt.setString(1, this.requestJSON.getString(CevaCommonConstants.SERVICE_CODE));
      
      serviceRS = servicePstmt.executeQuery();
      while (serviceRS.next())
      {
        json = new JSONObject();
        json.put("SERVICE_CODE",  serviceRS.getString(1));
        json.put("SERVICE_NAME", 
          serviceRS.getString(2));
        json.put("SUB_SERVICE_CODE", 
          serviceRS.getString(3));
        json.put("SUB_SERVICE_NAME", 
          serviceRS.getString(4));
        json.put("MAKER_DTTM", 
          serviceRS.getString(5));
        serviceJSONArray.add(json);
      }
      DBUtils.closeResultSet(serviceRS);
      DBUtils.closePreparedStatement(servicePstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("SUBSERVICE_LIST", serviceJSONArray);
      merchantMap.put("SUBSERVICE_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in fetchSubServiceDetails [" + 
        e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(serviceRS);
      DBUtils.closePreparedStatement(servicePstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      serviceJSONArray = null;
    }
    return this.responseDTO;
  }
  
  public ResponseDTO fetchFeeDetails(RequestDTO requestDTO)
  {
    Connection connection = null;
    this.logger.debug("Inside [NewServiceManagementDAO][fetchFeeDetails].. ");
    
    HashMap<String, Object> merchantMap = null;
    JSONObject resultJson = null;
    JSONArray serviceJSONArray = null;
    PreparedStatement servicePstmt = null;
    ResultSet serviceRS = null;
    
    JSONObject json = null;
    
    String merchantQry = "Select FEE_CODE,SERVICE_CODE,SUB_SERVICE_CODE,MAKER_ID,to_char(MAKER_DTTM,'DD-MM-YYYY HH24:MI:SS') from FEE_MASTER where SUB_SERVICE_CODE=?  order by FEE_CODE";
    try
    {
      this.requestJSON = requestDTO.getRequestJSON();
      
      this.responseDTO = new ResponseDTO();
      
      connection = connection == null ? DBConnector.getConnection() : connection;
      
      this.logger.debug("connection is [" + connection + "]");
      
      merchantMap = new HashMap();
      resultJson = new JSONObject();
      serviceJSONArray = new JSONArray();
      
      servicePstmt = connection.prepareStatement(merchantQry);
      
      servicePstmt.setString(1, this.requestJSON.getString(CevaCommonConstants.SUB_SERVICE_CODE));
      
      serviceRS = servicePstmt.executeQuery();
      while (serviceRS.next())
      {
        json = new JSONObject();
        json.put("FEE_CODE", serviceRS.getString(1));
        json.put("SERVICE_CODE", serviceRS.getString(2));
        json.put("SUB_SERVICE_CODE", serviceRS.getString(3));
        json.put("MAKER_ID", serviceRS.getString(4));
        json.put("MAKER_DTTM", serviceRS.getString(5));
        serviceJSONArray.add(json);
      }
      DBUtils.closeResultSet(serviceRS);
      DBUtils.closePreparedStatement(servicePstmt);
      DBUtils.closeConnection(connection);
      resultJson.put("FEE_LIST", serviceJSONArray);
      merchantMap.put("FEE_LIST", resultJson);
      this.logger.debug("EntityMap [" + merchantMap + "]");
      this.responseDTO.setData(merchantMap);
    }
    catch (Exception e)
    {
      this.logger.debug("Got Exception in fetchFeeDetails [" + 
        e.getMessage() + "]");
      e.printStackTrace();
    }
    finally
    {
      DBUtils.closeResultSet(serviceRS);
      DBUtils.closePreparedStatement(servicePstmt);
      DBUtils.closeConnection(connection);
      
      merchantMap = null;
      resultJson = null;
      serviceJSONArray = null;
    }
    return this.responseDTO;
  }
}
