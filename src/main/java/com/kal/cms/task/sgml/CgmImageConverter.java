package com.kal.cms.task.sgml;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
//import org.drools.core.spi.ProcessContext;
import org.drools.spi.ProcessContext;

/**
 * CGM 이미지 포맷 변환 클래스.
 */
public class CgmImageConverter {
	public static Logger logger = Logger.getLogger(CgmImageConverter.class);
	
	private static String fileSeparator = System.getProperty("file.separator")==null?"/":System.getProperty("file.separator");
	
	private static List<ImageReader> irPoolList = new ArrayList<ImageReader>();
	
	private int convertCnt = 0;
	
	private static final CgmImageConverter INSTANCE = null;//new CgmImageConverter();
	
	public static CgmImageConverter getInstance(){
		ImageIO.setUseCache(false);
		//return INSTANCE;
		return new CgmImageConverter();
	}
	
	/**
	 * CGM 이미지를 PNG 포맷으로 변환.
	 * @param src
	 * @param dest
	 * @throws Exception 
	 */
	public synchronized void convertToPng(String src, String dest) throws Exception{
//		File cgmFile = null;
//		File outFile = null;
//		BufferedImage image = null;
//		FileOutputStream fos = null;
//		FileInputStream fis = null;
//		try{
//			cgmFile = new File(src);
//			fis = new FileInputStream(cgmFile);
//			outFile = new File(dest);
//			fos = new FileOutputStream(outFile);
//			image = ImageIO.read(fis);
//			ImageIO.write(image, "PNG", fos);
//			image.flush();
//		}catch(Exception e){
//			e.printStackTrace();
//			throw e;
//		}finally{
//			image = null;
//			if(fis!=null)fis.close();
//			if(fos!=null)fos.close();
//			outFile = null;
//			cgmFile = null;
//			System.gc();
//		}
		ImageReader imageReader = ImageIO.getImageReadersByFormatName("cgm").next();
		ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("PNG").next();
		ImageInputStream iis = null;
		ImageOutputStream ios = null;
		try{
			File cgmFile = new File(src);
			File outFile = new File(dest);
			BufferedImage image = null;
			iis = ImageIO.createImageInputStream(cgmFile);
			imageReader.reset();
			imageReader.setInput(iis, true, true);
			ImageReadParam irParam = imageReader.getDefaultReadParam();
			image = imageReader.read(0, irParam);
			//ImageWriter imageWriter = ImageIO.getImageWriter(imageReader);
			imageWriter.reset();
			ios = ImageIO.createImageOutputStream(outFile);
			imageWriter.setOutput(ios);
			//ImageWriteParam iwParam = imageWriter.getDefaultWriteParam();
			imageWriter.write(image);
			image.flush();
			image = null;
			outFile = null;
			cgmFile = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			imageReader.dispose();
			imageWriter.dispose();
			imageWriter = null;
			imageReader = null;
			if(ios!=null)try{ios.close();}catch(Exception e){}
			if(iis!=null)try{iis.close();}catch(Exception e){}
//			convertCnt++;
//			if(convertCnt%5000 == 0){
//				///////////
//				// ibm jdk 환경에서 실행 시 memory leak이 발생하므로 
//				// 5000개 변환 시 마다 gc가 동작할 시간을 준다.
//				System.gc();
//				System.out.println("Wait for GC 20 seconds...");
//				Thread.sleep(20000);
//			}
		}
	}
	
	/**
	 * 소스 CGM 폴더 하위의 이미지 리소스를 대상 폴더로 모두 PNG 포맷 변환한다.
	 * @param srcFolder
	 * @param destFolder
	 */
	public void convertAll(String srcFolder, String destFolder, Object kcontext){
		File _destFolder = new File(destFolder);
		File _srcFolder = new File(srcFolder);
		
		try{
			if(!_destFolder.exists()){
				try {
					FileUtils.forceMkdir(_destFolder);
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
			
			if(_srcFolder.isDirectory()){
				File[] cgmFileArray = _srcFolder.listFiles(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(".cgm");
					}
				});
				System.out.println("CGM File total count: "+(cgmFileArray==null?0:cgmFileArray.length));
				/////////////
				// 5000개씩 나누어서 프로세스 재기동.
				int cgmCurrentIdx = 0;
				if(kcontext!=null){
					cgmFileArray = getInitialCgmFileList(cgmFileArray, kcontext, 5000);
					cgmCurrentIdx = getCgmCurrentIdx(kcontext, 5000);
				}
				if(cgmFileArray!=null){
					System.out.println("Current Unit Process CGM File total count: "+cgmFileArray.length);
					this.convertCnt = 0;
					long startTime = System.currentTimeMillis();
					for(int i=0;i<cgmFileArray.length;i++){
						File cgmFile = cgmFileArray[i];
						if("CGM".equalsIgnoreCase(FilenameUtils.getExtension(cgmFile.getName()))){
							/////
							// CGM 폴더 하위의 확장자 .cmp 인 파일은 png로 컨버팅하지 않는다. 
							String cgmFileName = cgmFile.getName();
							String pngFileName = FilenameUtils.removeExtension(cgmFileName)+".png";
							if(destFolder!=null && !destFolder.endsWith(fileSeparator))
								destFolder = destFolder + fileSeparator;
							convertToPng(cgmFile.getAbsolutePath(), destFolder+pngFileName);
							System.out.println("Converting "+(cgmCurrentIdx+i+1)+"th "+cgmFile.getAbsolutePath()+" to "+destFolder+pngFileName+" is completed.");
						}
					}
					System.out.println("Elapsed Time: "+(System.currentTimeMillis()-startTime));
				}
			}else{
				throw new Exception("CGM Src Folder is not valid directory.");
			}
			try{((ProcessContext)kcontext).setVariable("_taskResult", true);}catch(Exception e){}
		}catch(Exception e){
			e.printStackTrace();
			try{
				((ProcessContext)kcontext).setVariable("_taskResult", false);
				((ProcessContext)kcontext).setVariable("errorMsg", "Converting CGM to PNG Error. msg:"+e.getMessage());
			}catch(Exception _e){}
		}
	}
	
