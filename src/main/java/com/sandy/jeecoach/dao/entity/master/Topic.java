package com.sandy.jeecoach.dao.entity.master;

import javax.persistence.Column ;
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
@Table( name = "topic_master" )
public class Topic {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    
    private Boolean active = Boolean.TRUE ;

    @ManyToOne
    @JoinColumn( name="subject_name" )
    private Subject subject ;
    
    private String std ;
    
    private String section ;

    @Column( name = "topic_name" )
    private String topicName ;
    
    public String toString() {
        return id + " / " + 
               subject.getName() + " / " +
               "Std-" + std + " / " + 
               topicName ;
    }
}
