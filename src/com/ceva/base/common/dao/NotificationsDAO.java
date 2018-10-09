package com.ceva.base.common.dao;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;



public class NotificationsDAO {
	private Logger logger = Logger.getLogger(OrdersDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	JSONObject requestJSON2 = null;
	JSONObject responseJSON2 = null;
	ResponseDTO responseDTO2 = null;
	
	public ResponseDTO sendNotification(RequestDTO requestDTO) throws SQLException{
		Connection connection = null;
		this.logger.debug("Inside [NotificationsDAO][SendNotification].. ");

		HashMap<String, Object> notDetailsMap = null;
		JSONObject resultJson = null;
		JSONArray notJSONArray = null;
		PreparedStatement notificationPstmt = null;
		ResultSet notificationRS = null;
				
		try {
			
			requestJSON = requestDTO.getRequestJSON();
			
			String channel = requestJSON.getString("deliveryChannel");
			String sendToGroup = requestJSON.getString("sendToGroup");
			String sendToRider = requestJSON.getString("sendToRider");
			String sendToCustomer = requestJSON.getString("sendToCustomer");
			String notificationType = requestJSON.getString("notificationType");
			JSONObject notificationBody = requestJSON.getJSONObject("message");
									
			String orderQry = "INSERT INTO ALERTS(MSG_DATE, MESSAGE, APPL, UNIQUE_ID) values (?,?,?,?)";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			notDetailsMap = new HashMap();
			resultJson = new JSONObject();
			notJSONArray = new JSONArray();

			notificationPstmt = connection.prepareStatement(orderQry);

			notificationPstmt.setTimestamp(1, getCurrentTimeStamp());
			notificationPstmt.setString(2, notificationBody.toString());
			notificationPstmt.setString(3, channel);
			notificationPstmt.setString(4, getCurrentTimeStamp().toString());
			
			notificationPstmt.executeQuery();
			
		}catch(SQLException e) {
			this.logger.debug("Got Exception in sendNotification NotificationsDAO [" + e.getMessage() + "]");
			e.printStackTrace();
		}catch(Exception e) {
			this.logger.debug("Got Exception in sendNotification NotificationsDAO [" + e.getMessage() + "]");
			e.printStackTrace();
		}finally {
			DBUtils.closePreparedStatement(notificationPstmt);
			DBUtils.closeConnection(connection);
		}
		
		return this.responseDTO;
	}
	
	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}
	
	/*public class CurrentDateTimeExample1 {  
		  public static void main(String[] args) {  
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		   LocalDateTime now = LocalDateTime.now();
		   System.out.println(dtf.format(now));
		  }  
		}  */
}
