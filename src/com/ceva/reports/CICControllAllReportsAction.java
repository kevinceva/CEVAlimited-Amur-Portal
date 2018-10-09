package com.ceva.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.ceva.base.common.utils.DBConnector;
import com.ceva.reports.constants.Constants;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionSupport;

public class CICControllAllReportsAction extends ActionSupport implements
		ServletRequestAware {
	private static final long serialVersionUID = 223950660029122694L;
	private static Logger logger = Logger.getLogger(CICControllAllReportsAction.class);

	private HttpServletRequest request = null;
	private ResourceBundle bundle = null;

	public String generateInstantReports() {

		String dbSourcePath = null;
		String dbOutPath = null;
		String folderName = null;

		String reportDate = "";
		String sourceFileName = "";
		String genFileName = "";
		String reportFormats = "";
		String Query = "";
		String extraParams = "";

		String jsreportcode = "";
		String sysdate = "";
		String asondate = "";
		String querymode = "";
		String mode = "";
		String makerid = "";
		String subQuery = "";
		String xtrParam = "";
		String targetDir = "";
		String checkBrcStatus = "";
		String countQuery = "";

		HttpSession session = request.getSession();
		ReportController rh = null;

		JasperPrint jasperPrintObj = null;

		Hashtable<String, String> reportData = null;

		Connection con = null;
		int recCnt = 0;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			bundle = ResourceBundle.getBundle("auth");
			rh = new ReportController();
			logger.debug("************************************************************************");
			logger.debug("InstantReports Calling.");

			dbSourcePath = bundle.getString("ReportPath");
			dbOutPath = bundle.getString("ReportOutPath");

			con = DBConnector.getConnection();

			if (con == null) {
				logger.debug("Database Not Connected");
			} else {
				logger.debug("Database Connected");
			}

			Query = (String) session.getAttribute("Query") == null ? " "
					: (String) session.getAttribute("Query");

			try {
				countQuery = Query.toUpperCase();
				logger.debug("Checking countQuery is : " + countQuery);
				checkBrcStatus = (String) session.getAttribute("QryKey");

				logger.debug("Checking checkBrcStatus is : " + checkBrcStatus);

				if (bundle.getString(checkBrcStatus + "_BRC_CHK")
						.equalsIgnoreCase("Y")) {
					countQuery = "SELECT COUNT(*) "
							+ countQuery.substring(
									countQuery.indexOf(" $FROM$ "),
									countQuery.lastIndexOf(")"));
				} else {
					countQuery = "SELECT COUNT(*) "
							+ countQuery.substring(
									countQuery.indexOf(" $FROM$ "),
									countQuery.length());
				}

				countQuery = countQuery.replaceAll(" \\$FROM\\$ ", " FROM ");
				Query = Query.replaceAll(" \\$FROM\\$ ", " FROM ");

				stmt = con.createStatement();
				logger.debug("  Checking the count of the Dynamic Query : "
						+ countQuery);

				rs = stmt.executeQuery(countQuery);

				if (rs.next()) {
					recCnt = rs.getInt(1);
				}

				DBUtils.closeResultSet(rs);
				rs = null;
				DBUtils.closeStatement(stmt);
				stmt = null;
			} catch (Exception e) {
				logger.debug("Exception raised :: " + e.getMessage());
			}

			if (recCnt > 0) {
				reportData = new Hashtable<String, String>();

				jsreportcode = checkData((String) session
						.getAttribute("JRPTCODE"));
				sysdate = new SimpleDateFormat("dd-MMM-yyyy")
						.format(new Date());
				asondate = request.getParameter("RptDate") == null ? sysdate
						: request.getParameter("RptDate");
				querymode = checkData((String) session
						.getAttribute("querymode"));
				mode = checkData((String) session.getAttribute("MODE"));
				makerid = checkData((String) session.getAttribute("makerId"));
				subQuery = checkData((String) session.getAttribute("SUBQUERY"));
				xtrParam = checkData((String) session.getAttribute("xtrParam"));

				logger.debug("************************ From WEB Report************************");
				logger.debug("JasperReport Code : " + jsreportcode);
				logger.debug("Given Date        : " + asondate);
				logger.debug("Query Mode        : " + querymode);
				logger.debug("Mode              : " + mode);
				logger.debug("Maker ID          : " + makerid);
				logger.debug("Query             : " + Query);
				logger.debug("XtrParam          : " + xtrParam);

				reportDate = asondate;
				sourceFileName = jsreportcode;
				genFileName = jsreportcode + "-" + getDateFormat(reportDate);

				reportFormats = mode;

				extraParams = "QUERY@@" + Query;

				if (xtrParam.length() != 0) {
					extraParams += "##" + xtrParam;
				}

				logger.debug("***********************Required Parameters*******************");
				logger.debug("Report Date          : " + reportDate);
				logger.debug("Source File Name     : " + sourceFileName);
				logger.debug("Generating File Name : " + genFileName);
				logger.debug("Report Formats       : " + reportFormats);
				logger.debug("Extra Parameters     : " + extraParams);

				dbOutPath = dbOutPath + Constants.SEPARATOR + sourceFileName
						+ Constants.SEPARATOR
						+ ReportController.getDateFormat(reportDate);

				checkData(reportData.put("reportDate", reportDate));
				checkData(reportData.put("sourceFileName", sourceFileName));
				checkData(reportData.put("genFileName", genFileName));
				checkData(reportData.put("reportFormats", reportFormats));
				checkData(reportData.put("extraParams", extraParams));
				checkData(reportData.put("dbSourcePath", dbSourcePath));
				checkData(reportData.put("source", dbSourcePath));
				checkData(reportData.put("dbOutPath", dbOutPath));
				checkData(reportData.put("outPath", dbOutPath));
				checkData(reportData.put("subQuery", subQuery));

				targetDir = rh.generateReports(con, reportData);

				jasperPrintObj = rh.getJasperPrintObj();

				logger.debug("Report Handler Calling Completed");
				logger.debug("************************************************************************");

				folderName = new SimpleDateFormat("yyyyMMdd")
						.format(new SimpleDateFormat("dd-MMM-yyyy")
								.parse(reportDate));
				logger.debug("Folder Created is : " + targetDir);

				session.setAttribute("reportPath", targetDir);
				session.setAttribute("reqFromDate", folderName);
				session.setAttribute("jasperPrintObj", jasperPrintObj);

				return SUCCESS;
			} else {
				return "noRecords";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" EXCEPTION " + e.getMessage());
			return ERROR;
		} finally {

			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
			DBUtils.closeConnection(con);

			con = null;
			reportData = null;
			folderName = null;
			reportDate = null;
			sourceFileName = null;
			genFileName = null;
			reportFormats = null;
			Query = null;
			extraParams = null;

			jsreportcode = null;
			sysdate = null;
			asondate = null;
			querymode = null;
			mode = null;
			makerid = null;
			subQuery = null;
			xtrParam = null;
			rh = null;
			targetDir = null;
			jasperPrintObj = null;
			recCnt = 0;
			countQuery = null;
		}

	}

	private String checkData(String data) {
		return ((data == null || data.equalsIgnoreCase("")) ? " " : data);
	}

	private String getDateFormat(String inpfrmt) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			return outputFormat.format(inputFormat.parse(inpfrmt));
		} catch (Exception e) {
			logger.error("EXCEPTION in getDateFormat ::: " + e.getMessage());
		}
		return "";
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public static void main(String[] args) {
		String Query = "SELECT BANK,DECODE(TXN_DESC, 'CDP', 'CASH DEPOSIT', 'WDL', 'CASH WITHDRAWL', 'HDM', 'HUDUMA', 'BEQ', 'BALANCE INQUIRY', 'MST', 'MINI STATEMENT', 'FTR','FUND TRANSFER', TXN_DESC) TXN_DESC,CNT FROM (SELECT  (select BANK_NAME from bank_master where bin=LT.ACQUIRERBIN and rownum<2) BANK, LT.TXNTYPE TXN_DESC,  count(*) cnt FROM TBL_TRANLOG LT where @@CONDITION@@ and LT.RESPONSECODE='00' and LT.ISREVERSED='N' group by  LT.ACQUIRERBIN,LT.TXNTYPE) order by Bank";
		String countQuery = "";

		countQuery = Query.replaceAll("@@CONDITION@@", "LT.TXNTYPE in ('CDP')");
		System.out.println(countQuery);
		System.out.println("data)".lastIndexOf(")"));
	}

}