package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    /**
     * Tests that isGreaterThan() correctly handles a null argument.
     * The method's contract specifies that a null 'other' period is treated as zero.
     * Therefore, comparing zero years to null is equivalent to comparing zero to zero,
     * which should return false (as they are equal, not greater).
     */
    @Test
    public void isGreaterThan_shouldReturnFalse_whenComparingZeroToNull() {
        // Arrange
        Years zeroYears = Years.ZERO;

        // Act
        // The null argument should be treated as Years.ZERO according to the method's contract.
        boolean result = zeroYears.isGreaterThan(null);

        // Assert
        assertFalse("Years(0) should not be greater than null (which is treated as 0)", result);
    }
}