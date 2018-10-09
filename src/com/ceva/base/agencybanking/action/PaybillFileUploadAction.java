package com.ceva.base.agencybanking.action;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import com.ceva.base.common.bean.BulkDisbBean;
import com.ceva.base.common.dao.BulkDisbursmentDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.opensymphony.xwork2.ActionSupport;

public class PaybillFileUploadAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(PaybillFileUploadAction.class);

	private File uploadDisb;
	private String organization;
	private String uploadDisbContentType;
	private String uploadDisbFileName;

	private HttpSession session = null;

	private JSONObject requestJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	public String commonScreen() {
		return SUCCESS;
	}

	public String execute() {

		List<String> data = null;
		List list = null;

		BulkDisbBean country = null;
		ResourceBundle rb = ResourceBundle.getBundle("auth");
		BulkDisbursmentDAO bulkDisbDao = null;
		File fileToCreate = null;
		ColumnPositionMappingStrategy strat = null;
		String[] columns = null;
		CsvToBean csv = null;
		CSVReader csvReader = null;
		String result = "fail";
		ArrayList<String> errors = null;

		try {
			String filePath = rb.getString("bulk.disb.filepath");
			logger.debug("Server path :::: " + filePath);
			logger.debug("File Name is  :::: " + uploadDisbFileName);

			if (uploadDisbFileName.endsWith(".csv")) {
				uploadDisbFileName = uploadDisbFileName.replaceAll(".csv", "_"
						+ getConvertedFormat("yyddMMHmmssSSS") + ".csv");
			} else if (uploadDisbFileName.endsWith(".xls")) {
				uploadDisbFileName = uploadDisbFileName.replaceAll(".xls", "_"
						+ getConvertedFormat("yyddMMHmmssSSS") + ".xls");

			} else if (uploadDisbFileName.endsWith(".xlsx")) {
				uploadDisbFileName = uploadDisbFileName.replaceAll(".xlsx", "_"
						+ getConvertedFormat("yyddMMHmmssSSS") + ".xlsx");
			}

			fileToCreate = new File(filePath, this.uploadDisbFileName);

			FileUtils.copyFile(this.uploadDisb, fileToCreate);

			logger.debug("File successfully upload to server.");
			logger.debug("File uploading to app server.... starts");

			strat = new ColumnPositionMappingStrategy();
			strat.setType(BulkDisbBean.class);
			columns = rb.getString("bulk.disb.header").split("\\,"); // the
																		// fields
																		// to
																		// bind
																		// do
																		// in
																		// your
																		// JavaBean
			strat.setColumnMapping(columns);
			csv = new CsvToBean();
			csvReader = new CSVReader(new FileReader(fileToCreate), ',');

			list = csv.parse(strat, csvReader);

			data = new Vector<String>();
			for (ListIterator<?> listItr = list.listIterator(); listItr
					.hasNext();) {
				country = (BulkDisbBean) listItr.next();

				data.add(country.toString());
			}
			logger.debug("country:::"+country.toString());
			logger.debug("The array string data is ::: " + data);
			logger.debug("The array string data is ::: " + data.size());
			session = ServletActionContext.getRequest().getSession();
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();
			requestJSON
					.put("makerId",
							(String) session.getAttribute("makerId") == null ? "NO_VALUE"
									: (String) session.getAttribute("makerId"));

			requestJSON.put("filename", fileToCreate.getAbsolutePath());
			requestJSON.put("org", organization);

			requestDTO.setRequestJSON(requestJSON);

			bulkDisbDao = new BulkDisbursmentDAO();
			responseDTO = bulkDisbDao.bulkDisbursement(data, requestDTO);

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				errors = (ArrayList<String>) responseDTO.getMessages();
				for (int i = 0; i < errors.size(); i++) {
					addActionMessage(errors.get(i));
				}
				result = "success";
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
						new File(rb.getString("bulk.disb.filepath.backup")
								+ this.uploadDisb.getName()));

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

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

}
