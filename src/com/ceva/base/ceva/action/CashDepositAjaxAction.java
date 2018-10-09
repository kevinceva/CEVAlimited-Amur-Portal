package com.ceva.base.ceva.action;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionSupport;

public class CashDepositAjaxAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(CashDepositAjaxAction.class);

	private Long amount = null;
	private String bankId = null;
	private JSONObject responseJSON = null;
	
	
	public String fetchBarclaysFeeAmount(){
		
		logger.debug("[CashDepositAjaxAction][fetchBarclaysFeeAmount]");
		CallableStatement callableStatement = null;
		String queryConst = "{call CASHDEPOSITPKG.fetchBarclaysFeeAmount(?,?,?)}";
		Connection connection = null;
		try {
			connection = DBConnector.getConnection();
			logger.debug("	 queryConst [" + queryConst + "]");
			callableStatement = connection.prepareCall(queryConst);
			callableStatement.setString(1, getBankId());
			callableStatement.setLong(2, getAmount());
			callableStatement.registerOutParameter(3, Types.NUMERIC);

			callableStatement.execute();

			logger.debug(" 	 After Executing callableStatement.");

			int feeAmount = callableStatement.getInt(3);
			logger.debug("Fee Amount::"+feeAmount);
			responseJSON = new JSONObject();
			responseJSON.put("FEE_AMT", feeAmount);

		} catch (Exception e) {
			logger.debug("	  exception is  : " + e.getMessage());
		} finally {
			DBUtils.closeCallableStatement(callableStatement);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}
	
	
	
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	
	
	
	
}
