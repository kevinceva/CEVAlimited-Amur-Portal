package com.ceva.base.reports.action;

import java.util.ArrayList;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.ceva.base.common.dao.InstanceReportsDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.reports.ReportsGenerator;
import com.opensymphony.xwork2.ActionSupport;

public class InstanceReportsAction extends ActionSupport{

	/**
	 * @Author : Ravi D
	 * @since : 14-07-2014
	 * @version : V1.0
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(InstanceReportsAction.class);
	
	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject locationJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private String result = null;
	private String method = null;
	private String reportType = null;
	
	private String reportDate = null;
	private String yearName = null;
	private String quarterName = null;
	private String monthName = null;
	private String weekName = null;
	private String reportName = null;
	

	public String getCommonScreen() {
		logger.debug("Inside [getCommonScreen]");
		result = "success";
		return result;
	}
	
	public String getReportParameters(){
		logger.debug("inside [ReportsAction][getReportParameters][reportType::"+reportType+"]");
		requestJSON=new JSONObject();
		responseJSON=new JSONObject();
		requestDTO=new RequestDTO();
		responseDTO= new ResponseDTO();
		requestJSON.put("REPORT_TYPE", reportType);
		requestJSON.put("REPORT_NAME", reportName);
		requestDTO.setRequestJSON(requestJSON);	
		logger.debug("[ReportsAction][getReportParameters][requestDTO:::::"+requestDTO+"]");
		InstanceReportsDAO ajaxDAO=new InstanceReportsDAO();
		responseDTO=ajaxDAO.getReportParameters(requestDTO);
		logger.debug("[ReportsAction][getReportParameters][responseDTO:::::"+responseDTO+"]");
		
		if (responseDTO != null && responseDTO.getErrors().size()==0) {		
			responseJSON=(JSONObject) responseDTO.getData().get("REPORT_PARAMETERS");
			logger.debug("[ReportsAction][getReportParameters][responseJSON:::::"+responseJSON+"]");
		}else{			
			ArrayList<String> errors=(ArrayList<String>) responseDTO.getErrors();
			for(int i=0;i<errors.size();i++){
				addActionError(errors.get(i));
			}
		}
    	return SUCCESS;
	}
	
	public String generateReports() {

		logger.debug("Inside generateReports... ");
		ReportsGenerator reportGenerator = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();
			responseDTO = new ResponseDTO();

				if(getReportType()!=null){
					requestJSON.put("REPORT_TYPE", getReportType());
					if(getReportType().equals("D")){
						requestJSON.put("REPORT_DATE", getReportDate());
					}else if(getReportType().equals("W")){
						requestJSON.put("REPORT_YEAR", getYearName());
						requestJSON.put("REPORT_MONTH", getMonthName());
						requestJSON.put("REPORT_WEEK", getWeekName());
					}else if(getReportType().equals("M")){
						requestJSON.put("REPORT_YEAR", getYearName());
						requestJSON.put("REPORT_MONTH", getMonthName());
					}else if(getReportType().equals("Q")){
						requestJSON.put("REPORT_YEAR", getYearName());
						requestJSON.put("REPORT_QUARTER", getQuarterName());
					}else{
						addActionError("Please select Correct Report Type.");
					}
					
					if(getReportType().equals("D") || getReportType().equals("W") || getReportType().equals("M") || getReportType().equals("Q")){
						requestJSON.put("REPORT_NAME", getReportName());
						requestDTO.setRequestJSON(requestJSON);
						reportGenerator = new ReportsGenerator();
						responseDTO = reportGenerator.generateReport(requestDTO);
						logger.debug("Response DTO [" + responseDTO + "]");
						if (responseDTO != null && responseDTO.getErrors().size() == 0) {
							logger.debug("Getting messages from ReportsGenerator.");
							messages = (ArrayList<String>) responseDTO.getMessages();
							logger.debug("Messages [" + messages + "]");
							for (int i = 0; i < messages.size(); i++) {
								addActionMessage(messages.get(i));
							}
							result = "success";
						} else {
							logger.debug("Getting error from DB.");
							errors = (ArrayList<String>) responseDTO.getErrors();
							logger.debug("Errors [" + errors + "]");
							for (int i = 0; i < errors.size(); i++) {
								addActionError(errors.get(i));
							}
							result = "fail";
						}
					}else{
						result = "fail";
					}
				}else{
					addActionError("Report Type Missing.");
					result = "fail";
				}
				logger.debug("Result [" + result + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in ReportsGenerator ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			messages = null;
			reportGenerator = null;
		}

		return result;
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

	
	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}


