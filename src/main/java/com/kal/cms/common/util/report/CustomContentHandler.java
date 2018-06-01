package com.kal.cms.common.util.report;


import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.kal.cms.common.util.DateUtil;
import com.kal.cms.content.vo.ContentTaskInfo;

public class CustomContentHandler extends DefaultHandler{
	
	protected static String FILE_SEPARATOR = System.getProperty("file.separator");
	protected static String LINE_SEPARATOR = System.getProperty("line.separator");
	protected static String COL_SEPARATOR = ",";
	protected static String INDENT = "  ";
	protected String currIndent = "";
	protected static String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	
	protected static String QUOT = "\"";
	protected static String SQUOT = "'";
	
	protected static String TEXT_READY_VALUE = "R";

	protected StringBuilder xmlText;

	protected boolean innerTaskFlag = false;			//Task 노드에 진입 여부
	protected boolean innerSubTaskFlag = false;		//sub Task 노드에 진입 여부
	protected boolean innerFLTROW = false;			//FLTROW 노드에 진입 여부
	protected boolean innerFLTDESC = false;		//FLTDESC 노드에 진입 여부
	
	protected int subTaskSeq = 0;						//sub Task 노드에 진입 여부
	protected int fltdescSeq = 0;						//FLTDESC 노드에 진입 여부
	
	protected static String elemNameTask = "TASK";			//task's tag name
	protected static String elemNameSubTask = "SUBTASK";		//sub_task's tag name
	protected static String elemName_FLTROW = "FLTROW";		//FLTROW's tag name
	protected static String elemName_FLTDESC = "FLTDESC";		//FLTDESC's tag name
	
	protected int depth = 0;

	protected String outputFileName = "";
	protected String csvPrefix = "";
	protected Writer writer = null;
	protected int writeCnt = 0;
	protected boolean isCurrElemHasText = false;
	
	protected CustomDTDHandler _dtdHandler = null;
	protected String outputPath = "";
	
	protected String header;
	protected ContentTaskInfo taskInfo;
	protected ReportInfo report;
	
	protected String rDate;
	protected String rDateYYMM;

	public CustomContentHandler(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
		this._dtdHandler = (CustomDTDHandler)dtdHandler;
		this.outputPath = outputPath;
		this.csvPrefix = csvPrefix;
		
		this.report = report;
		this.taskInfo = report.getTaskInfo();
		this.header = report.getHeader();
		
		this.rDate = DateUtil.getDate2String(taskInfo.getWorkStartDatetime(), "yyyyMMdd");
		this.rDateYYMM = DateUtil.getDate2String(taskInfo.getWorkStartDatetime(), "yyyyMM");
		
		this.writer = writer;
	}	
	
	@Override
	public void startDocument()	{
		try {
			emitCsv(header);	//csv 헤더 셋팅
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void endDocument(){
		System.out.println("Report file creating Complete ... : file = " +taskInfo.getUrlAddress());
		
		if(this.writer!=null)try{writer.close();}catch(Exception e){}
	}	
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {	
	}
	
	/**
	 * xml에서 인식하지 못하는 어트리뷰트 내 값의 특수 기호들을 변환한다.
	 * @param (String)
	 * @return
	 */
	protected String convertXmlAttrUnusedChars(String attrValue){
		if(attrValue==null)return null;
		if(attrValue.contains("& ")){
			attrValue = attrValue.replaceAll("& ", "&amp; ");
		}
		if(attrValue.contains("<")){
			attrValue = attrValue.replaceAll("<", "&lt;");
		}
		if(attrValue.contains(">")){
			attrValue = attrValue.replaceAll(">", "&gt;");
		}
		
		return attrValue;
	}
	
	/**
	 * 특수 기호들을 원래 형태로 변환한다.
	 * @param (String)
	 * @return
	 */
	protected String convertPreChars(String str){
		if(str==null)return "";
		
		if(str.indexOf("&amp;") > -1){
			str = str.replaceAll("&amp;","&");
		}
		if(str.indexOf("&lt;") > -1){
			str = str.replaceAll("&lt;", "<");
		}
		if(str.indexOf("&gt;") > -1){
			str = str.replaceAll("&gt;", ">");
		}
		
		return str;
	}	

	protected StringBuilder getText() {
		return xmlText;
	}

	protected void setText(StringBuilder text) {
		this.xmlText = text;
	}

	protected void emitCsv(String source){
		try {
			writer.write(source.concat(LINE_SEPARATOR));
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	protected String getQuotString(String org){
		return QUOT.concat(org).concat(QUOT);
	}
	
	protected String getPreQuotString(String org){
		return SQUOT.concat(org);
	}

	/**
	 * 테스트용 코드, 진행할 갯수만 진행하고 예외를 발생시킨다. 파일은 남음.
	 * @param limit
	 * @param showXml
	 * @throws Exception
	 */
	protected void testRowLimit(int limit, boolean showXml) throws SAXException{
		if(writeCnt > limit) {
			if(showXml)
				System.out.println("last xml = "+xmlText.toString());
			throw new SAXException("강종");
		}
	}
}

