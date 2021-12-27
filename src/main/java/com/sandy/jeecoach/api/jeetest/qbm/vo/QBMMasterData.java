package com.sandy.jeecoach.api.jeetest.qbm.vo;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import com.sandy.jeecoach.dao.entity.master.Book ;
import com.sandy.jeecoach.dao.entity.master.Topic ;

import lombok.Data ;

@Data
public class QBMMasterData {
    
    public static final String EXAM_TYPE_MAIN = "MAIN" ;
    public static final String EXAM_TYPE_ADV  = "ADV" ;
    
    public static final String Q_TYPE_SCA = "SCA" ;
    public static final String Q_TYPE_MCA = "MCA" ;
    public static final String Q_TYPE_NT  = "NT" ;
    public static final String Q_TYPE_LCT = "LCT" ;
    public static final String Q_TYPE_MMT = "MMT" ;
    
    public static final String S_TYPE_PHY   = "IIT - Physics" ;
    public static final String S_TYPE_CHEM  = "IIT - Chemistry" ;
    public static final String S_TYPE_MATHS = "IIT - Maths" ;
    
    public static String[]  targetExams     = { EXAM_TYPE_MAIN, EXAM_TYPE_ADV } ;
    public static String[]  questionTypes   = { Q_TYPE_SCA, Q_TYPE_MCA, Q_TYPE_NT, Q_TYPE_LCT, Q_TYPE_MMT } ;
    public static String[]  subjectNames    = { S_TYPE_PHY, S_TYPE_CHEM, S_TYPE_MATHS } ;
    public static Integer[] difficultyLevel = { 1, 2, 3, 4, 5 } ;
    public static Integer[] approxSolveTime = { 15, 30, 60, 90, 120, 180, 240, 300, 600 } ; 

    private Map<Integer, Map<String, List<Topic>>> topicMap = new HashMap<>() ;
    private Map<Integer, Map<String, List<Book>>>  bookMap  = new HashMap<>() ;
    private List<Integer> standardNames = new ArrayList<>() ;
    
    public String[] getTargetExams() {
        return targetExams ;
    }

    public String[] getQuestionTypes() {
        return questionTypes ;
    }
    
    public String[] getSubjectNames() {
        return subjectNames ;
    }
    
    public Integer[] getDifficultyLevel() {
        return difficultyLevel ;
    }
    
    public Integer[] getApproxSolveTime() {
        return approxSolveTime ;
    }

    public void addTopic( Topic topic ) {
        
        Integer std = topic.getStd() ;
        String subjectName = topic.getSubject().getName() ;
        
        Map<String, List<Topic>> stdTopics = topicMap.get( std ) ;
        if( stdTopics == null ) {
            stdTopics = new HashMap<>() ;
            topicMap.put( std, stdTopics ) ;
        }
        
        List<Topic> subTopics = stdTopics.get( subjectName ) ;
        if( subTopics == null ) {
            subTopics = new ArrayList<>() ;
            stdTopics.put( subjectName, subTopics ) ;
        }
        
        if( !subTopics.contains( topic ) ) {
            subTopics.add( topic ) ;
        }
        
        if( !standardNames.contains( std ) ) {
            standardNames.add( std ) ;
        }
    }
    
    public void addBook( Book book ) {
        
        Integer std = book.getStd() ;
        String subjectName = book.getSubject().getName() ;
        
        Map<String, List<Book>> stdBooks = bookMap.get( std ) ;
        if( stdBooks == null ) {
            stdBooks = new HashMap<>() ;
            bookMap.put( std, stdBooks ) ;
        }
        
        List<Book> subBooks = stdBooks.get( subjectName ) ;
        if( subBooks == null ) {
            subBooks = new ArrayList<>() ;
            stdBooks.put( subjectName, subBooks ) ;
        }
        
        if( !subBooks.contains( book ) ) {
            subBooks.add( book ) ;
        }
        
        if( !standardNames.contains( std ) ) {
            standardNames.add( std ) ;
        }
    }
}
