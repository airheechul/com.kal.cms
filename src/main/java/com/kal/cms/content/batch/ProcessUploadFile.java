package com.kal.cms.content.batch;

import java.io.File;
import java.util.HashMap;


import com.kal.cms.common.util.ServiceFactory;


public class ProcessUploadFile extends Thread {
	private HashMap<String, Object> pMap;
	private File file;

    
	public ProcessUploadFile(File file , HashMap<String, Object> pMap) { 
		this.pMap = pMap;
		this.file = file;
	}

	@Override
	public void run() {
		System.out.println(this.getName() +  "Start" + "\n");
		
		try {
			// 업로드 파일 처리
			boolean fileResult = ServiceFactory.getContentProcessService().processContentFile(file, pMap);
			
			System.out.println("처리완료 ...");
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
