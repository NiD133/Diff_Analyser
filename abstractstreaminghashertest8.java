package com.google.common.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import junit.framework.TestCase;

/**
 * Tests for {@link AbstractStreamingHasher}, focusing on the {@code putDouble} method.
 */
public class AbstractStreamingHasherPutDoubleTest extends TestCase {

    /**
     * A test-only implementation of {@link AbstractStreamingHasher} that spies on the abstract
     * methods to verify interactions. It accumulates all processed bytes into a
     * {@link ByteArrayOutputStream} for inspection.
     */
    private static class Sink extends AbstractStreamingHasher {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int processCalled = 0;
        boolean remainingCalled = false;

        Sink(int chunkSize) {
            super(chunkSize);
        }

        @Override
        protected HashCode makeHash() {
            // The actual hash value is not important for these tests, but we must return something.
            return HashCode.fromBytes(out.toByteArray());
        }

        @Override
        protected void process(ByteBuffer bb) {
            processCalled++;
            assertEquals("Buffer should be in LITTLE_ENDIAN order", ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue(
                    "Buffer remaining bytes should be >= chunk size", bb.remaining() >= getChunkSize());
            // Write the bytes of a single chunk to the output stream.
            for (int i = 0; i < getChunkSize(); i++) {
                out.write(bb.get());
            }
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            assertFalse("processRemaining should only be called once", remainingCalled);
            remainingCalled = true;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("Remaining buffer must not be empty", bb.remaining() > 0);
            assertTrue(
                    "Remaining buffer must be smaller than bufferSize", bb.remaining() < getBufferSize());

            // The default implementation of processRemaining pads the buffer and calls process().
            // We want to track 'user-data' chunks vs the final padded chunk separately.
            int processCallsBefore = processCalled;
            super.processRemaining(bb);
            int processCallsAfter = processCalled;

            // Verify that the default implementation did indeed call process() once.
            assertEquals(
                    "super.processRemaining() should call process() once",
                    processCallsBefore + 1,
                    processCallsAfter);

            // Decrement the counter so our tests only count calls to process() for full chunks
            // of original data, making test assertions simpler.
            processCalled--;
        }

        /**
         * Asserts that the prefix of the bytes processed by the hasher matches the expected bytes.
         *
         * @param expectedPrefix the expected byte prefix
         */
        void assertBytesPrefix(byte[] expectedPrefix) {
            byte[] got = out.toByteArray();
            assertTrue(
                    "Actual byte array is shorter than expected prefix. Got: "
                            + got.length
                            + ", expected: "
                            + expectedPrefix.length,
                    got.length >= expectedPrefix.length);
            for (int i = 0; i < expectedPrefix.length; i++) {
                assertEquals("Byte mismatch at index " + i, expectedPrefix[i], got[i]);
            }
        }

        private int getChunkSize() {
            // Expose protected field for test assertions.
            try {
                java.lang.reflect.Field field = AbstractStreamingHasher.class.getDeclaredField("chunkSize");
                field.setAccessible(true);
                return (int) field.get(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private int getBufferSize() {
            // Expose protected field for test assertions.
            try {
                java.lang.reflect.Field field = AbstractStreamingHasher.class.getDeclaredField("bufferSize");
                field.setAccessible(true);
                return (int) field.get(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void testPutDouble_withFullChunk_processesBytesInLittleEndianOrder() {
        // Arrange
        // The Sink is a test-specific hasher that uses an 8-byte chunk size, the size of a double.
        Sink sink = new Sink(8);

        // A test value with distinct, sequential bytes, making it easy to verify byte ordering.
        // The long 0x0807060504030201L represents the byte sequence [8, 7, 6, 5, 4, 3, 2, 1].
        long testValueAsLong = 0x0807060504030201L;
        double testValue = Double.longBitsToDouble(testValueAsLong);

        // Act
        sink.putDouble(testValue);
        sink.hash(); // Finalize the hash to trigger processing of any buffered data.

        // Assert
        // AbstractStreamingHasher is little-endian, so the bytes of the long should be reversed.
        byte[] expectedBytes = {1, 2, 3, 4, 5, 6, 7, 8};
        sink.assertBytesPrefix(expectedBytes);
        assertEquals(
                "Total processed bytes should match expected",
                expectedBytes.length,
                sink.out.toByteArray().length);

        // Verify the interaction with the streaming mechanism.
        // Since we provided exactly 8 bytes (a full chunk), process() should be called once.
        assertEquals("process() should be called once for a full chunk", 1, sink.processCalled);
        // And since there are no leftover bytes, processRemaining() should not be called.
        assertFalse("processRemaining() should not be called for a full chunk", sink.remainingCalled);
    }
}