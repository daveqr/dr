/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.davedaniels.nlp.dao.SourceStringDao;
import com.davedaniels.nlp.model.NlpData;
import com.davedaniels.nlp.service.NlpService;

/**
 * Application entry point. Demonstrates use of the {@link NlpService} and
 * is equivalent to a main method, Web controller or some other calling code.
 * 
 * @since 1.0.0
 */
@Component( "appEntry" )
public class App {

   @Autowired
   private NlpService service;

   @Autowired
   private SourceStringDao dao;

   public void entry() throws Exception {
      List<String> strings = dao.loadSourceStrings();
      NlpData data = service.process( strings );

      System.out.println( data.toXml() );
      for ( String noun : data.findProperNouns() ) {
         System.out.println( noun );
      }
   }

   public void setDao( SourceStringDao dao ) {
      this.dao = dao;
   }
}