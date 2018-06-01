package com.kal.cms.task.sgml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import org.drools.spi.ProcessContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easycompany.cmm.tag.EgovMessageSource;
import com.kal.cms.task.mapper.SgmlProcessMapper;
import com.kal.cms.task.mapper.TaskMonitorMapper;
import com.kal.cms.task.service.impl.ProcessMonitorServiceImpl;

/**
 * CGM 이미지 포맷 변환 프로세스 호출 클래스.
 */
public class CgmConvertInvoker {
	private static final Logger LOGGER = LoggerFactory.getLogger(CgmConvertInvoker.class);

	@Resource(name = "SgmlProcessMapper")
	private SgmlProcessMapper sgmlProcessDao;

	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	private static String fileSeparator = System.getProperty("file.separator")==null?"/":System.getProperty("file.separator");

	
	private static final CgmConvertInvoker INSTANCE = new CgmConvertInvoker();
	
	public static CgmConvertInvoker getInstance(){
		return INSTANCE;
	}
	
	
	/**
	 * 소스 CGM 폴더 하위의 이미지 리소스를 대상 폴더로 모두 PNG 포맷 변환하는 프로세스를 호출한다.
	 * @param srcFolder
	 * @param destFolder
	 */
	public void invoke(String srcFolder, String retryCgmPath, String destFolder, String cgmBatchFilePath, Object _dao, Object kcontext, int threadCnt){
		
		String detailId = "";
		if(_dao!=null){
			sgmlProcessDao = (SgmlProcessMapper)_dao;
			detailId = ""+((ProcessContext)kcontext).getVariable("detailId");
		}
		
		String procMsg = "[CgmConvertInvoker] srcFolder:"+srcFolder+", retryCgmPath:"+retryCgmPath+", destFolder:"+destFolder
			+", cgmBatchFilePath:"+cgmBatchFilePath+", threadCnt:"+threadCnt+"\\r\\n";
		
		File _destFolder = new File(destFolder);
		File _srcFolder = null;
		if(retryCgmPath!=null)
			_srcFolder = new File(retryCgmPath);
		else
			_srcFolder = new File(srcFolder);
		
		try{
			if(!_destFolder.exists()){
				try {
					FileUtils.forceMkdir(_destFolder);
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
			
			if(_srcFolder.isDirectory()){
				File[] cgmFileArray = _srcFolder.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(".cgm");
					}
				});
				System.out.println("[CgmConvertInvoker] CGM File total count: "+(cgmFileArray==null?0:cgmFileArray.length));
				//procMsg += "[CgmConvertInvoker] CGM File total count: "+(cgmFileArray==null?0:cgmFileArray.length) + "\\r\\n";
				if(sgmlProcessDao!=null){
					HashMap<String, Object> pMap = new HashMap<String, Object>();
					pMap.put("detailId", detailId);									
					pMap.put("errorMessageContents", "[CgmConvertInvoker] CGM File total count: "+(cgmFileArray==null?0:cgmFileArray.length));
					sgmlProcessDao.addTaskDetailLog(pMap);
				}
				/////////////
				// 100개가 넘을 경우 쓰레드 갯수로 작업 대상 목록을 나누어 프로세스 call.
				// 아니면 하나의 프로세스만 호출.
				int[] fileCntArr = getInitialCgmFileCount(cgmFileArray, threadCnt);
				if(cgmFileArray!=null){
					long startTime = System.currentTimeMillis();
					ExecutorService threadPool = Executors.newFixedThreadPool(threadCnt);
					CompletionService<CgmToPngResult> completionService = new ExecutorCompletionService<CgmToPngResult>(threadPool);
					try{
						if(threadCnt>1 && cgmFileArray.length>100){
							for(int i=0;i<threadCnt;i++){
								if(retryCgmPath!=null)
									completionService.submit(new ConvertTask(retryCgmPath, destFolder, "cgmProc"+i, cgmBatchFilePath, i*fileCntArr[0], fileCntArr[i]));
								else
									completionService.submit(new ConvertTask(srcFolder, destFolder, "cgmProc"+i, cgmBatchFilePath, i*fileCntArr[0], fileCntArr[i]));
								if(sgmlProcessDao!=null){
									HashMap<String, Object> pMap = new HashMap<String, Object>();
									pMap.put("detailId", detailId);									
									pMap.put("errorMessageContents", "[Multi Thread Call] "+"Invoke Process cgmProc"+i+" from "+(i*fileCntArr[0]+1)+"th to "+(i*fileCntArr[0]+fileCntArr[i])+"th.");
									sgmlProcessDao.addTaskDetailLog(pMap);
								}
							}
						}else{
							if(retryCgmPath!=null)
								completionService.submit(new ConvertTask(retryCgmPath, destFolder, "cgmProc0", cgmBatchFilePath, 0, fileCntArr[0]));
							else
								completionService.submit(new ConvertTask(srcFolder, destFolder, "cgmProc0", cgmBatchFilePath, 0, fileCntArr[0]));
							if(sgmlProcessDao!=null){
								HashMap<String, Object> pMap = new HashMap<String, Object>();
								pMap.put("detailId", detailId);									
								pMap.put("errorMessageContents", "[Multi Thread Call] "+"Invoke Process cgmProc0 from 1th to "+(fileCntArr[0])+"th.");
								sgmlProcessDao.addTaskDetailLog(pMap);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
						throw e;
					}finally{
						
					}
					
					try {
						if(threadCnt>1 && cgmFileArray.length>100){
							for(int i=0;i<threadCnt;i++){
								CgmToPngResult cgmToPngResult = completionService.take().get();
								System.out.println("[Multi Thread Result] "+cgmToPngResult.getResultMessage());
								//procMsg += "[Multi Thread Result] "+cgmToPngResult.getResultMessage() + "\\r\\n";
								if(sgmlProcessDao!=null){
									HashMap<String, Object> pMap = new HashMap<String, Object>();
									pMap.put("detailId", detailId);									
									pMap.put("errorMessageContents", "[Multi Thread Result] "+cgmToPngResult.getResultMessage());
									sgmlProcessDao.addTaskDetailLog(pMap);
								}
							}
							System.out.println("Elapsed Time: "+(System.currentTimeMillis()-startTime));
						}else{
							CgmToPngResult cgmToPngResult = completionService.take().get();
							System.out.println("[Multi Thread Result] "+cgmToPngResult.getResultMessage());
							//procMsg += "[Multi Thread Result] "+cgmToPngResult.getResultMessage() + "\\r\\n";
							if(sgmlProcessDao!=null){
								HashMap<String, Object> pMap = new HashMap<String, Object>();
								pMap.put("detailId", detailId);									
								pMap.put("errorMessageContents", "[Multi Thread Result] "+cgmToPngResult.getResultMessage());
								sgmlProcessDao.addTaskDetailLog(pMap);
							}
							System.out.println("Elapsed Time: "+(System.currentTimeMillis()-startTime));
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw e;
					}
					threadPool.shutdown();
				}
				
			}else{
				throw new Exception("CGM Src Folder is not valid directory.");
			}
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Converting CGM to PNG Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * 배치 프로세스를 쓰레드를 통하여 call한다.
	 * @param srcFolder
	 * @param destFolder
	 * @param procName
	 * @param cgmBatchFilePath
	 * @param startIdx
	 * @param destCgmCnt
	 * @throws Exception
	 */
	public void callCgmConvertProcess(String srcFolder, String destFolder, String procName, String cgmBatchFilePath, int startIdx, int destCgmCnt) throws Exception{
		String _osName = System.getProperty("os.name").toLowerCase();
		boolean isWindows = false;
		boolean isAix = false;
		if(_osName.indexOf("win")>-1)isWindows = true;
		else if(_osName.indexOf("aix")>-1)isAix = true;
		
		String batchFilePath = cgmBatchFilePath;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[]{batchFilePath, srcFolder, destFolder, procName, ""+startIdx, ""+destCgmCnt});
			// 표준 출력
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			char[] buff = new char[1024];
			int len = reader.read(buff);
			while(len>=0){
				System.out.print(new String(buff, 0, len));
				len = reader.read(buff);
			}
			int result = -1;
			if(isAix)
				result = process.waitFor();
			else
				result = process.exitValue();
			System.out.println("return value: "+result);
			if(result==0){
				System.out.println("Job of "+procName+" is finished.");
			}else new Exception("There is no response from sx command.");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 단위 작업 CGM 파일 갯수를 리턴.
	 * @param cgmFileArray
	 * @param threadCnt
	 * @return
	 */
	private int[] getInitialCgmFileCount(File[] cgmFileArray, int threadCnt){
		int[] fileCntArr = new int[]{};
		if(cgmFileArray!=null){
			if(cgmFileArray.length<=100){
				// 100개 이하일 경우는 하나의 프로세스로 처리.
				fileCntArr = ArrayUtils.add(fileCntArr, cgmFileArray.length);
			}else{
				int fileCnt = cgmFileArray.length / threadCnt;
				for(int i=0;i<threadCnt;i++){
					if(i==threadCnt-1){
						fileCntArr = ArrayUtils.add(fileCntArr, cgmFileArray.length-(threadCnt-1)*fileCnt);
					}else{
						fileCntArr = ArrayUtils.add(fileCntArr, fileCnt);
					}
				}
			}
		}
		return fileCntArr;
	}
	
	
	class ConvertTask implements Callable<CgmToPngResult>{
		
		private String procName = "";
		private int startIdx = 0;
		private int destCgmCnt = 0;
		private String srcFolder = "";
		private String destFolder = "";
		private String cgmBatchFilePath = "";

		public ConvertTask(String srcFolder, String destFolder, String procName, String cgmBatchFilePath, int startIdx, int destCgmCnt){
			this.procName = procName;
			this.startIdx = startIdx;
			this.destCgmCnt = destCgmCnt;
			this.srcFolder = srcFolder;
			this.destFolder = destFolder;
			this.cgmBatchFilePath = cgmBatchFilePath;
		}
		
		@Override
		public CgmToPngResult call() throws Exception {
			CgmToPngResult cgmToPngResult = new CgmToPngResult();
			callCgmConvertProcess(srcFolder, destFolder, procName, cgmBatchFilePath, startIdx, destCgmCnt);
			cgmToPngResult.setResultMessage("Converting Process "+procName+" from "+(startIdx+1)+"th to "+(startIdx+destCgmCnt)+"th is completed.");
			return cgmToPngResult;
		}
		
	}
	
	final class CgmToPngResult implements Serializable{

		private static final long serialVersionUID = -5158379650779260258L;
		
		private String resultMessage = "";

		public String getResultMessage() {
			return resultMessage;
		}

		public void setResultMessage(String resultMessage) {
			this.resultMessage = resultMessage;
		}
		
	}
	
	public static void main(String[] args){
		
		CgmConvertInvoker tic = new CgmConvertInvoker();
		tic.invoke("c:/TEST_CGM/", null, "c:/TEST_CGM/converted/png", "c:/cgm_run.bat", null, null, 5);
		//tic.convertToPng("c:/allelm01.cgm", "c:/test.png");
		//tic.convertToPng("c:/l_mm_052100_6_aam0_01_00.cgm", "c:/l_mm_052100_6_aam0_01_00.png");
		//tic.convertAll("D:\\data\\kal\\KAL_sample\\B777\\SGML\\CGM", "D:\\data\\kal\\KAL_sample\\B777\\converted\\png", null);
		//tic.convertAll("c:TEST_CGM/", "c:/TEST_CGM/converted/png", null);
		System.out.println("Done.");
	}
}
