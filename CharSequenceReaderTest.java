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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link CharSequenceReader}. The tests are structured to verify:
 * 1. Correctness of all read and skip operations.
 * 2. State management for mark, reset, and close.
 * 3. Robustness against invalid arguments.
 */
@NullUnmarked
@RunWith(JUnit4.class)
public class CharSequenceReaderTest {

  private static final String SHORT_STRING = "abc";
  private static final String LONG_STRING =
      ""
          + "abcdefghijklmnopqrstuvwxyz\n"
          + "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r"
          + "0123456789\r\n"
          + "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| ";

  // =================================================================
  // Reading Correctness Tests
  // =================================================================

  @Test
  public void read_charByChar_readsEntireSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(SHORT_STRING);
    for (int i = 0; i < SHORT_STRING.length(); i++) {
      assertEquals(SHORT_STRING.charAt(i), reader.read());
    }
    assertFullyRead(reader);
  }

  @Test
  public void read_intoFullArray_readsEntireSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(SHORT_STRING);
    char[] buf = new char[SHORT_STRING.length()];

    int charsRead = reader.read(buf);

    assertEquals(SHORT_STRING.length(), charsRead);
    assertEquals(SHORT_STRING, new String(buf));
    assertFullyRead(reader);
  }

  @Test
  public void read_intoArrayInChunks_readsEntireSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(LONG_STRING);
    StringBuilder builder = new StringBuilder();
    char[] buf = new char[5]; // Use a small buffer to ensure chunking

    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      builder.append(buf, 0, read);
    }

    assertEquals(LONG_STRING, builder.toString());
    assertFullyRead(reader);
  }

  @Test
  public void read_intoFullCharBuffer_readsEntireSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(SHORT_STRING);
    CharBuffer buf = CharBuffer.allocate(SHORT_STRING.length());

    int charsRead = reader.read(buf);
    Java8Compatibility.flip(buf); // Prepare buffer for reading

    assertEquals(SHORT_STRING.length(), charsRead);
    assertEquals(SHORT_STRING, buf.toString());
    assertFullyRead(reader);
  }

  @Test
  public void read_intoCharBufferInChunks_readsEntireSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(LONG_STRING);
    StringBuilder builder = new StringBuilder();
    CharBuffer buf = CharBuffer.allocate(5); // Use a small buffer to ensure chunking

    while (reader.read(buf) != -1) {
      Java8Compatibility.flip(buf); // Prepare buffer for reading
      builder.append(buf);
      Java8Compatibility.clear(buf); // Prepare buffer for writing
    }

    assertEquals(LONG_STRING, builder.toString());
    assertFullyRead(reader);
  }

  @Test
  public void read_onEmptySequence_returnsEndOfStream() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("");
    assertFullyRead(reader);
  }

  @Test
  public void skip_fullLength_reachesEndOfSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(LONG_STRING);
    long skipped = reader.skip(Long.MAX_VALUE);

    assertEquals(LONG_STRING.length(), skipped);
    assertFullyRead(reader);
  }

  @Test
  public void skip_partialLength_readsRemainingSequence() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(SHORT_STRING);
    long skipped = reader.skip(1);

    assertEquals(1, skipped);
    assertEquals(SHORT_STRING.substring(1), readFully(reader));
    assertFullyRead(reader);
  }

  // =================================================================
  // State Management Tests (mark, reset, close)
  // =================================================================

  @Test
  public void markAndReset_allowsReReadingSequence() throws IOException {
    // Arrange
    String input = "abcdef";
    CharSequenceReader reader = new CharSequenceReader(input);
    assertTrue("mark() should be supported", reader.markSupported());

    // Act 1: Read part of the string, mark, and read the rest.
    assertEquals('a', reader.read());
    reader.mark(input.length()); // Mark at position 1
    assertEquals(input.substring(1), readFully(reader));
    assertFullyRead(reader);

    // Act 2: Reset to the mark and read again.
    reader.reset();
    assertEquals("Should read from marked position", input.substring(1), readFully(reader));
    assertFullyRead(reader);

    // Act 3: Reset to the beginning (initial mark) and read again.
    reader.reset(); // First reset goes to mark at position 1
    reader.reset(); // Second reset should go to beginning (position 0)
    assertEquals("Should read from beginning", input, readFully(reader));
  }

  @Test
  public void allMethods_whenReaderIsClosed_throwIOException() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(SHORT_STRING);
    reader.close();

    assertThrows(IOException.class, reader::read);
    assertThrows(IOException.class, () -> reader.read(new char[10]));
    assertThrows(IOException.class, () -> reader.read(new char[10], 0, 10));
    assertThrows(IOException.class, () -> reader.read(CharBuffer.allocate(10)));
    assertThrows(IOException.class, () -> reader.skip(10));
    assertThrows(IOException.class, reader::ready);
    assertThrows(IOException.class, () -> reader.mark(10));
    assertThrows(IOException.class, reader::reset);
  }

  // =================================================================
  // Error Handling Tests
  // =================================================================

  @Test
  public void read_withInvalidBufferRange_throwsIndexOutOfBoundsException() {
    CharSequenceReader reader = new CharSequenceReader("12345");
    char[] buf = new char[10];

    // Negative offset
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, -1, 5));
    // Negative length
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 0, -1));
    // Offset > buffer length
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 11, 0));
    // Offset + length > buffer length
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 5, 6));
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 10, 1));
  }

  @Test
  public void skip_withNegativeArgument_throwsIllegalArgumentException() {
    CharSequenceReader reader = new CharSequenceReader("12345");
    assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
  }

  @Test
  public void mark_withNegativeArgument_throwsIllegalArgumentException() {
    CharSequenceReader reader = new CharSequenceReader("12345");
    assertThrows(IllegalArgumentException.class, () -> reader.mark(-1));
  }

  // =================================================================
  // Helper Methods
  // =================================================================

  /** Asserts that the reader is fully read and at the end of the sequence. */
  private static void assertFullyRead(CharSequenceReader reader) throws IOException {
    assertEquals("Reading past the end should return -1", -1, reader.read());
    assertEquals("Reading into buffer past the end should return -1", -1, reader.read(new char[10]));
    assertEquals(
        "Reading into CharBuffer past the end should return -1",
        -1,
        reader.read(CharBuffer.allocate(10)));
    assertEquals("Skipping past the end should return 0", 0, reader.skip(10));
  }

  /** Reads all remaining characters from the reader and returns them as a string. */
  private static String readFully(CharSequenceReader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    int ch;
    while ((ch = reader.read()) != -1) {
      builder.append((char) ch);
    }
    return builder.toString();
  }
}