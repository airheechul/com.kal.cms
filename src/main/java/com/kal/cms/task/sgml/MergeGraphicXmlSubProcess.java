package com.kal.cms.task.sgml;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

import com.kal.cms.task.util.XmlXpathUtil;

/**
 * Airbus 타입 문서일 경우 GRAPHIC merge 서브 프로세스.
 */
public class MergeGraphicXmlSubProcess {
	private static String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final MergeGraphicXmlSubProcess INSTANCE = new MergeGraphicXmlSubProcess();
	
	public static MergeGraphicXmlSubProcess getInstance(){
		return INSTANCE;
	}
	
	/**
	 * 프로세스 수행.
	 * @param burstedXmlFolder
	 * @param burstedGraphicFolder
	 * @param _dao
	 * @param kcontext
	 */
	public void doMergeGraphicXmlProcess(String burstedXmlFolder, final String burstXmlPrefix, String burstedGraphicFolder, final String burstGraphicXmlPrefix, Object kcontext){
		///////
		// burst된 xml 목록을 읽어와 파싱을 수행하면서 컨텐츠에 GRPHCREF REFID 항목이 있을 경우 
		// 매칭되는 burst된 graphic xml을 읽어와 replace를 수행.
		
		try{
			String procMsg = "[MergeGraphicXmlSubProcess] burstedXmlFolder:"+burstedXmlFolder+", burstXmlPrefix:"+burstXmlPrefix+", burstedGraphicFolder:"+burstedGraphicFolder
				+", burstGraphicXmlPrefix:"+burstGraphicXmlPrefix+"\\r\\n";
			File _burstedXmlFolder = new File(burstedXmlFolder);
			File _burstedGraphicFolder = new File(burstedGraphicFolder);
			if(!_burstedXmlFolder.isDirectory() || !_burstedGraphicFolder.isDirectory())
				throw new Exception("Invalid folder path. Bursted xml folder: "+_burstedXmlFolder.getAbsolutePath()+", bursted graphic folder: "+_burstedGraphicFolder.getAbsolutePath());
			
			// full xml이 아닌 burst xml 목록을 조회.
			File[] burstedXmlFileArray = _burstedXmlFolder.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					boolean rt = false;
					if(name.startsWith(burstXmlPrefix) && name.toLowerCase().endsWith(".xml"))
						rt = true;
					return rt;
				}
			});
			File[] burstedGraphicFileArray = _burstedGraphicFolder.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					boolean rt = false;
					if(name.startsWith(burstGraphicXmlPrefix) && name.toLowerCase().endsWith(".xml"))
						rt = true;
					return rt;
				}
			});
			
			if(burstedXmlFileArray!=null && burstedXmlFileArray.length>0){
				if(burstedGraphicFileArray!=null && burstedGraphicFileArray.length>0){
					int excuteCount = 0;
					for(int i=0;i<burstedXmlFileArray.length;i++){
						File burstedXmlFile = burstedXmlFileArray[i];
						List<String> graphicRefIdList = parseXmlAndGetGraphicRefIdList(burstedXmlFile);
						if(graphicRefIdList!=null && graphicRefIdList.size()>0){
							HashMap<String, String> refIdGraphicXmlMap = getMatchedGraphicXmlMap(graphicRefIdList, burstedGraphicFileArray);
							mergeGraphicElementAndWriteFile(burstedXmlFile, refIdGraphicXmlMap);
							excuteCount++;
							System.out.println("[MergeGraphicXmlSubProcess] "+excuteCount+"th Merge GRPHCREF element in "+burstedXmlFile.getAbsolutePath()+" is completed.");
						}
					}
					procMsg += "[MergeGraphicXmlSubProcess] excuteCount:"+excuteCount+"\\r\\n";
				}
			}
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", true);
				((ProcessContext)kcontext).setVariable("procMsg", procMsg);
			}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Merge GRPHCREF element Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * burst 된 xml에서 GRPHCREF/REFID 어트리뷰트의 값을 파싱하여 목록을 리턴.
	 * @param burstedXmlFile
	 * @return
	 * @throws Exception
	 */
	public List<String> parseXmlAndGetGraphicRefIdList(File burstedXmlFile) throws Exception{
		List<String> graphicRefIdList = new ArrayList<String>();
		String xmlStr = readXmlString(burstedXmlFile);
		XmlXpathUtil xpathXmlUtil = new XmlXpathUtil();
		graphicRefIdList = xpathXmlUtil.getAttrValuesByElemNameAndAttrName(xmlStr, "GRPHCREF", "REFID");
		return graphicRefIdList;
	}
	
	/**
	 * burst된 graphic xml 파일명을 기준으로 매칭되는 파일을 찾아서 컨텐츠를 맵 형태로 리턴.
	 * @param graphicRefIdList
	 * @param burstedGraphicFileArray
	 * @return
	 * @throws Exception 
	 */
	public HashMap<String, String> getMatchedGraphicXmlMap(List<String> graphicRefIdList, File[] burstedGraphicFileArray) throws Exception{
		HashMap<String, String> refIdGraphicXmlMap = new HashMap<String, String>();
		// GRAPHIC 엘레멘트의 KEY 어트리뷰트에 맵핑되며, 그 값은 graphic xml burst시 파일명 가장 마지막에 붙음.
		for(int i=0;i<graphicRefIdList.size();i++){
			String refId = graphicRefIdList.get(i);
			if(refId!=null && !"".equals(refId)){
				for(int j=0;j<burstedGraphicFileArray.length;j++){
					String graphicXmlFileName = burstedGraphicFileArray[j].getName();
					if(graphicXmlFileName!=null && graphicXmlFileName.endsWith(refId+".xml")){
						String matchedGraphicXmlStr = readXmlString(burstedGraphicFileArray[j]);
						if(matchedGraphicXmlStr!=null && !"".equals(matchedGraphicXmlStr)){
							refIdGraphicXmlMap.put(refId, matchedGraphicXmlStr);
							// 매칭되는 graphic 엘레멘트는 하나만 있다고 가정한다.
							break;
						}
					}
				}
			}
		}
		return refIdGraphicXmlMap;
	}
	
	/**
	 * burst된 xml 파일의 GRPHCREF 엘레멘트를 replace하고 파일로 쓴다. 
	 * @param burstedXmlFile
	 * @param refIdGraphicXmlMap
	 * @throws Exception
	 */
	public void mergeGraphicElementAndWriteFile(File burstedXmlFile, HashMap<String, String> refIdGraphicXmlMap) throws Exception{
		String xmlStr = readXmlString(burstedXmlFile);
		String replacedXmlStr = "";
		Iterator entries = refIdGraphicXmlMap.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry<String, ?> hmEntry = (Map.Entry)entries.next();

			if(hmEntry.getKey()!=null && !"".equals(hmEntry.getKey())){
				String refId = hmEntry.getKey();
				String graphicXml = refIdGraphicXmlMap.get(refId);
				XmlXpathUtil xpathXmlUtil = new XmlXpathUtil();
				if(replacedXmlStr!=null && !"".equals(replacedXmlStr))
					replacedXmlStr = xpathXmlUtil.replaceElementByElemNameAndAttrNameValue(replacedXmlStr, graphicXml, "GRPHCREF", "REFID", refId);
				else
					replacedXmlStr = xpathXmlUtil.replaceElementByElemNameAndAttrNameValue(xmlStr, graphicXml, "GRPHCREF", "REFID", refId);
			}
		}
		
		if(replacedXmlStr!=null && !"".equals(replacedXmlStr)){
			FileUtils.writeStringToFile(burstedXmlFile, replacedXmlStr, "UTF-8");
		}
	}
	
	/**
	 * xml 파일을 읽어서 스트링 리턴.
	 * @param _file
	 * @return (String)
	 * @throws IOException
	 */
	public String readXmlString(File _file) throws Exception{
		String xmlStr = "";
		xmlStr = FileUtils.readFileToString(_file, "UTF-8");
		return xmlStr;
	}
	
}
