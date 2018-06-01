package com.kal.cms.task.vo;

import java.io.Serializable;

/**
 * 컨텐츠에 대한 키워드 테이블에 맵핑되는 info 클래스.
 */
public class KeywordInfo implements Serializable{
	
	private static final long serialVersionUID = 9152190042025303516L;
	private String keywordId;
	private String keyword;
	
	public String getKeywordId() {
		return keywordId;
	}
	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
