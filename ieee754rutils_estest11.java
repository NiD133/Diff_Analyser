package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.min() returns the input value when both double arguments are identical.
     */
    @Test
    public void minWithEqualDoublesShouldReturnTheValue() {
        // Arrange
        final double value = 2855.35762973;
        final double expected = 2855.35762973;

        // Act
        final double actual = IEEE754rUtils.min(value, value);

        // Assert
        assertEquals("The minimum of two equal doubles should be the value itself.", expected, actual, 0.0);
    }
}