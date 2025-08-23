package com.google.common.io;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

  /**
   * Tests that attempting to copy a CharBuffer to itself results in a BufferOverflowException.
   *
   * <p>The {@link CharStreams#copy(Readable, Appendable)} operation first reads from the source
   * {@code Readable} into an internal buffer. When the source and destination are the same {@code
   * CharBuffer}, this read operation advances the buffer's position to the end. The subsequent
   * write to the destination {@code Appendable} then fails because there is no remaining space in
   * the buffer.
   */
  @Test(expected = BufferOverflowException.class)
  public void copy_fromAndToSameBuffer_throwsBufferOverflowException() throws IOException {
    // Arrange: A CharBuffer is both Readable and Appendable. A newly created buffer
    // is ready to be read from (position=0, limit=capacity) but has no space to
    // be written to after its contents are read.
    CharBuffer buffer = CharStreams.createBuffer();

    // Act & Assert: Attempting to copy the buffer to itself. The @Test annotation
    // asserts that a BufferOverflowException is thrown.
    CharStreams.copy(buffer, buffer);
  }
}