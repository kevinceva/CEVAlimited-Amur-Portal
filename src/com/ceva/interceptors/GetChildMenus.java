package com.ceva.interceptors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.utils.AesUtil;
import com.ceva.base.common.utils.DBConnector;
import com.ceva.util.CommonUtil;
import com.ceva.util.DBUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class GetChildMenus extends AbstractInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Object> parameters = null;
	private Logger logger = Logger.getLogger(GetChildMenus.class);

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {

		JSONObject jsonData = null;
		JSONArray userLinksJSONArray = null;
		PreparedStatement menuStmt = null;
		ResultSet menuRS = null;
		String userId = null;
		String query = null;
		String pid[] = null;
		String sessionid[] = null;
		String randomVal = null;
		String pidval = null;
		Connection connection = null;
		boolean exceptionFlag = false;
		HttpSession session1 = null;

		// Get the session
		Map<String, Object> session = ActionContext.getContext().getSession();
		session1 = ServletActionContext.getRequest().getSession(false);

		userId = (String) session.get("makerId");

		query = (String) session.get("links_pid_query");

		logger.debug("|GetChildMenus| userid[" + userId + "] query [" + query + "] ");

		// Get the request Parameters
		parameters = invocation.getInvocationContext().getParameters();
		logger.debug("|GetChildMenus| Request Paramerters are : " + parameters);

		boolean flag = parameters.containsKey("pid");

		logger.debug("|GetChildMenus| Request Paramerter  pid flag  [" + flag + "]");

		// If Flag is not true, pid will skip this page
		if (flag) {

			sessionid = (String[]) parameters.get("sessionid");

			pid = (String[]) parameters.get("pid");
			if (session.isEmpty()) {
				return "error";
			}
			logger.info("pid[0]=" + pid[0]);
			logger.info("Session no :: " + session1.getAttribute("session_refno").toString());

			if (pid[0].toString().indexOf(session1.getAttribute("session_refno").toString()) == 0) {

				pidval = pid[0].toString().substring(session1.getAttribute("session_refno").toString().length());
			} else {
				session1.removeAttribute("session_refno");
				return "hackspage";
			}

			session1.removeAttribute("session_refno");
			randomVal = CommonUtil.getRandomInteger();
			session1.setAttribute("session_refno", AesUtil.md5(randomVal));
			System.out.println("sessionid new ::: " + session1.getAttribute("session_refno"));

			session1.setAttribute("pid", pidval);

			logger.debug("|GetChildMenus| Request Paramerter  pid is  [" + pid[0] + "]");
			try {
				if (query != null) {
					jsonData = new JSONObject();
					userLinksJSONArray = new JSONArray();
					synchronized (this) {

						connection = DBConnector.getConnection();

						if (connection == null)
							return "loginRedirect";

						menuStmt = connection.prepareStatement(query);
						menuStmt.setString(1, pidval);
						menuStmt.setString(2, userId.toUpperCase());

						menuRS = menuStmt.executeQuery();

						while (menuRS.next()) {
							jsonData.put("name", menuRS.getString(1));
							jsonData.put("status", menuRS.getString(2));
							userLinksJSONArray.add(jsonData);
							jsonData.clear();
						}

						session.put("USER_LINKS", userLinksJSONArray);

					} // End of synchronized block
				} else {
					exceptionFlag = true;
				}

			} catch (Exception e) {
				logger.debug("|GetChildMenus|  got Exception    ::: " + e.getMessage());

				try {
					connection.rollback();
				} catch (Exception e2) {
				}

				exceptionFlag = true;
			} finally {
				DBUtils.closeResultSet(menuRS);
				DBUtils.closePreparedStatement(menuStmt);
				DBUtils.closeConnection(connection);

				if (exceptionFlag)
					return "loginRedirect";
			}
		} else {
			logger.debug("|GetChildMenus| 'pid' is not in the request.");
			logger.debug("|GetChildMenus| removing the sub-links for the user [" + userId + "]");
			// session.remove("USER_LINKS");
		}

		// logger.debug("|GetChildMenus| Before ending session params [" +
		// session+"]");

		return invocation.invoke();
	}

}
