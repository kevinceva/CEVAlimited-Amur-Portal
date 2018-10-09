package com.ceva.base.ceva.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionSupport;

public class PwaleetProfileValidateAjaxAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Logger logger = Logger.getLogger(PwaleetProfileValidateAjaxAction.class);

	private String profileId = null;
	private String msisdn = null;
	private JSONObject responseJSON = null;
	
	
	public String validateProfiles(){
		
		logger.debug("[PwaleetProfileValidateAjaxAction][validateProfiles]"+getMsisdn()+"  "+getProfileId());
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		String queryConst = "select count(*) from W_PROFILES where MSISDN=? or PROFILEID=?";
		Connection connection = null;
		int Cnt=0;
		try {
			connection = DBConnector.getConnection();
			pstmt = connection.prepareStatement(queryConst);
			pstmt.setString(1, getMsisdn());
			pstmt.setString(2, getProfileId());

			rs=pstmt.executeQuery();

			while(rs.next()){
				Cnt=rs.getInt(1);
			}
			logger.debug(" 	 After Executing Cnt..."+Cnt);
			
			if(Cnt>0){
				responseJSON = new JSONObject();
				responseJSON.put("STATUS", "EXISTS");
			}else{
				responseJSON = new JSONObject();
				responseJSON.put("STATUS", "SUCCESS");
			}
			
			logger.debug("	  responseJSON  : " + responseJSON);
		} catch (Exception e) {
			logger.debug("	  exception is  : " + e.getMessage());
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closePreparedStatement(pstmt);
			DBUtils.closeConnection(connection);
			queryConst = null;
		}

		return SUCCESS;
	}
	
	
	
	

	public String getProfileId() {
		return profileId;
	}


	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}


	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}
	
	
	
	
}
