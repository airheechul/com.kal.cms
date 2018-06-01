package com.kal.cms.common.util.report;

import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.SAXException;

public class ContentHandlerForBoeing737FRMMR extends CustomContentHandler{

	private boolean innerFALTCODE;
	private boolean innerFKEYTEXT;
	
	private boolean innerMAINTMSG;
	private boolean innerMMSGDATA;
	
	private boolean innerREFINT;
	
	String FAULTYPE;
	String FMSGLVL;
	String FKEYTEXT;
	String MMSGNBR;
	
	String mDESC = "";
	String MR_TITLE;
	String REFINT;
	
	boolean bCMSDESC;
	String mRoute_No;
	
	public ContentHandlerForBoeing737FRMMR(DTDHandler dtdHandler, String outputPath, String csvPrefix, ReportInfo report, Writer writer){
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

		if("FALTCODE".equalsIgnoreCase(localName)){
			innerFALTCODE = true;
		}
		
		if("FALTDESC".equalsIgnoreCase(localName)){
			// task data map 을 준비
			if(attributes!=null){
				for(int i=0;i<attributes.getLength();i++){
					if("FMSGLVL".equalsIgnoreCase(attributes.getLocalName(i)))
						FMSGLVL = attributes.getValue(i);
				}
			}
		}
		
		if("FKEYTEXT".equalsIgnoreCase(localName)){
			innerFKEYTEXT = true;
		}
		
		if("MAINTMSG".equalsIgnoreCase(localName)){
			innerMAINTMSG = true;
			
			
			if(attributes!=null){
				for(int i=0;i<attributes.getLength();i++){
					if("MMSGNBR".equalsIgnoreCase(attributes.getLocalName(i)))
						MMSGNBR = attributes.getValue(i);
				}
			}
			/*
            For k = 0 To xNode.Attributes.length - 1
            Select Case UCase(xNode.Attributes(k).nodeName)
                   Case "MMSGNBR"
                        MMSGNBR = xNode.Attributes(k).Text
            End Select
	        Next k
	  
	        For k = 0 To xNode.childNodes.length - 1
	            Select Case UCase(xNode.childNodes(k).nodeName)
	                   Case "MMSGDATA"
	                       mDESC = Replace(Trim(xNode.Text), ",", "")
	            End Select
	        Next k
	        
	        MR_TITLE = cMODEL & "_" & FAULTYPE & "_" & FKEYTEXT & "_" & FMSGLVL & "_" & MMSGNBR
	        
	        bCMSDESC = True
	        
	        */
		}
		
		if(innerMAINTMSG && "MMSGDATA".equalsIgnoreCase(localName)){
			innerMMSGDATA = true;
		}
		
		if("REFINT".equalsIgnoreCase(localName)){
			if(bCMSDESC) {
				innerREFINT = true;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {
			
			if("FALTCODE".equalsIgnoreCase(localName)){
				innerFALTCODE = false;
			}
			
			if("FKEYTEXT".equalsIgnoreCase(localName)){
				innerFKEYTEXT = false;
			}

			if("MAINTMSG".equalsIgnoreCase(localName)){
				innerMAINTMSG = false;
				innerMMSGDATA = false;
				
		        MR_TITLE = report.getcMODEL()+"_"+FAULTYPE+"_"+FKEYTEXT+"_"+FMSGLVL+"_"+MMSGNBR;
		        
		        bCMSDESC = true;
			}
			
			if("REFINT".equalsIgnoreCase(localName)){
				if(bCMSDESC) {
					
					mRoute_No = REFINT;
                    
                    String mATA = mRoute_No.substring(mRoute_No.length()-2);
                    
                    if (mRoute_No.startsWith("7") || mRoute_No.startsWith("8"))
                       mRoute_No = mRoute_No + "-F00";
                    
                    mRoute_No = report.getcMODEL() + "_" + mRoute_No.replaceAll(" TASK ", "-00-810-");
                    
                    String data = convertPreChars(MR_TITLE.replaceAll(",", "")) + ",,,Aircraft,Trouble Shooting,,On,Unplanned," + rDate + ",,N,Next,First,,,,"
                    	+ mDESC + ",,,,,,,,,,,,,,,,"
                    	+ mRoute_No + ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
                    emitCsv(data);

					/*
					Case "REFINT":
	                        
	                        If bCMSDESC = True Then
	                           mRoute_No = xNode.Text
	                           
	                           mATA = ""
	                           mATA = Left(mRoute_No, 1)
	                           
	                           If (mATA = "7") Or (mATA = "8") Then
	                              mRoute_No = mRoute_No & "-F00"
	                           End If
	                           
	                           mRoute_No = cMODEL & "_" & Replace(Trim(mRoute_No), " TASK ", "-00-810-")
	                           
	                           Print #fNumber, MR_TITLE & ",,,Aircraft,Trouble Shooting,,On,Unplanned," & rDate & ",,N,Next,First,,,," & _
	                                           mDESC & ",,,,,,,,,,,,,,,," & _
	                                           mRoute_No & ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
	                           bCMSDESC = False
	                        End If
					 */

					innerREFINT = false;
					bCMSDESC = false;
					mDESC = "";
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
		if(innerFALTCODE)
			FAULTYPE = new String(ch, start, length);
		
		if(innerFKEYTEXT)
			FKEYTEXT = new String(ch, start, length);
		
		if(innerREFINT)
			REFINT = new String(ch, start, length);
		
		if(innerMMSGDATA) {
			mDESC += new String(ch, start, length);
		} 
		
		super.characters(ch, start, length);
	}
	
	/* TODO
	Public Sub Boing_FRM_MR_737(ByRef Nodes As MSXML2.IXMLDOMNodeList, ByVal Indent As Integer)
	
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
	                        
	                  Case "FALTCODE":
	                  
	                        FAULTYPE = xNode.Text
	                        
	                  Case "FALTDESC":
	
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "FMSGLVL"
	                                        FMSGLVL = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                        
	                  Case "FKEYTEXT":
	                        
	                        FKEYTEXT = Replace(Trim(xNode.Text), ",", "")
	                        
	''                  Case "FSUBPROB":
	''
	''                        FMSGLVL = Replace(Trim(xNode.Text), ",", "")
	                        
	                  Case "MAINTMSG":
	                  
	                        For k = 0 To xNode.Attributes.length - 1
	                            Select Case UCase(xNode.Attributes(k).nodeName)
	                                   Case "MMSGNBR"
	                                        MMSGNBR = xNode.Attributes(k).Text
	                            End Select
	                        Next k
	                  
	                        For k = 0 To xNode.childNodes.length - 1
	                            Select Case UCase(xNode.childNodes(k).nodeName)
	                                   Case "MMSGDATA"
	                                       mDESC = Replace(Trim(xNode.Text), ",", "")
	                            End Select
	                        Next k
	                        
	                        MR_TITLE = cMODEL & "_" & FAULTYPE & "_" & FKEYTEXT & "_" & FMSGLVL & "_" & MMSGNBR
	                        
	                        bCMSDESC = True
	                        
	                   Case "REFINT":
	                        
	                        If bCMSDESC = True Then
	                           mRoute_No = xNode.Text
	                           
	                           mATA = ""
	                           mATA = Left(mRoute_No, 1)
	                           
	                           If (mATA = "7") Or (mATA = "8") Then
	                              mRoute_No = mRoute_No & "-F00"
	                           End If
	                           
	                           mRoute_No = cMODEL & "_" & Replace(Trim(mRoute_No), " TASK ", "-00-810-")
	                           
	                           Print #fNumber, MR_TITLE & ",,,Aircraft,Trouble Shooting,,On,Unplanned," & rDate & ",,N,Next,First,,,," & _
	                                           mDESC & ",,,,,,,,,,,,,,,," & _
	                                           mRoute_No & ",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"
	                           bCMSDESC = False
	                        End If
	
	           End Select
	                      
	        End If
	        
	        If xNode.hasChildNodes Then   '하위노드 존재시 재호출
	        
	           Boing_FRM_MR_737 xNode.childNodes, Indent
	           
	        End If
	        
	    Next xNode
	
	End Sub
	 */
}

