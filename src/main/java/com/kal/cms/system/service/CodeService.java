package com.kal.cms.system.service;

import java.util.HashMap;
import java.util.List;

import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupListVO;
import com.kal.cms.system.vo.LookupVO;

public interface CodeService {

	
	public List<CodeInfo> findCodeList(HashMap<String , Object> pMap) throws Exception;

	public int findExistCodeCount(HashMap<String , Object> pMap) throws Exception;
	
	public int insertCode(HashMap<String , Object> pMap) throws Exception;
	
    public int updateCode(HashMap<String , Object> pMap) throws Exception;	
	
    public int deleteCode(HashMap<String , Object> pMap) throws Exception;   
    
	public List<CodeInfo> findSelectedCodeList(HashMap<String , Object> pMap) throws Exception;
	
	public List<LookupListVO> findComboList() throws Exception;
	
	public List<LookupVO> findLOVs() throws Exception;
	
}
