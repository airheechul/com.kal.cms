/**
 * (주)인크로스 | http://www.incross.co.kr
 * Copyright (c)2006-2011, Incross Co.
 * All rights reserved.
 * 
 * Enterprise Mobile Platform. 
 */
package com.kal.cms.content.vo;

import java.io.Serializable;
import java.util.Date;


/**
 * <pre>
 * Content 정보
 * </pre>
 * 
 * @author (주)인크로스 | IncrossCo.
 * @version
 * @since
 * @created 2013. 11. 06.
 */
public class ContentInfo implements Serializable{

	private static final long serialVersionUID = 1L;

    public ContentInfo() {}
    
    private String masterWorkorderContentsId; 	//컨텐트ID

    private String maintDocModelCode;			//모델코드
    
    private String WOCntsDocSubtypCd;			//메뉴얼타입
    
    private String masterWOCntsTypCd;			//컨텐트타입;
    
    private String fileName;					//파일이름;
                                    
    private String urlAddress;					//파일주소;
                                    
    private String masterWOCntsRevzNo;			//Revision 순번;
                                    
    private Date masterWorkorderCntsRevzDt;		//Revision 일자;
                                    
    private String masterWOCntsSttsCd;			//상태코드;

    private String masterWOCntsSttsNm;

	private String masterWOCntsDocRevzNo;		//정비 메뉴얼 Revision 순번;
                                    
    private Date masterWOCntsDocRevzDt;			//정비 메뉴얼 Revision 일자;
                                    
    private String releaseYn;					//Release 여부;
                                    
    private Date releaseDate;					//Release 일자;
                                    
    private String titleName;					//컨텐트 제목;
                                    
    private String createdBy;					//등록자ID;
    
    private String createdByV;					//등록자(문자열)	
                                    
    private Date creationDate;					//등록일자;
                                    
    private String lastUpdatedBy;				//최종 수정자ID;
    
    private String lastUpdatedByV;				//최종 수정자(문자열)
                                    
    private String lastUpdateLogin;				//최종 로그인ID;
                                    
    private Date lastUpdateDate;				//최종 수정일자;
    
    private String masterWOCntsTskTypCd;		//task 타입
    
    private Date workStartDatetime;			//task 시작일자
    
    private Date workEndDatetime;				//task 종료일자
    
    private String masterWOCntsTskSttsCd;		//task 상태
    
    private String latestRevYn;					//최신 버전 여부

    private String masterWOCntsImgId;			//이미지 아이디만 별도로 셋팅	
    
    
    
	public String getCreatedByV() {
		return createdByV;
	}

	public void setCreatedByV(String createdByV) {
		this.createdByV = createdByV;
	}

	public String getLastUpdatedByV() {
		return lastUpdatedByV;
	}

	public void setLastUpdatedByV(String lastUpdatedByV) {
		this.lastUpdatedByV = lastUpdatedByV;
	}

	public String getMasterWorkorderContentsId() {
		return masterWorkorderContentsId;
	}

	public void setMasterWorkorderContentsId(String masterWorkorderContentsId) {
		this.masterWorkorderContentsId = masterWorkorderContentsId;
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

	public String getMasterWOCntsRevzNo() {
		return masterWOCntsRevzNo;
	}

	public void setMasterWOCntsRevzNo(String masterWOCntsRevzNo) {
		this.masterWOCntsRevzNo = masterWOCntsRevzNo;
	}

	public Date getMasterWorkorderCntsRevzDt() {
		return masterWorkorderCntsRevzDt;
	}

	public void setMasterWorkorderCntsRevzDt(Date masterWorkorderCntsRevzDt) {
		this.masterWorkorderCntsRevzDt = masterWorkorderCntsRevzDt;
	}

	public String getMasterWOCntsSttsCd() {
		return masterWOCntsSttsCd;
	}

	public void setMasterWOCntsSttsCd(String masterWOCntsSttsCd) {
		this.masterWOCntsSttsCd = masterWOCntsSttsCd;
	}
	
    public String getMasterWOCntsSttsNm() {
		return masterWOCntsSttsNm;
	}

	public void setMasterWOCntsSttsNm(String masterWOCntsSttsNm) {
		this.masterWOCntsSttsNm = masterWOCntsSttsNm;
	}

	public Date getMasterWOCntsDocRevzDt() {
		return masterWOCntsDocRevzDt;
	}

	public void setMasterWOCntsDocRevzDt(Date masterWOCntsDocRevzDt) {
		this.masterWOCntsDocRevzDt = masterWOCntsDocRevzDt;
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

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdateLogin() {
		return lastUpdateLogin;
	}

	public void setLastUpdateLogin(String lastUpdateLogin) {
		this.lastUpdateLogin = lastUpdateLogin;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getMasterWOCntsTskTypCd() {
		return masterWOCntsTskTypCd;
	}

	public void setMasterWOCntsTskTypCd(String masterWOCntsTskTypCd) {
		this.masterWOCntsTskTypCd = masterWOCntsTskTypCd;
	}

	public String getMasterWOCntsTskSttsCd() {
		return masterWOCntsTskSttsCd;
	}

	public void setMasterWOCntsTskSttsCd(String masterWOCntsTskSttsCd) {
		this.masterWOCntsTskSttsCd = masterWOCntsTskSttsCd;
	}

	public void setLatestRevYn(String latestRevYn) {
		this.latestRevYn = latestRevYn;
	}

	public String getLatestRevYn() {
		return latestRevYn;
	}

	public void setMasterWOCntsImgId(String masterWOCntsImgId) {
		this.masterWOCntsImgId = masterWOCntsImgId;
	}

	public String getMasterWOCntsImgId() {
		return masterWOCntsImgId;
	}

	public void setMasterWOCntsDocRevzNo(String masterWOCntsDocRevzNo) {
		this.masterWOCntsDocRevzNo = masterWOCntsDocRevzNo;
	}

	public String getMasterWOCntsDocRevzNo() {
		return masterWOCntsDocRevzNo;
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

}
