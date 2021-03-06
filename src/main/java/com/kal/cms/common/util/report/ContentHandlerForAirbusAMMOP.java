package com.kal.cms.common.util.report;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;


public class ContentHandlerForAirbusAMMOP extends CustomContentHandler{
	
	boolean innerTaskTitle = false;
	boolean taskTitleOnce = false;
	String taskTitle = "";

	boolean innerPARA = false;
	String sDescription = "";
	
	int sStep = 0;
	String sRoute_no;
	String sGrp_Op;
	String sREVDATE;
	String sEnigma_op_id;
	String sOperation_no;
	
	String sChapter;
	String sSection;
	String sSubject;
	
	String SUBCHG;
    
    int iParaCnt = 0;
	
	public ContentHandlerForAirbusAMMOP(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
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
			innerTaskFlag = true;
			
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
				
				if(tempMap.get("VARNBR") == null || "".equals(tempMap.get("VARNBR"))){
					sRoute_no = report.getcMODEL() + "_" + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "-" + tempMap.get("FUNC") + "-" + tempMap.get("SEQ")+ "-" + tempMap.get("CONFLTR");
					sGrp_Op = "KE." + report.getcMODEL() + ".GRP." + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "." + tempMap.get("FUNC") + "." + tempMap.get("SEQ") + "." + tempMap.get("CONFLTR");
				} else {
	                sRoute_no = report.getcMODEL() + "_" + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "-" + tempMap.get("FUNC") + "-" + tempMap.get("SEQ") + "-" + tempMap.get("CONFLTR") + (tempMap.get("VARNBR")==null?"":tempMap.get("VARNBR"));
	                sGrp_Op = "KE." + report.getcMODEL() + ".GRP." + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "." + tempMap.get("FUNC") + "." + tempMap.get("SEQ") + "." + tempMap.get("CONFLTR") + (tempMap.get("VARNBR")==null?"":tempMap.get("VARNBR"));
				}

				taskTitleOnce = true;
				sStep = 0;
				/*
                If TASKVARNBR = "" Then
                   sRoute_no = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR
                   sGrp_Op = "KE." & cMODEL & ".GRP." & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "." & TASKFUNC & "." & TASKSEQ & "." & TASKCONFLTR
                Else
                   sRoute_no = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR & TASKVARNBR
                   sGrp_Op = "KE." & cMODEL & ".GRP." & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "." & TASKFUNC & "." & TASKSEQ & "." & TASKCONFLTR & TASKVARNBR
                End If
                
                sStep = 0
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
						SUBCHG = attributes.getValue(i);			
					
				}
				
				sEnigma_op_id = "AMM" + tempMap.get("KEY");
				
				// Operation_No : KE.330.AMM.05-21-00.869.050.A
				sOperation_no = ("KE." + report.getcMODEL() + ".AMM." + tempMap.get("CHAPNBR") + "-" + tempMap.get("SECTNBR") + "-" + tempMap.get("SUBJNBR") + "." + tempMap.get("FUNC") + "." + tempMap.get("SEQ") + "."+tempMap.get("CONFLTR")).replaceAll("\\p{Space}", "").replaceAll("\n","");
				//VB : sOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "." & SUBCONFLTR
				
				sREVDATE = tempMap.get("REVDATE"); 
	            sChapter = "'"+tempMap.get("CHAPNBR");
	            sSection = "'"+tempMap.get("SECTNBR");
	            sSubject = "'"+tempMap.get("SUBJNBR");
	            
	            iParaCnt = 0;
				
				/*
		        sEnigma_op_id = "AMM" & SUBKEY
	            If SUBCONFLTR = "" Then
	                sOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "."
	            Else
	                sOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "." & SUBCONFLTR & SUBVARNBR
	            End If
	            
	            sChapter = "'" & SUBCHAPNBR
	            sSection = "'" & SUBSECTNBR
	            sSubject = "'" & SUBSUBJNBR
	            
	            iParaCnt = 0
				*/
			}
		}

		if(innerSubTaskFlag && "PARA".equalsIgnoreCase(localName)) {
			innerPARA = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		try {
			if(innerTaskTitle && "TITLE".equalsIgnoreCase(localName)) {
				innerTaskTitle = false;
				taskTitleOnce = false;
			}

			if(elemNameTask.equalsIgnoreCase(localName)){
				innerTaskFlag = false;
			}			
			
			if(innerPARA && "PARA".equalsIgnoreCase(localName)) {
				innerPARA = false;
				innerSubTaskFlag = false;
				
                if (!"D".equals(SUBCHG) && iParaCnt == 0 ){
                	
                	writeCnt++;
                	iParaCnt = iParaCnt + 1;
                	
                    sStep += 10;
                    sDescription = convertPreChars(sDescription.trim().replaceAll(",", ""));
                    
                    String data = rDateYYMM+ "," + report.getcMODEL() + "," + sEnigma_op_id + "," + sRoute_no + "," +  (taskTitle==null||taskTitle.equals("")?"":getQuotString(taskTitle)) + "," + sStep + "," + sGrp_Op + ","
		                    + sOperation_no + "," +  sDescription + "," + sREVDATE + ","
		                    + sChapter + "," + sSection + "," + sSubject;
                    emitCsv(data);
                }

                
                sDescription = "";
                taskTitle = "";
				
				/*
			       Case "PARA":
	                   
	                iParaCnt = iParaCnt + 1
	                
	                If bSubTask = True And SUBCHG <> "D" And iParaCnt = 1 Then
	                   sStep = sStep + 10
	                   sDescription = Replace(Trim(xNode.Text), ",", "")
	                   
	                   Print #fNumber, sGubun & "," & cMODEL & "," & sEnigma_op_id & "," & sRoute_no & "," & rRouteTitle & "," & sStep & "," & sGrp_Op & "," & _
	                                   sOperation_no & "," & sDescription & "," & sREVDATE & "," & _
	                                   sChapter & "," & sSection & "," & sSubject
	                   bSubTask = False
	                   sDescription = ""
	                
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
		if(innerPARA) {
			sDescription += new String(ch, start, length).replaceAll("\n","").trim();
		}
		
		super.characters(ch, start, length);
	}
	
	//TODO 구 VB - Airbus_AMM_OP
	/*
	Public Sub Airbus_AMM_OP(ByRef Nodes As MSXML2.IXMLDOMNodeList, ByVal Indent As Integer)
	
	    Dim xNode As MSXML2.IXMLDOMNode
	    Dim k As Long, iParaCnt As Integer
	
	
	    Indent = Indent + 2
	    
	    sGubun = Format(Date, "YYYYMM")
	    
	'    Print #fNumber, "Gubun, Model, Enigma_Op_ID, Route_No, Title, Step, GRP_OP, Operation_No," & _
	'                    "Description, Remarks, Chapter, Section, Subject"
	
	    
	    For Each xNode In Nodes
	        
	        If xNode.nodeType = NODE_ELEMENT Then
	          
	           Select Case UCase(xNode.nodeName)
	              
	                  Case "TITLE":
	                        
	                        If xNode.parentNode.nodeName = "TASK" Then
	                           rRouteTitle = """" & Replace(xNode.Text, """", "'") & """"
	                        End If
	                        
	                  Case "TASK":
	                  
	                        TASKVARNBR = ""
	                        sStep = 0
	                        
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
	                                        sREVDATE = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                        
	                        'sRoute_no = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR
	                        'sGrp_Op = "KE." & cMODEL & ".GRP." & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "." & TASKFUNC & "." & TASKSEQ & "." & TASKCONFLTR & "."
	                        
	                        If TASKVARNBR = "" Then
	                           sRoute_no = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR
	                           sGrp_Op = "KE." & cMODEL & ".GRP." & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "." & TASKFUNC & "." & TASKSEQ & "." & TASKCONFLTR
	                        Else
	                           sRoute_no = cMODEL & "_" & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "-" & TASKFUNC & "-" & TASKSEQ & "-" & TASKCONFLTR & TASKVARNBR
	                           sGrp_Op = "KE." & cMODEL & ".GRP." & TASKCHAPNBR & "-" & TASKSECTNBR & "-" & TASKSUBJNBR & "." & TASKFUNC & "." & TASKSEQ & "." & TASKCONFLTR & TASKVARNBR
	                        End If
	                       
	                        
	                  Case "SUBTASK":
	                  
	                        bSubTask = True
	                        
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
	                        
	                        sEnigma_op_id = "AMM" & SUBKEY
	                        sOperation_no = "KE." & cMODEL & ".AMM." & SUBCHAPNBR & "-" & SUBSECTNBR & "-" & SUBSUBJNBR & "." & SUBFUNC & "." & SUBSEQ & "." & SUBCONFLTR
	                        sChapter = "'" & SUBCHAPNBR
	                        sSection = "'" & SUBSECTNBR
	                        sSubject = "'" & SUBSUBJNBR
	                                               
	                        iParaCnt = 0
	                  
	                  Case "PARA":
	                           
	                        iParaCnt = iParaCnt + 1
	                                                
	                        If bSubTask = True And SUBCHG <> "D" And iParaCnt = 1 Then
	                           sStep = sStep + 10
	                           sDescription = Replace(Trim(xNode.Text), ",", "")
	                           
	                           Print #fNumber, sGubun & "," & cMODEL & "," & sEnigma_op_id & "," & sRoute_no & "," & rRouteTitle & "," & sStep & "," & sGrp_Op & "," & _
	                                           sOperation_no & "," & sDescription & "," & sREVDATE & "," & _
	                                           sChapter & "," & sSection & "," & sSubject
	                           bSubTask = False
	                           sDescription = ""
	                           rRouteTitle = ""
	                          
	                        End If
	
	           End Select
	                      
	        End If
	        
	        If xNode.hasChildNodes Then   '하위노드 존재시 재호출
	           If xNode.nodeName = "CHGDESC" Or xNode.nodeName = "EFFECT" Then
	           Else
	              Airbus_AMM_OP xNode.childNodes, Indent
	           End If
	        End If
	        
	        
	    Next xNode
	   
	    If bSubTask = True And SUBCHG <> "D" Then   'PARA 없이 끝날때 SUBTASK 찍어줌
	       sStep = sStep + 10
	       Print #fNumber, sGubun & "," & cMODEL & "," & sEnigma_op_id & "," & sRoute_no & "," & rRouteTitle & "," & sStep & "," & sGrp_Op & "," & _
	                       sOperation_no & "," & sDescription & "," & sREVDATE & "," & _
	                       sChapter & "," & sSection & "," & sSubject
	        bSubTask = False
	    End If
	End Sub
	 */
}

