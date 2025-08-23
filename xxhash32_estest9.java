package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Unit tests for the XXHash32 class, focusing on invalid arguments.
 */
public class XXHash32Test {

    /**
     * Verifies that calling the update method with a null buffer
     * correctly throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void updateWithNullBufferShouldThrowNullPointerException() {
        // Arrange: Create an instance of the XXHash32 checksum.
        XXHash32 xxHash32 = new XXHash32();

        // Act: Call the update method with a null buffer.
        // The offset and length values are arbitrary, as the null check should occur first.
        xxHash32.update(null, 0, 0);

        // Assert: The test succeeds if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}