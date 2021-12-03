package com.sandy.jeecoach.dao.entity;

import javax.persistence.Entity ;
import javax.persistence.GeneratedValue ;
import javax.persistence.GenerationType ;
import javax.persistence.Id ;
import javax.persistence.Table ;

import lombok.Data ;

@Data
@Entity
@Table( name = "test_attempt_lap_snapshot" )
public class TestAttemptLapSnapshot {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    
    private Integer testAttemptId = null ;
    private Integer questionId = null ;
    private String  lapName = null ;
    private Integer timeSpent = 0 ;
    private String  attemptStatus = null ;

    public String toString() {
        return testAttemptId + "," + 
               questionId + "," + 
               lapName + "," + 
               timeSpent + "," + 
               attemptStatus ;
    }
}
