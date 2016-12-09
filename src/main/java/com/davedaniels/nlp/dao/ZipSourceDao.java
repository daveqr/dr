/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * Loads source strings from a zip file for processing.
 * 
 * @since 1.0.0
 */
@Primary
@Repository( "zipSourceDao" )
public class ZipSourceDao implements SourceDao {

   private static final Logger LOG = LoggerFactory.getLogger( ZipSourceDao.class );

   @Value( "${file.zip}" )
   private String fileName;


   public List<String> loadSourceStrings() throws Exception {
      List<String> sourceStrings = new ArrayList<>();

      try (final ZipFile zipFile = new ZipFile( findPath( fileName ).toFile() )) {
         List<String> entries = zipFile.stream()
               .filter( entry -> !entry.isDirectory() )
               .filter( entry -> !entry.getName().contains( "__MACOSX" ) )
               .filter( entry -> !entry.getName().contains( "DS_Store" ) )
               .map( entry -> readStringEntry( zipFile, entry ) )
               .collect( Collectors.toList() );

         sourceStrings.addAll( entries );
      }
      catch ( ZipException e ) {
         LOG.error( "Problem unzipping.", e );
      }

      return sourceStrings;
   }

   
   private String readStringEntry( ZipFile zipFile, ZipEntry entry ) {
      try (BufferedReader buffer = new BufferedReader( new InputStreamReader( zipFile.getInputStream( entry ) ) )) {
         return buffer.lines().collect( Collectors.joining( "\n" ) );
      }
      catch ( IOException e ) {
         LOG.error( "Problem unzipping.", e );
      }

      return null;
   }


   private Path findPath( final String fileName ) throws URISyntaxException {
      return Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() );
   }
}
