package com.sandy.jeecoach.web.jeetest;

import org.springframework.stereotype.* ;
import org.springframework.web.bind.annotation.* ;

@Controller
@RequestMapping( "/jeetest/exam" )
public class JEEExamController {
    
    @RequestMapping( "/xMainTest" )
    public String xMainTest() {
        return "jeetest/exam_xmain/exam_xmain_base" ;
    }

    @RequestMapping( "/xMainTestExam" )
    public String xMainTestExam() {
        return "jeetest/exam_xmain/exam_xmain_test" ;
    }

    @RequestMapping( "/xMainTestResult" )
    public String xMainTestResult() {
        return "jeetest/exam_xmain/exam_xmain_result" ;
    }

    @RequestMapping( "/advTest" )
    public String advTest() {
        return "jeetest/exam_adv/exam_adv_base" ;
    }

    @RequestMapping( "/advTestExam" )
    public String advTestExam() {
        return "jeetest/exam_adv/exam_adv_test" ;
    }

    @RequestMapping( "/advTestResult" )
    public String advTestResult() {
        return "jeetest/exam_adv/exam_adv_result" ;
    }
}
