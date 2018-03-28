package com.kal.cms.system.service;

import java.util.HashMap;
import java.util.List;

import com.kal.cms.system.vo.CodeInfo;

public interface CodeService {

	
	public List<CodeInfo> findCodeList(HashMap<String , Object> pMap) throws Exception;

	public int findExistCodeCount(HashMap<String , Object> pMap) throws Exception;
	
	public boolean insertCode(HashMap<String , Object> pMap) throws Exception;
	
    public boolean updateCode(HashMap<String , Object> pMap) throws Exception;	
	
    public boolean deleteCode(HashMap<String , Object> pMap) throws Exception;   
    
	public List<CodeInfo> findSelectedCodeList(HashMap<String , Object> pMap) throws Exception;
	
}
