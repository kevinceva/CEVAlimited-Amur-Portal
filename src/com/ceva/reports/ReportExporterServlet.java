package com.ceva.reports;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ceva.crypto.utils.CryptoTool;

public class ReportExporterServlet extends HttpServlet {
	private static final long serialVersionUID = -975083151356153154L;
	protected static Logger logger = Logger
			.getLogger(ReportExporterServlet.class);

	protected void processRequest(HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		String resourceURI = "";
		String filename = "";

		byte[] byteData = null;

		FileInputStream fin = null;
		BufferedOutputStream pw = null;

		try {
			resourceURI = CryptoTool.decrypt(new String(CryptoTool
					.HexStringToByteArray(request.getParameter("param"))));
			logger.debug(" ResourceURI : " + resourceURI);

			fin = new FileInputStream(resourceURI);
			filename = resourceURI.substring(resourceURI
					.lastIndexOf(File.separator) + 1);
			logger.debug(" File Name : " + filename);
			response.setHeader("Content-Disposition", "attachment;filename="
					+ filename);
			pw = new BufferedOutputStream(response.getOutputStream());

			byteData = new byte[65536];
			for (int len = 0; (len = fin.read(byteData)) != -1;) {
				pw.write(byteData, 0, len);
			}
			fin.close();
			pw.flush();
			pw.close();
		} catch (Exception e) {
			logger.error(" Exception Raised : "
					+ e.getMessage());

		} finally {
			if (fin != null) {
				fin.close();
			}
			if (pw != null) {
				pw.close();
			}

			resourceURI = null;
			filename = null;

			byteData = null;
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.debug(" [doGet] Started");
		processRequest(request, response);
		logger.debug(" [doGet] Ended");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		logger.debug(" [doPost] Started");
		processRequest(request, response);
		logger.debug(" [doPost] Ended");
	}
}