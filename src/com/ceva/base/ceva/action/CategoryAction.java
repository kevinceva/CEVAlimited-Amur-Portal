package com.ceva.base.ceva.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.CallCenterDAO;
import com.ceva.base.common.dao.CategoryManagementDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.ceva.base.common.bean.TwoWaySMSBean;
import com.ceva.util.HttpPostRequestHandler;

public class CategoryAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CategoryAction.class);
	ResourceBundle resourceBundle = ResourceBundle.getBundle("pathinfo_config");
	public String serverIp = resourceBundle.getString("AMUR_WEB_SERVICE");
	
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject catalogRespJSON = null;

	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	protected HttpServletRequest request;
	
	public String merchantId;
	public String storeId;
	public String srchval;
	
	public String catid;
	public String subcatid;
	public String catname;
	public String subcatname;
	public String catdesc;
	public String subcatdesc;
	public String catmkrdt;
	public String submkrdt;
	
	public String type;
	
	private String categoryInfoPage = null;
	private String subcatInfoPage = null;
	
	
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

public String fetchCategories(){
	
	logger.debug("inside [CategoryAction][fetchCategories].. ");
	//JSONObject json = null;
	JSONArray producatJsonArray = null;
	HashMap<String, Object> productDataMap = new HashMap<String, Object>();
	
	try {
		producatJsonArray = new JSONArray();
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
		String webServiceURL = serverIp+"/amurcore/amur/catalog/fetchcatalog";

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

public String fetchCatalog() {
	logger.info("Inside fetchCatalog");
	CategoryManagementDAO categoryManagementDAO = null;
	ArrayList<String> errors = null;
	try {

		requestJSON = new JSONObject();
		requestDTO = new RequestDTO();
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("Request DTO [" + requestDTO + "]");
		categoryManagementDAO = new CategoryManagementDAO();
		responseDTO = categoryManagementDAO.fetchAllCategories(requestDTO);
		logger.debug("Response DTO [" + responseDTO + "]");

		if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get("CATEGORY_JSON");
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
		categoryManagementDAO = null;
	}

	return result;
	
}

public String createCategory() {
	logger.debug("Inside createCategory..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String type = getType();
		
		System.out.println(" type["+type+"]");
		
		
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

public String categoryCreateConfirm() {
	logger.debug("Inside categoryCreateConfirm..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String type = getType();
		String catname=getCatname();
		String catdesc=getCatdesc();
		
		System.out.println("type["+type+"] ");
		
		
		responseJSON.put("catname", catname);
		responseJSON.put("catdesc", catdesc);
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

public String categoryCreateAck() {
	logger.debug("Inside categoryCreateAck..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		session = ServletActionContext.getRequest().getSession();
		
		
		String type = getType();
		String catname=getCatname();
		String catdesc=getCatdesc();
		String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		System.out.println("catname["+catname+"] type["+type+"] catdesc["+catdesc+"] mkrid["+mkrid+"]");
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();		
		logger.debug("fetching catalog");
		String catalogURL = serverIp+"/amurcore/amur/catalog/createcategory/"+URLEncoder.encode(catname, "UTF-8")+"/"+URLEncoder.encode(catdesc, "UTF-8")+"/A/"+mkrid;
		logger.debug("catalogURL  :::: " + catalogURL);
		String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
		JSONObject catalogobj = JSONObject.fromObject(catalogJson);
		logger.debug("catalogobj [" + catalogobj + "] catalogobj to string["+catalogobj.toString()+"]");
		responseJSON = catalogobj;
		if("SUCCESS".equalsIgnoreCase(catalogobj.getString("STATUS")))
		{	
			responseJSON.put("remarks", "SUCCESS");
			result = "success";
		}
		else
		{	
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
		}
		
		responseJSON.put("catname", catname);
		responseJSON.put("catdesc", catdesc);
		responseJSON.put("type", type);
			
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

public String categoryInformation() {
	logger.debug("Inside categoryInformation..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String catid = getCatid();
		String type = getType();
		
		System.out.println("catid["+catid+"] type["+type+"]");
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();		
		logger.debug("fetching catalog");
		String catalogURL = serverIp+"/amurcore/amur/catalog/fetchcatalog";
		logger.debug("catalogURL  :::: " + catalogURL);
		String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
		JSONObject catalogobj = JSONObject.fromObject(catalogJson);
		logger.debug("catalogobj [" + catalogobj + "] catalogobj to string["+catalogobj.toString()+"]");
		catalogRespJSON = catalogobj;
		
		responseJSON.put("catid", catid);
		responseJSON.put("type", type);
		
		result = "success";		
		System.out.println("resultJson ["+responseJSON.toString()+"] catalogRespJSON ["+catalogRespJSON+"]  result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			categoryInfoPage = "categoryModifyInformation";
		} else if (getType().equalsIgnoreCase("View")) {
			categoryInfoPage = "categoryViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			categoryInfoPage = "categoryActivate";
		} else {
			categoryInfoPage = "categoryViewInformation";
		}

		logger.debug(" categoryInfoPage [" + categoryInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}

public String categoryModifyConfirm() {
	logger.debug("Inside categoryModifyConfirm..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String type = getType();
		String catname=getCatname();
		String catdesc=getCatdesc();
		String catid=getCatid();
		
		System.out.println("type["+type+"] catid["+catid+"]");
		
		
		responseJSON.put("catname", catname);
		responseJSON.put("catdesc", catdesc);
		responseJSON.put("catid", catid);
		responseJSON.put("type", type);
		
		result = "success";		
		System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			categoryInfoPage = "categoryModifyConfirm";
		} else if (getType().equalsIgnoreCase("View")) {
			categoryInfoPage = "categoryViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			categoryInfoPage = "categoryActivate";
		} else {
			categoryInfoPage = "categoryViewInformation";
		}

		logger.debug(" categoryInfoPage [" + categoryInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}

public String categoryModifyAck() {
	logger.debug("Inside categoryModifyAck..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		session = ServletActionContext.getRequest().getSession();
		
		
		String type = getType();
		String catname=getCatname();
		String catdesc=getCatdesc();
		String catid=getCatid();
		String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		System.out.println("catid["+catid+"] type["+type+"] catname["+catname+"] catdesc["+catdesc+"]");
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();		
		logger.debug("fetching catalog");
		String catalogURL = serverIp+"/amurcore/amur/catalog/modifycategory/"+catid+"/"+URLEncoder.encode(catname, "UTF-8")+"/"+URLEncoder.encode(catdesc, "UTF-8")+"/"+mkrid;
		logger.debug("catalogURL  :::: " + catalogURL);
		String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
		JSONObject catalogobj = JSONObject.fromObject(catalogJson);
		logger.debug("catalogobj [" + catalogobj + "] catalogobj to string["+catalogobj.toString()+"]");
		responseJSON = catalogobj;
		if("SUCCESS".equalsIgnoreCase(catalogobj.getString("STATUS")))
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
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}


public String subcatInformation() {
	logger.debug("Inside subcatInformation..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String subcatid = getSubcatid();
		String type = getType();
		
		System.out.println("subcatid["+subcatid+"] type["+type+"]");
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();		
		logger.debug("fetching catalog");
		String catalogURL = serverIp+"/amurcore/amur/catalog/viewsubcategory/"+subcatid;
		logger.debug("catalogURL  :::: " + catalogURL);
		String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
		JSONObject catalogobj = JSONObject.fromObject(catalogJson);
		logger.debug("catalogobj [" + catalogobj + "] catalogobj to string["+catalogobj.toString()+"]");
		responseJSON = catalogobj;
		
		responseJSON.put("subcatid", subcatid);
		responseJSON.put("type", type);
		
		result = "success";		
		System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			subcatInfoPage = "subcatModifyInformation";
		} else if (getType().equalsIgnoreCase("View")) {
			subcatInfoPage = "subcatViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			subcatInfoPage = "categoryActivate";
		} else {
			subcatInfoPage = "subcatViewInformation";
		}

		logger.debug(" subcatInfoPage [" + subcatInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}

public String subcatModifyConfirm() {
	logger.debug("Inside subcatModifyConfirm..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String subcatid = getSubcatid();
		String type = getType();
		String subcatname=getSubcatname();
		String subcatdesc=getSubcatdesc();
		String catid=getCatid();
		
		System.out.println("subcatid["+subcatid+"] type["+type+"] catid["+catid+"]");
		
		responseJSON.put("subcatid", subcatid);
		responseJSON.put("subcatname", subcatname);
		responseJSON.put("subcatdesc", subcatdesc);
		responseJSON.put("catid", catid);
		responseJSON.put("type", type);
		
		result = "success";		
		System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			subcatInfoPage = "subcatModifyConfirm";
		} else if (getType().equalsIgnoreCase("View")) {
			subcatInfoPage = "subcatViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			subcatInfoPage = "categoryActivate";
		} else {
			subcatInfoPage = "subcatViewInformation";
		}

		logger.debug(" subcatInfoPage [" + subcatInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}

public String subcatModifyAck() {
	logger.debug("Inside subcatModifyConfirm..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		session = ServletActionContext.getRequest().getSession();
		
		String subcatid = getSubcatid();
		String type = getType();
		String subcatname=getSubcatname();
		String subcatdesc=getSubcatdesc();
		String catid=getCatid();
		String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		System.out.println("subcatid["+subcatid+"] type["+type+"] subcatname["+subcatname+"] catid["+catid+"]");
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();		
		logger.debug("fetching catalog");
		String catalogURL = serverIp+"/amurcore/amur/catalog/modifysubcategory/"+subcatid+"/"+catid+"/"+URLEncoder.encode(subcatname, "UTF-8")+"/"+URLEncoder.encode(subcatdesc, "UTF-8")+"/"+mkrid;
		logger.debug("catalogURL  :::: " + catalogURL);
		String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
		JSONObject catalogobj = JSONObject.fromObject(catalogJson);
		logger.debug("catalogobj [" + catalogobj + "] catalogobj to string["+catalogobj.toString()+"]");
		responseJSON = catalogobj;
	
		responseJSON.put("subcatid", subcatid);
		responseJSON.put("subcatname", subcatname);
		responseJSON.put("subcatdesc", subcatdesc);
		responseJSON.put("type", type);
		
		if("SUCCESS".equalsIgnoreCase(catalogobj.getString("STATUS")))
		{	
			responseJSON.put("remarks", "SUCCESS");
			result = "success";
		}
		else
		{	
			responseJSON.put("remarks", "FAILURE");
			result = "fail";
		}	
		System.out.println("resultJson ["+responseJSON.toString()+"] result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			subcatInfoPage = "subcatModifyConfirm";
		} else if (getType().equalsIgnoreCase("View")) {
			subcatInfoPage = "subcatViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			subcatInfoPage = "categoryActivate";
		} else {
			subcatInfoPage = "subcatViewInformation";
		}

		logger.debug(" subcatInfoPage [" + subcatInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
		errors = null;
	}

	return result;
}

public String createSubCategory() {
	logger.debug("Inside createSubCategory..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String catid = getCatid();
		String type = getType();
		
		System.out.println("catid["+catid+"] type["+type+"]");
	
		responseJSON.put("catid", catid);
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

public String subcatCreateConfirm() {
	logger.debug("Inside subcatCreateConfirm..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		
		String type = getType();
		String subcatname=getSubcatname();
		String subcatdesc=getSubcatdesc();
		String catid=getCatid();
		
		System.out.println(" type["+type+"] catid["+catid+"]");
		
		responseJSON.put("subcatname", subcatname);
		responseJSON.put("subcatdesc", subcatdesc);
		responseJSON.put("catid", catid);
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

public String subcatCreateAck() {
	logger.debug("Inside subcatCreateAck..");
	ArrayList<String> errors = null;
	
	try {
		
		responseJSON = new JSONObject();
		session = ServletActionContext.getRequest().getSession();
		
		String type = getType();
		String subcatname=getSubcatname();
		String subcatdesc=getSubcatdesc();
		String catid=getCatid();
		String mkrid = session.getAttribute(CevaCommonConstants.MAKER_ID).toString();
		
		System.out.println(" type["+type+"] subcatname["+subcatname+"] catid["+catid+"]");
		
		HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();		
		logger.debug("fetching catalog");
		String catalogURL = serverIp+"/amurcore/amur/catalog/createsubcategory/"+catid+"/"+URLEncoder.encode(subcatname, "UTF-8")+"/"+URLEncoder.encode(subcatdesc, "UTF-8")+"/"+mkrid;
		logger.debug("catalogURL  :::: " + catalogURL);
		String catalogJson = httpPostRequestHandler.sendRestPostRequest(catalogURL);
		JSONObject catalogobj = JSONObject.fromObject(catalogJson);
		logger.debug("catalogobj [" + catalogobj + "] catalogobj to string["+catalogobj.toString()+"]");
		responseJSON = catalogobj;
		
		responseJSON.put("subcatid", subcatid);
		responseJSON.put("subcatname", subcatname);
		responseJSON.put("subcatdesc", subcatdesc);
		responseJSON.put("type", type);
		
		if("SUCCESS".equalsIgnoreCase(catalogobj.getString("STATUS")))
		{	
			responseJSON.put("remarks", "SUCCESS");
			result = "success";
		}
		else
		{	
			responseJSON.put("remarks", "FAILED");
			result = "fail";
		}	
		System.out.println("resultJson ["+responseJSON.toString()+"] catalogRespJSON ["+catalogRespJSON+"]  result["+result+"]");	
		
		if (getType().equalsIgnoreCase("Modify")) {
			subcatInfoPage = "subcatModifyConfirm";
		} else if (getType().equalsIgnoreCase("View")) {
			subcatInfoPage = "subcatViewInformation";
		} else if (getType().equalsIgnoreCase("ActiveDeactive")) {
			subcatInfoPage = "categoryActivate";
		} else {
			subcatInfoPage = "subcatViewInformation";
		}

		logger.debug(" subcatInfoPage [" + subcatInfoPage + "]");

		
	} catch (Exception e) {
		result = "fail";
		e.printStackTrace();
		addActionError("Internal error occured.");
	} finally {
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



public String getCatdesc() {
	return catdesc;
}



public void setCatdesc(String catdesc) {
	this.catdesc = catdesc;
}



public String getSubcatdesc() {
	return subcatdesc;
}



public void setSubcatdesc(String subcatdesc) {
	this.subcatdesc = subcatdesc;
}



public String getCatmkrdt() {
	return catmkrdt;
}



public void setCatmkrdt(String catmkrdt) {
	this.catmkrdt = catmkrdt;
}



public String getSubmkrdt() {
	return submkrdt;
}



public void setSubmkrdt(String submkrdt) {
	this.submkrdt = submkrdt;
}



public JSONObject getCatalogRespJSON() {
	return catalogRespJSON;
}



public void setCatalogRespJSON(JSONObject catalogRespJSON) {
	this.catalogRespJSON = catalogRespJSON;
}



public String getType() {
	return type;
}



public void setType(String type) {
	this.type = type;
}


public String getCategoryInfoPage() {
	return categoryInfoPage;
}



public void setCategoryInfoPage(String categoryInfoPage) {
	this.categoryInfoPage = categoryInfoPage;
}



public String getSubcatInfoPage() {
	return subcatInfoPage;
}



public void setSubcatInfoPage(String subcatInfoPage) {
	this.subcatInfoPage = subcatInfoPage;
}



}
