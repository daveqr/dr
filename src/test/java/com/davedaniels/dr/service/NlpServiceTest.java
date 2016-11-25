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
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.davedaniels.dr.model.NlpData;

@RunWith( MockitoJUnitRunner.class )
public class NlpServiceTest {

   @Spy
   @InjectMocks
   private NlpService service;

   @Spy
   private NlpData data;

   private String fileName = "filename";

   private String properNounsFileName = "properNamesFileName";


   @Before
   public void before() {
      service.setFileName( fileName );
      service.setProperNounsFileName( properNounsFileName );
   }


   @Test
   public void process() throws Exception {
      String xml = "abc";

      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      PrintStream originalOut = System.out;
      System.setOut( new PrintStream( outContent ) );

      doReturn( data ).when( service ).loadData();
      doReturn( xml ).when( data ).toXml();
      doReturn( Arrays.asList( "1", "2" ) ).when( data ).findProperNouns();

      service.process();

      verify( service ).loadData();
      verify( data ).toXml();

      String expected = xml + System.getProperty( "line.separator" );
      expected += "Found these proper nouns:" + System.getProperty( "line.separator" );
      expected += "1" + System.getProperty( "line.separator" );
      expected += "2" + System.getProperty( "line.separator" );

      Assert.assertEquals( expected, outContent.toString() );

      System.setOut( originalOut );
   }


   @Test
   public void loadData() throws Exception {
      String text = "abc";
      List<String> properNouns = new ArrayList<>();

      doReturn( text ).when( service ).loadText( fileName );
      doReturn( properNouns ).when( service ).loadProperNouns( properNounsFileName );


      NlpData expected = new NlpData( text, properNouns );
      NlpData actual = service.loadData();

      Assert.assertEquals( expected, actual );
      verify( service ).loadText( fileName );
      verify( service ).loadProperNouns( properNounsFileName );
   }


   @Test
   public void loadTextFromClasspath() throws Exception {
      String expected = "abc 123";
      String actual = service.loadText( "test-data.txt" );

      Assert.assertEquals( expected, actual );
   }


   @Test
   public void loadProperNouns() throws Exception {
      List<String> properNouns = service.loadProperNouns( "test-proper-nouns.txt" );

      Assert.assertEquals( 3, properNouns.size() );
      Assert.assertTrue( properNouns.containsAll( Arrays.asList( "Ernst Haeckel", "Franz Ferdinand", "Gavrilo Princip" ) ) );
   }
}