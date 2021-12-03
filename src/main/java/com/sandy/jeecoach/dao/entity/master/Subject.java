package com.sandy.jeecoach.dao.entity.master;

import javax.persistence.* ;

import lombok.Data ;

@Data
@Entity
@Table( name = "subject_master" )
public class Subject {

    @Id
    @Column( name = "subject_name" )
    private String name ;
}
