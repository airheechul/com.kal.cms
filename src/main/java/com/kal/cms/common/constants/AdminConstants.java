/**
 * (주)인크로스 | http://www.incross.co.kr
 * Copyright (c)2006-2011, Incross Co.
 * All rights reserved.
 * 
 * Enterprise Mobile Platform. 
 */
package com.kal.cms.common.constants;

/**
 * <pre>
 * 관리자에서 사용되는 상수 정의
 * </pre>
 * 
 * @author <a href="http://incross.co.kr">(주)인크로스 | Incross Co.</a>
 * @version
 * @since
 * @created 2011. 3. 21.
 */
public final class AdminConstants {

    /**
     * 생성자
     */
    public AdminConstants() {
        // Default Constructor
    }

    /**
     * 관리자 세션 키
     */
    public final static String SESSION_KEY_ADMIN_INFO = "sessionAdminInfo";

    /**
     * AJAX 호출 결과 ViewName : jsonView
     */
    public final static String AJAX_VIEW_NAME_JSON_VIEW = "jsonView";

    /**
     * AJAX 호출 결과 json
     */
    public final static String MODEL_AND_VIEW_KEY_JSON = "json";

    /**
     * AJAX 호출 결과 성공 키
     */
    public final static String AJAX_RESULT_KEY_SUCCESS = "success";

    /**
     * AJAX 호출 결과 : 성공
     */
    public final static String AJAX_RESULT_VALUE_TRUE = "true";

    /**
     * AJAX 호출 결과 : 실패
     */
    public final static String AJAX_RESULT_VALUE_FALSE = "false";
    
    public final static String CONST_YES = "Y";
    public final static String CONST_NO = "N";
    
    /**
     * yes or no
     */
    public final static String USE_YN_Y = "Y";
    public final static String USE_YN_N = "N";
    
    /**
     * LOGIN FAIL RETURN MESSAGE
     */
    public final static String FAIL_MSG_ID = "90001";
    public final static String FAIL_MSG_AUTH = "90002";
    public final static String FAIL_MSG_CERTIFICATION = "90003";
    
    /**
     * ADMIN LEVEL
     */
    public final static String ADMIN_MANAGER = "50001";
    public final static String ADMIN_ADMINISTRATOR = "50002";
    public final static String ADMIN_ENGINEER = "50003";
    public final static String ADMIN_MANUFACTURE = "50005";
    
    
    public final static String FILE_SEPARATOR = "/";
    /**
     * <pre>
     * Contents Search 관련 상수 정의
     * </pre>
     *
     * @author <a href="http://incross.co.kr">(주)인크로스 | Incross Co.</a>
     * @version
     * @since
     * @created 2011. 7. 7.
     */
    public final class ContentSearch {

    	/**
    	 * Model Type
    	 */
    	public static final String MODEL_TYPE = "13000";	//모델 구분
    	public static final String MODEL_TYPE_AIRCRAFT = "13001";	//항공기
    	public static final String MODEL_TYPE_ENGINE = "13002";	//엔진
    	public static final String MODEL_TYPE_COMPONENT = "13003";	//부품    	
    	
    	/**
    	 * Manual Type
    	 */
    	public static final String MANUAL_TYPE_AMM = "AMM";
    	public static final String MANUAL_TYPE_KOR = "KOR";
    	public static final String MANUAL_TYPE_TSM = "TSM";
    	public static final String MANUAL_TYPE_FRMFIM = "FRMFIM";
    	
    	/**
    	 * Content Type
    	 */
    	public static final String CONTENT_TYPE_OP = "OP";
    	public static final String CONTENT_TYPE_RT = "RT";
    	public static final String CONTENT_TYPE_IMG = "IMAGE";
    	
    	/**
    	 * Revision Type
    	 */
    	public static final String REVISION_TYPE_REVISE = "R";
    	public static final String REVISION_TYPE_NEW = "N";
    	public static final String REVISION_TYPE_DELETE = "D";
    	
    	/**
    	 * 사용/RELEASE 여부
    	 */
    	public static final String RELEASE_TYPE_Y = "Y";
    	public static final String RELEASE_TYPE_N = "N";


    	/**
    	 * 생성자
    	 */
    	private ContentSearch() {
    		// Default Constructor
    	}
    }

    public final class TaskConstants {
    	/**
    	 * Manual Type
    	 */
    	public static final String MANUAL_TYPE_AMM = "AMM";
    	public static final String MANUAL_TYPE_KOR = "KOR";
    	public static final String MANUAL_TYPE_TSM = "TSM";
    	public static final String MANUAL_TYPE_FRMFIM = "FRMFIM";
    	
    	/**
    	 * Model Type
    	 */
    	public static final String MODEL_TYPE = "13000";	//모델 구분
    	public static final String MODEL_TYPE_AIRCRAFT = "13001";	//항공기
    	public static final String MODEL_TYPE_ENGINE = "13002";	//엔진
    	public static final String MODEL_TYPE_COMPONENT = "13003";	//부품
    	
    	/**
    	 * Content Type
    	 */
    	public static final String CONTENT_TYPE_OP = "OP";
    	public static final String CONTENT_TYPE_RT = "RT";
    	public static final String CONTENT_TYPE_IMG = "IMAGE";
    	
    	/**
    	 * Revision Type
    	 */
    	public static final String REVISION_TYPE_REVISE = "R";
    	public static final String REVISION_TYPE_NEW = "N";
    	public static final String REVISION_TYPE_DELETE = "D";
    	
    	/**
    	 * 사용/RELEASE 여부
    	 */
    	public static final String RELEASE_TYPE_Y = "Y";
    	public static final String RELEASE_TYPE_N = "N";
    	
    	/**
    	 * task 구분
    	 */
    	public static final String TASK_TPYE_SGML = "5001";	//SGML
    	public static final String TASK_TPYE_Batch_Download = "5002";	//Batch-Download
    	public static final String TASK_TPYE_Batch_Upload = "5003";	//Batch-Upload
    	public static final String TASK_TPYE_MJC = "5004";	//MJC

    	/**
    	 * task 상태
    	 */
    	public static final String TASK_STATUS_COMPLETE = "7001";
    	public static final String TASK_STATUS_INPROGRESS = "7002";
    	public static final String TASK_STATUS_FAIL = "7003";
    	public static final String TASK_STATUS_QUEUED = "7004";
    	public static final String TASK_STATUS_ABORTED = "7005";
    	public static final String TASK_STATUS_DELETED = "7006";
    	public static final String TASK_STATUS_REPORT = "7007";

    	/**
    	 * 프로세스 기동시 task 상태 체크 텀 (건수)
    	 */
    	public static final int TASK_PROCESS_CHECK_TERM = 100;
    	
    	/**
    	 * TASK ELEMENT 의 KMR_NUMBER 값이 KMR
    	 */
    	public static final String KMR_NUMBER = "KMR";
    	
    	/**
    	 * 생성자
    	 */
    	private TaskConstants() {
    		// Default Constructor
    	}
    }
}
