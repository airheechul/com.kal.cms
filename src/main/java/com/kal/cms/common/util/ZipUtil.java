package com.kal.cms.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


import com.kal.cms.common.constants.AdminConstants.TaskConstants;
import com.easycompany.cmm.exception.MpFrameException;

import com.kal.cms.content.mapper.ContentProcessMapper;

//import net.sf.jazzlib.ZipEntry;
//import net.sf.jazzlib.ZipInputStream;
//import net.sf.jazzlib.ZipOutputStream;

public class ZipUtil {
	private static final int COMPRESSION_LEVEL = 8; // 압축 레벨 - 최대 압축률은 9, 디폴트 8
	private static final int BUFFER_SIZE = 4096;
	
	/**
     * 지정된 폴더를 Zip 파일로 압축한다.
     * @param sourcePath - 압축 대상 디렉토리
     * @param output - 저장 zip 파일 이름
     * @throws Exception
     */
	public static void zip(String sourcePath, String output) throws Exception {
		// 압축 대상(sourcePath)이 디렉토리나 파일이 아니면 리턴한다.
		File sourceFile = new File(sourcePath);
		if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
			throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.");
		}

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(output); // FileOutputStream
			bos = new BufferedOutputStream(fos); // BufferedStream
			zos = new ZipOutputStream(bos); // ZipOutputStream
			
			zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8

