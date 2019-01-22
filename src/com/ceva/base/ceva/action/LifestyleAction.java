package com.ceva.base.ceva.action;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
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

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

public class LifestyleAction extends ActionSupport {
	Cloudinary cloudinary = new Cloudinary();

	Logger logger = Logger.getLogger(LifestyleAction.class);
	ResourceBundle resourceBundle = ResourceBundle.getBundle("pathinfo_config");

	public String cloud_name = resourceBundle.getString("CLOUDINARY_CLOUD_NAME");
	public String api_key = resourceBundle.getString("CLOUDINARY_KEY");
	public String api_secret = resourceBundle.getString("CLOUDINARY_SECRET");
	public String cloudinaryUrl = resourceBundle.getString("CLOUDINARY_IMAGE_URL");

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject merchantCategoriesJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private HttpSession session = null;
	private HttpServletRequest httpRequest = null;

	private String type;
	private String merchantID = null;
	private String merchantInfoPage = null;

	private String merchantName = null;
	private String merchantEmail = null;
	private String merchantTillNo = null;
	private String merchantAddress = null;
	private String merchantCategory = null;
	private String merchantSubCategory = null;
	private String merchantMobile = null;
	private String merchantImageURL = null;
	private String merchantImageName = null;
	private String merchantStatus = null;
	private File merchantImage = null;

	private String offerTitle = null;
	private String offerSubtitle = null;
	private String offerDiscountType = null;
	private String discountAmount = null;
	private String offerMessage = null;
	private File offerImage = null;

	private String finaljsonarray;

	private String offerID = null;

