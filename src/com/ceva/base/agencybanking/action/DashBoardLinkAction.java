package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.DashboardBean;
import com.ceva.base.common.dao.DashBoardLinkDAO;
import com.ceva.base.common.dao.OrdersDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class DashBoardLinkAction extends ActionSupport implements
		ModelDriven<DashboardBean> {

	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(DashBoardLinkAction.class);

	private String result;

	@Autowired
	private DashboardBean commonBean = null;

	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;

	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;

	private HttpSession session = null;


	public String getCommonScreen() {
		logger.debug("Inside GetCommonScreen... ");
		return SUCCESS;
	}

	public String getGroupDashBoard() {
		logger.debug("Inside GetGroupDashBoard..... ");
		DashBoardLinkDAO DashDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.ROLE_GRP_ID,
					commonBean.getRoleGroupId() == null ? " "
							: commonBean.getRoleGroupId());
			requestJSON.put(
					"GROUP_ID",
					commonBean.getGroupId() == null ? " " : commonBean
							.getGroupId());
			requestJSON.put("USER_ID", commonBean.getUserId() == null ? " "
					: commonBean.getUserId());

			logger.debug("Role Id [" + commonBean.getRoleGroupId() + "]");

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request JSON [" + requestJSON + "]");
			DashDAO = new DashBoardLinkDAO();
			responseDTO = DashDAO.getDashBoardLinks(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
				logger.debug("Response JSON [" + responseJSON + "]");

				commonBean.setResponseJSON(responseJSON);
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getGroupDashBoard [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			DashDAO = null;
			errors = null;
		}

		return result;
	}

	public String insertDashBoardLinks() {
		logger.debug("Inside InsertDashBoardLinks. ");
		DashBoardLinkDAO DashDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession(false);

			String makerId = session.getAttribute("makerId").toString();
			requestJSON.put(CevaCommonConstants.ROLE_GRP_ID,
					commonBean.getGroupId());
			requestJSON.put("user_id", commonBean.getUserId());
			requestJSON.put(CevaCommonConstants.TXN_DATA,
					commonBean.getSelectUsers());
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					makerId == null ? "NO_VAL" : makerId);

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request JSON [" + requestJSON + "]");
			DashDAO = new DashBoardLinkDAO();
			responseDTO = DashDAO.insertDashBoardLinks(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.RESPONSE_JSON);
				logger.debug("Response JSON [" + responseJSON + "]");
				commonBean.setResponseJSON(responseJSON);
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertDashBoardLinks [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			DashDAO = null;
			errors = null;
		}

		return result;
	}

	public String getAssinedDashLinks() {

		logger.debug("Inside GetAssinedDashLinks. ");
		DashBoardLinkDAO DashDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();

			requestJSON.put(CevaCommonConstants.ROLE_GRP_ID,
					commonBean.getRoleGroupId() == null ? " "
							: commonBean.getRoleGroupId());

			requestJSON.put("loc_name", session.getAttribute("location"));

			requestJSON.put(CevaCommonConstants.MAKER_ID, session
					.getAttribute(CevaCommonConstants.MAKER_ID) == null ? " "
					: session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request JSON In InsertDashBoardLinks [" + requestJSON
					+ "]");

			DashDAO = new DashBoardLinkDAO();
			responseDTO = DashDAO.getAssinedDashLinks(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.DASHBOARD_LINKS);
				commonBean.setResponseJSON(responseJSON);

				commonBean.setResponseJSON1((JSONObject) responseDTO
						.getData().get(CevaCommonConstants.HALF_PAGE));

				logger.debug("Response JSON ["
						+ commonBean.getResponseJSON() + "]");
				logger.debug("Response JSON1 ["
						+ commonBean.getResponseJSON1() + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetAssinedDashLinks [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			DashDAO = null;
			errors = null;
		}

		return result;
	}
	
	public String dashboardData() {

		System.out.println("Inside DashboardLinkAction dashboardData");
		OrdersDAO orderDetailsDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			orderDetailsDAO = new OrdersDAO();
			responseDTO = orderDetailsDAO.fetchAllOrders(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("ORDERS");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [DashboardLinkAction][dashboardData] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			orderDetailsDAO = null;
		}

		return result;
	}
	
	public String fetchPendingOrders() {

		System.out.println("Inside OrderAction fetchPendingOrders");
		OrdersDAO orderDetailsDAO = null;
		ArrayList<String> errors = null;
		try {

			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			orderDetailsDAO = new OrdersDAO();
			responseDTO = orderDetailsDAO.fetchUnassignedOrders(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get("NEW_ORDERS");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in [OrderAction][fetchOrdersDetails] [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;

			errors = null;
			orderDetailsDAO = null;
		}

		return result;
	}

	public String getUserAssinedDashLinks() {
		logger.debug("Inside GetUserAssinedDashLinks. ");
		DashBoardLinkDAO DashDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession(false);

			requestJSON.put(CevaCommonConstants.ROLE_GRP_ID,
					session.getAttribute("userGroup"));

			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute("makerId"));
			
			requestJSON.put(CevaCommonConstants.USER_LEVEL, session
					.getAttribute("userLevel").toString() == null ? " "
					: session.getAttribute("userLevel").toString());

			requestJSON.put("loc_name", session.getAttribute("location"));

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request JSON [" + requestJSON + "]");
			DashDAO = new DashBoardLinkDAO();
			responseDTO = DashDAO.getAssinedDashLinks(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {

				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.DASHBOARD_LINKS);

				commonBean.setResponseJSON(responseJSON);
				/*commonBean.setResponseJSON1((JSONObject) responseDTO
						.getData().get(CevaCommonConstants.HALF_PAGE));*/

				logger.debug("Response JSON ["
						+ commonBean.getResponseJSON() + "]");
				/*logger.debug("Response JSON1 ["
						+ commonBean.getResponseJSON1() + "]");*/
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in GetUserAssinedDashLinks ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			DashDAO = null;
			errors = null;
		}

		return result;
	}


	public DashboardBean getDashboardBean() {
		return commonBean;
	}

	public void setDashboardBean(DashboardBean commonBean) {
		this.commonBean = commonBean;
	} 
	 
	@Override
	public DashboardBean getModel() {
		return commonBean;
	}
}
