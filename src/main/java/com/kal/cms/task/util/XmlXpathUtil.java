package com.kal.cms.task.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * xPath를 이용한 XML 파싱 유틸 클래스.
 */
public class XmlXpathUtil {
	
	private static final String UTF8_BOM = "\uFEFF";
	
	private static final String FEATURE1 = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
	private static final String FEATURE2 = "http://xml.org/sax/features/external-general-entities";
	private static final String FEATURE3 = "http://xml.org/sax/features/external-parameter-entities";
	private static final String FEATURE4 = "http://xml.org/sax/features/validation";

	/**
	 * 주어진 xpath 표현식으로 매칭되는 xml 노드리스트를 찾아 리턴함.
	 * @param dataXml
	 * @param xPathExprStr
	 * @return NodeList
	 */
	public NodeList getXpathMatchedNodes(String dataXml, String xPathExprStr){
		StringReader stringReader = null;
		StringWriter stringWriter = null;
		NodeList nodeList = null;
		try{
			stringWriter = new StringWriter();
			String escapedDataXml = removeBOMCharacter(dataXml);
			stringReader = new StringReader(escapedDataXml);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature(FEATURE1, false);
			dbf.setFeature(FEATURE2, false);
			dbf.setFeature(FEATURE3, false);
			dbf.setFeature(FEATURE4, false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new InputSource(stringReader));
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression xPathExpr = xPath.compile(xPathExprStr);
			nodeList = (NodeList)xPathExpr.evaluate(document, XPathConstants.NODESET);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(stringWriter!=null)try{stringWriter.close();}catch(Exception e){}
			if(stringReader!=null)stringReader.close();
		}
		return nodeList;
	}
	
	/**
	 * 주어진 xml에 대한 Document 객체를 생성하여 리턴
	 * @param dataXml
	 * @return Document
	 */
	public Document getDataXmlDocument(String dataXml){
		Document _document = null;
		StringReader stringReader = null;
		try{
			String escapedDataXml = removeBOMCharacter(dataXml);
			stringReader = new StringReader(escapedDataXml);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature(FEATURE1, false);
			dbf.setFeature(FEATURE2, false);
			dbf.setFeature(FEATURE3, false);
			dbf.setFeature(FEATURE4, false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			_document = db.parse(new InputSource(stringReader));
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(stringReader!=null)stringReader.close();
		}
		return _document;
	}
	
	/**
	 * 주어진 엘레멘트명에 매칭되는 모든 xml 엘레멘트를 찾아 텍스트 value를 리스트에 <br/>
	 * 담아 리턴한다.
	 * @param dataXml
	 * @param elemName
	 * @return List&lt;String&gt;
	 */
	public List<String> getElemValuesByElemName(String dataXml, String elemName){
		String xPathExprStr = "//"+elemName;
		List<String> elemValueList = new ArrayList<String>();
		NodeList nodeList = getXpathMatchedNodes(dataXml, xPathExprStr);
		if(nodeList!=null){
			for(int i=0;i<nodeList.getLength();i++){
				String nodeText = nodeList.item(i).getTextContent();
				elemValueList.add(nodeText);
			}
		}
		
		return elemValueList;
	}
	
	/**
	 * 주어진 elemName 엘레멘트의 AttrName의 어트리뷰트 값을 찾아 리턴.
	 * @param dataXml
	 * @param elemName
	 * @param attrName
	 * @return List&lt;String&gt;
	 */
	public List<String> getAttrValuesByElemNameAndAttrName(String dataXml, String elemName, String attrName){
		List<String> attrValueList = new ArrayList<String>();
		String xPathExprStr = "//"+elemName+"/@"+attrName;
		NodeList nodeList = getXpathMatchedNodes(dataXml, xPathExprStr);
		if(nodeList!=null){
			for(int i=0;i<nodeList.getLength();i++){
				String nodeText = nodeList.item(i).getTextContent();
				attrValueList.add(nodeText);
			}
		}
		return attrValueList;
	}
	
	/**
	 * 주어진 엘레멘트 명과 어트리뷰트 명, 값에 매칭되는 엘레멘트를 replace한다.
	 * @param dataXml
	 * @param replaceElemXml
	 * @param elemName
	 * @param attrName
	 * @param attrVal
	 * @return (String)
	 */
	public String replaceElementByElemNameAndAttrNameValue(String dataXml, String replaceElemXml, String elemName, String attrName, String attrVal){
		String replacedXml = "";
		String xPathExprStr = "//"+elemName+"[@"+attrName+"='"+attrVal+"']";
		Document document = getDataXmlDocument(dataXml);
		StringWriter stringWriter = null;
		StreamResult streamResult = null;
		try{
			TransformerFactory tff = TransformerFactory.newInstance();
			stringWriter = new StringWriter();
			streamResult = new StreamResult(stringWriter);
			Transformer tf = tff.newTransformer();
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			XPathExpression xPathExpr = xPath.compile(xPathExprStr);
			NodeList nodeList = (NodeList)xPathExpr.evaluate(document, XPathConstants.NODESET);
			if(nodeList!=null){
				for(int i=0;i<nodeList.getLength();i++){
					Element matchedElem = (Element)nodeList.item(i);
					Node parentNode = matchedElem.getParentNode();
					Node replaceNode = document.importNode(getDataXmlDocument(replaceElemXml).getDocumentElement(), true);
					parentNode.replaceChild(replaceNode, matchedElem);
				}
			}
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			tf.transform(source, streamResult);
			replacedXml = streamResult.getWriter().toString();
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			if(stringWriter!=null)try{stringWriter.close();}catch(Exception e){}
		}
		return replacedXml;
	}
		
	/**
	 * UTF-8 문서의 첫부분에 기록되는 BOM 캐릭터를 제거한다.
	 * @param str
	 * @return
	 */
	private String removeBOMCharacter(String str){
		try{
			if(str.startsWith(UTF8_BOM))
				str = str.substring(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * xml 파일 체크
	 * @param dataXml
	 * @return
	 */
	public boolean checkSaxParseCheck(String dataXml){
		StringReader stringReader = null;
		try{
			String escapedDataXml = removeBOMCharacter(dataXml);
			stringReader = new StringReader(escapedDataXml);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setFeature(FEATURE1, false);
			dbf.setFeature(FEATURE2, false);
			dbf.setFeature(FEATURE3, false);
			dbf.setFeature(FEATURE4, false);
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.parse(new InputSource(stringReader));
		}catch(SAXParseException saxpe){
			return false;
		}catch(ParserConfigurationException pce){
		}catch(SAXException saxe){
		}catch(IOException ioe){}
		return true;
	}
	
	public static void main(String[] args){
		XmlXpathUtil xxUtil = new XmlXpathUtil();
		try {
			String xmlData = FileUtils.readFileToString(new File("C:/B777_AMM_05_41_01_2_210_805_EDE1C4CAD06E194D759B520D576380C1.xml"), "UTF-8");
			List<String> attrValueList = xxUtil.getAttrValuesByElemNameAndAttrName(xmlData, "SHEET", "GNBR");
			if(attrValueList!=null){
				for(int i=0;i<attrValueList.size();i++){
					System.out.println("GNBR ATTR value: "+attrValueList.get(i));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done.");
	}
}
