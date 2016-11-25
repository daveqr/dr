package com.davedaniels.dr.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.davedaniels.dr.model.NlpData;

public class NlpService {

   private String fileName;

   private NlpData data;

   public NlpService() {
      fileName = "nlp_data.txt";
   }


   public void setFileName( String fileName ) {
      this.fileName = fileName;
   }


   public void process() throws Exception {
      data = loadData();

      System.out.println( data.toXml() );
   }

   
   protected NlpData loadData() throws IOException, URISyntaxException {
      return new NlpData( loadTextFromClasspath( fileName ) );
   }

   
   protected String loadTextFromClasspath( final String fileName ) throws IOException, URISyntaxException {
      return new String( Files.readAllBytes( Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() ) ) );
   }
}