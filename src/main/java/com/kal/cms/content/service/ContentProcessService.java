package com.kal.cms.content.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.kal.cms.content.vo.ContentInfo;
import com.kal.cms.content.vo.ContentTaskInfo;

public interface ContentProcessService {

    public List<ContentInfo> findContentProcessList(HashMap<String, Object> pMap);
    public List<ContentInfo> findContentDownloadList(HashMap<String, Object> pMap);
    public List<ContentInfo> findContentImageList(HashMap<String, Object> pMap);
    public boolean processContentFile(File file , HashMap<String, Object> pMap);
    public int registTaskInfo(HashMap<String, Object> pMap) throws Exception;
    public void updateTask(HashMap<String, Object> pMap);
    public int registTaskDetail(HashMap<String, Object> pMap) throws Exception;
    public void updateTaskDetail(HashMap<String, Object> pMap);
    public String compressContents(HashMap<String, Object> pMap);
    public String registDownloadInfo(HashMap<String, Object> pMap);
    public List<ContentTaskInfo> findReportList(HashMap<String, Object> pMap);
    public ContentTaskInfo createReport(HashMap<String, Object> pMap) throws Exception;
    public boolean execCheckIn(HashMap<String, Object> pMap) throws Exception;
    public int insertTaskDefaultInfo(File file, HashMap<String, Object> pMap);
    
}
