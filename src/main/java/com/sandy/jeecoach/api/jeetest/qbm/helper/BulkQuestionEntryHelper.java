package com.sandy.jeecoach.api.jeetest.qbm.helper;

import java.awt.Point ;
import java.io.File ;
import java.io.FileFilter ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.LinkedHashMap ;
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
    
    public static class FileInfo {
        public String   qRef = null ;
        public Point    qNo = null ;
        public int      partNum = -1 ;
        public String   lctRef = null ;
        public boolean  isLCTQuestion = false ;
        public boolean  isLCTContext = false ;
        public Point    lctNo = null ;
        
        public boolean isLCT() {
            return lctRef != null ;
        }
        
        public boolean isPart() { 
            return partNum != -1 ;
        }
        
        public String getQRefBase() {
            return qRef.substring( 0, qRef.lastIndexOf( '/' ) ) ;
        }
        
        public String toString() {
            StringBuffer buffer = new StringBuffer() ;
            buffer.append( "\nQRef   = " + qRef ).append( "\n" )
                  .append( "QNo    = " + qNo.x + "." + qNo.y ).append( "\n" )
                  .append( "Part # = " + partNum ).append( "\n" )
                  .append( "lctRef = " + lctRef ).append( "\n" )
                  .append( "lct Q? = " + isLCTQuestion ).append( "\n" )
                  .append( "lct C? = " + isLCTContext ).append( "\n" )
                  .append( "lctNo  = " + lctNo ).append( "\n" ) ;
            return buffer.toString() ;
        }
    }
    
    static final Logger log = Logger.getLogger( BulkQuestionEntryHelper.class ) ;
    
    private TestQuestionRepository tqRepo = null ;
    
    public BulkQuestionEntryHelper( TestQuestionRepository repo ) {
        this.tqRepo = repo ;
    }

    public List<BulkQEntry> findBulkQuestionEntries( 
                                            String subjectName, Topic topic, 
                                            Book book, String baseQRef ) {
        
        List<JEEQuestionImage> unassignedImages = null ;
        List<String> usedQRefs = null ;
        ArrayList<BulkQEntry> qEntries = new ArrayList<>() ;
        
        usedQRefs = getUsedQRefs( topic.getId(), book.getId(), baseQRef ) ;
        unassignedImages = selectUnassignedImages( 
                                        subjectName, topic, book,
                                        baseQRef, usedQRefs ) ;
        
        Map<String, BulkQEntry> entriesMap = new LinkedHashMap<>() ;
        
        if( !unassignedImages.isEmpty() ) {
            Collections.sort( unassignedImages ) ;
            
            
            for( BulkQEntry entry : entriesMap.values() ) {
                entry.setTopic( topic ) ;
                qEntries.add( entry ) ;
            }
        }
        return qEntries ;
    }
    
    private List<JEEQuestionImage> selectUnassignedImages( 
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
                    log.info( "Not a valid JEE question.\nMsg: " + e.getMessage() ) ;
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
