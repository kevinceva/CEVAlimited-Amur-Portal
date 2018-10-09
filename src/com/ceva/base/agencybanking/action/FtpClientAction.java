package com.ceva.base.agencybanking.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ceva.agencybanking.usermgmt.FTPUpload;
import com.opensymphony.xwork2.ActionSupport;

public class FtpClientAction extends ActionSupport implements
		ServletRequestAware {

	private HttpServletRequest request;

	private String statusData;

	private Logger logger = Logger.getLogger(FtpClientAction.class);

	public String ftpData() {

		logger.debug("------- Inside FTP Settlement Data -------");

		net.sf.json.JSONObject jsonData = null;
		FTPUpload ftpupload = null;
		boolean status = false;

		String[] fileName = null;

		JSONParser jsonParser = null;
		InputStream inputStream = null;
		BufferedReader bufferReader = null;
		Object obj = null;
		JSONObject jsonObject = null;
		JSONObject ftpDetails = null;
		try {

			jsonData = constructToResponseJson(this.request);
			logger.debug("The JSONData is [" + jsonData + "]");

			try {
				fileName = jsonData.getString("filename").split("_");
			} catch (Exception e) {
				logger.debug("Exception while splitting to get the bankcode ["
						+ e.getMessage() + "]");
				fileName = new String[] { " " };
			}

			try {

				inputStream = FtpClientAction.class
						.getResourceAsStream("/resource/ftpdetails.json");
				bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				jsonParser = new JSONParser();
				obj = jsonParser.parse(bufferReader);
				jsonObject = (JSONObject) obj;
				ftpDetails = (JSONObject) jsonObject.get(fileName[0]);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			ftpupload = new FTPUpload();
			ftpupload.setServer(ftpDetails.get("ip").toString());
			ftpupload.setPort(Integer.parseInt(ftpDetails.get("port")
					.toString()));
			ftpupload.setUsername(ftpDetails.get("username").toString());
			ftpupload.setPassword(ftpDetails.get("password").toString());
			ftpupload.setLocalFilePath(ftpDetails.get("localpath").toString());

			try {
				status = ftpupload.ftpRemote(jsonData.getString("filename"));
			} catch (Exception e) {
				logger.debug("The Remote Server Upload Exception Is ["
						+ e.getMessage() + "]");
			}
			logger.debug("Local Path is ["
					+ ftpDetails.get("localpath").toString() + "]");
			logger.debug("Local Path is ["
					+ ftpDetails.get("localbackuppath").toString() + "]");
			logger.debug("The Remote Server Upload Status Is [" + status + "]");

			logger.debug("Bankcode splitted successfully..");

			File source = null;
			File dest = null;
			try {
				source = new File(ftpDetails.get("localpath").toString());

				if (!source.exists()) {
					source.mkdirs();
				}
				source.setReadable(true);
				source.setExecutable(true);
				source.setWritable(true);

				dest = new File(ftpDetails.get("localbackuppath").toString());

				if (!dest.exists()) {
					dest.mkdirs();
				}
				dest.setReadable(true);
				dest.setExecutable(true);
				dest.setWritable(true);

				FileUtils.copyDirectory(source, dest);
				logger.debug("Copied from source to destination successfully.");
				FileUtils.cleanDirectory(source);
				logger.debug("Source folder is successfully cleaned...");
			} catch (IOException e) {
				logger.debug("Problem While Cleaning File.");
			}

			logger.debug("Cleaning Done..");

		} catch (Exception e) {
			status = false;
			logger.debug("The Exception Occured Due To [" + e.getMessage()
					+ "]");
		} finally {
			if (status) {
				statusData = "SUCCESS";
			} else {
				statusData = "FAIL";
			}
		}
		return SUCCESS;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private net.sf.json.JSONObject constructToResponseJson(
			HttpServletRequest httpRequest) {
		Enumeration enumParams = httpRequest.getParameterNames();
		net.sf.json.JSONObject jsonObject = null;
		logger.debug(" Inside ConstructToResponseJson ...");
		try {
			jsonObject = new net.sf.json.JSONObject();
			while (enumParams.hasMoreElements()) {
				String key = (String) enumParams.nextElement();
				String val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}

		} catch (Exception e) {
			logger.debug(" Inside constructToResponseJson exception ["
					+ e.getMessage() + "]");

		}
		logger.debug(" JsonObject [" + jsonObject + "]");

		return jsonObject;
	}

	public String getStatusData() {
		return statusData;
	}

	public void setStatusData(String statusData) {
		this.statusData = statusData;
	}

}
