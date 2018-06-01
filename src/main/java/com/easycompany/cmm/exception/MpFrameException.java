package com.easycompany.cmm.exception;

public class MpFrameException extends RuntimeException {
	private static final long serialVersionUID = -3166960196081981598L;
	
	private String errorCode;
	
	/**
	 * RuntimeException 기본생성자 호출
	 */
	public MpFrameException(){		
		super();
	}

	/**
	 * RuntimeException의 throwable를 호출한다.
	 * @param (Throwable) throwable
	 */
	public MpFrameException(Throwable throwable){
		super(throwable);
	}

	/**
	 * RuntimeException의 errorMessage를 호출한다.
	 * @param (String) errorMessage
	 */
	public MpFrameException(String errorMessage) {
		super(errorMessage);		
	}
	
	/**
	 * RuntimeException의 errorMessage를 호출하고 errorcode리턴한다.
	 * @param (String) errorMessage
	 * @param (String) errorCode
	 */
	public MpFrameException(String errorMessage, String errorCode){
		super(errorMessage);
		this.errorCode = errorCode;
	}

	/**
	 * errorCode 리턴한다.
	 * @return errorCode
	 */
	public String getErrorCode() {
		return this.errorCode;
	}
}