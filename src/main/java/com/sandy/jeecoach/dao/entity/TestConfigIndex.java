package com.sandy.jeecoach.dao.entity;

import java.sql.Timestamp ;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.GenerationType ;
import javax.persistence.Id ;
import javax.persistence.Table ;

import org.hibernate.annotations.Formula ;

import lombok.Data ;

@Data
@Entity
@Table( name = "test_config_index" )
public class TestConfigIndex {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    private String examType = null ;
    private String shortName = null ;
    private Integer numPhyQuestions = 0 ;
    private Integer numChemQuestions = 0 ;
    private Integer numMathQuestions = 0 ;
    private Integer totalMarks = 0 ;
    
    // Note that duration is in seconds.
    private Integer duration = 0 ;
    private boolean synched = false ;
    
    private Timestamp creationDate         = null ;
    private Timestamp lastUpdateDate       = null ;
    
    @Formula( "(SELECT count(*) FROM test_attempt ta WHERE ta.test_id = id)" )
    private Boolean attempted = false ;
}
