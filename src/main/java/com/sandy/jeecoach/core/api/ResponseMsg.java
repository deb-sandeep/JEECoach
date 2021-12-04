package com.sandy.jeecoach.core.api;

import lombok.Data ;

@Data
public class ResponseMsg {

    private String msg = null ;
    
    public static final ResponseMsg SUCCESS = new ResponseMsg( "Success" ) ;
    
    public ResponseMsg( String msg ) {
        this.msg = msg ;
    }
}
