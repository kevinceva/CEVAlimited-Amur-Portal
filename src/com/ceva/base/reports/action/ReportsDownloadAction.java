package com.ceva.base.reports.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class ReportsDownloadAction extends ActionSupport {

	/**
	 * @Author : Ravi D
	 * @since : 17-07-2014
	 * @version : V1.0
	 */
	private static final long serialVersionUID = 1L;
	
	
Logger logger = Logger.getLogger(ReportsAction.class);
	
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject locationJSON = null;


	private InputStream inputStream;
	private String fileName;
	private long contentLength;
	
	

	private String result = null;
	private String method = null;
	private String reportType = null;
	
	private String reportDate = null;
	private String yearName = null;
	private String quarterName = null;
	private String monthName = null;
	private String weekName = null;
	private String reportName = null;
	private String reportDesc = null;
	private String downloadFileName = null;
	
	

	public String getCommonScreen() {
		logger.debug("Inside [getCommonScreen]");
		result = "success";
		return result;
	}
	
	public String getReportsList() {

		logger.debug("Inside getReportsList... ");
		ResourceBundle resourceBundle = null;
		String reportDestPath = null;
		File reporrtsDestinationPath = null;
		File[] fileList = null;
		HashSet<String>  fileNames = null;
		JSONArray fileListJSONArray = null;
		String reporrtsPath= null;
		String rptDate = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();

				if(getReportType()!=null){
					if(getReportType().equals("D") || getReportType().equals("W") || getReportType().equals("M") || getReportType().equals("Q")){
						resourceBundle = ResourceBundle.getBundle("pathinfo_config");
						logger.debug("[ReportsDownloadAction][getReportsList][resourceBundle:"+resourceBundle+"]");
						// code for getting destination folder
						reportDestPath = resourceBundle.getString("REPORT_DEST_PATH");
						logger.debug("[ReportsDownloadAction][getReportsList][reportDestPath:"+reportDestPath+"]");
						if(getReportType().equals("D")){
							reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.DAILY_PATH+CevaCommonConstants.SEPARATOR+getReportDate());
						}
						else if(getReportType().equals("W")){
							rptDate=getWeekName()+"-"+getMonthName()+"-"+getYearName();
							setReportDate(rptDate);
							reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.WEEKLY_PATH+CevaCommonConstants.SEPARATOR+getReportDate());
						}
						else if(getReportType().equals("M")){
							rptDate=getWeekName()+"-"+getMonthName()+"-"+getYearName();
							rptDate=getLastDayofMonth(Integer.parseInt(getMonthName()),Integer.parseInt(getYearName()))+"-"+(Integer.parseInt(getMonthName()))+"-"+getYearName();
							setReportDate(rptDate);
							reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.MONTHLY_PATH+CevaCommonConstants.SEPARATOR+getReportDate());
						}
						else if(getReportType().equals("Q")){
							if(getQuarterName().equals("01")){
								rptDate="31-03-"+getYearName();
							}else if(getQuarterName().equals("02")){
								rptDate="30-06-"+getYearName();
							}else if(getQuarterName().equals("03")){
								rptDate="30-09-"+getYearName();
							}else if(getQuarterName().equals("04")){
								rptDate="31-12-"+getYearName();
							}
							logger.debug("rptDate in Quarter:::::::::::::::::::"+rptDate);
							setReportDate(rptDate);
							reporrtsDestinationPath = new File(reportDestPath+CevaCommonConstants.QUARTERLY_PATH+CevaCommonConstants.SEPARATOR+getReportDate());
						}
						logger.debug("[ReportsDownloadAction][getReportsList][reporrtsDestinationPath:"+reporrtsDestinationPath+"]");
						if(reporrtsDestinationPath.exists()){
							fileList = reporrtsDestinationPath.listFiles();
							logger.debug("[ReportsDownloadAction][getReportsList][No of Files:"+fileList.length+"]");
							if(fileList.length==0){
								addActionError("No Files available.");
							}else{
								fileListJSONArray = new JSONArray();
								reporrtsPath=reporrtsDestinationPath.toString();
							    reporrtsPath=reporrtsPath.replace('\\', '$'); 
								if(getReportName().equals("ALL")){
									fileNames = new HashSet<String>();
									for (File file : fileList) {
								        if (file.isFile()) {
								        	fileNames.add(FilenameUtils.removeExtension(file.getName().toString()));
								        } 
								    }
									
									if(fileNames.size()>0){
										Iterator fileIterator = fileNames.iterator(); 
										JSONObject json=null;
									    while (fileIterator.hasNext()){
									    	json=new JSONObject();
									    	json.put("FILE_NAME", fileIterator.next().toString());
									    	fileListJSONArray.add(json);
									    }
									}
								}else{
									reporrtsDestinationPath=new File(reporrtsDestinationPath+CevaCommonConstants.SEPARATOR+getReportDesc()+".pdf");
									logger.debug("[ReportsDownloadAction][getReportsList][reporrtsDestinationPath for Single File:"+reporrtsDestinationPath+"]");
									if(reporrtsDestinationPath.exists()){
										JSONObject json=null;
										json=new JSONObject();
								    	json.put("FILE_NAME",getReportDesc() );
								    	fileListJSONArray.add(json);   
									}else{
										addActionError("No Files available.");
									}
									
								}
								 responseJSON.put("FILE_PATH", reporrtsPath);
								 responseJSON.put("REPORT_DATE", getReportDate());
								 responseJSON.put("REPORT_FILE_LIST", fileListJSONArray); 
								 logger.debug("responseJSON:::::::: "+responseJSON);  
							}
						}else{
							addActionError("No Files available.");
						}
						result = "success";
					}else if(getReportType().equals("W")){
						requestJSON.put("REPORT_YEAR", getYearName());
						requestJSON.put("REPORT_MONTH", getMonthName());
						requestJSON.put("REPORT_WEEK", getWeekName());
						result = "success";
					}else if(getReportType().equals("M")){
						requestJSON.put("REPORT_YEAR", getYearName());
						requestJSON.put("REPORT_MONTH", getMonthName());
						result = "success";
					}else if(getReportType().equals("Q")){
						requestJSON.put("REPORT_YEAR", getYearName());
						requestJSON.put("REPORT_QUARTER", getQuarterName());
						result = "success";
					}else{
						addActionError("Please select Correct Report Type.");
						result = "fail";
					}
				}else{
					addActionError("Report Type Missing.");
					result = "fail";
				}
				logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getReportsList ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestJSON = null;
		}

		return result;
	}
	
	public String DownloadFile(){
		logger.debug("File Name:::"+getDownloadFileName());
		File fileToDownload = new File(getDownloadFileName());

		try {
			inputStream = new FileInputStream(fileToDownload);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileName = fileToDownload.getName();
		contentLength = fileToDownload.length();
		
		return SUCCESS;
	}
	
	private int getLastDayofMonth(int month, int year){
         Calendar dateCal = Calendar.getInstance();
        dateCal.set(year, month, 2);
        int maxDay = dateCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }
	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	public String getReportDesc() {
		return reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}

	
	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
}
