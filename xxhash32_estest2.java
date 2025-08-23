package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XXHash32 class.
 */
public class XXHash32Test {

    /**
     * Tests that calling the update method with a negative length has no effect on the hash value.
     * The method should effectively be a no-op for invalid (negative) length arguments.
     */
    @Test
    public void updateWithNegativeLengthShouldHaveNoEffect() {
        // Arrange: Create a hasher and record its initial state.
        final XXHash32 xxHash32 = new XXHash32();
        final long initialHash = xxHash32.getValue();
        final byte[] data = new byte[16]; // Dummy data for the method call.

        // Act: Call the update method with a negative length.
        xxHash32.update(data, 0, -10);

        // Assert: The hash value should remain unchanged.
        final long finalHash = xxHash32.getValue();
        assertEquals("The hash value should not change when update is called with a negative length.",
                initialHash, finalHash);
    }
}