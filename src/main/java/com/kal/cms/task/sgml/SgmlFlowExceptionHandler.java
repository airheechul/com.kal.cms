package com.kal.cms.task.sgml;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.kal.cms.task.mapper.SgmlProcessMapper;

/**
 * SGML flow 간 Exception 발생시 Task 관련 DB 테이블에 status 업데이트한다.
 */
public class SgmlFlowExceptionHandler {
	private static final SgmlFlowExceptionHandler INSTANCE = new SgmlFlowExceptionHandler();
	private SgmlProcessMapper sgmlProcessDao = null;
	
	public static SgmlFlowExceptionHandler getInstance(){
		return INSTANCE;
	}
	
	public void logTaskException(String taskId, String detailId, String errorMsg, Object _dao){
		if(_dao!=null){
			this.sgmlProcessDao = (SgmlProcessMapper)_dao;
		}
		System.out.println("[SgmlFlowExceptionHandler] taskId:"+taskId+", detailId:"+detailId+", errorMsg:"+errorMsg);
		// 서브 프로세스를 fail 처리한 후, 메인 프로세스를 fail 처리.
		failSubProcess(detailId, errorMsg);
		failSgmlProcess(taskId);
	}
	
	/**
	 * 현재 SGML flow의 task 상태를 fail로 처리.
	 * @param taskId
	 */
	private void failSgmlProcess(String taskId){
		if(this.sgmlProcessDao!=null){
			// status code
			//7001	Complete
			//7002	In Progress
			//7003	Fail
			//7004	Queued
			//7005	Aborted
			
			HashMap<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("taskId", taskId);
			pMap.put("status", "7003");
			pMap.put("endDate", new Date());
			try{
				sgmlProcessDao.updateTaskStatus(pMap);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 해당하는 SGML 서브 프로세스의 상태를 fail로 처리.
	 * @param detailId
	 */
	private void failSubProcess(String detailId, String errorMsg){
		if(this.sgmlProcessDao!=null){
			HashMap<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("detailId", detailId);
			pMap.put("endDate", new Date());
			pMap.put("status", "7003");
			try{
				sgmlProcessDao.updateTaskDetailStatus(pMap);
				
				//에러 로그
				pMap.put("errorMessageContents", errorMsg);
				sgmlProcessDao.addTaskDetailLog(pMap);				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
