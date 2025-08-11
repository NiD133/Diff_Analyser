package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.CharBuffer;
import org.jspecify.annotations.NullUnmarked;
import org.junit.Test;

/**
 * Readability-focused tests for CharSequenceReader.
 *
 * These tests exercise all public Reader behaviors (single-char reads, array reads, CharBuffer
 * reads, skip, mark/reset, and closed-reader behavior) against a variety of inputs.
 */
@NullUnmarked
public class CharSequenceReaderTest {

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
  private static final String ASCII_MIXED =
      "abcdefghijklmnopqrstuvwxyz\n"
          + "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r"
          + "0123456789\r\n"
          + "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| ";
  private static final int CHUNK_SIZE = 5;

  @Test
  public void read_emptyString() throws IOException {
    verifyAllReadPaths("");
  }

  @Test
  public void read_variousStrings() throws IOException {
    verifyAllReadPaths("abc");
    verifyAllReadPaths("abcde");
    verifyAllReadPaths("abcdefghijkl");
    verifyAllReadPaths(ASCII_MIXED);
  }

  @Test
  public void markAndReset() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(ALPHABET);
    assertTrue(reader.markSupported());

    // Initial full read reaches EOF.
    assertEquals(ALPHABET, readFully(reader));
    assertFullyConsumed(reader);

    // reset() goes back to initial mark (position 0).
    reader.reset();
    assertEquals(ALPHABET, readFully(reader));
    assertFullyConsumed(reader);

    // Skip some, mark, read to end, then reset back to that mark.
    reader.reset();
    assertEquals(5, reader.skip(5));
    reader.mark(Integer.MAX_VALUE);

    assertEquals(ALPHABET.substring(5), readFully(reader));
    assertFullyConsumed(reader);

    reader.reset();
    assertEquals(ALPHABET.substring(5), readFully(reader));
    assertFullyConsumed(reader);
  }

  @Test
  public void invalidArguments() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("12345");
    char[] buf = new char[10];

    // read(char[], off, len) bounds checks
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 0, 11));  // off+len > len
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 10, 1));  // off at end
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 11, 0));  // off > len
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, -1, 5));  // off < 0
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 5, -1));  // len < 0
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buf, 0, 11));  // duplicate for clarity

    // skip and mark argument validation
    assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
    assertThrows(IllegalArgumentException.class, () -> reader.mark(-1));
  }

  @Test
  public void methodsThrowWhenClosed() throws IOException {
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

  // Verifies all supported "ways of reading" produce the exact same sequence.
  private static void verifyAllReadPaths(CharSequence input) throws IOException {
    String expected = input.toString();

    verifyReadOneByOne(input, expected);
    verifyReadIntoExactArray(input, expected);
    verifyReadIntoArrayByChunks(input, expected);
    verifyReadIntoExactCharBuffer(input, expected);
    verifyReadIntoCharBufferByChunks(input, expected);
    verifySkipAll(input, expected);
    verifySkipThenReadRemainderIfLongEnough(input, expected);
  }

  private static void verifyReadOneByOne(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    for (int i = 0; i < expected.length(); i++) {
      assertEquals("read() at index " + i, expected.charAt(i), reader.read());
    }
    assertFullyConsumed(reader);
  }

  private static void verifyReadIntoExactArray(CharSequence input, String expected)
      throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    char[] buf = new char[expected.length()];

    // For empty input, the implementation returns -1 for read(char[]).
    int expectedCount = expected.isEmpty() ? -1 : expected.length();
    assertEquals("read(char[]) size=" + buf.length, expectedCount, reader.read(buf));

    assertEquals(expected, new String(buf));
    assertFullyConsumed(reader);
  }

  private static void verifyReadIntoArrayByChunks(CharSequence input, String expected)
      throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    char[] buf = new char[CHUNK_SIZE];
    StringBuilder out = new StringBuilder();

    int n;
    while ((n = reader.read(buf, 0, buf.length)) != -1) {
      out.append(buf, 0, n);
    }
    assertEquals(expected, out.toString());
    assertFullyConsumed(reader);
  }

  private static void verifyReadIntoExactCharBuffer(CharSequence input, String expected)
      throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    CharBuffer buf = CharBuffer.allocate(expected.length());

    // For empty input, the implementation returns -1 for read(CharBuffer).
    int expectedCount = expected.isEmpty() ? -1 : expected.length();
    assertEquals("read(CharBuffer) cap=" + buf.capacity(), expectedCount, reader.read(buf));

    Java8Compatibility.flip(buf);
    assertEquals(expected, buf.toString());
    assertFullyConsumed(reader);
  }

  private static void verifyReadIntoCharBufferByChunks(CharSequence input, String expected)
      throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    CharBuffer buf = CharBuffer.allocate(CHUNK_SIZE);
    StringBuilder out = new StringBuilder();

    while (reader.read(buf) != -1) {
      Java8Compatibility.flip(buf);
      out.append(buf);
      Java8Compatibility.clear(buf);
    }
    assertEquals(expected, out.toString());
    assertFullyConsumed(reader);
  }

  private static void verifySkipAll(CharSequence input, String expected) throws IOException {
    CharSequenceReader reader = new CharSequenceReader(input);
    assertEquals(expected.length(), reader.skip(Long.MAX_VALUE));
    assertFullyConsumed(reader);
  }

  private static void verifySkipThenReadRemainderIfLongEnough(
      CharSequence input, String expected) throws IOException {
    if (expected.length() <= CHUNK_SIZE) {
      return; // Not enough data to meaningfully skip then read.
    }

    CharSequenceReader reader = new CharSequenceReader(input);
    assertEquals(CHUNK_SIZE, reader.skip(CHUNK_SIZE));

    char[] remaining = new char[expected.length() - CHUNK_SIZE];
    assertEquals(remaining.length, reader.read(remaining, 0, remaining.length));
    assertEquals(expected.substring(CHUNK_SIZE), new String(remaining));
    assertFullyConsumed(reader);
  }

  // After reaching EOF, all read variants must report EOF, and skip must return 0.
  private static void assertFullyConsumed(CharSequenceReader reader) throws IOException {
    assertEquals(-1, reader.read());
    assertEquals(-1, reader.read(new char[10], 0, 10));
    assertEquals(-1, reader.read(CharBuffer.allocate(10)));
    assertEquals(0, reader.skip(10));
  }

  private static String readFully(CharSequenceReader reader) throws IOException {
    StringBuilder out = new StringBuilder();
    int ch;
    while ((ch = reader.read()) != -1) {
      out.append((char) ch);
    }
    return out.toString();
  }
}