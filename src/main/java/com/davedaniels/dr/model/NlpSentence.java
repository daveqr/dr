package com.davedaniels.dr.model;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class NlpSentence {

   private int boundary;

   private List<String> words = new ArrayList<>();

   private String sentence;

   public NlpSentence() {

   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + boundary;
      result = prime * result + ((sentence == null) ? 0 : sentence.hashCode());
      return result;
   }

   @Override
   public boolean equals( Object obj ) {
      if ( this == obj ) return true;
      if ( obj == null ) return false;
      if ( getClass() != obj.getClass() ) return false;
      NlpSentence other = (NlpSentence) obj;
      if ( boundary != other.boundary ) return false;
      if ( sentence == null ) {
         if ( other.sentence != null ) return false;
      } else if ( !sentence.equals( other.sentence ) ) return false;
      return true;
   }

   public NlpSentence( int boundary, String sentence ) {
      this.boundary = boundary;
      this.sentence = sentence;
   }

   @XmlAttribute
   public int getBoundary() {
      return boundary;
   }

   public void setSentence( String sentence ) {
      this.sentence = sentence;
   }

   @XmlElement( name = "value" )
   public String getSentence() {
      return sentence;
   }


   @XmlElementWrapper( name = "words" )
   @XmlElement( name = "word" )
   public void setWords( List<String> words ) {
      this.words = words;
   }

   public List<String> getWords() {
      if ( words == null || words.isEmpty() ) {
         words = tokenize();
      }

      return words;
   }


   protected List<String> tokenize() {
      List<String> words = new ArrayList<>();

      BreakIterator wordIterator = BreakIterator.getWordInstance();
      wordIterator.setText( sentence );

      int start = wordIterator.first();
      int end = wordIterator.next();

      while ( end != BreakIterator.DONE ) {
         String word = sentence.substring( start, end );
         if ( Character.isLetterOrDigit( word.charAt( 0 ) ) ) {
            words.add( word );
         }

         start = end;
         end = wordIterator.next();
      }

      return words;
   }
}
