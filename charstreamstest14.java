package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Tests for {@link CharStreams#exhaust(Readable)}. This class focuses on verifying the behavior of
 * exhausting characters from various {@link Readable} sources.
 */
public class CharStreamsExhaustReadableTest extends IoTestCase {

  /**
   * Tests that calling {@code exhaust()} on a non-empty readable consumes all its characters and
   * returns the correct count.
   */
  public void testExhaust_onNonEmptyReadable_readsAllCharacters() throws IOException {
    // Arrange
    CharBuffer readable = CharBuffer.wrap(ASCII);
    int expectedCharCount = ASCII.length();

    // Act
    long consumedCharCount = CharStreams.exhaust(readable);

    // Assert
    assertEquals(
        "exhaust() should return the total number of characters that were read.",
        expectedCharCount,
        consumedCharCount);
    assertEquals(
        "After exhausting, the readable should have no characters remaining.",
        0,
        readable.remaining());
  }

  /**
   * Tests that calling {@code exhaust()} on a readable that has already been fully consumed returns
   * 0.
   */
  public void testExhaust_onAlreadyConsumedReadable_returnsZero() throws IOException {
    // Arrange
    CharBuffer readable = CharBuffer.wrap(ASCII);
    CharStreams.exhaust(readable); // Consume the readable completely before the test.
    assertEquals("Precondition failed: readable should be empty for this test.", 0, readable.remaining());

    // Act
    long consumedCharCount = CharStreams.exhaust(readable);

    // Assert
    assertEquals(
        "exhaust() on an already-consumed readable should return 0.", 0, consumedCharCount);
    assertEquals(
        "An already-consumed readable should still have 0 characters remaining.",
        0,
        readable.remaining());
  }

  /**
   * Tests that calling {@code exhaust()} on a readable that was initially empty returns 0.
   */
  public void testExhaust_onInitiallyEmptyReadable_returnsZero() throws IOException {
    // Arrange
    CharBuffer emptyReadable = CharBuffer.wrap("");

    // Act
    long consumedCharCount = CharStreams.exhaust(emptyReadable);

    // Assert
    assertEquals(
        "exhaust() on an initially empty readable should return 0.", 0, consumedCharCount);
    assertEquals(
        "An empty readable should have no characters remaining after the operation.",
        0,
        emptyReadable.remaining());
  }
}