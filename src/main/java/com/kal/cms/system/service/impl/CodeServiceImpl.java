package com.kal.cms.system.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kal.cms.admin.mapper.AdminMapper;
import com.kal.cms.system.mapper.CodeMapper;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;

@Service("codeService")
public class CodeServiceImpl implements CodeService {

	@Resource(name="codeMapper")
	private CodeMapper codeDAO;  
	
	@Override
	public List<CodeInfo> findCodeList(HashMap<String, Object> pMap) throws Exception {
		return null;
	}

	@Override
	public int findExistCodeCount(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean insertCode(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCode(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCode(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<CodeInfo> findSelectedCodeList(HashMap<String, Object> pMap) throws Exception {
		return codeDAO.getSelectedCodelist(pMap);
	}

}
