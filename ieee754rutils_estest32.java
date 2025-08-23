package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#max(double, double)} returns the input value
     * when both arguments are identical.
     */
    @Test
    public void max_shouldReturnArgument_whenBothDoubleArgumentsAreEqual() {
        // Arrange
        final double value = 0.0;
        final double expected = 0.0;

        // Act
        final double actual = IEEE754rUtils.max(value, value);

        // Assert
        assertEquals(expected, actual, 0.0);
    }
}