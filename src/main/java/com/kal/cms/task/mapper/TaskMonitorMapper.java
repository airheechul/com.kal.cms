package com.kal.cms.task.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kal.cms.task.vo.TaskDetailInfo;
import com.kal.cms.task.vo.TaskInfo;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("taskMonitorMapper")
public class TaskMonitorMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskMonitorMapper.class);

	public List<TaskInfo> getTaskList(HashMap<String, Object> pMap) {
		
/*		Iterator iter = pMap.entrySet().iterator();
    	while(iter.hasNext()) {
    	    Entry<String, Object> entry =  (Entry<String, Object>) iter.next();
    	    String key = entry.getKey();
    	    Object value = entry.getValue();
    	    
    	    LOGGER.debug("key / value => " + key + "/" + value);
    	}
		*/
		return selectList("com.kal.cms.task.mapper.TaskMapper.getTaskPageList", pMap);
	}
	
	public List<TaskDetailInfo> getTaskDetailLog(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMapper.getTaskDetailLog", pMap);
	}
	
	public List<TaskInfo> getSelectedTaskList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMapper.getSelectedTaskList", pMap);
	}
	
	public String getTaskStatus(HashMap<String, Object> pMap){
		return (String)selectOne("com.kal.cms.task.mapper.TaskMapper.getTaskStatus", pMap);
	}
	
	public int getTaskTotalCount(HashMap<String, Object> pMap){
		return (int)selectOne("com.kal.cms.task.mapper.TaskMapper.getTaskTotalCount", pMap);
	}
		
	public int updateMultiTaskStatus(HashMap<String, Object> pMap) {
		return update("com.kal.cms.task.mapper.TaskMapper.updateMultiTaskStatus", pMap);
	}
	
}
