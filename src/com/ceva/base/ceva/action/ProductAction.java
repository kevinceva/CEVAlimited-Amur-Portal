package com.ceva.base.ceva.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CategoryManagementDAO;
import com.ceva.base.common.dao.LifestyleDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dao.ProductsDAO;
import com.ceva.base.common.dao.SupplierDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;
import com.ceva.util.HttpPostRequestHandler;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;

public class ProductAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(ProductAction.class);
	ResourceBundle resourceBundle = ResourceBundle.getBundle("pathinfo_config");
	//public String discountImagePath = resourceBundle.getString("DISCOUNT_IMAGE");

	public String serverIp = resourceBundle.getString("AMUR_WEB_SERVICE");
	public String cloudinaryApiKey = resourceBundle.getString("CLOUDINARY_KEY");
	public String cloudinaryApiSecret = resourceBundle.getString("CLOUDINARY_SECRET");
	// public String referralRate = resourceBundle.getString("REFERRAL_RATE");
	// public String healthPremium = resourceBundle.getString("HEALTHPREMIUM");

	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject catalogRespJSON = null;
	JSONObject manfRespJSON = null;
	JSONObject productJSON = null;
	JSONObject supplyProductJSON = null;

	private HttpSession session = null;

	private HttpServletRequest httpRequest = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;

	public String merchantId;
	public String storeId;
	public String srchval;
	public String prdid;
	public String type;

	public String prdname;
	public String catname;
	public String subcatname;
	public String manfname;
	public String prddesc;
	public String pic1;
	public String modelno;
	public String pcode;
	public String discount;
	public String quantity;
	public String status;
	public String provider;
	public String disctype;
	public String reordrlvl;
	public String servprvdr;
	public String catid;
	public String subcatid;
	public String manfid;
	public String wholesaleprice;

	public String productDemandName;
	public String amurDemandName;
	public String sellPriceNoVat;
	public String vatRate;
	public String sellPriceVat;
	public String prdSupplierId;
	public String productDemandDesc;
	public String skuSize;
	public String skuUnit;
	public String markupRate;
	public String markupAmount;
	public String referralEarnings;
	public String healthPremiumAmt;
	public String totalVatAmt;

	public String markupType;
	public String markupAmountNoVat;
	public String markupVatType;
	public String markupAmountVat;
	public String markupAmtWithVat;

	private String referralRate;
	public String referralAmtNoVat;
	public String referralVatType;
	public String referralVatAmt;
	public String referralAmtVat;

	public String healthPremiumRate;
	public String healthPremiumNoVat;
	public String healthPremiumVatType;
	public String healthPremiumVatAmt;
	public String healthPremiumVat;

	public String catname2;
	public String subcatname2;
	public String manfname2;
	public String productSupplierName;
	public String productQuantity;
	public String reorderLvl;
	public String buyPriceNoVat;
	public String vatRate2;
	public String buyPriceVat;

	public String priceVatType;
	public String prdImageName;

	private File offerImage = null;
	private File prdImg = null;

	public String remarks;
	private String productInfoPage = null;

	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}

	public String callServlet() {

		logger.debug("inside [ProductAction][callServlet].. ");

		try {

			result = "success";
			logger.info("[ProductAction][callServlet result..:" + result);

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [ProductAction][callServlet] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}

	public String fetchProducts() {

		try {
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp + "/amurcore/amur/catalog/fetchproducts";

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("Product List :: [" + obj + "] obj to string[" + obj.toString() + "]");
			responseJSON = obj;
			result = "success";
		} catch (Exception e) {
			result = "fail";
			/*
			 * logger.debug("Exception in [ProductAction][fetchProducts] [" + e.getMessage()
			 * + "]");
			 */
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;
	}

	public String fetchDemandProducts() {
		System.out.println("Inside ProductAction fetchDemandProducts");
		ProductsDAO productsDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			productsDAO = new ProductsDAO();
			responseDTO = productsDAO.fetchDemandProducts(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("DEMAND_PRODUCTS");
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
			logger.debug("Exception in [ProductsAction][fetchDemandProducts] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
		}

		return result;
	}

	public String fetchSupplyProducts() {
		System.out.println("Inside ProductAction fetchSupplyProducts");
		ProductsDAO productsDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			productsDAO = new ProductsDAO();
			responseDTO = productsDAO.fetchSupplyProducts(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("SUPPLY_PRODUCTS");
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
			logger.debug("Exception in [ProductsAction][fetchSupplyProducts] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
		}

		return result;
	}

	public String createProduct() {
		logger.debug("Inside createProduct..");
		ArrayList<String> errors = null;
		ProductsDAO productsDAO = null;

		try {
			responseJSON = new JSONObject();

			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();

			logger.debug("fetching catalog");
			String catalogURL = serverIp + "/amurcore/amur/catalog/fetchcatalog";
			logger.debug("catalogURL  :::: " + catalogURL);
			String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
			JSONObject catalogobj = JSONObject.fromObject(catalogJson);
			logger.debug("catalogobj [" + catalogobj + "] catalogobj to string[" + catalogobj.toString() + "]");
			catalogRespJSON = catalogobj;

			logger.debug("fetching manufacturer");
			String manfURL = serverIp + "/amurcore/amur/catalog/fetchmanufacturers";
			logger.debug("manfURL  :::: " + manfURL);
			String manfJson = httpPostRequestHandler.sendRestPostRequest(manfURL);
			JSONObject manfobj = JSONObject.fromObject(manfJson);
			logger.debug("manfobj [" + catalogobj + "] catalogobj to string[" + manfobj.toString() + "]");
			manfRespJSON = manfobj;

			logger.debug("Fetching Supplier products");
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");

			productsDAO = new ProductsDAO();
			responseDTO = productsDAO.fetchSupplyCatalog(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("CAT_PRODUCTS");
				// responseJSON.put("REFERRAL_RATE", referralRate);
				// responseJSON.put("HEALTHPREMIUM", healthPremium);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			result = "success";
			System.out.println("resultJson [" + responseJSON.toString() + "] catalogRespJSON [" + catalogRespJSON
					+ "] manfRespJSON [" + manfRespJSON + "] result[" + result + "]");

		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}

		return result;
	}

	public String createDemandProducts() {

		logger.info("Inside ProductAction createDemandProducts.");
		ProductsDAO productDAO = null;
		ArrayList<String> errors = null;
		JSONObject Resp_Message = null;

		try {

			String catname = getCatname();
			String subcatname = getSubcatname();
			String manfname = getManfname();
			String prdSupplierId = getPrdSupplierId();
			String amurDemandName = getAmurDemandName();
			String productDemandDesc = getProductDemandDesc();
			String skuSize = getSkuSize();
			String skuUnit = getSkuUnit();
			String sellPriceNoVat = getSellPriceNoVat();
			String markupType = getMarkupType();
			String markupRate = getMarkupRate();
			String markupAmountNoVat = getMarkupAmountNoVat();
			/*
			 * String markupVatType = getMarkupVatType(); String markupAmountVat =
			 * getMarkupAmountVat(); String markupAmtWithVat = getMarkupAmtWithVat();
			 */

			String referralRate = getReferralRate();
			String referralAmtNoVat = getReferralAmtNoVat();
			/*
			 * String referralVatType = getReferralVatType(); String referralVatAmt =
			 * getReferralVatAmt(); String referralAmtVat = getReferralAmtVat();
			 */

			String healthPremiumRate = getHealthPremiumRate();
			String healthPremiumNoVat = getHealthPremiumNoVat();
			/*
			 * String healthPremiumVatType = getHealthPremiumVatType(); String
			 * healthPremiumVatAmt = getHealthPremiumVatAmt(); String healthPremiumVat =
			 * getHealthPremiumVat();
			 */
			File offerImage = getOfferImage();

			String sellPriceVat = getSellPriceVat();

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			Resp_Message = new JSONObject();

			requestJSON.put("catId", catname);
			requestJSON.put("subcatId", subcatname);
			requestJSON.put("manfname", manfname);
			requestJSON.put("prdSupplierId", prdSupplierId);
			requestJSON.put("amurDemandName", amurDemandName);
			requestJSON.put("productDemandDesc", productDemandDesc);
			requestJSON.put("skuSize", skuSize);
			requestJSON.put("skuUnit", skuUnit);
			requestJSON.put("sellPriceNoVat", sellPriceNoVat);

			requestJSON.put("markupType", markupType);
			requestJSON.put("markupRate", markupRate);
			requestJSON.put("markupAmountNoVat", markupAmountNoVat);
			/*
			 * requestJSON.put("markupVatType", markupVatType);
			 * requestJSON.put("markupAmountVat", markupAmountVat);
			 * requestJSON.put("markupAmtWithVat", markupAmtWithVat);
			 */

			requestJSON.put("referralRate", referralRate);
			requestJSON.put("referralAmtNoVat", referralAmtNoVat);
			/*
			 * requestJSON.put("referralVatType", referralVatType);
			 * requestJSON.put("referralVatAmt", referralVatAmt);
			 * requestJSON.put("referralAmtVat", referralAmtVat);
			 */

			requestJSON.put("healthPremiumRate", healthPremiumRate);
			requestJSON.put("healthPremiumNoVat", healthPremiumNoVat);
			/*
			 * requestJSON.put("healthPremiumVatType", healthPremiumVatType);
			 * requestJSON.put("healthPremiumVatAmt", healthPremiumVatAmt);
			 * requestJSON.put("healthPremiumVat", healthPremiumVat);
			 */

			requestJSON.put("sellPriceVat", sellPriceVat);
			requestJSON.put("productImage", offerImage.getName());

			System.out.println("Response :: " + requestJSON.toString());

			requestDTO.setRequestJSON(requestJSON);
			productDAO = new ProductsDAO();
			responseDTO = productDAO.saveDemandProducts(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
				logger.debug("Response JSON [" + responseJSON + "]");
				responseJSON.put("remarks", "SUCCESS");

				if (offerImage.renameTo(
						new File("file:///C:/Uploads/", offerImage.getName().replaceAll("\\.tmp$", ".jpg")))) {
					System.out.println("File is moved successful!");
				} else {
					System.out.println("File is failed to move!");
				}

				// insert audit trace here

				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				responseJSON.put("remarks", "FAILURE");
				result = "fail";
			}

		} catch (Exception e) {
			this.logger.debug("Got Exception in createDemandProducts ProductAction [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in createDemandProducts ProductAction [" + e.getMessage() + "]");
		}

		return result;

	}

	// Edit Product Information
	public String modifyProductInformation() {

		logger.info("Inside ProductAction modifyProductInformation.");
		ProductsDAO productDAO = null;
		ArrayList<String> errors = null;
		JSONObject Resp_Message = null;

		Cloudinary cloudinary = null;

		try {

			Map config = new HashMap();
			config.put("cloud_name", "cevaltd");
			config.put("api_key", "456274844924271");
			config.put("api_secret", "occwGsWdj0YbeTbOVczQSBhMStE");
			cloudinary = new Cloudinary(config);

			String prdid = getPrdid();
			String catname = getCatname();
			String subcatname = getSubcatname();
			String manfname = getManfname();
			String prdSupplierId = getPrdSupplierId();
			String amurDemandName = getAmurDemandName();
			String productDemandDesc = getProductDemandDesc();
			String skuSize = getSkuSize();
			String skuUnit = getSkuUnit();
			String sellPriceNoVat = getSellPriceNoVat();
			String markupType = getMarkupType();
			String markupRate = getMarkupRate();
			String markupAmountNoVat = getMarkupAmountNoVat();
			String priceVatType = getPriceVatType();
			String prdImageName = getPrdImageName();

			String referralRate = getReferralRate();
			String referralAmtNoVat = getReferralAmtNoVat();
			String healthPremiumRate = getHealthPremiumRate();
			String healthPremiumNoVat = getHealthPremiumNoVat();
			File prdImg = getPrdImg();

			Double mkpRate = (Double.parseDouble(markupRate)) / 100;
			Double refRate = (Double.parseDouble(referralRate)) / 100;
			Double healthRate = (Double.parseDouble(healthPremiumRate)) / 100;

			System.out.println("mkp :: " + mkpRate);
			System.out.println("ref :: " + refRate);
			System.out.println("healthRate :: " + healthRate);

			String sellPriceVat = getSellPriceVat();

			String newImage;

			if (prdImg != null) {

				if (prdImg.renameTo(new File(prdImg.getPath().replace(prdImg.getName(), ""),
						prdImg.getName().replaceAll("\\.tmp$", "")))) {
					System.out.println("File is moved successful!");
				} else {
					System.out.println("File is failed to move!");
				}

				System.out.println("File path :: " + prdImg.getPath());

				File toUpload = new File(prdImg.getPath().replace(prdImg.getName(), ""),
						prdImg.getName().replaceAll("\\.tmp$", ""));
				Map uploadResult = cloudinary.uploader().upload(toUpload,
						ObjectUtils.asMap("folder", "all/", "public_id", "" + prdImageName + ""));
				String imageUrl = uploadResult.get("secure_url").toString();
				String imgNewName = uploadResult.get("public_id").toString();
				System.out.println("Image url :: " + imageUrl);
				System.out.println("Image name :: " + imgNewName.replaceAll("all/", ""));
				newImage = imgNewName.replaceAll("all/", "");
			} else {
				newImage = prdImageName;
				System.out.println("Image name = " + newImage);
			}

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			Resp_Message = new JSONObject();

			requestJSON.put("prdid", prdid);
			requestJSON.put("catId", catname);
			requestJSON.put("subcatId", subcatname);
			requestJSON.put("manfname", manfname);

			requestJSON.put("prdSupplierId", prdSupplierId);
			requestJSON.put("amurDemandName", amurDemandName);
			requestJSON.put("productDemandDesc", productDemandDesc);
			requestJSON.put("skuSize", skuSize);
			requestJSON.put("skuUnit", skuUnit);
			requestJSON.put("sellPriceNoVat", sellPriceNoVat);

			requestJSON.put("markupType", markupType);
			requestJSON.put("markupRate", mkpRate);
			requestJSON.put("markupAmountNoVat", markupAmountNoVat);

			requestJSON.put("vatType", priceVatType);

			requestJSON.put("referralRate", refRate);
			requestJSON.put("referralAmtNoVat", referralAmtNoVat);
			requestJSON.put("healthPremiumRate", healthRate);
			requestJSON.put("healthPremiumNoVat", healthPremiumNoVat);

			requestJSON.put("sellPriceVat", sellPriceVat);
			requestJSON.put("productImage", newImage);

			requestDTO.setRequestJSON(requestJSON);
			productDAO = new ProductsDAO();
			responseDTO = productDAO.modifyDemandProducts(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			/*
			 * if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			 * responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
			 * logger.debug("Response JSON [" + responseJSON + "]");
			 * responseJSON.put("remarks", "SUCCESS");
			 * 
			 * // insert audit trace here
			 * 
			 * result = "success"; } else { errors = (ArrayList<String>)
			 * responseDTO.getErrors(); for (int i = 0; i < errors.size(); i++) {
			 * addActionError(errors.get(i)); } responseJSON.put("remarks", "FAILURE");
			 * result = "fail"; }
			 */

		} catch (Exception e) {
			this.logger.debug("Got Exception in ProductAction modifyProductInformation. [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in ProductAction modifyProductInformation. [" + e.getMessage() + "]");
		}

		return result;

	}

	public String createSupplyProducts() {

		logger.info("Inside ProductAction createSupplyProducts.");
		ProductsDAO productDAO = null;
		ArrayList<String> errors = null;
		JSONObject Resp_Message = null;

		try {

			String catname2 = getCatname2();
			String subcatname2 = getSubcatname2();
			String manfname2 = getManfname2();
			String productSupplierName = getProductSupplierName();
			String productQuantity = getProductQuantity();
			String reorderLvl = getReorderLvl();
			String buyPriceNoVat = getBuyPriceNoVat();
			String buyPriceVat = getBuyPriceVat();
			String vatRate = getVatRate2();
			// String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();

			System.out.println(catname2);
			System.out.println(productSupplierName);
			System.out.println(reorderLvl);

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			Resp_Message = new JSONObject();

			requestJSON.put("catId", catname2);
			requestJSON.put("subcatId", subcatname2);
			requestJSON.put("manfname", manfname2);
			requestJSON.put("productSupplierName", productSupplierName);
			requestJSON.put("productQuantity", productQuantity);
			requestJSON.put("reorderLvl", reorderLvl);
			requestJSON.put("buyPriceNoVat", buyPriceNoVat);
			requestJSON.put("buyPriceVat", buyPriceVat);
			requestJSON.put("vatRate", vatRate);
			// requestJSON.put("mkrid", mkrid);

			System.out.println("Product Supply request name :: " + requestJSON.toString());

			requestDTO.setRequestJSON(requestJSON);
			productDAO = new ProductsDAO();
			responseDTO = productDAO.saveSupplyProducts(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("Response_Message");
				logger.debug("Response JSON [" + responseJSON + "]");

				// insert audit trace here

				result = "success";
				responseJSON.put("remarks", result);
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
				responseJSON.put("remarks", result);
			}

		} catch (Exception e) {
			this.logger.debug("Got Exception in createSupplyProducts ProductAction [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in createSupplyProducts ProductAction [" + e.getMessage() + "]");
		}

		return result;

	}

	public String productCreateConfirm() {
		logger.debug("Inside productCreateConfirm...");

		JSONObject disctypeJson = null;
		JSONArray disctypeJsonArray = null;

		try {

			disctypeJsonArray = new JSONArray();
			disctypeJson = new JSONObject();
			disctypeJson.put("disctype", "F");
			disctypeJson.put("typedesc", "Flat");
			disctypeJsonArray.add(disctypeJson);
			disctypeJson.clear();
			disctypeJson.put("disctype", "P");
			disctypeJson.put("typedesc", "Percentage");
			disctypeJsonArray.add(disctypeJson);
			disctypeJson.clear();

			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();

			responseJSON = new JSONObject();
			responseJSON.put("prdname", getPrdname());
			responseJSON.put("catname", getCatname());
			responseJSON.put("subcatname", getSubcatname());
			responseJSON.put("manfname", getManfname());
			responseJSON.put("prddesc", getPrddesc().replace(' ', '+'));
			responseJSON.put("pic1", getPic1());
			responseJSON.put("modelno", getModelno().replace(' ', '+'));
			responseJSON.put("pcode", getPcode());
			responseJSON.put("disctype", getDisctype());
			responseJSON.put("discount", getDiscount());
			responseJSON.put("quantity", getQuantity());
			responseJSON.put("reordrlvl", getReordrlvl());
			responseJSON.put("provider", getProvider().replace(' ', '+'));
			responseJSON.put("type", getType());
			responseJSON.put("wholesaleprice", getWholesaleprice());
			responseJSON.put("disctype_json", disctypeJsonArray);

			logger.debug("responseJSON[" + responseJSON + "]");

			logger.debug("fetching catalog");
			String catalogURL = serverIp + "/amurcore/amur/catalog/fetchcatalog";
			logger.debug("catalogURL  :::: " + catalogURL);
			String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
			JSONObject catalogobj = JSONObject.fromObject(catalogJson);
			logger.debug("catalogobj [" + catalogobj + "] catalogobj to string[" + catalogobj.toString() + "]");
			catalogRespJSON = catalogobj;

			logger.debug("fetching manufacturer");
			String manfURL = serverIp + "/amurcore/amur/catalog/fetchmanufacturers";
			logger.debug("manfURL  :::: " + manfURL);
			String manfJson = httpPostRequestHandler.sendRestPostRequest(manfURL);
			JSONObject manfobj = JSONObject.fromObject(manfJson);
			logger.debug("manfobj [" + catalogobj + "] catalogobj to string[" + manfobj.toString() + "]");
			manfRespJSON = manfobj;

			result = "success";
			return result;
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			return result;
		}

	}

	public String productCreateAck() {

		logger.debug("inside [ProductAction][productCreateAck].. ");
		ResponseDTO responseDTO = new ResponseDTO();
		JSONObject disctypeJson = null;
		JSONArray disctypeJsonArray = null;
		try {
			disctypeJsonArray = new JSONArray();
			disctypeJson = new JSONObject();
			disctypeJson.put("disctype", "F");
			disctypeJson.put("typedesc", "Flat");
			disctypeJsonArray.add(disctypeJson);
			disctypeJson.clear();
			disctypeJson.put("disctype", "P");
			disctypeJson.put("typedesc", "Percentage");
			disctypeJsonArray.add(disctypeJson);
			disctypeJson.clear();

			session = ServletActionContext.getRequest().getSession();

			String type = getType();
			String prdName = getPrdname();
			String prdDesc = getPrddesc();
			String catid = getCatid();
			String catname = getCatname();
			String subcatid = getSubcatid();
			String subcatname = getSubcatname();
			String manfid = getManfid();
			String manfname = getManfname();
			String modelno = getModelno();
			String price = getPcode();
			String disctype = getDisctype();
			String discount = getDiscount();
			String quantity = getQuantity();
			String reorderlvl = getReordrlvl();
			String provider = getProvider();
			String wholesaleprice = getWholesaleprice();
			String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();

			if ("Percentage".equalsIgnoreCase(disctype))
				disctype = "P";
			else
				disctype = "F";

			System.out.println("prdid [" + prdid + "] type[" + type + "] prdName[" + prdName + "] prdDesc[" + prdDesc
					+ "] catid[" + catid + "] subcatid[" + subcatid + "] manfid[" + manfid + "]");
			System.out.println(
					"modelno [" + modelno + "] price[" + price + "] disctype[" + disctype + "] discount[" + discount
							+ "] quantity[" + quantity + "] reorderlvl[" + reorderlvl + "] provider[" + provider + "]");
			System.out.println(("mkrid[" + mkrid + "]"));

			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp + "/amurcore/amur/catalog/createproduct/" + prdName + "/" + prdDesc + "/"
					+ catid + "/" + subcatid + "/" + manfid + "/" + modelno + "/" + price + "/" + disctype + "/"
					+ discount + "/" + quantity + "/" + reorderlvl + "/" + provider + "/" + mkrid + "/"
					+ wholesaleprice;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string[" + obj.toString() + "]");
			responseJSON = obj;
			if ("SUCCESS".equalsIgnoreCase(obj.getString("STATUS"))) {
				responseJSON.put("remarks", "SUCCESS");
				remarks = "Product Details Modified Successfully.";
				responseDTO.addMessages("Product Created Successfully.");
				result = "success";
			} else {
				responseJSON.put("remarks", "FAILURE");
				remarks = "Product Creation Failed.";
				responseDTO.addError("Product Details Modify Failed.");
				result = "fail";
			}

			responseJSON.put("type", type);
			responseJSON.put("disctype_json", disctypeJsonArray);

			System.out.println("resultJson [" + responseJSON.toString() + "] result[" + result + "]");
		} catch (Exception e) {
			result = "fail";
			/*
			 * logger.debug("Exception in [ProductAction][fetchProducts] [" + e.getMessage()
			 * + "]");
			 */
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}

	/*
	 * public String productInformation() {
	 * logger.debug("Inside productInformation.."); ArrayList<String> errors = null;
	 * JSONObject requestJSON = null; JSONArray disctypeJsonArray = null;
	 * ProductsDAO productsDAO = null; try { String prdid = getPrdid(); String type
	 * = getType();
	 * 
	 * System.out.println("prdid [" + prdid + "] type[" + type + "]"); requestJSON =
	 * new JSONObject(); requestJSON.put("prdId", prdid);
	 * 
	 * requestDTO = new RequestDTO(); requestDTO.setRequestJSON(requestJSON);
	 * productsDAO = new ProductsDAO(); responseDTO =
	 * productsDAO.fetchProductCatalog(requestDTO); logger.debug("Response DTO [" +
	 * responseDTO + "]");
	 * 
	 * requestDTO = new RequestDTO(); requestDTO.setRequestJSON(requestJSON);
	 * productsDAO = new ProductsDAO(); responseDTO =
	 * productsDAO.fetchDemandProducts(requestDTO); logger.debug("Response DTO [" +
	 * responseDTO + "]");
	 * 
	 * requestDTO = new RequestDTO(); requestDTO.setRequestJSON(requestJSON);
	 * productsDAO = new ProductsDAO(); responseDTO =
	 * productsDAO.fetchDemandProducts(requestDTO); logger.debug("Response DTO [" +
	 * responseDTO + "]");
	 * 
	 * if (responseDTO != null && responseDTO.getErrors().size() == 0) {
	 * responseJSON = (JSONObject) responseDTO.getData().get("DEMAND_PRODUCTS");
	 * responseJSON = (JSONObject) responseDTO.getData().get("DEMAND_PRODUCTS");
	 * responseJSON = (JSONObject) responseDTO.getData().get("DEMAND_PRODUCTS");
	 * logger.debug("Response JSON [" + responseJSON + "]"); result = "success"; }
	 * else { errors = (ArrayList<String>) responseDTO.getErrors(); for (int i = 0;
	 * i < errors.size(); i++) { addActionError(errors.get(i)); } result = "fail"; }
	 * 
	 * /* session = ServletActionContext.getRequest().getSession();
	 * requestJSON.put("TYPE", getType()); requestJSON.put("MAKER_ID",
	 * session.getAttribute(CevaCommonConstants.MAKER_ID) .toString());
	 *
	 * 
	 * logger.debug("Web Service URL  :::: " + webServiceURL); String json1 =
	 * httpPostRequestHandler.sendRestPostRequest(webServiceURL); JSONObject obj =
	 * JSONObject.fromObject(json1); logger.debug("End to Sent Mobile Otp >> [" +
	 * obj + "] obj to string[" + obj.toString() + "]"); responseJSON = obj;
	 * responseJSON.put("type", type); responseJSON.put("prdid", prdid);
	 * responseJSON.put("disctype_json", disctypeJsonArray);
	 * 
	 * logger.debug("fetching catalog"); String catalogURL = serverIp +
	 * "/amurcore/amur/catalog/fetchcatalog"; logger.debug("catalogURL  :::: " +
	 * catalogURL); String catalogJson =
	 * httpPostRequestHandler.sendRestPostRequest(catalogURL); JSONObject catalogobj
	 * = JSONObject.fromObject(catalogJson); logger.debug("catalogobj [" +
	 * catalogobj + "] catalogobj to string[" + catalogobj.toString() + "]");
	 * catalogRespJSON = catalogobj;
	 * 
	 * logger.debug("fetching manufacturer"); String manfURL = serverIp +
	 * "/amurcore/amur/catalog/fetchmanufacturers"; logger.debug("manfURL  :::: " +
	 * manfURL); String manfJson =
	 * httpPostRequestHandler.sendRestPostRequest(manfURL); JSONObject manfobj =
	 * JSONObject.fromObject(manfJson); logger.debug("manfobj [" + catalogobj +
	 * "] catalogobj to string[" + manfobj.toString() + "]"); manfRespJSON =
	 * manfobj;
	 * 
	 * result = "success"; System.out.println("resultJson [" +
	 * responseJSON.toString() + "] catalogRespJSON [" + catalogRespJSON +
	 * "] manfRespJSON [" + manfRespJSON + "] result[" + result + "]");
	 * 
	 * if (getType().equalsIgnoreCase("Modify")) { productInfoPage =
	 * "productModifyInformation"; } else if (getType().equalsIgnoreCase("View")) {
	 * productInfoPage = "productViewInformation"; } else if
	 * (getType().equalsIgnoreCase("ActiveDeactive")) { productInfoPage =
	 * "productActivate"; } else { productInfoPage = "productViewInformation"; }
	 * 
	 * logger.debug(" productInfoPage [" + productInfoPage + "]");
	 * 
	 * } catch (Exception e) { result = "fail"; e.printStackTrace();
	 * addActionError("Internal error occured."); } finally { errors = null; }
	 * 
	 * return result; }
	 */

	public String fetchProductInfo() {

		System.out.println("Inside [ProductAction] [fetchProductInfo]");
		CategoryManagementDAO categoryMgt = null;
		ProductsDAO productsDAO = null;
		SupplierDAO supplierDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			catalogRespJSON = new JSONObject();
			manfRespJSON = new JSONObject();
			productJSON = new JSONObject();
			supplyProductJSON = new JSONObject();

			requestDTO = new RequestDTO();

			String prdid = getPrdid();
			String type = getType();

			requestJSON.put("prdid", prdid);
			requestDTO.setRequestJSON(requestJSON);

			// Fetching product category and subcategory
			categoryMgt = new CategoryManagementDAO();
			responseDTO = categoryMgt.fetchAllCategories(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				catalogRespJSON = (JSONObject) responseDTO.getData().get("CATEGORIES");
				logger.debug("Category Info Response JSON :: [" + catalogRespJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			// Fetching Suppliers
			supplierDAO = new SupplierDAO();
			responseDTO = supplierDAO.fetchAllSuppliers(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				manfRespJSON = (JSONObject) responseDTO.getData().get("SUPPLIER_LIST");
				logger.debug("Response JSON [" + manfRespJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			// Fetch Supply Products
			productsDAO = new ProductsDAO();
			responseDTO = productsDAO.fetchSupplyCatalog(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				supplyProductJSON = (JSONObject) responseDTO.getData().get("CAT_PRODUCTS");
				logger.debug("Supply Product JSON [" + supplyProductJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			// Fetchin product Info
			productsDAO = new ProductsDAO();
			responseDTO = productsDAO.fetchProductInfo(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				productJSON = (JSONObject) responseDTO.getData().get("PRODUCT_INFO");
				logger.debug("Response JSON [" + productJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			logger.debug("Response JSON [" + productJSON + "]");

			// responseJSON.put("Type", type);

			if (getType().equalsIgnoreCase("Modify")) {
				productInfoPage = "productModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				// productInfoPage = "productViewInformation";
				productInfoPage = "productModifyInformation";
			} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
				productInfoPage = "productActivate";
			} else {
				productInfoPage = "productViewInformation";
			}

		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [ProductAction] [fetchProductInfo] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			categoryMgt = null;
		}

		return result;
	}

	public String productInformation() {
		logger.debug("Inside productInformation..");
		ArrayList<String> errors = null;
		JSONObject disctypeJson = null;
		JSONArray disctypeJsonArray = null;

		try {
			String prdid = getPrdid();
			String type = getType();

			System.out.println("prdid[" + prdid + "] type[" + type + "]");

			disctypeJsonArray = new JSONArray();
			disctypeJson = new JSONObject();
			disctypeJson.put("disctype", "F");
			disctypeJson.put("typedesc", "Flat");
			disctypeJsonArray.add(disctypeJson);
			disctypeJson.clear();
			disctypeJson.put("disctype", "P");
			disctypeJson.put("typedesc", "Percentage");
			disctypeJsonArray.add(disctypeJson);
			disctypeJson.clear();

			System.out.println("prdid [" + prdid + "] type[" + type + "]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp + "/amurcore/amur/catalog/viewproduct/" + prdid;

			/*
			 * session = ServletActionContext.getRequest().getSession();
			 * requestJSON.put("TYPE", getType()); requestJSON.put("MAKER_ID",
			 * session.getAttribute(CevaCommonConstants.MAKER_ID) .toString());
			 */

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string[" + obj.toString() + "]");
			responseJSON = obj;
			responseJSON.put("type", type);
			responseJSON.put("prdid", prdid);
			responseJSON.put("disctype_json", disctypeJsonArray);

			logger.debug("fetching catalog");
			String catalogURL = serverIp + "/amurcore/amur/catalog/fetchcatalog";
			logger.debug("catalogURL  :::: " + catalogURL);
			String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
			JSONObject catalogobj = JSONObject.fromObject(catalogJson);
			logger.debug("catalogobj [" + catalogobj + "] catalogobj to string[" + catalogobj.toString() + "]");
			catalogRespJSON = catalogobj;

			logger.debug("fetching manufacturer");
			String manfURL = serverIp + "/amurcore/amur/catalog/fetchmanufacturers";
			logger.debug("manfURL  :::: " + manfURL);
			String manfJson = httpPostRequestHandler.sendRestPostRequest(manfURL);
			JSONObject manfobj = JSONObject.fromObject(manfJson);
			logger.debug("manfobj [" + catalogobj + "] catalogobj to string[" + manfobj.toString() + "]");
			manfRespJSON = manfobj;

			result = "success";
			System.out.println("resultJson [" + responseJSON.toString() + "] catalogRespJSON [" + catalogRespJSON
					+ "] manfRespJSON [" + manfRespJSON + "] result[" + result + "]");

			if (getType().equalsIgnoreCase("Modify")) {
				productInfoPage = "productModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				productInfoPage = "productViewInformation";
			} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
				productInfoPage = "productActivate";
			} else {
				productInfoPage = "productViewInformation";
			}

			logger.debug(" productInfoPage [" + productInfoPage + "]");

		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}

		return result;
	}

	/*
	 * public String productModifyConfirm() {
	 * logger.debug("Inside productModifyConfirm...");
	 * 
	 * JSONObject disctypeJson = null; JSONArray disctypeJsonArray = null;
	 * 
	 * try {
	 * 
	 * disctypeJsonArray = new JSONArray(); disctypeJson = new JSONObject();
	 * disctypeJson.put("disctype", "F"); disctypeJson.put("typedesc", "Flat");
	 * disctypeJsonArray.add(disctypeJson); disctypeJson.clear();
	 * disctypeJson.put("disctype", "P"); disctypeJson.put("typedesc",
	 * "Percentage"); disctypeJsonArray.add(disctypeJson); disctypeJson.clear();
	 * 
	 * HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
	 * 
	 * responseJSON = new JSONObject(); responseJSON.put("prdid", getPrdid());
	 * responseJSON.put("prdname", getPrdname()); responseJSON.put("catname",
	 * getCatname()); responseJSON.put("subcatname", getSubcatname());
	 * responseJSON.put("manfname", getManfname()); responseJSON.put("prddesc",
	 * getPrddesc()); responseJSON.put("pic1", getPic1());
	 * responseJSON.put("modelno", getModelno()); responseJSON.put("pcode",
	 * getPcode()); responseJSON.put("disctype", getDisctype());
	 * responseJSON.put("discount", getDiscount()); responseJSON.put("quantity",
	 * getQuantity()); responseJSON.put("reordrlvl", getReordrlvl());
	 * responseJSON.put("provider", getProvider()); responseJSON.put("type",
	 * getType()); responseJSON.put("wholesaleprice", getWholesaleprice());
	 * responseJSON.put("disctype_json", disctypeJsonArray);
	 * 
	 * logger.debug("responseJSON[" + responseJSON + "]");
	 * 
	 * logger.debug("fetching catalog"); String catalogURL = serverIp +
	 * "/amurcore/amur/catalog/fetchcatalog"; logger.debug("catalogURL  :::: " +
	 * catalogURL); String catalogJson =
	 * httpPostRequestHandler.sendRestPostRequest(catalogURL); JSONObject catalogobj
	 * = JSONObject.fromObject(catalogJson); logger.debug("catalogobj [" +
	 * catalogobj + "] catalogobj to string[" + catalogobj.toString() + "]");
	 * catalogRespJSON = catalogobj;
	 * 
	 * logger.debug("fetching manufacturer"); String manfURL = serverIp +
	 * "/amurcore/amur/catalog/fetchmanufacturers"; logger.debug("manfURL  :::: " +
	 * manfURL); String manfJson =
	 * httpPostRequestHandler.sendRestPostRequest(manfURL); JSONObject manfobj =
	 * JSONObject.fromObject(manfJson); logger.debug("manfobj [" + catalogobj +
	 * "] catalogobj to string[" + manfobj.toString() + "]"); manfRespJSON =
	 * manfobj;
	 * 
	 * if (getType().equalsIgnoreCase("Modify")) { productInfoPage =
	 * "productModifyConfirm"; } else if
	 * (getType().equalsIgnoreCase("ActiveDeactive")) { productInfoPage =
	 * "productActivate"; }
	 * 
	 * result = "success"; return result; } catch (Exception e) { result = "fail";
	 * e.printStackTrace(); return result; }
	 * 
	 * }
	 * 
	 * public String productModifyAck() {
	 * 
	 * logger.debug("inside [ProductAction][productModifyAck].. "); JSONObject
	 * disctypeJson = null; JSONArray disctypeJsonArray = null; try {
	 * disctypeJsonArray = new JSONArray(); disctypeJson = new JSONObject();
	 * disctypeJson.put("disctype", "F"); disctypeJson.put("typedesc", "Flat");
	 * disctypeJsonArray.add(disctypeJson); disctypeJson.clear();
	 * disctypeJson.put("disctype", "P"); disctypeJson.put("typedesc",
	 * "Percentage"); disctypeJsonArray.add(disctypeJson); disctypeJson.clear();
	 * 
	 * session = ServletActionContext.getRequest().getSession();
	 * 
	 * String type = getType(); String prdid = getPrdid(); String prdName =
	 * getPrdname(); String prdDesc = getPrddesc().replace(' ', '+'); String catid =
	 * getCatid(); String catname = getCatname(); String subcatid = getSubcatid();
	 * String subcatname = getSubcatname(); String manfid = getManfid(); String
	 * manfname = getManfname(); String modelno = getModelno().replace(' ', '+');
	 * String price = getPcode(); String disctype = getDisctype(); String discount =
	 * getDiscount(); String quantity = getQuantity(); String reorderlvl =
	 * getReordrlvl(); String provider = getProvider(); String wholesaleprice =
	 * getWholesaleprice().replace(' ', '+'); String mkrid =
	 * session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
	 * 
	 * if ("Percentage".equalsIgnoreCase(disctype)) disctype = "P"; else disctype =
	 * "F";
	 * 
	 * System.out.println("prdid [" + prdid + "] type[" + type + "] prdName[" +
	 * prdName + "] prdDesc[" + prdDesc + "] catid[" + catid + "] subcatid[" +
	 * subcatid + "] manfid[" + manfid + "]"); System.out.println( "modelno [" +
	 * modelno + "] price[" + price + "] disctype[" + disctype + "] discount[" +
	 * discount + "] quantity[" + quantity + "] reorderlvl[" + reorderlvl +
	 * "] provider[" + provider + "]"); System.out.println(("mkrid[" + mkrid +
	 * "]"));
	 * 
	 * HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
	 * String webServiceURL = serverIp + "/amurcore/amur/catalog//modifyproduct/" +
	 * prdid + "/" + prdName + "/" + prdDesc + "/" + catid + "/" + subcatid + "/" +
	 * manfid + "/" + modelno + "/" + price + "/" + disctype + "/" + discount + "/"
	 * + quantity + "/" + reorderlvl + "/" + provider + "/" + mkrid + "/" +
	 * wholesaleprice;
	 * 
	 * logger.debug("Web Service URL  :::: " + webServiceURL); String json1 =
	 * httpPostRequestHandler.sendRestPostRequest(webServiceURL); JSONObject obj =
	 * JSONObject.fromObject(json1); logger.debug("End to Sent Mobile Otp >> [" +
	 * obj + "] obj to string[" + obj.toString() + "]"); responseJSON = obj; if
	 * ("SUCCESS".equalsIgnoreCase(obj.getString("STATUS"))) {
	 * responseJSON.put("remarks", "SUCCESS"); result = "success"; } else {
	 * responseJSON.put("remarks", "FAILURE"); result = "fail"; }
	 * responseJSON.put("type", type); responseJSON.put("disctype_json",
	 * disctypeJsonArray);
	 * 
	 * System.out.println("resultJson [" + responseJSON.toString() + "] result[" +
	 * result + "]"); } catch (Exception e) { result = "fail"; /*
	 * logger.debug("Exception in [ProductAction][fetchProducts] [" + e.getMessage()
	 * + "]");
	 *
	 * e.printStackTrace(); addActionError("Internal error occured."); } finally {
	 * requestDTO = null; responseDTO = null; requestJSON = null; } result =
	 * "success"; return result;
	 * 
	 * }
	 */

	public String viewProduct() {

		logger.debug("inside [ProductAction][viewProduct].. ");
		try {
			String prdid = getPrdid();
			String type = getType();

			System.out.println("prdid [" + prdid + "] type[" + type + "]");

			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = serverIp + "/amurcore/amur/catalog/viewproduct/" + prdid;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string[" + obj.toString() + "]");
			responseJSON = obj;
			responseJSON.put("type", type);
			result = "success";
			System.out.println("resultJson [" + responseJSON.toString() + "] result[" + result + "]");
		} catch (Exception e) {
			result = "fail";
			/*
			 * logger.debug("Exception in [ProductAction][fetchProducts] [" + e.getMessage()
			 * + "]");
			 */
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
		result = "success";
		return result;

	}

	// Generate
	// timestamp...............................................................
	private static java.sql.Timestamp getCurrentTimeStamp() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());
	}

	// Generate Unique ID ......................................................
	public String generateUniqueId() {
		return UUID.randomUUID().toString().substring(0, 8).toUpperCase()
				+ UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
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

	public String getProductInfoPage() {
		return productInfoPage;
	}

	public void setProductInfoPage(String productInfoPage) {
		this.productInfoPage = productInfoPage;
	}

	public JSONObject getCatalogRespJSON() {
		return catalogRespJSON;
	}

	public void setCatalogRespJSON(JSONObject catalogRespJSON) {
		this.catalogRespJSON = catalogRespJSON;
	}

	public JSONObject getProductJSON() {
		return productJSON;
	}

	public void setProductJSON(JSONObject productJSON) {
		this.productJSON = productJSON;
	}

	public JSONObject getManfRespJSON() {
		return manfRespJSON;
	}

	public void setManfRespJSON(JSONObject manfRespJSON) {
		this.manfRespJSON = manfRespJSON;
	}

	public JSONObject getSupplyProductJSON() {
		return supplyProductJSON;
	}

	public void setSupplyProductJSON(JSONObject supplyProductJSON) {
		this.supplyProductJSON = supplyProductJSON;
	}

	public String getPrdname() {
		return prdname;
	}

	public void setPrdname(String prdname) {
		this.prdname = prdname;
	}

	public String getCatname() {
		return catname;
	}

	public void setCatname(String catname) {
		this.catname = catname;
	}

	public String getSubcatname() {
		return subcatname;
	}

	public void setSubcatname(String subcatname) {
		this.subcatname = subcatname;
	}

	public String getManfname() {
		return manfname;
	}

	public void setManfname(String manfname) {
		this.manfname = manfname;
	}

	public String getPrddesc() {
		return prddesc;
	}

	public void setPrddesc(String prddesc) {
		this.prddesc = prddesc;
	}

	public String getPic1() {
		return pic1;
	}

	public void setPic1(String pic1) {
		this.pic1 = pic1;
	}

	public String getModelno() {
		return modelno;
	}

	public void setModelno(String modelno) {
		this.modelno = modelno;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDisctype() {
		return disctype;
	}

	public void setDisctype(String disctype) {
		this.disctype = disctype;
	}

	public String getReordrlvl() {
		return reordrlvl;
	}

	public void setReordrlvl(String reordrlvl) {
		this.reordrlvl = reordrlvl;
	}

	public String getServprvdr() {
		return servprvdr;
	}

	public void setServprvdr(String servprvdr) {
		this.servprvdr = servprvdr;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getSubcatid() {
		return subcatid;
	}

	public void setSubcatid(String subcatid) {
		this.subcatid = subcatid;
	}

	public String getManfid() {
		return manfid;
	}

	public void setManfid(String manfid) {
		this.manfid = manfid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getWholesaleprice() {
		return wholesaleprice;
	}

	public void setWholesaleprice(String wholesaleprice) {
		this.wholesaleprice = wholesaleprice;
	}

	public String getProductDemandName() {
		return productDemandName;
	}

	public void setProductDemandName(String productDemandName) {
		this.productDemandName = productDemandName;
	}

	public String getAmurDemandName() {
		return amurDemandName;
	}

	public void setAmurDemandName(String amurDemandName) {
		this.amurDemandName = amurDemandName;
	}

	public String getSellPriceNoVat() {
		return sellPriceNoVat;
	}

	public void setSellPriceNoVat(String sellPriceNoVat) {
		this.sellPriceNoVat = sellPriceNoVat;
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	public String getSellPriceVat() {
		return sellPriceVat;
	}

	public void setSellPriceVat(String sellPriceVat) {
		this.sellPriceVat = sellPriceVat;
	}

	public String getCatname2() {
		return catname2;
	}

	public void setCatname2(String catname2) {
		this.catname2 = catname2;
	}

	public String getSubcatname2() {
		return subcatname2;
	}

	public void setSubcatname2(String subcatname2) {
		this.subcatname2 = subcatname2;
	}

	public String getManfname2() {
		return manfname2;
	}

	public void setManfname2(String manfname2) {
		this.manfname2 = manfname2;
	}

	public String getProductSupplierName() {
		return productSupplierName;
	}

	public void setProductSupplierName(String productSupplierName) {
		this.productSupplierName = productSupplierName;
	}

	public String getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getReorderLvl() {
		return reorderLvl;
	}

	public void setReorderLvl(String reorderLvl) {
		this.reorderLvl = reorderLvl;
	}

	public String getBuyPriceNoVat() {
		return buyPriceNoVat;
	}

	public void setBuyPriceNoVat(String buyPriceNoVat) {
		this.buyPriceNoVat = buyPriceNoVat;
	}

	public String getBuyPriceVat() {
		return buyPriceVat;
	}

	public void setBuyPriceVat(String buyPriceVat) {
		this.buyPriceVat = buyPriceVat;
	}

	public String getPrdSupplierId() {
		return prdSupplierId;
	}

	public void setPrdSupplierId(String prdSupplierId) {
		this.prdSupplierId = prdSupplierId;
	}

	public String getProductDemandDesc() {
		return productDemandDesc;
	}

	public void setProductDemandDesc(String productDemandDesc) {
		this.productDemandDesc = productDemandDesc;
	}

	public String getSkuSize() {
		return skuSize;
	}

	public void setSkuSize(String skuSize) {
		this.skuSize = skuSize;
	}

	public String getMarkupRate() {
		return markupRate;
	}

	public void setMarkupRate(String markupRate) {
		this.markupRate = markupRate;
	}

	public String getMarkupAmount() {
		return markupAmount;
	}

	public void setMarkupAmount(String markupAmount) {
		this.markupAmount = markupAmount;
	}

	public String getReferralEarnings() {
		return referralEarnings;
	}

	public void setReferralEarnings(String referralEarnings) {
		this.referralEarnings = referralEarnings;
	}

	public String getHealthPremiumAmt() {
		return healthPremiumAmt;
	}

	public void setHealthPremiumAmt(String healthPremiumAmt) {
		this.healthPremiumAmt = healthPremiumAmt;
	}

	public String getTotalVatAmt() {
		return totalVatAmt;
	}

	public void setTotalVatAmt(String totalVatAmt) {
		this.totalVatAmt = totalVatAmt;
	}

	public String getVatRate2() {
		return vatRate2;
	}

	public void setVatRate2(String vatRate2) {
		this.vatRate2 = vatRate2;
	}

	public String getMarkupType() {
		return markupType;
	}

	public void setMarkupType(String markupType) {
		this.markupType = markupType;
	}

	public String getMarkupAmountNoVat() {
		return markupAmountNoVat;
	}

	public void setMarkupAmountNoVat(String markupAmountNoVat) {
		this.markupAmountNoVat = markupAmountNoVat;
	}

	public String getMarkupVatType() {
		return markupVatType;
	}

	public void setMarkupVatType(String markupVatType) {
		this.markupVatType = markupVatType;
	}

	public String getMarkupAmountVat() {
		return markupAmountVat;
	}

	public void setMarkupAmountVat(String markupAmountVat) {
		this.markupAmountVat = markupAmountVat;
	}

	public String getMarkupAmtWithVat() {
		return markupAmtWithVat;
	}

	public void setMarkupAmtWithVat(String markupAmtWithVat) {
		this.markupAmtWithVat = markupAmtWithVat;
	}

	public String getReferralAmtNoVat() {
		return referralAmtNoVat;
	}

	public void setReferralAmtNoVat(String referralAmtNoVat) {
		this.referralAmtNoVat = referralAmtNoVat;
	}

	public String getReferralVatType() {
		return referralVatType;
	}

	public void setReferralVatType(String referralVatType) {
		this.referralVatType = referralVatType;
	}

	public String getReferralAmtVat() {
		return referralAmtVat;
	}

	public void setReferralAmtVat(String referralAmtVat) {
		this.referralAmtVat = referralAmtVat;
	}

	public String getHealthPremiumRate() {
		return healthPremiumRate;
	}

	public void setHealthPremiumRate(String healthPremiumRate) {
		this.healthPremiumRate = healthPremiumRate;
	}

	public String getHealthPremiumNoVat() {
		return healthPremiumNoVat;
	}

	public void setHealthPremiumNoVat(String healthPremiumNoVat) {
		this.healthPremiumNoVat = healthPremiumNoVat;
	}

	public String getHealthPremiumVatType() {
		return healthPremiumVatType;
	}

	public void setHealthPremiumVatType(String healthPremiumVatType) {
		this.healthPremiumVatType = healthPremiumVatType;
	}

	public String getHealthPremiumVat() {
		return healthPremiumVat;
	}

	public void setHealthPremiumVat(String healthPremiumVat) {
		this.healthPremiumVat = healthPremiumVat;
	}

	public String getReferralVatAmt() {
		return referralVatAmt;
	}

	public void setReferralVatAmt(String referralVatAmt) {
		this.referralVatAmt = referralVatAmt;
	}

	public String getHealthPremiumVatAmt() {
		return healthPremiumVatAmt;
	}

	public void setHealthPremiumVatAmt(String healthPremiumVatAmt) {
		this.healthPremiumVatAmt = healthPremiumVatAmt;
	}

	public String getReferralRate() {
		return referralRate;
	}

	public void setReferralRate(String referralRate) {
		this.referralRate = referralRate;
	}

	public File getOfferImage() {
		return offerImage;
	}

	public void setOfferImage(File offerImage) {
		this.offerImage = offerImage;
	}

	public String getSkuUnit() {
		return skuUnit;
	}

	public void setSkuUnit(String skuUnit) {
		this.skuUnit = skuUnit;
	}

	public String getPriceVatType() {
		return priceVatType;
	}

	public void setPriceVatType(String priceVatType) {
		this.priceVatType = priceVatType;
	}

	public String getPrdImageName() {
		return prdImageName;
	}

	public void setPrdImageName(String prdImageName) {
		this.prdImageName = prdImageName;
	}

	public File getPrdImg() {
		return prdImg;
	}

	public void setPrdImg(File prdImg) {
		this.prdImg = prdImg;
	}

}
