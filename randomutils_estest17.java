package org.apache.commons.lang3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Verifies that randomInt() throws an IllegalArgumentException when the
     * range boundaries are negative, as the contract requires non-negative values.
     */
    @Test
    void randomIntShouldThrowIllegalArgumentExceptionForNegativeRange() {
        // Arrange: Create a RandomUtils instance.
        final RandomUtils randomUtils = RandomUtils.secureStrong();
        final int negativeBound = -1830;

        // Act & Assert: Call the method with invalid arguments and verify the exception.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> randomUtils.randomInt(negativeBound, negativeBound),
            "Expected randomInt() to throw for negative range values."
        );

        // Assert: Verify the exception message is correct.
        assertEquals("Both range values must be non-negative.", thrown.getMessage());
    }
}