package org.example;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.io.EOFException;
import java.io.IOException;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true, useJEE = true)
public class NullReaderTest {

  @Test(timeout = 4000)
  public void testMarkAndReset() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-1L);
    
    // Reads one character. Because size is negative, this increments internal position.
    nullReader.read();
    
    // Marks the current position.
    nullReader.mark(0);
    
    // Resets to the marked position.
    nullReader.reset();
    
    // Verifies that the position is now 1 (because initial read increments by 1, mark resets to that value).
    assertEquals(1L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testReadCharArrayWithOffsetAndLength() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-1L);
    
    // Creates a char array.
    char[] charArray = new char[8];
    
    // Reads from the NullReader into the char array with a negative offset and length.
    nullReader.read(charArray, -1, -3484);
    
    // Reads a single character.
    int result = nullReader.read();
    
    // Assert that the result is 0 (end of stream).
    assertEquals(0, result);
    // Verifies that position has been adjusted (skipping chars increments position even if chars aren't 'read').
    assertEquals(-3483L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testProcessChars() throws Throwable {
    // Creates a NullReader with default size of 0.
    NullReader nullReader = new NullReader();
    
    // Creates a char array.
    char[] charArray = new char[2];
    
    // Processes the char array with a large offset and length. Method does nothing.
    nullReader.processChars(charArray, 1542, 1542);
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testSkipZeroCharacters() throws Throwable {
    // Creates a NullReader with a size of 597.
    NullReader nullReader = new NullReader(597L);
    
    // Skips 0 characters.
    long skipped = nullReader.skip(0);
    
    // Asserts that 0 characters were skipped.
    assertEquals(0L, skipped);
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testSkipCharacters() throws Throwable {
    // Creates a NullReader with a size of 1451, enabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(1451L, true, true);
    
    // Skips 1451 characters (to the end of the reader).
    long skipped = nullReader.skip(1451L);
    
    // Asserts that 1451 characters were skipped.
    assertEquals(1451L, skipped);
    
    // Verifies that the position is now 1451 (end of stream).
    assertEquals(1451L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testReadCharArrayWithZeroLength() throws Throwable {
    // Creates a NullReader with a size of 1279.
    NullReader nullReader = new NullReader(1279L);
    
    // Creates a char array.
    char[] charArray = new char[4];
    
    // Reads 0 characters into the array with a large offset.
    int read = nullReader.read(charArray, 938, 0);
    
    // Asserts that 0 characters were read.
    assertEquals(0, read);
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
    
    // Verifies that the position is still 0.
    assertEquals(0L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testReadCharArrayWithLargeLength() throws Throwable {
    // Creates a NullReader with a size of 1592.
    NullReader nullReader = new NullReader(1592L);
    
    // Creates a char array.
    char[] charArray = new char[5];
    
    // Reads a large number of characters (larger than the reader size) into the array with a large offset.
    int read = nullReader.read(charArray, 2146694131, 2146694131);
    
    // Asserts that the number of characters read is equal to the size of the reader.
    assertEquals(1592, read);
    
    // Verifies that the position is now at the end of the stream.
    assertEquals(1592L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testReadEmptyCharArray() throws Throwable {
    // Creates a NullReader with a size of 1254.
    NullReader nullReader = new NullReader(1254L);
    
    // Creates an empty char array.
    char[] charArray = new char[0];
    
    // Reads into the empty array.
    int read = nullReader.read(charArray);
    
    // Asserts that 0 characters were read.
    assertEquals(0, read);
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testReadCharArray() throws Throwable {
    // Creates a NullReader with a size of 1480, enabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(1480L, true, true);
    
    // Creates a char array.
    char[] charArray = new char[3];
    
    // Reads into the array.
    int read = nullReader.read(charArray);
    
    // Asserts that 3 characters were read.
    assertEquals(3, read);
    
    // Verifies that the position is now 3.
    assertEquals(3L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testProcessChar() throws Throwable {
    // Creates a NullReader with a negative size, enabling mark support, but not EOFException on end.
    NullReader nullReader = new NullReader(-1401L, true, false);
    
    // Processes a single character. Method does nothing.
    int result = nullReader.processChar();
    
    // Asserts that 0 is returned (default).
    assertEquals(0, result);
    
    // Verifies that the size remains negative.
    assertEquals(-1401L, nullReader.getSize());
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testMarkSupportedFalse() throws Throwable {
    // Creates a NullReader with a size of 0, disabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(0L, false, false);
    
    // Verifies that mark is not supported.
    assertFalse(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testGetSizeSingleton() throws Throwable {
    // Gets the size of the singleton instance.
    long size = NullReader.INSTANCE.getSize();
    
    // Asserts that the size is 0.
    assertEquals(0L, size);
  }

  @Test(timeout = 4000)
  public void testGetSize() throws Throwable {
    // Creates a NullReader with a size of 1279.
    NullReader nullReader = new NullReader(1279L);
    
    // Gets the size.
    long size = nullReader.getSize();
    
    // Asserts that the size is 1279.
    assertEquals(1279L, size);
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testGetPosition() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-961L);
    
    // Reads one character. Because size is negative, this increments internal position.
    nullReader.read();
    
    // Gets the position.
    long position = nullReader.getPosition();
    
    // Asserts that the position is 1.
    assertEquals(1L, position);
  }

  @Test(timeout = 4000)
  public void testReadCharArrayEOFException() throws Throwable {
    // Creates a NullReader with a size of 0, enabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(0L, true, true);
    
    // Creates a char array.
    char[] charArray = new char[7];
    
    // Tries to read into the array, expecting an EOFException because the size is 0.
    try {
      nullReader.read(charArray, 0, 0);
      fail("Expecting exception: EOFException");
    } catch (EOFException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testReadCharArrayNullPointerException() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-955L);
    
    // Tries to read into a null array, expecting a NullPointerException.
    try {
      NullReader.INSTANCE.read((char[]) null);
      fail("Expecting exception: NullPointerException");
    } catch (NullPointerException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testReadCharArrayEOFException2() throws Throwable {
    // Creates a NullReader with a size of 0, disabling mark support, but enabling EOFException on end.
    NullReader nullReader = new NullReader(0L, false, true);
    
    // Creates a char array.
    char[] charArray = new char[2];
    
    // Tries to read into the array, expecting an EOFException because the size is 0.
    try {
      nullReader.read(charArray);
      fail("Expecting exception: EOFException");
    } catch (EOFException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testReadEOFException() throws Throwable {
    // Creates a NullReader with a negative size, disabling mark support, but enabling EOFException on end.
    NullReader nullReader = new NullReader(-2271L, false, true);
    
    // Skips a negative number of characters, effectively moving the position to 0.
    nullReader.skip(-2271L);
    
    // Tries to read a single character, expecting an EOFException because the effective size is 0.
    try {
      nullReader.read();
      fail("Expecting exception: EOFException");
    } catch (EOFException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testSkipIOException() throws Throwable {
    // Creates a NullReader with a default size, enabling mark support and EOFException on end.
    NullReader nullReader = new NullReader();
    
    // Creates a char array.
    char[] charArray = new char[5];
    
    // Reads 0 characters into the array (to increment position to simulate reading up to end of stream).
    nullReader.read(charArray, 640, 0);
    
    // Tries to skip characters, expecting an IOException because already at end of stream.
    try {
      nullReader.skip(0L);
      fail("Expecting exception: IOException");
    } catch (IOException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testResetIOException() throws Throwable {
    // Creates a NullReader with a default size, enabling mark support and EOFException on end.
    NullReader nullReader = new NullReader();
    
    // Marks the current position with a negative read limit.
    nullReader.mark(-1791);
    
    // Tries to reset, expecting an IOException because the read limit has been passed.
    try {
      nullReader.reset();
      fail("Expecting exception: IOException");
    } catch (IOException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testGetPositionAndRead() throws Throwable {
    NullReader nullReader0 = NullReader.INSTANCE;
    nullReader0.getPosition();
    nullReader0.read();
  }

  @Test(timeout = 4000)
  public void testSkipNegativeCharacters() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-1324L);
    
    // Skips 8 characters. Since size is negative, position is not advanced.
    long skipped = nullReader.skip(8);
    
    // Asserts that the number of skipped characters matches the size.
    assertEquals(-1324L, skipped);
    
    // Verifies that the position remains at the negative size.
    assertEquals(-1324L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testResetUnsupportedOperationException() throws Throwable {
    // Creates a NullReader with a size of 247, disabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(247L, false, false);
    
    // Tries to reset, expecting an UnsupportedOperationException because mark is not supported.
    try {
      nullReader.reset();
      fail("Expecting exception: UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testResetIOException2() throws Throwable {
    // Creates a NullReader with a size of 0.
    NullReader nullReader = new NullReader(0L);
    
    // Tries to reset without marking first, expecting an IOException.
    try {
      nullReader.reset();
      fail("Expecting exception: IOException");
    } catch (IOException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testMarkAndReadAndReset() throws Throwable {
    NullReader nullReader0 = new NullReader((-955L));
    nullReader0.INSTANCE.mark(1180);
    nullReader0.getPosition();
    char[] charArray0 = new char[1];
    char char0 = 'U';
    charArray0[0] = 'U';
    nullReader0.read(charArray0, 1180, 1180);
    nullReader0.read();
    char[] charArray1 = new char[9];
    charArray1[0] = 'U';
    nullReader0.getPosition();
    nullReader0.INSTANCE.read(charArray1, (-1056), (-196));
  }

  @Test(timeout = 4000)
  public void testMarkUnsupportedOperationException() throws Throwable {
    // Creates a NullReader with a size of 10, disabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(10L, false, false);
    
    // Tries to mark, expecting an UnsupportedOperationException because mark is not supported.
    try {
      nullReader.mark(1452);
      fail("Expecting exception: UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testSkipEOFException() throws Throwable {
    // Creates a NullReader with a negative size, enabling mark support and EOFException on end.
    NullReader nullReader = new NullReader(-348L, true, true);
    
    // Skips a negative number of characters.
    nullReader.skip(-348L);
    
    // Tries to skip again, expecting an EOFException.
    try {
      nullReader.skip(-348L);
      fail("Expecting exception: EOFException");
    } catch (EOFException e) {
      // Expected exception.
    }
  }

  @Test(timeout = 4000)
  public void testClose() throws Throwable {
    // Gets the singleton instance.
    NullReader nullReader = NullReader.INSTANCE;
    
    // Closes the reader.
    nullReader.close();
    
    // Verifies that the position is 0.
    assertEquals(0L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testGetSize2() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-1324L);
    
    // Gets the size.
    long size = nullReader.getSize();
    
    // Asserts that the size is negative.
    assertEquals(-1324L, size);
    
    // Verifies that mark is supported.
    assertTrue(nullReader.markSupported());
  }

  @Test(timeout = 4000)
  public void testReadEndOfStream() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-970L);
    
    // Creates a char array.
    char[] charArray = new char[14];
    
    // Reads into the char array.
    nullReader.read(charArray);
    
    // Reads a single character, expecting end of stream.
    int result = nullReader.read();
    
    // Asserts that -1 is returned (end of stream).
    assertEquals(-1, result);
    
    // Verifies that the position is the negative size.
    assertEquals(-970L, nullReader.getPosition());
  }

  @Test(timeout = 4000)
  public void testMarkSupportedTrue() throws Throwable {
    // Creates a NullReader with a negative size.
    NullReader nullReader = new NullReader(-1324L);
    
    // Verifies that mark is supported.
    boolean markSupported = nullReader.markSupported();
    assertTrue(markSupported);
    
    // Verifies that the size is negative.
    assertEquals(-1324L, nullReader.getSize());
  }
}