package com.ceva.posta.airtime.insurance.sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import com.ceva.base.common.utils.DBConnector;

public class SendSMS {

	Logger logger= Logger.getLogger(SendSMS.class);

	
	
	public void requestToSendSMS(){
		
		Connection connection = null;
		PreparedStatement smsPstmt = null;
		ResultSet smsRs =null;
		
		String query="select MOBILE_NO,OUT_MESSAGE,TXN_REF_NO from ALERTS where APPL='SMS' and FETCH_STATUS='P' and RETRY_COUNT<3";
		
		try{
			connection = DBConnector.getConnection();
			smsPstmt = connection.prepareStatement(query);
			
			smsRs = smsPstmt.executeQuery();
			
			String mobileNo=null;
			String smsMsg=null;
			String txnRefNo=null;
			
			while (smsRs.next()) {
				
				mobileNo = smsRs.getString(1);
				smsMsg = smsRs.getString(2);
				txnRefNo = smsRs.getString(3);
				
				logger.debug("Mobile NO::"+mobileNo);
				logger.debug("SMS MSG::"+smsMsg);
				logger.debug("TXN REF NO::"+txnRefNo);
				
				if(mobileNo == null || smsMsg == null || mobileNo == "" || smsMsg ==""){
					String updateQry="update ALERTS set FETCH_STATUS='F' where TXN_REF_NO=?";
					PreparedStatement pstmt = connection.prepareStatement(updateQry);
					pstmt.setString(1, txnRefNo);
					
					int i= pstmt.executeUpdate();
					
					if(i==1){
						logger.debug("SMS Send Failed due to null in MSG or MOBILE NO and record updated...");
					}
					
					if(pstmt !=null)
						pstmt.close();
					
				}else{
					String result=new SMSRequest().sendSMS(mobileNo, smsMsg);
					
					if(result.equals("Success")){
							String updateQry="update ALERTS set FETCH_STATUS='S' where MOBILE_NO=? and TXN_REF_NO=?";
							PreparedStatement pstmt = connection.prepareStatement(updateQry);
							pstmt.setString(1, mobileNo);
							pstmt.setString(2, txnRefNo);
							
							int i= pstmt.executeUpdate();
							
							if(i==1){
								logger.debug("SMS send Successfully and record updated...");
							}
							
							if(pstmt !=null)
								pstmt.close();
							
					}else{
						int retCnt=0;
						
						String selectQry="select RETRY_COUNT from ALERTS where MOBILE_NO=? and TXN_REF_NO=?";
						PreparedStatement pstmt = connection.prepareStatement(selectQry);
						pstmt.setString(1, mobileNo);
						pstmt.setString(2, txnRefNo);
						
						ResultSet rs= pstmt.executeQuery();
						
						while(rs.next()){
							retCnt = rs.getInt(1);
						}
						
						rs.close();
						pstmt.close();
						
						if(retCnt>2){
							String updateQry="update ALERTS set FETCH_STATUS='F' where MOBILE_NO=? and TXN_REF_NO=?";
							pstmt = connection.prepareStatement(updateQry);
							pstmt.setString(1, mobileNo);
							pstmt.setString(2, txnRefNo);
							
							int i= pstmt.executeUpdate();
							
							if(i==1){
								logger.debug("SMS send Failed and record updated.....complete sms send failed.");
							}
							
							if(pstmt !=null)
								pstmt.close();
						}else{
							String updateQry="update ALERTS set RETRY_COUNT=RETRY_COUNT+1 where MOBILE_NO=? and TXN_REF_NO=?";
							pstmt = connection.prepareStatement(updateQry);
							pstmt.setString(1, mobileNo);
							pstmt.setString(2, txnRefNo);
							int i= pstmt.executeUpdate();
							if(i==1){
								logger.debug("SMS send Failed and record updated...");
							}
							
							if(pstmt !=null)
								pstmt.close();
						}
						
						
						
						logger.debug("issue with sms vendor connection...");
					}
				}
				
				
			}	
		}catch(Exception exception){
			
		}finally{
			
			if(connection !=null){
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
