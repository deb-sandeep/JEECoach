package com.sandy.jeecoach.web.jeetest;

import org.springframework.stereotype.* ;
import org.springframework.web.bind.annotation.* ;

@Controller
@RequestMapping( "/jeetest/qbm" )
public class JEETestQBMController {
    
    @RequestMapping( "/editQuestion" )
    public String jeeMain() {
        return "jeetest/qbm/edit_question" ;
    }
    
    @RequestMapping( "/mmtEditor" )
    public String mmtEditor() {
        return "jeetest/qbm/mmt_editor" ;
    }
    
    @RequestMapping( "/bulkEdit" )
    public String bulkEdit() {
        return "jeetest/qbm/bulk_edit" ;
    }
    
    @RequestMapping( "/qbInsight" )
    public String qbInsight() {
        return "jeetest/qbm/qb_insight" ;
    }
    
    @RequestMapping( "/searchQuestions" )
    public String jeeRed() {
        return "jeetest/qbm/search_questions" ;
    }
}
