package com.davedaniels.nlp.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.davedaniels.nlp.model.NlpData;
import com.davedaniels.nlp.model.NlpSentence;
import com.davedaniels.nlp.service.NlpSentenceService;

@RunWith( MockitoJUnitRunner.class )
public class NlpServiceTest {

   @Spy
   @InjectMocks
   private NlpSentenceService service;

   @Spy
   private NlpData data;

   private String fileName = "filename";

   private String properNounsFileName = "properNamesFileName";


   @Before
   public void before() {
      service.setSourceFileName( fileName );
      service.setProperNounsFileName( properNounsFileName );
   }


   @Test
   public void process() throws Exception {
      String xml = "abc";
      String sourceFileName = "123";
      String properNounsFileName = "234";

      service.setSourceFileName( sourceFileName );
      service.setProperNounsFileName( properNounsFileName );

      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      PrintStream originalOut = System.out;
      System.setOut( new PrintStream( outContent ) );

      List<String> sourceStrings = new ArrayList<>( Arrays.asList( "1", "2" ) );
      List<String> properNouns = Arrays.asList( "n1", "n2" );

      doReturn( sourceStrings ).when( service ).loadSourceStrings( );
      doReturn( properNouns ).when( service ).loadProperNouns( properNounsFileName );
      doReturn( data ).when( service ).aggregateData( sourceStrings, properNouns );
      doReturn( xml ).when( data ).toXml();
      doReturn( properNouns ).when( data ).findProperNouns();

      service.process();

      verify( service ).loadSourceStrings( );
      verify( service ).loadProperNouns( properNounsFileName );
      verify( data ).toXml();

      String expected = xml + System.getProperty( "line.separator" );
      expected += "Found these proper nouns:" + System.getProperty( "line.separator" );
      expected += "n1" + System.getProperty( "line.separator" );
      expected += "n2" + System.getProperty( "line.separator" );

      Assert.assertEquals( expected, outContent.toString() );

      System.setOut( originalOut );
   }


   @Test
   public void loadSourceString_zip() throws Exception {
      List<String> sourceStrings = service.loadSourceStrings( );

      Collections.sort( sourceStrings );
      Assert.assertEquals( 2, sourceStrings.size() );
      Assert.assertTrue( sourceStrings.get( 0 ).contains( "abc 123." ) );
      Assert.assertTrue( sourceStrings.get( 1 ).contains( "oh for a muse of fire" ) );
   }


   @Test
   public void loadSourceString_testFile() throws Exception {
      List<String> sourceStrings = service.loadSourceStrings( );

      Assert.assertEquals( 1, sourceStrings.size() );
      Assert.assertTrue( sourceStrings.get( 0 ).contains( "abc 123" ) );
   }


   @Test
   public void loadProperNouns() throws Exception {
      List<String> properNouns = service.loadProperNouns( "test-proper-nouns.txt" );

      Assert.assertEquals( 3, properNouns.size() );
      Assert.assertTrue( properNouns.containsAll( Arrays.asList( "Ernst Haeckel", "Franz Ferdinand", "Gavrilo Princip" ) ) );
   }


   @Test
   public void aggregateData() throws Exception {
      List<String> sourceStrings = new ArrayList<>();
      sourceStrings.addAll( Arrays.asList( "123 abc", "234 def" ) );
      List<String> properNouns = new ArrayList<>( Arrays.asList( "123", "xxx" ) );

      NlpData data = service.aggregateData( sourceStrings, properNouns );

      Assert.assertEquals( 2, data.getSentences().size() );
      Assert.assertEquals( 1, data.findProperNouns().size() );


      NlpSentence s1 = new NlpSentence( 0, "123 abc" );
      NlpSentence s2 = new NlpSentence( 0, "234 def" );

      Assert.assertEquals( s1, data.getSentences().get( 0 ) );
      Assert.assertEquals( s2, data.getSentences().get( 1 ) );

      data.findProperNouns().contains( "123" );
   }
}