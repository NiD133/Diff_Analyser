package org.apache.commons.lang3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that randomFloat() throws an IllegalArgumentException
     * when the start value is greater than the end value.
     */
    @Test
    public void randomFloatShouldThrowExceptionWhenStartIsGreaterThanEnd() {
        // Arrange
        final RandomUtils randomUtils = RandomUtils.insecure();
        final float startValue = 10.0F;
        final float endValue = 1.0F;
        final String expectedErrorMessage = "Start value must be smaller or equal to end value.";

        // Act & Assert
        // Call the method with an invalid range and verify that the correct exception is thrown.
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> randomUtils.randomFloat(startValue, endValue)
        );

        // Verify that the exception message is as expected.
        assertEquals(expectedErrorMessage, thrown.getMessage());
    }
}