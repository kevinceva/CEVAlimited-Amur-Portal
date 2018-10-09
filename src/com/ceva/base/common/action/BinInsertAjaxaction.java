package com.ceva.base.common.action;


import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.dao.InsertBinDetailsDAO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
public class BinInsertAjaxaction extends ActionSupport {

	Logger logger=Logger.getLogger(BinInsertAjaxaction.class);

	JSONObject requestJSON=null;
	JSONObject responseJSON=null;
	RequestDTO requestDTO=null;
	ResponseDTO responseDTO=null;

	String result;
	String newbin;
	String binDescription;
	String binType;

	public String InsertBinDetails() throws Exception {
    	logger.debug("inside [BinInsertAjaxaction][InsertBinDetails]newbin::["+newbin+"]");
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		requestDTO=new RequestDTO();
		responseDTO= new ResponseDTO();
		HttpSession session = ServletActionContext.getRequest().getSession();
		requestJSON.put("BIN", newbin);
		requestJSON.put("binDescription", binDescription);
		requestJSON.put("binType", binType);
		requestJSON.put("MAKER_ID",	session.getAttribute(CevaCommonConstants.MAKER_ID));
		logger.debug("[BinInsertAjaxaction][InsertBinDetails][responseJSON:::::"+responseJSON+"]");
		requestDTO.setRequestJSON(requestJSON);
		logger.debug("[BinInsertAjaxaction][InsertBinDetails][requestDTO:::::"+requestDTO+"]");
		InsertBinDetailsDAO binDAO=new InsertBinDetailsDAO();
		responseDTO=binDAO.InsertBinDetails(requestDTO);
		logger.debug("[BinInsertAjaxaction][InsertBinDetails][responseDTO:::::"+responseDTO+"]");
		if (responseDTO != null && responseDTO.getErrors().size()==0) {

			logger.debug("[BinInsertAjaxaction][InsertBinDetails][responseJSON:::::"+responseJSON+"]");
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
	public String getNewbin() {
		return newbin;
	}
	public void setNewbin(String newbin) {
		this.newbin = newbin;
	}
	public String getBinDescription() {
		return binDescription;
	}
	public void setBinDescription(String binDescription) {
		this.binDescription = binDescription;
	}
	public String getBinType() {
		return binType;
	}
	public void setBinType(String binType) {
		this.binType = binType;
	}


}
