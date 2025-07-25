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
  private static final String ASCII = "ASCII text";
  private static final String I18N = "International text";

  public void testToString() throws IOException {
    Reader reader = new StringReader(SAMPLE_TEXT);
    String result = CharStreams.toString(reader);
    assertEquals(SAMPLE_TEXT, result);
  }

  public void testReadLines() throws IOException {
    Reader reader = new StringReader("a\nb\nc");
    List<String> lines = CharStreams.readLines(reader);
    assertEquals(ImmutableList.of("a", "b", "c"), lines);
  }

  public void testReadLines_withLineProcessor() throws IOException {
    String text = "a\nb\nc";

    testLineProcessorAlwaysFalse(text);
    testLineProcessorAlwaysTrue(text);
    testLineProcessorConditional(text);
  }

  private void testLineProcessorAlwaysFalse(String text) throws IOException {
    Reader reader = new StringReader(text);
    LineProcessor<Integer> processor = new LineProcessor<Integer>() {
      int seen = 0;

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

    int result = CharStreams.readLines(reader, processor);
    assertEquals("processLine was called more than once", 1, result);
  }

  private void testLineProcessorAlwaysTrue(String text) throws IOException {
    Reader reader = new StringReader(text);
    LineProcessor<Integer> processor = new LineProcessor<Integer>() {
      int seen = 0;

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

    int result = CharStreams.readLines(reader, processor);
    assertEquals("processLine was not called for all the lines", 3, result);
  }

  private void testLineProcessorConditional(String text) throws IOException {
    Reader reader = new StringReader(text);
    StringBuilder sb = new StringBuilder();
    LineProcessor<Integer> processor = new LineProcessor<Integer>() {
      int seen = 0;

      @Override
      public boolean processLine(String line) {
        seen++;
        sb.append(line);
        return seen < 2;
      }

      @Override
      public Integer getResult() {
        return seen;
      }
    };

    int result = CharStreams.readLines(reader, processor);
    assertEquals(2, result);
    assertEquals("ab", sb.toString());
  }

  public void testSkipFully_eof() throws IOException {
    Reader reader = new StringReader("abcde");
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 6));
  }

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

  public void testAsWriter() {
    Appendable plainAppendable = new StringBuilder();
    Writer result = CharStreams.asWriter(plainAppendable);
    assertNotSame(plainAppendable, result);
    assertNotNull(result);

    Appendable secretlyAWriter = new StringWriter();
    result = CharStreams.asWriter(secretlyAWriter);
    assertSame(secretlyAWriter, result);
  }

  public void testCopy() throws IOException {
    testCopyToStringBuilderFromReader();
    testCopyToStringBuilderFromReadable();
    testCopyToWriterFromReader();
    testCopyToWriterFromReadable();
  }

  private void testCopyToStringBuilderFromReader() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(new StringReader(ASCII), builder);
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);

    StringBuilder builder2 = new StringBuilder();
    copied = CharStreams.copy(new StringReader(I18N), builder2);
    assertEquals(I18N, builder2.toString());
    assertEquals(I18N.length(), copied);
  }

  private void testCopyToStringBuilderFromReadable() throws IOException {
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), builder);
    assertEquals(ASCII, builder.toString());
    assertEquals(ASCII.length(), copied);

    StringBuilder builder2 = new StringBuilder();
    copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), builder2);
    assertEquals(I18N, builder2.toString());
    assertEquals(I18N.length(), copied);
  }

  private void testCopyToWriterFromReader() throws IOException {
    StringWriter writer = new StringWriter();
    long copied = CharStreams.copy(new StringReader(ASCII), writer);
    assertEquals(ASCII, writer.toString());
    assertEquals(ASCII.length(), copied);

    StringWriter writer2 = new StringWriter();
    copied = CharStreams.copy(new StringReader(I18N), writer2);
    assertEquals(I18N, writer2.toString());
    assertEquals(I18N.length(), copied);
  }

  private void testCopyToWriterFromReadable() throws IOException {
    StringWriter writer = new StringWriter();
    long copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(ASCII)), writer);
    assertEquals(ASCII, writer.toString());
    assertEquals(ASCII.length(), copied);

    StringWriter writer2 = new StringWriter();
    copied = CharStreams.copy(wrapAsGenericReadable(new StringReader(I18N)), writer2);
    assertEquals(I18N, writer2.toString());
    assertEquals(I18N.length(), copied);
  }

  public void testCopyWithReaderThatDoesNotFillBuffer() throws IOException {
    String string = Strings.repeat("0123456789", 100);
    StringBuilder builder = new StringBuilder();
    long copied = CharStreams.copy(newNonBufferFillingReader(new StringReader(string)), builder);
    assertEquals(string, builder.toString());
    assertEquals(string.length(), copied);
  }

  public void testExhaust_reader() throws IOException {
    Reader reader = new StringReader(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(reader));
    assertEquals(-1, reader.read());
    assertEquals(0, CharStreams.exhaust(reader));

    Reader empty = new StringReader("");
    assertEquals(0, CharStreams.exhaust(empty));
    assertEquals(-1, empty.read());
  }

  public void testExhaust_readable() throws IOException {
    CharBuffer buffer = CharBuffer.wrap(ASCII);
    assertEquals(ASCII.length(), CharStreams.exhaust(buffer));
    assertEquals(0, buffer.remaining());
    assertEquals(0, CharStreams.exhaust(buffer));

    CharBuffer emptyBuffer = CharBuffer.wrap("");
    assertEquals(0, CharStreams.exhaust(emptyBuffer));
    assertEquals(0, emptyBuffer.remaining());
  }

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

  private static Readable wrapAsGenericReadable(Readable readable) {
    return new Readable() {
      @Override
      public int read(CharBuffer cb) throws IOException {
        return readable.read(cb);
      }
    };
  }
}