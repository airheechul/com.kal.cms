package com.kal.cms.task.sgml;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

import com.kal.cms.task.mapper.SgmlProcessMapper;;

/**
 * Task 관련 리소스를 반환하는 서브 프로세스.
 */
public class ReleaseTaskSubProcess {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final ReleaseTaskSubProcess INSTANCE = new ReleaseTaskSubProcess();
	private SgmlProcessMapper sgmlProcessDao = null;
	
	public static ReleaseTaskSubProcess getInstance(){
		return INSTANCE;
	}
	
	public void doReleaseTask(String destBasePath, String taskId, String userId, Object _dao, Object kcontext){
		if(_dao!=null)
			sgmlProcessDao = (SgmlProcessMapper)_dao;
		/////////
		// 변환 작업이 되었던 temp 디렉토리를 삭제하고 
		// Task DB 정보를 완료로 업데이트한다.
		// 각각의 서브 Task에 대한 상태 업데이트는 각 Task별 핸들러를 차후 구현하여 
		// 상태값을 업데이트할 수 있도록 처리해야 한다.
		String tempDir = destBasePath + "temp";
		
		try{
			String procMsg = "[ReleaseTaskSubProcess] destBasePath:"+destBasePath+", taskId:"+taskId+", userId:"+userId+"\\r\\n";
			deleteTempDir(tempDir);
			System.out.println("[ReleaseTaskSubProcess] deleting temp folder ("+tempDir+") is completed.");
			procMsg += "[ReleaseTaskSubProcess] deleting temp folder ("+tempDir+") is completed." + "\\r\\n";
			// 상태 complete로 처리는 process event handler에서 처리함.
			//updateTaskToComplete(taskId);
			System.out.println("[ReleaseTaskSubProcess] releasing task is completed.");
			procMsg += "[ReleaseTaskSubProcess] releasing task is completed." + "\\r\\n";
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Releasing Task Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
		
	}
	
	/**
	 * 임시 디렉토리 삭제.
	 * @param tempDir
	 * @throws Exception
	 */
	public void deleteTempDir(String tempDir) throws Exception{
		File tempDirFile = new File(tempDir);
		if(tempDirFile.exists()){
			FileUtils.forceDelete(tempDirFile);
		}
	}
	
	public void updateTaskToComplete(String taskId) throws Exception{
		if(sgmlProcessDao!=null){
			// status code
			//7001	Complete
			//7002	In Progress
			//7003	Fail
			//7004	Queued
			//7005	Aborted
			HashMap<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("taskId", taskId);
			pMap.put("endDate", new Date());
			pMap.put("status", "7001");
			sgmlProcessDao.updateTaskStatus(pMap);
		}
	}
}
