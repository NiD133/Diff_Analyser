package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Years} class.
 */
public class YearsTest {

    @Test
    public void isGreaterThan_shouldReturnFalse_whenThisIsLessThanOther() {
        // Arrange
        Years smallerYears = Years.years(-690);
        Years largerYears = Years.MAX_VALUE;

        // Act
        boolean result = smallerYears.isGreaterThan(largerYears);

        // Assert
        assertFalse("A smaller Years value should not be greater than a larger one.", result);
    }
}