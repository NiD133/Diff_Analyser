package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link XXHash32} class.
 */
public class XXHash32Test {

    /**
     * The expected hash value for an empty input using the default seed (0).
     * This is a known-answer value for verifying the algorithm's initial state.
     */
    private static final long HASH_OF_EMPTY_INPUT = 46947589L;

    /**
     * Verifies that the reset() method correctly reverts the hasher's internal state
     * to its original condition after it has been updated with data.
     */
    @Test
    public void resetShouldRevertToInitialStateAfterUpdate() {
        // Arrange: Create a hasher and update it with some data to alter its state.
        final XXHash32 hasher = new XXHash32();
        hasher.update(new byte[]{'a', 'b', 'c', 'd'}, 0, 4);

        // Act: Reset the hasher.
        hasher.reset();

        // Assert: The hash value should now be identical to the hash of an empty input,
        // confirming the state has been successfully reset.
        final long hashAfterReset = hasher.getValue();
        assertEquals("After reset, the hash value should match the initial state for an empty input.",
                HASH_OF_EMPTY_INPUT, hashAfterReset);
    }
}