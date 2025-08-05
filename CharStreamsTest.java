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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for {@link CharStreams}.
 */
@NullUnmarked
@RunWith(JUnit4.class)
public class CharStreamsTest {

  private static final String ASCII = "abcdefghijklmnopqrstuvwxyz";
  private static final String I18N = "абвгдежзийклмнопрстуфхцчшщъыьэюя";
  private static final String TEXT = "The quick brown fox jumped over the lazy dog.";

  @Test
  public void toString_returnsEntireReaderContent() throws IOException {
    Reader reader = new StringReader(TEXT);
    assertEquals(TEXT, CharStreams.toString(reader));
  }

  @Test
  public void readLines_withNewlines_returnsListOfLines() throws IOException {
    Reader reader = new StringReader("a\nb\nc");
    List<String> lines = CharStreams.readLines(reader);
    assertEquals(ImmutableList.of("a", "b", "c"), lines);
  }

  @Test
  public void readLines_withLineProcessorThatStopsEarly_stopsAfterFirstLine() throws IOException {
    Reader reader = new StringReader("a\nb\nc");
    LineProcessor<Integer> lineProcessor =
        new LineProcessor<Integer>() {
          int linesSeen = 0;

          @Override
          public boolean processLine(String line) {
            linesSeen++;
            return false; // Stop after the first line
          }

          @Override
          public Integer getResult() {
            return linesSeen;
          }
        };

    Integer linesProcessed = CharStreams.readLines(reader, lineProcessor);
    assertEquals(1, linesProcessed.intValue());
  }

  @Test
  public void readLines_withLineProcessorThatContinues_processesAllLines() throws IOException {
    Reader reader = new StringReader("a\nb\nc");
    LineProcessor<Integer> lineProcessor =
        new LineProcessor<Integer>() {
          int linesSeen = 0;

          @Override
          public boolean processLine(String line) {
            linesSeen++;
            return true; // Continue processing
          }

          @Override
          public Integer getResult() {
            return linesSeen;
          }
        };

    Integer linesProcessed = CharStreams.readLines(reader, lineProcessor);
    assertEquals(3, linesProcessed.intValue());
  }

  @Test
  public void readLines_withConditionalLineProcessor_stopsAtCorrectLine() throws IOException {
    Reader reader = new StringReader("a\nb\nc");
    StringBuilder linesCombined = new StringBuilder();
    LineProcessor<Integer> lineProcessor =
        new LineProcessor<Integer>() {
          int linesSeen = 0;

          @Override
          public boolean processLine(String line) {
            linesSeen++;
            linesCombined.append(line);
            return linesSeen < 2; // Stop after the second line
          }

          @Override
          public Integer getResult() {
            return linesSeen;
          }
        };

    Integer linesProcessed = CharStreams.readLines(reader, lineProcessor);

    assertEquals(2, linesProcessed.intValue());
    assertEquals("ab", linesCombined.toString());
  }

