package com.sandy.jeecoach.dao.repository;

import java.util.List ;

import org.springframework.data.jpa.repository.Query ;
import org.springframework.data.repository.CrudRepository ;

import com.sandy.jeecoach.dao.entity.TestConfigIndex ;

public interface TestConfigIndexRepository 
    extends CrudRepository<TestConfigIndex, Integer> {
    
    @Query( value =   
            "SELECT "
          + "    tci "
          + "FROM "
          + "    TestConfigIndex tci "
          + "WHERE "
          + "    tci.id NOT IN ( "
          + "       SELECT ta.testConfig.id "
          + "       FROM TestAttempt ta "
          + "    ) "
          + "ORDER BY "
          + "    tci.examType ASC, "
          + "    tci.id ASC "
    )
    List<TestConfigIndex> findUnattemptedTests() ;
}
