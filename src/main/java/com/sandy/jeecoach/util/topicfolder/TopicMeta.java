package com.sandy.jeecoach.util.topicfolder;

import lombok.Data ;

@Data
public class TopicMeta {

    private String std = null ;
    private String subject = null ;
    private String section = null ;
    private int topicNum = 0 ;
    private String topicName = null ;
    private String jeeTopicMapping = null ;
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder() ;
        builder.append( "TopicMeta [std=" ) ;
        builder.append( std ) ;
        builder.append( ", subject=" ) ;
        builder.append( subject ) ;
        builder.append( ", section=" ) ;
        builder.append( section ) ;
        builder.append( ", topicNum=" ) ;
        builder.append( topicNum ) ;
        builder.append( ", topicName=" ) ;
        builder.append( topicName ) ;
        builder.append( "]" ) ;
        return builder.toString() ;
    }
}
