package com.kal.cms.common.util.report;

import java.util.HashMap;

import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

/**
 * CGM에 대한 Notation및 unparsedEntity 처리를 한다.
 * @author Administrator
 *
 */
public class CustomDTDHandler implements DTDHandler{
	private HashMap<String, String> notationMap = new HashMap<String, String>();
	private HashMap<String, String> unParsedEntityMap = new HashMap<String, String>();

	@Override
	public void notationDecl(String name, String publicId, String systemId)
			throws SAXException {
		notationMap.put(name, systemId);
	}

	@Override
	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		unParsedEntityMap.put(name, systemId);
	}
	
	public HashMap<String, String> getNotationMap() {
		return notationMap;
	}

	public void setNotationMap(HashMap<String, String> notationMap) {
		this.notationMap = notationMap;
	}
	
	public HashMap<String, String> getUnParsedEntityMap() {
		return unParsedEntityMap;
	}

	public void setUnParsedEntityMap(HashMap<String, String> unParsedEntityMap) {
		this.unParsedEntityMap = unParsedEntityMap;
	}
}
