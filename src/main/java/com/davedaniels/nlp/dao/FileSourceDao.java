package com.davedaniels.nlp.dao;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository( "fileSourceDao" )
public class FileSourceDao implements SourceStringDao {

   @Value( "${file.single}" )
   private String fileName;


   public List<String> loadSourceStrings() throws Exception {
      List<String> sourceStrings = new ArrayList<>();

      Path path = findPath( fileName );
      sourceStrings.add( new String( Files.readAllBytes( path ) ) );

      return sourceStrings;
   }


   private Path findPath( final String fileName ) throws URISyntaxException {
      return Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() );
   }
}
