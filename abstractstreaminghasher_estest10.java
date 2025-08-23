package com.google.common.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the state management of {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that once the hash() method is called on a hasher,
     * it can no longer be updated with new data.
     */
    @Test
    public void putData_afterHashIsCalculated_throwsIllegalStateException() {
        // Arrange: Create a hasher and add some data.
        // We use Crc32cHasher as a concrete implementation of the abstract class under test.
        HashFunction crc32c = new Crc32cHashFunction();
        Hasher hasher = crc32c.newHasher();
        hasher.putInt(42); // Add some initial data.

        // Act: Finalize the hash calculation. This should put the hasher in a "used" state.
        hasher.hash();

        // Assert: Any subsequent attempt to add data should fail.
        try {
            hasher.putChar('M');
            fail("Expected an IllegalStateException because the hasher has already been used.");
        } catch (IllegalStateException e) {
            // This is the expected behavior. We can optionally check the message for more specificity.
            assertEquals("Cannot use Hasher after calling #hash() on it", e.getMessage());
        }
    }
}