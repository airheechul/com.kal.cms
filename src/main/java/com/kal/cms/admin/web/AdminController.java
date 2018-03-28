package com.kal.cms.admin.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.admin.service.AdminService;
import com.kal.cms.admin.vo.AdminInfo;
import com.kal.cms.admin.vo.SearchKeywordVO;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
//import com.kal.cms.common.constants.AdminConstants;

//import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

//import org.apache.commons.lang.StringUtils;


@Controller
public class AdminController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	@Autowired //@Autowired:only supported in spring framework..must match type, @Resource(name = "adminService"): supported in java..
	private AdminService adminService;
	
	@Autowired
	private CodeService codeService;

    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	/*
	@RequestMapping("/login/login.do")
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String OAM_UID = request.getHeader("OAM_UID")==null?"":request.getHeader("OAM_UID").toString();
		
		//테스트 서버인지 판단해서 SSO check 여부 확인
		String url = request.getRequestURL().toString();
		
		String checkSSO = AdminConstants.USE_YN_Y;
		
		if(url.contains(config.getString("cms.dev.ip"))){
			checkSSO = AdminConstants.USE_YN_N;
			mav.addObject("checkSSO", checkSSO);
		} else {
			if(!OAM_UID.equals("")){
				mav.setViewName("redirect:/login/actionLogin.do?userId="+OAM_UID+"&checkSSO="+checkSSO);
			} else {
				if (url.contains(config.getString("cms.stg.ip")) || url.contains(config.getString("cms.stg.domain"))){
					mav.setViewName("redirect:".concat(config.getString("cms.stg.sso.url")));
				} else if (url.contains(config.getString("cms.prd.ip")) || url.contains(config.getString("cms.prd.domain"))){
					mav.setViewName("redirect:".concat(config.getString("cms.prd.sso.url")));
				}
			}
		}
		
		return mav;
	}
*/
/*
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/login/actionLogin.do")
	public ModelAndView actionLogin(HttpServletRequest request, @RequestParam Map<String, Object> map) throws Exception {

 		ModelAndView mav = new ModelAndView();

 		pMap  = super.getParameterMap(request, map);
		pMap.put("userId", pMap.get("userId"));
		
		AdminInfo adminInfo = null;
		
		//아이디가 숫자일때만 사용자DB조회
		if(StringUtils.isNumeric((String)pMap.get("userId"))){
			adminInfo = adminService.findAdminLogin(pMap);
		}
		
		String returnUrl = "";
		SessionAdminInfo sessionAdminInfo = new SessionAdminInfo();
		
		//스테이징이나 상용서버 접근 시
		if(pMap.get("checkSSO").equals(AdminConstants.USE_YN_Y)){
			if (adminInfo == null){//엔지니어 권한
				sessionAdminInfo.setUserId((String)pMap.get("userId"));
				sessionAdminInfo.setUserNm((String)pMap.get("userId"));
				sessionAdminInfo.setUserDivisionCd(AdminConstants.ADMIN_ENGINEER);
			} else {
				sessionAdminInfo.setUserId(adminInfo.getAdminId());
				sessionAdminInfo.setUserNm(adminInfo.getAdminNm());
				sessionAdminInfo.setUserDivisionCd(adminInfo.getRoleCode());
			}
			
			logger.info("[SESSION ADMIN INFO]" + sessionAdminInfo);
			
			SessionHandler sessionHandler = SessionHandler.getInstance();
			sessionHandler.setLoginInfo(request, sessionAdminInfo);
			//pdf제조사일때는 Task Monitoring만 보여주도록
			if (adminInfo == null){
				returnUrl = "redirect:/content/searchList.do?topNum=1";
			} else {
				if (adminInfo.getRoleCode().equals(AdminConstants.ADMIN_MANUFACTURE)){
					returnUrl = "redirect:/task/taskList.do?topNum=3";							
				} else {
					returnUrl = "redirect:/content/searchList.do?topNum=1";	
				}	
			}
		//테스트 서버 접근 시
		} else {
			if (adminInfo == null){
				returnUrl = "redirect:/login/login.do?rCd=".concat(AdminConstants.FAIL_MSG_ID);
			} else {
				if (adminInfo.getRoleCode() == null){
					returnUrl = "redirect:/login/login.do?rCd=".concat(AdminConstants.FAIL_MSG_CERTIFICATION);
				} else {
					sessionAdminInfo.setUserId(adminInfo.getAdminId());
					sessionAdminInfo.setUserNm(adminInfo.getAdminNm());
					sessionAdminInfo.setUserDivisionCd(adminInfo.getRoleCode());
					
					logger.info("[SESSION ADMIN INFO]" + sessionAdminInfo);
					
					SessionHandler sessionHandler = SessionHandler.getInstance();
					sessionHandler.setLoginInfo(request, sessionAdminInfo);
					//pdf제조사일때는 Task Monitoring만 보여주도록
					if (adminInfo.getRoleCode().equals(AdminConstants.ADMIN_MANUFACTURE)){
						returnUrl = "redirect:/task/taskList.do?topNum=3";							
					} else {
						returnUrl = "redirect:/content/searchList.do?topNum=1";
					}
					
				}
			}
		}
		mav.setViewName(returnUrl);
        
		return mav;
	}
*/
/*
	@SuppressWarnings("unchecked")
	@RequestMapping("/admin/adminList.do")
	public ModelAndView findAdminList(HttpServletRequest request, @RequestParam Map<String, Object> map) {

		ModelAndView mav = new ModelAndView();
        
 		pMap  = super.getParameterMap(request, map);   

 		pMap.put("currentPage", pMap.getCurrentPage());
 		pMap.put("pageSize", pMap.getPageSize());
 		    	
		mav.addObject("regularAdminList", adminService.findAdminList(pMap));
		mav.addObject("pMap", pMap);

		// 메뉴 권한 리스트 조회
		//mav.addObject("menuAuthList", adminService.findMenuAuthList());

		return mav;
	}
	*/
	@RequestMapping(value = "/admin/adminList.do")
	public String findAdminList(@RequestParam(value = "pageIndex", defaultValue = "1", required = false) String pageIndex, ModelMap model,
			@RequestParam Map<String, Object> commandMap) throws Exception {
		
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

		SearchKeywordVO searchKeywordVO = new SearchKeywordVO();
		
		int currentPageNo;
		try {
			currentPageNo = Integer.parseInt(pageIndex);
		} catch (Exception e) {
			
			LOGGER.debug("currentPageNo error => " + e.toString());
			
			currentPageNo = 1;
		}

//    	Iterator iter = commandMap.entrySet().iterator();
//    	while(iter.hasNext()) {
//    	    Entry<String, Object[]> entry =  (Entry<String, Object[]>) iter.next();
//    	    String key = entry.getKey();
//    	    Object value = entry.getValue()[0];
//    	    
//    	    LOGGER.debug("key / value => " + key + "/" + value);
//    	}

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(currentPageNo);
		paginationInfo.setRecordCountPerPage(10);
		paginationInfo.setPageSize(5);

		if (commandMap == null || commandMap.size() == 0)
		{
			System.out.println("commandMap is null");
			
			commandMap.put("searchKeywordType", "");
			commandMap.put("searchKeyword", "");
			 
		}
		else
		{
			if (commandMap.get("searchKeywordType") == null)
			{
				commandMap.put("searchKeywordType", "");
				commandMap.put("searchKeyword", "");
			}
		}
		
		
		searchKeywordVO.setSearchKeywordType(commandMap.get("searchKeywordType"));
		searchKeywordVO.setSearchKeyword(commandMap.get("searchKeyword"));


		commandMap.put("startRow", paginationInfo.getFirstRecordIndex());
		commandMap.put("endRow", paginationInfo.getLastRecordIndex());
		commandMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
//		commandMap.put("startRow", 1);
//		commandMap.put("endRow", 20);
//		commandMap.put("recordCountPerPage", 10);		
		
		List<AdminInfo> adminList = adminService.findAdminList((HashMap<String, Object>)commandMap);
		//adminList.add(0, null);
		
		HashMap<String, Object> roleMap = new HashMap<String, Object>(2);
		roleMap.put("lookupTypeName", "50000");
		roleMap.put("searchKeyword", "%");
		List<CodeInfo> roleList = codeService.findSelectedCodeList(roleMap);
		
		model.addAttribute("adminList", adminList);
		model.addAttribute("searchKeywordVO", searchKeywordVO);
		//model.addAttribute("searchCriteria", searchCriteria);
		
		model.addAttribute("roleList", roleList);

		int adminCount = adminService.getAdminTotalCount((HashMap<String, Object>)commandMap);

		
		paginationInfo.setTotalRecordCount(adminCount);
		model.addAttribute("paginationInfo", paginationInfo);

		return "admin/adminList";
	}
