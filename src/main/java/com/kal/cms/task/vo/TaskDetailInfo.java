package com.kal.cms.task.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * TB_TAK_DETAIL 테이블에 맵핑되는 Task 상세 info 클래스.
 */
public class TaskDetailInfo implements Serializable{

	private static final long serialVersionUID = 5779369616433501490L;
	
	private Long detailId;
	private Long taskId;
	private String taskDetailName;
	private String status;
	private Date startDate;
	private Date endDate;
	
	private Long detailLogId;
	private String detailLog;
	private Date logDate;
	
	public Long getDetailId() {
		return detailId;
	}
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getTaskDetailName() {
		return taskDetailName;
	}
	public void setTaskDetailName(String taskDetailName) {
		this.taskDetailName = taskDetailName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Long getDetailLogId() {
		return detailLogId;
	}
	public void setDetailLogId(Long detailLogId) {
		this.detailLogId = detailLogId;
	}
	public String getDetailLog() {
		return detailLog;
	}
	public void setDetailLog(String detailLog) {
		this.detailLog = detailLog;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public String getLogDateStr() {
		if (logDate != null)
			return "";//DateUtils.getDate2String(logDate, "yyyy-MM-dd HH:mm:ss");
		else 
			return "";
	}
}
