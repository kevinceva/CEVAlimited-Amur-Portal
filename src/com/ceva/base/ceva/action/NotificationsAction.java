package com.ceva.base.ceva.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.ceva.base.common.dao.CustomerDAO;
import com.ceva.base.common.dao.NotificationsDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dao.RidersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.util.HttpPostRequestHandler;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

public class NotificationsAction extends ActionSupport {
	
	String result = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject ridersJSON = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	ResponseDTO responseDTO2 = null;
	
	public String deliveryChannel;
	public String sendToGroup;
	public String sendToRider;
	public String sendToCustomer;
	public String notificationType;
	public String notificationSubject;
	public String messageTitle;
	public String messageSubTitle;
	public String notificationMessage;
		
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(NotificationsAction.class);
	
	public String fetchNotificationGroups() {
		
		System.out.println("Inside NotificationsAction fetchNotificationGroups");
		CustomerDAO customerInfo = null;
		RidersDAO riderInfo = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			
			customerInfo = new CustomerDAO();			
			responseDTO = customerInfo.fetchAllCustomers(requestDTO);
			
			riderInfo = new RidersDAO();			
			responseDTO2 = riderInfo.fetchAllRiders(requestDTO);
			
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("CUSTOMERS");
				ridersJSON = (JSONObject) responseDTO2.getData().get("RIDERS");
				responseJSON.put("Riders", ridersJSON);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [OrderAction][fetchOrdersDetails] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			customerInfo = null;
		}

		return result;
	}
	
	public String sendNotification() {
		
		logger.info("Inside NotificationsAction SendNotification.");
		NotificationsDAO notificationsDAO = null;
		ArrayList<String> errors = null;
		
		JSONObject messageJSON = new JSONObject();
		
		String deliveryChannel = getDeliveryChannel();
		String sendToGroup = getSendToGroup();
		String sendToRider = getSendToRider();
		String sendToCustomer = getSendToCustomer();
		String notificationType = getNotificationType();
		String notificationSubject = getNotificationSubject();
		String messageTitle = getMessageTitle();
		String messageSubTitle = getMessageSubTitle();
		String notificationMessage = getNotificationMessage();
		
		messageJSON.put("Subject", notificationSubject);
		messageJSON.put("Title", messageTitle);
		messageJSON.put("SubTitle", messageSubTitle);
		messageJSON.put("Message", notificationMessage);
		
		try {
			
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			requestJSON.put("deliveryChannel", deliveryChannel);
			requestJSON.put("sendToGroup", sendToGroup);
			requestJSON.put("sendToRider", sendToRider);
			requestJSON.put("sendToCustomer", sendToCustomer);
			requestJSON.put("notificationType", notificationType);
			requestJSON.put("message", messageJSON);
			
			requestDTO.setRequestJSON(requestJSON);
			notificationsDAO = new NotificationsDAO();
			responseDTO = notificationsDAO.sendNotification(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("ORDERS");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			
		}catch(Exception e) {
			
		}
		
		/*public String deliveryChannel;
		public String sendToGroup;
		public String sendToRider;
		public String sendToCustomer;
		public String notificationType;
		public String notificationSubject;
		public String messageTitle;
		public String messageSubTitle;
		public String notificationMessage;*/
		
		return result;
	}
	
	public static void sendPushNotification(JSONObject json) {

		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
		String webServiceURL = "https://amurnotifications.azurewebsites.net/api/HttpTriggerCSharp1?code=i0sWW38yhpCloGNhMaaaC1pV1ZOe6TKLle3w6q2r9pT6TcZKjF9qIg==";
		System.out.println("Distance URL  :::: " + webServiceURL);
		String json1;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(webServiceURL);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type", "application/json");

			/*
			 * JSONObject json = new JSONObject(); json.put("DeviceType", "Android");
			 * json.put("UserTag", "9030009911"); json.put("Payload",
			 * "to send to the device");
			 */

			HttpEntity entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			StringBuilder resp = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				resp.append(line);
			}
			rd.close();
			System.out.println("response String in HTTP Class:" + resp.toString());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(String deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	public String getSendToGroup() {
		return sendToGroup;
	}

	public void setSendToGroup(String sendToGroup) {
		this.sendToGroup = sendToGroup;
	}

	public String getSendToRider() {
		return sendToRider;
	}

	public void setSendToRider(String sendToRider) {
		this.sendToRider = sendToRider;
	}

	public String getSendToCustomer() {
		return sendToCustomer;
	}

	public void setSendToCustomer(String sendToCustomer) {
		this.sendToCustomer = sendToCustomer;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getNotificationSubject() {
		return notificationSubject;
	}

	public void setNotificationSubject(String notificationSubject) {
		this.notificationSubject = notificationSubject;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageSubTitle() {
		return messageSubTitle;
	}

	public void setMessageSubTitle(String messageSubTitle) {
		this.messageSubTitle = messageSubTitle;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	
	

}
