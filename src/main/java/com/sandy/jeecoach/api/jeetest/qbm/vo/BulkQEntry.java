package com.sandy.jeecoach.api.jeetest.qbm.vo;

import java.util.ArrayList ;
import java.util.List ;

import com.sandy.jeecoach.api.jeetest.qbm.helper.BulkQuestionEntryHelper.FileInfo ;
import com.sandy.jeecoach.dao.entity.master.Topic ;

import lombok.Data ;

@Data
public class BulkQEntry {

    private String  qRef         = "" ;
    private String  questionType = "SCA" ;
    private String  ansText      = "" ;
    private Integer latLevel     = 3 ;
    private Integer projTime     = 120 ;
    private Boolean saved        = false ;
    private Topic   topic        = null ;
    
    private List<String> imgPaths  = new ArrayList<>() ;
    private List<String> imgNames  = new ArrayList<>() ;
    
    public BulkQEntry() {
    }
    
    public BulkQEntry( FileInfo fi ) {
        this.qRef = fi.qRef ;
        prePopulateAttributesBasedOnDeducedQType( fi.qRef ) ;
    }
    
    private void prePopulateAttributesBasedOnDeducedQType( String qRef ) {
        this.latLevel = 3 ;
        this.projTime = 120 ;
        
        if( qRef.contains( "/SCA/" ) || 
            qRef.contains( "/ART/" ) ) {
            this.questionType = "SCA" ;
        }
        else if( qRef.contains( "/MCA/" ) || 
                 qRef.contains( "/MCQ/" ) ) {
            this.questionType = "MCA" ;
            this.projTime = 240 ;
        }
        else if( qRef.contains( "/MMT/" ) ||
                 qRef.contains( "/MLT/" ) || 
                 qRef.contains( "/CMT/" ) ) {
            this.questionType = "MMT" ;
            this.projTime = 240 ;
        }
        else if( qRef.contains( "/LCT/" ) ) {
            this.questionType = "LCT" ;
            this.projTime = 180 ;
        }
        else if( qRef.contains( "/NT/" ) ) {
            this.questionType = "NT" ;
            this.projTime = 240 ;
        }
        else {
            this.questionType = "SCA" ;
        }
    }
}
