/*
 * Copyright (C) 2007 The Guava Authors
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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.io.EOFException;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.List;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit test for {@link CharStreams}.
 *
 * @author Chris Nokleberg
 */
@NullUnmarked
public class CharStreamsTest extends IoTestCase {

  private static final String TEXT = "The quick brown fox jumped over the lazy dog.";
  private static final String MULTILINE_TEXT = "a\nb\nc";
  private static final String SHORT_TEXT = "abcde";
  private static final String LONG_TEXT = Strings.repeat("0123456789", 100);

  // Tests for toString() method -------------------------------------------------

  public void testToString_returnsCompleteContent() throws IOException {
    String result = CharStreams.toString(new StringReader(TEXT));
    assertEquals(TEXT, result);
  }

  // Tests for readLines() method ------------------------------------------------

  public void testReadLines_returnsListOfLines() throws IOException {
    List<String> lines = CharStreams.readLines(new StringReader(MULTILINE_TEXT));
    assertEquals(ImmutableList.of("a", "b", "c"), lines);
  }

  public void testReadLines_withLineProcessor_stopsAfterFirstLineWhenProcessorReturnsFalse() 
      throws IOException {
    // Processor that stops after first line
    LineProcessor<Integer> stopAfterFirstLine = new LineProcessor<Integer>() {
      int seen;
      
      @Override
      public boolean processLine(String line) {
        seen++;
        return false;
      }

      @Override
      public Integer getResult() {
        return seen;
      }
    };

    int processedLines = CharStreams.readLines(new StringReader(MULTILINE_TEXT), stopAfterFirstLine);
    assertEquals(1, processedLines);
  }

  public void testReadLines_withLineProcessor_processesAllLinesWhenProcessorReturnsTrue() 
      throws IOException {
    // Processor that processes all lines
    LineProcessor<Integer> processAllLines = new LineProcessor<Integer>() {
      int seen;

      @Override
      public boolean processLine(String line) {
        seen++;
        return true;
      }

      @Override
      public Integer getResult() {
        return seen;
      }
    };

    int processedLines = CharStreams.readLines(new StringReader(MULTILINE_TEXT), processAllLines);
    assertEquals(3, processedLines);
  }

  public void testReadLines_withLineProcessor_processesConditionally() throws IOException {
    StringBuilder collectedContent = new StringBuilder();
    LineProcessor<Integer> conditionalProcessor = new LineProcessor<Integer>() {
      int seen;

      @Override
      public boolean processLine(String line) {
        seen++;
        collectedContent.append(line);
        return seen < 2; // Process only first two lines
      }

      @Override
      public Integer getResult() {
        return seen;
      }
    };

    int processedLines = CharStreams.readLines(
        new StringReader(MULTILINE_TEXT), conditionalProcessor);
    assertEquals(2, processedLines);
    assertEquals("ab", collectedContent.toString());
  }

  // Tests for skipFully() method -----------------------------------------------

