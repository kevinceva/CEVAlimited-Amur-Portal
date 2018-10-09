package com.ceva.base.common.action;

import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.ProductAjaxDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
public class ProductAjaxAction extends ActionSupport{


	Logger logger=Logger.getLogger(ProductAjaxAction.class);
	JSONObject requestJSON=null;
	JSONObject responseJSON=null;
	RequestDTO requestDTO=null;
	ResponseDTO responseDTO=null;

	private String result;
	private String serviceCode1;
	private String serviceName;
	private String feecode;

	public String serviceRegistration() throws Exception {
    	logger.debug("inside [ProductAjaxAction][serviceRegistration]serviceCode1::["+serviceCode1+"]");
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		requestDTO=new RequestDTO();
		responseDTO= new ResponseDTO();
		HttpSession session = ServletActionContext.getRequest().getSession();
		requestJSON.put("serviceCode1", serviceCode1);
		requestJSON.put("serviceName", serviceName);

		requestJSON.put("MAKER_ID",	session.getAttribute(CevaCommonConstants.MAKER_ID));
		logger.debug("[ProductAjaxAction][serviceRegistration][responseJSON:::::"+responseJSON+"]");
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("[ProductAjaxAction][serviceRegistration][requestDTO:::::"+requestDTO+"]");
		ProductAjaxDAO prdDAO=new ProductAjaxDAO();
		responseDTO=prdDAO.InsertNewServiceDetails(requestDTO);
		logger.debug("[ProductAjaxAction][serviceRegistration][responseDTO:::::"+responseDTO+"]");
		if (responseDTO != null && responseDTO.getErrors().size()==0) {
			ArrayList<String> messages = (ArrayList<String>) responseDTO.getMessages();
			logger.debug("[ProductAjaxAction][InsertServiceDetails][messages==>"+ messages + "]");
			for (int i = 0; i < messages.size(); i++) {
				addActionMessage(messages.get(i));
			}
			logger.debug("[ProductAjaxAction][InsertServiceDetails][responseJSON:::::"+responseJSON+"]");
		}else{
			ArrayList<String> errors=(ArrayList<String>) responseDTO.getErrors();
			for(int i=0;i<errors.size();i++){
				addActionError(errors.get(i));
			}
		}
		       return ActionSupport.SUCCESS;
    }

	public String GetFeeDetails() throws Exception {
    	logger.debug("inside [ProductAjaxAction][GetFeeDetails]feecode::["+feecode+"]");
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		requestDTO=new RequestDTO();
		responseDTO= new ResponseDTO();
		HttpSession session = ServletActionContext.getRequest().getSession();
		requestJSON.put("feecode", feecode);
		logger.debug("[ProductAjaxAction][GetFeeDetails][responseJSON:::::"+responseJSON+"]");
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("[ProductAjaxAction][GetFeeDetails][requestDTO:::::"+requestDTO+"]");

		ProductAjaxDAO prdDAO=new ProductAjaxDAO();
		responseDTO=prdDAO.getFeecodeDetails(requestDTO);
		logger.debug("[ProductAjaxAction][GetFeeDetails][responseDTO:::::"+responseDTO+"]");
		if (responseDTO != null && responseDTO.getErrors().size()==0) {
			ArrayList<String> messages = (ArrayList<String>) responseDTO.getMessages();
			logger.debug("[ProductAjaxAction][GetFeeDetails][messages==>"+ messages + "]");
			responseJSON = (JSONObject) responseDTO.getData().get("Fee_Details");
			String feecode=responseJSON.getString("feeCode");
			logger.debug("[ProductAjaxAction][GetFeeDetails][feecode:::::"+feecode+"]");

				session.setAttribute("feecode",feecode);

			logger.debug("SESSION VALUE : "+session.getAttribute("feecode"));
			logger.debug("[ProductAjaxAction][GetFeeDetails][responseJSON:::::"+responseJSON+"]");
			for (int i = 0; i < messages.size(); i++) {
				addActionMessage(messages.get(i));
			}

		}else{
			ArrayList<String> errors=(ArrayList<String>) responseDTO.getErrors();
			for(int i=0;i<errors.size();i++){
				addActionError(errors.get(i));
			}
		}
		       return ActionSupport.SUCCESS;
    }


	public JSONObject getRequestJSON() {
		return requestJSON;
	}
	public void setRequestJSON(JSONObject requestJSON) {
		this.requestJSON = requestJSON;
	}
	public JSONObject getResponseJSON() {
		return responseJSON;
	}
	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	public RequestDTO getRequestDTO() {
		return requestDTO;
	}
	public void setRequestDTO(RequestDTO requestDTO) {
		this.requestDTO = requestDTO;
	}
	public ResponseDTO getResponseDTO() {
		return responseDTO;
	}
	public void setResponseDTO(ResponseDTO responseDTO) {
		this.responseDTO = responseDTO;
	}
	public String getServiceCode1() {
		return serviceCode1;
	}
	public void setServiceCode1(String serviceCode1) {
		this.serviceCode1 = serviceCode1;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}



	public String getFeecode() {
		return feecode;
	}



	public void setFeecode(String feecode) {
		this.feecode = feecode;
	}


}
