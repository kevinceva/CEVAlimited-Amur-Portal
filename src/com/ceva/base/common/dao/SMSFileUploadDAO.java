package com.ceva.base.common.dao;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.ceva.util.HttpPostRequestHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import sun.security.jgss.spi.MechanismFactory;

public class SMSFileUploadDAO
{
	private Logger logger = Logger.getLogger(SMSFileUploadDAO.class);
	ResponseDTO responseDTO = null;
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;


	public static String replaceNewLineChar(String str) {
		try {
			if (!str.isEmpty()) {
				return str.replaceAll("\n\r", "\\n")
						.replaceAll("\n", "\\n")
						.replaceAll(System.lineSeparator(), "\\n");
			}
			return str;
		} catch (Exception e) {
			// Log this exception
			return str;
		}
	}

	public boolean refnovalid(String refno)
	{

		boolean status = true;
		Connection connection = null;
		this.logger.debug("Inside [CICDRModuleDAO][barcodenovalid].. ");

		PreparedStatement agentPstmt = null;
		ResultSet agentRS = null;

		try
		{


			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");
			String agentQry="SELECT COUNT(*) FROM cic_drtopup_det WHERE MPESA_REFNO=? ";
			this.logger.debug("agentQry ["+agentQry+"] refno["+refno+"]");

			agentPstmt = connection.prepareStatement(agentQry);
			agentPstmt.setString(1,refno );
			agentRS = agentPstmt.executeQuery();				

			if (agentRS.next())
			{	 
				int agnetCnt = (Integer.parseInt(agentRS.getString(1)));
				this.logger.debug("agnetCnt ["+agnetCnt+"]");
				if( agnetCnt == 0)
				{
					status = true;
				}else{
					status = false;
				}
			}

		}catch(Exception e)
		{

			e.printStackTrace();
		}finally{
			DBUtils.closeResultSet(agentRS);
			DBUtils.closePreparedStatement(agentPstmt);
			DBUtils.closeConnection(connection);
		}


		return status;

	}

