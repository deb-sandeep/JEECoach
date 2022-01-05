package com.sandy.jeecoach.api.jeetest.qbm.helper;

import java.io.File ;
import java.io.FileFilter ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.log4j.Logger ;

import com.sandy.common.util.StringUtil ;
import com.sandy.jeecoach.JEECoach ;
import com.sandy.jeecoach.api.jeetest.qbm.vo.BulkQEntry ;
import com.sandy.jeecoach.dao.entity.master.Book ;
import com.sandy.jeecoach.dao.entity.master.TestQuestion ;
import com.sandy.jeecoach.dao.entity.master.Topic ;
import com.sandy.jeecoach.dao.repository.master.TestQuestionRepository ;
import com.sandy.jeecoach.util.JEEQuestionImage ;

public class BulkQuestionEntryHelper {
    
    static final Logger log = Logger.getLogger( BulkQuestionEntryHelper.class ) ;
    
    private TestQuestionRepository tqRepo = null ;
    
    public BulkQuestionEntryHelper( TestQuestionRepository repo ) {
        this.tqRepo = repo ;
    }

    public List<BulkQEntry> getEntries( String subjectName, Topic topic, 
                                        Book book, String baseQRef ) {
        
        // NOTE: Below logic does not cater for the case where LCT
        //       context is made of parts. 
        
        List<JEEQuestionImage> unassignedImgs = null ;
        List<String> usedQRefs = null ;
        
        ArrayList<BulkQEntry> qEntries = new ArrayList<>() ;
        Map<String, JEEQuestionImage> lctCtxMap = new HashMap<>() ;
        
        usedQRefs = getUsedQRefs( topic.getId(), book.getId(), baseQRef ) ;
        unassignedImgs = selectUnassignedImgs( subjectName, topic, book,
                                               baseQRef, usedQRefs ) ;
        
        String prevQRef = null ;
        BulkQEntry entry = null ;
        
        if( !unassignedImgs.isEmpty() ) {
            Collections.sort( unassignedImgs ) ;
            
            for( JEEQuestionImage img : unassignedImgs ) {
                
                String qRef = img.getQRef() ;
                if( img.isLCTContext() ) {
                    lctCtxMap.put( qRef, img ) ;
                }
                else if( prevQRef == null || !prevQRef.equals( qRef ) ) {
                    entry = new BulkQEntry( img, topic ) ;
                    qEntries.add( entry ) ;
                    
                    if( entry.isLCT() ) {
                        JEEQuestionImage lctCtxImg = null ;
                        
                        lctCtxImg = lctCtxMap.get( img.getLCTCtxQRef() ) ;
                        entry.addImage( lctCtxImg, true ) ;
                    }
                } 
                else {
                    entry.addImage( img ) ;
                }
                prevQRef = qRef ;
            }
        }
        
        for( BulkQEntry bulkEntry : qEntries ) {
            log.debug( bulkEntry.getQRef() ) ;
        }
        
        return qEntries ;
    }
    
    private List<JEEQuestionImage> selectUnassignedImgs( 
                                           String subjectName, Topic topic,
                                           Book book, String baseQRef, 
                                           List<String> usedQRefs ) {
       
        File baseDir = new File( JEECoach.JEETEST_IMG_DIR,
                                 subjectName + "/" +
                                 "Std-" + String.format("%02d", book.getStd() ) + "/" + 
                                 topic.getTopicName() + "/" + 
                                 book.getCode() ) ;
        
        if( !baseDir.exists() ) {
            log.error( "Base dir does not exist. " + baseDir.getAbsolutePath() );
        }
        
        List<JEEQuestionImage> unassignedImages = new ArrayList<>() ;
        
        baseDir.listFiles( new FileFilter() {
            
            public boolean accept( File file ) {
                String fName = file.getName() ;
                if( !fName.endsWith( ".png" ) ) {
                    return false ;
                }
                
                try {
                    JEEQuestionImage question = new JEEQuestionImage( file ) ;
                    String qRef = question.getQRef() ;
                    
                    if( StringUtil.isNotEmptyOrNull( qRef ) ) {
                        if( !qRef.contains( baseQRef ) ) {
                            return false ;
                        }
                    }
                    
                    if( usedQRefs != null ) {
                        if( usedQRefs.contains( qRef ) ) {
                            return false ;
                        }
                    }
                    
                    unassignedImages.add( question ) ;
                    return true ;
                }
                catch( Exception e ) {
                    log.info( "Not a valid JEE question.\nMsg: " + e.getMessage(), e ) ;
                    return false ;
                }
            }
        } ) ;
        
        return unassignedImages ;
    }
    
    private List<String> getUsedQRefs( 
                        Integer topicId, Integer bookId, String qRefFragment ) {
        
        List<String> qRefs = new ArrayList<>() ;
        List<TestQuestion> questions = null ;
        
        questions = tqRepo.findQuestionsWithQRef( topicId, bookId, qRefFragment ) ;
        for( TestQuestion q : questions ) {
            qRefs.add( q.getQuestionRef() ) ;
        }
        
        return qRefs ;
    }
}