/*	
	@RequestMapping(value = "/egovSampleList.do")
	public String selectSampleList(@ModelAttribute("searchVO") SampleDefaultVO searchVO, ModelMap model) throws Exception {


		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));


		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<?> sampleList = sampleService.selectSampleList(searchVO);
		model.addAttribute("resultList", sampleList);

		int totCnt = sampleService.selectSampleListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		model.addAttribute("paginationInfo", paginationInfo);

		return "sample/egovSampleList";
	}
*/




	/*
	@SuppressWarnings("unchecked")
	@RequestMapping("/admin/adminView.do")
	public ModelAndView findAdminInfo(HttpServletRequest request, @RequestParam Map<String, Object> map) {
		ModelAndView mav = new ModelAndView();

 		pMap  = super.getParameterMap(request, map);
		
		mav.addObject("adminInfo", adminService.findAdminInfo(pMap));

 		mav.addObject("pMap", pMap);
 		
		return mav;
	}*/

	@RequestMapping(value = "/admin/adminView.do")
	public String findAdminInfo(@RequestParam(value = "userId", required = false) String userId, ModelMap model) throws Exception {

		/*int currentPageNo;
		try {
			currentPageNo = Integer.parseInt(pageNo);
		} catch (Exception e) {
			currentPageNo = 1;
		}*/
		/*
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(currentPageNo);
		paginationInfo.setRecordCountPerPage(3);
		paginationInfo.setPageSize(8);
*/
		HashMap<String, Object> map = new HashMap<String, Object>(1);
		map.put("userId", userId);
		
		LOGGER.debug("userId => " + userId);
		
		AdminInfo adminInfo = adminService.findAdminInfo(map);
		//model.addAttribute("adminInfo", adminInfo);
		//model.addAttribute("searchCriteria", searchCriteria);

		//int employeeCount = adminService.get employeeService.getEmployeeCount(commandMap);
		/*
		paginationInfo.setTotalRecordCount(10);
		model.addAttribute("paginationInfo", paginationInfo);
*/
		HashMap<String, Object> roleMap = new HashMap<String, Object>(2);
		roleMap.put("lookupTypeName", "50000");
		roleMap.put("searchKeyword", adminInfo.getRoleCode());
		List<CodeInfo> roleList = codeService.findSelectedCodeList(roleMap);
		
		model.addAttribute("adminInfo", adminInfo);
		model.addAttribute("roleList", roleList);
		return "admin/adminView";
	}
	
	
	@RequestMapping(value = "/admin/adminModify.do")
	public String modifyAdmin(@RequestParam(value = "userId", required = false) String userId, ModelMap model) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>(1);
		map.put("userId", userId);
		
		LOGGER.debug("userId => " + userId);
		
		AdminInfo adminInfo = adminService.findAdminInfo(map);

		HashMap<String, Object> roleMap = new HashMap<String, Object>(2);
		roleMap.put("lookupTypeName", "50000");
		roleMap.put("searchKeyword", "%");
		List<CodeInfo> roleList = codeService.findSelectedCodeList(roleMap);
		
		model.addAttribute("adminInfo", adminInfo);
		model.addAttribute("roleList", roleList);
		return "admin/adminModify";
	}
	
