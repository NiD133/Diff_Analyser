package com.google.common.hash;

import static org.junit.Assert.assertThrows;

import com.google.common.collect.Iterables;
import com.google.common.hash.HashTestUtils.RandomHasherAction;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

public class AbstractStreamingHasherTestTest5 extends TestCase {

    /**
     * A test-specific implementation of AbstractStreamingHasher that captures the processed bytes and
     * records method invocations.
     */
    private static class Sink extends AbstractStreamingHasher {

        final int chunkSize;
        final int bufferSize;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int processCalled = 0;
        boolean remainingCalled = false;

        Sink(int chunkSize, int bufferSize) {
            super(chunkSize, bufferSize);
            this.chunkSize = chunkSize;
            this.bufferSize = bufferSize;
        }

        Sink(int chunkSize) {
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
            processCalled++;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("Buffer remaining bytes should be at least a full chunk", bb.remaining() >= chunkSize);
            for (int i = 0; i < chunkSize; i++) {
                out.write(bb.get());
            }
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            assertFalse("processRemaining should only be called once", remainingCalled);
            remainingCalled = true;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("processRemaining should be called with non-empty buffer", bb.remaining() > 0);
            assertTrue("processRemaining buffer should be smaller than bufferSize", bb.remaining() < bufferSize);
            int before = processCalled;
            super.processRemaining(bb); // default implementation pads and calls process()
            int after = processCalled;
            assertEquals("super.processRemaining should invoke process() once", before + 1, after);
            // Decrement to not count the final padded chunk as a "full" chunk,
            // which simplifies invariant assertions.
            processCalled--;
        }

        /**
         * Asserts that the internal state is consistent with the total number of bytes hashed.
         */
        void assertInvariants(int expectedBytes) {
            // The total output bytes should be padded to the next multiple of the chunk size.
            assertEquals(
                "Total processed bytes should be padded to chunk size",
                ceilToMultiple(expectedBytes, chunkSize),
                out.toByteArray().length);
            // Asserts the number of times process() was called for full chunks.
            assertEquals(
                "Number of full chunks processed", expectedBytes / chunkSize, processCalled);
            // Asserts whether the final partial chunk was processed.
            assertEquals(
                "processRemaining should be called if there is a partial chunk",
                expectedBytes % chunkSize != 0,
                remainingCalled);
        }

        // returns the minimum x such as x >= a && (x % b) == 0
        private static int ceilToMultiple(int a, int b) {
            int remainder = a % b;
            return remainder == 0 ? a : a + b - remainder;
        }

        /**
         * Asserts that the captured bytes match the expected byte array.
         */
        void assertBytes(byte[] expected) {
            byte[] actual = out.toByteArray();
            // Using Arrays.toString provides a much more readable failure message
            // than comparing elements in a loop.
            assertEquals(Arrays.toString(expected), Arrays.toString(actual));
        }
    }

    // Assumes that AbstractNonStreamingHashFunction works properly (must be tested elsewhere!)
    private static class Control extends AbstractNonStreamingHashFunction {
        // ... (Content unchanged as it's not used in the test case)
        @Override
        public HashCode hashBytes(byte[] input, int off, int len) {
            return HashCode.fromBytes(Arrays.copyOfRange(input, off, off + len));
        }

        @Override
        public int bits() {
            throw new UnsupportedOperationException();
        }
    }

    public void testPutChar_whenDataIsSmallerThanChunkSize_isPaddedWithZeros() {
        // Arrange
        final int chunkSize = 4;
        // A char is 2 bytes. The test character 0x0201 is {0x01, 0x02} in little-endian byte order.
        final char testChar = 0x0201;
        final int inputByteCount = Character.BYTES; // 2 bytes
        // The expected output is the char's bytes, padded with zeros to match the chunk size.
        final byte[] expectedPaddedBytes = {0x01, 0x02, 0x00, 0x00};

        Sink sink = new Sink(chunkSize);

        // Act
        sink.putChar(testChar);
        sink.hash(); // Finalizes hashing, which triggers padding and processing of the last chunk.

        // Assert
        // 1. Verify the internal state of the hasher.
        //    - 0 full chunks were processed.
        //    - The "process remaining" logic was called for the partial data.
        //    - The total output bytes match the padded chunk size.
        sink.assertInvariants(inputByteCount);

        // 2. Verify the actual bytes processed.
        //    They should match the little-endian representation of the char, plus padding.
        sink.assertBytes(expectedPaddedBytes);
    }
}