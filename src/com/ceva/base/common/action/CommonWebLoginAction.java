package com.ceva.base.common.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.CommonWebLoginBean;
import com.ceva.base.common.dao.CommonLoginDAO;
import com.ceva.base.common.dao.DashBoardLinkDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.AesUtil;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.CommonUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CommonWebLoginAction extends ActionSupport implements
		ServletRequestAware, ServletContextAware, ModelDriven<CommonWebLoginBean> {

	private static final long serialVersionUID = 658735490187966840L;
	private Logger logger = Logger.getLogger(CommonWebLoginAction.class);

	private HttpServletRequest request = null;
	private String result = null;

	private JSONObject requestJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private HttpSession session = null;
	
	ServletContext context;

	@Autowired
	private CommonWebLoginBean commonBean = null;
	
	

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String webLogin() {
		logger.debug("Inside WebLogin User ID [" + commonBean.getUserid() + "]");
		CommonLoginDAO prepaidLoginDAO = null;
		String userStatus = "";
		ArrayList<String> errors = null;
		ResourceBundle rb = ResourceBundle.getBundle("pathinfo_config");
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			logger.debug("Encrypted Password ["
					+ commonBean.getEncryptPassword() + "]");

			if (commonBean.getUserid() == null) {
				addActionError("Please enter User Id.");
			} else if (commonBean.getPassword() == null) {
				addActionError("Please enter password.");
			} else {
				session = ServletActionContext.getRequest().getSession(false);

				commonBean.setApplName("AgencyBanking");

				logger.debug("Application Name [" + commonBean.getApplName()
						+ "]");

				commonBean.setRandomValue((String) session
						.getAttribute(CevaCommonConstants.RANDOM_VAL));
				commonBean.setRemoteIp(request.getRemoteHost());

				requestJSON.put(CevaCommonConstants.USER_ID,
						commonBean.getUserid());
				requestJSON.put(CevaCommonConstants.PASSWORD,
						commonBean.getEncryptPassword());
				requestJSON.put(CevaCommonConstants.APPL_NAME,
						commonBean.getApplName());
				requestJSON.put(CevaCommonConstants.RANDOM_VAL,
						commonBean.getRandomValue());
				
				requestJSON.put("SESSION_ID", session.getId());
				
				requestJSON.put("REMOTE_ADDR", commonBean.getRemoteIp());

				requestJSON.put("REMOTE_IP", commonBean.getRemoteIp());

				logger.debug("Request JSON [" + requestJSON + "]");
				requestDTO.setRequestJSON(requestJSON);
				prepaidLoginDAO = new CommonLoginDAO();
				responseDTO = prepaidLoginDAO.validatLogin(requestDTO);
				logger.debug("Response DTO[" + responseDTO);

				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					session.setAttribute(CevaCommonConstants.MAKER_ID,
							commonBean.getUserid());

					commonBean.setResponseJSON((JSONObject) responseDTO
							.getData().get(CevaCommonConstants.MENU_DATA));

					logger.debug("Response JSON is ["
							+ commonBean.getResponseJSON() + "]");
					userStatus = (String) responseDTO.getData().get(
							"userStatus");
					logger.debug("UserStatus [" + userStatus
							+ "] LoginEntity ["
							+ (String) responseDTO.getData().get("LoginEntity")
							+ "]");

					// The Below are the user details that we are carrying.
					session.setAttribute("username", (String) responseDTO
							.getData().get("userid"));
					session.setAttribute("loginEntity", (String) responseDTO
							.getData().get("LoginEntity"));
					session.setAttribute("userLevel", (String) responseDTO
							.getData().get("UserLevel"));
					session.setAttribute("location", (String) responseDTO
							.getData().get("Location"));
					session.setAttribute(
							"links_pid_query",
							(String) responseDTO.getData().get(
									"links_pid_query"));
					session.setAttribute("loginTime", (String) responseDTO
							.getData().get("LoginTime"));
					session.setAttribute("userGroup", (String) responseDTO
							.getData().get("userGroup"));

					session.setAttribute("sessionLoginTime", Calendar
							.getInstance().getTimeInMillis());

					session.setAttribute("sessionMaxAge",
							rb.getString("session.expiry.period"));
					
					session.setAttribute("pid","1");
					
					session.setAttribute("dayCount",  responseDTO
							.getData().get("dayCount"));
					
					session.setAttribute("daySale",  responseDTO
							.getData().get("daySale"));

					session.setAttribute("weekCount",  responseDTO
							.getData().get("weekCount"));
					
					session.setAttribute("weekSale",  responseDTO
							.getData().get("weekSale"));
					
					session.setAttribute("policyCount",  responseDTO
							.getData().get("policyCount"));
					
					session.setAttribute("totalSale",  responseDTO
							.getData().get("totalSale"));
					
					session.setAttribute("totalCashSale",  responseDTO
							.getData().get("totalCashSale"));
					
					session.setAttribute("ClnCount",  responseDTO
							.getData().get("ClnCount"));
					
					session.setAttribute("BenCount",  responseDTO
							.getData().get("BenCount"));
					
					session.setAttribute("AgntCnt",  responseDTO
							.getData().get("AgntCnt"));
					
					session.setAttribute("CustCnt",  responseDTO
							.getData().get("CustCnt"));					

					session.setAttribute("ClnFailCnt",  responseDTO
							.getData().get("ClnFailCnt"));

					session.setAttribute("DailyClnActvCnt",  responseDTO
							.getData().get("DailyClnActvCnt"));
					
					session.setAttribute("TotalClnCnt",  responseDTO
							.getData().get("TotalClnCnt"));	
					
					session.setAttribute("MulPolCnt",  responseDTO
							.getData().get("MulPolCnt"));	
					
					session.setAttribute("session_id",request.getSession().getId());
				     session.setAttribute("session_refno", AesUtil.md5(CommonUtil.getRandomInteger()));
				     context.setAttribute((String) responseDTO.getData().get("userid"),request.getSession().getId());
				     session.setAttribute("~userid~", (String) responseDTO.getData().get("userid"));
				     session.setAttribute("SALT", AesUtil.md5(CommonUtil.getRandomInteger()));
				     //request.getUserPrincipal();
					logger.debug("Response builded.");

					if (userStatus.equals("A")) {

						session.setAttribute("MENU_DATA",
								commonBean.getResponseJSON());
						commonBean
								.setRedirectPage("WEB-INF/jsp/commonLogin.jsp");
						result = "success";
					}
					if (userStatus.equals("F")) {
						commonBean
								.setRedirectPage("WEB-INF/jsp/changePassword.jsp");
						result = "firstlogin";
					}

				} else {
					logger.debug("Error from DB.");
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					commonBean.setRedirectPage("WEB-INF/jsp/login.jsp");
					result = "loginfail";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Got exception webLogin is [" + e.getMessage() + "]");
			commonBean.setRedirectPage("WEB-INF/jsp/login.jsp");
			result = "loginfail";
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			prepaidLoginDAO = null;
			errors = null;
		}
		return result;
	}

	public String changePassword() {
		boolean checkFlag = false;
		ArrayList<String> errors = null;
		ArrayList<String> messages = null;
		CommonLoginDAO prepaidLoginDAO = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			logger.debug("ChangePassword :: " + commonBean.getUserid() + "-"
					+ commonBean.getNewPassword());
			if (commonBean.getUserid() == null) {
				addActionError("User ID Missing.");
			} else if (commonBean.getNewPassword() == null) {
				addActionError("New Password Missing.");
			} else if (commonBean.getConfirmNewPassword() == null) {
				addActionError("Confirm Password Missing.");
			} else if (commonBean.getNewPassword().equals(
					commonBean.getConfirmNewPassword())) {
				session = ServletActionContext.getRequest().getSession(false);
				requestJSON.put(CevaCommonConstants.USER_ID,
						commonBean.getUserid());
				requestJSON.put(CevaCommonConstants.PASSWORD,
						commonBean.getNewPassword());
				requestJSON.put(CevaCommonConstants.APPL_NAME, session
						.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME));
				requestDTO.setRequestJSON(requestJSON);
				prepaidLoginDAO = new CommonLoginDAO();
				responseDTO = prepaidLoginDAO.changePassword(requestDTO);
				logger.debug("Response DTO [" + responseDTO + "]");
				if (responseDTO != null && responseDTO.getErrors().size() == 0) {
					logger.debug("Response DTO [" + responseDTO + "]");
					messages = (ArrayList<String>) responseDTO.getMessages();
					logger.debug("Messages [" + messages + "]");
					for (int i = 0; i < messages.size(); i++) {
						addActionMessage(messages.get(i));
					}
					commonBean
							.setRedirectPage("WEB-INF/jsp/ChangePasswordAck.jsp");
					result = "success";
				} else {
					logger.debug("Getting error from DB");
					errors = (ArrayList<String>) responseDTO.getErrors();
					logger.debug("Errors [" + errors + "]");
					for (int i = 0; i < errors.size(); i++) {
						addActionError(errors.get(i));
					}
					commonBean
							.setRedirectPage("WEB-INF/jsp/changePassword.jsp");
					result = "loginfail";
				}
				checkFlag = true;
			} else {
				addActionError("New Password and Confirm New Password Not Matching.");
			}

			if (!checkFlag) {
				result = "loginfail";
			}

		} catch (Exception e) {
			logger.debug("Got exception in changePassword [" + e.getMessage()
					+ "]");
			commonBean.setRedirectPage("WEB-INF/jsp/changePassword.jsp");
			result = "loginfail";
		} finally {
			prepaidLoginDAO = null;
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
		}

		return result;
	}

	public String redirectLogin1() {
		logger.debug("Inside RedirectLogin... ");
		DashBoardLinkDAO DashDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession(false);

			logger.debug("Session Maker Id ["
					+ session.getAttribute(CevaCommonConstants.MAKER_ID) + "]");

			requestJSON.put(CevaCommonConstants.ROLE_GRP_ID,
					session.getAttribute("userGroup"));

			requestJSON.put("loc_name", session.getAttribute("location"));

			requestJSON.put(CevaCommonConstants.MAKER_ID, session
					.getAttribute(CevaCommonConstants.MAKER_ID) == null ? " "
					: session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestJSON.put(CevaCommonConstants.USER_LEVEL, session
					.getAttribute("userLevel").toString() == null ? " "
					: session.getAttribute("userLevel").toString());

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request JSON In Redirect Login [" + requestJSON + "]");

			DashDAO = new DashBoardLinkDAO();
			responseDTO = DashDAO.getAssinedDashLinks(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {

				commonBean.setResponseJSON((JSONObject) responseDTO.getData()
						.get(CevaCommonConstants.DASHBOARD_LINKS));

				logger.debug("Response JSON [" + commonBean.getResponseJSON()
						+ "]");

				commonBean.setResponseJSON1((JSONObject) responseDTO.getData()
						.get(CevaCommonConstants.HALF_PAGE));

				logger.debug("Response JSON1 [" + commonBean.getResponseJSON1()
						+ "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			logger.debug("Got exception RedirectLogin [" + e.getMessage() + "]");
			commonBean.setRedirectPage("WEB-INF/jsp/changePassword.jsp");
			result = "loginfail";
		} finally {
			DashDAO = null;
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}

		return result;
	}

	public String redirectLogin() {
		logger.debug("Inside RedirectLogin... ");
		return SUCCESS;
	}

	public String webLogOut() {
		logger.debug("Inside WebLogOut... ");
		String applName = null;
		HttpServletResponse response = null;
		Cookie[] cookies = null;

		try {

			session = ServletActionContext.getRequest().getSession(false);
			response = ServletActionContext.getResponse();
			logger.debug("Old session [" + session + "]");
			if (session != null) {
				applName = (String) session
						.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME);
				cookies = ServletActionContext.getRequest().getCookies();

				try {
					session.invalidate();

					for (Cookie cookie : cookies) {
						cookie.setMaxAge(0);
						cookie.setValue(null);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
					
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setHeader("Expires", "0");
				} catch (Exception e) {
					logger.debug("Inside  session invaldiate check exception :: "
							+ e.getMessage());
				}

				session = ServletActionContext.getRequest().getSession(true);

				logger.debug("New session [" + session + "]");

				session.setAttribute(CevaCommonConstants.ACCESS_APPL_NAME,
						applName == null ? "AgencyBanking" : applName);
				
				session.setAttribute(CevaCommonConstants.RANDOM_VAL,
						CommonUtil.getRandomInteger());
			}
			result = SUCCESS;
		} catch (Exception e) {
			logger.debug("Got exception in webLogOut [ " + e.getMessage() + "]");
			result = "loginfail";
		} finally {
			applName = null;
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			cookies = null;
		}

		return result;
	}

	 public void setServletContext(ServletContext context) {
		  this.context = context;
		 }
	
	public CommonWebLoginBean getCommonWebLoginBean() {
		return commonBean;
	}

	public void setCommonWebLoginBean(CommonWebLoginBean commonBean) {
		this.commonBean = commonBean;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public CommonWebLoginBean getModel() {
		return commonBean;
	}
}