  @Test
  public void skipFully_whenSkippingPastEndOfReader_throwsEofException() {
    Reader reader = new StringReader("abcde");
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 6));
  }

  @Test
  public void skipFully_withSufficientChars_skipsCorrectAmount() throws IOException {
    Reader reader = new StringReader("abcdef");

    // State: "abcdef", position 0
    assertEquals('a', reader.read()); // Reads 'a', state: "bcdef", position 1

    // Skip 1 char ('b'), state: "cdef", position 2
    CharStreams.skipFully(reader, 1);
    assertEquals('c', reader.read()); // Reads 'c', state: "def", position 3

    // Skip 2 chars ('d', 'e'), state: "f", position 5
    CharStreams.skipFully(reader, 2);
    assertEquals('f', reader.read()); // Reads 'f', state: "", position 6

    // At end of stream
    assertEquals(-1, reader.read());
  }

  @Test
  public void asWriter_withNonWriterAppendable_returnsNewWriterInstance() {
    Appendable appendable = new StringBuilder();
    Writer writer = CharStreams.asWriter(appendable);
    assertNotNull(writer);
    assertNotSame(appendable, writer);
  }

  @Test
  public void asWriter_withWriterInstance_returnsSameInstance() {
    Appendable writer = new StringWriter();
    Writer result = CharStreams.asWriter(writer);
    assertSame(writer, result);
  }

  // The following tests verify CharStreams.copy for various combinations of
  // generic and specialized Readable/Appendable types.

  @Test
  public void copy_fromGenericReadableToGenericAppendable_copiesAllContent() throws IOException {
    // Test with ASCII
    Readable reader1 = wrapAsGenericReadable(new StringReader(ASCII));
    Appendable writer1 = wrapAsGenericAppendable(new StringBuilder());
    long copied1 = CharStreams.copy(reader1, writer1);
    assertEquals(ASCII, writer1.toString());
    assertEquals(ASCII.length(), copied1);

    // Test with I18N
    Readable reader2 = wrapAsGenericReadable(new StringReader(I18N));
    Appendable writer2 = wrapAsGenericAppendable(new StringBuilder());
    long copied2 = CharStreams.copy(reader2, writer2);
    assertEquals(I18N, writer2.toString());
    assertEquals(I18N.length(), copied2);
  }

  @Test
  public void copy_fromReaderToWriter_copiesAllContent() throws IOException {
    // Test with ASCII
    StringWriter writer1 = new StringWriter();
    long copied1 = CharStreams.copy(new StringReader(ASCII), writer1);
    assertEquals(ASCII, writer1.toString());
    assertEquals(ASCII.length(), copied1);

    // Test with I18N
    StringWriter writer2 = new StringWriter();
    long copied2 = CharStreams.copy(new StringReader(I18N), writer2);
    assertEquals(I18N, writer2.toString());
    assertEquals(I18N.length(), copied2);
  }

  @Test
  public void copy_fromReaderToStringBuilder_copiesAllContent() throws IOException {
    // Test with ASCII
    StringBuilder builder1 = new StringBuilder();
    long copied1 = CharStreams.copy(new StringReader(ASCII), builder1);
    assertEquals(ASCII, builder1.toString());
    assertEquals(ASCII.length(), copied1);

    // Test with I18N
    StringBuilder builder2 = new StringBuilder();
    long copied2 = CharStreams.copy(new StringReader(I18N), builder2);
    assertEquals(I18N, builder2.toString());
    assertEquals(I18N.length(), copied2);
  }

  /**
   * Test for Guava issue 1061: https://github.com/google/guava/issues/1061
   *
   * <p>CharStreams.copy was failing to clear its CharBuffer after each read call. This test ensures
   * that even when a Reader doesn't fill the buffer on each read, the copy operation still
   * succeeds without entering an infinite loop.
   */
  @Test
  @SuppressWarnings("InlineMeInliner") // String.repeat unavailable under Java 8
  public void copy_withReaderThatDoesNotCompletelyFillBuffer_succeeds() throws IOException {
    // A long string is needed to expose the bug where the buffer size would shrink to zero.
    String longString = Strings.repeat("0123456789", 100);
    StringBuilder builder = new StringBuilder();
    Reader nonFillingReader = newNonBufferFillingReader(new StringReader(longString));

    // The main assertion is that this copy operation completes successfully.
    long copied = CharStreams.copy(nonFillingReader, builder);

    assertEquals(longString, builder.toString());
    assertEquals(longString.length(), copied);
  }

  @Test
  public void exhaust_withReader_consumesAllCharsAndReturnsLength() throws IOException {
    Reader reader = new StringReader(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(reader));
    assertEquals(-1, reader.read());

    // A second call on an exhausted reader should return 0
    assertEquals(0, CharStreams.exhaust(reader));
  }

  @Test
  public void exhaust_withReadable_consumesAllCharsAndReturnsLength() throws IOException {
    CharBuffer buffer = CharBuffer.wrap(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(buffer));
    assertEquals(0, buffer.remaining());

    // A second call on an exhausted readable should return 0
    assertEquals(0, CharStreams.exhaust(buffer));
  }

  @Test
  public void nullWriter_writeMethods_doNotThrow() throws IOException {
    Writer nullWriter = CharStreams.nullWriter();
    // These operations should be silently ignored without throwing an exception.
    nullWriter.write('n');
    nullWriter.write(TEXT);
    nullWriter.write(TEXT, 2, 10);
    nullWriter.append("a sequence");
    nullWriter.append("a sequence", 1, 4);
    nullWriter.flush();
    nullWriter.close();
  }

  @Test
  public void nullWriter_appendWithInvalidBounds_throwsIndexOutOfBounds() {
    Writer nullWriter = CharStreams.nullWriter();
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append("text", -1, 2));
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append("text", 2, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append("text", 0, 5));
  }

  @Test
  public void nullWriter_returnsSingletonInstance() {
    assertSame(CharStreams.nullWriter(), CharStreams.nullWriter());
  }

  /**
   * Returns a reader that intentionally does not fill the provided buffer completely, to test
   * buffer handling logic in copy operations.
   */
  private static Reader newNonBufferFillingReader(Reader reader) {
    return new FilterReader(reader) {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        if (len <= 0) {
          fail("read called with a non-positive length: " + len);
        }
        // Read fewer than the max number of chars to test buffer clearing.
        int readLen = Math.max(1, len / 2);
        return in.read(cbuf, off, readLen);
      }
    };
  }

  /** Wraps an Appendable to hide its specific type, forcing use of the generic code path. */
  private static Appendable wrapAsGenericAppendable(Appendable appendable) {
    return new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        return appendable.append(csq);
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return appendable.append(csq, start, end);
      }

      @Override
      public Appendable append(char c) throws IOException {
        return appendable.append(c);
      }

      @Override
      public String toString() {
        return appendable.toString();
      }
    };
  }

  /** Wraps a Readable to hide its specific type, forcing use of the generic code path. */
  private static Readable wrapAsGenericReadable(Readable readable) {
    return cb -> readable.read(cb);
  }
}