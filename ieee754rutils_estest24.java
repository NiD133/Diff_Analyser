package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that max(double, double) returns the arithmetically greater of two negative values.
     */
    @Test
    public void testMaxWithTwoNegativeDoubles() {
        // Arrange
        final double smallerNegative = -3055.5365;
        final double largerNegative = -1.0; // Closer to zero, hence arithmetically greater
        final double expectedMax = -1.0;

        // Act
        final double actualMax = IEEE754rUtils.max(smallerNegative, largerNegative);

        // Assert
        assertEquals(expectedMax, actualMax, 0.0);
    }
}