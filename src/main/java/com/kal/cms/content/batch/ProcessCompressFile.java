package com.kal.cms.content.batch;

import java.util.HashMap;

import com.kal.cms.common.util.ServiceFactory;


public class ProcessCompressFile extends Thread {
	private HashMap<String, Object> map;


	public ProcessCompressFile(HashMap<String, Object> map) { 
		this.map = map;
	}

	@Override
	public void run() {
		System.out.println(this.getName() +  "Start" + "\n");
		
		try {
			
			String filePath = ServiceFactory.getContentProcessService().compressContents(map);

			System.out.println("처리완료 ...");
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
