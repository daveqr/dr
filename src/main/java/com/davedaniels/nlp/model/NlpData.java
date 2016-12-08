/* Copyright (c) 2016 Dave Daniels */
package com.davedaniels.nlp.model;

import java.io.StringWriter;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * Represents a natural language text block.
 * 
 * @since 1.0.0
 */
@XmlRootElement
public class NlpData {

   private static final Logger LOG = LoggerFactory.getLogger( NlpData.class );

   private List<String> properNouns = new ArrayList<>();

   private List<NlpSentence> sentences = new ArrayList<>();


   public NlpData( String text, List<String> properNouns ) {
      this.properNouns = properNouns;

      parseSentences( text );
      addProperNounsToSentences();
   }


   public NlpData( List<String> properNouns ) {
      this.properNouns = properNouns;
   }


   public NlpData() {}


   public void setProperNouns( List<String> properNouns ) {
      this.properNouns = properNouns;
   }


   public List<String> findProperNouns() {
      return Optional.of( sentences ).or( new ArrayList<>() ).stream()
            .map( NlpSentence::getProperNouns ).flatMap( Collection::stream )
            .distinct().sorted()
            .collect( Collectors.collectingAndThen( Collectors.toList(), Collections::unmodifiableList ) );
   }


   @XmlElementWrapper( name = "sentences" )
   @XmlElement( name = "sentence" )
   public List<NlpSentence> getSentences() {
      return sentences;
   }


   public void setSentences( List<NlpSentence> sentences ) {
      this.sentences = sentences;
   }


   public String toXml() {
      StringWriter sw = new StringWriter();

      Marshaller jaxbMarshaller;
      try {
         jaxbMarshaller = JAXBContext.newInstance( NlpData.class ).createMarshaller();
         jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
         jaxbMarshaller.marshal( this, sw );
      }
      catch ( JAXBException e ) {
         LOG.error( "Problem marshelling", e );
      }

      return sw.toString();
   }


   protected void parseSentences( final String text ) {
      BreakIterator iterator = BreakIterator.getSentenceInstance();
      iterator.setText( text );

      int begin, end;
      begin = end = iterator.first();

      while ( begin != BreakIterator.DONE ) {
         if ( (end = iterator.next()) != BreakIterator.DONE ) {
            sentences.add( new NlpSentence( begin, text.substring( begin, end ) ) );
         }

         begin = end;
      }
   }


   protected void addProperNounsToSentences() {
      for ( NlpSentence sentence : sentences ) {
         sentence.addProperNouns( properNouns );
      }
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((sentences == null) ? 0 : sentences.hashCode());
      return result;
   }


   @Override
   public boolean equals( Object obj ) {
      if ( this == obj ) return true;
      if ( obj == null ) return false;
      if ( getClass() != obj.getClass() ) return false;
      NlpData other = (NlpData) obj;
      if ( sentences == null ) {
         if ( other.sentences != null ) return false;
      } else if ( !sentences.equals( other.sentences ) ) return false;
      return true;
   }
}