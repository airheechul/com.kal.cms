package com.kal.cms.system.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupListVO;
import com.kal.cms.system.vo.LookupVO;
import com.kal.cms.system.vo.ResponseVO;

@Controller
public class CodeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeController.class);
	
    @Autowired
    private CodeService codeService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

    @RequestMapping(value = "/system/codeList.do")
    public String findCodeList(ModelMap model,
			@RequestParam Map<String, Object> map) {

        map.put("groupCd","all");

        List<CodeInfo> codeList;
		try {
			codeList = codeService.findCodeList((HashMap<String, Object>)map);
		} catch (Exception e) {
			codeList = null;
		}
        model.addAttribute("codeList", codeList);
        
        return "system/codeList";
    }


    @RequestMapping(value = "/system/ajaxInsertCode.do")
    public ResponseEntity<ResponseVO> ajaxInsertCode(HttpServletRequest request, @ModelAttribute CodeInfo codeInfo, ModelMap model) throws Exception {
    	
    	LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    	
    	ResponseVO responseVO = new ResponseVO();

        HashMap<String, Object> pMap = new HashMap<String, Object>(2);
        pMap.put("lookupTypeName", codeInfo.getLookupTypeName());
        pMap.put("lookupCode", codeInfo.getLookupCode());
        
        
        int isExist = codeService.findExistCodeCount(pMap);

        LOGGER.debug("isExist => " + isExist);
        
        if (isExist > 0) {
        	responseVO.setMsgCode("E");
        	responseVO.setMsgText("사용하고 있는 코드 값 입니다.");
        	//jsonObject.put("success", false);
            //jsonObject.put("message", "사용하고 있는 코드 값 입니다.");
        } else {
        	/* 중복된 코드가 아닐경우 코드 등록 */
            if (codeService.insertCode(pMap) > 0) {
            	responseVO.setMsgCode("S");
            	responseVO.setMsgText("등록되었습니다.");
            	
//            	model.addAttribute("codeList", codeList);
//                jsonObject.put("success", true);
//                jsonObject.put("message", "등록되었습니다.");
            } else {
            	responseVO.setMsgCode("E");
            	responseVO.setMsgText("등록이 실패하였습니다.");
//            	jsonObject.put("success", false);
//            	jsonObject.put("message", "등록이 실패하였습니다.");
            }
        }
        
        return new ResponseEntity<ResponseVO>(responseVO, HttpStatus.OK);
        
    } 


    @RequestMapping(value = "/system/ajaxUpdateCode.do")
    public ResponseEntity<ResponseVO> ajaxUpdateCode(HttpServletRequest request, @ModelAttribute CodeInfo codeInfo, ModelMap model) throws Exception {

    	LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());
    	
    	ResponseVO responseVO = new ResponseVO();

        HashMap<String, Object> pMap = new HashMap<String, Object>(2);
        pMap.put("lookupTypeName", codeInfo.getLookupTypeName());
        pMap.put("lookupCode", codeInfo.getLookupCode());
        
        /* 공통 코드 수정 */
        if (codeService.updateCode(pMap) > 0) {
        	responseVO.setMsgCode("S");
        	responseVO.setMsgText("등록되었습니다.");
        } else {
        	responseVO.setMsgCode("E");
        	responseVO.setMsgText("정보 수정이 실패하였습니다.");
        }

        return new ResponseEntity<ResponseVO>(responseVO, HttpStatus.OK);

    }


/*    @RequestMapping(value = "/system/ajaxDeleteCode.do")
    public ModelAndView ajaxDeleteCode(HttpServletRequest request, @RequestParam Map<String , Object> pMap) throws Exception {

        
        LOGGER.debug("lookupTypeName : " + (String)pMap.get("lookupTypeName"));
        LOGGER.debug("lookupCode : " + (String)pMap.get("lookupCode"));
        
        JSONObject jsonObject = new JSONObject();

        if (codeService.deleteCode(pMap)) {
            jsonObject.put("success", true);

            LoadCodeData.getInstance().init();
        } else {
        	jsonObject.put("success", false);
        	jsonObject.put("message", "정보 수정이 실패하였습니다.");
        }

        mav.addObject("json", jsonObject);
        return mav;

    }*/

    @RequestMapping(value = "/system/ajaxDeleteCode.do")
    public @ResponseBody List<String> ajaxDeleteCode(HttpServletRequest request, @RequestParam Map<String , Object> pMap) {
        
        LOGGER.info("lookupTypeName : " + (String)pMap.get("lookupTypeName"));
        LOGGER.info("lookupCode : " + (String)pMap.get("lookupCode"));
        
		List<String> listResp = new ArrayList<String>(1);
		
        try {
			if(codeService.deleteCode((HashMap<String, Object>) pMap) > 0){
				listResp.add("T");
			}else{
				listResp.add("F");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return listResp;
    }
    

    @RequestMapping(value = "/system/ajaxCheckCodeId.do")
    public @ResponseBody List<String> ajaxCheckCodeId(HttpServletRequest request, @RequestParam Map<String , Object> pMap) {
        
        LOGGER.info("lookupTypeName : " + (String)pMap.get("lookupTypeName"));
        LOGGER.info("lookupCode : " + (String)pMap.get("lookupCode"));
        
		List<String> listResp = new ArrayList<String>(1);
		
        try {
			int iRecordCount = codeService.findExistCodeCount((HashMap<String, Object>) pMap);
			if(iRecordCount > 0){
				listResp.add("T");
			}else{
				listResp.add("F");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return listResp;
    }
    
    
    @RequestMapping(value = "/system/combolist.do")
    public @ResponseBody List<LookupListVO> findComboListGroup(ModelMap model,
			@RequestParam Map<String, Object> map) {

    	LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

        List<LookupListVO> hmComboGoup;
		try {
			hmComboGoup = codeService.findComboList();
		} catch (Exception e) {
			hmComboGoup = null;
		}
		
        model.addAttribute("hmComboGoup", hmComboGoup);
        
        return hmComboGoup;
    }
}
