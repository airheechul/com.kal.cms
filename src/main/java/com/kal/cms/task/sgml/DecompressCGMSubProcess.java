package com.kal.cms.task.sgml;

import java.io.File;
import java.io.FilenameFilter;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

import com.kal.cms.common.util.ZipUtil;

/**
 * CGM 하위 폴더에 zip 압축 파일이 있을 경우 압축을 푸는 서브 프로세스를 수행.
 */
public class DecompressCGMSubProcess {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final DecompressCGMSubProcess INSTANCE = new DecompressCGMSubProcess();
	
	public static DecompressCGMSubProcess getInstance(){
		return INSTANCE;
	}
	
	/**
	 * 프로세스 수행.
	 * @param srcCgmPath
	 * @param kcontext
	 */
	public void doDecompressProcess(String srcCgmPath, Object kcontext){
		///////
		// src cgm folder 하위의 확장자가 zip인 파일 목록을 읽어와 
		// 첫번 째 것을 선택하여 압축을 푼다.
		String procMsg = "";
		try{
			File _srcCgmFolder = new File(srcCgmPath);
			if(!_srcCgmFolder.isDirectory())
				throw new Exception("Invalid folder path. src cgm folder: "+_srcCgmFolder.getAbsolutePath());
			
			File[] zipFileArray = _srcCgmFolder.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					boolean rt = false;
					if(name.toLowerCase().endsWith(".zip"))
						rt = true;
					return rt;
				}
			});
			
			if(zipFileArray!=null && zipFileArray.length>0){
				ZipUtil.unzip(zipFileArray[0].getAbsolutePath(), srcCgmPath);
				System.out.println("[DecompressCGMSubProcess] Unziping "+zipFileArray[0].getAbsolutePath()+" to "+srcCgmPath+" is completed.");
				procMsg = "[DecompressCGMSubProcess] Unziping "+zipFileArray[0].getAbsolutePath()+" to "+srcCgmPath+" is completed.";
			}
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Unzip CGM zip file Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
}
