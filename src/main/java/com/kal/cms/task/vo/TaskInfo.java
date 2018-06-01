package com.kal.cms.task.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * TB_TASK 테이블에 맵핑되는 Task info 클래스.
 */
public class TaskInfo implements Serializable{

	private static final long serialVersionUID = -2174122046045976421L;
	
	private Long taskId;
	private String taskType;
	private String model;
	private String manualType;
	private String contentType;
	private String description;
	private Date startDate;
	private Date endDate;
	private String createdBy;
	private String createdByV;
	private Date creationDate;
	private String status;
	private String userId;
	private String fileName;
	private String urlAddress;
	private String userData;
	
	//task detail
	private Long subTaskId;
	private Date subTaskStartDate;
	private Date subTaskEndDate;
	private String subTaskStatus;
	private String subTaskDesc;
	private String subTaskRank;
	
	
	
	public String getCreatedByV() {
		return createdByV;
	}
	public void setCreatedByV(String createdByV) {
		this.createdByV = createdByV;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getManualType() {
		return manualType;
	}
	public void setManualType(String manualType) {
		this.manualType = manualType;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getSubTaskId() {
		return subTaskId;
	}
	public void setSubTaskId(Long subTaskId) {
		this.subTaskId = subTaskId;
	}
	public Date getSubTaskStartDate() {
		return subTaskStartDate;
	}
	public void setSubTaskStartDate(Date subTaskStartDate) {
		this.subTaskStartDate = subTaskStartDate;
	}
	public Date getSubTaskEndDate() {
		return subTaskEndDate;
	}
	public void setSubTaskEndDate(Date subTaskEndDate) {
		this.subTaskEndDate = subTaskEndDate;
	}
	public String getSubTaskStatus() {
		return subTaskStatus;
	}
	public void setSubTaskStatus(String subTaskStatus) {
		this.subTaskStatus = subTaskStatus;
	}
	public String getSubTaskDesc() {
		return subTaskDesc;
	}
	public void setSubTaskDesc(String subTaskDesc) {
		this.subTaskDesc = subTaskDesc;
	}
	public void setSubTaskRank(String subTaskRank) {
		this.subTaskRank = subTaskRank;
	}
	public String getSubTaskRank() {
		return subTaskRank;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	public String getUserData() {
		return userData;
	}
	
}
