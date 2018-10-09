package com.ceva.base.agencybanking.action;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.ceva.base.common.dao.ServiceManagementDAO;
import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.opensymphony.xwork2.ActionSupport;

public class ServiceManagementAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger = Logger.getLogger(ServiceManagementAction.class);

	JSONObject requestJSON = null;
	JSONObject responseJSON = null;
	JSONObject subServiceJSON = null;
	JSONObject feeJSON = null;

	RequestDTO requestDTO = null;
	ResponseDTO responseDTO = null;

	private String result;
	private String serviceCode;
	private String serviceName;
	private String subServiceCode;
	private String subServiceName;
	private String subServiceText;
	private String feeCode;
	private String serviceType;
	private String slabFrom;
	private String slabTo;
	private String flatPercent;
	private String accountMultiData;
	private String service;
	private String hudumaServiceName;

	private String bankMultiData;
	private String bin;
	private String binDescription;
	private String bankCode;
	private String bankName;
	private String settlementAccount;
	private String serviceNameDesc;
	private String settlementAccountDesc;
	private String virtualCard;
	private String hudumaServiceCode;
	private String hudumaServiceDesc;
	private String hudumaService;
	private String hudumaSubServiceName;
	private String hudumaSubService;
	private String feeFor;

	public String getCommonScreen() {
		logger.debug("Inside GetCommonScreen..");
		result = "success";
		return result;
	}

	public String getServiceInfo() {
		logger.debug("Inside getServiceInfo... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getServiceInfo(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SERVICE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				subServiceJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SUB_SERVICE_INFO);
				logger.debug("SubService JSON [" + subServiceJSON + "]");
				feeJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.FEE_INFO);
				logger.debug("Fee JSON [" + feeJSON + "]");
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
			logger.debug("Exception in getServiceInfo [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public String getNextServiceCode() {
		logger.debug("Inside GetNextServiceCode...");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getNextServiceCode(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SERVICE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getServiceInfo [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public String getServiceCreateConfirmScreen() {
		logger.debug("Inside GetServiceCreateConfirmScreen... ");
		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			responseJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			responseJSON.put(CevaCommonConstants.SETTLEMENT_ACCOUNT,
					settlementAccount);
			responseJSON.put(CevaCommonConstants.SERVICE_NAME_DESC,
					serviceNameDesc);
			responseJSON.put(CevaCommonConstants.SETTLEMENT_DESC,
					settlementAccountDesc);
			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getServiceCreateConfirmScreen ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String insertService() {
		logger.debug("Inside InsertService.. ");
		HttpSession session = null;
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			requestJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			requestJSON.put(CevaCommonConstants.SETTLEMENT_ACCOUNT,
					settlementAccount);
			requestJSON.put(CevaCommonConstants.SERVICE_NAME_DESC,
					serviceNameDesc);
			requestJSON.put(CevaCommonConstants.SETTLEMENT_DESC,
					settlementAccountDesc);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.insertService(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				} 
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}

				result = "fail";
			}
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InsertService [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String ServiceViewDetails() {
		logger.debug("Inside ServiceViewDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.ServiceViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SERVICE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in ServiceViewDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String SubServiceCreateScreenDetails() {
		logger.debug("Inside SubServiceCreateScreenDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			responseJSON = new JSONObject();
			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			requestJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			
			logger.debug("Resquest JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.SubServiceCreateScreenDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SERVICE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in SubServiceCreateScreenDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String SubServiceCreateSubmitDetails() {
		logger.debug("Inside SubServiceCreateSubmitDetails... ");

		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			responseJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			responseJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
					subServiceCode);
			responseJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
					subServiceName);
			responseJSON.put(CevaCommonConstants.SUB_SERVICE_TEXT,
					subServiceText);
			logger.debug("Response JSON[" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in SubServiceCreateSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String insertSubService() {
		logger.debug("Inside InsertSubService... ");
		HttpSession session = null;
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			requestJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			requestJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
					subServiceCode);
			requestJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
					subServiceName);
			requestJSON.put(CevaCommonConstants.SUB_SERVICE_TEXT,
					subServiceText);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.insertSubService(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug("Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				} 
 				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InsertSubService [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}
		return result;
	}

	public String FeeCreateScreenDetails() {
		logger.debug("Inside FeeCreateScreenDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();
			requestDTO = new RequestDTO();
			requestJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			requestJSON.put(CevaCommonConstants.FEE_CODE, feeCode);
			requestJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
					subServiceCode);
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.FeeCreateScrrenDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.FEE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in FeeCreateScrrenDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String FeeCreateSubmitDetails() {
		logger.debug("Inside FeeCreateSubmitDetails... ");

		try {
			responseJSON = new JSONObject();
			responseJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			responseJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			responseJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
					subServiceCode);
			responseJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
					subServiceName);
			responseJSON.put(CevaCommonConstants.FEE_CODE, feeCode);
			responseJSON.put(CevaCommonConstants.SERVICE_TYPE, serviceType);
			responseJSON.put(CevaCommonConstants.SLAB_FROM, slabFrom);
			responseJSON.put(CevaCommonConstants.SLAB_TO, slabTo);
			responseJSON.put(CevaCommonConstants.FALT_PERCENT, flatPercent);
			responseJSON.put(CevaCommonConstants.ACCOUNT_MULTI_DATA,
					accountMultiData);
			responseJSON.put(CevaCommonConstants.SERVICE, service);
			responseJSON.put(CevaCommonConstants.HUDUMA_SERVICE_NAME,
					hudumaServiceName);
			responseJSON.put(CevaCommonConstants.HUDUMA_SUB_SERVICE,
					hudumaSubService);
			responseJSON.put(CevaCommonConstants.HUDUMA_SUB_SERVICE_NAME,
					hudumaSubServiceName);
			responseJSON.put("feeFor",feeFor);

			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in FeeCreateSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String insertFeeDetails() {
		logger.debug("Inside InsertFeeDetails... ");
		HttpSession session = null;
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.SERVICE_CODE, serviceCode);
			requestJSON.put(CevaCommonConstants.SERVICE_NAME, serviceName);
			requestJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
					subServiceCode);
			requestJSON.put(CevaCommonConstants.SUB_SERVICE_NAME,
					subServiceName);
			requestJSON.put(CevaCommonConstants.FEE_CODE, feeCode);
			requestJSON.put(CevaCommonConstants.SERVICE_TYPE, serviceType);
			requestJSON.put(CevaCommonConstants.SLAB_FROM, slabFrom);
			requestJSON.put(CevaCommonConstants.SLAB_TO, slabTo);
			requestJSON.put(CevaCommonConstants.SERVICE, service);
			requestJSON.put(CevaCommonConstants.HUDUMA_SERVICE_NAME,
					hudumaServiceName);
			requestJSON.put(CevaCommonConstants.FALT_PERCENT, flatPercent);
			requestJSON.put(CevaCommonConstants.ACCOUNT_MULTI_DATA,
					accountMultiData);
			requestJSON.put(CevaCommonConstants.HUDUMA_SUB_SERVICE,
					hudumaSubService);
			requestJSON.put("feeFor",feeFor);
			
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.insertFeeDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
 				result = "fail";
			}
			
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertFeeDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String viewSubServiceDetails() {
		logger.debug("Inside ViewSubServiceDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.SUB_SERVICE_CODE,
					subServiceCode);
			logger.debug("Resquest JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.viewSubServiceDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.SERVICE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in insertFeeDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String viewFeeDetails() {
		logger.debug("Inside ViewFeeDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestJSON.put(CevaCommonConstants.FEE_CODE, feeCode);
			logger.debug("Resquest JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.viewFeeDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.FEE_INFO);
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in ViewFeeDetails [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String getBankDetails() {
		logger.debug("Inside GetBankDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getBankDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						"BANK_LIST");
				logger.debug("Response JSON [" + responseJSON + "]");
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in getBankDetails [" + e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public String registerBinSubmitDetails() {
		logger.debug("Inside RegisterBinSubmitDetails... ");
		try {
			responseJSON = new JSONObject();

			responseJSON.put(CevaCommonConstants.BANK_CODE, bankCode);
			responseJSON.put(CevaCommonConstants.BANK_NAME, bankName);
			responseJSON
			.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);
			logger.debug("Response JSON[" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in registerBinSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String inserRegisterBin() {
		logger.debug("Inside InserRegisterBin... ");
		HttpSession session = null;
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.BANK_CODE, bankCode);
			requestJSON.put(CevaCommonConstants.BANK_NAME, bankName);
			requestJSON.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);

			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.inserRegisterBin(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in inserRegisterBin [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String registerProcessingCodeSubmitDetails() {
		logger.debug("Inside RegisterProcessingCodeSubmitDetails... ");
		try {

			responseJSON = new JSONObject();

			responseJSON
			.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);
			logger.debug("Response JSON[" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in registerProcessingCodeSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String inserProcessingCode() {
		logger.debug("Inside InserProcessingCode ... ");
		HttpSession session = null;
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);
			logger.debug("Resquest JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.inserProcessingCode(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}

				
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
				result = "fail";
			}

			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in inserProcessingCode [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}
		return result;
	}

	public String inserhudumaService() {
		logger.debug("Inside InserhudumaService.. ");
		HttpSession session = null;
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> messages = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			session = ServletActionContext.getRequest().getSession();
			requestJSON.put(CevaCommonConstants.MAKER_ID,
					session.getAttribute(CevaCommonConstants.MAKER_ID));
			requestJSON.put(CevaCommonConstants.HUDUMA_SERVICE_CODE,
					hudumaServiceCode);
			requestJSON.put(CevaCommonConstants.HUDUMA_SERVICE_DESC,
					hudumaServiceDesc);
			requestJSON.put(CevaCommonConstants.VIRTUAL_CARD, virtualCard);
			requestJSON.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);
			logger.debug("Resquest JSON [" + requestJSON + "]");
			requestDTO.setRequestJSON(requestJSON);
			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.inserhudumaService(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");

			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				messages = (ArrayList<String>) responseDTO.getMessages();
				logger.debug(" Messages [" + messages + "]");
				for (int i = 0; i < messages.size(); i++) {
					addActionMessage(messages.get(i));
				}
				
				result = "success";
			} else {
				errors = (ArrayList<String>) responseDTO.getErrors();
				logger.debug("Errors [" + errors + "]");
				for (int i = 0; i < errors.size(); i++) {
					addActionError(errors.get(i));
				}
 				result = "fail";
			}
			
			responseJSON = requestJSON;
			logger.debug("Response JSON [" + responseJSON + "]");
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in InserhudumaService [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			messages = null;
			serviceDAO = null;
		}

		return result;
	}

	public String registerHuduamServiceSubmitDetails() {
		logger.debug("Inside RegisterHuduamServiceSubmitDetails... ");

		try {
			responseJSON = new JSONObject();

			responseJSON
			.put(CevaCommonConstants.BANK_MULTI_DATA, bankMultiData);
			responseJSON.put(CevaCommonConstants.HUDUMA_SERVICE_CODE,
					hudumaServiceCode);
			responseJSON.put(CevaCommonConstants.HUDUMA_SERVICE_DESC,
					hudumaServiceDesc);
			responseJSON.put(CevaCommonConstants.VIRTUAL_CARD, virtualCard);
			logger.debug("Response JSON [" + responseJSON + "]");
			result = "success";
		} catch (Exception e) {
			result = "fail";
			logger.debug("Exception in registerHuduamServiceSubmitDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {

		}

		return result;
	}

	public String getBinViewDetails() {

		logger.debug("Inside GetBinViewDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getBinViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DASHBOARD);
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
			logger.debug("Exception in GetBinViewDetails [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public String getProcessingCodeViewDetails() {

		logger.debug("Inside GetProcessingCodeViewDetails..");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getProcessingCodeViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DASHBOARD);
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
			logger.debug("Exception in GetProcessingCodeViewDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public String getHudumaServiceViewDetails() {

		logger.debug("Inside GetHudumaServiceViewDetails... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getHudumaServiceViewDetails(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DASHBOARD);
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
			logger.debug("Exception in GetHudumaServiceViewDetails ["
					+ e.getMessage() + "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public String getFeeDashBoard() {

		logger.debug("Inside GetFeeDashBoard... ");
		ServiceManagementDAO serviceDAO = null;
		ArrayList<String> errors = null;
		try {
			requestJSON = new JSONObject();

			requestDTO = new RequestDTO();

			requestDTO.setRequestJSON(requestJSON);

			logger.debug("Request DTO [" + requestDTO + "]");
			serviceDAO = new ServiceManagementDAO();
			responseDTO = serviceDAO.getFeeDashBoard(requestDTO);
			logger.debug("Response DTO [" + responseDTO + "]");
			if (responseDTO != null && responseDTO.getErrors().size() == 0) {
				responseJSON = (JSONObject) responseDTO.getData().get(
						CevaCommonConstants.MERCHANT_DASHBOARD);
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
			logger.debug("Exception in GetFeeDashBoard [" + e.getMessage()
					+ "]");
			addActionError("Internal error occured.");
		} finally {
			requestDTO = null;
			responseDTO = null;
			requestJSON = null;
			errors = null;
			serviceDAO = null;
		}

		return result;
	}

	public JSONObject getResponseJSON() {
		return responseJSON;
	}

	public void setResponseJSON(JSONObject responseJSON) {
		this.responseJSON = responseJSON;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSubServiceCode() {
		return subServiceCode;
	}

	public void setSubServiceCode(String subServiceCode) {
		this.subServiceCode = subServiceCode;
	}

	public String getSubServiceName() {
		return subServiceName;
	}

	public void setSubServiceName(String subServiceName) {
		this.subServiceName = subServiceName;
	}

	public String getSubServiceText() {
		return subServiceText;
	}

	public void setSubServiceText(String subServiceText) {
		this.subServiceText = subServiceText;
	}

	public JSONObject getSubServiceJSON() {
		return subServiceJSON;
	}

	public void setSubServiceJSON(JSONObject subServiceJSON) {
		this.subServiceJSON = subServiceJSON;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSlabFrom() {
		return slabFrom;
	}

	public void setSlabFrom(String slabFrom) {
		this.slabFrom = slabFrom;
	}

	public String getSlabTo() {
		return slabTo;
	}

	public void setSlabTo(String slabTo) {
		this.slabTo = slabTo;
	}

	public String getFlatPercent() {
		return flatPercent;
	}

	public void setFlatPercent(String flatPercent) {
		this.flatPercent = flatPercent;
	}

	public String getAccountMultiData() {
		return accountMultiData;
	}

	public void setAccountMultiData(String accountMultiData) {
		this.accountMultiData = accountMultiData;
	}

	public JSONObject getFeeJSON() {
		return feeJSON;
	}

	public void setFeeJSON(JSONObject feeJSON) {
		this.feeJSON = feeJSON;
	}

	public String getHudumaServiceName() {
		return hudumaServiceName;
	}

	public void setHudumaServiceName(String hudumaServiceName) {
		this.hudumaServiceName = hudumaServiceName;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getBankMultiData() {
		return bankMultiData;
	}

	public void setBankMultiData(String bankMultiData) {
		this.bankMultiData = bankMultiData;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getBinDescription() {
		return binDescription;
	}

	public void setBinDescription(String binDescription) {
		this.binDescription = binDescription;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSettlementAccount() {
		return settlementAccount;
	}

	public void setSettlementAccount(String settlementAccount) {
		this.settlementAccount = settlementAccount;
	}

	public String getServiceNameDesc() {
		return serviceNameDesc;
	}

	public void setServiceNameDesc(String serviceNameDesc) {
		this.serviceNameDesc = serviceNameDesc;
	}

	public String getSettlementAccountDesc() {
		return settlementAccountDesc;
	}

	public void setSettlementAccountDesc(String settlementAccountDesc) {
		this.settlementAccountDesc = settlementAccountDesc;
	}

	public String getVirtualCard() {
		return virtualCard;
	}

	public void setVirtualCard(String virtualCard) {
		this.virtualCard = virtualCard;
	}

	public String getHudumaServiceCode() {
		return hudumaServiceCode;
	}

	public void setHudumaServiceCode(String hudumaServiceCode) {
		this.hudumaServiceCode = hudumaServiceCode;
	}

	public String getHudumaServiceDesc() {
		return hudumaServiceDesc;
	}

	public void setHudumaServiceDesc(String hudumaServiceDesc) {
		this.hudumaServiceDesc = hudumaServiceDesc;
	}

	public String getHudumaService() {
		return hudumaService;
	}

	public void setHudumaService(String hudumaService) {
		this.hudumaService = hudumaService;
	}

	public String getHudumaSubServiceName() {
		return hudumaSubServiceName;
	}

	public void setHudumaSubServiceName(String hudumaSubServiceName) {
		this.hudumaSubServiceName = hudumaSubServiceName;
	}

	public String getHudumaSubService() {
		return hudumaSubService;
	}

	public void setHudumaSubService(String hudumaSubService) {
		this.hudumaSubService = hudumaSubService;
	}

	public String getFeeFor() {
		return feeFor;
	}

	public void setFeeFor(String feeFor) {
		this.feeFor = feeFor;
	}

	
}
