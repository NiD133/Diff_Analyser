package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Verifies that adding any positive value to Years.MAX_VALUE throws an
     * ArithmeticException, as the operation results in an integer overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void plus_onMaxValue_shouldThrowArithmeticException() {
        // Arrange: Start with the maximum possible Years value.
        Years maxYears = Years.MAX_VALUE;

        // Act: Attempt to add a positive number of years, which should cause an overflow.
        // The @Test(expected) annotation handles the assertion that an exception is thrown.
        maxYears.plus(1);
    }
}