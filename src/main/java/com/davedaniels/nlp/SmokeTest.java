/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.davedaniels.nlp.main.App;

/**
 * Application smoke test runner. It's only purpose it to kick off the app.
 * 
 * @since 1.0.0
 */
@SpringBootApplication
public class SmokeTest implements ApplicationRunner {

   @Autowired
   private ApplicationContext ctx;

   public static void main( String[] args ) {
      SpringApplication.run( SmokeTest.class, args );
   }


   @Override
   public void run( ApplicationArguments arg0 ) throws Exception {
      ctx.getBean( App.class ).entry();
   }
}