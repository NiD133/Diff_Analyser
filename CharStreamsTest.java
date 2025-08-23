package com.google.common.io;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
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

/**
 * Unit test for {@link CharStreams}.
 */
@NullUnmarked
public class CharStreamsTest extends IoTestCase {

  private static final String SAMPLE_TEXT = "The quick brown fox jumped over the lazy dog.";
  private static final String ASCII = "ASCII sample text";
  private static final String I18N = "Internationalized text with special characters";

  /**
   * Tests the toString method of CharStreams.
   */
  public void testToString() throws IOException {
    Reader reader = new StringReader(SAMPLE_TEXT);
    String result = CharStreams.toString(reader);
    assertEquals(SAMPLE_TEXT, result);
  }

  /**
   * Tests reading lines from a Reader into a List.
   */
  public void testReadLines() throws IOException {
    Reader reader = new StringReader("a\nb\nc");
    List<String> lines = CharStreams.readLines(reader);
    assertEquals(ImmutableList.of("a", "b", "c"), lines);
  }

  /**
   * Tests reading lines with a LineProcessor.
   */
  public void testReadLines_withLineProcessor() throws IOException {
    String text = "a\nb\nc";

    // Test a LineProcessor that always returns false.
    Reader reader = new StringReader(text);
    LineProcessor<Integer> alwaysFalseProcessor = new LineProcessor<>() {
      int lineCount = 0;

      @Override
      public boolean processLine(String line) {
        lineCount++;
        return false;
      }

      @Override
      public Integer getResult() {
        return lineCount;
      }
    };
    assertEquals(1, CharStreams.readLines(reader, alwaysFalseProcessor).intValue());

    // Test a LineProcessor that always returns true.
    reader = new StringReader(text);
    LineProcessor<Integer> alwaysTrueProcessor = new LineProcessor<>() {
      int lineCount = 0;

      @Override
      public boolean processLine(String line) {
        lineCount++;
        return true;
      }

      @Override
      public Integer getResult() {
        return lineCount;
      }
    };
    assertEquals(3, CharStreams.readLines(reader, alwaysTrueProcessor).intValue());

    // Test a LineProcessor that stops after two lines.
    reader = new StringReader(text);
    StringBuilder processedLines = new StringBuilder();
    LineProcessor<Integer> conditionalProcessor = new LineProcessor<>() {
      int lineCount = 0;

      @Override
      public boolean processLine(String line) {
        lineCount++;
        processedLines.append(line);
        return lineCount < 2;
      }

      @Override
      public Integer getResult() {
        return lineCount;
      }
    };
    assertEquals(2, CharStreams.readLines(reader, conditionalProcessor).intValue());
    assertEquals("ab", processedLines.toString());
  }

