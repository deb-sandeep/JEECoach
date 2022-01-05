package com.sandy.jeecoach.api.jeetest.qbm.vo;

import static com.sandy.jeecoach.JEECoach.JEETEST_IMG_DIR ;

import java.io.File ;
import java.util.ArrayList ;
import java.util.List ;

import com.sandy.jeecoach.JEECoach ;
import com.sandy.jeecoach.dao.entity.master.Topic ;
import com.sandy.jeecoach.util.JEEQuestionImage ;

import lombok.Data ;

@Data
public class BulkQEntry {

    private String  qRef            = null ;
    private String  questionType    = null ;
    private Integer difficultyLevel = 3 ;
    private Integer projTime        = 120 ;
    private Boolean saved           = false ;
    private Topic   topic           = null ;
    private String  ansText         = "" ;
    private boolean isLCT           = false ; 
    
    private List<String> imgPaths  = new ArrayList<>() ;
    private List<String> imgNames  = new ArrayList<>() ;
    
    public BulkQEntry( JEEQuestionImage qImg, Topic topic ) {
        this.topic = topic ;
        populateAttributes( qImg ) ;
        addImage( qImg ) ;
    }
    
    public void addImage( JEEQuestionImage qImg ) {
        addImage( qImg, false ) ;
    }
    
    public void addImage( JEEQuestionImage qImg, boolean first ) {
        
        File imgFile = qImg.getImgFile() ;
        
        String imgPath = imgFile.getAbsolutePath() ;
        String imgName = imgFile.getName() ;
        
        imgPath = imgPath.substring( JEETEST_IMG_DIR.getAbsolutePath().length() ) ;
        
        if( first ) {
            imgPaths.add( 0, imgPath ) ;
            imgNames.add( 0, imgName ) ;
        }
        else {
            imgPaths.add( imgPath ) ;
            imgNames.add( imgName ) ;
        }
    }
    
    private void populateAttributes( JEEQuestionImage qImg ) {
        
        this.qRef            = qImg.getQRef() ;
        this.questionType    = qImg.getQuestionType() ;
        this.difficultyLevel = qImg.getDifficultyLevel() ;
        this.projTime        = qImg.getProjectedTime() ;
        this.isLCT           = qImg.isLCT() ;
        this.ansText         = lookupAns() ;
    }
    
    private String lookupAns() {
        return JEECoach.BULK_ANS_LOOKUP.getProperty( qRef, "" ) ;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        sb.append( qRef ).append( "\n----------------------------\n" ) ;
        
        for( int i=0; i<imgPaths.size(); i++ ) {
            sb.append( "   " + imgNames.get( i ) + "\n" )
              .append( "      " + imgPaths.get( i ) + "\n" ) ;
        }
          
        return sb.toString() ;
    }
}
