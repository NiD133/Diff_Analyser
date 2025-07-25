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
import org.junit.Test;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for {@link CharStreams}.  Focuses on readability and clear intent.
 */
@NullUnmarked
public class CharStreamsTest extends IoTestCase {

  private static final String TEST_TEXT = "The quick brown fox jumped over the lazy dog.";
  private static final String MULTI_LINE_TEXT = "a\nb\nc";

  @Test
  public void testToString_readsAllCharacters() throws IOException {
    StringReader reader = new StringReader(TEST_TEXT);
    String result = CharStreams.toString(reader);
    assertEquals("String read from reader should match the original text.", TEST_TEXT, result);
  }

  @Test
  public void testReadLines_returnsListOfLines() throws IOException {
    StringReader reader = new StringReader(MULTI_LINE_TEXT);
    List<String> lines = CharStreams.readLines(reader);
    assertEquals(
        "List of lines should match the expected lines.",
        ImmutableList.of("a", "b", "c"),
        lines);
  }

  @Test
  public void testReadLines_withLineProcessor_stopsWhenProcessorReturnsFalse() throws IOException {
    StringReader reader = new StringReader(MULTI_LINE_TEXT);
    StringBuilder processedLines = new StringBuilder();
    LineProcessor<Integer> processor =
        new LineProcessor<Integer>() {
          int linesSeen = 0;

          @Override
          public boolean processLine(String line) {
            linesSeen++;
            processedLines.append(line);
            return linesSeen < 2; // Stop after processing the second line.
          }

          @Override
          public Integer getResult() {
            return linesSeen;
          }
        };

    int numberOfLinesProcessed = CharStreams.readLines(reader, processor).intValue();

    assertEquals("Processor should have processed 2 lines.", 2, numberOfLinesProcessed);
    assertEquals("Processor should have only seen the first 2 lines.", "ab", processedLines.toString());
  }

  @Test
  public void testReadLines_withLineProcessor_processesAllLinesWhenProcessorAlwaysReturnsTrue() throws IOException {
      StringReader reader = new StringReader(MULTI_LINE_TEXT);
      LineProcessor<Integer> alwaysTrueProcessor = new LineProcessor<Integer>() {
          int linesSeen = 0;
          @Override
          public boolean processLine(String line) {
              linesSeen++;
              return true;
          }

          @Override
          public Integer getResult() {
              return linesSeen;
          }
      };

      int numberOfLinesProcessed = CharStreams.readLines(reader, alwaysTrueProcessor).intValue();
      assertEquals("Processor should have processed all 3 lines.", 3, numberOfLinesProcessed);
  }


  @Test
  public void testSkipFully_throwsEofExceptionWhenEndOfFileIsReached() throws IOException {
    Reader reader = new StringReader("abcde");
    assertThrows(
        "Should throw EOFException if attempting to skip past the end of the reader.",
        EOFException.class,
        () -> CharStreams.skipFully(reader, 6));
  }

  @Test
  public void testSkipFully_skipsTheSpecifiedNumberOfCharacters() throws IOException {
    String testString = "abcdef";
    Reader reader = new StringReader(testString);

    assertEquals(testString.charAt(0), reader.read()); // Read 'a'
    CharStreams.skipFully(reader, 1); // Skip 'b'
    assertEquals(testString.charAt(2), reader.read()); // Read 'c'
    CharStreams.skipFully(reader, 2); // Skip 'd' and 'e'
    assertEquals(testString.charAt(5), reader.read()); // Read 'f'

    assertEquals(-1, reader.read()); // End of stream
  }

  @Test
  public void testAsWriter_wrapsAppendableInWriter() {
    Appendable plainAppendable = new StringBuilder();
    Writer writer = CharStreams.asWriter(plainAppendable);

    assertNotSame("Should return a new Writer instance, not the original Appendable.", plainAppendable, writer);
    assertNotNull("The returned writer should not be null.", writer);
  }

  @Test
  public void testAsWriter_returnsSameWriterIfAppendableIsAlreadyAWriter() {
    StringWriter stringWriter = new StringWriter();
    Writer writer = CharStreams.asWriter(stringWriter);

    assertSame(
        "Should return the same Writer instance if the Appendable is already a Writer.",
        stringWriter,
        writer);
  }

