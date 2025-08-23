package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import junit.framework.TestCase;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * <p>This suite focuses on verifying the correctness of various read and skip operations, as well
 * as the mark/reset functionality, across different types of CharSequence inputs.
 */
public class CharSequenceReaderTest extends TestCase {

  private static final String SHORT_STRING = "abcdef";
  private static final String LONG_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";

  // *****************************************************************
  // ** Tests for various read() and skip() operations
  // *****************************************************************

  public void testReadOperations_withEmptyString() throws IOException {
    assertReadOperationsWorkCorrectly("");
  }

  public void testReadOperations_withShortString() throws IOException {
    assertReadOperationsWorkCorrectly(SHORT_STRING);
  }

  public void testReadOperations_withLongString() throws IOException {
    assertReadOperationsWorkCorrectly(LONG_STRING);
  }

  /**
   * A test battery that runs a series of read/skip assertions against a given CharSequence. This
   * method is called by the public test methods with different inputs.
   */
  private void assertReadOperationsWorkCorrectly(String sequence) throws IOException {
    assertCanReadCharByChar(sequence);
    assertCanReadToFullCharArray(sequence);
    assertCanReadToCharArrayInChunks(sequence);
    assertCanReadToFullCharBuffer(sequence);
    assertCanReadToCharBufferInChunks(sequence);
    assertCanSkipEntireSequence(sequence);
    assertCanSkipPartially(sequence);
  }

  private void assertCanReadCharByChar(String expected) throws IOException {
    Reader reader = new CharSequenceReader(expected);
    for (int i = 0; i < expected.length(); i++) {
      assertEquals("Character at index " + i, expected.charAt(i), reader.read());
    }
    assertFullyRead(reader);
  }

  private void assertCanReadToFullCharArray(String expected) throws IOException {
    Reader reader = new CharSequenceReader(expected);
    char[] buffer = new char[expected.length()];

    int expectedBytesRead = expected.isEmpty() ? -1 : expected.length();
    assertEquals(expectedBytesRead, reader.read(buffer));
    assertEquals(expected, new String(buffer));

    assertFullyRead(reader);
  }

  private void assertCanReadToCharArrayInChunks(String expected) throws IOException {
    final int chunkSize = 5;
    Reader reader = new CharSequenceReader(expected);
    char[] buffer = new char[chunkSize];
    StringBuilder result = new StringBuilder();

    int read;
    while ((read = reader.read(buffer, 0, buffer.length)) != -1) {
      result.append(buffer, 0, read);
    }

    assertEquals(expected, result.toString());
    assertFullyRead(reader);
  }

  private void assertCanReadToFullCharBuffer(String expected) throws IOException {
    Reader reader = new CharSequenceReader(expected);
    CharBuffer buffer = CharBuffer.allocate(expected.length());

    int expectedBytesRead = expected.isEmpty() ? -1 : expected.length();
    assertEquals(expectedBytesRead, reader.read(buffer));

    buffer.flip(); // Prepare buffer for reading
    assertEquals(expected, buffer.toString());
    assertFullyRead(reader);
  }

  private void assertCanReadToCharBufferInChunks(String expected) throws IOException {
    final int chunkSize = 5;
    Reader reader = new CharSequenceReader(expected);
    CharBuffer buffer = CharBuffer.allocate(chunkSize);
    StringBuilder result = new StringBuilder();

    while (reader.read(buffer) != -1) {
      buffer.flip(); // Prepare buffer for reading
      result.append(buffer);
      buffer.clear(); // Prepare buffer for writing
    }

    assertEquals(expected, result.toString());
    assertFullyRead(reader);
  }

  private void assertCanSkipEntireSequence(String expected) throws IOException {
    Reader reader = new CharSequenceReader(expected);
    assertEquals(expected.length(), reader.skip(Long.MAX_VALUE));
    assertFullyRead(reader);
  }

  private void assertCanSkipPartially(String expected) throws IOException {
    if (expected.length() <= 5) {
      return; // Skip test for strings that are too short
    }
    Reader reader = new CharSequenceReader(expected);
    long charsToSkip = 5;
    assertEquals(charsToSkip, reader.skip(charsToSkip));
    assertEquals(expected.substring(5), readFully(reader));
    assertFullyRead(reader);
  }

  // *****************************************************************
  // ** Tests for mark() and reset()
  // *****************************************************************

  public void testMarkAndReset_allowsReReading() throws IOException {
    String data = "abcdefghijklmnopqrstuvwxyz";
    CharSequenceReader reader = new CharSequenceReader(data);

    assertTrue("Reader should support mark()", reader.markSupported());

    // Scenario 1: Read fully, reset to beginning, and read again.
    assertEquals("First read should match original data", data, readFully(reader));
    assertFullyRead(reader);

    reader.reset();
    assertEquals("Second read after reset should match original data", data, readFully(reader));
    assertFullyRead(reader);

    // Scenario 2: Reset, skip, mark, read, then reset to the mark.
    reader.reset();
    long skipped = reader.skip(5);
    assertEquals(5, skipped);

    reader.mark(Integer.MAX_VALUE);
    String remainingAfterSkip = data.substring(5);
    assertEquals(
        "Read after skip and mark should match remaining data",
        remainingAfterSkip,
        readFully(reader));
    assertFullyRead(reader);

    // Reset to the mark (after the first 5 chars) and read again.
    reader.reset();
    assertEquals(
        "Read after resetting to mark should match remaining data",
        remainingAfterSkip,
        readFully(reader));
    assertFullyRead(reader);
  }

  // *****************************************************************
  // ** Helper Methods
  // *****************************************************************

  /** Asserts that the reader is at its end and subsequent reads return EOF or 0. */
  private static void assertFullyRead(Reader reader) throws IOException {
    assertEquals("Reading a single char from a finished reader should return -1", -1, reader.read());
    assertEquals(
        "Reading into a char[] from a finished reader should return -1",
        -1,
        reader.read(new char[10], 0, 10));
    assertEquals(
        "Reading into a CharBuffer from a finished reader should return -1",
        -1,
        reader.read(CharBuffer.allocate(10)));
    assertEquals("Skipping from a finished reader should return 0", 0, reader.skip(10));
  }

  /** Reads all remaining characters from the reader and returns them as a String. */
  private static String readFully(Reader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    int ch;
    while ((ch = reader.read()) != -1) {
      builder.append((char) ch);
    }
    return builder.toString();
  }
}