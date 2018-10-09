package com.ceva.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.jasper.JasperException;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.BoundSheetRecord;

import com.ceva.reports.constants.Constants;
import com.ceva.util.GenUtils;

public class ReportController {
	private static Logger logger = Logger.getLogger(ReportController.class);
	private String indvPaths = "";

	private ArrayList<String> indvReports = new ArrayList<String>();

	private JasperPrint jasperPrintObj = null;

	public JasperPrint getJasperPrintObj() {
		return jasperPrintObj;
	}

	public String generateReports(Connection con,
			Hashtable<String, String> reportData) throws Exception {
		logger.debug("GenerateReports Started .......");

		String rtnDestDir = "";
		String srcFileName = "";
		File fileExists = null;
		try {
			srcFileName = reportData.get("source")
					+ reportData.get("sourceFileName");
			indvPaths = reportData.get("outPath");
			createDirs(reportData.get("outPath"));

			fileExists = new File(srcFileName + ".jrxml");
			logger.debug("File : " + srcFileName + ".jrxml");

			if (fileExists.exists()) {
				callOutputFormats(con, reportData);
			} else {
				logger.debug(" JRXML File Not Found ");
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException ffe) {
			logger.debug(" JRXML File Not Found " + ffe.getMessage());
			throw ffe;
		} catch (Exception e) {
			logger.debug(" Exception Raised : " + e.getMessage());
			throw e;
		} finally {
			srcFileName = null;
			fileExists = null;
			logger.debug("Destination Path : " + indvPaths);
			rtnDestDir = indvPaths;
			logger.debug("Destination Folder : " + rtnDestDir);
			logger.debug(" Ended .......");
		}

		return rtnDestDir;
	}

	public void callOutputFormats(Connection conn,
			Hashtable<String, String> reportData) throws Exception {
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;

		String[] rptIndvFormat = null;
		String fileWithOutExt = "";
		String sourceFileName = "";
		String genFileName = "";
		String outPath = "";
		String srcFileName = "";
		boolean isErrorFlag = false;
		HashMap<String, Object> mapPrams = null;
		try {
			jasperReport = getCompiledReport(reportData.get("source")
					+ reportData.get("sourceFileName"));
			logger.debug(" COMPLETED COMPILATION OF REPORT");

			try {
				isErrorFlag = false;
				logger.debug(" STARGING FILLING OF REPORT");

				mapPrams = reportParameters(reportData.get("reportDate"),
						reportData.get("extraParams"),
						reportData.get("source"), reportData.get("subQuery"));

				logger.debug(" STARGING FILLING OF REPORT parmas are :::"
						+ mapPrams);

				if (conn != null) {
					logger.debug("  Calling JManager ..  ");
					logger.debug("  JasperReport 111 " + jasperReport);
					logger.debug("  JasperReport 222 " + jasperReport.getColumnCount());
					logger.debug("  JasperReport 333 " + jasperReport.getName());
					logger.debug("  JasperReport " + jasperReport +"   mapPrams::"+mapPrams);
					jasperPrint = JasperFillManager.fillReport(jasperReport,mapPrams, conn);

					logger.debug(" After setting the jasper print parmas are ::: \n"+ mapPrams);
					this.jasperPrintObj = jasperPrint;
					logger.debug(" jasperPrintObj : " + jasperPrintObj);
					logger.debug(" " + conn);
				} else {
					logger.debug(" Connection is null");
				}
				logger.debug(" END OF FILLING OF REPORT");
			} catch (Exception e) {
				isErrorFlag = true;
				e.printStackTrace();
				logger.error(" EXCEPTION ..." + e.getMessage());
			}
			if (isErrorFlag) {
				return;
			}

			rptIndvFormat = reportData.get("reportFormats").split(",");
			fileWithOutExt = "";
			sourceFileName = reportData.get("source")
					+ reportData.get("sourceFileName");
			genFileName = reportData.get("genFileName");
			outPath = reportData.get("outPath");
			srcFileName = getFileWithOutPath(sourceFileName);

			for (String reportFrmt : rptIndvFormat) {
				fileWithOutExt = getFileWithOutExt(sourceFileName);
				logger.debug(" INPUT FILE FORMAT [" + reportFrmt + "]");
				if (("." + reportFrmt).equalsIgnoreCase(".pdf")) {
					logger.debug(" Generating  " + fileWithOutExt + ".pdf");
					logger.debug("Starting Time to generate Report ["
							+ genFileName + ".pdf" + " [   "
							+ GenUtils.getCurrentTime());
					generatePDFOutput(jasperPrint, outPath, srcFileName,
							genFileName, mapPrams);
					logger.debug("Ending Time to generate Report ["
							+ genFileName + ".pdf" + " [   "
							+ GenUtils.getCurrentTime());
				} else if (("." + reportFrmt).equalsIgnoreCase(".xls")) {
					logger.debug(" Generating  " + fileWithOutExt + ".xls");

					logger.debug(" Parametes For XLS File is jasperPrint: "
							+ jasperPrint + "\noutPath: " + outPath
							+ "\ngenFileName: " + genFileName + "\n HashMap: "
							+ mapPrams);

					logger.debug("Starting Time to generate Report ["
							+ genFileName + ".xls" + "] [   "
							+ GenUtils.getCurrentTime() + "]");
					generateXLSOutput(jasperReport, outPath, srcFileName,
							genFileName, mapPrams, conn);
					logger.debug("Ending Time to generate Report ["
							+ genFileName + ".xls" + "] [   "
							+ GenUtils.getCurrentTime() + "]");
				} else if (("." + reportFrmt).equalsIgnoreCase(".csv")) {
					logger.debug(" Generating  " + fileWithOutExt + ".csv");

					logger.debug("Starting Time to generate Report ["
							+ genFileName + ".csv" + "] [   "
							+ GenUtils.getCurrentTime());
					generateCSVOutput(jasperPrint, outPath, srcFileName,
							genFileName, mapPrams);
					logger.debug("Ending Time to generate Report ["
							+ genFileName + ".csv" + "] [   "
							+ GenUtils.getCurrentTime());
				} else if (("." + reportFrmt).equalsIgnoreCase(".txt")) {
					logger.debug(" Generating  " + fileWithOutExt + ".txt");

					logger.debug("Starting Time to generate Report ["
							+ genFileName + ".txt" + "] [   "
							+ GenUtils.getCurrentTime());
					generateTextOutput(jasperPrint, outPath, srcFileName,
							genFileName, mapPrams);
					logger.debug("Ending Time to generate Report ["
							+ genFileName + ".txt" + "] [   "
							+ GenUtils.getCurrentTime());
				} else if (("." + reportFrmt).equalsIgnoreCase(".html")) {
					logger.debug(" Generating  " + fileWithOutExt + ".txt");
					logger.debug("Starting Time to generate Report ["
							+ genFileName + ".html" + "] [   "
							+ GenUtils.getCurrentTime());
					generateHTMLOutput(jasperPrint, outPath, srcFileName,
							genFileName, mapPrams);
					logger.debug("Ending Time to generate Report ["
							+ genFileName + ".html" + "] [   "
							+ GenUtils.getCurrentTime());
				} else {
					logger.debug(" INVALID OUTPUT FILE FORMAT [" + reportFrmt
							+ "]");
				}
			}
		} catch (JasperException je) {
			logger.error(" JASPER EXCEPTION ..." + je.getMessage());

		} catch (JRException jre) {
			logger.error(" JASPER EXCEPTION ..." + jre.getMessage());

		} catch (Exception e) {
			logger.error(" EXCEPTION ..." + e.getMessage());

		} finally {
			jasperReport = null;
			jasperPrint = null;
			rptIndvFormat = null;
			fileWithOutExt = "";
			sourceFileName = "";
			genFileName = "";
			outPath = "";
			srcFileName = "";
			isErrorFlag = false;
			mapPrams = null;
		}
	}

	public JasperReport getCompiledReport(String fileName) throws JRException,
			Exception {
		logger.debug(" ABOUT TO COMPILE THE FILE [" + fileName + "]");
		File reportFile = null;
		FileInputStream file = null;

		JasperReport jasperReport = null;
		try {

			fileName = getFileWithOutExt(fileName);

			reportFile = new File(fileName + ".jasper");
			System.out.println(" value of reportFile: "+reportFile);

			if (!reportFile.exists()) {
				System.out.println("report not exists: "+reportFile);
				JasperCompileManager.compileReportToFile(fileName + ".jrxml",
						fileName + ".jasper");
			}
			file = new FileInputStream(reportFile.getPath());
			jasperReport = (JasperReport) JRLoader.loadObject(file);
			logger.debug(" COMPLETION OF METHOD");
		} catch (JRException jre) {
			logger.error(" JREXCEPTION  " + jre.getMessage());
			throw jre;
		} catch (Exception e) {
			logger.error(" EXCEPTION  " + e.getMessage());
			throw e;
		} finally {
			reportFile = null;
			fileName = null;
			if (file != null) {
				file.close();
			}
		}
		return jasperReport;
	}

	public String getFileWithOutExt(String fileName) throws Exception {
		logger.debug(" ACTUAL FILE NAME [" + fileName + "]");
		String splittedFileName = fileName;
		String subFileName = fileName.substring(
				fileName.length()
						- (fileName.length() - fileName
								.lastIndexOf(Constants.SEPARATOR)) + 1,
				fileName.length());
		if (subFileName.contains(".")) {
			splittedFileName = fileName.substring(0, fileName.length()
					- (fileName.length() - fileName.lastIndexOf('.')));
		}
		logger.debug(" FILE NAME AFTER SPLITTING [" + splittedFileName + "]");
		return splittedFileName;
	}

	public HashMap<String, Object> reportParameters(String reportDate,
			String extraParams, String sourcePath, String subQuery)
			throws Exception {
		String[] extraParamsList = null;
		String[] fieldVals = null;

		String logoPath = "";
		//String subRepPath = "";

		HashMap<String, Object> reportParams = null;
		try {
			logoPath = sourcePath + "logo.jpg";
			logger.debug(" LOGO PATH is  :: " + logoPath);

			//subRepPath = sourcePath;
			logger.debug(" SubReport Path is " + sourcePath);

			reportParams = new HashMap<String, Object>();
			reportParams.put("REPORT_INPUT_DATE", reportDate);
			reportParams.put("logo", logoPath);
			reportParams.put("SUBREPORT_DIR", sourcePath);
			reportParams.put("SUBQUERY", subQuery);

			logger.debug(" ExtraParams [" + extraParams + "]");

			extraParamsList = extraParams.split("##");
			logger.debug(" No of Extra Parameters : " + extraParamsList.length);
			for (int i = 0; i < extraParamsList.length; i++) {
				fieldVals = extraParamsList[i].split("@@");

				if (fieldVals.length == 2) {
					logger.debug("Param ::: " + fieldVals[0] + "-"
							+ fieldVals[1]);
					reportParams.put(fieldVals[0], fieldVals[1]);
				} else {
					logger.debug("Invalid Field Found .... "
							+ extraParamsList[i]);
				}
			}
		} catch (Exception e) {
			logger.error(" EXCEPTION " + e.getMessage());
			e.printStackTrace();
		} finally {
			extraParamsList = null;
			fieldVals = null;
			logoPath = null;
			//subRepPath = null;
		}
		return reportParams;
	}

	public static String getDateFormat(String inpfrmt) throws Exception {
		SimpleDateFormat inputFormat = null;
		SimpleDateFormat outputFormat = null;
		try {
			inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
			outputFormat = new SimpleDateFormat("yyyyMMdd");
			return outputFormat.format(inputFormat.parse(inpfrmt));
		} catch (Exception e) {
			logger.error(" EXCEPTION " + e.getMessage());

		}
		return "NO";
	}

	private void createDirs(String path) throws Exception {
		File dir = null;
		try {
			dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
				logger.debug(" DIRECTORY [" + dir + "] HAS BEEN CREATED");
			} else {
				logger.debug(" DIRECTORY [" + dir + "] ALREADY EXISTS");
			}
		} catch (Exception e) {

			logger.error(" Unable create the Directory : " + e.getMessage());
		} finally {
			dir = null;
		}
	}

