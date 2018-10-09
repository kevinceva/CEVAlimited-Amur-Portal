package com.ceva.base.ceva.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CallCenterDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;
import com.ceva.util.HttpPostRequestHandler;

public class ManufacturerAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(ManufacturerAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	public String merchantId;
	public String storeId;
	public String srchval;
	public String manfid;
	public String manfName;
	public String manfCont;
	public String manfSecCon;
	
	
	
	public String type;
	
	private String manufacturerInfoPage = null;
	
	private HttpServletRequest httpRequest = null;
	
	
	
	
	
/*	public String getMerchantDashBoard(){
		return "success";
	}*/
	
	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		result = "success";
		return result;
	}
	

	
public String callServlet(){
		
		logger.debug("inside [CustomerAction][callServlet].. ");
		
		
		try {
						
			result = "success";
			logger.info("[CustomerAction][callServlet result..:"+result);
			
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [CustomerAction][callServlet] [" + e.getMessage()
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

public String fetchManufacturers(){
	
	logger.debug("inside [ManufacturerAction][fetchManufacturers].. ");
	//JSONObject json = null;
	
	try {
		//http://104.42.234.123:5555/amurcore/amur/catalog/fetchproducts;
			HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
		String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/catalog/fetchmanufacturers";

		logger.debug("Web Service URL  :::: " + webServiceURL);
		String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
		JSONObject obj = JSONObject.fromObject(json1);
		logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string["+obj.toString()+"]");
		responseJSON = obj;
		result = "success";		
		System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");		
	} catch (Exception e) {
		result = "fail";
		/*logger.debug("Exception in [ProductAction][fetchProducts] [" + e.getMessage()
				+ "]");*/
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

public String manufacturerInformation() {
	logger.debug("Inside manufacturerInformation..");
	ArrayList<String> errors = null;
	try {
		String manfid = getManfid();
		String type = getType();
		
		session = ServletActionContext.getRequest().getSession();
		
		System.out.println("manfid ["+manfid+"] type["+type+"]");
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
		String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/catalog/viewmanufacturer/"+manfid;

		/*session = ServletActionContext.getRequest().getSession();
		requestJSON.put("TYPE", getType());
		requestJSON.put("MAKER_ID",
				session.getAttribute(CevaCommonConstants.MAKER_ID)
						.toString());*/

		logger.debug("Web Service URL  :::: " + webServiceURL);
		String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
		JSONObject obj = JSONObject.fromObject(json1);
		logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string["+obj.toString()+"]");
		responseJSON = obj;
		responseJSON.put("type", type);
		responseJSON.put("manfid", manfid);
		responseJSON.put("MAKER_ID", session.getAttribute(CevaCommonConstants.MAKER_ID).toString());
		result = "success";		
		System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			manufacturerInfoPage = "manufacturerModifyInformation";
		} else if (getType().equalsIgnoreCase("View")) {
			manufacturerInfoPage = "manufacturerViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			manufacturerInfoPage = "manufacturerActivate";
		} else {
			manufacturerInfoPage = "manufacturerViewInformation";
		}

		logger.debug(" manufacturerInfoPage [" + manufacturerInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}

public String manufacturerModifyConfirm() {
	logger.debug("Inside manufacturerModifyConfirm...");
	try
	{
		 responseJSON = new JSONObject();
		String type = getType();
		String manfid = getManfid();
		String manfName = getManfName();
		String manfCont = getManfCont();
		String manfSecCon = getManfSecCon();
		
		logger.debug("Type[" + type + "]");
		logger.debug("manfName[" + manfName + "] manfCont ["+manfCont+"] manfSecCon ["+manfSecCon+"]");
	
		responseJSON.put("type", type);
		responseJSON.put("manfid", manfid);
		responseJSON.put("manfName", manfName);
		responseJSON.put("manfCont", manfCont);
		responseJSON.put("manfSecCon",manfSecCon);
		
		logger.debug("responseJSON[" + responseJSON + "]");
		if (getType().equalsIgnoreCase("Modify")) {
			manufacturerInfoPage = "manufacturerModifyConfirm";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			manufacturerInfoPage = "manufacturerActivate";
		} 
		result = "success";
	}catch (Exception e)
	{
		result = "fail";
		e.printStackTrace();
	}
	return result;
}

public String manufacturerModifyAck() {

	logger.debug("Inside clientModifyAck Type[" + getType() + "]");

	ArrayList<String> errors = null;
	try {
		session = ServletActionContext.getRequest().getSession();
		
		String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		 responseJSON = new JSONObject();
		 
		 String type = getType();
			String manfid = getManfid();
			String manfName = getManfName();
			String manfCont = getManfCont();
			String manfSecCon = getManfSecCon();
		 
		 logger.debug("Type[" + getType() + "]");
			logger.debug("manfName[" + getManfName() + "] manfCont ["+getManfCont()+"] manfSecCon ["+getManfSecCon()+"]");
		
			responseJSON.put("type", type);
			responseJSON.put("manfid", manfid);
			responseJSON.put("manfName", manfName);
			responseJSON.put("manfCont", manfCont);
			responseJSON.put("manfSecCon",manfSecCon);
			
			logger.debug("responseJSON[" + responseJSON + "]");
		
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
		String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/catalog/modifymanufacturer/"+manfid+"/"+ URLEncoder.encode(manfName, "UTF-8")+"/"+manfCont+"/"+manfSecCon+"/"+mkrid;
		logger.debug("Web Service URL  :::: " + webServiceURL);
		String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
		JSONObject obj = JSONObject.fromObject(json1);
		logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string["+obj.toString()+"]");
		if("SUCCESS".equalsIgnoreCase(obj.getString("STATUS")))
		{	
			responseJSON.put("remarks", "SUCCESS");
			result = "success";
		}
		else
		{	
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
		}

	} catch (Exception e) {
		result = "fail";
		logger.debug("Exception in clientModifyAck [" + e.getMessage() + "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;
		
		errors = null;

	}
	return result;
}


public String manufacturerCreateConfirm() {
	logger.debug("Inside manufacturerCreateConfirm...");
	try
	{
		 responseJSON = new JSONObject();
		String manfName = getManfName();
		String manfCont = getManfCont();
		String manfSecCon = getManfSecCon();
		
		logger.debug("manfName[" + manfName + "] manfCont ["+manfCont+"] manfSecCon ["+manfSecCon+"]");
	
		responseJSON.put("manfName", manfName);
		responseJSON.put("manfCont", manfCont);
		responseJSON.put("manfSecCon",manfSecCon);		
		
		result = "success";
	}catch (Exception e)
	{
		result = "fail";
		e.printStackTrace();
	}
	return result;
}

public String manufacturerCreateAck() {

	logger.debug("Inside manufacturerCreateAck Type[" + getType() + "]");

	ArrayList<String> errors = null;
	try {
		session = ServletActionContext.getRequest().getSession();
		
		String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		 responseJSON = new JSONObject();

			String manfName = getManfName();
			String manfCont = getManfCont();
			String manfSecCon = getManfSecCon();
		 

			logger.debug("manfName[" + getManfName() + "] manfCont ["+getManfCont()+"] manfSecCon ["+getManfSecCon()+"]");
		

			responseJSON.put("manfName", manfName);
			responseJSON.put("manfCont", manfCont);
			responseJSON.put("manfSecCon",manfSecCon);
			
			logger.debug("responseJSON[" + responseJSON + "]");
		
			//http://104.42.234.123:5555/amurcore/amur/catalog/createmanufacturer/{manufacturerName}/{manufacturerContact}/{manufacturerSecondContact}/{createdBy}
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
		String webServiceURL = "http://104.42.234.123:5555/amurcore/amur/catalog/createmanufacturer/"+ URLEncoder.encode(manfName, "UTF-8")+"/"+manfCont+"/"+manfSecCon+"/"+mkrid;
		logger.debug("Web Service URL  :::: " + webServiceURL);
		String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
		JSONObject obj = JSONObject.fromObject(json1);
		logger.debug("End to Sent Mobile Otp >> [" + obj + "] obj to string["+obj.toString()+"]");
		
		if("SUCCESS".equalsIgnoreCase(obj.getString("STATUS")))
		{	
			responseJSON.put("remarks", "SUCCESS");
			result = "success";
		}
		else
		{	
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
		}

	} catch (Exception e) {
		result = "fail";
		logger.debug("Exception in clientModifyAck [" + e.getMessage() + "]");
		addActionError("Internal error occured.");
	} finally {
		requestDTO = null;
		responseDTO = null;
		requestJSON = null;
		
		errors = null;

	}
	return result;
}

public JSONObject getResponseJSON() {
	return responseJSON;
}



public void setResponseJSON(JSONObject responseJSON) {
	this.responseJSON = responseJSON;
}



public String getManfid() {
	return manfid;
}



public void setManfid(String manfid) {
	this.manfid = manfid;
}



public String getType() {
	return type;
}



public void setType(String type) {
	this.type = type;
}



public HttpServletRequest getHttpRequest() {
	return httpRequest;
}



public void setHttpRequest(HttpServletRequest httpRequest) {
	this.httpRequest = httpRequest;
}



public String getManufacturerInfoPage() {
	return manufacturerInfoPage;
}



public void setManufacturerInfoPage(String manufacturerInfoPage) {
	this.manufacturerInfoPage = manufacturerInfoPage;
}



public String getManfName() {
	return manfName;
}



public void setManfName(String manfName) {
	this.manfName = manfName;
}



public String getManfCont() {
	return manfCont;
}



public void setManfCont(String manfCont) {
	this.manfCont = manfCont;
}



public String getManfSecCon() {
	return manfSecCon;
}



public void setManfSecCon(String manfSecCon) {
	this.manfSecCon = manfSecCon;
}






}
