package com.google.common.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link AbstractStreamingHasher}, focusing on the {@code putFloat} method and its
 * interaction with the chunking mechanism.
 */
@NullUnmarked
public class AbstractStreamingHasherPutFloatTest extends TestCase {

  /**
   * A test spy for {@link AbstractStreamingHasher} that captures the processed bytes and records
   * invocations of the chunk-processing methods. This allows us to verify the streaming behavior of
   * the hasher.
   */
  private static class StreamingHasherSpy extends AbstractStreamingHasher {

    private final int chunkSize;
    private final int bufferSize;
    private final ByteArrayOutputStream processedBytes = new ByteArrayOutputStream();
    private int processInvocations = 0;
    private boolean processRemainingInvoked = false;

    StreamingHasherSpy(int chunkSize) {
      super(chunkSize);
      this.chunkSize = chunkSize;
      this.bufferSize = chunkSize;
    }

    /** Returns the concatenated bytes from all processed chunks. */
    byte[] getProcessedBytes() {
      return processedBytes.toByteArray();
    }

    /** Returns the number of times process() was called for full chunks from the input. */
    int getProcessInvocations() {
      return processInvocations;
    }

    /** Returns true if processRemaining() was called for the final partial chunk. */
    boolean wasProcessRemainingInvoked() {
      return processRemainingInvoked;
    }

    @Override
    protected HashCode makeHash() {
      // The actual hash value is not important for these tests, only the bytes processed.
      return HashCode.fromBytes(processedBytes.toByteArray());
    }

    @Override
    protected void process(ByteBuffer bb) {
      processInvocations++;
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Buffer should have at least one full chunk", bb.remaining() >= chunkSize);
      for (int i = 0; i < chunkSize; i++) {
        processedBytes.write(bb.get());
      }
    }

    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse("processRemaining should only be called once", processRemainingInvoked);
      processRemainingInvoked = true;
      assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("Remaining buffer must not be empty", bb.remaining() > 0);
      assertTrue("Remaining buffer must be smaller than bufferSize", bb.remaining() < bufferSize);

      // The default implementation of processRemaining pads the buffer with zeros to a full
      // chunk and then calls process(). We call it to ensure that behavior is preserved.
      int invocationsBefore = processInvocations;
      super.processRemaining(bb);
      int invocationsAfter = processInvocations;

      // We verify the superclass behavior here: it must call process() exactly once.
      assertEquals(
          "super.processRemaining() should delegate to process() exactly once",
          invocationsBefore + 1,
          invocationsAfter);

      // For our test assertions, we are interested in the number of *full* chunks processed
      // from the original input, not the final padded one. We decrement the counter to make
      // assertions in the tests more straightforward.
      processInvocations--;
    }
  }

  public void testPutFloat_writesBytesInLittleEndianOrder() {
    // Arrange
    // The AbstractStreamingHasher contract specifies that primitives are processed in little-endian
    // order. We test this by creating a float from an integer with a known byte pattern.
    // The integer 0x04030201, when written in little-endian byte order, becomes the sequence
    // {0x01, 0x02, 0x03, 0x04}.
    int intRepresentation = 0x04030201;
    float valueToHash = Float.intBitsToFloat(intRepresentation);
    byte[] expectedBytes = new byte[] {1, 2, 3, 4};

    // We use a chunk size of 4, which is the size of a float.
    // This means putting one float should exactly fill one chunk.
    StreamingHasherSpy hasherSpy = new StreamingHasherSpy(4);

    // Act
    hasherSpy.putFloat(valueToHash);
    hasherSpy.hash();

    // Assert
    // 1. Verify the chunk processing logic.
    assertEquals(
        "A single float should be processed as exactly one full chunk.",
        1,
        hasherSpy.getProcessInvocations());
    assertFalse(
        "processRemaining() should not be called when the input is a multiple of the chunk size.",
        hasherSpy.wasProcessRemainingInvoked());

    // 2. Verify the processed bytes.
    // The bytes captured by the spy should match the expected little-endian representation.
    // Since we wrote exactly one chunk's worth of data, there should be no padding.
    byte[] actualBytes = hasherSpy.getProcessedBytes();
    assertArrayEquals(
        "Processed bytes should be the little-endian representation of the float.",
        expectedBytes,
        actualBytes);
  }
}