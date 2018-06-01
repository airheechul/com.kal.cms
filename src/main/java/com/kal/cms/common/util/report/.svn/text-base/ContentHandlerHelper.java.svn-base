package com.kal.cms.common.util.report;

import java.io.Writer;

import org.xml.sax.DTDHandler;

public class ContentHandlerHelper {
	DTDHandler dtdHandler; 
	String outputPath; 
	String csvPrefix; 
	ReportInfo report;
	Writer writer;
	
	CustomContentHandler contentHandler = null;
	
	public ContentHandlerHelper(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
		this.dtdHandler = dtdHandler;
		this.outputPath = outputPath;
		this.csvPrefix = csvPrefix;
		this.report = report;
		
		this.writer = writer;
	}
	
	public CustomContentHandler getInstance() throws Exception{
		String MODEL = report.getMODEL();
		String manualType = report.getManualType();
		String reportType = report.getReportType();
		
		if(MODEL.startsWith("B")) {
			//Boing
			if("AMM".equals(manualType)) {
				if("OP".equals(reportType)){
					report.setHeader(CsvHeader.AMM_OP_HEADER);
					
					contentHandler = new ContentHandlerForBoeingAMMOP(dtdHandler, outputPath, csvPrefix, report, writer);
				} else if("RT".equals(reportType)){
					report.setHeader(CsvHeader.AMM_RT_HEADER);
					
					contentHandler = new ContentHandlerForBoeingAMMRT(dtdHandler, outputPath, csvPrefix, report, writer);
				}
			} else if("TSM".equals(manualType) || "FRMFIM".equals(manualType)) {
				if("MR".equals(reportType)){
					report.setHeader(CsvHeader.TSMFRM_MR_HEADER);
					
					if("B737".equals(MODEL)){
						contentHandler = new ContentHandlerForBoeing737FRMMR(dtdHandler, outputPath, csvPrefix, report, writer);
					} else {
						contentHandler = new ContentHandlerForBoeing777FRMMR(dtdHandler, outputPath, csvPrefix, report, writer);
					}
				} else if("RT".equals(reportType)){
					report.setHeader(CsvHeader.TSMFRM_RT_HEADER);
					
					if("B737".equals(MODEL)){
						contentHandler = new ContentHandlerForBoeing737FRMRT(dtdHandler, outputPath, csvPrefix, report, writer);
					} else {
						contentHandler = new ContentHandlerForBoeing777FRMRT(dtdHandler, outputPath, csvPrefix, report, writer);
					}
				}
			}
		} else if(MODEL.startsWith("A")) {
			//Airbus
			if("AMM".equals(manualType)) {
				if("OP".equals(reportType)){
					report.setHeader(CsvHeader.AMM_OP_HEADER);
					
					contentHandler = new ContentHandlerForAirbusAMMOP(dtdHandler, outputPath, csvPrefix, report, writer);
				} else if("RT".equals(reportType)){
					report.setHeader(CsvHeader.AMM_RT_HEADER);
					
					contentHandler = new ContentHandlerForAirbusAMMRT(dtdHandler, outputPath, csvPrefix, report, writer);
				}
			} else if("TSM".equals(manualType) || "FRMFIM".equals(manualType)) {
				if("MR".equals(reportType)){
					report.setHeader(CsvHeader.TSMFRM_MR_HEADER);
					if("A330".equals(MODEL)){
						contentHandler = new ContentHandlerForAirbusTSMMR(dtdHandler, outputPath, csvPrefix, report, writer);
					} else {
						contentHandler = new ContentHandlerForAirbus380TSMMR(dtdHandler, outputPath, csvPrefix, report, writer);
					}
				} else if("RT".equals(reportType)){
					report.setHeader(CsvHeader.TSMFRM_RT_HEADER);
					
					contentHandler = new ContentHandlerForAirbusTSMRT(dtdHandler, outputPath, csvPrefix, report, writer);
				}
			}
		}
		
		return contentHandler;
	}
	
	class CsvHeader {
		public static final String AMM_RT_HEADER="Model,Enigma_Publish_Date,Enigma_Route_ID,Route_No,Title,Route_Type,Process,Product_Type,Operator_Name,Zone,Sub_Zone,Service_Item_Number,Accounting_Class,QA_Inspection_Type,Time_Span,Active_Start_Date,Active_End_Date,Revision_Number,Remarks,Revision_Notes,System,Concatenated_Segments,Revision_Number,Step,Check_Point_Flag,Document_No,Revision_No,Use latest revision,Chapter,Section,Subject,Page,Figure,Note,Resource_Type,ASO_Resource_Name,Quantity,Duration,Cost_Basis,Scheduled_Type,AutoCharge_Type,Standard_Rate,ASO_Resource_Name,Priority,Inventory_Org,Item_Group_Name,Item_Number,UOM,Quantity,Access_Panel,MC,MC_Revision,Org_Code,Item_Number,Item_Group_Name,Item_Number,Position_Path,UOM,Quantity,Replace_Percent,ReWork_Percent,Exclude_Flag,CL_Material_Flag,Page Block";

		public static final String AMM_OP_HEADER="Gubun,Model, Enigma_Op_ID, Route_No, Title, Step, GRP_OP, Operation_No,Description, Remarks, Chapter, Section, Subject";
		
		public static final String TSMFRM_RT_HEADER="Model,Revision Date,Enigma Key,Route_No,Title,Route_Type,Process,Product_Type,Operator_Name,Zone,Sub_Zone,Service_Item_Number,Accounting_Class,QA_Inspection_Type,Time_Span,Active_Start_Date,Active_End_Date,Revision_Number,Remarks,Revision_Notes,System,Concatenated_Segments,Revision_Number,Step,Check_Point_Flag,Document_No,Revision_No,Use latest revision,Chapter,Section,Subject,Page,Figure,Note,Resource_Type,ASO_Resource_Name,Quantity,Duration,Cost_Basis,Scheduled_Type,AutoCharge_Type,Standard_Rate,ASO_Resource_Name,Priority,Inventory_Org,Item_Group_Name,Item_Number,UOM,Quantity,Access_Panel,MC,MC_Revision,Org_Code,Item_Number,Item_Group_Name,Item_Number,Position_Path,UOM,Quantity,Replace_Percent,ReWork_Percent,Exclude_Flag,CL_Material_Flag,Page Block";

		public static final String TSMFRM_MR_HEADER="Title,Revision,Version_Number,Category,Program_Type,Program_Subtype,Service_Type,Implement_Status,Effective_From,Effective_To,Repetitive_Flag,Show_Repetitive,Whichever_First,Copy_Accomplishment_Flag,Preceding_MR_Title,Preceding_MR_Revision,Description,Comments,Down_Time,UOM,Billing_Item,QA_Inspection_Type,Space_Category,Auto_SignOff_Flag,Document_No,Revision_No,Chapter,Section,Subject,Page,Figure,Note,Route_Number,Revision_Number,Product_Type,Operator,Stage,Dependant,Route,Number,Revision,Dependency,MR_Visit_Type,Name,Item_Number,Position_Ref_Meaning,Position_Item_Number,PC_Node_Name,Exclude_Flag,Serial_Number_From,Serial_Number_To,Manufacturer,Manufacture_Date_From,Manufacture_Date_To,Country,Counter_Name,Interval_Value,Start_Value,Stop_Value,Start_Date,Stop_Date,Tolerance_Before,Tolerance_After,Reset_Value,Relationship,Related_MR_Title,Related_MR_Revision";
	}
}
