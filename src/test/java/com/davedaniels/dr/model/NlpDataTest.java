package com.davedaniels.dr.model;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

@RunWith( MockitoJUnitRunner.class )
public class NlpDataTest {

   @Spy
   NlpData data;

   @Test
   public void parseSentences() throws Exception {
      String text = "Abc 123. Hello, goodbye.";

      List<NlpSentence> expected = new ArrayList<>();
      expected.addAll( Arrays.asList( new NlpSentence( 0, "Abc 123. " ), new NlpSentence( 9, "Hello, goodbye." ) ) );

      data.parseSentences( text );
      List<NlpSentence> actual = data.getSentences();

      Assert.assertEquals( expected.size(), actual.size() );
      Assert.assertTrue( actual.containsAll( expected ) );
   }


   @Test
   public void toXml() throws Exception {
      String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
      expected += "<nlpData>";
      expected += "<sentences>";
      expected += "<sentence boundary=\"0\"><properNouns/><value>This is 1</value><words><word>This</word><word>is</word><word>1</word></words></sentence>";
      expected += "<sentence boundary=\"10\"><properNouns/><value>This is 2</value><words><word>This</word><word>is</word><word>2</word></words></sentence>";
      expected += "</sentences>";
      expected += "</nlpData>";

      List<NlpSentence> sentences = new ArrayList<>();
      sentences.addAll( Arrays.asList( new NlpSentence( 0, "This is 1" ), new NlpSentence( 10, "This is 2" ) ) );

      data.setSentences( sentences );
      String actual = data.toXml();

      Diff diff = DiffBuilder.compare( expected ).withTest( actual ).ignoreWhitespace().build();
      Assert.assertFalse( diff.hasDifferences() );
   }

   @Test
   public void findProperNouns() throws Exception {
      List<NlpSentence> sentences = new ArrayList<>();

      NlpSentence s1 = Mockito.spy( new NlpSentence( 0, "This is 1" ) );
      doReturn( Arrays.asList( "p1", "p2" ) ).when( s1 ).getProperNouns();

      NlpSentence s2 = Mockito.spy( new NlpSentence( 10, "This is 2" ) );
      doReturn( new ArrayList<>() ).when( s2 ).getProperNouns();

      sentences.addAll( Arrays.asList( s1, s2 ) );

      data.setSentences( sentences );

      List<String> actual = data.findProperNouns();

      Assert.assertEquals( 2, actual.size() );
      Assert.assertTrue( actual.containsAll( Arrays.asList( "p1", "p2" ) ) );

      verify( s1 ).getProperNouns();
      verify( s2 ).getProperNouns();
   }

   @Test
   public void addProperNouns() throws Exception {
      List<NlpSentence> sentences = new ArrayList<>();
      NlpSentence s1 = Mockito.spy( new NlpSentence( 0, "This is 1" ) );
      NlpSentence s2 = Mockito.spy( new NlpSentence( 10, "This is 2" ) );

      sentences.addAll( Arrays.asList( s1, s2 ) );

      List<String> properNouns = new ArrayList<>();

      Mockito.doNothing().when( s1 ).addProperNouns( properNouns );
      Mockito.doNothing().when( s2 ).addProperNouns( properNouns );

      data.setSentences( sentences );
      data.setProperNouns( properNouns );

      data.addProperNounsToSentences();

      verify( s1 ).addProperNouns( properNouns );
      verify( s2 ).addProperNouns( properNouns );
   }
}