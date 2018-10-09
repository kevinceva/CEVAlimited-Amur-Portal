package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

public class LifestyleDAO {
	private Logger logger = Logger.getLogger(LifestyleDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;

	JSONObject requestJSON2 = null;
	JSONObject responseJSON2 = null;
	ResponseDTO responseDTO2 = null;

	// Save Merchant...............................................................
	public ResponseDTO saveMerchant(RequestDTO requestDTO) throws SQLException {
		Connection connection = null;
		this.logger.debug("Inside [LifestyleDAO][saveMerchant].. ");

		HashMap<String, Object> merchantMap = null;
		PreparedStatement merchantPstmt = null;
		JSONObject Resp_Message = null;

		try {

			requestJSON = requestDTO.getRequestJSON();

			String merchantId = requestJSON.getString("merchantId");
			String merchantName = requestJSON.getString("merchantName");
			String merchantEmail = requestJSON.getString("merchantEmail");
			String merchantTillNo = requestJSON.getString("merchantTillNo");

			String merchantQry = "INSERT INTO MERCHANT_MASTER(MERCHANT_ID_PK, MERCHANT_NAME, MPESA_TILL_NO, MERCHANT_EMAIL, DATE_CREATED) values (?,?,?,?,?)";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			Resp_Message = new JSONObject();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, merchantId);
			merchantPstmt.setString(2, merchantName);
			merchantPstmt.setString(3, merchantTillNo);
			merchantPstmt.setString(4, merchantEmail);
			merchantPstmt.setTimestamp(5, getCurrentTimeStamp());

			merchantPstmt.executeQuery();
			Resp_Message.put("Response_Message", "Merchant successfully saved.");

			merchantMap.put("Response_Message", Resp_Message);
			this.logger.info("EntityMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in saveMerchant LifestyleDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got SQL Exception in saveMerchant LifestyleDAO [" + e.getMessage() + "]");

		} catch (Exception e) {
			this.logger.debug("Got Exception in saveMerchant LifestyleDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message", "Got Exception in saveMerchant LifestyleDAO [" + e.getMessage() + "]");
		} finally {
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			Resp_Message = null;
		}

		return this.responseDTO;
	}

