package com.kal.cms.system.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.admin.vo.AdminInfo;
import com.kal.cms.common.constants.AdminConstants;
import com.kal.cms.system.service.CodeService;
import com.kal.cms.system.service.TemplateService;
import com.kal.cms.system.vo.CodeInfo;
import com.kal.cms.system.vo.TemplateInfo;

@Controller
public class TemplateController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

	@Autowired
	private TemplateService templateService;

	@Autowired
	private CodeService codeService;

	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@RequestMapping(value = "/system/templateList.do")
	public String findAdminList(ModelMap model, @RequestParam Map<String, Object> map) throws Exception {
		LOGGER.debug("Running Method Name : " + Thread.currentThread().getStackTrace()[1].getMethodName());

		List<TemplateInfo> templateList = templateService.findTemplateList((HashMap<String, Object>) map);
		model.addAttribute("templateList", templateList);

		return "system/templateList";
	}

	@RequestMapping("/system/templateCreate.do")
	public String templeteCreate(@RequestParam Map<String, Object> map, ModelMap model) {

		HashMap<String, Object> contentTypeMap = new HashMap<String, Object>(2);
		contentTypeMap.put("lookupTypeName", "9000");
		contentTypeMap.put("searchKeyword", "%");

		List<CodeInfo> contentTypeList;
		try {
			contentTypeList = codeService.findSelectedCodeList(contentTypeMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			contentTypeList = null;
		}

		model.addAttribute("contentTypeList", contentTypeList);

		return "system/templateCreate";
	}

	@RequestMapping("/system/templateRegist.do")
	public @ResponseBody List<String> insertAdmin(HttpServletRequest request,
			@ModelAttribute TemplateInfo templateInfo) {

		AdminInfo loginInfo = (AdminInfo) WebUtils.getSessionAttribute(request, "UserAccount");

		HashMap<String, Object> pMap = new HashMap<String, Object>();

		int success = 0;

		int totalCount = 0;
		int successCount = 0;
		int failCount = 0;

		try {
			if (request instanceof MultipartHttpServletRequest) {
				if (templateInfo != null) {

					pMap.put("masterWOCntsTmptTypCd", templateInfo.getMasterWOCntsTmptTypCd());
					pMap.put("templateName", templateInfo.getTemplateName());
					pMap.put("userName", loginInfo.getUserName());
					pMap.put("userId", loginInfo.getUserId());
					pMap.put("loginId", loginInfo.getUserId());
					pMap.put("act", "insert");

					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

					List<MultipartFile> multifileInfoList = multiRequest.getFiles("upFile");

					for (int i = 0; i < multifileInfoList.size(); i++) {
						MultipartFile mfile = multifileInfoList.get(i);
						System.out.println(i);
						// System.out.println("file.getName() => " +
						// mfile.getName());
						// System.out.println("file.getOriginalFilename() => " +
						// mfile.getOriginalFilename());
						System.out.println("getContentType() => " + mfile.getContentType());

						try {
							String fileName = mfile.getOriginalFilename().substring(0,
									mfile.getOriginalFilename().lastIndexOf("."));
							String ext = mfile.getOriginalFilename().substring(
									mfile.getOriginalFilename().lastIndexOf(".") + 1,
									mfile.getOriginalFilename().length());

							System.out.println("file name : " + fileName);
							System.out.println("file exe : " + ext);

							File tempFile = new File(egovMessageSource.getMessage("process.upload.file.temp.path"));
							if (!tempFile.exists()) {
								tempFile.mkdirs();
							}

							String tempFileFullPath = egovMessageSource.getMessage("process.upload.file.temp.path")
									.concat(AdminConstants.FILE_SEPARATOR).concat(mfile.getOriginalFilename());
							tempFile = new File(tempFileFullPath);

							mfile.transferTo(tempFile);

							boolean afterRegister = templateService.registTemplate(pMap, tempFile);

							success = (afterRegister) ? 0 : -1;

						} catch (Exception ex) {
							failCount++;
						}
					}

				} else {
					// ModelAndView modelAndView = new
					// ModelAndView("redirect:/login.do");
					// throw new ModelAndViewDefiningException(modelAndView);
					success = -1;
				}
			}

		} catch (Exception ex) {
			LOGGER.debug(ex.toString());
		}

		List<String> listResp = new ArrayList<String>(1);
		listResp.add(String.valueOf(success));

		return listResp;
	}

	@RequestMapping(value = "/system/templateModify.do")
	public String templeteModify(
			@RequestParam(value = "masterWOCntsTmptId", required = false) String masterWOCntsTmptId, ModelMap model)
			throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>(1);
		map.put("masterWOCntsTmptId", masterWOCntsTmptId);

		LOGGER.debug("masterWOCntsTmptId => " + masterWOCntsTmptId);

		TemplateInfo templateInfo = templateService.getTemplate(map);

		HashMap<String, Object> contentTypeMap = new HashMap<String, Object>(2);
		contentTypeMap.put("lookupTypeName", "9000");
		contentTypeMap.put("searchKeyword", "%");

		List<CodeInfo> contentTypeList;
		try {
			contentTypeList = codeService.findSelectedCodeList(contentTypeMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			contentTypeList = null;
		}

		model.addAttribute("templateInfo", templateInfo);
		model.addAttribute("contentTypeList", contentTypeList);

		return "admin/adminModify";
	}

	@RequestMapping("/system/templateUpdate.do")
	public @ResponseBody List<String> templeteUpdate(HttpServletRequest request,
			@ModelAttribute TemplateInfo templateInfo) {

		AdminInfo loginInfo = (AdminInfo) WebUtils.getSessionAttribute(request, "UserAccount");

		HashMap<String, Object> pMap = new HashMap<String, Object>();

		int success = 0;

		int totalCount = 0;
		int successCount = 0;
		int failCount = 0;

		try {
			if (templateInfo != null) {

				pMap.put("masterWOCntsTmptTypCd", templateInfo.getMasterWOCntsTmptTypCd());
				pMap.put("templateName", templateInfo.getTemplateName());
				pMap.put("userName", loginInfo.getUserName());
				pMap.put("userId", loginInfo.getUserId());
				pMap.put("loginId", loginInfo.getUserId());

				if (request instanceof MultipartHttpServletRequest) {
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

					List<MultipartFile> multifileInfoList = multiRequest.getFiles("upFile");

					int fileCount = multifileInfoList.size();

					if (fileCount == 0) {
						pMap.put("act", "modify");

						boolean afterRegister = templateService.updateTemplate(pMap);

						success = (afterRegister) ? 0 : -1;

					} else {
						pMap.put("act", "insert");

						for (int i = 0; i < fileCount; i++) {
							MultipartFile mfile = multifileInfoList.get(i);
							System.out.println(i);
							// System.out.println("file.getName() => " +
							// mfile.getName());
							// System.out.println("file.getOriginalFilename() =>
							// " + mfile.getOriginalFilename());
							System.out.println("getContentType() => " + mfile.getContentType());

							try {
								String fileName = mfile.getOriginalFilename().substring(0,
										mfile.getOriginalFilename().lastIndexOf("."));
								String ext = mfile.getOriginalFilename().substring(
										mfile.getOriginalFilename().lastIndexOf(".") + 1,
										mfile.getOriginalFilename().length());

								System.out.println("file name : " + fileName);
								System.out.println("file exe : " + ext);

								File tempFile = new File(egovMessageSource.getMessage("process.upload.file.temp.path"));
								if (!tempFile.exists()) {
									tempFile.mkdirs();
								}

								String tempFileFullPath = egovMessageSource.getMessage("process.upload.file.temp.path")
										.concat(AdminConstants.FILE_SEPARATOR).concat(mfile.getOriginalFilename());
								tempFile = new File(tempFileFullPath);

								mfile.transferTo(tempFile);

								boolean afterRegister = templateService.registTemplate(pMap, tempFile);

								success = (afterRegister) ? 0 : -1;

							} catch (Exception ex) {
								failCount++;
							}
						}

					}
				}

			} else {
				// ModelAndView modelAndView = new
				// ModelAndView("redirect:/login.do");
				// throw new ModelAndViewDefiningException(modelAndView);
				success = -1;
			}
		} catch (Exception ex) {
			success = -1;
			LOGGER.debug(ex.toString());
		}

		List<String> listResp = new ArrayList<String>(1);
		listResp.add(String.valueOf(success));

		return listResp;
	}

}
