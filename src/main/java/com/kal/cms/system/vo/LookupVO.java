
package com.kal.cms.system.vo;

import java.io.Serializable;


public class LookupVO implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String lookupTypeName;
	
	private String lookupCode;
	
	private String lookupName;


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

}
