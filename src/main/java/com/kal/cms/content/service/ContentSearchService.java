package com.kal.cms.content.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kal.cms.content.vo.ContentInfo;

public interface ContentSearchService {

	
	public List<ContentInfo> findContentSearchList(HashMap<String , Object> pMap) throws Exception;
	
	public ContentInfo getContentInfo(HashMap<String , Object> pMap) throws Exception;
	
	public List<ContentInfo> getContentRevisionList(HashMap<String , Object> pMap) throws Exception;

	//public int findExistCodeCount(HashMap<String , Object> pMap) throws Exception;
	
	public boolean saveContentFile(MultipartFile file, HashMap<String , Object> pMap) throws Exception;
	
	public boolean saveContentFile(File file, HashMap<String , Object> pMap) throws Exception;
	
	public boolean addContentNew(HashMap<String , Object> pMap) throws Exception;
	
	public boolean addContentRevise(HashMap<String , Object> pMap) throws Exception;
	
    public boolean updateContentRelease(HashMap<String , Object> pMap) throws Exception;	
	
    public boolean updateContentsDelete(HashMap<String , Object> pMap) throws Exception;   
    
	public String generateWebDavResource(HashMap<String , Object> pMap) throws Exception;
	
}
