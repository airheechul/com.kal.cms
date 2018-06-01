package com.kal.cms.task.sgml;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

/**
 * TR일 경우 원본 AMM 폴더 하위의 리소스를 복사하는 서브 프로세스를 수행.
 */
public class CopySGMLResourceSubProcess {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final CopySGMLResourceSubProcess INSTANCE = new CopySGMLResourceSubProcess();
	
	public static CopySGMLResourceSubProcess getInstance(){
		return INSTANCE;
	}
	
	/**
	 * 프로세스 수행.
	 * @param srcCgmPath
	 * @param kcontext
	 */
	public void copySGMLResourceProcess(String basePath, Object kcontext){
		///////
		// 원본 AMM 폴더 하위의 catalog, xml.dcl, ISO-entities 폴더, 
		// SGML/*.DTD 파일들을 copy하여 TR 작업 대상 폴더로 옮긴다. 
		// 이 때, TR/SGML/*.DTD 파일 중 원래 존재하는 DTD 파일은 덮어쓰지 않는다. 
		String procMsg = "";
		try{
			if(!basePath.endsWith(FILE_SEPARATOR))basePath = basePath + FILE_SEPARATOR;
			File _baseFolder = new File(basePath);
			if(!_baseFolder.isDirectory())
				throw new Exception("Invalid folder path. base folder: "+_baseFolder.getAbsolutePath());
			String orgAmmFolder = _baseFolder.getParent()+FILE_SEPARATOR+"AMM"+FILE_SEPARATOR;
			
			File catalogFile = new File(orgAmmFolder+"catalog");
			File xmlDclFile = new File(orgAmmFolder+"xml.dcl");
			File isoEntitesFolder = new File(orgAmmFolder+"ISO-entities");
			
			FileUtils.copyFileToDirectory(catalogFile, _baseFolder);
			System.out.println("[CopySGMLResourceSubProcess] copying "+catalogFile.getAbsolutePath()+" to "+_baseFolder.getAbsolutePath()+" is completed.");
			procMsg += "[CopySGMLResourceSubProcess] copying "+catalogFile.getAbsolutePath()+" to "+_baseFolder.getAbsolutePath()+" is completed.\\r\\n";
			
			FileUtils.copyFileToDirectory(xmlDclFile, _baseFolder);
			System.out.println("[CopySGMLResourceSubProcess] copying "+xmlDclFile.getAbsolutePath()+" to "+_baseFolder.getAbsolutePath()+" is completed.");
			procMsg += "[CopySGMLResourceSubProcess] copying "+xmlDclFile.getAbsolutePath()+" to "+_baseFolder.getAbsolutePath()+" is completed.\\r\\n";
			
			FileUtils.copyDirectoryToDirectory(isoEntitesFolder, _baseFolder);
			System.out.println("[CopySGMLResourceSubProcess] copying "+isoEntitesFolder.getAbsolutePath()+" to "+_baseFolder.getAbsolutePath()+" is completed.");
			procMsg += "[CopySGMLResourceSubProcess] copying "+isoEntitesFolder.getAbsolutePath()+" to "+_baseFolder.getAbsolutePath()+" is completed.";
			
			File orgAmmSgmlFolder = new File(orgAmmFolder + "SGML" + FILE_SEPARATOR);
			if(!orgAmmSgmlFolder.isDirectory())
				throw new Exception("Invalid folder path. Org AMM SGML folder: "+orgAmmSgmlFolder.getAbsolutePath());
			
			File[] orgDtdFileArray = orgAmmSgmlFolder.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					boolean rt = false;
					if(name.toLowerCase().endsWith(".dtd"))
						rt = true;
					return rt;
				}
			});
			
			File basePathSgmlFolder = new File(basePath+"SGML");
			if(!basePathSgmlFolder.isDirectory())
				throw new Exception("Invalid folder path. TR SGML folder: "+basePathSgmlFolder.getAbsolutePath());
			
			File[] dtdFileArray = basePathSgmlFolder.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					boolean rt = false;
					if(name.toLowerCase().endsWith(".dtd"))
						rt = true;
					return rt;
				}
			});
			
			if(dtdFileArray!=null){
				for(int i=0;i<dtdFileArray.length;i++){
					File dtdFile = dtdFileArray[i];
					File finedDtdFile = getContainedFileByFileName(orgDtdFileArray, dtdFile.getName());
					if(finedDtdFile!=null){
						orgDtdFileArray = (File[])ArrayUtils.removeElement(orgDtdFileArray, finedDtdFile);
					}
				}
			}
			
			if(orgDtdFileArray!=null){
				for(int i=0;i<orgDtdFileArray.length;i++){
					FileUtils.copyFileToDirectory(orgDtdFileArray[i], basePathSgmlFolder);
					System.out.println("[CopySGMLResourceSubProcess] copying "+orgDtdFileArray[i].getAbsolutePath()+" to "+basePathSgmlFolder.getAbsolutePath()+" is completed.");
					procMsg += "[CopySGMLResourceSubProcess] copying "+orgDtdFileArray[i].getAbsolutePath()+" to "+basePathSgmlFolder.getAbsolutePath()+" is completed.\\r\\n";
				}
			}
			
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Copy SGML Resource for TR Process Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * 파일명을 기준으로 file 배열에서 같은 이름의 파일 객체를 찾아서 리턴.
	 * @param fileArray
	 * @param fileName
	 * @return (File)
	 * @throws Exception
	 */
	private File getContainedFileByFileName(File[] fileArray, String fileName) throws Exception{
		File rt = null;
		if(fileArray!=null)
		for(int j=0;j<fileArray.length;j++){
			File _file = fileArray[j];
			if(_file!=null){
				if(fileName.equals(_file.getName())){
					rt = _file;
					break;
				}
			}
		}
		return rt;
	}
	
}
