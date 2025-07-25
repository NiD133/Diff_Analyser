package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * <p>This test suite focuses on verifying the core functionalities of the {@link
 * CharSequenceReader}, including reading from various CharSequences, mark/reset capabilities,
 * handling of invalid arguments, and behavior after closing the reader.
 */
public class CharSequenceReaderTest {

  private CharSequenceReader reader;

  @Before
  public void setup() {
    // Initialize the reader before each test to ensure a clean state.  This avoids state
    // carryover between tests.
    reader = new CharSequenceReader(""); // Initialize with an empty string for flexibility.
  }

  @Test
  public void testReadEmptyString() throws IOException {
    reader = new CharSequenceReader("");
    assertReadsCorrectly("");
  }

  @Test
  public void testReadsStringsCorrectly() throws IOException {
    assertReadsCorrectly("abc");
    assertReadsCorrectly("abcde");
    assertReadsCorrectly("abcdefghijkl");
    assertReadsCorrectly(
        ""
            + "abcdefghijklmnopqrstuvwxyz\n"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ\r"
            + "0123456789\r\n"
            + "!@#$%^&*()-=_+\t[]{};':\",./<>?\\| ");
  }

  @Test
  public void testMarkAndReset() throws IOException {
    String string = "abcdefghijklmnopqrstuvwxyz";
    reader = new CharSequenceReader(string);
    assertTrue(reader.markSupported());

    assertEquals("Initial read should return the entire string", string, readFully(reader));
    assertFullyRead(reader);

    // reset and read again
    reader.reset();
    assertEquals("After reset, reading should return the entire string again", string, readFully(reader));
    assertFullyRead(reader);

    // reset, skip, mark, then read the rest
    reader.reset();
    assertEquals("Skipping 5 characters should return 5", 5, reader.skip(5));
    reader.mark(Integer.MAX_VALUE);
    assertEquals(
        "After skipping and marking, reading should return the substring after the skipped part",
        string.substring(5),
        readFully(reader));
    assertFullyRead(reader);

    // reset to the mark and then read the rest
    reader.reset();
    assertEquals(
        "After resetting to the mark, reading should return the substring after the skipped part again",
        string.substring(5),
        readFully(reader));
    assertFullyRead(reader);
  }

  @Test
  public void testIllegalArguments() throws IOException {
    reader = new CharSequenceReader("12345");

    char[] buf = new char[10];
    assertThrows(
        "Reading with offset+length > buffer size should throw IndexOutOfBoundsException",
        IndexOutOfBoundsException.class,
        () -> reader.read(buf, 0, 11));

    assertThrows(
        "Reading with offset outside buffer bounds should throw IndexOutOfBoundsException",
        IndexOutOfBoundsException.class,
        () -> reader.read(buf, 10, 1));

    assertThrows(
        "Reading with offset outside buffer bounds should throw IndexOutOfBoundsException",
        IndexOutOfBoundsException.class,
        () -> reader.read(buf, 11, 0));

    assertThrows(
        "Reading with negative offset should throw IndexOutOfBoundsException",
        IndexOutOfBoundsException.class,
        () -> reader.read(buf, -1, 5));

    assertThrows(
        "Reading with negative length should throw IndexOutOfBoundsException",
        IndexOutOfBoundsException.class,
        () -> reader.read(buf, 5, -1));

    assertThrows(
        "Reading with offset+length > buffer size should throw IndexOutOfBoundsException",
        IndexOutOfBoundsException.class,
        () -> reader.read(buf, 0, 11));

    assertThrows(
        "Skipping a negative number of characters should throw IllegalArgumentException",
        IllegalArgumentException.class,
        () -> reader.skip(-1));

    assertThrows(
        "Marking with a negative readAheadLimit should throw IllegalArgumentException",
        IllegalArgumentException.class,
        () -> reader.mark(-1));
  }

