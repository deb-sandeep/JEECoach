package com.sandy.jeecoach ;

import java.io.File ;

import org.apache.log4j.Logger ;
import org.springframework.beans.BeansException ;
import org.springframework.boot.SpringApplication ;
import org.springframework.boot.autoconfigure.SpringBootApplication ;
import org.springframework.context.ApplicationContext ;
import org.springframework.context.ApplicationContextAware ;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry ;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer ;

import com.sandy.jeecoach.core.JEECoachConfig ;

@SpringBootApplication
public class JEECoach 
    implements ApplicationContextAware, WebMvcConfigurer {

    private static final Logger log = Logger.getLogger( JEECoach.class ) ;
    
    public static File JEETEST_IMG_DIR = new File( System.getProperty( "user.home" ),
                                                  "projects/workspace/jeecoach/jeetest/question_images" ) ;

    public static File JEETEST_EFFGRAPH_IMG_DIR = new File( System.getProperty( "user.home" ),
                                                  "projects/workspace/jeecoach/jeetest/effgraphs" ) ;

    public static File MATHJAX_DIR = new File( "/var/www/lib-ext/MathJax" ) ;

    private static ApplicationContext APP_CTX   = null ;
    private static JEECoach           APP       = null ;
    
    public static ApplicationContext getAppContext() {
        return APP_CTX ;
    }

    public static JEECoachConfig getConfig() {
        return (JEECoachConfig) APP_CTX.getBean( "config" ) ;
    }

    public static JEECoach getApp() {
        return APP ;
    }

    // ---------------- Instance methods start ---------------------------------

    public JEECoach() {
        APP = this ;
    }

    public void initialize() {
    }
    
    @Override
    public void setApplicationContext( ApplicationContext applicationContext )
            throws BeansException {
        APP_CTX = applicationContext ;
    }

    @Override
    public void addResourceHandlers( ResourceHandlerRegistry registry ) {
        registry.addResourceHandler("/jeetest/images/**")
                .addResourceLocations( "file:" + JEETEST_IMG_DIR.getAbsolutePath() + "/" ) ;
        
        registry.addResourceHandler("/jeetest/effgraphs/**")
                .addResourceLocations( "file:" + JEETEST_EFFGRAPH_IMG_DIR.getAbsolutePath() + "/" ) ;

        registry.addResourceHandler("/js/lib/MathJax/**")
                .addResourceLocations( "file:" + MATHJAX_DIR.getAbsolutePath() + "/" ) ;
    }
    
    // --------------------- Main method ---------------------------------------

    public static void main( String[] args ) {
        log.debug( "Starting Spring Booot..." ) ;
        
        System.setProperty("java.awt.headless", "false");
        
        SpringApplication.run( JEECoach.class, args ) ;

        log.debug( "Starting JEECoach.." ) ;
        JEECoach app = JEECoach.getAppContext().getBean( JEECoach.class ) ;
        app.initialize() ;
    }

}
