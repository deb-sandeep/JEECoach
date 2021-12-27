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
    
    private Boolean active = Boolean.FALSE ;

    @ManyToOne
    @JoinColumn( name="subject_name" )
    private Subject subject ;
    
    @Column( name = "topic_name" )
    private String topicName ;
    
    @Column( name = "jee_topic_mapping" ) 
    private String jeeTopicMapping ;
    
    private Integer std ;
    private String  section ;

    public String toString() {
        return id + " / " + 
               subject.getName() + " / " +
               "Std-" + std + " / " + 
               topicName ;
    }

    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) return true ;
        if( obj == null ) return false ;
        Topic other = (Topic) obj ;
        if( id == null ) {
            if( other.id != null ) return false ;
        }
        else if( !id.equals( other.id ) ) return false ;
        return true ;
    }

    @Override
    public int hashCode() {
        final int prime = 31 ;
        int result = 1 ;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() ) ;
        return result ;
    }
}
