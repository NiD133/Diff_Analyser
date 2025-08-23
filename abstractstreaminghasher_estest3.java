package com.google.common.hash;

import com.google.common.hash.Crc32cHashFunction.Crc32cHasher;
import java.nio.BufferOverflowException;
import org.junit.Test;

/**
 * This test class contains tests for AbstractStreamingHasher's state management.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class AbstractStreamingHasher_ESTestTest3 extends AbstractStreamingHasher_ESTest_scaffolding {

    /**
     * Verifies that attempting to add data to a hasher after the hash has been computed
     * results in an exception. Once {@code hash()} is called, the hasher is considered
     * finalized and cannot be updated further.
     */
    @Test(expected = BufferOverflowException.class)
    public void putShort_afterCallingHash_throwsBufferOverflowException() {
        // Arrange: Create a hasher instance. Crc32cHasher is a concrete implementation
        // of the abstract class under test, AbstractStreamingHasher.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();

        // Finalize the hash calculation. This action changes the internal state of the
        // hasher's buffer, preventing further writes.
        hasher.hash();

        // Act & Assert: Attempting to add more data by calling putShort() must now
        // throw an exception. The @Test(expected=...) annotation handles the assertion.
        hasher.putShort((short) 12345);
    }
}