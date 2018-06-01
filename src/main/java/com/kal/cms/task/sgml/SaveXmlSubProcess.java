package com.kal.cms.task.sgml;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

import com.kal.cms.task.mapper.SgmlProcessMapper;
import com.kal.cms.task.vo.KeywordInfo;
import com.kal.cms.task.util.XmlXpathUtil;

/**
 * XML 저장 및 이동 서브 프로세스.
 */
public class SaveXmlSubProcess {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final SaveXmlSubProcess INSTANCE = new SaveXmlSubProcess();
	private SgmlProcessMapper sgmlProcessDao = null;
	private List<KeywordInfo> keywordCacheList = null;
	private Map<String, List> contentMapCache = new HashMap<String, List>();
	private Map<String, List> imageMapCache = new HashMap<String, List>();
	
	public static SaveXmlSubProcess getInstance(){
		return INSTANCE;
	}
	
	/**
	 * 프로세스 실행.
	 * @param destBasePath
	 * @param burstXmlFilePrefix
	 * @param revNum
	 * @param revDate
	 * @param model
	 * @param manualType
	 * @param userId
	 * @param _dao
	 * @throws Exception
	 */
	public void doSaveSubProcess(String destBasePath, String burstXmlFilePrefix, String currRevNum, String currRevDate, String model, 
			String manualType, String taskId, String userId, Object _dao, Object kcontext){
		// xml 파일 폴더별 이동
		// xml 파일 파싱 후 DB 저장을 수행한다.
		String detailId = "";
		if(_dao!=null){
			sgmlProcessDao = (SgmlProcessMapper)_dao;
			detailId = ""+((ProcessContext)kcontext).getVariable("detailId");
		}
		try{
			String procMsg = "[SaveXmlSubProcess] destBasePath:"+destBasePath+", burstXmlFilePrefix:"+burstXmlFilePrefix+", currRevNum:"+currRevNum
				+", currRevDate:"+currRevDate+", model:"+model+", manualType:"+manualType+", taskId:"+taskId+", userId:"+userId+"\\r\\n";
			String burstedXmlFolderPath = destBasePath + "temp" + FILE_SEPARATOR + "converted" + FILE_SEPARATOR;
			File _burstedXmlFolder = new File(burstedXmlFolderPath);
			if(_burstedXmlFolder.isDirectory()){
				File[] burstedXmlFileArray = _burstedXmlFolder.listFiles();
				// burst 된 xml의 prefix로 시작되는 파일이면 
				// 필요한 정보를 파싱한다.
				if(burstedXmlFileArray!=null){
					FileDirManager fdm = new FileDirManager();
					String burstedXmlDestPath = fdm.getBurstedXmlDestPath(destBasePath, model, manualType, currRevNum);
					String destImagePath = fdm.getImageDestPath(destBasePath, model, manualType, currRevNum);
					String contentType = fdm.getContentTypeOpRt(manualType, model);
					File _burstedXmlDestFolder = new File(burstedXmlDestPath);
					System.out.println("[SaveXmlSubProcess] convertedXml count: "+burstedXmlFileArray.length);
					procMsg += "[SaveXmlSubProcess] convertedXml count: "+burstedXmlFileArray.length + "\\r\\n";
					/////////
					// manualType이 AMM일 경우에만 burst된 XML에 대한 파싱 및 DB 저장 처리를 하고 
					// FRMFIM, TSM 일 경우는 변환된 full XML, TR일 경우는 변환된 XML만 대상 폴더로 이동시킨다.
					if(manualType!=null && "AMM".equalsIgnoreCase(manualType)){
						////
						// batch 처리.
						int batchSize = 100;
						int prevBatchIndex = 0;
						int excuteCount = 0;
						int totalCount = burstedXmlFileArray.length;
						//if(sgmlProcessDao!=null)sgmlProcessDao.startBatch();
						for(int i=0;i<burstedXmlFileArray.length;i++){
							if(burstedXmlFileArray[i].getName().startsWith(burstXmlFilePrefix)){
								excuteCount ++;
								
								Map<String, Object> parseResultMap = parseBurstedXml(burstedXmlFileArray[i]);
								
								saveMetaData(burstedXmlFileArray[i], parseResultMap, model, manualType, contentType,
										currRevNum, currRevDate, userId, burstedXmlDestPath, destImagePath, taskId, _dao);
								
								System.out.println("[SaveXmlSubProcess] save "+(i+1)+"th "+burstedXmlFileArray[i].getAbsolutePath()+" to DB is completed.");
							}
							if(i!=0 && (i%batchSize==0 || i==burstedXmlFileArray.length-1)){
								if(sgmlProcessDao!=null){
									
									//진행 체크 로그
									HashMap<String, Object> pMap = new HashMap<String, Object>();
									pMap.put("detailId", detailId);
									pMap.put("errorMessageContents", excuteCount+"/"+totalCount+" 처리중 ...");
									sgmlProcessDao.addTaskDetailLog(pMap);
									
									//sgmlProcessDao.executeBatch();
									System.out.println("[SaveXmlSubProcess] batch executed. "+(prevBatchIndex+1)+"th - "+(i+1)+"th of total "+burstedXmlFileArray.length);
									prevBatchIndex = i;
								}
							}
						}
					}
					copyBurstedXml(_burstedXmlFolder, _burstedXmlDestFolder);
					System.out.println("[SaveXmlSubProcess] copy "+_burstedXmlFolder.getAbsolutePath()+" to "+_burstedXmlDestFolder.getAbsolutePath()+" is completed.");
					procMsg += "[SaveXmlSubProcess] copy "+_burstedXmlFolder.getAbsolutePath()+" to "+_burstedXmlDestFolder.getAbsolutePath()+" is completed." + "\\r\\n";
				}
				
			}else{
				throw new Exception("Bursted XML Folder is not valid directory. "+_burstedXmlFolder.getAbsolutePath());
			}
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Saving Bursted XML Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * xml 파일을 파싱하여 필요한 정보를 얻는다.
	 */
	public Map<String, Object> parseBurstedXml(File burstedXmlFile){
		Map<String, Object> parseResultMap = new HashMap<String, Object>();
		String xmlData = "";
		try {
			xmlData = FileUtils.readFileToString(burstedXmlFile, "UTF-8");
			parseResultMap.put("TITLE", parseTitleKeyword(xmlData));
			parseResultMap.put("IMAGE", parseGnbrImageAttr(xmlData));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parseResultMap;
	}
	
	/**
	 * xml 파일에서 검색 키워드로 사용될 TITLE 엘레멘트 값을 파싱한다.
	 * @param xml
	 * @return
	 */
	private List<String> parseTitleKeyword(String xml){
		List<String> titleKeywordList = new ArrayList<String>();
		XmlXpathUtil xpathXmlUtil = new XmlXpathUtil();
		titleKeywordList = xpathXmlUtil.getElemValuesByElemName(xml, "TITLE");
		return titleKeywordList;
	}
	
	/**
	 * xml 파일에서 이미지 리소스 정보를 추출한다.
	 * @param xml
	 * @return
	 */
	private List<String> parseGnbrImageAttr(String xml){
		List<String> imageAttrValueList = new ArrayList<String>();
		XmlXpathUtil xpathXmlUtil = new XmlXpathUtil();
		imageAttrValueList = xpathXmlUtil.getAttrValuesByElemNameAndAttrName(xml, "SHEET", "GNBR");
		return imageAttrValueList;
	}
	
	
	
	/**
	 * xml 파일 메타 정보를 DB에 저장한다.
	 * @param burstedXmlFile
	 * @param parseResultMap
	 * @param _dao
	 * @throws Exception 
	 */
	public void saveMetaData(File burstedXmlFile, Map<String, Object> parseResultMap, String model, 
			String manualType, String contentType, String revNum, String revDate, String userId, String burstedXmlDestPath, 
			String destImagePath, String taskId, Object _dao) throws Exception{
		// xml 정보를 content 테이블에 insert 한 후
		// keyword 테이블에서 keyword 목록을 모두 읽어와 메모리에 로딩하고(캐싱처리) 
		// 있는 키워드일 경우 keyword map 테이블에 insert
		// 없는 키워드일 경우 메모리에 로딩된 목록에 하나 추가하고 keyword map 테이블에 insert.
		String repTitle = "";
		if(parseResultMap!=null){
			Object _obj = parseResultMap.get("TITLE");
			if(_obj!=null){
				List<String> titleList = (List<String>)_obj;
				if(titleList.size()>0){
					// 대표 타이틀은 파싱된 첫번째 타이틀을 사용.
					repTitle = titleList.get(0);
					///////
					// 컬럼 사이즈가 240이므로 길이가 길면 자른다.
					if(repTitle!=null && repTitle.length()>240){
						repTitle = repTitle.substring(0, 240);
					}
				}
			}
		}
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("model", model);
		paramMap.put("manualType", manualType);
		paramMap.put("contentType", contentType);
		paramMap.put("fileName", burstedXmlFile.getName());
		paramMap.put("fileUrl", burstedXmlDestPath+burstedXmlFile.getName());
		paramMap.put("revSeq", revNum);
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
		Date currRevDate = simpledateformat.parse(revDate);
		paramMap.put("revDate", currRevDate);
		///////////
		// 8000	Revision 타입	
		// R	Revise	1	
		// N	New	2	
		// D	Delete	3	

		paramMap.put("revType", "R");
		paramMap.put("title", repTitle);
		paramMap.put("userIdV", userId);
		paramMap.put("userId", StringUtils.isNumeric(userId)==true?userId:"-1");
		
		long contentId = 0L;
		try{
			if(sgmlProcessDao!=null)
				contentId = sgmlProcessDao.addContent(paramMap);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		
		// task와 Content 맵핑 테이블에 insert.
		HashMap<String, Object> paramMap1 = new HashMap<String, Object>();
		paramMap1.put("contentId", contentId);
		paramMap1.put("taskId", taskId);
		try{
			if(sgmlProcessDao!=null)
				sgmlProcessDao.addTaskMap(paramMap1);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		
		insertOrUpdateKeyword(""+contentId, userId, parseResultMap);
		insertOrUpdateImage(""+contentId, model, manualType, userId, destImagePath, parseResultMap);
	}
	
	/**
	 * 키워드 DB 저장 수행.
	 * @param paramMap
	 * @throws Exception 
	 */
	private void insertOrUpdateKeyword(String contentId, String userId, Map<String, Object> keywordMap) throws Exception{
		// keyword 테이블에서 keyword 목록을 모두 읽어와 메모리에 로딩하고(캐싱처리) 
		// 있는 키워드일 경우 keyword map 테이블에 insert
		// 없는 키워드일 경우 메모리에 로딩된 목록에 하나 추가하고 keyword map 테이블에 insert.
		if(this.keywordCacheList==null){
			if(sgmlProcessDao!=null){
				keywordCacheList = sgmlProcessDao.getKeywordList(new HashMap<String, Object>());
			}
		}
		// TITLE에 대해서만 키워드 저장을 수행.
		List<String> keywordList = null;
		if(keywordMap!=null){
			Object _obj = keywordMap.get("TITLE");
			if(_obj!=null){
				keywordList = (List<String>)_obj;
			}
		}
		if(keywordList!=null && keywordList.size()>0){
			for(int i=0;i<keywordList.size();i++){
				String _keyword = getFirstWord(keywordList.get(i), 180);
				if(StringUtils.isEmpty(_keyword))continue;
				////
				// keyword 테이블은 단어를 기준으로 설계되었음.
				// 컬럼 사이즈가 200자이므로 일단 TITLE은 첫 음절을 넣고 사이즈가 더 길다면 
				// 180자 정도에서 자르는 것으로 처리한다.
				KeywordInfo matchedKeywordInfo = null;
				if(keywordCacheList!=null){
					for(int j=0;j<keywordCacheList.size();j++){
						KeywordInfo _keywordInfo = keywordCacheList.get(j);
						if(_keyword.equals(_keywordInfo.getKeyword())){
							matchedKeywordInfo = _keywordInfo;
							break;
						}
					}
				}
				if(matchedKeywordInfo!=null){ // 이미 키워드가 DB에 있는 경우
					HashMap<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("contentId", contentId);
					paramMap.put("keywordId", matchedKeywordInfo.getKeywordId());
					paramMap.put("userId", userId);
					try{
						if(!isAleadyContentMapInserted(contentId, matchedKeywordInfo.getKeywordId()))
							sgmlProcessDao.addContentMap(paramMap);
					}catch(Exception e){
						e.printStackTrace();
						throw e;
					}
				}else{ // 신규 키워드인 경우.
					if(sgmlProcessDao!=null){
						HashMap<String, Object> paramMap1 = new HashMap<String, Object>();
						paramMap1.put("keyword", _keyword);
						paramMap1.put("userId", userId);
						long insertedKeywordId = 0L;
						try{
							insertedKeywordId = sgmlProcessDao.addKeyword(paramMap1);
						}catch(Exception e){
							e.printStackTrace();
							throw e;
						}
						HashMap<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("contentId", contentId);
						paramMap.put("keywordId", insertedKeywordId);
						paramMap.put("userId", userId);
						try{
							if(!isAleadyContentMapInserted(contentId, ""+insertedKeywordId))
								sgmlProcessDao.addContentMap(paramMap);
						}catch(Exception e){
							e.printStackTrace();
							throw e;
						}
						
						KeywordInfo keywordInfoToCache = new KeywordInfo();
						keywordInfoToCache.setKeyword(_keyword);
						keywordInfoToCache.setKeywordId(""+insertedKeywordId);
						if(keywordCacheList==null){
							keywordCacheList = new ArrayList<KeywordInfo>();
						}
						keywordCacheList.add(keywordInfoToCache);
					}
				}
			}
		}
	}
	
	/**
	 * 이미지 리소스 정보 DB 저장 수행.
	 * @param contentId
	 * @param model
	 * @param manualType
	 * @param userId
	 * @param destImagePath
	 * @param resultMap
	 * @throws Exception
	 */
	private void insertOrUpdateImage(String contentId, String model, String manualType, String userId, String destImagePath, Map<String, Object> resultMap) throws Exception{
		List<String> imageList = null;
		if(resultMap!=null){
			Object _obj = resultMap.get("IMAGE");
			if(_obj!=null){
				imageList = (List<String>)_obj;
			}
		}
		if(imageList!=null && imageList.size()>0){
			for(int i=0;i<imageList.size();i++){
				String imageResource = imageList.get(i);
				if(StringUtils.isEmpty(imageResource))continue;
				
				if(sgmlProcessDao!=null){
					// cgm과 png 정보 save.
					HashMap<String, Object> paramMap = new HashMap<String, Object>();
					String imageBaseName = FilenameUtils.getBaseName(imageResource);
					paramMap.put("fileName", imageBaseName+".cgm");
					paramMap.put("fileUrl", destImagePath+imageBaseName+".cgm");
					paramMap.put("model", model);
					paramMap.put("manualType", manualType);
					paramMap.put("userId", userId);
					long insertedImageId = 0L;
					try{
						insertedImageId = sgmlProcessDao.addImage(paramMap);
					}catch(Exception e){
						e.printStackTrace();
						throw e;
					}
					////////////
					// xml contents와 image 맵핑 테이블은 삭제됨.
//					Map<String, Object> paramMap1 = new HashMap<String, Object>();
//					paramMap1.put("contentId", contentId);
//					paramMap1.put("imageId", insertedImageId);
//					paramMap1.put("userId", userId);
//					try{
//						if(!isAleadyImageMapInserted(contentId, ""+insertedImageId))
//							sgmlProcessDao.addImageMap(paramMap1);
//					}catch(Exception e){
//						e.printStackTrace();
//						throw e;
//					}
					
					HashMap<String, Object> paramMap2 = new HashMap<String, Object>();
					paramMap2.put("fileName", imageBaseName+".png");
					paramMap2.put("fileUrl", destImagePath+imageBaseName+".png");
					paramMap2.put("model", model);
					paramMap2.put("manualType", manualType);
					paramMap2.put("userId", userId);
					try{
						insertedImageId = sgmlProcessDao.addImage(paramMap2);
					}catch(Exception e){
						e.printStackTrace();
						throw e;
					}
					
//					Map<String, Object> paramMap3 = new HashMap<String, Object>();
//					paramMap3.put("contentId", contentId);
//					paramMap3.put("imageId", insertedImageId);
//					paramMap3.put("userId", userId);
//					try{
//						if(!isAleadyImageMapInserted(contentId, ""+insertedImageId))
//							sgmlProcessDao.addImageMap(paramMap3);
//					}catch(Exception e){
//						e.printStackTrace();
//						throw e;
//					}
					
					
				}
			}
		}
	}
	
	/**
	 * 처리가 끝난 xml 파일들을 대상 디렉토리로 옮긴다.
	 * @param burstedXmlFolder
	 * @param destPath
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void copyBurstedXml(File burstedXmlFolder, File destPath) throws Exception {
		try{
		FileUtils.copyDirectory(burstedXmlFolder, destPath);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 인자로 주어진 스트링에서 첫 음절을 잘라내어 리턴.
	 * @param _str
	 * @param maxLen
	 * @return (String)
	 */
	private String getFirstWord(String _str, int maxLen){
		String rt = "";
		if(_str!=null){
			String[] wordArr = _str.trim().split(" ");
			if(wordArr!=null){
				String _word = wordArr[0];
				if(_word!=null && _word.length()>maxLen){
					_word = _word.substring(0, maxLen);
				}
				rt = _word;
			}
		}
		return rt;
	}
	
	/**
	 * 현재 contentId와 keywordId로 이미 DB에 맵핑 정보를 넣었는지 확인한다.
	 * @param contentId
	 * @param keywordId
	 * @return
	 */
	private boolean isAleadyContentMapInserted(String contentId, String keywordId){
		boolean rt = false;
		if(this.contentMapCache.containsKey(contentId)){
			List<String> keywordIdList = (List<String>)contentMapCache.get(contentId);
			boolean isKeywordIdContained = false;
			for(int i=0;i<keywordIdList.size();i++){
				if(keywordId.equals(keywordIdList.get(i))){
					isKeywordIdContained = true;
					rt = true;
					break;
				}
			}
			if(!isKeywordIdContained){
				keywordIdList.add(keywordId);
				contentMapCache.put(contentId, keywordIdList);
			}
		}else{
			List<String> keywordIdList = new ArrayList<String>();
			keywordIdList.add(keywordId);
			contentMapCache.put(contentId, keywordIdList);
		}
		
		return rt;
	}
	
	/**
	 * 현재 contentId와 imageId로 이미 DB에 맵핑 정보를 넣었는지 확인한다.
	 * @param contentId
	 * @param keywordId
	 * @return
	 */
	private boolean isAlreadyImageMapInserted(String contentId, String imageId){
		boolean rt = false;
		if(this.imageMapCache.containsKey(contentId)){
			List<String> imageIdList = (List<String>)imageMapCache.get(contentId);
			boolean isImageIdContained = false;
			for(int i=0;i<imageIdList.size();i++){
				if(imageId.equals(imageIdList.get(i))){
					isImageIdContained = true;
					rt = true;
					break;
				}
			}
			if(!isImageIdContained){
				imageIdList.add(imageId);
				imageMapCache.put(contentId, imageIdList);
			}
		}else{
			List<String> imageIdList = new ArrayList<String>();
			imageIdList.add(imageId);
			imageMapCache.put(contentId, imageIdList);
		}
		return rt;
	}

}
