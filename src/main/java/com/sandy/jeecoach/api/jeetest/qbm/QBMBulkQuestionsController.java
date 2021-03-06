package com.sandy.jeecoach.api.jeetest.qbm;

import java.util.ArrayList ;
import java.util.List ;

import org.apache.log4j.Logger ;
import org.springframework.beans.factory.annotation.Autowired ;
import org.springframework.http.HttpStatus ;
import org.springframework.http.ResponseEntity ;
import org.springframework.web.bind.annotation.GetMapping ;
import org.springframework.web.bind.annotation.RequestParam ;
import org.springframework.web.bind.annotation.RestController ;

import com.sandy.jeecoach.api.jeetest.qbm.helper.BulkQuestionEntryHelper ;
import com.sandy.jeecoach.api.jeetest.qbm.vo.BulkQEntry ;
import com.sandy.jeecoach.dao.entity.master.Book ;
import com.sandy.jeecoach.dao.entity.master.Topic ;
import com.sandy.jeecoach.dao.repository.master.BookRepository ;
import com.sandy.jeecoach.dao.repository.master.TestQuestionRepository ;
import com.sandy.jeecoach.dao.repository.master.TopicRepository ;

@RestController
public class QBMBulkQuestionsController {
    
    static final Logger log = Logger.getLogger( QBMBulkQuestionsController.class ) ;
    
    @Autowired
    private TopicRepository topicRepo = null ;

    @Autowired
    private BookRepository bookRepo = null ;
    
    @Autowired
    private TestQuestionRepository tqRepo = null ;
    
    @GetMapping( "/BulkQuestionsEntryMeta" ) 
    public ResponseEntity<List<BulkQEntry>> getRawQuestions(
            @RequestParam( name="subjectName", required=true  ) String  subjectName,
            @RequestParam( name="topicId",     required=false ) Integer topicId,
            @RequestParam( name="bookId",      required=true  ) Integer bookId,
            @RequestParam( name="baseQRef",    required=false ) String  baseQRef ) {
        
        Book book = null ;
        BulkQuestionEntryHelper helper = null ;
        List<BulkQEntry> entries = new ArrayList<>() ;
        List<Topic> topicList = new ArrayList<>() ;
        
        try {
            
            book  = bookRepo.findById( bookId ).get() ;
            if( topicId != null ) {
                topicList.add( topicRepo.findById( topicId ).get() ) ;
            }
            else {
                topicList.addAll( 
                    topicRepo.findAllBySubjectNameAndStdOrderByIdAsc( 
                                  subjectName, book.getStd() ) 
                ) ;
            }
            
            helper = new BulkQuestionEntryHelper( tqRepo ) ;
            for( Topic topic : topicList ) {
                List<BulkQEntry> topicQs = null ;
                topicQs = helper.getEntries( subjectName, topic, book, baseQRef ) ;
                entries.addAll( topicQs ) ;
            }
            
            return ResponseEntity.status( HttpStatus.OK )
                                 .body( entries ) ;
        }
        catch( Exception e ) {
            log.error( "Error getting available test attempts.", e ) ;
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                                 .body( null ) ;
        }
    }
}
