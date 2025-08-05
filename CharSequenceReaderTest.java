/*
 * Copyright (C) 2013 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.io;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.CharBuffer;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * @author Colin Decker
 */
@NullUnmarked
public class CharSequenceReaderTest extends TestCase {

  // Test data constants
  private static final String EMPTY_STRING = "";
  private static final String SHORT_STRING = "abc";
  private static final String MEDIUM_STRING = "abcde";
  private static final String LONG_STRING = "abcdefghijkl";
  private static final String COMPLEX_STRING = 
      "abcdefghijklmnopqrstuvwxyz\n" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r" +
      "0123456789\r\n" +
      "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| ";
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  private static final String TEST_STRING_FOR_READER = "12345";

  // Test buffer sizes
  private static final int SMALL_BUFFER_SIZE = 5;
  private static final int STANDARD_BUFFER_SIZE = 10;

  public void testReadEmptyString() throws IOException {
    verifyReaderBehaviorForString(EMPTY_STRING);
  }

  public void testReadsStringsCorrectly() throws IOException {
    verifyReaderBehaviorForString(SHORT_STRING);
    verifyReaderBehaviorForString(MEDIUM_STRING);
    verifyReaderBehaviorForString(LONG_STRING);
    verifyReaderBehaviorForString(COMPLEX_STRING);
  }

  public void testMarkAndReset() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(ALPHABET);
    assertTrue("Reader should support mark operations", reader.markSupported());

    // Test initial read and reset
    String firstRead = readEntireContent(reader);
    assertEquals("First read should return entire alphabet", ALPHABET, firstRead);
    verifyReaderIsFullyConsumed(reader);

    // Test reset and re-read
    reader.reset();
    String secondRead = readEntireContent(reader);
    assertEquals("Second read after reset should return same content", ALPHABET, secondRead);
    verifyReaderIsFullyConsumed(reader);

    // Test skip, mark, and partial read
    reader.reset();
    int skipCount = 5;
    long actuallySkipped = reader.skip(skipCount);
    assertEquals("Should skip exactly 5 characters", skipCount, actuallySkipped);
    
    reader.mark(Integer.MAX_VALUE);
    String remainingContent = readEntireContent(reader);
    String expectedRemaining = ALPHABET.substring(skipCount);
    assertEquals("Should read remaining content after skip", expectedRemaining, remainingContent);
    verifyReaderIsFullyConsumed(reader);