	public String initiateSMSDet(JSONArray finalData, String userId, String fileName){

		JSONObject jres = new JSONObject();
		JSONArray drtopupJsonArray = null;
		
		String status = "";
		Connection connection = null;
		PreparedStatement casepsmt = null;
		int inpSize=0;
		int j=0;
		this.responseDTO = new ResponseDTO();
		PreparedStatement merchantPstmt = null;
		ResultSet merchantRS = null;
		PreparedStatement valPstmt = null;
		ResultSet valRS = null;
		PreparedStatement updPstmt = null;
		ResultSet updRS = null;
		PreparedStatement insPstmt = null;
		ResultSet insRS = null;
		
		JSONObject json = null;
		String rsedby="";
		String aprvdby="";
		
		int refno_cnt = 0;
		int updCnt = 0;
		
		String fname="";
		String lname="";
		

		String merchantQry = "select fname from cic_clients where mobile_number=? ";
		String insDRTopUpQry = "insert into cic_sms_file_upload (REF_NO,MOBILE_NUMBER,NAME,MESSAGE,HEADER,STATUS,UPLOAD_BY,UPLOAD_DATE) "
				+ " values (?,?,?,?,'CIC_BIMA','P',?,sysdate)";
		String updDRTopUpQry = "update cic_sms_file_upload set status=? where MOBILE_NUMBER=? and ref_no=?";
		try
		{

			drtopupJsonArray = new JSONArray();
			connection = connection == null ? DBConnector.getConnection() : connection;
			this.logger.debug("connection is [" + connection + "]");
			logger.debug("Inserting Data into card inventory Table");
			connection.setAutoCommit(false);

			logger.info("finalData size:::: ["+finalData.size()+"]");
			logger.info("finalData ["+finalData.toString()+"]");

			logger.info("client Name Query Is ["+merchantQry+"] ");
			logger.info("ins sms up Query Is ["+insDRTopUpQry+"] ");
			logger.info("upd sms up Query Is ["+updDRTopUpQry+"] ");

			j=0;
			int retval=0;
			inpSize=finalData.size();
			logger.debug("no of inputs ["+inpSize+"]");
			json = new JSONObject();
			insPstmt = connection.prepareStatement(insDRTopUpQry);
			merchantPstmt = connection.prepareStatement(merchantQry);
			updPstmt = connection.prepareStatement(updDRTopUpQry);
			for (int i = 0; i < finalData.size(); i++) {
				
				jres = (JSONObject) finalData.get(i);
				String rnum=null;
				
			    DateFormat df = new SimpleDateFormat("ddmmss");
			    
		        Date dateobj = new Date();
		        rnum=RandomStringUtils.randomNumeric(3)+df.format(dateobj)+RandomStringUtils.randomNumeric(2);
				
				String mob = (String) jres.get("mobNo");
				String msgText = (String) jres.get("msgText");
				String mkrid = userId;
				
				logger.debug(" Input values mob["+mob+"] msgText["+msgText+"] mkrid["+mkrid+"]");	
				
				merchantPstmt.setString(1, mob);
				merchantRS = merchantPstmt.executeQuery(); 
	
				if (merchantRS.next())
				{
					fname= merchantRS.getString(1);
				}else
				{
					fname="Customer";
				}
					
				insPstmt.setString(1,rnum );
				insPstmt.setString(2,mob );
				insPstmt.setString(3,fname );
				insPstmt.setString(4,msgText );
				insPstmt.setString(5,mkrid);
				int insrt=insPstmt.executeUpdate();
				
				this.logger.debug("Loop ["+i+"] sms ins cnt ["+insrt+"]");
				
				/*String prvdr = "Mpesa";
				String clnAcnt = "NO_DATA";
				String prdname = "Ceva";
				String metadt = "NO_DATA";
				String reqdt = "NO_DATA";
				String chanel = "174259";
		
					

		
					this.logger.debug("Loop ["+i+"] DR TopUp URL Params - mob ["
							+ mob + "] amt["
							+ famt + "] bonus [" + bonus + "] mkrid [" + mkrid +"]");*/
		
					//Production IP : 52.166.1.232
					//Test IO : 104.42.234.123
					/*HttpPostRequestHandler httpPostRequestHandler = new HttpPostRequestHandler();
					String webServiceURL = "http://104.42.234.123:1234/cicinsurancecore/rest/cicinsrest/c2bmanualinsreq"
							+ "?provider=" + prvdr + "&clientAccount=" + clnAcnt + "&productName="
							+ prdname + "&mobileNo=+" + mob + "&value=KES%20" + famt + "&providerMetadata=" 
							+ metadt + "&fname=" + fname + "&lname=" + lname + "&providerChannel="
							+ chanel + "&reqdata=" + reqdt;
		
					this.logger.debug("Loop ["+i+"] Web Service URL  :::: " + webServiceURL);
					String json1 = httpPostRequestHandler.sendRestPostRequest(webServiceURL);
					JSONObject obj = JSONObject.fromObject(json1);
					this.logger.debug("Loop ["+i+"] End to Sent Mobile Otp >> [" + obj + "]");
					String responseCode = obj.getString("STATUS_CODE");
					String responseMsg = obj.getString("STATUS_MSG");*/
		
					String responseCode ="00";
					String responseMsg ="Success";
					this.logger.debug("Loop ["+i+"]Response Code:"+responseCode);
					this.logger.debug("Loop ["+i+"]Response Msg:"+responseMsg);
		
								
					json.put("Loop ["+i+"] result", responseMsg);
		
					if ("00".equalsIgnoreCase(responseCode))
					{
		
						
						updPstmt.setString(1,"S" );
						updPstmt.setString(2,mob );
						updPstmt.setString(3,rnum );
						updCnt=updPstmt.executeUpdate();	
						
						this.logger.debug("Loop ["+i+"] update successs status ["+updCnt+"] responseMsg ["+responseMsg+"]");
					}else
					{
						
						updPstmt.setString(1,"F" );
						updPstmt.setString(2,mob );
						updPstmt.setString(3,rnum );
						updCnt=updPstmt.executeUpdate();	
						
						this.logger.debug("Loop ["+i+"] update status ["+updCnt+"] responseMsg ["+responseMsg+"]");
					}	
					//drtopupJsonArray.add(json);
					//json.clear();
					
				
				j=j+1;
			}
			connection.commit();
			status="SUCCESS";
			responseDTO.addMessages("Uploaded Successfully");

			logger.info("finalData.size() >>>>>>>>> ["+finalData.size()+"] No Lo ["+j+"]");
			


		} catch (Exception e) {
			try{
				status="FAILURE";
				responseDTO.addMessages("Insertion failed due to some error");
				connection.rollback();
			}catch(Exception e1){

			}
			e.printStackTrace();

		}finally{
			DBUtils.closePreparedStatement(casepsmt);
			DBUtils.closeConnection(connection);
		}
		logger.debug(" agent map insrt sataus:"+status);
		return status;

	}				
}
