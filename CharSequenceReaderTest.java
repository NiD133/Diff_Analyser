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
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * @author Colin Decker
 */
@NullUnmarked
@RunWith(Parameterized.class)
public class CharSequenceReaderTest {

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
      { "" },
      { "abc" },
      { "abcde" },
      { "abcdefghijkl" },
      { 
          "abcdefghijklmnopqrstuvwxyz\n" +
          "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r" +
          "0123456789\r\n" +
          "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| " 
      }
    });
  }

  @Parameter
  public String testString;

  // ======================== Core Reading Tests =========================

  @Test
  public void testReadCharByChar() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader(testString)) {
      for (int i = 0; i < testString.length(); i++) {
        assertEquals(
            "Character mismatch at index " + i, 
            testString.charAt(i), 
            (char) reader.read()
        );
      }
      assertFullyRead(reader);
    }
  }

  @Test
  public void testReadToCharArray() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader(testString)) {
      char[] buf = new char[testString.length()];
      int readCount = reader.read(buf);
      
      if (testString.isEmpty()) {
        assertEquals("Should return -1 for empty input", -1, readCount);
      } else {
        assertEquals("Didn't read full length", testString.length(), readCount);
        assertEquals("Content mismatch", testString, new String(buf));
      }
      assertFullyRead(reader);
    }
  }

  @Test
  public void testReadInChunksToCharArray() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader(testString)) {
      char[] buf = new char[5];
      StringBuilder builder = new StringBuilder();
      int read;
      while ((read = reader.read(buf, 0, buf.length)) != -1) {
        builder.append(buf, 0, read);
      }
      assertEquals("Content mismatch", testString, builder.toString());
      assertFullyRead(reader);
    }
  }

  @Test
  public void testReadToCharBuffer() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader(testString)) {
      CharBuffer buf = CharBuffer.allocate(testString.length());
      int readCount = reader.read(buf);
      buf.flip(); // Prepare buffer for reading
      
      if (testString.isEmpty()) {
        assertEquals("Should return -1 for empty input", -1, readCount);
      } else {
        assertEquals("Didn't read full length", testString.length(), readCount);
        assertEquals("Content mismatch", testString, buf.toString());
      }
      assertFullyRead(reader);
    }
  }

  @Test
  public void testReadInChunksToCharBuffer() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader(testString)) {
      CharBuffer buf = CharBuffer.allocate(5);
      StringBuilder builder = new StringBuilder();
      int readCount;
      while ((readCount = reader.read(buf)) != -1) {
        buf.flip(); // Prepare buffer for reading
        builder.append(buf);
        buf.clear(); // Reset buffer for next read
      }
      assertEquals("Content mismatch", testString, builder.toString());
      assertFullyRead(reader);
    }
  }

  @Test
  public void testSkipFully() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader(testString)) {
      long skipped = reader.skip(Long.MAX_VALUE);
      assertEquals("Skipped length mismatch", testString.length(), skipped);
      assertFullyRead(reader);
    }
  }

  @Test
  public void testSkipPartialThenRead() throws IOException {
    if (testString.length() > 5) {
      try (CharSequenceReader reader = new CharSequenceReader(testString)) {
        long skipped = reader.skip(5);
        assertEquals("Should skip 5 characters", 5, skipped);

        char[] buf = new char[testString.length() - 5];
        int readCount = reader.read(buf, 0, buf.length);
        assertEquals("Should read remaining characters", buf.length, readCount);
        assertEquals("Content mismatch", testString.substring(5), new String(buf));
        assertFullyRead(reader);
      }
    }
  }

  // ======================== Mark/Reset Tests =========================

  @Test
  public void markSupported_returnsTrue() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader("")) {
      assertTrue("markSupported should return true", reader.markSupported());
    }
  }

  @Test
  public void markAndReset_allowsRepeatedFullReads() throws IOException {
    String input = "abcdefghijklmnopqrstuvwxyz";
    try (CharSequenceReader reader = new CharSequenceReader(input)) {
      // First read
      assertEquals(input, readFully(reader));
      assertFullyRead(reader);
      
      // Reset and re-read
      reader.reset();
      assertEquals(input, readFully(reader));
      assertFullyRead(reader);
    }
  }

  @Test
  public void markAndReset_worksAfterSkip() throws IOException {
    String input = "abcdefghijklmnopqrstuvwxyz";
    try (CharSequenceReader reader = new CharSequenceReader(input)) {
      // Skip and mark
      assertEquals(5, reader.skip(5));
      reader.mark(Integer.MAX_VALUE);
      
      // Read and verify
      assertEquals(input.substring(5), readFully(reader));
      assertFullyRead(reader);
      
      // Reset to mark and re-read
      reader.reset();
      assertEquals(input.substring(5), readFully(reader));
      assertFullyRead(reader);
    }
  }

  // ======================== Argument Validation Tests =========================

  @Test
  public void read_throwsIndexOutOfBoundsForInvalidArguments() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader("12345")) {
      char[] buf = new char[10];
      
      assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 0, 11));
      assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 10, 1));
      assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 11, 0));
      assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, -1, 5));
      assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 5, -1));
    }
  }

  @Test
  public void skip_throwsIllegalArgumentForNegative() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader("12345")) {
      assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
    }
  }

  @Test
  public void mark_throwsIllegalArgumentForNegative() throws IOException {
    try (CharSequenceReader reader = new CharSequenceReader("12345")) {
      assertThrows(IllegalArgumentException.class, () -> reader.mark(-1));
    }
  }

  // ======================== Closed Reader Tests =========================

  @Test
  public void methods_throwAfterClose() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("");
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

  // ======================== Helper Methods =========================

  /**
   * Asserts that the reader is fully read (next read should return -1).
   */
  private static void assertFullyRead(CharSequenceReader reader) throws IOException {
    assertEquals("Should return -1 when fully read", -1, reader.read());
    char[] buf = new char[10];
    assertEquals("Should return -1 when reading to array", -1, reader.read(buf, 0, 10));
    CharBuffer charBuffer = CharBuffer.allocate(10);
    assertEquals("Should return -1 when reading to CharBuffer", -1, reader.read(charBuffer));
    assertEquals("Should skip 0 when fully read", 0, reader.skip(10));
  }

  /**
   * Reads all characters from the reader until EOF.
   */
  private static String readFully(CharSequenceReader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    int ch;
    while ((ch = reader.read()) != -1) {
      builder.append((char) ch);
    }
    return builder.toString();
  }
}