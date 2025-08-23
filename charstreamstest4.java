package com.google.common.io;

import static org.junit.Assert.assertThrows;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link CharStreams#skipFully(Reader, long)}.
 *
 * <p>Note: This test class retains its original base class, {@code IoTestCase}, to preserve the
 * project's testing infrastructure.
 */
public class CharStreamsTest extends IoTestCase {

  /**
   * Tests that {@link CharStreams#skipFully} throws an {@link EOFException} when trying to skip
   * more characters than are available in the reader.
   */
  public void testSkipFully_whenAttemptingToSkipPastEnd_throwsEofException() throws IOException {
    // Arrange: Create a reader with a known amount of content.
    String content = "abcde"; // 5 characters
    Reader reader = new StringReader(content);
    long charsToSkip = content.length() + 1; // Attempt to skip 6 characters, which is past the end.

    // Act & Assert: Verify that skipping past the end of the stream throws an EOFException.
    assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, charsToSkip));
  }
}