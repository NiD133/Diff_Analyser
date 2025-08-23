package com.google.common.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * A test-only implementation of {@link AbstractStreamingHasher} that records the bytes it
     * processes and counts the invocations of its processing methods. This allows tests to verify the
     * behavior of the abstract base class.
     */
    private static class RecordingStreamingHasher extends AbstractStreamingHasher {

        private final ByteArrayOutputStream processedBytes = new ByteArrayOutputStream();
        private int processInvocations = 0;
        private boolean processRemainingInvoked = false;

        RecordingStreamingHasher(int chunkSize) {
            super(chunkSize);
        }

        @Override
        protected void process(ByteBuffer bb) {
            processInvocations++;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            // The buffer should contain at least a full chunk.
            assertTrue(bb.remaining() >= super.chunkSize);
            while (bb.hasRemaining()) {
                processedBytes.write(bb.get());
            }
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            assertFalse("processRemaining should only be invoked once.", processRemainingInvoked);
            processRemainingInvoked = true;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("The remaining buffer should not be empty.", bb.remaining() > 0);

            // The default implementation of processRemaining pads the buffer and calls process().
            // We decrement the counter here to isolate the count of process() calls on *full* chunks
            // from the final call on a *padded, partial* chunk. This simplifies test assertions.
            int before = processInvocations;
            super.processRemaining(bb);
            int after = processInvocations;
            assertEquals("super.processRemaining() should delegate to process() once.", before + 1, after);
            processInvocations--;
        }

        @Override
        protected HashCode makeHash() {
            return HashCode.fromBytes(processedBytes.toByteArray());
        }

        // Getter methods for test verification
        int getProcessInvocations() {
            return processInvocations;
        }

        boolean wasProcessRemainingInvoked() {
            return processRemainingInvoked;
        }
    }

    @Test
    public void putBytes_withInputSizeMultipleOfChunkSize_processesInCompleteChunks() {
        // Arrange
        final int chunkSize = 4;
        RecordingStreamingHasher hasher = new RecordingStreamingHasher(chunkSize);
        byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};
        int expectedProcessCalls = 2; // 8 bytes / 4 bytes-per-chunk

        // Act: Add 8 bytes through various put methods. This total is a multiple of the chunk size.
        hasher.putByte((byte) 1);
        hasher.putBytes(new byte[] {2, 3, 4, 5, 6});
        hasher.putByte((byte) 7);
        hasher.putBytes(new byte[] {}); // Putting empty bytes should be a no-op.
        hasher.putBytes(new byte[] {8});
        HashCode result = hasher.hash();

        // Assert
        // The total input (8 bytes) is a multiple of the chunk size (4),
        // so all data should be processed in full chunks.
        assertEquals(
                "Number of times process() should be called for full chunks.",
                expectedProcessCalls,
                hasher.getProcessInvocations());
        assertFalse(
                "processRemaining() should not be called when input size is a multiple of chunk size.",
                hasher.wasProcessRemainingInvoked());

        // The resulting hash should contain exactly the input bytes, with no padding.
        assertArrayEquals(expectedBytes, result.asBytes());
    }
}