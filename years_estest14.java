package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that subtracting the same number of years from a Years object
     * results in Years.ZERO.
     */
    @Test
    public void minus_whenSubtractingTheSameNumberOfYears_shouldReturnZero() {
        // Arrange
        final int amount = 6488;
        Years initialYears = Years.years(amount);

        // Act
        Years result = initialYears.minus(amount);

        // Assert
        assertEquals(Years.ZERO, result);
    }
}