package com.davedaniels.dr.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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

   @Before
   public void before() {
      service.setFileName( fileName );
   }

   @Test
   public void process() throws Exception {
      // String fileName = "fileName";
      String xml = "abc";

      ByteArrayOutputStream outContent = new ByteArrayOutputStream();
      PrintStream originalOut = System.out;
      System.setOut( new PrintStream( outContent ) );

      doReturn( data ).when( service ).loadData();
      doReturn( xml ).when( data ).toXml();

      // service.setFileName( fileName );
      service.process();

      verify( service ).loadData();
      verify( data ).toXml();

      Assert.assertEquals( xml + System.getProperty( "line.separator" ), outContent.toString() );

      System.setOut( originalOut );
   }

   @Test
   public void loadData() throws Exception {
      String text = "abc";

      doReturn( text ).when( service ).loadTextFromClasspath( fileName );

      NlpData expected = new NlpData( text );
      NlpData actual = service.loadData();

      Assert.assertEquals( expected, actual );
      verify( service ).loadTextFromClasspath( fileName );
   }

   @Test
   public void loadTextFromClasspath() throws Exception {
      String expected = "abc 123";
      String actual = service.loadTextFromClasspath( "test-data.txt" );

      Assert.assertEquals( expected, actual );
   }
}