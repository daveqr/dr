/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.davedaniels.nlp.model.NlpData;
import com.davedaniels.nlp.service.NlpService;
import com.davedaniels.nlp.service.NlpSentenceService;

/**
 * Application entry point.
 * 
 * @since 1.0.0
 */
public class Main {

   private static final Logger LOG = LogManager.getLogger( Main.class );

   public static void main( String[] args ) throws Exception {
      LOG.info( "Starting NLP processing." );

      try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext()) {
         ctx.register( AppConfig.class );
         ctx.refresh();

         NlpService service = ctx.getBean( NlpSentenceService.class );
         NlpData data = service.process();

         LOG.info( data.toXml() );
         LOG.info( "Found these proper nouns:" );
         for ( String noun : data.findProperNouns() ) {
            LOG.info( noun );
         }
      }

      LOG.info( "End NLP processing." );

      // this is because the logger is not closing
      System.exit( 0 );
   }
}