  @Test
  public void testCopy_copiesAllCharactersFromReadableToAppendable() throws IOException {
    StringBuilder destination = new StringBuilder();
    long copied = CharStreams.copy(new StringReader(ASCII), destination);

    assertEquals("The copied string should match the original.", ASCII, destination.toString());
    assertEquals("The number of characters copied should match the length of the original string.", ASCII.length(), copied);

    StringBuilder destination2 = new StringBuilder();
    copied = CharStreams.copy(new StringReader(I18N), destination2);
    assertEquals(I18N, destination2.toString());
    assertEquals(I18N.length(), copied);
  }

  @Test
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

    @Test
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

    @Test
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

    @Test
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

  @Test
  public void testCopyWithReaderThatDoesNotFillBuffer() throws IOException {
    String longString = Strings.repeat("0123456789", 100); // Create a sufficiently long string.
    StringBuilder destination = new StringBuilder();

    // Copy from a reader that doesn't always fill the buffer.  This tests for a regression
    // where the buffer size was incorrectly reduced on each read.
    long copied = CharStreams.copy(newNonBufferFillingReader(new StringReader(longString)), destination);

    assertEquals("Copied string should match the original.", longString, destination.toString());
    assertEquals("Number of characters copied should match the length of the original.", longString.length(), copied);
  }

  @Test
  public void testExhaust_reader_readsUntilEndOfStream() throws IOException {
    Reader reader = new StringReader(ASCII);
    long charsRead = CharStreams.exhaust(reader);
    assertEquals("Should have read all characters from the reader.", ASCII.length(), charsRead);
    assertEquals("Reader should be at the end of the stream.", -1, reader.read());

    long additionalCharsRead = CharStreams.exhaust(reader);
    assertEquals("Should not read any more characters from an exhausted reader.", 0, additionalCharsRead);
  }

  @Test
  public void testExhaust_readable_readsUntilEndOfStream() throws IOException {
    CharBuffer buffer = CharBuffer.wrap(ASCII);
    long charsRead = CharStreams.exhaust(buffer);
    assertEquals("Should have read all characters from the readable.", ASCII.length(), charsRead);
    assertEquals("Readable should be at the end of the stream.", 0, buffer.remaining());

    long additionalCharsRead = CharStreams.exhaust(buffer);
    assertEquals("Should not read any more characters from an exhausted reader.", 0, additionalCharsRead);
  }

  @Test
  public void testNullWriter_writesNothing() throws Exception {
    Writer nullWriter = CharStreams.nullWriter(); //Get a null writer instance

    nullWriter.write('n'); //Write a char
    nullWriter.write("Test string"); //Write a String
    nullWriter.write("Test string", 2, 5); //Write a substring
    nullWriter.append(null); //Append null
    nullWriter.append(null, 0, 0); //Append a subsequence of null

    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, -1, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, 0, 5));

    //The null writer doesn't actually write anything, so there is nothing to assert regarding the *content* of what was written.
    assertSame(CharStreams.nullWriter(), CharStreams.nullWriter()); // Ensure singleton
  }

  /**
   * Returns a reader that reads a smaller chunk than the requested size. This is used to test the
   * copy method's ability to handle readers that don't fill the buffer completely.
   */
  private static Reader newNonBufferFillingReader(Reader reader) {
    return new FilterReader(reader) {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        if (len <= 0) {
          fail("read called with a len of " + len);
        }
        // Read a smaller number of chars than requested.
        return in.read(cbuf, off, Math.max(len - 1024, 0));
      }
    };
  }

    /** Wrap an appendable in an appendable to defeat any type specific optimizations. */
    private static Appendable wrapAsGenericAppendable(Appendable a) {
        return new Appendable() {

            @Override
            public Appendable append(CharSequence csq) throws IOException {
                a.append(csq);
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                a.append(csq, start, end);
                return this;
            }

            @Override
            public Appendable append(char c) throws IOException {
                a.append(c);
                return this;
            }
        };
    }

    /** Wrap a readable in a readable to defeat any type specific optimizations. */
    private static Readable wrapAsGenericReadable(Readable a) {
        return new Readable() {
            @Override
            public int read(CharBuffer cb) throws IOException {
                return a.read(cb);
            }
        };
    }
}