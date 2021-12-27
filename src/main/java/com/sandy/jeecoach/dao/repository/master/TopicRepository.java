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
          + "    t.subject.name = :subjectName and "
          + "    t.std = :std "
          + "order by "
          + "    t.std asc"
    )
    List<Topic> findAllBySubjectNameAndStdOrderByIdAsc(  
                                    @Param("subjectName") String subjectName,
                                    @Param("std") Integer std ) ;
    
    @Query( value = 
            "select t "
          + "from Topic t "
          + "where "
          + "    t.active = true "
          + "order by "
          + "    t.std asc "
    )
    List<Topic> findActiveTopics() ;
}
