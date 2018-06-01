package com.kal.cms.task.sgml;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

import com.kal.cms.task.mapper.SgmlProcessMapper;;

/**
 * CGM 및 변환된 PNG 이미지에 대한 이동 및 저장 서브 프로세스.
 */
public class SaveImageSubProcess {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final SaveImageSubProcess INSTANCE = new SaveImageSubProcess();
	private SgmlProcessMapper sgmlProcessDao = null;
	
	public static SaveImageSubProcess getInstance(){
		return INSTANCE;
	}
	
	/**
	 * 프로세스 수행.
	 * @param destBasePath
	 * @param srcCgmPath
	 * @param destPngPath
	 * @param revNum
	 * @param model
	 * @param manualType
	 * @param userId
	 * @param _dao
	 */
	public void doSaveSubProcess(String destBasePath, String srcCgmPath, String destPngPath, String currRevNum, String currRevDate, String model, String manualType, String userId, 
			Object _dao, Object kcontext){
		///////
		// 이미지 정보 추출 후 저장은 앞서 burst XML 정보 저장 시 수행하고 
		// 여기에서는 이미지를 대상 폴더로 옮기는 작업만 한다. 여기에서 DB 작업을 
		// 다시 수행할 경우 맵핑되는 xml 정보를 조회하여 파일별 IO를 수행해야 하므로 
		// 더 느려지기 때문이다.
		if(_dao!=null)
			sgmlProcessDao = (SgmlProcessMapper)_dao;
		File srcCgmFolder = new File(srcCgmPath);
		File destPngFolder = new File(destPngPath);
		FileDirManager fdm = new FileDirManager();
		
		try{
			String procMsg = "[SaveImageSubProcess] destBasePath:"+destBasePath+", srcCgmPath:"+srcCgmPath+", destPngPath:"+destPngPath
				+", currRevNum:"+currRevNum+", currRevDate:"+currRevDate+", model:"+model+", manualType:"+manualType+", userId:"+userId+"\\r\\n";
			String destImagePath = fdm.getImageDestPath(destBasePath, model, manualType, currRevNum);
			File destImageFolder = new File(destImagePath);
			copyCgmImage(srcCgmFolder, destImageFolder);
			System.out.println("[SaveImageSubProcess] copy cgm from "+srcCgmFolder.getAbsolutePath()+" to "+destImageFolder.getAbsolutePath()+" is completed.");
			procMsg += "[SaveImageSubProcess] copy cgm from "+srcCgmFolder.getAbsolutePath()+" to "+destImageFolder.getAbsolutePath()+" is completed." + "\\r\\n";
			copyPngImage(destPngFolder, destImageFolder);
			System.out.println("[SaveImageSubProcess] copy png from "+destPngFolder.getAbsolutePath()+" to "+destImageFolder.getAbsolutePath()+" is completed.");
			procMsg += "[SaveImageSubProcess] copy png from "+destPngFolder.getAbsolutePath()+" to "+destImageFolder.getAbsolutePath()+" is completed." + "\\r\\n";
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Saving CGM, PNG Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * CGM 파일들을 대상 디렉토리로 옮긴다.
	 * @param srcCgmFolder
	 * @param destPath
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void copyCgmImage(File srcCgmFolder, File destPath) throws Exception{
		try{
			FileUtils.copyDirectory(srcCgmFolder, destPath);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * PNG 파일들을 대상 디렉토리로 옮긴다.
	 * @param burstedXmlFolder
	 * @param destPath
	 * @throws Exception 
	 * @throws IOException 
	 */
	public void copyPngImage(File destPngFolder, File destPath) throws Exception{
		try{
			FileUtils.copyDirectory(destPngFolder, destPath);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

}
