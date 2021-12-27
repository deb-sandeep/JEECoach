package com.sandy.jeecoach.dao.repository.master;

import java.util.List ;

import org.springframework.data.jpa.repository.Query ;
import org.springframework.data.repository.CrudRepository ;

import com.sandy.jeecoach.dao.entity.master.Book ;

public interface BookRepository extends CrudRepository<Book, Integer> {

    @Query( value = 
            "select b "
          + "from Book b "
          + "where "
          + "    b.active = true "
          + "order by "
          + "    b.std asc"
    )
    List<Book> findActiveBooks() ;
}
