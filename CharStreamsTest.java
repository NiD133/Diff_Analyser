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

  private static final String SAMPLE_TEXT = "The quick brown fox jumped over the lazy dog.";
  private static final String MULTILINE_TEXT = "a\nb\nc";
  private static final String TEST_STRING_FOR_SKIPPING = "abcdef";

  // ========== toString() Tests ==========

  public void testToString_convertsReaderContentToString() throws IOException {
    String result = CharStreams.toString(new StringReader(SAMPLE_TEXT));
    
    assertEquals(SAMPLE_TEXT, result);
  }

  // ========== readLines() Tests ==========

  public void testReadLines_returnsAllLinesAsList() throws IOException {
    List<String> lines = CharStreams.readLines(new StringReader(MULTILINE_TEXT));
    
    assertEquals(ImmutableList.of("a", "b", "c"), lines);
  }

  public void testReadLines_withLineProcessorThatStopsEarly() throws IOException {
    Reader reader = new StringReader(MULTILINE_TEXT);
    LineProcessor<Integer> stopAfterFirstLine = createLineCounterProcessor(false);

    int processedLines = CharStreams.readLines(reader, stopAfterFirstLine);

    assertEquals("processLine was called more than once", 1, processedLines);
  }

  public void testReadLines_withLineProcessorThatProcessesAllLines() throws IOException {
    Reader reader = new StringReader(MULTILINE_TEXT);
    LineProcessor<Integer> processAllLines = createLineCounterProcessor(true);

    int processedLines = CharStreams.readLines(reader, processAllLines);

    assertEquals("processLine was not called for all the lines", 3, processedLines);
  }

  public void testReadLines_withConditionalLineProcessor() throws IOException {
    Reader reader = new StringReader(MULTILINE_TEXT);
    StringBuilder capturedContent = new StringBuilder();
    LineProcessor<Integer> conditionalProcessor = createConditionalLineProcessor(capturedContent);

    int processedLines = CharStreams.readLines(reader, conditionalProcessor);

    assertEquals("Should process exactly 2 lines", 2, processedLines);
    assertEquals("Should capture first two lines", "ab", capturedContent.toString());
  }

  // ========== skipFully() Tests ==========

  public void testSkipFully_throwsEOFExceptionWhenNotEnoughCharacters() throws IOException {
    Reader reader = new StringReader("abcde");
    
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 6));
  }

  public void testSkipFully_skipsExactNumberOfCharacters() throws IOException {
    Reader reader = new StringReader(TEST_STRING_FOR_SKIPPING);

    // Read first character: 'a'
    assertEquals('a', reader.read());
    
    // Skip 1 character: 'b'
    CharStreams.skipFully(reader, 1);
    assertEquals('c', reader.read());
    
    // Skip 2 characters: 'd', 'e'
    CharStreams.skipFully(reader, 2);
    assertEquals('f', reader.read());
    
    // Should be at end of stream
    assertEquals(-1, reader.read());
  }

  // ========== asWriter() Tests ==========

  public void testAsWriter_wrapsPlainAppendableInNewWriter() {
    Appendable plainAppendable = new StringBuilder();
    
    Writer result = CharStreams.asWriter(plainAppendable);
    
    assertNotSame("Should wrap Appendable in a new object", plainAppendable, result);
    assertNotNull(result);
  }

  public void testAsWriter_returnsWriterUnwrapped() {
    Writer existingWriter = new StringWriter();
    
    Writer result = CharStreams.asWriter(existingWriter);
    
    assertSame("A Writer should not be wrapped", existingWriter, result);
  }

  // ========== copy() Tests ==========
  // Note: CharStreams.copy has type-specific optimizations for Readers, StringBuilders and Writers

  public void testCopy_betweenGenericReadableAndAppendable() throws IOException {
    StringBuilder destination = new StringBuilder();
    Readable source = wrapAsGenericReadable(new StringReader(ASCII));
    Appendable target = wrapAsGenericAppendable(destination);
    
    long copiedChars = CharStreams.copy(source, target);
    
    assertEquals("Should copy ASCII content", ASCII, destination.toString());
    assertEquals("Should return number of copied characters", ASCII.length(), copiedChars);

    // Test with internationalized text
    StringBuilder i18nDestination = new StringBuilder();
    Readable i18nSource = wrapAsGenericReadable(new StringReader(I18N));
    Appendable i18nTarget = wrapAsGenericAppendable(i18nDestination);
    
    long i18nCopiedChars = CharStreams.copy(i18nSource, i18nTarget);
    
    assertEquals("Should copy I18N content", I18N, i18nDestination.toString());
    assertEquals("Should return number of copied characters", I18N.length(), i18nCopiedChars);
  }

  public void testCopy_fromReaderToStringBuilder() throws IOException {
    StringBuilder destination = new StringBuilder();
    
    long copiedChars = CharStreams.copy(new StringReader(ASCII), destination);
    
    assertEquals("Should copy ASCII content", ASCII, destination.toString());
    assertEquals("Should return number of copied characters", ASCII.length(), copiedChars);

    // Test with internationalized text
    StringBuilder i18nDestination = new StringBuilder();
    long i18nCopiedChars = CharStreams.copy(new StringReader(I18N), i18nDestination);
    
    assertEquals("Should copy I18N content", I18N, i18nDestination.toString());
    assertEquals("Should return number of copied characters", I18N.length(), i18nCopiedChars);
  }

  public void testCopy_fromReadableToStringBuilder() throws IOException {
    StringBuilder destination = new StringBuilder();
    Readable source = wrapAsGenericReadable(new StringReader(ASCII));
    
    long copiedChars = CharStreams.copy(source, destination);
    
    assertEquals("Should copy ASCII content", ASCII, destination.toString());
    assertEquals("Should return number of copied characters", ASCII.length(), copiedChars);

    // Test with internationalized text
    StringBuilder i18nDestination = new StringBuilder();
    Readable i18nSource = wrapAsGenericReadable(new StringReader(I18N));
    long i18nCopiedChars = CharStreams.copy(i18nSource, i18nDestination);
    
    assertEquals("Should copy I18N content", I18N, i18nDestination.toString());
    assertEquals("Should return number of copied characters", I18N.length(), i18nCopiedChars);
  }

  public void testCopy_fromReaderToWriter() throws IOException {
    StringWriter destination = new StringWriter();
    
    long copiedChars = CharStreams.copy(new StringReader(ASCII), destination);
    
    assertEquals("Should copy ASCII content", ASCII, destination.toString());
    assertEquals("Should return number of copied characters", ASCII.length(), copiedChars);

    // Test with internationalized text
    StringWriter i18nDestination = new StringWriter();
    long i18nCopiedChars = CharStreams.copy(new StringReader(I18N), i18nDestination);
    
    assertEquals("Should copy I18N content", I18N, i18nDestination.toString());
    assertEquals("Should return number of copied characters", I18N.length(), i18nCopiedChars);
  }

  public void testCopy_fromReadableToWriter() throws IOException {
    StringWriter destination = new StringWriter();
    Readable source = wrapAsGenericReadable(new StringReader(ASCII));
    
    long copiedChars = CharStreams.copy(source, destination);
    
    assertEquals("Should copy ASCII content", ASCII, destination.toString());
    assertEquals("Should return number of copied characters", ASCII.length(), copiedChars);

    // Test with internationalized text
    StringWriter i18nDestination = new StringWriter();
    Readable i18nSource = wrapAsGenericReadable(new StringReader(I18N));
    long i18nCopiedChars = CharStreams.copy(i18nSource, i18nDestination);
    
    assertEquals("Should copy I18N content", I18N, i18nDestination.toString());
    assertEquals("Should return number of copied characters", I18N.length, i18nCopiedChars);
  }

  /**
   * Test for Guava issue 1061: https://github.com/google/guava/issues/1061
   *
   * <p>CharStreams.copy was failing to clear its CharBuffer after each read call, which effectively
   * reduced the available size of the buffer each time a call to read didn't fill up the available
   * space in the buffer completely. This caused performance problems and could lead to infinite loops
   * with certain Reader implementations.
   */
  @SuppressWarnings("InlineMeInliner") // String.repeat unavailable under Java 8
  public void testCopy_withReaderThatDoesNotFillBuffer_avoidsInfiniteLoop() throws IOException {
    // Create a long string to ensure the buffer hits 0 remaining before copy completes
    String longString = Strings.repeat("0123456789", 100);
    StringBuilder destination = new StringBuilder();
    Reader nonBufferFillingReader = createNonBufferFillingReader(new StringReader(longString));
    
    // The main assertion: copy should complete without infinite loop
    long copiedChars = CharStreams.copy(nonBufferFillingReader, destination);
    
    assertEquals("Should copy entire string", longString, destination.toString());
    assertEquals("Should return correct character count", longString.length(), copiedChars);
  }

  // ========== exhaust() Tests ==========

  public void testExhaust_withReader() throws IOException {
    Reader reader = new StringReader(ASCII);
    
    long exhaustedChars = CharStreams.exhaust(reader);
    
    assertEquals("Should exhaust all characters", ASCII.length(), exhaustedChars);
    assertEquals("Reader should be at end", -1, reader.read());
    assertEquals("Exhausting empty reader should return 0", 0, CharStreams.exhaust(reader));

    // Test with empty reader
    Reader emptyReader = new StringReader("");
    assertEquals("Empty reader should exhaust 0 characters", 0, CharStreams.exhaust(emptyReader));
    assertEquals("Empty reader should be at end", -1, emptyReader.read());
  }

  public void testExhaust_withReadable() throws IOException {
    CharBuffer buffer = CharBuffer.wrap(ASCII);
    
    long exhaustedChars = CharStreams.exhaust(buffer);
    
    assertEquals("Should exhaust all characters", ASCII.length(), exhaustedChars);
    assertEquals("Buffer should have no remaining characters", 0, buffer.remaining());
    assertEquals("Exhausting empty buffer should return 0", 0, CharStreams.exhaust(buffer));

    // Test with empty buffer
    CharBuffer emptyBuffer = CharBuffer.wrap("");
    assertEquals("Empty buffer should exhaust 0 characters", 0, CharStreams.exhaust(emptyBuffer));
    assertEquals("Empty buffer should have no remaining characters", 0, emptyBuffer.remaining());
  }

  // ========== nullWriter() Tests ==========

  public void testNullWriter_discardsAllWrittenData() throws Exception {
    Writer nullWriter = CharStreams.nullWriter();
    
    // Write various types of data - all should be discarded silently
    nullWriter.write('n');
    String testString = "Test string for NullWriter";
    nullWriter.write(testString);
    nullWriter.write(testString, 2, 10);
    nullWriter.append(null);
    nullWriter.append(null, 0, 4);

    // Test boundary conditions that should throw exceptions
    assertThrows("Negative start index should throw", 
        IndexOutOfBoundsException.class, 
        () -> nullWriter.append(null, -1, 4));

    assertThrows("End index beyond null length should throw", 
        IndexOutOfBoundsException.class, 
        () -> nullWriter.append(null, 0, 5));

    // Verify singleton behavior
    assertSame("nullWriter should return same instance", 
        CharStreams.nullWriter(), CharStreams.nullWriter());
  }

  // ========== Helper Methods ==========

  /**
   * Creates a LineProcessor that counts processed lines and optionally continues processing.
   */
  private LineProcessor<Integer> createLineCounterProcessor(boolean continueProcessing) {
    return new LineProcessor<Integer>() {
      int processedLineCount = 0;

      @Override
      public boolean processLine(String line) {
        processedLineCount++;
        return continueProcessing;
      }

      @Override
      public Integer getResult() {
        return processedLineCount;
      }
    };
  }

  /**
   * Creates a LineProcessor that captures content and stops after processing 2 lines.
   */
  private LineProcessor<Integer> createConditionalLineProcessor(StringBuilder contentCapture) {
    return new LineProcessor<Integer>() {
      int processedLineCount = 0;

      @Override
      public boolean processLine(String line) {
        processedLineCount++;
        contentCapture.append(line);
        return processedLineCount < 2; // Stop after 2 lines
      }

      @Override
      public Integer getResult() {
        return processedLineCount;
      }
    };
  }

  /**
   * Creates a Reader that intentionally doesn't fill the buffer completely on each read.
   * This tests the buffer management logic in CharStreams.copy().
   */
  private static Reader createNonBufferFillingReader(Reader delegate) {
    return new FilterReader(delegate) {
      @Override
      public int read(char[] buffer, int offset, int length) throws IOException {
        if (length <= 0) {
          fail("read called with invalid length: " + length);
        }
        // Intentionally read fewer characters than requested to test buffer clearing logic
        int reducedLength = Math.max(length - 1024, 0);
        return in.read(buffer, offset, reducedLength);
      }
    };
  }

  /**
   * Wraps an Appendable to defeat type-specific optimizations in CharStreams methods.
   */
  private static Appendable wrapAsGenericAppendable(Appendable delegate) {
    return new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        delegate.append(csq);
        return this;
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        delegate.append(csq, start, end);
        return this;
      }

      @Override
      public Appendable append(char c) throws IOException {
        delegate.append(c);
        return this;
      }
    };
  }

  /**
   * Wraps a Readable to defeat type-specific optimizations in CharStreams methods.
   */
  private static Readable wrapAsGenericReadable(Readable delegate) {
    return new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        return delegate.read(cb);
      }
    };
  }
}