package com.sandy.jeecoach.dao.repository;

import java.util.List ;

import org.springframework.data.repository.CrudRepository ;

import com.sandy.jeecoach.dao.entity.TestAttempt ;

public interface TestAttemptRepository 
    extends CrudRepository<TestAttempt, Integer> {
    
    List<TestAttempt> findAllByOrderByDateAttemptedDesc() ;
}