  /**
   * Tests skipping characters with skipFully method.
   */
  public void testSkipFully_eof() throws IOException {
    Reader reader = new StringReader("abcde");
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 6));
  }

  /**
   * Tests skipping characters with skipFully method.
   */
  public void testSkipFully() throws IOException {
    String testString = "abcdef";
    Reader reader = new StringReader(testString);

    assertEquals(testString.charAt(0), reader.read());
    CharStreams.skipFully(reader, 1);
    assertEquals(testString.charAt(2), reader.read());
    CharStreams.skipFully(reader, 2);
    assertEquals(testString.charAt(5), reader.read());

    assertEquals(-1, reader.read());
  }

  /**
   * Tests the asWriter method.
   */
  public void testAsWriter() {
    // Should wrap Appendable in a new object
    Appendable appendable = new StringBuilder();
    Writer writer = CharStreams.asWriter(appendable);
    assertNotSame(appendable, writer);
    assertNotNull(writer);

    // A Writer should not be wrapped
    Appendable writerAppendable = new StringWriter();
    writer = CharStreams.asWriter(writerAppendable);
    assertSame(writerAppendable, writer);
  }

  /**
   * Tests the copy method with various Readable and Appendable combinations.
   */
  public void testCopy() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), wrapAsGenericAppendable(builder));
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);

    StringBuilder builder2 = new StringBuilder();
    copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), wrapAsGenericAppendable(builder2));
    assertEquals(I18N, builder2.toString());
    assertEquals(I18N.length(), copied);
  }

  /**
   * Tests the copy method from Reader to StringBuilder.
   */
  public void testCopy_toStringBuilder_fromReader() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(new StringReader(ASCII), builder);
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);

    StringBuilder builder2 = new StringBuilder();
    copied = CharStreams.copy(new StringReader(I18N), builder2);
    assertEquals(I18N, builder2.toString());
    assertEquals(I18N.length(), copied);
  }

  /**
   * Tests the copy method from Readable to StringBuilder.
   */
  public void testCopy_toStringBuilder_fromReadable() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), builder);
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);

    StringBuilder builder2 = new StringBuilder();
    copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), builder2);
    assertEquals(I18N, builder2.toString());
    assertEquals(I18N.length(), copied);
  }

  /**
   * Tests the copy method from Reader to Writer.
   */
  public void testCopy_toWriter_fromReader() throws IOException {
    StringWriter writer = new StringWriter();
    long copied = CharStreams.copy(new StringReader(ASCII), writer);
    assertEquals(ASCII, writer.toString());
    assertEquals(ASCII.length(), copied);

    StringWriter writer2 = new StringWriter();
    copied = CharStreams.copy(new StringReader(I18N), writer2);
    assertEquals(I18N, writer2.toString());
    assertEquals(I18N.length(), copied);
  }

  /**
   * Tests the copy method from Readable to Writer.
   */
  public void testCopy_toWriter_fromReadable() throws IOException {
    StringWriter writer = new StringWriter();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), writer);
    assertEquals(ASCII, writer.toString());
    assertEquals(ASCII.length(), copied);

    StringWriter writer2 = new StringWriter();
    copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), writer2);
    assertEquals(I18N, writer2.toString());
    assertEquals(I18N.length(), copied);
  }

  /**
   * Tests the copy method with a Reader that does not fill the buffer.
   */
  public void testCopyWithReaderThatDoesNotFillBuffer() throws IOException {
    String longString = Strings.repeat("0123456789", 100);
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(newNonBufferFillingReader(new StringReader(longString)), builder);
    assertEquals(longString, builder.toString());
    assertEquals(longString.length(), copied);
  }

  /**
   * Tests the exhaust method with a Reader.
   */
  public void testExhaust_reader() throws IOException {
    Reader reader = new StringReader(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(reader));
    assertEquals(-1, reader.read());
    assertEquals(0, CharStreams.exhaust(reader));

    Reader emptyReader = new StringReader("");
    assertEquals(0, CharStreams.exhaust(emptyReader));
    assertEquals(-1, emptyReader.read());
  }

  /**
   * Tests the exhaust method with a Readable.
   */
  public void testExhaust_readable() throws IOException {
    CharBuffer buffer = CharBuffer.wrap(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(buffer));
    assertEquals(0, buffer.remaining());
    assertEquals(0, CharStreams.exhaust(buffer));

    CharBuffer emptyBuffer = CharBuffer.wrap("");
    assertEquals(0, CharStreams.exhaust(emptyBuffer));
    assertEquals(0, emptyBuffer.remaining());
  }

  /**
   * Tests the nullWriter method.
   */
  public void testNullWriter() throws Exception {
    Writer nullWriter = CharStreams.nullWriter();
    nullWriter.write('n');
    String testString = "Test string for NullWriter";
    nullWriter.write(testString);
    nullWriter.write(testString, 2, 10);
    nullWriter.append(null);
    nullWriter.append(null, 0, 4);

    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, 0, 5));

    assertSame(CharStreams.nullWriter(), CharStreams.nullWriter());
  }

  /**
   * Returns a reader that reads fewer characters than requested.
   */
  private static Reader newNonBufferFillingReader(Reader reader) {
    return new FilterReader(reader) {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        if (len <= 0) {
          fail("read called with a len of " + len);
        }
        return in.read(cbuf, off, Math.max(len - 1024, 0));
      }
    };
  }

  /**
   * Wraps an Appendable to defeat type-specific optimizations.
   */
  private static Appendable wrapAsGenericAppendable(Appendable appendable) {
    return new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        appendable.append(csq);
        return this;
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        appendable.append(csq, start, end);
        return this;
      }

      @Override
      public Appendable append(char c) throws IOException {
        appendable.append(c);
        return this;
      }
    };
  }

  /**
   * Wraps a Readable to defeat type-specific optimizations.
   */
  private static Readable wrapAsGenericReadable(Readable readable) {
    return new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        return readable.read(cb);
      }
    };
  }
}