package com.google.common.io;

import java.io.IOException;
import java.util.Collections;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link MultiInputStream}, focusing on edge cases like handling a large number of
 * underlying streams.
 */
@NullUnmarked
public class MultiInputStreamTest extends IoTestCase {

  private static final int MANY_EMPTY_SOURCES = 10_000_000;

  /**
   * Tests that reading from a MultiInputStream composed of a huge number of empty sources does not
   * cause a StackOverflowError. This can happen with a naive recursive implementation of advancing
   * to the next stream.
   *
   * @see <a href="https://github.com/google/guava/issues/2996">Guava issue #2996</a>
   */
  public void testRead_withManyEmptySources_doesNotCauseStackOverflow() throws IOException {
    // This test doesn't read any data. Its purpose is to ensure that the constructor
    // and the first read() call can process a very large number of empty streams
    // without throwing a StackOverflowError.

    // Arrange
    MultiInputStream streamWithManyEmptySources =
        new MultiInputStream(
            Collections.nCopies(MANY_EMPTY_SOURCES, ByteSource.empty()).iterator());

    // Act
    int result = streamWithManyEmptySources.read();

    // Assert
    // The stream should correctly advance through all empty sources and return -1 for EOF.
    assertEquals("Expected end of stream", -1, result);
  }
}