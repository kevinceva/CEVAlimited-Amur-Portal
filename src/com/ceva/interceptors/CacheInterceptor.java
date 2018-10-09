package com.ceva.interceptors;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.memory.cache.MemoryCacheImplementation;
import com.ceva.base.memory.cache.dao.CacheMemoryDAO;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class CacheInterceptor extends AbstractInterceptor {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(CacheInterceptor.class);
	
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		logger.debug("[CacheInterceptor][intercept]:::");
		ActionContext context=(ActionContext)invocation.getInvocationContext();
		
		addRemoveObjects();
		
		return invocation.invoke();
		
	}
	
	
	private void addRemoveObjects() {
		 
        // Test with timeToLiveInSeconds = 200 seconds
        // timerIntervalInSeconds = 500 seconds
        // maxItems = 6
		
		/*RequestDTO requestDTO = new RequestDTO();
		ResponseDTO responseDTO = new ResponseDTO();
        MemoryCacheImplementation<String, Object> cache = new MemoryCacheImplementation<String, Object>(200, 500, 6);
 
        JSONObject responseJSON = new JSONObject();
        
        responseDTO = CacheMemoryDAO.getAllUserGrps(requestDTO);
        
        if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get("USER_ACCESS_MNG");

			logger.debug("Response JSON  [" + responseJSON + "]");
		}
        
        cache.put("UAM_DASHBOARD", responseJSON);
 
        logger.debug("Cache Object Added.. cache.size(): " + cache.get("UAM_DASHBOARD").toString());*/
		
		
		
        
    }

}
