package com.sandy.jeecoach.dao.entity.master;

import javax.persistence.* ;
import lombok.Data ;

@Data
@Entity
@Table( name = "book_master" )
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id ;
    
    @ManyToOne
    @JoinColumn( name="subject_name" )
    private Subject subject ;
    
    private String bookName ;
    private String bookShortName ;
    private boolean forProblems ;
    private String authorNames ;
    
    public String toString() {
        return subject.getName() + " / " + bookShortName ;
    }
}
