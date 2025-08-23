package com.google.common.hash;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import junit.framework.TestCase;

/**
 * Tests for the base functionality of {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest extends TestCase {

  /**
   * A test-specific implementation of {@link AbstractStreamingHasher} that acts as a spy,
   * recording the bytes processed and the invocations of its abstract methods.
   */
  private static class VerifyingSink extends AbstractStreamingHasher {

    private final int chunkSize;
    private final int bufferSize;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    int processInvocations = 0;
    boolean processRemainingInvoked = false;

    VerifyingSink(int chunkSize, int bufferSize) {
      super(chunkSize, bufferSize);
      this.chunkSize = chunkSize;
      this.bufferSize = bufferSize;
    }

    VerifyingSink(int chunkSize) {
      super(chunkSize);
      this.chunkSize = chunkSize;
      this.bufferSize = chunkSize;
    }

    @Override
    protected HashCode makeHash() {
      return HashCode.fromBytes(out.toByteArray());
    }

    @Override
    protected void process(ByteBuffer bb) {
      processInvocations++;
      assertEquals("ByteBuffer must be in LITTLE_ENDIAN order", ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue(
          "process() should be called with a full chunk", bb.remaining() >= chunkSize);
      for (int i = 0; i < chunkSize; i++) {
        out.write(bb.get());
      }
    }



    @Override
    protected void processRemaining(ByteBuffer bb) {
      assertFalse("processRemaining should only be called once", processRemainingInvoked);
      processRemainingInvoked = true;
      assertEquals("ByteBuffer must be in LITTLE_ENDIAN order", ByteOrder.LITTLE_ENDIAN, bb.order());
      assertTrue("processRemaining buffer should not be empty", bb.remaining() > 0);
      assertTrue(
          "processRemaining buffer should be smaller than the buffer size",
          bb.remaining() < bufferSize);

      int before = processInvocations;
      super.processRemaining(bb); // The default implementation pads the buffer and calls process().
      int after = processInvocations;

      // The default implementation of processRemaining() calls process() one last time with a
      // padded chunk. We decrement the counter here to hide this implementation detail from the
      // test's assertions, allowing them to focus only on the number of full chunks processed
      // from the original input data.
      assertEquals("super.processRemaining() should have called process()", before + 1, after);
      processInvocations--;
    }

    /**
     * Verifies that the hasher processed the expected number of chunks and handled any remaining
     * bytes correctly.
     */
    void verifyInvocations(int expectedInputBytes) {
      // The total bytes written should be padded up to the next multiple of the chunk size.
      assertEquals(
          "Total bytes written to the output stream",
          ceilToMultiple(expectedInputBytes, chunkSize),
          out.toByteArray().length);

      // Asserts the number of times process() was called on full chunks of original data.
      assertEquals(
          "Number of full chunks processed",
          expectedInputBytes / chunkSize,
          processInvocations);

      // Asserts whether the final, non-full chunk was processed.
      assertEquals(
          "Invocation of processRemaining",
          expectedInputBytes % chunkSize != 0,
          processRemainingInvoked);
    }

    /** Verifies that the bytes written to the hasher match the expected byte array. */
    void verifyWrittenBytes(byte[] expected) {
      byte[] actual = out.toByteArray();
      // Use Arrays.equals for a concise comparison, but a loop provides better failure messages.
      for (int i = 0; i < expected.length; i++) {
        assertEquals("Byte at index " + i + " does not match", expected[i], actual[i]);
      }
    }

    // Returns the smallest integer x such that x >= a and (x % b) == 0.
    private static int ceilToMultiple(int a, int b) {
      int remainder = a % b;
      return remainder == 0 ? a : a + b - remainder;
    }
  }

  public void testPutLong_whenInputSizeMatchesChunkSize_processesExactlyOneChunk() {
    // Arrange
    final int chunkSize = 8; // A long is 8 bytes
    VerifyingSink sink = new VerifyingSink(chunkSize);

    // This specific long value is chosen because its little-endian byte representation
    // is [1, 2, 3, 4, 5, 6, 7, 8], making verification straightforward.
    long testValue = 0x0807060504030201L;
    byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};

    // Act
    sink.putLong(testValue);
    sink.hash(); // Finalize the hashing process

    // Assert
    sink.verifyInvocations(Long.BYTES);
    sink.verifyWrittenBytes(expectedBytes);
  }
}