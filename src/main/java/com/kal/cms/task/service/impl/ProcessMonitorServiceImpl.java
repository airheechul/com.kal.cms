package com.kal.cms.task.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easycompany.cmm.exception.MpFrameException;
import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.common.constants.AdminConstants.TaskConstants;
import com.kal.cms.content.batch.ProcessCompressFile;
import com.kal.cms.content.batch.ProcessUploadFile;
import com.kal.cms.task.mapper.TaskMonitorMapper;
import com.kal.cms.task.mapper.SgmlProcessMapper;
import com.kal.cms.task.service.ProcessMonitorService;
import com.kal.cms.task.sgml.FileDirManager;
import com.kal.cms.task.sgml.SgmlProcess;
import com.kal.cms.task.vo.TaskDetailInfo;
import com.kal.cms.task.vo.TaskInfo;

@Service("processMonitorService")
public class ProcessMonitorServiceImpl extends EgovAbstractServiceImpl implements ProcessMonitorService {

	private static String _osName = System.getProperty("os.name").toLowerCase();
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessMonitorServiceImpl.class);

	@Resource(name = "sgmlProcessMapper")
	private SgmlProcessMapper sgmlProcessDao;
	
	@Resource(name = "taskMonitorMapper")
	private TaskMonitorMapper taskMonitorDao;

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	@Override
	public List<TaskInfo> findTaskList(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return taskMonitorDao.getTaskList(pMap);
	}

	@Override
	public List<TaskDetailInfo> findTaskDetailLogList(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return taskMonitorDao.getTaskDetailLog(pMap);
	}
	
	@Override
	public int getTaskTotalCount(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		return taskMonitorDao.getTaskTotalCount(pMap);
	}

	@Override
	public void doSgmlFlow(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		boolean isWindows = false;
		boolean isAix = false;
		if(_osName.indexOf("win")>-1)isWindows = true;
		else if(_osName.indexOf("aix")>-1)isAix = true;
		SgmlProcess sgmlProcess = new SgmlProcess();
		String basePath = pMap.get("basePath").toString();
		String cgmDTDPath = basePath + pMap.get("cgmDTDPath");
		String sgmFilePath = basePath + pMap.get("sgmFilePath");
		String srcCgmPath = basePath + pMap.get("srcCgmPath");
		String model = pMap.get("model").toString();
		String manualType = pMap.get("manualType").toString();
		String userId = pMap.get("userId").toString();
		FileDirManager fdm = new FileDirManager();
		String destFile = fdm.getTempXmlFilePath(egovMessageSource.getMessage("nas.file.path"), model, manualType);
		String destPngPath = fdm.getTempDestPngPath(egovMessageSource.getMessage("nas.file.path"), model, manualType);
		String destBasePath = fdm.getDestBasePath(egovMessageSource.getMessage("nas.file.path"), model, manualType);
		String contentType = fdm.getContentTypeOpRt(manualType, model);
		String revNum = ""+getRevSeq(manualType, model, contentType);
		String fullXmlRootElemName = fdm.getFullXmlRootElemName(manualType);
		String fullXmlFilePrefix = fdm.getFullXmlFilePrefix(sgmFilePath);
		String burstXmlElemName = fdm.getBurstXmlElemName(manualType, model);
		String burstXmlPrefix = fdm.getBurstXmlPrefix(manualType, model);
		String burstGraphicXmlElemName = fdm.getBurstGraphicXmlElemName(manualType, model);
		String burstGraphicXmlPrefix = fdm.getBurstGraphicXmlPrefix(manualType, model);
		
		// task validate.
		validateTask(pMap);
		
		// regist task (Queue)
		long taskId = registTask(pMap);
		
		HashMap<String, Object> sgmlParamMap = new HashMap<String, Object>();
		sgmlParamMap.put("basePath", basePath);
		sgmlParamMap.put("destFile", destFile);
		sgmlParamMap.put("cgmDTDPath", cgmDTDPath);
		sgmlParamMap.put("sgmFilePath", sgmFilePath);
		sgmlParamMap.put("srcCgmPath", srcCgmPath);
		sgmlParamMap.put("destPngPath", destPngPath);
		sgmlParamMap.put("destBasePath", destBasePath);
		sgmlParamMap.put("revNum", revNum);
		sgmlParamMap.put("model", model);
		sgmlParamMap.put("manualType", manualType);
		sgmlParamMap.put("userId", userId);
		sgmlParamMap.put("fullXmlRootElemName", fullXmlRootElemName);
		sgmlParamMap.put("fullXmlFilePrefix", fullXmlFilePrefix);
		sgmlParamMap.put("burstXmlElemName", burstXmlElemName);
		sgmlParamMap.put("burstXmlPrefix", burstXmlPrefix);
		sgmlParamMap.put("burstGraphicXmlElemName", burstGraphicXmlElemName);
		sgmlParamMap.put("burstGraphicXmlPrefix", burstGraphicXmlPrefix);
		sgmlParamMap.put("taskId", ""+taskId);
		if(isWindows){
			String batchFilePath = egovMessageSource.getMessage("window.batch.sx.run");
			String cgmBatchFilePath = egovMessageSource.getMessage("window.batch.cgm.run");
			String sxBinPath = egovMessageSource.getMessage("window.sx.bin.path");
			sgmlParamMap.put("batchFilePath", batchFilePath);
			sgmlParamMap.put("sxBinPath", sxBinPath);
			sgmlParamMap.put("cgmBatchFilePath", cgmBatchFilePath);
			sgmlProcess.doProcess(sgmlParamMap, sgmlProcessDao);
		}else if(isAix){
			String batchFilePath = egovMessageSource.getMessage("unix.shell.sx.run");
			String cgmBatchFilePath = egovMessageSource.getMessage("unix.shell.cgm.run");
			String sxBinPath = egovMessageSource.getMessage("unix.sx.bin.path");
			sgmlParamMap.put("batchFilePath", batchFilePath);
			sgmlParamMap.put("cgmBatchFilePath", cgmBatchFilePath);
			sgmlParamMap.put("sxBinPath", sxBinPath);
			sgmlProcess.doProcess(sgmlParamMap, sgmlProcessDao);
		}
	}

	@Override
	public void doValidation(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		String basePath = pMap.get("basePath").toString();
		File basePathFile = new File(basePath);
		if(!basePathFile.exists())throw new Exception("In Put 폴더 경로가 유효하지 않습니다.");
		String sgmFilePath = basePath + pMap.get("sgmFilePath");
		File sgmFilePathFile = new File(sgmFilePath);
		if(!sgmFilePathFile.exists())throw new Exception("SGML 파일 경로가 유효하지 않습니다.");
		String cgmDTDPath = basePath + pMap.get("cgmDTDPath");
		File cgmDTDPathFile = new File(cgmDTDPath);
		if(!cgmDTDPathFile.exists())throw new Exception("Entity DTD 파일 경로가 유효하지 않습니다.");
		String srcCgmPath = basePath + pMap.get("srcCgmPath");
		File srcCgmPathFile = new File(srcCgmPath);
		if(!srcCgmPathFile.exists())throw new Exception("Image 폴더 경로가 유효하지 않습니다.");
		
		// Manual Type과 Model validation.
		// 모델명이 input folder 경로명에 있는지 확인.
		String model = pMap.get("model").toString();
		if(basePath.indexOf(model)==-1)throw new Exception("In Put 폴더 경로가 모델명과 매칭되지 않습니다.");
		
		// 매뉴얼 타입이 대상 sgml file 명과 entity dtd 파일 경로에 포함되는지 확인.
		String manualType = pMap.get("manualType").toString();
		if(sgmFilePath.indexOf(manualType)==-1)throw new Exception("SGML 파일 경로가 Manual Type과 매칭되지 않습니다.");
		if(cgmDTDPath.indexOf(manualType)==-1)throw new Exception("Entity DTD 파일 경로가 Manual Type과 매칭되지 않습니다.");
		
	}

	@Override
	public long registTask(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub
		long taskId = 0L;
		// status code
		//7001	Complete
		//7002	In Progress
		//7003	Fail
		//7004	Queued
		//7005	Aborted

		String description = "SGML Flow";
		String status = TaskConstants.TASK_STATUS_QUEUED;
		///////////////
		// 5000	Task 구분	
		// 5001	SGML
		// 5002	Batch-Download
		// 5003	Batch-Upload
		
		pMap.put("taskType", "5001");
		pMap.put("contentType", "RT");
		pMap.put("description", description);
		pMap.put("status", status);
		
		pMap.put("releaseYn", "N");
		
		taskId = sgmlProcessDao.addTask(pMap);
		
		return taskId;
	}

	@Override
	public void validateTask(HashMap<String, Object> pMap) throws Exception {
		// TODO Auto-generated method stub

		pMap.put("contentType", "RT");
		///////////////
		// 5000	Task 구분	
		// 5001	SGML
		// 5002	Batch
		// 5003	Masterjobcard
		// 5004	PDF
		pMap.put("taskType", "5001");
		// taskType: SGML (5001)
		// 같은 종류의 문서에 대하여 작업 중인 것이 있는지 확인.
		List<TaskInfo> taskInfoList = sgmlProcessDao.getTaskList(pMap);
		if(taskInfoList!=null && taskInfoList.size()>0){
			for(int i=0;i<taskInfoList.size();i++){
				String status = taskInfoList.get(i).getStatus();
				// status code
				//7001	Complete
				//7002	In Progress
				//7003	Fail
				//7004	Queued
				//7005	Aborted
				if("Queue".equalsIgnoreCase(status) || "Progress".equalsIgnoreCase(status) 
						|| "7002".equals(status) || TaskConstants.TASK_STATUS_QUEUED.equals(status)){
					throw new Exception("[SGML] 같은 종류의 문서에 대하여 현재 작업 중인 프로세스가 있습니다.");
				}
			}
		}
	}

	@Override
	public boolean checkTaskStatus(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		return testTaskStatus(pMap);
	}

	@Override
	public boolean taskDelete(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		boolean chkResult = testTaskStatus(pMap);
		
		if(chkResult) {
			try {
				pMap.put("status", TaskConstants.TASK_STATUS_DELETED);
				taskMonitorDao.updateMultiTaskStatus(pMap);
			} catch (Exception e) {
				e.printStackTrace();
				pMap.put("errorMsg", "서버오류입니다.");
				return false;
			}
			return true;
		} else {
			return chkResult;
		}
	}

	@Override
	public boolean taskAbort(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		boolean chkResult = testTaskStatus(pMap);
		
		if(chkResult) {
			try {
				pMap.put("status", TaskConstants.TASK_STATUS_ABORTED);
				taskMonitorDao.updateMultiTaskStatus(pMap);
			} catch (Exception e) {
				e.printStackTrace();
				pMap.put("errorMsg", "서버오류입니다.");
				return false;
			}
			return true;
		} else {
			return chkResult;
		}
	}

	@Override
	public boolean taskResume(HashMap<String, Object> pMap) {
		// TODO Auto-generated method stub
		boolean result = testTaskStatus(pMap);
		
		if(result) {
			try {
				
				List<TaskInfo> taskList = taskMonitorDao.getSelectedTaskList(pMap);
				
				for (TaskInfo taskInfo : taskList){				
					if(TaskConstants.TASK_TPYE_Batch_Upload.equalsIgnoreCase(taskInfo.getTaskType())){
						resumeProcessUpload(pMap, taskInfo);
					} else if(TaskConstants.TASK_TPYE_Batch_Upload.equalsIgnoreCase(taskInfo.getTaskType())){
						resumeProcessDownload(pMap, taskInfo);
					} else if(TaskConstants.TASK_TPYE_SGML.equalsIgnoreCase(taskInfo.getTaskType())){
						resumeSGML(pMap, taskInfo);
					} else {
						throw new MpFrameException("서비스 준비중입니다.");
					}
				}

			} catch (MpFrameException e) {
				e.printStackTrace();
				pMap.put("errorMsg", e.getMessage());
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				pMap.put("errorMsg", "서버오류입니다.");
				return false;
			}
			return true;
		} else {
			return result;
		}
	}
	
	
	private int getRevSeq(String manualType, String model, String contentType){
		String fixedManualType = "";
		if("TR".equalsIgnoreCase(manualType))fixedManualType = "AMM";
		else fixedManualType = manualType;
		HashMap<String , Object> pMap = new HashMap<String, Object>();
		pMap.put("model", model);
		pMap.put("manualType", fixedManualType);
		pMap.put("contentType", contentType);
		int currentRevNum = sgmlProcessDao.getLastRevSeq(pMap);
		return currentRevNum;
	}
	
	
	private boolean resumeProcessUpload(HashMap<String, Object> pMap, TaskInfo taskInfo){
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		map.put("userId", pMap.get("userId"));
		
		map.put("masterWOCntsTskId",taskInfo.getTaskId());
		
		map.put("maintDocModelCode",taskInfo.getModel());
		map.put("WOCntsDocSubtypCd",taskInfo.getManualType());
		map.put("masterWOCntsTypCd",taskInfo.getContentType());
		
		map.put("masterWOCntsTskTypCd", TaskConstants.TASK_TPYE_Batch_Upload); 				// task : 상태 업로드
		map.put("description", "File Upload batch"); 			// 업로드 배치
		map.put("masterWOCntsTskSttsCd", TaskConstants.TASK_STATUS_QUEUED); 				// task status : Queued			
		
		File file = new File(taskInfo.getUrlAddress());
		if(file.canRead()){
			new ProcessUploadFile(file, map).start();
		} else {
			throw new MpFrameException("서비스 재실행에 필요한 파일을 찾지 못했습니다.");
		}
		
		return true;
	}
	
	private boolean resumeProcessDownload(HashMap<String, Object> pMap, TaskInfo taskInfo){
		HashMap<String,Object> map = new HashMap<String,Object>();
		
		map.put("userId", pMap.get("userId"));
		
		map.put("masterWOCntsTskId",taskInfo.getTaskId());
		
		map.put("maintDocModelCode",taskInfo.getModel());
		map.put("WOCntsDocSubtypCd",taskInfo.getManualType());
		map.put("masterWOCntsTypCd",taskInfo.getContentType());
		
		if(taskInfo.getUserData() == null || "".equals(taskInfo.getUserData().trim()))
		{
			throw new MpFrameException("서비스 재실행에 필요한 정보를 찾지 못했습니다.");
		}
		
		JSONObject jsonObject = new JSONObject(taskInfo.getUserData());
		
		map.put("checkAll",jsonObject.get("checkAll"));
		if("Y".equalsIgnoreCase((String)jsonObject.get("checkAll"))) {
			map.put("searchValue",jsonObject.get("searchValue"));              
			map.put("searchMaintDocModelCode",jsonObject.get("searchMaintDocModelCode"));   
			map.put("searchWOCntsDocSubtypCd",jsonObject.get("searchWOCntsDocSubtypCd"));
			map.put("searchMasterWOCntsTypCd",jsonObject.get("searchMasterWOCntsTypCd"));   
			map.put("searchReleaseYn",jsonObject.get("searchReleaseYn"));           
			map.put("searchReviseType",jsonObject.get("searchReviseType"));          
			map.put("searchRevSeq",jsonObject.get("searchRevSeq"));              
			map.put("searchStartDt",jsonObject.get("searchStartDt"));             
			map.put("searchEndDt",jsonObject.get("searchEndDt"));               
			map.put("searchFileName",jsonObject.get("searchFileName"));            
			map.put("searchFileNameGb",jsonObject.get("searchFileNameGb"));      

		} else {
			JSONArray jsonArray = jsonObject.getJSONArray("list_contentCheck");
			String[] contentIds = new String[jsonArray.length()];
			for (int i=0 ; i < jsonArray.length() ; i ++) {
				JSONObject jsonObj = (JSONObject)jsonArray.get(i);
				contentIds[i] = jsonObj.getString("contentCheck");
			}
			
			map.put("contentIds", contentIds);
		}

 		map.put("masterWOCntsTskTypCd", TaskConstants.TASK_TPYE_Batch_Upload); 				// task : 상태 업로드
 		map.put("description", "File Download batch"); 			// 업로드 배치
 		map.put("masterWOCntsTskSttsCd", TaskConstants.TASK_STATUS_QUEUED); 				// task status : Queued		
		
		new ProcessCompressFile(map).start();
		
		return true;
	}

	private boolean resumeSGML(HashMap<String, Object> pMap, TaskInfo taskInfo){
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("userId", (String)pMap.get("userId"));
		
		if(taskInfo.getUserData() == null || "".equals(taskInfo.getUserData().trim()))
			throw new MpFrameException("서비스 재실행에 필요한 정보를 찾지 못했습니다.");
		
		JSONObject jsonObject = new JSONObject(taskInfo.getUserData());

		paramMap.put("basePath", (String)jsonObject.get("inputFolder"));
		paramMap.put("sgmFilePath", (String)jsonObject.get("sgmlFile"));
		paramMap.put("cgmDTDPath", (String)jsonObject.get("entityDtdFile"));
		paramMap.put("manualDtdFile", (String)jsonObject.get("manualDtdFile"));
		paramMap.put("srcCgmPath", (String)jsonObject.get("imageFile"));
		paramMap.put("model", (String)jsonObject.get("model"));
		paramMap.put("manualType", (String)jsonObject.get("manualType"));
		
		boolean isValidate = true;
		try{
			doValidation(paramMap);
		}catch(Exception e){
			e.printStackTrace();
			throw new MpFrameException(e.getMessage());
		}
		
		if(isValidate){
			try{
				doSgmlFlow(paramMap);
			} catch(Exception e) {
				e.printStackTrace();
				////////
				// SGML flow 중간에 발생한 정의된 예외는 메세지에 
				// [SGML] prefix를 붙인다. 차후 사용자 정의 예외 클래스 추가가 필요하면 
				// 수정할 것.
				String resultMessage = "";
				String _message = e.getMessage();
				if(_message!=null && _message.startsWith("[SGML]")){
					resultMessage = _message;
				}else
					resultMessage = "처리 중 오류가 발생하였습니다.";
				
				throw new MpFrameException(resultMessage);
			}
		}		
		
		return true;
	}
	
	
	private boolean testTaskStatus(HashMap<String, Object> pMap) {
		boolean result = true;
		String  act = (String)pMap.get("act");
		String[] taskIds = (String[])pMap.get("taskIds");
		
		/*
		 resume : fail(7003)
		 abort : In Progress(7002), Fail(7003), Queued(7004)
		 delete : Complete(7001), Abort(7005)
		 */
		String validStatus = "";
		String errorMsg = "";
		if("R".equalsIgnoreCase(act)){
			validStatus = TaskConstants.TASK_STATUS_FAIL;
			errorMsg = "재실행 할 수 없는 Task 입니다.";
		} else if("A".equalsIgnoreCase(act)) {
			validStatus = TaskConstants.TASK_STATUS_INPROGRESS
					.concat(",").concat(TaskConstants.TASK_STATUS_FAIL)
					.concat(",").concat(TaskConstants.TASK_STATUS_QUEUED);
			errorMsg = "중단 할 수 없는 Task 입니다.";
		} else if("D".equalsIgnoreCase(act)) {
			validStatus = TaskConstants.TASK_STATUS_COMPLETE
					.concat(",").concat(TaskConstants.TASK_STATUS_ABORTED)
					.concat(",").concat(TaskConstants.TASK_STATUS_REPORT);
			errorMsg = "삭제 할 수 없는 Task 입니다.";
		}		
		
		for(String taskId : taskIds){
			pMap.put("taskId", taskId);
			String status = taskMonitorDao.getTaskStatus(pMap);
			System.out.println("taskId status == "+status);
			
			if(validStatus.indexOf(status) < 0){
				result = false;
				pMap.put("errorMsg", errorMsg);
				break;
			}
		}
		
		return result;
	}


}
