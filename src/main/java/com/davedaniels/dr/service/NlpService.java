/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.dr.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.davedaniels.dr.model.NlpData;

/**
 * Text processing service.
 * 
 * @since 1.0.0
 */
public class NlpService {

   private String sourceFileName;

   private String properNounsFileName;

   public NlpService() throws IOException, URISyntaxException {
      // sourceFileName would probably be injected
      // sourceFileName = "nlp_data.txt";
      sourceFileName = "nlp_data.zip";
      properNounsFileName = "NER.txt";
   }


   public void setSourceFileName( final String sourceFileName ) {
      this.sourceFileName = sourceFileName;
   }


   public void setProperNounsFileName( final String properNounsFileName ) {
      this.properNounsFileName = properNounsFileName;
   }

   /**
    * Process {@link #sourceFileName} and writes the XML representation to {@link System#out}.
    * 
    * @throws Exception
    */
   public void process() throws Exception {
      NlpData data = aggregateData( loadSourceStrings( sourceFileName ), loadProperNouns( properNounsFileName ) );

      System.out.println( data.toXml() );
      System.out.println( "Found these proper nouns:" );
      for ( String noun : data.findProperNouns() ) {
         System.out.println( noun );
      }
   }


   protected NlpData aggregateData( final List<String> sourceStrings, final List<String> properNouns ) {
      List<Callable<NlpData>> tasks = new ArrayList<>();
      ExecutorService executorPool = Executors.newFixedThreadPool( 5 );

      for ( String text : sourceStrings ) {
         tasks.add( () -> {
            return new NlpData( text, properNouns );
         } );
      }

      NlpData data = new NlpData( properNouns );
      try {
         for ( Future<NlpData> future : executorPool.invokeAll( tasks ) ) {
            data.getSentences().addAll( future.get().getSentences() );
         }
      }
      catch ( InterruptedException | ExecutionException e ) {
         e.printStackTrace();
      }

      executorPool.shutdown();

      return data;
   }


   protected List<String> loadSourceStrings( final String fileName ) throws Exception {
      List<String> sourceStrings = new ArrayList<>();

      Path path = findPath( fileName );
      try (final ZipFile zipFile = new ZipFile( path.toFile() )) {
         final Enumeration<? extends ZipEntry> entries = zipFile.entries();
         while ( entries.hasMoreElements() ) {
            final ZipEntry entry = entries.nextElement();

            // Some extra Mac weirdness in the zip
            if ( !entry.isDirectory() && !entry.getName().contains( "__MACOSX" ) && !entry.getName().contains( "DS_Store" ) ) {
               sourceStrings.add( convertInputStreamToString( zipFile.getInputStream( entry ) ) );
            }
         }
      }
      catch ( ZipException e ) {
         // this just means it's a text file, not a zip file

         sourceStrings.add( new String( Files.readAllBytes( path ) ) );
      }

      return sourceStrings;
   }


   private Path findPath( final String fileName ) throws URISyntaxException {
      return Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() );
   }


   // Would probably use a helper library for this in situ
   private String convertInputStreamToString( final InputStream inputStream ) throws IOException {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ( (length = inputStream.read( buffer )) != -1 ) {
         result.write( buffer, 0, length );
      }

      String string = result.toString( "UTF-8" );
      result.flush();

      return string;
   }


   protected List<String> loadProperNouns( final String fileName ) {
      List<String> properNounsList = new ArrayList<>();

      try {
         String properNouns = new String( Files.readAllBytes( findPath( fileName ) ) );
         properNounsList.addAll( Arrays.asList( properNouns.split( System.getProperty( "line.separator" ) ) ) );
      }
      catch ( IOException | URISyntaxException e ) {
         e.printStackTrace();
      }

      return properNounsList;
   }
}