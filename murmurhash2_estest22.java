package org.apache.commons.codec.digest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link MurmurHash2} class.
 */
class MurmurHash2Test {

    /**
     * Tests that hash64() throws an ArrayIndexOutOfBoundsException when the provided
     * length is negative, as this would lead to an invalid memory access attempt.
     */
    @Test
    void hash64ShouldThrowExceptionForNegativeLength() {
        // Arrange: Set up the input data and invalid parameters.
        byte[] data = new byte[1];
        int negativeLength = -26;
        int anySeed = 42; // The seed value is not relevant for this exception.

        // Act & Assert: Verify that the expected exception is thrown.
        // The method should fail because a negative length is an invalid argument
        // that causes an out-of-bounds access.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            MurmurHash2.hash64(data, negativeLength, anySeed);
        });
    }
}