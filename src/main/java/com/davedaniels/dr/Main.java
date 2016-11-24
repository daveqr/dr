package com.davedaniels.dr;

import com.davedaniels.dr.service.NlpService;

public class Main {

   public static void main( String[] args ) throws Exception {
      System.out.println( "Starting NLP processing." );
      new NlpService().process();
   }
}