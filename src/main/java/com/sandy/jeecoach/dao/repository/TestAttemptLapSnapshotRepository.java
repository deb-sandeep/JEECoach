package com.sandy.jeecoach.dao.repository;

import java.util.List ;

import org.springframework.data.repository.CrudRepository ;

import com.sandy.jeecoach.dao.entity.TestAttemptLapSnapshot ;

public interface TestAttemptLapSnapshotRepository 
    extends CrudRepository<TestAttemptLapSnapshot, Integer> {
    
    List<TestAttemptLapSnapshot> findAllByTestAttemptId( Integer testAttemptId ) ;

    List<TestAttemptLapSnapshot> findAllByTestAttemptIdOrderById( Integer testAttemptId ) ;
}
