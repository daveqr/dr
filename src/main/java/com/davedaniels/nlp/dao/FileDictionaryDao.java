/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * Loads dictionary of proper nouns from a file.
 * 
 * @since 1.0.0
 */
@Primary
@Repository( "fileDictionaryDao" )
public class FileDictionaryDao implements DictionaryDao {

   private static final Logger LOG = LoggerFactory.getLogger( FileDictionaryDao.class );

   @Value( "${file.dictionary}" )
   private String fileName;


   public void setFileName( String fileName ) {
      this.fileName = fileName;
   }


   public List<String> loadProperNouns() {
      List<String> properNounsList = new ArrayList<>();

      try {
         String properNouns = new String( Files.readAllBytes( findPath( fileName ) ) );
         properNounsList.addAll( Arrays.asList( properNouns.split( System.getProperty( "line.separator" ) ) ) );
      }
      catch ( IOException | URISyntaxException e ) {
         LOG.error( "Problem loading dictionary.", e );
      }

      return properNounsList;
   }

   private Path findPath( final String fileName ) throws URISyntaxException {
      return Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() );
   }
}
