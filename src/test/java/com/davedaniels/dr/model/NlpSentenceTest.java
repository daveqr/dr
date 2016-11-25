package com.davedaniels.dr.model;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class NlpSentenceTest {

   @Spy
   NlpSentence sentence;

   @Test
   public void getWords_Null() throws Exception {
      sentence.setWords( null );

      List<String> expected = new ArrayList<>();

      doReturn( expected ).when( sentence ).tokenize();
      List<String> actual = sentence.getWords();
      Assert.assertSame( expected, actual );
   }

   @Test
   public void getWords_Empty() throws Exception {
      sentence.setWords( new ArrayList<>() );

      List<String> expected = new ArrayList<>();

      doReturn( expected ).when( sentence ).tokenize();
      List<String> actual = sentence.getWords();
      Assert.assertSame( expected, actual );
   }

   @Test
   public void getWords() throws Exception {
      List<String> expected = new ArrayList<>( Arrays.asList( "xxx" ) );
      sentence.setWords( expected );

      List<String> actual = sentence.getWords();
      Assert.assertSame( expected, actual );

      verify( sentence ).setWords( expected );
      verify( sentence ).getWords();
      verifyNoMoreInteractions( sentence );
   }

   @Test
   public void tokenize() {
      String text = "Abc 123. Hello, goodbye.";
      sentence.setSentence( text );

      List<String> actual = sentence.tokenize();

      Assert.assertEquals( 4, actual.size() );
      Assert.assertTrue( actual.containsAll( Arrays.asList( "Abc", "123", "Hello", "goodbye" ) ) );
   }

}