	public String getFileWithOutPath(String fileName) throws Exception {
		logger.debug(" ACTUAL FILE NAME [" + fileName + "]");
		String splittedFileName = null;
		splittedFileName = fileName.substring(
				fileName.length()
						- (fileName.length() - fileName.lastIndexOf('/')) + 1,
				fileName.length());
		logger.debug("  FILE NAME AFTER SPLITTING [" + splittedFileName + "]");
		return splittedFileName;
	}

	public void generatePDFOutput(JasperPrint jasperPrint,
			String destinationPath, String fileName, String genFileName,
			HashMap<String, Object> params) throws IOException, JRException,
			Exception {
		String fName = getFileWithOutPath(fileName);
		byte[] output = null;
		FileOutputStream outputStream = null;
		String strFileName = "";
		try {
			// String reportDate = (String)params.get("REPORT_INPUT_DATE");
			logger.debug(" GENERATING PDF ...");

			output = JasperExportManager.exportReportToPdf(jasperPrint);
			getFileWithOutExt(fName);

			strFileName = destinationPath + Constants.SEPARATOR + genFileName
					+ ".pdf";
			logger.debug(" PDF FILE [" + strFileName + "]");
			outputStream = new FileOutputStream(strFileName);
			outputStream.write(output, 0, output.length);
			outputStream.flush();
			outputStream.close();

			logger.debug(" PDF [" + strFileName + "] HAS BEEN GENERATED ");

			indvReports.add(strFileName);
		} catch (Exception e) {
			logger.error(" EXCEPTION ..." + e.getMessage());

			throw e;
		} finally {
			output = null;
			strFileName = null;
			jasperPrint = null;
			if (outputStream != null) {
				outputStream.close();
			}
			fName = null;
		}
	}