	// Save Merchant Offer..........................................................
	public ResponseDTO saveMerchantOffer(RequestDTO requestDTO) throws SQLException {
		Connection connection = null;
		this.logger.debug("Inside [LifestyleDAO][saveMerchantOffer].. ");

		HashMap<String, Object> merchantMap = null;
		PreparedStatement merchantPstmt = null;
		JSONObject Resp_Message = null;

		try {

			requestJSON = requestDTO.getRequestJSON();

			String merchantId = requestJSON.getString("Merchant_ID");
			String offerId = requestJSON.getString("Offer_ID");
			String offerTitle = requestJSON.getString("Offer_Title");
			String offerSubtitle = requestJSON.getString("Offer_Subtitle");
			String offerMessage = requestJSON.getString("Offer_Message");
			String offerImage = requestJSON.getString("Offer_Image");
			String discountType = requestJSON.getString("discountType");
			String discountAmount = requestJSON.getString("discountAmount");
			String offerImageUrl = "Amur_Images/discounts/" + offerImage.replaceAll("\\.tmp$", ".jpg");

			String merchantQry = "INSERT INTO MERCHANT_OFFERS(OFFER_ID, MERCHANT_ID, OFFER_TITLE, OFFER_SUBTITLE, OFFER_IMAGE, OFFER_TYPE, AMOUNT, DATE_CREATED, OFFER_MESSAGE) values (?,?,?,?,?,?,?,?,?)";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantMap = new HashMap();
			Resp_Message = new JSONObject();

			merchantPstmt = connection.prepareStatement(merchantQry);

			merchantPstmt.setString(1, offerId);
			merchantPstmt.setString(2, merchantId);
			merchantPstmt.setString(3, offerTitle);
			merchantPstmt.setString(4, offerSubtitle);
			merchantPstmt.setString(5, offerImageUrl);
			merchantPstmt.setString(6, discountType);
			merchantPstmt.setString(7, discountAmount);
			merchantPstmt.setTimestamp(8, getCurrentTimeStamp());
			merchantPstmt.setString(9, offerMessage);

			merchantPstmt.executeQuery();
			Resp_Message.put("Response_Message", "Merchant Offer successfully saved.");

			merchantMap.put("Response_Message", Resp_Message);
			this.logger.info("EntityMap [" + merchantMap + "]");
			this.responseDTO.setData(merchantMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in saveMerchant LifestyleDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got SQL Exception in saveMerchantOffer LifestyleDAO [" + e.getMessage() + "]");

		} catch (Exception e) {
			this.logger.debug("Got Exception in saveMerchant LifestyleDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in saveMerchantOffer LifestyleDAO [" + e.getMessage() + "]");
		} finally {
			DBUtils.closePreparedStatement(merchantPstmt);
			DBUtils.closeConnection(connection);
			merchantMap = null;
			Resp_Message = null;
		}

		return this.responseDTO;
	}

	// Send Offer
	// Alert...............................................................
	public ResponseDTO sendOfferAlert(RequestDTO requestDTO) throws SQLException {
		Connection connection = null;
		this.logger.debug("Inside [LifestyleDAO][sendOfferAlert].. ");

		JSONObject customerJSON = null;
		JSONObject alertJSON = null;
		JSONObject alertBody = null;
		JSONObject payload = null;
		
		String usertag = null;
		String title = null;
		String subtitle = null;
		String message = null;
		String type = null;
		String imageurl = null;
		String status = null;
		String email = null;
		String customerId = null;
		Timestamp msgDate = null;
		String appl = null;
		String fetchStatus = null;
		String subject = null;
		String alertId = null;
		String offerId = null;
		int retryCount = 0;
		
		HashMap<String, Object> alertsMap = null;
		PreparedStatement alertsPstmt = null;
		JSONObject Resp_Message = null;

		try {
			
			alertJSON = new JSONObject();
			customerJSON = new JSONObject();
			requestJSON = requestDTO.getRequestJSON();

			offerId = requestJSON.getString("offerId");
			customerJSON = requestJSON.getJSONObject("CUSTOMERS");
			alertJSON = requestJSON.getJSONObject("OFFER_DETAILS");
			System.out.println("Alert JSON :: "+alertJSON.toString());
			msgDate = getCurrentTimeStamp();
			
			alertsMap = new HashMap();
			Resp_Message = new JSONObject();
			payload = new JSONObject();
		
			//Retrieve alerts details and packaging alert message.........................................
			JSONArray numberArray = customerJSON.getJSONArray("CUSTOMERS");

			for (int i = 0; i < numberArray.size(); i++) {
				
				
				customerId = numberArray.getJSONObject(i).getString("CUSTOMER_ID");
				usertag = numberArray.getJSONObject(i).getString("MOBILE_NUMBER");
				email = numberArray.getJSONObject(i).getString("EMAIL");

				alertBody = new JSONObject();		
				alertBody.put("UserTag", usertag);
								
				JSONArray alertArray = alertJSON.getJSONArray("OFFER_DETAILS");

				for (int j = 0; j < alertArray.size(); j++) {
										
					title = alertArray.getJSONObject(j).getString("OFFER_TITLE");
					subtitle = alertArray.getJSONObject(j).getString("OFFER_SUBTITLE");
					message = alertArray.getJSONObject(j).getString("OFFER_MESSAGE");
					type = alertArray.getJSONObject(j).getString("OFFER_TYPE");
					imageurl = alertArray.getJSONObject(j).getString("OFFER_IMAGE");
					message += "#"+offerId;
					status = "Success";
					retryCount = 0;
					
					System.out.println("Message payload :: "+message);
										
					payload.put("TITLE", title);
					payload.put("SUBTITLE", subtitle);
					payload.put("MESSAGE", message);
					payload.put("TYPE", "GN-MERCHANTPROMO");
					payload.put("IMAGEURL", imageurl);
					payload.put("STATUS", status);
					alertBody.put("Payload", payload);
					
				}
				
				appl = "PUSH";
				fetchStatus = "P";
				subject = "Amur Offer";
				alertId = generateUniqueId();
				
				//Insert into alerts table..................................................................
				
				String alertsQry = "INSERT INTO ALERTS(CIN, MSG_DATE, EMAIL_ID, MOBILE_NO, MESSAGE, APPL, FETCH_STATUS, SUBJECT, RETRY_COUNT, UNIQUE_ID) values (?,?,?,?,?,?,?,?,?,?)";

				this.responseDTO = new ResponseDTO();

				connection = connection == null ? DBConnector.getConnection() : connection;
				this.logger.debug("connection is [" + connection + "]");

				alertsMap = new HashMap();
				Resp_Message = new JSONObject();

				alertsPstmt = connection.prepareStatement(alertsQry);

				alertsPstmt.setString(1, customerId);
				alertsPstmt.setTimestamp(2, getCurrentTimeStamp());
				alertsPstmt.setString(3, email);
				alertsPstmt.setString(4, usertag);
				alertsPstmt.setObject(5, alertBody.toString());
				alertsPstmt.setString(6, appl);
				alertsPstmt.setString(7, fetchStatus);
				alertsPstmt.setString(8, subject);
				alertsPstmt.setInt(9, retryCount);
				alertsPstmt.setString(10, alertId);
				
				alertsPstmt.executeQuery();
				Resp_Message.put("Response_Message", "Merchant Offer successfully saved.");

				alertsMap.put("Response_Message", Resp_Message);
				this.logger.info("EntityMap [" + alertsMap + "]");
				this.responseDTO.setData(alertsMap);	
				
				alertBody = null;
			}
			
			Resp_Message.put("Response_Message", "Alerts sending in progress.");
			alertsMap.put("Response_Message", Resp_Message);

		} catch (Exception e) {
			this.logger.debug("Got Exception in saveMerchantOffer LifestyleDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in saveMerchantOffer LifestyleDAO [" + e.getMessage() + "]");
		} finally {
			DBUtils.closePreparedStatement(alertsPstmt);
			DBUtils.closeConnection(connection);
			alertsMap = null;
			Resp_Message = null;
			alertJSON = null;
			customerJSON = null;
		}

		return this.responseDTO;
	}

	// Fetch
	// Merchants..................................................................
	public ResponseDTO fetchMerchants(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [LifetsyleDAO][fetchMerchants].. ");

		HashMap<String, Object> merchantsMap = null;
		JSONObject resultJson = null;
		JSONArray merchantsArray = null;
		PreparedStatement merchantsPstmt = null;
		ResultSet merchantsRS = null;
		JSONObject json = null;

		String merchantsQRY = "SELECT MERCHANT_ID_PK, MERCHANT_NAME, MERCHANT_EMAIL, MPESA_TILL_NO, to_char( DATE_CREATED , 'DD-MM-YYYY HH:MM:SS' ) FROM MERCHANT_MASTER";
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			merchantsMap = new HashMap();
			resultJson = new JSONObject();
			merchantsArray = new JSONArray();

			merchantsPstmt = connection.prepareStatement(merchantsQRY);
			merchantsRS = merchantsPstmt.executeQuery();

			json = new JSONObject();
			while (merchantsRS.next()) {
				json.put("MERCHANT_ID", merchantsRS.getString(1));
				json.put("MERCHANT_NAME", merchantsRS.getString(2));
				json.put("MERCHANT_EMAIL", merchantsRS.getString(3));
				json.put("MERCHANT_TILL_NO", merchantsRS.getString(4));
				json.put("DATE_CREATED", merchantsRS.getString(5));
				merchantsArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(merchantsRS);
			DBUtils.closePreparedStatement(merchantsPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANTS", merchantsArray);
			merchantsMap.put("MERCHANTS", resultJson);
			this.logger.info("EntityMap [" + merchantsMap + "]");
			this.responseDTO.setData(merchantsMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in LifetsyleDAO fetchMerchants [" + e.getMessage() + "]");
			e.printStackTrace();

		} catch (Exception e) {
			this.logger.debug("Got Exception in LifetsyleDAO fetchMerchants [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(merchantsRS);
			DBUtils.closePreparedStatement(merchantsPstmt);
			DBUtils.closeConnection(connection);

			merchantsMap = null;
			resultJson = null;
			merchantsArray = null;
		}

		return this.responseDTO;
	}

	// Fetch
	// Merchants..................................................................
	public ResponseDTO fetchMerchantOffers(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [LifetsyleDAO][fetchMerchantOffers].. ");

		HashMap<String, Object> offersMap = null;
		JSONObject resultJson = null;
		JSONArray offersArray = null;
		PreparedStatement offersPstmt = null;
		ResultSet offersRS = null;
		JSONObject json = null;

		String offersQRY = "SELECT OFFER_ID, MERCHANT_ID, OFFER_TITLE, OFFER_SUBTITLE, OFFER_IMAGE, OFFER_TYPE, AMOUNT, to_char( DATE_CREATED , 'DD-MM-YYYY HH:MM:SS' ), OFFER_MESSAGE FROM MERCHANT_OFFERS";
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			offersMap = new HashMap();
			resultJson = new JSONObject();
			offersArray = new JSONArray();

			offersPstmt = connection.prepareStatement(offersQRY);
			offersRS = offersPstmt.executeQuery();

			json = new JSONObject();
			while (offersRS.next()) {
				json.put("OFFER_ID", offersRS.getString(1));
				json.put("MERCHANT_ID", offersRS.getString(2));
				json.put("OFFER_TITLE", offersRS.getString(3));
				json.put("OFFER_SUBTITLE", offersRS.getString(4));
				json.put("OFFER_IMAGE", URLEncoder.encode(offersRS.getString(5), "UTF-8"));
				json.put("OFFER_TYPE", offersRS.getString(6));
				json.put("AMOUNT", offersRS.getString(7));
				json.put("DATE_CREATED", offersRS.getString(8));
				json.put("OFFER_MESSAGE", offersRS.getString(9));
				offersArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(offersRS);
			DBUtils.closePreparedStatement(offersPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("MERCHANT_OFFERS", offersArray);
			offersMap.put("MERCHANT_OFFERS", resultJson);
			this.logger.info("EntityMap [" + offersMap + "]");
			this.responseDTO.setData(offersMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in LifetsyleDAO fetchMerchants [" + e.getMessage() + "]");
			e.printStackTrace();
		} catch (Exception e) {
			this.logger.debug("Got Exception in LifetsyleDAO fetchMerchants [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(offersRS);
			DBUtils.closePreparedStatement(offersPstmt);
			DBUtils.closeConnection(connection);

			offersMap = null;
			resultJson = null;
			offersArray = null;
		}

		return this.responseDTO;
	}

	// Fetch Offer Details..................................................................
	public ResponseDTO fetchOfferDetails(RequestDTO requestDTO) {
		Connection connection = null;
		this.logger.debug("Inside [LifestyleDAO][fetchOfferDetails].. ");

		HashMap<String, Object> offerMap = null;
		JSONObject resultJson = null;
		JSONArray offerArray = null;
		PreparedStatement offerPstmt = null;
		ResultSet offerRS = null;

		String orderID = null;

		JSONObject json = null;

		try {

			requestJSON = requestDTO.getRequestJSON();
			orderID = requestJSON.getString("offerId");

			System.out.println("Offer id resquested is   " + orderID);
			String offerQry = "SELECT OFFER_ID, MERCHANT_ID, OFFER_TITLE, OFFER_SUBTITLE, OFFER_IMAGE, OFFER_TYPE, AMOUNT, to_char( DATE_CREATED , 'DD-MM-YYYY HH:MM:SS' ), offer_message FROM MERCHANT_OFFERS WHERE OFFER_ID=?";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			offerMap = new HashMap();
			resultJson = new JSONObject();
			offerArray = new JSONArray();

			offerPstmt = connection.prepareStatement(offerQry);

			offerPstmt.setString(1, orderID);
			offerRS = offerPstmt.executeQuery();

			json = new JSONObject();
			while (offerRS.next()) {
				json.put("OFFER_ID", offerRS.getString(1));
				json.put("MERCHANT_ID", offerRS.getString(2));
				json.put("OFFER_TITLE", offerRS.getString(3));
				json.put("OFFER_SUBTITLE", offerRS.getString(4));
				json.put("OFFER_IMAGE", offerRS.getString(5));
				json.put("OFFER_TYPE", offerRS.getString(6));
				json.put("AMOUNT", offerRS.getString(7));
				json.put("DATE_CREATED", offerRS.getString(8));
				json.put("OFFER_MESSAGE", offerRS.getString(9));
				offerArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(offerRS);
			DBUtils.closePreparedStatement(offerPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("OFFER_DETAILS", offerArray);
			offerMap.put("OFFER_DETAILS", resultJson);

			this.logger.info("EntityMap [" + offerMap + "]");
			this.responseDTO.setData(offerMap);

		} catch (Exception e) {
			this.logger.debug("Got Exception in orderList DAO [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(offerRS);
			DBUtils.closePreparedStatement(offerPstmt);
			DBUtils.closeConnection(connection);

			offerMap = null;
			resultJson = null;
			offerArray = null;
		}

		return this.responseDTO;
	}

	// Generate
	// timestamp...............................................................
	private static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
	//Generate Unique ID ......................................................
	public String generateUniqueId(){
		return UUID.randomUUID().toString().substring(0,8).toUpperCase()+UUID.randomUUID().toString().substring(0,8).toUpperCase();		
	}

}
