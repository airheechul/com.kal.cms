package com.kal.cms.content.mapper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kal.cms.content.vo.ContentInfo;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("contentSearchMapper")
public class ContentSearchMapper extends EgovAbstractMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentSearchMapper.class);

	public List<ContentInfo> getContentSearchList(HashMap<String, Object> pMap) {
		
    	Iterator iter = pMap.entrySet().iterator();
    	while(iter.hasNext()) {
    	    Entry<String, Object[]> entry =  (Entry<String, Object[]>) iter.next();
    	    String key = entry.getKey();
    	    Object value = entry.getValue();
    	    
    	    LOGGER.debug("key / value => " + key + "/" + value);
    	}
		
		return selectList("com.kal.cms.content.mapper.ContentMapper.getContentPageList", pMap);
	}
	
	public List<ContentInfo> getContentRevisionList(HashMap<String, Object> pMap) {
		return selectList("com.kal.cms.content.mapper.ContentMapper.getRevisionList", pMap);
	}
	
	
	public ContentInfo getContentNextRevisionSeq(HashMap<String, Object> pMap){
		ContentInfo revInfo = (ContentInfo)selectOne("com.kal.cms.content.mapper.ContentMapper.getNextRevisionSeq", pMap);
		if(revInfo == null) {
			revInfo = new ContentInfo();
			revInfo.setReleaseYn("N");
			revInfo.setMasterWOCntsRevzNo("0");
			revInfo.setMasterWOCntsDocRevzNo("0");
		}
		
		return revInfo;
	}
	
	
	public int addContent(HashMap<String, Object> pMap) {

		int result = -1;
		
		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addContent", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentSearchMapper.addContent Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}

	
	public int addContentKeyword(HashMap<String, Object> pMap) {
		int result = -1;
		
		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addKeyword", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentSearchMapper.addContent Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}

	
	public int addContentMap(HashMap<String, Object> pMap) {
		int result = -1;
		
		try {
			insert("com.kal.cms.content.mapper.ContentMapper.addMap", pMap);
			result = 1;

		} catch (Exception e) {
			LOGGER.debug("ContentSearchMapper.addContent Exception: {}", e);
				e.printStackTrace();
		}

		return result;
	}

	
	public int updateContentRelease(HashMap<String, Object> pMap) {
		return update("com.kal.cms.content.mapper.ContentMapper.updateContentRelease", pMap);
	}
	
	
	public int updateContentDelete(HashMap<String, Object> pMap) {
		
/*    	Iterator iter = pMap.entrySet().iterator();
    	while(iter.hasNext()) {
    	    Entry<String, Object[]> entry =  (Entry<String, Object[]>) iter.next();
    	    String key = entry.getKey();
    	    Object value = entry.getValue();
    	    
    	    LOGGER.debug("key / value => " + key + "/" + value);
    	}*/
		
		return update("com.kal.cms.content.mapper.ContentMapper.updateContentDelete", pMap);
	}

	
	public ContentInfo getRevisionInfo(HashMap<String, Object> pMap){
		return (ContentInfo)selectOne("com.kal.cms.content.mapper.ContentMapper.getRevisionInfo", pMap);
	}

	
	public int updateContentRevision(HashMap<String, Object> pMap) {
		return update("com.kal.cms.content.mapper.ContentMapper.updateRevision", pMap);
	}
	
}
