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
 * Tests for CharStreams.
 *
 * The test cases are grouped by the CharStreams API they exercise. Small helper methods at the
 * bottom make intent explicit and avoid distracting boilerplate in the tests themselves.
 */
@NullUnmarked
public class CharStreamsTest extends IoTestCase {

  private static final String TEXT = "The quick brown fox jumped over the lazy dog.";

  // ----------------------------------------------------------------------
  // toString / readLines
  // ----------------------------------------------------------------------

  public void testToString_readsAllCharacters() throws IOException {
    assertEquals(TEXT, CharStreams.toString(new StringReader(TEXT)));
  }

  public void testReadLines_returnsAllLinesWithoutTerminators() throws IOException {
    List<String> lines = CharStreams.readLines(new StringReader("a\nb\nc"));
    assertEquals(ImmutableList.of("a", "b", "c"), lines);
  }

  public void testReadLines_withLineProcessor_stopsImmediately() throws IOException {
    String text = "a\nb\nc";

    // stop after the first line (processor returns false on first call)
    LineProcessor<Integer> processor = countingLineProcessor(/*stopAfter=*/ 1, /*sink=*/ null);

    Integer processed = CharStreams.readLines(new StringReader(text), processor);
    assertEquals("processLine should be called exactly once", 1, processed.intValue());
  }

  public void testReadLines_withLineProcessor_processesAllLines() throws IOException {
    String text = "a\nb\nc";

    // never stop (effectively "always true")
    LineProcessor<Integer> processor = countingLineProcessor(Integer.MAX_VALUE, /*sink=*/ null);

    Integer processed = CharStreams.readLines(new StringReader(text), processor);
    assertEquals("All lines should be processed", 3, processed.intValue());
  }

  public void testReadLines_withLineProcessor_stopsConditionally_andAccumulates() throws IOException {
    String text = "a\nb\nc";
    StringBuilder seen = new StringBuilder();

    // stop after reading 2 lines; append each seen line to 'seen'
    LineProcessor<Integer> processor = countingLineProcessor(/*stopAfter=*/ 2, seen);

    Integer processed = CharStreams.readLines(new StringReader(text), processor);
    assertEquals(2, processed.intValue());
    assertEquals("ab", seen.toString());
  }

  // ----------------------------------------------------------------------
  // skipFully
  // ----------------------------------------------------------------------

