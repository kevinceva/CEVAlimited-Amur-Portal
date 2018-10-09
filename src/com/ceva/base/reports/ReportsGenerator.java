package com.ceva.base.reports;

import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;







import net.sf.jasperreports.engine.export.JRXlsExporter;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.base.common.utils.DBConnector;

public class ReportsGenerator {
	
	Logger logger=Logger.getLogger(ReportsGenerator.class);
	
	Connection connection = null;
	JasperReport jasperReport;
	JasperPrint jasperPrint;
	
	ResponseDTO responseDTO=null;
	JSONObject requestJSON=null;
	JSONObject responseJSON=null;
	
	public ResponseDTO generateReport(RequestDTO requestDTO){
		
		responseDTO = new ResponseDTO();
		requestJSON = new JSONObject();
		responseJSON = new JSONObject();
		
		requestJSON=requestDTO.getRequestJSON();
		logger.debug("[ReportsGenerator][generateReport][requestJSON:"+requestJSON+"]");
		
		ResourceBundle resourceBundle = null;
		SimpleDateFormat fromWeb = null;
		SimpleDateFormat reportFormat = null;
		String reportInputDate = null;
		HashMap parameters = null;
		String reportBasePath = null;
		String reportDestPath = null;
		String absoluteJrxml = null;
		String outFileName = null;
		File reporrtsDestinationPath = null;
		PreparedStatement  rptPstmt = null;
		ResultSet rptRs =null;
		String reportType = null;
		String rptDate = null;
		
		if(requestJSON.getString("REPORT_TYPE") != null){
			
			if(requestJSON.getString("REPORT_TYPE").equals("D") || requestJSON.getString("REPORT_TYPE").equals("W") || requestJSON.getString("REPORT_TYPE").equals("M") || requestJSON.getString("REPORT_TYPE").equals("Q")){
				
				if(requestJSON.getString("REPORT_TYPE").equals("D")){
					parameters = new HashMap();
					 fromWeb = new SimpleDateFormat("dd-MM-yyyy");
					 reportFormat = new SimpleDateFormat("dd-MMM-YYYY");
					try {
						reportInputDate = reportFormat.format(fromWeb.parse(requestJSON.getString("REPORT_DATE")));
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					parameters.put("REPORT_INPUT_DATE",reportInputDate);
				}
				if(requestJSON.getString("REPORT_TYPE").equals("W")){
					rptDate=requestJSON.getString("REPORT_WEEK")+"-"+requestJSON.getString("REPORT_MONTH")+"-"+requestJSON.getString("REPORT_YEAR");
					parameters = new HashMap();
					 fromWeb = new SimpleDateFormat("dd-MM-yyyy");
					 reportFormat = new SimpleDateFormat("dd-MMM-YYYY");
					try {
						reportInputDate = reportFormat.format(fromWeb.parse(rptDate));
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					parameters.put("REPORT_INPUT_DATE",reportInputDate);
					requestJSON.put("REPORT_DATE", rptDate);
				}
				if(requestJSON.getString("REPORT_TYPE").equals("M")){
					rptDate=getLastDayofMonth(Integer.parseInt(requestJSON.getString("REPORT_MONTH")),Integer.parseInt(requestJSON.getString("REPORT_YEAR")))+"-"+(Integer.parseInt(requestJSON.getString("REPORT_MONTH")))+"-"+requestJSON.getString("REPORT_YEAR");
					logger.debug("rptDate:::::::::::::::::::"+rptDate);
					parameters = new HashMap();
					 fromWeb = new SimpleDateFormat("dd-MM-yyyy");
					 reportFormat = new SimpleDateFormat("dd-MMM-YYYY");
					try {
						reportInputDate = reportFormat.format(fromWeb.parse(rptDate));
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					parameters.put("REPORT_INPUT_DATE",reportInputDate);
					requestJSON.put("REPORT_DATE", rptDate);
				}
				if(requestJSON.getString("REPORT_TYPE").equals("Q")){
					if(requestJSON.getString("REPORT_QUARTER").equals("01")){
						rptDate="31-03-"+requestJSON.getString("REPORT_YEAR");
					}else if(requestJSON.getString("REPORT_QUARTER").equals("02")){
						rptDate="30-06-"+requestJSON.getString("REPORT_YEAR");
					}else if(requestJSON.getString("REPORT_QUARTER").equals("03")){
						rptDate="30-09-"+requestJSON.getString("REPORT_YEAR");
					}else if(requestJSON.getString("REPORT_QUARTER").equals("04")){
						rptDate="31-12-"+requestJSON.getString("REPORT_YEAR");
					}
					logger.debug("rptDate in Quarter:::::::::::::::::::"+rptDate);
					parameters = new HashMap();
					 fromWeb = new SimpleDateFormat("dd-MM-yyyy");
					 reportFormat = new SimpleDateFormat("dd-MMM-YYYY");
					try {
						reportInputDate = reportFormat.format(fromWeb.parse(rptDate));
					} catch (ParseException e) {
					    e.printStackTrace();
					}
					parameters.put("REPORT_INPUT_DATE",reportInputDate);
					requestJSON.put("REPORT_DATE", rptDate);
				}
				resourceBundle = ResourceBundle.getBundle("pathinfo_config");
				logger.debug("[ReportsGenerator][generateReport][resourceBundle:"+resourceBundle+"]");
				
				//code for getting jrxml file from folder
				reportBasePath = resourceBundle.getString("REPORT_BASE_PATH");
				logger.debug("[ReportsGenerator][generateReport][reportBasePath:"+reportBasePath+"]");
				// code for getting destination folder
				reportDestPath = resourceBundle.getString("REPORT_DEST_PATH");
				logger.debug("[ReportsGenerator][generateReport][reportDestPath:"+reportDestPath+"]");
				if(requestJSON.getString("REPORT_TYPE").equals("D")){
					reportType="D";
					reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.DAILY_PATH);
					if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					}
					reporrtsDestinationPath= new File(reporrtsDestinationPath+CevaCommonConstants.SEPARATOR+requestJSON.getString("REPORT_DATE"));
					 if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					 }
					 logger.debug("[ReportsGenerator][generateReport][reporrtsDestinationPath:"+reporrtsDestinationPath+"]");
					 
				}
				if(requestJSON.getString("REPORT_TYPE").equals("W")){
					reportType="W";
					reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.WEEKLY_PATH);
					if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					}
					reporrtsDestinationPath= new File(reporrtsDestinationPath+CevaCommonConstants.SEPARATOR+rptDate);
					 if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					 }
					 logger.debug("[ReportsGenerator][generateReport][reporrtsDestinationPath:"+reporrtsDestinationPath+"]");
				}
				if(requestJSON.getString("REPORT_TYPE").equals("M")){
					reportType="M";
					reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.MONTHLY_PATH);
					if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					}
					reporrtsDestinationPath= new File(reporrtsDestinationPath+CevaCommonConstants.SEPARATOR+rptDate);
					 if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					 }
					 logger.debug("[ReportsGenerator][generateReport][reporrtsDestinationPath:"+reporrtsDestinationPath+"]");
				}
				if(requestJSON.getString("REPORT_TYPE").equals("Q")){
					reportType="Q";
					reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.QUARTERLY_PATH);
					if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					}
					reporrtsDestinationPath= new File(reporrtsDestinationPath+CevaCommonConstants.SEPARATOR+rptDate);
					 if (!reporrtsDestinationPath.exists()) {
						 reporrtsDestinationPath.mkdir();
					 }
					 logger.debug("[ReportsGenerator][generateReport][reporrtsDestinationPath:"+reporrtsDestinationPath+"]");
				}
				 try {
					connection=DBConnector.getConnection();
					String rptQry="";
					if(requestJSON.getString("REPORT_NAME").equals("ALL")){
						logger.debug("[ReportsGenerator][generateReport][ALL]");
						rptQry="select REPORT_NAME,REPORT_DESC from  REPORTS_CONFIG_TBL where REPORT_TYPE=? ";
						rptPstmt = connection.prepareStatement(rptQry);
						rptPstmt.setString(1, reportType);	
					}else{
						logger.debug("[ReportsGenerator][generateReport][Inside Single Report Selection]");
						rptQry="select REPORT_NAME,REPORT_DESC from  REPORTS_CONFIG_TBL where REPORT_NAME=trim(?) ";
						rptPstmt = connection.prepareStatement(rptQry);
						rptPstmt.setString(1, requestJSON.getString("REPORT_NAME"));
					}
					rptRs = rptPstmt.executeQuery();
					while(rptRs.next()){
						String rptName=rptRs.getString(1);
						String rptDesc=rptRs.getString(2);
						absoluteJrxml=reportBasePath+rptName+".jrxml";
						logger.debug("[ReportsGenerator][generateReport][absoluteJrxml:"+absoluteJrxml+"]");
						requestJSON.put("ABSOLUTE_JRXML", absoluteJrxml);
						outFileName=reporrtsDestinationPath+CevaCommonConstants.SEPARATOR+rptDesc;
						logger.debug("[ReportsGenerator][generateReport][ALL][outFileName:"+outFileName+"]");
						requestJSON.put("OUT_FILE_NAME", outFileName);
						generatePdfExcelReport(requestJSON,parameters,connection);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else{
			responseDTO.addError("Report Type Missing.");
		}
		return responseDTO;
	}
	
	public String generatePdfExcelReport(JSONObject requestJSON,HashMap jasperParameter,Connection connection){
		String status=null;
		logger.debug("[ReportsGenerator][generatePdfExcelReport][requestJSON:"+requestJSON+"]");
		try {
			logger.debug("[ReportsGenerator][generatePdfExcelReport][connection:"+connection+"]");
			
			try {
				jasperReport = JasperCompileManager.compileReport(requestJSON.getString("ABSOLUTE_JRXML"));
				jasperPrint = JasperFillManager.fillReport(jasperReport,jasperParameter, connection);
				JasperExportManager.exportReportToPdfFile(jasperPrint, requestJSON.getString("OUT_FILE_NAME")+".pdf");
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, requestJSON.getString("OUT_FILE_NAME")+".xls" );
				exporter.exportReport();
			} catch (JRException e) {
				status="fail";
				e.printStackTrace();
			}
			status="success";
			return status;
		} catch (Exception e) {
			status="fail";
			e.printStackTrace();
		}
		
		return status;
	}
	
	private int getLastDayofMonth(int month, int year) {
         Calendar dateCal = Calendar.getInstance();
        dateCal.set(year, month, 2);
        int maxDay = dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }
}
