package org.joda.time;

import org.junit.Test;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that adding the maximum possible Years value to itself results in an
     * ArithmeticException due to integer overflow.
     */
    @Test(expected = ArithmeticException.class)
    public void plus_whenResultExceedsMaxValue_throwsArithmeticException() {
        // Arrange
        Years maxYears = Years.MAX_VALUE;

        // Act
        // This operation should overflow since Integer.MAX_VALUE + Integer.MAX_VALUE
        // is greater than Integer.MAX_VALUE.
        maxYears.plus(maxYears);

        // Assert
        // The test is expected to throw an ArithmeticException, which is
        // declared in the @Test annotation.
    }
}