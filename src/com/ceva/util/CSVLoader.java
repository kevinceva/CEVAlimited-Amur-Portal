package com.ceva.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.ceva.base.common.utils.DBConnector;

/**
 * 
 * @author viralpatel.net
 * 
 */
public class CSVLoader {
	Logger logger = Logger.getLogger(CSVLoader.class);
	private static final String SQL_INSERT = "INSERT INTO ${table}(${keys}) VALUES(${values})";
	String SQL_INSERT_FAILED = "INSERT INTO ${table}(${keys}) VALUES(${values})";
	private static final String TABLE_REGEX = "\\$\\{table\\}";
	private static final String KEYS_REGEX = "\\$\\{keys\\}";
	private static final String VALUES_REGEX = "\\$\\{values\\}";

	List<String[]> fileLines = new ArrayList<String[]>();
	List<Integer> failedRecords = new ArrayList<Integer>();
	private Connection connection;
	private char seprator;
	Statement stmt = null;
	ResultSet rs = null;
	String statusToBeUpdated = "U";
	Connection con = null;
	PreparedStatement ps = null;
	int[] resp = null;

	public CSVLoader(Connection connection) {
		this.connection = connection;
		this.seprator = ',';
	}

	public String loadCSV(String csvFile, String tableName,
			boolean truncateBeforeLoad, String id) throws Exception {

		CSVReader csvReader = null;
		if (null == this.connection) {
			throw new Exception("Not a valid connection.");
		}
		try {

			csvReader = new CSVReader(new FileReader(csvFile), this.seprator);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error occured while executing file. "
					+ e.getMessage());
		}

		String[] headerRow = csvReader.readNext();

		if (null == headerRow) {
			throw new FileNotFoundException(
					"No columns defined in given CSV file."
							+ "Please check the CSV file format.");
		}

		String questionmarks = StringUtils.repeat("?,", headerRow.length);
		questionmarks = (String) questionmarks.subSequence(0,
				questionmarks.length() - 1);

		String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
		query = query.replaceFirst(KEYS_REGEX, StringUtils.join(headerRow, ",")
				+ ",FILE_ID");
		query = query.replaceFirst(VALUES_REGEX, questionmarks + ",?");
		logger.debug("Query: " + query);

