package com.google.common.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * A test spy for AbstractStreamingHasher that records interactions and verifies the
     * contract of the methods it overrides.
     */
    private static class StreamingHasherSpy extends AbstractStreamingHasher {
        private final ByteArrayOutputStream processedBytes = new ByteArrayOutputStream();
        private int processCallCount = 0;
        private boolean processRemainingCalled = false;
        private final int chunkSize;

        StreamingHasherSpy(int chunkSize) {
            super(chunkSize);
            this.chunkSize = chunkSize;
        }

        @Override
        protected void process(ByteBuffer bb) {
            processCallCount++;
            assertEquals("Buffer should be in LITTLE_ENDIAN order", ByteOrder.LITTLE_ENDIAN, bb.order());
            // The final padded chunk from processRemaining() will be exactly chunkSize.
            assertTrue(
                "Buffer remaining bytes should be at least the chunk size",
                bb.remaining() >= chunkSize);

            while (bb.hasRemaining()) {
                processedBytes.write(bb.get());
            }
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            assertFalse("processRemaining should only be called once", processRemainingCalled);
            processRemainingCalled = true;

            assertEquals("Buffer should be in LITTLE_ENDIAN order", ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("Buffer for remaining bytes should not be empty", bb.remaining() > 0);
            assertTrue(
                "Buffer for remaining bytes should be smaller than a full chunk",
                bb.remaining() < chunkSize);

            // Delegate to the superclass to test its default padding behavior.
            super.processRemaining(bb);
        }

        @Override
        protected HashCode makeHash() {
            // The actual hash value is not important for this test,
            // only the bytes processed.
            return HashCode.fromBytes(processedBytes.toByteArray());
        }

        // Getter methods for test assertions
        byte[] getProcessedBytes() {
            return processedBytes.toByteArray();
        }

        int getProcessCallCount() {
            return processCallCount;
        }

        boolean wasProcessRemainingCalled() {
            return processRemainingCalled;
        }
    }

    @Test
    public void putShort_withDataSmallerThanChunkSize_isPaddedAndProcessed() {
        // Arrange
        final int chunkSize = 4;
        StreamingHasherSpy hasher = new StreamingHasherSpy(chunkSize);
        short input = 0x0201; // Represents {0x01, 0x02} in little-endian byte order

        // The input is 2 bytes, less than the 4-byte chunk size. We expect the
        // hasher to pad the remaining 2 bytes with zeros before processing.
        byte[] expectedProcessedBytes = {1, 2, 0, 0};

        // Act
        hasher.putShort(input);
        hasher.hash(); // Triggers processRemaining() and the final process() call

        // Assert
        // 1. Verify that processRemaining() was invoked for the leftover bytes.
        assertTrue(
            "processRemaining() should be called for inputs smaller than the chunk size",
            hasher.wasProcessRemainingCalled());

        // 2. The default implementation of processRemaining() pads the buffer and calls process().
        //    Therefore, process() should be called exactly once.
        assertEquals(
            "process() should be called once for the single padded chunk",
            1,
            hasher.getProcessCallCount());

        // 3. Verify that the bytes sent to process() were correctly padded.
        assertArrayEquals(
            "Bytes should be padded to the full chunk size",
            expectedProcessedBytes,
            hasher.getProcessedBytes());
    }
}