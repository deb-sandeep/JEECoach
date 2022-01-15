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
    private String prodServerIp = "192.168.0.101" ;
    private String prodServerPort = "8080" ;
}
