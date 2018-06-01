package com.kal.cms.task.sgml;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;

/*import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;*/

/**
 * SgmlProcess jBPM 호출 클래스. 웹 어플리케이션에서 이 클래스의 메쏘드를 <br/>
 * 호출하여 Task를 실행시킨다.
 */
public class SgmlProcess {
	
	public static void main(String[] args) throws Exception{
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		// B777
		//String basePath = "D:\\data\\kal\\KAL_sample\\B777\\";
		
		// B777 FRMFIM
		//String basePath = "C:\\SGML\\B777\\FRMFIM\\";
		
		// B747-400
		//String basePath = "C:\\SGML\\B747-400\\AMM\\";
		
		// B747-400 FRMFIM
		//String basePath = "C:\\SGML\\B747-400\\FRMFIM\\";
		
		// B747-8
		//String basePath = "C:\\SGML\\B747-8\\AMM\\";
		
		// B737
//		String basePath = "C:\\SGML\\B737\\AMM\\";
		
		// B737 FRMFIM
//		String basePath = "C:\\SGML\\B737\\FRMFIM\\";
		
		// A380
		//String basePath = "C:\\SGML\\A380\\AMM\\";
		
		// A380 TSM
		//String basePath = "C:\\SGML\\A380\\TSM\\";
		
		// A330 TSM
		//String basePath = "C:\\SGML\\A330\\TSM\\";
		
		// B747-8 TR
		String basePath = "C:\\SGML\\B747-8\\TR\\";
		
		String destFile = basePath + "SGML\\TEMP_AMM.xml";
		String cgmDTDPath = basePath+"SGML\\AMM_CGM.DTD";
		String sgmFilePath = basePath + "SGML\\AMM.SGM";
		paramMap.put("destFile", destFile);
		paramMap.put("basePath", basePath);
		paramMap.put("cgmDTDPath", cgmDTDPath);
		paramMap.put("sgmFilePath", sgmFilePath);
		paramMap.put("srcCgmPath", basePath + "CGM");
		paramMap.put("destPngPath", "/kalppl/hq/manual/oem/b747-8/amm/temp/png/");
		
		paramMap.put("cgmBatchFilePath", "C:\\cgm_run.bat");
		paramMap.put("batchFilePath", "C:\\sx_run.bat");
		paramMap.put("sxBinPath", "D:\\data\\kal\\KAL_sample\\sgml\\sx.exe");
		paramMap.put("destBasePath", "c:/kalppl/hq/manual/oem/b747-8/amm/");
		paramMap.put("revNum", "1");
		paramMap.put("model", "B747-8");
		paramMap.put("manualType", "TR");
		
		paramMap.put("userId", "8204151");
		paramMap.put("fullXmlRootElemName", "amm");
		paramMap.put("fullXmlFilePrefix", "AMM");
		paramMap.put("burstXmlElemName", "task");
		paramMap.put("burstXmlPrefix", "B747-8_AMM");
		paramMap.put("burstGraphicXmlElemName", "graphic");
		paramMap.put("burstGraphicXmlPrefix", "GRAPHIC");
		paramMap.put("retryCnt", 3);
		paramMap.put("cgmCurrentIdx", "0");
		paramMap.put("taskId", "1");
		paramMap.put("_dao", null);
		
		SgmlProcess sp = new SgmlProcess();
		sp.doProcess(paramMap, null);
		System.out.println("Done.");
	}
	
