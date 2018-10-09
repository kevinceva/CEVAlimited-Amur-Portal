package com.ceva.base.agencybanking.action;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.BulkBean;
import com.ceva.base.common.dao.BillerManagementDAO;
import com.ceva.base.common.dao.BulkDisbursmentDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BulkPaymentManagementAction extends ActionSupport implements
		ServletRequestAware, ModelDriven<BulkBean> {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(BulkPaymentManagementAction.class);

	private String result;
	private JSONObject requestJSON = null;
	private JSONObject responseJSON = null;
	private RequestDTO requestDTO = null;
	private ResponseDTO responseDTO = null;
	protected HttpServletRequest request;

	@Autowired
	private BulkBean bulkBean = null;
	private HttpSession session = null;

	public String CommonScreen() {
		logger.debug("Inside CommonScreen...");
		return SUCCESS;
	}

	public String billerInfoAck() {
		logger.debug("Inside BillerInfo Ack.");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new BillerManagementDAO();

			requestJSON.put("bulkBean", bulkBean);
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.insertBulkData(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BILLER_REL_INFO);
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
			logger.debug("Exception in Get BillerRelated Info ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String viewOrganizations() {
		logger.debug("Inside View Organizations...");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.viewOrganizations(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"ORG_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
				bulkBean.setResponseJSON(responseJSON);
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Get Organisation Details ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	// To get individual organization details... by passing organization id
	public String viewBulkOrgDetails() {
		logger.debug("Inside View Bulk Organizations...");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("orgId", bulkBean.getOrgId());

			logger.debug("Request JSON  [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);

			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.viewBulkOrgDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {

				bulkBean = (BulkBean) responseDTO.getData().get("ORG_DATA");

				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
				// bulkBean.setResponseJSON(responseJSON);
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Get Bulk Organisation Details ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String billerInformation() {
		logger.debug("Inside  BillerInformation....");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			// session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new BillerManagementDAO();
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.insertBulkData(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BILLER_REL_INFO);
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
			logger.debug("Exception in Get BillerRelated Info ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String editBulkOrgDetailsAck() {
		logger.debug("Inside  EditBulkOrgDetailsAck Info Ack.");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			session = ServletActionContext.getRequest().getSession();
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			billerDao = new BillerManagementDAO();

			requestJSON.put("bulkBean", bulkBean);
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestDTO.setRequestJSON(requestJSON);

			responseDTO = billerDao.editBulkOrgDetailsAck(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.BILLER_REL_INFO);
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
			logger.debug("Exception in Get BillerRelated Info ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String viewProfileDetails() {
		logger.debug("Inside View Profile Details...");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestJSON.put("id", bulkBean.getId());

			requestDTO.setRequestJSON(requestJSON);

			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.viewProfileDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				bulkBean = (BulkBean) responseDTO.getData().get("PRF_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
				bulkBean.setResponseJSON(responseJSON);
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in View Profile Details [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String editProfileDetails() {
		logger.debug("Inside Edit Profile Details...");
		ArrayList<String> errors = null;
		BillerManagementDAO billerDao = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put("id", bulkBean.getId());

			requestDTO.setRequestJSON(requestJSON);

			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.viewProfileDetails(requestDTO);

			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				bulkBean = (BulkBean) responseDTO.getData().get("PRF_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
				bulkBean.setResponseJSON(responseJSON);
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in Profile Details [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
		}
		return result;
	}

	public String profilePinReset() {
		logger.debug("Inside Pin Reset .. ");
		BillerManagementDAO billerDao = null;
		try {
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put("id", bulkBean.getId());
			requestJSON.put("makerId",
					session.getAttribute(CevaCommonConstants.MAKER_ID));

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			billerDao = new BillerManagementDAO();
			responseDTO = billerDao.profilePinReset(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DATA);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {

				responseDTO = billerDao.viewOrganizations(requestDTO);

				logger.debug("Response DTO [" + responseDTO + "]");
				responseJSON = (JSONObject) responseDTO.getData().get(
						"ORG_DATA");
				logger.debug("Response JSON [" + responseJSON + "]");
				bulkBean.setResponseJSON(responseJSON);

				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			responseJSON.put("error_flag", "error");
			logger.debug("Exception in Pin Reset [" + e.getMessage() + "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			billerDao = null;
		}

		return result;
	}

	public String listBulkUploadFiles() {
		logger.debug("Inside Bulk Upload Files ..... ");
		BulkDisbursmentDAO billerDao = null;
		ArrayList<String> errors = null;
		try {
			requestDTO = new RequestDTO();
			requestDTO.setRequestJSON(new JSONObject());

			logger.debug("Request DTO [" + requestDTO + "]");
			billerDao = new BulkDisbursmentDAO();
			responseDTO = billerDao.viewBulkWithdrawals(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				bulkBean = (BulkBean) responseDTO.getData().get("bulk_data");

				logger.debug("Response JSON [" + bulkBean.getResponseJSON()
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
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			logger.debug("Exception in Pin Reset [" + e.getMessage() + "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			billerDao = null;
		}

		return result;
	}

	public String viewIndividualBulkUploadData() {
		logger.debug("Inside Bulk Upload Files ..... ");
		BulkDisbursmentDAO billerDao = null;
		ArrayList<String> errors = null;
		try {
			requestDTO = new RequestDTO();
			requestJSON = new JSONObject();

			requestJSON.put("id", bulkBean.getRefNo());

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			billerDao = new BulkDisbursmentDAO();
			responseDTO = billerDao.viewIndividualBulkUploadData(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				bulkBean = (BulkBean) responseDTO.getData().get("bulk_data");
				
				logger.debug("Response JSON [" + bulkBean.getResponseJSON()
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
			result = "fail";
			addActionError("Internal Error Occured, Please try again.");
			logger.debug("Exception in Pin Reset [" + e.getMessage() + "]");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			billerDao = null;
		}

		return result;
	}

	private JSONObject constructToResponseJson(HttpServletRequest httpRequest) {

		JSONObject jsonObject = null;
		Enumeration<?> enumParams = null;
		logger.debug("Inside ConstructToResponseJson... ");
		String key = "";
		String val = "";

		try {
			enumParams = httpRequest.getParameterNames();
			jsonObject = new JSONObject();
			while (enumParams.hasMoreElements()) {
				key = (String) enumParams.nextElement();
				val = httpRequest.getParameter(key);
				jsonObject.put(key, val);
			}
		} catch (Exception e) {
			logger.debug("Exception while converting to httpreq to bean["
					+ e.getMessage() + "]");

		} finally {
			enumParams = null;
			key = null;
			val = null;
		}

		return jsonObject;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public BulkBean getBulkBean() {
		return bulkBean;
	}

	public void setBulkBean(BulkBean bulkBean) {
		this.bulkBean = bulkBean;
	}

	@Override
	public BulkBean getModel() {
		return bulkBean;
	}

}
