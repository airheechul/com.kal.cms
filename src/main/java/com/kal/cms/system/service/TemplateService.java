package com.kal.cms.system.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.TemplateInfo;

public interface TemplateService {
	
	public List<TemplateInfo> findTemplateList(HashMap<String , Object> pMap) throws Exception;

	public TemplateInfo getTemplate(HashMap<String , Object> pMap) throws Exception;
	
	public boolean registTemplate(HashMap<String , Object> pMap, File tempFile) throws Exception;
	
    public boolean updateTemplate(HashMap<String , Object> pMap) throws Exception;	
	
}
