package com.ceva.base.common.action;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.ceva.base.common.bean.CommonWebBean;
import com.ceva.base.common.utils.CevaCommonConstants;
import com.ceva.util.CommonUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CommonWebAction extends ActionSupport implements
		ModelDriven<CommonWebBean> {

	private static final long serialVersionUID = 1169823183008588660L;
	private Logger logger = Logger.getLogger(CommonWebAction.class);
	private String result;

	@Autowired
	private CommonWebBean commonWebBean = null;

	private SessionMap<String, Object> session;

	private HttpSession httpSession = null;

	public String execute() {
		String randomVal = null;
		HttpServletResponse response = null;

		try {
			logger.debug("Inside CommonWebAction..");
			commonWebBean.setApplName("AgencyBanking");
			logger.debug("Appl Name [" + commonWebBean.getApplName() + "]");
			response = ServletActionContext.getResponse();

			try {

				if (!session.isEmpty()) {
					if (session.containsKey("sessionLoginTime")
							&& session.containsKey("sessionMaxAge")) {

						try {

							response.setHeader("Pragma", "no-cache");
							response.setHeader("Cache-Control", "no-cache");
							response.setHeader("Expires", "0");
						} catch (Exception e) {
							logger.debug("Inside  session invaldiate check exception :: "
									+ e.getMessage());
						}

						session.clear();
					}
				}
			} catch (Exception e) {
				logger.debug("Inside  try catch session check exception :: "
						+ e.getMessage());
			}

			httpSession = ServletActionContext.getRequest().getSession(true);
			httpSession.setAttribute(CevaCommonConstants.ACCESS_APPL_NAME,
					commonWebBean.getApplName());
			randomVal = CommonUtil.getRandomInteger();
			logger.debug("Random Val [" + randomVal + "]");
			httpSession.setAttribute(CevaCommonConstants.RANDOM_VAL, randomVal);

			if (commonWebBean.getApplName() == null) {
				commonWebBean.setRedirectPage("WEB-INF/jsp/InvalidURL.jsp");
				result = "invalid";
			} else {
				commonWebBean.setRedirectPage("WEB-INF/jsp/login.jsp");
				result = "valid";
			}
			logger.debug("Result[" + result + "]");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			randomVal = null;
		}
		return result;
	}

	public CommonWebBean getCommonWebBean() {
		return commonWebBean;
	}

	public void setCommonWebBean(CommonWebBean commonWebBean) {
		this.commonWebBean = commonWebBean;
	}

	@Override
	public CommonWebBean getModel() {
		return commonWebBean;
	}

}
