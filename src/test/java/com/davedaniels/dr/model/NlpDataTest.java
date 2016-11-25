package com.davedaniels.dr.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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

      List<NlpSentence> actual = data.parseSentences( text );

      Assert.assertEquals( expected.size(), actual.size() );
      Assert.assertTrue( actual.containsAll( expected ) );
   }


   @Test
   public void toXml() throws Exception {
      String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
      expected += "<nlpData>";
      expected += "<sentences>";
      expected += "<sentence boundary=\"0\"><value>This is 1</value><words><word>This</word><word>is</word><word>1</word></words></sentence>";
      expected += "<sentence boundary=\"10\"><value>This is 2</value><words><word>This</word><word>is</word><word>2</word></words></sentence>";
      expected += "</sentences>";
      expected += "</nlpData>";

      List<NlpSentence> sentences = new ArrayList<>();
      sentences.addAll( Arrays.asList( new NlpSentence( 0, "This is 1" ), new NlpSentence( 10, "This is 2" ) ) );

      data.setSentences( sentences );
      String actual = data.toXml();

      Diff diff = DiffBuilder.compare( expected ).withTest( actual ).ignoreWhitespace().build();
      Assert.assertFalse( diff.hasDifferences() );
   }
}