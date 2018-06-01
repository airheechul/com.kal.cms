package com.kal.cms.common.util.report;

import com.kal.cms.content.vo.ContentTaskInfo;

public class ReportInfo {
	private String header;
	private ContentTaskInfo taskInfo;

	private String ENIGMA_MODEL;
	private String MODEL;
	private String cMODEL;

	private String manualType;
	private String reportType;
	
	ReportInfo(ContentTaskInfo taskInfo) throws Exception {
		this.taskInfo = taskInfo;
		this.ENIGMA_MODEL = taskInfo.getMaintDocModelCode();

		this.manualType = taskInfo.getWOCntsDocSubtypCd();
		this.reportType = taskInfo.getReportType();
		
		if(ENIGMA_MODEL.equalsIgnoreCase("B737")) {
	        MODEL = "B737";
	        cMODEL = "737";
		} else if(ENIGMA_MODEL.equalsIgnoreCase("B777")) {
	        MODEL = "B777";
	        cMODEL = "777";
		} else if(ENIGMA_MODEL.equalsIgnoreCase("B747-400")) {
	        MODEL = "B744";
	        cMODEL = "744";
		} else if(ENIGMA_MODEL.equalsIgnoreCase("B747-8")) {
	        MODEL = "B748";
	        cMODEL = "748";
		} else if(ENIGMA_MODEL.equalsIgnoreCase("A330")) {
            MODEL = "A330";
            cMODEL = "330";
		} else if(ENIGMA_MODEL.equalsIgnoreCase("A380")) {
            MODEL = "A380";
            cMODEL = "380";
		} else {
			//TODO B737_BBJ, A300-600 의 처리는 어찌하는지??
			
			throw new Exception("항공기 코드가 적합하지 않습니다. : model = "+ ENIGMA_MODEL);
		}
	}
	
	public String getHeader(){
		return header;
	}

	public void setHeader(String header){
		this.header = header;
	}
	
	public void setTaskInfo(ContentTaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public ContentTaskInfo getTaskInfo() {
		return taskInfo;
	}

	public String getENIGMA_MODEL() {
		return ENIGMA_MODEL;
	}

	public void setENIGMA_MODEL(String eNIGMA_MODEL) {
		ENIGMA_MODEL = eNIGMA_MODEL;
	}

	public String getMODEL() {
		return MODEL;
	}

	public void setMODEL(String mODEL) {
		MODEL = mODEL;
	}

	public String getcMODEL() {
		return cMODEL;
	}

	public void setcMODEL(String cMODEL) {
		this.cMODEL = cMODEL;
	}

	public String getManualType() {
		return manualType;
	}

	public void setManualType(String manualType) {
		this.manualType = manualType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}	
}