    // Test reset to mark position
    reader.reset();
    String contentFromMark = readEntireContent(reader);
    assertEquals("Reset should return to marked position", expectedRemaining, contentFromMark);
    verifyReaderIsFullyConsumed(reader);
  }

  public void testIllegalArguments() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(TEST_STRING_FOR_READER);
    char[] buffer = new char[STANDARD_BUFFER_SIZE];

    // Test invalid buffer operations
    verifyThrowsIndexOutOfBounds(() -> reader.read(buffer, 0, 11), 
        "Reading more than buffer capacity");
    verifyThrowsIndexOutOfBounds(() -> reader.read(buffer, 10, 1), 
        "Reading beyond buffer end");
    verifyThrowsIndexOutOfBounds(() -> reader.read(buffer, 11, 0), 
        "Starting beyond buffer bounds");
    verifyThrowsIndexOutOfBounds(() -> reader.read(buffer, -1, 5), 
        "Negative offset");
    verifyThrowsIndexOutOfBounds(() -> reader.read(buffer, 5, -1), 
        "Negative length");

    // Test invalid skip and mark operations
    verifyThrowsIllegalArgument(() -> reader.skip(-1), 
        "Negative skip count");
    verifyThrowsIllegalArgument(() -> reader.mark(-1), 
        "Negative mark read-ahead limit");
  }

  public void testMethodsThrowWhenClosed() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(EMPTY_STRING);
    reader.close();

    // Verify all read operations throw IOException when reader is closed
    verifyThrowsIOException(() -> reader.read(), 
        "Single character read on closed reader");
    verifyThrowsIOException(() -> reader.read(new char[STANDARD_BUFFER_SIZE]), 
        "Array read on closed reader");
    verifyThrowsIOException(() -> reader.read(new char[STANDARD_BUFFER_SIZE], 0, STANDARD_BUFFER_SIZE), 
        "Array read with offset on closed reader");
    verifyThrowsIOException(() -> reader.read(CharBuffer.allocate(STANDARD_BUFFER_SIZE)), 
        "CharBuffer read on closed reader");

    // Verify other operations throw IOException when reader is closed
    verifyThrowsIOException(() -> reader.skip(10), 
        "Skip on closed reader");
    verifyThrowsIOException(() -> reader.ready(), 
        "Ready check on closed reader");
    verifyThrowsIOException(() -> reader.mark(10), 
        "Mark on closed reader");
    verifyThrowsIOException(() -> reader.reset(), 
        "Reset on closed reader");
  }

  /**
   * Comprehensive test that verifies a CharSequenceReader correctly reads the given string
   * using all available read methods.
   */
  private static void verifyReaderBehaviorForString(CharSequence input) throws IOException {
    String expectedContent = input.toString();

    testCharacterByCharacterReading(input, expectedContent);
    testFullArrayReading(input, expectedContent);
    testChunkedArrayReading(input, expectedContent);
    testFullCharBufferReading(input, expectedContent);
    testChunkedCharBufferReading(input, expectedContent);
    testSkipOperations(input, expectedContent);
  }

  private static void testCharacterByCharacterReading(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    
    for (int i = 0; i < expected.length(); i++) {
      int readChar = reader.read();
      assertEquals("Character at position " + i + " should match", 
          (int) expected.charAt(i), readChar);
    }
    
    verifyReaderIsFullyConsumed(reader);
  }

  private static void testFullArrayReading(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    char[] buffer = new char[expected.length()];
    
    int bytesRead = reader.read(buffer);
    int expectedBytesRead = expected.length() == 0 ? -1 : expected.length();
    assertEquals("Should read expected number of characters", expectedBytesRead, bytesRead);
    assertEquals("Buffer content should match expected string", expected, new String(buffer));
    
    verifyReaderIsFullyConsumed(reader);
  }

  private static void testChunkedArrayReading(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    char[] buffer = new char[SMALL_BUFFER_SIZE];
    StringBuilder result = new StringBuilder();
    
    int bytesRead;
    while ((bytesRead = reader.read(buffer, 0, buffer.length)) != -1) {
      result.append(buffer, 0, bytesRead);
    }
    
    assertEquals("Chunked reading should produce complete content", expected, result.toString());
    verifyReaderIsFullyConsumed(reader);
  }

  private static void testFullCharBufferReading(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    CharBuffer buffer = CharBuffer.allocate(expected.length());
    
    int bytesRead = reader.read(buffer);
    int expectedBytesRead = expected.length() == 0 ? -1 : expected.length();
    assertEquals("Should read expected number of characters into CharBuffer", expectedBytesRead, bytesRead);
    
    Java8Compatibility.flip(buffer);
    assertEquals("CharBuffer content should match expected string", expected, buffer.toString());
    
    verifyReaderIsFullyConsumed(reader);
  }

  private static void testChunkedCharBufferReading(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    CharBuffer buffer = CharBuffer.allocate(SMALL_BUFFER_SIZE);
    StringBuilder result = new StringBuilder();
    
    while (reader.read(buffer) != -1) {
      Java8Compatibility.flip(buffer);
      result.append(buffer);
      Java8Compatibility.clear(buffer);
    }
    
    assertEquals("Chunked CharBuffer reading should produce complete content", expected, result.toString());
    verifyReaderIsFullyConsumed(reader);
  }

  private static void testSkipOperations(CharSequence input, String expected) throws IOException {
    // Test skipping entire content
    CharSequenceReader reader = new CharSequenceReader(input);
    long skipped = reader.skip(Long.MAX_VALUE);
    assertEquals("Should skip entire content length", expected.length(), skipped);
    verifyReaderIsFullyConsumed(reader);

    // Test partial skip and read remainder
    if (expected.length() > 5) {
      reader = new CharSequenceReader(input);
      long partialSkip = reader.skip(5);
      assertEquals("Should skip exactly 5 characters", 5, partialSkip);

      char[] remainderBuffer = new char[expected.length() - 5];
      int remainderRead = reader.read(remainderBuffer, 0, remainderBuffer.length);
      assertEquals("Should read remaining characters after skip", remainderBuffer.length, remainderRead);
      assertEquals("Remaining content should match expected substring", 
          expected.substring(5), new String(remainderBuffer));
      
      verifyReaderIsFullyConsumed(reader);
    }
  }

  /**
   * Verifies that the reader has been fully consumed by testing all read operations return -1.
   */
  private static void verifyReaderIsFullyConsumed(CharSequenceReader reader) throws IOException {
    assertEquals("Single character read should return -1 when fully consumed", -1, reader.read());
    assertEquals("Array read should return -1 when fully consumed", 
        -1, reader.read(new char[STANDARD_BUFFER_SIZE], 0, STANDARD_BUFFER_SIZE));
    assertEquals("CharBuffer read should return -1 when fully consumed", 
        -1, reader.read(CharBuffer.allocate(STANDARD_BUFFER_SIZE)));
    assertEquals("Skip should return 0 when fully consumed", 0, reader.skip(10));
  }

  /**
   * Reads the entire remaining content of the reader character by character.
   */
  private static String readEntireContent(CharSequenceReader reader) throws IOException {
    StringBuilder content = new StringBuilder();
    int character;
    while ((character = reader.read()) != -1) {
      content.append((char) character);
    }
    return content.toString();
  }

  // Exception verification helper methods
  private static void verifyThrowsIndexOutOfBounds(ThrowingRunnable runnable, String description) {
    assertThrows(description + " should throw IndexOutOfBoundsException", 
        IndexOutOfBoundsException.class, runnable);
  }

  private static void verifyThrowsIllegalArgument(ThrowingRunnable runnable, String description) {
    assertThrows(description + " should throw IllegalArgumentException", 
        IllegalArgumentException.class, runnable);
  }

  private static void verifyThrowsIOException(ThrowingRunnable runnable, String description) {
    assertThrows(description + " should throw IOException", 
        IOException.class, runnable);
  }

  @FunctionalInterface
  private interface ThrowingRunnable {
    void run() throws Exception;
  }
}