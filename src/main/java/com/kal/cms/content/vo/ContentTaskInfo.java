package com.kal.cms.content.vo;

import java.io.Serializable;
import java.util.Date;

public class ContentTaskInfo implements Serializable{
	
	private static final long serialVersionUID = 6853088785134304563L;

	private String masterWOCntsTskId;	//task id
    
	private String masterWOCntsTskTypCd;	//task type
	               
	private String maintDocModelCode;	//모델 코드
	               
	private String WOCntsDocSubtypCd;	//메뉴얼 타입
	               
	private String masterWOCntsTypCd;	//컨텐트 타입
	               
	private String masterWOCntsTskCtn;	//설명/비고
	               
	private String releaseYn;	//릴리즈 여부
	               
	private Date releaseDate;	//릴리즈 일자
	               
	private Date workStartDatetime;	//시작일자
	               
	private Date workEndDatetime;	//종료일자
	               
	private String createdBy;	//등록자
	
	private String createdByV;	//등록자(문자열)
	               
	private Date creationDate;	//등록일자
	               
	private String masterWOCntsTskSttsCd;	//상태
	               
	private String userId;	//다운로드 사용자
	               
	private String fileName;	//파일이름
	               
	private String urlAddress;	//url 주소

	private String masterWOCntsRevzNo;			//Revision 순번;
	
    private String masterWOCntsDocRevzNo;		//정비 메뉴얼 Revision 순번;
	
	private String latestRevYn;					//최신 버전 여부
	
	private String reportType;
	
	private String reportSrcPath;
	
	private String reportBase;
	
	public String getCreatedByV() {
		return createdByV;
	}

	public void setCreatedByV(String createdByV) {
		this.createdByV = createdByV;
	}

	public String getMasterWOCntsTskId() {
		return masterWOCntsTskId;
	}

	public void setMasterWOCntsTskId(String masterWOCntsTskId) {
		this.masterWOCntsTskId = masterWOCntsTskId;
	}

	public String getMasterWOCntsTskTypCd() {
		return masterWOCntsTskTypCd;
	}

	public void setMasterWOCntsTskTypCd(String masterWOCntsTskTypCd) {
		this.masterWOCntsTskTypCd = masterWOCntsTskTypCd;
	}

	public String getMaintDocModelCode() {
		return maintDocModelCode;
	}

	public void setMaintDocModelCode(String maintDocModelCode) {
		this.maintDocModelCode = maintDocModelCode;
	}

	public String getWOCntsDocSubtypCd() {
		return WOCntsDocSubtypCd;
	}

	public void setWOCntsDocSubtypCd(String wOCntsDocSubtypCd) {
		WOCntsDocSubtypCd = wOCntsDocSubtypCd;
	}

	public String getMasterWOCntsTypCd() {
		return masterWOCntsTypCd;
	}

	public void setMasterWOCntsTypCd(String masterWOCntsTypCd) {
		this.masterWOCntsTypCd = masterWOCntsTypCd;
	}

	public String getMasterWOCntsTskCtn() {
		return masterWOCntsTskCtn;
	}

	public void setMasterWOCntsTskCtn(String masterWOCntsTskCtn) {
		this.masterWOCntsTskCtn = masterWOCntsTskCtn;
	}

	public String getReleaseYn() {
		return releaseYn;
	}

	public void setReleaseYn(String releaseYn) {
		this.releaseYn = releaseYn;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getMasterWOCntsTskSttsCd() {
		return masterWOCntsTskSttsCd;
	}

	public void setMasterWOCntsTskSttsCd(String masterWOCntsTskSttsCd) {
		this.masterWOCntsTskSttsCd = masterWOCntsTskSttsCd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public void setMasterWOCntsRevzNo(String masterWOCntsRevzNo) {
		this.masterWOCntsRevzNo = masterWOCntsRevzNo;
	}

	public String getMasterWOCntsRevzNo() {
		return masterWOCntsRevzNo;
	}

	public void setLatestRevYn(String latestRevYn) {
		this.latestRevYn = latestRevYn;
	}

	public String getLatestRevYn() {
		return latestRevYn;
	}

	public Date getWorkStartDatetime() {
		return workStartDatetime;
	}

	public void setWorkStartDatetime(Date workStartDatetime) {
		this.workStartDatetime = workStartDatetime;
	}

	public Date getWorkEndDatetime() {
		return workEndDatetime;
	}

	public void setWorkEndDatetime(Date workEndDatetime) {
		this.workEndDatetime = workEndDatetime;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportBase(String reportBase) {
		this.reportBase = reportBase;
	}

	public String getReportBase() {
		return reportBase;
	}

	public void setReportSrcPath(String reportSrcPath) {
		this.reportSrcPath = reportSrcPath;
	}

	public String getReportSrcPath() {
		return reportSrcPath;
	}

	public void setMasterWOCntsDocRevzNo(String masterWOCntsDocRevzNo) {
		this.masterWOCntsDocRevzNo = masterWOCntsDocRevzNo;
	}

	public String getMasterWOCntsDocRevzNo() {
		return masterWOCntsDocRevzNo;
	}
}
