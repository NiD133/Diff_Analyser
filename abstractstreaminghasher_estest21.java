package com.google.common.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the AbstractStreamingHasher class, focusing on its state management.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to update a hasher after the final hash has been computed
     * results in an IllegalStateException. Once hash() is called, the hasher instance
     * is considered consumed and cannot be reused.
     */
    @Test
    public void hasherIsUnusableAfterHashIsCalculated() {
        // Arrange: Create a hasher instance. We use Crc32cHasher as a concrete
        // implementation of the abstract class under test.
        Hasher hasher = new Crc32cHashFunction().newHasher();
        hasher.putInt(12345); // Add some data, which is a typical use case.

        // Act (Part 1): Compute the hash. This should finalize the hasher's state.
        hasher.hash();

        // Act (Part 2) & Assert: Any subsequent attempt to add more data should fail.
        try {
            hasher.putByte((byte) 0x2A); // Attempt to use the hasher again.
            fail("Expected an IllegalStateException because the hasher has already been used.");
        } catch (IllegalStateException expected) {
            // Assert that the correct exception was thrown with the expected message.
            assertEquals(
                "The behavior of calling any method after calling hash() is undefined.",
                expected.getMessage());
        }
    }
}