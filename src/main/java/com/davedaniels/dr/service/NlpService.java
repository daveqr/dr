package com.davedaniels.dr.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.davedaniels.dr.model.NlpData;

public class NlpService {

   private String sourceFileName;

   private String properNounsFileName;

   private NlpData data;

   public NlpService() {
      sourceFileName = "nlp_data.txt";
      properNounsFileName = "NER.txt";
   }


   public void setFileName( String fileName ) {
      this.sourceFileName = fileName;
   }


   public void setProperNounsFileName( String properNounsFileName ) {
      this.properNounsFileName = properNounsFileName;
   }


   public void process() throws Exception {
      data = loadData();

      String xml = data.toXml();
      List<String> foundNouns = data.findProperNouns();

      System.out.println( xml );
      System.out.println( "Found these proper nouns:" );
      for ( String string : foundNouns ) {
         System.out.println( string );
      }
   }


   protected NlpData loadData() throws IOException, URISyntaxException {
      List<String> properNouns = loadProperNouns( properNounsFileName );
      String text = loadText( sourceFileName );

      NlpData data = new NlpData( text, properNouns );

      return data;
   }


   protected String loadText( final String fileName ) throws IOException, URISyntaxException {
      return new String( Files.readAllBytes( Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() ) ) );
   }

   protected List<String> loadProperNouns( final String fileName ) throws IOException, URISyntaxException {
      String xxx = new String( Files.readAllBytes( Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() ) ) );

      return Arrays.asList( xxx.split( System.getProperty( "line.separator" ) ) );
   }
}