package com.kal.cms.task.sgml;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeEvent;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;

import com.kal.cms.common.constants.AdminConstants.TaskConstants;
import com.kal.cms.task.mapper.SgmlProcessMapper;
import com.kal.cms.task.vo.TaskInfo;

/**
 * SGML Flow에 대한 이벤트 핸들러. task 관리 기능을 담당.
 */
public class SgmlProcessEventListener implements ProcessEventListener {
	private SgmlProcessMapper sgmlProcessDao = null;
	private String taskId = "";
	private String currentSubTaskId = "";
	
	public SgmlProcessEventListener(Object _dao, String taskId){
		if(_dao!=null){
			this.sgmlProcessDao = (SgmlProcessMapper)_dao;
			this.taskId = taskId;
		}
	}
	
	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		String nodeName = event.getNodeInstance().getNodeName();
		System.out.println("Before Node triggered. Node name:"+nodeName);
		
		// startEvent, endEvent 노드는 굳이 상태값을 저장하지 않음.
		if(isLoggerbleNode(nodeName)){
			if(this.sgmlProcessDao!=null){
				HashMap<String, Object> pMap = new HashMap<String, Object>();
				pMap.put("taskId", taskId);
				pMap.put("taskDetailName", getTaskDetailStr(nodeName));
				pMap.put("status", "7002");
				try{
					currentSubTaskId = ""+sgmlProcessDao.addTaskDetail(pMap);
					setProcessVariable(event, "detailId", currentSubTaskId);
					//시작 로그
					pMap.put("detailId", currentSubTaskId);
					pMap.put("errorMessageContents", getTaskDetailStr(nodeName) + " 시작");
					sgmlProcessDao.addTaskDetailLog(pMap);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		String nodeName = event.getNodeInstance().getNodeName();
		System.out.println("Before Node left. Node name:"+nodeName);
		setProcessVariable(event, "detailId", currentSubTaskId);
		// startEvent, endEvent 노드는 굳이 상태값을 저장하지 않음.
		if(isLoggerbleNode(nodeName)){
			if(this.sgmlProcessDao!=null){
				HashMap<String, Object> pMap = new HashMap<String, Object>();
				pMap.put("detailId", currentSubTaskId);
				pMap.put("endDate", new Date());
				pMap.put("status", "7001");
				try{
					
					sgmlProcessDao.updateTaskDetailStatus(pMap);
					String procMsg = "";
					Object procMsgObj = getProcessVariable(event, "procMsg");
					if(procMsgObj!=null){
						procMsg = (String)procMsgObj;
					}
					//종료 로그
					pMap.put("errorMessageContents", getTaskDetailStr(nodeName) + " 종료. [procMsg]"+procMsg);
					sgmlProcessDao.addTaskDetailLog(pMap);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		System.out.println("Before Process started. Process name:"+event.getProcessInstance().getProcessName());
		if(this.sgmlProcessDao!=null){
			// status code
			//7001	Complete
			//7002	In Progress
			//7003	Fail
			//7004	Queued
			//7005	Aborted
			
			// SGML 프로세스 시작 시 task 상태를 in progress로 업데이트한다.
			HashMap<String, Object> pMap = new HashMap<String, Object>();
			pMap.put("taskId", taskId);
			pMap.put("status", "7002");
			try{
				sgmlProcessDao.updateTaskStatus(pMap);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		System.out.println("After Process completed. Process name:"+event.getProcessInstance().getProcessName());
		// SGML 프로세스 종료 시 task 상태를 complete로 업데이트한다.
		if(this.sgmlProcessDao!=null){
			// 이 이벤트 핸들러는 에러가 발생해도 반드시 호출됨.
			// 그래서 현재 taskId의 status가 7002	In Progress 이거나 7004	Queued 일 때만 
			// complete로 업데이트한다.
			try{
				boolean isCompleteUpdatable = false;
				HashMap<String, Object> _pMap = new HashMap<String, Object>();
				_pMap.put("taskId", taskId);
				List<TaskInfo> taskInfoList = sgmlProcessDao.getTaskList(_pMap);
				if(taskInfoList!=null && taskInfoList.size()>0){
					if("7002".equals(taskInfoList.get(0).getStatus()) || TaskConstants.TASK_STATUS_QUEUED.equals(taskInfoList.get(0).getStatus()))
						isCompleteUpdatable = true;
				}
				if(isCompleteUpdatable){
					HashMap<String, Object> pMap = new HashMap<String, Object>();
					pMap.put("taskId", taskId);
					pMap.put("status", "7001");
					pMap.put("endDate", new Date());
					sgmlProcessDao.updateTaskStatus(pMap);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Task 노드 명으로 해당하는 작업 내용 스트링을 가져온다.
	 * @param nodeName
	 * @return (String)
	 */
	private String getTaskDetailStr(String nodeName){
		String rt = "";
		if(nodeName!=null){
			if(nodeName.equalsIgnoreCase("copySgmlResources")){
				rt = "Copy Original SGML AMM Resources For TR Process";
			}else if(nodeName.equalsIgnoreCase("findAndDecompressCGMZipFile")){
				rt = "Find CGM Zip file And Decompress";
			}else if(nodeName.equalsIgnoreCase("convertSgmlToTempXml")){
				rt = "Convert SGML to Temp XML";
			}else if(nodeName.equalsIgnoreCase("convertToFullXml")){
				rt = "Convert to Full XML";
			}else if(nodeName.equalsIgnoreCase("burstXml")){
				rt = "Burst XML";
			}else if(nodeName.equalsIgnoreCase("burstGraphicXml")){
				rt = "Burst Graphic XML";
			}else if(nodeName.equalsIgnoreCase("mergeGraphicXml")){
				rt = "Merge Graphic XML";
			}else if(nodeName.equalsIgnoreCase("saveXmlSubProcess")){
				rt = "Save XML File and Meta Data";
			}else if(nodeName.equalsIgnoreCase("convertCgmToPng")){
				rt = "Convert CGM to PNG";
			}else if(nodeName.equalsIgnoreCase("validateCgmToPng")){
				rt = "Validate Converted PNG files";
			}else if(nodeName.equalsIgnoreCase("saveImageSubProcess")){
				rt = "Save Image File and Meta Data";
			}else if(nodeName.equalsIgnoreCase("releaseTask")){
				rt = "Release Task and Remove Temp Files";
			}
		}
		return rt;
	}
	
	/**
	 * Task 관리 로깅을 할 대상 노드인지 판별.
	 * @param nodeName
	 * @return boolean
	 */
	private boolean isLoggerbleNode(String nodeName){
		boolean rt = true;
		if("StartProcess".equalsIgnoreCase(nodeName) || "End".equalsIgnoreCase(nodeName) || "Sub-Process".equalsIgnoreCase(nodeName) 
			|| "Error".equalsIgnoreCase(nodeName) || "ErrorEvent".equalsIgnoreCase(nodeName) || "ErrorHandling".equalsIgnoreCase(nodeName) 
			|| "StartSubProcess".equalsIgnoreCase(nodeName) || "EndSubProcess".equalsIgnoreCase(nodeName) || "Gateway".equalsIgnoreCase(nodeName))
			rt = false;
		
		return rt;
	}
	
	/**
	 * 현재 SGML process의 variable을 세팅한다.
	 * @param _event
	 * @param _key
	 * @param _value
	 */
	private void setProcessVariable(ProcessNodeEvent _event, String _key, String _value){
		try{
			org.jbpm.process.instance.ProcessInstance objProcessInstance = (org.jbpm.process.instance.ProcessInstance)_event.getProcessInstance();
			VariableScopeInstance objVariableScopeInstance = (VariableScopeInstance)objProcessInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
			objVariableScopeInstance.setVariable(_key, _value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 현재 SGML process의 variable을 가져온다.
	 * @param _event
	 * @param _key
	 * @param _value
	 */
	private Object getProcessVariable(ProcessNodeEvent _event, String _key){
		Object _value = null;
		try{
			org.jbpm.process.instance.ProcessInstance objProcessInstance = (org.jbpm.process.instance.ProcessInstance)_event.getProcessInstance();
			VariableScopeInstance objVariableScopeInstance = (VariableScopeInstance)objProcessInstance.getContextInstance(VariableScope.VARIABLE_SCOPE);
			_value = objVariableScopeInstance.getVariable(_key);
		}catch(Exception e){
			e.printStackTrace();
		}
		return _value;
	}

	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterProcessStarted(ProcessStartedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