  public void testSkipFully_throwsOnEof() throws IOException {
    Reader reader = new StringReader("abcde");
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 6));
  }

  public void testSkipFully_skipsExactlyAndLeavesReaderPositioned() throws IOException {
    String s = "abcdef";
    Reader reader = new StringReader(s);

    assertEquals(s.charAt(0), reader.read()); // consume 'a'
    CharStreams.skipFully(reader, 1); // skip 'b'
    assertEquals(s.charAt(2), reader.read()); // at 'c'
    CharStreams.skipFully(reader, 2); // skip 'd','e'
    assertEquals(s.charAt(5), reader.read()); // now at 'f'

    assertEquals(-1, reader.read()); // EOF
  }

  // ----------------------------------------------------------------------
  // asWriter
  // ----------------------------------------------------------------------

  public void testAsWriter_wrapsAppendable_andReturnsWriterUnchanged() {
    // Wraps a plain Appendable into a Writer
    Appendable plainAppendable = new StringBuilder();
    Writer wrapped = CharStreams.asWriter(plainAppendable);
    assertNotSame(plainAppendable, wrapped);
    assertNotNull(wrapped);

    // If target already is a Writer, it should be returned as-is
    Appendable alreadyWriter = new StringWriter();
    Writer same = CharStreams.asWriter(alreadyWriter);
    assertSame(alreadyWriter, same);
  }

  // ----------------------------------------------------------------------
  // copy(Readable, Appendable) and related optimized paths
  // ----------------------------------------------------------------------

  // Note: We call CharStreams.copy in each variant to exercise both the generic path and the
  // optimized Reader->StringBuilder and Reader->Writer paths that the implementation may select.

  public void testCopy_generic_fromReader_toAppendable() throws IOException {
    StringBuilder outAscii = new StringBuilder();
    long copiedAscii =
        CharStreams.copy(
            wrapAsGenericReadable(new StringReader(ASCII)), wrapAsGenericAppendable(outAscii));
    assertEquals(ASCII, outAscii.toString());
    assertEquals(ASCII.length(), copiedAscii);

    StringBuilder outI18n = new StringBuilder();
    long copiedI18n =
        CharStreams.copy(
            wrapAsGenericReadable(new StringReader(I18N)), wrapAsGenericAppendable(outI18n));
    assertEquals(I18N, outI18n.toString());
    assertEquals(I18N.length(), copiedI18n);
  }

  public void testCopy_fromReader_toStringBuilder() throws IOException {
    StringBuilder outAscii = new StringBuilder();
    long copiedAscii = CharStreams.copy(new StringReader(ASCII), outAscii);
    assertEquals(ASCII, outAscii.toString());
    assertEquals(ASCII.length(), copiedAscii);

    StringBuilder outI18n = new StringBuilder();
    long copiedI18n = CharStreams.copy(new StringReader(I18N), outI18n);
    assertEquals(I18N, outI18n.toString());
    assertEquals(I18N.length(), copiedI18n);
  }

  public void testCopy_fromReadable_toStringBuilder() throws IOException {
    StringBuilder outAscii = new StringBuilder();
    long copiedAscii = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), outAscii);
    assertEquals(ASCII, outAscii.toString());
    assertEquals(ASCII.length(), copiedAscii);

    StringBuilder outI18n = new StringBuilder();
    long copiedI18n = CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), outI18n);
    assertEquals(I18N, outI18n.toString());
    assertEquals(I18N.length(), copiedI18n);
  }

  public void testCopy_fromReader_toWriter() throws IOException {
    StringWriter outAscii = new StringWriter();
    long copiedAscii = CharStreams.copy(new StringReader(ASCII), outAscii);
    assertEquals(ASCII, outAscii.toString());
    assertEquals(ASCII.length(), copiedAscii);

    StringWriter outI18n = new StringWriter();
    long copiedI18n = CharStreams.copy(new StringReader(I18N), outI18n);
    assertEquals(I18N, outI18n.toString());
    assertEquals(I18N.length(), copiedI18n);
  }

  public void testCopy_fromReadable_toWriter() throws IOException {
    StringWriter outAscii = new StringWriter();
    long copiedAscii =
        CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), outAscii);
    assertEquals(ASCII, outAscii.toString());
    assertEquals(ASCII.length(), copiedAscii);

    StringWriter outI18n = new StringWriter();
    long copiedI18n =
        CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), outI18n);
    assertEquals(I18N, outI18n.toString());
    assertEquals(I18N.length(), copiedI18n);
  }

  /**
   * Regression test for https://github.com/google/guava/issues/1061.
   *
   * Ensures the internal CharBuffer is cleared between reads so that a Reader that doesn't
   * fill the buffer doesn't cause the effective buffer size to shrink to zero (and loop forever).
   */
  @SuppressWarnings("InlineMeInliner") // String.repeat not available on Java 8
  public void testCopy_withReaderThatDoesNotFillBuffer_doesNotShrinkBufferOrLoop() throws IOException {
    String source = Strings.repeat("0123456789", 100); // long enough to expose shrinking issues
    StringBuilder out = new StringBuilder();

    long copied = CharStreams.copy(newNonBufferFillingReader(new StringReader(source)), out);

    assertEquals(source, out.toString());
    assertEquals(source.length(), copied);
  }

  // ----------------------------------------------------------------------
  // exhaust
  // ----------------------------------------------------------------------

  public void testExhaust_withReader_consumesAllRemaining() throws IOException {
    Reader reader = new StringReader(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(reader));
    assertEquals(-1, reader.read());
    assertEquals(0, CharStreams.exhaust(reader)); // already at EOF

    Reader empty = new StringReader("");
    assertEquals(0, CharStreams.exhaust(empty));
    assertEquals(-1, empty.read());
  }

  public void testExhaust_withReadable_consumesAllRemaining() throws IOException {
    CharBuffer buf = CharBuffer.wrap(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(buf));
    assertEquals(0, buf.remaining());
    assertEquals(0, CharStreams.exhaust(buf));

    CharBuffer empty = CharBuffer.wrap("");
    assertEquals(0, CharStreams.exhaust(empty));
    assertEquals(0, empty.remaining());
  }

  // ----------------------------------------------------------------------
  // nullWriter
  // ----------------------------------------------------------------------

  public void testNullWriter_noopBehaviorAndSingleton() throws Exception {
    Writer nullWriter = CharStreams.nullWriter();

    // All operations are no-ops
    nullWriter.write('n');
    String test = "Test string for NullWriter";
    nullWriter.write(test);
    nullWriter.write(test, 2, 10);
    nullWriter.append(null);
    nullWriter.append(null, 0, 4);

    // Index validation should still be enforced
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, 0, 5));

    // The returned writer is a singleton
    assertSame(CharStreams.nullWriter(), CharStreams.nullWriter());
  }

  // ----------------------------------------------------------------------
  // Helpers
  // ----------------------------------------------------------------------

  /**
   * Returns a processor that counts lines and optionally appends each processed line to 'sink'.
   * It stops after 'stopAfter' lines (i.e., returns false on that call).
   */
  private static LineProcessor<Integer> countingLineProcessor(int stopAfter, StringBuilder sink) {
    return new LineProcessor<Integer>() {
      int seen;

      @Override
      public boolean processLine(String line) {
        seen++;
        if (sink != null) {
          sink.append(line);
        }
        return seen < stopAfter;
      }

      @Override
      public Integer getResult() {
        return seen;
      }
    };
  }

  /**
   * Wraps a Reader so that read(char[], int, int) deliberately asks the delegate to read fewer
   * characters than requested, ensuring CharStreams correctly clears its buffer between reads.
   */
  private static Reader newNonBufferFillingReader(Reader reader) {
    return new FilterReader(reader) {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        // If CharStreams ever calls us with len <= 0, it's a bug (infinite-loop risk).
        if (len <= 0) {
          fail("read called with a len of " + len);
        }
        // Ask the underlying reader to read fewer chars than available.
        return in.read(cbuf, off, Math.max(len - 1024, 0));
      }
    };
  }

  /** Wrap an Appendable in a generic Appendable to defeat any type-specific optimizations. */
  private static Appendable wrapAsGenericAppendable(Appendable target) {
    return new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        target.append(csq);
        return this;
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        target.append(csq, start, end);
        return this;
      }

      @Override
      public Appendable append(char c) throws IOException {
        target.append(c);
        return this;
      }
    };
  }

  /** Wrap a Readable in a generic Readable to defeat any type-specific optimizations. */
  private static Readable wrapAsGenericReadable(Readable target) {
    return new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        return target.read(cb);
      }
    };
  }
}