package com.kal.cms.task.sgml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class CustomContentHandler extends DefaultHandler{
	
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static String LINE_SEPARATOR = System.getProperty("line.separator");
	private static String INDENT = "  ";
	private String currIndent = "";

	private StringBuilder text = new StringBuilder();

	private boolean innerXmlFlag = false;
	private int depth = 0;
	private CustomDTDHandler _dtdHandler = null;
	private String xmlPrefix = "";
	private String chapnbr = "";
	private String sectnbr = "";
	private String subjnbr = "";
	private String pgblknbr = "";
	private String func = "";
	private String seq = "";
	private String key = "";
	private String elemNameToParse = "";
	private String outputPath = "";
	private String outputFileName = "";
	private HashMap<String, String> ammAttrNameValueMap = new HashMap<String, String>();
	private Writer writer = null;
	private OutputStream outputStream = null;
	private int writeCnt = 0;
	private boolean isCurrElemHasText = false;
	
	public CustomContentHandler(DTDHandler dtdHandler, String elemNameToParse, String outputPath, String xmlPrefix){
		this._dtdHandler = (CustomDTDHandler)dtdHandler;
		this.elemNameToParse = elemNameToParse;
		this.outputPath = outputPath;
		this.xmlPrefix = xmlPrefix;
	}
	
	private void emit(String _char){
		if(this.outputPath!=null && !"".equals(this.outputPath)){
			if(this.writer!=null){
				try {
					writer.write(_char);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			text.append(_char);
		}
	}
	
	private void emit(char[] _ch, int start, int length){
		if(this.outputPath!=null && !"".equals(this.outputPath)){
			if(this.writer!=null){
				try {
					writer.write(String.copyValueOf(_ch, start, length));
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			text.append(_ch, start, length);
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		//super.startElement(uri, localName, qName, attributes);
		
		boolean isElemNameMatched = false;
		
		if(elemNameToParse.equalsIgnoreCase(localName)){
			isElemNameMatched = true;
		}
		boolean isAttrMatched = false;
		if(attributes!=null && isElemNameMatched){
			for(int i=0;i<attributes.getLength();i++){
				if("chapnbr".equalsIgnoreCase(attributes.getLocalName(i)))
					this.chapnbr = attributes.getValue(i);
				else if("sectnbr".equalsIgnoreCase(attributes.getLocalName(i)))
					this.sectnbr = attributes.getValue(i);
				else if("subjnbr".equalsIgnoreCase(attributes.getLocalName(i)))
					this.subjnbr = attributes.getValue(i);
				else if("pgblknbr".equalsIgnoreCase(attributes.getLocalName(i)))
					this.pgblknbr = attributes.getValue(i);
				else if("func".equalsIgnoreCase(attributes.getLocalName(i)))
					this.func = attributes.getValue(i);
				else if("seq".equalsIgnoreCase(attributes.getLocalName(i)))
					this.seq = attributes.getValue(i);
				else if("key".equalsIgnoreCase(attributes.getLocalName(i)))
					this.key = attributes.getValue(i);
				
				////
				// full xml 변환 시 AMM 최상위 엘레멘트에 있는 어트리뷰트를 파싱하여 저장한다.
				if(elemNameToParse.equalsIgnoreCase("AMM")){
					this.ammAttrNameValueMap.put(attributes.getLocalName(i), attributes.getValue(i));
				}
			}
		}
		if(isElemNameMatched /*&& isAttrMatched*/){
			innerXmlFlag = true;
			if(depth==0){
				// matching되는 엘레멘트를 찾았고 output파일 정보가 지정되어 있으면 file 
				// outputstream을 생성하여 flush하게끔 한다. 
				try {
					initFileStream();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			depth++;
		}
		if(innerXmlFlag && text!=null){
			emit(LINE_SEPARATOR+currIndent+"<"+localName);
			if(attributes!=null)
			for(int i=0;i<attributes.getLength();i++){
				if("GNBR".equalsIgnoreCase(attributes.getLocalName(i))){
					// notation에 선언된 cgm entity와 같다면.cgm 확장자를 붙여서 처리한다.
					if(this._dtdHandler.getNotationMap()!=null && this._dtdHandler.getUnParsedEntityMap().containsKey(attributes.getValue(i))){
						emit(" "+attributes.getLocalName(i)+"=\""+convertXmlAttrUnusedChars(attributes.getValue(i))+".cgm\"");
					}else{
						emit(" "+attributes.getLocalName(i)+"=\""+convertXmlAttrUnusedChars(attributes.getValue(i))+"\"");
					}
				}else{
					emit(" "+attributes.getLocalName(i)+"=\""+convertXmlAttrUnusedChars(attributes.getValue(i))+"\"");
				}
			}
			emit(">");
			currIndent += INDENT;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//super.endElement(uri, localName, qName);
		if(innerXmlFlag && text!=null){
			currIndent = currIndent.substring(0, currIndent.length()-INDENT.length());
			if(isCurrElemHasText)
				emit("</"+localName+">");
			else
				emit(LINE_SEPARATOR+currIndent+"</"+localName+">");
			isCurrElemHasText = false;
		}
		// extract close.
		if(innerXmlFlag && elemNameToParse.equalsIgnoreCase(localName)){
			depth--;
			if(depth==0){
				writeCnt++;
				System.out.println("write to " + writeCnt + "th " + outputFileName + " is completed.");
				currIndent = "";
				innerXmlFlag = false;
				chapnbr = "";
				sectnbr = "";
				subjnbr = "";
				pgblknbr = "";
				func = "";
				seq = "";
				key = "";
				outputFileName = "";
				if(this.writer!=null)try{writer.close();}catch(Exception e){}
				if(this.outputStream!=null)try{outputStream.close();}catch(Exception e){}
				
				// xml을 파일로 저장한다.
//				try {
//					String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
//					FileUtils.writeStringToFile(new File("d:/data/kal/KAL_sample/B777/SGML/task_sample_BAE34CAC60023ABA2B16ADB5D7CE59C4.xml"), xmlHeader+text, "utf-8");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		}
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		//super.characters(ch, start, length);
		if (text != null && innerXmlFlag) {
			//ch = convertXmlUnusedChars(ch);
			emit(ch, start, length);
			isCurrElemHasText = true;
		}
	}
	
	/**
	 * xml에서 인식하지 못하는 어트리뷰트 내 값의 특수 기호들을 변환한다.
	 * @param (String)
	 * @return
	 */
	private String convertXmlAttrUnusedChars(String attrValue){
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

	public StringBuilder getText() {
		return text;
	}

	public void setText(StringBuilder text) {
		this.text = text;
	}
	
	public HashMap<String, String> getAmmAttrNameValueMap() {
		return ammAttrNameValueMap;
	}

	public void setAmmAttrNameValueMap(HashMap<String, String> ammAttrNameValueMap) {
		this.ammAttrNameValueMap = ammAttrNameValueMap;
	}

	private void initFileStream() throws Exception{
		if(this.outputPath!=null && !"".equals(this.outputPath)){
			String _outputFileName = this.outputPath + FILE_SEPARATOR + xmlPrefix;
			if(this.chapnbr!=null && !"".equals(this.chapnbr))
				_outputFileName += ("_"+this.chapnbr);
			if(this.sectnbr!=null && !"".equals(this.sectnbr))
				_outputFileName += ("_"+this.sectnbr);
			if(this.subjnbr!=null && !"".equals(this.subjnbr))
				_outputFileName += ("_"+this.subjnbr);
			if(this.pgblknbr!=null && !"".equals(this.pgblknbr))
				_outputFileName += ("_"+this.pgblknbr);
			if(this.func!=null && !"".equals(this.func))
				_outputFileName += ("_"+this.func);
			if(this.seq!=null && !"".equals(this.seq))
				_outputFileName += ("_"+this.seq);
			if(this.key!=null && !"".equals(this.key))
				_outputFileName += ("_"+this.key);
			_outputFileName += ".xml";
			this.outputFileName = _outputFileName;
			File outputFile = new File(_outputFileName);
			File _outputPath = new File(outputPath);
			if(!_outputPath.exists())_outputPath.mkdirs();
			this.outputStream = new FileOutputStream(outputFile);
			this.writer = new OutputStreamWriter(outputStream, "UTF-8");
			String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
			emit(xmlHeader);
		}
	}
	
}
