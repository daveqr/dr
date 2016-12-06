/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.davedaniels.nlp.model.NlpData;
import com.davedaniels.nlp.service.NlpSentenceService;

/**
 * Application entry point.
 * 
 * @since 1.0.0
 */
@SpringBootApplication
public class App implements ApplicationRunner {

   @Autowired
   private ApplicationContext ctx;

   public static void main( String[] args ) {
      SpringApplication.run( App.class, args );
   }


   @Override
   public void run( ApplicationArguments arg0 ) throws Exception {
      NlpData data = ctx.getBean( NlpSentenceService.class ).process();

      System.out.println( data.toXml() );
      for ( String noun : data.findProperNouns() ) {
         System.out.println( noun );
      }
   }

}