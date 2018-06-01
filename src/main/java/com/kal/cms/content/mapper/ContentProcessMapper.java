package com.kal.cms.content.mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kal.cms.common.constants.AdminConstants.TaskConstants;
import com.kal.cms.content.vo.ContentInfo;
import com.kal.cms.content.vo.ContentTaskInfo;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("contentProcessMapper")
public class ContentProcessMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentProcessMapper.class);

	public List<ContentInfo> getContentProcessList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getContentProcessPageList", pMap);
	}
	
	public List<ContentInfo> getContentDownloadList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getDownloadContentProcessPageList", pMap);
	}

	public List<ContentInfo> getSelectContentList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getSelectContentList", pMap);
	}
	
	public List<ContentInfo> getSelectAllContentList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getSelectAllContentList", pMap);
	}
	
	public List<ContentInfo> getSelectAllImageList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getSelectAllImageList", pMap);
	}
	
	public List<ContentTaskInfo> getReportList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getReportList", pMap);
	}
	
	public List<ContentInfo> getContentImageList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getSelectImageList", pMap);
	}
	
	public List<ContentInfo> getSelectImageList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getSelectImageList", pMap);
	}
	
	public String getTaskStatus(HashMap<String, Object> pMap) {
		return selectOne("com.kal.cms.content.mapper.ContentMapper.getTaskStatus", pMap);
	}

	
	public int insertTaskInfo(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addTaskInfo", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentSearchMapper.addContent Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}

	
	public int insertTaskDetail(HashMap<String, Object> pMap) {
		int result = -1;
		
		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addTaskDetail", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentProcessMapper.addTaskDetail Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}

	
	public int insertTaskDetailLog(HashMap<String, Object> pMap) {
		int result = -1;
		
		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addTaskDetailLog", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentProcessMapper.addTaskDetailLog Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}

	
	public boolean insertContentInfo(HashMap<String, Object> pMap) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> fileInfo = (List<HashMap<String, Object>>)pMap.get("fileInfoList");
		
		int totalCount = fileInfo.size();
		int excuteCount = 0;
		int checkTermCount = 0;
		
		pMap.put("errorMessageContents", totalCount+"건 DB 등록 처리 시작");
		insert("com.kal.cms.content.mapper.ContentMapper.addTaskDetailLog", pMap);

		
		if("IMAGE".equals((String)pMap.get("masterWOCntsTypCd"))) {
			System.out.println(" 이미지 파일 정보 등록 시작  ... ");
			for (HashMap<String, Object> dataMap : fileInfo) {
				
				checkTermCount++;
				excuteCount++;
				if (checkTermCount >= TaskConstants.TASK_PROCESS_CHECK_TERM){
					checkTermCount = 0;
					
					// 주기적으로 프로세스 정지 여부 확인한다.
/*					String taskStatus = (String)selectOne("com.kal.cms.content.mapper.ContentMapper.getTaskStatus", pMap);
					if(TaskConstants.TASK_STATUS_ABORTED.equals(taskStatus)){
						throw new MpFrameException("사용자 Abort 요청에 의한 종료", TaskConstants.TASK_STATUS_ABORTED);
					}*/
					
					// 주기적으로 로그 남긴다.
					pMap.put("errorMessageContents", excuteCount +"/" +totalCount +" 처리중...");
					insert("com.kal.cms.content.mapper.ContentMapper.addTaskDetailLog", pMap);
				}
				
				System.out.println(" - "+dataMap.get("fileName") + " .. 처리중 .."+pMap.get("userId"));

				dataMap.put("masterWOCntsTskId", pMap.get("masterWOCntsTskId"));
				dataMap.put("maintDocModelCode", pMap.get("maintDocModelCode"));
				dataMap.put("WOCntsDocSubtypCd", pMap.get("WOCntsDocSubtypCd"));
				
				/*	2014-04-17 by air
				 *  Image 파일인 경우, 중복(revision)을 허용하지 않는다. 필터 조건 추가. */
				int existImageFileCount = 0;
				try {
					existImageFileCount = (Integer)selectOne("com.kal.cms.content.mapper.ContentMapper.getCountExistImageFile", dataMap);
				} catch (Exception e)
				{
					throw e;
				}
				
				try {
					/*	2014-04-17 by air
					 *  Image 파일인 경우, 중복count가 0 일때만 table 에 insert. */
					if (existImageFileCount == 0) 
					{
						insert("com.kal.cms.content.mapper.ContentMapper.addImageInfo",dataMap);
					}
					
				} catch (Exception e) {
					throw e;
				}
			}	
		} else {	
			System.out.println(" XML 파일 정보 등록 시작  ... ");
			for (HashMap<String, Object> dataMap : fileInfo) {
				if (dataMap.get("skipData").equals("N")){
					checkTermCount++;
					excuteCount++;
					if (checkTermCount >= TaskConstants.TASK_PROCESS_CHECK_TERM){
						checkTermCount = 0;
						
						// 주기적으로 프로세스 정지 여부 확인한다.
/*						String taskStatus = (String)selectOne("com.kal.cms.content.mapper.ContentMapper.getTaskStatus", pMap);
						if(TaskConstants.TASK_STATUS_ABORTED.equals(taskStatus)){
							throw new MpFrameException("사용자 Abort 요청에 의한 종료", TaskConstants.TASK_STATUS_ABORTED);
						}*/
						
						// 주기적으로 로그 남긴다.
						pMap.put("errorMessageContents", excuteCount +"/" +totalCount +" 처리중...");
						insert("com.kal.cms.content.mapper.ContentMapper.addTaskDetailLog", pMap);
					}				
					
					System.out.println(" - "+dataMap.get("fileName") + " .. 처리중 .."+pMap.get("userId") +", REV_SEQ = "+dataMap.get("masterWOCntsRevzNo"));
					
					dataMap.put("userIdV", (String)pMap.get("userId"));
					dataMap.put("userId", pMap.get("userId"));
					dataMap.put("masterWOCntsTskId", pMap.get("masterWOCntsTskId"));
					dataMap.put("maintDocModelCode", pMap.get("maintDocModelCode"));
					dataMap.put("WOCntsDocSubtypCd", pMap.get("WOCntsDocSubtypCd"));
					dataMap.put("masterWOCntsTypCd", pMap.get("masterWOCntsTypCd"));
					
					int contentId = 0;
					try {
						// 컨텐츠 처리시 ReleaseYn = 'Y' 가 아닌 것들은 masterWorkorderContentsId 가 셋팅되어 있음.
						// 해당 컨텐츠들은 업데이트로 처리
						if(dataMap.get("masterWorkorderContentsId") == null ) {
							contentId = (Integer)insert("com.kal.cms.content.mapper.ContentMapper.addContentInfo", dataMap);
							
							insert("com.kal.cms.admin.mapper.AdminMapper.addAdmin", pMap);
							
							dataMap.put("masterWorkorderContentsId", contentId);
							
							insert("com.kal.cms.content.mapper.ContentMapper.addTaskContents", pMap);
							
							// 키워드 항목 갯수대로 loop
							for (String keyword : (List<String>)dataMap.get("keywordList")) {
								//pMap.put("keyword", keyword);	// TODO db 컬럼 사이즈 제한 때문에 아래 코드로 임시 처리
								dataMap.put("keyword", keyword.length() > 100 ? keyword.substring(0, 100) : keyword);
								
								int keywordId = (Integer)insert("com.kal.cms.content.mapper.ContentMapper.addContentKeyword", dataMap);
								
								dataMap.put("keywordId", keywordId);
								insert("com.kal.cms.content.mapper.ContentMapper.addContentMap", pMap);
							}						
						} else {
							update("com.kal.cms.content.mapper.ContentMapper.updateContentRevision", pMap);
						}

					} catch (Exception e) {
						throw e;
					}
				}
			}
		}
		// 종료 로그 남긴다.
		pMap.put("errorMessageContents", excuteCount +"/" +totalCount +" 처리 종료");
		insert("com.kal.cms.content.mapper.ContentMapper.addTaskDetailLog", pMap);
		
		return true;
	}
	
	
	public int updateTaskCompleteWithFile(HashMap<String, Object> pMap) {
		return update("com.kal.cms.content.mapper.ContentMapper.updateTaskCompleteWithFile", pMap);
	}
	

	public int updateTask(HashMap<String, Object> pMap) {
		return update("com.kal.cms.content.mapper.ContentMapper.updateTask", pMap);
	}
	
	
	public int updateTaskDetail(HashMap<String, Object> pMap) {
		return update("com.kal.cms.content.mapper.ContentMapper.updateTaskDetail", pMap);
	}


	public int updateTaskFile(HashMap<String, Object> pMap) {
		return update("com.kal.cms.content.mapper.ContentMapper.updateTaskFile", pMap);
	}
	
	
	
	public int execCheckIn(HashMap<String, Object> pMap) {

		int result = -1;
		
/*		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addTaskInfo", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentSearchMapper.addContent Exception: {}", e);
				e.printStackTrace();
		}*/
		
		
		HashMap<String, Object> dataMap = new HashMap<String, Object>() ;
		
		dataMap.put("contentId", pMap.get("masterWorkorderContentsId") == null ? "0" : pMap.get("masterWorkorderContentsId"));
		dataMap.put("taskId", pMap.get("masterWOCntsTskId") == null ? "0" : pMap.get("masterWOCntsTskId"));
		dataMap.put("userId", pMap.get("userId"));
		dataMap.put("immediateYn", pMap.get("immediateYn"));
		dataMap.put("v_buff", "");
		dataMap.put("v_ret", "");
		
		
		System.out.println("---------- check in dao --------------");
		System.out.println("contentId :"+dataMap.get("contentId"));
		System.out.println("taskId : "+dataMap.get("taskId"));
		System.out.println("userId : "+dataMap.get("userId"));
		System.out.println("immediateYn : "+dataMap.get("immediateYn"));
		/*
		HashMap<String, Object> afterCheckInMap = (HashMap<String, Object>)selectOne("com.kal.cms.content.mapper.ContentMapper.callCheckIn", dataMap);
		
		System.out.println("v_buff : "+afterCheckInMap.get("v_buff"));
		System.out.println("v_ret : "+afterCheckInMap.get("v_ret"));
		*/
		selectOne("com.kal.cms.content.mapper.ContentMapper.callCheckIn", dataMap);
		
		System.out.println("after dataMap v_buff : "+dataMap.get("v_buff"));
		System.out.println("after dataMap v_ret : "+dataMap.get("v_ret"));
		
		System.out.println("----------------------------------------");

		/*
		if("0".equals(afterCheckInMap.get("v_ret"))){
			if(pMap.get("masterWOCntsTskId") != null){
				//task 업데이트 인 경우 해당하는 컨텐츠도 같이 업데이트 한다.
				try {
					//getSqlMapClient().startTransaction();
					
					getSqlMapClient().update("Content.updateTaskRelease", pMap);
					
					getSqlMapClient().update("Content.updateTaskContentRelease", pMap);
					
					//getSqlMapClient().commitTransaction();

				} catch (Exception e) {
					
				} finally {
					try {
						//getSqlMapClient().endTransaction();
					} catch (Exception e) {}	
				}

			}		
			
			if(pMap.get("masterWorkorderContentsId") != null){
				getSqlMapClient().update("Content.updateContentRelease", pMap);
			}
		} else {
			pMap.put("result", dataMap.get("v_buff"));
			
			//throw new MpFrameException("체크인 프로시져 실행 실패 [ code ="+dataMap.get("v_ret")+", error="+dataMap.get("v_buff") +" ]");
		}
		*/
		

		return result;
	}
	
}
