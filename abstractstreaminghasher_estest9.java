package com.google.common.hash;

import java.nio.BufferOverflowException;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}, focusing on its behavior after finalization.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to add data to a hasher after the hash has been computed
     * (by calling {@code hash()}) results in an exception. Once {@code hash()} is called,
     * the hasher instance is considered finalized and cannot be reused.
     *
     * The expected exception is {@code BufferOverflowException} because the internal buffer
     * has been flipped for final processing and has no remaining capacity for new data.
     */
    @Test(expected = BufferOverflowException.class)
    public void putChar_afterCallingHash_throwsException() {
        // Arrange: Create a hasher, using a concrete implementation of AbstractStreamingHasher.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();

        // Finalize the hash calculation. After this point, the hasher should not accept more input.
        hasher.hash();

        // Act: Attempt to add more data to the finalized hasher.
        // This action is expected to throw the BufferOverflowException declared in the @Test annotation.
        hasher.putChar('7');
    }
}