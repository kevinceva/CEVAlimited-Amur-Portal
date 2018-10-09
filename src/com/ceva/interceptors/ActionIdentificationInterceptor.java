package com.ceva.interceptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ActionIdentificationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;
	private HttpServletRequest request;
	private HttpSession session;

	private Logger log = Logger.getLogger(getClass());
	private String result = null;
	private String insQry = "";

	private String actionName = "";
	private String className = "";
	private String ipAddress = "";
	private String userid = "";

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		PreparedStatement preparedStatement = null;
		Connection connection = null;

		insQry = "insert into AUDIT_TRAIL(TRANS_CODE,DATETIME,NET_ID,CHANNEL_ID,DATA_1,TRANS_CODE_DESC,MAKER_ID,DATA_2)"
				+ " values (?,?,?,?,?,?,?,?)";

		try {

			session = ServletActionContext.getRequest().getSession();
			request = ServletActionContext.getRequest();

			log.debug("|ActionIdentificationInterceptor| Client Address     : "
					+ request.getRemoteAddr());
			log.debug("|ActionIdentificationInterceptor| Character Encoding : "
					+ request.getCharacterEncoding());
			log.debug("|ActionIdentificationInterceptor| Content Length     : "
					+ request.getContentLength());

			actionName = invocation.getProxy().getActionName();
			className = invocation.getAction().getClass().getName();
			ipAddress = request.getRemoteHost();
			userid = (String) session.getAttribute("makerId");
			/*log.debug("=====================================connection status============================================");
			log.debug("Total Created Connections :"+ DBConnector.getConnectionPool().getTotalCreatedConnections());
			log.debug("Total Free    Connections :"+ DBConnector.getConnectionPool().getTotalFree());
			log.debug("Total Leased  Connections :"+DBConnector.getConnectionPool().getTotalLeased());
			log.debug("Statement Execution time :"+DBConnector.getConnectionPool().getStatistics().getStatementExecuteTimeAvg());
			log.debug("Total Statements  Executed :"+DBConnector.getConnectionPool().getStatistics().getStatementsExecuted());
			log.debug("Total Statements  Prepared :"+DBConnector.getConnectionPool().getStatistics().getStatementsPrepared());
			log.debug("Total connections requested :"+DBConnector.getConnectionPool().getStatistics().getConnectionsRequested());
			log.debug("=====================================connection status close============================================");*/
			if (!"livedata".contains(actionName)) {
				connection = connection ==null ?connection = DBConnector.getConnection():connection;

				preparedStatement = connection.prepareStatement(insQry);
				preparedStatement.setString(1, actionName);
				preparedStatement.setTimestamp(2, new Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(3, userid);
				preparedStatement.setString(4, "WEB");
				preparedStatement.setString(5, ipAddress);
				preparedStatement.setString(6, className);
				preparedStatement.setString(7, userid);
				preparedStatement.setString(8, " ");

				int i = preparedStatement.executeUpdate();
				if (i == 1) {
					log.debug("Action Identification Interceptor -> Successfully Inserted "
							+ ipAddress);
					connection.commit();
				} else {
					log.debug("Action Identification Interceptor -> insertion failed due to some error "
							+ ipAddress);
				}
			} else {
				log.debug("Skipping " + actionName + " insertion to db..."
						+ ipAddress);
			}

		} catch (SQLException e) {
			log.debug(" |ActionIdentificationInterceptor| Interceptor got SQLException ::: "
					+ e.getMessage());
			try {
				connection.rollback();
			} catch (Exception e2) {
				log.debug("|ActionIdentificationInterceptor| Interceptor got SQLException2 ::: "
						+ e2.getMessage());
			}
			e.printStackTrace();
		} catch (Exception e) {
			log.debug("|ActionIdentificationInterceptor| Interceptor got Exception ::: "
					+ e.getMessage());
			try {
				connection.rollback();
			} catch (Exception e3) {
				log.debug("|ActionIdentificationInterceptor| Interceptor got SQLException3 ::: "
						+ e3.getMessage());
			}
			e.printStackTrace();
		} finally {
			insQry = null;
			DBUtils.closePreparedStatement(preparedStatement);
			DBUtils.closeConnection(connection);
			
			//to clean memory in heap space
			long minRunningMemory = (128*128);

			Runtime runtime = Runtime.getRuntime();

			//log.debug("Free Memory in runtime::"+runtime.freeMemory()+"----"+minRunningMemory);
			
			//if(runtime.freeMemory()<minRunningMemory)
			 //System.gc();
			
			 /* Total number of processors or cores available to the JVM */
			/*log.debug("Available processors (cores): " + 
			  Runtime.getRuntime().availableProcessors());*/

			  /* Total amount of free memory available to the JVM */
			/* log.debug("Free memory (bytes): " +  Runtime.getRuntime().freeMemory());*/

			  /* This will return Long.MAX_VALUE if there is no preset limit */
			  /*long maxMemory = Runtime.getRuntime().maxMemory();*/
			  /* Maximum amount of memory the JVM will attempt to use */
			 /* log.debug("Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));*/

			  /* Total memory currently in use by the JVM */
			  log.debug("Total memory used by JVM (bytes): " +  Runtime.getRuntime().totalMemory());
			  System.gc();
			log.debug("After clean Free Memory in runtime::"+runtime.freeMemory());
		}
		return invocation.invoke();
	}

}
