package com.ceva.base.memory.cache;
 
import net.sf.json.JSONObject;

import com.ceva.base.common.dto.RequestDTO;
import com.ceva.base.common.dto.ResponseDTO;
import com.ceva.base.memory.cache.MemoryCacheImplementation;
import com.ceva.base.memory.cache.dao.CacheMemoryDAO;
 
/**
 * @author 
 */
 
public class CacheMemoryTest {
	
	
	private ResponseDTO responseDTO = null;
	private RequestDTO requestDTO = null;
 
    public static void main(String[] args) throws InterruptedException {
 
        CacheMemoryTest cacheMemoryTest = new CacheMemoryTest();
 
        System.out.println("\n\n==========Test1: AddRemoveObjects ==========");
        cacheMemoryTest.addRemoveObjects();
        System.out.println("\n\n==========Test2: ExpiredCacheObjects ==========");
        cacheMemoryTest.expiredCacheObjects();
        System.out.println("\n\n==========Test3: ObjectsCleanupTime ==========");
        cacheMemoryTest.objectsCleanupTime();
        
        
    }
 
    private void addRemoveObjects() {
 
        // Test with timeToLiveInSeconds = 200 seconds
        // timerIntervalInSeconds = 500 seconds
        // maxItems = 6
        MemoryCacheImplementation<String, Object> cache = new MemoryCacheImplementation<String, Object>(200, 500, 6);
 
        JSONObject responseJSON = new JSONObject();
        
        responseDTO = CacheMemoryDAO.getAllUserGrps(requestDTO);
        
        if (responseDTO != null && responseDTO.getErrors().size() == 0) {
			responseJSON = (JSONObject) responseDTO.getData().get("USER_ACCESS_MNG");

			System.out.println("Response JSON  [" + responseJSON + "]");
		}
        
        cache.put("UAM_DASHBOARD", responseJSON);
 
        System.out.println("Cache Object Added.. cache.size(): " + cache.size());
        
    }
 
    private void expiredCacheObjects() throws InterruptedException {
 
        // Test with timeToLiveInSeconds = 1 second
        // timerIntervalInSeconds = 1 second
        // maxItems = 10
        MemoryCacheImplementation<String, String> cache = new MemoryCacheImplementation<String, String>(1, 1, 10);
 
        cache.put("eBay", "eBay");
        cache.put("Paypal", "Paypal");
        // Adding 3 seconds sleep.. Both above objects will be removed from
        // Cache because of timeToLiveInSeconds value
        Thread.sleep(3000);
 
        System.out.println("Two objects are added but reached timeToLive. cache.size(): " + cache.size());
 
    }
 
    private void objectsCleanupTime() throws InterruptedException {
        int size = 500000;
 
        // Test with timeToLiveInSeconds = 100 seconds
        // timerIntervalInSeconds = 100 seconds
        // maxItems = 500000
 
        MemoryCacheImplementation<String, String> cache = new MemoryCacheImplementation<String, String>(100, 100, 500000);
 
        for (int i = 0; i < size; i++) {
            String value = Integer.toString(i);
            cache.put(value, value);
        }
 
        Thread.sleep(200);
 
        long start = System.currentTimeMillis();
        cache.cleanup();
        double finish = (double) (System.currentTimeMillis() - start) / 1000.0;
 
        System.out.println("Cleanup times for " + size + " objects are " + finish + " s");
 
    }
    
    
    
}