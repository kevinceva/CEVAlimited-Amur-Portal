package com.ceva.reports;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import org.apache.log4j.Logger;

public class HtmlReportExporterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger
			.getLogger(HtmlReportExporterServlet.class);

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.debug("Started");
		processRequest(request, response);
		logger.debug("Ended");
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = null;
		PrintWriter printWriter = null;
		//JRHtmlExporter htmlExporter = null;
		JasperPrint jasperPrint = null;
		try {
			session = request.getSession(false);

			printWriter = response.getWriter();
			jasperPrint = (JasperPrint) session.getAttribute("jasperPrintObj");
			logger.debug("JasperPrintObj :: " + jasperPrint);
			//htmlExporter = new JRHtmlExporter();
			response.setContentType("text/html");
			/*
			 * session.setAttribute( "net.sf.jasperreports.j2ee.jasper_print",
			 * jasperPrint);
			 */

			/*htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT,
					jasperPrint);
			htmlExporter.setParameter(JRExporterParameter.OUTPUT_WRITER,
					printWriter);
			htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
					"image?image=");
			htmlExporter.exportReport();*/
		} catch (Exception e) {
			logger.debug(" Exception Raised : " + e.getMessage());
			e.printStackTrace(printWriter);
		} finally {
			jasperPrint = null;
			session = null;
			if (printWriter != null) {
				printWriter.close();
			}
			printWriter = null;
			//htmlExporter = null;
		}
	}
}