package com.kal.cms.task.sgml;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

/**
 * CGM to PNG 변환 후 검증 클래스.
 */
public class CgmValidator {
	
	private static String fileSeparator = System.getProperty("file.separator")==null?"/":System.getProperty("file.separator");
	
	private static final CgmValidator INSTANCE = new CgmValidator();
	
	private int currentValidationCnt = 0;
	
	public static CgmValidator getInstance(){
		return INSTANCE;
	}
	
	/**
	 * CGM to PNG 변환 후 검증을 수행한다.
	 * @param srcFolder
	 * @param destFolder
	 * @param kcontext
	 */
	public void validateImages(String srcFolder, String destFolder, int retryCnt, Object kcontext){
		// 3번까지 실행하여 그 이상 validation에 실패하면 fail 처리.
		try{
			
			// validation count를 현재 프로세스 세션 변수에 저장해 두고 
			// 그 값을 가져온다.
			try{
				Object _currentValidationCntObj = ((ProcessContext)kcontext).getVariable("_currentValidationCnt");
				if(_currentValidationCntObj!=null){
					this.currentValidationCnt = (Integer)_currentValidationCntObj;
				}else{
					this.currentValidationCnt = 0;
				}
			}catch(Exception e){
				this.currentValidationCnt = 0;
			}
			String procMsg = "[CgmValidator] srcFolder:"+srcFolder+", destFolder:"+destFolder+", retryCnt:"+retryCnt+"\\r\\n";
			if(++currentValidationCnt > retryCnt)throw new Exception("Validating Converted PNG Files is failed. Retry Count: "+currentValidationCnt);
			else{
				System.out.println("[CgmValidator] Start validating cgm to png files. Current retry count: "+currentValidationCnt);
				procMsg += "[CgmValidator] Start validating cgm to png files. Current retry count: "+currentValidationCnt+"\\r\\n";
			}
			// CGM 파일과 변환된 PNG 파일 목록을 비교.
			File[] missingFiles = compareImagesAndGetMissing(srcFolder, destFolder);
			procMsg += "[CgmValidator] Missing CGM Files count: "+(missingFiles==null?0:missingFiles.length)+"\\r\\n";
			if(missingFiles!=null && missingFiles.length>0){
				// 누락된 파일을 retry 폴더로 복사
				File retryDir = getRetryFolder(destFolder);
				File[] retryFiles = copyMissingFiles(missingFiles, retryDir);
				// 누락된 파일이 있다면 CGM 변환 클래스에 그 목록을 전달.
				try{
					((ProcessContext)kcontext).setVariable("retryFiles", retryFiles);
					((ProcessContext)kcontext).setVariable("retryCgmPath", retryDir.getAbsolutePath());
				}catch(Exception e){}
			}else{
				((ProcessContext)kcontext).setVariable("retryFiles", null);
				((ProcessContext)kcontext).setVariable("retryCgmPath", null);
			}
			
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("_currentValidationCnt", currentValidationCnt);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Validating CGM to PNG Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * CGM 목록과 PNG 목록을 비교하여 누락된 CGM 목록을 리턴.
	 * @param srcFolder
	 * @param destFolder
	 * @return (File[])
	 * @throws Exception
	 */
	private File[] compareImagesAndGetMissing(String srcFolder, String destFolder) throws Exception{
		File _destFolder = new File(destFolder);
		File _srcFolder = new File(srcFolder);
		File[] missingFiles = new File[]{};
		if(!_destFolder.isDirectory() || !_srcFolder.isDirectory())
			throw new Exception("Invalid folder path. dest folder: "+_destFolder.getAbsolutePath()+", src folder: "+_srcFolder.getAbsolutePath());
		
		File[] cgmFileArray = _srcFolder.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".cgm");
			}
		});
		File[] pngFileArray = _destFolder.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".png");
			}
		});
		if(cgmFileArray!=null && cgmFileArray.length>0){
			
			if(pngFileArray!=null && pngFileArray.length>0){
				/////////
				// 결과 png 파일 목록과 같은 이름의 cgm 파일 목록을 지우고 
				// 남아있는(변환이 누락된) cgm 파일 목록을 추출한다.
				for(int i=0;i<pngFileArray.length;i++){
					File pngFile = pngFileArray[i];
					if(pngFile!=null){
						String pngBaseName = FilenameUtils.getBaseName(pngFile.getName());
						File findedCgmFile = getContainedFileByBaseName(cgmFileArray, pngBaseName);
						if(findedCgmFile!=null){
							cgmFileArray = (File[])ArrayUtils.removeElement(cgmFileArray, findedCgmFile);
						}
					}
				}
				missingFiles = cgmFileArray;
				System.out.println("Missing CGM Files count: "+(missingFiles==null?0:missingFiles.length));
			}else{
				throw new Exception("PNG Files count is not valid. png files count: 0, cgm files count: "+cgmFileArray.length);
			}
		}
		return missingFiles;
	}
	
	/**
	 * 파일 baseName을 기준으로 주어진 파일 배열에서 같은 이름의 파일을 리턴.
	 * @param fileArray
	 * @param baseFileName
	 * @return (boolean)
	 * @throws Exception
	 */
	private File getContainedFileByBaseName(File[] fileArray, String baseFileName) throws Exception{
		File rt = null;
		if(fileArray!=null)
		for(int j=0;j<fileArray.length;j++){
			File cgmFile = fileArray[j];
			if(cgmFile!=null){
				String cgmBaseName = FilenameUtils.getBaseName(cgmFile.getName());
				if(cgmBaseName.equals(baseFileName)){
					rt = cgmFile;
					break;
				}
			}
		}
		return rt;
	}
	
	/**
	 * 누락된 파일 목록을 retry 폴더로 복사함.
	 * @param missingFiles
	 * @param destRetryFolder
	 * @return (File[])
	 * @throws Exception
	 */
	private File[] copyMissingFiles(File[] missingFiles, File destRetryFolder) throws Exception{
		File[] retryFiles = new File[]{};
		if(missingFiles!=null)
		for(int i=0;i<missingFiles.length;i++)
			FileUtils.copyFileToDirectory(missingFiles[i], destRetryFolder);
		retryFiles = destRetryFolder.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".cgm");
			}
		});
		return retryFiles;
	}
	
	/**
	 * retry 폴더를 생성하여 리턴.(temp 폴더 하위에 retry 폴더를 생성.)
	 * @param destFolder
	 * @return
	 * @throws Exception
	 */
	private File getRetryFolder(String destFolder) throws Exception{
		File retryFolder = null;
		File _destFolder = new File(destFolder);
		if(!_destFolder.exists()){
			FileUtils.forceMkdir(_destFolder);
		}
			
		File parentTempPath = _destFolder.getParentFile();
		String parentTempPathStr = parentTempPath.getAbsolutePath();
		String retryFolderStr = "";
		if(parentTempPathStr.endsWith(this.fileSeparator)){
			retryFolderStr = parentTempPathStr + "retry" + this.fileSeparator;
		}else{
			retryFolderStr = parentTempPathStr + this.fileSeparator + "retry" + this.fileSeparator;
		}
		retryFolder = new File(retryFolderStr);
		if(!retryFolder.exists())
			FileUtils.forceMkdir(retryFolder);
		else{
			// retry 폴더가 이미 생성되어 있다면 비움.
			FileUtils.cleanDirectory(retryFolder);
		}
		
		return retryFolder;
	}

}
