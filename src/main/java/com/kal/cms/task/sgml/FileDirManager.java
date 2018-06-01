package com.kal.cms.task.sgml;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * SGML 컨버팅 시 사용되는 파일명 및 디렉토리 위치 조회.
 */
public class FileDirManager {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public String getTempXmlFilePath(String nasPath, String model, String manualType){
		String fixedManualType = "";
		if("TR".equalsIgnoreCase(manualType))fixedManualType = "amm";
		else fixedManualType = manualType.toLowerCase();
		String rt = "";
		String _destFolderPath = nasPath + FILE_SEPARATOR + (isOEMType(manualType)?"oem":"kor") + FILE_SEPARATOR + 
			model.toLowerCase() + FILE_SEPARATOR + fixedManualType + FILE_SEPARATOR + "temp" + FILE_SEPARATOR;
		rt =  _destFolderPath + "TEMP_"+manualType+".xml";
		File _destFolder = new File(_destFolderPath);
		if(!_destFolder.exists()){
			try {
				FileUtils.forceMkdir(_destFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rt;
	}
	
	/**
	 * manualType 파라메터로 OEM 타입인지 확인.
	 * @param manualType
	 * @return
	 */
	public boolean isOEMType(String manualType){
		boolean rt = false;
		if("AMM".equalsIgnoreCase(manualType))rt = true;
		else if("FRMFIM".equalsIgnoreCase(manualType))rt = true;
		else if("TSM".equalsIgnoreCase(manualType))rt = true;
		else if("TR".equalsIgnoreCase(manualType))rt = true;
		return rt;
	}
	
	/**
	 * CGM 이미지를 png로 바꿀 임시 경로를 리턴.
	 * @param nasPath
	 * @param model
	 * @param manualType
	 * @return
	 */
	public String getTempDestPngPath(String nasPath, String model, String manualType){
		String fixedManualType = "";
		if("TR".equalsIgnoreCase(manualType))fixedManualType = "amm";
		else fixedManualType = manualType.toLowerCase();
		String _destFolderPath = nasPath + FILE_SEPARATOR + (isOEMType(manualType)?"oem":"kor") + FILE_SEPARATOR + 
			model.toLowerCase() + FILE_SEPARATOR + fixedManualType + FILE_SEPARATOR + "temp" + FILE_SEPARATOR + 
			"png" + FILE_SEPARATOR;
		File _destFolder = new File(_destFolderPath);
		if(!_destFolder.exists()){
			try {
				FileUtils.forceMkdir(_destFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _destFolderPath;
	}
	
	/**
	 * XML 변환 결과가 저장될 base path를 가져온다.
	 * @param nasPath
	 * @param model
	 * @param manualType
	 * @return
	 */
	public String getDestBasePath(String nasPath, String model, String manualType){
		String fixedManualType = "";
		if("TR".equalsIgnoreCase(manualType))fixedManualType = "amm";
		else fixedManualType = manualType.toLowerCase();
		String _destFolderPath = nasPath + FILE_SEPARATOR + (isOEMType(manualType)?"oem":"kor") + FILE_SEPARATOR + 
			model.toLowerCase() + FILE_SEPARATOR + fixedManualType + FILE_SEPARATOR;
		File _destFolder = new File(_destFolderPath);
		if(!_destFolder.exists()){
			try {
				FileUtils.forceMkdir(_destFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _destFolderPath;
	}
	
	/**
	 * burst된 xml이 저장될 디렉토리 명을 생성하여 리턴.
	 * @param destBasePath
	 * @param revNum
	 * @return
	 */
	public String getBurstedXmlDestPath(String destBasePath, String model, String manualType, String revNum){
		String rt = "";
		if("KOR".equalsIgnoreCase(manualType)){
			rt = destBasePath + FILE_SEPARATOR + getContentTypeOpRt(manualType, model).toLowerCase() + FILE_SEPARATOR + revNum + FILE_SEPARATOR ;
		}else{
			rt = destBasePath + FILE_SEPARATOR + revNum + FILE_SEPARATOR + getContentTypeOpRt(manualType, model).toLowerCase() + FILE_SEPARATOR;
		}
		
		File _destFolder = new File(rt);
		if(!_destFolder.exists()){
			try {
				FileUtils.forceMkdir(_destFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rt;
	}
	
	/**
	 * XML 리소스 이미지가 최종적으로 저장될 폴더 경로를 생성하여 리턴.
	 * @param destBasePath
	 * @param model
	 * @param manualType
	 * @param revNum
	 * @return
	 */
	public String getImageDestPath(String destBasePath, String model, String manualType, String revNum){
		String rt = "";
		if("KOR".equalsIgnoreCase(manualType)){
			rt = destBasePath + FILE_SEPARATOR + "images" + FILE_SEPARATOR ;
		}else{
			//rt = destBasePath + FILE_SEPARATOR + "images" + FILE_SEPARATOR;
			rt = destBasePath + FILE_SEPARATOR + revNum + FILE_SEPARATOR + "images" + FILE_SEPARATOR;
		}
		
		File _destFolder = new File(rt);
		if(!_destFolder.exists()){
			try {
				FileUtils.forceMkdir(_destFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rt;
	}
	
	/**
	 * manualType을 기준으로 FullXml 생성 시 필요한 Root Element 명을 가져온다.
	 * @param manualType
	 * @return (String)
	 */
	public String getFullXmlRootElemName(String manualType){
		String rt = "";
		if("AMM".equalsIgnoreCase(manualType))rt = "amm";
		else if("KOR".equalsIgnoreCase(manualType))rt = "kor";
		else if("FRMFIM".equalsIgnoreCase(manualType)) rt = "frmfim";
		else if("TSM".equalsIgnoreCase(manualType)) rt = "tsm";
		else if("TR".equalsIgnoreCase(manualType)) rt = "amm";
		return rt;
	}
	
	/**
	 * 변환될 FullXML 파일명으로 사용될 Prefix를 리턴.
	 * @param sgmFilePath
	 * @return
	 */
	public String getFullXmlFilePrefix(String sgmFilePath){
		return FilenameUtils.getBaseName(sgmFilePath);
	}
	
	/**
	 * XML burst시 기준 엘레멘트로 사용될 엘레멘트명을 리턴.
	 * @param manualType
	 * @param model
	 * @return
	 */
	public String getBurstXmlElemName(String manualType, String model){
		String rt = "";
		if("AMM".equalsIgnoreCase(manualType))rt = "task";
		else if("TR".equalsIgnoreCase(manualType))rt = "task";
		return rt;
	}
	
	/**
	 * XML burst 시 사용될 파일명 Prefix를 리턴.
	 * @param manualType
	 * @param model
	 * @return
	 */
	public String getBurstXmlPrefix(String manualType, String model){
		String fixedManualType = "";
		if("TR".equalsIgnoreCase(manualType))fixedManualType = "AMM";
		else fixedManualType = manualType;
		String rt = model + "_" + fixedManualType;
		return rt;
	}
	
	/**
	 * XML Graphic 엘레멘트 burst시 기준 엘레멘트로 사용될 엘레멘트명을 리턴.
	 * @param manualType
	 * @param model
	 * @return
	 */
	public String getBurstGraphicXmlElemName(String manualType, String model){
		String rt = "";
		if("AMM".equalsIgnoreCase(manualType) || "TR".equalsIgnoreCase(manualType)){
			if(model.startsWith("A"))rt = "graphic";
		}
		return rt;
	}
	
	/**
	 * XML Graphic 엘레멘트 burst 시 사용될 파일명 Prefix를 리턴.
	 * @param manualType
	 * @param model
	 * @return
	 */
	public String getBurstGraphicXmlPrefix(String manualType, String model){
		String rt = "";
		if("AMM".equalsIgnoreCase(manualType) || "TR".equalsIgnoreCase(manualType)){
			if(model.startsWith("A"))rt = "GRAPHIC";
		}
		return rt;
	}
	
	/**
	 * 경로명에 사용될 ContentType 스트링을 리턴.
	 * @param manualType
	 * @param model
	 * @return
	 */
	public String getContentTypeOpRt(String manualType, String model){
		// manualType이 KOR일 경우 어떤 조건이면 ContentType이 OP가 된다.
		// 나머지는 모두 RT.
		return "RT";
	}

}
