package com.ceva.base.ceva.action;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.CommonWebLoginBean;
import com.ceva.base.common.bean.TravelOffers;
import com.ceva.base.common.dao.SpecialOffersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.HttpPostRequestHandler;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class SpecialOffersAction extends ActionSupport implements 
ServletRequestAware, ServletContextAware, ModelDriven<TravelOffers> {
	
	Logger logger = Logger.getLogger(SpecialOffersAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	private HttpSession session = null;
	
	private HttpServletRequest httpRequest = null;
	
	public String prdid;
	public String type;
	private String userid;
	private String productInfoPage = null;
	
	public String itinerary;
	public String info;
	public String term;
	
	@Autowired
	private TravelOffers travelsBean = null;
	
	ServletContext context;

	
	@Override
	public String execute() throws Exception {
		return super.execute();
	}
	
	/*public String fetchSpecialOffers(){
		
		System.out.println("Inside Special Offers Action");
		
		logger.debug("inside [SpecialOffersAction][fetchSpecialOffers].. ");
		SpecialOffersDAO offersDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			offersDAO = new SpecialOffersDAO();
			responseDTO = offersDAO.fetchSpecialOffersDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("OFFER_LIST");
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
			logger.debug("Exception in [SpecialOffersAction][fetchSpecialOffers] [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			offersDAO = null;
		}
		
		return "success";
	}*/
	
	public String fetchSpecialOffers(){
		
		logger.debug("inside [SpecialOffersAction][fetchSpecialOffers].. ");
		
		JSONArray offersJsonArray = null;
		HashMap<String, Object> offerDataMap = new HashMap<String, Object>();
		try {
			offersJsonArray = new JSONArray();
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/travel/fetchtraveloffers";

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string["+obj.toString()+"]");
			responseJSON = obj;
			result = obj.getString("STATUS");		
			System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
			
		} catch (Exception e) {
			result = "fail";
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
	
	public String callServlet(){
		
		logger.debug("inside [SpecialOffersAction][callServlet].. ");
		
		
		try {
						
			result = "success";
			logger.info("[SpecialOffersAction][callServlet result..:"+result);
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [SpecialOffersAction][callServlet] [" + e.getMessage()
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

	
	public String offerInformation(){
		logger.debug("Inside OfferInformation");
		ArrayList<String> errors = null;
		try {
					
			String offerid = getPrdid();
			String type = getType();
			
			System.out.println("prdid ["+offerid+"] type["+type+"]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/travel/viewtraveloffer/"+offerid;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string["+obj.toString()+"]");
			responseJSON = obj;
			responseJSON.put("type", type);
			result = "success";		
			System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
			
			if (getType().equalsIgnoreCase("Modify")) {
				productInfoPage = "offerModifyInformation";
			} else if (getType().equalsIgnoreCase("View")) {
				productInfoPage = "offerViewInformation";
			} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
				productInfoPage = "productActivate";
			} else {
				productInfoPage = "productViewInformation";
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
	
	public String addOffer(){
		
		logger.debug("==================Inside OfferInformation");
		ArrayList<String> errors = null;
		
		session = ServletActionContext.getRequest().getSession();
		String userid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();	
		
		
		JSONObject obj = constructToResponseJson(httpRequest);

		//String dealname = travelsBean.getDealName(); //not working
		String dealname = obj.getString("confirm_dealname"); //working
		String operatorname = obj.getString("confirm_operator");
		String startdate = obj.getString("confirm_startdate");
		String enddate = obj.getString("confirm_enddate");
		String country = obj.getString("confirm_country");
		String category = obj.getString("confirm_category");
		String price = obj.getString("confirm_price");
		String availablebookings = obj.getString("confirm_bookings");
		
		//String travelitinerary = obj.getString("confirm_travelitinerary");
		String importantinfo = obj.getString("confirm_info");
		String terms = obj.getString("confirm_terms");		
		
		
		try {
			
			String itinerary = URLEncoder.encode(obj.getString("confirm_travelitinerary"), "UTF-8");
			String info = URLEncoder.encode(importantinfo, "UTF-8");
			String term = URLEncoder.encode(terms, "UTF-8");
			
			
			System.out.println("Travel itineary"+itinerary);
			System.out.println("Terms"+term);
			System.out.println("Travel info"+info);
			
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/travel/createtraveloffer/"
			+dealname+"/"+country+"/"+startdate+"/"+enddate+"/"+operatorname+"/"+category+"/"+price+"/"+availablebookings+"/"
			+itinerary+"/"+info+"/"+term+"/"+userid;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj1 = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj1 + "] obj to string["+obj1.toString()+"]");
			responseJSON = obj1;
			responseJSON.put("type", type);
			result = "success";		
			System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
			
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}	
		
		return result;
	}
	
	public String modOffer(){
		
		logger.debug("==================Inside Mod Information");
		ArrayList<String> errors = null;
		
		session = ServletActionContext.getRequest().getSession();
		String userid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		JSONObject obj = constructToResponseJson(httpRequest);

		//String dealname = travelsBean.getDealName(); //not working
		String dealname = obj.getString("mod_dealname"); //working
		String operatorname = obj.getString("mod_operatorname");
		String startdate = obj.getString("mod_startdate");
		String enddate = obj.getString("mod_enddate");
		String country = obj.getString("mod_country");
		String category = obj.getString("mod_category");
		String price = obj.getString("mod_price");
		String availablebookings = obj.getString("mod_availablebookings");
		String travelitinerary = obj.getString("mod_travelitinerary");
		String importantinfo = obj.getString("mod_importantinformation");
		String terms = obj.getString("mod_terms");	
		String offerId = obj.getString("offerId");	
		
		try {
			/*http://104.42.234.123:1234/amurcore/amur/travel/modifytraveloffer/{travelOfferId}/{dealName}/{dealCountry}/{startDate}/{endDate}/{operatorName}/{offerType}/{amount}/{maxBookings}/{travelItinerary}/{importantInfo}/{terms}/{createdBy}*/
			
			//String offerid = getPrdid();
			String type = getType();
			
			System.out.println("prdid ["+offerId+"] type["+type+"]");
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
			String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/travel/modifytraveloffer/"+offerId+"/"
			+dealname+"/"+country+"/"+startdate+"/"+enddate+"/"+operatorname+"/"+category+"/"+price+"/"+availablebookings+"/"
			+travelitinerary+"/"+importantinfo+"/"+terms+"/"+userid;

			logger.debug("Web Service URL  :::: " + webServiceURL);
			String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
			JSONObject obj1 = JSONObject.fromObject(json1);
			logger.debug("End to Sent Mobile Otp >> [" + obj1 + "] obj to string["+obj1.toString()+"]");
			responseJSON = obj1;
			responseJSON.put("type", type);
			result = "success";		
			System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
			
		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			errors = null;
		}
		
		return result;
	}
	
	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {
		Enumeration enumParams = null;
		JSONObject jsonObject = null;
		logger.debug("Client Information - Inside ConstructToResponseJson...");
		try {
			enumParams = httpRequest.getParameterNames();

			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" Client Information - Exception in ConstructToResponseJson ["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
		}
		logger.debug(" jsonObject[" + jsonObject + "]");

		return jsonObject;
	}
	
	
	//Bean methods
	public TravelOffers getTravelOffersBean() {
		return travelsBean;
	}

	public void setTravelOffersBean(TravelOffers commonBean) {
		this.travelsBean = commonBean;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	
	public String getProductInfoPage() {
		return productInfoPage;
	}

	public void setProductInfoPage(String productInfoPage) {
		this.productInfoPage = productInfoPage;
	}


	@Override
	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
		this.context = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.httpRequest = arg0;
	}

	@Override
	public TravelOffers getModel() {
		// TODO Auto-generated method stub
		return travelsBean;
	}

	public String getItinerary() {
		return itinerary;
	}

	public void setItinerary(String itinerary) {
		this.itinerary = itinerary;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	/*public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}*/

	

}
