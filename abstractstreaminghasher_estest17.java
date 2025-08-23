package com.google.common.hash;

import java.nio.BufferOverflowException;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to add data to a hasher after its {@code hash()} method has been
     * called results in an exception. Once the hash is computed, the hasher instance is considered
     * finalized and should not accept further input.
     */
    @Test(expected = BufferOverflowException.class)
    public void putByte_afterHashIsCalculated_throwsException() {
        // Arrange: Create a concrete hasher and finalize it by calling hash().
        // We use Crc32cHashFunction as a concrete implementation of a streaming hash.
        Hasher hasher = new Crc32cHashFunction().newHasher();
        hasher.hash(); // This call finalizes the hasher.

        // Act & Assert: Attempting to add more data should throw an exception.
        // In this implementation, a BufferOverflowException is thrown because the
        // internal buffer is finalized. A different design might throw IllegalStateException.
        hasher.putByte((byte) 1);
    }
}