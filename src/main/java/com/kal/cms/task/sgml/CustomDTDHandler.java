package com.kal.cms.task.sgml;

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
		//System.out.println("notationDecl. name:"+name+", publicId:"+publicId+", systemId:"+systemId);
		
		notationMap.put(name, systemId);
	}

	@Override
	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		//System.out.println("unparsedEntityDecl. name:"+name+", publicId:"+publicId+", systemId:"+systemId
		//		+", notationName:"+notationName);
		//name:G00C06911F5712D181FB9FB592E181C3, publicId:null, systemId:file://D/data/kal/KAL_sample/B777/SGML/CGM/G00C06911F5712D181FB9FB592E181C3.cgm, notationName:CGM
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
