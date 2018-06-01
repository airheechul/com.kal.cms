package com.kal.cms.task.service;

import java.util.HashMap;
import java.util.List;

import com.kal.cms.task.vo.TaskDetailInfo;
import com.kal.cms.task.vo.TaskInfo;

public interface ProcessMonitorService {

	public List<TaskInfo> findTaskList(HashMap<String, Object> pMap) ;
	
	public List<TaskDetailInfo> findTaskDetailLogList(HashMap<String, Object> pMap) ;
	
	public void doSgmlFlow(HashMap<String , Object> pMap) throws Exception;
	
	public void doValidation(HashMap<String, Object> pMap) throws Exception;
	
	public long registTask(HashMap<String, Object> pMap) throws Exception;
	
	public int getTaskTotalCount(HashMap<String, Object> pMap) throws Exception;
	
	public void validateTask(HashMap<String, Object> pMap) throws Exception;
	
	public boolean checkTaskStatus(HashMap<String, Object> pMap) ;
	
	public boolean taskDelete(HashMap<String, Object> pMap);
	
	public boolean taskAbort(HashMap<String, Object> pMap);
	
	public boolean taskResume(HashMap<String, Object> pMap);
	
}
