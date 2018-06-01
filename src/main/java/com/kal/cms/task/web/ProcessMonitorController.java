package com.kal.cms.task.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
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

import com.kal.cms.common.util.Util;
import com.kal.cms.content.service.ContentProcessService;
import com.kal.cms.content.service.ContentSearchService;
import com.kal.cms.content.vo.ContentInfo;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupListVO;
import com.kal.cms.system.vo.LookupVO;
import com.kal.cms.task.service.ProcessMonitorService;
import com.kal.cms.task.vo.TaskDetailInfo;
import com.kal.cms.task.vo.TaskInfo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
//import com.kal.cms.common.constants.AdminConstants;

//import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

//import org.apache.commons.lang.StringUtils;

@Controller
public class ProcessMonitorController {
	
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessMonitorController.class);

	@Autowired
	private ProcessMonitorService processMonitorService;

	@Autowired
	private CodeService codeService;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@RequestMapping("/task/taskList.do")
	public String taskList(
			@RequestParam(value = "pageIndex", defaultValue = "1", required = false) String pageIndex, ModelMap model,
			@RequestParam Map<String, Object> pMap) {

		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

		int currentPageNo;
		try {
			currentPageNo = Integer.parseInt(pageIndex);
		} catch (Exception e) {

			LOGGER.debug("currentPageNo error => " + e.toString());

			currentPageNo = 1;
		}

		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(currentPageNo);
		paginationInfo.setRecordCountPerPage(10);
		paginationInfo.setPageSize(10);

		HashMap<String, Object> searchMap = getSearchParams((HashMap<String, Object>) pMap);

		pMap.putAll(searchMap);

		pMap.put("startRow", paginationInfo.getFirstRecordIndex());
		pMap.put("endRow", paginationInfo.getLastRecordIndex());
		pMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());

		int contentCount = 0;
		String act = (String) pMap.get("act");
		String searchRequest = pMap.get("searchRequest") == null ? "" : (String) pMap.get("searchRequest");
		if ("search".equals(act)) {
			if ("".equals(searchRequest)) {

				if (pMap.get("isDescriptionSearch").equals("Y")) {
					pMap.put("description", "requestId:".concat((String) pMap.get("searchRequest")));
				} else {
					pMap.put("createdBy", (String) pMap.get("searchRequest"));
				}

				List<TaskInfo> taskList;
				try {
					taskList = processMonitorService.findTaskList((HashMap<String, Object>) pMap);
					model.addAttribute("taskList", taskList);
					contentCount = processMonitorService.getTaskTotalCount((HashMap<String, Object>)pMap);//taskList.size();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} else {
			// 0504 model.addAttribute("contentSearchList", new PageList());
		}
		// preview properties
		model.addAttribute("previewBaseUrl", egovMessageSource.getMessage("paperless.base.url"));
		model.addAttribute("previewUrl", egovMessageSource.getMessage("content.search.preview.paperless.url"));

		List<LookupVO> LOVs;
		try {
			LOVs = codeService.findLOVs();
		} catch (Exception e) {
			LOVs = null;
		}

		model.addAttribute("LOVs", LOVs);
		model.addAttribute("searchMap", searchMap);
		// model.addAttribute("searchCriteria", searchCriteria);

		paginationInfo.setTotalRecordCount(contentCount);
		model.addAttribute("paginationInfo", paginationInfo);

		return "task/taskList";
	}

	@RequestMapping("/task/taskDetailLogPop.do")
	public ModelAndView taskDetailLogPop(HttpServletRequest request,
			@RequestParam Map<String, Object> pMap) throws Exception {
		ModelAndView mav = null;

		List<TaskDetailInfo> list = processMonitorService.findTaskDetailLogList((HashMap<String, Object>)pMap);


		if (pMap.get("prevDetailLogId") != null) {
			mav = new ModelAndView("jsonView");
			JSONObject jsonObject = new JSONObject();

			JSONArray jsonArray = new JSONArray();
			for (TaskDetailInfo data : list) {
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("detailLogId", data.getDetailLogId());
				jsonObj.put("detailLog", data.getDetailLog());
				jsonObj.put("logDateStr", data.getLogDateStr());
				
				jsonArray.put(jsonObj);
			}

			jsonObject.put("taskDetailLog", jsonArray);

			mav.addObject("json", jsonObject);
		} else {
			mav = new ModelAndView();

			if (list != null && list.size() > 0) {
				TaskDetailInfo lastMember = list.get(list.size() - 1);
				mav.addObject("prevDetailLogId", lastMember.getDetailLogId());
			} else {
				mav.addObject("prevDetailLogId", "0");
			}

			mav.addObject("taskDetailLog", list);
			mav.addObject("pMap", pMap);
		}

		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/taskDelete.do")
	public ModelAndView taskDelete(HttpServletRequest request, @RequestParam Map<String, Object> pMap) throws Exception {
 		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject jsonObject = new JSONObject();	
 		
 		System.out.println("manage act == " + pMap.get("act"));
 		
 		String  act = (String)pMap.get("act");
 		String[] taskIds = request.getParameterValues("checkedId");
 		pMap.put("taskIds", taskIds);
 		
 		try {
 			boolean chkResult = processMonitorService.taskDelete((HashMap<String, Object>)pMap);
 			
 			if(chkResult){
 				jsonObject.put("result", chkResult);
 			} else {
 				jsonObject.put("result", chkResult);
 				jsonObject.put("errorMsg", pMap.get("errorMsg"));
 			}

 		} catch (Exception e) {
 			e.printStackTrace();
 			jsonObject.put("result", false);
 			jsonObject.put("errorMsg", "서버오류 입니다. ");
 		}

		mav.addObject("json", jsonObject);

		return mav;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/taskAbort.do")
	public ModelAndView taskAbort(HttpServletRequest request, @RequestParam Map<String, Object> pMap) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject jsonObject = new JSONObject();	
 		
 		System.out.println("manage act == " + pMap.get("act"));
 		
 		String[] taskIds = request.getParameterValues("checkedId");
 		pMap.put("taskIds", taskIds);
 		
 		try {
 			boolean chkResult = processMonitorService.taskAbort((HashMap<String, Object>)pMap);
 			
 			if(chkResult){
 				jsonObject.put("result", chkResult);
 			} else {
 				jsonObject.put("result", chkResult);
 				jsonObject.put("errorMsg", pMap.get("errorMsg"));
 			}

 		} catch (Exception e) {
 			e.printStackTrace();
 			jsonObject.put("result", false);
 			jsonObject.put("errorMsg", "서버오류 입니다. ");
 		}

		mav.addObject("json", jsonObject);

		return mav;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/taskResume.do")
	public ModelAndView taskResume(HttpServletRequest request, @RequestParam Map<String, Object> pMap) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject jsonObject = new JSONObject();	
		
		AdminInfo loginInfo = (AdminInfo) WebUtils.getSessionAttribute(request, "UserAccount");
		
		if(loginInfo != null){

			pMap.put("userId", loginInfo.getUserId());
	 		
	 		System.out.println("manage act == " + pMap.get("act"));
	 		
	 		String  act = (String)pMap.get("act");
	 		String[] taskIds = request.getParameterValues("checkedId");
	 		pMap.put("taskIds", taskIds);
	 		
	 		try {
	 			
	 			boolean chkResult = processMonitorService.taskResume((HashMap<String, Object>)pMap);
	 			
	 			if(chkResult){
	 				jsonObject.put("result", chkResult);
	 			} else {
	 				jsonObject.put("result", chkResult);
	 				jsonObject.put("errorMsg", pMap.get("errorMsg"));
	 			}

	 		} catch (Exception e) {
	 			e.printStackTrace();
	 			jsonObject.put("result", false);
	 			jsonObject.put("errorMsg", "서버오류 입니다. ");
	 		}
			
		}else{
//			ModelAndView modelAndView = new ModelAndView("redirect:/login.do");			
//			throw new ModelAndViewDefiningException(modelAndView);
 			jsonObject.put("result", false);
 			jsonObject.put("errorMsg", "서버오류 입니다. ");
		}



		mav.addObject("json", jsonObject);

		return mav;
	}
	
	
	@RequestMapping("/task/sgmlFlowPop.do")
	public ModelAndView sgmlFlowPop(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		List<String> modelList = new ArrayList<String>();
		modelList.add("A330");
		modelList.add("A380");
		modelList.add("B737");
		modelList.add("B747-400");
		modelList.add("B747-8");
		modelList.add("B777");
		mav.addObject("modelList", modelList);
		
		List<String> manualTypeList = new ArrayList<String>();
		manualTypeList.add("AMM");
		manualTypeList.add("FRMFIM");
		manualTypeList.add("TSM");
		manualTypeList.add("TR");
		mav.addObject("manualTypeList", manualTypeList);
		
		// 위 코드값은 DB의 코드 테이블을 참조하도록 수정됨.
		
		// 임시 경로 세팅.
		String basePath = FILE_SEPARATOR+"kalppl"+FILE_SEPARATOR+"sgml"+FILE_SEPARATOR+"B777"+FILE_SEPARATOR;
		String destFile = "SGML"+FILE_SEPARATOR+"TEMP_AMM.xml";
		String cgmDTDPath = "SGML"+FILE_SEPARATOR+"AMM_CGM.DTD";
		String sgmFilePath = "SGML"+FILE_SEPARATOR+"AMM.SGM";
		String imageFile = "SGML"+FILE_SEPARATOR+"CGM";
		mav.addObject("basePath", basePath);
		mav.addObject("destFile", destFile);
		mav.addObject("cgmDTDPath", cgmDTDPath);
		mav.addObject("sgmFilePath", sgmFilePath);
		mav.addObject("imageFile", imageFile);
		
		return mav;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/task/getUserData.do")
	public ModelAndView getUserData(HttpServletRequest request) throws Exception {
 		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject jsonObject = new JSONObject();

 		try {
 			JSONObject userData = Util.getJsonUserData(egovMessageSource.getMessage("task.userdata.file.path"));
 			
			jsonObject.put("result", true);
			jsonObject.put("userData", userData);
 		} catch (Exception e) {
 			e.printStackTrace();
 			jsonObject.put("result", false);
 			jsonObject.put("errorMsg", "서버오류 입니다. ");
 		}

		mav.addObject("json", jsonObject);

		return mav;
	}

	
	
    private HashMap<String, Object> getSearchParams(HashMap<String, Object> pMap) {
    	HashMap<String, Object> params = new HashMap<String, Object>();
    	
		params.put("searchMaintDocModelCode",  (pMap.get("searchMaintDocModelCode") == null ? "":pMap.get("searchMaintDocModelCode")));
		params.put("searchWOCntsDocSubtypCd",  (pMap.get("searchWOCntsDocSubtypCd") == null ? "":pMap.get("searchWOCntsDocSubtypCd")));
		params.put("searchMasterWOCntsTypCd",  (pMap.get("searchMasterWOCntsTypCd") == null ? "":pMap.get("searchMasterWOCntsTypCd")));
		params.put("searchReleaseYn",          (pMap.get("searchReleaseYn") == null ? "":pMap.get("searchReleaseYn")));
		params.put("searchMasterWOCntsSttsCd", (pMap.get("searchMasterWOCntsSttsCd") == null ? "":pMap.get("searchMasterWOCntsSttsCd")));
		params.put("searchAuthor",             (pMap.get("searchAuthor") == null ? "":pMap.get("searchAuthor")));
		params.put("searchFileName",           (pMap.get("searchFileName") == null ? "":pMap.get("searchFileName")));
		params.put("searchValue",              (pMap.get("searchValue") == null ? "":pMap.get("searchValue")));
		params.put("searchStartDt",            (pMap.get("searchStartDt") == null ? "":pMap.get("searchStartDt")));
		params.put("searchEndDt",              (pMap.get("searchEndDt") == null ? "":pMap.get("searchEndDt")));

		return params;
    }
    
}