  public void testSkipFully_throwsEofExceptionWhenSkippingBeyondEnd() throws IOException {
    Reader reader = new StringReader(SHORT_TEXT);
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 6));
  }

  public void testSkipFully_skipsCorrectNumberOfCharacters() throws IOException {
    Reader reader = new StringReader("abcdef");
    assertEquals('a', reader.read());
    
    CharStreams.skipFully(reader, 1);
    assertEquals('c', reader.read());
    
    CharStreams.skipFully(reader, 2);
    assertEquals('f', reader.read());
    
    assertEquals(-1, reader.read());
  }

  // Tests for asWriter() method ------------------------------------------------

  public void testAsWriter_wrapsNonWriterAppendable() {
    Appendable plainAppendable = new StringBuilder();
    Writer result = CharStreams.asWriter(plainAppendable);
    
    assertNotSame(plainAppendable, result);
    assertNotNull(result);
  }

  public void testAsWriter_returnsSameInstanceForWriter() {
    Appendable secretlyAWriter = new StringWriter();
    Writer result = CharStreams.asWriter(secretlyAWriter);
    
    assertSame(secretlyAWriter, result);
  }

  // Tests for copy() method ----------------------------------------------------
  // These test different combinations of source/destination types

  public void testCopy_genericReadableToGenericAppendable() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(
        wrapAsGenericReadable(new StringReader(ASCII)), 
        wrapAsGenericAppendable(builder)
    );
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);
  }

  public void testCopy_readerToStringBuilder() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(new StringReader(ASCII), builder);
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);
  }

  public void testCopy_genericReadableToStringBuilder() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), builder);
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);
  }

  public void testCopy_readerToWriter() throws IOException {
    StringWriter writer = new StringWriter();
    long copied = CharStreams.copy(new StringReader(ASCII), writer);
    assertEquals(ASCII, writer.toString());
    assertEquals(ASCII.length(), copied);
  }

  public void testCopy_genericReadableToWriter() throws IOException {
    StringWriter writer = new StringWriter();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), writer);
    assertEquals(ASCII, writer.toString());
    assertEquals(ASCII.length(), copied);
  }

  public void testCopy_handlesNonBufferFillingReaders() throws IOException {
    // Tests fix for Guava issue 1061: https://github.com/google/guava/issues/1061
    StringBuilder builder = new StringBuilder();
    Reader reader = newNonBufferFillingReader(new StringReader(LONG_TEXT));
    
    long copied = CharStreams.copy(reader, builder);
    assertEquals(LONG_TEXT, builder.toString());
    assertEquals(LONG_TEXT.length(), copied);
  }

  // Tests for exhaust() method -------------------------------------------------

  public void testExhaust_reader() throws IOException {
    Reader reader = new StringReader(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(reader));
    assertEquals(-1, reader.read());
    assertEquals(0, CharStreams.exhaust(reader));

    Reader empty = new StringReader("");
    assertEquals(0, CharStreams.exhaust(empty));
  }

  public void testExhaust_readable() throws IOException {
    CharBuffer buf = CharBuffer.wrap(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(buf));
    assertEquals(0, buf.remaining());
    assertEquals(0, CharStreams.exhaust(buf));

    CharBuffer empty = CharBuffer.wrap("");
    assertEquals(0, CharStreams.exhaust(empty));
  }

  // Tests for nullWriter() method ----------------------------------------------

  public void testNullWriter_ignoresAllWrites() throws IOException {
    Writer nullWriter = CharStreams.nullWriter();
    
    // Verify various write operations are ignored
    nullWriter.write('n');
    nullWriter.write("Test string");
    nullWriter.write("Test string", 2, 5);
    nullWriter.append("null");
    nullWriter.append("null", 1, 3);
    nullWriter.append('c');
    
    // Verify singleton behavior
    assertSame(CharStreams.nullWriter(), CharStreams.nullWriter());
  }

  public void testNullWriter_appendWithInvalidIndicesThrows() {
    Writer nullWriter = CharStreams.nullWriter();
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, 0, 5));
  }

  // Helper methods ------------------------------------------------------------

  /**
   * Creates a Reader that intentionally reads fewer characters than requested to test
   * buffer management logic. This simulates readers that don't fill their buffers completely.
   */
  private static Reader newNonBufferFillingReader(Reader reader) {
    return new FilterReader(reader) {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        if (len <= 0) {
          fail("Invalid read length: " + len);
        }
        // Always read at least 1 character less than requested
        return in.read(cbuf, off, Math.max(len - 1024, 0));
      }
    };
  }

  /**
   * Wraps an Appendable to defeat type-specific optimizations in the code being tested.
   * Ensures we're testing the generic path.
   */
  private static Appendable wrapAsGenericAppendable(Appendable a) {
    return new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        return a.append(csq);
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return a.append(csq, start, end);
      }

      @Override
      public Appendable append(char c) throws IOException {
        return a.append(c);
      }
    };
  }

  /**
   * Wraps a Readable to defeat type-specific optimizations in the code being tested.
   * Ensures we're testing the generic path.
   */
  private static Readable wrapAsGenericReadable(Readable r) {
    return new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        return r.read(cb);
      }
    };
  }
}