		String[] nextLine;
		List<String[]> lines = new ArrayList<String[]>();
		try {
			con = DBConnector.getConnection();
			// con.setAutoCommit(false);
			ps = con.prepareStatement(query);

			if (truncateBeforeLoad) {
				// delete data from table before loading csv
				con.createStatement().execute("DELETE FROM " + tableName);
			}

			final int batchSize = 1000;
			int count = 0;
			Date date = null;
			while ((nextLine = csvReader.readNext()) != null) {
				lines.add(nextLine);
				if (null != nextLine) {
					int index = 1;
					for (String string : nextLine) {
						date = DateUtil.convertToDate(string);
						if (null != date) {
							ps.setDate(index++,
									new java.sql.Date(date.getTime()));
						} else {
							ps.setString(index++, string);
						}
					}
					ps.setString(headerRow.length + 1, id);
					ps.addBatch();
				}
				if (++count % batchSize == 0) {
					ps.executeBatch();
				}
			}
			resp = ps.executeBatch();
			con.commit();
		} catch (BatchUpdateException bue) {
			int stmtups = ps.getUpdateCount() - 1;
			int[] res = bue.getUpdateCounts();
			logger.debug("res..:" + res.length + ":..stmtups..:" + stmtups
					+ ":..lines..:" + lines.size());
			while (stmtups >= 0) {
				lines.remove(stmtups);
				stmtups--;
			}
			if (lines.size() > 0) {
				insertFailedReords(lines, id);
				statusToBeUpdated = "E";
			}

		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
			throw new Exception(
					"Error occured while loading data from file to database."
							+ e.getMessage());
		} finally {
			if (null != ps)
				ps.close();
			if (null != con)
				con.close();

			csvReader.close();
		}
		return statusToBeUpdated;
	}

	public char getSeprator() {
		return seprator;
	}

	public void setSeprator(char seprator) {
		this.seprator = seprator;
	}

	public void insertFailedReords(List<String[]> fileLines, String id) {
		String query = SQL_INSERT.replaceFirst(TABLE_REGEX, "FILE_DATA_FAIL");
		query = query.replaceFirst(KEYS_REGEX,
				"FILE_ID, LINE_ID, LINE_DATA_FILE");
		query = query.replaceFirst(VALUES_REGEX, "?,?,?");

		logger.debug("Query: " + query);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DBConnector.getConnection();
			ps = con.prepareStatement(query);
			for (int num = 0; num < fileLines.size(); num++) {
				logger.debug(fileLines.get(num));
				String line = Arrays.toString(fileLines.get(num));
				line = line.substring(1, line.length() - 1);
				ps.setString(1, id);
				ps.setInt(2, num);
				ps.setString(3, line);
				ps.addBatch();
			}
			ps.executeBatch();
			con.commit();
		} catch (Exception ex) {
			logger.debug("error..!" + ex.getMessage());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (Exception ex) {
			}
		}
	}

	public String loadArrayToTable(String[] rows, String fileLocation,
			String fileId) {
		String message = "";
		List<String> lines = Arrays.asList(rows);
		try {
			CSVReader csvReader = null;
			if (null == this.connection) {
				throw new Exception("Not a valid connection.");
			}
			con = this.connection;
			csvReader = new CSVReader(new FileReader(fileLocation),
					this.seprator);
			String[] headerRow = csvReader.readNext();

			if (null == headerRow) {
				throw new FileNotFoundException(
						"No columns defined in given CSV file."
								+ "Please check the CSV file format.");
			}

			String questionmarks = StringUtils.repeat("?,", headerRow.length);
			questionmarks = (String) questionmarks.subSequence(0,
					questionmarks.length() - 1);

			String query = SQL_INSERT.replaceFirst(TABLE_REGEX,
					"FILE_DATA_SUCCESS");
			query = query.replaceFirst(KEYS_REGEX,
					StringUtils.join(headerRow, ",") + ",FILE_ID");
			query = query.replaceFirst(VALUES_REGEX, questionmarks + ",?");
			ps = con.prepareStatement(query);
			logger.debug("Query: " + query);
			int count = 0;
			Date date = null;
			for (String row : rows) {
				if (null != row) {
					int index = 1;
					String[] clos = row.split(",");
					for (String string : clos) {
						date = DateUtil.convertToDate(string);
						if (null != date) {
							ps.setDate(index++,
									new java.sql.Date(date.getTime()));
						} else {
							ps.setString(index++, string);
						}
					}
					ps.setString(headerRow.length + 1, fileId);
					ps.addBatch();
				}
			}
			resp = ps.executeBatch();
			if (resp.length == rows.length) {
				message = "Updated";
				statusToBeUpdated = "U";
			}
		} catch (BatchUpdateException bue) {

			int stmtups;
			try {
				stmtups = ps.getUpdateCount() - 1;
				int[] res = bue.getUpdateCounts();
				logger.debug("res..:" + res.length + ":..stmtups..:" + stmtups
						+ ":..lines..:" + lines.size());
				while (stmtups >= 0) {
					lines.remove(stmtups);
					stmtups--;
				}
				if (lines.size() > 0) {
					insertFailed(lines, fileId);
					statusToBeUpdated = "E";
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception ex) {
			message = ex.getLocalizedMessage();
		}
		return statusToBeUpdated;
	}

	private void insertFailed(List<String> lines, String fileId) {

		String query = SQL_INSERT.replaceFirst(TABLE_REGEX, "FILE_DATA_FAIL");
		query = query.replaceFirst(KEYS_REGEX,
				"FILE_ID, LINE_ID, LINE_DATA_FILE");
		query = query.replaceFirst(VALUES_REGEX, "?,?,?");

		logger.debug("Query: " + query);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = this.connection;
			ps = con.prepareStatement(query);
			for (int num = 0; num < lines.size(); num++) {
				logger.debug(fileLines.get(num));
				String line = lines.get(num);
				line = line.substring(1, line.length() - 1);
				ps.setString(1, fileId);
				ps.setInt(2, num);
				ps.setString(3, line);
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (Exception ex) {
			logger.debug("error..!" + ex.getMessage());
		}

	}

}
