package com.ceva.reports.fileserver.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.ceva.base.agencybanking.action.CevaGroupAction;
import com.ceva.reports.fileserver.beans.ReportDescriptor;

public class ReportServices {
	
	private Logger logger=Logger.getLogger(CevaGroupAction.class);
	
	private File base = null;
	private final static int bufferSize=65536;
	
	public static void main(String[] args) {
		System.out.println(new ReportServices().listPrepaidReports("C:/Users/Deeru/Desktop/Test//DAILY_ALL_POS_TRAN_RPT/20140411"));
	}

	public ReportDescriptor[] listPrepaidReports(String pathString) {
		ReportDescriptor[] reportDescriptor = null;
		File dateDir = null;
		try {
			dateDir = new File(pathString);
			reportDescriptor = new ReportDescriptor[0];
			if ((dateDir != null) && (dateDir.exists())
					&& (dateDir.isDirectory())) {
				reportDescriptor = getFiles(dateDir);
			} else {
				logger.debug("No Prepaid Reports for path :: " + pathString);
				reportDescriptor = null;
			}
		} catch (Exception e) {
			logger.debug("Exception in Listing Reports ::: " + e);
		}

		return reportDescriptor;
	}

	private ReportDescriptor[] getFiles(File path) throws IOException {
		ArrayList<ReportDescriptor> list = new ArrayList<ReportDescriptor>();
		File[] files = path.listFiles();
		ReportDescriptor rd = null;
		for (File file : files) {
			if (file.isFile()) {
				rd = new ReportDescriptor(); 
				rd.setActualCreationDate(new Date(file.lastModified()));
				rd.setFileame(file.getName());
 				rd.setUriString(path + "/" + file.getName());
				list.add(rd);
			}
		}
 		
		return  list.toArray(new ReportDescriptor[list.size()]);
	}

	public void processRequest(String reportspath) {
		String resourceURI = reportspath;
		File tmpFile = null;
		 
		try {
			tmpFile = new File(resourceURI);
			tmpFile = tmpFile.getCanonicalFile();

			logger.debug("File requested " + tmpFile + " exists ? "
					+ tmpFile.exists());
			if (!validateFile(tmpFile))
				logger.debug("Validate returned false");
			while (true) {

				logger.debug("Validate returned true");
				writeFileContent(tmpFile);
				return;
			}
		} catch (Exception e) {
			logger.debug("Exception " + e);
		} finally {
			logger.debug("in finally block");
		}
	}

	protected void writeFileContent(File file) throws IOException {
		OutputStream fileOut = null;
		InputStream fileIn = null;
		byte []buf = null;
		try {
			buf = new byte[bufferSize];
			fileOut  = new FileOutputStream(file);
			fileIn = new FileInputStream(file);
			int read=0;
			while ((read = fileIn.read(buf)) > 0) {
				fileOut.write(buf);
			}
		} finally {
			if(fileIn !=null) fileIn.close();
			if(fileOut !=null) fileOut.close();
		}
	}

	private boolean validateFile(File file) {
		logger.debug("Inside validateFile()");
		if ((file != null) && (file.exists()) && (file.isFile())) {
			while ((file.getParentFile() != null)
					&& (file.getParentFile().exists())) {
				if (file.getParentFile().equals(this.base))
					return true;
				file = file.getParentFile();
			}
		}
		return false;
	}

	private ReportDescriptor[] getEmptyList() throws IOException {
		return new ReportDescriptor[0];
	}
}