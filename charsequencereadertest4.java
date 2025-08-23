package com.google.common.io;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.CharBuffer;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests the functionality of {@link CharSequenceReader}, including various read and skip methods.
 *
 * <p>This test was refactored from a single, large test method into a collection of focused helper
 * methods to improve readability and maintainability.
 */
@NullUnmarked
public class CharSequenceReaderBehaviorTest extends TestCase {

  private static final int CHUNK_SIZE = 5;

  /**
   * Asserts that a {@link CharSequenceReader} for the given sequence behaves correctly for all
   * reading and skipping scenarios.
   */
  private static void assertReadsCorrectly(CharSequence charSequence) throws IOException {
    assertReadsCharByChar(charSequence);
    assertReadsToCharArray(charSequence);
    assertReadsToCharArrayInChunks(charSequence);
    assertReadsToCharBuffer(charSequence);
    assertReadsToCharBufferInChunks(charSequence);
    assertSkipsAll(charSequence);
    assertSkipsAndReadsRemaining(charSequence);
  }

  /** Tests reading the sequence character by character using {@link CharSequenceReader#read()}. */
  private static void assertReadsCharByChar(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    for (int i = 0; i < expected.length(); i++) {
      assertEquals(expected.charAt(i), reader.read());
    }
    assertFullyRead(reader);
  }

  /** Tests reading the entire sequence into a char array using {@link CharSequenceReader#read(char[])}. */
  private static void assertReadsToCharArray(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    char[] buf = new char[expected.length()];

    if (expected.isEmpty()) {
      // Per Reader.read(char[]), reading into a 0-length buffer should return 0.
      assertEquals(0, reader.read(buf));
    } else {
      assertEquals(expected.length(), reader.read(buf));
      assertEquals(expected, new String(buf));
    }
    assertFullyRead(reader);
  }

  /**
   * Tests reading the sequence in chunks into a char array using {@link
   * CharSequenceReader#read(char[], int, int)}.
   */
  private static void assertReadsToCharArrayInChunks(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    char[] buf = new char[CHUNK_SIZE];
    StringBuilder builder = new StringBuilder();
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      builder.append(buf, 0, read);
    }
    assertEquals(expected, builder.toString());
    assertFullyRead(reader);
  }

  /**
   * Tests reading the entire sequence into a {@link CharBuffer} using {@link
   * CharSequenceReader#read(CharBuffer)}.
   */
  private static void assertReadsToCharBuffer(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    CharBuffer charBuffer = CharBuffer.allocate(expected.length());

    int expectedRead = expected.isEmpty() ? -1 : expected.length();
    assertEquals(expectedRead, reader.read(charBuffer));

    if (!expected.isEmpty()) {
      Java8Compatibility.flip(charBuffer);
      assertEquals(expected, charBuffer.toString());
    }
    assertFullyRead(reader);
  }

  /**
   * Tests reading the sequence in chunks into a {@link CharBuffer} using {@link
   * CharSequenceReader#read(CharBuffer)}.
   */
  private static void assertReadsToCharBufferInChunks(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    CharBuffer charBuffer = CharBuffer.allocate(CHUNK_SIZE);
    StringBuilder builder = new StringBuilder();
    while (reader.read(charBuffer) != -1) {
      Java8Compatibility.flip(charBuffer);
      builder.append(charBuffer);
      Java8Compatibility.clear(charBuffer);
    }
    assertEquals(expected, builder.toString());
    assertFullyRead(reader);
  }

  /** Tests skipping the entire sequence using {@link CharSequenceReader#skip(long)}. */
  private static void assertSkipsAll(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    assertEquals(expected.length(), reader.skip(Long.MAX_VALUE));
    assertFullyRead(reader);
  }

  /**
   * Tests skipping a portion of the sequence and then reading the remainder.
   */
  private static void assertSkipsAndReadsRemaining(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();
    if (expected.length() > CHUNK_SIZE) {
      CharSequenceReader reader = new CharSequenceReader(charSequence);
      assertEquals(CHUNK_SIZE, reader.skip(CHUNK_SIZE));

      char[] remainingBuf = new char[expected.length() - CHUNK_SIZE];
      assertEquals(remainingBuf.length, reader.read(remainingBuf, 0, remainingBuf.length));
      assertEquals(expected.substring(CHUNK_SIZE), new String(remainingBuf));
      assertFullyRead(reader);
    }
  }

  /** Asserts that the reader is fully consumed and subsequent reads or skips do nothing. */
  private static void assertFullyRead(CharSequenceReader reader) throws IOException {
    assertEquals(-1, reader.read());
    assertEquals(-1, reader.read(new char[10], 0, 10));
    assertEquals(-1, reader.read(CharBuffer.allocate(10)));
    assertEquals(0, reader.skip(10));
  }

  public void testIllegalArguments() {
    CharSequenceReader reader = new CharSequenceReader("12345");
    char[] buf = new char[10];

    // Test invalid arguments for read(char[], int, int)
    // offset + length is greater than buffer length
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 0, 11));
    // offset + length is greater than buffer length
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 10, 1));
    // offset is greater than buffer length
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 11, 0));
    // offset is negative
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, -1, 5));
    // length is negative
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 5, -1));

    // Test invalid arguments for other methods
    assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
    assertThrows(IllegalArgumentException.class, () -> reader.mark(-1));
  }

  // This test method is a placeholder to show how assertReadsCorrectly would be used.
  // The original test class did not include the actual test methods calling the helper.
  public void testReading_longString() throws IOException {
    assertReadsCorrectly("abcdefghijklmnopqrstuvwxyz");
  }

  // This test method is a placeholder to show how assertReadsCorrectly would be used.
  public void testReading_emptyString() throws IOException {
    assertReadsCorrectly("");
  }
}