/*
	@SuppressWarnings("unchecked")
	@RequestMapping("/admin/adminModify.do")
	public ModelAndView modifyAdmin(HttpServletRequest request, @RequestParam Map<String, Object> map) {
		ModelAndView mav = new ModelAndView();

 		pMap  = super.getParameterMap(request, map);		
		
		mav.addObject("adminInfo", adminService.findAdminInfo(pMap));

		// 메뉴 권한 리스트 조회
		//mav.addObject("menuAuthList", adminService.findMenuAuthList());
 		mav.addObject("pMap", pMap);
 		
		return mav;
	}
*/
	@RequestMapping("/admin/adminCreate.do")
	public String createAdmin(@RequestParam Map<String, Object> map, ModelMap model) {

		
		HashMap<String, Object> roleMap = new HashMap<String, Object>(2);
		roleMap.put("lookupTypeName", "50000");
		roleMap.put("searchKeyword", "%");
		List<CodeInfo> roleList;
		try {
			roleList = codeService.findSelectedCodeList(roleMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			roleList = null;
		}
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("param", map);


		return "admin/adminCreate";
	}

	
	@RequestMapping("/admin/adminInsert.do")
	public @ResponseBody List<String> insertAdmin(HttpServletRequest request, @ModelAttribute AdminInfo adminInfo) {
		
		AdminInfo loginInfo = (AdminInfo) WebUtils.getSessionAttribute(request, "UserAccount");
		
		HashMap<String, Object> pMap = new HashMap<String, Object>();

		int success = 0;
		
		if(loginInfo != null){
			//return true;
			if (adminInfo != null) {
				pMap.put("roleCode", adminInfo.getRoleCode());
				pMap.put("userLocalName", adminInfo.getUserLocalName());
				pMap.put("userName", adminInfo.getUserName());
				pMap.put("userId", adminInfo.getUserId());
				pMap.put("loginId", loginInfo.getUserId());
			}
			
		}else{
//			ModelAndView modelAndView = new ModelAndView("redirect:/login.do");			
//			throw new ModelAndViewDefiningException(modelAndView);
			success = -1;
		}

		
		try {
			success = adminService.insertAdmin(pMap);
		} catch (Exception e) {
			success = -1;
		} 

		List<String> listResp = new ArrayList<String>(1);
		listResp.add(String.valueOf(success));
		
		return listResp;
	}

	@RequestMapping("/admin/adminUpdate.do")
	public @ResponseBody List<String> updateAdmin(HttpServletRequest request, @ModelAttribute AdminInfo adminInfo) {

		HashMap<String, Object> pMap = new HashMap<String, Object>();
		

		AdminInfo loginInfo = (AdminInfo) WebUtils.getSessionAttribute(request, "UserAccount");
		
		int success = 0;
		
		if(loginInfo != null){
			if (adminInfo != null) {
				pMap.put("roleCode", adminInfo.getRoleCode());
				pMap.put("userLocalName", adminInfo.getUserLocalName());
				pMap.put("userName", adminInfo.getUserName());
				pMap.put("userId", adminInfo.getUserId());
				pMap.put("loginId", loginInfo.getUserId());
			}
			
		}else{
//			ModelAndView modelAndView = new ModelAndView("redirect:/login.do");			
//			throw new ModelAndViewDefiningException(modelAndView);
			success = -1;
		}

		
		
		
		try {
			success = adminService.updateAdmin(pMap);
		} catch (Exception e) {
			success = -1;
		} 


		List<String> listResp = new ArrayList<String>(1);
		listResp.add(String.valueOf(success));
		
		return listResp;
	}


	@RequestMapping("/admin/adminDelete.do")
	public ModelAndView deleteAdmin(@RequestParam(value = "userId", required = false) List<String> userId) {

		ModelAndView mav = new ModelAndView("jsonView");

		boolean status = true;
		
		try {
			adminService.deleteAdmin(userId);
		} catch (Exception e) {
			LOGGER.debug("exception is occurred");
			LOGGER.debug("e -> " + e.toString());
			
			status = false; 
		}


		mav.addObject("status", status);


		LOGGER.debug("deleteAdmin ended.");
 		
		return mav;
	}

	
	@RequestMapping("/login/login.do")
	public ModelAndView login(HttpServletRequest request) {
	
		ModelAndView mav = new ModelAndView();
		String OAM_UID = request.getHeader("OAM_UID")==null?"":request.getHeader("OAM_UID").toString();

		String url = request.getRequestURL().toString();

		String checkSSO = "Y";// AdminConstants.USE_YN_Y;
		
		if(url.contains(egovMessageSource.getMessage("cms.dev.ip")) || url.contains(egovMessageSource.getMessage("cms.local.ip"))){
			checkSSO = "N";//AdminConstants.USE_YN_N;
			mav.addObject("checkSSO", checkSSO);
		} else {
			if(!OAM_UID.equals("")){
				mav.setViewName("redirect:/login/actionLogin.do?userName="+OAM_UID+"&checkSSO="+checkSSO);
			} else {
				if (url.contains(egovMessageSource.getMessage("cms.stg.ip")) || url.contains(egovMessageSource.getMessage("cms.stg.domain"))){
					mav.setViewName("redirect:".concat(egovMessageSource.getMessage("cms.stg.sso.url")));
				} else if (url.contains(egovMessageSource.getMessage("cms.prd.ip")) || url.contains(egovMessageSource.getMessage("cms.prd.domain"))){
					mav.setViewName("redirect:".concat(egovMessageSource.getMessage("cms.prd.sso.url")));
				}
			}
		}
		
		return mav;
	}

	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();

		request.getSession().removeAttribute("UserAccount");

		mav.setViewName("redirect:/login/login.do");
//		mav.setViewName("redirect:https://ssoint.koreanair.com/ssoidm/common/ssologout.jsp?returl=http://pplstgap.koreanair.com/cms/");
		// 기존 경로 "redirect:/index.jsp"
		return mav;
	}

	

	@RequestMapping("/login/actionLogin.do")
	public ModelAndView actionLogin(HttpServletRequest request, 
			@RequestParam("userName") String userName, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");

		List<String> objList = new ArrayList<String>(1);
		
		

		int loginId = -1;
		
		try
		{
			loginId = Integer.parseInt(userName);
		}
		catch(Exception ex)
		{
			loginId = -1;
		}
		
 		HashMap<String, Object> pMap = new HashMap<String, Object>(2);
 		pMap.put("loginId", loginId);
		pMap.put("userName", userName);
		
		AdminInfo adminInfo = adminService.findAdminLogin(pMap);

        if (adminInfo != null) {
            request.getSession().setAttribute("UserAccount", adminInfo);
            //model.addAttribute("authorized", "true");
            
            objList.add("T");
System.out.println("authorized - " + model.get("authorized"));            
            //return "redirect:/admin/adminList.do?topNum=4";
        } else {
        	//model.addAttribute("authorized", "false");
System.out.println("not authorized - " + model.get("authorized"));        	
        	//return "login/login";
			objList.add("F");
        }
        

		mav.addObject("authorized", objList);
        
		return mav;
	}
}