			zipEntry(sourceFile, sourcePath, zos); // Zip 파일 생성
			zos.finish(); // ZipOutputStream finish
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (Exception e) {}
			
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {}
			
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {}
		}
	}
	
	/**
     * 지정된 폴더를 Zip 파일로 압축한다.
     * @param sourcePath - 압축 대상 파일 path 리스트
     * @param output - 저장 zip 파일 이름
     * @throws Exception
     */
	public static void zip(List<String> sourceFilePathList, String output) throws Exception {

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(output); // FileOutputStream
			bos = new BufferedOutputStream(fos); // BufferedStream
			zos = new ZipOutputStream(bos); // ZipOutputStream
			
			zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8

			zipEntry(sourceFilePathList ,zos); // Zip 파일 생성
			zos.finish(); // ZipOutputStream finish
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (Exception e) {}
			
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {}
			
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {}
		}
	}
	
	/**
     * 지정된 폴더를 Zip 파일로 압축한다.
     * @param sourcePath - 압축 대상 파일 path 리스트
     * @param output - 저장 zip 파일 이름
     * @throws Exception
     */
	public static void zipWithLog(List<String> sourceFilePathList, String output, java.util.HashMap<String, Object> pMap, ContentProcessMapper contentProcessDao) throws Exception {

		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;

		try {
			fos = new FileOutputStream(output); // FileOutputStream
			bos = new BufferedOutputStream(fos); // BufferedStream
			zos = new ZipOutputStream(bos); // ZipOutputStream
			
			zos.setLevel(COMPRESSION_LEVEL); // 압축 레벨 - 최대 압축률은 9, 디폴트 8

			zipEntryWithLog(sourceFilePathList , zos, pMap, contentProcessDao); // Zip 파일 생성
			zos.finish(); // ZipOutputStream finish
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (Exception e) {}
			
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {}
			
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {}
		}
	}
	
	/**
     * 압축
     * @param sourceFile
     * @param sourcePath
     * @param zos
     * @throws Exception
     */
	private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws Exception {
		// sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
		if (sourceFile.isDirectory()) {
			File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
			for (int i = 0; i < fileArray.length; i++) {
				zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
			}
		} else { // sourcehFile 이 디렉토리가 아닌 경우
			BufferedInputStream bis = null;
			try {
				String sFilePath = sourceFile.getPath();
				String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());

				bis = new BufferedInputStream(new FileInputStream(sourceFile));
				ZipEntry zentry = new ZipEntry(zipEntryName);
				zentry.setTime(sourceFile.lastModified());
				zos.putNextEntry(zentry);

				byte[] buffer = new byte[BUFFER_SIZE];
				int cnt = 0;
				while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
					zos.write(buffer, 0, cnt);
				}
				zos.closeEntry();
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
				} catch (Exception e) {}
			}
		}
	}
	


	/**
     * 압축2 (파일 패스 리스트로 압축)
     * @param sourceFile
     * @param sourcePath
     * @param zos
     * @throws Exception
     */
	private static void zipEntry(List<String> sourceFilePathList, ZipOutputStream zos) throws Exception {
		for (String path :  sourceFilePathList) {
			File file = new File(path);
			
			if(file.exists()) {
				zipEntry2(file, zos);
			} else {
				throw new Exception("File not exist ...");
			}
		}
	}
	
	/**
     * 압축2 (파일 패스 리스트로 압축)
     * @param sourceFile
     * @param sourcePath
     * @param zos
     * @throws Exception
     */
	private static void zipEntryWithLog(List<String> sourceFilePathList, ZipOutputStream zos, java.util.HashMap<String, Object> pMap, ContentProcessMapper contentProcessDao) throws Exception {
		int totalCount = sourceFilePathList.size();
		int excuteCount = 0;
		int checkTermCount = 0;

		// 주기적으로 로그 남긴다.
		pMap.put("errorMessageContents", "zipping 처리 시작");
		contentProcessDao.insertTaskDetailLog(pMap);		
		
		for (String path :  sourceFilePathList) {
			
			checkTermCount++;
			excuteCount++;
			if (checkTermCount >= TaskConstants.TASK_PROCESS_CHECK_TERM){
				checkTermCount = 0;
				
				// 주기적으로 프로세스 정지 여부 확인한다.
				String taskStatus = contentProcessDao.getTaskStatus(pMap);
				if(TaskConstants.TASK_STATUS_ABORTED.equals(taskStatus)){
					throw new MpFrameException("사용자 Abort 요청에 의한 종료", TaskConstants.TASK_STATUS_ABORTED);
				}
				
				// 주기적으로 로그 남긴다.
				pMap.put("errorMessageContents", excuteCount +"/" +totalCount +" 처리중...");
				contentProcessDao.insertTaskDetailLog(pMap);
			}			
			
			File file = new File(path);
			
			if(file.exists()) {
				zipEntry2(file, zos);
			} else {
				throw new Exception("File not exist ...");
			}
		}
		
		// 주기적으로 로그 남긴다.
		pMap.put("errorMessageContents", totalCount+"개 파일 zipping 처리 종료");
		contentProcessDao.insertTaskDetailLog(pMap);
	}
	
	private static void zipEntry2(File sourceFile, ZipOutputStream zos) throws Exception {

		BufferedInputStream bis = null;
		try {
			//String zipEntryName = targetPath.substring(targetPath.length() + 1, sFilePath.length());

			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			//ZipEntry zentry = new ZipEntry(".");
			//zentry.setTime(sourceFile.lastModified());
			zos.putNextEntry(new ZipEntry(sourceFile.getName()));

			byte[] buffer = new byte[BUFFER_SIZE];
			int cnt = 0;
			while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
				zos.write(buffer, 0, cnt);
			}
			zos.closeEntry();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {}
		}
	}
	
	/**
     * Zip 파일의 압축을 푼다.
     *
     * @param zipFile - 압축 풀 Zip 파일
     * @param targetDir - 압축 푼 파일이 들어간 디렉토리
     * @throws Exception
     */
	public static void unzip(String zipFile, String targetDir) throws Exception {
		ZipInputStream zis = null;
		ZipEntry zentry = null;
		File sourceFile = new File(zipFile);
		
		if (sourceFile.isFile() == false) {
			throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.(" + sourceFile.getAbsolutePath() + ")");
		}

		try {
			zis = new ZipInputStream(new FileInputStream(sourceFile));

			while ((zentry = zis.getNextEntry()) != null ) {
				String fileNameToUnzip = zentry.getName();

				File targetFile = new File(targetDir, fileNameToUnzip);
				
				if (zentry.isDirectory()) {// Directory 인 경우					
					makeDir(targetFile.getAbsolutePath()); // 디렉토리 생성
				} else { // File 인 경우
					// parent Directory 생성
					makeDir(targetFile.getParent());			
					unzipEntry(zis, targetFile);
				}
			}
		} finally {
			try {
				if (zis != null) zis.close();
			} catch (Exception e) {}
		}
	}
	
	/**
     * Zip 파일의 압축을 푼다. 서브 폴더를 추가로 생성한다. (대용량 처리용)
     *
     * @param zipFile - 압축 풀 Zip 파일
     * @param targetDir - 압축 푼 파일이 들어간 디렉토리
     * @throws Exception
     */
	public static void unzipToSub(String zipFile, String targetDir, int fileNum) throws Exception {
		ZipInputStream zis = null;
		ZipEntry zentry = null;
		File sourceFile = new File(zipFile);
		
		if (sourceFile.isFile() == false) {
			throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.(" + sourceFile.getAbsolutePath() + ")");
		}

		try {
			zis = new ZipInputStream(new FileInputStream(sourceFile));

			int totFileNum = 0;
			int fileCnt = 0;
			int dir = 0;
			String upperDir = "0";
			
			while ((zentry = zis.getNextEntry()) != null ) {
				String fileNameToUnzip = zentry.getName();

				File targetFile = new File(targetDir, fileNameToUnzip);
				
				if (zentry.isDirectory()) {// Directory 인 경우					
					makeDir(targetFile.getAbsolutePath()); // 디렉토리 생성
				} else { // File 인 경우
					// parent Directory 생성

					if(totFileNum == 0)
						makeDir(targetFile.getParent()+"/"+upperDir);
					
					totFileNum ++;		
					
					if(fileCnt > fileNum) {
						fileCnt = 0;
						dir ++;	
						upperDir = Integer.toString(dir);
						
						makeDir(targetFile.getParent()+"/"+upperDir);
					} else {
						fileCnt ++;
					}
					
					targetFile = new File(targetDir+"/"+upperDir, fileNameToUnzip);
					
					unzipEntry(zis, targetFile);
				}
			}
		} finally {
			try {
				if (zis != null) zis.close();
			} catch (Exception e) {}
		}
	}

	/**
     * Zip 파일의 한 개 엔트리의 압축을 푼다.
     *
     * @param zis - Zip Input Stream
     * @param filePath - 압축 풀린 파일의 경로
     * @return
     * @throws Exception
     */
	protected static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);	
			
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = zis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {		
			try {
				if (fos != null) fos.close();
			} catch (Exception e) {}
		}
		
		return targetFile;
	}
	
	/**
	 * 폴더 생성하기
	 * @param dest
	 * @return
	 */
    public static boolean makeDir(String dest){
    	boolean isDir = false;
    	
    	File file = new File(dest);

    	if(!(isDir=file.isDirectory())){
    		file.mkdirs();
    	}
    	
    	return isDir;
    }
}
