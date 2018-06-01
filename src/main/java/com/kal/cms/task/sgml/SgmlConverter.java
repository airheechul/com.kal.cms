package com.kal.cms.task.sgml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import nu.xom.ParsingException;
import nu.xom.ValidityException;


import org.apache.commons.lang3.ArrayUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * SGML to xml 변환 테스트 클래스.
 */
public class SgmlConverter{
	
	private static final String FEATURE1 = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	private static final String FEATURE2 = "http://xml.org/sax/features/external-general-entities";
	private static final String FEATURE3 = "http://xml.org/sax/features/external-parameter-entities";
	private static final String FEATURE4 = "http://xml.org/sax/features/validation";
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static String _osName = System.getProperty("os.name").toLowerCase();
	
	private static final SgmlConverter INSTANCE = new SgmlConverter();
	
	public static SgmlConverter getInstance(){
		return INSTANCE;
	}
	
	public SgmlConverter(){
	}
	
	/**
	 * sx 커맨트 라인 툴을 호출하여 sgml을 temp xml로 변환.
	 * runtime 호출을 바로 할 경우, sx.exe 파일의 output 출력을 받아 파일로 저장해야 함.
	 * 메모리 사용량이 많고 비효율적임.
	 */
	public boolean convertToXml(String destPath){
		boolean rt = false;
		// sx.exe -E0 -D"D:\data\KAL_sample\sgml\ISO-entities" -c"D:\data\KAL_sample\sgml\catalog" -f"D:\data\KAL_sample\sgml\errors.log" -xno-nl-in-tag -xnotation -xndata "D:\data\KAL_sample\sgml\AMM_CGM.DTD" "D:\data\KAL_sample\sgml\chapter_sample.SGM" > "D:\data\KAL_sample\sgml\TEMP_chapter_sample.xml"
		String basePath = "D:\\data\\KAL_sample\\sgml\\";
		String entityFilesPath = basePath+"SGML\\CGM"; // entity 경로가 아니라 CGM 이미지 파일경로를 지정한다.
		String catalogFilePath = basePath+"catalog";
		String errorLogPath = basePath+"errors.log";
		String dtdFilePath = basePath+"SGML\\AMM_CGM.DTD"; // AM020702.DTD 파일은 basePath하위에 SGML 폴더를 만들어 그곳에 위치시켜두어야 한다.
		String sgmFilePath = basePath + "amm.SGM";
		String _command = "cmd.exe /c "+basePath+"sx.exe -E0 -D\""+entityFilesPath+"\" -c\""+catalogFilePath+"\" -f\""+errorLogPath+"\" -xnotation -xndata \""+dtdFilePath+"\" \""+sgmFilePath+"\" >\""+destPath+"\"";
		System.out.println("command: "+_command);
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(_command);
			// 표준 출력
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			char[] buff = new char[1024];
			int len = reader.read(buff);
			while(len>=0){
				System.out.print(new String(buff, 0, len));
				len = reader.read(buff);
			}
			// 파일 출력
//			File _destFile = new File(destPath);
//			FileOutputStream fos = new FileOutputStream(_destFile);
//			IOUtils.copy(new InputStreamReader(process.getInputStream()), fos, "utf-8");
			
			int result = process.exitValue();
			System.out.println("return value: "+result);
			if(result==0)rt = true;
			//if(fos!=null)fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rt;
	}
	
