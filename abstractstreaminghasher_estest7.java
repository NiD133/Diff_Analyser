package com.google.common.hash;

import java.nio.BufferOverflowException;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 *
 * This test focuses on the behavior of the hasher after its terminal `hash()` method has been called.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that a Hasher cannot be updated after the hash has been computed.
     *
     * <p>The {@code hash()} method is a terminal operation. Once called, the hasher instance
     * should not be used for further input. This test confirms that attempting to add more
     * data via {@code putInt()} throws an exception. For this specific implementation,
     * the internal buffer state leads to a {@link BufferOverflowException}.
     */
    @Test(expected = BufferOverflowException.class)
    public void cannotUpdateHasherAfterFinalization() {
        // Arrange: Create a concrete hasher and finalize it by calling hash().
        // Crc32cHasher is a concrete implementation of AbstractStreamingHasher.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash(); // This is a terminal operation.

        // Act: Attempt to add more data to the finalized hasher.
        // This should fail because the internal buffer is no longer writable.
        hasher.putInt(12345);

        // Assert: The test expects a BufferOverflowException to be thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}