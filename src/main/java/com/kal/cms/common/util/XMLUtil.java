package com.kal.cms.common.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtil {
	Log logger = LogFactory.getLog(this.getClass());

	private SAXBuilder builder;
	private Document document;
	private Element rootNode;

	public XMLUtil(String fileUrl) {
		loadXMLFile(fileUrl);
	}

	public void loadXMLFile(String fileUrl) {
		try {
			builder = new SAXBuilder();
			document = builder.build(new URL(fileUrl));
			rootNode = document.getRootElement();
		} catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex);
		}
	}
	
	public XMLUtil(File file) throws Exception{
		loadXMLFile(file);
	}
	
	public XMLUtil(StringReader docRead) {
		loadXMLFile(docRead);
	}	

	public void loadXMLFile(File file) throws Exception{
		try {
			builder = new SAXBuilder();
			disableDtdValidation(builder);
			document = builder.build(file);
			rootNode = document.getRootElement();
		} catch(Exception ex) {
			logger.error(ex);
			
			throw ex;
		}
	}
	
	public void loadXMLFile(StringReader docRead) {
		try {
			builder = new SAXBuilder();
			disableDtdValidation(builder);
			document = builder.build(docRead);
			rootNode = document.getRootElement();
			
		} catch(Exception ex) {
			logger.error(ex);
		}
	}	

	private static void disableDtdValidation(SAXBuilder db) {
		// disable the dtd validation by setting a custom entity resolver
		db.setEntityResolver((new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId)
					throws SAXException, IOException {
				return new InputSource(new StringReader(""));
			}
		}));
	}

	public Element getRoot() {
		return rootNode;
	}

	public String getChildValue(String nodeName, Element parentNode) {
		Element node = getChild(nodeName, parentNode);
		if (null == node) {
			return null;
		} else {
			return node.getValue();
		}
	}

	public String getChildValue(String nodeName) {
		return getChildValue(nodeName, rootNode);
	}

	public Element getChild(String nodeName, Element parentNode) {
		List<Element> elements = parentNode.getChildren();
		for (Element element : elements) {
			if (nodeName.equals(element.getName())) {
				return element;
			}
		}
		return null;

	}
	
	public List<Element> getChilds(String nodeName, Element parentNode) {
		List<Element> elements = parentNode.getChildren();
		
		List<Element> matchElements =  new ArrayList<Element>();
		for (Element element : elements) {
			if (nodeName.equals(element.getName())) {
				matchElements.add(element);
			}
		}
		return matchElements;
	}	

	public Element getChild(String nodeName) {
		return getChild(nodeName, rootNode);
	}
	
	public String getTitleValue() {
		String retStr = "";
		
		retStr = getChildValue("TITLE");
		if ( Util.isNotNVL(retStr) ) {
			return retStr;
		}
		Element childElem = getChild("TASK");
		if(childElem == null)
			childElem = getChild("SUBTASK");
		
		if(childElem == null)
			retStr = "";
		else {
			retStr = getChildValue("TITLE", childElem);
			if ( Util.isNotNVL(retStr) ) {
				return retStr;
	
			}
		}
		
		return retStr;
	}
	
	public String getKeyAttrValue() {
		String retStr = "";
		
		retStr = rootNode.getAttributeValue("KEY");
		if ( Util.isNotNVL(retStr) ) {
			return retStr;
		}
		Element childElem = getChild("TASK");
		if(childElem==null)childElem = getChild("SUBTASK");
		
		retStr = childElem.getAttributeValue("KEY");
		if ( Util.isNotNVL(retStr) ) {
			return retStr;	
		}
		
		return retStr;
	}

	/**
	 * 특정 노드에서 이름이 매칭되는 하위 노드 리스트를 찾을때까지 재귀호출하여 찾는다.
	 * 이름이 매칭되는 노드를 찾으면 더이상 하위 노드를 검색하지 않고 리턴.
	 * 단! 부모 노드가 여러개인 경우 첫번째 부모노드에서만 검색하고 리턴하므로 주의.
	 * @param nodeName
	 * @param parentNode
	 * @return
	 */
	public Element getFirstMatchElement(String nodeName, Element parentNode){
		List<Element> elements = parentNode.getChildren();
		
		if(elements.isEmpty())
			return null;
		
		Element matchElement;
		for (Element element : elements) {
			if (nodeName.equals(element.getName())) {
				matchElement = element;
				
				return matchElement;
			}
		}
		
		// 매핑 되는 값을 찾지 못하면 하위노드에서 계속 검색
		for (Element element : elements) {
			Element childElement = getFirstMatchElement(nodeName, element);
			
			if(childElement != null){
				return childElement;
			}
		}

		return null;
	}
}
