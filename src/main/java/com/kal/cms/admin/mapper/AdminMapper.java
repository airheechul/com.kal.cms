package com.kal.cms.admin.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.kal.cms.admin.vo.AdminInfo;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


@Repository("adminMapper")
public class AdminMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminMapper.class);

	public AdminInfo getAdminLogin(HashMap<String, Object> pMap) throws DataAccessException {
		return (AdminInfo)selectOne("com.kal.cms.admin.mapper.AdminMapper.getAdminLogin", pMap);
	}
	
	public List<AdminInfo> getAdminList(HashMap<String, Object> pMap) throws DataAccessException, Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		
/*    	Iterator iter = pMap.entrySet().iterator();
    	while(iter.hasNext()) {
    	    Entry<String, Object> entry =  (Entry<String, Object>) iter.next();
    	    String key = entry.getKey();
    	    Object value = entry.getValue();
    	    
    	    LOGGER.debug("key / value => " + key + "/" + value);
    	}
*/    	
	
		return selectList("com.kal.cms.admin.mapper.AdminMapper.getAdminPageList", pMap);
	}
	
	public AdminInfo getAdminInfo(HashMap<String, Object> pMap) throws DataAccessException {
		return (AdminInfo)selectOne("com.kal.cms.admin.mapper.AdminMapper.getAdminInfo", pMap);
	}	

	public int insertAdmin(HashMap<String, Object> pMap) {

	    int result = -1;
	    
        try {
        	insert("com.kal.cms.admin.mapper.AdminMapper.addAdmin", pMap);
        	result = 1;

        } catch (Exception e) {
        	LOGGER.debug("AdminMapper.insertEmployee Exception: {}", e);
            e.printStackTrace();
        }

		return result;
	}

	public int updateAdmin(HashMap<String, Object> pMap) {
		return update("com.kal.cms.admin.mapper.AdminMapper.updateAdmin", pMap);
	}
	
	public int removeAdmin(HashMap<String, Object> pMap) {
		return delete("com.kal.cms.admin.mapper.AdminMapper.removeAdmin", pMap);
	}
	
	public int getAdminTotalCount(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return (int)selectOne("com.kal.cms.admin.mapper.AdminMapper.getAdminTotalCount", pMap);
	}
	
/*
 * 	@Override
	public AdminInfo getAdminInfo(ParameterMap<String, Object> pMap) {
		return (AdminInfo) get(generateQueryId("get", "Info"), pMap);
	}

	@Override
	public PageList getAdminList(ParameterMap<String, Object> pMap) {
		PageList pageList = getPageList("",pMap);
		return pageList;
	}
	

	@Override
	public AdminInfo getAdminLogin(ParameterMap<String, Object> pMap) {
		return (AdminInfo) get(generateQueryId("get", "Login"),pMap);
	}

 */
}
