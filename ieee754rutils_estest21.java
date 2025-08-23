package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#max(double, double, double)} correctly returns the largest value
     * when the inputs include a negative number and zero.
     */
    @Test
    public void testMaxWithThreeDoublesShouldReturnLargestValue() {
        // Arrange
        final double value1 = 0.0;
        final double value2 = -1416.7961547236;
        final double value3 = 0.0;
        final double expectedMaximum = 0.0;

        // Act
        final double actualMaximum = IEEE754rUtils.max(value1, value2, value3);

        // Assert
        final String message = "The maximum of 0.0, a negative number, and 0.0 should be 0.0";
        assertEquals(message, expectedMaximum, actualMaximum, 0.0);
    }
}