package com.kal.cms.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kal.cms.system.mapper.CodeMapper;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupListVO;
import com.kal.cms.system.vo.LookupVO;

@Service("codeService")
public class CodeServiceImpl implements CodeService {

	@Resource(name="codeMapper")
	private CodeMapper codeMapper;  
	
	@Override
	public List<CodeInfo> findCodeList(HashMap<String, Object> pMap) throws Exception {
		return codeMapper.getCodeList(pMap);
	}

	@Override
	public int findExistCodeCount(HashMap<String, Object> pMap) throws Exception {
		return codeMapper.getExistCodeCount(pMap);
	}

	@Override
	public int insertCode(HashMap<String, Object> pMap) throws Exception {
		return codeMapper.addCode(pMap);
	}

	@Override
	public int updateCode(HashMap<String, Object> pMap) throws Exception {
		return codeMapper.updateCode(pMap);
	}

	@Override
	public int deleteCode(HashMap<String, Object> pMap) throws Exception {
		return codeMapper.removeCode(pMap);
	}

	@Override
	public List<CodeInfo> findSelectedCodeList(HashMap<String, Object> pMap) throws Exception {
		return codeMapper.getSelectedCodelist(pMap);
	}

	@Override
	public List<LookupListVO> findComboList() throws Exception {

		HashMap<String, String> lookupTypeMap = new HashMap<String, String>();
		lookupTypeMap.put("Model", "1000");
		lookupTypeMap.put("ManualType", "2000");
		lookupTypeMap.put("ContentType", "3000");
		
		
		List<LookupListVO> lookupListVOs = new ArrayList<LookupListVO>(); 

    	Iterator<Entry<String, String>> iter = lookupTypeMap.entrySet().iterator();
    	while(iter.hasNext()) {
    	    Entry<String, String> entry =  (Entry<String, String>) iter.next();
    	    String key = entry.getKey();
    	    String value = entry.getValue();
    	    
			List<LookupVO> lookupVOs;
			try {
				HashMap<String, Object> param = new HashMap<String, Object>();
				
				param.put("lookupTypeName", value);
				param.put("searchKeyword", "%");
				
				lookupVOs = codeMapper.getComboList(param);
			} catch (Exception e) {
				lookupVOs = null;
			}
			LookupListVO lookupListVO = new LookupListVO();
			lookupListVO.setLookupTypeName(key);
			lookupListVO.setLookupVOList(lookupVOs);

			lookupListVOs.add(lookupListVO);
    	    
    	}
   	
		return lookupListVOs;
	}

	@Override
	public List<LookupVO> findLOVs() throws Exception {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		String[] sLookupType = {"1000","2000","3000","4000","5000","7000","8000"};
		param.put("sLookupType", sLookupType);
		
		
//		
//		
//		
//		// TODO Auto-generated method stub
//		HashMap<String, String> lookupTypeMap = new HashMap<String, String>();
//		lookupTypeMap.put("Model", "1000");
//		lookupTypeMap.put("ManualType", "2000");
//		lookupTypeMap.put("ContentType", "3000");
		
		
		
		List<LookupVO> lookupVOs = codeMapper.getLOVs(param);
		
/*		List<LookupListVO> lookupListVOs = new ArrayList<LookupListVO>();
		
		for (int idx=0; idx<lookupVOs.size(); idx++) {
			System.out.println("LookupType -> " + lookupVOs.get(idx).getLookupTypeName());
			System.out.println("LookupCode -> " + lookupVOs.get(idx).getLookupCode());
			
			lookupVOs.get(idx).getLookupTypeName()
		}*/
		
		
		
/*
    	Iterator<Entry<String, String>> iter = lookupTypeMap.entrySet().iterator();
    	while(iter.hasNext()) {
    	    Entry<String, String> entry =  (Entry<String, String>) iter.next();
    	    String key = entry.getKey();
    	    String value = entry.getValue();
    	    
			List<LookupVO> lookupVOs;
			try {
				HashMap<String, Object> param = new HashMap<String, Object>();
				
				param.put("lookupTypeName", value);
				param.put("searchKeyword", "%");
				
				lookupVOs = codeMapper.getComboList(param);
			} catch (Exception e) {
				lookupVOs = null;
			}
			LookupListVO lookupListVO = new LookupListVO();
			lookupListVO.setLookupTypeName(key);
			lookupListVO.setLookupVOList(lookupVOs);

			lookupListVOs.add(lookupListVO);
    	    
    	}*/
   	
		return lookupVOs;
	}

}
