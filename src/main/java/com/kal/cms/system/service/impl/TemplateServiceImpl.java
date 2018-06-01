package com.kal.cms.system.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.common.constants.AdminConstants;
import com.kal.cms.system.mapper.TemplateMapper;
import com.kal.cms.system.service.TemplateService;
import com.kal.cms.system.vo.TemplateInfo;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {

	@Resource(name = "templateMapper")
	private TemplateMapper templateMapper;

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Override
	public List<TemplateInfo> findTemplateList(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return templateMapper.getTemplateList(pMap);
	}

	@Override
	public TemplateInfo getTemplate(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return templateMapper.getTemplate(pMap);
	}

	@Override
	public boolean registTemplate(HashMap<String, Object> pMap, File tempFile) throws Exception {
		// TODO Auto-generated method stub
		boolean isSucess = false;

		// 9001 : Operation, 9002 : Route
		String filePath = pMap.get("masterWOCntsTmptTypCd").equals("9001")
				? egovMessageSource.getMessage("template.file.operation.path")
				: egovMessageSource.getMessage("template.file.route.path")
						.concat(egovMessageSource.getMessage("template.file.kor.path"))
						.concat(AdminConstants.FILE_SEPARATOR
								+ ((String) pMap.get("masterWOCntsTmptTypCd")).toLowerCase())
						.concat(AdminConstants.FILE_SEPARATOR + ((String) pMap.get("templateName")).toLowerCase());

		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// file copy
		String fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + tempFile.getName();
		dir = new File(fileFullPath);

		tempFile.renameTo(dir);

		pMap.put("fileName", dir.getName());
		pMap.put("urlAddress", fileFullPath);

		pMap.put("userIdV", (String) pMap.get("userId"));
		pMap.put("userId",
				StringUtils.isNumeric((String) pMap.get("userId")) == true ? (String) pMap.get("userId") : -1);

		try {
			String act = (String) pMap.get("act");
			if ("modify".equals(act)) {
				templateMapper.updateTemplate(pMap);
			} else {
				templateMapper.addTemplate(pMap);
			}

			isSucess = true;
		} catch (Exception e) {
			System.out.println(" e.getMessage() ?  " + e.getMessage());
			isSucess = false;
		}

		return isSucess;
	}

	@Override
	public boolean updateTemplate(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		boolean isSucess = false;

		if (templateMapper.updateTemplate(pMap) == 1) {
			isSucess = true;
		}

		return isSucess;
	}

}
