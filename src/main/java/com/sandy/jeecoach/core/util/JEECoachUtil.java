package com.sandy.jeecoach.core.util;

import com.sandy.jeecoach.JEECoach ;

public class JEECoachUtil {

    public static boolean isOperatingOnPiMon() throws Exception {
        return JEECoach.getConfig().getEnvType().equalsIgnoreCase( "PROD" ) ;
    }
}
