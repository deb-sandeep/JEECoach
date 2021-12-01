package com.sandy.jeecoach.core;

import org.springframework.boot.context.properties.* ;
import org.springframework.context.annotation.* ;

@Configuration( "config" )
@PropertySource( "classpath:jeecoach.properties" )
@ConfigurationProperties( "jeecoach" )
public class JEECoachConfig {

    private boolean recordTestAttempt = true ;
    private String envType = "dev" ;

    public boolean isRecordTestAttempt() {
        return recordTestAttempt ;
    }

    public void setRecordTestAttempt( boolean recordTestAttempt ) {
        this.recordTestAttempt = recordTestAttempt ;
    }

    public String getEnvType() {
        return envType ;
    }

    public void setEnvType( String envType ) {
        this.envType = envType ;
    }
}
