
package com.kal.cms.system.vo;

import java.io.Serializable;
import java.util.List;


public class LookupListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String lookupTypeName;
	private List<LookupVO> lookupVOList;


	public List<LookupVO> getLookupVOList() {
		return lookupVOList;
	}

	public void setLookupVOList(List<LookupVO> lookupVOList) {
		this.lookupVOList = lookupVOList;
	}

	public String getLookupTypeName() {
		return lookupTypeName;
	}

	public void setLookupTypeName(String lookupTypeName) {
		this.lookupTypeName = lookupTypeName;
	}
}
