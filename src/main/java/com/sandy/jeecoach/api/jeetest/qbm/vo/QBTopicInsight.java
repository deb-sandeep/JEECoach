package com.sandy.jeecoach.api.jeetest.qbm.vo;

import java.util.HashMap ;
import java.util.Map ;

import lombok.Data ;

@Data
public class QBTopicInsight {
    
    private Integer topicId = null ;
    private String  subjectName ;
    private Integer standard = 0 ;
    private String  topicName ;
    private Integer totalQuestions = 0 ;
    private Integer attemptedQuestions = 0 ;
    private Integer assignedQuestions = 0 ;
    private Integer availableQuestions = 0 ;
    
    private Map<String, Integer> totalQuestionsByType = new HashMap<>() ;
    private Map<String, Integer> attemptedQuestionsByType = new HashMap<>() ;
    private Map<String, Integer> assignedQuestionsByType = new HashMap<>() ;
    private Map<String, Integer> availableQuestionsByType = new HashMap<>() ;
}
