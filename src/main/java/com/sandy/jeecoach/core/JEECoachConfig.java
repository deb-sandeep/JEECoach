package com.sandy.jeecoach.core;

import org.springframework.boot.context.properties.* ;
import org.springframework.context.annotation.* ;

import lombok.Data ;

@Data
@Configuration( "config" )
@PropertySource( "classpath:jeecoach.properties" )
@ConfigurationProperties( "jeecoach" )
public class JEECoachConfig {

    private boolean recordTestAttempt = true ;
    private String envType = "dev" ;
}
