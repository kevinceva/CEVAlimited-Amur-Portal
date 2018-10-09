package com.ceva.base.ceva.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dao.CustomerDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dao.TransactionDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.util.HttpPostRequestHandler;
import com.opensymphony.xwork2.ActionSupport;

public class TransactionAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CustomerAction.class);
	
	String result;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	
	private HttpSession session = null;
	
	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;
	
	public String txnType = null;
	public String startdate = null;
	public String enddate = null;
	
	protected HttpServletRequest request;
	
	
	//Fetch Transaction details
	public String fetchTransactions() {
		System.out.println("Inside TransactionAction :: fetchTransctions");
		TransactionDAO transactionDAO = null;
		ArrayList<String> errors = null;
		
		String txnType = getTxnType();
		String startdate = getStartdate();
		String enddate = getEnddate();
		
/*Iterator keys = detailJSON.keys();
while(keys.hasNext())
  detailJSON.remove(keys.next().toString());*/

		try {
			
			responseJSON = null;
			responseDTO = null;
			requestDTO = null;
			
			requestJSON = new JSONObject();
			requestJSON.put("txnType", txnType);
			requestJSON.put("startDate", startdate);
			requestJSON.put("endDate", enddate);
			
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			transactionDAO = new TransactionDAO();
			responseDTO = transactionDAO.fetchTransactions(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("TXNS");
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
			e.printStackTrace();
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
		}
			
		return result;
	}
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

}