	public String callServlet() {

		logger.debug("inside [LifestyleAction][callServlet].. ");

		try {

			result = "success";
			logger.info("[LifestyleAction][callServlet result..:" + result);

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [LifestyleAction][callServlet] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}

	// Fetch Merchant
	// Categories...............................................................
	public String fetchMerchantCategories() {

		logger.info("Fetching Merchant Categories");
		LifestyleDAO lifetsyleDAO = null;
		ArrayList<String> errors = null;

		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			lifetsyleDAO = new LifestyleDAO();
			responseDTO = lifetsyleDAO.fetchMerchantsCategories(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("MERCHANTS_CATEGORIES");
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
			logger.debug("Exception in [LifetsyleAction][fetchMerchantCategories] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			lifetsyleDAO = null;
		}
		return result;
	}

	// Create
	// Merchant.........................................................................
	public String createMerchant() {
		logger.info("Inside LifestyleAction createMerchant.");
		LifestyleDAO lifestyleDAO = null;
		ArrayList<String> errors = null;
		Cloudinary cloudinary = null;

		try {
			Map config = new HashMap();
			config.put("cloud_name", cloud_name);
			config.put("api_key", api_key);
			config.put("api_secret", api_secret);
			cloudinary = new Cloudinary(config);

			String merchantId = generateUniqueId();
			String merchantName = getMerchantName();
			String merchantEmail = getMerchantEmail();
			String merchantTillNo = getMerchantTillNo();
			String merchantMobile = getMerchantMobile();
			String merchantAddress = getMerchantAddress();
			String merchantCategory = getMerchantCategory();
			String merchantSubCategory = getMerchantSubCategory();
			File merchantImage = getMerchantImage();

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			if (merchantImage.renameTo(new File(merchantImage.getPath().replace(merchantImage.getName(), ""),
					merchantImage.getName().replaceAll("\\.tmp$", ".jpg")))) {
				System.out.println("File is moved successful!");
			} else {
				System.out.println("File is failed to move!");
			}

			File toUpload = new File(merchantImage.getPath().replace(merchantImage.getName(), ""),
					merchantImage.getName().replaceAll("\\.tmp$", ".jpg"));
			Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.asMap("public_id", merchantName));
			String imageUrl = uploadResult.get("secure_url").toString();
			String imageVersion = uploadResult.get("version").toString();
			String imageName = uploadResult.get("public_id").toString();

			System.out.println("Image URL ::::::::::::::::: " + imageUrl);

			String merchantImageUrl = imageUrl;

			requestJSON.put("merchantId", merchantId);
			requestJSON.put("merchantName", merchantName);
			requestJSON.put("merchantEmail", merchantEmail);
			requestJSON.put("merchantTillNo", merchantTillNo);
			requestJSON.put("merchantMobile", merchantMobile);
			requestJSON.put("merchantAddress", merchantAddress);
			requestJSON.put("merchantCategory", merchantCategory);
			requestJSON.put("merchantSubCategory", merchantSubCategory);
			requestJSON.put("merchantImageUrl", merchantImageUrl);

			requestDTO.setRequestJSON(requestJSON);
			lifestyleDAO = new LifestyleDAO();
			responseDTO = lifestyleDAO.saveMerchant(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
				logger.debug("Response JSON [" + responseJSON + "]");

				// insert audit trace here

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

		} catch (Exception e) {

		}

		return result;
	}

	// Create Merchant
	// Offer.................................................................
	public String createMerchantOffer() {
		logger.info("Inside LifestyleAction confirmOfferCreation.");
		LifestyleDAO lifestyleDAO = null;
		ArrayList<String> errors = null;

		Cloudinary cloudinary = null;

		try {

			Map config = new HashMap();
			config.put("cloud_name", cloud_name);
			config.put("api_key", api_key);
			config.put("api_secret", api_secret);
			cloudinary = new Cloudinary(config);

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

			if (offerImage.renameTo(new File(offerImage.getPath().replace(offerImage.getName(), ""),
					offerImage.getName().replaceAll("\\.tmp$", ".jpg")))) {
				System.out.println("File is moved successful!");
			} else {
				System.out.println("File is failed to move!");
			}

			File toUpload = new File(offerImage.getPath().replace(offerImage.getName(), ""),
					offerImage.getName().replaceAll("\\.tmp$", ".jpg"));
			Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
			String imageUrl = uploadResult.get("secure_url").toString();

			requestJSON.put("Merchant_ID", merchantId);
			requestJSON.put("Offer_ID", offerId);
			requestJSON.put("Offer_Title", offerTitle);
			requestJSON.put("Offer_Subtitle", offerSubtitle);
			requestJSON.put("Offer_Message", offerMessage);
			requestJSON.put("Offer_Image", imageUrl);
			requestJSON.put("discountType", discountType);
			requestJSON.put("discountAmount", discountAmount);

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

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [LifetsyleAction][createMerchantOffer] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		}

		return result;
	}

	// Fetch
	// Merchant.........................................................................
	public String fetchMerchant() {
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

	// Fetch Merchant
	// Offers...................................................................
	public String fetchMerchantOffers() {
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

	// Fetch Merchant
	// Info..................................................................
	public String merchantInformation() {
		logger.info("Inside MerchantInformation :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		ArrayList<String> errors = null;
		LifestyleDAO lifestyleDAO = null;
		String merchantId = null;
		JSONObject merchantCategoryJSON = null;
		JSONObject merchantInformationJSON = null;

		try {

			merchantId = getMerchantID();
			String type = getType();

			System.out.println("Merchant ID " + merchantId);
			System.out.println("Request Type " + type);

			lifestyleDAO = new LifestyleDAO();

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseJSON = new JSONObject();

			merchantCategoryJSON = new JSONObject();
			merchantInformationJSON = new JSONObject();

			requestJSON.put("merchantID", merchantId);
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = lifestyleDAO.fetchMerchantInformation(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				merchantInformationJSON = (JSONObject) responseDTO.getData().get("MERCHANT_INFO");
				System.out.println("Merchant Information ::::::::::::::::::::::::::::::: " + merchantInformationJSON);
				responseJSON.put("MerchantInfo", merchantInformationJSON);
				logger.info("Merchant Information [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			responseDTO = lifestyleDAO.fetchMerchantsCategories(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				merchantCategoryJSON = (JSONObject) responseDTO.getData().get("MERCHANTS_CATEGORIES");
				responseJSON.put("MerchantCat", merchantCategoryJSON);
				logger.info("Merchant Categories JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			System.out.println("Response JSON  for merchant " + responseJSON);

			if (getType().equalsIgnoreCase("Modify")) {
				merchantInfoPage = "merchantModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				merchantInfoPage = "merchantModifyInformation";
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

	public String modifyMerchant() {

		logger.info("Inside LifestyleAction modifyMerchant.:::::::::::::::::::::::::::::::::::::::::::::::;");
		LifestyleDAO lifestyleDAO = null;
		ArrayList<String> errors = null;
		Cloudinary cloudinary = null;
		String merchantImageUrl = null;
		String prdImageName = null;
		String newImage = null;

		try {
			Map config = new HashMap();
			config.put("cloud_name", cloud_name);
			config.put("api_key", api_key);
			config.put("api_secret", api_secret);
			cloudinary = new Cloudinary(config);

			String merchantOldImageURL = getMerchantImageURL();
			System.out.println(merchantOldImageURL);
			String merchantId = getMerchantID();
			String merchantName = getMerchantName();
			String merchantEmail = getMerchantEmail();
			String merchantTillNo = getMerchantTillNo();
			String merchantMobile = getMerchantMobile();
			String merchantAddress = getMerchantAddress();
			String merchantCategory = getMerchantCategory();
			String merchantSubCategory = getMerchantSubCategory();
			String merchantImageName = getMerchantImageName();
			String merchantStatus = getMerchantStatus();
			File merchantImage = getMerchantImage();
			prdImageName = merchantImageName;

			if (merchantImage == null) {
				merchantImageUrl = merchantOldImageURL;
			} else {
				if (merchantImage.renameTo(new File(merchantImage.getPath().replace(merchantImage.getName(), ""),
						merchantImage.getName().replaceAll("\\.tmp$", ".jpg")))) {
					System.out.println("File is moved successful!");
				} else {
					System.out.println("File is failed to move!");
				}

				// System.out.println("File path :: " + merchantImage.getPath());

				File toUpload = new File(merchantImage.getPath().replace(merchantImage.getName(), ""),
						merchantImage.getName().replaceAll("\\.tmp$", ".jpg"));
				Map uploadResult = cloudinary.uploader().upload(toUpload,
						ObjectUtils.asMap("public_id", merchantName, "invalidate", true));
				String imageUrl = uploadResult.get("secure_url").toString();
				// String imgNewName = uploadResult.get("public_id").toString();

				System.out.println("Image url :: " + imageUrl);
				merchantImageUrl = imageUrl;

			}

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("merchantId", merchantId);
			requestJSON.put("merchantName", merchantName);
			requestJSON.put("merchantEmail", merchantEmail);
			requestJSON.put("merchantTillNo", merchantTillNo);
			requestJSON.put("merchantCategory", merchantCategory);
			requestJSON.put("merchantSubCategory", merchantSubCategory);
			requestJSON.put("merchantImageUrl", merchantImageUrl);
			requestJSON.put("merchantMobile", merchantMobile);
			requestJSON.put("merchantAddress", merchantAddress);
			requestJSON.put("merchantStatus", merchantStatus);

			System.out.println("Request JSON :::::::::::::::::: " + requestJSON);

			requestDTO.setRequestJSON(requestJSON);
			lifestyleDAO = new LifestyleDAO();
			responseDTO = lifestyleDAO.modifyMerchant(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
				logger.debug("Response JSON [" + responseJSON + "]");

				// insert audit trace here

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

		} catch (Exception e) {

		}

		return result;
	}

	// Create
	// Merchant.........................................................................
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

			System.out.println("Offer ID :: " + offerID);

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("offerId", offerID);
			requestDTO.setRequestJSON(requestJSON);

			customerJSON = new JSONObject();
			alertBody = new JSONObject();

			// Fetching Offer Details...................................................
			responseDTO = lifestyleDAO.fetchOfferDetails(requestDTO);
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				alertBody = (JSONObject) responseDTO.getData().get("OFFER_DETAILS");

				requestJSON.put("OFFER_DETAILS", alertBody);
				System.out.println("Offer Body :: " + alertBody.toString());
				// requestDTO.setRequestJSON(requestJSON);

			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			// Fetching Customers.......................................................
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

		} catch (Exception e) {

		}

		return result;
	}

	public String generateUniqueId() {
		return UUID.randomUUID().toString().substring(0, 8).toUpperCase()
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();

	}

	public String cardCreationConfirm() {
		logger.debug(
				"########################### clusterCreationconfirm ChannelMappingConf Data Started ###########################");

		try {

			requestJSON = new JSONObject();
			responseJSON = new JSONObject();

			logger.info("finaljsonarray >>> [" + finaljsonarray + "]");

			JSONArray jsonarray = JSONArray.fromObject(finaljsonarray);

			responseJSON.put("FINAL_JSON", jsonarray);

			logger.info("Response Json [" + responseJSON + "]");

			result = "success";

		} catch (Exception e) {

			result = "fail";
			e.printStackTrace();
			logger.debug("Exception in getRequest[" + e.getMessage() + "]");
			addActionError("Internal error occured.");

		} finally {

			requestJSON = null;

		}

		logger.debug("########################### ChannelMappingConf Data End ###########################");
		return result;
	}

	public String cardCreationAck() {

		logger.debug("########################### checking ifrezckasfkjjk ChannelMappingAck Data Started ###########################");

		ArrayList<String> errors = null;
		LifestyleDAO agdao = null;
		String remarks = null;
		try {
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();

			session = ServletActionContext.getRequest().getSession();

			responseJSON.put(CevaCommonConstants.MAKER_ID, session.getAttribute(CevaCommonConstants.MAKER_ID));
			responseJSON.put("remoteip", session.getAttribute("REMOTE_IP"));

			agdao = new LifestyleDAO();
			logger.info("finaljsonarray >>> [" + finaljsonarray + "]");

			JSONArray jsonarray = JSONArray.fromObject(finaljsonarray);
			responseJSON.put("FINAL_JSON", jsonarray);
			requestDTO.setRequestJSON(responseJSON);
			/*responseDTO = agdao.ClusterCreationAck(requestDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			logger.info("Response Json [" + responseJSON + "]");
			*/
			result = "success";
			remarks = "SUCCESS";
			responseJSON.put("remarks", remarks);

		} catch (Exception e) {

			result = "fail";
			e.printStackTrace();
			logger.debug("Exception in getRequest[" + e.getMessage() + "]");
			addActionError("Internal error occured.");

		} finally {
			requestJSON = null;
		}

		logger.debug("########################### ChannelMappingAck Data End ###########################");
		return result;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getFinaljsonarray() {
		return finaljsonarray;
	}

	public void setFinaljsonarray(String finaljsonarray) {
		this.finaljsonarray = finaljsonarray;
	}

	public JSONObject getMerchantCategoriesJSON() {
		return merchantCategoriesJSON;
	}

	public void setMerchantCategoriesJSON(JSONObject merchantCategoriesJSON) {
		this.merchantCategoriesJSON = merchantCategoriesJSON;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantEmail() {
		return merchantEmail;
	}

	public void setMerchantEmail(String merchantEmail) {
		this.merchantEmail = merchantEmail;
	}

	public String getMerchantTillNo() {
		return merchantTillNo;
	}

	public void setMerchantTillNo(String merchantTillNo) {
		this.merchantTillNo = merchantTillNo;
	}

	public String getMerchantMobile() {
		return merchantMobile;
	}

	public void setMerchantMobile(String merchantMobile) {
		this.merchantMobile = merchantMobile;
	}

	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}

	public String getMerchantCategory() {
		return merchantCategory;
	}

	public void setMerchantCategory(String merchantCategory) {
		this.merchantCategory = merchantCategory;
	}

	public String getMerchantSubCategory() {
		return merchantSubCategory;
	}

	public void setMerchantSubCategory(String merchantSubCategory) {
		this.merchantSubCategory = merchantSubCategory;
	}

	public File getMerchantImage() {
		return merchantImage;
	}

	public void setMerchantImage(File merchantImage) {
		this.merchantImage = merchantImage;
	}

	public String getMerchantImageURL() {
		return merchantImageURL;
	}

	public void setMerchantImageURL(String merchantImageURL) {
		this.merchantImageURL = merchantImageURL;
	}

	public String getMerchantImageName() {
		return merchantImageName;
	}

	public void setMerchantImageName(String merchantImageName) {
		this.merchantImageName = merchantImageName;
	}

	public String getMerchantStatus() {
		return merchantStatus;
	}

	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = merchantStatus;
	}

}
