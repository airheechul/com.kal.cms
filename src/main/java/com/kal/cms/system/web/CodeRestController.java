package com.kal.cms.system.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.LookupVO;

@RestController
public class CodeRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeRestController.class);
	
    @Autowired
    private CodeService codeService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
/*
    @RequestMapping(value = "/system/params.do")
    public ResponseEntity<HashMap<String, List<ComboVO>>> findComboListGroup(ModelMap model,
			@RequestParam Map<String, Object> map) {

    	LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

    	HashMap<String, List<ComboVO>> hmComboGoup;
    	

    	
//    	
////        List<CodeInfo> codeList;
//		try {
			hmComboGoup = codeService.findComboList();
//		} catch (Exception e) {
//			codeList = null;
//		}
////        model.addAttribute("codeList", codeList);
    	
        
        return new ResponseEntity<HashMap<String, List<ComboVO>>>(hmComboGoup, HttpStatus.OK);
    }*/

    
}