  @Test
  public void testMethodsThrowWhenClosed() throws IOException {
    reader = new CharSequenceReader("");
    reader.close();

    assertThrows("Reading a single char after close should throw IOException", IOException.class, () -> reader.read());

    assertThrows(
        "Reading to a char array after close should throw IOException",
        IOException.class,
        () -> reader.read(new char[10]));

    assertThrows(
        "Reading to a char array with offset/length after close should throw IOException",
        IOException.class,
        () -> reader.read(new char[10], 0, 10));

    assertThrows(
        "Reading to a CharBuffer after close should throw IOException",
        IOException.class,
        () -> reader.read(CharBuffer.allocate(10)));

    assertThrows(
        "Skipping after close should throw IOException", IOException.class, () -> reader.skip(10));

    assertThrows("Ready after close should throw IOException", IOException.class, () -> reader.ready());

    assertThrows("Mark after close should throw IOException", IOException.class, () -> reader.mark(10));

    assertThrows("Reset after close should throw IOException", IOException.class, () -> reader.reset());
  }

  /**
   * Creates a CharSequenceReader wrapping the given CharSequence and tests that the reader produces
   * the same sequence when read using each type of read method it provides.
   */
  private void assertReadsCorrectly(CharSequence charSequence) throws IOException {
    String expected = charSequence.toString();

    // read char by char
    reader = new CharSequenceReader(charSequence);
    for (int i = 0; i < expected.length(); i++) {
      assertEquals("Reading char by char should return the correct character at each position", expected.charAt(i), reader.read());
    }
    assertFullyRead(reader);

    // read all to one array
    reader = new CharSequenceReader(charSequence);
    char[] buf = new char[expected.length()];
    int expectedLengthResult = expected.length() == 0 ? -1 : expected.length();
    assertEquals("Reading all to one array should return the length of the string or -1 if empty", expectedLengthResult, reader.read(buf));
    assertEquals("The buffer should contain the full string", expected, new String(buf));
    assertFullyRead(reader);

    // read in chunks to fixed array
    reader = new CharSequenceReader(charSequence);
    buf = new char[5];
    StringBuilder builder = new StringBuilder();
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      builder.append(buf, 0, read);
    }
    assertEquals("Reading in chunks to a fixed array should result in the original string", expected, builder.toString());
    assertFullyRead(reader);

    // read all to one CharBuffer
    reader = new CharSequenceReader(charSequence);
    CharBuffer buf2 = CharBuffer.allocate(expected.length());
    assertEquals("Reading all to one CharBuffer should return length or -1 if empty", expectedLengthResult, reader.read(buf2));
    Java8Compatibility.flip(buf2);
    assertEquals("CharBuffer should contain full string", expected, buf2.toString());
    assertFullyRead(reader);

    // read in chunks to fixed CharBuffer
    reader = new CharSequenceReader(charSequence);
    buf2 = CharBuffer.allocate(5);
    builder = new StringBuilder();
    while (reader.read(buf2) != -1) {
      Java8Compatibility.flip(buf2);
      builder.append(buf2);
      Java8Compatibility.clear(buf2);
    }
    assertEquals("Reading in chunks to a fixed CharBuffer should result in the original string", expected, builder.toString());
    assertFullyRead(reader);

    // skip fully
    reader = new CharSequenceReader(charSequence);
    assertEquals("Skipping the entire sequence should return the length of the sequence", expected.length(), reader.skip(Long.MAX_VALUE));
    assertFullyRead(reader);

    // skip 5 and read the rest
    if (expected.length() > 5) {
      reader = new CharSequenceReader(charSequence);
      assertEquals("Skipping 5 characters should return 5", 5, reader.skip(5));

      buf = new char[expected.length() - 5];
      assertEquals(
          "Reading the remaining part after skipping should return the length of the remaining part",
          buf.length,
          reader.read(buf, 0, buf.length));
      assertEquals("The buffer should contain the remaining part of the string", expected.substring(5), new String(buf));
      assertFullyRead(reader);
    }
  }

  private void assertFullyRead(CharSequenceReader reader) throws IOException {
    assertEquals("Reading after the end of the sequence should return -1", -1, reader.read());
    assertEquals(
        "Reading into a buffer after the end of the sequence should return -1",
        -1,
        reader.read(new char[10], 0, 10));
    assertEquals(
        "Reading into a CharBuffer after the end of the sequence should return -1",
        -1,
        reader.read(CharBuffer.allocate(10)));
    assertEquals(
        "Skipping after the end of the sequence should return 0", 0, reader.skip(10));
  }

  private String readFully(CharSequenceReader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    int read;
    while ((read = reader.read()) != -1) {
      builder.append((char) read);
    }
    return builder.toString();
  }
}