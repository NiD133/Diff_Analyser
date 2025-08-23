package com.google.common.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

/**
 * Exhaustive tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherExhaustiveTest extends TestCase {

    /**
     * A test spy implementation of {@link AbstractStreamingHasher}. It doesn't compute a real hash;
     * instead, it records all bytes passed to its {@code process} methods and counts the number of
     * invocations. This allows us to verify the chunking and buffering behavior of the base class.
     */
    private static class StreamingHasherSpy extends AbstractStreamingHasher {

        final int chunkSize;
        final int bufferSize;
        final ByteArrayOutputStream processedBytes = new ByteArrayOutputStream();

        int processCallCount = 0;
        boolean processRemainingCalled = false;

        StreamingHasherSpy(int chunkSize, int bufferSize) {
            super(chunkSize, bufferSize);
            this.chunkSize = chunkSize;
            this.bufferSize = bufferSize;
        }

        StreamingHasherSpy(int chunkSize) {
            super(chunkSize);
            this.chunkSize = chunkSize;
            this.bufferSize = chunkSize;
        }

        @Override
        protected HashCode makeHash() {
            return HashCode.fromBytes(processedBytes.toByteArray());
        }

        @Override
        protected void process(ByteBuffer bb) {
            processCallCount++;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("Buffer remaining bytes must be at least a full chunk", bb.remaining() >= chunkSize);
            for (int i = 0; i < chunkSize; i++) {
                processedBytes.write(bb.get());
            }
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            assertFalse("processRemaining should only be called once", processRemainingCalled);
            processRemainingCalled = true;
            assertEquals(ByteOrder.LITTLE_ENDIAN, bb.order());
            assertTrue("Remaining buffer must not be empty", bb.remaining() > 0);
            assertTrue("Remaining buffer must be smaller than the buffer size", bb.remaining() < bufferSize);

            int before = processCallCount;
            super.processRemaining(bb);
            int after = processCallCount;

            // The default implementation of processRemaining pads the buffer and calls process().
            // We decrement the counter here to isolate the testing of process() to only full chunks,
            // which simplifies the assertion logic in assertInvariants().
            assertEquals("super.processRemaining should have called process() once", before + 1, after);
            processCallCount--;
        }

        /**
         * Asserts that the internal state (call counts, total bytes processed) is consistent with the
         * total number of bytes that were expected to be hashed.
         */
        void assertInvariants(int expectedByteCount) {
            // The total bytes processed should be padded to the next multiple of chunkSize.
            assertEquals(
                "Total bytes in output stream should be padded to a multiple of chunk size",
                ceilToMultiple(expectedByteCount, chunkSize),
                processedBytes.toByteArray().length);

            // process() should have been called once for each full chunk of input.
            assertEquals(
                "process() call count should match the number of full chunks",
                expectedByteCount / chunkSize,
                processCallCount);

            // processRemaining() should be called if and only if the input was not a multiple of chunkSize.
            assertEquals(
                "processRemaining() should be called iff there are leftover bytes",
                expectedByteCount % chunkSize != 0,
                processRemainingCalled);
        }

        /**
         * Asserts that the spy processed the exact same sequence of bytes as the expected sequence.
         */
        void assertOutputMatches(byte[] expected) {
            byte[] actualPadded = processedBytes.toByteArray();
            // The spy pads the output to a multiple of chunkSize, so we only compare the prefix.
            byte[] actual = Arrays.copyOf(actualPadded, expected.length);
            assertArrayEquals(expected, actual);
        }

        // Returns the smallest integer x such that x >= a && (x % b) == 0.
        private static int ceilToMultiple(int a, int b) {
            int remainder = a % b;
            return remainder == 0 ? a : a + b - remainder;
        }
    }

    /**
     * A simple, non-streaming hash function used as a "golden reference" to determine the
     * correct final output. It simply concatenates all input bytes.
     */
    private static class ReferenceNonStreamingHasher extends AbstractNonStreamingHashFunction {
        @Override
        public HashCode hashBytes(byte[] input, int off, int len) {
            return HashCode.fromBytes(Arrays.copyOfRange(input, off, off + len));
        }

        @Override
        public int bits() {
            throw new UnsupportedOperationException();
        }
    }

    private static final int MAX_ACTIONS_PER_RUN = 200;
    private static final int MIN_CHUNK_SIZE = 4;
    private static final int MAX_CHUNK_SIZE = 32;
    private static final int MAX_BUFFER_MULTIPLIER = 4;

    /**
     * This test verifies that {@link AbstractStreamingHasher} produces a consistent byte stream
     * regardless of its internal chunk and buffer size configurations. It does this by feeding a
     * random sequence of inputs to many differently configured hashers and asserting that they all
     * produce the same final byte sequence as a simple, non-streaming reference implementation.
     */
    @AndroidIncompatible // This test is slow and not suitable for all environments.
    public void testStreamingConsistency_withVariousChunkAndBufferSizes() {
        Random random = new Random(0); // Use a fixed seed for reproducible tests.

        // Test with a varying number of random 'put' operations to cover a wide range of states.
        for (int numberOfActions = 0; numberOfActions < MAX_ACTIONS_PER_RUN; numberOfActions++) {
            // --- Arrange ---

            // 1. Create a list of streaming hasher spies with various chunk and buffer sizes.
            List<StreamingHasherSpy> streamingHasherSpies = new ArrayList<>();
            for (int chunkSize = MIN_CHUNK_SIZE; chunkSize <= MAX_CHUNK_SIZE; chunkSize++) {
                for (int bufferSize = chunkSize;
                    bufferSize <= chunkSize * MAX_BUFFER_MULTIPLIER;
                    bufferSize += chunkSize) {
                    streamingHasherSpies.add(new StreamingHasherSpy(chunkSize, bufferSize));
                }
            }

            // 2. Create a non-streaming reference hasher to establish the correct output.
            ReferenceNonStreamingHasher referenceHashFunction = new ReferenceNonStreamingHasher();
            Hasher referenceHasher = referenceHashFunction.newHasher();

            // 3. Group all hashers to apply the same operations to them.
            Iterable<Hasher> allHashers =
                Iterables.concat(streamingHasherSpies, Collections.singleton(referenceHasher));

            // --- Act ---

            // 1. Apply a random sequence of 'put' operations to all hashers.
            for (int i = 0; i < numberOfActions; i++) {
                RandomHasherAction.pickAtRandom(random).performAction(random, allHashers);
            }

            // 2. Add a final int. This ensures the hasher has at least 4 bytes, which is required by
            // some implementations before calling hash(), and tests handling of a final small input.
            int finalInt = random.nextInt();
            for (Hasher hasher : allHashers) {
                hasher.putInt(finalInt);
            }

            // --- Assert ---

            // 1. Get the expected result from the reference hasher. This consumes the hasher.
            byte[] expectedBytes = referenceHasher.hash().asBytes();

            // 2. For each streaming hasher configuration, verify its output and internal behavior.
            for (StreamingHasherSpy spy : streamingHasherSpies) {
                // Finalize the streaming hasher, which triggers processRemaining and makeHash.
                HashCode unused = spy.hash();

                // Assert that the internal processing (chunking, padding) was correct.
                spy.assertInvariants(expectedBytes.length);

                // Assert that the final byte output matches the reference hasher.
                spy.assertOutputMatches(expectedBytes);
            }
        }
    }
}