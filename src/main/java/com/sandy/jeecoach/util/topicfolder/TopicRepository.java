package com.sandy.jeecoach.util.topicfolder;

import java.io.File ;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.log4j.Logger ;

import com.sandy.common.xlsutil.XLSRow ;
import com.sandy.common.xlsutil.XLSWrapper ;

public class TopicRepository {
    
    static final Logger log = Logger.getLogger( TopicRepository.class ) ;
    
    private List<TopicMeta> topicList = new ArrayList<>() ;
    private Map<String, List<TopicMeta>> stdTopicListMap = new HashMap<>() ;
    private Map<String, Map<String, List<TopicMeta>>> stdSubTopicListMap = new HashMap<>() ;

    public TopicRepository( File file ) throws Exception {
        loadRepository( file ) ;
    }
    
    public List<TopicMeta> getAllTopics() {
        return topicList ;
    }
    
    public List<TopicMeta> getTopicsForStd( String std ) {
        return stdTopicListMap.get( std ) ;
    }
    
    public List<TopicMeta> getTopicsForStdAndSub( String std, String sub ) {
        
        Map<String, List<TopicMeta>> topicMap = null ;
        topicMap = stdSubTopicListMap.get( std ) ;
        return topicMap.get( sub ) ;
    }
    
    private void loadRepository( File file ) throws Exception {
        XLSWrapper xls = new XLSWrapper( file ) ;
        List<XLSRow> rows = xls.getRows( 0, 0, 5 ) ;
        for( XLSRow row : rows ) {
            TopicMeta meta = buildMeta( row ) ;
            classifyMeta( meta ) ;
        }
    }

    private TopicMeta buildMeta( XLSRow row ) {
        
        TopicMeta meta = new TopicMeta() ;
        meta.setStd( row.getCellValue( "Standard" ) ) ;
        meta.setSubject( row.getCellValue( "Subject" ) ) ;
        meta.setSection( row.getCellValue( "Section" ) ) ;
        meta.setTopicNum( Integer.parseInt( row.getCellValue( "TopicNo" ) ) ) ;
        meta.setTopicName( row.getCellValue( "TopicName" ) ) ;
        meta.setJeeTopicMapping( row.getCellValue( "JEE Topic Mapping" ) ) ;
        return meta ;
    }
    
    private void classifyMeta( TopicMeta meta ) {
        
        List<TopicMeta> metaList = null ;
        Map<String, List<TopicMeta>> topicMap = null ;
        
        String std = meta.getStd() ;
        String sub = meta.getSubject() ;
        
        // Add in the list of all topics
        topicList.add( meta ) ;
        
        // Add in the list of topics for standard
        metaList = stdTopicListMap.get( std ) ;
        if( metaList == null ) {
            metaList = new ArrayList<>() ;
            stdTopicListMap.put( std, metaList ) ;
        }
        metaList.add( meta ) ;
        
        // Add in the list of topics for standard and subject
        topicMap = stdSubTopicListMap.get( std ) ;
        if( topicMap == null ) {
            topicMap = new HashMap<>() ;
            stdSubTopicListMap.put( std, topicMap ) ;
        }
        metaList = topicMap.get( sub ) ;
        if( metaList == null ) {
            metaList = new ArrayList<>() ;
            topicMap.put( sub, metaList ) ;
        }
        metaList.add( meta ) ;
    }
}
