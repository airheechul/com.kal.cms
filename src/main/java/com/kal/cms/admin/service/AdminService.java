package com.kal.cms.admin.service;

import java.util.HashMap;
import java.util.List;

import com.kal.cms.admin.vo.AdminInfo;

public interface AdminService  {

    public AdminInfo findAdminLogin(HashMap<String, Object> pMap) throws Exception;

    public List<AdminInfo> findAdminList(HashMap<String, Object> pMap) throws Exception;

    public int insertAdmin(HashMap<String, Object> pMap) throws Exception;

    public int updateAdmin(HashMap<String, Object> pMap) throws Exception;

	public void deleteAdmin(List<String> userId) throws Exception;

    public AdminInfo findAdminInfo(HashMap<String, Object> pMap) throws Exception;
    
    public int getAdminTotalCount(HashMap<String, Object> pMap) throws Exception;

}
