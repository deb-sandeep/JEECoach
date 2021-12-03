package com.sandy.jeecoach.dao.repository.master;

import org.springframework.data.repository.CrudRepository ;

import com.sandy.jeecoach.dao.entity.master.Book ;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findByBookShortName( String shortName ) ;
}
