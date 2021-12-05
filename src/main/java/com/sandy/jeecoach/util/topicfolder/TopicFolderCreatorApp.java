package com.sandy.jeecoach.util.topicfolder;

import java.io.File ;
import java.net.URL ;
import java.nio.file.Paths ;
import java.util.List ;

import org.apache.log4j.Logger ;

public class TopicFolderCreatorApp {
    
    static final Logger log = Logger.getLogger( TopicFolderCreatorApp.class ) ;

    private static File TOPIC_META_FILE = null ;
    private static File CACHE_DIR = null ;
    
    public static void main( String[] args ) throws Exception {
        
        File homeDir = new File( System.getProperty( "user.home" ) ) ;
        CACHE_DIR = new File( homeDir, "/projects/source/JEECoachQuestionImages" ) ;
        
        URL url = TopicFolderCreatorApp.class.getResource( "/config/syllabus-meta.xlsx" ) ;
        TOPIC_META_FILE = Paths.get( url.toURI() ).toFile() ;
        
        TopicFolderCreatorApp app = new TopicFolderCreatorApp() ;
        app.execute() ;
    }
    
    private TopicRepository topicRepo = null ;
    
    private void execute() throws Exception {
        
        topicRepo = new TopicRepository( TOPIC_META_FILE ) ;
        List<TopicMeta> topics = topicRepo.getTopicsForStd( "VII" ) ;
        
        for( TopicMeta meta : topics ) {
            makeLocalImageCacheFolders( meta ) ;
            writeSQL( meta ) ;
        }
    }
    
    private void makeLocalImageCacheFolders( TopicMeta meta ) 
        throws Exception {
        
        File subDir = new File( CACHE_DIR, meta.getSubject() ) ;
        File stdDir = new File( subDir, "Std-" + meta.getStd() ) ;
        
        String topicDirName = String.format( 
                                    "%02d - " + meta.getTopicName(), 
                                    meta.getTopicNum() ) ;
        
        File topicDir = new File( stdDir, topicDirName ) ;
        
        if( !topicDir.exists() ) {
            topicDir.mkdirs() ;
            log.debug( "[X] - " + topicDirName ) ;
        }
    }
    
    private void writeSQL( TopicMeta meta ) {

        StringBuilder sb = new StringBuilder() ;
        
        sb.append( "INSERT INTO jee_coach.topic_master" )
          .append( "(active, subject_name, std, section, topic_name, jee_topic_mapping)" )
          .append( "VALUES" )
          .append( "(" )
          .append( "0," )
          .append( "\"" + meta.getSubject() + "\"," )
          .append( "\"" + meta.getStd() + "\"," )
          .append( "\"" + meta.getSection() + "\"," )
          .append( "\"" + meta.getTopicName() + "\"," ) 
          .append( "\"" + meta.getJeeTopicMapping() + "\");" ) ;
        
        log.debug( sb.toString() ) ;

    }
}
