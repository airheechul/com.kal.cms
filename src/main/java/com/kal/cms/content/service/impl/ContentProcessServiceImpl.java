package com.kal.cms.content.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
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
import org.jdom2.input.JDOMParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.NodeList;

import com.easycompany.cmm.exception.MpFrameException;
import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.common.constants.AdminConstants;
import com.kal.cms.common.constants.AdminConstants.ContentSearch;
import com.kal.cms.common.constants.AdminConstants.TaskConstants;
import com.kal.cms.common.util.Util;
import com.kal.cms.common.util.XMLUtil;
import com.kal.cms.common.util.XmlXpathUtil;
import com.kal.cms.common.util.ZipUtil;
import com.kal.cms.common.util.report.CsvUtil;
import com.kal.cms.content.mapper.ContentProcessMapper;
import com.kal.cms.content.mapper.ContentSearchMapper;
import com.kal.cms.content.service.ContentProcessService;
import com.kal.cms.content.service.ContentSearchService;
import com.kal.cms.content.vo.ContentInfo;
import com.kal.cms.content.vo.ContentTaskInfo;
import com.kal.cms.task.vo.TaskInfo;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("contentProcessService")
public class ContentProcessServiceImpl extends EgovAbstractServiceImpl implements ContentProcessService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentProcessServiceImpl.class);

	// @Autowired
	// private AdminDao adminDao;

	// TODO mybatis 사용
	@Resource(name = "contentSearchMapper")
	private ContentSearchMapper contentSearchDao;
	
	@Resource(name = "contentProcessMapper")
	private ContentProcessMapper contentProcessDao;

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	@Resource(name = "txManager")
	protected DataSourceTransactionManager txManager;
	
	private Long lastChecked;

	@Override
	public List<ContentInfo> findContentProcessList(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return contentProcessDao.getContentProcessList(pMap);
	}

	@Override
	public List<ContentInfo> findContentDownloadList(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return contentProcessDao.getContentDownloadList(pMap);
	}

	@Override
	public List<ContentInfo> findContentImageList(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return contentProcessDao.getContentImageList(pMap);
	}

	@Override
	public boolean processContentFile(File file, HashMap<String, Object> pMap) {
		boolean result = false;

	
		try {
			synchronized(contentProcessDao){
				
				pMap.put("masterWOCntsTskId", pMap.get("masterWOCntsTskId"));		// task id			

				//TASK 를 진행중으로 업데이트 한다.
				pMap.put("masterWOCntsTskSttsCd", "7002"); 				// task status : in progress
				contentProcessDao.updateTask(pMap);
				
				//file process
				pMap.put("parseError", "N");
				saveMultipartFile(file, pMap);
				
				//Task detail 에 파일 등록 처리완료 업데이트
				pMap.put("masterWOCntsStskSttsCd", "7001");
				contentProcessDao.updateTaskDetail(pMap);

				//Task detail 에 파일 정보 등록 처리중 인서트
				pMap.put("masterWOCntsStskSttsCd", "7002");
				pMap.put("masterWOCntsStskCtn", file.getName() +"업로드 파일 정보 등록 ");
				contentProcessDao.insertTaskDetail(pMap);
				
				// 컨텐츠 정보 등록
				contentProcessDao.insertContentInfo(pMap);

				//Task detail 에 파일 정보 등록 처리완료 업데이트
				pMap.put("masterWOCntsStskSttsCd", "7001");
				contentProcessDao.updateTaskDetail(pMap);				
				
				//TASK 를 완료로 업데이트 한다.
				//중간에 parsing error 파일이 있다면 7007로 업데이트
				if (pMap.get("parseError").equals("Y")){
					pMap.put("masterWOCntsTskSttsCd", TaskConstants.TASK_STATUS_REPORT); 				// task status : complete
				} else {
					pMap.put("masterWOCntsTskSttsCd", TaskConstants.TASK_STATUS_COMPLETE); 				// task status : report					
				}
				
				contentProcessDao.updateTask(pMap);
				
				result = true;
			}
		} catch (Exception e) {
			try {
				pMap.put("masterWOCntsTskSttsCd", "7003"); 				// task status : error
				contentProcessDao.updateTask(pMap);
				
				if(pMap.get("masterWOCntsStskId") != null){
					pMap.put("masterWOCntsStskSttsCd", "7003");				// sub status : error
					contentProcessDao.updateTaskDetail(pMap);
					
					String errorMsg = e.getMessage();
					if(errorMsg != null && errorMsg.length() > 1000)
						errorMsg = errorMsg.substring(0, 1000);
					pMap.put("errorMessageContents", errorMsg);
					contentProcessDao.insertTaskDetailLog(pMap);
				}

			} catch (Exception e1){
			}
		}
		
		return result;
	}

	@Override
	public int registTaskInfo(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateTask(HashMap<String, Object> pMap) {
		try {
			contentProcessDao.updateTask(pMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public int registTaskDetail(HashMap<String, Object> pMap) throws Exception {
		return contentProcessDao.insertTaskDetail(pMap);
	}

	@Override
	public void updateTaskDetail(HashMap<String, Object> pMap) {
		try {
			contentProcessDao.updateTaskDetail(pMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String compressContents(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String registDownloadInfo(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		try {
			// 파일 어드레스 업데이트 및 완료
			contentProcessDao.updateTaskCompleteWithFile(pMap);
		} catch (Exception e) {
			LOGGER.error("Content 다운로드 Transaction 오류", e);
		}
		
		return "";
	}

	@Override
	public List<ContentTaskInfo> findReportList(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return contentProcessDao.getReportList(pMap);
	}

	@Override
	public ContentTaskInfo createReport(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		List<ContentTaskInfo> reportInfoList = contentProcessDao.getReportList(pMap);

		ContentTaskInfo taskInfo = null;
 		
 		if(reportInfoList != null && reportInfoList.size() > 0){
 			taskInfo = (ContentTaskInfo)reportInfoList.get(0);
 			taskInfo.setReportBase(egovMessageSource.getMessage("process.upload.file.report.path"));
 			
 			//TODO 현재 메뉴얼 리비젼 번호가 올바르지 않음..
 			if(taskInfo.getMasterWOCntsDocRevzNo() == null || "".equalsIgnoreCase(taskInfo.getMasterWOCntsDocRevzNo()))
 				taskInfo.setMasterWOCntsDocRevzNo("1");
 			
 			String filePath = getStoragePath(taskInfo.getMaintDocModelCode(), taskInfo.getWOCntsDocSubtypCd(), taskInfo.getMasterWOCntsDocRevzNo(), taskInfo.getMasterWOCntsTypCd(), null);
 			filePath += "/".concat(taskInfo.getWOCntsDocSubtypCd()).concat(".xml");
 			
 			taskInfo.setReportSrcPath(filePath);
 			
 			//TODO 제거
 			//Thread.sleep(5000);
 			
 			CsvUtil.createReport(taskInfo);
 		} else {
 			//throw new MpFrameException("Task 정보 조회 실패");
 		}
		return taskInfo;
	}

	@Override
	public boolean execCheckIn(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		contentProcessDao.execCheckIn(pMap);
		return true;
	}

	@Override
	public int insertTaskDefaultInfo(File file, HashMap<String, Object> pMap) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus txStatus= txManager.getTransaction(def);

		int masterWOCntsTskId = 0;

		try {
			//Contetnt Type 이 image라면 releaseYn은 Y로 
			pMap.put("releaseYn", pMap.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_IMG)?AdminConstants.USE_YN_Y:AdminConstants.USE_YN_N);
			pMap.put("fileName", file.getName());
			pMap.put("urlAddress", file.getAbsolutePath());
			masterWOCntsTskId = contentProcessDao.insertTaskInfo(pMap);
			txManager.commit(txStatus);
			
			//Task detail 에 파일 처리중 인서트
			pMap.put("masterWOCntsTskId", masterWOCntsTskId);		// task id
			pMap.put("masterWOCntsStskSttsCd", "7002");
			pMap.put("masterWOCntsStskCtn", file.getName() +"업로드 파일 처리 ");	
			contentProcessDao.insertTaskDetail(pMap);
			txManager.commit(txStatus);
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				txManager.rollback(txStatus);
			} catch (Exception e) {
				LOGGER.error("TaskInfo Insert endTransaction 오류", e);
			}
		}
		return masterWOCntsTskId;
	}

	private boolean saveMultipartFile(File file, HashMap<String, Object> map) throws Exception {

		boolean result = false;
		int cnt = 0;
		
		try {	
			String fileName = (String)map.get("mfileName");
			String ext = (String)map.get("ext");
			
			//ext test
			boolean isZip = false;
			if("OP".equals(map.get("masterWOCntsTypCd")) || "RT".equals(map.get("masterWOCntsTypCd"))) {
				String possibleExt  = "XML:ZIP";
				if(possibleExt.indexOf(ext.toUpperCase()) < 0){
					//throw new MpFrameException("업로드 가능한 파일 타입 아님");
				}
				
				if("ZIP".equals(ext.toUpperCase()))
					isZip = true;
				
			} else if(ContentSearch.CONTENT_TYPE_IMG.equals(map.get("masterWOCntsTypCd"))) {
				String possibleExt  = egovMessageSource.getMessage("process.upload.file.exts").toUpperCase();
				if(possibleExt.indexOf(ext.toUpperCase()) < 0){
					//throw new MpFrameException("업로드 가능한 파일 타입 아님");	
				}
				
				if("ZIP".equals(ext.toUpperCase()))
					isZip = true;
				
			} else {
				//throw new MpFrameException("컨텐츠 타입이 올바르지 않음");
			}

			//server upload directory ??
			String filePath = "";
			List<HashMap<String, Object>> fileInfoList = new ArrayList<HashMap<String, Object>>();
			
			if(isZip == true) {
				LOGGER.debug("zip 파일 처리 시작 ...");	
				
				//-------------- 1. zip extract
				String zipPath = egovMessageSource.getMessage("process.upload.file.zip.path");
				
				if(zipPath.length() < 15){
					throw new MpFrameException("압축 파일 저장 처리중 오류");
				}
				
				File zipFile = new File(zipPath);
				if(!zipFile.exists()){
					zipFile.mkdirs();
				}
				
				//zipfile copy
				String zipFileFullPath = zipPath + AdminConstants.FILE_SEPARATOR + fileName + "." + ext;
				zipFile = new File(zipFileFullPath);
				file.renameTo(zipFile);
				
				//zip 파일 위치를 저장해둠. (프로세스 재실행시 사용)
				map.put("fileName", fileName + "." + ext);
				map.put("urlAddress", zipFileFullPath);
				contentProcessDao.updateTaskFile(map);
	
				//unzip : zip 파일이 위치한 폴더 내에 파일 명과 동일한 임시 폴더를 생성하여 사용함 
				String unZipPath = zipPath.concat(AdminConstants.FILE_SEPARATOR).concat(fileName);
				ZipUtil.unzipToSub(zipFileFullPath, unZipPath, 5000);
				
				//-------------- 2.개별파일 처리
				File fileArray = new File(unZipPath); // 언집된 파일 디렉토리
				
				
				int totalCount = Util.getFileCount(fileArray, 0);
				int excuteCount = 0;
				
				int exCount = 0;
				
				//시작 로그
				map.put("errorMessageContents", totalCount +"건 파일 처리 시작");
				contentProcessDao.insertTaskDetailLog(map);						
				
				try {				
				
					for(File ffile : fileArray.listFiles()) {
						
						for (File sfile : ffile.listFiles()) {
							
							boolean fileStatus = true;
							String sExt = sfile.getName().substring(sfile.getName().lastIndexOf(".")+1, sfile.getName().length()).toLowerCase();
							if(ContentSearch.CONTENT_TYPE_IMG.equals(map.get("masterWOCntsTypCd"))) {
								// 이미지 파일 업로드인 경우 이미지 파일이 아니면 무시
			
								String pExts  = egovMessageSource.getMessage("process.upload.file.image.exts");
								if(pExts.indexOf(sExt) < 0){
									exCount ++;
									continue;
								}			
							} else {
								// xml 이 아니면 무시
								if(!"xml".equalsIgnoreCase(sExt)){
									exCount ++;
									continue;
								}
							}

							cnt++;
							excuteCount++;
							if (cnt >= TaskConstants.TASK_PROCESS_CHECK_TERM){
								cnt = 0;
								
								// 주기적으로 프로세스 정지 여부 확인한다.
								String taskStatus = contentProcessDao.getTaskStatus(map);
								if(TaskConstants.TASK_STATUS_ABORTED.equals(taskStatus)){
									throw new MpFrameException("사용자 Abort 요청에 의한 종료", TaskConstants.TASK_STATUS_ABORTED);
								}
								
								// 주기적으로 로그 남긴다.
								map.put("errorMessageContents", excuteCount +"/" +totalCount +" 처리중...");
								contentProcessDao.insertTaskDetailLog(map);
							}
							
							// 현재 파일 명에 해당하는 리비젼 넘버를 가져옴
							HashMap<String, Object> pm = new HashMap<String, Object>();
							pm.put("fileName", sfile.getName());
							ContentInfo revInfo = contentSearchDao.getContentNextRevisionSeq(pm);
							String revSeq = revInfo.getMasterWOCntsRevzNo();
							String docRevSeq = revInfo.getMasterWOCntsDocRevzNo();
							
							// 풀린 파일들을 원래 가야할 위치로 분배한다.
							filePath = getStoragePath((String)map.get("maintDocModelCode"), (String)map.get("WOCntsDocSubtypCd"), docRevSeq, (String)map.get("masterWOCntsTypCd"), revSeq);
		
							File dir = new File(filePath);
							if(!dir.exists()){
								dir.mkdirs();
							}
							
							//file copy
							String fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + sfile.getName();
							String useSaveFileName = sfile.getName();;
							dir = new File(fileFullPath);
							
							sfile.renameTo(dir);
							String strXml = org.apache.commons.io.FileUtils.readFileToString(dir,"UTF-8");
							String kmrNumber = getAttributeValue(strXml,"KMR_NUMBER");
							if(!ContentSearch.CONTENT_TYPE_IMG.equals(map.get("masterWOCntsTypCd"))) {
								// 이미지파일인 경우는 체크하지 않음
								//validation check
								String modelNameInFile = "";
								
								if (!kmrNumber.equals(TaskConstants.KMR_NUMBER)){
									if (map.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR)){
										modelNameInFile = getAttributeValue(strXml,ContentSearch.MANUAL_TYPE_KOR , "MODEL");
										if(!modelNameInFile.equals("")){
											if (!map.get("maintDocModelCode").equals(modelNameInFile)){
												throw new MpFrameException("선택한 파일의 모델명이 파일의 모델명과 같지 않습니다.[파일명 : "+sfile.getName()+"]");
											}	
										} 
									} else if(map.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_AMM)) {
										//KOR로 모델명을 조회해서 데이터가 조회 되면 메뉴얼타입이 틀렸다고 판단.
										modelNameInFile = getAttributeValue(strXml,ContentSearch.MANUAL_TYPE_KOR , "MODEL");
										if (!modelNameInFile.equals("")){
											throw new MpFrameException("선택한 파일의 메뉴얼타입이 파일의 메뉴얼타입과 같지 않습니다.[파일명 : "+sfile.getName()+"]");
										}
									}	
								}
							}	
							
							/**
							 * Task element에서 KMR_NUMBER가 KMR라면 파일명&메뉴얼 타입 체크하지 않음
							 * 파일 생성 후에 체크해서 파일 이름이 잘못 저장되었다면 저장된 파일 지워주고 다시 생성
							 * WOCntsDocSubtypCd : KOR
							 * masterWOCntsTypCd : OP or RT 
							 */
							//OPEN 될때까지 변경되지 않으면 그때는 삭제 처리.
							if (!kmrNumber.equals(TaskConstants.KMR_NUMBER) && 
									(map.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR) &&
									(map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_OP) || map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_RT)))
							){
								String generateFileName = "";
								//comp는 파일명 변경 시 SUBJNBR를 넣지 않는다. 
								if (map.get("maintDocModelCode").equals("COMP")){
									if (map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_OP)){
										generateFileName = generateFileName2InFileUseCompForOP(strXml);	
									} else if (map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_RT)){
										generateFileName = generateFileName2InFileUseCompForRT(strXml);
									}
									
								} else {
									generateFileName = generateFileName2InFile(strXml);	
								}
								
								if (generateFileName.equals("")){
									useSaveFileName = sfile.getName();
								} else if (!sfile.getName().equals(generateFileName.concat(".xml"))){
									pm.put("fileName", generateFileName.concat(".xml"));
									revInfo = contentSearchDao.getContentNextRevisionSeq(pm);
									revSeq = revInfo.getMasterWOCntsRevzNo();
									docRevSeq = revInfo.getMasterWOCntsDocRevzNo();
									fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + generateFileName + "." + sExt;
									dir.renameTo(new File(fileFullPath));
									dir = new File(fileFullPath);
									useSaveFileName = generateFileName + "." + sExt;
									map.put("errorMessageContents", "파일명 형식 오류 : ["+sfile.getName()+"] => ["+generateFileName.concat(".xml")+"]");
									contentProcessDao.insertTaskDetailLog(map);
								} else {
									useSaveFileName = sfile.getName();
								}
							}
							
							
							HashMap<String, Object> fileInfoMap = new HashMap<String, Object>();
							
							//체크인 이전 데이터인 경우 기존 데이터를 업데이트 한다. 그러기 위해 아이디를 셋팅함
							if(!"Y".equals(revInfo.getReleaseYn()))
								fileInfoMap.put("masterWorkorderContentsId", revInfo.getMasterWorkorderContentsId());
							
							
							// xml 타이틀 및 키워드 항목 조회
							
							if( "OP".equals(map.get("masterWOCntsTypCd")) || "RT".equals(map.get("masterWOCntsTypCd"))) {
								try {
									XMLUtil xml = new XMLUtil(dir);
									
									List<String> keywordList = new ArrayList<String>();
									if(xml.getTitleValue() != null && !"".equals(xml.getTitleValue()))
										keywordList.add(xml.getTitleValue());
									if(xml.getKeyAttrValue() != null && !"".equals(xml.getKeyAttrValue()))
										keywordList.add(xml.getKeyAttrValue());
									
									fileInfoMap.put("keywordList", keywordList);								
									fileInfoMap.put("titleName", xml.getTitleValue());
								} catch (JDOMParseException jdompep){
									fileStatus = false;
									exCount++;
									excuteCount--;
									String errMsg = jdompep.getMessage();
									if(errMsg != null && errMsg.length() > 500)
										errMsg = errMsg.substring(0, 500);
									//여러개의 파일중 한개라도 발생되면 7007 코드를 가지도록.
									if (!map.get("parseError").equals("Y")){
										map.put("parseError","Y");	
									}
									map.put("errorMessageContents", "Parse Error : ["+sfile.getName()+"]");
									contentProcessDao.insertTaskDetailLog(map);
								} catch (Exception e) {
									String errMsg = e.getMessage();
									
									if(errMsg != null && errMsg.length() > 500)
										errMsg = errMsg.substring(0, 500);
									
									throw new MpFrameException("xml 데이터 처리 도중 오류 발=생 : "+dir+" ; e="+errMsg);
								}
							}

							
							fileInfoMap.put("fileName", useSaveFileName);
							fileInfoMap.put("masterWOCntsRevzNo", revSeq);
							if("0".equals(revSeq))
								fileInfoMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_NEW);
							else
								fileInfoMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_REVISE);
							
							fileInfoMap.put("urlAddress", fileFullPath);
							
							LOGGER.debug("zip 파일 : "+fileInfoMap.get("fileName") + " , path ="+ fileInfoMap.get("urlAddress"));
							LOGGER.debug("          title = "+fileInfoMap.get("titleName"));
							
							fileInfoMap.put("skipData", "N");
							if (fileStatus==false){
								fileInfoMap.put("skipData", "Y");
							}
							
							fileInfoList.add(fileInfoMap);
							
							
						}
					}
					// 로그 남김
					map.put("errorMessageContents", excuteCount +"/" +totalCount +" 처리 종료" + (exCount == 0 ? "" : " 예외 처리 파일 = "+exCount));
					contentProcessDao.insertTaskDetailLog(map);
				
				} catch (MpFrameException me) {
					throw me;
				} catch (Exception e) {
					e.printStackTrace();

					throw new MpFrameException("압축 파일 저장 처리중 오류");
				} finally {
					try {
						File unZipDir = new File(unZipPath);
						if(unZipDir.exists()){
							removeDir(unZipPath);
							unZipDir.delete();
						}
					} catch (Exception se){}
				}

				
			} else {
				
				//시작 로그
				map.put("errorMessageContents", "업로드 파일 처리 시작");
				contentProcessDao.insertTaskDetailLog(map);
				
				LOGGER.debug("싱글 파일 처리 시작 ...");
				// 현재 파일 명에 해당하는 리비젼 넘버를 가져옴
				HashMap<String, Object> pm = new HashMap<String, Object>();
				pm.put("fileName", file.getName());
				ContentInfo revInfo = contentSearchDao.getContentNextRevisionSeq(pm);
				String revSeq = revInfo.getMasterWOCntsRevzNo();
				String docRevSeq = revInfo.getMasterWOCntsDocRevzNo();
				
				filePath = getStoragePath((String)map.get("maintDocModelCode"), (String)map.get("WOCntsDocSubtypCd"), docRevSeq, (String)map.get("masterWOCntsTypCd"), revSeq);

				File dir = new File(filePath);
				if(!dir.exists()){
					dir.mkdirs();
				}				

				//file copy
				String fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + fileName + "." + ext;
				String saveFileName = "";
				File orgFile = new File(fileFullPath);
				
				System.out.println("fileFullPath == "+fileFullPath);

				file.renameTo(orgFile);
				String strXml = org.apache.commons.io.FileUtils.readFileToString(orgFile,"UTF-8");
				String kmrNumber = getAttributeValue(strXml,"KMR_NUMBER");
				
				
				if(!ContentSearch.CONTENT_TYPE_IMG.equals(map.get("masterWOCntsTypCd"))) {
					if (!kmrNumber.equals(TaskConstants.KMR_NUMBER)){
						//validation check
						String modelNameInFile = "";
						if (map.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR)){
							modelNameInFile = getAttributeValue(strXml,ContentSearch.MANUAL_TYPE_KOR , "MODEL");
							if (!modelNameInFile.equals("")){
								if (!map.get("maintDocModelCode").equals(modelNameInFile)){
									throw new MpFrameException("선택한 파일의 모델명이 파일의 모델명과 같지 않습니다.[파일명 : "+fileName + "." + ext+"]");
								}	
							}
						} else if(map.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_AMM)) {
							//KOR로 모델명을 조회해서 데이터가 조회 되면 메뉴얼타입이 틀렸다고 판단.
							modelNameInFile = getAttributeValue(strXml,ContentSearch.MANUAL_TYPE_KOR , "MODEL");
							if (!modelNameInFile.equals("")){
								throw new MpFrameException("선택한 파일의 메뉴얼타입이 파일의 메뉴얼타입과 같지 않습니다.[파일명 : "+fileName + "." + ext+"]");
							}
						}	
					}
					
				}
				
				/**
				 * 파일 생성 후에 체크해서 파일 이름이 잘못 저장되었다면 기존 파일명 변경
				 * WOCntsDocSubtypCd : KOR
				 * masterWOCntsTypCd : OP or RT 
				 */
				//OPEN 될때까지 변경되지 않으면 그때는 삭제 처리.
				if (!kmrNumber.equals(TaskConstants.KMR_NUMBER) && 
					(map.get("WOCntsDocSubtypCd").equals(ContentSearch.MANUAL_TYPE_KOR) &&
					(map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_OP) || map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_RT)))
				){
					String generateFileName = "";
					//comp는 파일명 변경 시 SUBJNBR를 넣지 않는다. 
					if (map.get("maintDocModelCode").equals("COMP")){
						if (map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_OP)){
							generateFileName = generateFileName2InFileUseCompForOP(strXml);	
						} else if (map.get("masterWOCntsTypCd").equals(ContentSearch.CONTENT_TYPE_RT)){
							generateFileName = generateFileName2InFileUseCompForRT(strXml);
						}
					} else {
						generateFileName = generateFileName2InFile(strXml);	
					}
					
					if (!generateFileName.equals(fileName)){
						pm.put("fileName", file.getName());
						revInfo = contentSearchDao.getContentNextRevisionSeq(pm);
						revSeq = revInfo.getMasterWOCntsRevzNo();
						docRevSeq = revInfo.getMasterWOCntsDocRevzNo();
						fileFullPath = filePath + AdminConstants.FILE_SEPARATOR + generateFileName + "." + ext;
						saveFileName = generateFileName + "." + ext;
						orgFile.renameTo(new File(fileFullPath));
						orgFile = new File(fileFullPath);
						map.put("errorMessageContents", "파일명 형식 오류 : ["+fileName + "." + ext+"] => ["+generateFileName+"]");
						contentProcessDao.insertTaskDetailLog(map);
					} else {
						saveFileName = fileName + "." + ext;
					}
				}
				
				//파일 위치를 저장해둠. (프로세스 재실행시 사용)
				map.put("fileName", saveFileName);
				map.put("urlAddress", fileFullPath);
				contentProcessDao.updateTaskFile(map);				
				
				HashMap<String, Object> fileInfoMap = new HashMap<String, Object>();
				
				//체크인 이전 데이터인 경우 기존 데이터를 업데이트 한다. 그러기 위해 아이디를 셋팅함
				if(!"Y".equals(revInfo.getReleaseYn()))
					fileInfoMap.put("masterWorkorderContentsId", revInfo.getMasterWorkorderContentsId());
				
				// xml 타이틀 및 키워드 항목 조회	
				if( "OP".equals(map.get("masterWOCntsTypCd")) || "RT".equals(map.get("masterWOCntsTypCd"))) {
					try {
						XMLUtil xml = new XMLUtil(orgFile);
						
						List<String> keywordList = new ArrayList<String>();
						if(xml.getTitleValue() != null && !"".equals(xml.getTitleValue()))
							keywordList.add(xml.getTitleValue());
						if(xml.getKeyAttrValue() != null && !"".equals(xml.getKeyAttrValue()))
							keywordList.add(xml.getKeyAttrValue());
	
						fileInfoMap.put("keywordList", keywordList);	
						fileInfoMap.put("titleName", xml.getTitleValue());
					} catch (Exception e) {
						String errMsg = e.getMessage();
						
						if(errMsg != null && errMsg.length() > 500)
							errMsg = errMsg.substring(0, 500);
						
						//throw new MpFrameException("xml 데이터 처리 도중 오류 발생 : file="+orgFile+" ; e="+errMsg);
					}
				}
				
				fileInfoMap.put("skipData", "N");
				fileInfoMap.put("fileName", orgFile.getName());
				fileInfoMap.put("masterWOCntsRevzNo", revSeq);
				if("0".equals(revSeq))
					fileInfoMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_NEW);
				else
					fileInfoMap.put("masterWOCntsSttsCd", ContentSearch.REVISION_TYPE_REVISE);
				
				
				fileInfoMap.put("urlAddress", fileFullPath);
				
				LOGGER.debug("일반 파일 : "+fileInfoMap.get("fileName") + " , path ="+ fileInfoMap.get("urlAddress"));
				LOGGER.debug("          title = "+fileInfoMap.get("titleName"));
				
				//무조건 리스트 형태로 만든다
				fileInfoList.add(fileInfoMap);
				
				// 로그 남김
				map.put("errorMessageContents", "1/1 업로드 파일 처리 종료");
				contentProcessDao.insertTaskDetailLog(map);
			}
			
			map.put("fileInfoList", fileInfoList);
			
			LOGGER.debug(" 인서트할 파일 정보 사이즈 =="+((List)map.get("fileInfoList")).size());

			result = true;
		} catch (Exception e) {
			//e.printStackTrace();
			//throw new MpFrameException("압축 파일 저장 처리중 오류");
		}

		return result;
	}
	
	
	private List<String> removeDuplicate(List<String> filePathList){
		List<String> regulatedList = new ArrayList<String>();
		if(filePathList!=null){
			for(int i=0;i<filePathList.size();i++){
				if(regulatedList.contains(filePathList.get(i))){
					
				}else{
					regulatedList.add(filePathList.get(i));
				}
			}
			
		}
		return regulatedList;
	}
	
	private String getStoragePath(String maintDocModelCode, String WOCntsDocSubtypCd, String manualRevision, String masterWOCntsTypCd, String fileRevision){
		
		StringBuffer path = new StringBuffer(egovMessageSource.getMessage("content.upload.file.root.path"));

		if("KOR".equals(WOCntsDocSubtypCd)) {
			//2. KOR Content
			//	Manual/OEM/ {모델} / {매뉴얼Type} / { 매뉴얼 Revision No } / { Content Type } / { File List }
			path.append(egovMessageSource.getMessage("content.upload.file.kor.path"));
			path.append(AdminConstants.FILE_SEPARATOR.concat(maintDocModelCode.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(WOCntsDocSubtypCd.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(manualRevision));
			if(!ContentSearch.CONTENT_TYPE_IMG.equals(masterWOCntsTypCd)) {
				path.append(AdminConstants.FILE_SEPARATOR.concat(masterWOCntsTypCd.toLowerCase()));
				path.append(AdminConstants.FILE_SEPARATOR.concat(fileRevision));
			} else {
				path.append(AdminConstants.FILE_SEPARATOR.concat("images"));
			}
			
		} else {
			//1. OEM Content 의 경우 ( AMM / TSM / FRMFIM )
			//	/Manual/KAL/ {모델} / {매뉴얼Type} / { 매뉴얼 Revision No } / { Content Type } / { File REV } / { File List }
			path.append(egovMessageSource.getMessage("content.upload.file.oem.path"));
			path.append(AdminConstants.FILE_SEPARATOR.concat(maintDocModelCode.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(WOCntsDocSubtypCd.toLowerCase()));
			path.append(AdminConstants.FILE_SEPARATOR.concat(manualRevision));
			if(!ContentSearch.CONTENT_TYPE_IMG.equals(masterWOCntsTypCd)) {
				path.append(AdminConstants.FILE_SEPARATOR.concat(masterWOCntsTypCd.toLowerCase()));
			} else {
				path.append(AdminConstants.FILE_SEPARATOR.concat("images"));
			}			
		}
		
		return path.toString();
	}
	
	
	private String generateFileName2InFileUseCompForOP(String xml){
		String rtnModel = "";
		String rtnChapnbr = "";
		String retSectnbr = "";
		String retFunc = "";
		String retSeq = "";
		
		try{
			rtnModel = getAttributeValue(xml, "KOR", "MODEL");
			rtnChapnbr = getAttributeValue(xml, "CHAPNBR");
			retSectnbr = getAttributeValue(xml, "SECTNBR");
			retFunc = getAttributeValue(xml, "FUNC");
			retSeq = getAttributeValue(xml, "SEQ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if (rtnModel.equals("") || rtnChapnbr.equals("") || retSectnbr.equals("") || retFunc.equals("") || retSeq.equals("")) return "";
		
		return rtnModel.concat("_").concat("KOR").concat("_").concat(rtnChapnbr).concat("_")
				.concat(retSectnbr).concat("_").concat(retFunc).concat("_").concat(retSeq);
	}
	
	/**
	 * MODEL TYPE COMP RT
	 * 파일의 속성값으로 조합한 파일명 리턴
	 * @param String
	 * @return
	 */
	private String generateFileName2InFileUseCompForRT(String xml){
		String rtnModel = "";
		String rtnChapnbr = "";
		String retSectnbr = "";
		String retSubjnbr = "";
		String retFunc = "";
		String retSeq = "";
		
		try{
			rtnModel = getAttributeValue(xml, "KOR", "MODEL");
			rtnChapnbr = getAttributeValue(xml, "CHAPNBR");
			retSectnbr = getAttributeValue(xml, "SECTNBR");
			retSubjnbr = getAttributeValue(xml, "SUBJNBR");
			retFunc = getAttributeValue(xml, "FUNC");
			retSeq = getAttributeValue(xml, "SEQ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if (rtnModel.equals("") || rtnChapnbr.equals("") || retSectnbr.equals("") || retSubjnbr.equals("") || retFunc.equals("") || retSeq.equals("")) return "";
		
		return rtnModel.concat("_").concat("KOR").concat("_").concat(rtnChapnbr).concat("_")
		.concat(retSectnbr).concat("_").concat(retSubjnbr).concat("_").concat(retFunc).concat("_").concat(retSeq);
	}
	
	/**
	 * 파일의 속성값으로 조합한 파일명 리턴
	 * @param String
	 * @return
	 */
	private String generateFileName2InFile(String xml){
		String rtnModel = "";
		String rtnChapnbr = "";
		String retSectnbr = "";
		String retSubjnbr = "";
		String retFunc = "";
		String retSeq = "";
		
		try{
			rtnModel = getAttributeValue(xml, "KOR", "MODEL");
			rtnChapnbr = getAttributeValue(xml, "CHAPNBR");
			retSectnbr = getAttributeValue(xml, "SECTNBR");
			retSubjnbr = getAttributeValue(xml, "SUBJNBR");
			retFunc = getAttributeValue(xml, "FUNC");
			retSeq = getAttributeValue(xml, "SEQ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return rtnModel.concat("_").concat("KOR").concat("_").concat(rtnChapnbr).concat("_")
				.concat(retSectnbr).concat("_").concat(retSubjnbr).concat("_").concat(retFunc).concat("_").concat(retSeq);
	}
	

	/**
	 * 태그에 매칭된 속성 값 리턴
	 * @param xml
	 * @param tagName
	 * @param attributeName
	 * @return
	 */
	private String getAttributeValue(String xml, String attributeName){
		XmlXpathUtil xxu = new XmlXpathUtil();
		String tagName = "SUBTASK";
		try{
			NodeList nodeList = xxu.getXpathMatchedNodes(xml, "//KOR/SUBTASK");
			if (nodeList==null || nodeList.getLength()==0){
				tagName = "TASK";
			}
			return xxu.getAttrValuesByElemNameAndAttrName(xml, tagName, attributeName).size()==0?"":xxu.getAttrValuesByElemNameAndAttrName(xml, tagName, attributeName).get(0);
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * 태그에 매칭된 속성 값 리턴
	 * @param xml
	 * @param tagName
	 * @param attributeName
	 * @return
	 */
	private String getAttributeValue(String xml, String tagName, String attributeName){
		XmlXpathUtil xxu = new XmlXpathUtil();
		try {
			return xxu.getAttrValuesByElemNameAndAttrName(xml, tagName, attributeName).size()==0?"":xxu.getAttrValuesByElemNameAndAttrName(xml, tagName, attributeName).get(0);
		} catch(Exception e){
			return "";
		}
	}

	
	private static void removeDir(String path) {

		File[] listFile = new File(path).listFiles();

		try {
			if (listFile.length > 0) {
				for (int i = 0; i < listFile.length; i++) {

					if (listFile[i].isFile()) {
						listFile[i].delete();
					} else {
						removeDir(listFile[i].getPath());
					}
					listFile[i].delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
