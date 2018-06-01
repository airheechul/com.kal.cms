package com.kal.cms.system.vo;

import java.io.Serializable;
import java.util.Date;

public class TemplateInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String masterWOCntsTmptId;
	private String masterWOCntsTmptTypCd;
	private String templateName;
	private String fileName;
	private String urlAddress;
	
	private String useYn;
    private String createdBy;					//등록자ID;    
    private Date creationDate;					//등록일자;
    private String lastUpdatedBy;				//최종 수정자ID;
    private Date lastUpdateDate;				//최종 수정일자;
	
	
	public String getMasterWOCntsTmptId() {
		return masterWOCntsTmptId;
	}
	public void setMasterWOCntsTmptId(String masterWOCntsTmptId) {
		this.masterWOCntsTmptId = masterWOCntsTmptId;
	}
	public String getMasterWOCntsTmptTypCd() {
		return masterWOCntsTmptTypCd;
	}
	public void setMasterWOCntsTmptTypCd(String masterWOCntsTmptTypCd) {
		this.masterWOCntsTmptTypCd = masterWOCntsTmptTypCd;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
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
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
}
