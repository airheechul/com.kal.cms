package com.kal.cms.task.sgml;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * cgm 그래픽 이미지 엔티티에 대한 처리 클래스.
 */
public class CustomEntityResolver implements EntityResolver{

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		//if(publicId!=null && publicId.indexOf("HCC2E5DC25B34C926D3E7D7EEB39C8DF")!=-1
		//		|| systemId!=null && systemId.indexOf("HCC2E5DC25B34C926D3E7D7EEB39C8DF")!=-1)
			System.out.println("Resolving Entity. publicId:"+publicId+", systemId:"+systemId);
		return null;
	}

}
