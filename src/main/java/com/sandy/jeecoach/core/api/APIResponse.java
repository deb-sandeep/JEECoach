package com.sandy.jeecoach.core.api;

import lombok.Data ;

@Data
public class APIResponse {

    private String message = null ;
    
    public APIResponse( String msg ) {
        this.message = msg ;
    }
}
