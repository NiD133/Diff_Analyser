package com.google.common.hash;

import static org.junit.Assert.assertThrows;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Tests for the argument validation in {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * A minimal, no-op implementation of {@link AbstractStreamingHasher} used to test the
     * functionality of the abstract class itself, such as input validation.
     */
    private static class NoOpStreamingHasher extends AbstractStreamingHasher {
        NoOpStreamingHasher() {
            // The chunk size value does not matter for the tests in this class,
            // as the exceptions are thrown before any processing logic is invoked.
            super(4);
        }

        @Override
        protected void process(ByteBuffer bb) {
            // This method is not called by the tests here.
        }

        @Override
        protected void processRemaining(ByteBuffer bb) {
            // This method is not called by the tests here.
        }

        @Override
        protected HashCode makeHash() {
            // This method is not called by the tests here.
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void putBytes_withInvalidArguments_throwsIndexOutOfBoundsException() {
        Hasher hasher = new NoOpStreamingHasher();
        byte[] bytes = new byte[8];

        // Case 1: Negative offset
        assertThrows(
                "Should throw for a negative offset",
                IndexOutOfBoundsException.class,
                () -> hasher.putBytes(bytes, -1, 4));

        // Case 2: Length extends beyond the array's bounds
        assertThrows(
                "Should throw when offset + length > array length",
                IndexOutOfBoundsException.class,
                () -> hasher.putBytes(bytes, 0, 9));

        // Case 3: Negative length
        assertThrows(
                "Should throw for a negative length",
                IndexOutOfBoundsException.class,
                () -> hasher.putBytes(bytes, 0, -1));
    }
}