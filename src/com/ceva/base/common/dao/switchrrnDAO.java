package com.ceva.base.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class switchrrnDAO {
	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject resonseJSON=null;
	Connection connection=null;
	JSONObject json=null;

	Logger logger=Logger.getLogger(switchrrnDAO.class);


	public ResponseDTO SwitchRRDAO(RequestDTO requestDTO){
		responseDTO=new ResponseDTO();
		requestJSON=new JSONObject();
		resonseJSON=new JSONObject();
 
		logger.debug("inside [switchrrnDAO][SwitchrrDAO]"); 
 		HashMap<String,Object> BankDataMap=null; 
		JSONObject resultJson=null;
		requestJSON=requestDTO.getRequestJSON();
		String rrn="";


		PreparedStatement userChkPstmt=null;
		ResultSet USerChkRS=null;
		String userChkQry="select RRN,decode(RESPONSECODE,'00',(CASE WHEN RESPONSECODE = '00' AND ISREVERSED = '1' THEN 'Reversal' ELSE 'Approved' END),'Declined'), "
				+ "(amount/100),MERCHANTID,TERMINALNUMBER,substr(PAN,1,4)||'XXXXXXXX'||substr(PAN,13,16),"
				+ " APPROVEDBY , (select BM.BANK_NAME  FROM  BANK_MASTER BM WHERE BM.BIN=substr(PAN,1,6)),"
				+ "(select PBM.OFFICE_CODE||'-'||PBM.OFFICE_NAME from user_information A,USER_LOGIN_CREDENTIALS B ,POSTA_BRANCH_MASTER PBM where A.COMMON_ID=B.COMMON_ID AND b.LOGIN_USER_ID=APPROVEDBY  AND A.LOCATION=PBM.OFFICE_CODE ),"
				+ "DECODE(TXNTYPE, 'DOS', 'CASH DEPOSIT', 'WDL', 'CASH WITHDRAWL','BAL','BALANCE ENQUIRY','HDM','HUDUMA',TXNTYPE)  from tbl_TRANLOG where RRN=?";

		try {
 			BankDataMap=new HashMap<String,Object>(); 
			resultJson=new JSONObject();

			connection = connection == null ? DBConnector.getConnection():connection;
			logger.debug("inside [switchrrnDAO][SwitchrrDAO] connection["+connection+"]");
			rrn=requestJSON.getString(CevaCommonConstants.RRN);
			userChkPstmt=connection.prepareStatement(userChkQry);
			userChkPstmt.setString(1, rrn);
			USerChkRS=userChkPstmt.executeQuery();

			if(USerChkRS.next()){ 
				resultJson.put(CevaCommonConstants.RRN, USerChkRS.getString(1));
				resultJson.put(CevaCommonConstants.responseCode, USerChkRS.getString(2));  
				resultJson.put("amt", USerChkRS.getString(3)); 
				resultJson.put("mid", USerChkRS.getString(4)); 
				resultJson.put("tid", USerChkRS.getString(5)); 
				resultJson.put("maskedcard", USerChkRS.getString(6)); 
				resultJson.put("authid", USerChkRS.getString(7)); 
				resultJson.put("bankname", USerChkRS.getString(8)); 
				resultJson.put("location", USerChkRS.getString(9)); 
				resultJson.put("txntype", USerChkRS.getString(10)); 
			}

 			BankDataMap.put(CevaCommonConstants.USER_CHECK_INFO, resultJson);

			logger.debug("inside [switchrrnDAO][SwitchrrDAO][merchantDataMap:::"+BankDataMap+"]");
			responseDTO.setData(BankDataMap);
			logger.debug("inside [switchrrnDAO][SwitchrrDAO][responseDTO:::"+responseDTO+"]");

		} catch (SQLException e) {
			 
		}finally{

			DBUtils.closeResultSet(USerChkRS);
			DBUtils.closePreparedStatement(userChkPstmt);
			DBUtils.closeConnection(connection);
		}

		return responseDTO;
	}

}
