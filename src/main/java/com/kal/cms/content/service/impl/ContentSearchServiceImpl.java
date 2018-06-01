package com.kal.cms.content.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

//import org.apache.commons.http.client.HttpConnectionManager;
//import org.apache.commons.httpclient.HostConfiguration;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
//import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
//import org.apache.commons.httpclient.methods.PutMethod;
//import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.jackrabbit.webdav.client.methods.HttpDelete;
import org.apache.jackrabbit.webdav.client.methods.HttpMkcol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.NodeList;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.common.constants.AdminConstants;
import com.kal.cms.common.constants.AdminConstants.ContentSearch;
import com.kal.cms.common.constants.AdminConstants.TaskConstants;
import com.kal.cms.common.util.XMLUtil;
import com.kal.cms.common.util.XmlXpathUtil;
import com.kal.cms.content.mapper.ContentSearchMapper;
import com.kal.cms.content.service.ContentSearchService;
import com.kal.cms.content.vo.ContentInfo;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("contentSearchService")
public class ContentSearchServiceImpl extends EgovAbstractServiceImpl implements ContentSearchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentSearchServiceImpl.class);

	// @Autowired
	// private AdminDao adminDao;

	// TODO mybatis 사용
	@Resource(name = "contentSearchMapper")
	private ContentSearchMapper contentSearchDao;

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Override
	public List<ContentInfo> findContentSearchList(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return contentSearchDao.getContentSearchList(pMap);
	}

	@Override
	public ContentInfo getContentInfo(HashMap<String, Object> pMap) throws Exception {
		return contentSearchDao.getRevisionInfo(pMap);
	}

	@Override
	public List<ContentInfo> getContentRevisionList(HashMap<String, Object> pMap) throws Exception {
		return contentSearchDao.getContentRevisionList(pMap);
	}

	@Override
	public boolean saveContentFile(MultipartFile file, HashMap<String, Object> pMap) throws Exception {
		return saveMultipartFile(file, pMap);
	}

	@Override
	public boolean saveContentFile(File file, HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addContentNew(HashMap<String, Object> pMap) throws Exception {
		pMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_NEW);
		return addContent(pMap);
	}

	@Override
	public boolean addContentRevise(HashMap<String, Object> pMap) throws Exception {
		pMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_REVISE);
		ContentInfo contentInfo = contentSearchDao.getRevisionInfo(pMap);
		return addContent(pMap);
	}

	@Override
	public boolean updateContentRelease(HashMap<String, Object> pMap) throws Exception {
		if (contentSearchDao.updateContentRelease(pMap) >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateContentsDelete(HashMap<String, Object> pMap) throws Exception {
		pMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_DELETE);
		if (contentSearchDao.updateContentDelete(pMap) >= 1) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String generateWebDavResource(HashMap<String, Object> pMap) throws Exception {
		String webdavUrl = egovMessageSource.getMessage("webdav.repository.url");
		//HttpClient client = this.setHttpClient(webdavUrl);

		File file = null;
		String thirdPath = "";
		String mkColMethodUrl = webdavUrl.concat((String) pMap.get("userId"));
		String mkFileName = "";
		String deleteMethodUrl = "";

		try {
			
	        CloseableHttpClient client = HttpClients.createDefault();
	        
			
			if (pMap.get("xmetalPathType").equals("create")) {
				file = new File((String) pMap.get("templateUrlAddress"));
				thirdPath = (String) pMap.get("templatePath");
				mkFileName = (String) pMap.get("templateFileName");
				deleteMethodUrl = webdavUrl.concat((String) pMap.get("userId")).concat(AdminConstants.FILE_SEPARATOR)
						.concat("create").concat(AdminConstants.FILE_SEPARATOR).concat(thirdPath);
			} else {
				ContentInfo contentInfo = contentSearchDao.getRevisionInfo(pMap);
				String fileFullPath = contentInfo.getUrlAddress().replaceAll("//", "/");
				file = new File(fileFullPath);
				mkFileName = file.getName();

				String revNo = "";
				if (pMap.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_AMM)) {
					revNo = contentInfo.getMasterWOCntsRevzNo() == null ? "" : contentInfo.getMasterWOCntsRevzNo();
				} else if (pMap.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR)) {
					// check in 상태로 rev_no 하나 증가
					if (contentInfo.getReleaseYn().equals(ContentSearch.RELEASE_TYPE_Y)) {
						revNo = String.valueOf((Integer.parseInt(contentInfo.getMasterWOCntsRevzNo()) + 1));
					} else {
						revNo = contentInfo.getMasterWOCntsRevzNo();
					}

				}

				fileFullPath = getStoragePath(contentInfo.getMaintDocModelCode(), contentInfo.getWOCntsDocSubtypCd(),
						contentInfo.getMasterWOCntsDocRevzNo() == null ? "0" : contentInfo.getMasterWOCntsDocRevzNo(),
						contentInfo.getMasterWOCntsTypCd(), revNo).replaceAll("//", "/")
								.concat(AdminConstants.FILE_SEPARATOR).concat(contentInfo.getFileName());

				thirdPath = fileFullPath.substring(0, fileFullPath.lastIndexOf(AdminConstants.FILE_SEPARATOR))
						.replaceAll("/kalppl/hq/manual/", "").replaceAll(AdminConstants.FILE_SEPARATOR, "_");
				deleteMethodUrl = webdavUrl.concat((String) pMap.get("userId")).concat(AdminConstants.FILE_SEPARATOR)
						.concat("revise").concat(AdminConstants.FILE_SEPARATOR).concat(thirdPath);
			}
			// 기존 경로 삭제
			this.deleteFoler(client, deleteMethodUrl);

			/*
			 * 폴더 생성 - 현재 mkColMethod에서는 다중 폴더 생성이 불가(한듯)하여 method를 3번 호출 - 최상위
			 * 폴더, 1depth는 현재 사용자의 아이디, 2depth는 현재 요청이 revise인지 create 인지 구분,
			 * 3depth는 Nas 상대 경로에서 폴더 구분자를 _로 바꾼 형태 - ex)/8204151/create or
			 * revise/kor_b777_kor_0_rt_1/B777_KOR_WT_2013MC_HL7531_200_242.xml
			 */
			this.makeFolder(client, mkColMethodUrl);
			mkColMethodUrl = mkColMethodUrl.concat(AdminConstants.FILE_SEPARATOR)
					.concat((String) pMap.get("xmetalPathType"));
			this.makeFolder(client, mkColMethodUrl);
			mkColMethodUrl = mkColMethodUrl.concat(AdminConstants.FILE_SEPARATOR).concat(thirdPath);
			this.makeFolder(client, mkColMethodUrl);

			// 파일 DTD 변경
			String fileToString = this.replaceDtdFromCmsDomain(file, (String) pMap.get("nowDomain"));

			// 파일 생성
			this.webdavMakeFile(client, mkColMethodUrl, fileToString, mkFileName);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mkColMethodUrl.concat(AdminConstants.FILE_SEPARATOR).concat(mkFileName);
	}

	private boolean addContent(HashMap<String, Object> pMap) {
		try {
			synchronized (contentSearchDao) {
				LOGGER.info("-- Content 추가 작업 트랜잭션 시작 ---");
				// contentSearchDao.startTransaction();

				boolean updateGb = false;
				if (ContentSearch.REVISION_TYPE_REVISE.equals(pMap.get("masterWOCntsSttsCd"))) {
					ContentInfo contentInfo = contentSearchDao.getRevisionInfo(pMap);

					if (contentInfo != null && !"Y".equals(contentInfo.getReleaseYn())) {
						updateGb = true;
						pMap.put("masterWorkorderContentsId", contentInfo.getMasterWorkorderContentsId());
					}
				}

				pMap.put("userIdV", (String) pMap.get("userId"));
				pMap.put("userId", (String) pMap.get("userId"));

				if (updateGb) {
					contentSearchDao.updateContentRevision(pMap);

					return true;
				} else {
					int contentId = contentSearchDao.addContent(pMap);
					LOGGER.debug("contentId[" + contentId + "]");

					// 키워드 등록
					pMap.put("contentId", contentId);
					// 키워드 항목 갯수대로 loop
					for (String keyword : (List<String>) pMap.get("keywordList")) {
						// pMap.put("keyword", keyword); // TODO db 컬럼 사이즈 제한
						// 때문에 아래 코드로 임시 처리
						pMap.put("keyword", keyword.length() > 100 ? keyword.substring(0, 100) : keyword);
						int keywordId = contentSearchDao.addContentKeyword(pMap);

						pMap.put("keywordId", keywordId);
						contentSearchDao.addContentMap(pMap);
					}

					LOGGER.info("-- Content 추가 작업 트랜잭션 commit ---");
					// contentSearchDao.commitTransaction();

					return true;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Content 추가 작업 오류", e);
			return false;
		} finally {
			LOGGER.info("-- Content 추가 작업 트랜잭션 rollback ---");
			// commit 거치지 않고 endTransaction() 호출시 자동 rollback
			// contentSearchDao.endTransaction();
		}
	}

	private String getStoragePath(String maintDocModelCode, String WOCntsDocSubtypCd, String manualRevision,
			String masterWOCntsTypCd, String fileRevision) {

		StringBuffer path = new StringBuffer(egovMessageSource.getMessage("content.upload.file.root.path"));

		if ("KOR".equals(WOCntsDocSubtypCd)) {
			// 2. KOR Content
			// Manual/OEM/ {모델} / {매뉴얼Type} / { 매뉴얼 Revision No } / { Content
			// Type } / { File List }
			path.append(egovMessageSource.getMessage("content.upload.file.kor.path"));
			path.append(AdminConstants.FILE_SEPARATOR.concat(maintDocModelCode.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(WOCntsDocSubtypCd.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(manualRevision));
			if (!ContentSearch.CONTENT_TYPE_IMG.equals(masterWOCntsTypCd)) {
				path.append(AdminConstants.FILE_SEPARATOR.concat(masterWOCntsTypCd.toLowerCase()));
				path.append(AdminConstants.FILE_SEPARATOR.concat(fileRevision));
			} else {
				path.append(AdminConstants.FILE_SEPARATOR.concat("images"));
			}

		} else {
			// 1. OEM Content 의 경우 ( AMM / TSM / FRMFIM )
			// /Manual/KAL/ {모델} / {매뉴얼Type} / { 매뉴얼 Revision No } / { Content
			// Type } / { File REV } / { File List }
			path.append(egovMessageSource.getMessage("content.upload.file.oem.path"));
			path.append(AdminConstants.FILE_SEPARATOR.concat(maintDocModelCode.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(WOCntsDocSubtypCd.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(manualRevision));
			if (!ContentSearch.CONTENT_TYPE_IMG.equals(masterWOCntsTypCd)) {
				path.append(AdminConstants.FILE_SEPARATOR.concat(masterWOCntsTypCd.toLowerCase()));
			} else {
				path.append(AdminConstants.FILE_SEPARATOR.concat("images"));
			}
		}

		return path.toString();
	}

	private String generateFileName2InFileUseCompForOP(String xml) {
		String rtnModel = "";
		String rtnChapnbr = "";
		String retSectnbr = "";
		String retFunc = "";
		String retSeq = "";

		rtnModel = getAttributeValue(xml, "KOR", "MODEL");
		rtnChapnbr = getAttributeValue(xml, "CHAPNBR");
		retSectnbr = getAttributeValue(xml, "SECTNBR");
		retFunc = getAttributeValue(xml, "FUNC");
		retSeq = getAttributeValue(xml, "SEQ");

		return rtnModel.concat("_").concat("KOR").concat("_").concat(rtnChapnbr).concat("_").concat(retSectnbr)
				.concat("_").concat(retFunc).concat("_").concat(retSeq);
	}

	/**
	 * MODEL TYPE COMP OP RT 파일의 속성값으로 조합한 파일명 리턴
	 * 
	 * @param String
	 * @return
	 */
	private String generateFileName2InFileUseCompForRT(String xml) {
		String rtnModel = "";
		String rtnChapnbr = "";
		String retSectnbr = "";
		String retSubjnbr = "";
		String retFunc = "";
		String retSeq = "";

		rtnModel = getAttributeValue(xml, "KOR", "MODEL");
		rtnChapnbr = getAttributeValue(xml, "CHAPNBR");
		retSectnbr = getAttributeValue(xml, "SECTNBR");
		retSubjnbr = getAttributeValue(xml, "SUBJNBR");
		retFunc = getAttributeValue(xml, "FUNC");
		retSeq = getAttributeValue(xml, "SEQ");

		return rtnModel.concat("_").concat("KOR").concat("_").concat(rtnChapnbr).concat("_").concat(retSectnbr)
				.concat("_").concat(retSubjnbr).		concat("_").concat(retFunc).concat("_").concat(retSeq);
	}

	/**
	 * 파일의 속성값으로 조합한 파일명 리턴
	 * 
	 * @param String
	 * @return
	 */
	private String generateFileName2InFile(String xml) {
		String rtnModel = "";
		String rtnChapnbr = "";
		String retSectnbr = "";
		String retSubjnbr = "";
		String retFunc = "";
		String retSeq = "";

		rtnModel = getAttributeValue(xml, "KOR", "MODEL");
		rtnChapnbr = getAttributeValue(xml, "CHAPNBR");
		retSectnbr = getAttributeValue(xml, "SECTNBR");
		retSubjnbr = getAttributeValue(xml, "SUBJNBR");
		retFunc = getAttributeValue(xml, "FUNC");
		retSeq = getAttributeValue(xml, "SEQ");

		return rtnModel.concat("_").concat("KOR").concat("_").concat(rtnChapnbr).concat("_").concat(retSectnbr)
				.concat("_").concat(retSubjnbr).concat("_").concat(retFunc).concat("_").concat(retSeq);
	}

	/**
	 * 태그에 매칭된 속성 값 리턴
	 * 
	 * @param xml
	 * @param tagName
	 * @param attributeName
	 * @return
	 */
	private String getAttributeValue(String xml, String attributeName) {
		XmlXpathUtil xxu = new XmlXpathUtil();
		String tagName = "SUBTASK";
		NodeList nodeList = xxu.getXpathMatchedNodes(xml, "//KOR/SUBTASK");
		if (nodeList == null || nodeList.getLength() == 0) {
			tagName = "TASK";
		}
		List<String> attrValues = xxu.getAttrValuesByElemNameAndAttrName(xml, tagName, attributeName);
		return attrValues.size() == 0 ? "" : attrValues.get(0);
	}

	/**
	 * 태그에 매칭된 속성 값 리턴
	 * 
	 * @param xml
	 * @param tagName
	 * @param attributeName
	 * @return
	 */
	private String getAttributeValue(String xml, String tagName, String attributeName) {
		XmlXpathUtil xxu = new XmlXpathUtil();
		List<String> attrValues = xxu.getAttrValuesByElemNameAndAttrName(xml, tagName, attributeName);
		return attrValues.size() == 0 ? "" : attrValues.get(0);
	}

	/**
	 * WEB DAV 서버에 디렉토리 생성
	 * 
	 * @param client
	 * @param makeUrl
	 */
	public void makeFolder(CloseableHttpClient client, String makeUrl) {
		HttpMkcol mkColMethod = null;
		try {
			mkColMethod = new HttpMkcol(makeUrl);
			//mkColMethod.setDoAuthentication(true);
			
			ResponseHandler<String> responseHandler = response -> {
			    int status = response.getStatusLine().getStatusCode();
			    if (status >= 200 && status < 300) {
			        HttpEntity entity = response.getEntity();
			        return entity != null ? EntityUtils.toString(entity) : null;
			    } else {
			        throw new ClientProtocolException("Unexpected response status: " + status);
			    }
			};
			
			String responseBody = client.execute(mkColMethod, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			
		} catch (Exception e) {
			LOGGER.error("makeFolder Error. Url : " + makeUrl);
			e.printStackTrace();
		} finally {
			mkColMethod.releaseConnection();
		}
	}

	/**
	 * 기존 WEB DAV 서버에 생성된 디렉토리 삭제
	 * 
	 * @param client
	 * @param deleteUrl
	 */
	public void deleteFoler(CloseableHttpClient client, String deleteUrl) {
		
        try {
			HttpDelete httpDelete = new HttpDelete(deleteUrl);

			System.out.println("Executing request " + httpDelete.getRequestLine());

			// Create a custom response handler
			ResponseHandler<String> responseHandler = response -> {
			    int status = response.getStatusLine().getStatusCode();
			    if (status >= 200 && status < 300) {
			        HttpEntity entity = response.getEntity();
			        return entity != null ? EntityUtils.toString(entity) : null;
			    } else {
			        throw new ClientProtocolException("Unexpected response status: " + status);
			    }
			};
			
			String responseBody = client.execute(httpDelete, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("deleteFoler Error Url : " + deleteUrl);
			e.printStackTrace();
		} 
	}

	/**
	 * 신규 생성된 WEB DAV 폴더에 파일 생성
	 * 
	 * @param client
	 * @param fileUrl
	 * @param String
	 * @param fileName
	 * @return
	 */
	public String webdavMakeFile(CloseableHttpClient client, String fileUrl, String xml, String fileName) {
		HttpPut httpPut = null;
		String fileFullUrl = fileUrl.concat(AdminConstants.FILE_SEPARATOR).concat(fileName);
		try {
			httpPut = new HttpPut(fileFullUrl);
			byte[] bytes = xml.getBytes("UTF-8");
			InputStream is = new ByteArrayInputStream(bytes);
			
			httpPut.setEntity(new InputStreamEntity(is, bytes.length));
			
			// Create a custom response handler
			ResponseHandler<String> responseHandler = response -> {
			    int status = response.getStatusLine().getStatusCode();
			    if (status >= 200 && status < 300) {
			        HttpEntity entity = response.getEntity();
			        return entity != null ? EntityUtils.toString(entity) : null;
			    } else {
			        throw new ClientProtocolException("Unexpected response status: " + status);
			    }
			};
			
			String responseBody = client.execute(httpPut, responseHandler);
			System.out.println("----------------------------------------");
			System.out.println(responseBody);
			
		} catch (Exception e) {
			LOGGER.error("webdavMakeFile Error Url : " + fileUrl);
			e.printStackTrace();
			return "";
		} finally {
			httpPut.releaseConnection();
		}
		return fileFullUrl;

	}

	private boolean saveMultipartFile(MultipartFile multipartFile, HashMap<String, Object> pMap) {
		if (multipartFile == null || multipartFile.isEmpty()) {
			LOGGER.error("file이 전달되지 않았습니다.");
			pMap.put("result", "파일이 없습니다.");
			return false;
		}

		try {
			// get file name
			String fileName = multipartFile.getOriginalFilename().substring(0,
					multipartFile.getOriginalFilename().lastIndexOf("."));

			// get file ext
			String ext = multipartFile.getOriginalFilename().substring(
					multipartFile.getOriginalFilename().lastIndexOf(".") + 1,
					multipartFile.getOriginalFilename().length());

			LOGGER.debug("file name : " + fileName);
			LOGGER.debug("file exe : " + ext);

			// ext test
			if ("OP".equals(pMap.get("masterWOCntsTypCd")) || "RT".equals(pMap.get("masterWOCntsTypCd"))) {
				String possibleExt = egovMessageSource.getMessage("content.upload.file.exts").toUpperCase();
				if (possibleExt.indexOf(ext.toUpperCase()) < 0) {
					pMap.put("result", "업로드 가능한 파일 타입이 아닙니다.");
					return false;
				}

			} else {
				pMap.put("result", "컨텐츠 타입이 올바르지 않습니다.");
				return false;
			}

			pMap.put("fileName", multipartFile.getOriginalFilename());
			ContentInfo revInfo = contentSearchDao.getContentNextRevisionSeq(pMap);
			String revSeq = revInfo.getMasterWOCntsRevzNo();
			String docRevSeq = revInfo.getMasterWOCntsDocRevzNo();

			// 아래 조건에 대한 확인 필요.
			/*
			 * CASE WHEN RELEASE_YN = 'Y' THEN 1 ELSE 0 END
			 * MASTER_W_O_CNTS_REVZ_NO if("new".equals(pMap.get("addType")) &&
			 * Integer.parseInt(revSeq) > 0) {
			 */
			// AMM인 경우에만 파일명으로 체크
			if (pMap.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_AMM)) {
				if ("new".equals(pMap.get("addType")) && revInfo.getMasterWorkorderContentsId() != null) {
					pMap.put("result", "이미 등록된 파일이 있습니다.");
					return false;
				}
			}

			// 체크인 이전 데이터인 경우 기존 데이터를 업데이트 한다. 그러기 위해 아이디를 셋팅함
			if (!"Y".equals(revInfo.getReleaseYn()))
				pMap.put("masterWorkorderContentsId", revInfo.getMasterWorkorderContentsId());

			// 파일 저장소 위치 셋팅
			String filePath = getStoragePath((String) pMap.get("maintDocModelCode"),
					(String) pMap.get("WOCntsDocSubtypCd"), docRevSeq, (String) pMap.get("masterWOCntsTypCd"), revSeq);

			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// file copy
			String fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + fileName + "." + ext;

			dir = new File(fileFullPath);

			multipartFile.transferTo(dir);
			String strXml = org.apache.commons.io.FileUtils.readFileToString(dir, "UTF-8");

			XmlXpathUtil xxu = new XmlXpathUtil();
			// xml 파일 형태 체크
			boolean bool = xxu.checkSaxParseCheck(strXml);
			if (!bool) {
				pMap.put("result", "xml 파일의 형식이 맞지 않습니다.");
				dir.delete();
				return false;
			}

			// validation check
			String modelNameInFile = "";
			// Task element에서 KMR_NUMBER가 KMR라면 파일명&메뉴얼 타입 체크하지 않음
			String kmrNumber = getAttributeValue(strXml, "KMR_NUMBER");
			if (!kmrNumber.equals(TaskConstants.KMR_NUMBER)) {
				if (pMap.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR)) {
					modelNameInFile = getAttributeValue(strXml, ContentSearch.MANUAL_TYPE_KOR, "MODEL");

					if (!pMap.get("maintDocModelCode").equals(modelNameInFile)) {
						pMap.put("result", "선택한 파일의 모델명이 파일의 모델명과 같지 않습니다.");
						dir.delete();
						return false;
					}

				} else if (pMap.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_AMM)) {
					// KOR로 모델명을 조회해서 데이터가 조회 되면 메뉴얼타입이 틀렸다고 판단.
					modelNameInFile = getAttributeValue(strXml, ContentSearch.MANUAL_TYPE_KOR, "MODEL");
					if (!modelNameInFile.equals("")) {
						pMap.put("result", "선택한 파일의 메뉴얼타입이 파일의 메뉴얼타입과 같지 않습니다.");
						dir.delete();
						return false;
					}
				}
			}

			/**
			 * Task element에서 KMR_NUMBER가 KMR라면 파일명&메뉴얼 타입 체크하지 않음 위에서 조회했지만
			 * xml파일에서 조합한 파일명으로 DB에서 한번 더 조회 후 있으면 등록된 파일 메시지 파일 생성 후에 체크해서 파일
			 * 이름이 잘못 저장되었다면 저장된 파일 지워주고 다시 생성 WOCntsDocSubtypCd : KOR
			 * masterWOCntsTypCd : OP or RT
			 */
			if (!kmrNumber.equals(TaskConstants.KMR_NUMBER)
					&& (pMap.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR)
							&& (pMap.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_OP)
									|| pMap.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_RT)))) {

				String generateFileName = "";
				// comp는 파일명 변경 시 SUBJNBR를 넣지 않는다.
				if (pMap.get("maintDocModelCode").equals("COMP")) {
					if ((pMap.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_OP))) {
						generateFileName = generateFileName2InFileUseCompForOP(strXml);
					} else if (pMap.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_RT)) {
						generateFileName = generateFileName2InFileUseCompForRT(strXml);
					}
				} else {
					generateFileName = generateFileName2InFile(strXml);
				}

				pMap.put("fileName", generateFileName.concat(".").concat(ext));
				revInfo = contentSearchDao.getContentNextRevisionSeq(pMap);
				if ("new".equals(pMap.get("addType")) && revInfo.getMasterWorkorderContentsId() != null) {
					pMap.put("result", "이미 등록된 파일이 있습니다.");
					return false;
				}

				// 사용자가 지정한 파일명과 xml파일 내부에서 조합된 파일명을 비교
				if (!generateFileName.equals(fileName)) {
					revSeq = revInfo.getMasterWOCntsRevzNo();
					docRevSeq = revInfo.getMasterWOCntsDocRevzNo();
					filePath = getStoragePath((String) pMap.get("maintDocModelCode"),
							(String) pMap.get("WOCntsDocSubtypCd"), docRevSeq, (String) pMap.get("masterWOCntsTypCd"),
							revSeq);
					fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + generateFileName + "." + ext;
					// dir = new File(fileFullPath);
					org.apache.commons.io.FileUtils.copyFile(dir, new File(fileFullPath));

					// 데이터 셋팅
					XMLUtil xml = new XMLUtil(new File(fileFullPath));

					List<String> keywordList = new ArrayList<String>();
					// TODO 키워드 항목 결정 후 수정
					keywordList.add(xml.getTitleValue());
					keywordList.add(xml.getKeyAttrValue());

					pMap.put("keywordList", keywordList);

					pMap.put("fileName", generateFileName + "." + ext);
					pMap.put("urlAddress", fileFullPath);
					pMap.put("revSeq", revSeq);
					pMap.put("releaseYn", ContentSearch.RELEASE_TYPE_N);
					pMap.put("titleName", xml.getTitleValue());
					pMap.put("userId", pMap.get("userId"));

				} else {
					// 데이터 셋팅
					XMLUtil xml = new XMLUtil(dir);

					List<String> keywordList = new ArrayList<String>();
					// TODO 키워드 항목 결정 후 수정
					keywordList.add(xml.getTitleValue());
					keywordList.add(xml.getKeyAttrValue());

					pMap.put("keywordList", keywordList);

					pMap.put("fileName", dir.getName());
					pMap.put("urlAddress", fileFullPath);
					pMap.put("revSeq", revSeq);
					pMap.put("releaseYn", ContentSearch.RELEASE_TYPE_N);
					pMap.put("titleName", xml.getTitleValue());
					pMap.put("userId", pMap.get("userId"));
				}

			} else {
				// 데이터 셋팅
				XMLUtil xml = new XMLUtil(dir);

				List<String> keywordList = new ArrayList<String>();
				// TODO 키워드 항목 결정 후 수정
				keywordList.add(xml.getTitleValue());
				keywordList.add(xml.getKeyAttrValue());

				pMap.put("keywordList", keywordList);

				pMap.put("fileName", dir.getName());
				pMap.put("urlAddress", fileFullPath);
				pMap.put("revSeq", revSeq);
				pMap.put("releaseYn", ContentSearch.RELEASE_TYPE_N);
				pMap.put("titleName", xml.getTitleValue());
				pMap.put("userId", pMap.get("userId"));
			}

		} catch (Exception e) {
			pMap.put("result", "파일 처리 실패 ..");
			LOGGER.error("파일 처리 실패 ..", e);

			return false;
		}

		return true;
	}

	/**
	 * cms 도애인에 맞춰 템플릿 DTD 주소 replace
	 * 
	 * @param file
	 * @param domain
	 */
	public String replaceDtdFromCmsDomain(File file, String domain) {
		String readXml = "";
		try {
			readXml = FileUtils.readFileToString(file, "utf-8");
			String replaceDomain = "";

			if (domain.equals("DEV")) {
				replaceDomain = "http://10.0.42.78/cms/dtd";
			} else if (domain.equals("STG")) {
				replaceDomain = "http://10.0.126.111:9080/cms/dtd";
			} else {
				replaceDomain = "http://10.7.11.23:9080/cms/dtd";
			}

			readXml = readXml.replaceAll(
					"<[$]HttpAbsoluteEnterpriseWebRoot[$]><[$]HttpRelativeWebRoot[$]><[$]DTDFolderName[$]>",
					replaceDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readXml;
	}

	/**
	 * HTTP Client 세팅
	 * 
	 * @param webdavUrl
	 * @return
	 */
/*	public HttpClient setHttpClient(String webdavUrl) {
		String webdavHost = "";

		try {
			URL _webdavUrl = new URL(webdavUrl);
			webdavHost = _webdavUrl.getHost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HostConfiguration hostConfig = new HostConfiguration();
		hostConfig.setHost(webdavHost);
		HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		int maxHostConnections = 20;
		params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
		connectionManager.setParams(params);
		HttpClient client = new HttpClient(connectionManager);
		Credentials creds = new UsernamePasswordCredentials("admin", "admin");
		client.getState().setCredentials(AuthScope.ANY, creds);
		client.setHostConfiguration(hostConfig);

		return client;
	}*/
	/*
	 * @Override public AdminInfo findAdminLogin (HashMap<String, Object> pMap)
	 * { LOGGER.debug("Running Method Name : " +
	 * Thread.currentThread().getStackTrace()[1].getMethodName());
	 * 
	 * return adminDAO.getAdminLogin(pMap); }
	 * 
	 * @Override public AdminInfo findAdminInfo(HashMap<String, Object> pMap)
	 * throws Exception { LOGGER.debug("Running Method Name : " +
	 * Thread.currentThread().getStackTrace()[1].getMethodName());
	 * 
	 * return adminDAO.getAdminInfo(pMap); }
	 * 
	 * @Override public List<AdminInfo> findAdminList(HashMap<String, Object>
	 * pMap) throws Exception { LOGGER.debug("Running Method Name : " +
	 * Thread.currentThread().getStackTrace()[1].getMethodName());
	 * 
	 * return adminDAO.getAdminList(pMap); }
	 * 
	 * @Override public int insertAdmin(HashMap<String, Object> pMap) throws
	 * Exception { return adminDAO.insertAdmin(pMap); }
	 * 
	 * @Override public int updateAdmin(HashMap<String, Object> pMap) throws
	 * Exception { return adminDAO.updateAdmin(pMap); }
	 * 
	 * @Override public void deleteAdmin(List<String> userId) throws Exception {
	 * 
	 * HashMap<String, Object> pMap;
	 * 
	 * for(String id : userId) { pMap = new HashMap<String, Object>();
	 * pMap.put("userId", id);
	 * 
	 * adminDAO.removeAdmin(pMap); } }
	 * 
	 * @Override public int getAdminTotalCount(HashMap<String, Object> pMap)
	 * throws Exception { // TODO Auto-generated method stub return
	 * adminDAO.getAdminTotalCount(pMap); }
	 * 
	 */
}
