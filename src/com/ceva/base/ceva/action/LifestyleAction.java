package com.ceva.base.ceva.action;

import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;

import com.ceva.base.common.dao.CustomerDAO;
import com.ceva.base.common.dao.LifestyleDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.HttpPostRequestHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;



public class LifestyleAction extends ActionSupport {
	
	Logger logger = Logger.getLogger(LifestyleAction.class);
	ResourceBundle resourceBundle=ResourceBundle.getBundle("pathinfo_config");
	
	public String discountImagePath = resourceBundle.getString("DISCOUNT_IMAGE");
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;	
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	private HttpSession session = null;	
	private HttpServletRequest httpRequest = null;
	
	private String type;
	private String merchantID;
	private String merchantInfoPage = null;
	private String confirm_merchantName = null;
	private String confirm_merchantEmail = null;
	private String confirm_merchantTillNo = null;
	
	private String offerTitle = null;
	private String offerSubtitle  =null;
	private String offerDiscountType = null;
	private String discountAmount = null;
	private String offerMessage = null;
	private File offerImage = null;
	
	private String offerID = null;
			
	public String callServlet(){
		
		logger.debug("inside [LifestyleAction][callServlet].. ");
		
		
		try {
						
			result = "success";
			logger.info("[LifestyleAction][callServlet result..:"+result);
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [LifestyleAction][callServlet] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}	

	//Create Merchant.........................................................................
	public String createMerchant() {
		logger.info("Inside LifestyleAction createMerchant.");
		LifestyleDAO lifestyleDAO = null;
		ArrayList<String> errors = null;
		
		try {
			
			String merchantName = getConfirm_merchantName();
			String merchantTillNo = getConfirm_merchantTillNo();
			String merchantId = generateUniqueId();
			String merchantEmail = getConfirm_merchantEmail();
			
			
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			requestJSON.put("merchantId", merchantId);
			requestJSON.put("merchantName", merchantName);
			requestJSON.put("merchantEmail", merchantEmail);
			requestJSON.put("merchantTillNo", merchantTillNo);
			
			requestDTO.setRequestJSON(requestJSON);
			lifestyleDAO = new LifestyleDAO();
			responseDTO = lifestyleDAO.saveMerchant(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
				logger.debug("Response JSON [" + responseJSON + "]");
				
				//insert audit trace here
				
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
		
		return result;
	}
	
	//Create Merchant Offer.................................................................
	public String createMerchantOffer() {
		logger.info("Inside LifestyleAction confirmOfferCreation.");
		LifestyleDAO lifestyleDAO = null;
		ArrayList<String> errors = null;
		
		try {
			
			String merchantId = getMerchantID();
			String offerTitle = getOfferTitle();
			String offerSubtitle = getOfferSubtitle();
			String discountType = getOfferDiscountType();
			String discountAmount = getDiscountAmount();
			String offerMessage = getOfferMessage();
			String offerId = generateUniqueId();
			File offerImage = getOfferImage();
									
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			
			requestJSON.put("Merchant_ID", merchantId);
			requestJSON.put("Offer_ID", offerId);
			requestJSON.put("Offer_Title", offerTitle);
			requestJSON.put("Offer_Subtitle", offerSubtitle);
			requestJSON.put("Offer_Message", offerMessage);
			requestJSON.put("Offer_Image", offerImage.getName());
			requestJSON.put("discountType", discountType);
			requestJSON.put("discountAmount", discountAmount);
			
			if(offerImage.renameTo(new File(discountImagePath, offerImage.getName().replaceAll("\\.tmp$", ".jpg")))){
	    		System.out.println("File is moved successful!");
	    	   }else{
	    		System.out.println("File is failed to move!");
	    	}
			
			requestDTO.setRequestJSON(requestJSON);
			lifestyleDAO = new LifestyleDAO();
			responseDTO = lifestyleDAO.saveMerchantOffer(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				
				
				
				responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
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
			result = "fail";
			logger.debug("Exception in [LifetsyleAction][createMerchantOffer] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		}
		
		return result;
	}
	
	//Fetch Merchant.........................................................................
	public String fetchMerchant(){
		System.out.println("Inside LifestyleAction FetchMerchants");
		LifestyleDAO lifetsyleDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			lifetsyleDAO = new LifestyleDAO();
			responseDTO = lifetsyleDAO.fetchMerchants(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("MERCHANTS");
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
			logger.debug("Exception in [LifetsyleDAO][fetchMerchants] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			lifetsyleDAO = null;
		}		
		
		return "success";
	}
	
	//Fetch Merchant Offers...................................................................
		public String fetchMerchantOffers(){
			System.out.println("Inside LifestyleAction fetchMerchantOffers");
			LifestyleDAO lifetsyleDAO = null;
			ArrayList<String> errors = null;
			try {

				requestJSON = new JSONObject();
				requestDTO = new RequestDTO();
				requestDTO.setRequestJSON(requestJSON);
				logger.debug("Request DTO [" + requestDTO + "]");
				lifetsyleDAO = new LifestyleDAO();
				responseDTO = lifetsyleDAO.fetchMerchantOffers(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					responseJSON = (JSONObject) responseDTO.getData().get("MERCHANT_OFFERS");
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
				logger.debug("Exception in [LifetsyleAction][fetchMerchantOffers] [" + e.getMessage() + "]");
				addActionError("Internal error occured.");
			} finally {
				requestDTO = null;
				responseDTO = null;
				requestJSON = null;

				errors = null;
				lifetsyleDAO = null;
			}		
			
			return "success";
		}
	
	//Fetch Merchant Info..................................................................
	public String merchantInformation(){
		logger.debug("Inside OfferInformation");
		ArrayList<String> errors = null;
		
		try {
			
			String offerid = getMerchantID();
			String type = getType();
			
			if (getType().equalsIgnoreCase("Modify")) {
				merchantInfoPage = "merchantModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				merchantInfoPage = "merchantViewInformation";
			} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
				merchantInfoPage = "merchantActivate";
			} else {
				merchantInfoPage = "merchantViewInformation";
			}

		
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}

		return result;
	}
	
	//Create Merchant.........................................................................
		public String sendOfferAlert() {
			logger.info("Inside LifestyleAction sendOfferAlert.");
			LifestyleDAO lifestyleDAO = null;
			ArrayList<String> errors = null;
			
			CustomerDAO customerDAO = null;
			JSONObject customerJSON = null;
			JSONObject alertBody = null;
			
			try {
				
				String offerID = getOfferID();
				customerDAO = new CustomerDAO();
				lifestyleDAO = new LifestyleDAO();
				
				System.out.println("Offer ID :: "+offerID);
				
				requestJSON = new JSONObject();
				requestDTO = new RequestDTO();
				
				requestJSON.put("offerId", offerID);
				requestDTO.setRequestJSON(requestJSON);
				
				customerJSON = new JSONObject();
				alertBody = new JSONObject();
				
				//Fetching Offer Details...................................................
				responseDTO = lifestyleDAO.fetchOfferDetails(requestDTO);								
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					alertBody = (JSONObject) responseDTO.getData().get("OFFER_DETAILS");
					
					requestJSON.put("OFFER_DETAILS", alertBody);	
					System.out.println("Offer Body :: "+alertBody.toString());
					//requestDTO.setRequestJSON(requestJSON);					
										
				} else {
					errors = (ArrayList<String>) responseDTO.getErrors();
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
				}
					result = "fail";
				}
				
				//Fetching Customers.......................................................				
				responseDTO = customerDAO.fetchAllCustomers(requestDTO);
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					customerJSON = (JSONObject) responseDTO.getData().get("CUSTOMERS");					
					
					requestJSON.put("CUSTOMERS", customerJSON);									
					requestDTO.setRequestJSON(requestJSON);					
					responseDTO = lifestyleDAO.sendOfferAlert(requestDTO);					
										
				} else {
					errors = (ArrayList<String>) responseDTO.getErrors();
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
				}
					result = "fail";
				}
				
				responseJSON = new JSONObject();
				result = "success";
				responseJSON.put("Resp_Message", "Alert sent successfully.");
								
			}catch(Exception e) {
				
			}
			
			return result;
		}
	
	public String generateUniqueId(){
		return UUID.randomUUID().toString().substring(0,8).toUpperCase()+UUID.randomUUID().toString().substring(0,8).toUpperCase();
		
	}
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMerchantInfoPage() {
		return merchantInfoPage;
	}


	public void setMerchantInfoPage(String merchantInfoPage) {
		this.merchantInfoPage = merchantInfoPage;
	}


	public String getMerchantID() {
		return merchantID;
	}


	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}

	public String getConfirm_merchantName() {
		return confirm_merchantName;
	}

	public void setConfirm_merchantName(String confirm_merchantName) {
		this.confirm_merchantName = confirm_merchantName;
	}

	public String getConfirm_merchantTillNo() {
		return confirm_merchantTillNo;
	}

	public void setConfirm_merchantTillNo(String confirm_merchantTillNo) {
		this.confirm_merchantTillNo = confirm_merchantTillNo;
	}

	public String getOfferTitle() {
		return offerTitle;
	}

	public void setOfferTitle(String offerTitle) {
		this.offerTitle = offerTitle;
	}

	public String getOfferSubtitle() {
		return offerSubtitle;
	}

	public void setOfferSubtitle(String offerSubtitle) {
		this.offerSubtitle = offerSubtitle;
	}

	public String getOfferDiscountType() {
		return offerDiscountType;
	}

	public void setOfferDiscountType(String offerDiscountType) {
		this.offerDiscountType = offerDiscountType;
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getOfferMessage() {
		return offerMessage;
	}

	public void setOfferMessage(String offerMessage) {
		this.offerMessage = offerMessage;
	}

	public File getOfferImage() {
		return offerImage;
	}

	public void setOfferImage(File offerImage) {
		this.offerImage = offerImage;
	}

	public String getOfferID() {
		return offerID;
	}

	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}

	public String getConfirm_merchantEmail() {
		return confirm_merchantEmail;
	}

	public void setConfirm_merchantEmail(String confirm_merchantEmail) {
		this.confirm_merchantEmail = confirm_merchantEmail;
	}

	
}
