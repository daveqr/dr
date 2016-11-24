package com.davedaniels.dr.service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import com.davedaniels.dr.model.NlpData;

public class NlpService {


   private String fileName;


   public NlpService() {
      fileName = "nlp_data.txt";
   }


   public void setFileName( String fileName ) {
      this.fileName = fileName;
   }


   public void process() throws Exception {
      String text = loadTextFromClasspath( fileName );

      List<Integer> boundaries = findBoundaries( text );
      List<String> words = tokenizeWords( text );

      NlpData data = new NlpData();
      data.setBoundaries( boundaries );
      data.setWords( words );

      String xmlString = convertDataToXml( data );

      printXmlString( xmlString );
   }


   protected void printXmlString( String xmlString ) {
      System.out.println( xmlString );
   }


   protected String convertDataToXml( NlpData data ) throws JAXBException, PropertyException {
      Marshaller jaxbMarshaller = JAXBContext.newInstance( NlpData.class ).createMarshaller();
      jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal( data, sw );

      return sw.toString();
   }


   protected String loadTextFromClasspath( final String fileName ) throws IOException, URISyntaxException {
      return new String( Files.readAllBytes( Paths.get( getClass().getClassLoader().getResource( fileName ).toURI() ) ) );
   }


   protected List<Integer> findBoundaries( String text ) {

      BreakIterator iterator = BreakIterator.getSentenceInstance();
      List<Integer> boundaries = new ArrayList<>();

      iterator.setText( text );
      int boundary = iterator.first();

      while ( boundary != BreakIterator.DONE ) {
         boundaries.add( boundary );
         boundary = iterator.next();
      }

      return boundaries;
   }


   protected List<String> tokenizeWords( String text ) {
      BreakIterator wordIterator = BreakIterator.getWordInstance();

      List<String> words = new ArrayList<>();

      wordIterator.setText( text );

      int start = wordIterator.first();
      int end = wordIterator.next();

      while ( end != BreakIterator.DONE ) {
         String word = text.substring( start, end );
         if ( Character.isLetterOrDigit( word.charAt( 0 ) ) ) {
            words.add( word );
         }

         start = end;
         end = wordIterator.next();
      }

      return words;
   }
}
