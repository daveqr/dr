/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.davedaniels.nlp.app.App;
import com.davedaniels.nlp.dao.DbDao;
import com.davedaniels.nlp.dao.SourceDao;

/**
 * Application demo runner. It's only purpose it to kick off the demo.
 * 
 * @since 1.0.0
 */
@SpringBootApplication
public class NlpLibDemo implements ApplicationRunner {

   @Autowired
   private ApplicationContext ctx;

   @Autowired
   @Qualifier( "dbSourceDao" )
   private DbDao dbDao;

   @Autowired
   @Qualifier( "fileSourceDao" )
   private SourceDao dao;


   public static void main( String[] args ) {
      SpringApplication.run( NlpLibDemo.class, args );
   }


   @Override
   public void run( ApplicationArguments arg0 ) throws Exception {
      updateInMemoryDaoWithLongText();

      ctx.getBean( App.class ).entry();
   }


   /**
    * Initializes the in-memory database with the longtext from the file.
    * 
    * @throws Exception
    */
   private void updateInMemoryDaoWithLongText() throws Exception {
      dbDao.insertClob( 1, dao.loadSourceStrings().get( 0 ) );
   }
}