	@SuppressWarnings("deprecation")
	public void generateXLSOutput(JasperReport jasperReport,
			String destinationPath, String fileName, String genFileName,
			HashMap<String, Object> params, Connection conn) throws Exception {
		logger.debug(" GENERATING XLS ...");

		byte[] output = null;
		JRExporter exporter = null;
		BoundSheetRecord bsr = null;
		JasperPrint jasperPrint = null;
		OutputStream outputStream = null;
		String strFileName = "";
		try {
			exporter = new JRXlsExporter();
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE,
					Boolean.valueOf(true));
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
					Boolean.valueOf(true));
			exporter.setParameter(
					JRXlsAbstractExporterParameter.CHARACTER_ENCODING, "UTF-8");
			exporter.setParameter(
					JRXlsAbstractExporterParameter.MAXIMUM_ROWS_PER_SHEET,
					Constants.XLS_MAX_ROWS_COUNT);
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.valueOf(true));
			params.put("IS_IGNORE_PAGINATION",
					Constants.XLS_IS_IGNORE_PAGINATION);

			jasperPrint = JasperFillManager.fillReport(jasperReport, params,
					conn);
			logger.debug("  MAXIMUM_ROWS_PER_SHEET Applied with ["
					+ Constants.XLS_MAX_ROWS_COUNT + "]");

			bsr = new BoundSheetRecord();
			bsr.setSheetnameLength((byte) 100);

			output = exportReportToBytes(jasperPrint, exporter);
			logger.debug("  Object after exporttoBytes " + output);

			strFileName = destinationPath + Constants.SEPARATOR + genFileName
					+ ".xls";

			logger.debug(" XLS FILE [" + strFileName + "]");

			outputStream = new FileOutputStream(strFileName);
			outputStream.write(output, 0, output.length);
			outputStream.flush();
			outputStream.close();

			exporter.exportReport();

			logger.debug(" XLS [" + strFileName + "] HAS BEEN GENERATED");
			indvReports.add(strFileName);
		} catch (Exception e) {
			logger.error(" EXCEPTION " + e.getMessage());

			throw e;
		} finally {
			output = null;
			exporter = null;
			bsr = null;
			jasperPrint = null;
			if (outputStream != null) {
				outputStream.close();
			}
			strFileName = null;
		}
	}

	public byte[] exportReportToBytes(JasperPrint jasperPrint,
			JRExporter exporter) throws JRException, Exception {

		ByteArrayOutputStream baos = null;
		byte[] output = null;
		try {
			logger.debug(" MethodParameters are JasperPrint  " + jasperPrint
					+ "\nexporter " + exporter);
			baos = new ByteArrayOutputStream();
			logger.debug(" " + baos);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			output = baos.toByteArray();
			logger.debug("    " + output.length);
		} catch (Exception e) {
			logger.error(" EXCEPTION " + e.getMessage());
			throw e;
		} finally {
			exporter = null;
			if (baos != null) {
				baos.close();
			}
		}

		return output;
	}

	public void generateCSVOutput(JasperPrint jasperPrint,
			String destinationPath, String fileName, String genFileName,
			HashMap<String, Object> params) throws IOException, JRException,
			Exception {
		OutputStream csvOut = null;
		JRExporter exporter = null;
		String strFileName = "";
		try {
			logger.debug(" GENERATING CSV ...");
			strFileName = destinationPath + Constants.SEPARATOR + genFileName
					+ ".csv";

			logger.debug(" CSV FILE [" + strFileName + "]");

			csvOut = new FileOutputStream(strFileName);
			exporter = new JRCsvExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, strFileName);
			exporter.setParameter(JRCsvExporterParameter.CHARACTER_ENCODING,
					"UTF-8");
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, csvOut);
			exporter.exportReport();
			csvOut.close();

			logger.debug(" CSV [" + strFileName + "] HAS BEEN GENERATED");
			indvReports.add(strFileName);
		} catch (Exception e) {
			logger.debug(" EXCEPTION " + e.getMessage());

			throw e;
		} finally {
			exporter = null;
			if (csvOut != null) {
				csvOut.close();
			}
			strFileName = null;
		}
	}

	public void generateTextOutput(JasperPrint jasperPrint,
			String destinationPath, String fileName, String genFileName,
			HashMap<String, Object> params) throws IOException, JRException,
			Exception {
		OutputStream txtOut = null;
		JRExporter exporter = null;
		String strFileName = "";

		try {
			logger.debug("GENERATING text ...");

			strFileName = destinationPath + Constants.SEPARATOR + genFileName
					+ ".txt";

			logger.debug("Text FILE [" + strFileName + "]");

			txtOut = new FileOutputStream(strFileName);
			exporter = new JRTextExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, strFileName);
			exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH,
					new Integer(5));
			exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT,
					new Integer(10));
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, txtOut);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.exportReport();
			txtOut.close();

			logger.debug("TEXT [" + strFileName + "] HAS BEEN GENERATED");
			indvReports.add(strFileName);
		} catch (Exception e) {
			logger.debug("The exception is :" + e.getMessage());

			throw e;
		} finally {
			exporter = null;
			if (txtOut != null) {
				txtOut.close();
			}
			strFileName = null;
		}
	}

	public void generateHTMLOutput(JasperPrint jasperPrint,
			String destinationPath, String fileName, String genFileName,
			HashMap<String, Object> params) throws IOException, JRException,
			Exception {
		JRHtmlExporter htmlExporter = null;
		String rptFileName = "";
		String strFileName = "";
		try {
			logger.debug(" GENERATING HTML ...");
			rptFileName = genFileName + ".html";
			strFileName = destinationPath + Constants.SEPARATOR + rptFileName;

			logger.debug(" HTML FILE [" + strFileName + "]");

			htmlExporter = new JRHtmlExporter();
			htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT,
					jasperPrint);
			htmlExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					strFileName);

			htmlExporter.setParameter(
					JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
					Boolean.FALSE);
			htmlExporter.setParameter(
					JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.FALSE);
			htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
					"image?image=");

			htmlExporter.exportReport();

			logger.debug(" HTML [" + strFileName + "] HAS BEEN GENERATED");
			indvReports.add(strFileName);
		} catch (Exception e) {
			logger.debug(" EXCEPTION " + e.getMessage());

			throw e;
		} finally {
			htmlExporter = null;
			rptFileName = null;
			strFileName = null;
		}
	}
}