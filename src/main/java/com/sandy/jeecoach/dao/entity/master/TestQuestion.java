package com.sandy.jeecoach.dao.entity.master;

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
@Table( name = "mocktest_question_master" )
public class TestQuestion {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id = -1 ;

    @ManyToOne
    @JoinColumn( name="subject_name" )
    private Subject subject ;
    
    @ManyToOne
    @JoinColumn( name="topic_id" )
    private Topic topic ;
    
    @ManyToOne
    @JoinColumn( name="book_id" )
    private Book book ;
    
    private String    hash                 = null ;
    private String    targetExam           = "MAIN" ;
    private String    questionType         = "SCA" ;
    private String    questionRef          = null ;
    private String    questionText         = null ;
    private String    lctContext           = null ;
    private String    questionFormattedText= null ;
    private String    answerText           = null ;
    private Integer   difficultyLevel      = 1 ;
    private Boolean   synched              = Boolean.FALSE ;
    private Boolean   attempted            = Boolean.FALSE ;
    private Timestamp creationTime         = null ;
    private Timestamp lastUpdateTime       = null ;
}
