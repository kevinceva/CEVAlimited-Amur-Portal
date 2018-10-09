package com.ceva.base.common.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;

import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;

public class PaymentDAO {

	private Logger logger = Logger.getLogger(PaymentDAO.class);
	private ResponseDTO responseDTO = null;
	private JSONObject responseJSON = null;

	public ResponseDTO acknowledgeService(RequestDTO requestDTO) {
		JSONObject jsonReq = null;
		HashMap<String, Object> data = null;
		Connection con = null;
		CallableStatement callable = null;
		String message = "";
		String printMessage = "";

		String callingProcedure = "{call hudumaDataInsertion(?,?,?,?,?,?,?,?,?)}";

		try {
			jsonReq = requestDTO.getRequestJSON();

			con = con == null ? DBConnector.getConnection():con;
			data = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();

			logger.debug(" jsonReq [" + jsonReq + "]");

			logger.debug(" Before Calling Procedure...");

			callable = con.prepareCall(callingProcedure);
			callable.setString(1, jsonReq.getString("total_info"));
			callable.setString(2, jsonReq.getString("billing_info"));
			callable.setString(3, jsonReq.getString("service_info"));
			callable.setString(4, jsonReq.getString("method"));
			callable.setString(5, jsonReq.getString("user_id"));
			callable.registerOutParameter(6, Types.VARCHAR);
			callable.registerOutParameter(7, Types.VARCHAR);
			callable.registerOutParameter(8, Types.VARCHAR);
			callable.registerOutParameter(9, Types.VARCHAR);

			logger.debug(" Before executing Procedure...");

			boolean flag = callable.execute();

			logger.debug(" Prcedure Executed Successfully [" + flag + "]"
					+ " Error Message[" + callable.getString(8) + "]");

			logger.debug(" txn_ref_no [" + callable.getString(6) + "]");
			message = callable.getString(8);
			if (!message.equalsIgnoreCase("SUCCESS")) {
				responseDTO.addError(message);
			} else {
				printMessage = callable.getString(9);

				jsonReq.put("txn_ref_no", callable.getString(6));
				jsonReq.put("print_details", printMessage);

				data.put("print_details", printMessage);
				data.put("service_details", jsonReq);
				responseDTO.setData(data);
			}
		} catch (Exception e) {
			logger.debug(" execption [" + e.getMessage() + "]");

		}

		finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(con);
		}

		logger.debug(" Before executing data[" + data + "]");

		return responseDTO;
	}

	public ResponseDTO loadSerial(RequestDTO requestDTO) {
		JSONObject jsonReq = null;
		HashMap<String, Object> data = null;
		Connection con = null;
		CallableStatement callable = null;
		String message = "";

		String callingProcedure = "{call HudumaPkg.LOADSERIALDATA(?,?,?,?,?)}";

		try {

			jsonReq = requestDTO.getRequestJSON();
			con = con == null ? DBConnector.getConnection():con;
			data = new HashMap<String, Object>();
			responseDTO = new ResponseDTO();

			logger.debug("jsonReq [" + jsonReq + "]");

			logger.debug("Before Calling Procedure...");

			callable = con.prepareCall(callingProcedure);
			callable.setString(1, jsonReq.getString("user"));
			callable.setString(2, jsonReq.getString("service"));
			callable.setString(3, jsonReq.getString("serialnoFrom"));
			callable.setString(4, jsonReq.getString("user_id"));
			callable.registerOutParameter(5, Types.VARCHAR);

			logger.debug("Before executing Procedure...");

			callable.execute();
			message = callable.getString(5);
			logger.debug("message [" + message + "]");

			if (!message.contains("Successfully")) {
				responseDTO.addError(message);
			} else {
				responseDTO.addMessages(message);
			}

		} catch (Exception e) {
			logger.debug("execption [" + e.getMessage() + "]");

		}

		finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeConnection(con);
		}

		logger.debug("Before executing data[" + data + "]");

		return responseDTO;
	}

	public ResponseDTO loadSerialUsers(RequestDTO requestDTO) {
		HashMap<String, Object> data = null;
		Connection con = null;
		CallableStatement callable = null;
		String message = "";

		String callingProcedure = "{call HudumaPkg.LOADSERIALS(?,?)}";

		ResultSet rs = null;
		JSONObject jsonData = null;

		try {

			logger.debug("Before Calling Procedure.");

			responseJSON = new JSONObject();
			responseDTO = new ResponseDTO();

			con = con == null ? DBConnector.getConnection():con;
			data = new HashMap<String, Object>();
			jsonData = new JSONObject();

			callable = con.prepareCall(callingProcedure);
			callable.registerOutParameter(1, OracleTypes.CURSOR);
			callable.registerOutParameter(2, Types.VARCHAR);

			logger.debug("Before executing Procedure...");

			callable.execute();
			message = callable.getString(2);
			logger.debug("message [" + message + "]");

			if (!message.contains("Successfully")) {
				responseDTO.addError(message);
			} else {
				rs = (ResultSet) callable.getObject(1);

				while (rs.next()) {
					jsonData.put(rs.getString(1), rs.getString(1));
				}
			}

			responseJSON.put("USERS_DATA", jsonData);
			data.put("USERS_DATA", responseJSON);
			responseDTO.setData(data);
		} catch (Exception e) {
			logger.debug("execption [" + e.getMessage() + "]");
		} finally {
			DBUtils.closeCallableStatement(callable);
			DBUtils.closeResultSet(rs);
			DBUtils.closeConnection(con);
		}

		logger.debug("Before executing data[" + data + "]");

		return responseDTO;
	}

}
