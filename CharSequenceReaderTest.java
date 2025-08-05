package com.google.common.io;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.CharBuffer;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Unit tests for {@link CharSequenceReader}.
 * This test suite verifies the correct behavior of CharSequenceReader under various conditions.
 * It includes tests for reading, marking, resetting, handling illegal arguments, and behavior when closed.
 * 
 * Author: Colin Decker
 */
@NullUnmarked
public class CharSequenceReaderTest extends TestCase {

  /**
   * Tests reading from an empty string.
   */
  public void testReadEmptyString() throws IOException {
    assertReadsCorrectly("");
  }

  /**
   * Tests reading from various non-empty strings.
   */
  public void testReadsStringsCorrectly() throws IOException {
    assertReadsCorrectly("abc");
    assertReadsCorrectly("abcde");
    assertReadsCorrectly("abcdefghijkl");
    assertReadsCorrectly(
        "abcdefghijklmnopqrstuvwxyz\n" +
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r" +
        "0123456789\r\n" +
        "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| ");
  }

  /**
   * Tests the mark and reset functionality of the reader.
   */
  public void testMarkAndReset() throws IOException {
    String testString = "abcdefghijklmnopqrstuvwxyz";
    CharSequenceReader reader = new CharSequenceReader(testString);
    assertTrue(reader.markSupported());

    // Read the entire string
    assertEquals(testString, readFully(reader));
    assertFullyRead(reader);

    // Reset and read again
    reader.reset();
    assertEquals(testString, readFully(reader));
    assertFullyRead(reader);

    // Reset, skip some characters, mark, and read the rest
    reader.reset();
    assertEquals(5, reader.skip(5));
    reader.mark(Integer.MAX_VALUE);
    assertEquals(testString.substring(5), readFully(reader));
    assertFullyRead(reader);

    // Reset to the mark and read the rest
    reader.reset();
    assertEquals(testString.substring(5), readFully(reader));
    assertFullyRead(reader);
  }

  /**
   * Tests handling of illegal arguments.
   */
  public void testIllegalArguments() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("12345");
    char[] buffer = new char[10];

    // Test various out-of-bounds read operations
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 0, 11));
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 10, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 11, 0));
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, -1, 5));
    assertThrows(IndexOutOfBoundsException.class, () -> reader.read(buffer, 5, -1));

    // Test illegal skip and mark operations
    assertThrows(IllegalArgumentException.class, () -> reader.skip(-1));
    assertThrows(IllegalArgumentException.class, () -> reader.mark(-1));
  }

  /**
   * Tests behavior of the reader when it is closed.
   */
  public void testMethodsThrowWhenClosed() throws IOException {
    CharSequenceReader reader = new CharSequenceReader("");
    reader.close();

    // Verify that all operations throw IOException when the reader is closed
    assertThrows(IOException.class, () -> reader.read());
    assertThrows(IOException.class, () -> reader.read(new char[10]));
    assertThrows(IOException.class, () -> reader.read(new char[10], 0, 10));
    assertThrows(IOException.class, () -> reader.read(CharBuffer.allocate(10)));
    assertThrows(IOException.class, () -> reader.skip(10));
    assertThrows(IOException.class, () -> reader.ready());
    assertThrows(IOException.class, () -> reader.mark(10));
    assertThrows(IOException.class, () -> reader.reset());
  }

  /**
   * Verifies that the CharSequenceReader reads the given CharSequence correctly using various methods.
   */
  private static void assertReadsCorrectly(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();

    // Read character by character
    CharSequenceReader reader = new CharSequenceReader(charSequence);
    for (int i = 0; i < expected.length(); i++) {
      assertEquals(expected.charAt(i), reader.read());
    }
    assertFullyRead(reader);

    // Read all characters into a single array
    reader = new CharSequenceReader(charSequence);
    char[] buffer = new char[expected.length()];
    assertEquals(expected.length() == 0 ? -1 : expected.length(), reader.read(buffer));
    assertEquals(expected, new String(buffer));
    assertFullyRead(reader);

    // Read in chunks into a fixed-size array
    reader = new CharSequenceReader(charSequence);
    buffer = new char[5];
    StringBuilder builder = new StringBuilder();
    int read;
    while ((read = reader.read(buffer, 0, buffer.length)) != -1) {
      builder.append(buffer, 0, read);
    }
    assertEquals(expected, builder.toString());
    assertFullyRead(reader);

    // Read all characters into a CharBuffer
    reader = new CharSequenceReader(charSequence);
    CharBuffer charBuffer = CharBuffer.allocate(expected.length());
    assertEquals(expected.length() == 0 ? -1 : expected.length(), reader.read(charBuffer));
    Java8Compatibility.flip(charBuffer);
    assertEquals(expected, charBuffer.toString());
    assertFullyRead(reader);

    // Read in chunks into a fixed-size CharBuffer
    reader = new CharSequenceReader(charSequence);
    charBuffer = CharBuffer.allocate(5);
    builder = new StringBuilder();
    while (reader.read(charBuffer) != -1) {
      Java8Compatibility.flip(charBuffer);
      builder.append(charBuffer);
      Java8Compatibility.clear(charBuffer);
    }
    assertEquals(expected, builder.toString());
    assertFullyRead(reader);

    // Skip all characters
    reader = new CharSequenceReader(charSequence);
    assertEquals(expected.length(), reader.skip(Long.MAX_VALUE));
    assertFullyRead(reader);

    // Skip some characters and read the rest
    if (expected.length() > 5) {
      reader = new CharSequenceReader(charSequence);
      assertEquals(5, reader.skip(5));

      buffer = new char[expected.length() - 5];
      assertEquals(buffer.length, reader.read(buffer, 0, buffer.length));
      assertEquals(expected.substring(5), new String(buffer));
      assertFullyRead(reader);
    }
  }

  /**
   * Asserts that the reader is fully read and no more characters can be read.
   */
  private static void assertFullyRead(CharSequenceReader reader) throws IOException {
    assertEquals(-1, reader.read());
    assertEquals(-1, reader.read(new char[10], 0, 10));
    assertEquals(-1, reader.read(CharBuffer.allocate(10)));
    assertEquals(0, reader.skip(10));
  }

  /**
   * Reads all characters from the reader and returns them as a string.
   */
  private static String readFully(CharSequenceReader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    int read;
    while ((read = reader.read()) != -1) {
      builder.append((char) read);
    }
    return builder.toString();
  }
}