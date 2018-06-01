package com.kal.cms.common.util.report;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

public class ContentHandlerForBoeing777FRMRT extends CustomContentHandler{
	
	boolean innerTaskTitle = false;
	String taskTitle = "";
	String REVDATE;
	String Enigma_Key;
	String Route_No;
	String System;
	
	public ContentHandlerForBoeing777FRMRT(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
		super(dtdHandler, outputPath, csvPrefix, report, writer);
	}

	@Override
	public void startDocument()	{		
		super.startDocument();
	}
	
	@Override
	public void endDocument(){
		super.endDocument();
	}	
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		// task 노드에 진입
		if(elemNameTask.equalsIgnoreCase(localName)){
			// task data map 을 준비
			// 기본 데이터를 셋팅한다.
			Map<String, String> tempMap = new HashMap<String, String>();
			
			// attributes 있으면 task 에 필요한 데이터를 챙긴다.
			if(attributes!=null){
				//CHAPNBR="21" SECTNBR="43" SUBJNBR="00" FUNC="812" SEQ="802" CONFLTR="A" PGBLKNBR="06" CONFNBR="00" CHG="U" KEY="EN05213020080300" REVDATE="20120101"

				for(int i=0;i<attributes.getLength();i++){
					if("CHAPNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("CHAPNBR",attributes.getValue(i));
					else if("SECTNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("SECTNBR",attributes.getValue(i));
					else if("SUBJNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("SUBJNBR",attributes.getValue(i));
					else if("FUNC".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("FUNC",attributes.getValue(i));
					else if("SEQ".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("SEQ",attributes.getValue(i));
					else if("PGBLKNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("PGBLKNBR",attributes.getValue(i));
					else if("CONFNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("CONFNBR",attributes.getValue(i));
					else if("CHG".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("CHG",attributes.getValue(i));
					else if("KEY".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("KEY",attributes.getValue(i));
					else if("REVDATE".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("REVDATE",attributes.getValue(i));
					else if("VARNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("VARNBR",attributes.getValue(i));
					else if("CONFLTR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("CONFLTR",attributes.getValue(i));
				}
				String RN = "";
				//Route_No : 330_05-21-00-200-801-A
				if(tempMap.get("CONFLTR") == null || "".equals(tempMap.get("CONFLTR")))
					RN = report.getcMODEL() + "_" + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "-" + tempMap.get("FUNC") + "-" + tempMap.get("SEQ");
				else
	             	RN = report.getcMODEL() + "_" + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "-" + tempMap.get("FUNC") + "-" + tempMap.get("SEQ") + "-" + tempMap.get("CONFLTR") + (tempMap.get("VARNBR")==null?"":tempMap.get("VARNBR"));
				/*
                If TASKCONFLTR = "" Then
                rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ
	             Else
	                rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR & TASKVARNBR
	             End If
	             rSystem = rRoute_No & "........."				
				*/
				
				//셋팅
				REVDATE = tempMap.get("REVDATE");
				Enigma_Key = tempMap.get("KEY");
				Route_No = RN;
				System = RN.concat(".........");
			}
			
			innerTaskFlag = true;
		}
		
		if(innerTaskFlag && "TITLE".equalsIgnoreCase(localName)) {
			innerTaskTitle = true;
		}		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		try {
			// extract close.
			if(innerTaskTitle &&  "TITLE".equalsIgnoreCase(localName)){
				innerTaskTitle = false;
				innerTaskFlag = false;
				writeCnt++;

				// task 맵 데이터를 CSV 에 추가, 이전에 모든 데이터가 채워져야함.
				
				String data = report.getcMODEL() + "," + REVDATE + "," + Enigma_Key + "," + Route_No + "," + getQuotString(convertPreChars(taskTitle.replaceAll(",", ""))) + ","
					+ ",,,,,,,,,,"
					+ rDate + ","
					+ ",,,," 
					+ System + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
				
				emitCsv(data);
				
				
				taskTitle = "";
				//testRowLimit(100, false); //TODO 테스트 코드 : 개발 편의상 TASK 한개만 일단 확인
			}
			
		} catch (Exception se) {
			try {
				if(this.writer!=null)try{writer.close();}catch(Exception ne){}
			} catch (Exception ne) {}
			
			se.printStackTrace();
			
			throw new SAXException();
		} 
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// task title
		if(innerTaskTitle) {
			taskTitle += new String(ch, start, length).trim();
		}		
		
		super.characters(ch, start, length);
	}
	
	/* TODO Boing_FRM_RT_777
	Public Sub Boing_FRM_RT_777(ByRef Nodes As MSXML2.IXMLDOMNodeList, ByVal Indent As Integer)
	
	    Dim xNode As MSXML2.IXMLDOMNode
	    Dim k As Long
	    
	    Indent = Indent + 2
	    
	
	    'rEnigma_Publish_Date = Format(Date, "YYYYMMDD")
	    rDate = Format(Date, "YYYYMMDD")
	    
	''        Print #fNumber, "Model,Revision Date,Enigma Key,Route_No,Title,Route_Type,Process,Product_Type,Operator_Name,Zone," & _
	''                                             "Sub_Zone,Service_Item_Number,Accounting_Class,QA_Inspection_Type,Time_Span,Active_Start_Date," & _
	''                                             "Active_End_Date,Revision_Number,Remarks,Revision_Notes,System,Concatenated_Segments,Revision_Number," & _
	''                                             "Step,Check_Point_Flag,Document_No,Revision_No,Use latest revision,Chapter,Section,Subject,Page,Figure," & _
	''                                             "Note,Resource_Type,ASO_Resource_Name,Quantity,Duration,Cost_Basis,Scheduled_Type,AutoCharge_Type," & _
	''                                             "Standard_Rate,ASO_Resource_Name,Priority,Inventory_Org,Item_Group_Name,Item_Number,UOM,Quantity," & _
	''                                             "Access_Panel,MC,MC_Revision,Org_Code,Item_Number,Item_Group_Name,Item_Number,Position_Path,UOM,Quantity," & _
	''                                             "Replace_Percent,ReWork_Percent,Exclude_Flag,CL_Material_Flag,Page Block"
	    
	    For Each xNode In Nodes
	        
	        If xNode.nodeType = NODE_ELEMENT Then
	          
	           Select Case UCase(xNode.nodeName)
	           
	                  Case "TITLE":
	                        
	                        If xNode.parentNode.nodeName = "TASK" Then
	                           rRouteTitle = """" & Replace(xNode.Text, """", "'") & """"
	                           Print #fNumber, cMODEL & "," & TASKDATE & "," & TASKKEY & "," & rRoute_No & "," & rRouteTitle & "," & _
	                                            ",,,,,,,,,," & _
	                                            rDate & "," & _
	                                            ",,,," & _
	                                            rSystem & ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
	
	                        End If
	                        
	                  Case "TASK":
	                  
	                        TASKVARNBR = ""
	                        TASKCONFLTR = ""
	                        
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "CHAPNBR"
	                                        TASKCHAPNBR = xNode.Attributes(k).Text
	                                   Case "SECTNBR"
	                                        TASKSECTNBR = xNode.Attributes(k).Text
	                                   Case "SUBJNBR"
	                                        TASKSUBJNBR = xNode.Attributes(k).Text
	                                   Case "FUNC"
	                                        TASKFUNC = xNode.Attributes(k).Text
	                                   Case "SEQ"
	                                        TASKSEQ = xNode.Attributes(k).Text
	                                   Case "CONFLTR"
	                                        TASKCONFLTR = xNode.Attributes(k).Text
	                                   Case "VARNBR"
	                                        TASKVARNBR = xNode.Attributes(k).Text
	                                   'Case "KEY"
	                                   '     TASKKEY = xNode.Attributes(k).Text
	                                   Case "REVDATE"
	                                        TASKDATE = xNode.Attributes(k).Text
	                                        
	                            End Select
	                        Next k
	                        
	                        If TASKCONFLTR = "" Then
	                           rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ
	                        Else
	                           rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR & TASKVARNBR
	                        End If
	                        rSystem = rRoute_No & "........."
	                       
	           End Select
	                      
	        End If
	        
	        If xNode.hasChildNodes Then   '하위노드 존재시 재호출
	        
	           Boing_FRM_RT_777 xNode.childNodes, Indent
	           
	        End If
	        
	    Next xNode
	
	End Sub
	 */
}

