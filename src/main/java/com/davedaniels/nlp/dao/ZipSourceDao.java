/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.dao;

import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.google.common.io.CharStreams;

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

      Path path = findPath( fileName );
      try (final ZipFile zipFile = new ZipFile( path.toFile() )) {
         final Enumeration<? extends ZipEntry> entries = zipFile.entries();
         while ( entries.hasMoreElements() ) {
            final ZipEntry entry = entries.nextElement();

            // Some extra Mac weirdness in the zip
            if ( !entry.isDirectory() && !entry.getName().contains( "__MACOSX" ) && !entry.getName().contains( "DS_Store" ) ) {
               String text = CharStreams.toString( new InputStreamReader( zipFile.getInputStream( entry ), "UTF-8" ) );
               sourceStrings.add( text );
            }
         }
      }
      catch ( ZipException e ) {
         LOG.error( "Problem unzipping.", e );
      }

      return sourceStrings;
   }


   private Path findPath( final String fileName ) throws URISyntaxException {
      return Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() );
   }
}
