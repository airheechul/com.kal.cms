package com.kal.cms.common.util.report;

import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

public class ContentHandlerForAirbusTSMMR extends CustomContentHandler{

	private boolean innerFLTDESC; //parent
	private boolean innerFLTMSG;
	
	private boolean innerCMSDESC; //parent
	private boolean innerCMSMSG;
	private boolean innerSOURCE;
	private boolean innerATACMS;
	
	private boolean innerREFINT; //parent
	private boolean innerEFFECT;
	
	
	String CATEG = "";
	String FLTMSG = "";
	String CMSMSG = "";
	String mSOURCE = "";
	String ATACMS = "";
	String CLLVL = "";
	
	String mDESC = "";
	String MR_TITLE;
	String REFINT = "";
	String mTASKREF = "";
	
	boolean bCMSDESC;
	String mRoute_No;
	
	public ContentHandlerForAirbusTSMMR(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
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

		if("FLTDESC".equalsIgnoreCase(localName)){
			// task data map 을 준비
			//taskMap = new HashMap<String, String>();
			//subTaskListMap = new LinkedHashMap<String, Map<String, String>>();

			if(attributes!=null){
				for(int i=0;i<attributes.getLength();i++){
					if("CATEG".equalsIgnoreCase(attributes.getLocalName(i)))
						CATEG = attributes.getValue(i);
				}
			}
			
			/*
           	For k = 0 To xNode.Attributes.length - 1
                Select Case UCase(xNode.Attributes(k).nodeName)
                       Case "CATEG"
                            CATEG = xNode.Attributes(k).Text
                End Select
            Next k
            
            For k = 0 To xNode.childNodes.length - 1
                Select Case UCase(xNode.childNodes(k).nodeName)
                       Case "FLTMSG"
                            FLTMSG = Replace(Trim(xNode.childNodes(k).Text), ",", "")
                End Select
            Next k
			 */
			
			innerFLTDESC = true;
		}
		
		if(innerFLTDESC && "FLTMSG".equalsIgnoreCase(localName)){
			innerFLTMSG = true;
		}
		
		if("CMSDESC".equalsIgnoreCase(localName)){
			innerCMSDESC = true;
			/*
          	Case "CMSDESC":
          
                'sDescription = Replace(Trim(CMSMSG), ",", "")
                For k = 0 To xNode.childNodes.length - 1
                    Select Case UCase(xNode.childNodes(k).nodeName)
                           Case "CMSMSG"
                                CMSMSG = Replace(Trim(xNode.childNodes(k).Text), ",", "")
                           Case "SOURCE"
                                mSOURCE = Replace(Trim(xNode.childNodes(k).Text), ",", "")
                           Case "ATACMS"
                                ATACMS = xNode.childNodes(k).Text
                    End Select
                Next k
			 */
		}
		
		if(innerCMSDESC && "CMSMSG".equalsIgnoreCase(localName)){
			innerCMSMSG = true;
		}
		
		if(innerCMSDESC && "SOURCE".equalsIgnoreCase(localName)){
			innerSOURCE = true;
		}
		
		if(innerCMSDESC && "ATACMS".equalsIgnoreCase(localName)){
			innerATACMS = true;
		}
		
		if("CLASS".equalsIgnoreCase(localName)){	
			if(attributes!=null){
				for(int i=0;i<attributes.getLength();i++){
					if("CLLVL".equalsIgnoreCase(attributes.getLocalName(i)))
						CLLVL = attributes.getValue(i);
				}
			}
			
	        bCMSDESC = true;
			/*
	        Case "CLASS":
	  
	        For k = 0 To xNode.Attributes.length - 1
	            Select Case UCase(xNode.Attributes(k).nodeName)
	                   Case "CLLVL"
	                        CLLVL = xNode.Attributes(k).Text
	            End Select
	        Next k
	        
	        
	        MR_TITLE = cMODEL & "_" & ATACMS & "-" & mSOURCE & "-" & CLLVL & "-" & CMSMSG
	        mDESC = FLTMSG & "_" & ATACMS & "-" & mSOURCE & "-" & CLLVL & "-" & CMSMSG
	        
	        bCMSDESC = True
			 */
		}
		
		if("REFINT".equalsIgnoreCase(localName)){
			if(bCMSDESC) {
				innerREFINT = true;
			}
		}
		
		if(innerREFINT && "EFFECT".equalsIgnoreCase(localName)){
			innerEFFECT = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			if(innerFLTDESC && "FLTMSG".equalsIgnoreCase(localName)){
				innerFLTMSG = false;
				innerFLTDESC = false;
			}
			
			if(innerCMSDESC && "CMSMSG".equalsIgnoreCase(localName)){
				innerCMSMSG = false;
			}
			
			if(innerCMSDESC && "SOURCE".equalsIgnoreCase(localName)){
				innerSOURCE = false;
			}
			
			if(innerCMSDESC && "ATACMS".equalsIgnoreCase(localName)){
				innerATACMS = false;
			}
			
			if("CMSDESC".equalsIgnoreCase(localName)){
				innerCMSDESC = false;
			}
			
			if(innerREFINT && "EFFECT".equalsIgnoreCase(localName)){
				innerEFFECT = false;
			}
			
			if("REFINT".equalsIgnoreCase(localName)){
				if(bCMSDESC) {
					writeCnt++;
			        MR_TITLE = report.getcMODEL() + "_" + ATACMS + "-" + mSOURCE + "-" + CLLVL + "-" + CMSMSG;
			        mDESC = FLTMSG + "_" + ATACMS + "-" + mSOURCE + "-" + CLLVL + "-" + CMSMSG;					
					
					mTASKREF = REFINT;
					String mRoute_No = report.getcMODEL() + "_" + mTASKREF;
                     
                    String data = convertPreChars(MR_TITLE.trim().replaceAll(",", "")) + ",,,Aircraft,Trouble Shooting," + CATEG + ",On,Unplanned," + rDate + ",,N,Next,First,,,,"
	                    + convertPreChars(mDESC.trim().replaceAll(",", "")) + ",,,,,,,,,,,,,,,,"
	                    + mRoute_No + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
					
					/*
                   	Case "REFINT":
                        
                        If bCMSDESC = True Then
                        
                           For k = 0 To xNode.childNodes.length - 1
                               Select Case UCase(xNode.childNodes(k).nodeName)
                                      Case "EFFECT"
                                           mTASKREF = xNode.Text
                               End Select
                           Next k
                           
                           mRoute_No = cMODEL & "_" & mTASKREF
                        
                           Print #fNumber, MR_TITLE & ",,,Aircraft,Trouble Shooting," & CATEG & ",On,Unplanned," & rDate & ",,N,Next,First,,,," & _
                                           mDESC & ",,,,,,,,,,,,,,,," & _
                                           mRoute_No & ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
                           bCMSDESC = False
                        End If
					 */
                    
					emitCsv(data);
					
					innerREFINT = false;
					bCMSDESC = false;
					mDESC = "";
					FLTMSG = "";
					CMSMSG = "";
					mSOURCE = "";
					ATACMS = "";
					REFINT = "";
					mTASKREF = "";
				}
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
		if(innerFLTMSG)
			FLTMSG = new String(ch, start, length);
		
		if(innerCMSMSG)
			CMSMSG += new String(ch, start, length);
		
		if(innerSOURCE)
			mSOURCE += new String(ch, start, length);

		if(innerATACMS)
			ATACMS += new String(ch, start, length);

		if(innerREFINT)
			REFINT = new String(ch, start, length);
		
		super.characters(ch, start, length);
	}
	
	//TODO Airbus_TSM_MR
	/*
	Public Sub Airbus_TSM_MR(ByRef Nodes As MSXML2.IXMLDOMNodeList, ByVal Indent As Integer)
	
	    Dim xNode As MSXML2.IXMLDOMNode
	    Dim k As Long
	    
	    Indent = Indent + 2
	    
	
	  ''  rEnigma_Publish_Date = Format(Date, "YYYYMMDD")
	    rDate = Format(Date, "YYYYMMDD")
	    
	''        Print #fNumber, "Title,Revision,Version_Number,Category,Program_Type,Program_Subtype,Service_Type,Implement_Status," & _
	''                        "Effective_From,Effective_To,Repetitive_Flag,Show_Repetitive,Whichever_First,Copy_Accomplishment_Flag,Preceding_MR_Title,Preceding_MR_Revision," & _
	''                        "Description,Comments,Down_Time,UOM,Billing_Item,QA_Inspection_Type,Space_Category,Auto_SignOff_Flag," & _
	''                        "Document_No,Revision_No,Chapter,Section,Subject,Page,Figure,Note,Route_Number," & _
	''                        "Revision_Number,Product_Type,Operator,Stage,Dependant,Route,Number,Revision,Dependency,MR_Visit_Type,Name,Item_Number," & _
	''                        "Position_Ref_Meaning,Position_Item_Number,PC_Node_Name,Exclude_Flag,Serial_Number_From,Serial_Number_To,Manufacturer," & _
	''                        "Manufacture_Date_From,Manufacture_Date_To,Country,Counter_Name,Interval_Value,Start_Value,Stop_Value,Start_Date,Stop_Date," & _
	''                        "Tolerance_Before,Tolerance_After,Reset_Value,Relationship,Related_MR_Title,Related_MR_Revision"
	
	   
	    For Each xNode In Nodes
	        
	        If xNode.nodeType = NODE_ELEMENT Then
	          
	           Select Case UCase(xNode.nodeName)
	                        
	                  Case "FLTDESC":
	                    
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "CATEG"
	                                        CATEG = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                        
	                        For k = 0 To xNode.childNodes.length - 1
	                            Select Case UCase(xNode.childNodes(k).nodeName)
	                                   Case "FLTMSG"
	                                        FLTMSG = Replace(Trim(xNode.childNodes(k).Text), ",", "")
	                            End Select
	                        Next k
	                        
	                        
	                  Case "CMSDESC":
	                  
	                        'sDescription = Replace(Trim(CMSMSG), ",", "")
	                        For k = 0 To xNode.childNodes.length - 1
	                            Select Case UCase(xNode.childNodes(k).nodeName)
	                                   Case "CMSMSG"
	                                        CMSMSG = Replace(Trim(xNode.childNodes(k).Text), ",", "")
	                                   Case "SOURCE"
	                                        mSOURCE = Replace(Trim(xNode.childNodes(k).Text), ",", "")
	                                   Case "ATACMS"
	                                        ATACMS = xNode.childNodes(k).Text
	                            End Select
	                        Next k
	                       
	                  Case "CLASS":
	                  
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "CLLVL"
	                                        CLLVL = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                        
	                        
	                        MR_TITLE = cMODEL & "_" & ATACMS & "-" & mSOURCE & "-" & CLLVL & "-" & CMSMSG
	                        mDESC = FLTMSG & "_" & ATACMS & "-" & mSOURCE & "-" & CLLVL & "-" & CMSMSG
	                        
	                        bCMSDESC = True
	                        
	                   Case "REFINT":
	                        
	                        If bCMSDESC = True Then
	                        
	                           For k = 0 To xNode.childNodes.length - 1
	                               Select Case UCase(xNode.childNodes(k).nodeName)
	                                      Case "EFFECT"
	                                           mTASKREF = xNode.Text
	                               End Select
	                           Next k
	                           
	                           mRoute_No = cMODEL & "_" & mTASKREF
	                        
	                           Print #fNumber, MR_TITLE & ",,,Aircraft,Trouble Shooting," & CATEG & ",On,Unplanned," & rDate & ",,N,Next,First,,,," & _
	                                           mDESC & ",,,,,,,,,,,,,,,," & _
	                                           mRoute_No & ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
	                           bCMSDESC = False
	                        End If
	
	           End Select
	                      
	        End If
	        
	        If xNode.hasChildNodes Then   '하위노드 존재시 재호출
	        
	           Airbus_TSM_MR xNode.childNodes, Indent
	           
	        End If
	        
	    Next xNode
	
	End Sub
	 */
}

