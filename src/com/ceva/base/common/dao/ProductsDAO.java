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

public class ProductsDAO {

	private Logger logger = Logger.getLogger(LifestyleDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	String prdId = null;
	String imgUrl = null;

	public ResponseDTO saveDemandProducts(RequestDTO requestDTO) throws SQLException {
		Connection connection = null;
		this.logger.debug("Inside [ProductsDAO][saveDemandProducts].. ");

		HashMap<String, Object> demandProductMap = null;
		PreparedStatement demandProductPstmt = null;
		JSONObject Resp_Message = null;

		try {

			requestJSON = requestDTO.getRequestJSON();

			String prdId = "PRD" + generateUniqueId();
			String catId = requestJSON.getString("catId");
			String subcatId = requestJSON.getString("subcatId");
			String manfname = requestJSON.getString("manfname");

			String prdSupplierId = requestJSON.getString("prdSupplierId");
			String amurDemandName = requestJSON.getString("amurDemandName");
			String productDemandDesc = requestJSON.getString("productDemandDesc");
			String skuSize = requestJSON.getString("skuSize");
			String skuUnit = requestJSON.getString("skuUnit");
			String sellPriceNoVat = requestJSON.getString("sellPriceNoVat");

			// String markupType = requestJSON.getString("markupType");
			String markupRate = requestJSON.getString("markupRate");
			String markupAmountNoVat = requestJSON.getString("markupAmountNoVat");
			// String markupVatType = requestJSON.getString("markupVatType");
			// String markupAmountVat = requestJSON.getString("markupAmountVat");
			// String markupAmtWithVat = requestJSON.getString("markupAmtWithVat");

			String referralRate = requestJSON.getString("referralRate");
			String referralAmtNoVat = requestJSON.getString("referralAmtNoVat");
			// String referralVatType = requestJSON.getString("referralVatType");
			// String referralVatAmt = requestJSON.getString("referralVatAmt");
			// String referralAmtVat = requestJSON.getString("referralAmtVat");

			String healthPremiumRate = requestJSON.getString("healthPremiumRate");
			String healthPremiumNoVat = requestJSON.getString("healthPremiumNoVat");
			// String healthPremiumVatType = requestJSON.getString("healthPremiumVatType");
			// String healthPremiumVatAmt = requestJSON.getString("healthPremiumVatAmt");
			// String healthPremiumVat = requestJSON.getString("healthPremiumVat");
			String sellPrice = requestJSON.getString("sellPriceVat");

			String productImage = requestJSON.getString("productImage");
			String productImageUrl = "Amur_Images/products/" + productImage.replaceAll("\\.tmp$", ".jpg");

			String prdQry = "INSERT INTO PRODUCT_MASTER(PRD_ID, CATEGORY_ID, SUB_CATEGORY_ID, MANUFACTURER_ID, PRD_SUPPLY_FK, PRD_NAME, PRD_DESC, SKU1, SKU2,"
					+ " PRICE_EXCL_VAT, MARKUP_RATE, MARKUP_VALUE_EXCL_VAT, REFERRAL_EARN_RATE, REFERRAL_EARN_EXCL_VAT, "
					+ "HEALTH_PREMIUM_RATE, HEALTH_PREMIUM_EXCL_VAT, CREATED_DATE, STATUS, PRICE, DISP_ORDER, PRD_IMG1) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			demandProductMap = new HashMap();
			Resp_Message = new JSONObject();

			demandProductPstmt = connection.prepareStatement(prdQry);

			demandProductPstmt.setString(1, prdId);
			demandProductPstmt.setString(2, catId);
			demandProductPstmt.setString(3, subcatId);
			demandProductPstmt.setString(4, manfname);
			demandProductPstmt.setString(5, prdSupplierId);
			demandProductPstmt.setString(6, amurDemandName);
			demandProductPstmt.setString(7, productDemandDesc);
			demandProductPstmt.setString(8, skuSize);
			demandProductPstmt.setString(9, skuUnit);
			demandProductPstmt.setString(10, sellPriceNoVat);

			demandProductPstmt.setString(11, markupRate);
			demandProductPstmt.setString(12, markupAmountNoVat);

			demandProductPstmt.setString(13, referralRate);
			demandProductPstmt.setString(14, referralAmtNoVat);

			demandProductPstmt.setString(15, healthPremiumRate);
			demandProductPstmt.setString(16, healthPremiumNoVat);
			demandProductPstmt.setTimestamp(17, getCurrentTimeStamp());
			demandProductPstmt.setString(18, "A");
			demandProductPstmt.setString(19, sellPrice);
			demandProductPstmt.setString(20, "1");
			demandProductPstmt.setString(21, productImageUrl);

			demandProductPstmt.executeQuery();
			Resp_Message.put("Response_Message", "Product added saved.");

			demandProductMap.put("Response_Message", Resp_Message);
			this.logger.info("EntityMap [" + demandProductMap + "]");
			this.responseDTO.setData(demandProductMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in saveDemandProducts ProductsDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got SQL Exception in saveDemandProducts ProductsDAO [" + e.getMessage() + "]");

		} catch (Exception e) {
			this.logger.debug("Got Exception in saveDemandProducts ProductsDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in saveDemandProducts ProductsDAO [" + e.getMessage() + "]");
		} finally {
			DBUtils.closePreparedStatement(demandProductPstmt);
			DBUtils.closeConnection(connection);
			demandProductMap = null;
			Resp_Message = null;
		}

		return this.responseDTO;
	}

	public ResponseDTO saveSupplyProducts(RequestDTO requestDTO) throws SQLException {
		Connection connection = null;
		this.logger.debug("Inside [ProductsDAO][saveSupplyProducts].. ");

		HashMap<String, Object> supplyProductMap = null;
		PreparedStatement supplyProductPstmt = null;
		JSONObject Resp_Message = null;

		try {

			requestJSON = requestDTO.getRequestJSON();

			String prdId = "PRD" + generateUniqueId();
			String catId = requestJSON.getString("catId");
			String subcatId = requestJSON.getString("subcatId");
			String manfname = requestJSON.getString("manfname");
			String productSupplierName = requestJSON.getString("productSupplierName");
			String productQuantity = requestJSON.getString("productQuantity");
			String reorderLvl = requestJSON.getString("reorderLvl");
			String buyPriceNoVat = requestJSON.getString("buyPriceNoVat");
			String buyPriceVat = requestJSON.getString("buyPriceVat");
			String vatRate = requestJSON.getString("vatRate");
			// String mkrid = requestJSON.getString("mkrid");

			String prdQry = "INSERT INTO PRODUCT_MASTER_SUPPLY(PRD_ID, PRD_NAME, CATEGORY_ID, SUB_CATEGORY_ID, MANUFACTURER_ID, PRICE, QUANTITY, REORDER_LEVEL, CREATED_DATE, VAT_TYPE, PRICE_EXCL_VAT) values (?,?,?,?,?,?,?,?,?,?,?)";

			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			supplyProductMap = new HashMap();
			Resp_Message = new JSONObject();

			supplyProductPstmt = connection.prepareStatement(prdQry);

			supplyProductPstmt.setString(1, prdId);
			supplyProductPstmt.setString(2, productSupplierName);
			supplyProductPstmt.setString(3, catId);
			supplyProductPstmt.setString(4, subcatId);
			supplyProductPstmt.setString(5, manfname);
			supplyProductPstmt.setString(6, buyPriceVat);
			supplyProductPstmt.setString(7, productQuantity);
			supplyProductPstmt.setString(8, reorderLvl);
			supplyProductPstmt.setTimestamp(9, getCurrentTimeStamp());
			supplyProductPstmt.setString(10, vatRate);
			supplyProductPstmt.setString(11, buyPriceNoVat);

			supplyProductPstmt.executeQuery();
			Resp_Message.put("Response_Message", "Product added successfully.");

			supplyProductMap.put("Response_Message", Resp_Message);
			this.logger.info("EntityMap [" + supplyProductMap + "]");
			this.responseDTO.setData(supplyProductMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in saveSupplyProducts ProductsDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got SQL Exception in saveSupplyProducts ProductsDAO [" + e.getMessage() + "]");

		} catch (Exception e) {
			this.logger.debug("Got Exception in saveSupplyProducts ProductsDAO [" + e.getMessage() + "]");
			e.printStackTrace();
			Resp_Message.put("Response_Message",
					"Got Exception in saveSupplyProducts ProductsDAO [" + e.getMessage() + "]");
		} finally {
			DBUtils.closePreparedStatement(supplyProductPstmt);
			DBUtils.closeConnection(connection);
			supplyProductMap = null;
			Resp_Message = null;
		}

		return this.responseDTO;
	}

	public ResponseDTO fetchDemandProducts(RequestDTO requestDTO) throws SQLException {

		Connection connection = null;
		this.logger.debug("Inside [ProductsDAO][fetchDemandProducts].. ");

		HashMap<String, Object> productsMap = null;
		JSONObject resultJson = null;
		JSONArray productsArray = null;
		PreparedStatement productsPstmt = null;
		ResultSet productsRS = null;
		JSONObject json = null;

		String productsQRY = "SELECT PRD_ID, PRD_NAME, PRD_DESC, PRICE FROM PRODUCT_MASTER";
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			productsMap = new HashMap();
			resultJson = new JSONObject();
			productsArray = new JSONArray();

			productsPstmt = connection.prepareStatement(productsQRY);
			productsRS = productsPstmt.executeQuery();

			json = new JSONObject();
			while (productsRS.next()) {
				json.put("PRD_ID", productsRS.getString(1));
				json.put("PRD_NAME", productsRS.getString(2));
				json.put("PRD_DESC", productsRS.getString(3));
				json.put("PRD_PRICE", productsRS.getString(4));
				productsArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(productsRS);
			DBUtils.closePreparedStatement(productsPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("DEMAND_PRODUCTS", productsArray);
			productsMap.put("DEMAND_PRODUCTS", resultJson);
			this.logger.info("EntityMap [" + productsMap + "]");
			this.responseDTO.setData(productsMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in ProductsDAO fetchDemandProducts [" + e.getMessage() + "]");
			e.printStackTrace();

		} catch (Exception e) {
			this.logger.debug("Got Exception in ProductsDAO fetchDemandProducts [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(productsRS);
			DBUtils.closePreparedStatement(productsPstmt);
			DBUtils.closeConnection(connection);

			productsMap = null;
			resultJson = null;
			productsArray = null;
		}

		return this.responseDTO;
	}
	
	public ResponseDTO fetchSupplyProducts(RequestDTO requestDTO) throws SQLException {

		Connection connection = null;
		this.logger.debug("Inside [ProductsDAO][fetchSupplyProducts].. ");

		HashMap<String, Object> productsMap = null;
		JSONObject resultJson = null;
		JSONArray productsArray = null;
		PreparedStatement productsPstmt = null;
		ResultSet productsRS = null;
		JSONObject json = null;

		String productsQRY = "SELECT PRD_ID, PRD_NAME, PRD_DESC, PRICE FROM PRODUCT_MASTER";
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			productsMap = new HashMap();
			resultJson = new JSONObject();
			productsArray = new JSONArray();

			productsPstmt = connection.prepareStatement(productsQRY);
			productsRS = productsPstmt.executeQuery();

			json = new JSONObject();
			while (productsRS.next()) {
				json.put("PRD_ID", productsRS.getString(1));
				json.put("PRD_NAME", productsRS.getString(2));
				json.put("PRD_DESC", productsRS.getString(3));
				json.put("PRD_PRICE", productsRS.getString(4));
				productsArray.add(json);
				json.clear();
			}
			DBUtils.closeResultSet(productsRS);
			DBUtils.closePreparedStatement(productsPstmt);
			DBUtils.closeConnection(connection);
			resultJson.put("SUPPLY_PRODUCTS", productsArray);
			productsMap.put("SUPPLY_PRODUCTS", resultJson);
			this.logger.info("EntityMap [" + productsMap + "]");
			this.responseDTO.setData(productsMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in ProductsDAO fetchSupplyProducts [" + e.getMessage() + "]");
			e.printStackTrace();

		} catch (Exception e) {
			this.logger.debug("Got Exception in ProductsDAO fetchSupplyProducts [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(productsRS);
			DBUtils.closePreparedStatement(productsPstmt);
			DBUtils.closeConnection(connection);

			productsMap = null;
			resultJson = null;
			productsArray = null;
		}

		return this.responseDTO;
	}

	public ResponseDTO fetchSupplyCatalog(RequestDTO requestDTO) throws SQLException {
		Connection connection = null;
		this.logger.debug("Inside [ProductsDAO][fetchSupplyCatalog].. ");

		HashMap<String, Object> productsMap = null;
		PreparedStatement productsPstmt = null;
		ResultSet productsRS = null;
		String subcatId = null;

		JSONArray productsArray = null; // Hold products from specific subcatId
		JSONArray subcatArray = null; // Hold subcatId

		JSONObject resultJson = null;
		JSONObject subcatObj = null;
		JSONObject json = null;
		JSONArray prodArray = null;

		String subcatQRY = "SELECT PMS.SUB_CATEGORY_ID FROM PRODUCT_MASTER_SUPPLY PMS, SUB_CATEGORY_MASTER SCM WHERE PMS.SUB_CATEGORY_ID = SCM.SUB_CATEGORY_ID";
		try {
			this.responseDTO = new ResponseDTO();

			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");

			productsMap = new HashMap();
			resultJson = new JSONObject();
			json = new JSONObject();

			productsArray = new JSONArray();
			subcatArray = new JSONArray();

			productsPstmt = connection.prepareStatement(subcatQRY);
			productsRS = productsPstmt.executeQuery();

			subcatObj = new JSONObject();
			prodArray = new JSONArray();

			while (productsRS.next()) {
				subcatArray.add(productsRS.getString(1));
				System.out.println("Sub category array :: " + subcatArray.toString());
			}

			for (int i = 0; i < subcatArray.size(); i++) {

				subcatId = subcatArray.getString(i);
				subcatObj.put("SUB_CATEGORY_ID", subcatId);

				String prdQRY = "SELECT PRD_ID, PRD_NAME, PRICE_EXCL_VAT FROM PRODUCT_MASTER_SUPPLY WHERE SUB_CATEGORY_ID = ?";
				productsPstmt = connection.prepareStatement(prdQRY);
				productsPstmt.setString(1, subcatId);
				productsRS = productsPstmt.executeQuery();

				while (productsRS.next()) {
					resultJson.put("PRD_ID", productsRS.getString(1));
					resultJson.put("PRD_NAME", productsRS.getString(2));
					resultJson.put("PRD_PRICE", productsRS.getString(3));
					productsArray.add(resultJson);
					resultJson.clear();
				}

				subcatId = null;

				subcatObj.put("SUB_CAT_PRODUCTS", productsArray);
				prodArray.add(subcatObj);
				productsArray.clear();
			}

			System.out.println("All products is :: " + prodArray.toString());

			DBUtils.closeResultSet(productsRS);
			DBUtils.closePreparedStatement(productsPstmt);
			DBUtils.closeConnection(connection);
			json.put("CAT_PRODUCTS", prodArray);
			productsMap.put("CAT_PRODUCTS", json);
			this.logger.info("EntityMap [" + productsMap + "]");
			this.responseDTO.setData(productsMap);

		} catch (SQLException e) {
			this.logger.debug("Got SQL Exception in LifetsyleDAO fetchMerchants [" + e.getMessage() + "]");
			e.printStackTrace();

		} catch (Exception e) {
			this.logger.debug("Got Exception in LifetsyleDAO fetchMerchants [" + e.getMessage() + "]");
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(productsRS);
			DBUtils.closePreparedStatement(productsPstmt);
			DBUtils.closeConnection(connection);

			productsMap = null;
			subcatObj = null;
			productsArray = null;
			subcatArray = null;
		}

		return this.responseDTO;
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

}
