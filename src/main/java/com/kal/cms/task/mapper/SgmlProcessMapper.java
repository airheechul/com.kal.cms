package com.kal.cms.task.mapper;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kal.cms.task.vo.KeywordInfo;
import com.kal.cms.task.vo.TaskDetailInfo;
import com.kal.cms.task.vo.TaskInfo;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("sgmlProcessMapper")
public class SgmlProcessMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(SgmlProcessMapper.class);

/*	public List<TaskInfo> getTaskList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMonitorMapper.getTaskList", pMap);
	}
	
	public List<TaskDetailInfo> getTaskDetailLog(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMonitorMapper.getTaskDetailLog", pMap);
	}
	
	public List<TaskDetailInfo> getSelectedTaskList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMonitorMapper.getSelectedTaskList", pMap);
	}
	
	public String getTaskStatus(HashMap<String, Object> pMap){
		return (String)selectOne("com.kal.cms.task.mapper.TaskMonitorMapper.getTaskStatus", pMap);
	}
	
	public int updateMultiTaskStatus(HashMap<String, Object> pMap) {
		return update("com.kal.cms.task.mapper.TaskMonitorMapper.updateMultiTaskStatus", pMap);
	}*/
	
	
	public List<KeywordInfo> getKeywordList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMapper.getKeywordList", pMap);
	}
	
	
	public int getLastRevSeq(HashMap<String, Object> pMap) {
		return (int)selectOne("com.kal.cms.task.mapper.TaskMapper.getLastRevSeq", pMap);
	}
	
	
	public List<TaskInfo> getTaskList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMapper.getTaskList", pMap);
	}
	

	public List<TaskDetailInfo> getTaskDetailList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.task.mapper.TaskMapper.getTaskDetailList", pMap);
	}
	
	
	public int addTaskDetailLog(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addTaskDetailLog", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addTaskDetailLog Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	public int addContent(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addContent", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addContent Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	public int addKeyword(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addKeyword", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addKeyword Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int addContentMap(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addContentMap", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addContentMap Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int addImage(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addImage", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addImage Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int addImageMap(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addImageMap", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addImageMap Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int addTaskMap(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addTaskMap", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addTaskMap Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int addTask(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addTask", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addTask Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int addTaskDetail(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.task.mapper.TaskMapper.addTaskDetail", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("TaskMapper.addTaskDetail Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}
	
	
	public int updateTask(HashMap<String, Object> pMap) {
		return update("com.kal.cms.task.mapper.TaskMapper.updateTask", pMap);
	}

	
	public int updateTaskDetail(HashMap<String, Object> pMap) {
		return update("com.kal.cms.task.mapper.TaskMapper.updateTaskDetail", pMap);
	}
	
	
	public int updateTaskStatus(HashMap<String, Object> pMap) {
		return update("com.kal.cms.task.mapper.TaskMapper.updateTaskStatus", pMap);
	}
	
	
	public int updateTaskDetailStatus(HashMap<String, Object> pMap) {
		return update("com.kal.cms.task.mapper.TaskMapper.updateTaskDetailStatus", pMap);
	}
	
	
	
}
