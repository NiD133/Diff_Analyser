package com.google.common.io;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CharSequenceReader}.
 *
 * <p>This suite verifies the correctness of all read and skip operations under various conditions,
 * as well as the behavior of the reader when it is closed.
 */
class CharSequenceReaderTest {

  private static final int CHUNK_SIZE = 5;

  /** Provides various CharSequence instances for parameterized tests. */
  private static Stream<CharSequence> charSequences() {
    return Stream.of(
        "", "short", "a long string that is longer than the chunk size");
  }

  /**
   * Verifies that reading character by character consumes the sequence correctly.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void read_charByChar(CharSequence sequence) throws IOException {
    // Arrange
    String expected = sequence.toString();
    Reader reader = new CharSequenceReader(sequence);
    StringBuilder result = new StringBuilder();

    // Act
    int c;
    while ((c = reader.read()) != -1) {
      result.append((char) c);
    }

    // Assert
    assertEquals(expected, result.toString());
    assertReaderIsExhausted(reader);
  }

  /**
   * Verifies that reading the entire sequence into a perfectly-sized array works correctly.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void read_intoFullArray(CharSequence sequence) throws IOException {
    // Arrange
    String expected = sequence.toString();
    Reader reader = new CharSequenceReader(sequence);
    char[] buffer = new char[expected.length()];

    // Act
    int charsRead = reader.read(buffer);

    // Assert
    if (expected.isEmpty()) {
      assertEquals(-1, charsRead, "Reading from an empty source should return -1");
    } else {
      assertEquals(expected.length(), charsRead, "Should read the entire sequence");
      assertEquals(expected, new String(buffer));
    }
    assertReaderIsExhausted(reader);
  }

  /**
   * Verifies that reading the sequence in fixed-size chunks into an array works correctly.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void read_intoArrayInChunks(CharSequence sequence) throws IOException {
    // Arrange
    String expected = sequence.toString();
    Reader reader = new CharSequenceReader(sequence);
    char[] buffer = new char[CHUNK_SIZE];
    StringBuilder result = new StringBuilder();

    // Act
    int charsRead;
    while ((charsRead = reader.read(buffer, 0, buffer.length)) != -1) {
      result.append(buffer, 0, charsRead);
    }

    // Assert
    assertEquals(expected, result.toString());
    assertReaderIsExhausted(reader);
  }

  /**
   * Verifies that reading the entire sequence into a perfectly-sized CharBuffer works correctly.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void read_intoFullCharBuffer(CharSequence sequence) throws IOException {
    // Arrange
    String expected = sequence.toString();
    Reader reader = new CharSequenceReader(sequence);
    CharBuffer buffer = CharBuffer.allocate(expected.length());

    // Act
    int charsRead = reader.read(buffer);
    buffer.flip();

    // Assert
    if (expected.isEmpty()) {
      assertEquals(-1, charsRead, "Reading from an empty source should return -1");
    } else {
      assertEquals(expected.length(), charsRead, "Should read the entire sequence");
      assertEquals(expected, buffer.toString());
    }
    assertReaderIsExhausted(reader);
  }

  /**
   * Verifies that reading the sequence in fixed-size chunks into a CharBuffer works correctly.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void read_intoCharBufferInChunks(CharSequence sequence) throws IOException {
    // Arrange
    String expected = sequence.toString();
    Reader reader = new CharSequenceReader(sequence);
    CharBuffer buffer = CharBuffer.allocate(CHUNK_SIZE);
    StringBuilder result = new StringBuilder();

    // Act
    while (reader.read(buffer) != -1) {
      buffer.flip();
      result.append(buffer);
      buffer.clear();
    }

    // Assert
    assertEquals(expected, result.toString());
    assertReaderIsExhausted(reader);
  }

  /**
   * Verifies that skipping the entire length of the sequence exhausts the reader.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void skip_full(CharSequence sequence) throws IOException {
    // Arrange
    Reader reader = new CharSequenceReader(sequence);

    // Act
    long skipped = reader.skip(Long.MAX_VALUE);

    // Assert
    assertEquals(sequence.length(), skipped);
    assertReaderIsExhausted(reader);
  }

  /**
   * Verifies that partially skipping and then reading the remainder works correctly.
   */
  @ParameterizedTest
  @MethodSource("charSequences")
  void skip_partialThenReadRemaining(CharSequence sequence) throws IOException {
    // Arrange
    String expected = sequence.toString();
    Assumptions.assumeTrue(expected.length() > CHUNK_SIZE, "Test requires a sequence longer than the chunk size");

    Reader reader = new CharSequenceReader(sequence);
    char[] buffer = new char[expected.length() - CHUNK_SIZE];

    // Act
    long skipped = reader.skip(CHUNK_SIZE);
    int read = reader.read(buffer, 0, buffer.length);

    // Assert
    assertEquals(CHUNK_SIZE, skipped, "Should skip the requested number of characters");
    assertEquals(expected.length() - CHUNK_SIZE, read, "Should read the remaining characters");
    assertEquals(expected.substring(CHUNK_SIZE), new String(buffer));
    assertReaderIsExhausted(reader);
  }

  @Nested
  @DisplayName("When reader is closed")
  class ClosedStateTest {
    /**
     * Verifies that all relevant methods throw an IOException after the reader has been closed.
     */
    @Test
    void allMethodsThrowIOException() throws IOException {
      // Arrange
      Reader reader = new CharSequenceReader("test");
      reader.close();

      // Act & Assert
      assertAll(
          "Methods on a closed reader should throw IOException",
          () -> assertThrows(IOException.class, reader::read, "read()"),
          () -> assertThrows(IOException.class, () -> reader.read(new char[10]), "read(char[])"),
          () -> assertThrows(IOException.class, () -> reader.read(new char[10], 0, 10), "read(char[], off, len)"),
          () -> assertThrows(IOException.class, () -> reader.read(CharBuffer.allocate(10)), "read(CharBuffer)"),
          () -> assertThrows(IOException.class, () -> reader.skip(10), "skip()"),
          () -> assertThrows(IOException.class, reader::ready, "ready()"),
          () -> assertThrows(IOException.class, () -> reader.mark(10), "mark()"),
          () -> assertThrows(IOException.class, reader::reset, "reset()"));
    }
  }

  /**
   * Asserts that the reader is fully consumed and subsequent read/skip operations behave as
   * expected for an exhausted stream.
   */
  private static void assertReaderIsExhausted(Reader reader) throws IOException {
    assertEquals(-1, reader.read(), "read() should return -1 at end of stream");
    assertEquals(-1, reader.read(new char[10], 0, 10), "read(char[], off, len) should return -1 at end of stream");
    assertEquals(-1, reader.read(CharBuffer.allocate(10)), "read(CharBuffer) should return -1 at end of stream");
    assertEquals(0, reader.skip(Long.MAX_VALUE), "skip() should return 0 at end of stream");
  }
}