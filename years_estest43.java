package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the Years class.
 */
public class YearsTest {

    /**
     * Tests that the factory method `years(int)` correctly creates an instance
     * when given the maximum possible integer value.
     */
    @Test
    public void yearsFactory_withMaxValue_createsYearsWithCorrectValue() {
        // Arrange
        int maxIntValue = Integer.MAX_VALUE;

        // Act
        Years maxYears = Years.years(maxIntValue);

        // Assert
        assertEquals("The number of years should match the maximum integer value",
                     maxIntValue, maxYears.getYears());
    }
}