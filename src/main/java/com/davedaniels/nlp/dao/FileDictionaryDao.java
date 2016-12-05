package com.davedaniels.nlp.dao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.davedaniels.nlp.service.NlpService;

@Repository( "fileDictionaryDao" )
public class FileDictionaryDao implements DictionaryDao {

   private static final Logger LOG = LogManager.getLogger( NlpService.class );

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
