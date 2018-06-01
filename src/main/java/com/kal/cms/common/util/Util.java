package com.kal.cms.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;

public class Util {
	
	private static char SPACE = ' ';
	
	public static String getFileSize(String size)
	  {
	      String gubn[] = {"Byte", "KB", "MB" } ;
	      String returnSize = new String ();
	      int gubnKey = 0;
	      double changeSize = 0;
	      long fileSize = 0;
	      try{
	        fileSize =  Long.parseLong(size);
	        for( int x=0 ; (fileSize / (double)1024 ) >0 ; x++, fileSize/= (double) 1024 ){
	          gubnKey = x;
	          changeSize = fileSize;
	        }
	        returnSize = changeSize + gubn[gubnKey];
	      }catch ( Exception ex){ returnSize = "0.0 Byte"; }

	      return returnSize;
	  }
	
	public static String getNullToEmptyString(String input){
		if(input == null)
			return "";
		else
			return input;
	}
	
	public static boolean isNotNVL(String input){
		if(input == null || "".equals(input))
			return false;
		else
			return true;
	}
	
	public static String getEmptyToNullString(String input){
		if(input == null || "".equals(input))
			return null;
		else
			return input;
	}
	
	// 메시지박스를 출력하고 스크립트를 실행하는 ModelAndView 객체를 리턴한다.
	public static ModelAndView getMessageView(final String msg, final String script) {
	    View view = new AbstractView() {
	        @Override
	        protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	            response.setContentType("text/html; charset=UTF-8");
	            response.setCharacterEncoding("UTF-8");
	            ServletOutputStream outs = response.getOutputStream();
	            outs.println("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><script type='text/javascript'>");
	            if ( null != msg && !"".equals(msg) ) {
	            	outs.println("alert(\"" + new String(msg.getBytes("UTF-8"), "ISO_8859_1") + "\");");
	            }
	            outs.println(new String(script.getBytes("UTF-8"), "ISO_8859_1"));
	            outs.println("</script></head></html>");
	            outs.flush();
	        }
	    };
	    return new ModelAndView(view);
	}
	
	public static ModelAndView getMessageView(final String script) {
		return getMessageView(null, script);
	}
	
	public static long termCheck(long startTime, long term){
		Calendar cal=Calendar.getInstance();
		Date endDate=cal.getTime();
		long endTime = endDate.getTime();
		
		//System.out.println("cal : "+(endTime - startTime ) );
		
		if(endTime - startTime > term){
			return endTime;
		} else {
			return 0;
		}
	}
	
	public static int getFileCount(File f, int totalCount) {
		if (f.isDirectory()) {
			String[] list = f.list();
			for (int i = 0; i < list.length; i++) {
				totalCount = getFileCount(new File(f, list[i]), totalCount);
			}
		} else {
			totalCount++;
		}
		return totalCount;
	}
	
	public static String getModelSelectBox(String subCode){
		String result = "";
		StringBuffer sb = new StringBuffer();

/*		HashMap<String, Object> pMap = new HashMap<String, Object>();
		pMap.put("lookupTypeName", "1000");
		String name = "searchMaintDocModelCode";
		List<CodeInfo> codeList = ServiceFactory.getCodeService().findSelectedCodeList(pMap);
		
        String attributes = (new StringBuilder().append(" id=\"").append(name).append("\"").toString());
        attributes = (new StringBuilder(String.valueOf(attributes))).append(" name=\"").append(name).append("\"").toString();
		
        sb.append("<select").append(attributes).append(" >");
        sb.append("<option value=\"\">- 선택 -</option>");
        
        if(codeList != null && codeList.size() > 0)
        {
            for(int i = 0; i < codeList.size(); i++)
            {
            	CodeInfo data = (CodeInfo)codeList.get(i);
                sb.append((new StringBuilder("<option value=\"")).append(data.getLookupCode()).append("\"").toString());
                sb.append(" class='"+data.getLookupClass()+"'");
                
                if(subCode != null && (data.getLookupCode()).equals(subCode))
                    sb.append(" selected");                
                sb.append(">");
                sb.append(data.getLookupName());
                sb.append("</option>");
            }
        }
        sb.append("</select>");*/
		
		return sb.toString();
	}
	
	public static String parameterSerialize(HttpServletRequest request) {
		StringBuffer buff = new StringBuffer();
		Enumeration paramNames = request.getParameterNames();
		JSONObject jsonObject = new JSONObject();

		while(paramNames.hasMoreElements()) {
			String param = (String)paramNames.nextElement();
			String[] values = request.getParameterValues(param);
			
			if(values != null && values.length > 0){
				if(values.length > 1){
					
					JSONArray jsonArray = new JSONArray();
					for (String data : values) {
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put(param, data);
						
						jsonArray.put(jsonObj);
					}
					
					jsonObject.put("list_"+param, jsonArray);
				} else {
					jsonObject.put(param, values[0]);
				}
			}

		}
		/*
		System.out.println( " --------------1 " +jsonObject.toString());
		
		try {
			JSONObject jsonObject2 = new JSONObject(jsonObject.toString());
			System.out.println( " --------------2 " +jsonObject2.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		*/
		
		return jsonObject.toString();
	}
	
	/**
	 * 사용자 설정값을 저장, SGML
	 * @param dv : WORK 설정 구분 (SGML의 경우 model 로 구분)
	 * @param filePath
	 * @param strJsonData
	 * @return
	 */
	public static boolean storeJsonUserData(String dv, String filePath, String strJsonData) {
		
		BufferedReader br = null;
		BufferedWriter bw = null; 
		try {
			makeDir(filePath);
			
			JSONObject jsonObject = null;
			JSONObject udata = new JSONObject(strJsonData);
			
			File file = new File(filePath+"/sgmlinput.dat");
			if(file.canRead()){
				//stream을 리더로 읽기 
				br = new BufferedReader(new FileReader(file)); 

	            StringBuilder sb = new StringBuilder(); 

	            int bufferSize = 4096; 
	            char readBuf [] = new char[bufferSize]; 
	            int resultSize = 0; 
	         
	            //파일의 전체 내용 읽어오기 
	            while((resultSize = br.read(readBuf))  != -1){ 
	                if(resultSize == bufferSize){ 
	                       sb.append(readBuf); 
	                } else { 
	                       for(int i = 0; i < resultSize; i++){ 
	                            sb.append(readBuf[i]); 
	                       } 
	                } 
	            }   
	            br.close();

	            String jString = sb.toString().trim();
	            
	            try {
	            	jsonObject =  new JSONObject(jString);
	            }catch (Exception de){
	            	de.printStackTrace();
	            	
	            	jsonObject = new JSONObject();
	            } 
	            
	            if(jsonObject.has(dv))
	            	jsonObject.remove(dv);
	            
	            
	            
	            jsonObject.put(dv, udata);
			} else {
				jsonObject =  new JSONObject();
				jsonObject.put(dv, udata);
			}

			bw = new BufferedWriter(new FileWriter(file));
			
			bw.write(jsonObject.toString());
			bw.flush();
			bw.close();
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			if(br != null) {
				try {
					br.close();
				} catch (Exception se){}
			}
			if(bw != null) {
				try {
					bw.close();
				} catch (Exception se){}
			}
			
			return false;
		}
	}
	
	public static JSONObject getJsonUserData(String filePath){
		BufferedReader br = null;

		try {
			JSONObject jsonObject = null;
			
			File file = new File(filePath+"/sgmlinput.dat");
			if(file.canRead()){
				//stream을 리더로 읽기 
				br = new BufferedReader(new FileReader(file)); 

	            StringBuilder sb = new StringBuilder(); 

	            int bufferSize = 4096; 
	            char readBuf [] = new char[bufferSize]; 
	            int resultSize = 0; 
	         
	            //파일의 전체 내용 읽어오기 
	            while((resultSize = br.read(readBuf))  != -1){ 
	                if(resultSize == bufferSize){ 
	                       sb.append(readBuf); 
	                } else { 
	                       for(int i = 0; i < resultSize; i++){ 
	                            sb.append(readBuf[i]); 
	                       } 
	                } 
	            }   
	            br.close();

	            String jString = sb.toString().trim();
	            
	            try {
	            	jsonObject =  new JSONObject(jString);
	            }catch (Exception de){
	            	de.printStackTrace();
	            	jsonObject = new JSONObject();
	            } 
	            
			} else {
				return new JSONObject();
			}
			
			return jsonObject;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			if(br != null) {
				try {
					br.close();
				} catch (Exception se){}
			}
			
			return new JSONObject();
		}
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
	
	/**
	 * 입력 받은 스트링에서 개행 제거 및 연속된 스페이스가 있으면 하나만 남기고 지움
	 * @param org
	 * @return
	 */
	public static String removeSpace(String org) {
		
		if(org == null){
			return "";
		}
		String result = org.replaceAll("\\r\\n", " ").replaceAll("\\n", " ");
		
	    String space = "\\s{2,}";
	    result = result.replaceAll(space, " ");
		
		if(result.startsWith(" "))
			return result.substring(1);
		
		return result;
	}
	
	
	public static void main(String[] args){
		System.out.println("==" +removeSpace("                                                          Do a general visual inspection of the zone under the cabin floor (without expansion tanks installed) (zones 145 and 146).                                                         "));
	}
}
