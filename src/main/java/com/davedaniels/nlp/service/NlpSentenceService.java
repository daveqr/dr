/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davedaniels.nlp.dao.DictionaryDao;
import com.davedaniels.nlp.dao.SourceStringDao;
import com.davedaniels.nlp.model.NlpData;

/**
 * Sentence processing service.
 * 
 * @since 1.0.0
 */
@Service( "nlpService" )
public class NlpSentenceService implements NlpService {

   private static final Logger LOG = LoggerFactory.getLogger( NlpSentenceService.class );

   @Autowired
   private SourceStringDao dao;

   @Autowired
   private DictionaryDao dictionaryDao;


   public void setDao( SourceStringDao dao ) {
      this.dao = dao;
   }


   public void setDictionaryDao( DictionaryDao dictionaryDao ) {
      this.dictionaryDao = dictionaryDao;
   }


   @Override
   public NlpData process() throws Exception {
      LOG.debug( "Beginning String process" );
      return aggregateData( dao.loadSourceStrings(), dictionaryDao.loadProperNouns() );
   }


   protected NlpData aggregateData( final List<String> sourceStrings, final List<String> properNouns ) {
      ExecutorService executorPool = Executors.newFixedThreadPool( 5 );
      NlpData data = new NlpData( properNouns );

      try {
         sourceStrings.stream()
               .map( text -> CompletableFuture.supplyAsync( () -> new NlpData( text, properNouns ), executorPool ) )
               .forEach( future -> data.getSentences().addAll( future.join().getSentences() ) );
      }
      finally {
         executorPool.shutdown();
      }

      return data;
   }
}