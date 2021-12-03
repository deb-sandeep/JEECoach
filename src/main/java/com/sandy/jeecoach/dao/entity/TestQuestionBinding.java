package com.sandy.jeecoach.dao.entity;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.GenerationType ;
import javax.persistence.Id ;
import javax.persistence.JoinColumn ;
import javax.persistence.ManyToOne ;
import javax.persistence.Table ;

import com.sandy.jeecoach.dao.entity.master.Subject ;
import com.sandy.jeecoach.dao.entity.master.TestQuestion ;
import com.sandy.jeecoach.dao.entity.master.Topic ;

import lombok.Data ;

@Data
@Entity
@Table( name = "test_question_binding" )
public class TestQuestionBinding {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;

    @ManyToOne
    @JoinColumn( name="test_config_id" )
    private TestConfigIndex testConfig ;
    
    @ManyToOne
    @JoinColumn( name="topic_id" )
    private Topic topic ;
    
    @ManyToOne
    @JoinColumn( name="question_id" )
    private TestQuestion question ;
    
    @ManyToOne
    @JoinColumn( name="subject_name" )
    private Subject subject ;
    
    private Integer sectionIndex = 1 ;
    private String sectionName = "SCA" ;
    private Integer sequence = null ;
}
