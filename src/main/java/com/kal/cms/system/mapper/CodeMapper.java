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

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


@Repository("codeMapper")
public class CodeMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeMapper.class);

	
	public List<CodeInfo> getSelectedCodelist(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
	
		return selectList("com.kal.cms.system.mapper.CodeMapper.getSelectedCodeList", pMap);
	}


	public int updateCodeOrder(CodeInfo pVO) {
		return update("com.kal.cms.system.mapper.CodeMapper.updateCodeOrder", pVO);
	}
	
}
