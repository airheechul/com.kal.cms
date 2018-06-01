package com.kal.cms.common.util.report;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

public class ContentHandlerForAirbusAMMRT extends CustomContentHandler{
	
	boolean innerTaskTitle = false;
	boolean taskTitleOnce = false;
	String taskTitle = "";

	boolean innerPageBlock = false;
	boolean innerPageBlockTitle = false;
	String pageBlockTitle = "";
	
	int rStep = 0;
	String rRoute_No;
    String rEnigma_Route_ID;
    String rSystem;
    String rChapter;
    String rSection;
    String rSubject;
    String rEnigma_Publish_Date;
    
    String rOperation_no;
    String SUBCHG;
	
	public ContentHandlerForAirbusAMMRT(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
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
		
		if("PGBLK".equalsIgnoreCase(localName)) {			
			innerPageBlock = true;
			pageBlockTitle = "";
		}		
		
		if(innerPageBlock && "TITLE".equalsIgnoreCase(localName)) {
			innerPageBlockTitle = true;
		}
		
		// task 노드에 진입
		if(elemNameTask.equalsIgnoreCase(localName)){
			innerTaskFlag = true;
			taskTitleOnce = true;

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

				//Route_No : 330_05-21-00-200-801-A
				if(tempMap.get("CONFLTR") == null || "".equals(tempMap.get("CONFLTR")))
					rRoute_No = report.getcMODEL() + "_" + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "-" + tempMap.get("FUNC") + "-" + tempMap.get("SEQ");
				else
					rRoute_No = report.getcMODEL() + "_" + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "-" + tempMap.get("FUNC") + "-" + tempMap.get("SEQ") + "-" + tempMap.get("CONFLTR") + (tempMap.get("VARNBR")==null?"":tempMap.get("VARNBR"));
				/*
                If TASKCONFLTR = "" Then
                   rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ
                Else
                   rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR & TASKVARNBR
                End If		
				*/

		        rEnigma_Route_ID = report.getENIGMA_MODEL() + "_AMM_" + tempMap.get("CHAPNBR") + "_" + tempMap.get("SECTNBR") + "_" + tempMap.get("SUBJNBR") + "_" + tempMap.get("PGBLKNBR") + "_" + tempMap.get("FUNC") + "_" + tempMap.get("SEQ") + "_" + tempMap.get("KEY");
		        rSystem = rRoute_No + ".........";
		        rChapter = tempMap.get("CHAPNBR");
		        rSection = tempMap.get("SECTNBR");
		        rSubject = tempMap.get("SUBJNBR");
		        rEnigma_Publish_Date = tempMap.get("REVDATE");
				rStep = 0;
				
				/*
		        rEnigma_Route_ID = ENIGMA_MODEL & "_AMM_" & TASKCHAPNBR & "_" & TASKSECTNBR & "_" & TASKSUBJNBR & "_" & TASKPGBLKNBR & "_" & TASKFUNC & "_" & TASKSEQ & "_" & TASKKEY
	            rSystem = rRoute_No & "........."
	            rChapter = TASKCHAPNBR
	            rSection = TASKSECTNBR
	            rSubject = TASKSUBJNBR
				*/
			}
		}
		
		if(innerTaskFlag && "TITLE".equalsIgnoreCase(localName)) {
			if(taskTitleOnce)
				innerTaskTitle = true;
		}

