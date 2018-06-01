package com.kal.cms.system.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupVO;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


@Repository("codeMapper")
public class CodeMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeMapper.class);

	public List<CodeInfo> getCodeList(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	
		return selectList("com.kal.cms.system.mapper.CodeMapper.getCodeList", pMap);
	}
	
	
	public List<CodeInfo> getSelectedCodelist(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	
		return selectList("com.kal.cms.system.mapper.CodeMapper.getSelectedCodeList", pMap);
	}
	
	public List<LookupVO> getComboList(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	
		return selectList("com.kal.cms.system.mapper.CodeMapper.getComboList", pMap);
	}
	
	public List<LookupVO> getLOVs(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	
		if (pMap == null) System.out.println("getLOVs is null.");
		
		return selectList("com.kal.cms.system.mapper.CodeMapper.getLOVs", pMap);
	}


	public int getExistCodeCount(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	
		return selectOne("com.kal.cms.system.mapper.CodeMapper.getExistCodeCount", pMap);
	}
	
	
	public int addCode(HashMap<String, Object> pMap) {
	    int result = -1;
	    
        try {
        	insert("com.kal.cms.system.mapper.CodeMapper.addCode", pMap);
        	result = 1;

        } catch (Exception e) {
        	LOGGER.debug("com.kal.cms.system.mapper.CodeMapper.addCode Exception: {}", e);
            e.printStackTrace();
        }

		return result;
	}
	
	
	public int updateCode(HashMap<String, Object> pMap) {
		return update("com.kal.cms.system.mapper.CodeMapper.updateCode", pMap);
	}
	
	
	public int removeCode(HashMap<String, Object> pMap) {
		return delete("com.kal.cms.system.mapper.CodeMapper.removeCode", pMap);
	}
	
	//return delete("com.kal.cms.admin.mapper.AdminMapper.removeAdmin", pMap);
	public int updateCodeOrder(CodeInfo pVO) {
		return update("com.kal.cms.system.mapper.CodeMapper.updateCodeOrder", pVO);
	}
	

}
