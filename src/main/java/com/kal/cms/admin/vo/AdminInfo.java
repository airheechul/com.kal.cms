package com.kal.cms.admin.vo;

import java.io.Serializable;
import java.util.Date;


public class AdminInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int rowIdx;
	private int userId;
	private String userLocalName;
	private String roleCode;
	private int createdBy;
	private Date creationDate;
	private String createdByV;
	private String userName;

	public int getRowIdx() {
		return rowIdx;
	}
	public void setRowIdx(int rowIdx) {
		this.rowIdx = rowIdx;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserLocalName() {
		return userLocalName;
	}
	public void setUserLocalName(String userLocalName) {
		this.userLocalName = userLocalName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedByV() {
		return createdByV;
	}
	public void setCreatedByV(String createdByV) {
		this.createdByV = createdByV;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
