package com.davedaniels.dr.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NlpData {

   private List<Integer> boundaries = new ArrayList<>();
   private List<String> words = new ArrayList<>();

   @XmlElementWrapper
   @XmlElement( name = "boundary" )
   public void setBoundaries( List<Integer> boundaries ) {
      this.boundaries = boundaries;
   }


   public List<Integer> getBoundaries() {
      return boundaries;
   }


   @XmlElementWrapper
   @XmlElement( name = "word" )
   public List<String> getWords() {
      return words;
   }

   public void setWords( List<String> words ) {
      this.words = words;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((boundaries == null) ? 0 : boundaries.hashCode());
      result = prime * result + ((words == null) ? 0 : words.hashCode());
      return result;
   }


   @Override
   public boolean equals( Object obj ) {
      if ( this == obj ) return true;
      if ( obj == null ) return false;
      if ( getClass() != obj.getClass() ) return false;
      NlpData other = (NlpData) obj;
      if ( boundaries == null ) {
         if ( other.boundaries != null ) return false;
      } else if ( !boundaries.equals( other.boundaries ) ) return false;
      if ( words == null ) {
         if ( other.words != null ) return false;
      } else if ( !words.equals( other.words ) ) return false;
      return true;
   }
   
}