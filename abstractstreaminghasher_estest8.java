package com.google.common.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Unit tests for {@link AbstractStreamingHasher}, focusing on its lifecycle and state management.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to update the hasher after the hash has been computed
     * throws an {@link IllegalStateException}. Once {@code hash()} is called, the hasher
     * instance is considered "consumed" and cannot be modified further.
     */
    @Test
    public void putIntAfterHashThrowsIllegalStateException() {
        // Arrange: Create a concrete hasher, add data, and consume it by calling hash().
        // We use Crc32cHasher as a concrete implementation of the abstract class under test.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.putBytes(ByteBuffer.allocate(16)); // Add some arbitrary data.
        hasher.hash(); // This is a terminal operation that consumes the hasher.

        // Act & Assert: Verify that any subsequent 'put' operation fails.
        try {
            hasher.putInt(42); // Attempt to reuse the consumed hasher.
            fail("Expected an IllegalStateException because the hasher cannot be used after hash() is called.");
        } catch (IllegalStateException expected) {
            // This is the correct behavior, confirming the hasher is a one-time-use object.
            assertEquals(
                "The behavior of calling any method after calling hash() is undefined.",
                expected.getMessage());
        }
    }
}