package com.sandy.jeecoach.core.util;

import java.net.InetAddress ;
import java.net.NetworkInterface ;
import java.net.SocketException ;
import java.util.Collections ;
import java.util.Enumeration ;

import org.apache.log4j.Logger ;

import com.sandy.jeecoach.JEECoach ;
import com.sandy.jeecoach.core.JEECoachConfig ;

public class JEECoachUtil {

    private static final Logger log = Logger.getLogger( JEECoachUtil.class ) ;
    
    private static Boolean isExecutingOnProdServer = null ;
    
    public static boolean isExecutingOnProdServer() throws SocketException {
        
        if( isExecutingOnProdServer != null ) {
            return isExecutingOnProdServer ;
        }
        
        JEECoachConfig config = JEECoach.getConfig() ;
        String prodServerIp = config.getProdServerIp() ;
        
        log.debug( "Checking if we are already executing on the server." ) ;
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces() ;
        
        isExecutingOnProdServer  = false ;
        
        for( NetworkInterface nif : Collections.list( nets ) ) {
            log.debug( "Investing network interface : " + nif.getDisplayName() ) ;
            Enumeration<InetAddress> addresses = nif.getInetAddresses() ;
            
            for( InetAddress address : Collections.list(  addresses ) ) {
                String ipAddress = address.getHostAddress() ;
                log.debug( "   IP bound to this nif : " + ipAddress ) ;
                if( ipAddress.equals( prodServerIp ) ) {
                    isExecutingOnProdServer = true ;
                    break ;
                }
            }
        }
        
        return isExecutingOnProdServer ;
    }
    
    public static String getProdServerAddress() {
        
        JEECoachConfig config = JEECoach.getConfig() ;
        return config.getProdServerIp() + ":" + config.getProdServerPort() ;
    }
}
