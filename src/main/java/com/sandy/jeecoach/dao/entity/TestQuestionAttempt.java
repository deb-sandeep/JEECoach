package com.sandy.jeecoach.dao.entity;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.GenerationType ;
import javax.persistence.Id ;
import javax.persistence.Table ;

import lombok.Data ;

@Data
@Entity
@Table( name = "test_question_attempt" )
public class TestQuestionAttempt {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    
    Integer testAttemptId = null ;
    Integer testQuestionId = null ;
    String  attemptStatus = null ;
    String  answerProvided = null ;
    Boolean isCorrect = Boolean.FALSE ;
    String  rootCause = "" ;
    Integer score = 0 ;
    Integer timeSpent = 0 ;
}