	/**
	 * 현재 프로세스의 작업대상 cgm 목록을 5000개 단위로 자른다.
	 * @param cgmFileArray
	 * @param kcontext
	 * @return
	 */
	private File[] getInitialCgmFileList(File[] cgmFileArray, Object kcontext, int unitProcCount){
		File[] destCgmFileList = null;
		File[] cgmFileList = null;
		Object cgmFileListObj = ((ProcessContext)kcontext).getVariable("cgmFileList");
		if(cgmFileListObj!=null)
			cgmFileList = (File[])cgmFileListObj;
		
		if(cgmFileList!=null){
			// 전역 대상 파일 목록이 값이 있으면 그 값에서 5000개를 취하고 
			// 전역 대상 파일 목록을 업데이트.
			if(cgmFileList.length>=unitProcCount){
				destCgmFileList = (File[])ArrayUtils.subarray(cgmFileList, 0, unitProcCount);
				File[] subCgmFileList = (File[])ArrayUtils.subarray(cgmFileList, unitProcCount, cgmFileList.length);
				((ProcessContext)kcontext).setVariable("cgmFileList", subCgmFileList);
			}else{
				destCgmFileList = (File[])ArrayUtils.subarray(cgmFileList, 0, cgmFileList.length);
				File[] subCgmFileList = new File[]{};
				((ProcessContext)kcontext).setVariable("cgmFileList", subCgmFileList);
			}
		}else{
			// 전역 대상 파일 목록 값이 없고 처음 cgm 변환 프로세스가 실행되었다면 
			// 전체 파일 목록에서 5000개를 취하고 전역 대상 파일 목록을 업데이트.
			if(cgmFileArray!=null){
				if(cgmFileArray.length >= unitProcCount){
					destCgmFileList = (File[])ArrayUtils.subarray(cgmFileArray, 0, unitProcCount);
					File[] subCgmFileList = (File[])ArrayUtils.subarray(cgmFileArray, unitProcCount, cgmFileArray.length);
					((ProcessContext)kcontext).setVariable("cgmFileList", subCgmFileList);
				}else{
					destCgmFileList = (File[])ArrayUtils.subarray(cgmFileArray, 0, cgmFileArray.length);
					File[] subCgmFileList = new File[]{};
					((ProcessContext)kcontext).setVariable("cgmFileList", subCgmFileList);
				}
			}else{
				
			}
		}
		return destCgmFileList;
	}
	
	/**
	 * 전체 cgm 변환 프로세스의 현재까지 진행된 인덱스를 전역변수로부터 가져온다.
	 * @param kcontext
	 * @param unitProcCount
	 * @return
	 */
	private int getCgmCurrentIdx(Object kcontext, int unitProcCount){
		int currentIdx = 0;
		Object cgmCurrentIdxObj = ((ProcessContext)kcontext).getVariable("cgmCurrentIdx");
		if(cgmCurrentIdxObj!=null){
			currentIdx = 0;
			try{
				currentIdx = Integer.parseInt((String)cgmCurrentIdxObj);
			}catch(Exception e){
				currentIdx = 0;
			}
		}
		((ProcessContext)kcontext).setVariable("cgmCurrentIdx", ""+(currentIdx+unitProcCount));
		return currentIdx;
	}
	
	private void initImageReaderPool(int poolSize){
		if(irPoolList.size()==0){
			for(int i=0;i<poolSize;i++){
				irPoolList.add(ImageIO.getImageReadersByFormatName("cgm").next());
			}
		}
	}
	
	private ImageReader getImageReader(){
		if(this.irPoolList.isEmpty())return null;
		return irPoolList.remove(0);
	}
	
	private void returnBackImageReader(ImageReader imageReader){
		this.irPoolList.add(imageReader);
	}
	
