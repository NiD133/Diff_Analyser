package com.google.common.io;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.common.base.Strings;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for a specific bug in {@link CharStreams#copy(Readable, Appendable)} related to buffer
 * handling.
 */
@NullUnmarked
public class CharStreamsTestTest12 extends IoTestCase {

  /**
   * Creates a reader that intentionally performs partial reads, never filling the buffer it's given.
   * This is used to simulate I/O conditions that could trigger buffer-handling bugs.
   *
   * @param reader the underlying reader to wrap
   */
  private static Reader createReaderWithPartialReads(Reader reader) {
    return new FilterReader(reader) {
      @Override
      public int read(char[] cbuf, int off, int len) throws IOException {
        if (len <= 0) {
          // The bug this test targets would eventually cause `len` to become 0, leading to an
          // infinite loop. This check fails the test explicitly if that state is reached.
          fail("read() was called with a non-positive length: " + len);
        }
        // Simulate a partial read by reading at most `len - 1` characters.
        // This ensures the calling buffer is never completely filled.
        return in.read(cbuf, off, Math.max(1, len - 1));
      }
    };
  }

  /**
   * Test for Guava issue 1061: https://github.com/google/guava/issues/1061
   *
   * <p>This test verifies that {@code CharStreams.copy} correctly handles readers that do not
   * completely fill the provided buffer on each read. The original bug caused the internal buffer's
   * available space to shrink with each partial read, which could lead to an infinite loop.
   */
  public void testCopy_whenReaderPerformsPartialReads_copiesAllContent() throws IOException {
    // Arrange
    // A long string is needed to force many read cycles, exposing the bug where the
    // buffer's capacity would otherwise diminish to zero.
    String inputString = Strings.repeat("0123456789", 500);
    Reader partialReader = createReaderWithPartialReads(new StringReader(inputString));
    StringBuilder output = new StringBuilder();

    // Act
    // The key behavior under test is that this `copy` operation completes successfully
    // without entering an infinite loop.
    long charsCopied = CharStreams.copy(partialReader, output);

    // Assert
    assertThat(output.toString()).isEqualTo(inputString);
    assertThat(charsCopied).isEqualTo(inputString.length());
  }
}