	/**
	 * SGML to XML 변환 호출 클래스.
	 * @param Map
	 * @throws Exception
	 */
	public void doProcess(HashMap<String, Object> sgmlParamMap, Object _dao) throws Exception{
		// SGML 정보 등록 UI에서 전달된 리소스 경로 정보를 인자로 받아 변환 플로우를 진행시킨다.
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		// 이벤트 리스너 등록.
		ksession.addEventListener(new SgmlProcessEventListener(_dao, ""+sgmlParamMap.get("taskId")));

		
		// Windows 시스템인 경우 batch 파일을 실행하고 
		// Unix 게열일 경우 sx를 바로 실행시킨다.
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("destPath", sgmlParamMap.get("destFile"));
		paramMap.put("basePath", sgmlParamMap.get("basePath"));
		paramMap.put("cgmDTDPath", sgmlParamMap.get("cgmDTDPath"));
		paramMap.put("sgmFilePath", sgmlParamMap.get("sgmFilePath"));
		paramMap.put("srcCgmPath", sgmlParamMap.get("srcCgmPath"));
		paramMap.put("destPngPath", sgmlParamMap.get("destPngPath"));
		paramMap.put("batchFilePath", sgmlParamMap.get("batchFilePath"));
		paramMap.put("cgmBatchFilePath", sgmlParamMap.get("cgmBatchFilePath"));
		paramMap.put("sxBinPath", sgmlParamMap.get("sxBinPath"));
		paramMap.put("destBasePath", sgmlParamMap.get("destBasePath"));
		paramMap.put("revNum", sgmlParamMap.get("revNum"));
		paramMap.put("model", sgmlParamMap.get("model"));
		paramMap.put("manualType", sgmlParamMap.get("manualType"));
		paramMap.put("userId", sgmlParamMap.get("userId"));
		paramMap.put("fullXmlRootElemName", sgmlParamMap.get("fullXmlRootElemName"));
		paramMap.put("fullXmlFilePrefix", sgmlParamMap.get("fullXmlFilePrefix"));
		paramMap.put("burstXmlElemName", sgmlParamMap.get("burstXmlElemName"));
		paramMap.put("burstXmlPrefix", sgmlParamMap.get("burstXmlPrefix"));
		paramMap.put("burstGraphicXmlElemName", sgmlParamMap.get("burstGraphicXmlElemName"));
		paramMap.put("burstGraphicXmlPrefix", sgmlParamMap.get("burstGraphicXmlPrefix"));
		paramMap.put("taskId", sgmlParamMap.get("taskId"));
		paramMap.put("cgmCurrentIdx", "0");
		paramMap.put("retryCnt", 3);
		paramMap.put("_dao", _dao);
		paramMap.put("threadCnt", 5);
		// start a new process instance
		//ksession.startProcess("com.kal.sgml.SgmlProcess", paramMap);
		
		// 비동기로 호출한다.
		doAsync(ksession, paramMap);
	}
	
	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("com.kal.sgml.SgmlProcess.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}
	
	public void doAsync(StatefulKnowledgeSession ksession, Map<String, Object> paramMap){
		ExecutorService threadPool = Executors.newFixedThreadPool(1);
		CompletionService<Object> completionService = new ExecutorCompletionService<Object>(threadPool);
		completionService.submit(new SgmlAsyncTask(ksession, paramMap));
		threadPool.shutdown();
	}
	
	class SgmlAsyncTask implements Callable<Object>{
		private StatefulKnowledgeSession ksession;
		private Map paramMap;
		
		public SgmlAsyncTask(StatefulKnowledgeSession ksession, Map<String, Object> paramMap){
			this.ksession = ksession;
			this.paramMap = paramMap;
		}
		
		@Override
		public Object call() throws Exception {
			
	        //Each Command will generate an interaction
	        System.out.println(" >>> Create Process Instance");
	        ProcessInstance processInstance = ksession.createProcessInstance("com.kal.sgml.SgmlProcess", paramMap);
	        System.out.println(" >>> Start the Process Instance processId:"+processInstance.getId());
	        try{
	        	ksession.startProcessInstance(processInstance.getId());
	        }catch(Exception e){
	        	e.printStackTrace();
	        }finally{
	        	if(this.ksession!=null){
	        		this.ksession.dispose();
	        	}
	        }
			return null;
		}
	}
}
