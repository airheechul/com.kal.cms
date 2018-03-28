
package com.kal.cms.system.vo;

import java.io.Serializable;


public class CodeInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codeType;
	
	private String lookupTypeName;
	
	private String lookupCode;
	
	private String lookupName;
	
	private String sortSequence;
	
	private String useYn;
	
	private String lookupExplain;
	
	private String commentContents;
	
	private String lookupClass;
	
	private String groupCnt;

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getLookupTypeName() {
		return lookupTypeName;
	}

	public void setLookupTypeName(String lookupTypeName) {
		this.lookupTypeName = lookupTypeName;
	}

	public String getLookupCode() {
		return lookupCode;
	}

	public void setLookupCode(String lookupCode) {
		this.lookupCode = lookupCode;
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getSortSequence() {
		return sortSequence;
	}

	public void setSortSequence(String sortSequence) {
		this.sortSequence = sortSequence;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getLookupExplain() {
		return lookupExplain;
	}

	public void setLookupExplain(String lookupExplain) {
		this.lookupExplain = lookupExplain;
	}

	public String getCommentContents() {
		return commentContents;
	}

	public void setCommentContents(String commentContents) {
		this.commentContents = commentContents;
	}

	public void setGroupCnt(String groupCnt) {
		this.groupCnt = groupCnt;
	}

	public String getGroupCnt() {
		return groupCnt;
	}

	public void setLookupClass(String lookupClass) {
		this.lookupClass = lookupClass;
	}

	public String getLookupClass() {
		return lookupClass;
	}
}
