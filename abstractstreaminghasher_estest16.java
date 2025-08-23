package com.google.common.hash;

import org.junit.Test;
import java.nio.ByteBuffer;

/**
 * Tests for {@link AbstractStreamingHasher}.
 * This class contains the refactored test case.
 */
public class AbstractStreamingHasher_ESTestTest16 {

    /**
     * Verifies that attempting to add bytes to a hasher after its hash has been
     * computed results in an IllegalStateException. This enforces the contract that
     * a Hasher is a single-use object.
     */
    @Test(expected = IllegalStateException.class)
    public void putBytes_afterHashIsCalculated_throwsIllegalStateException() {
        // Arrange: Create a concrete hasher instance and some data.
        // We use Crc32cHasher as a representative implementation of AbstractStreamingHasher.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        ByteBuffer data = ByteBuffer.allocate(16);

        // Finalize the hash calculation, which puts the hasher in a "used" state.
        hasher.hash();

        // Act & Assert: Attempting to add more data should throw the expected exception.
        // The @Test(expected) annotation handles the assertion.
        hasher.putBytes(data);
    }
}