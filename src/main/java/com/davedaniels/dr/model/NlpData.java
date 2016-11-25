package com.davedaniels.dr.model;

import java.io.StringWriter;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NlpData {


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


   public NlpData( String text ) {
      this.sentences = this.parseSentences( text );
   }


   public NlpData() {}


   @XmlElementWrapper( name = "sentences" )
   @XmlElement( name = "sentence" )
   private List<NlpSentence> sentences = new ArrayList<>();


   public void setSentences( List<NlpSentence> sentences ) {
      this.sentences = sentences;
   }


   public String toXml() throws Exception {
      StringWriter sw = new StringWriter();

      Marshaller jaxbMarshaller = JAXBContext.newInstance( NlpData.class ).createMarshaller();
      jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
      jaxbMarshaller.marshal( this, sw );

      return sw.toString();
   }


   protected List<NlpSentence> parseSentences( final String text ) {
      List<NlpSentence> sentences = new ArrayList<>();

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

      return sentences;
   }
}