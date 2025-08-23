package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that randomDouble() throws an IllegalArgumentException when the start value
     * is greater than the end value, which constitutes an invalid range.
     */
    @Test
    public void randomDoubleShouldThrowIllegalArgumentExceptionWhenStartIsGreaterThanEnd() {
        // Arrange: Set up an invalid range where the start value is greater than the end value.
        final RandomUtils randomUtils = RandomUtils.insecure();
        final double invalidStart = 2316.0;
        final double end = 0.0;

        // Act & Assert: Verify that calling the method with the invalid range throws the correct exception.
        // The assertThrows method concisely captures the expected exception for verification.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> randomUtils.randomDouble(invalidStart, end)
        );

        // Assert: Further verify that the exception message is correct, ensuring the
        // right validation check failed.
        assertEquals("Start value must be smaller or equal to end value.", thrown.getMessage());
    }
}