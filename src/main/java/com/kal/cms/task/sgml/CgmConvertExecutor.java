package com.kal.cms.task.sgml;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * CGM 이미지 포맷 변환 실행 클래스.
 */
public class CgmConvertExecutor {
	
	private static String fileSeparator = System.getProperty("file.separator")==null?"/":System.getProperty("file.separator");
	
	/**
	 * CGM 이미지를 PNG 포맷으로 변환.
	 * @param src
	 * @param dest
	 * @throws Exception 
	 */
	public synchronized void convertToPng(String src, String dest) throws Exception{

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
			//System.out.println("Converting "+src+" to "+dest+" is completed.");
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
	public void convertAll(String srcFolder, String destFolder, int startIdx, int destCgmCnt, String procName){
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
				System.out.println("["+procName+"] CGM File total count: "+(cgmFileArray==null?0:cgmFileArray.length));
				cgmFileArray = getInitialCgmFileList(cgmFileArray, startIdx, destCgmCnt);
				if(cgmFileArray!=null){
					System.out.println("["+procName+"] Current Unit Process CGM File total count: "+cgmFileArray.length);
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
							System.out.println("["+procName+"] Converting "+(startIdx+i+1)+"th "+cgmFile.getAbsolutePath()+" to "+destFolder+pngFileName+" is completed.");
						}
					}
					System.out.println("["+procName+"] Elapsed Time: "+(System.currentTimeMillis()-startTime));
				}
			}else{
				throw new Exception("["+procName+"] CGM Src Folder is not valid directory.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 단위 작업당 처리할 대상 cgm 파일 목록을 리턴.
	 * @param cgmFileArray
	 * @param startIdx
	 * @param destCgmCnt
	 * @return
	 */
	private File[] getInitialCgmFileList(File[] cgmFileArray, int startIdx, int destCgmCnt){
		File[] destCgmFileList = null;
		//File[] cgmFileList = null;

		if(cgmFileArray!=null){
			if(cgmFileArray.length > startIdx){
				destCgmFileList = (File[])ArrayUtils.subarray(cgmFileArray, startIdx, startIdx+destCgmCnt);
			}
		}
		return destCgmFileList;
	}
	
	public static void main(String[] args){
		for(int i=0;i<args.length;i++)
			System.out.println("args: "+args[i]);
		CgmConvertExecutor tic = new CgmConvertExecutor();
		//tic.convertToPng("c:/allelm01.cgm", "c:/test.png");
		//tic.convertToPng("c:/l_mm_052100_6_aam0_01_00.cgm", "c:/l_mm_052100_6_aam0_01_00.png");
		//tic.convertAll("D:\\data\\kal\\KAL_sample\\B777\\SGML\\CGM", "D:\\data\\kal\\KAL_sample\\B777\\converted\\png", null);
		//tic.convertAll("c:TEST_CGM/", "c:/TEST_CGM/converted/png", null);
		int startIdx = Integer.parseInt(args[3]);
		int destCgmCnt = Integer.parseInt(args[4]);
		tic.convertAll(args[0], args[1], startIdx, destCgmCnt, args[2]);
		System.out.println("Done.");
	}
}
