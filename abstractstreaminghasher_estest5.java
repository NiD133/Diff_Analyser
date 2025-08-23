package com.google.common.hash;

import java.nio.BufferOverflowException;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 *
 * <p>This test focuses on the state management of the hasher, particularly its behavior after the
 * hash has been computed.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to add more data to a hasher after {@code hash()} has been called
     * results in an exception. Once {@code hash()} is invoked, the hasher is considered finalized
     * and should not accept further input.
     */
    @Test(expected = BufferOverflowException.class)
    public void putLong_afterHashIsCalculated_throwsBufferOverflowException() {
        // Arrange: Create a hasher and compute the hash to finalize its state.
        // We use Crc32cHasher as a concrete implementation of AbstractStreamingHasher.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash();

        // Act: Attempt to add more data to the finalized hasher.
        // This should throw an exception because the internal buffer is no longer writable.
        hasher.putLong(12345L);
    }
}