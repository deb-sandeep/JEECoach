package com.sandy.jeecoach.dao.entity;

import java.sql.Timestamp ;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.GenerationType ;
import javax.persistence.Id ;
import javax.persistence.JoinColumn ;
import javax.persistence.ManyToOne ;
import javax.persistence.Table ;

import lombok.Data ;

@Data
@Entity
@Table( name = "test_attempt" )
public class TestAttempt {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;

    @ManyToOne
    @JoinColumn( name="test_id" )
    private TestConfigIndex testConfig ;
    
    private Integer score = 0 ;
    private Integer timeTaken = 0 ; // In minutes
    
    private Integer numCorrectAnswers = 0 ;
    private Integer numWrongAnswers = 0 ;
    private Integer numNotVisited = 0 ;
    private Integer numNotAnswered = 0 ;
    private Integer numAttempted = 0 ;
    private Integer numMarkedForReview = 0 ;
    private Integer numAnsAndMarkedForReview = 0 ;
    
    private Timestamp dateAttempted = null ;
}
