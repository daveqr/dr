/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.davedaniels.nlp.model.NlpData;
import com.davedaniels.nlp.service.NlpService;

/**
 * Application entry point.
 * 
 * @since 1.0.0
 */
public class Main {

   public static void main( String[] args ) throws Exception {
      System.out.println( "Starting NLP processing." );

      try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
         ctx.register( AppConfig.class );
         ctx.refresh();

         NlpService service = ctx.getBean( NlpService.class );
         NlpData data = service.process();

         System.out.println( data.toXml() );
         System.out.println( "Found these proper nouns:" );
         for ( String noun : data.findProperNouns() ) {
            System.out.println( noun );
         }
      }

      System.out.println( "End NLP processing." );
   }
}