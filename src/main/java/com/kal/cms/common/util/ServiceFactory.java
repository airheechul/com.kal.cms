package com.kal.cms.common.util;

import java.util.HashMap;
import java.util.Map;

import com.kal.cms.content.service.ContentProcessService;
import com.kal.cms.system.service.CodeService;

public class ServiceFactory {
	    /**
	     * 서비스맵  
	     */
	    private static Map<String, Object> factory = new HashMap<String, Object>();

	    public ServiceFactory() {}
	    
	    public void setContentProcessService(ContentProcessService contentProcessService) {
	        factory.put("contentProcessService", contentProcessService);
	    }
	    
	    public static ContentProcessService getContentProcessService() {
	        return (ContentProcessService)factory.get("contentProcessService");
	    }
	    
	    public void setCodeService(CodeService codeService) {
	        factory.put("codeService", codeService);
	    }
	    
	    public static CodeService getCodeService() {
	        return (CodeService)factory.get("codeService");
	    }
	}

