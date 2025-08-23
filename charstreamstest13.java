package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link CharStreams#exhaust(Readable)}.
 */
@NullUnmarked
public class CharStreamsTestTest13 extends IoTestCase {

  /**
   * Tests that exhaust() on a non-empty reader consumes all its characters, returns the correct
   * count, and leaves the reader at its end.
   */
  public void testExhaust_onNonEmptyReader_returnsCharacterCountAndEmptiesReader()
      throws IOException {
    // Arrange
    Reader reader = new StringReader(ASCII);
    long expectedLength = ASCII.length();

    // Act
    long exhaustedCount = CharStreams.exhaust(reader);

    // Assert
    assertEquals("Should return the total number of characters read", expectedLength, exhaustedCount);
    assertEquals("Reader should be at the end of the stream after being exhausted", -1, reader.read());
  }

  /**
   * Tests that calling exhaust() on a reader that has already been fully read (exhausted)
   * correctly returns 0.
   */
  public void testExhaust_onAlreadyExhaustedReader_returnsZero() throws IOException {
    // Arrange
    Reader reader = new StringReader(ASCII);
    CharStreams.exhaust(reader); // Exhaust the reader completely

    // Act
    long exhaustedCount = CharStreams.exhaust(reader); // Attempt to exhaust it again

    // Assert
    assertEquals("Exhausting an already-empty reader should return 0", 0, exhaustedCount);
  }

  /**
   * Tests that calling exhaust() on a reader that was empty to begin with correctly returns 0.
   */
  public void testExhaust_onEmptyReader_returnsZero() throws IOException {
    // Arrange
    Reader emptyReader = new StringReader("");

    // Act
    long exhaustedCount = CharStreams.exhaust(emptyReader);

    // Assert
    assertEquals("Exhausting an empty reader should return 0", 0, exhaustedCount);
    assertEquals("Empty reader should immediately return end-of-stream", -1, emptyReader.read());
  }
}