	/**
	 * CGM 이미지를 PNG 포맷으로 변환. jcgm 은 multithreading 지원하지 않음.
	 * @param src
	 * @param dest
	 */
	public void convertToPng(String src, String dest, int poolSize){
		initImageReaderPool(poolSize);
		ImageReader imageReader = getImageReader();
		//ImageReader imageReader = ImageIO.getImageReadersByFormatName("cgm").next();
		ImageInputStream iis = null;
		try{
			File cgmFile = new File(src);
			File outFile = new File(dest);
			BufferedImage image = null;
			iis = ImageIO.createImageInputStream(cgmFile);
			imageReader.reset();
			imageReader.setInput(iis, true, true);
			ImageReadParam irParam = imageReader.getDefaultReadParam();
			
			image = imageReader.read(0, irParam);
			ImageIO.write(image, "PNG", outFile);
			image.flush();
			image = null;
			//System.out.println("Converting "+src+" to "+dest+" is completed.");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			returnBackImageReader(imageReader);
			//imageReader.dispose();
			if(iis!=null)try{iis.close();}catch(Exception e){}
		}
	}
	
	/**
	 * 소스 CGM 폴더 하위의 이미지 리소스를 대상 폴더로 모두 PNG 포맷 변환한다.
	 * @param srcFolder
	 * @param destFolder
	 */
	public void convertAll(String srcFolder, String destFolder, int threadCnt){
		File _destFolder = new File(destFolder);
		File _srcFolder = new File(srcFolder);
		if(!_destFolder.exists()){
			try {
				FileUtils.forceMkdir(_destFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(destFolder!=null && !destFolder.endsWith(fileSeparator))
			destFolder = destFolder + fileSeparator;
		
		if(_srcFolder.isDirectory()){
			File[] cgmFileArray = _srcFolder.listFiles();
			if(cgmFileArray!=null){
				System.out.println("CGM File total count: "+cgmFileArray.length);
				ExecutorService threadPool = Executors.newFixedThreadPool(threadCnt);
				CompletionService<CgmToPngResult> completionService = new ExecutorCompletionService<CgmToPngResult>(threadPool);
				int taskCnt = 0;
				long startTime = System.currentTimeMillis();
				for(int i=0;i<cgmFileArray.length;i++){
					File cgmFile = cgmFileArray[i];
					if("CGM".equalsIgnoreCase(FilenameUtils.getExtension(cgmFile.getName()))){
						String cgmFileName = cgmFile.getName();
						String pngFileName = FilenameUtils.removeExtension(cgmFileName)+".png";
						//convertToPng(cgmFile.getAbsolutePath(), destFolder+pngFileName);
						try{
							completionService.submit(new ConvertTask(cgmFile.getAbsolutePath(),destFolder+pngFileName, threadCnt));
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							taskCnt++;
						}
					}
				}
				
				
				// 비동기 처리 결과 출력
				for(int i=0;i<cgmFileArray.length;i++){
					try {
						CgmToPngResult cgmToPngResult = completionService.take().get();
						System.out.println("[Multi Thread Result] "+cgmToPngResult.getResultMessage());
						if(i==cgmFileArray.length-1)
							System.out.println("Elapsed Time: "+(System.currentTimeMillis()-startTime));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				threadPool.shutdown();
			}
		}
	}
	
	class ConvertTask implements Callable<CgmToPngResult>{
		
		private String src = "";
		private String dest = "";
		private int poolSize = 0;

		public ConvertTask(String src, String dest, int poolSize){
			this.src = src;
			this.dest = dest;
			this.poolSize = poolSize;
		}
		
		@Override
		public CgmToPngResult call() throws Exception {
			
			CgmToPngResult cgmToPngResult = new CgmToPngResult();
			
			convertToPng(src, dest, poolSize);
			cgmToPngResult.setResultMessage("Converting "+src+" to "+dest+" is completed.");
			return cgmToPngResult;
		}
		
	}
	
	final class CgmToPngResult implements Serializable{

		private static final long serialVersionUID = -5158379650779260258L;
		
		private String resultMessage = "";

		public String getResultMessage() {
			return resultMessage;
		}

		public void setResultMessage(String resultMessage) {
			this.resultMessage = resultMessage;
		}
		
	}
	
	public static void main(String[] args){
		
		CgmImageConverter tic = new CgmImageConverter();
		//tic.convertToPng("c:/allelm01.cgm", "c:/test.png");
		//tic.convertToPng("c:/l_mm_052100_6_aam0_01_00.cgm", "c:/l_mm_052100_6_aam0_01_00.png");
		//tic.convertAll("D:\\data\\kal\\KAL_sample\\B777\\SGML\\CGM", "D:\\data\\kal\\KAL_sample\\B777\\converted\\png", null);
		tic.convertAll("c:TEST_CGM/", "c:/TEST_CGM/converted/png", null);
		System.out.println("Done.");
	}
}