		//서브 task 내용 조회
		if(innerTaskFlag && elemNameSubTask.equalsIgnoreCase(localName)){
			innerSubTaskFlag = true;

			Map<String, String> tempMap = new HashMap<String, String>();

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
					else if("CONFLTR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("CONFLTR",attributes.getValue(i));
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
					else if("SUBCHG".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("SUBCHG",attributes.getValue(i));
					else if("VARNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						tempMap.put("VARNBR",attributes.getValue(i));					
					
				}
				// Operation_No : KE.330.AMM.05-21-00.869.050.A
				if(tempMap.get("CONFLTR") == null || "".equals(tempMap.get("CONFLTR"))){
					rOperation_no = "KE." + report.getcMODEL() + ".AMM." + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "." + tempMap.get("FUNC") + "." + tempMap.get("SEQ") + ".";
				} else {
					rOperation_no = "KE." + report.getcMODEL() + ".AMM." + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "." + tempMap.get("FUNC") + "." + tempMap.get("SEQ") + "." + tempMap.get("CONFLTR") + (tempMap.get("VARNBR")==null?"":tempMap.get("VARNBR"));
				}
				
				/*
				If SUBCONFLTR = "" Then
                    sOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "."
                Else
                    sOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "." & SUBCONFLTR & SUBVARNBR
                End If
				*/
				
				SUBCHG = tempMap.get("SUBCHG");
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		try {
			
			if(innerPageBlockTitle && "TITLE".equalsIgnoreCase(localName)){
				innerPageBlock = false;
				innerPageBlockTitle = false;
			}			
			
			// extract close.
			if(innerTaskTitle &&  "TITLE".equalsIgnoreCase(localName)){
				innerTaskTitle = false;
				taskTitleOnce = false;
				
				writeCnt++;
				
				// task 맵 데이터를 CSV 에 추가, 이전에 모든 데이터가 채워져야함.
                String data = report.getcMODEL() + "," + rEnigma_Publish_Date + "," + rEnigma_Route_ID + "," + rRoute_No + "," + getQuotString(convertPreChars(taskTitle)) + ","
		                + ",,,,,,,,,,"
		                + rDate + ","
		                + ",,,,"
		                + rSystem + ",,"
		                + ",,,,,,"
		                + rChapter + "," + rSection + "," + rSubject + ","
		                + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
		                + getQuotString(pageBlockTitle);
				
				emitCsv(data);
				
				/*
	           If xNode.parentNode.nodeName = "TASK" Then
               rRouteTitle = """" & Replace(xNode.Text, """", "'") & """"
               Print #fNumber, cMODEL & "," & rEnigma_Publish_Date & "," & rEnigma_Route_ID & "," & rRoute_No & "," & rRouteTitle & "," & _
                                ",,,,,,,,,," & _
                                rDate & "," & _
                                ",,,," & _
                                rSystem & ",," & _
                                ",,,,,," & _
                                rChapter & "," & rSection & "," & rSubject & "," & _
                                ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," & _
                                rPageBlock
				 */
				
				//testRowLimit(100, false); //TODO 테스트 코드 : 개발 편의상 TASK 한개만 일단 확인
				taskTitle = "";
			}
			
			if(elemNameTask.equalsIgnoreCase(localName)){
				innerTaskFlag = false;
			}	
			
			// extract close.
			if(innerTaskFlag && elemNameSubTask.equalsIgnoreCase(localName)){
				innerSubTaskFlag = false;
				
				if(!"D".equals(SUBCHG)) {
					writeCnt++;
	
					rStep += 10;
					
					String data = ",,,,,"
		                + ",,,,,,,,,,"
		                + ","
		                + ",,,,"
		                + "," + rOperation_no + ","
		                + ","
		                + rStep
		                + ",,,,"
		                + ",,,"
		                + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," 
		                + ",";
					
					emitCsv(data);
				}
				/*
		       If SUBCHG <> "D" Then
                    
                    istep = istep + 10
                    rStep = istep
                    
                    Print #fNumber, ",,,,," & _
                                    ",,,,,,,,,," & _
                                    "," & _
                                    ",,,," & _
                                    "," & rOperation_no & "," & _
                                    "," & _
                                    rStep & _
                                    ",,,," & _
                                    ",,," & _
                                    ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," & _
                                    ","
                End If
				*/
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
			taskTitle += new String(ch, start, length).replaceAll("\n", "").trim();
		}
		
		// page block title
		if(innerPageBlockTitle) {
			pageBlockTitle += new String(ch, start, length).replaceAll("\n", "").trim();
		}
		
		super.characters(ch, start, length);
	}
	
	//TODO 구 VB - Airbus_AMM_RT
	/* 
		 Public Sub Airbus_AMM_RT(ByRef Nodes As MSXML2.IXMLDOMNodeList, ByVal Indent As Integer)
	
	    Dim xNode As MSXML2.IXMLDOMNode
	    Dim k As Long
	    
	    Indent = Indent + 2
	    
	
	   ' rEnigma_Publish_Date = Format(Date, "YYYYMMDD")
	    rDate = Format(Date, "YYYYMMDD")
	    
	'                            Print #fNumber, "Model,Enigma_Publish_Date,Enigma_Route_ID,Route_No,Title," & _
	'                                            "Route_Type,Process,Product_Type,Operator_Name,Zone,Sub_Zone,Service_Item_Number,Accounting_Class,QA_Inspection_Type,Time_Span," & _
	'                                            "Active_Start_Date," & _
	'                                            "Active_End_Date,Revision_Number,Remarks,Revision_Notes," & _
	'                                            "System,Concatenated_Segments," & _
	'                                            "Revision_Number," & _
	'                                            "Step," & _
	'                                            "Check_Point_Flag,Document_No,Revision_No,Use latest revision," & _
	'                                            "Chapter,Section,Subject," & _
	'                                            "Page,Figure,Note,Resource_Type,ASO_Resource_Name,Quantity,Duration,Cost_Basis,Scheduled_Type,AutoCharge_Type,Standard_Rate,ASO_Resource_Name,Priority,Inventory_Org,Item_Group_Name,Item_Number,UOM,Quantity,Access_Panel,MC,MC_Revision,Org_Code,Item_Number,Item_Group_Name,Item_Number,Position_Path,UOM,Quantity,Replace_Percent,ReWork_Percent,Exclude_Flag,CL_Material_Flag," & _
	'                                            "Page Block"
	    
	    
	    For Each xNode In Nodes
	        
	        If xNode.nodeType = NODE_ELEMENT Then
	          
	           Select Case UCase(xNode.nodeName)
	           
	                  Case "TITLE":
	                  
	                        If xNode.parentNode.nodeName = "PGBLK" Then
	                           rPageBlock = """" & Replace(xNode.Text, """", "'") & """"
	                        End If
	                        If xNode.parentNode.nodeName = "TASK" Then
	                           rRouteTitle = """" & Replace(xNode.Text, """", "'") & """"
	                           Print #fNumber, cMODEL & "," & rEnigma_Publish_Date & "," & rEnigma_Route_ID & "," & rRoute_No & "," & rRouteTitle & "," & _
	                                            ",,,,,,,,,," & _
	                                            rDate & "," & _
	                                            ",,,," & _
	                                            rSystem & ",," & _
	                                            ",,,,,," & _
	                                            rChapter & "," & rSection & "," & rSubject & "," & _
	                                            ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," & _
	                                            rPageBlock
	
	                        End If
	                        
	                  Case "TASK":
	                  
	                        istep = 0
	                        
	                        TASKVARNBR = ""
	                        
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "FUNC"
	                                        TASKFUNC = xNode.Attributes(k).Text
	                                   Case "SEQ"
	                                        TASKSEQ = xNode.Attributes(k).Text
	                                   Case "CONFLTR"
	                                        TASKCONFLTR = xNode.Attributes(k).Text
	                                   Case "SECTNBR"
	                                        TASKSECTNBR = xNode.Attributes(k).Text
	                                   Case "CHAPNBR"
	                                        TASKCHAPNBR = xNode.Attributes(k).Text
	                                   Case "SUBJNBR"
	                                        TASKSUBJNBR = xNode.Attributes(k).Text
	                                   Case "KEY"
	                                        TASKKEY = xNode.Attributes(k).Text
	                                   Case "PGBLKNBR"
	                                        TASKPGBLKNBR = xNode.Attributes(k).Text
	                                   Case "VARNBR"
	                                        TASKVARNBR = xNode.Attributes(k).Text
	                                   Case "REVDATE"
	                                        rEnigma_Publish_Date = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                        
	                        If TASKVARNBR = "" Then
	                           rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR
	                        Else
	                           rRoute_No = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR & TASKVARNBR
	                        End If
	                        
	                        rEnigma_Route_ID = MODEL & "_AMM_" & TASKCHAPNBR & "_" & TASKSECTNBR & "_" & TASKSUBJNBR & "_" & TASKPGBLKNBR & "_" & TASKFUNC & "_" & TASKSEQ & "_" & TASKKEY
	                        rSystem = rRoute_No & "........."
	                        rChapter = TASKCHAPNBR
	                        rSection = TASKSECTNBR
	                        rSubject = TASKSUBJNBR
	                        
	                  Case "SUBTASK":
	                       
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "FUNC"
	                                        SUBFUNC = xNode.Attributes(k).Text
	                                   Case "SEQ"
	                                        SUBSEQ = xNode.Attributes(k).Text
	                                   Case "CONFLTR"
	                                        SUBCONFLTR = xNode.Attributes(k).Text
	                                   Case "SECTNBR"
	                                        SUBSECTNBR = xNode.Attributes(k).Text
	                                   Case "CHAPNBR"
	                                        SUBCHAPNBR = xNode.Attributes(k).Text
	                                   Case "SUBJNBR"
	                                        SUBSUBJNBR = xNode.Attributes(k).Text
	                                   Case "KEY"
	                                        SUBKEY = xNode.Attributes(k).Text
	                                   Case "CHG"
	                                        SUBCHG = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                        
	                        rOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "." & SUBCONFLTR
	                                                
	                        If SUBCHG <> "D" Then
	                        
	                            istep = istep + 10
	                            rStep = istep
	                        
	                            Print #fNumber, ",,,,," & _
	                                            ",,,,,,,,,," & _
	                                            "," & _
	                                            ",,,," & _
	                                            "," & rOperation_no & "," & _
	                                            "," & _
	                                            rStep & _
	                                            ",,,," & _
	                                            ",,," & _
	                                            ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," & _
	                                            ","
	                        End If
	
	           End Select
	                      
	        End If
	        
	        If xNode.hasChildNodes Then   '하위노드 존재시 재호출
	        
	           Airbus_AMM_RT xNode.childNodes, Indent
	           
	        End If
	        
	    Next xNode
	
	End Sub
	 */
}

