/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp;

import com.davedaniels.nlp.service.NlpService;

/**
 * Application entry point.
 * 
 * @since 1.0.0
 */
public class Main {

   public static void main( String[] args ) throws Exception {
      System.out.println( "Starting NLP processing." );
      new NlpService().process();
      System.out.println( "End NLP processing." );
   }
}