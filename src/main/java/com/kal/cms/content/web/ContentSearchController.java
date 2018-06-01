package com.kal.cms.content.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.kal.cms.common.constants.AdminConstants.ContentSearch;
import com.kal.cms.content.service.ContentProcessService;
import com.kal.cms.content.service.ContentSearchService;
import com.kal.cms.content.vo.ContentInfo;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupListVO;
import com.kal.cms.system.vo.LookupVO;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
//import com.kal.cms.common.constants.AdminConstants;

//import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

//import org.apache.commons.lang.StringUtils;


@Controller
public class ContentSearchController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentSearchController.class);
	
	@Autowired
	private ContentSearchService contentSearchService;
	
	@Autowired
	private ContentProcessService contentProcessService;
	
	@Autowired
	private CodeService codeService;

    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    

    @RequestMapping("/content/searchList.do")
	public String forwardContentMain(@RequestParam(value = "pageIndex", defaultValue = "1", required = false) String pageIndex, ModelMap model,
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

		HashMap<String, Object> searchMap = getSearchParams((HashMap<String, Object>)pMap);
		
		pMap.putAll(searchMap);


		pMap.put("startRow", paginationInfo.getFirstRecordIndex());
		pMap.put("endRow", paginationInfo.getLastRecordIndex());
		pMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		
		int contentCount = 0;
 		String act = (String)pMap.get("act");
 		if("search".equals(act)){
 			if(ContentSearch.CONTENT_TYPE_IMG.equals(pMap.get("searchMasterWOCntsTypCd"))){
 				pMap.put("searchFileNameGb", "S");
 				//0504 model.addAttribute("contentSearchList", contentProcessService.findContentImageList(pMap));
 			} else {
 				List<ContentInfo> contentSearchList;
				try {
					contentSearchList = contentSearchService.findContentSearchList((HashMap<String, Object>)pMap);
	 				model.addAttribute("contentSearchList", contentSearchList);
	 				contentCount = contentSearchList.size();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 				

 			}
 		} else {
 			//0504 model.addAttribute("contentSearchList", new PageList());
 		}
 		//preview properties
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
		//model.addAttribute("searchCriteria", searchCriteria);

		
		paginationInfo.setTotalRecordCount(contentCount);
		model.addAttribute("paginationInfo", paginationInfo);
 		

		return "content/searchList";
	}
    
    
    @RequestMapping("/content/findContentList.do")
 	public String findContentSearchList(@RequestParam(value = "pageIndex", defaultValue = "1", required = false) String pageIndex, ModelMap model,
 			@RequestParam Map<String, Object> pMap) {
 		
  		
  		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

 		SearchKeywordVO searchKeywordVO = new SearchKeywordVO();
 		
 		int currentPageNo;
 		try {
 			currentPageNo = Integer.parseInt(pageIndex);
 		} catch (Exception e) {
 			
 			LOGGER.debug("currentPageNo error => " + e.toString());
 			
 			currentPageNo = 1;
 		}

//     	Iterator iter = pMap.entrySet().iterator();
//     	while(iter.hasNext()) {
//     	    Entry<String, Object[]> entry =  (Entry<String, Object[]>) iter.next();
//     	    String key = entry.getKey();
//     	    Object value = entry.getValue()[0];
//     	    
//     	    LOGGER.debug("key / value => " + key + "/" + value);
//     	}

 		PaginationInfo paginationInfo = new PaginationInfo();
 		paginationInfo.setCurrentPageNo(currentPageNo);
 		paginationInfo.setRecordCountPerPage(10);
 		paginationInfo.setPageSize(10);

 		if (pMap == null || pMap.size() == 0)
 		{
 			System.out.println("pMap is null");
 			
 			pMap.put("searchKeywordType", "");
 			pMap.put("searchKeyword", "");
 			 
 		}
 		else
 		{
 			if (pMap.get("searchKeywordType") == null)
 			{
 				pMap.put("searchKeywordType", "");
 				pMap.put("searchKeyword", "");
 			}
 		}
 		
 		
 		searchKeywordVO.setSearchKeywordType(pMap.get("searchKeywordType"));
 		searchKeywordVO.setSearchKeyword(pMap.get("searchKeyword"));


 		pMap.put("startRow", paginationInfo.getFirstRecordIndex());
 		pMap.put("endRow", paginationInfo.getLastRecordIndex());
 		pMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
// 		pMap.put("startRow", 1);
// 		pMap.put("endRow", 20);
// 		pMap.put("recordCountPerPage", 10);		
 		
 		int contentCount = 0;
  		String act = (String)pMap.get("act");
  		if("search".equals(act)){
  			if(ContentSearch.CONTENT_TYPE_IMG.equals(pMap.get("searchMasterWOCntsTypCd"))){
  				pMap.put("searchFileNameGb", "S");
  				//0504 model.addAttribute("contentSearchList", contentProcessService.findContentImageList(pMap));
  			} else {
  				List<ContentInfo> contentSearchList;
 				try {
 					contentSearchList = contentSearchService.findContentSearchList((HashMap<String, Object>)pMap);
 	 				model.addAttribute("contentSearchList", contentSearchList);
 	 				contentCount = contentSearchList.size();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
  				

  			}
  		} else {
  			//0504 model.addAttribute("contentSearchList", new PageList());
  		}

 		model.addAttribute("searchKeywordVO", searchKeywordVO);
 		//model.addAttribute("searchCriteria", searchCriteria);

 		
 		paginationInfo.setTotalRecordCount(contentCount);
 		model.addAttribute("paginationInfo", paginationInfo);
  		

 		return "content/searchList";
 	}
    
    
    @RequestMapping("/content/checkInContent.do")
	public @ResponseBody List<String> checkInContent(HttpServletRequest request, @RequestParam Map<String, Object> pMap) {
		
		AdminInfo loginInfo = (AdminInfo) WebUtils.getSessionAttribute(request, "UserAccount");

		int success = 0;
		
		if(loginInfo != null){

			pMap.put("userId", loginInfo.getUserId());
				
			try {
				contentProcessService.execCheckIn((HashMap<String, Object>)pMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
//			ModelAndView modelAndView = new ModelAndView("redirect:/login.do");			
//			throw new ModelAndViewDefiningException(modelAndView);
			success = -1;
		}


		List<String> listResp = new ArrayList<String>(1);
		listResp.add(String.valueOf(success));
		
		return listResp;
	}
   

	public @ResponseBody String releaseContent(HttpServletRequest request, @RequestParam Map<String, Object> pMap) {
		
		boolean isSuccess = false;
		try {
			isSuccess = contentSearchService.updateContentRelease((HashMap<String,Object>)pMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// TODO 추후 Release 구현
		
		if (isSuccess) {
			return "Check In 되었습니다.";
		}
		return "오류가 발생했습니다. 관리자에게 문의하세요.";
	}
	
	
	@RequestMapping("/content/deleteContents.do")
 	public @ResponseBody List<String> deleteContents(@RequestParam(value="contentCheck[]") List<String> contentCheck, ModelMap model) {
 		
  		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

  		//contentCheck.forEach(value -> System.out.println("contentCheck -> " + contentCheck));
 		
 		HashMap<String, Object> param = new HashMap<String, Object>(); 
 		param.put("contentCheck", contentCheck);
 		
 		boolean isSuccess = false;
 		
 		try {
			isSuccess = contentSearchService.updateContentsDelete(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> listResp = new ArrayList<String>(1);
		listResp.add(String.valueOf(isSuccess));
		
		return listResp;
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
