package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class, focusing on the dividedBy method.
 */
public class YearsTest {

    /**
     * Tests that dividing the maximum possible Years value by a negative number
     * results in the correctly calculated negative value, respecting integer division rules.
     */
    @Test
    public void dividedBy_whenDividingMaxValueByNegativeDivisor_returnsCorrectlyTruncatedResult() {
        // Arrange
        Years maxYears = Years.MAX_VALUE;
        int divisor = -592;
        // The expected result is based on standard integer division.
        // Calculating it here makes the test's logic transparent.
        int expectedYears = Integer.MAX_VALUE / divisor;

        // Act
        Years result = maxYears.dividedBy(divisor);

        // Assert
        assertEquals(expectedYears, result.getYears());
    }
}