	/**
	 * sx_run.bat 파일을 대신 실행시켜 sx.exe 파일의 output을 파일로 쓰게 한다.
	 * @param destPath - xml convert한 결과 파일의 경로.
	 * @param basePath - 작업이 이루어질 base path. 이 경로를 기준으로 로그 파일과 <br/>
	 * 	CGM, catalog 파일 참조가 이루어짐.
	 * @param cgmDTDPath - cgm 이미지에 대한 dtd 파일 경로
	 * @param sgmFilePath - sgm 파일 경로.
	 * @return
	 * @throws Exception 
	 */
	public boolean convertToXmlUsingWrapperBatch(String destPath, String basePath, String cgmDTDPath, String sgmFilePath, 
			String batchFilePath, String sxBinPath, Object kcontext) {
		boolean isWindows = false;
		boolean isAix = false;
		if(_osName.indexOf("win")>-1)isWindows = true;
		else if(_osName.indexOf("aix")>-1)isAix = true;
		
		boolean rt = false;
		//String batchFilePath = "C:\\sx_run.bat";
		// sx.exe -E0 -D"D:\data\KAL_sample\sgml\ISO-entities" -c"D:\data\KAL_sample\sgml\catalog" -f"D:\data\KAL_sample\sgml\errors.log" -xno-nl-in-tag -xnotation -xndata "D:\data\KAL_sample\sgml\AMM_CGM.DTD" "D:\data\KAL_sample\sgml\chapter_sample.SGM" > "D:\data\KAL_sample\sgml\TEMP_chapter_sample.xml"
		//String binPath = "D:\\data\\kal\\KAL_sample\\sgml\\";
		String entityFilesPath = basePath+"SGML"+FILE_SEPARATOR+"CGM"; // entity 경로가 아니라 CGM 이미지 파일경로를 지정한다.
		String catalogFilePath = basePath+"catalog";
		String errorLogPath = basePath+"errors.log";
		// AM020702.DTD 파일은 basePath하위에 SGML 폴더를 만들어 그곳에 위치시켜두어야 한다.
		String destExePath = "" + sxBinPath+" -E0 -D"+entityFilesPath+" -c"+catalogFilePath+" -f"+errorLogPath+" -xnotation -xndata "+cgmDTDPath+" "+sgmFilePath+"";
		String outputRedirectPath = "" + destPath+ "";
		String _command = "";
		String procMsg = "";
		if(batchFilePath==null || "".equals(batchFilePath)){
			_command = sxBinPath+" -E0 -D"+entityFilesPath+" -c"+catalogFilePath+" -f"+errorLogPath+" -xnotation -xndata "+cgmDTDPath+" "+sgmFilePath+" > "+destPath;
		}else{
			_command = batchFilePath + " \"" + sxBinPath+" -E0 -D"+entityFilesPath+" -c"+catalogFilePath+" -f"+errorLogPath+" -xnotation -xndata "+cgmDTDPath+" "+sgmFilePath+"\" \""+destPath+"\"";
		}
		System.out.println("[SgmlConverter] command: "+_command);
		procMsg += "[SgmlConverter] command: "+_command + "\\r\\n";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[]{batchFilePath, destExePath, outputRedirectPath});
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
			System.out.println("[SgmlConverter] return value: "+result);
			procMsg += "[SgmlConverter] return value: "+result + "\\r\\n";
			if(result==0){
				rt = true;
				try{
					((ProcessContext)kcontext).setVariable("_taskResult", true);
					((ProcessContext)kcontext).setVariable("procMsg", procMsg);
				}catch(Exception e){}
			}else new Exception("There is no response from sx command.");
		} catch (Exception e) {
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Converting SGML to Temp XML error. msg:"+e.getMessage());
			}catch(Exception _e){}
			//throw e;
		}
		
		return rt;
	}
	
	public String a(File file, String s, String s1, String s2, String s3, String s4, String s5)
	{
	    if(file == null || file.length() == 0L || s == null || s.length() == 0 || s3 == null || s3.length() == 0 || s4 == null || s4.length() == 0)
	        return null;
	    String s6 = "D:/data/KAL_sample/sgml"; //path
	    String s7 = file.getName();
	    s7 = "TEMP_" + s7.substring(0, s7.lastIndexOf('.')) + ".xml";
	    String s8 = s3 + File.separatorChar + s7;
	    String s9 = "System" + File.separatorChar + "Tools" + File.separatorChar + "convertSgml";
	    String s10 = s6 + File.separatorChar + s9 + File.separatorChar + "sx.exe";
	    int i = 11;
	    if(s1 != null && s1.length() != 0)
	        i++;
	    if(s2 != null && s2.length() != 0)
	        i += 2;
	    String as[] = new String[i];
	    int j = 0;
	    as[j++] = s10;
	    as[j++] = "-E0";
	    if(s2 != null && s2.length() != 0)
	    {
	        as[j++] = "-D";
	        as[j++] = s2;
	    }
	    as[j++] = "-c";
	    as[j++] = s;
	    as[j++] = "-f";
	    as[j++] = s4;
	    as[j++] = "-x";
	    as[j++] = "notation";
	    as[j++] = "-x";
	    as[j++] = "ndata";
	    if(s1 != null && s1.length() != 0)
	        as[j++] = s1;
	    as[j++] = file.getPath();
	   System.out.println("execute following args.. as:"+ArrayUtils.toString(as)+", s8: "+s8);
	    //int k = corbaexecutablerunner.execute(as, s8, "");
	    
	    return s7;
	}
	
	/**
	 * 임시 xml 파일에서 필요한 영역을 추출하거나 정규화된 xml으로 변환한다.
	 * @throws IOException 
	 * @throws ParsingException 
	 * @throws ValidityException 
	 */
	public void formatXml(String xmlPath, String elemNameToParse, String outputPath, String xmlPrefix, Object kcontext) {
		try{
			/////
			// GRPHCREF REFID 에 대한 GRAPHIC 엘레멘트 참조 로직은, 
			// burstXML 시 GRAPHIC 엘레멘트에 대한 burst 로직을 한단계 더 두어 
			// 다시 merge하는 과정으로 수행한다.
			String procMsg = "[SgmlConverter(burst)] xmlPath:"+xmlPath+", elemNameToParse:"+elemNameToParse+", outputPath:"+outputPath
				+", xmlPrefix:"+xmlPrefix;
			InputStream inputStream = new FileInputStream(new File(xmlPath));
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("utf-8");
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setFeature(FEATURE1, false);
			xmlReader.setFeature(FEATURE2, false);
			xmlReader.setFeature(FEATURE3, false);
			xmlReader.setFeature(FEATURE4, false);
			CustomDTDHandler _dtdHandler = new CustomDTDHandler();
			CustomContentHandler cch = new CustomContentHandler(_dtdHandler, elemNameToParse, outputPath, xmlPrefix);
			XMLFilter xmlFilter = new XMLFilterEntityImpl(xmlReader);
			xmlFilter.setDTDHandler(_dtdHandler);
			xmlFilter.setContentHandler(cch);
			xmlFilter.parse(is);
			////xmlReader.setEntityResolver(new CustomEntityResolver());
			//xmlReader.setDTDHandler(_dtdHandler);
			//xmlReader.setContentHandler(cch);
			//xmlReader.parse(is);
			if(inputStream != null)inputStream.close();
			if(outputPath==null || "".equals(outputPath))
				System.out.println("extract result: "+cch.getText().toString());
			
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "SAX XML Parsing Error. msg:"+e.getMessage());
			}catch(Exception _e){}
			//throw e;
		}
	}
	
	/**
	 * full xml을 변환하고 rev 정보를 validate 한다.
	 * @param xmlPath
	 * @param elemNameToParse
	 * @param outputPath
	 * @param xmlPrefix
	 * @param revNum
	 * @param manualType
	 * @param kcontext
	 */
	public void formatFullXmlAndValidateRevNum(String xmlPath, String elemNameToParse, String outputPath, String xmlPrefix, String revNum, 
			String manualType, Object kcontext){
		try{
			String procMsg = "[Convert Full XML] xmlPath:"+xmlPath+", elemNameToParse:"+elemNameToParse+", outputPath:"+outputPath
				+", xmlPrefix:"+xmlPrefix+", revNum:"+revNum+"\\r\\n";
			InputStream inputStream = new FileInputStream(new File(xmlPath));
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("utf-8");
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setFeature(FEATURE1, false);
			xmlReader.setFeature(FEATURE2, false);
			xmlReader.setFeature(FEATURE3, false);
			xmlReader.setFeature(FEATURE4, false);
			CustomDTDHandler _dtdHandler = new CustomDTDHandler();
			CustomContentHandler cch = new CustomContentHandler(_dtdHandler, elemNameToParse, outputPath, xmlPrefix);
			XMLFilter xmlFilter = new XMLFilterEntityImpl(xmlReader);
			xmlFilter.setDTDHandler(_dtdHandler);
			xmlFilter.setContentHandler(cch);
			xmlFilter.parse(is);
			////xmlReader.setEntityResolver(new CustomEntityResolver());
			//xmlReader.setDTDHandler(_dtdHandler);
			//xmlReader.setContentHandler(cch);
			//xmlReader.parse(is);
			if(inputStream != null)inputStream.close();
			if(outputPath==null || "".equals(outputPath))
				System.out.println("extract result: "+cch.getText().toString());
			
			///////////
			// full xml 변환시, AMM 최상위 엘레멘트 어트리뷰트로 있는 REVDATE, TSN 값을 파싱하여 
			// 현재 리비전 번호와 비교하여 크지 않으면 오류로 처리한다.
			HashMap<String, String> ammAttrNameValueMap = cch.getAmmAttrNameValueMap();
			if(ammAttrNameValueMap.containsKey("TSN") && ammAttrNameValueMap.containsKey("REVDATE")){
				String currRevNum = ammAttrNameValueMap.get("TSN");
				String revDate = ammAttrNameValueMap.get("REVDATE");
				try{
					SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
					simpledateformat.parse(revDate);
					// TR 매뉴얼 처리시에는 문서 내에 포함된 revNum validation은 하지 않고 
					// DB에 저장된 최신 revNum 에 덮어쓰는 개념으로 처리한다.
					if("TR".equalsIgnoreCase(manualType)){
						try{
							((ProcessContext)kcontext).setVariable("currRevNum", revNum);
							((ProcessContext)kcontext).setVariable("currRevDate", revDate);
						}catch(Exception e){}
					}else{
						if(Integer.parseInt(currRevNum, 10) > Integer.parseInt(revNum, 10)){
							System.out.println("[Convert Full XML] Current revNum: "+currRevNum+", Current revDate: "+revDate+", Previous revNum: "+revNum);
							procMsg += "[Convert Full XML] Current revNum: "+currRevNum+", Current revDate: "+revDate+", Previous revNum: "+revNum + "\\r\\n";
						}else{
							throw new Exception("Invalid revNum");
						}
						try{
							((ProcessContext)kcontext).setVariable("currRevNum", currRevNum);
							((ProcessContext)kcontext).setVariable("currRevDate", revDate);
						}catch(Exception e){}
					}
					
				}catch(Exception e){
					e.printStackTrace();
					throw new Exception("Invalid revNum or revDate. Current revNum: "+currRevNum+", Current revDate: "+revDate+", Previous revNum: "+revNum);
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
				((ProcessContext)kcontext).setVariable("errorMsg", "SAX XML Parsing Error. msg:"+e.getMessage());
			}catch(Exception _e){}
			//throw e;
		}
	}

	public static void main(String[] args) throws Exception{
		SgmlConverter sc = new SgmlConverter();
		//File sgmlFile = new File("D:/data/kal/KAL_sample/sgml/chapter_sample.SGM");
//		System.out.println("parent file:"+sgmlFile.getParentFile().getPath());
		//System.out.println(sc.a(sgmlFile, "catalog", "AMM_CGM.DTD", "D:/data/KAL_sample/sgml/ISO-entities", sgmlFile.getParentFile().getPath(), "errors.log", "s5"));
		String basePath = "c:\\";
		String destFile = basePath + "test.xml";
		sc.formatXml(destFile, "task", basePath + "converted", "B777_AMM", null);
		
		System.out.println("Done.");
//		String basePath = "D:\\data\\kal\\KAL_sample\\B777\\";
//		String destFile = basePath + "SGML\\TEMP_AMM.xml";
//		String cgmDTDPath = basePath+"SGML\\AMM_CGM.DTD";
//		String sgmFilePath = basePath + "SGML\\AMM.SGM";
//		String batchFilePath = "C:\\sx_run.bat";
//		String sxBinPath = "D:\\data\\kal\\KAL_sample\\sgml\\sx.exe";
//		boolean isTempXmlConverted = sc.convertToXmlUsingWrapperBatch(destFile, basePath, cgmDTDPath, sgmFilePath, batchFilePath, sxBinPath);
//		if(isTempXmlConverted){
//			// burst to task xml
//			sc.formatXml(destFile, "task", basePath + "converted", "B777_AMM");
//			// convert full xml
//			sc.formatXml(destFile, "amm", basePath + "converted", "AMM");
//		}
//		System.out.println("B777 process Done!!");
		
//		String basePath1 = "D:\\data\\kal\\KAL_sample\\A380\\";
//		String destFile1 = basePath1 + "SGML\\TEMP_AMM.xml";
//		String cgmDTDPath1 = basePath1+"SGML\\AMM.SGE";
//		String sgmFilePath1 = basePath1 + "SGML\\AMM.SGM";
//		isTempXmlConverted = sc.convertToXmlUsingWrapperBatch(destFile1, basePath1, cgmDTDPath1, sgmFilePath1, batchFilePath, sxBinPath);
//		if(isTempXmlConverted){
//			// burst to task xml
//			sc.formatXml(destFile1, "task", basePath1 + "converted", "A380_AMM");
//			// convert full xml
//			sc.formatXml(destFile1, "amm", basePath1 + "converted", "AMM");
//		}
//		System.out.println("A380 process Done!!");
		
	}
	
}
