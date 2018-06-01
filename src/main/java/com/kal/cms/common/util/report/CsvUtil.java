package com.kal.cms.common.util.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.lang3.time.StopWatch;
import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.easycompany.cmm.exception.MpFrameException;
import com.kal.cms.common.util.DateUtil;
import com.kal.cms.content.vo.ContentTaskInfo;

public class CsvUtil {
	private static final String FEATURE1 = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	private static final String FEATURE2 = "http://xml.org/sax/features/external-general-entities";
	private static final String FEATURE3 = "http://xml.org/sax/features/external-parameter-entities";
	private static final String FEATURE4 = "http://xml.org/sax/features/validation";	
	
	private Writer writer = null;
	
	/**
	 * 임시 xml 파일에서 필요한 영역을 추출하거나 정규화된 xml으로 변환한다.
	 * @throws IOException 
	 * @throws ParsingException 
	 * @throws ValidityException 
	 */
	public void formatXml(ContentTaskInfo taskInfo, String xmlPath, String elemNameToParse, String outputPath, String csvPrefix) {

		try{
			//들고와야할 정보 셋팅
			ReportInfo report = new ReportInfo(taskInfo);
			//-------------------
			Reader reader = new InputStreamReader(new FileInputStream(new File(xmlPath)), "UTF-8");
			InputSource is = new InputSource(reader);
			
			//CSV 파일 준비
			initFileStream(taskInfo, outputPath, csvPrefix);
			
			is.setEncoding("utf-8");
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setFeature(FEATURE1, false);
			xmlReader.setFeature(FEATURE2, false);
			xmlReader.setFeature(FEATURE3, false);
			xmlReader.setFeature(FEATURE4, false);
			CustomDTDHandler _dtdHandler = new CustomDTDHandler();

			XMLFilter xmlFilter = new XMLFilterEntityImpl(xmlReader);
			xmlFilter.setDTDHandler(_dtdHandler);

			xmlFilter.setContentHandler(new ContentHandlerHelper(_dtdHandler, outputPath, csvPrefix, report, writer).getInstance());

			xmlFilter.parse(is);

		} catch (FileNotFoundException fe) {
			if(this.writer!=null)try{writer.close();}catch(Exception de){}
			
			throw new MpFrameException("원본 xml을 읽지 못했습니다. filePath ="+xmlPath);
		} catch(Exception e) {
			if(this.writer!=null)try{writer.close();}catch(Exception de){}
			
			e.printStackTrace();
			throw new MpFrameException("서버 오류 입니다.");
		} finally {
		}
	}
	
	private void initFileStream(ContentTaskInfo taskInfo , String outputPath, String csvPrefix) throws Exception{
		String _outputFileName = outputPath;
		String _csvFileName = csvPrefix +"_"+ DateUtil.getToday("yyyyMMddhhmmss")+ ".csv";
		
		System.out.println("Report file creating start ... : fileName = " +_csvFileName + ", path = "+_outputFileName);
		
		_outputFileName += "/" + _csvFileName;

		taskInfo.setFileName(_csvFileName);
		taskInfo.setUrlAddress(_outputFileName);
		
		File outputFile = new File(_outputFileName);
		File _outputPath = new File(outputPath);
		if(!_outputPath.exists())
			_outputPath.mkdirs();

		this.writer = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
	}
	
	public static ContentTaskInfo createReport(ContentTaskInfo taskInfo) throws Exception{
		/*
		CsvUtil sc = new CsvUtil();

		String basePath = "c:\\";
		String destFile = basePath + "test.xml";
		sc.formatXml(destFile, "task", basePath + "converted", "B777_AMM");
		*/
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		try {
			CsvUtil sc = new CsvUtil();

			String basePath = taskInfo.getReportBase();
			String srcFile = taskInfo.getReportSrcPath();
			String destFilePrefix = taskInfo.getMaintDocModelCode()+"_"+taskInfo.getWOCntsDocSubtypCd()+"_"+taskInfo.getReportType();
			sc.formatXml(taskInfo, srcFile, "TASK", basePath, destFilePrefix);

		} catch (MpFrameException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		stopWatch.stop();
		long time = stopWatch.getTime();
		System.out.println("elapsedTime : " + time);
		System.out.println("Done.");
		
		return taskInfo;
	}
}
