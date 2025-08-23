package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link XXHash32} class.
 *
 * Note: The original test class name 'XXHash32_ESTestTest8' suggests it was
 * auto-generated. A more conventional name like 'XXHash32Test' is used here for clarity.
 */
public class XXHash32Test {

    /**
     * Tests that creating an XXHash32 instance with a specific seed and without
     * updating it with any data produces the correct, pre-calculated initial hash value.
     */
    @Test
    public void shouldReturnInitialHashValueForGivenSeed() {
        // Arrange
        final int seed = 97;
        // This is the known, pre-calculated xxHash32 value for an empty input with seed 97.
        final long expectedHash = 3659767818L;
        final XXHash32 xxHash = new XXHash32(seed);

        // Act
        final long actualHash = xxHash.getValue();

        // Assert
        assertEquals(expectedHash, actualHash);
    }
}