package com.sandy.jeecoach.dao.repository.master;

import java.util.List ;

import org.springframework.data.jpa.repository.Query ;
import org.springframework.data.repository.CrudRepository ;
import org.springframework.data.repository.query.Param ;

import com.sandy.jeecoach.dao.entity.master.Topic ;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
    
    @Query( value = 
            "select t "
          + "from Topic t "
          + "where "
          + "    t.active = true and "
          + "    t.subject.name = :subjectName"
    )
    List<Topic> findAllBySubjectNameOrderByIdAsc(  
                                    @Param("subjectName") String subjectName ) ;
    
    @Query( value = 
            "select t "
          + "from Topic t "
          + "where "
          + "    t.active = true "
    )
    List<Topic> findActiveTopics() ;
}
