package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * <p>This test class is parameterized to run all test cases against a variety of input strings,
 * ensuring consistent behavior for empty, short, and longer sequences. Each test method focuses on a
 * single aspect of the {@link CharSequenceReader}'s functionality.
 */
@RunWith(Parameterized.class)
public class CharSequenceReaderTest {

  @Parameters(name = "input=\"{0}\"")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {""},
          {"a"},
          {"abc"},
          {"abcdefghijklmnopqrstuvwxyz"},
        });
  }

  private final CharSequence sequence;
  private final String expected;

  public CharSequenceReaderTest(String sequence) {
    this.sequence = sequence;
    this.expected = sequence;
  }

  /** Asserts that the reader has been fully consumed. */
  private void assertFullyRead(CharSequenceReader reader) throws IOException {
    assertEquals(-1, reader.read());
    assertEquals(-1, reader.read(new char[10]));
    assertEquals(-1, reader.read(CharBuffer.allocate(10)));
    assertEquals(0, reader.skip(10));
  }

  @Test
  public void testRead_singleCharByChar() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(sequence);
    for (int i = 0; i < expected.length(); i++) {
      assertEquals("Character at index " + i, expected.charAt(i), reader.read());
    }
    assertFullyRead(reader);
  }

  @Test
  public void testRead_intoArray_allAtOnce() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(sequence);
    char[] buf = new char[expected.length()];

    if (expected.isEmpty()) {
      // Reading from an empty reader into a non-empty buffer should return -1.
      assertEquals(-1, reader.read(new char[1]));
    } else {
      assertEquals(expected.length(), reader.read(buf));
      assertEquals(expected, new String(buf));
    }
    assertFullyRead(reader);
  }

  @Test
  public void testRead_intoArray_inChunks() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(sequence);
    char[] buf = new char[5];
    StringBuilder builder = new StringBuilder();
    int read;
    while ((read = reader.read(buf, 0, buf.length)) != -1) {
      builder.append(buf, 0, read);
    }
    assertEquals(expected, builder.toString());
    assertFullyRead(reader);
  }

  @Test
  public void testRead_intoBuffer_allAtOnce() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(sequence);
    CharBuffer buf = CharBuffer.allocate(expected.length());

    int expectedReadCount = expected.isEmpty() ? -1 : expected.length();
    assertEquals(expectedReadCount, reader.read(buf));

    Java8Compatibility.flip(buf);
    assertEquals(expected, buf.toString());
    assertFullyRead(reader);
  }

  @Test
  public void testRead_intoBuffer_inChunks() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(sequence);
    CharBuffer buf = CharBuffer.allocate(5);
    StringBuilder builder = new StringBuilder();
    while (reader.read(buf) != -1) {
      Java8Compatibility.flip(buf);
      builder.append(buf);
      Java8Compatibility.clear(buf);
    }
    assertEquals(expected, builder.toString());
    assertFullyRead(reader);
  }

  @Test
  public void testSkip_all() throws IOException {
    CharSequenceReader reader = new CharSequenceReader(sequence);
    assertEquals(expected.length(), reader.skip(Long.MAX_VALUE));
    assertFullyRead(reader);
  }

  @Test
  public void testSkip_partialAndReadRemaining() throws IOException {
    final int charsToSkip = 5;
    assumeTrue("Test is not applicable for strings shorter than " + charsToSkip,
        expected.length() > charsToSkip);

    CharSequenceReader reader = new CharSequenceReader(sequence);
    assertEquals(charsToSkip, reader.skip(charsToSkip));

    int remainingLength = expected.length() - charsToSkip;
    char[] buf = new char[remainingLength];
    assertEquals(remainingLength, reader.read(buf, 0, buf.length));
    assertEquals(expected.substring(charsToSkip), new String(buf));
    assertFullyRead(reader);
  }

  @Test
  public void testRead_intoZeroLengthArray_returnsZero() throws IOException {
    // This behavior is specified by java.io.Reader.
    CharSequenceReader reader = new CharSequenceReader(sequence);
    assertEquals(0, reader.read(new char[0]));
    // A zero-length read should not consume any characters.
    if (!expected.isEmpty()) {
      assertEquals(expected.charAt(0), reader.read());
    } else {
      assertEquals(-1, reader.read());
    }
  }
}