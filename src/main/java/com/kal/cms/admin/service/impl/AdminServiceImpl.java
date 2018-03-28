/*
 * (주)오픈잇 | http://www.openit.co.kr
 * Copyright (c)2006-2010, OpenIT Co., Ltd.
 * All rights reserved.
 * 
 * Mobile Identity. OpenIT.
 */
package com.kal.cms.admin.service.impl;
 
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kal.cms.admin.mapper.AdminMapper;
import com.kal.cms.admin.service.AdminService;
import com.kal.cms.admin.vo.AdminInfo;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("adminService")
public class AdminServiceImpl extends EgovAbstractServiceImpl implements AdminService  {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
			
//    @Autowired
//    private AdminDao adminDao;
    
	// TODO mybatis 사용
	@Resource(name="adminMapper")
	private AdminMapper adminDAO;    

	@Override	    
    public AdminInfo findAdminLogin (HashMap<String, Object> pMap) {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		
        return adminDAO.getAdminLogin(pMap);
    }

	@Override		
    public AdminInfo findAdminInfo(HashMap<String, Object> pMap) throws Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		
        return adminDAO.getAdminInfo(pMap);
    }

	@Override
	public List<AdminInfo> findAdminList(HashMap<String, Object> pMap) throws Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
		
		return adminDAO.getAdminList(pMap);
	}

	@Override
	public int insertAdmin(HashMap<String, Object> pMap) throws Exception {
		return adminDAO.insertAdmin(pMap);
	}

	@Override
	public int updateAdmin(HashMap<String, Object> pMap) throws Exception {
		return adminDAO.updateAdmin(pMap);
	}

	@Override
	public void deleteAdmin(List<String> userId) throws Exception {
		
		HashMap<String, Object> pMap;
		
		for(String id : userId)
		{
			pMap = new HashMap<String, Object>();
			pMap.put("userId", id);
			
			adminDAO.removeAdmin(pMap);
		}
	}

	@Override
	public int getAdminTotalCount(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return adminDAO.getAdminTotalCount(pMap);
	}

}
