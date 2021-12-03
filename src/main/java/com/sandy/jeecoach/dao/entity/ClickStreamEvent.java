package com.sandy.jeecoach.dao.entity;

import java.sql.Timestamp ;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.GenerationType ;
import javax.persistence.Id ;
import javax.persistence.Table ;

import lombok.Data ;

@Data
@Entity
@Table( name = "click_stream_event" )
public class ClickStreamEvent {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    
    private Integer testAttemptId = null ;
    private String eventId = null ;
    private String payload = null ;
    private Integer timeMarker = null ;
    private Timestamp creationTimestamp = null ;
}
