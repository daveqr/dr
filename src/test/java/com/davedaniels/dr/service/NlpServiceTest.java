package com.davedaniels.dr.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import com.davedaniels.dr.model.NlpData;
import com.davedaniels.dr.service.NlpService;

@RunWith( MockitoJUnitRunner.class )
public class NlpServiceTest {

   @Spy
   private NlpService service;

   private NlpData data = new NlpData();

   private List<Integer> boundaries = new ArrayList<>();

   private List<String> words = new ArrayList<>();

   @Before
   public void setExpectedData() {
      boundaries.add( 1 );
      boundaries.add( 2 );

      words.add( "word1" );
      words.add( "word2" );

      data.setBoundaries( boundaries );
      data.setWords( words );
   }

   @Test
   public void process() throws Exception {
      String fileName = "fileName";
      String text = "abc";
      String xmlString = "expectedXmlString";

      doReturn( text ).when( service ).loadTextFromClasspath( fileName );
      doReturn( boundaries ).when( service ).findBoundaries( text );
      doReturn( words ).when( service ).tokenizeWords( text );
      doReturn( xmlString ).when( service ).convertDataToXml( data );

      service.setFileName( fileName );
      service.process();

      verify( service ).loadTextFromClasspath( fileName );
      verify( service ).findBoundaries( text );
      verify( service ).tokenizeWords( text );
      verify( service ).convertDataToXml( data );
   }


   @Test
   public void convertDataToXml() throws Exception {
      String actual = service.convertDataToXml( data );

      String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
      expected += "<nlpData>";
      expected += "<boundaries><boundary>1</boundary><boundary>2</boundary></boundaries>";
      expected += "<words><word>word1</word><word>word2</word></words></nlpData>";

      Diff diff = DiffBuilder.compare( expected ).withTest( actual ).ignoreWhitespace().build();
      Assert.assertFalse( diff.hasDifferences() );
   }

   @Test
   public void loadTextFromClasspath() throws Exception {
      String expected = "abc 123";
      String actual = service.loadTextFromClasspath( "test-data.txt" );

      Assert.assertEquals( expected, actual );
   }

   @Test
   public void findSentenceBoundaries() throws Exception {
      String text = "Abc 123. Hello, goodbye.";

      List<Integer> actualBoundaries = service.findBoundaries( text );

      Assert.assertEquals( 3, actualBoundaries.size() );
      Assert.assertTrue( actualBoundaries.containsAll( Arrays.asList( 0, 9, 24 ) ) );
   }

   @Test
   public void tokenizeWords() throws Exception {
      String text = "Abc 123. Hello, goodbye.";
      List<String> actualWords = service.tokenizeWords( text );
      System.out.println( actualWords );

      Assert.assertEquals( 4, actualWords.size() );
      Assert.assertTrue( actualWords.containsAll( Arrays.asList( "Abc", "123", "Hello", "goodbye" ) ) );
   }


   @Test
   public void printXmlString() throws Exception {
      ByteArrayOutputStream outContent = new ByteArrayOutputStream();

      PrintStream originalOut = System.out;
      System.setOut( new PrintStream( outContent ) );

      service.printXmlString( "abc" );
      Assert.assertEquals( "abc" + System.getProperty( "line.separator" ), outContent.toString() );

      System.setOut( originalOut );
   }
}