package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void isLessThan_shouldReturnFalse_whenComparingZeroYearsToNull() {
        // The Javadoc for isLessThan() states that a null comparison value is treated as zero.
        // This test verifies that comparing zero years to null returns false, as 0 is not less than 0.

        // Arrange
        Years zeroYears = Years.years(0);

        // Act
        boolean result = zeroYears.isLessThan(null);

        // Assert
        assertFalse("Zero years should not be considered less than null (treated as zero)", result);
    }
}