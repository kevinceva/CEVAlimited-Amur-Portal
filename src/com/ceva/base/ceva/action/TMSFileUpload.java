package com.ceva.base.ceva.action;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.TMSDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class TMSFileUpload extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(TMSFileUpload.class);

	private File uploadDisb;
	private String uploadDisbContentType;
	private String uploadDisbFileName;

	private HttpSession session = null;
	private JSONObject requestJSON = null;
	private JSONObject responseJson = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;
	private TMSDAO tmsdao = null;
	
	public String commonScreen() {
		return SUCCESS;
	}

	public String execute() {

		
		ResourceBundle rb = ResourceBundle.getBundle("auth");
		
		File fileToCreate = null;
		String result = "fail";
		ArrayList<String> errors = null;

		try {
			String filePath = rb.getString("bulk.disb.filepath");
			logger.debug("Server path :::: " + filePath);
			logger.debug("File Name is  :::: " + uploadDisbFileName);

			fileToCreate = new File(filePath, this.uploadDisbFileName);

			FileUtils.copyFile(this.uploadDisb, fileToCreate);

			logger.debug("File successfully upload to server.");
			logger.debug("File uploading to app server.... starts");

			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();
			requestJSON.put("makerId",(String) session.getAttribute("makerId") == null ? "NO_VALUE": (String) session.getAttribute("makerId"));
			requestJSON.put("filename", fileToCreate.getAbsolutePath());

			requestDTO.setRequestJSON(requestJSON);

			
			tmsdao = new TMSDAO();
			responseDTO = tmsdao.insertTMSFileData(requestDTO);
			
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJson = (JSONObject) responseDTO.getData().get("UPLOAD_INFO");
				logger.debug("Response JSON [" + responseJson + "]");
				
				if("SUCCESS".equals(responseJson.getString("STATUS")))
						result = "success";
				else
						result = "fail";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			
			logger.debug("File uploading to app server.... ends");
			filePath = null;
		} catch (Exception e) {
			e.printStackTrace();
			addActionError(e.getMessage());
			return INPUT;
		} finally {

			try {
				FileUtils.copyFile(this.uploadDisb,
						new File(rb.getString("bulk.disb.filepath.backup")	+ this.uploadDisb.getName()));

				this.uploadDisb.delete();

			} catch (IOException e) {
				e.printStackTrace();
			}

			fileToCreate = null;
		}
		return result;
	}
	
	
	
	

	public File getUploadDisb() {
		return uploadDisb;
	}

	public void setUploadDisb(File uploadDisb) {
		this.uploadDisb = uploadDisb;
	}

	public String getUploadDisbContentType() {
		return uploadDisbContentType;
	}

	public void setUploadDisbContentType(String uploadDisbContentType) {
		this.uploadDisbContentType = uploadDisbContentType;
	}

	public String getUploadDisbFileName() {
		return uploadDisbFileName;
	}

	public void setUploadDisbFileName(String uploadDisbFileName) {
		this.uploadDisbFileName = uploadDisbFileName;
	}

	private String getConvertedFormat(String inPattern) {
		SimpleDateFormat informat = null;
		StringBuffer year = null;
		try {
			year = new StringBuffer(15);
			informat = new SimpleDateFormat(inPattern);
			year.append(informat.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		} finally {

		}

		return year.toString();
	}

	

}

