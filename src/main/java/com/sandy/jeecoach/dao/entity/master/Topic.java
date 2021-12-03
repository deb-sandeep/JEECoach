package com.sandy.jeecoach.dao.entity.master;

import java.sql.Timestamp ;

import javax.persistence.* ;

import lombok.Data ;

@Data
@Entity
@Table( name = "topic_master" )
public class Topic {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    
    @ManyToOne
    @JoinColumn( name="subject_name" )
    private Subject subject ;
    
    @Column( name = "topic_name" )
    private String topicName ;
    
    private String section ;
    private Timestamp burnStart ;
    private Timestamp burnCompletion ;
    private Integer streamNumber ;
    private Boolean active = Boolean.FALSE ;
    
    public String toString() {
        return id + " / " + subject.getName() + " / " + topicName ;
    }
}
